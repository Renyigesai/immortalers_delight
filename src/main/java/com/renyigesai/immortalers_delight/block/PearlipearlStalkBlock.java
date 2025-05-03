package com.renyigesai.immortalers_delight.block;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;

public class PearlipearlStalkBlock extends HorizontalDirectionalBlock implements IPlantable, BonemealableBlock {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_2;
    public static final BooleanProperty IS_LEAVES = BooleanProperty.create("is_leaves");
    public static final BooleanProperty BLOOM = BooleanProperty.create("bloom");
    public static final VoxelShape AGE_1 = box(6.0D,0.0D,6.0D,10.0D,16.0D,10.0D);
    public static final VoxelShape AGE_2 = box(5.0D,0.0D,5.0D,11.0D,16.0D,11.0D);
    public static final VoxelShape AGE_3 = box(4.0D,0.0D,4.0D,12.0D,16.0D,12.0D);
    public PearlipearlStalkBlock(Properties p_49795_) {
        super(p_49795_);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0).setValue(IS_LEAVES,true).setValue(BLOOM,false).setValue(FACING,Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        int age = state.getValue(AGE);
        return switch (age) {
            case 1 -> AGE_2;
            case 2 -> AGE_3;
            default -> AGE_1;
        };
    }

    public BlockState getStateForLeave(boolean isLeaves) {
        return this.defaultBlockState().setValue(IS_LEAVES, isLeaves);
    }
    public void tick(BlockState p_222543_, ServerLevel p_222544_, BlockPos p_222545_, RandomSource p_222546_) {
        if (!p_222543_.canSurvive(p_222544_, p_222545_)) {
            p_222544_.destroyBlock(p_222545_, true);
        }

    }

    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource randomSource) {
        if (level.isEmptyBlock(pos.above())) {
            int i;
            for(i = 1; level.getBlockState(pos.below(i)).is(this); ++i) {
            }
            if (ForgeHooks.onCropsGrowPre(level, pos, state, randomSource.nextInt(6) == 0)) {
                stalkGrowing(state,level,pos,randomSource,i);
                if (state.getValue(BLOOM)) {
                    // 获取当前方块的朝向
                    Direction direction = state.getValue(FACING);
                    BlockPos neighborPos = pos.below().relative(direction);
                    // 如果上方可以放置方块
                    if (level.getBlockState(neighborPos).isAir()) {
                        // 设置 B 方块的 BlockState，使其 C 面朝向 A 方块
                        BlockState bBlockState = ImmortalersDelightBlocks.PEARLIPEARL_BUNDLE.get().defaultBlockState().setValue(FACING, direction.getOpposite()); // C 面朝向 A 方块
                        // 在相邻位置放置 B 方块
                        level.setBlockAndUpdate(neighborPos, bBlockState);
                    }
                }
            }
        }
    }

    public void stalkGrowing(BlockState state, ServerLevel level, BlockPos pos, RandomSource randomSource, int hight) {
        int age = state.getValue(AGE);
        /*最大高度小于3时尝试向上生长一次*/
        if (hight < 3){
            level.setBlockAndUpdate(pos.above(), this.defaultBlockState().setValue(FACING, state.getValue(FACING)).setValue(IS_LEAVES,true));
            level.setBlock(pos,state.setValue(IS_LEAVES,false),3);
            ForgeHooks.onCropsGrowPost(level, pos.above(), this.defaultBlockState());
        }else {
            if (age < 2){
                /*每个茎age需达到3才能变粗*/
                for (int j = 0; j < 3; j++) {
                    boolean b = j != 2 && j != 1;
                    int n = level.getBlockState(pos.below(j)).getValue(AGE)!= 2?level.getBlockState(pos.below(j)).getValue(AGE)+1:2;
                    level.setBlock(pos.below(j),state.setValue(AGE,n).setValue(IS_LEAVES,b), 3);
                }
            }
        }
        if (hight == 3 && !state.getValue(IS_LEAVES)){
            level.setBlock(pos,state.setValue(IS_LEAVES,true),3);
        }
        if (age == 2 && hight == 3 && state.getValue(IS_LEAVES)){
            level.setBlock(pos,state.setValue(BLOOM,true),3);
        }
    }

    public BlockState updateShape(BlockState p_57179_, Direction p_57180_, BlockState p_57181_, LevelAccessor p_57182_, BlockPos p_57183_, BlockPos p_57184_) {
        if (!p_57179_.canSurvive(p_57182_, p_57183_)) {
            p_57182_.scheduleTick(p_57183_, this, 1);
        }

        return super.updateShape(p_57179_, p_57180_, p_57181_, p_57182_, p_57183_, p_57184_);
    }

    public boolean canSurvive(BlockState p_57175_, LevelReader p_57176_, BlockPos p_57177_) {
        BlockState soil = p_57176_.getBlockState(p_57177_.below());
        if (soil.canSustainPlant(p_57176_, p_57177_.below(), Direction.UP, this)) return true;
        BlockState blockstate = p_57176_.getBlockState(p_57177_.below());
        if (blockstate.is(this)) {
            return true;
        } else {
            if (blockstate.is(BlockTags.DIRT) || blockstate.is(BlockTags.SAND)) {
                BlockPos blockpos = p_57177_.below();

                for(Direction direction : Direction.Plane.HORIZONTAL) {
                    BlockState blockstate1 = p_57176_.getBlockState(blockpos.relative(direction));
                    FluidState fluidstate = p_57176_.getFluidState(blockpos.relative(direction));
                    if (p_57175_.canBeHydrated(p_57176_, p_57177_, fluidstate, blockpos.relative(direction)) || blockstate1.is(Blocks.FROSTED_ICE)) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (player.getItemInHand(hand).is(ImmortalersDelightItems.PEARLIP.get())) {
            if (player.isCreative()) return super.use(state, level, pos, player, hand, hitResult);
            else {return InteractionResult.SUCCESS;}
        }
        return super.use(state, level, pos, player, hand, hitResult);
    }

    // 判断是否可以对当前方块使用骨粉的方法
    public boolean isValidBonemealTarget(LevelReader pLevel, BlockPos pPos, BlockState pState, boolean pIsClient) {
        // 获取上方方块的状态
        BlockState blockstate = pLevel.getBlockState(pPos.above());
        BlockState blockstate1 = pLevel.getBlockState(pPos.above(2));
        // 判断上方方块是否可以结果
        boolean canFruit =  blockstate.is(this)
                && blockstate.getValue(IS_LEAVES) == getStateForLeave(false).getValue(IS_LEAVES)
                && blockstate1.is(this)
                && blockstate1.getValue(BLOOM) != getStateForLeave(false).getValue(IS_LEAVES);
        if (canFruit) return true;
        int i;
        for(i = 1; pLevel.getBlockState(pPos.below(i)).is(this); ++i) {
        }
        boolean canBloom = i == 3 && pLevel.getBlockState(pPos).is(this) && pState.getValue(BLOOM) == getStateForLeave(false).getValue(IS_LEAVES);
        boolean canGrow = i < 3 && blockstate.isAir();
        return canGrow || canBloom;
    }

    // 判断使用骨粉是否成功的方法，这里总是返回true
    public boolean isBonemealSuccess(Level pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
        return !pLevel.getBlockState(pPos.below()).is(this) || pRandom.nextInt(5) < 3;
    }

    // 使用骨粉后的处理方法，在上方放置茎和叶子方块
    public void performBonemeal(ServerLevel pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
        // 获取上方方块的位置和状态，如果不是棱蕉茎，先让他生长
        BlockPos blockpos = pPos.above();
        BlockState blockstate = pLevel.getBlockState(blockpos);
        if (!blockstate.is(this)) {
            int i;
            for(i = 1; pLevel.getBlockState(pPos.below(i)).is(this); ++i) {
            }
            stalkGrowing(pState, pLevel, pPos, pRandom, i);
            return;
        }
        // 遍历水平方向的四个方向
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            // 获取 A 方块相邻的位置
            BlockPos neighborPos = blockpos.relative(direction);
            BlockState neighborState = pLevel.getBlockState(neighborPos);
            // 检查相邻位置是否是空气方块
            if (neighborState.isAir()) {
                // 设置 B 方块的 BlockState，使其 C 面朝向 A 方块
                BlockState bBlockState = ImmortalersDelightBlocks.PEARLIPEARL_BUNDLE.get().defaultBlockState().setValue(FACING, direction.getOpposite()); // C 面朝向 A 方块
                // 在相邻位置放置 B 方块
                pLevel.setBlockAndUpdate(neighborPos, bBlockState);
            }
            if (neighborState.is(ImmortalersDelightBlocks.PEARLIPEARL_BUNDLE.get())) {
                int age = neighborState.getValue(AGE);
                if (age < 2 && pRandom.nextInt(4) == 0) {
                    pLevel.setBlock(neighborPos,neighborState.setValue(AGE, age + 1), 2);
                    net.minecraftforge.common.ForgeHooks.onCropsGrowPost(pLevel, pPos, pState);
                }
            }
        }

    }

    // 获取放置方块时的状态的方法
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        // 获取点击位置下方方块的状态
        BlockState blockstate = pContext.getLevel().getBlockState(pContext.getClickedPos().below());
        // 获取点击位置的流体状态
        //FluidState fluidstate = pContext.getLevel().getFluidState(pContext.getClickedPos());
        // 判断下方方块是否是此方块
        boolean flag = blockstate.is(this);
        // 创建方块状态，设置水logged和朝向
        return this.defaultBlockState().setValue(FACING, flag ? blockstate.getValue(FACING) : pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(AGE,IS_LEAVES,BLOOM,FACING);
    }

    @Override
    public BlockState getPlant(BlockGetter world, BlockPos pos) {
        return defaultBlockState();
    }
}
