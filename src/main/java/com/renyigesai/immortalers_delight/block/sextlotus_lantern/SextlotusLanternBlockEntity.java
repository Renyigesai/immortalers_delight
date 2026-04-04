package com.renyigesai.immortalers_delight.block.sextlotus_lantern;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import com.renyigesai.immortalers_delight.util.NBTListUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

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
    private List<BlockPos> lightSourcePoses = new ArrayList<>();
    public List<BlockPos> getLightSourcePos() {return lightSourcePoses;}
    private final List<Vec3> defaultLightSourcePos = new ArrayList<>();
    public SextlotusLanternBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ImmortalersDelightBlocks.SEXTLOTUS_LANTERN_BLOCK_ENTITY.get(), pPos, pBlockState);
        // 初始化光束位置
        defaultLightSourcePos.add(new Vec3(2, 0, 0));
        defaultLightSourcePos.add(new Vec3(-2, 0, 0));
        defaultLightSourcePos.add(new Vec3(0, 2, 0));
        defaultLightSourcePos.add(new Vec3(0, -2, 0));
        defaultLightSourcePos.add(new Vec3(0, 0, 2));
        defaultLightSourcePos.add(new Vec3(0, 0, -2));

        defaultLightSourcePos.add(new Vec3(1, 1, 0));
        defaultLightSourcePos.add(new Vec3(-1, 1, 0));
        defaultLightSourcePos.add(new Vec3(0, 1, 1));
        defaultLightSourcePos.add(new Vec3(0, 1, -1));

        defaultLightSourcePos.add(new Vec3(1, 0, 1));
        defaultLightSourcePos.add(new Vec3(1, 0, -1));
        defaultLightSourcePos.add(new Vec3(-1, 0, 1));
        defaultLightSourcePos.add(new Vec3(-1, 0, -1));

        defaultLightSourcePos.add(new Vec3(1, -1, 0));
        defaultLightSourcePos.add(new Vec3(-1, -1, 0));
        defaultLightSourcePos.add(new Vec3(0, -1, 1));
        defaultLightSourcePos.add(new Vec3(0, -1, -1));
    }
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

    /**
     * 初始化区块数据时导出NBT（确保区块加载时实体数据完整）
     * @return 包含实体数据的NBT复合标签
     */
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        saveAdditional(tag);
        return tag;
    }

    //=======================核心功能部分，实现方块实体的功能逻辑==========================//
//
//    /**
//     * 删除当前关联的光源块
//     * @return true=光源块成功删除（且确认为探照灯光源块）；false=无有效光源块/删除失败
//     */
//    public boolean deleteLightSource() {
//
//    }
//    /**
//     * 关闭光源块（红石供电时触发）
//     * 逻辑：删除光源块 → 更新探照灯红石供电状态 → 保留光源坐标（便于重新开启）
//     * @return true=关闭成功；false=无光源块/状态更新失败
//     */
//    public boolean turnOffLightSource() {
//    }
//    /**
//     * 开启光源块（红石断电时触发）
//     * 逻辑：更新探照灯红石状态 → 若原光源位置为空则重建；否则重新射线检测生成新光源
//     * @return true=开启成功；false=无光源坐标/状态更新失败
//     */
//    public boolean turnOnLightSource() {
//    }
//    /**
//     * 射线检测并放置新的光源块
//     * 1. 归一化光束方向 → 2. 计算有效光源位置 → 3. 放置光源块
//     * @param beamDirection 目标光束方向向量
//     * @return true=新光源块放置成功；false=无有效位置/放置失败
//     */
//    public boolean raycastAndPlaceLightSource(@NotNull Vec3 beamDirection) {
//
//    }
//    /**
//     * 放置新光源块（核心逻辑）
//     * 1. 删除旧光源 → 2. 校验新位置 → 3. 放置光源块 → 4. 关联光源块与探照灯
//     * @param newLightPos 新光源块坐标（null则直接清空光源）
//     * @return true=新光源块成功放置；false=位置无效/放置失败
//     */
//    public boolean placeLightSource(@Nullable BlockPos newLightPos) {
//
//    }
//
//    /**
//     * 射线检测计算有效光源块位置（核心算法）
//     * 逻辑：从探照灯出发沿光束方向遍历，找到符合条件的最远空气方块：
//     * 1. 穿透透光方块（楼梯、树叶等，按原版透光规则）
//     * 2. 不超出世界建造边界
//     * 3. 不进入未加载区块
//     * 4. 避开流体方块
//     * @param beamDirection 归一化的光束方向向量
//     * @return 有效光源块坐标（null=无有效位置）
//     */
//    public @Nullable BlockPos calculateLightSourcePosition(@NotNull Vec3 beamDirection) {
//
//    }

    public List<BlockPos> getLightSourcePoses() {
        return lightSourcePoses;
    }
}
