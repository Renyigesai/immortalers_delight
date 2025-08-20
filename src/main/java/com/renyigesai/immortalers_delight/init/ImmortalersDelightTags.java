package com.renyigesai.immortalers_delight.init;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public class ImmortalersDelightTags {
    public static final TagKey<Biome> IS_CRIMSON_FOREST = createBiomeTag("is_crimson_forest");
    public static final TagKey<Biome> IS_WARPED_FOREST = createBiomeTag("is_warped_forest");

    public static final TagKey<Item> ANCIENT_BOAT_NEED_1 = createItemTag("boat_needs/ancient_boat_need_1");
    public static final TagKey<Item> ANCIENT_BOAT_NEED_2 = createItemTag("boat_needs/ancient_boat_need_2");
    public static final TagKey<Item> ANCIENT_CHEST_BOAT_NEED_1 = createItemTag("boat_needs/ancient_chest_boat_need_1");
    public static final TagKey<Item> ANCIENT_CHEST_BOAT_NEED_2 = createItemTag("boat_needs/ancient_chest_boat_need_2");
    public static final TagKey<Item> IMMORTAL_KNIFES = createItemTag("tools/immortal_knifes");
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
