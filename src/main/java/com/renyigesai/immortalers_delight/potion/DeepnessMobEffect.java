package com.renyigesai.immortalers_delight.potion;

import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class DeepnessMobEffect extends MobEffect {
    public DeepnessMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 955261);
    }

    @Override
    public void applyEffectTick(LivingEntity pEntity, int amplifier) {
        if (DifficultyModeUtil.isPowerBattleMode() && !pEntity.hasEffect(MobEffects.WEAKNESS)) {
            float buffer = (float) (0.9675*Math.exp(-0.0372*(amplifier+1)));
            pEntity.heal(pEntity.getMaxHealth() * (1 - buffer) * 0.1f);
        }
    }
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration  % 20 == 0;
    }
}
