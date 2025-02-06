package com.renyigesai.immortalers_delight.init;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import vectorwing.farmersdelight.common.registry.ModEffects;

public class ImmortalersDelightFoodProperties {

    public static final FoodProperties BOWL_OF_MILLENIAN_BAMBOO = new FoodProperties.Builder().nutrition(6)
            .saturationMod(0.65f).build();

    public static final FoodProperties EVOLUTCORN = new FoodProperties.Builder().nutrition(2)
            .saturationMod(0.75f).build();

    public static final FoodProperties ROAST_EVOLUTCORN = new FoodProperties.Builder().nutrition(4)
            .saturationMod(0.625f).build();

    public static final FoodProperties EVOLUTCORN_GRAINS = new FoodProperties.Builder().nutrition(2)
            .saturationMod(0.75f).build();

    public static final FoodProperties ROAST_EVOLUTCORN_CHOPS = new FoodProperties.Builder().nutrition(4)
            .saturationMod(0.625f).build();

    public static final FoodProperties CRETACEOUS_ZEA_BALL = new FoodProperties.Builder().nutrition(6)
            .saturationMod(0.45f)
            .effect(()-> new MobEffectInstance(ModEffects.NOURISHMENT.get(),800,0),1.0F)
            .build();

    public static final FoodProperties COLORFUL_GRILLED_SKEWERS = new FoodProperties.Builder().nutrition(9)
            .saturationMod(0.85f).build();

    public static final FoodProperties PEARLIP = new FoodProperties.Builder().nutrition(1)
            .saturationMod(0.4f).build();

    public static final FoodProperties POPOLUTCORN = new FoodProperties.Builder().nutrition(4).fast()
            .saturationMod(0.625f).effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.INCANDESCENCE.get(),600,0),1.0F).build();

    public static final FoodProperties PEARLIPEARL = new FoodProperties.Builder().nutrition(1).fast()
            .saturationMod(0.15f).build();

    public static final FoodProperties PEATIC_MUSA_SALAD = new FoodProperties.Builder().nutrition(6).fast()
            .saturationMod(0.6f).effect(()-> new MobEffectInstance(MobEffects.HEAL,1,0),1.0F).build();

    public static final FoodProperties PEARLIP_MILK_SHAKE = new FoodProperties.Builder().alwaysEat()
            .effect(()-> new MobEffectInstance(MobEffects.REGENERATION,600,0),1.0F)
            .effect(()-> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,600,0),1F).build();

    public static final FoodProperties PEARLIP_PUMPKIN_PIE = new FoodProperties.Builder().nutrition(3).fast()
            .saturationMod(1.0f).build();

    public static final FoodProperties PEARLIPEARL_TART = new FoodProperties.Builder().nutrition(8)
            .saturationMod(0.625f).build();

    public static final FoodProperties PEARLIPEARL_EGGSTEAM = new FoodProperties.Builder().nutrition(2)
            .saturationMod(0.3f).effect(()-> new MobEffectInstance(ModEffects.COMFORT.get(),1200,0),1.0F)
            .build();

    public static final FoodProperties PEARLIP_JELLY = new FoodProperties.Builder().nutrition(2)
            .saturationMod(0.2f).effect(()-> new MobEffectInstance(MobEffects.JUMP,1200,0),1.0F)
            .effect(()-> new MobEffectInstance(MobEffects.MOVEMENT_SPEED,1200,0),1.0F)
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
            .effect(()-> new MobEffectInstance(MobEffects.SATURATION,4,0),1.0F).build();
    public static final FoodProperties YOGURT = new FoodProperties.Builder().alwaysEat()
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.RELIEVE_POISON.get(),400,0),1.0F)
            .effect(()-> new MobEffectInstance(MobEffects.SATURATION,4,0),0.51F).build();
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
                return new MobEffectInstance((MobEffect)ImmortalersDelightMobEffect.RESISTANCE_TO_UNDEAD.get(),320,0);
            }, 0.6F)
            .build();

    public static final FoodProperties CRISPY_YOGURT_ROTTEN_FLESH = new FoodProperties.Builder()
            .nutrition(6)
            .saturationMod(0.6f)
            .effect(() -> {
                return new MobEffectInstance((MobEffect)ImmortalersDelightMobEffect.RESISTANCE_TO_UNDEAD.get(),720,0);
            }, 1.0F)
            .build();

    public static final FoodProperties MEATY_ROTTEN_TOMATO_BROTH = new FoodProperties.Builder()
            .nutrition(8)
            .saturationMod(0.4f)
            .effect(() -> {
                return new MobEffectInstance((MobEffect)ImmortalersDelightMobEffect.RESISTANCE_TO_UNDEAD.get(),560,0);
            }, 1.0F)
            .effect(() -> {
                return new MobEffectInstance((MobEffect)ImmortalersDelightMobEffect.RESISTANCE_TO_ILLAGER.get(),360,0);
            }, 1.0F)
            .build();

    public static final FoodProperties BRAISED_SPIDER_EYES_IN_GRAVY = new FoodProperties.Builder()
            .nutrition(4)
            .saturationMod(0.5f)
            .effect(() -> {
                return new MobEffectInstance((MobEffect)ImmortalersDelightMobEffect.RESISTANCE_TO_ARTHROPOD.get(),560,0);
            }, 1.0F)
            .effect(() -> {
                return new MobEffectInstance((MobEffect)ImmortalersDelightMobEffect.RESISTANCE_TO_UNDEAD.get(),360,0);
            }, 1.0F)
            .effect(new MobEffectInstance(MobEffects.REGENERATION,150,0),1.0F)
            .build();
    public static final FoodProperties TARTARE_CHICKEN = new FoodProperties.Builder()
            .nutrition(8)
            .saturationMod(0.875f)
            .effect(() -> {
                return new MobEffectInstance((MobEffect)ImmortalersDelightMobEffect.RESISTANCE_TO_ARTHROPOD.get(),640,1);
            }, 1.0F)
            .effect(new MobEffectInstance(MobEffects.REGENERATION,450,0),1.0F)
            .build();

    public static final FoodProperties STUFFED_POISONOUS_POTATO = new FoodProperties.Builder()
            .nutrition(10)
            .saturationMod(0.6f)
            .effect(() -> {
                return new MobEffectInstance((MobEffect)ImmortalersDelightMobEffect.RESISTANCE_TO_UNDEAD.get(),360,0);
            }, 1.0F)
            .effect(() -> {
                return new MobEffectInstance((MobEffect)ImmortalersDelightMobEffect.RESISTANCE_TO_ILLAGER.get(),800,0);
            }, 1.0F)
            .build();

    public static final FoodProperties PUFFERFISH_ROLL = new FoodProperties.Builder()
            .nutrition(6)
            .saturationMod(0.725f)
            .effect(() -> {
                return new MobEffectInstance((MobEffect)ImmortalersDelightMobEffect.RESISTANCE_TO_ABYSSAL.get(),800,0);
            }, 1.0F)
            .effect(new MobEffectInstance(MobEffects.HEALTH_BOOST,600,0),1.0F)
            .effect(new MobEffectInstance(MobEffects.REGENERATION,200,1),1.0F)
            .build();
    public static final FoodProperties BOWL_OF_STEWED_ROTTEN_MEAT_IN_CLAY_POT = new FoodProperties.Builder()
            .nutrition(10)
            .saturationMod(0.75f)
            .effect(() -> {
                return new MobEffectInstance((MobEffect)ImmortalersDelightMobEffect.RESISTANCE_TO_ABYSSAL.get(),1600,0);
            }, 1.0F)
            .effect(new MobEffectInstance(MobEffects.HEALTH_BOOST,2400,0),1.0F)
            .effect(new MobEffectInstance(MobEffects.REGENERATION,200,1),1.0F)
            .build();
    public static final FoodProperties GOLDEN_HIMEKAIDO = new FoodProperties.Builder()
            .nutrition(4)
            .saturationMod(0.6f)
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
            .effect(new MobEffectInstance(MobEffects.ABSORPTION,2400,1),1.0F)
            .effect(new MobEffectInstance(MobEffects.HEALTH_BOOST,2400,1),1.0F)
            .alwaysEat()
            .build();


    //PEARLIPEARL_EGGSTEAM

    public static final FoodProperties ZEA_PANCAKE_SLICE = new FoodProperties.Builder().nutrition(3)
            .saturationMod(0.65f).build();
}
