package com.renyigesai.immortalers_delight.potion;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class EsteemedGuestMobEffect extends MobEffect {
    public EsteemedGuestMobEffect() {
        super(MobEffectCategory.HARMFUL, -39424);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
            return true;
    }
}
