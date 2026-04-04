package com.renyigesai.immortalers_delight.block.food;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import vectorwing.farmersdelight.common.block.PieBlock;

import java.util.function.Supplier;

public class RoastedMushroomPizzaBlock extends PieBlock {
    protected static final VoxelShape SHAPE;
    public RoastedMushroomPizzaBlock(Properties properties, Supplier<Item> pieSlice) {
        super(properties, pieSlice);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    static {
        SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 1.0, 15.0);
    }
}
