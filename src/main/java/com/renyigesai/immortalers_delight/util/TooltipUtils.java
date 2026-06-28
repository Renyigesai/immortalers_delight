package com.renyigesai.immortalers_delight.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import vectorwing.farmersdelight.common.utility.TextUtils;

public final class TooltipUtils {
    private TooltipUtils() {
    }

    /** Farmer's Delight {@code TextUtils} expects {@code tooltip.<path>}, not {@code tooltip.<namespace:path>}. */
    public static String farmersDelightTooltipKey(Item item) {
        return "tooltip." + BuiltInRegistries.ITEM.getKey(item).getPath();
    }

    public static String farmersDelightTooltipKey(Item item, String suffix) {
        return farmersDelightTooltipKey(item) + suffix;
    }

    public static MutableComponent farmersDelightTranslation(Item item, Object... args) {
        return TextUtils.getTranslation(farmersDelightTooltipKey(item), args);
    }

    public static MutableComponent farmersDelightTranslation(Item item, String suffix, Object... args) {
        return TextUtils.getTranslation(farmersDelightTooltipKey(item, suffix), args);
    }
}
