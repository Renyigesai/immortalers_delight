package com.renyigesai.immortalers_delight.api;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public interface AntiFeedingFoodItem {

    @Nullable
    FoodProperties getWholeFoodStats(ItemStack itemIn);

    default FoodProperties notEffectFood(FoodProperties foodProperties) {
        boolean always = foodProperties.canAlwaysEat();
        boolean fast = foodProperties.isFastFood();
        boolean meat = foodProperties.isMeat();
        int nutrition = foodProperties.getNutrition();
        float saturation = foodProperties.getSaturationModifier();
        if (always && fast && meat) {
            return new FoodProperties.Builder().nutrition(nutrition).saturationMod(saturation).alwaysEat().fast().meat().build();
        } else if (always && fast) {
            return new FoodProperties.Builder().nutrition(nutrition).saturationMod(saturation).alwaysEat().fast().build();
        } else if (meat && fast) {
            return new FoodProperties.Builder().nutrition(nutrition).saturationMod(saturation).meat().fast().build();
        } else if (always && meat) {
            return new FoodProperties.Builder().nutrition(nutrition).saturationMod(saturation).alwaysEat().meat().build();
        }else if (always) {
            return new FoodProperties.Builder().nutrition(nutrition).saturationMod(saturation).alwaysEat().build();
        } else if (fast) {
            return new FoodProperties.Builder().nutrition(nutrition).saturationMod(saturation).fast().build();
        } else if (meat) {
            return new FoodProperties.Builder().nutrition(nutrition).saturationMod(saturation).meat().build();
        } else {
            return new FoodProperties.Builder().nutrition(nutrition).saturationMod(saturation).build();
        }
    }
    default void addEatEffect(ItemStack pFood, Level pLevel, LivingEntity pLivingEntity) {
        Item item = pFood.getItem();
        if (item.isEdible() && item instanceof AntiFeedingFoodItem successor) {
            FoodProperties foodStats = successor.getWholeFoodStats(pFood);
            if (foodStats != null) {
                for(Pair<MobEffectInstance, Float> pair : foodStats.getEffects()) {
                    if (!pLevel.isClientSide && pair.getFirst() != null && pLevel.random.nextFloat() < pair.getSecond()) {
                        pLivingEntity.addEffect(new MobEffectInstance(pair.getFirst()));
                    }
                }
            }
        }

    }
    default void addUsedEffectTooltip(ItemStack itemIn, List<Component> lores, float durationFactor) {
        FoodProperties foodStats = this.getWholeFoodStats(itemIn);
        if (foodStats != null) {
            List<Pair<MobEffectInstance, Float>> effectList = foodStats.getEffects();
            List<Pair<Attribute, AttributeModifier>> attributeList = Lists.newArrayList();
            Iterator var6;
            Pair pair;
            MutableComponent iformattabletextcomponent;
            MobEffect effect;
            if (effectList.isEmpty()) {
                lores.add(Component.translatable("effect.none").withStyle(ChatFormatting.GRAY));
            } else {
                for(var6 = effectList.iterator(); var6.hasNext(); lores.add(iformattabletextcomponent.withStyle(effect.getCategory().getTooltipFormatting()))) {
                    pair = (Pair)var6.next();
                    MobEffectInstance instance = (MobEffectInstance)pair.getFirst();
                    iformattabletextcomponent = Component.translatable(instance.getDescriptionId());
                    effect = instance.getEffect();
                    Map<Attribute, AttributeModifier> attributeMap = effect.getAttributeModifiers();
                    if (!attributeMap.isEmpty()) {
                        Iterator var12 = attributeMap.entrySet().iterator();

                        while(var12.hasNext()) {
                            Map.Entry<Attribute, AttributeModifier> entry = (Map.Entry)var12.next();
                            AttributeModifier rawModifier = (AttributeModifier)entry.getValue();
                            AttributeModifier modifier = new AttributeModifier(rawModifier.getName(), effect.getAttributeModifierValue(instance.getAmplifier(), rawModifier), rawModifier.getOperation());
                            attributeList.add(new Pair((Attribute)entry.getKey(), modifier));
                        }
                    }

                    if (instance.getAmplifier() > 0) {
                        iformattabletextcomponent = Component.translatable("potion.withAmplifier", new Object[]{iformattabletextcomponent, Component.translatable("potion.potency." + instance.getAmplifier())});
                    }

                    if (instance.getDuration() > 20) {
                        iformattabletextcomponent = Component.translatable("potion.withDuration", new Object[]{iformattabletextcomponent, MobEffectUtil.formatDuration(instance, durationFactor)});
                    }
                }
            }

            if (!attributeList.isEmpty()) {
                lores.add(CommonComponents.EMPTY);
                lores.add(Component.translatable("potion.whenDrank").withStyle(ChatFormatting.DARK_PURPLE));
                var6 = attributeList.iterator();

                while(var6.hasNext()) {
                    pair = (Pair)var6.next();
                    AttributeModifier modifier = (AttributeModifier)pair.getSecond();
                    double amount = modifier.getAmount();
                    double formattedAmount;
                    if (modifier.getOperation() != AttributeModifier.Operation.MULTIPLY_BASE && modifier.getOperation() != AttributeModifier.Operation.MULTIPLY_TOTAL) {
                        formattedAmount = modifier.getAmount();
                    } else {
                        formattedAmount = modifier.getAmount() * 100.0;
                    }

                    if (amount > 0.0) {
                        lores.add(Component.translatable("attribute.modifier.plus." + modifier.getOperation().toValue(), new Object[]{ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(formattedAmount), Component.translatable(((Attribute)pair.getFirst()).getDescriptionId())}).withStyle(ChatFormatting.BLUE));
                    } else if (amount < 0.0) {
                        formattedAmount *= -1.0;
                        lores.add(Component.translatable("attribute.modifier.take." + modifier.getOperation().toValue(), new Object[]{ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(formattedAmount), Component.translatable(((Attribute)pair.getFirst()).getDescriptionId())}).withStyle(ChatFormatting.RED));
                    }
                }
            }

        }
    }
}
