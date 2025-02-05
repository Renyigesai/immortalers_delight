package com.renyigesai.immortalers_delight.init;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.common.item.ConsumableItem;
import vectorwing.farmersdelight.common.item.DrinkableItem;

public class ImmortalersDelightItems {

    public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, ImmortalersDelightMod.MODID);


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
    public static final RegistryObject<Item> POPOLUTCORN;
    public static final RegistryObject<Item> PEARLIPEARL;
    public static final RegistryObject<Item> PEATIC_MUSA_SALAD;
    public static final RegistryObject<Item> PEARLIP_MILK_SHAKE;
    public static final RegistryObject<Item> PEARLIP_PUMPKIN_PIE;
    public static final RegistryObject<Item> PEARLIPEARL_TART;
    public static final RegistryObject<Item> PEARLIPEARL_EGGSTEAM;
    public static final RegistryObject<Item> PEARLIP_JELLY;
    public static final RegistryObject<Item> ZEA_PANCAKE_SLICE;
    public static final RegistryObject<Item> ENCHANTAL_COOLER;

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
    public static final RegistryObject<Item> HIMEKAIDO_CUTS;

    static {

        //Items
        ANCIENT_FIBER = REGISTER.register("ancient_fiber",() -> new Item(new Item.Properties()));
        PEARLIP_SHELL = item("pearlip_shell");
//        BANANA_STALK = item("banana_stalk");
        PEARLIP = foodItem("pearlip",ImmortalersDelightFoodProperties.PEARLIP);

        //Foods
        BOWL_OF_MILLENIAN_BAMBOO = REGISTER.register("bowl_of_millenian_bamboo",()->
                new ConsumableItem(bowlFoodItem(ImmortalersDelightFoodProperties.BOWL_OF_MILLENIAN_BAMBOO)));
        CRETACEOUS_ZEA_BALL = foodItem("cretaceous_zea_ball",ImmortalersDelightFoodProperties.CRETACEOUS_ZEA_BALL,true);
        COLORFUL_GRILLED_SKEWERS = foodItem("colorful_grilled_skewers",ImmortalersDelightFoodProperties.COLORFUL_GRILLED_SKEWERS);
        EVOLUTCORN = foodItem("evolutcorn",ImmortalersDelightFoodProperties.EVOLUTCORN);
        ROAST_EVOLUTCORN = foodItem("roast_evolutcorn",ImmortalersDelightFoodProperties.ROAST_EVOLUTCORN);
        EVOLUTCORN_GRAINS = REGISTER.register("evolutcorn_grains",() ->
                new ItemNameBlockItem(ImmortalersDelightBlocks.EVOLUTCORN.get(),new Item.Properties().food( ImmortalersDelightFoodProperties.EVOLUTCORN_GRAINS)));
        ROAST_EVOLUTCORN_CHOPS = foodItem("roast_evolutcorn_chops",ImmortalersDelightFoodProperties.ROAST_EVOLUTCORN_CHOPS);
        POPOLUTCORN = REGISTER.register("popolutcorn",() -> new ConsumableItem(new Item.Properties().food(ImmortalersDelightFoodProperties.POPOLUTCORN),true));
        PEARLIPEARL = foodItem("pearlipearl",ImmortalersDelightFoodProperties.PEARLIPEARL);
        PEATIC_MUSA_SALAD = REGISTER.register("peatic_musa_salad",() ->
                new ConsumableItem(bowlFoodItem(ImmortalersDelightFoodProperties.PEATIC_MUSA_SALAD),true));
        PEARLIP_MILK_SHAKE = REGISTER.register("pearlip_milk_shake",() ->
                new DrinkableItem(new Item.Properties().craftRemainder(Items.GLASS_BOTTLE).stacksTo(16).food(ImmortalersDelightFoodProperties.PEARLIP_MILK_SHAKE),true,false));
        PEARLIP_PUMPKIN_PIE = foodItem("pearlip_pumpkin_pie",ImmortalersDelightFoodProperties.PEARLIP_PUMPKIN_PIE);
        PEARLIPEARL_EGGSTEAM = foodItem("pearlipearl_eggsteam",ImmortalersDelightFoodProperties.PEARLIPEARL_EGGSTEAM,true);
        PEARLIP_JELLY = foodItem("pearlip_jelly",ImmortalersDelightFoodProperties.PEARLIP_JELLY,true);
        PEARLIPEARL_TART = foodItem("pearlipearl_tart",ImmortalersDelightFoodProperties.PEARLIPEARL_TART);
        ZEA_PANCAKE_SLICE = foodItem("zea_pancake_slice",ImmortalersDelightFoodProperties.ZEA_PANCAKE_SLICE);
        ENCHANTAL_COOLER = block(ImmortalersDelightBlocks.ENCHANTAL_COOLER);

        /*
        姬海棠系列材料
         */
        HIMEKAIDO_SEED = REGISTER.register("himekaido_seed",() ->
                new ItemNameBlockItem(ImmortalersDelightBlocks.HIMEKAIDO_SHRUB.get(), new Item.Properties()));
        ROTTEN_FLESH_CUTS = item("rotten_flesh_cuts");
        HIMEKAIDO_JELLY = item("himekaido_jelly");
        HIMEKAIDO_CUTS = item("himekaido_cuts");
        /*
        姬海棠系列食物
         */
        YOGURT = item("yogurt");
        HIMEKAIDO = foodItem("himekaido",
                ImmortalersDelightFoodProperties.HIMEKAIDO);
        BAKED_POISONOUS_POTATO = foodItem("baked_poisonous_potato",
                ImmortalersDelightFoodProperties.BAKED_POISONOUS_POTATO);
        BRAISED_SPIDER_EYES_IN_GRAVY = foodItem("braised_spider_eyes_in_gravy",
                ImmortalersDelightFoodProperties.BRAISED_SPIDER_EYES_IN_GRAVY);
        TARTARE_CHICKEN = foodItem("tartare_chicken",
                ImmortalersDelightFoodProperties.TARTARE_CHICKEN);
        DIPPED_ROTTEN_FLESH = foodItem("dipped_rotten_flesh",
                ImmortalersDelightFoodProperties.DIPPED_ROTTEN_FLESH);
        CRISPY_YOGURT_ROTTEN_FLESH = foodItem("crispy_yogurt_rotten_flesh",
                ImmortalersDelightFoodProperties.CRISPY_YOGURT_ROTTEN_FLESH);
        MEATY_ROTTEN_TOMATO_BROTH = foodItem("meaty_rotten_tomato_broth",
                ImmortalersDelightFoodProperties.MEATY_ROTTEN_TOMATO_BROTH);
        STUFFED_POISONOUS_POTATO = foodItem("stuffed_poisonous_potato",
                ImmortalersDelightFoodProperties.STUFFED_POISONOUS_POTATO);
        PUFFERFISH_ROLL = foodItem("pufferfish_roll",
                ImmortalersDelightFoodProperties.PUFFERFISH_ROLL);
        BOWL_OF_STEWED_ROTTEN_MEAT_IN_CLAY_POT = foodItem("bowl_of_stewed_rotten_meat_in_clay_pot",
                ImmortalersDelightFoodProperties.BOWL_OF_STEWED_ROTTEN_MEAT_IN_CLAY_POT);
        GOLDEN_HIMEKAIDO = fantasticFoodItem("golden_himekaido",
                ImmortalersDelightFoodProperties.GOLDEN_HIMEKAIDO, Rarity.RARE,true);
        ENCHANTED_GOLDEN_HIMEKAIDO = REGISTER.register("enchanted_golden_himekaido",() ->
                new EnchantedGoldenAppleItem((new Item.Properties()).rarity(Rarity.EPIC).food(ImmortalersDelightFoodProperties.ENCHANTED_GOLDEN_HIMEKAIDO)));

    }

    private static RegistryObject<Item> item(String pName) {
        return REGISTER.register(pName, () -> new Item(new Item.Properties()));
    }
    private static RegistryObject<Item> fantasticItem(String pName,Rarity soCool) {
        return REGISTER.register(pName, () -> new Item(new Item.Properties().rarity(soCool)));
    }

    private static RegistryObject<Item> foodItem(String pName, FoodProperties foodProperties) {
        return REGISTER.register(pName, () -> new Item(new Item.Properties().food(foodProperties)));
    }

    private static RegistryObject<Item> foodItem(String pName, FoodProperties foodProperties,boolean tool) {
        return REGISTER.register(pName, () -> new ConsumableItem(new Item.Properties().food(foodProperties),tool));
    }
    private static RegistryObject<Item> fantasticFoodItem(String pName,FoodProperties foodProperties,Rarity soCool, boolean tool) {
        return REGISTER.register(pName, () -> new ConsumableItem(new Item.Properties().rarity(soCool).food(foodProperties),tool));
    }
    private static RegistryObject<Item> foodItem(String pName, FoodProperties foodProperties,boolean tool,boolean cTool) {
        return REGISTER.register(pName, () -> new ConsumableItem(new Item.Properties().food(foodProperties),tool,cTool));
    }

    public static Item.Properties bowlFoodItem(FoodProperties food) {
        return new Item.Properties().food(food).craftRemainder(Items.BOWL).stacksTo(16);
    }

    private static RegistryObject<Item> block(RegistryObject<Block> block) {
        return REGISTER.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
    }
}

