package com.renyigesai.immortalers_delight.potion;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class BurnTheBoatsMobEffect extends MobEffect {

    public BurnTheBoatsMobEffect() {
        super(MobEffectCategory.BENEFICIAL, -39424);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
