package com.renyigesai.immortalers_delight.util.datautil.datastorage;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

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
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putLong(EXIT_TIME_INFO_NAME, exitTime);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        this.exitTime = nbt.getLong(EXIT_TIME_INFO_NAME);
    }

}