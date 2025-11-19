package com.renyigesai.immortalers_delight.screen;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.level.block.Blocks;

public class GrindstoneWithNoBlockMenu extends GrindstoneMenu {
    public GrindstoneWithNoBlockMenu(int pContainerId, Inventory pPlayerInventory) {
        super(pContainerId, pPlayerInventory);
    }

    public GrindstoneWithNoBlockMenu(int pContainerId, Inventory pPlayerInventory, ContainerLevelAccess pAccess) {
        super(pContainerId, pPlayerInventory, pAccess);
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }
}
