package com.renyigesai.immortalers_delight.item.food;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.api.AntiFeedingFoodItem;
import com.renyigesai.immortalers_delight.item.DrinkItem;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.utility.TextUtils;

import javax.annotation.Nullable;
import java.util.List;

public class SuperKwatBurgerItem extends EdibleBlockFoodItem {
    public SuperKwatBurgerItem(Block pBlock, Properties pProperties, FoodProperties poweredFoodProperties, boolean hasCustomTooltip) {
        super(pBlock, pProperties,poweredFoodProperties,hasCustomTooltip);
    }

    public SuperKwatBurgerItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    protected void addAheadFoodEffect(ItemStack stack, Level level, LivingEntity livingEntity) {
        if (!level.isClientSide() && livingEntity != null) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 80));
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity consumer, int timeLeft) {
        if (!level.isClientSide() && timeLeft + 1 <= this.getUseDuration(stack) / 2) {
            this.finishUsingItem(stack, level, consumer);
        }
        super.releaseUsing(stack, level, consumer, timeLeft);
    }
    @Override
    public void appendHoverText(ItemStack stack, @javax.annotation.Nullable Level level, List<Component> tooltip, TooltipFlag isAdvanced) {
        if (Configuration.FOOD_EFFECT_TOOLTIP.get()) {
            MutableComponent textValue = Component.translatable("tooltip." + ImmortalersDelightMod.MODID+ ".super_kwat_burger");
            tooltip.add(textValue.withStyle(ChatFormatting.GRAY));
        }
        super.appendHoverText(stack, level, tooltip, isAdvanced);
    }
    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        //判断在使用物品
        if (pRemainingUseDuration >= 0 && !pLivingEntity.level().isClientSide()) {
            if (pRemainingUseDuration + 1 <= this.getUseDuration(pStack) / 2) {
                if (pRemainingUseDuration % 40 == 0) addAheadFoodEffect(pStack, pLivingEntity.level(), pLivingEntity);
            }
        }
        //结束使用物品
        if (pRemainingUseDuration == 1) {
            this.addEatEffect(pStack, pLevel, pLivingEntity);
        }
    }
}
