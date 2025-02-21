package com.renyigesai.immortalers_delight.potion.immortaleffects;

import com.google.common.collect.ImmutableMap;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.util.task.TimekeepingTask;
import com.renyigesai.immortalers_delight.util.task.ScheduledExecuteTask;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class MagicalReverseEffectTask extends ScheduledExecuteTask {
    private final LivingEntity pLivingEntity;
    private final Long expireTime;
    /*
    最大持续时间，为防止修改系统时，保险起见使用双判定
     */
    private int maxDurationTicks;
    private final int amplifier;
    /*
    tick计数，用于dot伤害之类的计算
     */
    private int tick = 0;
    public MagicalReverseEffectTask(int initialDelay, int delay, LivingEntity entity, Long durationSeconds, int amplifier) {
            super(initialDelay, delay);
            this.pLivingEntity = entity;
            this.expireTime = TimekeepingTask.getImmortalTickTime() + (long) (durationSeconds * 1000);
            this.amplifier = amplifier;
            this.maxDurationTicks = (int) (durationSeconds * 20 > Integer.MAX_VALUE ? Integer.MAX_VALUE : durationSeconds * 20);
    }
    public MagicalReverseEffectTask(int initialDelay, int delay, int taskID, LivingEntity entity, Long durationSeconds, int amplifier) {
        super(initialDelay, delay,taskID);
        this.pLivingEntity = entity;
        this.expireTime = TimekeepingTask.getImmortalTickTime() + (long) (durationSeconds * 1000);
        this.amplifier = amplifier;
        this.maxDurationTicks = (int) (durationSeconds * 20 > Integer.MAX_VALUE ? Integer.MAX_VALUE : durationSeconds * 20);
    }
    public MobEffect FD_Nourished = vectorwing.farmersdelight.common.registry.ModEffects.NOURISHMENT.get();
    public MobEffect FD_Comfit = vectorwing.farmersdelight.common.registry.ModEffects.COMFORT.get();

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
            .put(MobEffects.HARM,MobEffects.HEAL)
            .build();

    @Override
    public void run() {
        /*
        到时间解除buff，取消计划任务
         */
        if ((++tick > maxDurationTicks && TimekeepingTask.getImmortalTickTime()> expireTime ) || !pLivingEntity.isAlive()) {
            this.cancel();
            return;
        }
        /*
        能解的实现：若无对应buff，取消计划任务
        因为计划任务本身就是为了不能续而生，所以不需要专门写一个不能续
         */
        if (pLivingEntity.hasEffect(ImmortalersDelightMobEffect.MAGICAL_REVERSE.get())) {
            reverseEffect(pLivingEntity, amplifier);
        } else if (tick > 1){
            this.cancel();
        }
    }

    public void reverseEffect(LivingEntity pEntity, int amplifier) {
            /*
            对反胃进行特殊处理，其将变为20分之1时间的饱和。
             */
        if (pEntity.hasEffect(MobEffects.CONFUSION)){
            int lv = pEntity.hasEffect(MobEffects.CONFUSION)? Objects.requireNonNull(pEntity.getEffect(MobEffects.CONFUSION)).getAmplifier():0;
            int time = pEntity.hasEffect(MobEffects.CONFUSION)? Objects.requireNonNull(pEntity.getEffect(MobEffects.CONFUSION)).getDuration():0;
            time /= 20;
            MobEffectInstance Saturation = new MobEffectInstance(
                    MobEffects.SATURATION,time,lv);
            pEntity.addEffect(Saturation);
            pEntity.removeEffect(MobEffects.CONFUSION);
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
                        int tureLv = mobeffectinstance.getAmplifier() > amplifier ? amplifier : mobeffectinstance.getAmplifier();
                        MobEffectInstance buffToAdd = new MobEffectInstance(
                                reversedEffect,time,tureLv);
                        pEntity.addEffect(buffToAdd);
                    }
                        /*
                        无论是否可以逆转，令负面效果的等级降低lv级
                         */
                    pEntity.removeEffect(mobeffect);
                    if (lv > amplifier) {
                        MobEffectInstance lowEffect = new MobEffectInstance(mobeffect,time,lv - amplifier);
                        pEntity.addEffect(lowEffect);
                    }
                }
            }
        } catch (ConcurrentModificationException concurrentmodificationexception) {
        }
    }

}
