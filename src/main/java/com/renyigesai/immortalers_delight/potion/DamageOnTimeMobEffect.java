package com.renyigesai.immortalers_delight.potion;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.tags.EntityTypeTags;

import java.util.Objects;

public class DamageOnTimeMobEffect extends BaseMobEffect {


    public DamageOnTimeMobEffect() {
        super(MobEffectCategory.HARMFUL, -39424);
    }

    @Override
    public void applyEffectTickInControl(LivingEntity pEntity, int amplifier) {
        if (this == ImmortalersDelightMobEffect.WEAK_POISON.get()) {
            float minHealth = pEntity.hasEffect(ImmortalersDelightMobEffect.INEBRIATED) ? 1.0F : pEntity.getMaxHealth() * 0.5F;
            if (pEntity.hasEffect(MobEffects.POISON)) {
                int lv = pEntity.hasEffect(MobEffects.POISON)? Objects.requireNonNull(pEntity.getEffect(MobEffects.POISON)).getAmplifier():0;
                if (lv > amplifier) {
                    pEntity.removeEffect(ImmortalersDelightMobEffect.WEAK_POISON);
                } else pEntity.removeEffect(MobEffects.POISON);
            }else if (pEntity.getHealth() > minHealth && !pEntity.getType().is(EntityTypeTags.UNDEAD)) {
                float damage = 1 << amplifier;
                if (damage > pEntity.getHealth() - minHealth) {damage = pEntity.getHealth() - minHealth;}
                pEntity.hurt(pEntity.damageSources().magic(), pEntity.hasEffect(ImmortalersDelightMobEffect.INEBRIATED) ? 1.6f * damage : damage);
            }
        }
        if (this == ImmortalersDelightMobEffect.WEAK_WITHER.get()) {
            if (pEntity.hasEffect(MobEffects.WITHER)) {
                int lv = pEntity.hasEffect(MobEffects.WITHER)? Objects.requireNonNull(pEntity.getEffect(MobEffects.WITHER)).getAmplifier():0;
                if (lv > amplifier) {
                    pEntity.removeEffect(ImmortalersDelightMobEffect.WEAK_WITHER);
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
    public boolean isDurationEffectTickInControl(int duration, int amplifier) {
        if (this == ImmortalersDelightMobEffect.WEAK_POISON.get()) {
            int j = 40 ;
            return duration % j == 0;
        }
        if (this == ImmortalersDelightMobEffect.WEAK_WITHER.get()) {
            int j = 50 ;
            return duration % j == 0;
        }
        return true;
    }
}
