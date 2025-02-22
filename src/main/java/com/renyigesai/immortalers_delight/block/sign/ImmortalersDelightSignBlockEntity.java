package com.renyigesai.immortalers_delight.block.sign;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ImmortalersDelightSignBlockEntity extends SignBlockEntity {
    public ImmortalersDelightSignBlockEntity(BlockPos pos, BlockState state) {
        this(ImmortalersDelightBlockEntityTypes.HANGING_SIGN.get(), pos, state);
    }
    public ImmortalersDelightSignBlockEntity(BlockEntityType blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
}