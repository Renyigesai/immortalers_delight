package com.renyigesai.immortalers_delight.potion.immortaleffects;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.util.datautil.EffectData;
import com.renyigesai.immortalers_delight.util.datautil.datasaveloadhelper.BaseImmortalMapSaveLoadHelper;
import com.renyigesai.immortalers_delight.util.datautil.datasaveloadhelper.ExitTimeSaveLoadHelper;
import com.renyigesai.immortalers_delight.util.task.ScheduledExecuteTask;
import com.renyigesai.immortalers_delight.util.task.TimekeepingTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber
public class BaseImmortalEffect {
    /*
    这个类能对实体进行标记（以及解除标记），
     */
    /*
    用Map存储受到当前效果的实体与对应的持续时间（表示为结束时刻）
     */
    private static final Map<UUID, EffectData> entityHasEffect = new ConcurrentHashMap<>();
    private static boolean pausing = true;
    private MobEffect THIS_EFFECT = ImmortalersDelightMobEffect.LINGERING_FLAVOR.get();

    public static void setPausing(boolean pFirst) {
        pausing = pFirst;
    }
    public static Map<UUID, EffectData> getEntityMap() {
        return entityHasEffect;
    }

    /**
     * 对指定的生物实体应用特殊效果，输入实体与持续时间（秒）
     * @param entity
     * @param durationSeconds
     */
    public static void applyImmortalEffect(LivingEntity entity, double durationSeconds, int amplifier) {
        /*
        判断合理的实体目标
         */
        if (entity == null || entity.isRemoved()) {
            return;
        }
        /*
        获取实体UUID以唯一标记对应实体
         */
        UUID uuid = entity.getUUID();
        /*
        计算效果的结束时刻，使用自定义的计时器以绕开WorldTime相关操作
         */
        long expireTime = TimekeepingTask.getImmortalTickTime() + (long) (durationSeconds * 1000);
        MobEffectInstance effectInstance = new MobEffectInstance(ImmortalersDelightMobEffect.LINGERING_FLAVOR.get(), (int) (durationSeconds *20), amplifier);
        entity.addEffect(effectInstance);
        /*
        构造新的计划任务，在其中执行buff的逻辑
         */
        int taskID = new Random().nextInt(Short.MAX_VALUE);
        ScheduledExecuteTask task = buildEffectTask(taskID,entity,durationSeconds,amplifier);
        task.start();
        /*
        将实体与Buff相关数据保存到Map
         */
        EffectData effectData = new EffectData(entity.blockPosition(),expireTime,amplifier,taskID);
        entityHasEffect.put(uuid,effectData);
    }

    /**
     * 在生物的tick事件处理效果的逻辑
     * @param event
     */
    @SubscribeEvent
    public static void onTick(LivingEvent.LivingTickEvent event) {
        /*
        判断是否触发了暂停，由于暂停时系统时间依然是在跑的
        又因为暂停时会触发WorldSave但解除暂停不会触发WorldLoad，因此在这里再触发一次读取实体Map矫正时间
         */
        if (pausing && !event.getEntity().level().isClientSide){
            LoadEntityMap(event.getEntity().level());
        }
        /*
        判断当前实体是否合法
         */
        LivingEntity entity = event.getEntity();
        if (entity == null || entity.isRemoved() || !entity.isAlive()) {
            return;
        }
        /*
        获取当前实体的效果结束时刻
         */
        UUID uuid = entity.getUUID();
        if (entityHasEffect.get(uuid) == null) {
            return;
        }
        Long expireTime = entityHasEffect.get(uuid).getTime();
        Long durationTime = (expireTime - TimekeepingTask.getImmortalTickTime()) / 1000;
        /*
        如果当前系统时间超过了结束时刻，取消效果（将实体从Map中移除，取消计划任务）
         */
        ImmortalersDelightMod.LOGGER.info("你这B玩意还有多久结束？" + durationTime + "秒");
        ImmortalersDelightMod.LOGGER.info("这里是Buff模板的Tick方法，汇报一个现在的时间" + TimekeepingTask.getImmortalTickTime());
        if (TimekeepingTask.getImmortalTickTime() > expireTime) {
            removeImmortalEffect(entity);
        }
        else {
             /*
            此处为具体的效果方法逻辑
            如果是用Task执行的效果，此处一般只用于与Effect交互（例如实现能解不能续或能续不能解）
            如果不用Task，理论上也可以在这里执行效果的逻辑
            但我不建议与Effect交互和具体效果写一起，以免逻辑没理顺导致时间失控等问题
             */
            int amplifier = entityHasEffect.get(uuid).getAmplifier();
            Integer taskID = entityHasEffect.get(uuid).getTaskId();
            DoOnTick(entity,durationTime,amplifier,taskID,uuid,expireTime);
            /*
             此处的处理为如果没找到实体对应的计划任务（例如退出重进导致计划任务丢失），补一个新的计划任务
             */
            ScheduledExecuteTask task = BaseImmortalEffectTask.getTaskFromID(taskID);
            if (task == null) {
                ImmortalersDelightMod.LOGGER.info("WTF怎么没找到计划任务？让我看看现在的实体表是什么玩意");
                for (Map.Entry<UUID, EffectData> entry : entityHasEffect.entrySet()) {
                    UUID uuid1 = entry.getKey();
                    EffectData effectData = entry.getValue();
                    ImmortalersDelightMod.LOGGER.info("UUID: " + uuid1 + ", Effect Level: " + effectData.getAmplifier() +
                            ", Duration: " + effectData.getTime() + ", Task ID " + effectData.getTaskId());
                }
                ScheduledExecuteTask newTask = buildEffectTask(taskID,entity, (double) durationTime / 1000,amplifier);
                newTask.start();
                System.out.println("计划任务异常！已创建新的计划任务");
                ImmortalersDelightMod.LOGGER.info("计划任务异常！已创建新的计划任务");
            }
        }
    }
    public static void DoOnTick (LivingEntity entity, Long durationTime, int amplifier, int taskID, UUID uuid, Long expireTime) {
        int durationTicks = (int) (durationTime * 20);
        /*
        这是能解不能续的实现，其原理为如果断Effect则解除Buff状态
        由于Task本身就是为了独立的持续时间而生，所以默认就是不能续的，只需要写一个解buff就好
         */
//        if (!entity.hasEffect(ImmortalersDelightMobEffect.LINGERING_FLAVOR.get())) {
//            removeImmortalEffect(entity);
//        }
        /*
        这是能续不能解的实现，其原理为如果Map内的实体没有对应Effect则一直给他补充Effect，使不能解除
        而如果有，则会在Effect和Buff状态的持续时间取其大来更新Map的记录，使续药水效果可以作用于Buff状态
         */
        if (!entity.hasEffect(ImmortalersDelightMobEffect.LINGERING_FLAVOR.get())) {
            ImmortalersDelightMod.LOGGER.info("effect被解除，正在刷新effect！");
            MobEffectInstance newOne = new MobEffectInstance(ImmortalersDelightMobEffect.LINGERING_FLAVOR.get(),durationTicks ,amplifier);
            entity.addEffect(newOne);
            return;
        }
        int lv = Objects.requireNonNull(entity.getEffect(ImmortalersDelightMobEffect.LINGERING_FLAVOR.get())).getAmplifier();
        int time = Objects.requireNonNull(entity.getEffect(ImmortalersDelightMobEffect.LINGERING_FLAVOR.get())).getDuration();
        ImmortalersDelightMod.LOGGER.info("目前的Effect时间是：" + time);
        ImmortalersDelightMod.LOGGER.info("目前的Effect等级是：" + lv);
        ImmortalersDelightMod.LOGGER.info("目前的Buff时间是：" + durationTicks);
        ImmortalersDelightMod.LOGGER.info("目前的Buff等级是：" + amplifier);
        ImmortalersDelightMod.LOGGER.info("目前的时间差是：" + (time - durationTicks <= 0 ? -(time - durationTicks) : time - durationTicks));
        if (lv > amplifier || (time - durationTicks <= 0 ? -(time - durationTicks) : time - durationTicks) > 50) {
            ImmortalersDelightMod.LOGGER.info("Buff被续，即将更新Map");
            Long maxTime = time > durationTicks ? TimekeepingTask.getImmortalTickTime() + time*50 : expireTime;
            int maxAmplifier = lv > amplifier ? lv : amplifier;
            EffectData effectData = new EffectData(entity.blockPosition(), maxTime, maxAmplifier, taskID);
            entityHasEffect.put(uuid,effectData);
        }
    }
    /**
     * 用于去除指定实体的特殊效果的方法
     * @param entity
     */
    public static void removeImmortalEffect(LivingEntity entity) {
        UUID uuid = entity.getUUID();
        onImmortalEffectRemove(entity);
        entityHasEffect.remove(uuid);
    }
    /**
     * 用于去除指定实体的特殊效果时进行善后的方法
     * @param entity
     */
    private static void onImmortalEffectRemove (LivingEntity entity) {
        if (entity == null || entity.isRemoved()) {
            return;
        }
        UUID uuid = entity.getUUID();
        /*
        在这里处理一些善后工作，这不是药水效果所以要记得手动善后
         */
        entity.removeEffect(ImmortalersDelightMobEffect.LINGERING_FLAVOR.get());
        if (entityHasEffect.get(uuid) == null) {return;}
        Integer taskID = entityHasEffect.get(uuid).getTaskId();
        ScheduledExecuteTask task = BaseImmortalEffectTask.getTaskFromID(taskID);
        if (task != null) {
            task.stop();
            task.cancel();
        }
    }

    /**
     * 监听世界加载事件，将存盘的实体Map读取出来
     * @param event
     */
    @SubscribeEvent
    public static void onWorldLoad(LevelEvent.Load event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            LoadEntityMap(serverLevel);
        }
    }

    /**
     * 这个方法用于修正时序以及从存盘数据中加载退出时的实体数据
     * @param serverLevel
     */
    private static void LoadEntityMap(Level serverLevel) {

        ImmortalersDelightMod.LOGGER.info("正在读取存盘的Effect实体Map");
        Map<UUID, EffectData> oldMap = BaseImmortalMapSaveLoadHelper.loadEntityHasEffect(serverLevel);
        if (oldMap == null) {
            ImmortalersDelightMod.LOGGER.info("存盘表为null！");
            pausing = false;
            return;
        }
        //entityHasEffect = new ConcurrentHashMap<>();
            /*
            遍历存盘的Map，将存盘的Map数据同步到新Map
             */
        for (Map.Entry<UUID, EffectData> entry : oldMap.entrySet()) {
            UUID uuidOld = entry.getKey();
            EffectData dataOld = entry.getValue();
            /*
            修正时刻，存盘的Map里存的时间理论上为[上次关服时间 + 剩余持续时间]，因此减去一个上次关服时间再加上当前时间即可保持持续时间不变
             */
            Long trueTime = dataOld.getTime() - ExitTimeSaveLoadHelper.loadExitTime(serverLevel) + TimekeepingTask.getImmortalTickTime();
            EffectData newOne = new EffectData(dataOld, trueTime, dataOld.getAmplifier(), dataOld.getTaskId());
            entityHasEffect.put(uuidOld,newOne);
        }
        ImmortalersDelightMod.LOGGER.info("存盘Map读取完毕，当前Effect实体Map为：");
        pausing = false;
        // 输出读取到的 Map
        for (Map.Entry<UUID, EffectData> entry : entityHasEffect.entrySet()) {
            UUID uuid = entry.getKey();
            EffectData effectData = entry.getValue();
            ImmortalersDelightMod.LOGGER.info("UUID: " + uuid + ", Effect Level: " + effectData.getAmplifier() +
                    ", Duration: " + effectData.getTime() + ", Task ID " + effectData.getTaskId());
        }
    }
    /**
     * 用于构建新的计划任务的方法
     * @param id
     * @param pLivingEntity
     * @param durationSeconds
     * @param amplifier
     * @return
     */
    public static ScheduledExecuteTask buildEffectTask(int id, LivingEntity pLivingEntity, double durationSeconds, int amplifier) {

        BaseImmortalEffectTask task = new BaseImmortalEffectTask(1,1,id,pLivingEntity, (long) durationSeconds,amplifier);
        task.start();
        return task;
    }
}

