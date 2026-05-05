package com.renyigesai.immortalers_delight.init;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.world.feature.OxygrapeFeature;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ImmortalersDelightBiomeFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, ImmortalersDelightMod.MODID);
    public static final DeferredHolder<Feature<?>, Feature<ProbabilityFeatureConfiguration>> OXYGRAPE;

    static {
        OXYGRAPE = FEATURES.register("oxygrape", OxygrapeFeature::new);
    }
}
