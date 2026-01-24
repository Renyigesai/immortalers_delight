package com.renyigesai.immortalers_delight.block.food;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

public class LargeColumnBlock extends StackedBreadBlock{

    private static final VoxelShape[] SHAPE_BY_AGE_0 = new VoxelShape[]{
            Block.box(0.1D, 0.0D, 0.1D, 15.9D, 16.0D, 15.9D),
            Block.box(0.1D, 0.0D, 0.1D, 15.9D, 11.0D, 15.9D),
            Block.box(1.0D, 0.0D, 1.0D, 15.0D, 6.0D, 15.0D),
            Block.box(1.0D, 0.0D, 1.0D, 15.0D, 1.0D, 15.0D)
    };
    public LargeColumnBlock(Properties p_54120_, Supplier<Item> servingItem, Supplier<Item> pileItem, int pilePerItem) {
        super(p_54120_, servingItem, pileItem, pilePerItem);
    }

    @Override
    public int getMaxBites(){return 6;}
    @Override
    public boolean isEdible(){return false;}
    @Override
    public boolean isCuttable(ItemStack stack){return stack.is(ItemTags.AXES);}
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        int i =state.getValue(BITES) / 2;
        if (i >= SHAPE_BY_AGE_0.length) i = SHAPE_BY_AGE_0.length - 1;
        return SHAPE_BY_AGE_0[i];
    }
}
