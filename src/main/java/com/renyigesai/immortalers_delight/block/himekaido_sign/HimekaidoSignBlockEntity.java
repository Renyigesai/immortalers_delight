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
    public HimekaidoSignBlockEntity(BlockPos p_155700_, BlockState p_155701_) {
        this(ImmortalersDelightBlockEntityTypes.SIGN.get(), p_155700_, p_155701_);
    }
    public HimekaidoSignBlockEntity(BlockEntityType p_249609_, BlockPos p_248914_, BlockState p_249550_) {
        super(p_249609_, p_248914_, p_249550_);
    }
}