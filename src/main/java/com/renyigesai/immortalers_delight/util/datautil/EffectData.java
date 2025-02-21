package com.renyigesai.immortalers_delight.util.datautil;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

public class EffectData extends BlockPos
{
    /**
     * 存储分支基础高度
     */
    private final Long time;
    private final int amplifier;
    private final int taskId;

    /**
     * 构造函数，调用父类（BlockPos）构造函数初始化坐标，并设置分支基础高度
     */
    public EffectData(BlockPos pos, Long time)
    {
        super(pos.getX(), pos.getY(), pos.getZ());
        this.time = time;
        this.amplifier = 0;
        this.taskId = 0;
    }
    public EffectData(BlockPos pos, Long time, int amplifier)
    {
        super(pos.getX(), pos.getY(), pos.getZ());
        this.time = time;
        this.amplifier = amplifier;
        this.taskId = 0;
    }
    public EffectData(BlockPos pos, Long time, int amplifier, int taskId)
    {
        super(pos.getX(), pos.getY(), pos.getZ());
        this.time = time;
        this.amplifier = amplifier;
        this.taskId = taskId;
    }

    /**
     * 获取储存的内部数据的方法
     */
    public Long getTime()
    {
        return this.time;
    }
    public int getAmplifier()
    {
        return this.amplifier;
    }
    public int getTaskId()
    {
        return this.taskId;
    }

    /**
     * 将 EffectData 的数据保存到 CompoundTag 中
     * @return 包含 EffectData 数据的 CompoundTag
     */
    public CompoundTag saveToNBT(String effectID) {
        CompoundTag tag = new CompoundTag();
        tag.putLong(effectID + "position", this.asLong());
        tag.putLong(effectID + "duration", this.getTime());
        tag.putInt(effectID + "amplifier", this.getAmplifier());
        tag.putInt(effectID + "task", this.getTaskId());
        return tag;
    }

    /**
     * 从 CompoundTag 中读取数据并初始化 EffectData
     * @param tag 包含 EffectData 数据的 CompoundTag
     * @return 初始化后的 EffectData 对象
     */
    public static EffectData loadFromNBT(CompoundTag tag, String effectID) {
        BlockPos position = BlockPos.of(tag.getLong(effectID + "position"));
        long duration = tag.getLong(effectID + "duration");
        int amplifier = tag.getInt(effectID + "amplifier");
        int taskID = tag.getInt(effectID + "task");
        return new EffectData(position, duration, amplifier, taskID);
    }
}
