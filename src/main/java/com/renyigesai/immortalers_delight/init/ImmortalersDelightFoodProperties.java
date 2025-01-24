package com.renyigesai.immortalers_delight.init;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

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
            .saturationMod(0.45f).build();

    public static final FoodProperties COLORFUL_GRILLED_SKEWERS = new FoodProperties.Builder().nutrition(9)
            .saturationMod(0.85f).build();

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
            .saturationMod(0.3f).build();

    public static final FoodProperties PEARLIP_JELLY = new FoodProperties.Builder().nutrition(2)
            .saturationMod(0.2f).build();

    //PEARLIPEARL_EGGSTEAM

    public static final FoodProperties ZEA_PANCAKE_SLICE = new FoodProperties.Builder().nutrition(3)
            .saturationMod(0.65f).build();
}
