package com.renyigesai.immortalers_delight.potion.immortaleffects;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.util.task.ScheduledExecuteTask;
import com.renyigesai.immortalers_delight.util.task.TimekeepingTask;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;

import java.util.Objects;

//public class BaseImmortalEffectTask extends ScheduledExecuteTask {
////    private final MobEffect THIS_EFFECT = ImmortalersDelightMobEffect.LINGERING_FLAVOR.get();
////    private final LivingEntity pLivingEntity;
////    private final Long expireTime;
////    /*
////    最大持续时间，为防止修改系统时，保险起见使用双判定
////     */
////    private int maxDurationTicks;
////    private int amplifier;
////    /*
////    tick计数，用于dot伤害之类的计算
////     */
////    private int tick = 0;
////    public BaseImmortalEffectTask(int initialDelay, int delay, LivingEntity entity, Long durationSeconds, int amplifier) {
////            super(initialDelay, delay);
////            this.pLivingEntity = entity;
////            this.expireTime = TimekeepingTask.getImmortalTickTime() + (long) (durationSeconds * 1000);
////            this.amplifier = amplifier;
////            this.maxDurationTicks = (int) (durationSeconds * 20 > Integer.MAX_VALUE ? Integer.MAX_VALUE : durationSeconds * 20);
////    }
////    public BaseImmortalEffectTask(int initialDelay, int delay, int taskID, LivingEntity entity, Long durationSeconds, int amplifier) {
////        super(initialDelay, delay,taskID);
////        this.pLivingEntity = entity;
////        this.expireTime = TimekeepingTask.getImmortalTickTime() + (long) (durationSeconds * 1000);
////        this.amplifier = amplifier;
////        this.maxDurationTicks = (int) (durationSeconds * 20 > Integer.MAX_VALUE ? Integer.MAX_VALUE : durationSeconds * 20);
////    }
////    public synchronized void expireNow() {
////        maxDurationTicks = tick;
////    }
////    @Override
////    public void run() {
////        /*
////        这里是在超过结束时间时终止计划任务
////         */
////        if ((++tick > maxDurationTicks && TimekeepingTask.getImmortalTickTime()> expireTime )
////                || !pLivingEntity.isAlive()
////                || BaseImmortalEffect.getEntityMap().get(pLivingEntity.getUUID()) == null) {
////            if (pLivingEntity.hasEffect(THIS_EFFECT)) {
////                pLivingEntity.removeEffect(THIS_EFFECT);
////            }
////            this.cancel();
////            return;
////        }
////        int duration = maxDurationTicks - tick > 0 ? maxDurationTicks - tick : 1;
////        /*
////        这里是实现能解的行为，如果对象没有需要的Buff，计划任务将终止
////         */
//////        if (!pLivingEntity.hasEffect(THIS_EFFECT) && tick > 1) {
//////            ImmortalersDelightMod.LOGGER.info("这里是计划任务，检测到Buff被解，取消计划任务");
//////            if (pLivingEntity.hasEffect(THIS_EFFECT)) {
//////                pLivingEntity.removeEffect(THIS_EFFECT);
//////            }
//////            this.cancel();
//////            return;
//////        }
////        /*
////        这里是实现能续的行为，如果Effect的持续时间更长，更新计划任务的持续时间
////         */
////        if (pLivingEntity.hasEffect(THIS_EFFECT)) {
////            int lv = pLivingEntity.hasEffect(THIS_EFFECT)?
////                    Objects.requireNonNull(pLivingEntity.getEffect(THIS_EFFECT).getAmplifier()):0;
////            int time = pLivingEntity.hasEffect(THIS_EFFECT)?
////                    Objects.requireNonNull(pLivingEntity.getEffect(THIS_EFFECT).getDuration()):0;
////            if (lv > amplifier) {amplifier = lv;ImmortalersDelightMod.LOGGER.info("Buff被强化，等级增加到" + amplifier);}
////            if (time > duration) {maxDurationTicks = tick + time;ImmortalersDelightMod.LOGGER.info("Buff被续，时间延长到" + maxDurationTicks);}
////        }
////        DoOnRun(pLivingEntity, amplifier,duration);
////    }
////    public void DoOnRun(LivingEntity pLivingEntity, int amplifier, int duration) {
////        if (tick % 20 == 0) ImmortalersDelightMod.LOGGER.info("这里是Buff模板的Task，剩余时间为：" + duration);
////    }
//}
