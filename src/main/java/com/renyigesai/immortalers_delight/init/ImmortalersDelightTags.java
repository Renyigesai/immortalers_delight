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

    public static final TagKey<Item> ANCIENT_BOAT_NEED_1 = createImmItemTag("boat_needs/ancient_boat_need_1");
    public static final TagKey<Item> ANCIENT_BOAT_NEED_2 = createImmItemTag("boat_needs/ancient_boat_need_2");
    public static final TagKey<Item> ANCIENT_CHEST_BOAT_NEED_1 = createImmItemTag("boat_needs/ancient_chest_boat_need_1");
    public static final TagKey<Item> ANCIENT_CHEST_BOAT_NEED_2 = createImmItemTag("boat_needs/ancient_chest_boat_need_2");
    public static final TagKey<Item> IMMORTAL_KNIVES = createImmItemTag("tools/immortal_knives");
    public static final TagKey<Item> IMMORTAL_HAMMERS = createImmItemTag("tools/immortal_hammers");
    public static final TagKey<Item> MILK = createItemTag("milk");
    public static final TagKey<Item> STRAW = createItemTag("straw");
    public static final TagKey<Block> MINEABLE_WITH_DRILL_ROD = createBlockTag("mineable/drill_rod");
    public static final TagKey<Block> MINEABLE_HAMMER = createBlockTag("mineable/meat_tenderizer");

    public static final TagKey<EntityType<?>> IMMORTAL_NORMAL_MOBS = createEntityTag("normal_mobs");

    public static final TagKey<EntityType<?>> IMMORTAL_ELITE_MOBS = createEntityTag("elite_mobs");

    public static final TagKey<EntityType<?>> IMMORTAL_MINI_BOSS = createEntityTag("mini_boss");

    private static TagKey<Biome> createBiomeTag(String pName) {
        return TagKey.create(Registries.BIOME, ImmortalersDelightMod.prefix(pName));
    }

    private static TagKey<Item> createImmItemTag(String pName) {
        return TagKey.create(Registries.ITEM, ImmortalersDelightMod.prefix(pName));
    }

    private static TagKey<Item> createItemTag(String pName) {
        return TagKey.create(Registries.ITEM, new ResourceLocation("forge",pName));
    }

    private static TagKey<Block> createBlockTag(String pName) {
        return TagKey.create(Registries.BLOCK, ImmortalersDelightMod.prefix(pName));
    }

    private static TagKey<EntityType<?>> createEntityTag(String pName) {
        return TagKey.create(Registries.ENTITY_TYPE, ImmortalersDelightMod.prefix(pName));
    }
}
