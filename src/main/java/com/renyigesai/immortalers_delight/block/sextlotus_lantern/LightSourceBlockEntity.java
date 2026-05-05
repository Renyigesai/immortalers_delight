package com.renyigesai.immortalers_delight.block.sextlotus_lantern;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class LightSourceBlockEntity  extends BlockEntity {
    /**
     * 关联的探照灯本体所在坐标
     * 可为 null（如光源块未正确绑定探照灯时）
     */
    public @Nullable BlockPos lanternBlockPos;

    public static final String LANTERN_POS_X = "lantern_pos_x";
    public static final String LANTERN_POS_Y = "lantern_pos_y";
    public static final String LANTERN_POS_Z = "lantern_pos_z";
    public LightSourceBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ImmortalersDelightBlocks.MOONLIGHT_LIGHT_SOURCE_BLOCK_ENTITY.get(), pPos, pBlockState);
    }
    public LightSourceBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }
    //=======================基础功能部分，实现方块实体的通用逻辑并管理数据==========================//
    //读取NBT，在加载时从nbt读取需要额外Tick的坐标数据
    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider registries) {
        super.loadAdditional(pTag, registries);
        if (pTag.contains(LANTERN_POS_X, Tag.TAG_INT) && pTag.contains(LANTERN_POS_Y, Tag.TAG_INT) && pTag.contains(LANTERN_POS_Z, Tag.TAG_INT)) {
            lanternBlockPos = new BlockPos(pTag.getInt(LANTERN_POS_X), pTag.getInt(LANTERN_POS_Y), pTag.getInt(LANTERN_POS_Z));

        } else {
            lanternBlockPos = null;
        }
    }

    //保存NBT，在卸载时将需要额外Tick的坐标数据保存为nbt
    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider registries) {
        super.saveAdditional(pTag, registries);
        if (!(lanternBlockPos == null)) {
            pTag.putInt(LANTERN_POS_X, lanternBlockPos.getX());
            pTag.putInt(LANTERN_POS_Y, lanternBlockPos.getY());
            pTag.putInt(LANTERN_POS_Z, lanternBlockPos.getZ());
        }
    }

    /**
     * 初始化区块数据时导出NBT（确保区块加载时实体数据完整）
     * @return 包含实体数据的NBT复合标签
     */
    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        saveAdditional(tag, registries);
        return tag;
    }

    //=======================核心功能部分，实现方块实体的功能逻辑==========================//
    /**
     * 光源块被替换/破坏时触发的核心逻辑：重新计算并移动光源位置
     * 调用时机：{SearchlightLightSourceBlock#onStateReplaced} 中触发
     * 核心行为：
     * 1. 校验世界环境（仅服务端执行）和探照灯绑定关系
     * 2. 获取关联的探照灯实体，触发其重新放置光源的逻辑
     */
//    public void moveLightSource()
//    {
//        // 客户端/无探照灯绑定/无世界时直接返回，不执行逻辑
//        if (this.level == null || this.level.isClientSide || lanternBlockPos == null)
//            return;
//
//        // 尝试获取关联的探照灯实体，触发其重新放置光源
//        SextlotusLightHelper.castBlockEntity(this.level.getBlockEntity(lanternBlockPos), lanternBlockPos, (SextlotusLanternBlockEntity searchlightBlockEntity) -> {
//            // 校验当前光源块是否仍与探照灯绑定，避免无效操作
//            if (searchlightBlockEntity.getLightSourcePos().contains(this.getBlockPos()))
//                // 反向射线检测（从光源到探照灯），计算新光源位置并放置
//                searchlightBlockEntity.placeLightSource(calculateLightSourcePosition(searchlightBlockEntity.getBeamDirection().multiply(-1)));
//        });
//    }
//
//    /**
//     * 从当前光源位置反向射线检测，计算新的合法光源位置
//     * 与 {@link SearchlightBlockEntity#calculateLightSourcePosition} 区别：
//     * - 本方法从光源位置向探照灯方向迭代
//     * - 后者从探照灯位置向光束方向迭代
//     *
//     * @param direction 射线检测方向（从光源指向探照灯的归一化向量）
//     * @return 新的合法光源位置（空气方块）；若超出建造限制/未加载区块则返回null
//     */
//    public @Nullable BlockPos calculateLightSourcePosition(@NotNull Vec3d direction)
//    {
//        // 归一化方向向量，确保射线步长一致
//        direction = direction.normalize();
//        ChunkManager chunkManager = world.getChunkManager();
//
//        // 初始化当前位置（光源块中心），使用可变向量避免频繁创建对象
//        MutableVector3d currentBlockPosD = new MutableVector3d(getPos().getX() + 0.5, getPos().getY() + 0.5, getPos().getZ() + 0.5);
//        BlockPos.Mutable currentBlockPos = new BlockPos.Mutable(currentBlockPosD.x, currentBlockPosD.y, currentBlockPosD.z);
//        BlockPos.Mutable prevBlockPos = new BlockPos.Mutable(0, 0, 0);
//
//        BlockPos.Mutable currentChunkPos = new BlockPos.Mutable(0, 0, 0);
//        BlockPos.Mutable prevChunkPos = new BlockPos.Mutable(0, 0, 0);
//
//        while (true)
//        {
//            // 记录上一位置，更新当前位置
//            prevBlockPos.set(currentBlockPos);
//            currentBlockPosD.add(direction);
//            currentBlockPos.set(currentBlockPosD.x, currentBlockPosD.y, currentBlockPosD.z);
//
//            // 位置未变化时跳过（避免无限循环）
//            if (prevBlockPos.equals(currentBlockPos))
//                continue;
//
//            // 超出世界建造限制，返回null
//            if (!world.isInBuildLimit(currentBlockPos))
//                return null;
//
//            // 检测区块是否加载：跨区块时若新区块未加载则返回null
//            prevChunkPos.set(prevBlockPos.getX() >> 4, 0, prevBlockPos.getZ() >> 4);
//            currentChunkPos.set(currentBlockPos.getX() >> 4, 0, currentBlockPos.getZ() >> 4);
//            if (!prevChunkPos.equals(currentChunkPos) && !chunkManager.isChunkLoaded(currentChunkPos.getX(), currentChunkPos.getZ()))
//                return null;
//
//            // 到达探照灯位置，返回null（避免光源与探照灯重叠）
//            if (currentBlockPos.equals(searchlightBlockPos))
//                return null;
//
//            // 找到空气方块，调整位置（远离表面）后返回
//            if (SearchlightUtil.getBlockStateIfLoaded(world, currentBlockPos).isAir())
//                return SearchlightUtil.moveAwayFromSurfaces(world, currentBlockPos);
//        }
//    }
}
