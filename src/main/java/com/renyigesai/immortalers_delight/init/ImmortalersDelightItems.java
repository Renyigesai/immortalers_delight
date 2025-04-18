package com.renyigesai.immortalers_delight.init;

import com.google.common.collect.Sets;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.entities.ImmortalersBoat;
import com.renyigesai.immortalers_delight.entities.ImmortalersChestBoat;
import com.renyigesai.immortalers_delight.item.*;
import com.renyigesai.immortalers_delight.util.datautil.worlddata.BaseImmortalWorldData;
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
    public static final RegistryObject<Item> HIMEKAIDO_CHEST_BOAT;
    public static final RegistryObject<Item> MILLENIAN_BAMBOO;
    public static final RegistryObject<Item> STEWED_ROTTEN_MEAT_POT;
    public static final RegistryObject<Item> BRAISED_SPIDER_EYES_BLOCK;
    public static final RegistryObject<Item> EVOLUTCORN_GRAIN_BAG;
    public static final RegistryObject<Item> HIMEKAIDO_CRATE;
    public static final RegistryObject<Item> PEARLIP_CRATE;
    public static final RegistryObject<Item> EVOLUTCORN_BLOCK;
    public static final RegistryObject<Item> HIMEKAIDO_SIGN;
    public static final RegistryObject<Item> HIMEKAIDO_HANGING_SIGN;
    public static final RegistryObject<Item> LEISAMBOO_PLANKS;
    public static final RegistryObject<Item> LEISAMBOO_STAIRS;
    public static final RegistryObject<Item> LEISAMBOO_SLAB;
    public static final RegistryObject<Item> LEISAMBOO_FENCE;
    public static final RegistryObject<Item> LEISAMBOO_FENCE_GATE;
    public static final RegistryObject<Item> LEISAMBOO_PRESSURE_PLATE;
    public static final RegistryObject<Item> LEISAMBOO_BUTTON;
    public static final RegistryObject<Item> ANCIENT_FIBER;
    public static final RegistryObject<Item> BOWL_OF_MILLENIAN_BAMBOO;
    public static final RegistryObject<Item> PITCHER_POD_PETAL;
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
    public static final RegistryObject<Item> HIMEKAIDO_YOGURT_PIE;
    public static final RegistryObject<Item> HIMEKAIDO_YOGURT_PIE_SLICE;
    public static final RegistryObject<Item> DREUMK_WINE;
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
    /*
    瓦斯麦相关物品
    */
    public static final RegistryObject<Item> KWAT_WHEAT_SEEDS;
    public static final RegistryObject<Item> KWAT_WHEAT;
    public static final RegistryObject<Item> KWAT_WHEAT_DOUGH;
    public static final RegistryObject<Item> KWAT_WHEAT_PASTE;
    public static final RegistryObject<Item> KWAT_WHEAT_DOUFU;
    public static final RegistryObject<Item> FRY_KWAT_WHEAT_DOUFU;
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

    public static final RegistryObject<Item> GOLDEN_FABRIC;
    public static final RegistryObject<Item> GOLDEN_FABRIC_VEIL;
    public static final RegistryObject<Item> RAW_SNIFFER_SLICE;
    public static final RegistryObject<Item> COOKED_SNIFFER_SLICE;
    public static final RegistryObject<Item> RAW_SNIFFER_STEAK;
    public static final RegistryObject<Item> COOKED_SNIFFER_STEAK;
    public static final RegistryObject<Item> SNIFFER_HIDE;
    public static final RegistryObject<Item> CLEAR_WATER_VODKA;
    public static final RegistryObject<Item> SNIFFER_ROTATING_ROAST_MEAT;
    public static final RegistryObject<Item> VULCAN_COKTAIL;
    //public static final RegistryObject<Item> TEST_DAMAGE_ITEM;


    static {

        ENCHANTAL_COOLER = block(ImmortalersDelightBlocks.ENCHANTAL_COOLER);

        EVOLUTCORN_GRAIN_BAG = registerWithTab("evolutcorn_grain_bag", () ->
                new BlockItem(ImmortalersDelightBlocks.EVOLUTCORN_GRAIN_BAG.get(), basicItem()));

        HIMEKAIDO_CRATE = registerWithTab("himekaido_crate", () ->
                new BlockItem(ImmortalersDelightBlocks.HIMEKAIDO_CRATE.get(), basicItem()));

        PEARLIP_CRATE = registerWithTab("pearlip_crate", () ->
                new BlockItem(ImmortalersDelightBlocks.PEARLIP_CRATE.get(), basicItem()));

        EVOLUTCORN_BLOCK = registerWithTab("evolutcorn_block", () ->
                new BlockItem(ImmortalersDelightBlocks.EVOLUTCORN_BLOCK.get(), basicItem()));

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

        LEISAMBOO_PLANKS = registerWithTab("leisamboo_planks", () ->
                new BlockItem(ImmortalersDelightBlocks.LEISAMBOO_PLANKS.get(), basicItem()));

        LEISAMBOO_STAIRS = registerWithTab("leisamboo_stairs", () ->
                new BlockItem(ImmortalersDelightBlocks.LEISAMBOO_STAIRS.get(), basicItem()));

        LEISAMBOO_SLAB = registerWithTab("leisamboo_slab", () ->
                new BlockItem(ImmortalersDelightBlocks.LEISAMBOO_SLAB.get(), basicItem()));

        LEISAMBOO_FENCE = registerWithTab("leisamboo_fence", () ->
                new BlockItem(ImmortalersDelightBlocks.LEISAMBOO_FENCE.get(), basicItem()));

        LEISAMBOO_FENCE_GATE = registerWithTab("leisamboo_fence_gate", () ->
                new BlockItem(ImmortalersDelightBlocks.LEISAMBOO_FENCE_GATE.get(), basicItem()));

        LEISAMBOO_PRESSURE_PLATE = registerWithTab("leisamboo_pressure_plate", () ->
                new BlockItem(ImmortalersDelightBlocks.LEISAMBOO_PRESSURE_PLATE.get(), basicItem()));

        LEISAMBOO_BUTTON = registerWithTab("leisamboo_button", () ->
                new BlockItem(ImmortalersDelightBlocks.LEISAMBOO_BUTTON.get(), basicItem()));

        ANCIENT_FIBER = registerWithTab("ancient_fiber", () ->
                new Item(basicItem()));
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

        /*
        玉米系列物品
        */

        CRETACEOUS_ZEA_BALL = registerWithTab("cretaceous_zea_ball", () ->
                new ConsumableItem(foodItem(ImmortalersDelightFoodProperties.CRETACEOUS_ZEA_BALL), true));

        COLORFUL_GRILLED_SKEWERS = registerWithTab("colorful_grilled_skewers", () ->
                new Item(foodItem(ImmortalersDelightFoodProperties.COLORFUL_GRILLED_SKEWERS)));

        POPOLUTCORN = registerWithTab("popolutcorn", () ->
                new ConsumableItem(new Item.Properties().food(ImmortalersDelightFoodProperties.POPOLUTCORN), true));

        ZEA_PANCAKE = registerWithTab("zea_pancake", () ->
                new BlockItem(ImmortalersDelightBlocks.ZEA_PANCAKE.get(), basicItem()));

        ZEA_PANCAKE_SLICE = registerWithTab("zea_pancake_slice", () ->
                new Item(foodItem(ImmortalersDelightFoodProperties.ZEA_PANCAKE_SLICE)));

        EVOLUTCORN_PIE_CRUST = registerWithTab("evolutcorn_pie_crust",()->
                new Item(new Item.Properties()));

        PEARLIP_PIE = block(ImmortalersDelightBlocks.PEARLIP_PIE);

        PEARLIP_PIE_SLICE = foodItem("pearlip_pie_slice",ImmortalersDelightFoodProperties.PEARLIP_PIE_SLICE);

        PEATIC_MUSA_SALAD = registerWithTab("peatic_musa_salad", () ->
                new ConsumableItem(bowlFoodItem(ImmortalersDelightFoodProperties.PEATIC_MUSA_SALAD), true));

        /*
        香蕉系列物品
        */

        PEARLIP_MILK_SHAKE = registerWithTab("pearlip_milk_shake", () ->
                new DrinkableItem(new Item.Properties().craftRemainder(Items.GLASS_BOTTLE).stacksTo(16).food(ImmortalersDelightFoodProperties.PEARLIP_MILK_SHAKE), true, false));

        PEARLIP_PUMPKIN_PIE = registerWithTab("pearlip_pumpkin_pie", () ->
                new Item(foodItem(ImmortalersDelightFoodProperties.PEARLIP_PUMPKIN_PIE)));

        PEARLIPEARL_EGGSTEAM = registerWithTab("pearlipearl_eggsteam", () ->
                new ConsumableItem(foodItem(ImmortalersDelightFoodProperties.PEARLIPEARL_EGGSTEAM), true));

        PEARLIP_JELLY = registerWithTab("pearlip_jelly", () ->
                new ConsumableItem(foodItem(ImmortalersDelightFoodProperties.PEARLIP_JELLY), true));

        PEARLIPEARL_TART = registerWithTab("pearlipearl_tart", () ->
                new Item(foodItem(ImmortalersDelightFoodProperties.PEARLIPEARL_TART)));
        /*
         溪竹相关物品
        */
        LEAF_TEA = registerWithTab("leaf_tea",()->
                new DrinkableItem(drinkItem(ImmortalersDelightFoodProperties.LEAF_TEA),true, false));

        EMPTY_BAMBOO_CUP = registerWithTab("empty_bamboo_cup", () ->
                new Item(new Item.Properties().stacksTo(16)));

        LEISAMBOO_TEA = registerWithTab("leisamboo_tea", () ->
                new DrinkableItem(leisambooDrinksItem(ImmortalersDelightFoodProperties.LEISAMBOO_TEA),true, false));

        ICED_BLACK_TEA = registerWithTab("iced_black_tea",() ->
                new DrinkableItem(drinkItem(ImmortalersDelightFoodProperties.ICED_BLACK_TEA),true, false));

        PEARLIPEARL_MILK_TEA = registerWithTab("pearlipearl_milk_tea",() ->
                new DrinkableItem(drinkItem(ImmortalersDelightFoodProperties.PEARLIPEARL_MILK_TEA),true, false));

        PEARLIPEARL_MILK_GREEN = registerWithTab("pearlipearl_milk_green",() ->
                new DrinkableItem(drinkItem(ImmortalersDelightFoodProperties.PEARLIPEARL_MILK_GREEN),true, false));

        STOVE_BLACK_TEA = registerWithTab("stove_black_tea",() ->
                new DrinkableItem(drinkItem(ImmortalersDelightFoodProperties.STOVE_BLACK_TEA),true, false));

        LEAF_GREEN_TEA = registerWithTab("leaf_green_tea",() ->
                new DrinkableItem(drinkItem(ImmortalersDelightFoodProperties.LEAF_GREEN_TEA),true, false));

        BRITISH_YELLOW_TEA = registerWithTab("british_yellow_tea",() ->
                new DrinkableItem(drinkItem(ImmortalersDelightFoodProperties.BRITISH_YELLOW_TEA),true, false));

        MILLENIAN_BAMBOO = registerWithTab("millenian_bamboo", () ->
                new BlockItem(ImmortalersDelightBlocks.MILLENIAN_BAMBOO.get(), basicItem()));

        BOWL_OF_MILLENIAN_BAMBOO = registerWithTab("bowl_of_millenian_bamboo", () ->
                new ConsumableItem(bowlFoodItem(ImmortalersDelightFoodProperties.BOWL_OF_MILLENIAN_BAMBOO),true, false));
        /*
        姬海棠系列物品
         */

        MASHED_POTATOES = foodItem("mashed_potatoes",ImmortalersDelightFoodProperties.MASHED_POTATOES);

        MASHED_POISONOUS_POTATO = registerWithTab("mashed_poisonous_potato",()->
                new ConsumableItem(new Item.Properties().food(ImmortalersDelightFoodProperties.MASHED_POISONOUS_POTATO)));

        MASHED_POTATO_WITH_JAM = registerWithTab("mashed_potato_with_jam",()->
                new ConsumableItem(new Item.Properties().food(ImmortalersDelightFoodProperties.MASHED_POTATO_WITH_JAM).craftRemainder(Items.BOWL),true));

        MASHED_POISONOUS_POTATO_WITH_JAM = registerWithTab("mashed_poisonous_potato_with_jam",()->
                new ConsumableItem(new Item.Properties().food(ImmortalersDelightFoodProperties.MASHED_POISONOUS_POTATO_WITH_JAM).craftRemainder(Items.BOWL),true));

        HIMEKAIDO_YOGURT_PIE = block(ImmortalersDelightBlocks.HIMEKAIDO_YOGURT_PIE);

        HIMEKAIDO_YOGURT_PIE_SLICE = foodItem("himekaido_yogurt_pie_slice",ImmortalersDelightFoodProperties.HIMEKAIDO_YOGURT_PIE_SLICE);

        HIMEKAIDO = registerWithTab("himekaido", () ->
                new ConsumableItem(fantasticFoodItem(ImmortalersDelightFoodProperties.HIMEKAIDO, Rarity.COMMON, false), true));

        GOLDEN_HIMEKAIDO = registerWithTab("golden_himekaido", () ->
                new GoldenHimkaidoFoodItem(fantasticFoodItem(ImmortalersDelightFoodProperties.GOLDEN_HIMEKAIDO, Rarity.RARE, false),true, true,false));

        ENCHANTED_GOLDEN_HIMEKAIDO = registerWithTab("enchanted_golden_himekaido", () ->
                new EnchantedGoldenHimekaidoFoodItem((new Item.Properties()).rarity(Rarity.EPIC).food(ImmortalersDelightFoodProperties.ENCHANTED_GOLDEN_HIMEKAIDO),true,true,true,3,1.0));

        HIMEKAIDO_JELLY = registerWithTab("himekaido_jelly", () ->
                new DrinkableItem(new Item.Properties().craftRemainder(Items.GLASS_BOTTLE).stacksTo(16).food(ImmortalersDelightFoodProperties.HIMEKAIDO_JELLY), true, false));

        YOGURT = registerWithTab("yogurt", () ->
                new DrinkableItem(new Item.Properties().craftRemainder(Items.GLASS_BOTTLE).stacksTo(16).food(ImmortalersDelightFoodProperties.YOGURT), true, false));

        STUFFED_POISONOUS_POTATO = registerWithTab("stuffed_poisonous_potato", () ->
                new ConsumableItem(fantasticFoodItem(ImmortalersDelightFoodProperties.STUFFED_POISONOUS_POTATO, Rarity.COMMON, false), true));

        BAKED_POISONOUS_POTATO = registerWithTab("baked_poisonous_potato", () ->
                new ConsumableItem(fantasticFoodItem(ImmortalersDelightFoodProperties.BAKED_POISONOUS_POTATO, Rarity.COMMON, false), true));

        BRAISED_SPIDER_EYES_BLOCK = registerWithTab("braised_spider_eyes_block", () ->
                new BlockItem(ImmortalersDelightBlocks.BRAISED_SPIDER_EYES_BLOCK.get(), basicItem()));

        BRAISED_SPIDER_EYES_IN_GRAVY = registerWithTab("braised_spider_eyes_in_gravy", () ->
                new ConsumableItem(bowlFoodItem(ImmortalersDelightFoodProperties.BRAISED_SPIDER_EYES_IN_GRAVY), true));


        TARTARE_CHICKEN = registerWithTab("tartare_chicken", () ->
                new ConsumableItem(bowlFoodItem(ImmortalersDelightFoodProperties.TARTARE_CHICKEN), true));


        DIPPED_ROTTEN_FLESH = registerWithTab("dipped_rotten_flesh", () ->
                new ConsumableItem(fantasticFoodItem(ImmortalersDelightFoodProperties.DIPPED_ROTTEN_FLESH, Rarity.COMMON, false), true));

        CRISPY_YOGURT_ROTTEN_FLESH = registerWithTab("crispy_yogurt_rotten_flesh", () ->
                new ConsumableItem(bowlFoodItem(ImmortalersDelightFoodProperties.CRISPY_YOGURT_ROTTEN_FLESH), true));

        MEATY_ROTTEN_TOMATO_BROTH = registerWithTab("meaty_rotten_tomato_broth", () ->
                new ConsumableItem(bowlFoodItem(ImmortalersDelightFoodProperties.MEATY_ROTTEN_TOMATO_BROTH), true));

        PUFFERFISH_ROLL = registerWithTab("pufferfish_roll", () ->
                new ConsumableItem(fantasticFoodItem(ImmortalersDelightFoodProperties.PUFFERFISH_ROLL, Rarity.COMMON, false), true));

        STEWED_ROTTEN_MEAT_POT = registerWithTab("stewed_rotten_meat_pot", () ->
                new BlockItem(ImmortalersDelightBlocks.STEWED_ROTTEN_MEAT_POT.get(), basicItem()));

        BOWL_OF_STEWED_ROTTEN_MEAT_IN_CLAY_POT = registerWithTab("bowl_of_stewed_rotten_meat_in_clay_pot", () ->
                new ConsumableItem(bowlFoodItem(ImmortalersDelightFoodProperties.BOWL_OF_STEWED_ROTTEN_MEAT_IN_CLAY_POT), true));
        /*
        瓦斯麦相关物品
        */

        KWAT_WHEAT_DOUGH = registerWithTab("kwat_wheat_dough",()->
                new Item(new Item.Properties()));

        KWAT_WHEAT_PASTE = registerWithTab("kwat_wheat_paste",()->
                new Item(new Item.Properties().stacksTo(16).craftRemainder(Items.GLASS_BOTTLE)));

        KWAT_WHEAT_TOAST = foodItem("kwat_wheat_toast",ImmortalersDelightFoodProperties.KWAT_WHEAT_TOAST);

        KWAT_WHEAT_TOAST_SLICE = foodItem("kwat_wheat_toast_slice",ImmortalersDelightFoodProperties.KWAT_WHEAT_TOAST_SLICE);

        NETHER_BREAD_CREAM_SOUP = block(ImmortalersDelightBlocks.NETHER_BREAD_CREAM_SOUP);

        NETHER_CREAM_SOUP = registerWithTab("nether_cream_soup",() ->
                new ConsumableItem(bowlFoodItem(ImmortalersDelightFoodProperties.NETHER_CREAM_SOUP),true,false));

        NETHER_CREAM_BREAD = registerWithTab("nether_cream_bread",() ->
                new ConsumableItem(bowlFoodItem(ImmortalersDelightFoodProperties.NETHER_CREAM_BREAD),true,false));

        KWAT_WHEAT_DOUFU = foodItem("kwat_wheat_doufu",ImmortalersDelightFoodProperties.KWAT_WHEAT_DOUFU);

        FRY_KWAT_WHEAT_DOUFU = registerWithTab("fry_kwat_wheat_doufu",()->
                new ConsumableItem(new Item.Properties().food(ImmortalersDelightFoodProperties.FRY_KWAT_WHEAT_DOUFU),true,false));

        JADE_AND_RUBY_SOUP = registerWithTab("jade_and_ruby_soup",()->
                new ConsumableItem(bowlFoodItem(ImmortalersDelightFoodProperties.JADE_AND_RUBY_SOUP),true));

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

        SNIFFER_HIDE = REGISTER.register("sniffer_hide", () ->
                new ConsumableItem(fantasticItem(Rarity.COMMON)));

        /*
        火把花相关物品
        */

        TORCHFLOWER_MUSTARD = registerWithTab("torchflower_mustard",()->
                new Item(new Item.Properties()));

        TORCHFLOWER_COOKIE = foodItem("torchflower_cookie",ImmortalersDelightFoodProperties.TORCHFLOWER_COOKIE,true);

        TORCHFLOWER_CAKE = foodItem("torchflower_cake",ImmortalersDelightFoodProperties.TORCHFLOWER_CAKE,true);

        TORCHFLOWER_CURRY_RICE = registerWithTab("torchflower_curry_rice",()->
                new ConsumableItem(bowlFoodItem(ImmortalersDelightFoodProperties.TORCHFLOWER_CURRY_RICE),true));

        EXTRA_SPICY_PASTA = registerWithTab("extra_spicy_pasta",()->
                new ConsumableItem(bowlFoodItem(ImmortalersDelightFoodProperties.EXTRA_SPICY_PASTA),true));

        HOT_HI_SOUP = registerWithTab("hot_hi_soup",()->
                new ConsumableItem(bowlFoodItem(ImmortalersDelightFoodProperties.HOT_HI_SOUP),true));

        INCANDESCENCE_SUSHI = foodItem("incandescence_sushi",ImmortalersDelightFoodProperties.INCANDESCENCE_SUSHI,true);

        /*火把花相关物品*/
        TORCHFLOWER_MUSTARD = registerWithTab("torchflower_mustard",()->
                new Item(new Item.Properties()));

        TORCHFLOWER_COOKIE = foodItem("torchflower_cookie",ImmortalersDelightFoodProperties.TORCHFLOWER_COOKIE,true);

        TORCHFLOWER_CAKE = foodItem("torchflower_cake",ImmortalersDelightFoodProperties.TORCHFLOWER_CAKE,true);

        TORCHFLOWER_CURRY_RICE = registerWithTab("torchflower_curry_rice",()->
                new ConsumableItem(bowlFoodItem(ImmortalersDelightFoodProperties.TORCHFLOWER_CURRY_RICE),true));

        EXTRA_SPICY_PASTA = registerWithTab("extra_spicy_pasta",()->
                new ConsumableItem(bowlFoodItem(ImmortalersDelightFoodProperties.EXTRA_SPICY_PASTA),true));

        HOT_HI_SOUP = registerWithTab("hot_hi_soup",()->
                new ConsumableItem(bowlFoodItem(ImmortalersDelightFoodProperties.HOT_HI_SOUP),true));

        INCANDESCENCE_SUSHI = foodItem("incandescence_sushi",ImmortalersDelightFoodProperties.INCANDESCENCE_SUSHI,true);


        //酒品
        DREUMK_WINE = registerWithTab("dreumk_wine",()->
                new InebriatedToxicFoodItem(drinkItem(ImmortalersDelightFoodProperties.DREUMK_WINE),true,false));

        EVOLUTCORN_BEER = registerWithTab("evolutcorn_beer",()->
                new InebriatedToxicFoodItem(new Item.Properties().craftRemainder(Items.GLASS_BOTTLE).stacksTo(16).food(ImmortalersDelightFoodProperties.EVOLUTCORN_BEER), true, false));

        VULCAN_COKTAIL = registerWithTab("vulcan_coktail",()->
                new InebriatedToxicFoodItem(drinkItem(ImmortalersDelightFoodProperties.VULCAN_COKTAIL),true,false));

        CLEAR_WATER_VODKA = registerWithTab("clear_water_vodka", () ->
                new InebriatedToxicFoodItem(fantasticFoodItem(ImmortalersDelightFoodProperties.CLEAR_WATER_VODKA, Rarity.COMMON, false),true,false,false,true));

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

    public static Item.Properties drinkItem() {
        return (new Item.Properties()).craftRemainder(Items.GLASS_BOTTLE).stacksTo(16);
    }

    public static Item.Properties drinkItem(FoodProperties foodProperties) {
        return (new Item.Properties()).craftRemainder(Items.GLASS_BOTTLE).stacksTo(16).food(foodProperties);
    }

    public static Item.Properties leisambooDrinksItem() {
        return (new Item.Properties()).craftRemainder(EMPTY_BAMBOO_CUP.get()).stacksTo(16);
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

