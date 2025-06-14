package com.renyigesai.immortalers_delight.potion;

import com.google.common.collect.ImmutableMap;
import com.renyigesai.immortalers_delight.Config;
import com.renyigesai.immortalers_delight.event.DifficultyModeHelper;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.util.EffectUtils;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.registry.ModEffects;

import java.util.*;

import static com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect.*;
public class MagicalReverseMobEffect extends MobEffect {

    @Override
    public boolean isInstantenous() {
        return true;
    }
//    public MobEffect FD_Nourished = EffectUtils.get0therModMobEffect(FarmersDelight.MODID + ":nourishment");
//
//    public MobEffect FD_Comfit = EffectUtils.get0therModMobEffect(FarmersDelight.MODID + ":comfort");

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
    public void applyEffectTick(@NotNull LivingEntity pEntity, int amplifier) {
        /*
        下面是金魔法果反转效果的实现
         */
        if (this == MAGICAL_REVERSE.get() && !pEntity.level().isClientSide()) {
            boolean isPowerful = DifficultyModeHelper.isPowerBattleMode();
            /*获取实体的EffectMap进行遍历*/
            Map<MobEffect, MobEffectInstance> effectsMap = new HashMap<>(pEntity.getActiveEffectsMap());
            Map<MobEffect, MobEffectInstance> inputEffectsMap = new HashMap<>();
            Map<MobEffect, MobEffectInstance> outputEffectsMap = new HashMap<>();
            for (Map.Entry<MobEffect, MobEffectInstance> entry : effectsMap.entrySet()) {
                MobEffect mobeffect = entry.getKey();
                if (!mobeffect.isBeneficial()) {
                    inputEffectsMap.put(entry.getKey(),  entry.getValue());
                }
            }
            /*遍历获取到的所有负面效果，判断是否能反转并尝试去除*/
            for (Map.Entry<MobEffect, MobEffectInstance> entry : inputEffectsMap.entrySet()) {
                boolean flag = isPowerful || (entry.getValue().getAmplifier() <= amplifier && entry.getValue().getDuration() >= 0);
                if (flag) {
                    if (reverseNormalEffect.get(entry.getKey()) != null) {
                        outputEffectsMap.put(reverseNormalEffect.get(entry.getKey()), entry.getValue());
                    }
                    pEntity.removeEffect(entry.getKey());
                }
            }
            /*添加反转后的效果*/
            for (Map.Entry<MobEffect, MobEffectInstance> entry : outputEffectsMap.entrySet()) {
                int tureTime = entry.getValue().getDuration() > (3000 << amplifier) ? (3000 << amplifier) : entry.getValue().getDuration();
                int tureLv = entry.getValue().getAmplifier() > amplifier ? amplifier : entry.getValue().getAmplifier();
                if (isPowerful) {
                    pEntity.addEffect(new MobEffectInstance(entry.getKey(), entry.getValue().getDuration(), entry.getValue().getAmplifier()));
                } else {
                    pEntity.addEffect(new MobEffectInstance(entry.getKey(), tureTime, tureLv));
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
    }
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
