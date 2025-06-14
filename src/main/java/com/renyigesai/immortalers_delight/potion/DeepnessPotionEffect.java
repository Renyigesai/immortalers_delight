package com.renyigesai.immortalers_delight.potion;

import com.renyigesai.immortalers_delight.event.DifficultyModeHelper;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class DeepnessPotionEffect {
    @SubscribeEvent
    public static void onCreatureHurt(LivingHurtEvent evt) {
        if (evt.isCanceled() || evt.getSource().is(DamageTypeTags.BYPASSES_RESISTANCE)) {
            return;
        }
        LivingEntity hurtOne = evt.getEntity();
        LivingEntity attacker = null;
        if (evt.getSource().getEntity() instanceof LivingEntity livingEntity){
            attacker = livingEntity;
        }

        if (!hurtOne.level().isClientSide) {
            if (attacker != null){
                float damage = evt.getAmount();
                if (hurtOne.hasEffect(ImmortalersDelightMobEffect.DEEPNESS.get())){
                    int lv = hurtOne.hasEffect(ImmortalersDelightMobEffect.DEEPNESS.get())?hurtOne.getEffect(ImmortalersDelightMobEffect.DEEPNESS.get()).getAmplifier():0;
                    attacker.addEffect(new MobEffectInstance(MobEffects.WEAKNESS,40 + lv * 20,lv));
                    if (DifficultyModeHelper.isPowerBattleMode() && attacker.hasEffect(MobEffects.WEAKNESS)) {
                        lv++;
                        float buffer = 1.0F;
                        for (int i = 0; i < lv; i++) {
                            buffer  = buffer * 0.8F;
                        }
                        evt.setAmount(Math.min(damage*buffer, damage));
                    }
                }
            }
        }
    }
}
