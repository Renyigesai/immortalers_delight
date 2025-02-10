package com.renyigesai.immortalers_delight.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import vectorwing.farmersdelight.common.item.DrinkableItem;

public class DrinkItem extends DrinkableItem {

    public final ItemStack containerSlice;

    public DrinkItem(Properties properties, ItemStack containerSlice) {
        super(properties);
        this.containerSlice = containerSlice;
    }
    public DrinkItem(Properties properties, Item containerSlice) {
        super(properties);
        this.containerSlice = containerSlice.getDefaultInstance();
    }
    public DrinkItem(Item.Properties properties, ItemStack containerSlice,boolean hasFoodEffectTooltip) {
        super(properties, hasFoodEffectTooltip);
        this.containerSlice = containerSlice;
    }

    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity) {
        ItemStack itemstack = super.finishUsingItem(itemStack, level, livingEntity);
        return livingEntity instanceof Player && ((Player)livingEntity).getAbilities().instabuild ? itemstack : new ItemStack(containerSlice.getItem());
    }
}
