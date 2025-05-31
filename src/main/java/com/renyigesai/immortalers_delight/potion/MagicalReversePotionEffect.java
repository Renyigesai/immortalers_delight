package com.renyigesai.immortalers_delight.potion;

import com.renyigesai.immortalers_delight.event.DifficultyModeHelper;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class MagicalReversePotionEffect {
    @SubscribeEvent
    public static void onEntityAddEffect(MobEffectEvent.Applicable event) {
        if (event != null && event.getEntity() != null) {
            Entity entity = event.getEntity();
            if (entity instanceof LivingEntity livingEntity
                    && livingEntity.hasEffect(ImmortalersDelightMobEffect.MAGICAL_REVERSE.get())) {
                if (event.getEffectInstance().getEffect() == ImmortalersDelightMobEffect.MAGICAL_REVERSE.get()) {
                    event.setResult(Event.Result.DENY);
                }
                if (event.getEffectInstance().getEffect() == MobEffects.HARM) {
                    int lv = event.getEffectInstance().getAmplifier();
                    int time = event.getEffectInstance().getDuration();
                    if (DifficultyModeHelper.isPowerBattleMode()) {
                        lv++;
                        livingEntity.addEffect(new MobEffectInstance(MobEffects.HEAL, lv * time, time));
                    } else livingEntity.addEffect(new MobEffectInstance(MobEffects.HEAL, time, lv));
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }
}
