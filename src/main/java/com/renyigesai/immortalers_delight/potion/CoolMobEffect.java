package com.renyigesai.immortalers_delight.potion;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class CoolMobEffect extends MobEffect {

    public CoolMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 7969524);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
