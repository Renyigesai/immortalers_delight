//package com.renyigesai.immortalers_delight.data;
//
//
//import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
//import net.minecraft.core.HolderLookup;
//import net.minecraft.data.PackOutput;
//import net.minecraftforge.common.data.ExistingFileHelper;
//import net.minecraftforge.data.event.GatherDataEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//import vectorwing.farmersdelight.FarmersDelight;
//
//import java.util.concurrent.CompletableFuture;
//
//@SuppressWarnings("unused")
//@Mod.EventBusSubscriber(modid = FarmersDelight.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
//public class DataGenerator {
//
//    @SubscribeEvent
//    public static void onGatherData(GatherDataEvent event) {
//        net.minecraft.data.DataGenerator generator = event.getGenerator();
//        PackOutput output = generator.getPackOutput();
//
//        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
//        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
//
////        MSTagsProvider.Blocks blockTagsProvider = new MSTagsProvider.Blocks(output, provider, existingFileHelper);
////        generator.addProvider(events.includeServer(), new MSTagsProvider.Blocks(output, provider, existingFileHelper));
////        generator.addProvider(events.includeServer(), new MSTagsProvider.Items(
////                output, provider, blockTagsProvider.contentsGetter(), existingFileHelper));
//
//
//        generator.addProvider(event.includeClient(), new RegistryDataGenerator(output, provider));
//        generator.addProvider(event.includeClient(),new Languages(output,"en_us"));
//        generator.addProvider(event.includeClient(),new Languages(output,"zh_cn"));
//    }
//
//}