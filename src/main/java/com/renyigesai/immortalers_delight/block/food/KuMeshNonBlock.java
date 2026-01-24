package com.renyigesai.immortalers_delight.block.food;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

public class KuMeshNonBlock extends StackedBreadBlock{

    private static final VoxelShape[] SHAPE_BY_AGE_1 = new VoxelShape[]{
            Block.box(0.1D, 0.0D, 0.1D, 15.9D, 14.0D, 15.9D),
            Block.box(1.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D),
            Block.box(1.0D, 0.0D, 1.0D, 15.0D, 2.0D, 15.0D),
    };
    public KuMeshNonBlock(Properties p_54120_, Supplier<Item> servingItem, Supplier<Item> pileItem, int pilePerItem) {
        super(p_54120_, servingItem, pileItem, pilePerItem);
    }

    @Override
    public int getMaxBites(){return 8;}
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        int i =state.getValue(BITES) / 4;
        if (i >= SHAPE_BY_AGE_1.length) i = SHAPE_BY_AGE_1.length - 1;
        return SHAPE_BY_AGE_1[i];
    }
}
