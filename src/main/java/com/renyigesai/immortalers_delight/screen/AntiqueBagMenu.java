package com.renyigesai.immortalers_delight.screen;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightMenuTypes;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightTags;
import com.renyigesai.immortalers_delight.item.antique.AntiqueBagItem;
import com.renyigesai.immortalers_delight.screen.slot.AntiqueBagAntiqueSlot;
import com.renyigesai.immortalers_delight.screen.slot.AntiqueBagToolSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import java.util.Map;

public class AntiqueBagMenu extends AbstractContainerMenu {
    private final Player player;
    private final IItemHandler playerInventory;
    private final ItemStackHandler toolSlots;
    private final ItemStackHandler antiqueSlots;
    public static final Map<Integer,Integer> INDEX_MAP = Map.of(
            4,0,
            5,1,
            6,2,
            7,3
    );



    public AntiqueBagMenu(int windowId, Inventory playerInventory, ItemStackHandler toolSlots, ItemStackHandler antiqueSlots) {
        super(ImmortalersDelightMenuTypes.ANTIQUE_BAG_MENU.get(),windowId);
        this.player = playerInventory.player;
        this.playerInventory = new InvWrapper(playerInventory);
        this.toolSlots = toolSlots;
        this.antiqueSlots = antiqueSlots;

        //添加工具槽（0-2）
        addSlot(new AntiqueBagToolSlot(toolSlots,0,9,11));
        addSlot(new AntiqueBagToolSlot(toolSlots,1,27,32));
        addSlot(new AntiqueBagToolSlot(toolSlots,2,9,53));

        //添加古物槽（3-11）
        int x = 53;
        int y = 21;
        int slotSizeX = 25;
        for (int index = 0; index < Math.max(antiqueSlots.getSlots(),AntiqueBagItem.MAX_ANTIQUEL_SLOT - 1); index++) {
            int addX = index > 3 ? 12 : 0;
            int addY = index > 3 ? 22 : 0;
            int start = index > 3 ? INDEX_MAP.get(index) : index;
            addSlot(new AntiqueBagAntiqueSlot(antiqueSlots,index,x + (slotSizeX * start) + addX,y + addY,player));
        }

        //添加玩家物品栏
        layoutPlayerInventorySlots(8,84);
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

    public static AntiqueBagMenu create(int windowId, Inventory playerInventory, FriendlyByteBuf data) {
        ItemStack stack = data.readItem();
        if (stack.getItem() instanceof AntiqueBagItem antiqueBag){
            return new AntiqueBagMenu(windowId, playerInventory, antiqueBag.getToolSlot(stack),antiqueBag.getAntiqueSlot(stack));
        }
        throw new IllegalStateException("Item is not an AntiqueBagItem!");
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot == null || !slot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack stackInSlot = slot.getItem();
        result = stackInSlot.copy();
        final int toolStart = 0;
        final int toolEnd = 3;
        final int antiqueStart = toolEnd;
        final int antiqueEnd = antiqueStart + antiqueSlots.getSlots();
        final int playerStart = antiqueEnd;
        final int playerEnd = playerStart + 36;

        if (index < antiqueEnd) {
            if (!this.moveItemStackTo(stackInSlot, playerStart, playerEnd, true)) {
                return ItemStack.EMPTY;
            }
        }
        else {
            if (stackInSlot.is(ItemTags.TOOLS)) {
                if (!this.moveItemStackTo(stackInSlot, toolStart, toolEnd, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (stackInSlot.is(ImmortalersDelightTags.Items.IS_ANTIQUE)) {
                if (!this.moveItemStackTo(stackInSlot, antiqueStart, antiqueEnd, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (index < playerStart + 27) {
                    if (!this.moveItemStackTo(stackInSlot, playerStart + 27, playerEnd, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.moveItemStackTo(stackInSlot, playerStart, playerStart + 27, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }
        }

        if (stackInSlot.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        if (stackInSlot.getCount() == result.getCount()) {
            return ItemStack.EMPTY;
        }

        slot.onTake(player, stackInSlot);
        return result;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    public ItemStackHandler getAntiqueSlots() {
        return antiqueSlots;
    }

    public ItemStackHandler getToolSlots() {
        return toolSlots;
    }
}
