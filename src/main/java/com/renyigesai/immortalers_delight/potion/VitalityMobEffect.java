package com.renyigesai.immortalers_delight.potion;

import com.renyigesai.immortalers_delight.event.DifficultyModeHelper;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;

public class VitalityMobEffect extends MobEffect {
    public VitalityMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 11325584);
    }

    @Override
    public void applyEffectTick(LivingEntity pEntity, int amplifier) {
        if (!pEntity.level().isClientSide){
            if (!(pEntity.getHealth() > 0.0F && pEntity.getHealth() < pEntity.getMaxHealth())) return;
            float healthProgress = pEntity.getHealth() / pEntity.getMaxHealth();
            boolean isPowerful = DifficultyModeHelper.isPowerBattleMode();
            if (isPowerful) {
                float buffer = pEntity.getMaxHealth() * 0.005F;
                pEntity.heal(healthProgress * healthProgress * (1 << amplifier) * buffer);
            } else pEntity.heal(healthProgress * healthProgress * (amplifier + 1));
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }
}
