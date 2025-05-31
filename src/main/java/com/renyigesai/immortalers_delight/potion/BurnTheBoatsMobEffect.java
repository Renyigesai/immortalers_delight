package com.renyigesai.immortalers_delight.potion;

import com.renyigesai.immortalers_delight.event.DifficultyModeHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class BurnTheBoatsMobEffect extends MobEffect {
    public static boolean isPowerful = DifficultyModeHelper.isPowerBattleMode();
    public BurnTheBoatsMobEffect() {
        super(MobEffectCategory.BENEFICIAL, -39424);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }



//    @Override
//    protected String getOrCreateDescriptionId() {
//        return "test";
//    }
}
