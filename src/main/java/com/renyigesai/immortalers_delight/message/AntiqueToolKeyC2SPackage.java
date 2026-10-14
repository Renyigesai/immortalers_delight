package com.renyigesai.immortalers_delight.message;

import com.renyigesai.immortalers_delight.api.IPlayerLastAntiqueSlot;
import com.renyigesai.immortalers_delight.item.antique.AntiqueBagHandler;
import com.renyigesai.immortalers_delight.item.antique.AntiqueBagItem;
import com.renyigesai.immortalers_delight.util.ItemUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class AntiqueToolKeyC2SPackage {

    private final int type;
    public AntiqueToolKeyC2SPackage(int type){
        this.type = type;
    }

    public static void write(AntiqueToolKeyC2SPackage message, FriendlyByteBuf buffer){
        buffer.writeInt(message.type);
    }
    public static AntiqueToolKeyC2SPackage read(FriendlyByteBuf buffer){
        return new AntiqueToolKeyC2SPackage(buffer.readInt());
    }

    public static void handle(AntiqueToolKeyC2SPackage message, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        Player entity = context.getSender();
        int type = message.type;
        if (entity != null) {
            context.enqueueWork(() -> handle(entity, type));
        }
        context.setPacketHandled(true);
    }

    public static void handle(Player player,int type){
        AntiqueBagHandler.BagRecord lastAntiqueSlot = ((IPlayerLastAntiqueSlot) player).getLastAntiqueSlot();
        if (lastAntiqueSlot == null){
            return;
        }
        ItemStack stack = AntiqueBagHandler.getRealBag(lastAntiqueSlot,player);
        if (!stack.isEmpty() && stack.getItem() instanceof AntiqueBagItem antiqueBagItem){
            ItemStackHandler toolSlot = antiqueBagItem.getToolSlot(stack);
            ItemStack tool = toolSlot.getStackInSlot(type).copy();
            ItemUtils.givePlayerItem(player,tool);
            toolSlot.setStackInSlot(type,ItemStack.EMPTY);
        }
    }

    public int getType(){
        return type;
    }
}
