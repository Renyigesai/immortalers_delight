package com.renyigesai.immortalers_delight.init;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.world.food.FoodProperties;
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
//    public static final RegistryObject<Item> BANANA_STALK;
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
    }

    private static RegistryObject<Item> item(String pName) {
        return REGISTER.register(pName, () -> new Item(new Item.Properties()));
    }

    private static RegistryObject<Item> foodItem(String pName, FoodProperties foodProperties) {
        return REGISTER.register(pName, () -> new Item(new Item.Properties().food(foodProperties)));
    }

    private static RegistryObject<Item> foodItem(String pName, FoodProperties foodProperties,boolean tool) {
        return REGISTER.register(pName, () -> new ConsumableItem(new Item.Properties().food(foodProperties),tool));
    }

    private static RegistryObject<Item> foodItem(String pName, FoodProperties foodProperties,boolean tool,boolean cTool) {
        return REGISTER.register(pName, () -> new ConsumableItem(new Item.Properties().food(foodProperties),tool,cTool));
    }

    public static Item.Properties bowlFoodItem(FoodProperties food) {
        return new Item.Properties().food(food).craftRemainder(Items.BOWL).stacksTo(16);
    }

    private static RegistryObject<Item> BLOCK(RegistryObject<Block> block) {
        return REGISTER.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
    }
}

