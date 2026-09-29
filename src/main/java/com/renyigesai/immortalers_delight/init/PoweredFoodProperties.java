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
    public static final FoodProperties LONELY_SPIRIT_WINE = new FoodProperties.Builder()
            .alwaysEat()
            .nutrition(-4).saturationMod(1.0f)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.WEAK_WITHER.get(),1200,4),1.0F)
            .build();
    public static final FoodProperties LONELY_SPIRIT_WINE_AHEAD = new FoodProperties.Builder()
            .effect(()->new MobEffectInstance(MobEffects.CONFUSION,300,1),1.0F)
            .build();
    public static final FoodProperties GLISTERING_WATERMELON_JUICE = new FoodProperties.Builder()
            .alwaysEat()
            .effect(()->new MobEffectInstance(MobEffects.HEAL,1,3),1.0F)
            .effect(()->new MobEffectInstance(MobEffects.REGENERATION,1125,1),1.0F)
            .build();
    public static final FoodProperties KWAT_SOUP = new FoodProperties.Builder()
            .nutrition(8).saturationMod(0.55f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.INCANDESCENCE.get(),4800),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.GAS_POISON.get(),300),1f)
            .build();
    public static final FoodProperties ICED_KWAT_SOUP = new FoodProperties.Builder()
            .nutrition(13).saturationMod(0.75f)
            .effect(()-> new MobEffectInstance(ModEffects.COMFORT.get(),3600),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.GAS_POISON.get(),100,0),1.0F)
            .build();
    public static final FoodProperties SUPER_KWAT_WHEAT_HAMBURGER_POWERED = new FoodProperties.Builder()
            .nutrition(14).saturationMod(0.8f)
            .effect(() -> new MobEffectInstance(ImmortalersDelightMobEffect.DEEPNESS.get(),4000,1),1f)
            .effect(() -> new MobEffectInstance(ImmortalersDelightMobEffect.BURN_THE_BOATS.get(),3200,3),1f)
            .build();
    public static final FoodProperties SUPER_KWAT_WHEAT_HAMBURGER_SLICE = new FoodProperties.Builder()
            .nutrition(7).saturationMod(1.15f)
            .effect(() -> new MobEffectInstance(ImmortalersDelightMobEffect.DEEPNESS.get(),2400),1f)
            .effect(() -> new MobEffectInstance(ImmortalersDelightMobEffect.BURN_THE_BOATS.get(),1600,2),1f)
            .build();
    public static final FoodProperties COLD_KWAT_TOFU_SALAD = new FoodProperties.Builder()
            .nutrition(14).saturationMod(0.75f)
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
    public static final FoodProperties BOWL_OF_KWAT_TOFU_STEW = new FoodProperties.Builder()
            .nutrition(10).saturationMod(1.0f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.INCANDESCENCE.get(),3300,1),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.WARM_CURRENT_SURGES.get(),3600,1),1f)
            .build();
    public static final FoodProperties BRAISED_PORK  = new FoodProperties.Builder()
            .nutrition(12).saturationMod(0.7f)
            .effect(() -> new MobEffectInstance(ImmortalersDelightMobEffect.INCANDESCENCE.get(),4000,2),1f)
            .effect(()->new MobEffectInstance(ModEffects.COMFORT.get(),3600),1F)
            .build();

    public static final FoodProperties BOWL_OF_UNIVERSAL_CHICKEN_SOUP  = new FoodProperties.Builder()
            .nutrition(11).saturationMod(0.8f)
            .effect(() -> new MobEffectInstance(ImmortalersDelightMobEffect.DEEPNESS.get(),1200,1),1f)
            .effect(() -> new MobEffectInstance(ModEffects.COMFORT.get(),7200),1f)
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
            .effect(()->new MobEffectInstance(MobEffects.DAMAGE_BOOST,2400,1),1F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.INCANDESCENCE.get(),1200,1),1F)
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
            .effect(()->new MobEffectInstance(MobEffects.HEAL,1,3),1.0F)
            .effect(()->new MobEffectInstance(MobEffects.ABSORPTION,2400,5),1.0F)
            .build();
    public static final FoodProperties BIZARRE_SAUSAGE_FOE_DOG = new FoodProperties.Builder()
            .effect(()->new MobEffectInstance(MobEffects.HEALTH_BOOST,12000,4),1.0F)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.BURN_THE_BOATS.get(),12000,2),1.0F)
            .build();
    public static final FoodProperties ANCIENT_ONSEN_TAMAGO = new FoodProperties.Builder()
            .nutrition(6).saturationMod(1.0f)
            .effect(()->new MobEffectInstance(ImmortalersDelightMobEffect.PREHISTORIC_POWERS.get(),1800),1.0F)
            .effect(()->new MobEffectInstance(MobEffects.HEAL,1,2),1.0F)
            .build();
    public static final FoodProperties ONSEN_TAMAGO = new FoodProperties.Builder()
            .nutrition(4).saturationMod(0.9f)
            .effect(()->new MobEffectInstance(MobEffects.REGENERATION,600),1.0F)
            .build();
    public static final FoodProperties JENG_NANU_SLICE = new FoodProperties.Builder()
            .nutrition(3).saturationMod(0.45f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.PREHISTORIC_POWERS.get(),1800,1),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.INCANDESCENCE.get(),1600,1),1f)
            .build();

    public static final FoodProperties SYRUP_WITH_KERNELCURD = new FoodProperties.Builder()
            .nutrition(10).saturationMod(1)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.COOL.get(),1200,1),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.INFERNAL_FORGING.get(),3600,2),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.RELIEVE_POISON.get(),3600,2),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.LET_IT_FREEZE.get(),3600),1f)
            .build();

    public static final FoodProperties BOWL_OF_ANCIENT_OYAKODON = new FoodProperties.Builder()
            .nutrition(10).saturationMod(0.9f)
            .effect(()-> new MobEffectInstance(MobEffects.HEAL,1,2),1f)
            .effect(()-> new MobEffectInstance(MobEffects.HEALTH_BOOST,2400,2),1f)
            .effect(()-> new MobEffectInstance(MobEffects.MOVEMENT_SPEED,3600,1),1f)
            .effect(()-> new MobEffectInstance(MobEffects.DIG_SPEED,3600,1),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),1600),1f)
            .build();
    public static final FoodProperties BOWL_OF_OCEAN_CURRENT_SOBA = new FoodProperties.Builder()
            .nutrition(20).saturationMod(0.95f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.COOL.get(),1600),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.PREHISTORIC_POWERS.get(),2400),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.WARM_CURRENT_SURGES.get(),4800),1f)
            .effect(()-> new MobEffectInstance(MobEffects.CONDUIT_POWER,10800),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),300),1f)
            .build();
    public static final FoodProperties SOY_PASTE_SCRAMBLED_EGGS = new FoodProperties.Builder()
            .nutrition(9).saturationMod(1)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.INCANDESCENCE.get(),1600),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.PREHISTORIC_POWERS.get(),2400),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),1200),1f)
            .build();
    public static final FoodProperties BOWL_OF_SOY_PASTE_SCRAMBLED_EGGS = new FoodProperties.Builder()
            .nutrition(7).saturationMod(1)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.INCANDESCENCE.get(),1600),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.DEEPNESS.get(),2400),1f)
            .effect(()-> new MobEffectInstance(MobEffects.REGENERATION,1800),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),100),1f)
            .build();

    public static final FoodProperties GLACIER_FIRES = new FoodProperties.Builder()
            .nutrition(5).saturationMod(1.15f)
            .alwaysEat()
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.LET_IT_FREEZE.get(),1200,1),1f)
            .build();

    public static final FoodProperties ICE_AND_FIRE_CREAM = new FoodProperties.Builder()
            .alwaysEat()
            .nutrition(8).saturationMod(1.25f)
            .build();

    public static final FoodProperties FIREBIRD_YAKITORI = new FoodProperties.Builder()
            .nutrition(12).saturationMod(0.75f)
            .effect(()-> new MobEffectInstance(MobEffects.REGENERATION,600,1),1f)
            .effect(()-> new MobEffectInstance(MobEffects.HEALTH_BOOST,4800),2f)
            .effect(()-> new MobEffectInstance(MobEffects.ABSORPTION,2400,3),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),300),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.INCANDESCENCE.get(),12000,1),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.WARM_CURRENT_SURGES.get(),7200,1),1f)
            .build();
    public static final FoodProperties MEATBALL_EGG_VEG_SOUP = new FoodProperties.Builder()
            .nutrition(9).saturationMod(1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.MOONBRIGHT.get(),18000,1),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),200,1),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.PREHISTORIC_POWERS.get(),18000,1),1f)
            .build();

    public static final FoodProperties SEXTLOTUS_MOCHI = new FoodProperties.Builder()
            .nutrition(4).saturationMod(2.05f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.MOONBRIGHT.get(),3000,1),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.CRISP_FORTITUDE.get(),800,2),1f)
            .effect(()-> new MobEffectInstance(ImmortalersDelightMobEffect.CULTURAL_LEGACY.get(),10800,1),1f)
            .build();
}
