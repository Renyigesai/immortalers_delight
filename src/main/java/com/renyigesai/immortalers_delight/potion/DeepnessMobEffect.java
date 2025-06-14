package com.renyigesai.immortalers_delight.potion;

import com.renyigesai.immortalers_delight.event.DifficultyModeHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;

import java.util.Objects;

import static com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect.*;
import static com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect.WEAK_WITHER;

public class DeepnessMobEffect extends MobEffect {
    public DeepnessMobEffect() {
        super(MobEffectCategory.BENEFICIAL, -39424);
    }

    @Override
    public void applyEffectTick(LivingEntity pEntity, int amplifier) {
        if (DifficultyModeHelper.isPowerBattleMode() && !pEntity.hasEffect(MobEffects.WEAKNESS)) {
            float buffer = (float) (0.9675*Math.exp(-0.0372*(amplifier+1)));
            pEntity.heal(pEntity.getMaxHealth() * (1 - buffer) * 0.1f);
        }
    }
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration  % 20 == 0;
    }
}
