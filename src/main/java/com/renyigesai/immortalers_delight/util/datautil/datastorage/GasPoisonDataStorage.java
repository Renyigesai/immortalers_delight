package com.renyigesai.immortalers_delight.util.datautil.datastorage;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.util.datautil.EffectData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
//
//// 自定义数据存储类，实现 INBTSerializable 接口
//public class GasPoisonDataStorage implements INBTSerializable<CompoundTag> {
//    private static final String GAS_POISON_INFO_NAME = ImmortalersDelightMod.MODID + "gas_poison_info";
//    // 要存储的自定义信息
//    private Map<UUID, EffectData> entitiesHasGasPoisonEffect;
//
//    // 获取自定义信息的方法
//    public Map<UUID, EffectData> getEntitiesHasGasPoisonEffect() {
//        return entitiesHasGasPoisonEffect;
//    }
//
//    // 设置自定义信息的方法
//    public void setEntitiesHasGasPoisonEffect(Map<UUID, EffectData> entitiesHasGasPoisonEffect) {
//        this.entitiesHasGasPoisonEffect = entitiesHasGasPoisonEffect;
//    }
//
//    // 将数据写入 NBT 标签的方法
//    @Override
//    public CompoundTag serializeNBT() {
//        CompoundTag tag = new CompoundTag();
//        // 将自定义信息写入 NBT 标签
//        saveEffectMapToCompoundTag(entitiesHasGasPoisonEffect, tag, GAS_POISON_INFO_NAME);
//        return tag;
//    }
//
//    // 从 NBT 标签读取数据的方法
//    @Override
//    public void deserializeNBT(CompoundTag nbt) {
//        // 从 NBT 标签中读取自定义信息
//        this.entitiesHasGasPoisonEffect = loadEffectMapFromCompoundTag(nbt, GAS_POISON_INFO_NAME);
//    }
//
//
//    /**
//     * 将 Map<UUID, EffectData> 保存到 CompoundTag 中
//     * @param map 要保存的 Map
//     * @return 包含 Map 数据的 CompoundTag
//     */
//    public static CompoundTag saveEffectMapToCompoundTag(Map<UUID, EffectData> map, CompoundTag compoundTag, String effectID) {
//        ListTag uuidList = new ListTag();
//        ListTag effectDataList = new ListTag();
//
//        // 遍历 Map 的键值对
//        for (Map.Entry<UUID, EffectData> entry : map.entrySet()) {
//            UUID uuid = entry.getKey();
//            EffectData effectData = entry.getValue();
//
//            // 将 UUID 作为字符串添加到 UUID 列表中
//            uuidList.add(StringTag.valueOf(uuid.toString()));
//            // 将 EffectData 保存到 CompoundTag 并添加到效果数据列表中
//            effectDataList.add(effectData.saveToNBT(effectID));
//        }
//
//        // 将 UUID 列表和效果数据列表保存到 CompoundTag 中
//        compoundTag.put(effectID + "uuids", uuidList);
//        compoundTag.put(effectID + "effectDataList", effectDataList);
//
//        return compoundTag;
//    }
//
//    /**
//     * 从 CompoundTag 中读取 Map<UUID, EffectData>
//     * @param compoundTag 包含 Map 数据的 CompoundTag
//     * @return 读取到的 Map
//     */
//    public static Map<UUID, EffectData> loadEffectMapFromCompoundTag(CompoundTag compoundTag, String effectID) {
//        Map<UUID, EffectData> map = new HashMap<>();
//
//        // 从 CompoundTag 中获取 UUID 列表和效果数据列表
//        ListTag uuidList = compoundTag.getList(effectID + "uuids", Tag.TAG_STRING);
//        ListTag effectDataList = compoundTag.getList(effectID + "effectDataList", Tag.TAG_COMPOUND);
//
//        // 确保 UUID 列表和效果数据列表的长度相同
//        if (uuidList.size() == effectDataList.size()) {
//            for (int i = 0; i < uuidList.size(); i++) {
//                // 获取 UUID
//                UUID uuid = UUID.fromString(uuidList.getString(i));
//                // 从效果数据列表中获取对应的 CompoundTag 并加载 EffectData
//                CompoundTag effectDataTag = effectDataList.getCompound(i);
//                EffectData effectData = EffectData.loadFromNBT(effectDataTag,effectID);
//
//                // 将键值对添加到 Map 中
//                map.put(uuid, effectData);
//            }
//        }
//
//        return map;
//    }
//}