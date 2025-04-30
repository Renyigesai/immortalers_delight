package com.renyigesai.immortalers_delight.block;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Tilt;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

// 大型垂滴叶方块类，继承自HorizontalDirectionalBlock，实现了BonemealableBlock和SimpleWaterloggedBlock接口
public class SpikeTrapBlock extends HorizontalDirectionalBlock implements BonemealableBlock, SimpleWaterloggedBlock {
    // 定义一个布尔属性，表示方块是否被水logged（即是否被水填充）
    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    // 定义一个枚举属性，表示方块的倾斜状态
    private static final EnumProperty<Tilt> TILT = BlockStateProperties.TILT;
    // 定义一个常量，表示不进行方块刻（tick）的标识
    private static final int NO_TICK = -1;
    // 定义一个映射，存储不同倾斜状态到下一个倾斜状态的延迟时间（以方块刻为单位）
    private static final Object2IntMap<Tilt> DELAY_UNTIL_NEXT_TILT_STATE = Util.make(new Object2IntArrayMap<>(), (p_152305_) -> {
        p_152305_.defaultReturnValue(-1);
        p_152305_.put(Tilt.UNSTABLE, 10);
        p_152305_.put(Tilt.PARTIAL, 10);
        p_152305_.put(Tilt.FULL, 100);
    });
    // 定义最大生成高度的常量
    private static final int MAX_GEN_HEIGHT = 5;
    // 定义茎的宽度常量
    private static final int STEM_WIDTH = 6;
    // 定义实体伤害值
    private float entity_damage = 1.0F;

    // 定义是否为长钉陷阱
    private boolean isLongType = false;
    // 定义最低的叶子顶部Y坐标常量
    private static final int LOWEST_LEAF_TOP = 13;
    protected static final VoxelShape NORTH_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);
    protected static final VoxelShape SOUTH_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);
    protected static final VoxelShape EAST_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);
    protected static final VoxelShape WEST_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);
    // 定义不同倾斜状态对应的叶子形状的映射
    private static final Map<Tilt, VoxelShape> LEAF_SHAPES = ImmutableMap.of(
        Tilt.NONE, Block.box(0.0D, 11.0D, 0.0D, 16.0D, 15.0D, 16.0D),
        Tilt.UNSTABLE, Block.box(0.0D, 11.0D, 0.0D, 16.0D, 15.0D, 16.0D), 
        Tilt.PARTIAL, Block.box(0.0D, 11.0D, 0.0D, 16.0D, 13.0D, 16.0D), 
        Tilt.FULL, Block.box(0.0D, 5.0D, 0.0D, 16.0D, 9.0D, 16.0D));
    // 定义茎的切片形状
    private static final VoxelShape STEM_SLICER = Block.box(0.0D, 13.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    // 定义不同方向对应的茎的形状的映射
    private static final Map<Direction, VoxelShape> STEM_SHAPES = ImmutableMap.of(
            Direction.NORTH, Shapes.joinUnoptimized(SpikeTrapBlock.NORTH_SHAPE, STEM_SLICER, BooleanOp.ONLY_FIRST),
            Direction.SOUTH, Shapes.joinUnoptimized(SpikeTrapBlock.SOUTH_SHAPE, STEM_SLICER, BooleanOp.ONLY_FIRST),
            Direction.EAST, Shapes.joinUnoptimized(SpikeTrapBlock.EAST_SHAPE, STEM_SLICER, BooleanOp.ONLY_FIRST),
            Direction.WEST, Shapes.joinUnoptimized(SpikeTrapBlock.WEST_SHAPE, STEM_SLICER, BooleanOp.ONLY_FIRST)
    );
    // 定义一个缓存，存储每个方块状态对应的形状
    private final Map<BlockState, VoxelShape> shapesCache;

    // 构造函数，初始化方块的属性和默认状态，并缓存形状
    public SpikeTrapBlock(Properties pProperties) {
        super(pProperties);
        // 注册默认状态，设置初始的水logged为false，朝向为北方，倾斜状态为NONE
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, Boolean.valueOf(false)).setValue(FACING, Direction.NORTH).setValue(TILT, Tilt.NONE));
        // 计算并缓存每个状态的形状
        this.shapesCache = this.getShapeForEachState(SpikeTrapBlock::calculateShape);
    }
    public SpikeTrapBlock(Float damage,Properties pProperties) {
        super(pProperties);
        // 注册默认状态，设置初始的水logged为false，朝向为北方，倾斜状态为NONE
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, Boolean.valueOf(false)).setValue(FACING, Direction.NORTH).setValue(TILT, Tilt.NONE));
        // 计算并缓存每个状态的形状
        this.shapesCache = this.getShapeForEachState(SpikeTrapBlock::calculateShape);
        this.entity_damage = damage;
    }


    public SpikeTrapBlock(boolean isLong, Float damage, Properties pProperties) {
        super(pProperties);
        // 注册默认状态，设置初始的水logged为false，朝向为北方，倾斜状态为NONE
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, Boolean.valueOf(false)).setValue(FACING, Direction.NORTH).setValue(TILT, Tilt.NONE));
        // 计算并缓存每个状态的形状
        this.shapesCache = this.getShapeForEachState(SpikeTrapBlock::calculateShape);
        this.entity_damage = damage;
        this.isLongType = isLong;
    }

    // 计算方块形状的静态方法，根据倾斜状态和朝向返回对应的形状
    private static VoxelShape calculateShape(BlockState p_152318_) {
        return Shapes.or(LEAF_SHAPES.get(p_152318_.getValue(TILT)), STEM_SHAPES.get(p_152318_.getValue(FACING)));
    }

    // 以随机高度放置大型垂滴叶的静态方法
    public static void placeWithRandomHeight(LevelAccessor pLevel, RandomSource pRandom, BlockPos pPos, Direction pDirection) {
        // 生成一个在2到5之间的随机高度
        int i = Mth.nextInt(pRandom, 2, 5);
        // 创建一个可变的方块位置对象
        BlockPos.MutableBlockPos blockpos$mutableblockpos = pPos.mutable();
        int j = 0;

        // 检查并找到可以放置的位置，直到达到最大高度或者不能放置为止
        while(j < i && canPlaceAt(pLevel, blockpos$mutableblockpos, pLevel.getBlockState(blockpos$mutableblockpos))) {
            ++j;
            blockpos$mutableblockpos.move(Direction.UP);
        }

        int k = pPos.getY() + j - 1;
        blockpos$mutableblockpos.setY(pPos.getY());

        // 放置茎方块
        while(blockpos$mutableblockpos.getY() < k) {
            SpikeTrapBlock.place(pLevel, blockpos$mutableblockpos, pLevel.getFluidState(blockpos$mutableblockpos), pDirection);
            blockpos$mutableblockpos.move(Direction.UP);
        }

        // 放置叶子方块
        place(pLevel, blockpos$mutableblockpos, pLevel.getFluidState(blockpos$mutableblockpos), pDirection);
    }

    // 判断方块是否可以被替换的静态方法
    private static boolean canReplace(BlockState pState) {
        return pState.isAir() || pState.is(Blocks.WATER) || pState.is(Blocks.SMALL_DRIPLEAF);
    }

    // 判断是否可以在指定位置放置方块的静态方法
    protected static boolean canPlaceAt(LevelHeightAccessor pLevel, BlockPos pPos, BlockState pState) {
        return !pLevel.isOutsideBuildHeight(pPos) && canReplace(pState);
    }

    // 在指定位置放置方块的静态方法
    protected static boolean place(LevelAccessor pLevel, BlockPos pPos, FluidState pFluidState, Direction pDirection) {
        // 创建方块状态，设置水logged和朝向
        BlockState blockstate = Blocks.BIG_DRIPLEAF.defaultBlockState().setValue(WATERLOGGED, Boolean.valueOf(pFluidState.isSourceOfType(Fluids.WATER))).setValue(FACING, pDirection);
        // 设置方块状态到指定位置
        return pLevel.setBlock(pPos, blockstate, 3);
    }

    // 当有投射物击中时的处理方法
    public void onProjectileHit(Level pLevel, BlockState pState, BlockHitResult pHit, Projectile pProjectile) {
        // 设置倾斜状态为FULL，并安排下一次方块刻和播放声音
        this.setTiltAndScheduleTick(pState, pLevel, pHit.getBlockPos(), Tilt.FULL, SoundEvents.BIG_DRIPLEAF_TILT_DOWN);
    }

    // 获取方块的流体状态的方法
    public FluidState getFluidState(BlockState pState) {
        // 如果方块是水logged的，返回水的源流体状态，否则返回父类的流体状态
        return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
    }

    // 判断方块是否可以在指定位置生存的方法
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        // 获取下方方块的位置和状态
        BlockPos blockpos = pPos.below();
        BlockState blockstate = pLevel.getBlockState(blockpos);
        // 判断下方方块是否是大型垂滴叶、大型垂滴叶茎或者可放置大型垂滴叶的方块
        //return blockstate.is(this) || blockstate.is(Blocks.BIG_DRIPLEAF_STEM) || blockstate.is(BlockTags.BIG_DRIPLEAF_PLACEABLE);
        return true;
    }

    // 更新方块状态的方法，根据邻居方块的状态和方向返回新的状态
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pPos, BlockPos pNeighborPos) {
        // 如果下方方块不能支撑当前方块，返回空气方块状态
        if (pDirection == Direction.DOWN && !pState.canSurvive(pLevel, pPos)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            // 如果方块是水logged的，安排水的方块刻
            if (pState.getValue(WATERLOGGED)) {
                pLevel.scheduleTick(pPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
            }

            // 如果上方方块是大型垂滴叶，返回大型垂滴叶茎的状态
            //return pDirection == Direction.UP && pNeighborState.is(this) ? Blocks.BIG_DRIPLEAF_STEM.withPropertiesOf(pState) : super.updateShape(pState, pDirection, pNeighborState, pLevel, pPos, pNeighborPos);
            //如果上方不是空气，返回激活状态
            boolean isGround = pDirection == Direction.UP && isFaceFull(pNeighborState.getCollisionShape(pLevel, pPos.above()), Direction.DOWN);
            //if (isGround) this.setTiltAndScheduleTick(pState, (Level) pLevel, pPos, Tilt.FULL, SoundEvents.BIG_DRIPLEAF_TILT_DOWN);
            return isGround ? this.withPropertiesOf(pState).setValue(TILT, Tilt.FULL) : super.updateShape(pState, pDirection, pNeighborState, pLevel, pPos, pNeighborPos);
        }
    }

    // 判断是否可以对当前方块使用骨粉的方法
    public boolean isValidBonemealTarget(LevelReader pLevel, BlockPos pPos, BlockState pState, boolean pIsClient) {
        // 获取上方方块的状态
        BlockState blockstate = pLevel.getBlockState(pPos.above());
        // 判断上方方块是否可以被替换
        //return canReplace(blockstate);
        return  false;
    }

    // 判断使用骨粉是否成功的方法，这里总是返回true
    public boolean isBonemealSuccess(Level pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
        //return true;
        return false;
    }

    // 使用骨粉后的处理方法，在上方放置茎和叶子方块
    public void performBonemeal(ServerLevel pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
//        // 获取上方方块的位置和状态
//        BlockPos blockpos = pPos.above();
//        BlockState blockstate = pLevel.getBlockState(blockpos);
//        // 如果上方可以放置方块
//        if (canPlaceAt(pLevel, blockpos, blockstate)) {
//            // 获取当前方块的朝向
//            Direction direction = pState.getValue(FACING);
//            // 放置茎方块
//            SpikeTrapBlock.place(pLevel, pPos, pState.getFluidState(), direction);
//            // 放置叶子方块
//            place(pLevel, blockpos, blockstate.getFluidState(), direction);
//        }

    }

    // 当有实体进入方块时的处理方法
    public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        // 仅在服务端处理
        if (!pLevel.isClientSide) {
            // 如果当前倾斜状态为NONE，且实体可以使方块倾斜，并且没有邻居信号
            if (pState.getValue(TILT) == Tilt.NONE && canEntityTilt(pPos, pEntity) && !pLevel.hasNeighborSignal(pPos)) {
                // 设置倾斜状态为UNSTABLE，并安排下一次方块刻，不播放声音
                this.setTiltAndScheduleTick(pState, pLevel, pPos, Tilt.UNSTABLE, (SoundEvent)null);
            }
            if (pEntity instanceof LivingEntity livingentity) {
                if (pState.getValue(TILT) == Tilt.FULL) {
                    //造成伤害并添加buff，对非玩家生物造成的伤害不会致死
                    if (livingentity instanceof Player || livingentity.getHealth() > entity_damage) {
                        livingentity.hurt(pLevel.damageSources().cactus(), entity_damage);
                    }
                }
            }
        }
    }

    // 方块刻的处理方法
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        // 如果有邻居信号，重置倾斜状态
        if (pLevel.hasNeighborSignal(pPos)) {
            resetTilt(pState, pLevel, pPos);
        } else {
            // 获取当前倾斜状态
            Tilt tilt = pState.getValue(TILT);
            // 根据不同的倾斜状态进行处理
            if (tilt == Tilt.UNSTABLE) {
                this.setTiltAndScheduleTick(pState, pLevel, pPos, Tilt.PARTIAL, SoundEvents.BIG_DRIPLEAF_TILT_DOWN);
            } else if (tilt == Tilt.PARTIAL) {
                //在弹出时会造成范围伤害
                List<LivingEntity> list = pLevel.getEntitiesOfClass(LivingEntity.class, new AABB(pPos.above()).inflate(3.0D, 3.0D, 3.0D));
                if (!list.isEmpty()) {
                    for (LivingEntity livingentity : list) {
                        //造成伤害并添加buff，对非玩家生物造成的伤害不会致死
                        if (livingentity instanceof Player || livingentity.getHealth() > entity_damage) {
                            livingentity.hurt(pLevel.damageSources().cactus(), entity_damage);
                        }
                    }
                }
                this.setTiltAndScheduleTick(pState, pLevel, pPos, Tilt.FULL, SoundEvents.BIG_DRIPLEAF_TILT_DOWN);
            } else if (tilt == Tilt.FULL) {
                resetTilt(pState, pLevel, pPos);
            }

        }
    }

    // 当邻居方块改变时的处理方法
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pNeighborBlock, BlockPos pNeighborPos, boolean pMovedByPiston) {
        // 如果有邻居信号，重置倾斜状态
        if (pLevel.hasNeighborSignal(pPos)) {
            resetTilt(pState, pLevel, pPos);
        }

    }

    // 播放倾斜声音的静态方法
    private static void playTiltSound(Level pLevel, BlockPos pPos, SoundEvent pSound) {
        // 生成一个在0.8到1.2之间的随机音量
        float f = Mth.randomBetween(pLevel.random, 0.8F, 1.2F);
        // 播放声音
        pLevel.playSound((Player)null, pPos, pSound, SoundSource.BLOCKS, 1.0F, f);
    }

    // 判断实体是否可以使方块倾斜的静态方法
    private static boolean canEntityTilt(BlockPos pPos, Entity pEntity) {
        return pEntity.onGround() && pEntity.position().y > (double)((float)pPos.getY() + 0.6875F);
    }

    // 设置倾斜状态并安排下一次方块刻的方法
    private void setTiltAndScheduleTick(BlockState pState, Level pLevel, BlockPos pPos, Tilt pTilt, @Nullable SoundEvent pSound) {
        // 设置倾斜状态
        setTilt(pState, pLevel, pPos, pTilt);
        // 如果有声音，播放声音
        if (pSound != null) {
            playTiltSound(pLevel, pPos, pSound);
        }

        // 获取到下一个倾斜状态的延迟时间
        int i = DELAY_UNTIL_NEXT_TILT_STATE.getInt(pTilt);
        // 如果延迟时间不为-1，安排方块刻
        if (i != -1) {
            pLevel.scheduleTick(pPos, this, i);
        }

    }

    // 重置倾斜状态的静态方法
    private static void resetTilt(BlockState pState, Level pLevel, BlockPos pPos) {
        // 设置倾斜状态为NONE
        setTilt(pState, pLevel, pPos, Tilt.NONE);
        // 如果当前倾斜状态不是NONE，播放倾斜上升的声音
        if (pState.getValue(TILT) != Tilt.NONE) {
            playTiltSound(pLevel, pPos, SoundEvents.BIG_DRIPLEAF_TILT_UP);
        }

    }
    // 设置倾斜状态的静态方法
    private static void setTilt(BlockState pState, Level pLevel, BlockPos pPos, Tilt pTilt) {
        // 获取当前方块状态的倾斜状态
        Tilt tilt = pState.getValue(TILT);
        // 更新方块状态为新的倾斜状态
        pLevel.setBlock(pPos, pState.setValue(TILT, pTilt), 2);
        // 如果新的倾斜状态会引起振动且与旧的倾斜状态不同
        if (pTilt.causesVibration() && pTilt != tilt) {
            // 触发方块改变的游戏事件
            pLevel.gameEvent((Entity)null, GameEvent.BLOCK_CHANGE, pPos);
        }
    }

    // 获取方块的碰撞形状的方法
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        // 根据当前方块状态的倾斜状态从LEAF_SHAPES中获取对应的形状
        return LEAF_SHAPES.get(pState.getValue(TILT));
    }

    // 获取方块的形状的方法
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        // 从形状缓存中获取当前方块状态对应的形状
        return this.shapesCache.get(pState);
    }

    // 获取放置方块时的状态的方法
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        // 获取点击位置下方方块的状态
        BlockState blockstate = pContext.getLevel().getBlockState(pContext.getClickedPos().below());
        // 获取点击位置的流体状态
        FluidState fluidstate = pContext.getLevel().getFluidState(pContext.getClickedPos());
        // 判断下方方块是否是大型垂滴叶或大型垂滴叶茎
        boolean flag = blockstate.is(Blocks.BIG_DRIPLEAF) || blockstate.is(Blocks.BIG_DRIPLEAF_STEM);
        // 创建方块状态，设置水logged和朝向
        return this.defaultBlockState().setValue(WATERLOGGED, Boolean.valueOf(fluidstate.isSourceOfType(Fluids.WATER))).setValue(FACING, flag ? blockstate.getValue(FACING) : pContext.getHorizontalDirection().getOpposite());
    }

    // 创建方块状态定义的方法，用于添加方块的属性
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        // 添加水logged、朝向和倾斜状态属性
        pBuilder.add(WATERLOGGED, FACING, TILT);
    }

}