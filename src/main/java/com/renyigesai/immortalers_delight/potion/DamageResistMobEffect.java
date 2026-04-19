package com.renyigesai.immortalers_delight.potion;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vectorwing.farmersdelight.common.registry.ModEffects;

public class DamageResistMobEffect extends BaseMobEffect {

    public DamageResistMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 8007740);
    }

    @Override
    public boolean isDurationEffectTickInControl(int duration, int amplifier) {
        return true;
    }

    @Mod.EventBusSubscriber(
            modid = ImmortalersDelightMod.MODID,
            bus = Mod.EventBusSubscriber.Bus.FORGE
    )
    public static class DamageResistPotionEffect {
        @SubscribeEvent
        public static void onCreatureHurt(LivingHurtEvent evt) {
            if (evt.isCanceled() || evt.getSource().is(DamageTypeTags.BYPASSES_RESISTANCE)) {
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
                    float damage = evt.getAmount();
                    if (attacker.getMobType() == MobType.UNDEAD){
                        MobEffectInstance thisEffect = hurtOne.getEffect(ImmortalersDelightMobEffect.RESISTANCE_TO_UNDEAD.get());
                        if (thisEffect != null && thisEffect.getEffect() instanceof BaseMobEffect baseMobEffect) {
                            int lv = baseMobEffect.getTruthUsingAmplifier(thisEffect.getAmplifier());
                            damage = damage / (lv + 2);
                            if (isPowerful) hurtOne.addEffect(new MobEffectInstance(ImmortalersDelightMobEffect.VITALITY.get(),40,thisEffect.getAmplifier()));
                        }
                    }
                    else if (attacker.getMobType() == MobType.ARTHROPOD){
                        MobEffectInstance thisEffect = hurtOne.getEffect(ImmortalersDelightMobEffect.RESISTANCE_TO_ARTHROPOD.get());
                        if (thisEffect != null && thisEffect.getEffect() instanceof BaseMobEffect baseMobEffect) {
                            int lv = baseMobEffect.getTruthUsingAmplifier(thisEffect.getAmplifier());
                            damage = damage / (lv + 2);
                            hurtOne.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED,(thisEffect.getAmplifier() + 2)*10,3));
                            if (isPowerful) hurtOne.addEffect(new MobEffectInstance(ImmortalersDelightMobEffect.RELIEVE_POISON.get(),(thisEffect.getAmplifier() + 2)*10,3));
                        }
                    }
                    else if (attacker.getMobType() == MobType.WATER){
                        MobEffectInstance thisEffect = hurtOne.getEffect(ImmortalersDelightMobEffect.RESISTANCE_TO_ABYSSAL.get());
                        if (thisEffect != null && thisEffect.getEffect() instanceof BaseMobEffect baseMobEffect) {
                            int lv = baseMobEffect.getTruthUsingAmplifier(thisEffect.getAmplifier());
                            damage = damage / (lv + 2);
                            MobEffectInstance oxygenBuff = new MobEffectInstance(isPowerful ? MobEffects.CONDUIT_POWER : MobEffects.WATER_BREATHING,(thisEffect.getAmplifier() + 2)*40,thisEffect.getAmplifier());
                            hurtOne.addEffect(oxygenBuff);
                            MobEffectInstance healBuff =isPowerful ? new MobEffectInstance(MobEffects.HEAL,1, thisEffect.getAmplifier()) : new MobEffectInstance(ModEffects.COMFORT.get(),52,thisEffect.getAmplifier());
                            hurtOne.addEffect(healBuff);
                            hurtOne.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE,(thisEffect.getAmplifier() + 2)*40,thisEffect.getAmplifier()));
                        }
                    }
                    else if (attacker.getMobType() == MobType.ILLAGER){
                        MobEffectInstance thisEffect = hurtOne.getEffect(ImmortalersDelightMobEffect.RESISTANCE_TO_ILLAGER.get());
                        if (thisEffect != null && thisEffect.getEffect() instanceof BaseMobEffect baseMobEffect) {
                            int lv = baseMobEffect.getTruthUsingAmplifier(thisEffect.getAmplifier());
                            damage = damage / (lv + 2);
                            if (isPowerful) hurtOne.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,(thisEffect.getAmplifier() + 2)*(thisEffect.getAmplifier() > 3 ? 10 : 40),thisEffect.getAmplifier()));
                        }
                    }
                    evt.setAmount(Math.max(damage, 0f));
                } else {
                    MobEffectInstance thisEffect = hurtOne.getEffect(ImmortalersDelightMobEffect.RESISTANCE_TO_SURROUNDINGS.get());
                    if (thisEffect != null && thisEffect.getEffect() instanceof BaseMobEffect baseMobEffect) {
                        int lv = baseMobEffect.getTruthUsingAmplifier(thisEffect.getAmplifier());
                        int buffer = 2 << lv;
                        if (isPowerful) {
                            evt.setAmount(Math.max(evt.getAmount() - (lv + 2), 0f));
                        } else evt.setAmount(Math.max(evt.getAmount() - 0.4F - 0.15F * buffer, 0f));
                    }
                }
            }
        }
    }
}
