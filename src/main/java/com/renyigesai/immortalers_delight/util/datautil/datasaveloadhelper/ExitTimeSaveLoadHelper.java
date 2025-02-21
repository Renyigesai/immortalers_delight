package com.renyigesai.immortalers_delight.util.datautil.datasaveloadhelper;

import com.renyigesai.immortalers_delight.util.task.TimekeepingTask;
import com.renyigesai.immortalers_delight.util.datautil.datastorage.ExitTimeDataStorage;
import com.renyigesai.immortalers_delight.util.datautil.worlddata.ExitTimeWorldData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// 使用自定义数据存储的示例类
@Mod.EventBusSubscriber
public class ExitTimeSaveLoadHelper {
    // 存储自定义信息的方法，由于此处为退出时间，理论上不需要也不应该在他处主动使用
    public static void saveExitTime (Level level, Long info) {
        if (level instanceof ServerLevel serverLevel) {
            // 获取自定义世界数据实例
            ExitTimeWorldData data = ExitTimeWorldData.get(serverLevel);
            // 获取自定义数据存储实例
            ExitTimeDataStorage customData = data.getCustomData();
            // 设置自定义信息
            customData.setExitTime(info);
            // 标记数据已更改，以便保存
            data.setDirty();
        }
    }

    // 读取自定义信息的方法
    public static Long loadExitTime (Level level) {
        if (level instanceof ServerLevel serverLevel) {
            // 获取自定义世界数据实例
            ExitTimeWorldData data = ExitTimeWorldData.get(serverLevel);
            // 获取自定义数据存储实例
            ExitTimeDataStorage customData = data.getCustomData();
            // 返回自定义信息
            return customData.getExitTime();
        }
        return null;
    }
    // 订阅世界保存事件，在此处保存游戏退出时的系统时间
    @SubscribeEvent
    public static void onWorldSave(LevelEvent.Save event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            saveExitTime(serverLevel, TimekeepingTask.getImmortalTickTime());
        }
    }
}