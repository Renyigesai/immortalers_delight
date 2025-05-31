package com.renyigesai.immortalers_delight.potion;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;

import java.util.Objects;

import static com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect.*;

public class DamageOnTimeMobEffect extends MobEffect {


    public DamageOnTimeMobEffect() {
        super(MobEffectCategory.HARMFUL, -39424);
    }

    @Override
    public void applyEffectTick(LivingEntity pEntity, int amplifier) {
        if (this == WEAK_POISON.get()) {
            float minHealth = pEntity.hasEffect(INEBRIATED.get()) ? 1.0F : pEntity.getMaxHealth() * 0.5F;
            if (pEntity.hasEffect(MobEffects.POISON)) {
                int lv = pEntity.hasEffect(MobEffects.POISON)? Objects.requireNonNull(pEntity.getEffect(MobEffects.POISON)).getAmplifier():0;
                if (lv > amplifier) {
                    pEntity.removeEffect(WEAK_POISON.get());
                } else pEntity.removeEffect(MobEffects.POISON);
            }else if (pEntity.getHealth() > minHealth && pEntity.getMobType() != MobType.UNDEAD) {
                float damage = 1 << amplifier;
                if (damage > pEntity.getHealth() - minHealth) {damage = pEntity.getHealth() - minHealth;}
                pEntity.hurt(pEntity.damageSources().magic(), damage);
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
