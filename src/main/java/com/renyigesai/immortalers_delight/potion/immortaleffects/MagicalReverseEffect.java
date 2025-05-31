package com.renyigesai.immortalers_delight.potion.immortaleffects;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.util.datautil.EffectData;
import com.renyigesai.immortalers_delight.util.task.TimekeepingTask;
import com.renyigesai.immortalers_delight.util.datautil.datasaveloadhelper.ExitTimeSaveLoadHelper;
//import com.renyigesai.immortalers_delight.util.datautil.datasaveloadhelper.MagicalReverseMapSaveLoadHelper;
import com.renyigesai.immortalers_delight.util.task.ScheduledExecuteTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
//
//@Mod.EventBusSubscriber
//public class MagicalReverseEffect {
//    /*
//    这个类能对实体进行标记（以及解除标记），
//     */
//    /*
//    用Map存储受到当前效果的实体与对应的持续时间（表示为结束时刻）
//     */
//    private static final Map<UUID, EffectData> entityHasEffect = new ConcurrentHashMap<>();
//    private static boolean pausing = true;
//    public static void setPausing(boolean pFirst) {
//        pausing = pFirst;
//    }
//    /**
//     * 定义内部静态类 EffectData，用于存储实体所在位置和药水的效果等信息，继承自 BlockPos
//     */
//
//    public static Map<UUID, EffectData> getEntityMap() {
//        return entityHasEffect;
//    }
//
////    public static void setEntityMap(Map<UUID, Long> pEntityMap) {
////        timeToEntity.
////    }
//
//    /**
//     * 对指定的生物实体应用特殊效果，输入实体与持续时间（秒）
//     * @param entity
//     * @param durationSeconds
//     */
//    public static void applyImmortalEffect(LivingEntity entity, double durationSeconds, int amplifier) {
//        /*
//        判断合理的实体目标
//         */
//        if (entity == null || entity.isRemoved()) {
//            return;
//        }
//
//        /*
//        获取实体UUID以唯一标记对应实体
//         */
//        UUID uuid = entity.getUUID();
//        /*
//        计算效果的结束时刻，使用系统时以绕开tick相关操作
//         */
//        long expireTime = TimekeepingTask.getImmortalTickTime() + (long) (durationSeconds * 1000);
//        int durationTicks = (int) (durationSeconds * 20);
//
//        /*
//        构造新的计划任务，在其中执行buff的逻辑
//         */
//        int taskID = new Random().nextInt(Short.MAX_VALUE);
//        ScheduledExecuteTask task = buildEffectTask(taskID,entity,durationSeconds,amplifier);
//        task.start();
//        /*
//        将实体与Buff相关数据保存到Map
//         */
//        EffectData effectData = new EffectData(entity.blockPosition(),expireTime,amplifier,taskID);
//        entityHasEffect.put(uuid,effectData);
//        System.out.println("应用了 " + durationSeconds + " 秒的特殊效果在实体 " + uuid + " 上");
//        ImmortalersDelightMod.LOGGER.info("应用了 " + durationSeconds + " 秒的特殊效果在实体 " + uuid + " 上");
////        if (!entity.level().isClientSide()) {
////            MagicalReverseMapSaveLoadHelper.saveEntityHasEffect(entity.level(), entityHasEffect);
////        }
//    }
//
//    /**
//     * 在生物的tick事件处理效果的逻辑
//     * @param event
//     */
//    @SubscribeEvent
//    public static void onTick(LivingEvent.LivingTickEvent event) {
//        /*
//        判断是否触发了暂停，由于
//        又因为暂停时会触发WorldSave但解除暂停不会触发WorldLoad，因此在这里再触发一次读取实体Map
//         */
//        if (pausing && !event.getEntity().level().isClientSide){
//            LoadEntityMap(event.getEntity().level());
//        }
//
//        LivingEntity entity = event.getEntity();
//        if (entity == null || entity.isRemoved() || !entity.isAlive()) {
//            return;
//        }
//
//        /*
//        获取当前实体的效果结束时刻
//         */
//        UUID uuid = entity.getUUID();
//        if (entityHasEffect.get(uuid) == null) {
//            return;
//        }
//        Long expireTime = entityHasEffect.get(uuid).getTime();
//
//        /*
//        如果当前系统时间超过了结束时刻，取消效果（将实体从Map中移除，取消计划任务）
//         */
////        ImmortalersDelightMod.LOGGER.info("你这B玩意还有多久结束？" + (expireTime - TimekeepingTask.getImmortalTickTime()) / 1000 + "秒");
////        ImmortalersDelightMod.LOGGER.info("附一个现在的时间" + TimekeepingTask.getImmortalTickTime());
//        if (TimekeepingTask.getImmortalTickTime() > expireTime) {
//            removeImmortalEffect(entity);
//        } else {
//            /*
//            此处为具体的效果方法逻辑
//            此处的处理为如果没找到实体对应的计划任务（例如退出重进导致计划任务丢失），补一个新的计划任务
//             */
//            Integer taskID = entityHasEffect.get(uuid).getTaskId();
//            ScheduledExecuteTask task = MagicalReverseEffectTask.getTaskFromID(taskID);
//            // 输出读取到的 Map
//
//            if (task == null) {
////                ImmortalersDelightMod.LOGGER.info("WTF怎么没找到计划任务？让我看看现在的实体表是什么玩意");
////                for (Map.Entry<UUID, EffectData> entry : entityHasEffect.entrySet()) {
////                    UUID uuid1 = entry.getKey();
////                    EffectData effectData = entry.getValue();
////                    ImmortalersDelightMod.LOGGER.info("UUID: " + uuid1 + ", Effect Level: " + effectData.getAmplifier() +
////                            ", Duration: " + effectData.getTime() + ", Task ID " + effectData.getTaskId());
////                }
//
//                Long durationTime = expireTime - TimekeepingTask.getImmortalTickTime();
////                ImmortalersDelightMod.LOGGER.info("你这B玩意还有多久结束？" + durationTime);
//                ScheduledExecuteTask newTask = buildEffectTask(taskID,entity,durationTime,entityHasEffect.get(uuid).getAmplifier());
//                newTask.start();
////                System.out.println("计划任务异常！已创建新的计划任务");
////                ImmortalersDelightMod.LOGGER.info("计划任务异常！已创建新的计划任务");
//            }
//
//        }
//    }
//
//    /**
//     * 用于去除指定实体的特殊效果的方法
//     * @param entity
//     */
//    public static void removeImmortalEffect(LivingEntity entity) {
//        UUID uuid = entity.getUUID();
//        onImmortalEffectRemove(entity);
//        entityHasEffect.remove(uuid);
//        System.out.println("已将实体" + uuid + "从携带者列表中移除");
//        ImmortalersDelightMod.LOGGER.info("已将实体" + uuid + "从携带者列表中移除");
////        if (!entity.level().isClientSide()) {
////            MagicalReverseMapSaveLoadHelper.saveEntityHasEffect(entity.level(), entityHasEffect);
////        }
//    }
//    /**
//     * 用于去除指定实体的特殊效果时进行善后的方法
//     * @param entity
//     */
//    private static void onImmortalEffectRemove (LivingEntity entity) {
//        if (entity == null || entity.isRemoved()) {
//            return;
//        }
//
//        UUID uuid = entity.getUUID();
//        System.out.println("实体 " + uuid + " 解除了特殊效果");
//        ImmortalersDelightMod.LOGGER.info("实体 " + uuid + " 解除了特殊效果");
//        /*
//        在这里处理一些善后工作，这不是药水效果所以要记得手动善后
//         */
//        if (entityHasEffect.get(uuid) == null) {return;}
//        Integer taskID = entityHasEffect.get(uuid).getTaskId();
//        ScheduledExecuteTask task = MagicalReverseEffectTask.getTaskFromID(taskID);
//        if (task != null) {
//            task.cancel();
//        }
//
//    }
//
//    /**
//     * 监听世界保存事件，将实体Map存盘
//     * @param event
//     */
//    @SubscribeEvent
//    public static void onWorldLoad(LevelEvent.Load event) {
//        if (event.getLevel() instanceof ServerLevel serverLevel) {
//            LoadEntityMap(serverLevel);
//        }
//    }
//
//    /**
//     * 这个方法用于修正时序以及从存盘数据中加载退出时的实体数据
//     * @param serverLevel
//     */
//    private static void LoadEntityMap(Level serverLevel) {
//
//        ImmortalersDelightMod.LOGGER.info("正在读取存盘的Effect实体Map");
//        Map<UUID, EffectData> oldMap = MagicalReverseMapSaveLoadHelper.loadEntityHasEffect(serverLevel);
//        if (oldMap == null) {
//            ImmortalersDelightMod.LOGGER.info("存盘表为null！");
//            pausing = false;
//            return;
//        }
//        //entityHasEffect = new ConcurrentHashMap<>();
//            /*
//            遍历存盘的Map，将存盘的Map数据同步到新Map
//             */
//        for (Map.Entry<UUID, EffectData> entry : oldMap.entrySet()) {
//            UUID uuidOld = entry.getKey();
//            EffectData dataOld = entry.getValue();
//            /*
//            修正时刻，存盘的Map里存的时间理论上为[上次关服时间 + 剩余持续时间]，因此减去一个上次关服时间再加上当前时间即可保持持续时间不变
//             */
//            Long trueTime = dataOld.getTime() - ExitTimeSaveLoadHelper.loadExitTime(serverLevel) + TimekeepingTask.getImmortalTickTime();
//            EffectData newOne = new EffectData(dataOld, trueTime, dataOld.getAmplifier(), dataOld.getTaskId());
//            entityHasEffect.put(uuidOld,newOne);
//        }
//        ImmortalersDelightMod.LOGGER.info("存盘Map读取完毕，当前Effect实体Map为：");
//        pausing = false;
//        // 输出读取到的 Map
//        for (Map.Entry<UUID, EffectData> entry : entityHasEffect.entrySet()) {
//            UUID uuid = entry.getKey();
//            EffectData effectData = entry.getValue();
//            ImmortalersDelightMod.LOGGER.info("UUID: " + uuid + ", Effect Level: " + effectData.getAmplifier() +
//                    ", Duration: " + effectData.getTime() + ", Task ID " + effectData.getTaskId());
//        }
//    }
//    /**
//     * 用于构建新的计划任务的方法
//     * @param id
//     * @param pLivingEntity
//     * @param durationSeconds
//     * @param amplifier
//     * @return
//     */
//    public static ScheduledExecuteTask buildEffectTask(int id, LivingEntity pLivingEntity, double durationSeconds, int amplifier) {
//
//        MagicalReverseEffectTask task = new MagicalReverseEffectTask(1,1,id,pLivingEntity, (long) durationSeconds,amplifier);
//        task.start();
//        return task;
//    }
//}
//
