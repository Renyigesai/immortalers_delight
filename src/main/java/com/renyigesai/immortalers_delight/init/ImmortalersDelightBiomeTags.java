package com.renyigesai.immortalers_delight.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class ImmortalersDelightBiomeTags {
    public static final TagKey<Biome> IS_CRIMSON_FOREST = create("is_crimson_forest");

    private static TagKey<Biome> create(String pName) {
        return TagKey.create(Registries.BIOME, new ResourceLocation(pName));
    }
}
