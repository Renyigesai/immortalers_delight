package com.renyigesai.immortalers_delight.init;

import com.google.common.collect.Sets;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.entities.boat.ImmortalersBoat;
import com.renyigesai.immortalers_delight.entities.boat.ImmortalersChestBoat;
import com.renyigesai.immortalers_delight.item.*;
//import com.renyigesai.immortalers_delight.util.datautil.worlddata.BaseImmortalWorldData;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.common.item.ConsumableItem;
import vectorwing.farmersdelight.common.item.DrinkableItem;

import java.util.LinkedHashSet;
import java.util.function.Supplier;


public class ImmortalersDelightItems {


    public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, ImmortalersDelightMod.MODID);
    public static LinkedHashSet<RegistryObject<Item>> CREATIVE_TAB_ITEMS = Sets.newLinkedHashSet();
    public static final RegistryObject<Item> HIMEKAIDO_LOG;
    public static final RegistryObject<Item> HIMEKAIDO_WOOD;
    public static final RegistryObject<Item> STRIPPED_HIMEKAIDO_WOOD;
    public static final RegistryObject<Item> STRIPPED_HIMEKAIDO_LOG;
    public static final RegistryObject<Item> HIMEKAIDO_FRUITED_LEAVES;
    public static final RegistryObject<Item> HIMEKAIDO_FLOWERING_LEAVES;
    public static final RegistryObject<Item> HIMEKAIDO_LEAVES;
    public static final RegistryObject<Item> HIMEKAIDO_PLANKS;
    public static final RegistryObject<Item> HIMEKAIDO_STAIRS;
    public static final RegistryObject<Item> HIMEKAIDO_SLAB;
    public static final RegistryObject<Item> HIMEKAIDO_DOOR;
    public static final RegistryObject<Item> HIMEKAIDO_TRAPDOOR;
    public static final RegistryObject<Item> HIMEKAIDO_FENCE;
    public static final RegistryObject<Item> HIMEKAIDO_FENCE_GATE;
    public static final RegistryObject<Item> HIMEKAIDO_PRESSURE_PLATE;
    public static final RegistryObject<Item> HIMEKAIDO_BUTTON;
    public static final RegistryObject<Item> HIMEKAIDO_CABINET;
    public static final RegistryObject<Item> HIMEKAIDO_BOAT;
    public static final RegistryObject<Item> ANCIENT_WOOD_BOAT;
    public static final RegistryObject<Item> PEARLIP_SHELL_BOAT;
    public static final RegistryObject<Item> HIMEKAIDO_CHEST_BOAT;
    public static final RegistryObject<Item> PEARLIP_SHELL_CHEST_BOAT;
    public static final RegistryObject<Item> ANCIENT_WOOD_CHEST_BOAT;
    public static final RegistryObject<Item> MILLENIAN_BAMBOO;
    public static final RegistryObject<Item> STEWED_ROTTEN_MEAT_POT;
    public static final RegistryObject<Item> BRAISED_SPIDER_EYES_BLOCK;
    public static final RegistryObject<Item> EVOLUTCORN_GRAIN_BAG;
    public static final RegistryObject<Item> HIMEKAIDO_CRATE;
    public static final RegistryObject<Item> PEARLIP_CRATE;
    public static final RegistryObject<Item> EVOLUTCORN_BLOCK;
    public static final RegistryObject<Item> KWAT_WHEAT_BLOCK;
    public static final RegistryObject<Item> ALFALFA_BLOCK;
    public static final RegistryObject<Item> LEISAMBOO_BLOCK;
    public static final RegistryObject<Item> HIMEKAIDO_SIGN;
    public static final RegistryObject<Item> HIMEKAIDO_HANGING_SIGN;
    public static final RegistryObject<Item> LEISAMBOO_PLANKS;
    public static final RegistryObject<Item> LEISAMBOO_STAIRS;
    public static final RegistryObject<Item> LEISAMBOO_SLAB;
    public static final RegistryObject<Item> LEISAMBOO_CABINET;
    public static final RegistryObject<Item> LEISAMBOO_DOOR;
    public static final RegistryObject<Item> LEISAMBOO_TRAPDOOR;
    public static final RegistryObject<Item> LEISAMBOO_FENCE;
    public static final RegistryObject<Item> LEISAMBOO_FENCE_GATE;
    public static final RegistryObject<Item> LEISAMBOO_PRESSURE_PLATE;
    public static final RegistryObject<Item> LEISAMBOO_BUTTON;
    public static final RegistryObject<Item> LEISAMBOO_SIGN;
    public static final RegistryObject<Item> LEISAMBOO_HANGING_SIGN;
    public static final RegistryObject<Item> PEARLIP_SHELL_PLANKS;
    public static final RegistryObject<Item> PEARLIP_SHELL_STAIRS;
    public static final RegistryObject<Item> PEARLIP_SHELL_SLAB;
    public static final RegistryObject<Item> PEARLIP_SHELL_CABINET;
    public static final RegistryObject<Item> PEARLIP_SHELL_DOOR;
    public static final RegistryObject<Item> PEARLIP_SHELL_TRAPDOOR;
    public static final RegistryObject<Item> PEARLIP_SHELL_FENCE;
    public static final RegistryObject<Item> PEARLIP_SHELL_FENCE_GATE;
    public static final RegistryObject<Item> PEARLIP_SHELL_PRESSURE_PLATE;
    public static final RegistryObject<Item> PEARLIP_SHELL_BUTTON;
    public static final RegistryObject<Item> PEARLIP_SHELL_SIGN;
    public static final RegistryObject<Item> PEARLIP_SHELL_HANGING_SIGN;
    public static final RegistryObject<Item> ANCIENT_FIBER;
    public static final RegistryObject<Item> BOWL_OF_MILLENIAN_BAMBOO;
    public static final RegistryObject<Item> PITCHER_POD_PETAL;
    public static final RegistryObject<Item> TRAVASTRUGGLER_LOG;
    public static final RegistryObject<Item> STRIPPED_TRAVASTRUGGLER_LOG;
    public static final RegistryObject<Item> TRAVA_PLANKS;
    public static final RegistryObject<Item> TRAVASTRUGGLER_LEAVES;
    public static final RegistryObject<Item> TRAVASTRUGGLER_LEAVES_TRAVARICE;

    /*
    玉米系列物品
    */
    public static final RegistryObject<Item> EVOLUTCORN;
    public static final RegistryObject<Item> ROAST_EVOLUTCORN;
    public static final RegistryObject<Item> EVOLUTCORN_GRAINS;
    public static final RegistryObject<Item> ROAST_EVOLUTCORN_CHOPS;
    public static final RegistryObject<Item> POPOLUTCORN;
    public static final RegistryObject<Item> CRETACEOUS_ZEA_BALL;
    public static final RegistryObject<Item> COLORFUL_GRILLED_SKEWERS;
    public static final RegistryObject<Item> ZEA_PANCAKE;
    public static final RegistryObject<Item> ZEA_PANCAKE_SLICE;
    public static final RegistryObject<Item> EVOLUTCORN_PIE_CRUST;
    public static final RegistryObject<Item> EVOLUTCORN_BEER;
    public static final RegistryObject<Item> STICKY_BEER;
    public static final RegistryObject<Item> CUSTARD_TART_PASTRY;
    public static final RegistryObject<Item> EVOLUTCORN_HARD_CANDY;
    /*
    香蕉系列物品
    */
    public static final RegistryObject<Item> PEARLIP;
    public static final RegistryObject<Item> PEARLIP_SHELL;
    public static final RegistryObject<Item> PEARLIPEARL;
    public static final RegistryObject<Item> PEATIC_MUSA_SALAD;
    public static final RegistryObject<Item> PEARLIP_MILK_SHAKE;
    public static final RegistryObject<Item> PEARLIP_PUMPKIN_PIE;
    public static final RegistryObject<Item> PEARLIPEARL_TART;
    public static final RegistryObject<Item> PEARLIPEARL_EGGSTEAM;
    public static final RegistryObject<Item> PEARLIP_JELLY;
    public static final RegistryObject<Item> PEARLIP_PIE;
    public static final RegistryObject<Item> PEARLIP_PIE_SLICE;
    public static final RegistryObject<Item> ICE_PEARLIP;
    public static final RegistryObject<Item> CHOCOLATE_PEARLIP_STICKS;
    public static final RegistryObject<Item> PERFECT_SUMMER_ICE;
    public static final RegistryObject<Item> TWILIGHT_GELATO;
    public static final RegistryObject<Item> PEARLIP_BUBBLE_MILK;
    public static final RegistryObject<Item> CHOCOREEZE;
    public static final RegistryObject<Item> TROPICAL_FRUITY_CYCLONE;
    /*
    姬海棠系列物品
     */
    public static final RegistryObject<Item> MASHED_POTATOES;
    public static final RegistryObject<Item> MASHED_POISONOUS_POTATO;
    public static final RegistryObject<Item> MASHED_POTATO_WITH_JAM;
    public static final RegistryObject<Item> MASHED_POISONOUS_POTATO_WITH_JAM;
    public static final RegistryObject<Item> ROTTEN_FLESH_CUTS;
    public static final RegistryObject<Item> HIMEKAIDO_JELLY;
    public static final RegistryObject<Item> YOGURT;
    public static final RegistryObject<Item> HIMEKAIDO;
    public static final RegistryObject<Item> BAKED_POISONOUS_POTATO;
    public static final RegistryObject<Item> BRAISED_SPIDER_EYES_IN_GRAVY;
    public static final RegistryObject<Item> DIPPED_ROTTEN_FLESH;
    public static final RegistryObject<Item> CRISPY_YOGURT_ROTTEN_FLESH;
    public static final RegistryObject<Item> MEATY_ROTTEN_TOMATO_BROTH;
    public static final RegistryObject<Item> STUFFED_POISONOUS_POTATO;
    public static final RegistryObject<Item> PUFFERFISH_ROLL;
    public static final RegistryObject<Item> GOLDEN_HIMEKAIDO;
    public static final RegistryObject<Item> ENCHANTED_GOLDEN_HIMEKAIDO;
    public static final RegistryObject<Item> HIMEKAIDO_SEED;
    public static final RegistryObject<Item> BOWL_OF_STEWED_ROTTEN_MEAT_IN_CLAY_POT;
    public static final RegistryObject<Item> TARTARE_CHICKEN;
    public static final RegistryObject<Item> ENCHANTAL_COOLER;
    public static final RegistryObject<Item> ANCIENT_STOVE;
    public static final RegistryObject<Item> OXIDIZED_ANCIENT_STOVE;
    public static final RegistryObject<Item> HIMEKAIDO_YOGURT_PIE;
    public static final RegistryObject<Item> HIMEKAIDO_YOGURT_PIE_SLICE;
    public static final RegistryObject<Item> DREUMK_WINE;
    public static final RegistryObject<Item> SCARLET_GELATO;
    public static final RegistryObject<Item> SCARLET_SUNDAE;
    public static final RegistryObject<Item> HIMEKAIDO_CHAZUKE;
    /*
    古木相关
    */
    public static final RegistryObject<Item> ANCIENT_WOOD_LOG;
    public static final RegistryObject<Item> STRIPPED_ANCIENT_WOOD_LOG;
    public static final RegistryObject<Item> ANCIENT_WOOD;
    public static final RegistryObject<Item> STRIPPED_ANCIENT_WOOD;
    public static final RegistryObject<Item> ANCIENT_WOOD_PLANKS;
    public static final RegistryObject<Item> ANCIENT_WOOD_CABINET;
    public static final RegistryObject<Item> ANCIENT_WOOD_STAIRS;
    public static final RegistryObject<Item> ANCIENT_WOOD_SLAB;
    public static final RegistryObject<Item> ANCIENT_WOOD_DOOR;
    public static final RegistryObject<Item> ANCIENT_WOOD_TRAPDOOR;
    public static final RegistryObject<Item> ANCIENT_WOOD_FENCE;
    public static final RegistryObject<Item> ANCIENT_WOOD_FENCE_GATE;
    public static final RegistryObject<Item> ANCIENT_WOOD_PRESSURE_PLATE;
    public static final RegistryObject<Item> ANCIENT_WOOD_BUTTON;

    /*
     溪竹相关物品
     */
    public static final RegistryObject<Item> LEAF_TEA;
    public static final RegistryObject<Item> CONTAINS_TEA_LEISAMBOO;
    public static final RegistryObject<Item> EMPTY_BAMBOO_CUP;
    public static final RegistryObject<Item> LEISAMBOO_TEA;
    public static final RegistryObject<Item> ICED_BLACK_TEA;
    public static final RegistryObject<Item> PEARLIPEARL_MILK_TEA;
    public static final RegistryObject<Item> PEARLIPEARL_MILK_GREEN;
    public static final RegistryObject<Item> STOVE_BLACK_TEA;
    public static final RegistryObject<Item> LEAF_GREEN_TEA;
    public static final RegistryObject<Item> BRITISH_YELLOW_TEA;
    public static final RegistryObject<Item> CHERRY_PEARLIPEARL_TEA;
    public static final RegistryObject<Item> GLEEMAN_TEAR;
    public static final RegistryObject<Item> FRUIT_TEA;
    public static final RegistryObject<Item> LEISAMBOO_TEA_CAKE;

    /*
    通天竹相关
    */
    public static final RegistryObject<Item> TRAVAPLANK;
    public static final RegistryObject<Item> TRAVARICE;
    public static final RegistryObject<Item> COOKED_TRAVARICE;
    public static final RegistryObject<Item> TRAVEER;

    /*
    瓦斯麦相关物品
    */
    public static final RegistryObject<Item> KWAT_WHEAT_SEEDS;
    public static final RegistryObject<Item> KWAT_WHEAT;
    public static final RegistryObject<Item> KWAT_WHEAT_DOUGH;
    public static final RegistryObject<Item> KWAT_WHEAT_PASTE;
    public static final RegistryObject<Item> KWAT_WHEAT_DOUFU;
    public static final RegistryObject<Item> FRY_KWAT_WHEAT_DOUFU;
    public static final RegistryObject<Item> SUTFFED_KWAT_WHEAT_DOUFU;
    public static final RegistryObject<Item> JADE_AND_RUBY_SOUP;
    public static final RegistryObject<Item> KWAT_WHEAT_TOAST;
    public static final RegistryObject<Item> KWAT_WHEAT_TOAST_SLICE;
    public static final RegistryObject<Item> NETHER_BREAD_CREAM_SOUP;
    public static final RegistryObject<Item> NETHER_CREAM_SOUP;
    public static final RegistryObject<Item> NETHER_CREAM_BREAD;
    /*
    火把花相关物品
    */
    public static final RegistryObject<Item> TORCHFLOWER_MUSTARD;
    public static final RegistryObject<Item> HOT_HI_SOUP;
    public static final RegistryObject<Item> INCANDESCENCE_SUSHI;
    public static final RegistryObject<Item> TORCHFLOWER_CAKE;
    public static final RegistryObject<Item> TORCHFLOWER_CURRY_RICE;
    public static final RegistryObject<Item> EXTRA_SPICY_PASTA;
    public static final RegistryObject<Item> TORCHFLOWER_COOKIE;


    public static final RegistryObject<Item> PITCHER_PLANT_BARBECUE;
    public static final RegistryObject<Item> POD_SHELL_BURGER_MEAT;
    public static final RegistryObject<Item> POD_SHELL_BURGER_MEAT_CUBE;
    public static final RegistryObject<Item> AROMATIC_POD_AFFOGATO;

    public static final RegistryObject<Item> GOLDEN_FABRIC;
    public static final RegistryObject<Item> GOLDEN_FABRIC_VEIL;
    public static final RegistryObject<Item> RAW_SNIFFER_SLICE;
    public static final RegistryObject<Item> COOKED_SNIFFER_SLICE;
    public static final RegistryObject<Item> RAW_SNIFFER_STEAK;
    public static final RegistryObject<Item> COOKED_SNIFFER_STEAK;
    public static final RegistryObject<Item> SNIFFER_FUR;
    public static final RegistryObject<Item> BRUSH;
    public static final RegistryObject<Item> SNIFFER_FUR_BRUSH;
    public static final RegistryObject<Item> CLEAR_WATER_VODKA;
    public static final RegistryObject<Item> SNIFFER_ROTATING_ROAST_MEAT;
    public static final RegistryObject<Item> VULCAN_COKTAIL;
    public static final RegistryObject<Item> NETHER_KVASS;
    public static final RegistryObject<Item> PURGATORY_ALE;
    public static final RegistryObject<Item> HIMEKANDY;
    //public static final RegistryObject<Item> PALE_DEW_WINE;
    //public static final RegistryObject<Item> TEST_DAMAGE_ITEM;
    public static final RegistryObject<Item> SCARLET_DEVILS_CAKE;
    public static final RegistryObject<Item> SCARLET_DEVILS_CAKE_SLICE;
    public static final RegistryObject<Item> RED_STUFFED_BUN;
    //public static final RegistryObject<Item> SPICY_PUDDING;


    public static final RegistryObject<Item> POISONOUS_LONG_SPIKE_TRAP;
    public static final RegistryObject<Item> LONG_SPIKE_TRAP;
    public static final RegistryObject<Item> POISONOUS_SPIKE_TRAP;
    public static final RegistryObject<Item> SPIKE_TRAP;
    public static final RegistryObject<Item> POISONOUS_METAL_CALTROP;
    public static final RegistryObject<Item> METAL_CALTROP;
    public static final RegistryObject<Item> SPIKE_BAR_BASE;
    public static final RegistryObject<Item> SPIKE_BAR;
    public static final RegistryObject<Item> MUD_TILES;
    public static final RegistryObject<Item> MUD_TILES_STAIRS;
    public static final RegistryObject<Item> MUD_TILES_SLAB;
    public static final RegistryObject<Item> MUD_TILES_WALL;
    public static final RegistryObject<Item> CRACKED_MUD_TILES;
    public static final RegistryObject<Item> CRACKED_MUD_TILES_STAIRS;
    public static final RegistryObject<Item> CRACKED_MUD_TILES_SLAB;
    public static final RegistryObject<Item> CRACKED_MUD_TILES_WALL;
    public static final RegistryObject<Item> MOSSY_MUD_BRICK;
    public static final RegistryObject<Item> MOSSY_MUD_BRICK_STAIRS;
    public static final RegistryObject<Item> MOSSY_MUD_BRICK_SLAB;
    public static final RegistryObject<Item> MOSSY_MUD_BRICK_WALL;
    public static final RegistryObject<Item>CRACKED_MUD_BRICK;
    public static final RegistryObject<Item>CRACKED_MUD_BRICK_STAIRS;
    public static final RegistryObject<Item>CRACKED_MUD_BRICK_SLAB;
    public static final RegistryObject<Item>CRACKED_MUD_BRICK_WALL;
    public static final RegistryObject<Item>CHISELED_MUD_BRICK;
    public static final RegistryObject<Item>PACKED_MUD_STAIRS;
    public static final RegistryObject<Item>PACKED_MUD_SLAB;
    public static final RegistryObject<Item>PACKED_MUD_WALL;

    public static final RegistryObject<Item> INFESTED_GRAVEL;
    public static final RegistryObject<Item> INFESTED_SAND;

//    public static final RegistryObject<Item> INFESTED_COAL_BLOCK;
//    public static final RegistryObject<Item> INFESTED_COPPER_BLOCK;
//    public static final RegistryObject<Item> INFESTED_IRON_BLOCK;
//    public static final RegistryObject<Item> INFESTED_REDSTONE_BLOCK;
//    public static final RegistryObject<Item> INFESTED_LAPIS_BLOCK;
//    public static final RegistryObject<Item> INFESTED_GOLD_BLOCK;
//    public static final RegistryObject<Item> INFESTED_EMERALD_BLOCK;
//    public static final RegistryObject<Item> INFESTED_DIAMOND_BLOCK;

    /*ALFALFA*/
    public static final RegistryObject<Item> ALFALFA_SEEDS;
    public static final RegistryObject<Item> ALFALFA;
    public static final RegistryObject<Item> ALFALFA_PORRIDGE;

    public static final RegistryObject<Item> BANANA_BOX_SALMON;
    public static final RegistryObject<Item> BANANA_BOX_COD;
    public static final RegistryObject<Item> PEARLIP_RICE_ROLL_BOAT;


    //public static final RegistryObject<Item> STRANGE_ARMOUR_STAND_SPAWN_EGG;


    public static final RegistryObject<Item> SACHETS;
    public static final RegistryObject<Item> DRILL_ROD_WAND;

    static {

        ENCHANTAL_COOLER = block(ImmortalersDelightBlocks.ENCHANTAL_COOLER);

        ANCIENT_STOVE = block(ImmortalersDelightBlocks.ANCIENT_STOVE);

        OXIDIZED_ANCIENT_STOVE = block(ImmortalersDelightBlocks.OXIDIZED_ANCIENT_STOVE);

        EVOLUTCORN_GRAIN_BAG = registerWithTab("evolutcorn_grain_bag", () ->
                new BlockItem(ImmortalersDelightBlocks.EVOLUTCORN_GRAIN_BAG.get(), basicItem()));

        HIMEKAIDO_CRATE = registerWithTab("himekaido_crate", () ->
                new BlockItem(ImmortalersDelightBlocks.HIMEKAIDO_CRATE.get(), basicItem()));

        PEARLIP_CRATE = registerWithTab("pearlip_crate", () ->
                new BlockItem(ImmortalersDelightBlocks.PEARLIP_CRATE.get(), basicItem()));

        EVOLUTCORN_BLOCK = registerWithTab("evolutcorn_block", () ->
                new BlockItem(ImmortalersDelightBlocks.EVOLUTCORN_BLOCK.get(), basicItem()));
        KWAT_WHEAT_BLOCK = registerWithTab("kwat_wheat_block", () ->
                new BlockItem(ImmortalersDelightBlocks.KWAT_WHEAT_BLOCK.get(), basicItem()));
        ALFALFA_BLOCK = registerWithTab("alfalfa_block", () ->
                new BlockItem(ImmortalersDelightBlocks.ALFALFA_BLOCK.get(), basicItem()));
        LEISAMBOO_BLOCK = registerWithTab("leisamboo_block", () ->
                new BlockItem(ImmortalersDelightBlocks.LEISAMBOO_BLOCK.get(), basicItem()));

        HIMEKAIDO_LOG = registerWithTab("himekaido_log", () ->
                new BlockItem(ImmortalersDelightBlocks.HIMEKAIDO_LOG.get(), basicItem()));
        HIMEKAIDO_WOOD = registerWithTab("himekaido_wood", () ->
                new BlockItem(ImmortalersDelightBlocks.HIMEKAIDO_WOOD.get(), basicItem()));

        STRIPPED_HIMEKAIDO_WOOD = registerWithTab("stripped_himekaido_wood", () ->
                new BlockItem(ImmortalersDelightBlocks.STRIPPED_HIMEKAIDO_WOOD.get(), basicItem()));

        STRIPPED_HIMEKAIDO_LOG = registerWithTab("stripped_himekaido_log", () ->
                new BlockItem(ImmortalersDelightBlocks.STRIPPED_HIMEKAIDO_LOG.get(), basicItem()));

        HIMEKAIDO_FRUITED_LEAVES = registerWithTab("himekaido_fruited_leaves", () ->
                new BlockItem(ImmortalersDelightBlocks.HIMEKAIDO_FRUITED_LEAVES.get(), basicItem()));

        HIMEKAIDO_FLOWERING_LEAVES = registerWithTab("himekaido_flowering_leaves", () ->
                new BlockItem(ImmortalersDelightBlocks.HIMEKAIDO_FLOWERING_LEAVES.get(), basicItem()));

        HIMEKAIDO_LEAVES = registerWithTab("himekaido_leaves", () ->
                new BlockItem(ImmortalersDelightBlocks.HIMEKAIDO_LEAVES.get(), basicItem()));

        HIMEKAIDO_PLANKS = registerWithTab("himekaido_planks", () ->
                new BlockItem(ImmortalersDelightBlocks.HIMEKAIDO_PLANKS.get(), basicItem()));
        HIMEKAIDO_CABINET = registerWithTab("himekaido_cabinet", () ->
                new BlockItem(ImmortalersDelightBlocks.HIMEKAIDO_CABINET.get(), basicItem()));
        HIMEKAIDO_STAIRS = registerWithTab("himekaido_stairs", () ->
                new BlockItem(ImmortalersDelightBlocks.HIMEKAIDO_STAIRS.get(), basicItem()));

        HIMEKAIDO_SLAB = registerWithTab("himekaido_slab", () ->
                new BlockItem(ImmortalersDelightBlocks.HIMEKAIDO_SLAB.get(), basicItem()));

        HIMEKAIDO_DOOR = registerWithTab("himekaido_door", () ->
                new BlockItem(ImmortalersDelightBlocks.HIMEKAIDO_DOOR.get(), basicItem()));

        HIMEKAIDO_TRAPDOOR = registerWithTab("himekaido_trapdoor", () ->
                new BlockItem(ImmortalersDelightBlocks.HIMEKAIDO_TRAPDOOR.get(), basicItem()));

        HIMEKAIDO_FENCE = registerWithTab("himekaido_fence", () ->
                new BlockItem(ImmortalersDelightBlocks.HIMEKAIDO_FENCE.get(), basicItem()));

        HIMEKAIDO_FENCE_GATE = registerWithTab("himekaido_fence_gate", () ->
                new BlockItem(ImmortalersDelightBlocks.HIMEKAIDO_FENCE_GATE.get(), basicItem()));

        HIMEKAIDO_PRESSURE_PLATE = registerWithTab("himekaido_pressure_plate", () ->
                new BlockItem(ImmortalersDelightBlocks.HIMEKAIDO_PRESSURE_PLATE.get(), basicItem()));

        HIMEKAIDO_BUTTON = registerWithTab("himekaido_button", () ->
                new BlockItem(ImmortalersDelightBlocks.HIMEKAIDO_BUTTON.get(), basicItem()));
        HIMEKAIDO_SIGN = registerWithTab("himekaido_sign",() ->
                new SignItem((new Item.Properties()).stacksTo(16),
                        ImmortalersDelightBlocks.HIMEKAIDO_SIGN.get(),
                        ImmortalersDelightBlocks.HIMEKAIDO_WALL_SIGN.get()
                        ));
        HIMEKAIDO_HANGING_SIGN = registerWithTab("himekaido_hanging_sign",() ->
                new HangingSignItem(
                        ImmortalersDelightBlocks.HIMEKAIDO_HANGING_SIGN.get(),
                        ImmortalersDelightBlocks.HIMEKAIDO_WALL_HANGING_SIGN.get(),
                        (new Item.Properties()).stacksTo(16)
                ));
        HIMEKAIDO_BOAT = registerWithTab("himekaido_boat",()->
                new ImmortalersBoatItem(ImmortalersBoat.Type.HIMEKAIDO, basicItem().stacksTo(1)));
        HIMEKAIDO_CHEST_BOAT = registerWithTab("himekaido_chest_boat",()->
                new ImmortalersChestBoatItem(ImmortalersChestBoat.Type.HIMEKAIDO, basicItem().stacksTo(1)));

        /*
        溪竹装饰方块
         */
        LEISAMBOO_PLANKS = registerWithTab("leisamboo_planks", () ->
                new BlockItem(ImmortalersDelightBlocks.LEISAMBOO_PLANKS.get(), basicItem()));

        LEISAMBOO_STAIRS = registerWithTab("leisamboo_stairs", () ->
                new BlockItem(ImmortalersDelightBlocks.LEISAMBOO_STAIRS.get(), basicItem()));

        LEISAMBOO_CABINET = block(ImmortalersDelightBlocks.LEISAMBOO_CABINET);

        LEISAMBOO_SLAB = registerWithTab("leisamboo_slab", () ->
                new BlockItem(ImmortalersDelightBlocks.LEISAMBOO_SLAB.get(), basicItem()));

        LEISAMBOO_DOOR = block(ImmortalersDelightBlocks.LEISAMBOO_DOOR);

        LEISAMBOO_TRAPDOOR = block(ImmortalersDelightBlocks.LEISAMBOO_TRAPDOOR);

        LEISAMBOO_FENCE = registerWithTab("leisamboo_fence", () ->
                new BlockItem(ImmortalersDelightBlocks.LEISAMBOO_FENCE.get(), basicItem()));

        LEISAMBOO_FENCE_GATE = registerWithTab("leisamboo_fence_gate", () ->
                new BlockItem(ImmortalersDelightBlocks.LEISAMBOO_FENCE_GATE.get(), basicItem()));

        LEISAMBOO_PRESSURE_PLATE = registerWithTab("leisamboo_pressure_plate", () ->
                new BlockItem(ImmortalersDelightBlocks.LEISAMBOO_PRESSURE_PLATE.get(), basicItem()));

        LEISAMBOO_BUTTON = registerWithTab("leisamboo_button", () ->
                new BlockItem(ImmortalersDelightBlocks.LEISAMBOO_BUTTON.get(), basicItem()));
        LEISAMBOO_SIGN = registerWithTab("leisamboo_sign",() ->
                new SignItem((new Item.Properties()).stacksTo(16),
                        ImmortalersDelightBlocks.LEISAMBOO_SIGN.get(),
                        ImmortalersDelightBlocks.LEISAMBOO_WALL_SIGN.get()
                ));
        LEISAMBOO_HANGING_SIGN = registerWithTab("leisamboo_hanging_sign",() ->
                new HangingSignItem(
                        ImmortalersDelightBlocks.LEISAMBOO_HANGING_SIGN.get(),
                        ImmortalersDelightBlocks.LEISAMBOO_WALL_HANGING_SIGN.get(),
                        (new Item.Properties()).stacksTo(16)
                ));

        /*
        棱蕉装饰方块
         */
        PEARLIP_SHELL_PLANKS = registerWithTab("pearlip_shell_planks", () ->
                new BlockItem(ImmortalersDelightBlocks.PEARLIP_SHELL_PLANKS.get(), basicItem()));

        PEARLIP_SHELL_STAIRS = registerWithTab("pearlip_shell_stairs", () ->
                new BlockItem(ImmortalersDelightBlocks.PEARLIP_SHELL_STAIRS.get(), basicItem()));

        PEARLIP_SHELL_CABINET = block(ImmortalersDelightBlocks.PEARLIP_SHELL_CABINET);

        PEARLIP_SHELL_SLAB = registerWithTab("pearlip_shell_slab", () ->
                new BlockItem(ImmortalersDelightBlocks.PEARLIP_SHELL_SLAB.get(), basicItem()));

        PEARLIP_SHELL_DOOR = block(ImmortalersDelightBlocks.PEARLIP_SHELL_DOOR);

        PEARLIP_SHELL_TRAPDOOR = block(ImmortalersDelightBlocks.PEARLIP_SHELL_TRAPDOOR);

        PEARLIP_SHELL_FENCE = registerWithTab("pearlip_shell_fence", () ->
                new BlockItem(ImmortalersDelightBlocks.PEARLIP_SHELL_FENCE.get(), basicItem()));

        PEARLIP_SHELL_FENCE_GATE = registerWithTab("pearlip_shell_fence_gate", () ->
                new BlockItem(ImmortalersDelightBlocks.PEARLIP_SHELL_FENCE_GATE.get(), basicItem()));

        PEARLIP_SHELL_PRESSURE_PLATE = registerWithTab("pearlip_shell_pressure_plate", () ->
                new BlockItem(ImmortalersDelightBlocks.PEARLIP_SHELL_PRESSURE_PLATE.get(), basicItem()));

        PEARLIP_SHELL_BUTTON = registerWithTab("pearlip_shell_button", () ->
                new BlockItem(ImmortalersDelightBlocks.PEARLIP_SHELL_BUTTON.get(), basicItem()));
        PEARLIP_SHELL_SIGN = registerWithTab("pearlip_shell_sign",() ->
                new SignItem((new Item.Properties()).stacksTo(16),
                        ImmortalersDelightBlocks.PEARLIP_SHELL_SIGN.get(),
                        ImmortalersDelightBlocks.PEARLIP_SHELL_WALL_SIGN.get()
                ));
        PEARLIP_SHELL_HANGING_SIGN = registerWithTab("pearlip_shell_hanging_sign",() ->
                new HangingSignItem(
                        ImmortalersDelightBlocks.PEARLIP_SHELL_HANGING_SIGN.get(),
                        ImmortalersDelightBlocks.PEARLIP_SHELL_WALL_HANGING_SIGN.get(),
                        (new Item.Properties()).stacksTo(16)
                ));
        PEARLIP_SHELL_BOAT = registerWithTab("pearlip_shell_boat",()->
                new ImmortalersBoatItem(ImmortalersBoat.Type.PEARLIP_SHELL, basicItem().stacksTo(1)));
        PEARLIP_SHELL_CHEST_BOAT = registerWithTab("pearlip_shell_chest_boat",()->
                new ImmortalersChestBoatItem(ImmortalersChestBoat.Type.PEARLIP_SHELL, basicItem().stacksTo(1)));

        /*
        古木
        */
        ANCIENT_WOOD_LOG = block(ImmortalersDelightBlocks.ANCIENT_WOOD_LOG);
        STRIPPED_ANCIENT_WOOD_LOG = block(ImmortalersDelightBlocks.STRIPPED_ANCIENT_WOOD_LOG);
        ANCIENT_WOOD = block(ImmortalersDelightBlocks.ANCIENT_WOOD);
        STRIPPED_ANCIENT_WOOD = block(ImmortalersDelightBlocks.STRIPPED_ANCIENT_WOOD);
        ANCIENT_WOOD_PLANKS = block(ImmortalersDelightBlocks.ANCIENT_WOOD_PLANKS);
        ANCIENT_WOOD_CABINET = block(ImmortalersDelightBlocks.ANCIENT_WOOD_CABINET);
        ANCIENT_WOOD_SLAB = block(ImmortalersDelightBlocks.ANCIENT_WOOD_SLAB);
        ANCIENT_WOOD_DOOR = block(ImmortalersDelightBlocks.ANCIENT_WOOD_DOOR);
        ANCIENT_WOOD_TRAPDOOR = block(ImmortalersDelightBlocks.ANCIENT_WOOD_TRAPDOOR);
        ANCIENT_WOOD_STAIRS = block(ImmortalersDelightBlocks.ANCIENT_WOOD_STAIRS);
        ANCIENT_WOOD_FENCE = block(ImmortalersDelightBlocks.ANCIENT_WOOD_FENCE);
        ANCIENT_WOOD_FENCE_GATE = block(ImmortalersDelightBlocks.ANCIENT_WOOD_FENCE_GATE);
        ANCIENT_WOOD_PRESSURE_PLATE = block(ImmortalersDelightBlocks.ANCIENT_WOOD_PRESSURE_PLATE);
        ANCIENT_WOOD_BUTTON = block(ImmortalersDelightBlocks.ANCIENT_WOOD_BUTTON);
        ANCIENT_WOOD_BOAT = registerWithTab("ancient_wood_boat",()->
                new ImmortalersBoatItem(ImmortalersBoat.Type.ANCIENT_WOOD, basicItem().stacksTo(1)));
        ANCIENT_WOOD_CHEST_BOAT = registerWithTab("ancient_wood_chest_boat",()->
                new ImmortalersChestBoatItem(ImmortalersChestBoat.Type.ANCIENT_WOOD, basicItem().stacksTo(1)));

        ANCIENT_FIBER = registerWithTab("ancient_fiber", () ->
                new Item(basicItem()));

        /*
        泥砖
         */
        MUD_TILES = block(ImmortalersDelightBlocks.MUD_TILES);
        MUD_TILES_STAIRS = block(ImmortalersDelightBlocks.MUD_TILES_STAIRS);
        MUD_TILES_SLAB = block(ImmortalersDelightBlocks.MUD_TILES_SLAB);
        MUD_TILES_WALL = block(ImmortalersDelightBlocks.MUD_TILES_WALL);
        CRACKED_MUD_TILES = block(ImmortalersDelightBlocks.CRACKED_MUD_TILES);
        CRACKED_MUD_TILES_STAIRS = block(ImmortalersDelightBlocks.CRACKED_MUD_TILES_STAIRS);
        CRACKED_MUD_TILES_SLAB = block(ImmortalersDelightBlocks.CRACKED_MUD_TILES_SLAB);
        CRACKED_MUD_TILES_WALL = block(ImmortalersDelightBlocks.CRACKED_MUD_TILES_WALL);
        MOSSY_MUD_BRICK = block(ImmortalersDelightBlocks.MOSSY_MUD_BRICK);
        MOSSY_MUD_BRICK_STAIRS = block(ImmortalersDelightBlocks.MOSSY_MUD_BRICK_STAIRS);
        MOSSY_MUD_BRICK_SLAB = block(ImmortalersDelightBlocks.MOSSY_MUD_BRICK_SLAB);
        MOSSY_MUD_BRICK_WALL = block(ImmortalersDelightBlocks.MOSSY_MUD_BRICK_WALL);
        CRACKED_MUD_BRICK = block(ImmortalersDelightBlocks.CRACKED_MUD_BRICK);
        CRACKED_MUD_BRICK_STAIRS = block(ImmortalersDelightBlocks.CRACKED_MUD_BRICK_STAIRS);
        CRACKED_MUD_BRICK_SLAB = block(ImmortalersDelightBlocks.CRACKED_MUD_BRICK_SLAB);
        CRACKED_MUD_BRICK_WALL = block(ImmortalersDelightBlocks.CRACKED_MUD_BRICK_WALL);
        CHISELED_MUD_BRICK = block(ImmortalersDelightBlocks.CHISELED_MUD_BRICK);
        PACKED_MUD_STAIRS = block(ImmortalersDelightBlocks.PACKED_MUD_STAIRS);
        PACKED_MUD_SLAB = block(ImmortalersDelightBlocks.PACKED_MUD_SLAB);
        PACKED_MUD_WALL = block(ImmortalersDelightBlocks.PACKED_MUD_WALL);


        /*
        地牢工具箱
         */
        POISONOUS_LONG_SPIKE_TRAP = block(ImmortalersDelightBlocks.POISONOUS_LONG_SPIKE_TRAP);
        LONG_SPIKE_TRAP = block(ImmortalersDelightBlocks.LONG_SPIKE_TRAP);
        POISONOUS_SPIKE_TRAP = block(ImmortalersDelightBlocks.POISONOUS_SPIKE_TRAP);
        SPIKE_TRAP = block(ImmortalersDelightBlocks.SPIKE_TRAP);
        POISONOUS_METAL_CALTROP = block(ImmortalersDelightBlocks.POISONOUS_METAL_CALTROP);
        METAL_CALTROP = block(ImmortalersDelightBlocks.METAL_CALTROP);
        SPIKE_BAR_BASE = block(ImmortalersDelightBlocks.SPIKE_BAR_BASE);
        SPIKE_BAR = block(ImmortalersDelightBlocks.SPIKE_BAR);
        INFESTED_GRAVEL = block(ImmortalersDelightBlocks.INFESTED_GRAVEL);
        INFESTED_SAND = block(ImmortalersDelightBlocks.INFESTED_SAND);
//        INFESTED_COAL_BLOCK = block(ImmortalersDelightBlocks.INFESTED_COAL_BLOCK);
//        INFESTED_COPPER_BLOCK = block(ImmortalersDelightBlocks.INFESTED_COPPER_BLOCK);
//        INFESTED_IRON_BLOCK = block(ImmortalersDelightBlocks.INFESTED_IRON_BLOCK);
//        INFESTED_REDSTONE_BLOCK = block(ImmortalersDelightBlocks.INFESTED_REDSTONE_BLOCK);
//        INFESTED_LAPIS_BLOCK = block(ImmortalersDelightBlocks.INFESTED_LAPIS_BLOCK);
//        INFESTED_GOLD_BLOCK = block(ImmortalersDelightBlocks.INFESTED_GOLD_BLOCK);
//        INFESTED_EMERALD_BLOCK = block(ImmortalersDelightBlocks.INFESTED_EMERALD_BLOCK);
//        INFESTED_DIAMOND_BLOCK = block(ImmortalersDelightBlocks.INFESTED_DIAMOND_BLOCK);

        /*
        材料类物品
        */

        EVOLUTCORN = registerWithTab("evolutcorn", () ->
                new Item(foodItem(ImmortalersDelightFoodProperties.EVOLUTCORN)));

        ROAST_EVOLUTCORN = registerWithTab("roast_evolutcorn", () ->
                new Item(foodItem(ImmortalersDelightFoodProperties.ROAST_EVOLUTCORN)));

        EVOLUTCORN_GRAINS = registerWithTab("evolutcorn_grains", () ->
                new ItemNameBlockItem(ImmortalersDelightBlocks.EVOLUTCORN.get(), new Item.Properties().food(ImmortalersDelightFoodProperties.EVOLUTCORN_GRAINS)));

        ROAST_EVOLUTCORN_CHOPS = registerWithTab("roast_evolutcorn_chops", () ->
                new Item(foodItem(ImmortalersDelightFoodProperties.ROAST_EVOLUTCORN_CHOPS)));

        PEARLIP = registerWithTab("pearlip", () ->
                new ItemNameBlockItem(ImmortalersDelightBlocks.PEARLIPEARL_BUNDLE.get(), new Item.Properties().food(ImmortalersDelightFoodProperties.PEARLIP)));

        PEARLIPEARL = registerWithTab("pearlipearl", () ->
                new ItemNameBlockItem(ImmortalersDelightBlocks.PEARLIPEARL_STALK.get(), new Item.Properties().food(ImmortalersDelightFoodProperties.PEARLIPEARL)));

        PEARLIP_SHELL = registerWithTab("pearlip_shell", () ->
                new Item(basicItem()));

        CONTAINS_TEA_LEISAMBOO = registerWithTab("contains_tea_leisamboo",()->
                new ItemNameBlockItem(ImmortalersDelightBlocks.LEISAMBOO_CROP.get(),new Item.Properties()));

        HIMEKAIDO_SEED = registerWithTab("himekaido_seed", () ->
                new ItemNameBlockItem(ImmortalersDelightBlocks.HIMEKAIDO_SHRUB.get(), new Item.Properties()));

        ROTTEN_FLESH_CUTS = registerWithTab("rotten_flesh_cuts", () ->
                new Item(new Item.Properties()));

        PITCHER_POD_PETAL = registerWithTab("pitcher_pod_petal",()->
                new Item(new Item.Properties()));

        KWAT_WHEAT_SEEDS =  registerWithTab("kwat_wheat_seeds",()->
                new ItemNameBlockItem(ImmortalersDelightBlocks.KWAT_WHEAT.get(), new Item.Properties()));

        KWAT_WHEAT = registerWithTab("kwat_wheat", () ->
                new GasToxicFoodItem(fantasticFoodItem(ImmortalersDelightFoodProperties.KWAT_WHEAT, Rarity.COMMON, false),true,false));

        ALFALFA_SEEDS = registerWithTab("alfalfa_seeds",()->
                new ItemNameBlockItem(ImmortalersDelightBlocks.ALFALFA.get(),basicItem()));

        ALFALFA = registerWithTab("alfalfa",()-> new Item(basicItem()));

        CUSTARD_TART_PASTRY = registerWithTab("custard_tart_pastry",()-> new Item(basicItem()));
        /*
        玉米系列物品
        */

        CRETACEOUS_ZEA_BALL = registerWithTab("cretaceous_zea_ball", () ->
                new PowerfulAbleFoodItem(foodItem(ImmortalersDelightFoodProperties.CRETACEOUS_ZEA_BALL), ImmortalersDelightFoodProperties.CRETACEOUS_ZEA_BALL_POWERED, true, false));

        COLORFUL_GRILLED_SKEWERS = registerWithTab("colorful_grilled_skewers", () ->
                new PowerfulAbleFoodItem(new Item.Properties().food(ImmortalersDelightFoodProperties.COLORFUL_GRILLED_SKEWERS), ImmortalersDelightFoodProperties.COLORFUL_GRILLED_SKEWERS_POWERED, true, false));

        POPOLUTCORN = registerWithTab("popolutcorn", () ->
                new PowerfulAbleFoodItem(new Item.Properties().food(ImmortalersDelightFoodProperties.POPOLUTCORN), ImmortalersDelightFoodProperties.POPOLUTCORN_POWERED, true, false));

        ZEA_PANCAKE = registerWithTab("zea_pancake", () ->
                new BlockItem(ImmortalersDelightBlocks.ZEA_PANCAKE.get(), basicItem()));

        ZEA_PANCAKE_SLICE = registerWithTab("zea_pancake_slice", () ->
                new ConsumableItem(foodItem(ImmortalersDelightFoodProperties.ZEA_PANCAKE_SLICE), true));

        EVOLUTCORN_PIE_CRUST = registerWithTab("evolutcorn_pie_crust",()->
                new Item(new Item.Properties()));

        PEARLIP_PIE = block(ImmortalersDelightBlocks.PEARLIP_PIE);

        PEARLIP_PIE_SLICE = registerWithTab("pearlip_pie_slice", () ->
                new ConsumableItem(bowlFoodItem(ImmortalersDelightFoodProperties.PEARLIP_PIE_SLICE), true));

        PEATIC_MUSA_SALAD = registerWithTab("peatic_musa_salad", () ->
                new PowerfulAbleFoodItem(bowlFoodItem(ImmortalersDelightFoodProperties.PEATIC_MUSA_SALAD), ImmortalersDelightFoodProperties.PEATIC_MUSA_SALAD_POWERED, true, false));

        EVOLUTCORN_HARD_CANDY = registerWithTab("evolutcorn_hard_candy", () ->
                new ShieldLikeFoodItem(foodItem(ImmortalersDelightFoodProperties.EVOLUTCORN_HARD_CANDY),ImmortalersDelightFoodProperties.EVOLUTCORN_HARD_CANDY_POWERED,
                        ImmortalersDelightFoodProperties.EVOLUTCORN_HARD_CANDY_AHEAD,ImmortalersDelightFoodProperties.EVOLUTCORN_HARD_CANDY_AHEAD_POWERED,
                        true,true)
        );

        /*
        香蕉系列物品
        */

        PEARLIP_MILK_SHAKE = registerWithTab("pearlip_milk_shake", () ->
                new DrinkableItem(new Item.Properties().craftRemainder(Items.GLASS_BOTTLE).stacksTo(16).food(ImmortalersDelightFoodProperties.PEARLIP_MILK_SHAKE), true, false));

        PEARLIP_PUMPKIN_PIE = registerWithTab("pearlip_pumpkin_pie", () ->
                new ConsumableItem(foodItem(ImmortalersDelightFoodProperties.PEARLIP_PUMPKIN_PIE),true));

        PEARLIPEARL_EGGSTEAM = registerWithTab("pearlipearl_eggsteam", () ->
                new ConsumableItem(foodItem(ImmortalersDelightFoodProperties.PEARLIPEARL_EGGSTEAM), true));

        PEARLIP_JELLY = registerWithTab("pearlip_jelly", () ->
                new ConsumableItem(foodItem(ImmortalersDelightFoodProperties.PEARLIP_JELLY), true));

        PEARLIPEARL_TART = registerWithTab("pearlipearl_tart", () ->
                new PowerfulAbleFoodItem((foodItem(ImmortalersDelightFoodProperties.PEARLIPEARL_TART)), ImmortalersDelightFoodProperties.PEARLIPEARL_TART_POWERED, true,false));

        ICE_PEARLIP = registerWithTab("ice_pearlip", () ->
                new ConsumableItem(foodItem(ImmortalersDelightFoodProperties.ICE_PEARLIP), true));

        CHOCOLATE_PEARLIP_STICKS = registerWithTab("chocolate_pearlip_sticks", () ->
                new ConsumableItem(foodItem(ImmortalersDelightFoodProperties.CHOCOLATE_PEARLIP_STICKS), true));

        PERFECT_SUMMER_ICE = registerWithTab("perfect_summer_ice",()->
                new ConsumableItem(new Item.Properties().food(ImmortalersDelightFoodProperties.PERFECT_SUMMER_ICE),true));

        TWILIGHT_GELATO = registerWithTab("twilight_gelato",()->
                new ConsumableItem(new Item.Properties().food(ImmortalersDelightFoodProperties.TWILIGHT_GELATO),true));

        PEARLIP_BUBBLE_MILK = registerWithTab("pearlip_bubble_milk",()->
                new ConsumableItem(drinkItem(ImmortalersDelightFoodProperties.PEARLIP_BUBBLE_MILK),true, true));

        CHOCOREEZE = registerWithTab("chocoreeze",()->
                new ConsumableItem(foodItem(ImmortalersDelightFoodProperties.CHOCOREEZE),true, false));

        TROPICAL_FRUITY_CYCLONE = registerWithTab("tropical_fruity_cyclone",()->
                new PowerfulAbleFoodItem(drinkItem(ImmortalersDelightFoodProperties.TROPICAL_FRUITY_CYCLONE),ImmortalersDelightFoodProperties.TROPICAL_FRUITY_CYCLONE_POWERED,true, false));

        BANANA_BOX_COD = registerWithTab("banana_box_cod",()->
                new PowerfulAbleFoodItem(new Item.Properties().food(ImmortalersDelightFoodProperties.BANANA_BOX_COD),ImmortalersDelightFoodProperties.BANANA_BOX_COD_POWERED,true,false));

        BANANA_BOX_SALMON = registerWithTab("banana_box_salmon",()->
                new PowerfulAbleFoodItem(new Item.Properties().food(ImmortalersDelightFoodProperties.BANANA_BOX_SALMON),ImmortalersDelightFoodProperties.BANANA_BOX_SALMON_POWERED,true,false));

        PEARLIP_RICE_ROLL_BOAT = registerWithTab("pearlip_rice_roll_boat",()->
                new BlockItem(ImmortalersDelightBlocks.PEARLIP_RICE_ROLL_BOAT.get(),new Item.Properties().stacksTo(1)));


        /*
         溪竹相关物品
        */
        LEAF_TEA = registerWithTab("leaf_tea",()->
                new DrinkItem(ImmortalersDelightBlocks.LEAF_TEA.get(), drinkItem(ImmortalersDelightFoodProperties.LEAF_TEA),true));

        EMPTY_BAMBOO_CUP = registerWithTab("empty_bamboo_cup", () ->
                new Item(new Item.Properties().stacksTo(16)));

        LEISAMBOO_TEA = registerWithTab("leisamboo_tea", () ->
                new DrinkableItem(leisambooDrinksItem(ImmortalersDelightFoodProperties.LEISAMBOO_TEA),true, false));

        ICED_BLACK_TEA = registerWithTab("iced_black_tea",()->
                new DrinkItem(ImmortalersDelightBlocks.ICED_BLACK_TEA.get(), drinkItem(ImmortalersDelightFoodProperties.ICED_BLACK_TEA),true));

        PEARLIPEARL_MILK_TEA = registerWithTab("pearlipearl_milk_tea",()->
                new DrinkItem(ImmortalersDelightBlocks.PEARLIPEARL_MILK_TEA.get(), drinkItem(ImmortalersDelightFoodProperties.PEARLIPEARL_MILK_TEA),true));

        PEARLIPEARL_MILK_GREEN = registerWithTab("pearlipearl_milk_green",()->
                new DrinkItem(ImmortalersDelightBlocks.PEARLIPEARL_MILK_GREEN.get(), drinkItem(ImmortalersDelightFoodProperties.PEARLIPEARL_MILK_GREEN),true));

        STOVE_BLACK_TEA = registerWithTab("stove_black_tea",() ->
                new DrinkItem(ImmortalersDelightBlocks.STOVE_BLACK_TEA.get(), drinkItem(ImmortalersDelightFoodProperties.STOVE_BLACK_TEA),true));

        LEAF_GREEN_TEA = registerWithTab("leaf_green_tea",() ->
                new DrinkItem(ImmortalersDelightBlocks.LEAF_GREEN_TEA.get(), drinkItem(ImmortalersDelightFoodProperties.LEAF_GREEN_TEA),true));

        BRITISH_YELLOW_TEA = registerWithTab("british_yellow_tea",() ->
                new DrinkItem(ImmortalersDelightBlocks.BRITISH_YELLOW_TEA.get(), drinkItem(ImmortalersDelightFoodProperties.BRITISH_YELLOW_TEA),true));

        CHERRY_PEARLIPEARL_TEA = registerWithTab("cherry_pearlipearl_tea",()->
                new ConsumableItem(drinkItem(ImmortalersDelightFoodProperties.CHERRY_PEARLIPEARL_TEA),true, false));

        GLEEMAN_TEAR = registerWithTab("gleeman_tear",()->
                new ConsumableItem(drinkItem(ImmortalersDelightFoodProperties.GLEEMAN_TEAR),true, false));

        FRUIT_TEA = registerWithTab("fruit_tea",()->
                new ConsumableItem(drinkItem(ImmortalersDelightFoodProperties.FRUIT_TEA),true, false));

        MILLENIAN_BAMBOO = registerWithTab("millenian_bamboo", () ->
                new BlockItem(ImmortalersDelightBlocks.MILLENIAN_BAMBOO.get(), basicItem()));

        BOWL_OF_MILLENIAN_BAMBOO = registerWithTab("bowl_of_millenian_bamboo", () ->
                new PowerfulAbleFoodItem(bowlFoodItem(ImmortalersDelightFoodProperties.BOWL_OF_MILLENIAN_BAMBOO),ImmortalersDelightFoodProperties.BOWL_OF_MILLENIAN_BAMBOO_POWERED,true, false));

        LEISAMBOO_TEA_CAKE = registerWithTab("leisamboo_tea_cake",()->
                new ConsumableItem(foodItem(ImmortalersDelightFoodProperties.LEISAMBOO_TEA_CAKE),true, false));

        /*
        通天竹
        */
        TRAVAPLANK = registerWithTab("travaplank",()->new Item(basicItem()));
        TRAVARICE = registerWithTab("travarice",()->new ItemNameBlockItem(ImmortalersDelightBlocks.TRAVASTRUGGLER_SAPLING.get(), basicItem()));
        COOKED_TRAVARICE = registerWithTab("cooked_travarice",()->new ConsumableItem(bowlFoodItem(ImmortalersDelightFoodProperties.COOKED_TRAVARICE),true));



        /*
        姬海棠系列物品
         */

        MASHED_POTATOES = foodItem("mashed_potatoes",ImmortalersDelightFoodProperties.MASHED_POTATOES);

        MASHED_POISONOUS_POTATO = registerWithTab("mashed_poisonous_potato",()->
                new ConsumableItem(new Item.Properties().food(ImmortalersDelightFoodProperties.MASHED_POISONOUS_POTATO)));

        MASHED_POTATO_WITH_JAM = registerWithTab("mashed_potato_with_jam",()->
                new PowerfulAbleFoodItem(new Item.Properties().food(ImmortalersDelightFoodProperties.MASHED_POTATO_WITH_JAM).craftRemainder(Items.BOWL),ImmortalersDelightFoodProperties.MASHED_POTATO_WITH_JAM_POWERED,true,false));

        MASHED_POISONOUS_POTATO_WITH_JAM = registerWithTab("mashed_poisonous_potato_with_jam",()->
                new PowerfulAbleFoodItem(new Item.Properties().food(ImmortalersDelightFoodProperties.MASHED_POISONOUS_POTATO_WITH_JAM).craftRemainder(Items.BOWL),ImmortalersDelightFoodProperties.MASHED_POISONOUS_POTATO_WITH_JAM_POWERED,true,false));

        HIMEKAIDO_YOGURT_PIE = block(ImmortalersDelightBlocks.HIMEKAIDO_YOGURT_PIE);

        HIMEKAIDO_YOGURT_PIE_SLICE = registerWithTab("himekaido_yogurt_pie_slice",()->
                new ConsumableItem(new Item.Properties().food(ImmortalersDelightFoodProperties.HIMEKAIDO_YOGURT_PIE_SLICE),true));

        SCARLET_GELATO = registerWithTab("scarlet_gelato",()->
                new ConsumableItem(foodItem(ImmortalersDelightFoodProperties.SCARLET_GELATO),true));

        SCARLET_SUNDAE = registerWithTab("scarlet_sundae",()->
                new ConsumableItem(bowlFoodItem(ImmortalersDelightFoodProperties.SCARLET_SUNDAE),true));

        HIMEKAIDO_CHAZUKE = registerWithTab("himekaido_chazuke",()->
                new PowerfulAbleFoodItem(bowlFoodItem(ImmortalersDelightFoodProperties.HIMEKAIDO_CHAZUKE),ImmortalersDelightFoodProperties.HIMEKAIDO_CHAZUKE_POWERED,true,false));

        HIMEKAIDO = registerWithTab("himekaido", () ->
                new ConsumableItem(fantasticFoodItem(ImmortalersDelightFoodProperties.HIMEKAIDO, Rarity.COMMON, false), true));

        GOLDEN_HIMEKAIDO = registerWithTab("golden_himekaido", () ->
                new GoldenHimkaidoFoodItem(fantasticFoodItem(ImmortalersDelightFoodProperties.GOLDEN_HIMEKAIDO, Rarity.RARE, false),true, true,false));

        ENCHANTED_GOLDEN_HIMEKAIDO = registerWithTab("enchanted_golden_himekaido", () ->
                new EnchantedGoldenHimekaidoFoodItem((new Item.Properties()).rarity(Rarity.EPIC).food(ImmortalersDelightFoodProperties.ENCHANTED_GOLDEN_HIMEKAIDO),true,true,true,3,1.0));

        HIMEKAIDO_JELLY = registerWithTab("himekaido_jelly", () ->
                new Item(new Item.Properties().craftRemainder(Items.GLASS_BOTTLE).stacksTo(16)));

        HIMEKANDY = registerWithTab("himekandy", () ->
                new ConsumableItem(fantasticFoodItem(ImmortalersDelightFoodProperties.HIMEKANDY, Rarity.COMMON, false), true));

        YOGURT = registerWithTab("yogurt", () ->
                new DrinkItem(ImmortalersDelightBlocks.YOGURT.get(), drinkItem(ImmortalersDelightFoodProperties.YOGURT),true));

        STUFFED_POISONOUS_POTATO = registerWithTab("stuffed_poisonous_potato", () ->
                new PowerfulAbleFoodItem(fantasticFoodItem(ImmortalersDelightFoodProperties.STUFFED_POISONOUS_POTATO, Rarity.COMMON, false),ImmortalersDelightFoodProperties.STUFFED_POISONOUS_POTATO_POWERED, true,false));

        BAKED_POISONOUS_POTATO = registerWithTab("baked_poisonous_potato", () ->
                new ConsumableItem(fantasticFoodItem(ImmortalersDelightFoodProperties.BAKED_POISONOUS_POTATO, Rarity.COMMON, false), true));

        BRAISED_SPIDER_EYES_BLOCK = registerWithTab("braised_spider_eyes_block", () ->
                new BlockItem(ImmortalersDelightBlocks.BRAISED_SPIDER_EYES_BLOCK.get(), basicItem()));

        BRAISED_SPIDER_EYES_IN_GRAVY = registerWithTab("braised_spider_eyes_in_gravy", () ->
                new ConsumableItem(bowlFoodItem(ImmortalersDelightFoodProperties.BRAISED_SPIDER_EYES_IN_GRAVY), true));


        TARTARE_CHICKEN = registerWithTab("tartare_chicken", () ->
                new PowerfulAbleFoodItem(bowlFoodItem(ImmortalersDelightFoodProperties.TARTARE_CHICKEN),ImmortalersDelightFoodProperties.TARTARE_CHICKEN_POWERED, true,false));


        DIPPED_ROTTEN_FLESH = registerWithTab("dipped_rotten_flesh", () ->
                new ConsumableItem(fantasticFoodItem(ImmortalersDelightFoodProperties.DIPPED_ROTTEN_FLESH, Rarity.COMMON, false), true));

        CRISPY_YOGURT_ROTTEN_FLESH = registerWithTab("crispy_yogurt_rotten_flesh", () ->
                new PowerfulAbleFoodItem(bowlFoodItem(ImmortalersDelightFoodProperties.CRISPY_YOGURT_ROTTEN_FLESH),ImmortalersDelightFoodProperties.CRISPY_YOGURT_ROTTEN_FLESH_POWERED, true,false));

        MEATY_ROTTEN_TOMATO_BROTH = registerWithTab("meaty_rotten_tomato_broth", () ->
                new PowerfulAbleFoodItem(bowlFoodItem(ImmortalersDelightFoodProperties.MEATY_ROTTEN_TOMATO_BROTH),ImmortalersDelightFoodProperties.MEATY_ROTTEN_TOMATO_BROTH_POWERED, true,false));

        PUFFERFISH_ROLL = registerWithTab("pufferfish_roll", () ->
                new PowerfulAbleFoodItem(fantasticFoodItem(ImmortalersDelightFoodProperties.PUFFERFISH_ROLL, Rarity.COMMON, false),ImmortalersDelightFoodProperties.PUFFERFISH_ROLL_POWERED, true,false));

        STEWED_ROTTEN_MEAT_POT = registerWithTab("stewed_rotten_meat_pot", () ->
                new BlockItem(ImmortalersDelightBlocks.STEWED_ROTTEN_MEAT_POT.get(), basicItem()));

        BOWL_OF_STEWED_ROTTEN_MEAT_IN_CLAY_POT = registerWithTab("bowl_of_stewed_rotten_meat_in_clay_pot", () ->
                new PowerfulAbleFoodItem(bowlFoodItem(ImmortalersDelightFoodProperties.BOWL_OF_STEWED_ROTTEN_MEAT_IN_CLAY_POT), ImmortalersDelightFoodProperties.BOWL_OF_STEWED_ROTTEN_MEAT_IN_CLAY_POT_POWERED, true,false));
        /*
        瓦斯麦相关物品
        */

        KWAT_WHEAT_DOUGH = registerWithTab("kwat_wheat_dough",()->
                new Item(new Item.Properties()));

        KWAT_WHEAT_PASTE = registerWithTab("kwat_wheat_paste",()->
                new Item(new Item.Properties().stacksTo(16).craftRemainder(Items.GLASS_BOTTLE)));

        KWAT_WHEAT_TOAST = registerWithTab("kwat_wheat_toast",() ->new PowerfulAbleFoodItem(foodItem(ImmortalersDelightFoodProperties.KWAT_WHEAT_TOAST),ImmortalersDelightFoodProperties.KWAT_WHEAT_TOAST_POWERED,true,false));

        KWAT_WHEAT_TOAST_SLICE = registerWithTab("kwat_wheat_toast_slice",() ->new PowerfulAbleFoodItem(foodItem(ImmortalersDelightFoodProperties.KWAT_WHEAT_TOAST_SLICE),ImmortalersDelightFoodProperties.KWAT_WHEAT_TOAST_SLICE_POWERED,true,false));

        NETHER_BREAD_CREAM_SOUP = block(ImmortalersDelightBlocks.NETHER_BREAD_CREAM_SOUP);

        NETHER_CREAM_SOUP = registerWithTab("nether_cream_soup",() ->
                new PowerfulAbleFoodItem(bowlFoodItem(ImmortalersDelightFoodProperties.NETHER_CREAM_SOUP),ImmortalersDelightFoodProperties.NETHER_CREAM_SOUP_POWERED,true,false));

        NETHER_CREAM_BREAD = registerWithTab("nether_cream_bread",() ->
                new PowerfulAbleFoodItem(bowlFoodItem(ImmortalersDelightFoodProperties.NETHER_CREAM_BREAD),ImmortalersDelightFoodProperties.NETHER_CREAM_BREAD_POWERED,true,false));

        KWAT_WHEAT_DOUFU = registerWithTab("kwat_wheat_doufu",() -> new ConsumableItem(bowlFoodItem(ImmortalersDelightFoodProperties.KWAT_WHEAT_DOUFU),true));

        FRY_KWAT_WHEAT_DOUFU = registerWithTab("fry_kwat_wheat_doufu",()->
                new ConsumableItem(new Item.Properties().food(ImmortalersDelightFoodProperties.FRY_KWAT_WHEAT_DOUFU),true,false));

        SUTFFED_KWAT_WHEAT_DOUFU = registerWithTab("sutffed_kwat_wheat_doufu",()->
                new PowerfulAbleFoodItem(new Item.Properties().food(ImmortalersDelightFoodProperties.SUTFFED_KWAT_WHEAT_DOUFU), ImmortalersDelightFoodProperties.SUTFFED_KWAT_WHEAT_DOUFU_POWERED,true, false));

        JADE_AND_RUBY_SOUP = registerWithTab("jade_and_ruby_soup",()->
                new PowerfulAbleFoodItem(bowlFoodItem(ImmortalersDelightFoodProperties.JADE_AND_RUBY_SOUP),ImmortalersDelightFoodProperties.JADE_AND_RUBY_SOUP_POWERED,true,false));

        SCARLET_DEVILS_CAKE = registerWithTab("scarlet_devils_cake", () ->
                new BlockItem(ImmortalersDelightBlocks.SCARLET_DEVILS_CAKE.get(), basicItem()));

        SCARLET_DEVILS_CAKE_SLICE = registerWithTab("scarlet_devils_cake_slice",()->
                new ConsumableItem(new Item.Properties().food(ImmortalersDelightFoodProperties.SCARLET_DEVILS_CAKE_SLICE),true,false));

        RED_STUFFED_BUN = registerWithTab("red_stuffed_bun",()->
                new PowerfulAbleFoodItem(new Item.Properties().food(ImmortalersDelightFoodProperties.RED_STUFFED_BUN),ImmortalersDelightFoodProperties.RED_STUFFED_BUN_POWERED,true,false));

//        SPICY_PUDDING = registerWithTab("spicy_pudding",()->
//                new ConsumableItem(new Item.Properties().food(ImmortalersDelightFoodProperties.SPICY_PUDDING),true,false));

        /*
        冒险相关物品
         */
        GOLDEN_FABRIC = register("golden_fabric", () ->
                new ConsumableItem(fantasticItem(Rarity.RARE),false,true));

        GOLDEN_FABRIC_VEIL = registerWithTab("golden_fabric_veil", () ->
                new GoldenFabricArmor(ImmortalersArmorMaterials.GOLDEN_FABRIC,ArmorItem.Type.HELMET,fantasticItem(Rarity.RARE)));

        RAW_SNIFFER_SLICE = REGISTER.register("raw_sniffer_slice", () ->
                new ConsumableItem(fantasticFoodItem(ImmortalersDelightFoodProperties.RAW_SNIFFER_SLICE, Rarity.COMMON, false), true));

        COOKED_SNIFFER_SLICE = REGISTER.register("cooked_sniffer_slice", () ->
                new ConsumableItem(fantasticFoodItem(ImmortalersDelightFoodProperties.COOKED_SNIFFER_SLICE, Rarity.COMMON, false), true));

        RAW_SNIFFER_STEAK = REGISTER.register("raw_sniffer_steak", () ->
                new ConsumableItem(fantasticFoodItem(ImmortalersDelightFoodProperties.RAW_SNIFFER_STEAK, Rarity.COMMON, false), true));

        COOKED_SNIFFER_STEAK = REGISTER.register("cooked_sniffer_steak", () ->
                new ConsumableItem(fantasticFoodItem(ImmortalersDelightFoodProperties.COOKED_SNIFFER_STEAK, Rarity.COMMON, false), true));

        SNIFFER_ROTATING_ROAST_MEAT = REGISTER.register("sniffer_rotating_roast_meat",()->
                new ItemNameBlockItem(ImmortalersDelightBlocks.SNIFFER_ROTATING_ROAST_MEAT.get(), new Item.Properties()));

        SNIFFER_FUR = registerWithTab("sniffer_fur", () ->
                new ConsumableItem(fantasticItem(Rarity.COMMON)));

        BRUSH = REGISTER.register("brush",()->
                new SnifferBrushItem((new Item.Properties()).durability(64)));


        SNIFFER_FUR_BRUSH = registerWithTab("sniffer_fur_brush", () ->
                new SnifferBrushItem((new Item.Properties()).durability(781).rarity(Rarity.UNCOMMON)));

        SACHETS = registerWithTab("sachets", () ->
                new ConsumableItem(fantasticItem(Rarity.UNCOMMON)));

        DRILL_ROD_WAND = registerWithTab("drill_rod_wand", () ->
                new DrillRodItem(3,-2.4F, ImmortalersTiers.MAGIC_POWERED, BlockTags.MINEABLE_WITH_PICKAXE,ImmortalersDelightTags.MINEABLE_WITH_DRILL_ROD,new Item.Properties(),4));
        /*
        火把花相关物品
        */

        TORCHFLOWER_MUSTARD = registerWithTab("torchflower_mustard",()->
                new Item(new Item.Properties()));

        TORCHFLOWER_COOKIE = foodItem("torchflower_cookie",ImmortalersDelightFoodProperties.TORCHFLOWER_COOKIE,true);

        TORCHFLOWER_CAKE = foodItem("torchflower_cake",ImmortalersDelightFoodProperties.TORCHFLOWER_CAKE,true);

        TORCHFLOWER_CURRY_RICE = registerWithTab("torchflower_curry_rice",()->
                new PowerfulAbleFoodItem(bowlFoodItem(ImmortalersDelightFoodProperties.TORCHFLOWER_CURRY_RICE), ImmortalersDelightFoodProperties.TORCHFLOWER_CURRY_RICE_POWERED,true,false));

        EXTRA_SPICY_PASTA = registerWithTab("extra_spicy_pasta",()->
                new PowerfulAbleFoodItem(bowlFoodItem(ImmortalersDelightFoodProperties.EXTRA_SPICY_PASTA),ImmortalersDelightFoodProperties.EXTRA_SPICY_PASTA_POWERED,true,false));

        HOT_HI_SOUP = registerWithTab("hot_hi_soup",()->
                new PowerfulAbleFoodItem(bowlFoodItem(ImmortalersDelightFoodProperties.HOT_HI_SOUP),ImmortalersDelightFoodProperties.HOT_HI_SOUP_POWERED,true,false));

        INCANDESCENCE_SUSHI = registerWithTab("incandescence_sushi",() -> new PowerfulAbleFoodItem(foodItem(ImmortalersDelightFoodProperties.INCANDESCENCE_SUSHI),ImmortalersDelightFoodProperties.INCANDESCENCE_SUSHI_POWERED,true,false));

        /*
        瓶子草相关物品
         */
        PITCHER_PLANT_BARBECUE = registerWithTab("pitcher_plant_barbecue",()->
                new PowerfulAbleFoodItem(new Item.Properties().food(ImmortalersDelightFoodProperties.PITCHER_PLANT_BARBECUE),ImmortalersDelightFoodProperties.PITCHER_PLANT_BARBECUE_POWERED,true,false));

        POD_SHELL_BURGER_MEAT = block(ImmortalersDelightBlocks.POD_SHELL_BURGER_MEAT);

        POD_SHELL_BURGER_MEAT_CUBE = registerWithTab("pod_shell_burger_meat_cube",()->
                new PowerfulAbleFoodItem(new Item.Properties().food(ImmortalersDelightFoodProperties.POD_SHELL_BURGER_MEAT_CUBE), ImmortalersDelightFoodProperties.POD_SHELL_BURGER_MEAT_CUBE_POWERED,true,false));

        AROMATIC_POD_AFFOGATO = registerWithTab("aromatic_pod_affogato",()->
                new ConsumableItem(drinkItem(ImmortalersDelightFoodProperties.AROMATIC_POD_AFFOGATO),true, false));

        ALFALFA_PORRIDGE = registerWithTab("alfalfa_porridge",()->
                new PowerfulAbleFoodItem(bowlFoodItem(ImmortalersDelightFoodProperties.ALFALFA_PORRIDGE),ImmortalersDelightFoodProperties.ALFALFA_PORRIDGE_POWERED,true,false));


        //酒品
        DREUMK_WINE = registerWithTab("dreumk_wine",()->
                new InebriatedToxicFoodItem(drinkItem(ImmortalersDelightFoodProperties.DREUMK_WINE),true,false));

        EVOLUTCORN_BEER = registerWithTab("evolutcorn_beer",()->
                new InebriatedToxicFoodItem(drinkItem(ImmortalersDelightFoodProperties.EVOLUTCORN_BEER), true, false));

        STICKY_BEER = registerWithTab("sticky_beer",()->
                new InebriatedToxicFoodItem(drinkItem(ImmortalersDelightFoodProperties.STICKY_BEER), true, false));

        VULCAN_COKTAIL = registerWithTab("vulcan_coktail",()->
                new InebriatedToxicFoodItem(drinkItem(ImmortalersDelightFoodProperties.VULCAN_COKTAIL),true,false));

        CLEAR_WATER_VODKA = registerWithTab("clear_water_vodka", () ->
                new InebriatedToxicFoodItem(drinkItem(ImmortalersDelightFoodProperties.CLEAR_WATER_VODKA),true,false,false,true));

        NETHER_KVASS = registerWithTab("nether_kvass", () ->
                new InebriatedToxicFoodItem(drinkItem(ImmortalersDelightFoodProperties.NETHER_KVASS),true,false,false,true));

        PURGATORY_ALE = registerWithTab("purgatory_ale",()->
                new InebriatedToxicFoodItem(drinkItem(ImmortalersDelightFoodProperties.PURGATORY_ALE),true,false,false,true));

        TRAVEER = registerWithTab("traveer",()-> new InebriatedToxicFoodItem(drinkItem(ImmortalersDelightFoodProperties.TRAVEER),true,false,false,true));

        TRAVASTRUGGLER_LOG = block(ImmortalersDelightBlocks.TRAVASTRUGGLER_LOG);
        STRIPPED_TRAVASTRUGGLER_LOG = block(ImmortalersDelightBlocks.STRIPPED_TRAVASTRUGGLER_LOG);
        TRAVA_PLANKS = block(ImmortalersDelightBlocks.TRAVA_PLANKS);
        TRAVASTRUGGLER_LEAVES = block(ImmortalersDelightBlocks.TRAVASTRUGGLER_LEAVES);
        TRAVASTRUGGLER_LEAVES_TRAVARICE = block(ImmortalersDelightBlocks.TRAVASTRUGGLER_LEAVES_TRAVARICE);

//        PALE_DEW_WINE = registerWithTab("pale_dew_wine",()->
//                new InebriatedToxicFoodItem(drinkItem(ImmortalersDelightFoodProperties.PALE_DEW_WINE),true,false));

        //刷怪蛋
//        STRANGE_ARMOUR_STAND_SPAWN_EGG = registerWithTab("strange_armour_stand_spawn_egg",()->
//                new SpawnEggItem(ImmortalersDelightEntities.STRANGE_ARMOUR_STAND.get(),0x252525,0x4D4D4D,new Item.Properties()));
    }

    public static RegistryObject<Item> registerWithTab(String name, Supplier<Item> supplier) {
        RegistryObject<Item> item = REGISTER.register(name, supplier);
        CREATIVE_TAB_ITEMS.add(item);
        return item;
    }

    public static RegistryObject<Item> register(String name, Supplier<Item> supplier) {
        return REGISTER.register(name, supplier);
    }

    public static Item.Properties basicItem() {
        return new Item.Properties();
    }

    public static Item.Properties foodItem(FoodProperties food) {
        return (new Item.Properties()).food(food);
    }

    public static RegistryObject<Item> foodItem(String name,FoodProperties food) {
        return registerWithTab(name,()->new Item(new Item.Properties().food(food)));
    }

    public static RegistryObject<Item> foodItem(String name,FoodProperties food,boolean hasFoodEffectTooltip) {
        return registerWithTab(name,()->new ConsumableItem(new Item.Properties().food(food),hasFoodEffectTooltip));
    }

    public static Item.Properties bowlFoodItem(FoodProperties food) {
        return (new Item.Properties()).food(food).craftRemainder(Items.BOWL).stacksTo(16);
    }

    public static Item.Properties drinkItem(FoodProperties foodProperties) {
        return (new Item.Properties()).craftRemainder(Items.GLASS_BOTTLE).stacksTo(16).food(foodProperties);
    }

    public static Item.Properties leisambooDrinksItem(FoodProperties foodProperties) {
        return (new Item.Properties()).craftRemainder(EMPTY_BAMBOO_CUP.get()).stacksTo(16).food(foodProperties);
    }
    private static Item.Properties fantasticItem(Rarity soCool) {
        return (new Item.Properties()).rarity(soCool);
    }


    private static Item.Properties fantasticFoodItem(FoodProperties foodProperties, Rarity soCool, boolean isBowl) {
        if (isBowl) {
            return new Item.Properties().rarity(soCool).food(foodProperties).craftRemainder(Items.BOWL).stacksTo(16);
        }
        return (new Item.Properties().rarity(soCool).food(foodProperties));
    }

    private static RegistryObject<Item> block(RegistryObject<Block> block) {
        return registerWithTab(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
    }
}

