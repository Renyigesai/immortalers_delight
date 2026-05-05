package com.renyigesai.immortalers_delight.item.food;

import com.renyigesai.immortalers_delight.api.AntiFeedingFoodItem;
import com.renyigesai.immortalers_delight.item.DrinkItem;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.utility.TextUtils;

import javax.annotation.Nullable;
import java.util.List;

public class EdibleBlockFoodItem extends DrinkItem implements AntiFeedingFoodItem {
    @Nullable
    protected final FoodProperties poweredFoodProperties;

    public EdibleBlockFoodItem(Block pBlock, Properties pProperties, boolean hasPotionEffectTooltip, boolean hasCustomToolTip) {
        super(pBlock, pProperties, hasPotionEffectTooltip, hasCustomToolTip);
        this.poweredFoodProperties = null;
    }

    public EdibleBlockFoodItem(Block pBlock, Properties pProperties, FoodProperties poweredFoodProperties, boolean hasCustomTooltip) {
        super(pBlock, pProperties, true, hasCustomTooltip);
        this.poweredFoodProperties = poweredFoodProperties;
    }

    public EdibleBlockFoodItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
        this.poweredFoodProperties = null;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.EAT;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack pStack, LivingEntity entity) {
        return DifficultyModeUtil.isPowerBattleMode() ? 150 : 300;
    }

    protected void addAheadFoodEffect(ItemStack stack, Level level, LivingEntity livingEntity) {

    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag isAdvanced) {
        tooltip.add(Component.translatable("farmersdelight.tooltip.drink_block_item").withStyle(ChatFormatting.GRAY));
        if (Configuration.ENABLE_FOOD_EFFECT_TOOLTIP.get()) {
            if (this.hasCustomToolTip) {
                MutableComponent textEmpty = TextUtils.getTranslation("tooltip." + this, new Object[0]);
                tooltip.add(textEmpty.withStyle(ChatFormatting.BLUE));
            }
            if (this.hasPotionEffectTooltip) {
                this.addUsedEffectTooltip(stack, tooltip, 1.0f);
            }
        }
    }

    @Nullable
    @Override
    public FoodProperties getWholeFoodStats(ItemStack itemIn) {
        if (DifficultyModeUtil.isPowerBattleMode() && this.poweredFoodProperties != null) {
            return this.poweredFoodProperties;
        }
        return super.getFoodProperties(itemIn, null);
    }

    @Nullable
    protected FoodProperties noEffectFoodProperties = null;

    @Override
    public FoodProperties getFoodProperties(ItemStack stack, LivingEntity entity) {
        FoodProperties full = super.getFoodProperties(stack, entity);
        if (full == null) {
            return null;
        }
        if (this.noEffectFoodProperties == null) {
            this.noEffectFoodProperties = this.notEffectFood(full);
        }
        return this.noEffectFoodProperties;
    }
}
