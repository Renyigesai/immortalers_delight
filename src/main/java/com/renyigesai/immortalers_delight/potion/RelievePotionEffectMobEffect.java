package com.renyigesai.immortalers_delight.potion;

import com.google.common.collect.ImmutableMap;
import com.renyigesai.immortalers_delight.event.DifficultyModeHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import vectorwing.farmersdelight.common.registry.ModEffects;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import static com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect.*;

public class RelievePotionEffectMobEffect extends MobEffect {

    public RelievePotionEffectMobEffect() {
        super(MobEffectCategory.BENEFICIAL, -39424);
    }

    @Override
    public void applyEffectTick(LivingEntity pEntity, int amplifier) {
        boolean isPowerful = DifficultyModeHelper.isPowerBattleMode();
        if (this == RELIEVE_POISON.get()) {
            if (pEntity.hasEffect(MobEffects.POISON)){
                if (!isPowerful) {
                    int lv = pEntity.hasEffect(MobEffects.POISON)? Objects.requireNonNull(pEntity.getEffect(MobEffects.POISON)).getAmplifier():0;
                /*
                解毒效果可以解掉相当于其等级的中毒，对更高等级的中毒会转化为减去解毒效果等级的弱毒
                 */
                    if (lv > amplifier) {
                        int time = pEntity.hasEffect(MobEffects.POISON)? Objects.requireNonNull(pEntity.getEffect(MobEffects.POISON)).getDuration():0;
                        MobEffectInstance weakPoison = new MobEffectInstance(
                                ImmortalersDelightMobEffect.WEAK_POISON.get(),time,lv - amplifier);
                        pEntity.addEffect(weakPoison);
                    }
                }
                pEntity.removeEffect(MobEffects.POISON);
            }
            if (isPowerful && pEntity.hasEffect(WEAK_POISON.get())) {
                pEntity.removeEffect(WEAK_POISON.get());
            }
            if (pEntity.hasEffect(MobEffects.WITHER)){
                int lv = pEntity.hasEffect(MobEffects.WITHER)? Objects.requireNonNull(pEntity.getEffect(MobEffects.WITHER)).getAmplifier():0;
                int time = pEntity.hasEffect(MobEffects.WITHER)? Objects.requireNonNull(pEntity.getEffect(MobEffects.WITHER)).getDuration():0;
                /*
                高等级的解毒效果会缩短弱凋零的时间
                 */
                pEntity.removeEffect(MobEffects.WITHER);
                if (isPowerful) {
                    time /= (amplifier + 1);
                    lv = lv - amplifier > 0 ? lv - amplifier : 0;
                }
                MobEffectInstance weakWither = new MobEffectInstance(
                        ImmortalersDelightMobEffect.WEAK_WITHER.get(),time,lv);
                pEntity.addEffect(weakWither);
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
