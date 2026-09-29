package com.renyigesai.immortalers_delight.block.food;

import com.renyigesai.immortalers_delight.block.SpoonBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.function.Supplier;

public class SoyPasteScrambledEggsBlock extends SpoonBlock {
    public SoyPasteScrambledEggsBlock(Properties p_49795_, Supplier<Item> spoonItem) {
        super(p_49795_, spoonItem);
    }

    public ItemStack getScoopItem(){
        return new ItemStack(ModItems.COOKED_RICE.get());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return box(1.0D,0.0D,1.0D,15.0D,4.0D,15.0D);
    }

}
