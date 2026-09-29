package com.renyigesai.immortalers_delight.item.food;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.item.PowerfulAbleFoodItem;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class GlacierFiresItem extends PowerfulAbleFoodItem {
    public GlacierFiresItem(Properties properties, @Nullable FoodProperties powerFoodProperties, boolean hasFoodEffectTooltip, boolean hasCustomTooltip) {
        super(properties, powerFoodProperties, hasFoodEffectTooltip, hasCustomTooltip);
    }

    public GlacierFiresItem(Properties properties, @Nullable FoodProperties powerFoodProperties, boolean hasFoodEffectTooltip, boolean hasCustomTooltip, boolean isFoil) {
        super(properties, powerFoodProperties, hasFoodEffectTooltip, hasCustomTooltip, isFoil);
    }

    public GlacierFiresItem(Properties properties, @Nullable FoodProperties powerFoodProperties, boolean hasFoodEffectTooltip, boolean hasCustomTooltip, int toolTipColor) {
        super(properties, powerFoodProperties, hasFoodEffectTooltip, hasCustomTooltip, toolTipColor);
    }

    public GlacierFiresItem(Properties properties, @Nullable FoodProperties powerFoodProperties, boolean hasFoodEffectTooltip, boolean hasCustomTooltip, boolean isFoil, int toolTipColor) {
        super(properties, powerFoodProperties, hasFoodEffectTooltip, hasCustomTooltip, isFoil, toolTipColor);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity consumer) {
        ItemStack result = super.finishUsingItem(stack, level, consumer);

        if (level.isClientSide()) return result;
        if (consumer.getRandom().nextBoolean()) result.grow(1);
        else if (DifficultyModeUtil.isPowerBattleMode()) {
            MobEffectInstance instance = consumer.getEffect(ImmortalersDelightMobEffect.LET_IT_FREEZE.get());
            if (instance != null) {
                int time = instance.getDuration();
                int lv = instance.getAmplifier();
                if (lv <= 1) consumer.addEffect(new MobEffectInstance(instance.getEffect(),time + 1200, lv));
            }
        }

        return result;
    }
}
