package com.renyigesai.immortalers_delight.item;

import com.mojang.datafixers.util.Pair;
import com.renyigesai.immortalers_delight.event.DifficultyModeHelper;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ShieldLikeFoodItem extends PowerfulAbleFoodItem{

    @javax.annotation.Nullable
    private final FoodProperties aheadFoodProperties;

    @javax.annotation.Nullable
    private final FoodProperties poweredAheadFoodProperties;

    public ShieldLikeFoodItem(Properties properties, @Nullable FoodProperties powerFoodProperties,
                              @Nullable FoodProperties aheadFoodProperties, @Nullable FoodProperties poweredAheadFoodProperties,
                              boolean hasFoodEffectTooltip, boolean hasCustomTooltip) {
        super(properties, powerFoodProperties, hasFoodEffectTooltip, hasCustomTooltip);
        this.aheadFoodProperties = aheadFoodProperties;
        this.poweredAheadFoodProperties = poweredAheadFoodProperties;
    }

    public ShieldLikeFoodItem(Properties properties, @Nullable FoodProperties powerFoodProperties,
                              @Nullable FoodProperties aheadFoodProperties, @Nullable FoodProperties poweredAheadFoodProperties,
                              boolean hasFoodEffectTooltip, boolean hasCustomTooltip, boolean isFoil) {
        super(properties, powerFoodProperties, hasFoodEffectTooltip, hasCustomTooltip, isFoil);
        this.aheadFoodProperties = aheadFoodProperties;
        this.poweredAheadFoodProperties = poweredAheadFoodProperties;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack pStack) {
        return 300;
    }

    public @Nullable FoodProperties getAheadFoodProperties() {
        return DifficultyModeHelper.isPowerBattleMode() ? this.poweredAheadFoodProperties : this.aheadFoodProperties;
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        if (pRemainingUseDuration >= 0 && !pLivingEntity.level().isClientSide()) {
            if (pStack.getItem() instanceof ShieldLikeFoodItem && pRemainingUseDuration >= 14) {
                if ((this.getUseDuration(pStack) - pRemainingUseDuration + 1) % 14 == 1) {
                    addAheadFoodEffect(pStack, pLivingEntity.level(), pLivingEntity);
                }
            }
        }
    }

    private void addAheadFoodEffect(ItemStack stack, Level level, LivingEntity livingEntity) {
        if (stack.getItem() instanceof ShieldLikeFoodItem shieldLikeFoodItem && shieldLikeFoodItem.getAheadFoodProperties() != null) {
            for (Pair<MobEffectInstance, Float> pair : shieldLikeFoodItem.getAheadFoodProperties().getEffects()) {
                if (!level.isClientSide() && pair.getFirst() != null) {
                    livingEntity.addEffect(pair.getFirst());
                }
            }
        }
    }
}
