package com.renyigesai.immortalers_delight.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TravastrugglerLogBlock extends BasicsLogsBlock {
    public TravastrugglerLogBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(AXIS)) {
            case X -> Shapes.or(box(0, 0, 0, 16, 16, 3), box(0, 0, 13, 16, 16, 16), box(0, 0, 3, 16, 3, 13), box(0, 13, 3, 16, 16, 13));
            case Y -> Shapes.or(box(0, 0, 0, 3, 16, 16), box(13, 0, 0, 16, 16, 16), box(3, 0, 0, 13, 16, 3), box(3, 0, 13, 13, 16, 16));
            case Z -> Shapes.or(box(0, 0, 0, 3, 16, 16), box(13, 0, 0, 16, 16, 16), box(3, 0, 0, 13, 3, 16), box(3, 13, 0, 13, 16, 16));
        };
    }
}
