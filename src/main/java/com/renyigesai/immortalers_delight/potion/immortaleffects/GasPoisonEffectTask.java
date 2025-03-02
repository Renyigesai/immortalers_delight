package com.renyigesai.immortalers_delight.potion.immortaleffects;

import com.google.common.collect.ImmutableMap;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.util.task.ScheduledExecuteTask;
import com.renyigesai.immortalers_delight.util.task.TimekeepingTask;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GasPoisonEffectTask extends ScheduledExecuteTask {
    private final MobEffect THIS_EFFECT = ImmortalersDelightMobEffect.GAS_POISON.get();
    private final LivingEntity pLivingEntity;
    private final Long expireTime;
    /*
    最大持续时间，为防止修改系统时，保险起见使用双判定
     */
    private int maxDurationTicks;
    private int amplifier;
    /*
    tick计数，用于dot伤害之类的计算
     */
    private int tick = 0;
    /*
    这是瓦斯毒特有的信息，记录派生buff
     */
    public Map<MobEffect,Integer> derivativeEffect = new ConcurrentHashMap<MobEffect,Integer>();

    public List<MobEffect> derivativeEffectName = new ArrayList<>();
    public GasPoisonEffectTask(int initialDelay, int delay, LivingEntity entity, Long durationSeconds, int amplifier) {
            super(initialDelay, delay);
            this.pLivingEntity = entity;
            this.expireTime = TimekeepingTask.getImmortalTickTime() + (long) (durationSeconds * 1000);
            this.amplifier = amplifier;
            this.maxDurationTicks = (int) (durationSeconds * 20 > Integer.MAX_VALUE ? Integer.MAX_VALUE : durationSeconds * 20);
        this.derivativeEffect.put(MobEffects.MOVEMENT_SLOWDOWN,100);
        this.derivativeEffect.put(MobEffects.BLINDNESS,3600);
        this.derivativeEffect.put(MobEffects.WEAKNESS,100);
        this.derivativeEffect.put(MobEffects.CONFUSION,100);
        this.derivativeEffect.put(MobEffects.HUNGER,100);

    }
    public GasPoisonEffectTask(int initialDelay, int delay, int taskID, LivingEntity entity, Long durationSeconds, int amplifier) {
        super(initialDelay, delay,taskID);
        this.pLivingEntity = entity;
        this.expireTime = TimekeepingTask.getImmortalTickTime() + (long) (durationSeconds * 1000);
        this.amplifier = amplifier;
        this.maxDurationTicks = (int) (durationSeconds * 20 > Integer.MAX_VALUE ? Integer.MAX_VALUE : durationSeconds * 20);
        this.derivativeEffect.put(MobEffects.MOVEMENT_SLOWDOWN,100);
        this.derivativeEffect.put(MobEffects.BLINDNESS,3600);
        this.derivativeEffect.put(MobEffects.WEAKNESS,100);
        this.derivativeEffect.put(MobEffects.CONFUSION,100);
        this.derivativeEffect.put(MobEffects.HUNGER,100);
    }
    public synchronized void expireNow() {
        maxDurationTicks = tick;
    }
    @Override
    public void run() {
        /*
        这里是在超过结束时间时终止计划任务
         */
        if ((++tick > maxDurationTicks && TimekeepingTask.getImmortalTickTime()> expireTime )
                || !pLivingEntity.isAlive()
                || GasPoisonEffect.getEntityMap().get(pLivingEntity.getUUID()) == null) {
            if (pLivingEntity.hasEffect(THIS_EFFECT)) {
                pLivingEntity.removeEffect(THIS_EFFECT);
            }
            this.cancel();
            return;
        }
        int duration = maxDurationTicks - tick > 0 ? maxDurationTicks - tick : 1;
        /*
        这里是实现能解的行为，如果对象没有需要的Buff，计划任务将终止
         */
//        if (!pLivingEntity.hasEffect(THIS_EFFECT) && tick > 1) {
//            ImmortalersDelightMod.LOGGER.info("这里是计划任务，检测到Buff被解，取消计划任务");
//            if (pLivingEntity.hasEffect(THIS_EFFECT)) {
//                pLivingEntity.removeEffect(THIS_EFFECT);
//            }
//            this.cancel();
//            return;
//        }
        /*
        这里是实现能续的行为，如果Effect的持续时间更长，更新计划任务的持续时间
         */
        if (pLivingEntity.hasEffect(THIS_EFFECT)) {
            int lv = pLivingEntity.hasEffect(THIS_EFFECT)?
                    Objects.requireNonNull(pLivingEntity.getEffect(THIS_EFFECT).getAmplifier()):0;
            int time = pLivingEntity.hasEffect(THIS_EFFECT)?
                    Objects.requireNonNull(pLivingEntity.getEffect(THIS_EFFECT).getDuration()):0;
            if (lv > amplifier) {amplifier = lv;ImmortalersDelightMod.LOGGER.info("Buff被强化，等级增加到" + amplifier);}
            if (time > duration) {maxDurationTicks = tick + time;ImmortalersDelightMod.LOGGER.info("Buff被续，时间延长到" + maxDurationTicks);}
        }
        DoOnRun(pLivingEntity, amplifier,duration);
    }
    public void DoOnRun(LivingEntity pLivingEntity, int amplifier, int duration) {
        if (tick % 20 == 0) ImmortalersDelightMod.LOGGER.info("这里是Buff模板的Task，剩余时间为：" + duration);
        gasDamage(pLivingEntity,amplifier);
    }

    public void gasDamage(LivingEntity pEntity, int amplifier) {
        float damage = (float) (pEntity.getMaxHealth() * 0.06 );
        damage = damage > 1.2F ? damage : 1.2F;
        /*
        瓦斯毒伤害，由于需要派生中毒，伤害由setHealth进行以免撞到无敌时间
         */
        if (pEntity.getHealth() - damage > 0) {
            if (tick % (32 >> amplifier) == 0) pEntity.setHealth(pEntity.getHealth() - damage);
            /*
            瓦斯毒派生其他DeBuff，通过Map记录DeBuff时间以使得派生的DeBuff也无法通过常规手段解掉
             */
            for (Map.Entry<MobEffect,Integer> entry : derivativeEffect.entrySet()) {
                /*
                如果Buff剩余时间是0，那我们就不管他
                 */
                int duration = entry.getValue();
                if (duration >0) {
                    MobEffect effect = entry.getKey();

                    if (!pEntity.hasEffect(effect)) {
                        MobEffectInstance effectInstance = new MobEffectInstance(effect,duration,amplifier);
                        pEntity.addEffect(effectInstance);
                    } else {
                        /*
                        如果在已经有了派生buff且持续时间比记录的更长，在此处令派生Buff的持续时间与表中的记录相加
                        用于下面随机刷新DeBuff的方法，以绕开从Map随机的一坨
                         */
                        int time = pEntity.hasEffect(effect)? Objects.requireNonNull(pEntity.getEffect(effect)).getDuration():0;
                        if (time > duration) {
                            derivativeEffect.put(effect,duration + time);
                        }
                    }
                    /*
                    让Map的持续时间-1，不然buff结束不了了
                     */
                    derivativeEffect.put(effect, duration - 1);
                }
            }
            /*
            瓦斯毒刷新DeBuff,每秒令随机一个DeBuff的持续时间+100
             */
            if (tick % 20 == 0) {
                derivativeEffectName = new ArrayList<>();
                for (Map.Entry<MobEffect,Integer> entry : derivativeEffect.entrySet()) {
                    derivativeEffectName.add(entry.getKey());
                }
                int size = derivativeEffectName.size() - 1 > 0 ? derivativeEffectName.size() - 1 : 0;
                ImmortalersDelightMod.LOGGER.info("现在有几个能用的DeBuff？" + derivativeEffectName.size());
                MobEffect randEffect = derivativeEffectName.get(pEntity.getRandom().nextInt(size));
                int time = derivativeEffect.get(randEffect) == null ? derivativeEffect.get(randEffect) : 0;
                derivativeEffect.put(randEffect, time + 100);
            }
        } else {
            /*
            使用setHealth要管杀管埋，写一个秒杀处理负血量情况
             */
            pEntity.hurt(pEntity.damageSources().magic(),pEntity.getMaxHealth() * 2);
            if (pEntity.isAlive() || !pEntity.isRemoved()) {
                pEntity.setHealth(0);
                pEntity.hurt(pEntity.damageSources().fellOutOfWorld(),pEntity.getMaxHealth() * 2);
                //pEntity.die(pEntity.damageSources().fellOutOfWorld());
            }
            this.cancel();
        }
    }
}
