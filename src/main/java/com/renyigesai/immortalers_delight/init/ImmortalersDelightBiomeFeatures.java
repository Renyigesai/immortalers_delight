package com.renyigesai.immortalers_delight.init;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.world.feature.OxygrapeFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ImmortalersDelightBiomeFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, ImmortalersDelightMod.MODID);
    public static final RegistryObject<Feature<ProbabilityFeatureConfiguration>> OXYGRAPE;

    static {
        OXYGRAPE = FEATURES.register("oxygrape", OxygrapeFeature::new);
    }
}
