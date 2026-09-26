package com.renyigesai.immortalers_delight.data;


import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.data.builder.SeriesBlockRecipeBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerator {

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        net.minecraft.data.DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();

        // 生成方块模型 JSON，注意在生成BlockStateJSON的时候会校验这个所以要放上面
        generator.addProvider(
                event.includeClient(),
                new ImmBlockModelProvider(output, ImmortalersDelightMod.MODID, existingFileHelper)
        );

        generator.addProvider(event.includeClient(),new ItemModels(output,existingFileHelper));
        generator.addProvider(event.includeServer(), new BlockStates(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new LootTableProvider(output, Collections.emptySet(), List.of(new LootTableProvider.SubProviderEntry(BlockLootTables::new, LootContextParamSets.BLOCK))));
        generator.addProvider(event.includeClient(), new Languages(output, "en_us"));
        generator.addProvider(event.includeClient(), new Languages(output, "zh_cn"));
        generator.addProvider(event.includeClient(), new Recipes(output));

    }
}