package com.renyigesai.immortalers_delight.potion;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import static com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect.MAGICAL_REVERSE;
import static com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect.RELIEVE_POISON;

public class RelievePotionEffectMobEffect extends MobEffect {
    public Map<MobEffect,MobEffect> reverseEffect = (new ImmutableMap.Builder<MobEffect,MobEffect>())
            .put(MobEffects.BAD_OMEN,MobEffects.HERO_OF_THE_VILLAGE)
            .put(MobEffects.UNLUCK,MobEffects.LUCK)
            .put(MobEffects.GLOWING,MobEffects.INVISIBILITY)
            .put(MobEffects.MOVEMENT_SLOWDOWN,MobEffects.MOVEMENT_SPEED)
            .put(MobEffects.LEVITATION,MobEffects.SLOW_FALLING)
            .put(MobEffects.BLINDNESS,MobEffects.NIGHT_VISION)
            .put(MobEffects.DIG_SLOWDOWN,MobEffects.DIG_SPEED)
            .put(MobEffects.WEAKNESS,MobEffects.DAMAGE_BOOST)
            .put(MobEffects.POISON,MobEffects.REGENERATION)
            .put(MobEffects.HARM,MobEffects.HEAL)
            .build();
    public RelievePotionEffectMobEffect() {
        super(MobEffectCategory.BENEFICIAL, -39424);
    }

    @Override
    public void applyEffectTick(LivingEntity pEntity, int amplifier) {
        if (this == RELIEVE_POISON.get()) {
            if (pEntity.hasEffect(MobEffects.POISON)){
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
                pEntity.removeEffect(MobEffects.POISON);
            }
            if (pEntity.hasEffect(MobEffects.WITHER)){
                int lv = pEntity.hasEffect(MobEffects.WITHER)? Objects.requireNonNull(pEntity.getEffect(MobEffects.WITHER)).getAmplifier():0;
                int time = pEntity.hasEffect(MobEffects.WITHER)? Objects.requireNonNull(pEntity.getEffect(MobEffects.WITHER)).getDuration():0;
                /*
                高等级的解毒效果会缩短弱凋零的时间
                 */
                time /= (amplifier + 1);
                MobEffectInstance weakWither = new MobEffectInstance(
                        ImmortalersDelightMobEffect.WEAK_WITHER.get(),time,lv);
                pEntity.addEffect(weakWither);
                pEntity.removeEffect(MobEffects.WITHER);
            }
        }

        /*
        下面是金魔法果反转效果的实现
         */
        if (this == MAGICAL_REVERSE.get()) {
            /*
            对饥饿进行特殊处理，其将变为20分之1时间的饱和。
             */
            if (pEntity.hasEffect(MobEffects.HUNGER)){
                int lv = pEntity.hasEffect(MobEffects.HUNGER)? Objects.requireNonNull(pEntity.getEffect(MobEffects.HUNGER)).getAmplifier():0;
                int time = pEntity.hasEffect(MobEffects.HUNGER)? Objects.requireNonNull(pEntity.getEffect(MobEffects.HUNGER)).getDuration():0;
                time /= 20;
                MobEffectInstance Saturation = new MobEffectInstance(
                        MobEffects.SATURATION,time,lv);
                pEntity.addEffect(Saturation);
                pEntity.removeEffect(MobEffects.HUNGER);
            }
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
                        如果逆转用的map收录了当前效果
                         */
                        if (reverseEffect.containsKey(mobeffect)){
                            /*
                            获取逆转后的效果
                             */
                            MobEffect reversedEffect =reverseEffect.get(mobeffect);
                            /*
                            获取当前效果的等级和时间，并对对反转后的效果等级进行限制
                             */
                            MobEffectInstance mobeffectinstance = effectsMap.get(mobeffect);
                            int time = mobeffectinstance.getDuration();
                            int lv = mobeffectinstance.getAmplifier() > amplifier ? amplifier : mobeffectinstance.getAmplifier();
                            /*
                            添加逆转后的效果
                             */
                            MobEffectInstance buffToAdd = new MobEffectInstance(
                                    reversedEffect,time,lv);
                            pEntity.addEffect(buffToAdd);
                        }
                        /*
                        无论是否可以逆转，对负面效果都逐条进行删除
                         */
                        pEntity.removeEffect(mobeffect);
                    }
                }
            } catch (ConcurrentModificationException concurrentmodificationexception) {
            }
        }
    }
    //public boolean isInstantenous() {
    //    return true;
    //}

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
