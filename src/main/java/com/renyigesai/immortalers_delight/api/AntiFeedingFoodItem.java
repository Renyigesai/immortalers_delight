package com.renyigesai.immortalers_delight.api;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public interface AntiFeedingFoodItem {

    @Nullable
    FoodProperties getWholeFoodStats(ItemStack itemIn);

    default FoodProperties notEffectFood(FoodProperties foodProperties) {
        FoodProperties.Builder builder = new FoodProperties.Builder()
                .nutrition(foodProperties.nutrition())
                .saturationModifier(foodProperties.saturation());
        if (foodProperties.canAlwaysEat()) {
            builder.alwaysEdible();
        }
        return builder.build();
    }

    default void addEatEffect(ItemStack pFood, Level pLevel, LivingEntity pLivingEntity) {
        if (!(pFood.getItem() instanceof AntiFeedingFoodItem successor)) {
            return;
        }
        FoodProperties foodStats = successor.getWholeFoodStats(pFood);
        if (foodStats == null || pLevel.isClientSide()) {
            return;
        }
        for (FoodProperties.PossibleEffect possibleEffect : foodStats.effects()) {
            if (pLevel.random.nextFloat() >= possibleEffect.probability()) {
                continue;
            }
            MobEffectInstance instance = possibleEffect.effect();
            pLivingEntity.addEffect(new MobEffectInstance(instance));
        }
    }

    default void addUsedEffectTooltip(ItemStack itemIn, List<Component> lores, float durationFactor) {
        FoodProperties foodStats = this.getWholeFoodStats(itemIn);
        if (foodStats == null) {
            return;
        }
        List<FoodProperties.PossibleEffect> effectList = foodStats.effects();
        if (effectList.isEmpty()) {
            lores.add(Component.translatable("effect.none").withStyle(ChatFormatting.GRAY));
            return;
        }
        for (FoodProperties.PossibleEffect pe : effectList) {
            MobEffectInstance instance = pe.effect();
            MutableComponent line = Component.translatable(instance.getDescriptionId());
            MobEffect effect = instance.getEffect().value();
            if (instance.getAmplifier() > 0) {
                line = Component.translatable("potion.withAmplifier", line, Component.translatable("potion.potency." + instance.getAmplifier()));
            }
            if (instance.getDuration() > 20) {
                line = Component.translatable("potion.withDuration", line, MobEffectUtil.formatDuration(instance, durationFactor, 20.0F));
            }
            lores.add(line.withStyle(effect.getCategory().getTooltipFormatting()));
        }
    }
}
