package com.renyigesai.immortalers_delight.potion;

import com.google.common.collect.ImmutableMap;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.util.EffectUtils;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.registry.ModEffects;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import static com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect.*;
public class MagicalReverseMobEffect extends MobEffect {

    @Override
    public boolean isInstantenous() {
        return true;
    }
    public MobEffect FD_Nourished = EffectUtils.get0therModMobEffect(FarmersDelight.MODID + ":nourishment");

    public MobEffect FD_Comfit = EffectUtils.get0therModMobEffect(FarmersDelight.MODID + ":comfort");

    public Map<MobEffect,MobEffect> reverseEffect = (new ImmutableMap.Builder<MobEffect,MobEffect>())
            .put(MobEffects.BAD_OMEN,MobEffects.HERO_OF_THE_VILLAGE)
            .put(MobEffects.UNLUCK,MobEffects.LUCK)
            .put(MobEffects.GLOWING,MobEffects.INVISIBILITY)
            .put(MobEffects.MOVEMENT_SLOWDOWN,MobEffects.MOVEMENT_SPEED)
            .put(MobEffects.LEVITATION,MobEffects.SLOW_FALLING)
            .put(MobEffects.BLINDNESS,MobEffects.NIGHT_VISION)
            .put(MobEffects.DARKNESS,MobEffects.CONDUIT_POWER)
            .put(MobEffects.DIG_SLOWDOWN,MobEffects.DIG_SPEED)
            .put(MobEffects.WEAKNESS,MobEffects.DAMAGE_BOOST)
            .put(MobEffects.POISON,FD_Comfit)
            .put(MobEffects.WITHER,MobEffects.REGENERATION)
            .put(MobEffects.HUNGER,FD_Nourished)
            .put(MobEffects.CONFUSION, KEEP_A_FAST.get())
            .build();
    public MagicalReverseMobEffect() {
        super(MobEffectCategory.BENEFICIAL, -39424);
    }

    @Override
    public void applyEffectTick(LivingEntity pEntity, int amplifier) {
        /*
        下面是金魔法果反转效果的实现
         */
        if (this == MAGICAL_REVERSE.get() && !pEntity.level().isClientSide()) {
            /*
            获取实体的EffectMap进行遍历
             */
            Map<MobEffect, MobEffectInstance> effectsMap = pEntity.getActiveEffectsMap();
            Iterator<MobEffect> iterator = effectsMap.keySet().iterator();
            try {
                while(iterator.hasNext()) {
                    MobEffect mobeffect = iterator.next();
                    /*
                    如果不是正面效果，比较是否可以逆转，随后逐条进行删除
                     */
                    if (!mobeffect.isBeneficial()) {
                        /*
                         获取当前效果的等级和时间，并对对反转后的效果等级进行限制
                         */
                        MobEffectInstance mobeffectinstance = effectsMap.get(mobeffect);
                        int time = mobeffectinstance.getDuration();
                        int lv = mobeffectinstance.getAmplifier();

                        /*
                        如果逆转用的map收录了当前效果
                         */
                        if (reverseEffect.containsKey(mobeffect)){
                            /*
                            获取逆转后的效果
                             */
                            MobEffect reversedEffect =reverseEffect.get(mobeffect);
                            /*
                            添加逆转后的效果
                             */
                            int tureLv = lv > amplifier ? amplifier : lv;
                            int tureTime = time > (3000 << amplifier) ? (3000 << amplifier) : time;
                            MobEffectInstance buffToAdd = new MobEffectInstance(
                                    reversedEffect,tureTime,tureLv);
                            pEntity.addEffect(buffToAdd);
                        }
                        /*
                        无论是否可以逆转，移除负面效果
                         */
                        pEntity.removeEffect(mobeffect);
                    }
                }
            } catch (ConcurrentModificationException concurrentmodificationexception) {
            }
        }
    }
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
