package com.renyigesai.immortalers_delight.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TBrushableBlockEntity extends BrushableBlockEntity {
    public TBrushableBlockEntity(BlockEntityType<?> entityType, BlockPos pPos, BlockState pBlockState) {
        super(pPos, pBlockState);
        this.type = entityType;
    }

}
