package com.renyigesai.immortalers_delight.screen.slot;

import com.renyigesai.immortalers_delight.item.antique.IAntiqueItem;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class AntiqueBagToolSlot extends SlotItemHandler {
    public AntiqueBagToolSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        if (stack.is(ItemTags.TOOLS)){
            return super.mayPlace(stack);
        }
        return false;
    }
}
