package com.renyigesai.immortalers_delight.block.brushable;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SuspiciousAshPileBlockEntity extends ModBrushableBlockEntity{

    public SuspiciousAshPileBlockEntity(BlockEntityType<? extends ModBrushableBlockEntity> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    public SuspiciousAshPileBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ImmortalersDelightBlocks.SUSPICIOUS_ASH_PILE_BLOCK_ENTITY.get(), pPos, pBlockState);
    }
}
