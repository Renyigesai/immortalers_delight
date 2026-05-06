package com.renyigesai.immortalers_delight.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class WorldUtils {
    /*通过输入资源地址获取一个战利品表*/
    public static LootTable getLootTables(String name,Level world){
        if (!world.isClientSide() && world.getServer() != null) {
            ResourceLocation id = ResourceLocation.parse(name);
            return world.getServer().reloadableRegistries().getLootTable(ResourceKey.create(Registries.LOOT_TABLE, id));
        }
        return LootTable.lootTable().build();
    }

    /**
     * Rolls a loot table at {@code pos}. When {@code thisEntity} is non-null (e.g. sniffer), uses {@link LootContextParamSets#GIFT}
     * like vanilla sniffer digging so gameplay/item pools resolve correctly.
     */
    public static List<ItemStack> getFromLootTableItemStack(LootTable lootTable, Level world, BlockPos pos, @Nullable Entity thisEntity) {
        List<ItemStack> stacks = new ArrayList<>();
        if (!world.isClientSide() && world.getServer() != null) {
            ServerLevel sl = (ServerLevel) world;
            LootParams.Builder builder = new LootParams.Builder(sl)
                    .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos));
            if (thisEntity != null) {
                builder.withParameter(LootContextParams.THIS_ENTITY, thisEntity);
                stacks.addAll(lootTable.getRandomItems(builder.create(LootContextParamSets.GIFT)));
            } else {
                stacks.addAll(lootTable.getRandomItems(builder
                        .withParameter(LootContextParams.BLOCK_STATE, world.getBlockState(pos))
                        .withOptionalParameter(LootContextParams.BLOCK_ENTITY, world.getBlockEntity(pos))
                        .create(LootContextParamSets.EMPTY)));
            }
        }
        return stacks;
    }

    /** @deprecated Prefer {@link #getFromLootTableItemStack(LootTable, Level, BlockPos, Entity)} from sniffer flows. */
    @Deprecated
    public static List<ItemStack> getFromLootTableItemStack(LootTable lootTable, Level world, BlockPos pos) {
        return getFromLootTableItemStack(lootTable, world, pos, null);
    }
}
