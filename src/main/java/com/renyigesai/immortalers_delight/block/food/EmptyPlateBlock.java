package com.renyigesai.immortalers_delight.block.food;

import com.mojang.serialization.MapCodec;
import com.renyigesai.immortalers_delight.api.PlateBaseBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import static net.minecraft.world.level.block.Block.simpleCodec;

public class EmptyPlateBlock extends HorizontalDirectionalBlock implements PlateBaseBlock {

    public static final MapCodec<EmptyPlateBlock> CODEC = simpleCodec(EmptyPlateBlock::new);
    public static final IntegerProperty TYPES = IntegerProperty.create("types",0,3);
    public static final VoxelShape BOX_LOW = box(1.0D,0.0D,1.0D,15.0D,1.0D,15.0D);
    public static final VoxelShape BOX = box(1.0D,0.0D,1.0D,15.0D,2.0D,15.0D);

    public EmptyPlateBlock(Properties p_54120_) {
        super(p_54120_);
        super.registerDefaultState(defaultBlockState().setValue(TYPES,0).setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return state.getValue(TYPES) == 0 ? BOX : BOX_LOW;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection());
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        return facing == Direction.DOWN && !stateIn.canSurvive(level, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).isSolid();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TYPES,FACING);
    }

    @Override
    public boolean isEmptyPlate(BlockState state) {
        return true;
    }
}
