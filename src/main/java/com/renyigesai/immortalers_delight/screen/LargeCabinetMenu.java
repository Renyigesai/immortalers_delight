package com.renyigesai.immortalers_delight.screen;


import com.renyigesai.immortalers_delight.init.ImmortalersDelightMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class LargeCabinetMenu extends AbstractContainerMenu {
    public static final int COLUMNS = 12;
    public static final int ROWS = 9;
    public static final int TOTAL_CONTAINER_SLOTS = COLUMNS * ROWS; // 108 格

    private final Container container;

    // 客户端构造函数（由客户端打开 Menu 时反射调用）
    public LargeCabinetMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(TOTAL_CONTAINER_SLOTS));
    }

    public LargeCabinetMenu(int id, Inventory inventory, FriendlyByteBuf friendlyByteBuf) {
        this(id, inventory, new SimpleContainer(TOTAL_CONTAINER_SLOTS));
    }

    // 服务端构造函数
    public LargeCabinetMenu(int containerId, Inventory playerInventory, Container container) {
        super(ImmortalersDelightMenuTypes.LARGE_CABINET_MENU.get(), containerId);
        checkContainerSize(container, TOTAL_CONTAINER_SLOTS);
        this.container = container;
        container.startOpen(playerInventory.player);

        // 1. 添加大型橱柜的 108 个物品槽 (9 行 12 列)
        // GUI 内网格起始坐标为 x=8, y=11
        int containerStartX = 8;
        int containerStartY = 11;
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLUMNS; ++col) {
                int index = col + row * COLUMNS;
                int x = containerStartX + col * 18;
                int y = containerStartY + row * 18;
                this.addSlot(new Slot(container, index, x, y));
            }
        }

        // 2. 添加玩家主背包 (3 行 9 列)，水平居中对齐 (起始 x 设为 36)
        int playerInvStartX = 36;
        int playerInvStartY = 176; // 位于 9 行橱柜槽位下方
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                int index = col + row * 9 + 9;
                int x = playerInvStartX + col * 18;
                int y = playerInvStartY + row * 18;
                this.addSlot(new Slot(playerInventory, index, x, y));
            }
        }

        // 3. 添加玩家快捷栏 (1 行 9 列)
        int hotbarStartY = playerInvStartY + 58; // 176 + 58 = 234
        for (int col = 0; col < 9; ++col) {
            int x = playerInvStartX + col * 18;
            this.addSlot(new Slot(playerInventory, col, x, hotbarStartY));
        }
    }


    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);

        if (slot != null && slot.hasItem()) {
            ItemStack slotItem = slot.getItem();
            itemStack = slotItem.copy();

            if (slotIndex < TOTAL_CONTAINER_SLOTS) {
                // 从橱柜快速转移到玩家背包 (尝试转移至快捷栏与主背包)
                if (!this.moveItemStackTo(slotItem, TOTAL_CONTAINER_SLOTS, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // 从玩家背包快速转移进橱柜
                if (!this.moveItemStackTo(slotItem, 0, TOTAL_CONTAINER_SLOTS, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (slotItem.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemStack;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.container.stopOpen(player);
    }

    public Container getContainer() {
        return this.container;
    }
}
