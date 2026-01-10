package com.renyigesai.immortalers_delight.potion;

import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class VitalityMobEffect extends MobEffect {
    public VitalityMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 11325584);
    }

    @Override
    public void applyEffectTick(LivingEntity pEntity, int amplifier) {
        if (!pEntity.level().isClientSide){
            if (!(pEntity.getHealth() > 0.0F && pEntity.getHealth() < pEntity.getMaxHealth())) return;
            //计算生命值百分比
            float healthProgress = pEntity.getHealth() / pEntity.getMaxHealth();
            boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
            float healAmount = healthProgress * healthProgress;
            pEntity.heal(healAmount * (isPowerful ? 0.9f + pEntity.getMaxHealth() * 0.005f : 1));
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        int i = 20 >> amplifier;
        if (i > 0) {
            return duration % i == 0;
        } else {
            return true;
        }
    }
}
