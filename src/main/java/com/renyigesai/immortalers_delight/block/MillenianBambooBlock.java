package com.renyigesai.immortalers_delight.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

public class MillenianBambooBlock extends SpoonBlock {
    public static final VoxelShape BOX = box(1.0D,0.0D,1.0D,15.0D,10.0D,15.0D);

    public MillenianBambooBlock(Properties p_49795_, Supplier<Item> spoonItem) {
        super(p_49795_, spoonItem);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return BOX;
    }

    @Override
    public ItemStack getRemainItem() {
        return new ItemStack(Items.BAMBOO);
    }

    @Override
    public void dropRemainItem(Level level, BlockPos pos) {
        vectorwing.farmersdelight.common.utility.ItemUtils.spawnItemEntity(level,
                new ItemStack(this.getRemainItem().getItem(),6),pos.getX() + 0.5,pos.getY() + 0.5,pos.getZ() + 0.5,0.0,0.0,0.0);
    }
}
