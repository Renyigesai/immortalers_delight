package com.renyigesai.immortalers_delight.util.datautil.datasaveloadhelper;

import com.renyigesai.immortalers_delight.event.DifficultyModeHelper;
import com.renyigesai.immortalers_delight.util.datautil.datastorage.DifficultyDataStorage;
import com.renyigesai.immortalers_delight.util.datautil.worlddata.DifficultyModeWorldData;
import com.renyigesai.immortalers_delight.util.task.TimekeepingTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// 使用自定义数据存储的示例类
@Mod.EventBusSubscriber
public class DifficultyModeSaveLoadHelper {
    // 存储自定义信息的方法，由于此处为退出时间，理论上不需要也不应该在他处主动使用
    public static void saveDifficultyMode(Level level, Boolean info) {
        if (level instanceof ServerLevel serverLevel) {
            // 获取自定义世界数据实例
            DifficultyModeWorldData data = DifficultyModeWorldData.get(serverLevel);
            // 获取自定义数据存储实例
            DifficultyDataStorage customData = data.getCustomData();
            // 设置自定义信息
            customData.setPowerBattleMode(info);
            // 标记数据已更改，以便保存
            data.setDirty();
        }
    }

    // 读取自定义信息的方法
    public static Boolean loadDifficultyMode (Level level) {
        if (level instanceof ServerLevel serverLevel) {
            // 获取自定义世界数据实例
            DifficultyModeWorldData data = DifficultyModeWorldData.get(serverLevel);
            // 获取自定义数据存储实例
            DifficultyDataStorage customData = data.getCustomData();
            // 返回自定义信息
            return customData.getPowerBattleMode();
        }
        return null;
    }
    // 订阅世界保存事件，在此处保存游戏退出时的系统时间
    @SubscribeEvent
    public static void onWorldSave(LevelEvent.Save event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            saveDifficultyMode(serverLevel, DifficultyModeHelper.isPowerBattleMode());
        }
    }
}