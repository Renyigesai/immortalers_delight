package com.renyigesai.immortalers_delight.util;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public final class IllagerRaidEnchantmentHelper {

    private IllagerRaidEnchantmentHelper() {
    }

    /** Same pattern as vanilla pillager raid weapon rolls: {@link Raid#getEnchantOdds()} then enchanting-table style roll. */
    public static void enchantEquipmentSlotIfRaid(ServerLevel level, Raider raider, EquipmentSlot slot, int wave) {
        Raid raid = raider.getCurrentRaid();
        if (raid == null || !(raider.getRandom().nextFloat() <= raid.getEnchantOdds())) {
            return;
        }
        ItemStack stack = raider.getItemBySlot(slot);
        if (stack.isEmpty()) {
            return;
        }
        EnchantmentHelper.enchantItem(
                level.random,
                stack,
                (int) (5.0F * wave),
                level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).listElements().map(h -> (Holder<Enchantment>) h));
        raider.setItemSlot(slot, stack);
    }
}
