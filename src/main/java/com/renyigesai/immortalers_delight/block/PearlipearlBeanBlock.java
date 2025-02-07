package com.renyigesai.immortalers_delight.block;

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



    public void randomTick(BlockState p_221000_, ServerLevel p_221001_, BlockPos p_221002_, RandomSource p_221003_) {
        if (true) {
            int i = p_221000_.getValue(AGE);
            if (i < 2 && net.minecraftforge.common.ForgeHooks.onCropsGrowPre(p_221001_, p_221002_, p_221000_, p_221001_.random.nextInt(5) == 0)) {
                p_221001_.setBlock(p_221002_, p_221000_.setValue(AGE, i + 1), 2);
                net.minecraftforge.common.ForgeHooks.onCropsGrowPost(p_221001_, p_221002_, p_221000_);
            }
        }

    }

    public boolean canSurvive(BlockState p_51767_, LevelReader p_51768_, BlockPos p_51769_) {
        BlockState blockstate = p_51768_.getBlockState(p_51769_.relative(p_51767_.getValue(FACING)));
        return blockstate.is(ImmortalersDelightBlocks.PEARLIPEARL_STALK.get());
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

    public boolean isValidBonemealTarget(LevelReader p_256189_, BlockPos p_51753_, BlockState p_51754_, boolean p_51755_) {
        return p_51754_.getValue(AGE) < 2;
    }

    public boolean isBonemealSuccess(Level p_220995_, RandomSource p_220996_, BlockPos p_220997_, BlockState p_220998_) {
        return true;
    }

    public void performBonemeal(ServerLevel p_220990_, RandomSource p_220991_, BlockPos p_220992_, BlockState p_220993_) {
        p_220990_.setBlock(p_220992_, p_220993_.setValue(AGE, Integer.valueOf(p_220993_.getValue(AGE) + 1)), 2);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_51778_) {
        p_51778_.add(FACING, AGE);
    }

    public boolean isPathfindable(BlockState p_51762_, BlockGetter p_51763_, BlockPos p_51764_, PathComputationType p_51765_) {
        return false;
    }
}
