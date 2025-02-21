package com.renyigesai.immortalers_delight.block.himekaido_sign;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;

public class HimekaidoSignBlockEntity extends SignBlockEntity {
    public HimekaidoSignBlockEntity(BlockPos pos, BlockState state) {
        this(ImmortalersDelightBlockEntityTypes.SIGN.get(), pos, state);
    }
    public HimekaidoSignBlockEntity(BlockEntityType blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
}