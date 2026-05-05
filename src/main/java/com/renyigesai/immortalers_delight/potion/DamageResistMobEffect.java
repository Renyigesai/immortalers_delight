package com.renyigesai.immortalers_delight.potion;
import net.neoforged.fml.common.EventBusSubscriber;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import vectorwing.farmersdelight.common.registry.ModEffects;

public class DamageResistMobEffect extends BaseMobEffect {

    public DamageResistMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 8007740);
    }

    @Override
    public boolean isDurationEffectTickInControl(int duration, int amplifier) {
        return true;
    }

    @EventBusSubscriber(
            modid = ImmortalersDelightMod.MODID)
    public static class DamageResistPotionEffect {
        @SubscribeEvent
        public static void onCreatureHurt(LivingDamageEvent.Pre evt) {
            if (evt.getSource().is(DamageTypeTags.BYPASSES_RESISTANCE)) {
                return;
            }
            boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
            LivingEntity hurtOne = evt.getEntity();
            LivingEntity attacker = null;
            if (evt.getSource().getEntity() instanceof LivingEntity livingEntity){
                attacker = livingEntity;
            }

            if (!hurtOne.level().isClientSide) {
                if (attacker != null){
                    //System.out.println("超凡模式启动了吗？" + isPowerful );
                    float damage = evt.getNewDamage();
                    if (attacker.getType().is(EntityTypeTags.UNDEAD)){
                        MobEffectInstance thisEffect = hurtOne.getEffect(ImmortalersDelightMobEffect.RESISTANCE_TO_UNDEAD);
                        if (thisEffect != null && thisEffect.getEffect() instanceof BaseMobEffect baseMobEffect) {
                            int lv = baseMobEffect.getTruthUsingAmplifier(thisEffect.getAmplifier());
                            damage = damage / (lv + 2);
                            if (isPowerful) hurtOne.addEffect(new MobEffectInstance(ImmortalersDelightMobEffect.VITALITY,40,thisEffect.getAmplifier()));
                        }
                    }
                    else if (attacker.getType().is(EntityTypeTags.ARTHROPOD)){
                        MobEffectInstance thisEffect = hurtOne.getEffect(ImmortalersDelightMobEffect.RESISTANCE_TO_ARTHROPOD);
                        if (thisEffect != null && thisEffect.getEffect() instanceof BaseMobEffect baseMobEffect) {
                            int lv = baseMobEffect.getTruthUsingAmplifier(thisEffect.getAmplifier());
                            damage = damage / (lv + 2);
                            hurtOne.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED,(thisEffect.getAmplifier() + 2)*10,3));
                            if (isPowerful) hurtOne.addEffect(new MobEffectInstance(ImmortalersDelightMobEffect.RELIEVE_POISON,(thisEffect.getAmplifier() + 2)*10,3));
                        }
                    }
                    else if (attacker.getType().is(EntityTypeTags.AQUATIC)){
                        MobEffectInstance thisEffect = hurtOne.getEffect(ImmortalersDelightMobEffect.RESISTANCE_TO_ABYSSAL);
                        if (thisEffect != null && thisEffect.getEffect() instanceof BaseMobEffect baseMobEffect) {
                            int lv = baseMobEffect.getTruthUsingAmplifier(thisEffect.getAmplifier());
                            damage = damage / (lv + 2);
                            MobEffectInstance oxygenBuff = new MobEffectInstance(isPowerful ? MobEffects.CONDUIT_POWER : MobEffects.WATER_BREATHING,(thisEffect.getAmplifier() + 2)*40,thisEffect.getAmplifier());
                            hurtOne.addEffect(oxygenBuff);
                            MobEffectInstance healBuff =isPowerful ? new MobEffectInstance(MobEffects.HEAL,1, thisEffect.getAmplifier()) : new MobEffectInstance(ModEffects.COMFORT,52,thisEffect.getAmplifier());
                            hurtOne.addEffect(healBuff);
                            hurtOne.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE,(thisEffect.getAmplifier() + 2)*40,thisEffect.getAmplifier()));
                        }
                    }
                    else if (attacker.getType().is(EntityTypeTags.ILLAGER)){
                        MobEffectInstance thisEffect = hurtOne.getEffect(ImmortalersDelightMobEffect.RESISTANCE_TO_ILLAGER);
                        if (thisEffect != null && thisEffect.getEffect() instanceof BaseMobEffect baseMobEffect) {
                            int lv = baseMobEffect.getTruthUsingAmplifier(thisEffect.getAmplifier());
                            damage = damage / (lv + 2);
                            if (isPowerful) hurtOne.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,(thisEffect.getAmplifier() + 2)*(thisEffect.getAmplifier() > 3 ? 10 : 40),thisEffect.getAmplifier()));
                        }
                    }
                    evt.setNewDamage(Math.max(damage, 0f));
                } else {
                    MobEffectInstance thisEffect = hurtOne.getEffect(ImmortalersDelightMobEffect.RESISTANCE_TO_SURROUNDINGS);
                    if (thisEffect != null && thisEffect.getEffect() instanceof BaseMobEffect baseMobEffect) {
                        int lv = baseMobEffect.getTruthUsingAmplifier(thisEffect.getAmplifier());
                        int buffer = 2 << lv;
                        if (isPowerful) {
                            evt.setNewDamage(Math.max(evt.getNewDamage() - (lv + 2), 0f));
                        } else evt.setNewDamage(Math.max(evt.getNewDamage() - 0.4F - 0.15F * buffer, 0f));
                    }
                }
            }
        }
    }
}
