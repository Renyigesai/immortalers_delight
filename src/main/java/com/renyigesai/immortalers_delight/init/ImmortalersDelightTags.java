package com.renyigesai.immortalers_delight.init;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public class ImmortalersDelightTags {
    public static final TagKey<Biome> IS_CRIMSON_FOREST = createBiomeTag("is_crimson_forest");
    public static final TagKey<Biome> IS_WARPED_FOREST = createBiomeTag("is_warped_forest");
    public static final TagKey<Biome> IS_ICE_SPIKES = createBiomeTag("is_ice_spikes");
    public static final TagKey<Biome> IS_NETHER_WASTES = createBiomeTag("is_nether_wastes");

    public static final TagKey<Item> ANCIENT_BOAT_NEED_1 = createImmItemTag("boat_needs/ancient_boat_need_1");
    public static final TagKey<Item> ANCIENT_BOAT_NEED_2 = createImmItemTag("boat_needs/ancient_boat_need_2");
    public static final TagKey<Item> ANCIENT_CHEST_BOAT_NEED_1 = createImmItemTag("boat_needs/ancient_chest_boat_need_1");
    public static final TagKey<Item> ANCIENT_CHEST_BOAT_NEED_2 = createImmItemTag("boat_needs/ancient_chest_boat_need_2");
    public static final TagKey<Item> IMMORTAL_KNIVES = createImmItemTag("tools/immortal_knives");
    public static final TagKey<Item> IMMORTAL_HAMMERS = createImmItemTag("tools/immortal_hammers");
    public static final TagKey<Item> MILK = createItemTag("milk");
    public static final TagKey<Item> STRAW = createImmItemTag("straw");
    public static final TagKey<Item> KNIVES = createImmItemTag("tools/immortal_knives");
    /** Farmer's Delight `#farmersdelight:knives` (avoid deprecated `ModTags.KNIVES`). */
    public static final TagKey<Item> FARMERSDELIGHT_KNIVES = TagKey.create(Registries.ITEM, ResourceLocation.parse("farmersdelight:knives"));
    public static final TagKey<Block> FARMERSDELIGHT_HEAT_SOURCES = TagKey.create(Registries.BLOCK, ResourceLocation.parse("farmersdelight:heat_sources"));
    public static final TagKey<Block> FARMERSDELIGHT_HEAT_CONDUCTORS = TagKey.create(Registries.BLOCK, ResourceLocation.parse("farmersdelight:heat_conductors"));
    /** Mirrors Farmer's Delight offhand equipment tag for interactions that must ignore offhand items. */
    public static final TagKey<Item> OFFHAND_EQUIPMENT = TagKey.create(Registries.ITEM, ResourceLocation.parse("farmersdelight:offhand_equipment"));
    public static final TagKey<Block> MINEABLE_WITH_DRILL_ROD = createBlockTag("mineable/drill_rod");
    public static final TagKey<Block> MINEABLE_HAMMER = createBlockTag("mineable/meat_tenderizer");
    public static final TagKey<Block> SEXTLOTUS_TRANSFORM_AIR = createBlockTag("sextlotus_transform_air");
    public static final TagKey<Block> SEXTLOTUS_TRANSFORM_SAND = createBlockTag("sextlotus_transform_sand");
    public static final TagKey<Block> SEXTLOTUS_TRANSFORM_DIRT = createBlockTag("sextlotus_transform_dirt");
    public static final TagKey<Block> SEXTLOTUS_TRANSFORM_COAL = createBlockTag("sextlotus_transform_coal");
    public static final TagKey<Block> SEXTLOTUS_TRANSFORM_SPECIAL = createBlockTag("sextlotus_transform_special");

    public static final TagKey<EntityType<?>> FARMERSDELIGHT_DOG_FOOD_USERS =
            TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.parse("farmersdelight:dog_food_users"));
    public static final TagKey<Block> FARMERSDELIGHT_DROPS_CAKE_SLICE =
            TagKey.create(Registries.BLOCK, ResourceLocation.parse("farmersdelight:drops_cake_slice"));

    public static final TagKey<EntityType<?>> IMMORTAL_NORMAL_MOBS = createEntityTag("normal_mobs");

    public static final TagKey<EntityType<?>> IMMORTAL_ELITE_MOBS = createEntityTag("elite_mobs");

    public static final TagKey<EntityType<?>> IMMORTAL_MID_BOSS = createEntityTag("mid_boss");

    private static TagKey<Biome> createBiomeTag(String pName) {
        return TagKey.create(Registries.BIOME, ImmortalersDelightMod.prefix(pName));
    }

    private static TagKey<Item> createImmItemTag(String pName) {
        return TagKey.create(Registries.ITEM, ImmortalersDelightMod.prefix(pName));
    }

    private static TagKey<Item> createItemTag(String pName) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("forge", pName));
    }

    private static TagKey<Block> createBlockTag(String pName) {
        return TagKey.create(Registries.BLOCK, ImmortalersDelightMod.prefix(pName));
    }

    private static TagKey<EntityType<?>> createEntityTag(String pName) {
        return TagKey.create(Registries.ENTITY_TYPE, ImmortalersDelightMod.prefix(pName));
    }
}
