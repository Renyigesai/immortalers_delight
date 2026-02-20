package com.renyigesai.immortalers_delight.util;

import net.minecraft.nbt.*;

import java.util.ArrayList;
import java.util.List;

/**
 * CompoundTag 与 List<Integer> 互转工具类
 */
public class NBTListUtil {

    /**
     * 将 List<Integer> 封装到 CompoundTag 中
     * @param tag 目标 CompoundTag
     * @param key 存储的键名（自定义，如"int_list"）
     * @param intList 要存储的整数列表
     */
    public static void writeIntListToTag(CompoundTag tag, String key, List<Integer> intList) {
        // 1. 创建整数类型的 TagList（指定类型为 INT）
        ListTag tagList = new ListTag();

        // 2. 遍历原生列表，逐个转为 IntTag 并加入 TagList
        for (Integer num : intList) {
            if (num != null) { // 空值容错
                tagList.add(IntTag.valueOf(num));
            }
        }

        // 3. 将 TagList 存入 CompoundTag
        tag.put(key, tagList);
    }

    /**
     * 从 CompoundTag 中读取 List<Integer>
     * @param tag 源 CompoundTag
     * @param key 存储的键名（需与写入时一致）
     * @return 读取到的整数列表（标签不存在/类型错误时返回空列表，避免空指针）
     */
    public static List<Integer> readIntListFromTag(CompoundTag tag, String key) {
        List<Integer> intList = new ArrayList<>();

        // 1. 校验标签是否存在，且类型为 TagList
        if (!tag.contains(key, Tag.TAG_LIST)) {
            return intList; // 无该标签，返回空列表
        }

        // 2. 取出 TagList，并校验列表内标签类型为 INT
        ListTag tagList = tag.getList(key, Tag.TAG_INT);
        if (tagList.getElementType() != Tag.TAG_INT) {
            return intList; // 类型不匹配，返回空列表
        }

        // 3. 遍历 TagList，解析为 Integer 并加入原生列表
        for (Tag nbtTag : tagList) {
            if (nbtTag instanceof IntTag intTag) {
                intList.add(intTag.getAsInt());
            }
        }

        return intList;
    }
}
