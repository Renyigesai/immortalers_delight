package com.renyigesai.immortalers_delight.util.datautil.datasaveloadhelper;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.potion.immortaleffects.InebriatedEffect;
import com.renyigesai.immortalers_delight.util.datautil.EffectData;
import com.renyigesai.immortalers_delight.util.datautil.datastorage.InebriatedDataStorage;
import com.renyigesai.immortalers_delight.util.datautil.worlddata.InebriatedWorldData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

// 使用自定义数据存储的示例类
@Mod.EventBusSubscriber
public class InebriatedMapSaveLoadHelper {
    // 存储自定义信息的方法
    public static void saveEntityHasEffect (Level level, Map<UUID, EffectData> entitiesHasInebriatedEffect) {
        if (level instanceof ServerLevel serverLevel) {
            // 获取自定义世界数据实例
            InebriatedWorldData data = InebriatedWorldData.get(serverLevel);
            // 获取自定义数据存储实例
            InebriatedDataStorage customData = data.getCustomData();
            Map<UUID,EffectData> copyMap = new ConcurrentHashMap<>(entitiesHasInebriatedEffect);
            // 设置自定义信息
            customData.setEntitiesHasInebriatedEffect(copyMap);
            // 标记数据已更改，以便保存
            data.setDirty();
        }
    }

    // 读取自定义信息的方法
    public static Map<UUID, EffectData> loadEntityHasEffect (Level level) {
        if (level instanceof ServerLevel serverLevel) {
            // 获取自定义世界数据实例
            InebriatedWorldData data = InebriatedWorldData.get(serverLevel);
            // 获取自定义数据存储实例
            InebriatedDataStorage customData = data.getCustomData();
            // 返回自定义信息
            return customData.getEntitiesHasInebriatedEffect();
        }
        return null;
    }

    // 订阅世界保存事件，在此处保存游戏退出时的系统时间
    @SubscribeEvent
    public static void onWorldSave(LevelEvent.Save event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            saveEntityHasEffect(serverLevel, InebriatedEffect.getEntityMap());
//            ImmortalersDelightMod.LOGGER.info("这里是世界保存，正在保存实体Map");
            // 输出读取到的 Map
            for (Map.Entry<UUID, EffectData> entry : InebriatedEffect.getEntityMap().entrySet()) {
                UUID uuid = entry.getKey();
                EffectData effectData = entry.getValue();
//                ImmortalersDelightMod.LOGGER.info("UUID: " + uuid + ", Effect Level: " + effectData.getAmplifier() +
//                        ", Duration: " + effectData.getTime() + ", Task ID " + effectData.getTaskId());
            }
            InebriatedEffect.setPausing(true);
        }
    }
}