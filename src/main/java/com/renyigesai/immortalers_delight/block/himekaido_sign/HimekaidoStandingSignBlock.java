package com.renyigesai.immortalers_delight.block.himekaido_sign;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;

public class HimekaidoStandingSignBlock extends StandingSignBlock {


    public HimekaidoStandingSignBlock(Properties p_56273_, WoodType p_56274_) {
        super(p_56273_, p_56274_);
    }
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new HimekaidoSignBlockEntity(blockPos, blockState);
    }

    @Override
    public float getYRotationDegrees(BlockState blockState) {
        return  RotationSegment.convertToDegrees(blockState.getValue(ROTATION));
    }

}