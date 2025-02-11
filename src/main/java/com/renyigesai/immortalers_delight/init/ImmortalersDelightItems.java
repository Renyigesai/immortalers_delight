package com.renyigesai.immortalers_delight.init;

import com.google.common.collect.Sets;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.item.DrinkItem;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.common.FoodValues;
import vectorwing.farmersdelight.common.item.ConsumableItem;
import vectorwing.farmersdelight.common.item.DrinkableItem;
import vectorwing.farmersdelight.common.registry.ModBlocks;

import java.util.LinkedHashSet;
import java.util.function.Supplier;


public class ImmortalersDelightItems {


    public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, ImmortalersDelightMod.MODID);
    public static LinkedHashSet<RegistryObject<Item>> CREATIVE_TAB_ITEMS = Sets.newLinkedHashSet();

    //方块物品 BlockItems
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
    public static final RegistryObject<Item> MILLENIAN_BAMBOO;
    public static final RegistryObject<Item> ZEA_PANCAKE;
    public static final RegistryObject<Item> STEWED_ROTTEN_MEAT_POT;
    public static final RegistryObject<Item> BRAISED_SPIDER_EYES_BLOCK;
    public static final RegistryObject<Item> EVOLUTCORN_GRAIN_BAG;
    public static final RegistryObject<Item> HIMEKAIDO_CRATE;
    public static final RegistryObject<Item> PEARLIP_CRATE;
    public static final RegistryObject<Item> EVOLUTCORN_BLOCK;
    //物品 Items
    public static final RegistryObject<Item> ANCIENT_FIBER;
    public static final RegistryObject<Item> BOWL_OF_MILLENIAN_BAMBOO;
    public static final RegistryObject<Item> PEARLIP;
    public static final RegistryObject<Item> PEARLIP_SHELL;
    public static final RegistryObject<Item> CRETACEOUS_ZEA_BALL;
    public static final RegistryObject<Item> COLORFUL_GRILLED_SKEWERS;
    public static final RegistryObject<Item> EVOLUTCORN;
    public static final RegistryObject<Item> ROAST_EVOLUTCORN;
    public static final RegistryObject<Item> EVOLUTCORN_GRAINS;
    public static final RegistryObject<Item> ROAST_EVOLUTCORN_CHOPS;
//    public static final RegistryObject<Item> PEARLIPEARL_STALK;
    public static final RegistryObject<Item> POPOLUTCORN;
    public static final RegistryObject<Item> PEARLIPEARL;
    public static final RegistryObject<Item> PEATIC_MUSA_SALAD;
    public static final RegistryObject<Item> PEARLIP_MILK_SHAKE;
    public static final RegistryObject<Item> PEARLIP_PUMPKIN_PIE;
    public static final RegistryObject<Item> PEARLIPEARL_TART;
    public static final RegistryObject<Item> PEARLIPEARL_EGGSTEAM;
    public static final RegistryObject<Item> PEARLIP_JELLY;
    public static final RegistryObject<Item> ZEA_PANCAKE_SLICE;
//    public static final RegistryObject<Item> ENCHANTAL_COOLER;

    /*
    姬海棠系列物品
     */
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
    /*
     溪竹相关物品
     */

    public static final RegistryObject<Item> EMPTY_BAMBOO_CUP;
    public static final RegistryObject<Item> LEISAMBOO_TEA;


    static {
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


        //Items
        ANCIENT_FIBER = registerWithTab("ancient_fiber",() ->
                new Item(basicItem()));

        CRETACEOUS_ZEA_BALL = registerWithTab("cretaceous_zea_ball",()->
                new ConsumableItem(foodItem(ImmortalersDelightFoodProperties.CRETACEOUS_ZEA_BALL),true));

        COLORFUL_GRILLED_SKEWERS = registerWithTab("colorful_grilled_skewers",()->
                new Item(foodItem(ImmortalersDelightFoodProperties.COLORFUL_GRILLED_SKEWERS)));

        EVOLUTCORN = registerWithTab("evolutcorn",()->
                new Item(foodItem(ImmortalersDelightFoodProperties.EVOLUTCORN)));

        ROAST_EVOLUTCORN =  registerWithTab("roast_evolutcorn",()->
                new Item(foodItem(ImmortalersDelightFoodProperties.ROAST_EVOLUTCORN)));

        EVOLUTCORN_GRAINS = registerWithTab("evolutcorn_grains",() ->
                new ItemNameBlockItem(ImmortalersDelightBlocks.EVOLUTCORN.get(), new Item.Properties().food(ImmortalersDelightFoodProperties.EVOLUTCORN_GRAINS)));

        ROAST_EVOLUTCORN_CHOPS =registerWithTab("roast_evolutcorn_chops",()->
                new Item(foodItem(ImmortalersDelightFoodProperties.ROAST_EVOLUTCORN_CHOPS)));

        POPOLUTCORN = registerWithTab("popolutcorn",() ->
                new ConsumableItem(new Item.Properties().food(ImmortalersDelightFoodProperties.POPOLUTCORN), true));


        PEATIC_MUSA_SALAD = registerWithTab("peatic_musa_salad",() ->
                new ConsumableItem(bowlFoodItem(ImmortalersDelightFoodProperties.PEATIC_MUSA_SALAD), true));

        PEARLIP_MILK_SHAKE = registerWithTab("pearlip_milk_shake",() ->
                new DrinkableItem(new Item.Properties().craftRemainder(Items.GLASS_BOTTLE).stacksTo(16).food(ImmortalersDelightFoodProperties.PEARLIP_MILK_SHAKE), true, false));

        PEARLIP_PUMPKIN_PIE = registerWithTab("pearlip_pumpkin_pie",() ->
                new Item(foodItem(ImmortalersDelightFoodProperties.PEARLIP_PUMPKIN_PIE)));
        PEARLIPEARL = registerWithTab ("pearlipearl",()->
                new ItemNameBlockItem(ImmortalersDelightBlocks.PEARLIPEARL_STALK.get(),new Item.Properties().food(ImmortalersDelightFoodProperties.PEARLIPEARL)));

        PEARLIP_SHELL = registerWithTab("pearlip_shell",() ->
                new Item(basicItem()));

//        PEARLIPEARL_STALK = block(ImmortalersDelightBlocks.PEARLIPEARL_STALK);

        PEARLIP = registerWithTab ("pearlip",()->
                new ItemNameBlockItem(ImmortalersDelightBlocks.PEARLIPEARL_BUNDLE.get(),new Item.Properties().food(ImmortalersDelightFoodProperties.PEARLIP)));
        PEARLIPEARL_EGGSTEAM = registerWithTab("pearlipearl_eggsteam",()->
                new ConsumableItem(foodItem(ImmortalersDelightFoodProperties.PEARLIPEARL_EGGSTEAM),true));

        PEARLIP_JELLY = registerWithTab("pearlip_jelly",()->
                new ConsumableItem(foodItem(ImmortalersDelightFoodProperties.PEARLIP_JELLY),true));

        PEARLIPEARL_TART = registerWithTab("pearlipearl_tart",() ->
                new Item(foodItem(ImmortalersDelightFoodProperties.PEARLIPEARL_TART)));
        ZEA_PANCAKE = registerWithTab("zea_pancake", () ->
                new BlockItem(ImmortalersDelightBlocks.ZEA_PANCAKE.get(), basicItem()));

        ZEA_PANCAKE_SLICE = registerWithTab("zea_pancake_slice",() ->
                new Item(foodItem(ImmortalersDelightFoodProperties.ZEA_PANCAKE_SLICE)));
//        ENCHANTAL_COOLER = block(ImmortalersDelightBlocks.ENCHANTAL_COOLER);

        /*
         溪竹相关物品
        */
        EMPTY_BAMBOO_CUP = registerWithTab("empty_bamboo_cup",() ->
                new Item(new Item.Properties().stacksTo(16)));

        LEISAMBOO_TEA = registerWithTab("leisamboo_tea",() ->
                new DrinkItem(new Item.Properties().stacksTo(1), EMPTY_BAMBOO_CUP.get()));

        MILLENIAN_BAMBOO = registerWithTab("millenian_bamboo", () ->
                new BlockItem(ImmortalersDelightBlocks.MILLENIAN_BAMBOO.get(), basicItem()));
        BOWL_OF_MILLENIAN_BAMBOO = registerWithTab("bowl_of_millenian_bamboo",()->
                new Item(bowlFoodItem(ImmortalersDelightFoodProperties.BOWL_OF_MILLENIAN_BAMBOO)));
        /*
        姬海棠系列材料
         */
        HIMEKAIDO_SEED = registerWithTab("himekaido_seed",() ->
                new ItemNameBlockItem(ImmortalersDelightBlocks.HIMEKAIDO_SHRUB.get(), new Item.Properties()));

        ROTTEN_FLESH_CUTS = registerWithTab("rotten_flesh_cuts",()->
                new Item(new Item.Properties()));
        /*
        姬海棠系列食物
         */
        HIMEKAIDO = registerWithTab("himekaido", () ->
                new ConsumableItem(fantasticFoodItem(ImmortalersDelightFoodProperties.HIMEKAIDO, Rarity.COMMON, false), true));

        GOLDEN_HIMEKAIDO = registerWithTab("golden_himekaido", () ->
                new ConsumableItem(fantasticFoodItem(ImmortalersDelightFoodProperties.GOLDEN_HIMEKAIDO, Rarity.RARE, false), true));

        ENCHANTED_GOLDEN_HIMEKAIDO = registerWithTab("enchanted_golden_himekaido",() ->
                new EnchantedGoldenAppleItem((new Item.Properties()).rarity(Rarity.EPIC).food(ImmortalersDelightFoodProperties.ENCHANTED_GOLDEN_HIMEKAIDO)));

        HIMEKAIDO_JELLY = registerWithTab("himekaido_jelly",() ->
                new DrinkableItem(new Item.Properties().craftRemainder(Items.GLASS_BOTTLE).stacksTo(16).food(ImmortalersDelightFoodProperties.HIMEKAIDO_JELLY),true,false));

        YOGURT = registerWithTab("yogurt",() ->
                new DrinkableItem(new Item.Properties().craftRemainder(Items.GLASS_BOTTLE).stacksTo(16).food(ImmortalersDelightFoodProperties.YOGURT),true,false));

        STUFFED_POISONOUS_POTATO = registerWithTab("stuffed_poisonous_potato",()->
                new ConsumableItem(fantasticFoodItem(ImmortalersDelightFoodProperties.STUFFED_POISONOUS_POTATO, Rarity.COMMON,false), true));

        BAKED_POISONOUS_POTATO = registerWithTab("baked_poisonous_potato", () ->
                new ConsumableItem(fantasticFoodItem(ImmortalersDelightFoodProperties.BAKED_POISONOUS_POTATO, Rarity.COMMON, false), true));

        BRAISED_SPIDER_EYES_BLOCK = registerWithTab("braised_spider_eyes_block", () ->
                new BlockItem(ImmortalersDelightBlocks.BRAISED_SPIDER_EYES_BLOCK.get(), basicItem()));

        BRAISED_SPIDER_EYES_IN_GRAVY = registerWithTab("braised_spider_eyes_in_gravy", () ->
                new ConsumableItem(fantasticFoodItem(ImmortalersDelightFoodProperties.BRAISED_SPIDER_EYES_IN_GRAVY, Rarity.COMMON, false), true));


        TARTARE_CHICKEN = registerWithTab("tartare_chicken", () ->
                new ConsumableItem(fantasticFoodItem(ImmortalersDelightFoodProperties.TARTARE_CHICKEN, Rarity.COMMON, false), true));


        DIPPED_ROTTEN_FLESH = registerWithTab("dipped_rotten_flesh", () ->
                new ConsumableItem(fantasticFoodItem(ImmortalersDelightFoodProperties.DIPPED_ROTTEN_FLESH, Rarity.COMMON, false), true));

        CRISPY_YOGURT_ROTTEN_FLESH = registerWithTab("crispy_yogurt_rotten_flesh", () ->
                new ConsumableItem(fantasticFoodItem(ImmortalersDelightFoodProperties.CRISPY_YOGURT_ROTTEN_FLESH, Rarity.COMMON, false), true));

        MEATY_ROTTEN_TOMATO_BROTH = registerWithTab("meaty_rotten_tomato_broth", () ->
                new ConsumableItem(fantasticFoodItem(ImmortalersDelightFoodProperties.MEATY_ROTTEN_TOMATO_BROTH, Rarity.COMMON, false), true));

        PUFFERFISH_ROLL = registerWithTab("pufferfish_roll", () ->
                new ConsumableItem(fantasticFoodItem(ImmortalersDelightFoodProperties.PUFFERFISH_ROLL, Rarity.COMMON, false), true));

        STEWED_ROTTEN_MEAT_POT = registerWithTab("stewed_rotten_meat_pot", () ->
                new BlockItem(ImmortalersDelightBlocks.STEWED_ROTTEN_MEAT_POT.get(), basicItem()));

        BOWL_OF_STEWED_ROTTEN_MEAT_IN_CLAY_POT = registerWithTab("bowl_of_stewed_rotten_meat_in_clay_pot", () ->
                new ConsumableItem(fantasticFoodItem(ImmortalersDelightFoodProperties.BOWL_OF_STEWED_ROTTEN_MEAT_IN_CLAY_POT, Rarity.COMMON, false), true));


    }
    public static RegistryObject<Item> registerWithTab(String name, Supplier<Item> supplier) {
        RegistryObject<Item> item = REGISTER.register(name, supplier);
        CREATIVE_TAB_ITEMS.add(item);
        return item;
    }
    public static Item.Properties basicItem() {
        return new Item.Properties();
    }

    public static Item.Properties foodItem(FoodProperties food) {
        return (new Item.Properties()).food(food);
    }

    public static Item.Properties bowlFoodItem(FoodProperties food) {
        return(new Item.Properties()).food(food).craftRemainder(Items.BOWL).stacksTo(16);
    }
    public static Item.Properties drinkItem() {
        return (new Item.Properties()).craftRemainder(Items.GLASS_BOTTLE).stacksTo(16);
    }
    private static Item.Properties fantasticItem(Rarity soCool) {
        return (new Item.Properties()).rarity(soCool);
    }


    private static Item.Properties fantasticFoodItem(FoodProperties foodProperties,Rarity soCool,boolean isBowl) {
        if (isBowl) {
            return new Item.Properties().rarity(soCool).food(foodProperties).craftRemainder(Items.BOWL).stacksTo(16);
        }
        return (new Item.Properties().rarity(soCool).food(foodProperties));
    }


    /*private static RegistryObject<Item> block(RegistryObject<Block> block) {
        return registerWithTab(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
    }*/
}

