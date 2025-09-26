package com.renyigesai.immortalers_delight.compat.init;

import com.doggystudio.chirencqr.ltc.server.item.ItemLatiaoBase;
import com.doggystudio.chirencqr.ltc.server.misc.EnumLatiaoGrade;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.compat.item.DeveloperLatiaoItem;
import com.renyigesai.immortalers_delight.compat.item.KwatWheatLatiaoItem;
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
    public static final RegistryObject<Item> RARE_EVOLUTCORN_LATIAO;
    public static final RegistryObject<Item> SUPERIOR_EVOLUTCORN_LATIA;
    public static final RegistryObject<Item> DELICACY_EVOLUTCORN_LATIAO;
    public static final RegistryObject<Item> TREASURE_EVOLUTCORN_LATIAO;

    public static final RegistryObject<Item> KWAT_WHEAT_LATIAO;
    public static final RegistryObject<Item> RARE_KWAT_WHEAT_LATIAO;
    public static final RegistryObject<Item> SUPERIOR_KWAT_WHEAT_LATIAO;
    public static final RegistryObject<Item> DELICACY_KWAT_WHEAT_LATIAO;
    public static final RegistryObject<Item> TREASURE_KWAT_WHEAT_LATIAO;

    public static final RegistryObject<Item> MASHED_POISONOUS_POTATO_WITH_JAM_LATIAO;
    public static final RegistryObject<Item> RARE_MASHED_POISONOUS_POTATO_WITH_JAM_LATIAO;
    public static final RegistryObject<Item> SUPERIOR_MASHED_POISONOUS_POTATO_WITH_JAM_LATIAO;
    public static final RegistryObject<Item> DELICACY_MASHED_POISONOUS_POTATO_WITH_JAM_LATIAO;
    public static final RegistryObject<Item> TREASURE_MASHED_POISONOUS_POTATO_WITH_JAM_LATIAO;
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
        RARE_EVOLUTCORN_LATIAO = registerWithTab("rare_evolutcorn_latiao",()-> new ItemLatiaoBase(9,0.5f, EnumLatiaoGrade.RARE));
        SUPERIOR_EVOLUTCORN_LATIA = registerWithTab("superior_evolutcorn_latiao",()-> new ItemLatiaoBase(13,0.5f, EnumLatiaoGrade.SUPERIOR));
        DELICACY_EVOLUTCORN_LATIAO = registerWithTab("delicacy_evolutcorn_latiao",()-> new ItemLatiaoBase(17,0.5f, EnumLatiaoGrade.DELICACY));
        TREASURE_EVOLUTCORN_LATIAO = registerWithTab("treasure_evolutcorn_latiao",()-> new ItemLatiaoBase(21,0.5f, EnumLatiaoGrade.TREASURE));

        KWAT_WHEAT_LATIAO = registerWithTab("kwat_wheat_latiao",()-> new KwatWheatLatiaoItem(5,0.5f,600,0,EnumLatiaoGrade.ORDINARY).addLTEffect(ImmortalersDelightMobEffect.SATIATED.get()).addLTEffect(ImmortalersDelightMobEffect.INCANDESCENCE.get()));
        RARE_KWAT_WHEAT_LATIAO = registerWithTab("rare_kwat_wheat_latiao",()-> new KwatWheatLatiaoItem(9,0.5f,700,0,EnumLatiaoGrade.RARE).addLTEffect(ImmortalersDelightMobEffect.SATIATED.get()).addLTEffect(ImmortalersDelightMobEffect.INCANDESCENCE.get()));
        SUPERIOR_KWAT_WHEAT_LATIAO = registerWithTab("superior_kwat_wheat_latiao",()-> new KwatWheatLatiaoItem(13,0.5f,800,0,EnumLatiaoGrade.SUPERIOR).addLTEffect(ImmortalersDelightMobEffect.SATIATED.get()).addLTEffect(ImmortalersDelightMobEffect.INCANDESCENCE.get()));
        DELICACY_KWAT_WHEAT_LATIAO = registerWithTab("delicacy_kwat_wheat_latiao",()-> new KwatWheatLatiaoItem(17,0.5f,900,0,EnumLatiaoGrade.DELICACY).addLTEffect(ImmortalersDelightMobEffect.SATIATED.get()).addLTEffect(ImmortalersDelightMobEffect.INCANDESCENCE.get()));
        TREASURE_KWAT_WHEAT_LATIAO = registerWithTab("treasure_kwat_wheat_latiao",()-> new KwatWheatLatiaoItem(21,0.5f,1000,0,EnumLatiaoGrade.TREASURE).addLTEffect(ImmortalersDelightMobEffect.SATIATED.get()).addLTEffect(ImmortalersDelightMobEffect.INCANDESCENCE.get()));

        MASHED_POISONOUS_POTATO_WITH_JAM_LATIAO = registerWithTab("mashed_poisonous_potato_with_jam_latiao",()-> new ItemLatiaoBase(5,0.5f,600,0,EnumLatiaoGrade.ORDINARY).addLTEffect(ImmortalersDelightMobEffect.SATIATED.get()).addLTEffect(ImmortalersDelightMobEffect.RESISTANCE_TO_ILLAGER.get()).addLTEffect(ImmortalersDelightMobEffect.RELIEVE_POISON.get()));
        RARE_MASHED_POISONOUS_POTATO_WITH_JAM_LATIAO = registerWithTab("rare_mashed_poisonous_potato_with_jam_latiao",()-> new ItemLatiaoBase(9,0.5f,700,0,EnumLatiaoGrade.RARE).addLTEffect(ImmortalersDelightMobEffect.SATIATED.get()).addLTEffect(ImmortalersDelightMobEffect.RESISTANCE_TO_ILLAGER.get()).addLTEffect(ImmortalersDelightMobEffect.RELIEVE_POISON.get()));
        SUPERIOR_MASHED_POISONOUS_POTATO_WITH_JAM_LATIAO = registerWithTab("superior_mashed_poisonous_potato_with_jam_latiao",()-> new ItemLatiaoBase(13,0.5f,800,0,EnumLatiaoGrade.SUPERIOR).addLTEffect(ImmortalersDelightMobEffect.SATIATED.get()).addLTEffect(ImmortalersDelightMobEffect.RESISTANCE_TO_ILLAGER.get()).addLTEffect(ImmortalersDelightMobEffect.RELIEVE_POISON.get()));
        DELICACY_MASHED_POISONOUS_POTATO_WITH_JAM_LATIAO = registerWithTab("delicacy_mashed_poisonous_potato_with_jam_latiao",()-> new ItemLatiaoBase(17,0.5f,900,0,EnumLatiaoGrade.DELICACY).addLTEffect(ImmortalersDelightMobEffect.SATIATED.get()).addLTEffect(ImmortalersDelightMobEffect.RESISTANCE_TO_ILLAGER.get()).addLTEffect(ImmortalersDelightMobEffect.RELIEVE_POISON.get()));
        TREASURE_MASHED_POISONOUS_POTATO_WITH_JAM_LATIAO = registerWithTab("treasure_mashed_poisonous_potato_with_jam_latiao",()-> new ItemLatiaoBase(21,0.5f,1000,0,EnumLatiaoGrade.TREASURE).addLTEffect(ImmortalersDelightMobEffect.SATIATED.get()).addLTEffect(ImmortalersDelightMobEffect.RESISTANCE_TO_ILLAGER.get()).addLTEffect(ImmortalersDelightMobEffect.RELIEVE_POISON.get()));

        /*开发者辣条*/
        IMMORTALERS_LATIAO_LUCKY_BAG = registerWithTab("immortalers_latiao_lucky_bag", LatiaoLuckyBagItem::new);
        HAKO_LATIAO = registerWithTab("hako_latiao", () -> new DeveloperLatiaoItem(ImmortalersDelightFoodProperties.DEVELOPER_LATIAO, EnumLatiaoGrade.TREASURE));
        WINDY_NARRATOR_LATIA = registerWithTab("windy_narrator_latiao", () -> new DeveloperLatiaoItem(ImmortalersDelightFoodProperties.DEVELOPER_LATIAO, EnumLatiaoGrade.TREASURE));
        MOASWIES_LATIAO = registerWithTab("moaswies_latiao", () -> new DeveloperLatiaoItem(ImmortalersDelightFoodProperties.DEVELOPER_LATIAO, EnumLatiaoGrade.TREASURE));
        RENYIGESAI_LATIAO = registerWithTab("renyigesai_latiao", () -> new DeveloperLatiaoItem(ImmortalersDelightFoodProperties.DEVELOPER_LATIAO, EnumLatiaoGrade.TREASURE));
        LYZ_DELIGHT_LATIAO = registerWithTab("lyz_delight_latiao", () -> new DeveloperLatiaoItem(ImmortalersDelightFoodProperties.DEVELOPER_LATIAO, EnumLatiaoGrade.TREASURE));
        XIAOSUHUAJI_LATIAO = registerWithTab("xiaosuhuaji_latiao", () -> new DeveloperLatiaoItem(ImmortalersDelightFoodProperties.DEVELOPER_LATIAO, EnumLatiaoGrade.TREASURE));
        TELLURIUM_LATIAO = registerWithTab("tellurium_latiao", () -> new DeveloperLatiaoItem(ImmortalersDelightFoodProperties.DEVELOPER_LATIAO, EnumLatiaoGrade.TREASURE));
        KA_QKO_LATIAO = registerWithTab("ka_qko_latiao", () -> new DeveloperLatiaoItem(ImmortalersDelightFoodProperties.DEVELOPER_LATIAO, EnumLatiaoGrade.TREASURE));
        BEI_DOU_LATIAO = registerWithTab("bei_dou_latiao", () -> new DeveloperLatiaoItem(ImmortalersDelightFoodProperties.DEVELOPER_LATIAO, EnumLatiaoGrade.TREASURE));
        DOUGER_LATIAO = registerWithTab("douger_latiao", () -> new DeveloperLatiaoItem(ImmortalersDelightFoodProperties.DEVELOPER_LATIAO, EnumLatiaoGrade.TREASURE));
    }

    public static RegistryObject<Item> registerWithTab(String name, Supplier<Item> supplier) {
        RegistryObject<Item> item = ITEMS.register(name, supplier);
        ImmortalersDelightItems.CREATIVE_TAB_ITEMS.add(item);
        return item;
    }

}
