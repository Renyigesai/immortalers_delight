package com.renyigesai.immortalers_delight.potion;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.event.DifficultyModeHelper;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import vectorwing.farmersdelight.common.registry.ModEffects;


@Mod.EventBusSubscriber
public class DamageResistPotionEffect {
    @SubscribeEvent
    public static void onCreatureHurt(LivingHurtEvent evt) {
        if (evt.isCanceled() || evt.getSource().is(DamageTypeTags.BYPASSES_RESISTANCE)) {
            return;
        }
        boolean isPowerful = DifficultyModeHelper.isPowerBattleMode();
        LivingEntity hurtOne = evt.getEntity();
        LivingEntity attacker = null;
        if (evt.getSource().getEntity() instanceof LivingEntity livingEntity){
            attacker = livingEntity;
        }

        if (!hurtOne.level().isClientSide) {
            if (attacker != null){
                //System.out.println("超凡模式启动了吗？" + isPowerful );
                float damage = evt.getAmount();
                if (attacker.getMobType() == MobType.UNDEAD && hurtOne.hasEffect(ImmortalersDelightMobEffect.RESISTANCE_TO_UNDEAD.get())){
                    int lv = hurtOne.hasEffect(ImmortalersDelightMobEffect.RESISTANCE_TO_UNDEAD.get())?hurtOne.getEffect(ImmortalersDelightMobEffect.RESISTANCE_TO_UNDEAD.get()).getAmplifier():0;
                    damage = damage / (lv + 2);
                    if (isPowerful) hurtOne.addEffect(new MobEffectInstance(ImmortalersDelightMobEffect.VITALITY.get(),40,lv));
                }
                else if (attacker.getMobType() == MobType.ARTHROPOD && hurtOne.hasEffect(ImmortalersDelightMobEffect.RESISTANCE_TO_ARTHROPOD.get())){
                    int lv = hurtOne.hasEffect(ImmortalersDelightMobEffect.RESISTANCE_TO_ARTHROPOD.get())?hurtOne.getEffect(ImmortalersDelightMobEffect.RESISTANCE_TO_ARTHROPOD.get()).getAmplifier():0;
                    damage = damage / (lv + 2);
                    hurtOne.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED,(lv + 2)*10,3));
                    if (isPowerful) hurtOne.addEffect(new MobEffectInstance(ImmortalersDelightMobEffect.RELIEVE_POISON.get(),(lv + 2)*10,3));
                }
                else if (attacker.getMobType() == MobType.WATER && hurtOne.hasEffect(ImmortalersDelightMobEffect.RESISTANCE_TO_ABYSSAL.get())){
                    int lv = hurtOne.hasEffect(ImmortalersDelightMobEffect.RESISTANCE_TO_ABYSSAL.get())?hurtOne.getEffect(ImmortalersDelightMobEffect.RESISTANCE_TO_ABYSSAL.get()).getAmplifier():0;
                    damage = damage / (lv + 2);
                    MobEffectInstance oxygenBuff = new MobEffectInstance(isPowerful ? MobEffects.CONDUIT_POWER : MobEffects.WATER_BREATHING,(lv + 2)*40,lv);
                    hurtOne.addEffect(oxygenBuff);
                    MobEffectInstance healBuff =isPowerful ? new MobEffectInstance(MobEffects.HEAL,1, lv) : new MobEffectInstance(ModEffects.COMFORT.get(),52,lv);
                    hurtOne.addEffect(healBuff);
                    hurtOne.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE,(lv + 2)*40,lv));
                }
                else if (attacker.getMobType() == MobType.ILLAGER && hurtOne.hasEffect(ImmortalersDelightMobEffect.RESISTANCE_TO_ILLAGER.get())){
                    int lv = hurtOne.hasEffect(ImmortalersDelightMobEffect.RESISTANCE_TO_ILLAGER.get())?hurtOne.getEffect(ImmortalersDelightMobEffect.RESISTANCE_TO_ILLAGER.get()).getAmplifier():0;
                    damage = damage / (lv + 2);
                    if (isPowerful) hurtOne.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,(lv + 2)*(lv > 3 ? 10 : 40),lv));
                }
                evt.setAmount(Math.max(damage, 0f));
            } else if (hurtOne.hasEffect(ImmortalersDelightMobEffect.RESISTANCE_TO_SURROUNDINGS.get())) {
                int lv = hurtOne.hasEffect(ImmortalersDelightMobEffect.RESISTANCE_TO_SURROUNDINGS.get())?hurtOne.getEffect(ImmortalersDelightMobEffect.RESISTANCE_TO_SURROUNDINGS.get()).getAmplifier():0;
                int buffer = 2 << lv;
                if (isPowerful) {
                    evt.setAmount(Math.max(evt.getAmount() - (lv + 2), 0f));
                } else evt.setAmount(Math.max(evt.getAmount() - 0.4F - 0.15F * buffer, 0f));
            }
        }
    }
}
