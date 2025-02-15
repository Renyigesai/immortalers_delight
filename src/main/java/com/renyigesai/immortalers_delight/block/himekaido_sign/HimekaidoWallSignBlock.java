package com.renyigesai.immortalers_delight.block.himekaido_sign;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.WoodType;

public class HimekaidoWallSignBlock extends WallSignBlock {
    public HimekaidoWallSignBlock(Properties p_58068_, WoodType p_58069_) {
        super(p_58068_, p_58069_);
    }
    public BlockEntity newBlockEntity(BlockPos p_154556_, BlockState p_154557_) {
        return new HimekaidoSignBlockEntity(p_154556_, p_154557_);
    }

    @Override
    public float getYRotationDegrees(BlockState p_277705_) {
        return 0;
    }

}
