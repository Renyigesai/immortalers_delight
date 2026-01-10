package com.renyigesai.immortalers_delight.init;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import vectorwing.farmersdelight.common.registry.ModEffects;
public class PoweredFoodProperties {
    public static final FoodProperties FILTERED_PEARLIP_BEER = new FoodProperties.Builder()
            .alwaysEat()
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.INEBRIATED.get(),1200),1f)
            .effect(()-> new MobEffectInstance(MobEffects.DAMAGE_BOOST,12000),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.VITALITY.get(),15000,1),1f)
            .build();
    public static final FoodProperties PEARLIP_BEER = new FoodProperties.Builder()
            .alwaysEat()
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.INEBRIATED.get(),3000),1f)
            .effect(()-> new MobEffectInstance(MobEffects.DAMAGE_BOOST,12000),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.VITALITY.get(),15000,1),1f)
            .build();
    public static final FoodProperties FILTERED_VARA_JI = new FoodProperties.Builder()
            .alwaysEat()
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.INEBRIATED.get(),1800),1f)
            .effect(()-> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,5400,2),1.0F)
            .effect(()-> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,800,3),1.0F)
            .effect(()-> new MobEffectInstance(MobEffects.REGENERATION,3600,1),1.0F)
            .build();
    public static final FoodProperties VARA_JI = new FoodProperties.Builder()
            .alwaysEat()
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.INEBRIATED.get(),3700),1f)
            .effect(()-> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,5400,2),1.0F)
            .effect(()-> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,1200,3),1.0F)
            .effect(()-> new MobEffectInstance(MobEffects.REGENERATION,3600,1),1.0F)
            .build();
    public static final FoodProperties GLISTERING_WATERMELON_JUICE = new FoodProperties.Builder()
            .alwaysEat()
            .effect(()->new MobEffectInstance(MobEffects.HEAL,1,3),1.0F)
            .effect(()->new MobEffectInstance(MobEffects.REGENERATION,1125,1),1.0F)
            .build();
    public static final FoodProperties SUPER_KWAT_WHEAT_HAMBURGER_POWERED = new FoodProperties.Builder()
            .nutrition(15).saturationMod(1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.WARM_CURRENT_SURGES.get(),8000,2),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.BURN_THE_BOATS.get(),6000,3),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.INCANDESCENCE.get(),6600,2),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),640),1f)
            .build();
    public static final FoodProperties COLD_KWAT_TOFU_SALAD = new FoodProperties.Builder()
            .nutrition(14).saturationMod(0.8f)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.INCANDESCENCE.get(),1200),1.0F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.COOL.get(),1200),1.0F)
            .effect(()->new MobEffectInstance(MobEffects.HEAL,1,3),1.0F)
            .effect(()->new MobEffectInstance(MobEffects.REGENERATION,320,2),1.0F)
            .build();
    public static final FoodProperties EVOLUTCORN_CHICKEN_BURGER = new FoodProperties.Builder()
            .nutrition(14).saturationMod(0.75f)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.GAIXIA.get(),4400),1.0F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.WARM_CURRENT_SURGES.get(),3600,2),1.0F)
            .build();
    public static final FoodProperties BRAISED_PORK  = new FoodProperties.Builder()
            .nutrition(12).saturationMod(0.7f)
            .effect(() -> new MobEffectInstance(ImmortalersDelightMobEffect.INCANDESCENCE.get(),2400),1f)
            .effect(()->new MobEffectInstance(ModEffects.COMFORT.get(),3600),1F)
            .build();

    public static final FoodProperties BOWL_OF_UNIVERSAL_CHICKEN_SOUP  = new FoodProperties.Builder()
            .nutrition(11).saturationMod(0.8f)
            .effect(() -> new MobEffectInstance(ImmortalersDelightMobEffect.DEEPNESS.get(),4000,1),1f)
            .effect(() -> new MobEffectInstance(ImmortalersDelightMobEffect.RESISTANCE_TO_SURROUNDINGS.get(),3200,3),1f)
            .build();
    public static final FoodProperties BOWL_OF_TENCHIMUYO = new FoodProperties.Builder()
            .nutrition(12).saturationMod(0.6f)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION,600,1),1f)
            .effect(() -> new MobEffectInstance(MobEffects.HEALTH_BOOST,2400,2),1f)
            .effect(() -> new MobEffectInstance(MobEffects.HEAL,3),1f)
            .build();
    public static final FoodProperties LU_CHICKEN_LEGS  = new FoodProperties.Builder()
            .nutrition(10).saturationMod(0.6F)
            .effect(() -> new MobEffectInstance(ModEffects.COMFORT.get(),1800),1f)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.INCANDESCENCE.get(),900,1),1F)
            .build();
     public static final FoodProperties APOLLYON_CAKE_ROLL  = new FoodProperties.Builder().nutrition(8).saturationMod(0.6875f)
            .effect(() -> new MobEffectInstance(ImmortalersDelightMobEffect.DEEPNESS.get(),9000,1),1f)
            .effect(new  MobEffectInstance(MobEffects.DARKNESS,3600),1F).build();
    public static final FoodProperties BOWL_PITCHER_PLANT_CLAYPOT_RICE = new FoodProperties.Builder()
            .nutrition(8).saturationMod(0.4f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.BURN_THE_BOATS.get(),5400,1),1f)
            .effect(()-> new MobEffectInstance(ModEffects.COMFORT.get(),7200),1f)
            .build();
    //超凡吃冰派还会得180s冻结III
    public static final FoodProperties FROSTY_CROWN_MOUSSE = new FoodProperties.Builder()
            .nutrition(11).saturationMod(0.95f)
            .effect(()->new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,5400,3),1.0F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.COOL.get(),1200,3),1.0F)
            .build();
    //吃冰派切片还会得10s冻结
    public static final FoodProperties FROSTY_CROWN_MOUSSE_SLICE = new FoodProperties.Builder()
            .alwaysEat()
            .nutrition(6).saturationMod(1.1f)
            .effect(()->new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,1200,1),1.0F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.COOL.get(),1200,1),1.0F)
            .build();
    public static final FoodProperties BOWL_OF_THIS_SIDE_DOWN = new FoodProperties.Builder()
            .nutrition(12).saturationMod(0.6875f)
            .effect(() -> new MobEffectInstance(ImmortalersDelightMobEffect.UP_SIDE_DOWN.get(),6000,7),1f)
            .effect(() -> new MobEffectInstance(MobEffects.WEAKNESS,3000,3),1f)
            .build();
    public static final FoodProperties FROZEN_MARGARITA_JELLY = new FoodProperties.Builder()
            .alwaysEat()
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.INEBRIATED.get(),1200),1.0F)
            .effect(()->new MobEffectInstance(MobEffects.HEAL,1,1),1.0F)
            .effect(()->new MobEffectInstance(MobEffects.ABSORPTION,2400,2),1.0F)
            .build();

}
