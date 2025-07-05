package com.renyigesai.immortalers_delight.util.datautil.worlddata;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.util.datautil.datastorage.DifficultyDataStorage;
import com.renyigesai.immortalers_delight.util.datautil.datastorage.ExitTimeDataStorage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// 自定义世界数据存储管理器类
@Mod.EventBusSubscriber
public class DifficultyModeWorldData extends SavedData {

    private static final String DIFFICULTY_MODE_DATA_NAME = ImmortalersDelightMod.MODID + "power_battle_mode_data";
    // 自定义数据存储实例
    private final DifficultyDataStorage customData = new DifficultyDataStorage();
    // 数据存储的名称
    private static final String DIFFICULTY_MODE_WORLD_DATA_NAME = ImmortalersDelightMod.MODID + "power_battle_mode_world_data";

    // 构造函数
    public DifficultyModeWorldData() {
    }

    // 从 NBT 标签加载数据的静态方法
    public static DifficultyModeWorldData load(CompoundTag nbt) {
        DifficultyModeWorldData data = new DifficultyModeWorldData();
        // 从 NBT 标签中读取自定义数据
        data.customData.deserializeNBT(nbt.getCompound(DIFFICULTY_MODE_DATA_NAME));
        return data;
    }

    // 将数据保存到 NBT 标签的方法
    @Override
    public CompoundTag save(CompoundTag nbt) {
        // 将自定义数据写入 NBT 标签
        nbt.put(DIFFICULTY_MODE_DATA_NAME, customData.serializeNBT());
        return nbt;
    }

    // 获取自定义数据存储实例的方法
    public DifficultyDataStorage getCustomData() {
        return customData;
    }

    // 订阅世界加载事件，在此处加载所保存的数据
    @SubscribeEvent
    public static void onWorldLoad(LevelEvent.Load event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            DimensionDataStorage storage = serverLevel.getDataStorage();
            // 获取或创建自定义世界数据实例
            DifficultyModeWorldData data = storage.computeIfAbsent(DifficultyModeWorldData::load, DifficultyModeWorldData::new, DIFFICULTY_MODE_WORLD_DATA_NAME);
        }
    }

    // 获取自定义世界数据实例的静态方法
    public static DifficultyModeWorldData get(ServerLevel level) {
        DimensionDataStorage storage = level.getDataStorage();
        return storage.computeIfAbsent(DifficultyModeWorldData::load, DifficultyModeWorldData::new, DIFFICULTY_MODE_WORLD_DATA_NAME);
    }
}