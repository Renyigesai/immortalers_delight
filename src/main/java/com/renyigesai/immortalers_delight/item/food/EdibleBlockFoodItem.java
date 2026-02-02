package com.renyigesai.immortalers_delight.item.food;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.api.AntiFeedingFoodItem;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.item.DrinkItem;
import com.renyigesai.immortalers_delight.potion.immortaleffects.FreezeEffect;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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
    public int getUseDuration(@NotNull ItemStack pStack) {return DifficultyModeUtil.isPowerBattleMode() ? 150 : 300;}
    protected void addAheadFoodEffect(ItemStack stack, Level level, LivingEntity livingEntity) {

    }
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag isAdvanced) {
        tooltip.add(Component.translatable("farmersdelight.tooltip.drink_block_item").withStyle(ChatFormatting.GRAY));
        if (Configuration.FOOD_EFFECT_TOOLTIP.get()) {
           if (this.hasCustomToolTip) {
                MutableComponent textEmpty = TextUtils.getTranslation("tooltip." + this, new Object[0]);
                tooltip.add(textEmpty.withStyle(ChatFormatting.BLUE));
            }
            if (this.hasPotionEffectTooltip) {
                this.addUsedEffectTooltip(stack, tooltip,1.0f);
            }
        }
    }

    @Nullable
    @Override
    public FoodProperties getWholeFoodStats(ItemStack itemIn) {
        return DifficultyModeUtil.isPowerBattleMode() ? this.poweredFoodProperties : super.getFoodProperties();
    }
    @Nullable
    protected FoodProperties noEffectFoodProperties = null;

    @Override
    public FoodProperties getFoodProperties() {
        if (this.noEffectFoodProperties != null) {
            return this.noEffectFoodProperties;
        } else if (super.getFoodProperties() != null) {
            this.noEffectFoodProperties = this.notEffectFood(super.getFoodProperties());
            return this.noEffectFoodProperties;
        }
        return super.getFoodProperties();
    }
}
