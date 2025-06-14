package com.renyigesai.immortalers_delight.potion;

import com.renyigesai.immortalers_delight.Config;
import com.renyigesai.immortalers_delight.event.DifficultyModeHelper;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.util.EffectUtils;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber
public class MagicalReversePotionEffect {
    public static Map<MobEffect,MobEffect> reverseInstantEffect = new HashMap<MobEffect,MobEffect>();
    @SubscribeEvent
    public static void onEntityAddEffect(MobEffectEvent.Applicable event) {
        if (event != null && event.getEntity() != null) {
            Entity entity = event.getEntity();
            if (entity instanceof LivingEntity livingEntity) {
                if (livingEntity.hasEffect(ImmortalersDelightMobEffect.MAGICAL_REVERSE.get())) {
                    if (event.getEffectInstance().getEffect() == ImmortalersDelightMobEffect.MAGICAL_REVERSE.get()) {
                        event.setResult(Event.Result.DENY);
                    }
                    if (reverseInstantEffect.get(event.getEffectInstance().getEffect()) != null) {
                        boolean isPowerful = DifficultyModeHelper.isPowerBattleMode();
                        int lv = event.getEffectInstance().getAmplifier();
                        int time = event.getEffectInstance().getDuration();
                        int amplifier = livingEntity.getEffect(ImmortalersDelightMobEffect.MAGICAL_REVERSE.get()).getAmplifier();
                        int tureTime = time > (3000 << amplifier) ? (3000 << amplifier) : time;
                        int tureLv = lv > amplifier ? amplifier : lv;
                        if (isPowerful) livingEntity.addEffect(new MobEffectInstance(reverseInstantEffect.get(event.getEffectInstance().getEffect()), time, lv));
                        if (!isPowerful) livingEntity.addEffect(new MobEffectInstance(reverseInstantEffect.get(event.getEffectInstance().getEffect()), tureTime, tureLv));
                        event.setResult(Event.Result.DENY);
                    }
                } else if (event.getEffectInstance().getEffect() == ImmortalersDelightMobEffect.MAGICAL_REVERSE.get()) {
                    MagicalReverseMobEffect.reverseNormalEffect = EffectUtils.get0therModMobEffect(Config.REVERSE_NORMAL_EFFECT.get());
                    reverseInstantEffect = EffectUtils.get0therModMobEffect(Config.REVERSE_INSTANT_EFFECT.get());
                }

            }
        }
    }
}
