package com.renyigesai.immortalers_delight.block.sextlotus_lantern;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class SextlotusLightHelper {
    /**
     * 类型安全地转换 BlockEntity 并执行消费逻辑，转换失败时记录错误日志
     * @param blockEntity 待转换的方块实体（可为 null）
     * @param blockPos    方块实体所在坐标（用于日志定位）
     * @param result      转换成功后执行的消费逻辑（入参为目标类型的 BlockEntity）
     * @param <T>         目标 BlockEntity 类型
     * @return 转换并执行成功返回 true；转换失败/方块实体无效返回 false
     */
    public static <@Nullable T extends BlockEntity> boolean castBlockEntity(@Nullable BlockEntity blockEntity, @NotNull BlockPos blockPos, @NotNull Consumer<T> result)
    {
        // 空值校验：方块实体为 null 时记录错误并返回
        if (blockEntity == null)
        {
            try
            {
                throw new IllegalStateException();
            }
            catch (Exception e)
            {
                ImmortalersDelightMod.LOGGER.error("Attempted to cast a null blockEntity at " + blockPos, e);
            }
            return false;
        }
        // 世界校验：方块实体未关联世界时记录错误并返回
        if (!blockEntity.hasLevel())
        {
            try
            {
                throw new IllegalStateException();
            }
            catch (Exception e)
            {
                ImmortalersDelightMod.LOGGER.error(
                        String.format("Attempted to use a blockEntity '%s' (%s) at %s with world==null.",
                                blockEntity,
                                blockEntity.getClass(),
                                blockPos), e);
            }
            return false;
        }
        // 类型转换：失败时记录错误日志
        try
        {
            //noinspection unchecked
            result.accept((T) blockEntity);
            return true;
        }
        catch (ClassCastException ex)
        {
            ImmortalersDelightMod.LOGGER.error(
                    String.format("Attempted to cast '%s' (%s) at %s but failed",
                            blockEntity,
                            blockEntity.getClass(),
                            blockPos),
                    ex);
            return false;
        }
    }


    /**
     * 仅在区块已加载时获取方块状态，未加载/超出建造限制时返回 VOID_AIR
     * 适用场景：非核心逻辑（如射线检测），避免触发区块强制加载
     * @param level    世界实例
     * @param blockPos 方块坐标
     * @return 已加载区块返回实际方块状态；未加载/超限返回 VOID_AIR
     */
    public static @NotNull BlockState getBlockStateIfLoaded(Level level, BlockPos blockPos) {
        if (level.isOutsideBuildHeight(blockPos))
            return Blocks.AIR.defaultBlockState();
        BlockGetter chunk = level.getChunkForCollisions(blockPos.getX() >> 4, blockPos.getZ() >> 4);
        if (chunk == null)
            return Blocks.VOID_AIR.defaultBlockState();

        return chunk.getBlockState(blockPos);
    }


    /**
     * 将光源位置向空方块方向偏移，远离墙面/地面/天花板
     * 解决光源与固体方块重叠的问题，保证光源放置在空气方块中
     * @param level    世界实例
     * @param blockPos 原始光源位置（可为 null）
     * @return 调整后的光源位置；入参为 null 时返回 null
     */
    public static BlockPos moveAwayFromSurfaces(Level level, BlockPos blockPos) {
        if (blockPos == null) return null;

        BlockPos resultPos = blockPos.immutable();

        // X轴方向调整：避免嵌入前后/左右墙面
        if (!level.getBlockState(resultPos.offset(-1, 0, 0)).isAir() && level.getBlockState(resultPos.offset(1, 0, 0)).isAir())
            resultPos = resultPos.offset(1, 0, 0);
        else if (!level.getBlockState(resultPos.offset(1, 0, 0)).isAir() && level.getBlockState(resultPos.offset(-1, 0, 0)).isAir())
            resultPos = resultPos.offset(-1, 0, 0);

        // Y轴方向调整：避免嵌入地面/天花板
        if (!level.getBlockState(resultPos.offset(0, -1, 0)).isAir() && level.getBlockState(resultPos.offset(0, 1, 0)).isAir())
            resultPos = resultPos.offset(0, 1, 0);
        else if (!level.getBlockState(resultPos.offset(0, 1, 0)).isAir() && level.getBlockState(resultPos.offset(0, -1, 0)).isAir())
            resultPos = resultPos.offset(0, -1, 0);

        // Z轴方向调整：避免嵌入前后/左右墙面
        if (!level.getBlockState(resultPos.offset(0, 0, -1)).isAir() && level.getBlockState(resultPos.offset(0, 0, 1)).isAir())
            resultPos = resultPos.offset(0, 0, 1);
        else if (!level.getBlockState(resultPos.offset(0, 0, 1)).isAir() && level.getBlockState(resultPos.offset(0, 0, -1)).isAir())
            resultPos = resultPos.offset(0, 0, -1);

        return resultPos;
    }
}
