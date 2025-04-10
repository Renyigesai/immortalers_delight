package com.renyigesai.immortalers_delight.event;

import com.renyigesai.immortalers_delight.api.event.SnifferDropSeedEvent;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBiomeTags;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.Tags;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber
public class SnifferEvent {
    @SubscribeEvent
    public static void onDropSeed(SnifferDropSeedEvent event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            BlockPos pos = event.getBlockPos();
            Holder<Biome> biomeHolder = event.getLevel().getBiome(pos);
            for (Map.Entry<TagKey<Biome>, ItemStack> key : addSeedEntity().entrySet()) {
                TagKey<Biome> tagKey = key.getKey();
                if (biomeHolder.is(tagKey)){
                    ItemStack stack = addSeedEntity().get(tagKey);
                    ItemEntity itemEntity = new ItemEntity(event.getLevel(), pos.getX(), pos.getY(), pos.getZ(), stack);
                    serverLevel.addFreshEntity(itemEntity);
                }
            }
        }
    }

    private static HashMap<TagKey<Biome>, ItemStack> addSeedEntity() {
        HashMap<TagKey<Biome>, ItemStack> itemStackHashMap = new HashMap<>();
        itemStackHashMap.put(BiomeTags.IS_JUNGLE, new ItemStack(ImmortalersDelightItems.PEARLIPEARL.get()));
        itemStackHashMap.put(Tags.Biomes.IS_PLAINS, new ItemStack(ImmortalersDelightItems.EVOLUTCORN_GRAINS.get()));
        itemStackHashMap.put(BiomeTags.IS_FOREST, new ItemStack(ImmortalersDelightItems.HIMEKAIDO_SEED.get()));
        itemStackHashMap.put(BiomeTags.IS_RIVER, new ItemStack(ImmortalersDelightItems.CONTAINS_TEA_LEISAMBOO.get()));
        itemStackHashMap.put(ImmortalersDelightBiomeTags.IS_CRIMSON_FOREST, new ItemStack(ImmortalersDelightItems.PEARLIPEARL.get()));
        return itemStackHashMap;
    }
}
