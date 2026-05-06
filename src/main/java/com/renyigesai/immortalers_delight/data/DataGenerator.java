package com.renyigesai.immortalers_delight.data;
import net.neoforged.fml.common.EventBusSubscriber;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

@EventBusSubscriber(modid = ImmortalersDelightMod.MODID)
public class DataGenerator {

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        net.minecraft.data.DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
        generator.addProvider(event.includeClient(),new ItemModels(output,existingFileHelper));
        generator.addProvider(event.includeServer(), new BlockStates(output, existingFileHelper));
        generator.addProvider(event.includeServer(), new LootTableProvider(output, Set.<ResourceKey<LootTable>>of(), List.of(new LootTableProvider.SubProviderEntry(BlockLootTables::new, LootContextParamSets.BLOCK)), provider));
        generator.addProvider(event.includeClient(), new Languages(output, "en_us"));
        generator.addProvider(event.includeClient(), new Languages(output, "zh_cn"));
        generator.addProvider(event.includeServer(), new Recipes(output, provider));
    }

}