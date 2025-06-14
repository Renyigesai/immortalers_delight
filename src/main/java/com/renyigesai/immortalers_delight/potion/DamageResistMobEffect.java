package com.renyigesai.immortalers_delight.potion;

import com.renyigesai.immortalers_delight.event.DifficultyModeHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class DamageResistMobEffect extends MobEffect {

    public DamageResistMobEffect() {
        super(MobEffectCategory.BENEFICIAL, -39424);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
