package com.renyigesai.immortalers_delight.block.sextlotus_lantern;

import com.renyigesai.immortalers_delight.util.NBTListUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 月莲灯方块实体类，负责存储动态数据（光源块位置），
 * 处理光束射线检测、光源块创建/删除/移动，以及红石信号联动逻辑
 */
public class SextlotusLanternBlockEntity extends BlockEntity {

    public static final String LIGHT_SOURCE_POS_X = "light_source_pos_x";
    public static final String LIGHT_SOURCE_POS_Y = "light_source_pos_y";
    public static final String LIGHT_SOURCE_POS_Z = "light_source_pos_z";
    @Getter
    private List<BlockPos> lightSourcePoses = new ArrayList<>();
    public SextlotusLanternBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }
    //=======================基础功能部分，实现方块实体的通用逻辑并管理数据==========================//
    //读取NBT，在加载时从nbt读取需要额外Tick的坐标数据
    public void load(CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains(LIGHT_SOURCE_POS_X) && pTag.contains(LIGHT_SOURCE_POS_Y) && pTag.contains(LIGHT_SOURCE_POS_Z)) {
            List<Integer> listX = NBTListUtil.readIntListFromTag(pTag, LIGHT_SOURCE_POS_X);
            List<Integer> listY = NBTListUtil.readIntListFromTag(pTag, LIGHT_SOURCE_POS_Y);
            List<Integer> listZ = NBTListUtil.readIntListFromTag(pTag, LIGHT_SOURCE_POS_Z);
            if (listX.size() == listY.size() && listY.size() == listZ.size()) {
                for (int i = 0; i < listX.size(); i++) {
                    lightSourcePoses.add(new BlockPos(listX.get(i), listY.get(i), listZ.get(i)));
                }
            }

        }
    }

    //保存NBT，在卸载时将需要额外Tick的坐标数据保存为nbt
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        if (!lightSourcePoses.isEmpty()) {
            List<BlockPos> copy = new ArrayList<>(lightSourcePoses);
            List<Integer> x = new ArrayList<>();
            List<Integer> y = new ArrayList<>();
            List<Integer> z = new ArrayList<>();
            for (BlockPos pos : copy) {
                x.add(pos.getX());
                y.add(pos.getY());
                z.add(pos.getZ());
            }
            NBTListUtil.writeIntListToTag(pTag, LIGHT_SOURCE_POS_X, x);
            NBTListUtil.writeIntListToTag(pTag, LIGHT_SOURCE_POS_Y, y);
            NBTListUtil.writeIntListToTag(pTag, LIGHT_SOURCE_POS_Z, z);
        }
    }
}
