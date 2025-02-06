package com.renyigesai.immortalers_delight.block;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import vectorwing.farmersdelight.common.registry.ModSounds;

import java.util.List;
import java.util.OptionalInt;

public class HimekaidoLeavesGrowing extends ReapCropBlock{

    /*
     树叶的腐烂距离阈值，当距离超过此值时可能腐烂
     */
    public static final int DECAY_DISTANCE = 7;
    /*
     表示树叶到最近原木的距离属性
     */
    public static final IntegerProperty DISTANCE = BlockStateProperties.DISTANCE;
    /*
    表示树叶是否持久存在的属性（不受自然腐烂影响）
     */
    public static final BooleanProperty PERSISTENT = BlockStateProperties.PERSISTENT;
    /*
    / 用于控制树叶方块可否进行收获方法
     */
    private static final int FRUIT_AGE = 5;
    /*
        仙人掌的轮廓形状
         */
    protected static final VoxelShape OUTLINE_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);

    public HimekaidoLeavesGrowing(Properties p_52247_) {
        super(p_52247_);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(DISTANCE, Integer.valueOf(7))
                .setValue(PERSISTENT, Boolean.valueOf(false))
                .setValue(AGE, Integer.valueOf(0)));
    }
    /*
     创建方块状态定义，将距离、持久和含水属性添加到状态定义中
     */
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(DISTANCE, PERSISTENT, AGE);
    }
    /*
     获取树叶方块提供的支撑形状，这里返回空形状，表示树叶不提供实体支撑
     */
    public VoxelShape getBlockSupportShape(BlockState p_54456_, BlockGetter p_54457_, BlockPos p_54458_) {
        return Shapes.empty();
    }
    /*
     判断方块是否需要随机 tick，
     如果是持久的，那么不需要（不生长不腐烂）
     当距离为7时需要，会触发腐烂检查；或不为最大生长阶段需要，触发生长方法
     */
    public boolean isRandomlyTicking(BlockState pState) {
        if (pState.getValue(PERSISTENT)) {return false;}
        return pState.getValue(DISTANCE) == DECAY_DISTANCE || !this.isMaxAge(pState);
    }

    /*
     执行随机 tick 逻辑，若树叶处于腐烂状态，则掉落资源并移除方块
     如果不是,执行生长方法
     */
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        /*
        执行树叶的腐烂行为
         */
        pLevel.setBlock(pPos, updateDistance(pState, pLevel, pPos), 3);
        if (this.decaying(pState)) {
            dropResources(pState, pLevel, pPos);
            pLevel.removeBlock(pPos, false);
            return;
        }
        /*
        执行生长逻辑
         */
        if (!pLevel.isAreaLoaded(pPos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
        if (pLevel.getRawBrightness(pPos, 0) >= 9) {
            int i = this.getAge(pState);
            if (i < this.getMaxAge()) {
                float f = getGrowthSpeed(this, pLevel, pPos);
                if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(pLevel, pPos, pState, pRandom.nextInt((int)(219.0F / f) + 1) == 0)) {
                    this.growCrops(pLevel, pPos, pState);
                    net.minecraftforge.common.ForgeHooks.onCropsGrowPost(pLevel, pPos, pState);
                }
            }
        } else {

        }
    }
    /*
     检查树叶是否处于腐烂状态，即非持久且距离为7
     */
    protected boolean decaying(BlockState p_221386_) {
        return !p_221386_.getValue(PERSISTENT) && p_221386_.getValue(DISTANCE) == DECAY_DISTANCE;
    }
    /*
     理论上是每 tick 执行的逻辑，更新树叶到最近原木的距离
     实际上并不稳定，似乎不能在这里跟其他方块交互（存疑）
     */
    public void tick(BlockState p_221369_, ServerLevel p_221370_, BlockPos p_221371_, RandomSource p_221372_) {
        p_221370_.setBlock(p_221371_, updateDistance(p_221369_, p_221370_, p_221371_), 3);
    }

    /*
     获取树叶方块的光照强度，这里返回1，表示树叶有一定的透光性
     */
    public int getLightBlock(BlockState p_54460_, BlockGetter p_54461_, BlockPos p_54462_) {
        return 1;
    }


    /*
    / 根据邻居方块状态更新当前方块状态，若含水则安排水的 tick，根据邻居距离更新自身距离并安排自身 tick
     */
    public BlockState updateShape(BlockState p_54440_, Direction p_54441_, BlockState p_54442_, LevelAccessor p_54443_, BlockPos p_54444_, BlockPos p_54445_) {

        int i = getDistanceAt(p_54442_) + 1;
        if (i != 1 || p_54440_.getValue(DISTANCE) != i) {
            p_54443_.scheduleTick(p_54444_, this, 1);
        }

        return p_54440_;
    }

    /*
    / 更新树叶到最近原木的距离，遍历周围方向，取最小距离并更新方块状态
     */
    private static BlockState updateDistance(BlockState p_54436_, LevelAccessor p_54437_, BlockPos p_54438_) {
        int i = 7;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for(Direction direction : Direction.values()) {
            blockpos$mutableblockpos.setWithOffset(p_54438_, direction);
            i = Math.min(i, getDistanceAt(p_54437_.getBlockState(blockpos$mutableblockpos)) + 1);
            if (i == 1) {
                break;
            }
        }

        return p_54436_.setValue(DISTANCE, Integer.valueOf(i));
    }

    /*
    / 获取邻居方块的距离，若邻居是原木则距离为0，否则返回其自身的距离属性或默认值7
     */
    private static int getDistanceAt(BlockState p_54464_) {
        return getOptionalDistanceAt(p_54464_).orElse(7);
    }

    /*
    / 获取邻居方块距离的可选值，若为原木则返回0，否则根据是否有距离属性返回相应值或空
     */
    public static OptionalInt getOptionalDistanceAt(BlockState p_277868_) {
        if (p_277868_.is(BlockTags.LOGS)) {
            return OptionalInt.of(0);
        } else {
            return p_277868_.hasProperty(DISTANCE) ? OptionalInt.of(p_277868_.getValue(DISTANCE)) : OptionalInt.empty();
        }
    }
    /*
     在客户端定期调用，用于显示效果（如在下雨时可能生成水滴粒子）
     */
    public void animateTick(BlockState p_221374_, Level p_221375_, BlockPos p_221376_, RandomSource p_221377_) {
        if (p_221375_.isRainingAt(p_221376_.above())) {
            if (p_221377_.nextInt(15) == 1) {
                BlockPos blockpos = p_221376_.below();
                BlockState blockstate = p_221375_.getBlockState(blockpos);
                if (!blockstate.canOcclude() || !blockstate.isFaceSturdy(p_221375_, blockpos, Direction.UP)) {
                    ParticleUtils.spawnParticleBelow(p_221375_, p_221376_, p_221377_, ParticleTypes.DRIPPING_WATER);
                }
            }
        }
    }
    /*
     判断作物是否能在给定环境中存活，如果是持久的则始终满足
     如果不是则需要距离小于7
     重写存活条件以确保能跟树叶一样浮空放置
     */
    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return (pState.getValue(PERSISTENT) || !(pState.getValue(DISTANCE) == 7));
    }

    protected static float getGrowthSpeed(Block p_52273_, BlockGetter p_52274_, BlockPos p_52275_) {
        float f = 1.0F;
        BlockPos blockpos = p_52275_.below();

        for(int i = -1; i <= 1; ++i) {
            for(int j = -1; j <= 1; ++j) {
                float f1 = 0.0F;
                BlockState blockstate = p_52274_.getBlockState(blockpos.offset(i, 0, j));
                int d = getDistanceAt(blockstate);
                if (d == 0) {f1 = 10.0F;f += f1;break;}
                if (d < 5
                    //blockstate.canSustainPlant(p_52274_, blockpos.offset(i, 0, j), net.minecraft.core.Direction.UP, (net.minecraftforge.common.IPlantable) p_52273_)
                ) {
                    f1 = 1.0F;
                    if (d < 3
                        //blockstate.isFertile(p_52274_, p_52275_.offset(i, 0, j))
                    ) {
                        f1 = 3.0F;
                    }
                }

                if (i != 0 || j != 0) {
                    f1 /= 4.0F;
                }

                f += f1;
            }
        }

        BlockPos blockpos1 = p_52275_.north();
        BlockPos blockpos2 = p_52275_.south();
        BlockPos blockpos3 = p_52275_.west();
        BlockPos blockpos4 = p_52275_.east();
        boolean flag = p_52274_.getBlockState(blockpos3).is(p_52273_) || p_52274_.getBlockState(blockpos4).is(p_52273_);
        boolean flag1 = p_52274_.getBlockState(blockpos1).is(p_52273_) || p_52274_.getBlockState(blockpos2).is(p_52273_);
        if (flag && flag1) {
            f /= 2.0F;
        } else {
            boolean flag2 = p_52274_.getBlockState(blockpos3.north()).is(p_52273_) || p_52274_.getBlockState(blockpos4.north()).is(p_52273_) || p_52274_.getBlockState(blockpos4.south()).is(p_52273_) || p_52274_.getBlockState(blockpos3.south()).is(p_52273_);
            if (flag2) {
                f /= 2.0F;
            }
        }

        return f;
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ImmortalersDelightItems.HIMEKAIDO_SEED.get();
    }

    /*
    获取仙人掌的轮廓形状
     */
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return OUTLINE_SHAPE;
    }

    @Override
    public List<ItemStack> getDrops(BlockState p_287732_, LootParams.Builder p_287596_) {
        return super.getDrops(p_287732_, p_287596_);
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }
    @Override
    public boolean isValidBonemealTarget(LevelReader p_255715_, BlockPos p_52259_, BlockState p_52260_, boolean p_52261_) {
        return p_52260_.getValue(PERSISTENT);
    }
    @Override
    public void performBonemeal(ServerLevel p_221040_, RandomSource p_221041_, BlockPos p_221042_, BlockState p_221043_) {
        if (this.getAge(p_221043_) < this.getMaxAge()) {
            p_221040_.setBlock(p_221042_, this.getStateForAge(this.getAge(p_221043_) + 1), 2);
        }
    }
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        int age = state.getValue(AGE);
        if (age >= FRUIT_AGE) {
            boolean temp = false;
            if (level instanceof ServerLevel level1) {
                List<ItemStack> stacks = getDrops(state, level1, pos, null);
                if (!stacks.isEmpty()){
                    for (ItemStack stack : stacks) {
                        popResource(level, pos, stack);
                    }
                    temp = true;
                }
            }
            if (temp){
                level.setBlock(pos,state.setValue(AGE,0),3);
                level.playSound((Player)null, pos, (SoundEvent) ModSounds.ITEM_TOMATO_PICK_FROM_BUSH.get(), SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
                return InteractionResult.SUCCESS;
            }
        }
        return super.use(state, level, pos, player, hand, hitResult);
    }
}
