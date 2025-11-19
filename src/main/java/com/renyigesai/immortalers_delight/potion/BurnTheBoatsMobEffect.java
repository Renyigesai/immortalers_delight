package com.renyigesai.immortalers_delight.potion;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class BurnTheBoatsMobEffect extends MobEffect {
    public BurnTheBoatsMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 11829585);
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
