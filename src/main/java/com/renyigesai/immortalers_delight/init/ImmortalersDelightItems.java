package com.renyigesai.immortalers_delight.init;

import com.google.common.collect.Sets;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.api.annotation.ItemData;
import com.renyigesai.immortalers_delight.entities.boat.ImmortalersBoat;
import com.renyigesai.immortalers_delight.entities.boat.ImmortalersChestBoat;
import com.renyigesai.immortalers_delight.fluid.ImmortalersDelightFluids;
import com.renyigesai.immortalers_delight.item.*;
//import com.renyigesai.immortalers_delight.util.datautil.worlddata.BaseImmortalWorldData;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeSpawnEggItem;
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
    @ItemData(zhCn = "姬海棠原木",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> HIMEKAIDO_LOG;
    @ItemData(zhCn = "姬海棠木",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> HIMEKAIDO_WOOD;
    @ItemData(zhCn = "去皮姬海棠木",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> STRIPPED_HIMEKAIDO_WOOD;
    @ItemData(zhCn = "去皮姬海棠原木",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> STRIPPED_HIMEKAIDO_LOG;
    @ItemData(zhCn = "结果的姬海棠树叶叶",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> HIMEKAIDO_FRUITED_LEAVES;
    @ItemData(zhCn = "盛开的姬海棠叶",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> HIMEKAIDO_FLOWERING_LEAVES;
    @ItemData(zhCn = "姬海棠树叶",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> HIMEKAIDO_LEAVES;
    @ItemData(zhCn = "姬海棠木板",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> HIMEKAIDO_PLANKS;
    @ItemData(zhCn = "姬海棠木楼梯",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> HIMEKAIDO_STAIRS;
    @ItemData(zhCn = "姬海棠木台阶",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> HIMEKAIDO_SLAB;
    @ItemData(zhCn = "姬海棠木门")
    public static final RegistryObject<Item> HIMEKAIDO_DOOR;
    @ItemData(zhCn = "姬海棠木活板门",model = ItemData.ModelType.TRAPDOOR)
    public static final RegistryObject<Item> HIMEKAIDO_TRAPDOOR;
    @ItemData(zhCn = "姬海棠木栅栏",model = ItemData.ModelType.FENCE)
    public static final RegistryObject<Item> HIMEKAIDO_FENCE;
    @ItemData(zhCn = "姬海棠木栅栏门",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> HIMEKAIDO_FENCE_GATE;
    @ItemData(zhCn = "姬海棠木压力板",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> HIMEKAIDO_PRESSURE_PLATE;
    @ItemData(zhCn = "姬海棠木按钮",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> HIMEKAIDO_BUTTON;
    @ItemData(zhCn = "姬海棠木橱柜",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> HIMEKAIDO_CABINET;
    @ItemData(zhCn = "姬海棠木船")
    public static final RegistryObject<Item> HIMEKAIDO_BOAT;
    @ItemData(zhCn = "姬海棠木运输船")
    public static final RegistryObject<Item> HIMEKAIDO_CHEST_BOAT;
    @ItemData(zhCn = "姬海棠木告示牌")
    public static final RegistryObject<Item> HIMEKAIDO_SIGN;
    @ItemData(zhCn = "姬海棠木悬挂告示牌")
    public static final RegistryObject<Item> HIMEKAIDO_HANGING_SIGN;


    @ItemData(zhCn = "千年竹")
    public static final RegistryObject<Item> MILLENIAN_BAMBOO;
    @ItemData(zhCn = "瓦罐烂肉炖")
    public static final RegistryObject<Item> STEWED_ROTTEN_MEAT_POT;
    @ItemData(zhCn = "四眼丸子")
    public static final RegistryObject<Item> BRAISED_SPIDER_EYES_BLOCK;
    @ItemData(zhCn = "白垩玉黍粒袋",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> EVOLUTCORN_GRAIN_BAG;
    @ItemData(zhCn = "箱装姬海棠",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> HIMEKAIDO_CRATE;
    @ItemData(zhCn = "箱装棱蕉",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> PEARLIP_CRATE;
    @ItemData(zhCn = "白垩玉黍捆",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> EVOLUTCORN_BLOCK;
    @ItemData(zhCn = "瓦斯麦捆",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> KWAT_WHEAT_BLOCK;
    @ItemData(zhCn = "古苜蓿捆",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> ALFALFA_BLOCK;

    @ItemData(zhCn = "溪竹块",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> LEISAMBOO_BLOCK;
    @ItemData(zhCn = "溪竹板",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> LEISAMBOO_PLANKS;
    @ItemData(zhCn = "溪竹楼梯",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> LEISAMBOO_STAIRS;
    @ItemData(zhCn = "溪竹台阶",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> LEISAMBOO_SLAB;
    @ItemData(zhCn = "溪竹橱柜",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> LEISAMBOO_CABINET;
    @ItemData(zhCn = "溪竹门")
    public static final RegistryObject<Item> LEISAMBOO_DOOR;
    @ItemData(zhCn = "溪竹活板门",model = ItemData.ModelType.TRAPDOOR)
    public static final RegistryObject<Item> LEISAMBOO_TRAPDOOR;
    @ItemData(zhCn = "溪竹栅栏",model = ItemData.ModelType.FENCE)
    public static final RegistryObject<Item> LEISAMBOO_FENCE;
    @ItemData(zhCn = "溪竹栅栏门",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> LEISAMBOO_FENCE_GATE;
    @ItemData(zhCn = "溪竹压力板",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> LEISAMBOO_PRESSURE_PLATE;
    @ItemData(zhCn = "溪竹按钮",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> LEISAMBOO_BUTTON;
    @ItemData(zhCn = "溪竹告示牌")
    public static final RegistryObject<Item> LEISAMBOO_SIGN;
    @ItemData(zhCn = "溪竹悬挂告示牌")
    public static final RegistryObject<Item> LEISAMBOO_HANGING_SIGN;


    @ItemData(zhCn = "棱蕉木板",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> PEARLIP_SHELL_PLANKS;
    @ItemData(zhCn = "棱蕉木楼梯",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> PEARLIP_SHELL_STAIRS;
    @ItemData(zhCn = "棱蕉木台阶",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> PEARLIP_SHELL_SLAB;
    @ItemData(zhCn = "棱蕉木橱柜",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> PEARLIP_SHELL_CABINET;
    @ItemData(zhCn = "棱蕉木门")
    public static final RegistryObject<Item> PEARLIP_SHELL_DOOR;
    @ItemData(zhCn = "棱蕉木活板门",model = ItemData.ModelType.TRAPDOOR)
    public static final RegistryObject<Item> PEARLIP_SHELL_TRAPDOOR;
    @ItemData(zhCn = "棱蕉木栅栏",model = ItemData.ModelType.FENCE)
    public static final RegistryObject<Item> PEARLIP_SHELL_FENCE;
    @ItemData(zhCn = "棱蕉木栅栏门",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> PEARLIP_SHELL_FENCE_GATE;
    @ItemData(zhCn = "棱蕉木压力板",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> PEARLIP_SHELL_PRESSURE_PLATE;
    @ItemData(zhCn = "棱蕉木按钮",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> PEARLIP_SHELL_BUTTON;
    @ItemData(zhCn = "棱蕉木告示牌")
    public static final RegistryObject<Item> PEARLIP_SHELL_SIGN;
    @ItemData(zhCn = "棱蕉木悬挂告示牌")
    public static final RegistryObject<Item> PEARLIP_SHELL_HANGING_SIGN;
    @ItemData(zhCn = "棱蕉木船")
    public static final RegistryObject<Item> PEARLIP_SHELL_BOAT;
    @ItemData(zhCn = "棱蕉木运输船")
    public static final RegistryObject<Item> PEARLIP_SHELL_CHEST_BOAT;

    @ItemData(zhCn = "碗装千年竹")
    public static final RegistryObject<Item> BOWL_OF_MILLENIAN_BAMBOO;
    @ItemData(zhCn = "瓶子草荚壳碎")
    public static final RegistryObject<Item> PITCHER_POD_PETAL;

    /*嗅探兽毛*/
    @ItemData(zhCn = "嗅探兽毛块",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> SNIFFER_FUR_BLOCK;
    @ItemData(zhCn = "嗅探兽毛榻榻米方块",model = ItemData.ModelType.CUSTOM)
    public static final RegistryObject<Item> SNIFFER_FUR_TATAMI;
    @ItemData(zhCn = "嗅探兽长毛榻榻米")
    public static final RegistryObject<Item> SNIFFER_FUR_FULL_TATAMI_MAT;
    @ItemData(zhCn = "嗅探兽毛短榻榻米")
    public static final RegistryObject<Item> SNIFFER_FUR_HALF_TATAMI_MAT;


    /*
    玉米系列物品
    */
    @ItemData(zhCn = "白垩玉黎")
    public static final RegistryObject<Item> EVOLUTCORN;
    @ItemData(zhCn = "烤白垩玉黎")
    public static final RegistryObject<Item> ROAST_EVOLUTCORN;
    @ItemData(zhCn = "白垩玉黎粒")
    public static final RegistryObject<Item> EVOLUTCORN_GRAINS;
    @ItemData(zhCn = "烤白垩玉黎粒")
    public static final RegistryObject<Item> ROAST_EVOLUTCORN_CHOPS;
    @ItemData(zhCn = "爆黎花")
    public static final RegistryObject<Item> POPOLUTCORN;
    @ItemData(zhCn = "玉黎饭团")
    public static final RegistryObject<Item> CRETACEOUS_ZEA_BALL;
    @ItemData(zhCn = "彩色烤串")
    public static final RegistryObject<Item> COLORFUL_GRILLED_SKEWERS;
    @ItemData(zhCn = "玉黎烙")
    public static final RegistryObject<Item> ZEA_PANCAKE;
    @ItemData(zhCn = "玉黎烙切片")
    public static final RegistryObject<Item> ZEA_PANCAKE_SLICE;
    @ItemData(zhCn = "玉黎酥皮")
    public static final RegistryObject<Item> EVOLUTCORN_PIE_CRUST;
    @ItemData(zhCn = "玉黎啤酒")
    public static final RegistryObject<Item> EVOLUTCORN_BEER;
    @ItemData(zhCn = "黏糊啤酒")
    public static final RegistryObject<Item> STICKY_BEER;
    @ItemData(zhCn = "蛋挞酥皮")
    public static final RegistryObject<Item> CUSTARD_TART_PASTRY;
    @ItemData(zhCn = "玉黎硬糖")
    public static final RegistryObject<Item> EVOLUTCORN_HARD_CANDY;
    @ItemData(zhCn = "桶装白垩玉黎面糊")
    public static final RegistryObject<Item> EVOLUTCORN_PASTE_BUCKET;
    @ItemData(zhCn = "白垩玉黎面糊")
    public static final RegistryObject<Item> EVOLUTCORN_PASTE;
    @ItemData(zhCn = "玉黎面包")
    public static final RegistryObject<Item> EVOLUTCORN_BREAD;
    @ItemData(zhCn = "玉黎饺子")
    public static final RegistryObject<Item> EVOLUTCORN_JIAOZI;

    /*
    香蕉系列物品
    */
    @ItemData(zhCn = "棱蕉")
    public static final RegistryObject<Item> PEARLIP;
    @ItemData(zhCn = "棱蕉壳")
    public static final RegistryObject<Item> PEARLIP_SHELL;
    @ItemData(zhCn = "棱珠")
    public static final RegistryObject<Item> PEARLIPEARL;
    @ItemData(zhCn = "热带沙拉")
    public static final RegistryObject<Item> PEATIC_MUSA_SALAD;
    @ItemData(zhCn = "棱蕉奶昔")
    public static final RegistryObject<Item> PEARLIP_MILK_SHAKE;
    @ItemData(zhCn = "棱蕉南瓜派")
    public static final RegistryObject<Item> PEARLIP_PUMPKIN_PIE;
    @ItemData(zhCn = "棱蕉蛋挞")
    public static final RegistryObject<Item> PEARLIPEARL_TART;
    @ItemData(zhCn = "蕉香蒸蛋")
    public static final RegistryObject<Item> PEARLIPEARL_EGGSTEAM;
    @ItemData(zhCn = "棱蕉果冻")
    public static final RegistryObject<Item> PEARLIP_JELLY;
    @ItemData(zhCn = "珍珠棱蕉派")
    public static final RegistryObject<Item> PEARLIP_PIE;
    @ItemData(zhCn = "珍珠棱蕉派切片")
    public static final RegistryObject<Item> PEARLIP_PIE_SLICE;
    @ItemData(zhCn = "冻棱蕉")
    public static final RegistryObject<Item> ICE_PEARLIP;
    @ItemData(zhCn = "巧克力棱蕉棒")
    public static final RegistryObject<Item> CHOCOLATE_PEARLIP_STICKS;
    @ItemData(zhCn = "完美夏冰")
    public static final RegistryObject<Item> PERFECT_SUMMER_ICE;
    @ItemData(zhCn = "暮色冰淇淋")
    public static final RegistryObject<Item> TWILIGHT_GELATO;
    @ItemData(zhCn = "棱珠牛奶")
    public static final RegistryObject<Item> PEARLIP_BUBBLE_MILK;
    @ItemData(zhCn = "巧乐风")
    public static final RegistryObject<Item> CHOCOREEZE;
    @ItemData(zhCn = "热带水果旋风")
    public static final RegistryObject<Item> TROPICAL_FRUITY_CYCLONE;

    /*
    姬海棠系列物品
     */
    @ItemData(zhCn = "马铃薯泥")
    public static final RegistryObject<Item> MASHED_POTATOES;
    @ItemData(zhCn = "毒马铃薯泥")
    public static final RegistryObject<Item> MASHED_POISONOUS_POTATO;
    @ItemData(zhCn = "果酱马铃薯泥")
    public static final RegistryObject<Item> MASHED_POTATO_WITH_JAM;
    @ItemData(zhCn = "果酱毒马铃薯泥")
    public static final RegistryObject<Item> MASHED_POISONOUS_POTATO_WITH_JAM;
    @ItemData(zhCn = "腐肉碎")
    public static final RegistryObject<Item> ROTTEN_FLESH_CUTS;
    @ItemData(zhCn = "姬海棠果酱")
    public static final RegistryObject<Item> HIMEKAIDO_JELLY;
    @ItemData(zhCn = "酸奶")
    public static final RegistryObject<Item> YOGURT;
    @ItemData(zhCn = "姬海棠果")
    public static final RegistryObject<Item> HIMEKAIDO;
    @ItemData(zhCn = "烤毒土豆")
    public static final RegistryObject<Item> BAKED_POISONOUS_POTATO;
    @ItemData(zhCn = "肉汁炖蜘蛛眼")
    public static final RegistryObject<Item> BRAISED_SPIDER_EYES_IN_GRAVY;
    @ItemData(zhCn = "酱蘸腐肉")
    public static final RegistryObject<Item> DIPPED_ROTTEN_FLESH;
    @ItemData(zhCn = "腐肉酸奶脆")
    public static final RegistryObject<Item> CRISPY_YOGURT_ROTTEN_FLESH;
    @ItemData(zhCn = "烂茄肉汤")
    public static final RegistryObject<Item> MEATY_ROTTEN_TOMATO_BROTH;
    @ItemData(zhCn = " 填馅毒马铃薯")
    public static final RegistryObject<Item> STUFFED_POISONOUS_POTATO;
    @ItemData(zhCn = "河豚卷")
    public static final RegistryObject<Item> PUFFERFISH_ROLL;
    @ItemData(zhCn = "金姬海棠果")
    public static final RegistryObject<Item> GOLDEN_HIMEKAIDO;
    @ItemData(zhCn = "金魔法果",model = ItemData.ModelType.CUSTOM)
    public static final RegistryObject<Item> ENCHANTED_GOLDEN_HIMEKAIDO;
    @ItemData(zhCn = "姬海棠种子")
    public static final RegistryObject<Item> HIMEKAIDO_SEED;
    @ItemData(zhCn = "碗装瓦罐烂肉炖")
    public static final RegistryObject<Item> BOWL_OF_STEWED_ROTTEN_MEAT_IN_CLAY_POT;
    @ItemData(zhCn = "鞑靼鸡肉")
    public static final RegistryObject<Item> TARTARE_CHICKEN;
    @ItemData(zhCn = "姬海棠酸奶派")
    public static final RegistryObject<Item> HIMEKAIDO_YOGURT_PIE;
    @ItemData(zhCn = "姬海棠酸奶派片")
    public static final RegistryObject<Item> HIMEKAIDO_YOGURT_PIE_SLICE;
    @ItemData(zhCn = "醉梦姬")
    public static final RegistryObject<Item> DREUMK_WINE;
    @ItemData(zhCn = "绯红冰淇淋")
    public static final RegistryObject<Item> SCARLET_GELATO;
    @ItemData(zhCn = "绯红圣代")
    public static final RegistryObject<Item> SCARLET_SUNDAE;
    @ItemData(zhCn = "姬海棠茶泡饭")
    public static final RegistryObject<Item> HIMEKAIDO_CHAZUKE;

    /*
    古木相关
    */
    @ItemData(zhCn = "古木原木",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> ANCIENT_WOOD_LOG;
    @ItemData(zhCn = "去皮古木原木",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> STRIPPED_ANCIENT_WOOD_LOG;
    @ItemData(zhCn = "古木",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> ANCIENT_WOOD;
    @ItemData(zhCn = "去皮古木",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> STRIPPED_ANCIENT_WOOD;
    @ItemData(zhCn = "古木木板",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> ANCIENT_WOOD_PLANKS;
    @ItemData(zhCn = "古木橱柜",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> ANCIENT_WOOD_CABINET;
    @ItemData(zhCn = "古木楼梯",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> ANCIENT_WOOD_STAIRS;
    @ItemData(zhCn = "古木台阶",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> ANCIENT_WOOD_SLAB;
    @ItemData(zhCn = "古木门")
    public static final RegistryObject<Item> ANCIENT_WOOD_DOOR;
    @ItemData(zhCn = "古木活板门",model = ItemData.ModelType.TRAPDOOR)
    public static final RegistryObject<Item> ANCIENT_WOOD_TRAPDOOR;
    @ItemData(zhCn = "古木栅栏",model = ItemData.ModelType.FENCE)
    public static final RegistryObject<Item> ANCIENT_WOOD_FENCE;
    @ItemData(zhCn = "古木栅栏门",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> ANCIENT_WOOD_FENCE_GATE;
    @ItemData(zhCn = "古木压力板",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> ANCIENT_WOOD_PRESSURE_PLATE;
    @ItemData(zhCn = "古木按钮",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> ANCIENT_WOOD_BUTTON;
    @ItemData(zhCn = "古木船")
    public static final RegistryObject<Item> ANCIENT_WOOD_BOAT;
    @ItemData(zhCn = "古木运输船")
    public static final RegistryObject<Item> ANCIENT_WOOD_CHEST_BOAT;
    @ItemData(zhCn = "远古纤维")
    public static final RegistryObject<Item> ANCIENT_FIBER;


    /*
     溪竹相关物品
     */
    @ItemData(zhCn = "树叶茶")
    public static final RegistryObject<Item> LEAF_TEA;
    @ItemData(zhCn = "含茶溪竹")
    public static final RegistryObject<Item> CONTAINS_TEA_LEISAMBOO;
    @ItemData(zhCn = "空竹杯")
    public static final RegistryObject<Item> EMPTY_BAMBOO_CUP;
    @ItemData(zhCn = "溪竹茶")
    public static final RegistryObject<Item> LEISAMBOO_TEA;
    @ItemData(zhCn = "冰红茶")
    public static final RegistryObject<Item> ICED_BLACK_TEA;
    @ItemData(zhCn = "棱蕉珍珠奶茶")
    public static final RegistryObject<Item> PEARLIPEARL_MILK_TEA;
    @ItemData(zhCn = "棱蕉珍珠绿茶")
    public static final RegistryObject<Item> PEARLIPEARL_MILK_GREEN;
    @ItemData(zhCn = "炉红茶")
    public static final RegistryObject<Item> STOVE_BLACK_TEA;
    @ItemData(zhCn = "叶绿茶")
    public static final RegistryObject<Item> LEAF_GREEN_TEA;
    @ItemData(zhCn = "英黄茶")
    public static final RegistryObject<Item> BRITISH_YELLOW_TEA;
    @ItemData(zhCn = "樱桃棱蕉珍珠茶")
    public static final RegistryObject<Item> CHERRY_PEARLIPEARL_TEA;
    @ItemData(zhCn = "伶人泪")
    public static final RegistryObject<Item> GLEEMAN_TEAR;
    @ItemData(zhCn = "水果茶")
    public static final RegistryObject<Item> FRUIT_TEA;
    @ItemData(zhCn = "溪竹茶饼")
    public static final RegistryObject<Item> LEISAMBOO_TEA_CAKE;


    /*
    通天竹相关
    */
    @ItemData(zhCn = "通天竹原木",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> TRAVASTRUGGLER_LOG;
    @ItemData(zhCn = "去皮通天竹原木",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> STRIPPED_TRAVASTRUGGLER_LOG;
    @ItemData(zhCn = "通天竹叶",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> TRAVASTRUGGLER_LEAVES;
    @ItemData(zhCn = "结果通天竹叶",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> TRAVASTRUGGLER_LEAVES_TRAVARICE;
    @ItemData(zhCn = "通天竹板",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> TRAVA_PLANKS;
    @ItemData(zhCn = "通天竹米")
    public static final RegistryObject<Item> TRAVAPLANK;
    @ItemData(zhCn = "通天竹稻")
    public static final RegistryObject<Item> TRAVARICE;
    @ItemData(zhCn = "熟通天竹稻")
    public static final RegistryObject<Item> COOKED_TRAVARICE;
    @ItemData(zhCn = "通天竹酒")
    public static final RegistryObject<Item> TRAVEER;


    /*
    瓦斯麦相关物品
    */
    @ItemData(zhCn = "瓦斯麦种子")
    public static final RegistryObject<Item> KWAT_WHEAT_SEEDS;
    @ItemData(zhCn = "瓦斯麦")
    public static final RegistryObject<Item> KWAT_WHEAT;
    @ItemData(zhCn = "瓦斯麦面团")
    public static final RegistryObject<Item> KWAT_WHEAT_DOUGH;
    @ItemData(zhCn = "瓦斯麦浆")
    public static final RegistryObject<Item> KWAT_WHEAT_PASTE;
    @ItemData(zhCn = "瓦斯麦豆腐")
    public static final RegistryObject<Item> KWAT_WHEAT_DOUFU;
    @ItemData(zhCn = "炸瓦斯麦豆腐")
    public static final RegistryObject<Item> FRY_KWAT_WHEAT_DOUFU;
    @ItemData(zhCn = "酿瓦斯麦豆腐")
    public static final RegistryObject<Item> SUTFFED_KWAT_WHEAT_DOUFU;
    @ItemData(zhCn = "翡翠红玉汤")
    public static final RegistryObject<Item> JADE_AND_RUBY_SOUP;
    @ItemData(zhCn = "瓦斯麦吐司")
    public static final RegistryObject<Item> KWAT_WHEAT_TOAST;
    @ItemData(zhCn = "瓦斯麦吐司片")
    public static final RegistryObject<Item> KWAT_WHEAT_TOAST_SLICE;
    @ItemData(zhCn = "金瓦斯麦吐司")
    public static final RegistryObject<Item> GOLDEN_KWAT_TOAST;
    @ItemData(zhCn = "金瓦斯麦吐司片")
    public static final RegistryObject<Item> GOLDEN_KWAT_TOAST_SLICE;
    @ItemData(zhCn = "下界奶油浓汤")
    public static final RegistryObject<Item> NETHER_BREAD_CREAM_SOUP;
    @ItemData(zhCn = "下界奶油浓汤")
    public static final RegistryObject<Item> NETHER_CREAM_SOUP;
    @ItemData(zhCn = "下界奶油面包")
    public static final RegistryObject<Item> NETHER_CREAM_BREAD;
    @ItemData(zhCn = "超级瓦斯麦汉堡")
    public static final RegistryObject<Item> SUPER_KWAT_WHEAT_HAMBURGER;

    /*
    火把花相关物品
    */
    @ItemData(zhCn = "火把花芥末")
    public static final RegistryObject<Item> TORCHFLOWER_MUSTARD;
    @ItemData(zhCn = "热嗨汤")
    public static final RegistryObject<Item> HOT_HI_SOUP;
    @ItemData(zhCn = "火热寿司")
    public static final RegistryObject<Item> INCANDESCENCE_SUSHI;
    @ItemData(zhCn = "火把花饼")
    public static final RegistryObject<Item> TORCHFLOWER_CAKE;
    @ItemData(zhCn = "火把花咖喱饭")
    public static final RegistryObject<Item> TORCHFLOWER_CURRY_RICE;
    @ItemData(zhCn = "特辣意面")
    public static final RegistryObject<Item> EXTRA_SPICY_PASTA;
    @ItemData(zhCn = "火把花曲奇")
    public static final RegistryObject<Item> TORCHFLOWER_COOKIE;


    @ItemData(zhCn = "金丝织缕")
    public static final RegistryObject<Item> GOLDEN_FABRIC;
    @ItemData(zhCn = "绯炵金纱")
    public static final RegistryObject<Item> GOLDEN_FABRIC_VEIL;
    @ItemData(zhCn = "生嗅探兽肉片")
    public static final RegistryObject<Item> RAW_SNIFFER_SLICE;
    @ItemData(zhCn = "熟嗅探兽肉片")
    public static final RegistryObject<Item> COOKED_SNIFFER_SLICE;
    @ItemData(zhCn = "生嗅探兽肉排")
    public static final RegistryObject<Item> RAW_SNIFFER_STEAK;
    @ItemData(zhCn = "熟嗅探兽肉排")
    public static final RegistryObject<Item> COOKED_SNIFFER_STEAK;
    @ItemData(zhCn = "嗅探兽毛")
    public static final RegistryObject<Item> SNIFFER_FUR;
    @ItemData(zhCn = "刷子",model = ItemData.ModelType.CUSTOM)
    public static final RegistryObject<Item> BRUSH;
    @ItemData(zhCn = "嗅探兽毛刷")
    public static final RegistryObject<Item> SNIFFER_FUR_BRUSH;
    @ItemData(zhCn = "清水伏特加")
    public static final RegistryObject<Item> CLEAR_WATER_VODKA;
    @ItemData(zhCn = "Sniffer Rotating Roast Meat",model = ItemData.ModelType.CUSTOM)
    public static final RegistryObject<Item> SNIFFER_ROTATING_ROAST_MEAT;
    @ItemData(zhCn = "火神鸡尾酒")
    public static final RegistryObject<Item> VULCAN_COKTAIL;
    @ItemData(zhCn = "地狱格瓦斯")
    public static final RegistryObject<Item> NETHER_KVASS;
    @ItemData(zhCn = "炼狱麦酒")
    public static final RegistryObject<Item> PURGATORY_ALE;
    @ItemData(zhCn = "猪灵踊")
    public static final RegistryObject<Item> PIGLIN_ODORI_SAKE;
    @ItemData(zhCn = "姬海糖")
    public static final RegistryObject<Item> HIMEKANDY;
    @ItemData(zhCn = "红魔鬼蛋糕")
    public static final RegistryObject<Item> SCARLET_DEVILS_CAKE;
    @ItemData(zhCn = "红魔鬼蛋糕切片")
    public static final RegistryObject<Item> SCARLET_DEVILS_CAKE_SLICE;
    @ItemData(zhCn = "大红包子")
    public static final RegistryObject<Item> RED_STUFFED_BUN;
    @ItemData(zhCn = "尖刺栅栏",model = ItemData.ModelType.CUSTOM)
    public static final RegistryObject<Item> SPIKE_BAR_BASE;
    @ItemData(zhCn = "泥瓦",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> MUD_TILES;
    @ItemData(zhCn = "泥瓦楼梯",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> MUD_TILES_STAIRS;
    @ItemData(zhCn = "台阶",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> MUD_TILES_SLAB;
    @ItemData(zhCn = "泥瓦墙",model = ItemData.ModelType.WALL)
    public static final RegistryObject<Item> MUD_TILES_WALL;
    @ItemData(zhCn = "裂泥瓦",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> CRACKED_MUD_TILES;
    @ItemData(zhCn = "裂泥瓦楼梯",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> CRACKED_MUD_TILES_STAIRS;
    @ItemData(zhCn = "裂泥瓦台阶",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> CRACKED_MUD_TILES_SLAB;
    @ItemData(zhCn = "裂泥瓦墙",model = ItemData.ModelType.WALL)
    public static final RegistryObject<Item> CRACKED_MUD_TILES_WALL;
    @ItemData(zhCn = "苔石砖",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> MOSSY_MUD_BRICK;
    @ItemData(zhCn = "苔石砖楼梯",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> MOSSY_MUD_BRICK_STAIRS;
    @ItemData(zhCn = "苔石砖台阶",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> MOSSY_MUD_BRICK_SLAB;
    @ItemData(zhCn = "苔石砖墙",model = ItemData.ModelType.WALL)
    public static final RegistryObject<Item> MOSSY_MUD_BRICK_WALL;
    @ItemData(zhCn = "裂泥砖",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item>CRACKED_MUD_BRICK;
    @ItemData(zhCn = "裂泥砖楼梯",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item>CRACKED_MUD_BRICK_STAIRS;
    @ItemData(zhCn = "裂泥砖台阶",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item>CRACKED_MUD_BRICK_SLAB;
    @ItemData(zhCn = "裂泥砖墙",model = ItemData.ModelType.WALL)
    public static final RegistryObject<Item>CRACKED_MUD_BRICK_WALL;
    @ItemData(zhCn = "雕纹泥砖",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item>CHISELED_MUD_BRICK;
    @ItemData(zhCn = "泥坯楼梯",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item>PACKED_MUD_STAIRS;
    @ItemData(zhCn = "泥坯台阶",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item>PACKED_MUD_SLAB;
    @ItemData(zhCn = "泥坯墙",model = ItemData.ModelType.WALL)
    public static final RegistryObject<Item>PACKED_MUD_WALL;

    @ItemData(zhCn = "可疑的砂砾",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> INFESTED_GRAVEL;
    @ItemData(zhCn = "可疑的沙子",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> INFESTED_SAND;

    /*ALFALFA*/
    @ItemData(zhCn = "古苜蓿种子")
    public static final RegistryObject<Item> ALFALFA_SEEDS;
    @ItemData(zhCn = "古苜蓿")
    public static final RegistryObject<Item> ALFALFA;
    @ItemData(zhCn = "古苜蓿七草粥")
    public static final RegistryObject<Item> ALFALFA_PORRIDGE;//alfalfa_porridge

    @ItemData(zhCn = "蕉盒鲑")
    public static final RegistryObject<Item> BANANA_BOX_SALMON;
    @ItemData(zhCn = "蕉盒鳕")
    public static final RegistryObject<Item> BANANA_BOX_COD;
    @ItemData(zhCn = "棱蕉寿司船")
    public static final RegistryObject<Item> PEARLIP_RICE_ROLL_BOAT;

    /*冒险相关*/
    @ItemData(zhCn = "刷怪蛋",model = ItemData.ModelType.CUSTOM)
    public static final RegistryObject<Item> SKELVERFISH_AMBUSHER_SPAWN_EGG;
    @ItemData(zhCn = "刷怪蛋",model = ItemData.ModelType.CUSTOM)
    public static final RegistryObject<Item> SKELVERFISH_BOMBER_SPAWN_EGG;
    @ItemData(zhCn = "刷怪蛋",model = ItemData.ModelType.CUSTOM)
    public static final RegistryObject<Item> SKELVERFISH_THRASHER_SPAWN_EGG;
    @ItemData(zhCn = "刷怪蛋",model = ItemData.ModelType.CUSTOM)
    public static final RegistryObject<Item> STRANGE_ARMOUR_STAND_SPAWN_EGG;
    @ItemData(zhCn = "刷怪蛋",model = ItemData.ModelType.CUSTOM)
    public static final RegistryObject<Item> SCAVENGER_SPAWN_EGG;
    @ItemData(zhCn = "魔凝机",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> ENCHANTAL_COOLER;
    @ItemData(zhCn = "远古炉灶",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> ANCIENT_STOVE;
    @ItemData(zhCn = "风化远古炉灶",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> EXPOSED_ANCIENT_STOVE;
    @ItemData(zhCn = "锈蚀远古炉灶",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> WEATHERED_ANCIENT_STOVE;
    @ItemData(zhCn = "氧化远古炉灶",model = ItemData.ModelType.BLOCK)
    public static final RegistryObject<Item> OXIDIZED_ANCIENT_STOVE;
    @ItemData(zhCn = "谷物香囊")
    public static final RegistryObject<Item> SACHETS;
    @ItemData(zhCn = "龙骨钎【WIP】",enUs = "Drill Rod Wand【WIP】",model = ItemData.ModelType.TOOL)
    public static final RegistryObject<Item> DRILL_ROD_WAND;
    @ItemData(zhCn = "锈蚀古刀",model = ItemData.ModelType.TOOL)
    public static final RegistryObject<Item> RUSTY_ANCIENT_BLADE;
    @ItemData(zhCn = "新硎古刀",model = ItemData.ModelType.TOOL)
    public static final RegistryObject<Item> ANCIENT_BLADE;
    @ItemData(zhCn = "灾厄匕首",model = ItemData.ModelType.TOOL)
    public static final RegistryObject<Item> PILLAGER_KNIFE;
    @ItemData(zhCn = "骨质匕首",model = ItemData.ModelType.TOOL)
    public static final RegistryObject<Item> BONE_KNIFE;
    @ItemData(zhCn = "手持式砂轮",model = ItemData.ModelType.TOOL)
    public static final RegistryObject<Item> GRINDSTONE_HAMMER;
    @ItemData(zhCn = "古代连弩",model = ItemData.ModelType.CUSTOM)
    public static final RegistryObject<Item> REPEATING_CROSSBOW;
    @ItemData(zhCn = "汤勺",model = ItemData.ModelType.TOOL)
    public static final RegistryObject<Item> SPOON;
    @ItemData(zhCn = "古代口粮包")
    public static final RegistryObject<Item> SEALED_ANCIENT_RATIONS;

    /*诡怨藤*/
    @ItemData(zhCn = "诡怨藤种子")
    public static final RegistryObject<Item> WARPED_LAUREL_SEEDS;
    @ItemData(zhCn = "诡怨藤")
    public static final RegistryObject<Item> WARPED_LAUREL;
    @ItemData(zhCn = "卤鸡腿")
    public static final RegistryObject<Item> LU_CHICKEN_LEGS;
    @ItemData(zhCn = "下界汤")
    public static final RegistryObject<Item> NETHER_SOUP;
    @ItemData(zhCn = "红烧肉")
    public static final RegistryObject<Item> BRAISED_PORK;
    @ItemData(zhCn = "亚波伦蛋糕卷")
    public static final RegistryObject<Item> APOLLYON_CAKE_ROLL;


    /*瓶子草*/
    @ItemData(zhCn = "瓶子草烧烤")
    public static final RegistryObject<Item> PITCHER_PLANT_BARBECUE;
    @ItemData(zhCn = "荚壳汉堡肉")
    public static final RegistryObject<Item> POD_SHELL_BURGER_MEAT;
    @ItemData(zhCn = "荚壳汉堡肉块")
    public static final RegistryObject<Item> POD_SHELL_BURGER_MEAT_CUBE;
    @ItemData(zhCn = "辛荚佳朵")
    public static final RegistryObject<Item> AROMATIC_POD_AFFOGATO;
    @ItemData(zhCn = "瓶子草烧卖")
    public static final RegistryObject<Item> PITCHER_PLANT_SHAO_MAI;
    @ItemData(zhCn = "瓶子草饺子")
    public static final RegistryObject<Item> PITCHER_PLANT_JIAO_ZI;
    @ItemData(zhCn = "芝香瓜果汁")
    public static final RegistryObject<Item> CHEESE_MELON_JUICE;
    @ItemData(zhCn = "瓶装芝香瓜果汁")
    public static final RegistryObject<Item> BOTTLE_MELON_JUICE;
    @ItemData(zhCn = "瓶子草煲仔饭")
    public static final RegistryObject<Item> PITCHER_PLANT_CLAYPOT_RICE;
    @ItemData(zhCn = "碗装瓶子草煲仔饭")
    public static final RegistryObject<Item> BOWL_PITCHER_PLANT_CLAYPOT_RICE;


    @ItemData(zhCn = "温泉桶")
    public static final RegistryObject<Item> HOT_SPRING_BUCKET;
    @ItemData(zhCn = "石锅")
    public static final RegistryObject<Item> STONE_POT;
    @ItemData(zhCn = "天地有用")
    public static final RegistryObject<Item> UNIVERSAL_CHICKEN_SOUP;
    @ItemData(zhCn = "碗装天地有用")
    public static final RegistryObject<Item> BOWL_OF_UNIVERSAL_CHICKEN_SOUP;


    static {

        ENCHANTAL_COOLER = block(ImmortalersDelightBlocks.ENCHANTAL_COOLER);

        ANCIENT_STOVE = block(ImmortalersDelightBlocks.ANCIENT_STOVE);

        EXPOSED_ANCIENT_STOVE = block(ImmortalersDelightBlocks.EXPOSED_ANCIENT_STOVE);

        WEATHERED_ANCIENT_STOVE = block(ImmortalersDelightBlocks.WEATHERED_ANCIENT_STOVE);

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

        LEISAMBOO_CABINET = block(ImmortalersDelightBlocks.LEISAMBOO_CABINET);

        LEISAMBOO_STAIRS = registerWithTab("leisamboo_stairs", () ->
                new BlockItem(ImmortalersDelightBlocks.LEISAMBOO_STAIRS.get(), basicItem()));;

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

        /*嗅探兽毛*/
        SNIFFER_FUR_BLOCK = block(ImmortalersDelightBlocks.SNIFFER_FUR_BLOCK);
        SNIFFER_FUR_TATAMI = block(ImmortalersDelightBlocks.SNIFFER_FUR_TATAMI);
        SNIFFER_FUR_FULL_TATAMI_MAT = block(ImmortalersDelightBlocks.SNIFFER_FUR_FULL_TATAMI_MAT);
        SNIFFER_FUR_HALF_TATAMI_MAT = block(ImmortalersDelightBlocks.SNIFFER_FUR_HALF_TATAMI_MAT);

        /*通天竹*/
        TRAVASTRUGGLER_LOG = block(ImmortalersDelightBlocks.TRAVASTRUGGLER_LOG);
        STRIPPED_TRAVASTRUGGLER_LOG = block(ImmortalersDelightBlocks.STRIPPED_TRAVASTRUGGLER_LOG);
        TRAVASTRUGGLER_LEAVES = block(ImmortalersDelightBlocks.TRAVASTRUGGLER_LEAVES);
        TRAVASTRUGGLER_LEAVES_TRAVARICE = block(ImmortalersDelightBlocks.TRAVASTRUGGLER_LEAVES_TRAVARICE);
        TRAVA_PLANKS = block(ImmortalersDelightBlocks.TRAVA_PLANKS);

        /*泥砖*/
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
//        POISONOUS_LONG_SPIKE_TRAP = block(ImmortalersDelightBlocks.POISONOUS_LONG_SPIKE_TRAP);
//        LONG_SPIKE_TRAP = block(ImmortalersDelightBlocks.LONG_SPIKE_TRAP);
//        POISONOUS_SPIKE_TRAP = block(ImmortalersDelightBlocks.POISONOUS_SPIKE_TRAP);
//        SPIKE_TRAP = block(ImmortalersDelightBlocks.SPIKE_TRAP);
//        POISONOUS_METAL_CALTROP = block(ImmortalersDelightBlocks.POISONOUS_METAL_CALTROP);
//        METAL_CALTROP = block(ImmortalersDelightBlocks.METAL_CALTROP);
        SPIKE_BAR_BASE = block(ImmortalersDelightBlocks.SPIKE_BAR_BASE);
        //SPIKE_BAR = block(ImmortalersDelightBlocks.SPIKE_BAR);
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

        WARPED_LAUREL_SEEDS = registerWithTab("warped_laurel_seeds",()->
                new ItemNameBlockItem(ImmortalersDelightBlocks.WARPED_LAUREL_CROP.get(), new Item.Properties()));

        WARPED_LAUREL = registerWithTab("warped_laurel",()-> new Item(new Item.Properties()));

        TRAVAPLANK = registerWithTab("travaplank",()->new Item(basicItem()));

        TRAVARICE = registerWithTab("travarice",()->new ItemNameBlockItem(ImmortalersDelightBlocks.TRAVASTRUGGLER_SAPLING.get(), basicItem()));

        /*
        玉米系列物品
        */

        CRETACEOUS_ZEA_BALL = registerWithTab("cretaceous_zea_ball", () ->
                new PowerfulAbleFoodItem(foodItem(ImmortalersDelightFoodProperties.CRETACEOUS_ZEA_BALL), ImmortalersDelightFoodProperties.CRETACEOUS_ZEA_BALL_POWERED, true, false));

        COLORFUL_GRILLED_SKEWERS = registerWithTab("colorful_grilled_skewers", () ->
                new PowerfulAbleFoodItem(new Item.Properties().food(ImmortalersDelightFoodProperties.COLORFUL_GRILLED_SKEWERS), ImmortalersDelightFoodProperties.COLORFUL_GRILLED_SKEWERS_POWERED, true, false));

        POPOLUTCORN = registerWithTab("popolutcorn", () ->
                new PowerfulAbleFoodItem(new Item.Properties().food(ImmortalersDelightFoodProperties.POPOLUTCORN), ImmortalersDelightFoodProperties.POPOLUTCORN_POWERED, true, false));

        ZEA_PANCAKE = blockFood(ImmortalersDelightBlocks.ZEA_PANCAKE);

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

        EVOLUTCORN_PASTE_BUCKET = registerWithTab("evolutcorn_paste_bucket",()-> new Item(new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
        EVOLUTCORN_PASTE = registerWithTab("evolutcorn_paste",()-> new Item(new Item.Properties().craftRemainder(Items.BOWL).stacksTo(16)));
        EVOLUTCORN_BREAD = foodItem("evolutcorn_bread",ImmortalersDelightFoodProperties.EVOLUTCORN_BREAD);
        EVOLUTCORN_JIAOZI = foodItem("evolutcorn_jiaozi",ImmortalersDelightFoodProperties.EVOLUTCORN_JIAOZI,true);

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
                new DrinkItem(ImmortalersDelightBlocks.PEARLIP_BUBBLE_MILK.get(),drinkItem(ImmortalersDelightFoodProperties.PEARLIP_BUBBLE_MILK),true));

        CHOCOREEZE = registerWithTab("chocoreeze",()->
                new ConsumableItem(foodItem(ImmortalersDelightFoodProperties.CHOCOREEZE),true, false));

        TROPICAL_FRUITY_CYCLONE = registerWithTab("tropical_fruity_cyclone",()->
                new PowerfulAbleFoodItem(drinkItem(ImmortalersDelightFoodProperties.TROPICAL_FRUITY_CYCLONE),ImmortalersDelightFoodProperties.TROPICAL_FRUITY_CYCLONE_POWERED,true, false));

        BANANA_BOX_COD = registerWithTab("banana_box_cod",()->
                new PowerfulAbleFoodItem(new Item.Properties().food(ImmortalersDelightFoodProperties.BANANA_BOX_COD),ImmortalersDelightFoodProperties.BANANA_BOX_COD_POWERED,true,false));

        BANANA_BOX_SALMON = registerWithTab("banana_box_salmon",()->
                new PowerfulAbleFoodItem(new Item.Properties().food(ImmortalersDelightFoodProperties.BANANA_BOX_SALMON),ImmortalersDelightFoodProperties.BANANA_BOX_SALMON_POWERED,true,false));

        PEARLIP_RICE_ROLL_BOAT = blockFood(ImmortalersDelightBlocks.PEARLIP_RICE_ROLL_BOAT);


        /*
         溪竹相关物品
        */
        LEAF_TEA = registerWithTab("leaf_tea",()->
                new DrinkItem(ImmortalersDelightBlocks.LEAF_TEA.get(), drinkItem(ImmortalersDelightFoodProperties.LEAF_TEA),true));

        EMPTY_BAMBOO_CUP = registerWithTab("empty_bamboo_cup", () ->
                new Item(new Item.Properties().stacksTo(16)));

        LEISAMBOO_TEA = registerWithTab("leisamboo_tea", () ->
                new DrinkItem(ImmortalersDelightBlocks.LEISAMBOO_TEA.get(), new Item.Properties().craftRemainder(EMPTY_BAMBOO_CUP.get()).stacksTo(16).food(ImmortalersDelightFoodProperties.LEISAMBOO_TEA)));

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
                new DrinkableItem(drinkItem(ImmortalersDelightFoodProperties.CHERRY_PEARLIPEARL_TEA),true, false));

        GLEEMAN_TEAR = registerWithTab("gleeman_tear",()->
                new DrinkableItem(drinkItem(ImmortalersDelightFoodProperties.GLEEMAN_TEAR),true, false));

        FRUIT_TEA = registerWithTab("fruit_tea",()->
                new DrinkItem(ImmortalersDelightBlocks.FRUIT_TEA.get(),drinkItem(ImmortalersDelightFoodProperties.FRUIT_TEA),true));

        MILLENIAN_BAMBOO = blockFood(ImmortalersDelightBlocks.MILLENIAN_BAMBOO);

        BOWL_OF_MILLENIAN_BAMBOO = registerWithTab("bowl_of_millenian_bamboo", () ->
                new PowerfulAbleFoodItem(bowlFoodItem(ImmortalersDelightFoodProperties.BOWL_OF_MILLENIAN_BAMBOO),ImmortalersDelightFoodProperties.BOWL_OF_MILLENIAN_BAMBOO_POWERED,true, false));

        LEISAMBOO_TEA_CAKE = registerWithTab("leisamboo_tea_cake",()->
                new ConsumableItem(foodItem(ImmortalersDelightFoodProperties.LEISAMBOO_TEA_CAKE),true, false));

        /*
        通天竹
        */
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

        BRAISED_SPIDER_EYES_BLOCK = blockFood(ImmortalersDelightBlocks.BRAISED_SPIDER_EYES_BLOCK);

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

        STEWED_ROTTEN_MEAT_POT = blockFood(ImmortalersDelightBlocks.STEWED_ROTTEN_MEAT_POT);

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

        GOLDEN_KWAT_TOAST = registerWithTab("golden_kwat_toast",() ->
                new GoldenToastItem(fantasticFoodItem(ImmortalersDelightFoodProperties.GOLDEN_TOAST,Rarity.RARE,false),ImmortalersDelightFoodProperties.GOLDEN_TOAST_POWERED,true,true,1));

        GOLDEN_KWAT_TOAST_SLICE = registerWithTab("golden_kwat_toast_slice",() ->
                new GoldenToastItem(fantasticFoodItem(ImmortalersDelightFoodProperties.GOLDEN_TOAST_SLICE,Rarity.RARE,false),ImmortalersDelightFoodProperties.GOLDEN_TOAST_SLICE_POWERED,true,true,2));

        NETHER_BREAD_CREAM_SOUP = blockFood(ImmortalersDelightBlocks.NETHER_BREAD_CREAM_SOUP);

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

        SUPER_KWAT_WHEAT_HAMBURGER = registerWithTab("super_kwat_wheat_hamburger",()-> new ConsumableItem(new Item.Properties().food(ImmortalersDelightFoodProperties.SUPER_KWAT_WHEAT_HAMBURGER),true));


        /*
        冒险相关物品
         */

        GOLDEN_FABRIC = registerWithTab("golden_fabric", () ->
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
                new SnifferBrushItem(fantasticItem(Rarity.UNCOMMON).durability(384)));

        SACHETS = registerWithTab("sachets", () ->
                new SachetsItem(fantasticItem(Rarity.UNCOMMON).durability(64),false,true));

        //隐藏
        DRILL_ROD_WAND = register("drill_rod_wand", () ->
                new DrillRodItem(1,-2.4F, ImmortalersTiers.MAGIC_POWERED, BlockTags.MINEABLE_WITH_PICKAXE,ImmortalersDelightTags.MINEABLE_WITH_DRILL_ROD,fantasticItem(Rarity.RARE),4));

        RUSTY_ANCIENT_BLADE = registerWithTab("rusty_ancient_blade", () ->
                new ImmortalersKnifeItem(ImmortalersKnifeItem.ANCIENT_KNIFE_TYPE,ImmortalersTiers.RUSTY_IRON,2,-2.0f,2,0,new Item.Properties()));

        ANCIENT_BLADE = registerWithTab("ancient_blade", () ->
                new ImmortalersKnifeItem(ImmortalersKnifeItem.NEW_ANCIENT_KNIFE_TYPE,ImmortalersTiers.ANCIENT_KNIFE,3.5f,-2.0f,4,0,fantasticItem(Rarity.UNCOMMON)));

        PILLAGER_KNIFE = registerWithTab("pillager_knife", () ->
                new PillagersKnifeItem(ImmortalersKnifeItem.PILLAGER_KNIFE_TYPE,ImmortalersTiers.PILLAGER_KNIFE,3,-2.0f,2,0,fantasticItem(Rarity.UNCOMMON)));

        BONE_KNIFE = registerWithTab("bone_knife", () ->
                new BoneKnifeItem(ImmortalersKnifeItem.BONE_KNIFE_TYPE,ImmortalersTiers.BONE_KNIFE,0.5f,-2.0f,0.5f,0,new Item.Properties()));

        GRINDSTONE_HAMMER = registerWithTab("grindstone_hammer", () ->
                new GrindstoneHammerItem(ImmortalersTiers.GRINDSTONE_HAMMER,3.5f,-3.55f,new Item.Properties()));

        REPEATING_CROSSBOW = registerWithTab("repeating_crossbow",()->
                new RepeatingCrossbowItem((new Item.Properties()).stacksTo(1).durability(465)));
        SPOON = registerWithTab("spoon",()->
                new SpoonItem(ImmortalersDelightBlocks.UNFINISHED_TANGYUAN.get(), (new Item.Properties()).durability(64)));
        //隐藏
        SEALED_ANCIENT_RATIONS = register("sealed_ancient_rations",()->
                new PowerfulAbleFoodItem(new Item.Properties().food(ImmortalersDelightFoodProperties.SEALED_ANCIENT_RATIONS),ImmortalersDelightFoodProperties.SEALED_ANCIENT_RATIONS_POWERED,true,false));

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

        POD_SHELL_BURGER_MEAT = blockFood(ImmortalersDelightBlocks.POD_SHELL_BURGER_MEAT);

        POD_SHELL_BURGER_MEAT_CUBE = registerWithTab("pod_shell_burger_meat_cube",()->
                new PowerfulAbleFoodItem(new Item.Properties().food(ImmortalersDelightFoodProperties.POD_SHELL_BURGER_MEAT_CUBE), ImmortalersDelightFoodProperties.POD_SHELL_BURGER_MEAT_CUBE_POWERED,true,false));

        AROMATIC_POD_AFFOGATO = registerWithTab("aromatic_pod_affogato",()->
                new DrinkItem(ImmortalersDelightBlocks.AROMATIC_POD_AFFOGATO.get(),drinkItem(ImmortalersDelightFoodProperties.AROMATIC_POD_AFFOGATO),true));

        ALFALFA_PORRIDGE = registerWithTab("alfalfa_porridge",()->
                new PowerfulAbleFoodItem(bowlFoodItem(ImmortalersDelightFoodProperties.ALFALFA_PORRIDGE),ImmortalersDelightFoodProperties.ALFALFA_PORRIDGE_POWERED,true,false));

        PITCHER_PLANT_JIAO_ZI = foodItem("pitcher_plant_jiao_zi",ImmortalersDelightFoodProperties.PITCHER_PLANT_JIAO_ZI,true);
        PITCHER_PLANT_SHAO_MAI = foodItem("pitcher_plant_shao_mai",ImmortalersDelightFoodProperties.PITCHER_PLANT_SHAO_MAI,true);
        CHEESE_MELON_JUICE = blockFood(ImmortalersDelightBlocks.CHEESE_MELON_JUICE);
        BOTTLE_MELON_JUICE = registerWithTab("bottle_melon_juice",()-> new DrinkableItem(drinkItem(ImmortalersDelightFoodProperties.BOTTLE_MELON_JUICE),true));
        PITCHER_PLANT_CLAYPOT_RICE = blockFood(ImmortalersDelightBlocks.PITCHER_PLANT_CLAYPOT_RICE);
        BOWL_PITCHER_PLANT_CLAYPOT_RICE = registerWithTab("bowl_pitcher_plant_claypot_rice",()->
                new ConsumableItem(bowlFoodItem(ImmortalersDelightFoodProperties.BOWL_PITCHER_PLANT_CLAYPOT_RICE),true));

        /*诡怨桂相关物品*/
        LU_CHICKEN_LEGS = foodItem("lu_chicken_legs",ImmortalersDelightFoodProperties.LU_CHICKEN_LEGS,true);
        NETHER_SOUP = registerWithTab("nether_soup",()-> new ConsumableItem(bowlFoodItem(ImmortalersDelightFoodProperties.NETHER_SOUP),true));
        BRAISED_PORK = registerWithTab("braised_pork",()-> new ConsumableItem(bowlFoodItem(ImmortalersDelightFoodProperties.BRAISED_PORK),true));
        //隐藏
        APOLLYON_CAKE_ROLL = foodItem("apollyon_cake_roll",ImmortalersDelightFoodProperties.APOLLYON_CAKE_ROLL,true);


        //酒品
        DREUMK_WINE = registerWithTab("dreumk_wine",()->
                new InebriatedToxicDrinkBlockItem(ImmortalersDelightBlocks.DREUMK_WINE.get(), drinkItem(ImmortalersDelightFoodProperties.DREUMK_WINE),true));

        EVOLUTCORN_BEER = registerWithTab("evolutcorn_beer",()->
                new InebriatedToxicDrinkBlockItem(ImmortalersDelightBlocks.EVOLUTCORN_BEER.get(),drinkItem(ImmortalersDelightFoodProperties.EVOLUTCORN_BEER), true));

        STICKY_BEER = registerWithTab("sticky_beer",()->
                new InebriatedToxicDrinkBlockItem(ImmortalersDelightBlocks.STICKY_BEER.get(),drinkItem(ImmortalersDelightFoodProperties.STICKY_BEER),true));

        VULCAN_COKTAIL = registerWithTab("vulcan_coktail",()->
                new InebriatedToxicDrinkBlockItem(ImmortalersDelightBlocks.VULCAN_COKTAIL.get(), drinkItem(ImmortalersDelightFoodProperties.VULCAN_COKTAIL),true));

        CLEAR_WATER_VODKA = registerWithTab("clear_water_vodka", () ->
                new InebriatedToxicFoodItem(drinkItem(ImmortalersDelightFoodProperties.CLEAR_WATER_VODKA),true,false,false,true));

        NETHER_KVASS = registerWithTab("nether_kvass", () ->
                new InebriatedToxicFoodItem(drinkItem(ImmortalersDelightFoodProperties.NETHER_KVASS),true,false));

        PURGATORY_ALE = registerWithTab("purgatory_ale",()->
                new InebriatedToxicDrinkBlockItem(ImmortalersDelightBlocks.PURGATORY_ALE.get(),drinkItem(ImmortalersDelightFoodProperties.PURGATORY_ALE),true));

        PIGLIN_ODORI_SAKE = registerWithTab("piglin_odori_sake",()->
                new InebriatedToxicDrinkBlockItem(ImmortalersDelightBlocks.PIGLIN_ODORI_SAKE.get(), (new Item.Properties()).craftRemainder(Items.WITHER_SKELETON_SKULL).stacksTo(16).food(ImmortalersDelightFoodProperties.PIGLIN_ODORI_SAKE),true));

        TRAVEER = register("traveer",()-> new InebriatedToxicFoodItem(drinkItem(ImmortalersDelightFoodProperties.TRAVEER),true,false));

        /*石锅*/
        STONE_POT = block(ImmortalersDelightBlocks.STONE_POT);
        UNIVERSAL_CHICKEN_SOUP = blockFood(ImmortalersDelightBlocks.UNIVERSAL_CHICKEN_SOUP);
        BOWL_OF_UNIVERSAL_CHICKEN_SOUP = registerWithTab("bowl_of_universal_chicken_soup",()-> new ConsumableItem(bowlFoodItem(ImmortalersDelightFoodProperties.BOWL_OF_UNIVERSAL_CHICKEN_SOUP),true));

        //隐藏
        SKELVERFISH_AMBUSHER_SPAWN_EGG = register("skelverfish_ambusher_spawn_egg",()->
                new ForgeSpawnEggItem(ImmortalersDelightEntities.SKELVERFISH_AMBUSHER,1645516,6845733,new Item.Properties()));
        SKELVERFISH_BOMBER_SPAWN_EGG = register("skelverfish_bomber_spawn_egg",()->
                new ForgeSpawnEggItem(ImmortalersDelightEntities.SKELVERFISH_BOMBER,1645516,6845733,new Item.Properties()));
        SKELVERFISH_THRASHER_SPAWN_EGG = register("skelverfish_thrasher_spawn_egg",()->
                new ForgeSpawnEggItem(ImmortalersDelightEntities.SKELVERFISH_THRASHER,1645516,6845733,new Item.Properties()));
        STRANGE_ARMOUR_STAND_SPAWN_EGG = register("strange_armour_stand_spawn_egg",()->
                new ForgeSpawnEggItem(ImmortalersDelightEntities.STRANGE_ARMOUR_STAND,1645516,6845733,new Item.Properties()));
        SCAVENGER_SPAWN_EGG = register("scavenger_spawn_egg",()->
                new ForgeSpawnEggItem(ImmortalersDelightEntities.SCAVENGER,1645516,6845733,new Item.Properties()));

        HOT_SPRING_BUCKET = register("hot_spring_bucket",()->new BucketItem(ImmortalersDelightFluids.HOT_SPRING,new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));



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

    private static RegistryObject<Item> blockFood(RegistryObject<Block> block) {
        return registerWithTab(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties().stacksTo(1)));
    }

    private static RegistryObject<Item> block(RegistryObject<Block> block,boolean hide) {
        return register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
    }
}

