package com.renyigesai.immortalers_delight.potion;

import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect.*;
public class MagicalReverseMobEffect extends MobEffect {

    @Override
    public boolean isInstantenous() {
        return true;
    }

    public static Map<MobEffect,MobEffect> reverseNormalEffect = new HashMap<MobEffect,MobEffect>();
//            .put(MobEffects.BAD_OMEN,MobEffects.HERO_OF_THE_VILLAGE)
//            .put(MobEffects.UNLUCK,MobEffects.LUCK)
//            .put(MobEffects.GLOWING,MobEffects.INVISIBILITY)
//            .put(MobEffects.MOVEMENT_SLOWDOWN,MobEffects.MOVEMENT_SPEED)
//            .put(MobEffects.LEVITATION,MobEffects.SLOW_FALLING)
//            .put(MobEffects.BLINDNESS,MobEffects.NIGHT_VISION)
//            .put(MobEffects.DARKNESS,MobEffects.CONDUIT_POWER)
//            .put(MobEffects.DIG_SLOWDOWN,MobEffects.DIG_SPEED)
//            .put(MobEffects.WEAKNESS,MobEffects.DAMAGE_BOOST)
//            .put(MobEffects.POISON,FD_Comfit)
//            .put(MobEffects.WITHER,MobEffects.REGENERATION)
//            .put(MobEffects.HUNGER,FD_Nourished)
//            .put(MobEffects.CONFUSION, KEEP_A_FAST.get())
//            .build();
    public MagicalReverseMobEffect() {
        super(MobEffectCategory.BENEFICIAL, -39424);
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity pEntity, int amplifier) {
        /*
        下面是金魔法果反转效果的实现
         */
        if (this == MAGICAL_REVERSE.value() && !pEntity.level().isClientSide()) {
            boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
            Map<Holder<MobEffect>, MobEffectInstance> effectsMap = new HashMap<>(pEntity.getActiveEffectsMap());
            Map<Holder<MobEffect>, MobEffectInstance> inputEffectsMap = new HashMap<>();
            Map<MobEffect, MobEffectInstance> outputEffectsMap = new HashMap<>();
            for (Map.Entry<Holder<MobEffect>, MobEffectInstance> entry : effectsMap.entrySet()) {
                MobEffect mobeffect = entry.getKey().value();
                if (!mobeffect.isBeneficial()) {
                    inputEffectsMap.put(entry.getKey(), entry.getValue());
                }
            }
            for (Map.Entry<Holder<MobEffect>, MobEffectInstance> entry : inputEffectsMap.entrySet()) {
                MobEffect mobeffect = entry.getKey().value();
                boolean flag = isPowerful || (entry.getValue().getAmplifier() <= amplifier && entry.getValue().getDuration() >= 0);
                if (flag) {
                    if (reverseNormalEffect.get(mobeffect) != null) {
                        outputEffectsMap.put(reverseNormalEffect.get(mobeffect), entry.getValue());
                    }
                    pEntity.removeEffect(entry.getKey());
                }
            }
            for (Map.Entry<MobEffect, MobEffectInstance> entry : outputEffectsMap.entrySet()) {
                int tureTime = entry.getValue().getDuration() > (3000 << amplifier) ? (3000 << amplifier) : entry.getValue().getDuration();
                int tureLv = entry.getValue().getAmplifier() > amplifier ? amplifier : entry.getValue().getAmplifier();
                Holder<MobEffect> outHolder = Holder.direct(entry.getKey());
                if (isPowerful) {
                    pEntity.addEffect(new MobEffectInstance(outHolder, entry.getValue().getDuration(), entry.getValue().getAmplifier()));
                } else {
                    pEntity.addEffect(new MobEffectInstance(outHolder, tureTime, tureLv));
                }
            }
//            Iterator<MobEffect> iterator = effectsMap.keySet().iterator();
//            try {
//                while(iterator.hasNext()) {
//                    MobEffect mobeffect = iterator.next();
//                    /*
//                    如果不是正面效果，比较是否可以逆转，随后逐条进行删除
//                     */
//                    if (!mobeffect.isBeneficial()) {
//                        /*
//                         获取当前效果的等级和时间，并对对反转后的效果等级进行限制
//                         */
//                        MobEffectInstance mobeffectinstance = effectsMap.get(mobeffect);
//                        int time = mobeffectinstance.getDuration();
//                        int lv = mobeffectinstance.getAmplifier();
//
//                        /*
//                        如果逆转用的map收录了当前效果
//                         */
//                        if (reverseEffect.containsKey(mobeffect)){
//                            /*
//                            获取逆转后的效果
//                             */
//                            MobEffect reversedEffect =reverseEffect.get(mobeffect);
//                            /*
//                            添加逆转后的效果
//                             */
//                            int tureLv = lv > amplifier ? amplifier : lv;
//                            int tureTime = time > (3000 << amplifier) ? (3000 << amplifier) : time;
//                            MobEffectInstance buffToAdd = new MobEffectInstance(
//                                    reversedEffect,tureTime,tureLv);
//                            pEntity.addEffect(buffToAdd);
//                        }
//                        /*
//                        无论是否可以逆转，移除负面效果
//                         */
//                        pEntity.removeEffect(mobeffect);
//                    }
//                }
//            } catch (ConcurrentModificationException concurrentmodificationexception) {
//            }
        }
        return true;
    }
    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
