package com.renyigesai.immortalers_delight.potion;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

import static com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect.WEAK_WITHER;
import static com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect.WEAK_POISON;

public class DamageOnTimeMobEffect extends MobEffect {


    public DamageOnTimeMobEffect() {
        super(MobEffectCategory.HARMFUL, -39424);
    }

    @Override
    public void applyEffectTick(LivingEntity pEntity, int amplifier) {
        if (this == WEAK_POISON.get()) {
            if (pEntity.getHealth() > pEntity.getMaxHealth() * 0.5) {
                float damage = 1.0F;
                for (int i = 0; i< amplifier; i++) {
                    damage *= 2.0F;
                }
                if (damage >= pEntity.getMaxHealth() * 0.5) {
                    pEntity.setHealth((float) (pEntity.getMaxHealth() * 0.5));
                }else pEntity.hurt(pEntity.damageSources().magic(), damage);
            }
        }
        if (this == WEAK_WITHER.get()) {
            if (pEntity.getHealth() > 1.0F) {
                float damage = 1.0F;
                int exponent = amplifier;
                for (int i=0;i<exponent;i++) {
                    damage *= 2.0F;
                }
                if (damage >= pEntity.getHealth()) {
                    pEntity.setHealth(1.0F);
                }else pEntity.hurt(pEntity.damageSources().magic(), damage);
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        if (this == WEAK_POISON.get()) {
            int j = 40 ;
            return duration % j == 0;
        }
        if (this == WEAK_WITHER.get()) {
            int j = 50 ;
            return duration % j == 0;
        }
        return true;
    }
}
