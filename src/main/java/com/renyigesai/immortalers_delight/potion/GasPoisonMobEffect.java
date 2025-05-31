package com.renyigesai.immortalers_delight.potion;

import com.renyigesai.immortalers_delight.event.DifficultyModeHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

import static com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect.*;

public class GasPoisonMobEffect extends MobEffect {
    public GasPoisonMobEffect() {
        super(MobEffectCategory.HARMFUL, -39424);
    }
    @Override
    public void applyEffectTick(LivingEntity pEntity, int amplifier) {
        if (this == GAS_POISON.get() && !pEntity.level().isClientSide()) {
            boolean isPowerful = DifficultyModeHelper.isPowerBattleMode();
            float health = pEntity.getHealth();
            float damage = pEntity.getMaxHealth() * 0.06F > 1.2F ? pEntity.getMaxHealth() * 0.06F : 1.2F;
            pEntity.invulnerableTime = 0;
            pEntity.hurt(new DamageSource(pEntity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("immortalers_delight:gas")))), damage);
            pEntity.invulnerableTime = 0;
            if (isPowerful && (health - damage) > 0 && pEntity.getHealth() > (health - damage)) {
                pEntity.setHealth(health - damage);
            }
            int i = pEntity.getRandom().nextInt(5);
            switch (i) {
                case 0 -> pEntity.addEffect(new MobEffectInstance(MobEffects.HUNGER, 100, amplifier));
                case 1 -> pEntity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 400, amplifier));
                case 2 -> pEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 100, amplifier));
                case 3 -> pEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, amplifier));
                case 4 -> pEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, amplifier));
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        int j = 32 >> pAmplifier;
        if (j > 0) {
            return pDuration % j == 0;
        } else {
            return true;
        }
    }

}
