package com.renyigesai.immortalers_delight.block.hanging_sign;

import com.renyigesai.immortalers_delight.block.sign.ImmortalersDelightSignBlockEntity;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ImmortalersDelightHangingSignBlockEntity extends SignBlockEntity {

    private static final int MAX_TEXT_LINE_WIDTH = 60;
    private static final int TEXT_LINE_HEIGHT = 9;

    public ImmortalersDelightHangingSignBlockEntity(BlockPos p_250603_, BlockState p_251674_) {
        super(ImmortalersDelightBlockEntityTypes.HANGING_SIGN.get(), p_250603_, p_251674_);
    }

    public int getTextLineHeight() {
        return 9;
    }

    public int getMaxTextLineWidth() {
        return 60;
    }
}
