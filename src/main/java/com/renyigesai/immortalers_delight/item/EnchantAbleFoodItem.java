package com.renyigesai.immortalers_delight.item;


import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.world.feature.ModConfigureFeature;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import vectorwing.farmersdelight.common.item.ConsumableItem;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.List;

public class EnchantAbleFoodItem extends ConsumableItem{
    private final boolean foil;
    private int tooltipColorId;

    public EnchantAbleFoodItem(Properties properties, boolean hasFoodEffectTooltip, boolean hasCustomTooltip) {
        super(properties, hasFoodEffectTooltip, hasCustomTooltip);
        foil = false;
        tooltipColorId = 9;
    }
    public EnchantAbleFoodItem(Properties properties, boolean hasFoodEffectTooltip, boolean hasCustomTooltip, boolean isFoil) {
        super(properties, hasFoodEffectTooltip, hasCustomTooltip);
        foil = isFoil;
        tooltipColorId = 9;
    }
    public EnchantAbleFoodItem(Properties properties, boolean hasFoodEffectTooltip, boolean hasCustomTooltip, int tooltipColorId) {
        super(properties, hasFoodEffectTooltip, hasCustomTooltip);
        foil = false;
        this.tooltipColorId = tooltipColorId;
    }
    public EnchantAbleFoodItem(Properties properties, boolean hasFoodEffectTooltip, boolean hasCustomTooltip, int tooltipColorId, boolean isFoil) {
        super(properties, hasFoodEffectTooltip, hasCustomTooltip);
        foil = isFoil;
        this.tooltipColorId = tooltipColorId;
    }

    public boolean isFoil(ItemStack p_41172_) {
        return foil;
    }

    @Override
    public void appendHoverText(ItemStack stack, @javax.annotation.Nullable Level level, List<Component> tooltip, TooltipFlag isAdvanced) {
        if (this.tooltipColorId != 9) {
            MutableComponent textEmpty = Component.translatable("tooltip." + ImmortalersDelightMod.MODID+ ".colorful." + this);
            ChatFormatting color = ChatFormatting.getById(this.tooltipColorId);
            if (color != null) {tooltip.add(textEmpty.withStyle(color));}
        }
        super.appendHoverText(stack, level, tooltip, isAdvanced);
    }
}
