package com.renyigesai.immortalers_delight.screen.slot;

import com.renyigesai.immortalers_delight.api.event.AntiqueBagEvent;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightTags;
import com.renyigesai.immortalers_delight.item.antique.IAntiqueItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class AntiqueBagAntiqueSlot extends SlotItemHandler {

    private final Player player;

    public AntiqueBagAntiqueSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, Player player) {
        super(itemHandler, index, xPosition, yPosition);
        this.player = player;
    }

    @Override
    public void setByPlayer(ItemStack newStack) {
        ItemStack old = getItem();
        super.setByPlayer(newStack);
        if (!newStack.isEmpty() && newStack.is(ImmortalersDelightTags.Items.IS_ANTIQUE)){
            AntiqueBagEvent.Place antiqueBagPlaceEvent = new AntiqueBagEvent.Place(this.player, newStack);
            if (!MinecraftForge.EVENT_BUS.post(antiqueBagPlaceEvent)){
                if (newStack.getItem() instanceof IAntiqueItem antiqueItem){
                    antiqueItem.onPlace(this.player,this.player.level());
                }

            }
        }
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        if (stack.is(ImmortalersDelightTags.Items.IS_ANTIQUE)){
            return super.mayPlace(stack);
        }
        return false;
    }

    @Override
    public void onTake(Player player, ItemStack stack) {
        super.onTake(player, stack);
        if (!stack.isEmpty() && stack.is(ImmortalersDelightTags.Items.IS_ANTIQUE)){
            AntiqueBagEvent.Take antiqueBagTakEvent = new AntiqueBagEvent.Take(this.player, stack);
            if (!MinecraftForge.EVENT_BUS.post(antiqueBagTakEvent)){
                if (stack.getItem() instanceof IAntiqueItem antiqueItem){
                    antiqueItem.onTake(this.player,this.player.level());
                }

            }
        }
    }

    public Player getPlayer() {
        return player;
    }

}
