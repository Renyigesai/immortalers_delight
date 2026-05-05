package com.renyigesai.immortalers_delight.util;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightTags;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;
import vectorwing.farmersdelight.common.tag.ModTags;

public class ItemUtils {
    public static void givePlayerItem(Player player, ItemStack item){
        player.getInventory().placeItemBackInInventory(item);
    }

    public static void spawnItemEntity(Level level, ItemStack stack, double x, double y, double z, double xMotion, double yMotion, double zMotion) {
        ItemEntity entity = new ItemEntity(level, x, y, z, stack);
        entity.setDeltaMovement(xMotion, yMotion, zMotion);
        level.addFreshEntity(entity);
    }

    public static boolean isKnives(ItemStack handStack){
        return vectorwing.farmersdelight.common.utility.ItemUtils.isKnife(handStack) || handStack.is(ImmortalersDelightTags.KNIVES);
    }

    public static boolean isInventoryEmpty(IItemHandler inventory) {
        for(int i = 0; i < inventory.getSlots(); ++i) {
            if (!inventory.getStackInSlot(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
