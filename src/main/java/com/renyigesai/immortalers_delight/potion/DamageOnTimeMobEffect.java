package com.renyigesai.immortalers_delight.potion;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;

import java.util.Objects;

import static com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect.WEAK_WITHER;
import static com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect.WEAK_POISON;

public class DamageOnTimeMobEffect extends MobEffect {


    public DamageOnTimeMobEffect() {
        super(MobEffectCategory.HARMFUL, -39424);
    }

    @Override
    public void applyEffectTick(LivingEntity pEntity, int amplifier) {
        if (this == WEAK_POISON.get()) {
            if (pEntity.hasEffect(MobEffects.POISON)) {
                int lv = pEntity.hasEffect(MobEffects.POISON)? Objects.requireNonNull(pEntity.getEffect(MobEffects.POISON)).getAmplifier():0;
                if (lv > amplifier) {
                    pEntity.removeEffect(WEAK_POISON.get());
                } else pEntity.removeEffect(MobEffects.POISON);
            }else if (pEntity.getHealth() > pEntity.getMaxHealth() * 0.5 && pEntity.getMobType() != MobType.UNDEAD) {
                float damage = 1 << amplifier;
                if (damage >= pEntity.getMaxHealth() * 0.5) {
                    pEntity.setHealth((float) (pEntity.getMaxHealth() * 0.5));
                }else pEntity.hurt(pEntity.damageSources().magic(), damage);
            }
        }
        if (this == WEAK_WITHER.get()) {
            if (pEntity.hasEffect(MobEffects.WITHER)) {
                int lv = pEntity.hasEffect(MobEffects.WITHER)? Objects.requireNonNull(pEntity.getEffect(MobEffects.WITHER)).getAmplifier():0;
                if (lv > amplifier) {
                    pEntity.removeEffect(WEAK_WITHER.get());
                } else pEntity.removeEffect(MobEffects.WITHER);
            }else if (pEntity.getHealth() > 1.0F) {
                float damage = 1 << amplifier;
                if (damage >= pEntity.getHealth()) {
                    pEntity.setHealth(1.0F);
                }else pEntity.hurt(pEntity.damageSources().wither(), damage);
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
