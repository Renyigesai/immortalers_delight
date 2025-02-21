package com.renyigesai.immortalers_delight.util.datautil.datasaveloadhelper;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.potion.immortaleffects.BaseImmortalEffect;
import com.renyigesai.immortalers_delight.util.datautil.EffectData;
import com.renyigesai.immortalers_delight.util.datautil.datastorage.BaseImmortalDataStorage;
import com.renyigesai.immortalers_delight.util.datautil.worlddata.BaseImmortalWorldData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
import java.util.UUID;

// 使用自定义数据存储的示例类
@Mod.EventBusSubscriber
public class BaseImmortalMapSaveLoadHelper {
    // 存储自定义信息的方法
    public static void saveEntityHasEffect (Level level, Map<UUID, EffectData> entitiesHasBaseImmortalEffect) {
        if (level instanceof ServerLevel serverLevel) {
            // 获取自定义世界数据实例
            BaseImmortalWorldData data = BaseImmortalWorldData.get(serverLevel);
            // 获取自定义数据存储实例
            BaseImmortalDataStorage customData = data.getCustomData();
            // 设置自定义信息
            customData.setEntitiesHasBaseImmortalEffect(entitiesHasBaseImmortalEffect);
            // 标记数据已更改，以便保存
            data.setDirty();
        }
    }

    // 读取自定义信息的方法
    public static Map<UUID, EffectData> loadEntityHasEffect (Level level) {
        if (level instanceof ServerLevel serverLevel) {
            // 获取自定义世界数据实例
            BaseImmortalWorldData data = BaseImmortalWorldData.get(serverLevel);
            // 获取自定义数据存储实例
            BaseImmortalDataStorage customData = data.getCustomData();
            // 返回自定义信息
            return customData.getEntitiesHasBaseImmortalEffect();
        }
        return null;
    }

    // 订阅世界保存事件，在此处保存游戏退出时的系统时间
    @SubscribeEvent
    public static void onWorldSave(LevelEvent.Save event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            saveEntityHasEffect(serverLevel, BaseImmortalEffect.getEntityMap());
            ImmortalersDelightMod.LOGGER.info("这里是世界保存，正在保存实体Map");
            // 输出读取到的 Map
            for (Map.Entry<UUID, EffectData> entry : BaseImmortalEffect.getEntityMap().entrySet()) {
                UUID uuid = entry.getKey();
                EffectData effectData = entry.getValue();
                ImmortalersDelightMod.LOGGER.info("UUID: " + uuid + ", Effect Level: " + effectData.getAmplifier() +
                        ", Duration: " + effectData.getTime() + ", Task ID " + effectData.getTaskId());
            }
            BaseImmortalEffect.setPausing(true);
        }
    }
}