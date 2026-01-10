package com.renyigesai.immortalers_delight.block.food;

import com.renyigesai.immortalers_delight.block.SpoonBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import vectorwing.farmersdelight.common.block.FeastBlock;

import java.util.function.Supplier;

public class PitcherPlantClaypotRiceBlock extends SpoonBlock {

    private static final VoxelShape BOX = box(3,0,3,13,9,13);

    public PitcherPlantClaypotRiceBlock(Properties p_49795_, Supplier<Item> spoonItem) {
        super(p_49795_, spoonItem);
    }

    @Override
    public void dropRemainItem(Level level, BlockPos pos) {

    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return BOX;
    }
}
