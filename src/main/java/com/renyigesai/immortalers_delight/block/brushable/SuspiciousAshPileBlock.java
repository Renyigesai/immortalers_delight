package com.renyigesai.immortalers_delight.block.brushable;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BrushableBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class SuspiciousAshPileBlock extends ModBrushableBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final VoxelShape BOX = box(1.0D,0.0D,1.0D,15.0D,10.0D,15.0D);
    public static final VoxelShape OUTLINE_BOX = box(0.1D,0.0D,0.1D,15.9D,12.0D,15.9D);
    public SuspiciousAshPileBlock(Block pTurnsInto, Properties pProperties, SoundEvent pBrushSound, SoundEvent pBrushCompletedSound) {
        super(pTurnsInto, pProperties, pBrushSound, pBrushCompletedSound);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(BlockStateProperties.DUSTED, 0));
    }
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return OUTLINE_BOX;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return BOX;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
        super.createBlockStateDefinition(pBuilder);
    }
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new SuspiciousAshPileBlockEntity(pPos, pState);
    }
}
