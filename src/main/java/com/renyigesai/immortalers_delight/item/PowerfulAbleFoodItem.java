package com.renyigesai.immortalers_delight.item;

import com.renyigesai.immortalers_delight.event.DifficultyModeHelper;
import net.minecraft.world.food.FoodProperties;

import javax.annotation.Nullable;

public class PowerfulAbleFoodItem extends EnchantAbleFoodItem{
    @Nullable
    private final FoodProperties poweredFoodProperties;

    public PowerfulAbleFoodItem(Properties properties, @org.jetbrains.annotations.Nullable FoodProperties powerFoodProperties, boolean hasFoodEffectTooltip, boolean hasCustomTooltip) {
        super(properties, hasFoodEffectTooltip, hasCustomTooltip);
        this.poweredFoodProperties = powerFoodProperties;
    }

    public PowerfulAbleFoodItem(Properties properties, @org.jetbrains.annotations.Nullable FoodProperties powerFoodProperties, boolean hasFoodEffectTooltip, boolean hasCustomTooltip, boolean isFoil) {
        super(properties, hasFoodEffectTooltip, hasCustomTooltip, isFoil);
        this.poweredFoodProperties = powerFoodProperties;
    }

    @Override
    public FoodProperties getFoodProperties() {
        return DifficultyModeHelper.isPowerBattleMode() ? this.poweredFoodProperties : super.getFoodProperties();
    }


}
