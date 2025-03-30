package com.renyigesai.immortalers_delight.util;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class WorldUtils {
    /*通过输入资源地址获取一个战利品表*/
    public static LootTable getLootTables(String name,Level world){
        if (!world.isClientSide() && world.getServer() != null) {
            return world.getServer().getLootData().getLootTable(new ResourceLocation(name));
        }
        return LootTable.lootTable().build();
    }
    /*从战利品表中获取物品列表*/
    public static List<ItemStack> getFromLootTableItemStack(LootTable lootTable,Level world,BlockPos pos){
        List<ItemStack> stacks = new ArrayList<>();
        if (!world.isClientSide() && world.getServer() != null){
            stacks.addAll(lootTable.getRandomItems(new LootParams.Builder((ServerLevel) world).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos)).withParameter(LootContextParams.BLOCK_STATE, world.getBlockState(pos)).withOptionalParameter(LootContextParams.BLOCK_ENTITY, world.getBlockEntity(pos)).create(LootContextParamSets.EMPTY)));
        }
        return stacks;
    }
}
