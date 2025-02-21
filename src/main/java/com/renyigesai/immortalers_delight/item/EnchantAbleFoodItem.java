package com.renyigesai.immortalers_delight.item;


import net.minecraft.world.item.ItemStack;
import vectorwing.farmersdelight.common.item.ConsumableItem;

public class EnchantAbleFoodItem extends ConsumableItem{
    private final boolean foil;

    public EnchantAbleFoodItem(Properties properties, boolean hasFoodEffectTooltip, boolean hasCustomTooltip) {
        super(properties, hasFoodEffectTooltip, hasCustomTooltip);
        foil = false;
    }
    public EnchantAbleFoodItem(Properties properties, boolean hasFoodEffectTooltip, boolean hasCustomTooltip, boolean isFoil) {
        super(properties, hasFoodEffectTooltip, hasCustomTooltip);
        foil = isFoil;
    }

    public boolean isFoil(ItemStack p_41172_) {
        return foil;
    }

}
