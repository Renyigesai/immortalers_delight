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

// 自定义数据存储类，实现 INBTSerializable 接口
public class ExitTimeDataStorage implements INBTSerializable<CompoundTag> {
    private static final String EXIT_TIME_INFO_NAME = ImmortalersDelightMod.MODID + "exit_time_info";
    // 要存储的自定义信息
    private Long exitTime;

    // 获取自定义信息的方法
    public Long getExitTime() {
        return exitTime;
    }

    // 设置自定义信息的方法
    public void setExitTime(Long exitTime) {
        this.exitTime = exitTime;
    }

    // 将数据写入 NBT 标签的方法
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        // 将自定义信息写入 NBT 标签
        tag.putLong(EXIT_TIME_INFO_NAME, exitTime);
        return tag;
    }

    // 从 NBT 标签读取数据的方法
    @Override
    public void deserializeNBT(CompoundTag nbt) {
        // 从 NBT 标签中读取自定义信息
        this.exitTime = nbt.getLong(EXIT_TIME_INFO_NAME);
    }

}