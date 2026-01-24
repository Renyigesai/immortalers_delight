package com.renyigesai.immortalers_delight.init;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import vectorwing.farmersdelight.common.registry.ModEffects;

public class ImmortalersDelightFoodProperties {

    public static final FoodProperties BOWL_OF_MILLENIAN_BAMBOO = new FoodProperties.Builder()
            .nutrition(6).saturationMod(0.65f)
            .effect(()-> new MobEffectInstance(ModEffects.COMFORT.get(),1200,0),1.0F)
            .effect(()-> new MobEffectInstance(ModEffects.NOURISHMENT.get(),1200,0),1.0F)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.CULTURAL_LEGACY.get(),3600,0),1.0F)
            .build();

    public static final FoodProperties EVOLUTCORN = new FoodProperties.Builder().nutrition(2)
            .saturationMod(0.75f).build();

    public static final FoodProperties ROAST_EVOLUTCORN = new FoodProperties.Builder().nutrition(5)
            .saturationMod(0.7f).build();

    public static final FoodProperties EVOLUTCORN_GRAINS = new FoodProperties.Builder().nutrition(2)
            .saturationMod(0.75f).build();

    public static final FoodProperties ROAST_EVOLUTCORN_CHOPS = new FoodProperties.Builder().nutrition(5)
            .saturationMod(0.7f).build();

    public static final FoodProperties CRETACEOUS_ZEA_BALL = new FoodProperties.Builder().nutrition(6)
            .saturationMod(0.45f)
            .effect(()-> new MobEffectInstance(ModEffects.NOURISHMENT.get(),800,0),1.0F)
            .build();

    public static final FoodProperties COLORFUL_GRILLED_SKEWERS = new FoodProperties.Builder().nutrition(9).saturationMod(0.85f)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),300),1.0F).build();

    public static final FoodProperties PEARLIP = new FoodProperties.Builder()
            .nutrition(2).saturationMod(0.6f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.VITALITY.get(),200),1.0F)
            .build();

    public static final FoodProperties POPOLUTCORN = new FoodProperties.Builder().fast().nutrition(10).saturationMod(0.45f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.INCANDESCENCE.get(),600,0),1.0F).build();

    public static final FoodProperties PEARLIPEARL = new FoodProperties.Builder()
            .nutrition(2).fast().saturationMod(0.4f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.VITALITY.get(),200),1.0F)
            .build();

    public static final FoodProperties PEATIC_MUSA_SALAD = new FoodProperties.Builder().nutrition(6).fast()
            .saturationMod(0.625f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.VITALITY.get(),1400,0),1.0F)
            .effect(()-> new MobEffectInstance(MobEffects.REGENERATION,160,0),1.0F)
            .build();

    public static final FoodProperties PEARLIP_MILK_SHAKE = new FoodProperties.Builder().alwaysEat()
            .nutrition(3).saturationMod(1.0f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.VITALITY.get(),600,0),1.0F)
            .effect(()-> new MobEffectInstance(MobEffects.REGENERATION,600,0),1.0F)
            .effect(()-> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,600,0),1F).build();

    public static final FoodProperties PEARLIP_PUMPKIN_PIE = new FoodProperties.Builder().fast()
            .nutrition(3).saturationMod(1.25f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.VITALITY.get(),1400,0),1.0F)
            .build();

    public static final FoodProperties PEARLIPEARL_TART = new FoodProperties.Builder()
            .nutrition(6).saturationMod(1.6f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.VITALITY.get(),2000,1),1.0F)
            .build();

    public static final FoodProperties PEARLIPEARL_EGGSTEAM = new FoodProperties.Builder()
            .nutrition(7).saturationMod(0.85f)
            .effect(()-> new MobEffectInstance(ModEffects.COMFORT.get(),300,0),1.0F)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.VITALITY.get(),1000,0),1.0F)
            .build();

    public static final FoodProperties PEARLIP_JELLY = new FoodProperties.Builder()
            .nutrition(3).saturationMod(1.25f)
            .effect(()-> new MobEffectInstance(MobEffects.JUMP,1200,0),1.0F)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.VITALITY.get(),1360,0),1.0F)
            .build();

    /*
    姬海棠系列食物
     */
    public static final FoodProperties HIMEKAIDO = new FoodProperties.Builder()
            .nutrition(3)
            .saturationMod(0.6f)
            .effect(() -> {
                return new MobEffectInstance((MobEffect)ImmortalersDelightMobEffect.RELIEVE_POISON.get(), 25, 0);
            }, 1.0F)
            .build();
    public static final FoodProperties HIMEKAIDO_JELLY = new FoodProperties.Builder().alwaysEat()
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.RELIEVE_POISON.get(),120,1),1.0F)
            .effect(()-> new MobEffectInstance(MobEffects.SATURATION,5,0),1.0F).build();
    public static final FoodProperties YOGURT = new FoodProperties.Builder().alwaysEat().nutrition(4).saturationMod(1.0F)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.RELIEVE_POISON.get(),600,0),1.0F)
            .build();
    public static final FoodProperties BAKED_POISONOUS_POTATO = new FoodProperties.Builder()
            .nutrition(6)
            .saturationMod(0.55f)
            .effect(() -> {
                return new MobEffectInstance((MobEffect)ImmortalersDelightMobEffect.WEAK_POISON.get(), 80, 0);
            }, 0.6F)
            .build();
    public static final FoodProperties DIPPED_ROTTEN_FLESH = new FoodProperties.Builder()
            .nutrition(4)
            .saturationMod(0.45f)
            .effect(() -> {
                return new MobEffectInstance((MobEffect)ImmortalersDelightMobEffect.RESISTANCE_TO_UNDEAD.get(),800,0);
            }, 0.6F)
            .build();

    public static final FoodProperties CRISPY_YOGURT_ROTTEN_FLESH = new FoodProperties.Builder()
            .nutrition(8)
            .saturationMod(0.875f)
            .effect(() -> {
                return new MobEffectInstance((MobEffect)ImmortalersDelightMobEffect.RESISTANCE_TO_UNDEAD.get(),1200,0);
            }, 1.0F)
            .build();

    public static final FoodProperties MEATY_ROTTEN_TOMATO_BROTH = new FoodProperties.Builder()
            .nutrition(8)
            .saturationMod(0.4f)
            .effect(() -> {
                return new MobEffectInstance((MobEffect)ImmortalersDelightMobEffect.RESISTANCE_TO_UNDEAD.get(),3000,0);
            }, 1.0F)
            .effect(() -> {
                return new MobEffectInstance((MobEffect)ImmortalersDelightMobEffect.RESISTANCE_TO_ILLAGER.get(),1800,0);
            }, 1.0F)
            .effect(()-> new MobEffectInstance(ModEffects.COMFORT.get(),1800,0),1.0F)
            .build();

    public static final FoodProperties BRAISED_SPIDER_EYES_IN_GRAVY = new FoodProperties.Builder()
            .nutrition(4)
            .saturationMod(0.5f)
            .effect(() -> {
                return new MobEffectInstance((MobEffect)ImmortalersDelightMobEffect.RESISTANCE_TO_ARTHROPOD.get(),1000,0);
            }, 1.0F)
            .effect(() -> {
                return new MobEffectInstance((MobEffect)ImmortalersDelightMobEffect.RESISTANCE_TO_UNDEAD.get(),800,0);
            }, 1.0F)
            .effect(new MobEffectInstance(MobEffects.REGENERATION,150,0),1.0F)
            .build();
    public static final FoodProperties TARTARE_CHICKEN = new FoodProperties.Builder()
            .nutrition(10).saturationMod(1.0f)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),300),1.0F)
            .effect(() -> {
                return new MobEffectInstance((MobEffect)ImmortalersDelightMobEffect.RESISTANCE_TO_ARTHROPOD.get(),3600,1);
            }, 1.0F)
            .effect(new MobEffectInstance(MobEffects.REGENERATION,560,0),1.0F)
            .build();

    public static final FoodProperties STUFFED_POISONOUS_POTATO = new FoodProperties.Builder()
            .nutrition(10).saturationMod(0.6f)
            .effect(() -> {
                return new MobEffectInstance((MobEffect)ImmortalersDelightMobEffect.RESISTANCE_TO_UNDEAD.get(),5400,0);
            }, 1.0F)
            .effect(() -> {
                return new MobEffectInstance((MobEffect)ImmortalersDelightMobEffect.RESISTANCE_TO_ILLAGER.get(),1200,1);
            }, 1.0F)
            .build();

    public static final FoodProperties PUFFERFISH_ROLL = new FoodProperties.Builder()
            .nutrition(7).saturationMod(0.7f)
            .effect(() -> {
                return new MobEffectInstance((MobEffect)ImmortalersDelightMobEffect.RESISTANCE_TO_ABYSSAL.get(),2000,0);
            }, 1.0F)
            .effect(new MobEffectInstance(MobEffects.HEALTH_BOOST,600,0),1.0F)
            .effect(new MobEffectInstance(MobEffects.REGENERATION,250,1),1.0F)
            .build();
    public static final FoodProperties BOWL_OF_STEWED_ROTTEN_MEAT_IN_CLAY_POT = new FoodProperties.Builder()
            .nutrition(8)
            .saturationMod(1.2f)
            .effect(() -> {
                return new MobEffectInstance((MobEffect)ModEffects.NOURISHMENT.get(),2000,0);
            }, 1.0F)
            .effect(() -> {
                return new MobEffectInstance((MobEffect)ImmortalersDelightMobEffect.RESISTANCE_TO_ABYSSAL.get(),1600,1);
            }, 1.0F)
            .effect(new MobEffectInstance(MobEffects.HEALTH_BOOST,2400,0),1.0F)
            .effect(new MobEffectInstance(MobEffects.HEAL,1,1),1.0F)
            .effect(new MobEffectInstance(MobEffects.REGENERATION,250,1),1.0F)
            .build();
    public static final FoodProperties GOLDEN_HIMEKAIDO = new FoodProperties.Builder()
            .nutrition(4)
            .saturationMod(1.2f)
            .effect(() -> {
                return new MobEffectInstance((MobEffect)ImmortalersDelightMobEffect.MAGICAL_REVERSE.get(),3,1);
            }, 1.0F)
            .effect(new MobEffectInstance(MobEffects.ABSORPTION,2400,0),1.0F)
            .alwaysEat()
            .build();
    public static final FoodProperties ENCHANTED_GOLDEN_HIMEKAIDO = new FoodProperties.Builder()
            .nutrition(8)
            .saturationMod(1.25f)
            .effect(() -> {
                return new MobEffectInstance((MobEffect)ImmortalersDelightMobEffect.MAGICAL_REVERSE.get(),20,3);
            }, 1.0F)
            .effect(() -> {
                return new MobEffectInstance((MobEffect)ImmortalersDelightMobEffect.RELIEVE_POISON.get(),6000,0);
            }, 1.0F)
            .effect(new MobEffectInstance(MobEffects.ABSORPTION,2400,1),1.0F)
            .effect(new MobEffectInstance(MobEffects.HEALTH_BOOST,2400,1),1.0F)
            .alwaysEat()
            .build();
    public static final FoodProperties KWAT_WHEAT = new FoodProperties.Builder().alwaysEat()
            .nutrition(3)
            .saturationMod(0.1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.GAS_POISON.get(),65),1f).build();
    public static final FoodProperties RAW_SNIFFER_SLICE = new FoodProperties.Builder()
            .nutrition(2)
            .saturationMod(0.5f)
            .effect(new MobEffectInstance(MobEffects.DIG_SPEED,60,0),1.0F)
            .build();
    public static final FoodProperties COOKED_SNIFFER_SLICE = new FoodProperties.Builder()
            .nutrition(3)
            .saturationMod(1.45f)
            .effect(new MobEffectInstance(MobEffects.DIG_SPEED,80,0),1.0F).build();

    public static final FoodProperties RAW_SNIFFER_STEAK = new FoodProperties.Builder()
            .nutrition(4)
            .saturationMod(0.5f)
            .effect(new MobEffectInstance(MobEffects.DIG_SPEED,120,0),1.0F).build();

    public static final FoodProperties COOKED_SNIFFER_STEAK = new FoodProperties.Builder()
            .nutrition(9)
            .saturationMod(0.8f)
            .effect(new MobEffectInstance(MobEffects.DIG_SPEED,300,0),1.0F).build();

    public static final FoodProperties CLEAR_WATER_VODKA = new FoodProperties.Builder()
            .alwaysEat()
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.INEBRIATED.get(),12000,0),1.0F)
            .effect(()-> new MobEffectInstance(MobEffects.REGENERATION,1500,0),1.0F)
            .effect(()-> new MobEffectInstance(MobEffects.ABSORPTION,900,9),1.0F)
            .effect(()-> new MobEffectInstance(MobEffects.HEAL,1,1),1.0F).build();

    public static final FoodProperties ZEA_PANCAKE_SLICE = new FoodProperties.Builder()
            .nutrition(7).saturationMod(0.55f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.CULTURAL_LEGACY.get(),600,1),1.0F)
            .effect(()-> new MobEffectInstance(MobEffects.HEAL,1),1.0F)
            .build();

    public static final FoodProperties PEARLIP_PIE_SLICE = new FoodProperties.Builder()
            .nutrition(5).saturationMod(0.4f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.VITALITY.get(),1000),1.0F)
            .effect(()-> new MobEffectInstance(ModEffects.COMFORT.get(),800),1.0F)
            .effect(()-> new MobEffectInstance(MobEffects.HEAL,1),1.0F)
            .build();

    public static final FoodProperties HIMEKAIDO_YOGURT_PIE_SLICE = new FoodProperties.Builder()
            .nutrition(6).saturationMod(0.55f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.RESISTANCE_TO_UNDEAD.get(),1200),1.0F)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.VITALITY.get(),200),1.0F)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.RELIEVE_POISON.get(),120,1),1.0F)
            .effect(()-> new MobEffectInstance(MobEffects.HEAL,1),1.0F)
            .build();

    public static final FoodProperties EVOLUTCORN_BEER = new FoodProperties.Builder()
            .alwaysEat()
            .effect(()-> new MobEffectInstance(MobEffects.DAMAGE_BOOST,2700,2),1F)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.INEBRIATED.get(),1800),1F)
            .build();

    public static final FoodProperties STICKY_BEER = new FoodProperties.Builder()
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.INEBRIATED.get(),2400),1f)
            .effect(()->new MobEffectInstance(MobEffects.MOVEMENT_SPEED,6600,2),1f)
            .alwaysEat().build();

    public static final FoodProperties DREUMK_WINE = new FoodProperties.Builder()
            .alwaysEat()
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.INEBRIATED.get(),1800),1F)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.RELIEVE_POISON.get(),6000),1.0F).alwaysEat().build();

    public static final FoodProperties VULCAN_COKTAIL = new FoodProperties.Builder()
            .alwaysEat()
            .effect(() ->new MobEffectInstance(ImmortalersDelightMobEffect.INEBRIATED.get(),600),1F)
            .effect(() -> new MobEffectInstance(ImmortalersDelightMobEffect.WARM_CURRENT_SURGES.get(),9600),1F).alwaysEat().build();

    public static final FoodProperties LEAF_TEA = new FoodProperties.Builder()
            .alwaysEat()
            .effect(() ->new MobEffectInstance(MobEffects.MOVEMENT_SPEED,1800),1F)
            .effect(() -> new MobEffectInstance(MobEffects.DIG_SPEED,360),1F).alwaysEat().build();

    public static final FoodProperties LEISAMBOO_TEA = new FoodProperties.Builder()
            .alwaysEat()
            .effect(() ->new MobEffectInstance(MobEffects.NIGHT_VISION,1800),1F)
            .effect(() -> new MobEffectInstance(ImmortalersDelightMobEffect.LINGERING_INFUSION.get(),540),1F).alwaysEat().build();

//    public static final FoodProperties ICED_BLACK_TEA = new FoodProperties.Builder()
//            .effect(() ->new MobEffectInstance(MobEffects.DAMAGE_BOOST,1440,2),1F)
//            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,1200,1),1F)
//            .effect(() -> new MobEffectInstance(MobEffects.JUMP,20,3),1F).alwaysEat().build();

//    public static final FoodProperties PEARLIPEARL_MILK_TEA = new FoodProperties.Builder()
//            .effect(() ->new MobEffectInstance(MobEffects.DAMAGE_BOOST,2000,1),1F)
//            .effect(() ->new MobEffectInstance(MobEffects.SATURATION,3),1F)
//            .effect(() -> new MobEffectInstance(ImmortalersDelightMobEffect.RESISTANCE_TO_SURROUNDINGS.get(),2000,1),1F).alwaysEat().build();

    //    public static final FoodProperties PEARLIPEARL_MILK_GREEN = new FoodProperties.Builder()
//            .effect(() ->new MobEffectInstance(MobEffects.MOVEMENT_SPEED,1600,1),1F)
//            .effect(() ->new MobEffectInstance(MobEffects.SATURATION,1,2),1F)
//            .effect(() -> new MobEffectInstance(ImmortalersDelightMobEffect.RESISTANCE_TO_SURROUNDINGS.get(),4200),1F).alwaysEat().build();
    public static final FoodProperties PEARLIPEARL_MILK_GREEN = new FoodProperties.Builder()
            .alwaysEat()
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED,1600,1),1F)
            .effect(() -> new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),55,2),1F)
            .effect(() -> new MobEffectInstance(ImmortalersDelightMobEffect.RESISTANCE_TO_SURROUNDINGS.get(),4200),1F).build();

    public static final FoodProperties PEARLIPEARL_MILK_TEA = new FoodProperties.Builder()
            .alwaysEat()
            .effect(() ->new MobEffectInstance(MobEffects.JUMP,1500,2),1F)
            .effect(() ->new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),120),1F)
            .effect(() -> new MobEffectInstance(ImmortalersDelightMobEffect.RESISTANCE_TO_SURROUNDINGS.get(),2000,1),1F).build();

    public static final FoodProperties ICED_BLACK_TEA = new FoodProperties.Builder()
            .alwaysEat()
            .effect(() ->new MobEffectInstance(MobEffects.JUMP,480,9),1F)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,1000,2),1F).build();

    public static final FoodProperties STOVE_BLACK_TEA = new FoodProperties.Builder()
            .alwaysEat()
            .effect(() ->new MobEffectInstance(MobEffects.JUMP,2000,1),1F)
            .effect(()-> new MobEffectInstance(ModEffects.COMFORT.get(),600,0),1.0F).build();


    public static final FoodProperties LEAF_GREEN_TEA = new FoodProperties.Builder()
            .alwaysEat()
            .effect(() ->new MobEffectInstance(MobEffects.MOVEMENT_SPEED,1350,1),1F)
            .effect(() ->new MobEffectInstance(MobEffects.DIG_SPEED,510,1),1F)
            .effect(() -> new MobEffectInstance(MobEffects.NIGHT_VISION,480),1F).alwaysEat().build();

    public static final FoodProperties BRITISH_YELLOW_TEA = new FoodProperties.Builder()
            .alwaysEat()
            .effect(() ->new MobEffectInstance(MobEffects.DIG_SPEED,1800),1F)
            .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE,300),1F).alwaysEat().build();

    public static final FoodProperties SCARLET_DEVILS_CAKE_SLICE = new FoodProperties.Builder()
            .nutrition(5).saturationMod(0.6F)
            .effect(() ->new MobEffectInstance(MobEffects.DAMAGE_BOOST,675,3),1F)
            .effect(()-> new MobEffectInstance(ModEffects.COMFORT.get(),600,0),1F)
            .effect(()-> new MobEffectInstance(MobEffects.HEAL,1,0),1F)
            .build();

    public static final FoodProperties LEISAMBOO_TEA_CAKE = new FoodProperties.Builder().nutrition(5)
            .saturationMod(0.4F).effect(() -> new MobEffectInstance(MobEffects.NIGHT_VISION,2700),1F)
            .effect(() -> new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),120),1F).build();

    public static final FoodProperties RED_STUFFED_BUN = new FoodProperties.Builder().nutrition(10).saturationMod(0.77F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),300),1.0F)
            .effect(() ->new MobEffectInstance(MobEffects.DAMAGE_BOOST,3000,1),1F).build();

    public static final FoodProperties KWAT_WHEAT_DOUFU = new FoodProperties.Builder().nutrition(4).saturationMod(1.0F).effect(() ->new MobEffectInstance(MobEffects.DAMAGE_BOOST,400,1),1F).build();

    public static final FoodProperties FRY_KWAT_WHEAT_DOUFU = new FoodProperties.Builder().nutrition(9).saturationMod(0.65F)
            .effect(new MobEffectInstance(MobEffects.DAMAGE_BOOST,1200),1F).build();

    public static final FoodProperties JADE_AND_RUBY_SOUP = new FoodProperties.Builder().alwaysEat().nutrition(7).saturationMod(1.0F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),200,1),1F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.INCANDESCENCE.get(),800),1F)
            .effect(()->new MobEffectInstance(ModEffects.COMFORT.get(),800),1F).build();

    public static final FoodProperties KWAT_WHEAT_TOAST = new FoodProperties.Builder().nutrition(6).saturationMod(1.25F).build();

    public static final FoodProperties KWAT_WHEAT_TOAST_SLICE = new FoodProperties.Builder().nutrition(2).saturationMod(1.25F).build();

    public static final FoodProperties NETHER_CREAM_SOUP = new FoodProperties.Builder().nutrition(7).saturationMod(0.55f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.INCANDESCENCE.get(),800,2),1F)
            .effect(()->new MobEffectInstance(ModEffects.COMFORT.get(),1200),1F)
            .build();

    public static final FoodProperties NETHER_CREAM_BREAD = new FoodProperties.Builder().nutrition(8).saturationMod(0.375f)
            .effect(()->new MobEffectInstance(MobEffects.DAMAGE_BOOST,800,2),1.0F)
            .effect(()-> new MobEffectInstance(ModEffects.NOURISHMENT.get(),1200),1F)
            .build();

    public static final FoodProperties HOT_HI_SOUP = new FoodProperties.Builder().nutrition(8).saturationMod(1.25F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),300),1.0F).effect(()->new MobEffectInstance(MobEffects.DAMAGE_BOOST,2200),1F).effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.WARM_CURRENT_SURGES.get(),600),1F).build();

    public static final FoodProperties INCANDESCENCE_SUSHI = new FoodProperties.Builder().nutrition(8).saturationMod(0.375f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.WARM_CURRENT_SURGES.get(),600),1F).build();

    public static final FoodProperties TORCHFLOWER_CAKE = new FoodProperties.Builder()
            .nutrition(5).saturationMod(0.98f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.WARM_CURRENT_SURGES.get(),1200,1),1F)
            .effect(()-> new MobEffectInstance(ModEffects.COMFORT.get(),1200),1F).build();

    public static final FoodProperties TORCHFLOWER_CURRY_RICE = new FoodProperties.Builder().nutrition(10).saturationMod(0.5F)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.WARM_CURRENT_SURGES.get(),1200),1F).build();
    public static final FoodProperties EXTRA_SPICY_PASTA = new FoodProperties.Builder().nutrition(12).saturationMod(0.35f)
            .effect(()-> new MobEffectInstance(MobEffects.DAMAGE_BOOST,1000),1F).effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),100,1),1F).effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.WARM_CURRENT_SURGES.get(),1200),1F).build();

    //TORCHFLOWER_COOKIE
    public static final FoodProperties TORCHFLOWER_COOKIE = new FoodProperties.Builder().nutrition(4).saturationMod(0.375f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.WARM_CURRENT_SURGES.get(),400),1F).fast().build();

    public static final FoodProperties MASHED_POTATOES = new FoodProperties.Builder().nutrition(1).saturationMod(0.5f).fast().build();

    public static final FoodProperties MASHED_POISONOUS_POTATO = new FoodProperties.Builder().nutrition(1).saturationMod(0.5f)
            .effect(()-> new MobEffectInstance(MobEffects.POISON,600),0.3F).fast().build();

    public static final FoodProperties MASHED_POTATO_WITH_JAM = new FoodProperties.Builder().nutrition(8).saturationMod(0.75F)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.RELIEVE_POISON.get(),1200),1F).build();

    public static final FoodProperties MASHED_POISONOUS_POTATO_WITH_JAM = new FoodProperties.Builder().nutrition(8).saturationMod(1.125f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.RESISTANCE_TO_ILLAGER.get(),2400),1F).build();

    public static final FoodProperties SUTFFED_KWAT_WHEAT_DOUFU = new FoodProperties.Builder().nutrition(12).saturationMod(0.75f)
            .effect(()->new MobEffectInstance(MobEffects.DAMAGE_BOOST,2000,2),1F)
            .build();

    public static final FoodProperties ICE_PEARLIP = new FoodProperties.Builder().fast()
            .nutrition(4).saturationMod(1.6f)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.VITALITY.get(),1800,1),1F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.COOL.get(),800,0),1F)
            .effect(()->new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,1200),1F)
            .build();

    public static final FoodProperties CHOCOLATE_PEARLIP_STICKS = new FoodProperties.Builder().fast()
            .nutrition(8).saturationMod(0.625f)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.COOL.get(),800,0),1F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.VITALITY.get(),2000,0),1F)
            .build();

    public static final FoodProperties NETHER_KVASS = new FoodProperties.Builder()
            .alwaysEat()
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),80,1),1F)
            .effect(()->new MobEffectInstance(MobEffects.DAMAGE_BOOST,3125,1),1F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.INCANDESCENCE.get(),3600,1),1F)
            .effect(()->new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,4000),1F)
            .effect(()->new MobEffectInstance(MobEffects.CONFUSION,300),1F)
            .build();

    public static final FoodProperties PITCHER_PLANT_BARBECUE = new FoodProperties.Builder().nutrition(8).saturationMod(0.375F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),300),1F).effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.BURN_THE_BOATS.get(),1800,1),1F).build();

    public static final FoodProperties POD_SHELL_BURGER_MEAT_CUBE = new FoodProperties.Builder().nutrition(11).saturationMod(0.7F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),240),1.0F).effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.BURN_THE_BOATS.get(),5200),1F).build();

    public static final FoodProperties PURGATORY_ALE = new FoodProperties.Builder()
            .alwaysEat()
            .effect(()->new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,1000,1),1F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.PREHISTORIC_POWERS.get(),1200,2),1F)
            .effect(()->new MobEffectInstance(MobEffects.DIG_SLOWDOWN,1200,2),1F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.INEBRIATED.get(),1200),1F).build();


    public static final FoodProperties PIGLIN_ODORI_SAKE = new FoodProperties.Builder()
            .alwaysEat()
            .effect(()->new MobEffectInstance(MobEffects.DAMAGE_BOOST,150,3),1F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.INCANDESCENCE.get(),1500,3),1F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.WARM_CURRENT_SURGES.get(),3000,1),1F)
            .effect(()->new MobEffectInstance(MobEffects.FIRE_RESISTANCE,6000),1F)
            .effect(()->new MobEffectInstance(MobEffects.HARM,1),1F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.INEBRIATED.get(),3600),1F).build();

    public static final FoodProperties ALFALFA_PORRIDGE  = new FoodProperties.Builder().nutrition(7).saturationMod(1.15f).effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.KEEP_A_FAST.get(),10200),1F).effect(()->new MobEffectInstance(ModEffects.COMFORT.get(),800),1F).effect(()->new MobEffectInstance(MobEffects.MOVEMENT_SPEED,3600),1F).build();

    public static final FoodProperties BANANA_BOX_SALMON  = new FoodProperties.Builder()
            .nutrition(7).saturationMod(0.6F)
            .effect(new MobEffectInstance(MobEffects.WATER_BREATHING,600),1F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.VITALITY.get(),1200),1F)
            .build();
    public static final FoodProperties BANANA_BOX_COD  = new FoodProperties.Builder()
            .nutrition(7).saturationMod(0.6F)
            .effect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE,600),1F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.VITALITY.get(),1200),1F)
            .build();
    //public static final FoodProperties LEISAMBOO_TEA_CAKE  = new FoodProperties.Builder().nutrition(5).saturationMod(0.6F).effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.RESISTANCE_TO_SURROUNDINGS.get(),600),1F).build();
    public static final FoodProperties SCARLET_GELATO  = new FoodProperties.Builder()
            .nutrition(6).saturationMod(0.6f)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION,1050,1),1f)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.RELIEVE_POISON.get(),2200),1F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.COOL.get(),800),1F)
            .effect(()->new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,1000),1F)
            .alwaysEat().build();

    public static final FoodProperties SCARLET_SUNDAE = new FoodProperties.Builder()
            .nutrition(8).saturationMod(0.65F)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION,1500,1),1f)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.RELIEVE_POISON.get(),2400),1F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.COOL.get(),2400),1F)
            .effect(()->new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,1200,1),1F)
            .alwaysEat().build();
    public static final FoodProperties HIMEKAIDO_CHAZUKE  = new FoodProperties.Builder()
            .nutrition(6).saturationMod(1.125F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.RELIEVE_POISON.get(),2400),1F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.LINGERING_INFUSION.get(),2400),1F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.COOL.get(),1000),1F)
            .build();
    public static final FoodProperties PERFECT_SUMMER_ICE  = new FoodProperties.Builder().nutrition(3).saturationMod(2.0F).effect(() -> new MobEffectInstance(ImmortalersDelightMobEffect.COOL.get(),1000,1),1f).alwaysEat().build();
    public static final FoodProperties TWILIGHT_GELATO  = new FoodProperties.Builder()
            .nutrition(6).saturationMod(0.6f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,1200,1),1f)
            .effect(() -> new MobEffectInstance(ImmortalersDelightMobEffect.COOL.get(),1800,1),1f)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,1200,1),1f)
            .alwaysEat().build();

    public static final FoodProperties HIMEKANDY = new FoodProperties.Builder()
            .nutrition(5).saturationMod(1.0F)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.RELIEVE_POISON.get(),1200,0),1.0F)
            .build();

    public static final FoodProperties EVOLUTCORN_HARD_CANDY = new FoodProperties.Builder()
            .nutrition(4).saturationMod(1.2F).alwaysEat()
            .effect(()-> new MobEffectInstance(MobEffects.DAMAGE_BOOST,40,1),1.0F)
            .build();

    public static final FoodProperties EVOLUTCORN_HARD_CANDY_AHEAD = new FoodProperties.Builder()
            .nutrition(4).saturationMod(1.2F).alwaysEat()
            .effect(()-> new MobEffectInstance(MobEffects.WEAKNESS,20,5),1.0F)
            .effect(()-> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,20,2),1.0F)
            .build();

    public static final FoodProperties AROMATIC_POD_AFFOGATO = new FoodProperties.Builder()
            .nutrition(4).saturationMod(1.25F)
            .effect(()-> new MobEffectInstance(MobEffects.FIRE_RESISTANCE,14400),1.0F)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.WARM_CURRENT_SURGES.get(),2400),1.0F)
            .alwaysEat().build();

    public static final FoodProperties CHERRY_PEARLIPEARL_TEA = new FoodProperties.Builder()
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.KEEP_A_FAST.get(),2400),1.0F)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.LINGERING_INFUSION.get(),1200),1.0F)
            .alwaysEat().build();

    public static final FoodProperties GLEEMAN_TEAR = new FoodProperties.Builder()
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.KEEP_A_FAST.get(),5400),1.0F)
            .effect(()-> new MobEffectInstance(ModEffects.COMFORT.get(),1800),1.0F)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.LINGERING_INFUSION.get(),1800,1),1.0F)
            .alwaysEat().build();

    public static final FoodProperties FRUIT_TEA = new FoodProperties.Builder()
            .effect(()-> new MobEffectInstance(MobEffects.JUMP,2400,9),1.0F)
            .effect(()-> new MobEffectInstance(MobEffects.SLOW_FALLING,2400),1.0F)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.LINGERING_INFUSION.get(),1200),1.0F)
            .alwaysEat().build();

    public static final FoodProperties PEARLIP_BUBBLE_MILK = new FoodProperties.Builder()
            .nutrition(4).saturationMod(0.5F)
            .effect(()-> new MobEffectInstance(MobEffects.REGENERATION,100,1),1.0F)
            .effect(()-> new MobEffectInstance(MobEffects.HEAL,1),1.0F)
            .effect(()-> new MobEffectInstance(MobEffects.HEALTH_BOOST,2400),1.0F)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.VITALITY.get(),300,2),1.0F)
            .alwaysEat().build();

    public static final FoodProperties CHOCOREEZE = new FoodProperties.Builder()
            .nutrition(4).saturationMod(0.625f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.VITALITY.get(),500,2),1.0F)
            .alwaysEat().build();

    public static final FoodProperties TROPICAL_FRUITY_CYCLONE = new FoodProperties.Builder()
            .nutrition(4).saturationMod(1.25F)
            .effect(()-> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,4000),1.0F)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.VITALITY.get(),2800),1.0F)
            .effect(()-> new MobEffectInstance(MobEffects.REGENERATION,600,1),1.0F)
            .alwaysEat().build();

    public static final FoodProperties GOLDEN_TOAST = new FoodProperties.Builder()
            .nutrition(12).saturationMod(0.75f)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),300),1.0F)
            .effect(()-> new MobEffectInstance(MobEffects.HEALTH_BOOST,2400,1),1.0F)
            .effect(()-> new MobEffectInstance(MobEffects.ABSORPTION,2400,1),1.0F)
            .effect(()-> new MobEffectInstance(MobEffects.REGENERATION,300,1),1.0F)
            .alwaysEat().build();

    public static final FoodProperties GOLDEN_TOAST_SLICE = new FoodProperties.Builder()
            .nutrition(4).saturationMod(0.75f)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),100),1.0F)
            .effect(()-> new MobEffectInstance(MobEffects.HEALTH_BOOST,2400),1.0F)
            .effect(()-> new MobEffectInstance(MobEffects.ABSORPTION,2400),1.0F)
            .effect(()-> new MobEffectInstance(MobEffects.REGENERATION,200,1),1.0F)
            .alwaysEat().build();

    public static final FoodProperties SEALED_ANCIENT_RATIONS  = new FoodProperties.Builder()
            .nutrition(13).saturationMod(0.75f)
            .effect(() -> new MobEffectInstance(MobEffects.HEAL,1,2),1f)
            .effect(()->new MobEffectInstance(MobEffects.REGENERATION,100,2),1F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),1200,1),1F)
            .build();


    /*======================================================Powered=====================================================*/
    public static final FoodProperties TROPICAL_FRUITY_CYCLONE_POWERED = new FoodProperties.Builder()
            .nutrition(4).saturationMod(1.25F)
            .effect(()-> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,4000,1),1.0F)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.VITALITY.get(),8400),1.0F)
            .effect(()-> new MobEffectInstance(MobEffects.REGENERATION,1800,1),1.0F)
            .alwaysEat().build();

    public static final FoodProperties COOKED_TRAVARICE = new FoodProperties.Builder().nutrition(6).saturationMod(0.4F).effect(()-> new MobEffectInstance(ModEffects.COMFORT.get(),600),1.0F).build();

    public static final FoodProperties TRAVEER = new FoodProperties.Builder().effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.INEBRIATED.get(),2400),1.0F).alwaysEat().build();
    public static final FoodProperties CRETACEOUS_ZEA_BALL_POWERED = new FoodProperties.Builder()
            .nutrition(6).saturationMod(0.45f)
            .effect(()-> new MobEffectInstance(ModEffects.NOURISHMENT.get(),2400,0),1.0F)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),100,0),1.0F)
            .build();

    public static final FoodProperties COLORFUL_GRILLED_SKEWERS_POWERED = new FoodProperties.Builder()
            .nutrition(9).saturationMod(0.85f)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),700),1.0F)
            .build();

    public static final FoodProperties POPOLUTCORN_POWERED = new FoodProperties.Builder().fast()
            .nutrition(10).saturationMod(0.45f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),200,0),1.0F)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.INCANDESCENCE.get(),1200,0),1.0F)
            .build();

    public static final FoodProperties PEATIC_MUSA_SALAD_POWERED = new FoodProperties.Builder()
            .nutrition(6).fast().saturationMod(0.625f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),100,0),1.0F)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.VITALITY.get(),1280,0),1.0F)
            .effect(()-> new MobEffectInstance(MobEffects.REGENERATION,320,0),1.0F)
            .build();

    public static final FoodProperties PEARLIPEARL_TART_POWERED = new FoodProperties.Builder()
            .nutrition(6).saturationMod(1.6f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),100),1.0F)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.VITALITY.get(),4000,1),1.0F)
            .build();

    public static final FoodProperties BANANA_BOX_SALMON_POWERED = new FoodProperties.Builder()
            .nutrition(7).saturationMod(0.6F)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),100),1.0F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.VITALITY.get(),1200,1),1F)
            .effect(new MobEffectInstance(MobEffects.WATER_BREATHING,1800),1F)
            .build();
    public static final FoodProperties BANANA_BOX_COD_POWERED = new FoodProperties.Builder()
            .nutrition(7).saturationMod(0.6F)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),100),1.0F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.VITALITY.get(),1200,1),1F)
            .effect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE,1800),1F)
            .build();

    public static final FoodProperties BOWL_OF_MILLENIAN_BAMBOO_POWERED = new FoodProperties.Builder()
            .nutrition(6).saturationMod(0.65f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),100),1F)
            .effect(()-> new MobEffectInstance(ModEffects.COMFORT.get(),3600,0),1.0F)
            .effect(()-> new MobEffectInstance(ModEffects.NOURISHMENT.get(),3600,0),1.0F)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.CULTURAL_LEGACY.get(),7200,0),1.0F)
            .build();

    public static final FoodProperties MASHED_POTATO_WITH_JAM_POWERED = new FoodProperties.Builder()
            .nutrition(8).saturationMod(0.75F)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),100),1F)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.RELIEVE_POISON.get(),2400),1F)
            .build();

    public static final FoodProperties MASHED_POISONOUS_POTATO_WITH_JAM_POWERED = new FoodProperties.Builder()
            .nutrition(8).saturationMod(1.125f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),100),1F)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.RESISTANCE_TO_ILLAGER.get(),4800),1F)
            .build();

    public static final FoodProperties HIMEKAIDO_CHAZUKE_POWERED = new FoodProperties.Builder()
            .nutrition(6).saturationMod(1.125F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),100),1F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.RELIEVE_POISON.get(),2400),1F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.LINGERING_INFUSION.get(),2400),1F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.COOL.get(),1000),1F)
            .build();

    public static final FoodProperties STUFFED_POISONOUS_POTATO_POWERED = new FoodProperties.Builder()
            .nutrition(10).saturationMod(0.6f)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),200),1F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.RESISTANCE_TO_UNDEAD.get(),5400,1),1F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.RESISTANCE_TO_ILLAGER.get(),2400,1),1F)
            .build();

    public static final FoodProperties TARTARE_CHICKEN_POWERED = new FoodProperties.Builder()
            .nutrition(10).saturationMod(1.0f)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),400,1),1.0F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.RESISTANCE_TO_ARTHROPOD.get(),7200,1),1F)
            .effect(()->new MobEffectInstance(MobEffects.REGENERATION,1680),1F)
            .build();

    public static final FoodProperties CRISPY_YOGURT_ROTTEN_FLESH_POWERED = new FoodProperties.Builder()
            .nutrition(8).saturationMod(0.875f)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),100),1F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.RESISTANCE_TO_UNDEAD.get(),2400),1F)
            .build();

    public static final FoodProperties MEATY_ROTTEN_TOMATO_BROTH_POWERED = new FoodProperties.Builder()
            .nutrition(8).saturationMod(0.4f)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),100),1F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.RESISTANCE_TO_UNDEAD.get(),6000),1F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.RESISTANCE_TO_ILLAGER.get(),3600),1F)
            .effect(()->new MobEffectInstance(ModEffects.COMFORT.get(),5400),1F)
            .build();

    public static final FoodProperties PUFFERFISH_ROLL_POWERED = new FoodProperties.Builder()
            .nutrition(7).saturationMod(0.7f)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),100),1F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.RESISTANCE_TO_ABYSSAL.get(),4000),1F)
            .effect(()->new MobEffectInstance(MobEffects.HEALTH_BOOST,600,1),1F)
            .effect(()->new MobEffectInstance(MobEffects.REGENERATION,500,1),1F)
            .build();

    public static final FoodProperties BOWL_OF_STEWED_ROTTEN_MEAT_IN_CLAY_POT_POWERED = new FoodProperties.Builder()
            .nutrition(8).saturationMod(1.2f)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),100),1F)
            .effect(()->new MobEffectInstance(ModEffects.NOURISHMENT.get(),4000),1F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.RESISTANCE_TO_ABYSSAL.get(),3200,1),1F)
            .effect(()->new MobEffectInstance(MobEffects.HEALTH_BOOST,2400,1),1F)
            .effect(()->new MobEffectInstance(MobEffects.HEAL,1,3),1F)
            .effect(()->new MobEffectInstance(MobEffects.REGENERATION,500,1),1F)
            .build();

    public static final FoodProperties KWAT_WHEAT_TOAST_POWERED = new FoodProperties.Builder()
            .nutrition(6).saturationMod(1.25F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),300),1F)
            .build();

    public static final FoodProperties KWAT_WHEAT_TOAST_SLICE_POWERED = new FoodProperties.Builder()
            .nutrition(2).saturationMod(1.25F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),100),1F)
            .build();

    public static final FoodProperties NETHER_CREAM_SOUP_POWERED = new FoodProperties.Builder()
            .nutrition(7).saturationMod(0.55f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.INCANDESCENCE.get(),1600,2),1F)
            .effect(()->new MobEffectInstance(ModEffects.COMFORT.get(),3600),1F)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),400,1),1F)
            .build();

    public static final FoodProperties NETHER_CREAM_BREAD_POWERED = new FoodProperties.Builder()
            .nutrition(8).saturationMod(0.375f)
            .effect(()->new MobEffectInstance(MobEffects.DAMAGE_BOOST,1200,2),1.0F)
            .effect(()-> new MobEffectInstance(ModEffects.NOURISHMENT.get(),3600),1F)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),300,2),1F)
            .build();

    public static final FoodProperties SUTFFED_KWAT_WHEAT_DOUFU_POWERED = new FoodProperties.Builder()
            .nutrition(12).saturationMod(0.75f)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),100),1F)
            .effect(()->new MobEffectInstance(MobEffects.DAMAGE_BOOST,6000,2),1F)
            .build();

    public static final FoodProperties RED_STUFFED_BUN_POWERED = new FoodProperties.Builder()
            .nutrition(10).saturationMod(0.77F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),800),1.0F)
            .effect(() ->new MobEffectInstance(MobEffects.DAMAGE_BOOST,6000,1),1F)
            .build();

    public static final FoodProperties JADE_AND_RUBY_SOUP_POWERED = new FoodProperties.Builder().alwaysEat()
            .nutrition(7).saturationMod(1.0F)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),250,1),1F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.INCANDESCENCE.get(),1600),1F)
            .effect(()->new MobEffectInstance(ModEffects.COMFORT.get(),2400),1F)
            .build();


    public static final FoodProperties HOT_HI_SOUP_POWERED = new FoodProperties.Builder()
            .nutrition(8).saturationMod(1.25F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),700),1.0F)
            .effect(()->new MobEffectInstance(MobEffects.DAMAGE_BOOST,6600),1F)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.WARM_CURRENT_SURGES.get(),1200),1F)
            .build();

    public static final FoodProperties INCANDESCENCE_SUSHI_POWERED = new FoodProperties.Builder()
            .nutrition(8).saturationMod(0.375f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),100),1F)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.WARM_CURRENT_SURGES.get(),1800),1F)
            .build();

    public static final FoodProperties TORCHFLOWER_CURRY_RICE_POWERED = new FoodProperties.Builder()
            .nutrition(10).saturationMod(0.5F)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),300),1F)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.WARM_CURRENT_SURGES.get(),3600),1F)
            .build();

    public static final FoodProperties EXTRA_SPICY_PASTA_POWERED = new FoodProperties.Builder()
            .nutrition(12).saturationMod(0.35f)
            .effect(()-> new MobEffectInstance(MobEffects.DAMAGE_BOOST,3000),1F)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),300,1),1F)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.WARM_CURRENT_SURGES.get(),2400),1F)
            .build();

    public static final FoodProperties PITCHER_PLANT_BARBECUE_POWERED = new FoodProperties.Builder()
            .nutrition(8).saturationMod(0.375F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),700),1F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.BURN_THE_BOATS.get(),3600,1),1F)
            .build();

    public static final FoodProperties POD_SHELL_BURGER_MEAT_CUBE_POWERED = new FoodProperties.Builder()
            .nutrition(11).saturationMod(0.7F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),680),1.0F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.BURN_THE_BOATS.get(),10400),1F)
            .build();

    public static final FoodProperties ALFALFA_PORRIDGE_POWERED = new FoodProperties.Builder()
            .nutrition(12).saturationMod(0.6f)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),200),1.0F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.KEEP_A_FAST.get(),10200,1),1F)
            .effect(()->new MobEffectInstance(ModEffects.COMFORT.get(),2400),1F)
            .effect(()->new MobEffectInstance(MobEffects.MOVEMENT_SPEED,10800),1F)
            .build();

    public static final FoodProperties EVOLUTCORN_HARD_CANDY_POWERED = new FoodProperties.Builder()
            .nutrition(4).saturationMod(1.2F).alwaysEat()
            .effect(()-> new MobEffectInstance(MobEffects.DAMAGE_BOOST,40,5),1.0F)
            .build();

    public static final FoodProperties EVOLUTCORN_HARD_CANDY_AHEAD_POWERED = new FoodProperties.Builder()
            .nutrition(4).saturationMod(1.2F).alwaysEat()
            .effect(()-> new MobEffectInstance(MobEffects.WEAKNESS,20,5),1.0F)
            .effect(()-> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,20,2),1.0F)
            .build();

    public static final FoodProperties GOLDEN_TOAST_POWERED = new FoodProperties.Builder()
            .nutrition(12).saturationMod(0.75f)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),1200),1.0F)
            .effect(()-> new MobEffectInstance(MobEffects.HEALTH_BOOST,3600,2),1.0F)
            .effect(()-> new MobEffectInstance(MobEffects.ABSORPTION,3600,3),1.0F)
            .effect(()-> new MobEffectInstance(MobEffects.REGENERATION,1100,1),1.0F)
            .alwaysEat().build();

    public static final FoodProperties GOLDEN_TOAST_SLICE_POWERED = new FoodProperties.Builder()
            .nutrition(4).saturationMod(0.75f)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),400),1.0F)
            .effect(()-> new MobEffectInstance(MobEffects.HEALTH_BOOST,2400,1),1.0F)
            .effect(()-> new MobEffectInstance(MobEffects.ABSORPTION,2400,1),1.0F)
            .effect(()-> new MobEffectInstance(MobEffects.REGENERATION,600,1),1.0F)
            .alwaysEat().build();

    public static final FoodProperties SEALED_ANCIENT_RATIONS_POWERED  = new FoodProperties.Builder()
            .nutrition(13).saturationMod(0.75f)
            .effect(() -> new MobEffectInstance(MobEffects.HEAL,1,5),1f)
            .effect(()->new MobEffectInstance(MobEffects.REGENERATION,300,2),1F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),1500,1),1F)
            .build();

    public static final FoodProperties LU_CHICKEN_LEGS  = new FoodProperties.Builder().nutrition(10).saturationMod(0.6F)
            .effect(() -> new MobEffectInstance(ModEffects.COMFORT.get(),600),1f)
            .effect(()->new MobEffectInstance(MobEffects.DAMAGE_BOOST,1200,1),1F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.INCANDESCENCE.get(),600,1),1F)
            .build();

    public static final FoodProperties NETHER_SOUP  = new FoodProperties.Builder().nutrition(8).saturationMod(0.6f).effect(() -> new MobEffectInstance(ImmortalersDelightMobEffect.INEBRIATED.get(),500),1f).effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.DEEPNESS.get(),600,1),1F).build();

    public static final FoodProperties BRAISED_PORK  = new FoodProperties.Builder().nutrition(12).saturationMod(0.7f)
            .effect(() -> new MobEffectInstance(ImmortalersDelightMobEffect.INCANDESCENCE.get(),2000,2),1f)
            .effect(()->new MobEffectInstance(ModEffects.COMFORT.get(),1200),1F)
            .build();

    public static final FoodProperties BOWL_OF_UNIVERSAL_CHICKEN_SOUP  = new FoodProperties.Builder()
            .nutrition(11).saturationMod(0.8f)
            .effect(() -> new MobEffectInstance(ImmortalersDelightMobEffect.DEEPNESS.get(),600,1),1f)
            .effect(() -> new MobEffectInstance(ModEffects.COMFORT.get(),3600),1f)
            .build();

    public static final FoodProperties APOLLYON_CAKE_ROLL  = new FoodProperties.Builder().nutrition(8).saturationMod(0.6875f).effect(() -> new MobEffectInstance(ImmortalersDelightMobEffect.DEEPNESS.get(),6000),1f).effect(new  MobEffectInstance(MobEffects.DARKNESS,3600),1F).build();

    public static final FoodProperties DEVELOPER_LATIAO  = new FoodProperties.Builder().nutrition(20).saturationMod(0.5f)
            .effect(() -> new MobEffectInstance(ImmortalersDelightMobEffect.RESISTANCE_TO_UNDEAD.get(),6000),1f)
            .effect(() -> new MobEffectInstance(ImmortalersDelightMobEffect.RESISTANCE_TO_ARTHROPOD.get(),6000),1f)
            .effect(() -> new MobEffectInstance(ImmortalersDelightMobEffect.RESISTANCE_TO_ABYSSAL.get(),6000),1f)
            .effect(() -> new MobEffectInstance(ImmortalersDelightMobEffect.RESISTANCE_TO_ILLAGER.get(),6000),1f)
            .effect(() -> new MobEffectInstance(ImmortalersDelightMobEffect.MAGICAL_REVERSE.get(),1),1f)
            .fast().alwaysEat().build();

    public static final FoodProperties SUPER_KWAT_WHEAT_HAMBURGER = new FoodProperties.Builder()
            .nutrition(14).saturationMod(0.8f)
            .effect(() -> new MobEffectInstance(ImmortalersDelightMobEffect.DEEPNESS.get(),2000,1),1f)
            .effect(() -> new MobEffectInstance(ImmortalersDelightMobEffect.BURN_THE_BOATS.get(),1600,3),1f)
            .build();
    public static final FoodProperties SUPER_KWAT_WHEAT_HAMBURGER_SLICE = new FoodProperties.Builder()
            .nutrition(7).saturationMod(1.15f)
            .effect(() -> new MobEffectInstance(ImmortalersDelightMobEffect.DEEPNESS.get(),1200),1f)
            .effect(() -> new MobEffectInstance(ImmortalersDelightMobEffect.BURN_THE_BOATS.get(),800,2),1f)
            .build();

    public static final FoodProperties PITCHER_PLANT_JIAO_ZI = new FoodProperties.Builder().nutrition(8).saturationMod(0.8f).effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.GAIXIA.get(),6000),1f).build();

    public static final FoodProperties PITCHER_PLANT_SHAO_MAI = new FoodProperties.Builder().nutrition(10).saturationMod(0.8f).effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.BURN_THE_BOATS.get(),2400),1f).effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.GAIXIA.get(),2400),1f).build();

    public static final FoodProperties BOTTLE_MELON_JUICE = new FoodProperties.Builder().effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.BURN_THE_BOATS.get(),6000),1f).effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.COOL.get(),6000),1f).build();

    public static final FoodProperties BOWL_PITCHER_PLANT_CLAYPOT_RICE = new FoodProperties.Builder().nutrition(8).saturationMod(0.4f).effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.BURN_THE_BOATS.get(),3600),1f).effect(()-> new MobEffectInstance(ModEffects.COMFORT.get(),3600),1f).build();

    public static final FoodProperties EVOLUTCORN_BREAD = new FoodProperties.Builder().saturationMod(1.6f).nutrition(4).build();

    public static final FoodProperties EVOLUTCORN_JIAOZI = new FoodProperties.Builder()
            .saturationMod(0.425f).nutrition(8)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.CULTURAL_LEGACY.get(),1200),1f)
            .effect(()-> new MobEffectInstance(ModEffects.NOURISHMENT.get(),1200),1f)
            .build();

    public static final FoodProperties GELPITAYA_FLESH = new FoodProperties.Builder()
            .nutrition(2).saturationMod(1.2f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.LET_IT_FREEZE.get(),100),1f)
            .build();
    public static final FoodProperties GELPITAYA_ICEPOP = new FoodProperties.Builder()
            .nutrition(4).saturationMod(1.2f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.LET_IT_FREEZE.get(),1800),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.COOL.get(),3600),1f)
            .effect(()-> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,3600),1f)
            .effect(()-> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,3000,1),1f)
            .build();
    public static final FoodProperties GELPITAYA_TART = new FoodProperties.Builder()
            .nutrition(9).saturationMod(0.85f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.LET_IT_FREEZE.get(),2400),1f)
            .build();
    public static final FoodProperties KWAT_SOUP = new FoodProperties.Builder()
            .nutrition(8).saturationMod(0.55f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.INCANDESCENCE.get(),2400),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.GAS_POISON.get(),300),1f)
            .build();
    public static final FoodProperties ICED_KWAT_SOUP = new FoodProperties.Builder()
            .nutrition(13).saturationMod(0.75f)
            .effect(()-> new MobEffectInstance(ModEffects.COMFORT.get(),1200),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.GAS_POISON.get(),100,0),1.0F)
            .build();
    public static final FoodProperties SOUL_TEA = new FoodProperties.Builder()
            .alwaysEat()
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.DEEPNESS.get(),1800),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.LINGERING_INFUSION.get(),1800),1f)
            .build();
    public static final FoodProperties HAMBURGER_MEAT_SUSHI = new FoodProperties.Builder()
            .nutrition(6).saturationMod(1.4f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.BURN_THE_BOATS.get(),3000),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),180),1f)
            .build();
    public static final FoodProperties HONE_MEI_LING = new FoodProperties.Builder()
            .alwaysEat()
            .effect(()-> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,300,2),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.LINGERING_INFUSION.get(),1200),1f)
            .build();
    public static final FoodProperties CARROT_TEA = new FoodProperties.Builder()
            .alwaysEat()
            .effect(()-> new MobEffectInstance(MobEffects.NIGHT_VISION,7200),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.LINGERING_INFUSION.get(),3600),1f)
            .build();
    public static final FoodProperties POD_PETAL_CHEESE_STICK = new FoodProperties.Builder()
            .alwaysEat()
            .nutrition(4).saturationMod(0.5f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.BURN_THE_BOATS.get(),6000,1),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.COOL.get(),1200),1f)
            .effect(()-> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,600),1f)
            .build();
    public static final FoodProperties FILTERED_PEARLIP_BEER = new FoodProperties.Builder()
            .alwaysEat()
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.INEBRIATED.get(),1200),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.VITALITY.get(),12000),1f)
            .build();
    public static final FoodProperties PEARLIP_BEER = new FoodProperties.Builder()
            .alwaysEat()
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.INEBRIATED.get(),3000),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.VITALITY.get(),12000),1f)
            .build();
    public static final FoodProperties FILTERED_VARA_JI = new FoodProperties.Builder()
            .alwaysEat()
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.INEBRIATED.get(),1800),1f)
            .effect(()-> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,1800,2),1.0F)
            .effect(()-> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,1800,3),1.0F)
            .effect(()-> new MobEffectInstance(MobEffects.REGENERATION,1100,1),1.0F)
            .build();
    public static final FoodProperties VARA_JI = new FoodProperties.Builder()
            .alwaysEat()
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.INEBRIATED.get(),3700),1f)
            .effect(()-> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,1800,2),1.0F)
            .effect(()-> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,1800,3),1.0F)
            .effect(()-> new MobEffectInstance(MobEffects.REGENERATION,1100,1),1.0F)
            .build();

    public static final FoodProperties LARGE_COLUMN_SLICE = new FoodProperties.Builder()
            .nutrition(2).saturationMod(2.25f)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.KEEP_A_FAST.get(),400,1),1F)
            .build();
    public static final FoodProperties KWAT_TOFU_MISO_SOUP = new FoodProperties.Builder()
            .nutrition(6).saturationMod(1.5f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.INCANDESCENCE.get(),400,1),1f)
            .effect(()-> new MobEffectInstance(MobEffects.DAMAGE_BOOST,2000,1),1f)
            .build();
    public static final FoodProperties PITCHER_SUSHI = new FoodProperties.Builder()
            .nutrition(6).saturationMod(1.25f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.BURN_THE_BOATS.get(),1200),1f)
            .effect(()-> new MobEffectInstance(ModEffects.COMFORT.get(),3600),1f)
            .build();
    public static final FoodProperties BOWL_OF_KWAT_TOFU_STEW = new FoodProperties.Builder()
            .nutrition(10).saturationMod(1.0f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.INCANDESCENCE.get(),3000),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.WARM_CURRENT_SURGES.get(),3600),1f)
            .build();
    public static final FoodProperties LEISAMBOO_ICECREAM = new FoodProperties.Builder()
            .alwaysEat()
            .nutrition(3).saturationMod(2f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.LINGERING_INFUSION.get(),1000,1),1f)
            .effect(()-> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,1000,1),1f)
            .effect(()-> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,1000,1),1f)
            .build();
    public static final FoodProperties HAKO_JIAOZI = new FoodProperties.Builder()
            .effect(()-> new MobEffectInstance(ModEffects.COMFORT.get(),1,99),0.01f)
            .effect(()-> new MobEffectInstance(MobEffects.LUCK),1f)
            .nutrition(8).saturationMod(0.55f)
            .build();
    public static final FoodProperties EVOLUTCORN_HONEYCOMB_CAKE = new FoodProperties.Builder()
            .nutrition(8).saturationMod(0.5f)
            .effect(()-> new MobEffectInstance(MobEffects.SATURATION,1,3),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.CULTURAL_LEGACY.get(),6000),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.PREHISTORIC_POWERS.get(),6000),1f)
            .build();
    public static final FoodProperties KWAT_POCKET_SUSHI = new FoodProperties.Builder()
            .nutrition(8).saturationMod(0.8625f)
            .effect(()-> new MobEffectInstance(MobEffects.SATURATION,1,2),1f)
            .effect(()-> new MobEffectInstance(MobEffects.DAMAGE_BOOST,1200),1f)
            .effect(()-> new MobEffectInstance(ModEffects.COMFORT.get(),3600),1f)
            .build();
    public static final FoodProperties EVOLUTCORN_MILLEFEUILLE = new FoodProperties.Builder()
            .nutrition(9).saturationMod((float) 2 /3)
            .effect(()-> new MobEffectInstance(MobEffects.HEAL),1f)
            .effect(()-> new MobEffectInstance(MobEffects.HEALTH_BOOST,2400),1f)
            .effect(()-> new MobEffectInstance(ModEffects.COMFORT.get(),1200),1f)
            .build();
    public static final FoodProperties EVOLUTCORN_POT_STICKERS = new FoodProperties.Builder()
            .nutrition(5).saturationMod(1.2f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.CULTURAL_LEGACY.get(),1200),1f)
            .build();
    public static final FoodProperties EVOLUTCORN_ICECREAM = new FoodProperties.Builder()
            .alwaysEat()
            .nutrition(6).saturationMod(0.75f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.CULTURAL_LEGACY.get(),3600),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.COOL.get(),600),1f)
            .effect(()-> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,1000),1f)
            .build();
    public static final FoodProperties EVOLUTCORN_JUICE = new FoodProperties.Builder()
            .alwaysEat()
            .nutrition(4).saturationMod(1.25f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.CULTURAL_LEGACY.get(),2400),1f)
            .effect(()-> new MobEffectInstance(ModEffects.NOURISHMENT.get(),2400),1f)
            .build();
    public static final FoodProperties KU_MESH_NON = new FoodProperties.Builder()
            .nutrition(15).saturationMod(1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.WARM_CURRENT_SURGES.get(),4000,2),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.RESISTANCE_TO_SURROUNDINGS.get(),3000,3),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.INCANDESCENCE.get(),3300,2),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),320),1f)
            .build();
    public static final FoodProperties KU_MESH_NON_SLICE = new FoodProperties.Builder()
            .nutrition(10).saturationMod(1.0f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.WARM_CURRENT_SURGES.get(),2400),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.RESISTANCE_TO_SURROUNDINGS.get(),2200),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.INCANDESCENCE.get(),1900),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),150),1f)
            .build();
    public static final FoodProperties KU_MESH_NON_POWERED = new FoodProperties.Builder()
            .nutrition(15).saturationMod(1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.WARM_CURRENT_SURGES.get(),8000,2),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.RESISTANCE_TO_SURROUNDINGS.get(),6000,3),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.INCANDESCENCE.get(),6600,2),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),640),1f)
            .build();
    public static final FoodProperties KU_MESH_NON_SLICE_POWERED = new FoodProperties.Builder()
            .nutrition(10).saturationMod(1.0f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.WARM_CURRENT_SURGES.get(),4800),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.RESISTANCE_TO_SURROUNDINGS.get(),4400),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.INCANDESCENCE.get(),3800),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),300),1f)
            .build();

    public static final FoodProperties JENG_NANU_SLICE = new FoodProperties.Builder()
            .nutrition(3).saturationMod(0.45f)
            .build();
    public static final FoodProperties SHUTORCH = new FoodProperties.Builder()
            .nutrition(7).saturationMod(1.0f)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.WARM_CURRENT_SURGES.get(),1800),1.0F)
            .effect(()->new MobEffectInstance(MobEffects.WATER_BREATHING,1800),1.0F)
            .build();
    public static final FoodProperties PITCHER_SAUSAGE = new FoodProperties.Builder()
            .nutrition(5).saturationMod(1.6f)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.GAIXIA.get(),3600),1.0F)
            .build();
    public static final FoodProperties BIZARRE_SAUSAGE = new FoodProperties.Builder()
            .nutrition(5).saturationMod(1.5f)
            .effect(()->new MobEffectInstance(MobEffects.MOVEMENT_SPEED,1200),1.0F)
            .effect(()->new MobEffectInstance(MobEffects.JUMP,2400,1),1.0F)
            .effect(()->new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,1200),1.0F)
            .effect(()->new MobEffectInstance(MobEffects.CONFUSION,1200),1.0F)
            .build();
    public static final FoodProperties BIZARRE_SAUSAGE_FOE_DOG = new FoodProperties.Builder()
            .effect(()->new MobEffectInstance(MobEffects.HEALTH_BOOST,6000,2),1.0F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.BURN_THE_BOATS.get(),6000),1.0F)
            .build();
    public static final FoodProperties LONELY_SPIRIT_WINE = new FoodProperties.Builder()
            .alwaysEat()
            .effect(()->new MobEffectInstance(MobEffects.HUNGER,200,29),1.0F)
            .build();
    public static final FoodProperties EVOLUTCORN_CHICKEN_BURGER = new FoodProperties.Builder()
            .nutrition(14).saturationMod(0.75f)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.GAIXIA.get(),2200),1.0F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.WARM_CURRENT_SURGES.get(),1800,2),1.0F)
            .build();
    public static final FoodProperties GLISTERING_WATERMELON_JUICE = new FoodProperties.Builder()
            .alwaysEat()
            .effect(()->new MobEffectInstance(MobEffects.HEAL,1,1),1.0F)
            .effect(()->new MobEffectInstance(MobEffects.REGENERATION,375,1),1.0F)
            .build();
    public static final FoodProperties COLD_KWAT_TOFU_SALAD = new FoodProperties.Builder()
            .nutrition(14).saturationMod(0.75f)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.INCANDESCENCE.get(),600),1.0F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.COOL.get(),600),1.0F)
            .effect(()->new MobEffectInstance(MobEffects.HEAL,1,1),1.0F)
            .effect(()->new MobEffectInstance(MobEffects.REGENERATION,160,1),1.0F)
            .build();
    //吃冰派还会得60s冻结III
    public static final FoodProperties FROSTY_CROWN_MOUSSE = new FoodProperties.Builder()
            .nutrition(11).saturationMod(0.95f)
            .effect(()->new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,5400,2),1.0F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.COOL.get(),1200,2),1.0F)
            .build();
    //吃冰派切片还会得10s冻结
    public static final FoodProperties FROSTY_CROWN_MOUSSE_SLICE = new FoodProperties.Builder()
            .alwaysEat()
            .nutrition(6).saturationMod(1.1f)
            .effect(()->new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,1200,1),1.0F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.COOL.get(),1200,1),1.0F)
            .build();
    public static final FoodProperties BOWL_OF_TENCHIMUYO = new FoodProperties.Builder()
            .nutrition(12).saturationMod(0.6f)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION,200,1),1f)
            .effect(() -> new MobEffectInstance(MobEffects.HEALTH_BOOST,2400,1),1f)
            .effect(() -> new MobEffectInstance(MobEffects.HEAL,1),1f)
            .build();
    public static final FoodProperties BOWL_OF_THIS_SIDE_DOWN = new FoodProperties.Builder()
            .nutrition(12).saturationMod(0.6875f)
            .effect(() -> new MobEffectInstance(ImmortalersDelightMobEffect.UP_SIDE_DOWN.get(),3600,3),1f)
            .effect(() -> new MobEffectInstance(MobEffects.WEAKNESS,2400,4),1f)
            .build();
    public static final FoodProperties FROZEN_MARGARITA_JELLY = new FoodProperties.Builder()
            .alwaysEat()
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.INEBRIATED.get(),1200),1.0F)
            .effect(()->new MobEffectInstance(MobEffects.HEAL,1,1),1.0F)
            .effect(()->new MobEffectInstance(MobEffects.ABSORPTION,2400,2),1.0F)
            .build();
    public static final FoodProperties ANCIENT_ONSEN_TAMAGO = new FoodProperties.Builder()
            .nutrition(6).saturationMod(1.0f)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.PREHISTORIC_POWERS.get(),600),1.0F)
            .effect(()->new MobEffectInstance(MobEffects.HEAL,1,1),1.0F)
            .build();
    public static final FoodProperties ONSEN_TAMAGO = new FoodProperties.Builder()
            .nutrition(4).saturationMod(0.9f)
            .effect(()->new MobEffectInstance(MobEffects.REGENERATION,200),1.0F)
            .build();

    public static final FoodProperties GIANT_TART_SLICE = new FoodProperties.Builder().saturationMod(1.25f).nutrition(3).effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.INCANDESCENCE.get(),1,200),1f).fast().build();

    public static final FoodProperties ABC_OFFEE = new FoodProperties.Builder().effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.SMOKE_ABSTINENCE.get(),9600),1f).alwaysEat().build();
    public static final FoodProperties ABBLUE_BEAUTY_C_OFFEE = new FoodProperties.Builder().effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.SMOKE_ABSTINENCE.get(),8400),1f).effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.DEEPNESS.get(),2400),1f).alwaysEat().build();
    public static final FoodProperties ABC_OOKIE = new FoodProperties.Builder().effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.SMOKE_ABSTINENCE.get(),3600),1f).nutrition(4).saturationMod(0.1f).alwaysEat().build();

//    public static final FoodProperties TEXT = new FoodProperties.Builder()
//            .build();

}
