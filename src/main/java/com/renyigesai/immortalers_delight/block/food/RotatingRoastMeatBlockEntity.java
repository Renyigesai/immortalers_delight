package com.renyigesai.immortalers_delight.block.food;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class RotatingRoastMeatBlockEntity extends BlockEntity {
    public RotatingRoastMeatBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ImmortalersDelightBlocks.ENCHANTAL_COOLER_ENTITY.get(), pPos, pBlockState);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, RotatingRoastMeatBlockEntity blockEntity){
        boolean lit = level.getMaxLocalRawBrightness(pos) >= 10;
        state.setValue(RotatingRoastMeatBlock.LIT,lit);
    }
}
