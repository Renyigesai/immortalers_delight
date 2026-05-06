package com.renyigesai.immortalers_delight.util.datautil.datastorage;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

// 自定义数据存储类，实现 INBTSerializable 接口
public class DifficultyDataStorage implements INBTSerializable<CompoundTag> {
    private static final String DIFFICULTY_MODE_INFO_NAME = ImmortalersDelightMod.MODID + "power_battle_mode_info";
    // 要存储的自定义信息
    private Boolean isPowerBattleMode = false;

    // 获取自定义信息的方法
    public boolean getPowerBattleMode() {
        return isPowerBattleMode;
    }

    // 设置自定义信息的方法
    public void setPowerBattleMode(boolean exitTime) {
        this.isPowerBattleMode = exitTime;
    }

    // 将数据写入 NBT 标签的方法
    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean(DIFFICULTY_MODE_INFO_NAME, isPowerBattleMode);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        this.isPowerBattleMode = nbt.getBoolean(DIFFICULTY_MODE_INFO_NAME);
    }

}