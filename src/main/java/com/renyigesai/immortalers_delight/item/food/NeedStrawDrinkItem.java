package com.renyigesai.immortalers_delight.item.food;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightTags;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class NeedStrawDrinkItem extends InebriatedToxicFoodItem{
    @Nullable
    private final FoodProperties haveStrawFoodProperties;
    @Nullable
    private final FoodProperties poweredFoodProperties;
    @Nullable
    private final FoodProperties haveStrawPoweredFoodProperties;
    public NeedStrawDrinkItem(Properties properties,FoodProperties haveStrawFood, FoodProperties poweredFood, FoodProperties haveStrawPoweredFood, boolean hasFoodEffectTooltip) {
        super(properties, hasFoodEffectTooltip);
        this.haveStrawFoodProperties = haveStrawFood;
        this.poweredFoodProperties = poweredFood;
        this.haveStrawPoweredFoodProperties = haveStrawPoweredFood;
    }

    public NeedStrawDrinkItem(Properties properties,FoodProperties haveStrawFood, FoodProperties poweredFood, FoodProperties haveStrawPoweredFood, boolean hasFoodEffectTooltip, boolean hasCustomTooltip) {
        super(properties, hasFoodEffectTooltip, hasCustomTooltip);
        this.haveStrawFoodProperties = haveStrawFood;
        this.poweredFoodProperties = poweredFood;
        this.haveStrawPoweredFoodProperties = haveStrawPoweredFood;
    }

    public NeedStrawDrinkItem(Properties properties,FoodProperties haveStrawFood, FoodProperties poweredFood, FoodProperties haveStrawPoweredFood, boolean hasFoodEffectTooltip, boolean hasCustomTooltip, boolean isFoil, boolean canFeed) {
        super(properties, hasFoodEffectTooltip, hasCustomTooltip, isFoil, canFeed);
        this.haveStrawFoodProperties = haveStrawFood;
        this.poweredFoodProperties = poweredFood;
        this.haveStrawPoweredFoodProperties = haveStrawPoweredFood;
    }

    @Override
    public @NotNull ItemStack finishUsingItem (@NotNull ItemStack stack, @NotNull Level level, LivingEntity livingEntity) {
        ItemStack itemStack = super.finishUsingItem(stack, level, livingEntity);
        if (livingEntity instanceof Player player && !player.getAbilities().instabuild) {
            InteractionHand hand = livingEntity.getUsedItemHand();
            if (player.getMainHandItem().is(ImmortalersDelightTags.STRAW)) {
                hand = InteractionHand.MAIN_HAND;
            }
            if (player.getOffhandItem().is(ImmortalersDelightTags.STRAW)) {
                hand = InteractionHand.OFF_HAND;
            }
            player.getItemInHand(hand).shrink(1);
        }
        return itemStack;
    }

    @Override
    public FoodProperties getFoodProperties() {
        if (DifficultyModeUtil.isPowerBattleMode()) {
            if (poweredFoodProperties != null) {
                return this.poweredFoodProperties;
            }
        }
        return super.getFoodProperties();
    }
    @Override
    public FoodProperties getFoodProperties(ItemStack stack, @org.jetbrains.annotations.Nullable LivingEntity entity) {
        if (entity != null && haveStrawFoodProperties != null) {
            if (entity instanceof Player) {
                if (entity.getOffhandItem().is(ImmortalersDelightTags.STRAW) || entity.getMainHandItem().is(ImmortalersDelightTags.STRAW)) {
                    if (DifficultyModeUtil.isPowerBattleMode()) {
                        return this.haveStrawPoweredFoodProperties;
                    }
                    return this.haveStrawFoodProperties;
                }
            } else if (entity instanceof Enemy) {
                if (DifficultyModeUtil.isPowerBattleMode()) {
                    return this.haveStrawPoweredFoodProperties;
                }
                return this.haveStrawFoodProperties;
            }
        }
        return this.getFoodProperties();
    }

}
