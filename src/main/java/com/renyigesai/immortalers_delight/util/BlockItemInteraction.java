package com.renyigesai.immortalers_delight.util;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.level.Level;

public final class BlockItemInteraction {
    private BlockItemInteraction() {}

    public static ItemInteractionResult from(Level level, InteractionResult result) {
        if (result == InteractionResult.SUCCESS) {
            return ItemInteractionResult.sidedSuccess(level.isClientSide());
        }
        if (result == InteractionResult.CONSUME) {
            return ItemInteractionResult.CONSUME;
        }
        if (result == InteractionResult.FAIL) {
            return ItemInteractionResult.FAIL;
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
}
