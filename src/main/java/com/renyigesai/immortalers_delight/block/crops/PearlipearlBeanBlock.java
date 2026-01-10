package com.renyigesai.immortalers_delight.block.crops;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
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
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

import static com.renyigesai.immortalers_delight.block.crops.PearlipearlStalkBlock.IS_LEAVES;

public class PearlipearlBeanBlock extends HorizontalDirectionalBlock implements BonemealableBlock {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_2;
    public PearlipearlBeanBlock(Properties p_54120_) {
        super(p_54120_);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(AGE, Integer.valueOf(0)));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            default -> box(3, 0, 6, 13, 12, 16);
            case NORTH -> box(3, 0, 0, 13, 12, 10);
            case EAST -> box(6, 0, 3, 16, 12, 13);
            case WEST -> box(0, 0, 3, 10, 12, 13);
        };
    }

    public int getAge(BlockState state){
        return state.getValue(AGE);
    }

    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        //if (!pLevel.isAreaLoaded(pPos, 1) || pLevel.getRawBrightness(pPos, 0) <= 9) return;
        int i = pState.getValue(AGE);
        if (i < 2 ) {
            if (pLevel.getBlockState(pPos.above().relative(pState.getValue(FACING))).is(ImmortalersDelightBlocks.PEARLIPEARL_STALK.get())
                    && pLevel.getBlockState(pPos.above().relative(pState.getValue(FACING))).getValue(IS_LEAVES) == ImmortalersDelightBlocks.PEARLIPEARL_STALK.get().defaultBlockState().setValue(PearlipearlStalkBlock.IS_LEAVES,true).getValue(IS_LEAVES)
                    && net.minecraftforge.common.ForgeHooks.onCropsGrowPre(pLevel, pPos, pState, pLevel.random.nextInt(8) == 0)
            ) {
                pLevel.setBlock(pPos, pState.setValue(AGE, i + 1), 2);
                net.minecraftforge.common.ForgeHooks.onCropsGrowPost(pLevel, pPos, pState);
            }

        }

    }

    public boolean canSurvive(BlockState p_51767_, LevelReader p_51768_, BlockPos p_51769_) {
        BlockState blockstate = p_51768_.getBlockState(p_51769_.relative(p_51767_.getValue(FACING)));
        return blockstate.is(ImmortalersDelightBlocks.PEARLIPEARL_STALK.get())
                && blockstate.getValue(IS_LEAVES) != ImmortalersDelightBlocks.PEARLIPEARL_STALK.get().defaultBlockState().setValue(PearlipearlStalkBlock.IS_LEAVES,true).getValue(IS_LEAVES);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext p_51750_) {
        BlockState blockstate = this.defaultBlockState();
        LevelReader levelreader = p_51750_.getLevel();
        BlockPos blockpos = p_51750_.getClickedPos();

        for(Direction direction : p_51750_.getNearestLookingDirections()) {
            if (direction.getAxis().isHorizontal()) {
                blockstate = blockstate.setValue(FACING, direction);
                if (blockstate.canSurvive(levelreader, blockpos)) {
                    return blockstate;
                }
            }
        }

        return null;
    }

    public BlockState updateShape(BlockState p_51771_, Direction p_51772_, BlockState p_51773_, LevelAccessor p_51774_, BlockPos p_51775_, BlockPos p_51776_) {
        return p_51772_ == p_51771_.getValue(FACING) && !p_51771_.canSurvive(p_51774_, p_51775_) ? Blocks.AIR.defaultBlockState() : super.updateShape(p_51771_, p_51772_, p_51773_, p_51774_, p_51775_, p_51776_);
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

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_51778_) {
        p_51778_.add(FACING, AGE);
    }

    public boolean isPathfindable(BlockState p_51762_, BlockGetter p_51763_, BlockPos p_51764_, PathComputationType p_51765_) {
        return false;
    }
}
