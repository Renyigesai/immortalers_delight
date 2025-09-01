package com.renyigesai.immortalers_delight.compat.init;

import com.doggystudio.chirencqr.ltc.server.item.ItemLatiaoBase;
import com.doggystudio.chirencqr.ltc.server.misc.EnumLatiaoGrade;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.compat.item.KwatWheatLatiaoItem;
import com.renyigesai.immortalers_delight.compat.item.LatiaoItem;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightFoodProperties;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.item.LatiaoLuckyBagItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class Ltc2Items {
    public static final DeferredRegister<Item> ITEMS;
    /*原料*/
    public static final RegistryObject<Item> EVOLUTCORN_POWDER;
    /*辣条*/
    public static final RegistryObject<Item> EVOLUTCORN_LATIAO;
    public static final RegistryObject<Item> KWAT_WHEAT_LATIAO;
    public static final RegistryObject<Item> MASHED_POISONOUS_POTATO_WITH_JAM_LATIAO;
    /*开发者辣条*/
    public static final RegistryObject<Item> IMMORTALERS_LATIAO_LUCKY_BAG;
    public static final RegistryObject<Item> HAKO_LATIAO;
    public static final RegistryObject<Item> WINDY_NARRATOR_LATIA;
    public static final RegistryObject<Item> MOASWIES_LATIAO;
    public static final RegistryObject<Item> RENYIGESAI_LATIAO;
    public static final RegistryObject<Item> LYZ_DELIGHT_LATIAO;
    public static final RegistryObject<Item> XIAOSUHUAJI_LATIAO;
    public static final RegistryObject<Item> TELLURIUM_LATIAO;
    public static final RegistryObject<Item> KA_QKO_LATIAO;
    public static final RegistryObject<Item> BEI_DOU_LATIAO;
    public static final RegistryObject<Item> DOUGER_LATIAO;
    static {
        ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ImmortalersDelightMod.MODID);
        EVOLUTCORN_POWDER = registerWithTab("evolutcorn_powder",()-> new Item(new Item.Properties()));
        /*辣条*/
        EVOLUTCORN_LATIAO = registerWithTab("evolutcorn_latiao",()-> new ItemLatiaoBase(5,0.5f, EnumLatiaoGrade.ORDINARY));
        KWAT_WHEAT_LATIAO = registerWithTab("kwat_wheat_latiao",()-> new KwatWheatLatiaoItem(ImmortalersDelightFoodProperties.KWAT_WHEAT_LATIAO,EnumLatiaoGrade.RARE));
        MASHED_POISONOUS_POTATO_WITH_JAM_LATIAO = registerWithTab("mashed_poisonous_potato_with_jam_latiao",()-> new LatiaoItem(ImmortalersDelightFoodProperties.MASHED_POISONOUS_POTATO_WITH_JAM_LATIAO,EnumLatiaoGrade.RARE));
        /*开发者辣条*/
        IMMORTALERS_LATIAO_LUCKY_BAG = registerWithTab("immortalers_latiao_lucky_bag", LatiaoLuckyBagItem::new);
        HAKO_LATIAO = registerWithTab("hako_latiao", () -> new ItemLatiaoBase(5,0.5f, EnumLatiaoGrade.TREASURE));
        WINDY_NARRATOR_LATIA = registerWithTab("windy_narrator_latiao", () -> new ItemLatiaoBase(5,0.5f, EnumLatiaoGrade.TREASURE));
        MOASWIES_LATIAO = registerWithTab("moaswies_latiao", () -> new ItemLatiaoBase(5,0.5f, EnumLatiaoGrade.TREASURE));
        RENYIGESAI_LATIAO = registerWithTab("renyigesai_latiao", () -> new ItemLatiaoBase(5,0.5f, EnumLatiaoGrade.TREASURE));
        LYZ_DELIGHT_LATIAO = registerWithTab("lyz_delight_latiao", () -> new ItemLatiaoBase(5,0.5f, EnumLatiaoGrade.TREASURE));
        XIAOSUHUAJI_LATIAO = registerWithTab("xiaosuhuaji_latiao", () -> new ItemLatiaoBase(5,0.5f, EnumLatiaoGrade.TREASURE));
        TELLURIUM_LATIAO = registerWithTab("tellurium_latiao", () -> new ItemLatiaoBase(5,0.5f, EnumLatiaoGrade.TREASURE));
        KA_QKO_LATIAO = registerWithTab("ka_qko_latiao", () -> new ItemLatiaoBase(5,0.5f, EnumLatiaoGrade.TREASURE));
        BEI_DOU_LATIAO = registerWithTab("bei_dou_latiao", () -> new ItemLatiaoBase(5,0.5f, EnumLatiaoGrade.TREASURE));
        DOUGER_LATIAO = registerWithTab("douger_latiao", () -> new ItemLatiaoBase(5,0.5f, EnumLatiaoGrade.TREASURE));
    }

    public static RegistryObject<Item> registerWithTab(String name, Supplier<Item> supplier) {
        RegistryObject<Item> item = ITEMS.register(name, supplier);
        ImmortalersDelightItems.CREATIVE_TAB_ITEMS.add(item);
        return item;
    }

//    if (isLtc2){
//            Ltc2Items.ITEMS.register(bus);
//        }

}
