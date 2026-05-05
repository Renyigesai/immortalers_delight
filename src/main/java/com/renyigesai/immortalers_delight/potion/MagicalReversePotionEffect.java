package com.renyigesai.immortalers_delight.potion;
import net.neoforged.fml.common.EventBusSubscriber;

import com.renyigesai.immortalers_delight.Config;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.util.EffectUtils;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber
public class MagicalReversePotionEffect {
    public static Map<MobEffect,MobEffect> reverseInstantEffect = new HashMap<MobEffect,MobEffect>();
    @SubscribeEvent
    public static void onEntityAddEffect(MobEffectEvent.Applicable event) {
        if (event != null && event.getEntity() != null) {
            Entity entity = event.getEntity();
            if (entity instanceof LivingEntity livingEntity) {
                if (livingEntity.hasEffect(ImmortalersDelightMobEffect.MAGICAL_REVERSE)) {
                    Holder<MobEffect> adding = event.getEffectInstance().getEffect();
                    if (adding.is(ImmortalersDelightMobEffect.MAGICAL_REVERSE)) {
                        event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
                    }
                    MobEffect addingEffect = adding.value();
                    if (reverseInstantEffect.get(addingEffect) != null) {
                        boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
                        int lv = event.getEffectInstance().getAmplifier();
                        int time = event.getEffectInstance().getDuration();
                        int amplifier = livingEntity.getEffect(ImmortalersDelightMobEffect.MAGICAL_REVERSE).getAmplifier();
                        int tureTime = time > (3000 << amplifier) ? (3000 << amplifier) : time;
                        int tureLv = lv > amplifier ? amplifier : lv;
                        MobEffect reversed = reverseInstantEffect.get(addingEffect);
                        Holder<MobEffect> reversedHolder = Holder.direct(reversed);
                        if (isPowerful) livingEntity.addEffect(new MobEffectInstance(reversedHolder, time, lv));
                        if (!isPowerful) livingEntity.addEffect(new MobEffectInstance(reversedHolder, tureTime, tureLv));
                        event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
                    }
                } else if (event.getEffectInstance().getEffect().is(ImmortalersDelightMobEffect.MAGICAL_REVERSE)) {
                    updateReverseEffect();
                }

            }
        }
    }

    public static void updateReverseEffect() {

        MagicalReverseMobEffect.reverseNormalEffect = EffectUtils.get0therModMobEffect(Config.REVERSE_NORMAL_EFFECT.get());
        reverseInstantEffect = EffectUtils.get0therModMobEffect(Config.REVERSE_INSTANT_EFFECT.get());
    }
}
