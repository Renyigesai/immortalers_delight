package com.renyigesai.immortalers_delight.screen;

import com.renyigesai.immortalers_delight.block.enchantal_cooler.EnchantalCoolerBlockEntity;
import com.renyigesai.immortalers_delight.block.soul_infuser.SoulInfuserBlockEntity;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class SoulInfuserMenu extends AbstractContainerMenu {
    private final SoulInfuserBlockEntity blockEntity;
    private final ContainerData data;

    public SoulInfuserMenu(int windowId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(windowId, playerInventory, (SoulInfuserBlockEntity) playerInventory.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(4));
    }

    public SoulInfuserMenu(int windowId, Inventory playerInventory, SoulInfuserBlockEntity blockEntity, ContainerData data) {
        super(ImmortalersDelightMenuTypes.SOUL_INFUSER_MENU.get(), windowId);
        this.blockEntity = blockEntity;
        this.data = data;
        ItemStackHandler inv = blockEntity.getInventory();

        // 1. 3x3 输入原料栏位 (Slots 0~8)
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 3; ++col) {
                this.addSlot(new SlotItemHandler(inv, col + row * 3, 29 + col * 24, 17 + row * 24));
            }
        }

        // 2. 左侧燃料栏位 (Slot 10)
        this.addSlot(new SlotItemHandler(inv, SoulInfuserBlockEntity.FUEL_SLOT, 8, 68) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return blockEntity.isFuel(stack);
            }
        });

        // 3. 右侧输出栏位 (Slot 9)
        this.addSlot(new SlotItemHandler(inv, SoulInfuserBlockEntity.OUTPUT_SLOT, 134, 41) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return false;
            }
        });

        // 4. 玩家物品栏 (176*176 布局适配：y 从 94 开始)
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 94 + i * 18));
            }
        }
        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 152));
        }

        addDataSlots(data);
    }

    public int getProgressScaled() {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);
        return maxProgress != 0 && progress != 0 ? progress * 128 / maxProgress : 0;
    }

    public int getChargeScaled() {
        int charge = this.data.get(2);
        int maxCharge = 64; // 最大安全充能
        return charge != 0 ? Math.min(32, charge * 32 / maxCharge) : 0;
    }

    public int getChargeAddScaled() {
        int charge = this.data.get(3);
        int tool = this.data.get(2);
        int maxCharge = tool > 48 ? 240 : 160; // 充燃料进度
        return charge != 0 ? Math.min(16, charge * 16 / maxCharge) : 0;
    }
    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack current = slot.getItem();
            itemstack = current.copy();

            if (index == SoulInfuserBlockEntity.OUTPUT_SLOT) { // 输出槽
                if (!this.moveItemStackTo(current, 11, 47, true)) return ItemStack.EMPTY;
                slot.onQuickCraft(current, itemstack);
            } else if (index < 11) { // 容器内部槽
                if (!this.moveItemStackTo(current, 11, 47, false)) return ItemStack.EMPTY;
            } else { // 玩家物品栏
                if (blockEntity.isFuel(current)) {
                    if (!this.moveItemStackTo(current, SoulInfuserBlockEntity.FUEL_SLOT, SoulInfuserBlockEntity.FUEL_SLOT + 1, false)) return ItemStack.EMPTY;
                } else if (!this.moveItemStackTo(current, 0, 9, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (current.isEmpty()) slot.set(ItemStack.EMPTY);
            else slot.setChanged();
            if (current.getCount() == itemstack.getCount()) return ItemStack.EMPTY;
            slot.onTake(player, current);
        }
        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return blockEntity.stillValid(player);
    }
}
