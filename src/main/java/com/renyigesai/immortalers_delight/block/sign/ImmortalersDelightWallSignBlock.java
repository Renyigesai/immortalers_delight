package com.renyigesai.immortalers_delight.block.sign;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;

public class ImmortalersDelightWallSignBlock extends WallSignBlock {
    public ImmortalersDelightWallSignBlock(Properties p_58068_, WoodType p_58069_) {
        super(p_58068_, p_58069_);
    }
    public BlockEntity newBlockEntity(BlockPos p_154556_, BlockState p_154557_) {
        return new ImmortalersDelightSignBlockEntity(p_154556_, p_154557_);
    }

    @Override
    public float getYRotationDegrees(BlockState p_277705_) {
        return p_277705_.getValue(FACING).toYRot();
    }

}
