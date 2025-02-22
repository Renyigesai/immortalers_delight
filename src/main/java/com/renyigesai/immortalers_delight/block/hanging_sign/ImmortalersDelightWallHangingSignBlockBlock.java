package com.renyigesai.immortalers_delight.block.hanging_sign;

import com.renyigesai.immortalers_delight.block.sign.ImmortalersDelightSignBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.level.block.state.properties.WoodType;

public class ImmortalersDelightWallHangingSignBlockBlock extends WallHangingSignBlock {


    public ImmortalersDelightWallHangingSignBlockBlock(Properties pProperties, WoodType pType) {
        super(pProperties, pType);
    }
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ImmortalersDelightHangingSignBlockEntity(pPos, pState);
    }
}