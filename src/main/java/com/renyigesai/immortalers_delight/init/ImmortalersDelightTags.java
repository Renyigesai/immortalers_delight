package com.renyigesai.immortalers_delight.init;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public class ImmortalersDelightTags {
    public static final TagKey<Biome> IS_CRIMSON_FOREST = createBiomeTag("is_crimson_forest");
    public static final TagKey<Block> MINEABLE_WITH_DRILL_ROD = createBlockTag("mineable/drill_rod");

    private static TagKey<Biome> createBiomeTag(String pName) {
        return TagKey.create(Registries.BIOME, ImmortalersDelightMod.prefix(pName));
    }

    private static TagKey<Item> createItemTag(String pName) {
        return TagKey.create(Registries.ITEM, ImmortalersDelightMod.prefix(pName));
    }


    private static TagKey<Block> createBlockTag(String pName) {
        return TagKey.create(Registries.BLOCK, ImmortalersDelightMod.prefix(pName));
    }
}
