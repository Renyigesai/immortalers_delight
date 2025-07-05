package com.renyigesai.immortalers_delight.item;

import net.minecraft.world.food.FoodProperties;
import org.jetbrains.annotations.Nullable;

public class LingeringFoodItem extends PowerfulAbleFoodItem{
    public LingeringFoodItem(Properties properties, @Nullable FoodProperties powerFoodProperties, boolean hasFoodEffectTooltip, boolean hasCustomTooltip) {
        super(properties, powerFoodProperties, hasFoodEffectTooltip, hasCustomTooltip);
    }

    public LingeringFoodItem(Properties properties, @Nullable FoodProperties powerFoodProperties, boolean hasFoodEffectTooltip, boolean hasCustomTooltip, boolean isFoil) {
        super(properties, powerFoodProperties, hasFoodEffectTooltip, hasCustomTooltip, isFoil);
    }
}
