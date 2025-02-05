package com.renyigesai.immortalers_delight.potion;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;

@Mod.EventBusSubscriber
public class DamageResistPotionEffect {
    @SubscribeEvent
    public static void onCreatureHurt(LivingHurtEvent evt) {
        LivingEntity hurtOne = evt.getEntity();
        LivingEntity attacker = (LivingEntity) evt.getSource().getEntity();
        if (evt.isCanceled() || evt.getSource().is(DamageTypeTags.BYPASSES_RESISTANCE)) {
            return;
        }

        if (attacker != null) {
            if (!hurtOne.level().isClientSide){
                float damage = evt.getAmount();
                if (attacker.getMobType() == MobType.UNDEAD && hurtOne.hasEffect(ImmortalersDelightMobEffect.RESISTANCE_TO_UNDEAD.get())){
                    int lv = hurtOne.hasEffect(ImmortalersDelightMobEffect.RESISTANCE_TO_UNDEAD.get())?hurtOne.getEffect(ImmortalersDelightMobEffect.RESISTANCE_TO_UNDEAD.get()).getAmplifier():0;
                    float f = lv == 0 ? 0.25F : (float)((lv*2-1)/(lv*2)) ;
                    damage *= 1 - f;
                }
                else if (attacker.getMobType() == MobType.ARTHROPOD && hurtOne.hasEffect(ImmortalersDelightMobEffect.RESISTANCE_TO_ARTHROPOD.get())){
                    int lv = hurtOne.hasEffect(ImmortalersDelightMobEffect.RESISTANCE_TO_ARTHROPOD.get())?hurtOne.getEffect(ImmortalersDelightMobEffect.RESISTANCE_TO_ARTHROPOD.get()).getAmplifier():0;
                    float f = lv == 0 ? 0.25F : (float)((lv*2-1)/(lv*2)) ;
                    damage *= 1 - f;
                    MobEffectInstance speed = new MobEffectInstance(MobEffects.MOVEMENT_SPEED,(lv + 1)*10,3);
                    hurtOne.addEffect(speed);
                }
                else if (attacker.getMobType() == MobType.WATER && hurtOne.hasEffect(ImmortalersDelightMobEffect.RESISTANCE_TO_ABYSSAL.get())){
                    int lv = hurtOne.hasEffect(ImmortalersDelightMobEffect.RESISTANCE_TO_ABYSSAL.get())?hurtOne.getEffect(ImmortalersDelightMobEffect.RESISTANCE_TO_ABYSSAL.get()).getAmplifier():0;
                    float f = lv == 0 ? 0.25F : (float)((lv*2-1)/(lv*2)) ;
                    damage *= 1 - f;
                    MobEffectInstance conduitPower = new MobEffectInstance(lv > 1 ? MobEffects.CONDUIT_POWER : MobEffects.WATER_BREATHING,(lv + 1)*10,0);
                    hurtOne.addEffect(conduitPower);
                    MobEffectInstance swingSpeed = new MobEffectInstance(MobEffects.DOLPHINS_GRACE,(lv + 2)*10,lv);
                    hurtOne.addEffect(swingSpeed);
                }
                else if (attacker.getMobType() == MobType.ILLAGER && hurtOne.hasEffect(ImmortalersDelightMobEffect.RESISTANCE_TO_ILLAGER.get())){
                    int lv = hurtOne.hasEffect(ImmortalersDelightMobEffect.RESISTANCE_TO_ILLAGER.get())?hurtOne.getEffect(ImmortalersDelightMobEffect.RESISTANCE_TO_ILLAGER.get()).getAmplifier():0;
                    float f = lv == 0 ? 0.25F : (float)((lv*2-1)/(lv*2)) ;
                    damage *= 1 - f;
                }
                evt.setAmount(Math.max(damage, 0f));
            }
        }
    }
}
