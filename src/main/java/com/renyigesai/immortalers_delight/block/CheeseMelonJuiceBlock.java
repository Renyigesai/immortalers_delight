package com.renyigesai.immortalers_delight.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

public class CheeseMelonJuiceBlock extends SpoonBlock{

    private static final VoxelShape BOX = box(3,0,3,13,9,13);
    public CheeseMelonJuiceBlock(Properties p_49795_, Supplier<Item> spoonItem) {
        super(p_49795_, spoonItem);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return BOX;
    }

    @Override
    public void dropRemainItem(Level level, BlockPos pos) {

    }

    @Override
    public ItemStack getScoopItem() {
        return new ItemStack(Items.GLASS_BOTTLE);
    }

    @Override
    public SoundEvent getScoopSound() {
        return SoundEvents.BOTTLE_FILL;
    }
}
