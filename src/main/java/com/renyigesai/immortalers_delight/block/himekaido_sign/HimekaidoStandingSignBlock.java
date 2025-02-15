package com.renyigesai.immortalers_delight.block.himekaido_sign;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.WoodType;

public class HimekaidoStandingSignBlock extends StandingSignBlock {


    public HimekaidoStandingSignBlock(Properties p_56273_, WoodType p_56274_) {
        super(p_56273_, p_56274_);
    }
    @Override
    public BlockEntity newBlockEntity(BlockPos p_154556_, BlockState p_154557_) {
        return new HimekaidoSignBlockEntity(p_154556_, p_154557_);
    }

    @Override
    public float getYRotationDegrees(BlockState p_277705_) {
        return 0;
    }

}