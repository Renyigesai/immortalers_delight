package com.renyigesai.immortalers_delight.screen;

import com.renyigesai.immortalers_delight.block.enchantal_cooler.EnchantalCoolerBlockEntity;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;

public class EnchantalCoolerMenu extends AbstractContainerMenu {

    private final EnchantalCoolerBlockEntity blockEntity;
    private final Player player;
    private final IItemHandler playerInventory;

    public EnchantalCoolerMenu(int windowId, Inventory playerInventory, EnchantalCoolerBlockEntity blockEntity) {
        super(ImmortalersDelightMenuTypes.ENCHANTAL_COOLER_MENU.get(), windowId);
        this.blockEntity = blockEntity;
        ItemStackHandler inventory = blockEntity.getInventory();
        this.player = playerInventory.player;
        this.playerInventory = new InvWrapper(playerInventory);

        // 添加输入槽 (0-3)
        addSlot(new SlotItemHandler(inventory, 0, 52, 17));
        addSlot(new SlotItemHandler(inventory, 1, 70, 17));
        addSlot(new SlotItemHandler(inventory, 2, 52, 35));
        addSlot(new SlotItemHandler(inventory, 3, 70, 35));
        // 添加容器槽 (4)
        addSlot(new SlotItemHandler(inventory, blockEntity.CONTAINER_SLOT, 137, 55));
        // 添加输出槽 (5)
        addSlot(new SlotItemHandler(inventory, blockEntity.OUTPUT_SLOT, 137, 25){
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return false;
            }
        });
        // 添加燃料槽 (6)
        addSlot(new SlotItemHandler(inventory, blockEntity.FUEL_SLOT, 24, 55){
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return blockEntity.isFuel(stack);
            }
        });
        // 添加玩家物品栏
        layoutPlayerInventorySlots(8, 84);
    }

    public static EnchantalCoolerMenu create(int windowId, Inventory playerInventory, FriendlyByteBuf data) {
        BlockPos pos = data.readBlockPos();
        BlockEntity blockEntity = playerInventory.player.level().getBlockEntity(pos);
        if (blockEntity instanceof EnchantalCoolerBlockEntity) {
            return new EnchantalCoolerMenu(windowId, playerInventory, (EnchantalCoolerBlockEntity) blockEntity);
        }
        throw new IllegalStateException("Block entity is not an EnchantalCoolerBlockEntity!");
    }

    private void layoutPlayerInventorySlots(int leftCol, int topRow) {
        // 玩家物品栏
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        // 玩家快捷栏
        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }

    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    private int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        ItemStack originalStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);

        if (slot != null && slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            originalStack = stackInSlot.copy();
            if (slotIndex < 7) {
                if (!this.moveItemStackTo(stackInSlot, 7, 43, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else{
                if (blockEntity.isFuel(stackInSlot)){
                    if (!this.moveItemStackTo(stackInSlot, 6, 7, false)) {
                        return ItemStack.EMPTY;
                    }
                }else {
                    if (!this.moveItemStackTo(stackInSlot, 0, 6, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                if (!this.moveItemStackTo(stackInSlot, 0, 6, false)) {
                    return ItemStack.EMPTY;
                }
            }
            if (stackInSlot.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (stackInSlot.getCount() == originalStack.getCount()) {
                return ItemStack.EMPTY;
            }
        }
        return originalStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return blockEntity.stillValid(player);
    }

    public EnchantalCoolerBlockEntity getBlockEntity() {
        return blockEntity;
    }
}