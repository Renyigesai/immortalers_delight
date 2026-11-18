package com.renyigesai.immortalers_delight.item.antique;

import com.renyigesai.immortalers_delight.api.IPlayerLastAntiqueSlot;
import com.renyigesai.immortalers_delight.api.event.AntiqueBagTickEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class AntiqueBagHandler {

    @SubscribeEvent
    public static void onTick(TickEvent.PlayerTickEvent event){
        if (event.phase != TickEvent.Phase.END){
            return;
        }
        Player player = event.player;
        if (player.level().isClientSide){
            return;
        }
        onAntiqueBagTick(player);
    }

    private static void onAntiqueBagTick(Player player){
        BagRecord oldRecord = ((IPlayerLastAntiqueSlot) player).getLastAntiqueSlot();
        if (oldRecord != null){
            ItemStack inSlot = getRealBag(oldRecord, player);
            boolean stillBag = !inSlot.isEmpty() && inSlot.getItem() instanceof AntiqueBagItem;
            if (stillBag){
                if (inSlot.getItem() instanceof AntiqueBagItem bagItem) {
                    player.getAttributes().removeAttributeModifiers(bagItem.getAllAttributeModifier(inSlot));
                    player.getAttributes().addTransientAttributeModifiers(bagItem.getAllAttributeModifier(inSlot));
                }
            }else {
                if (oldRecord.stack.getItem() instanceof AntiqueBagItem bagItem) {
                    player.getAttributes().removeAttributeModifiers(bagItem.getAllAttributeModifier(oldRecord.stack));
                }
            }
        }
        BagRecord bagRecord = getOptimalBag(player);
        ((IPlayerLastAntiqueSlot)player).setLastAntiqueSlot(bagRecord);
        if (bagRecord != null){
            ItemStack realBag = getRealBag(bagRecord, player);
            if (!realBag.isEmpty() && realBag.getItem() instanceof AntiqueBagItem bagItem) {
                AntiqueBagTickEvent antiqueBagTickEvent = new AntiqueBagTickEvent(TickEvent.Phase.START, player, bagRecord);
                MinecraftForge.EVENT_BUS.post(antiqueBagTickEvent);
                bagItem.onAllAntiqueTick(realBag, player, player.level());
            }
        }
    }

    private static BagRecord getOptimalBag(Player player){
        BagRecord bag = null;
        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
            ItemStack item = player.getInventory().getItem(slot);
            if (item.getItem() instanceof AntiqueBagItem bagItem){
                if (bag == null){
                    bag = new BagRecord(item.copy(), slot, false);
                }else {
                    int level = bagItem.getLevel();
                    if (level > ((AntiqueBagItem)bag.stack.getItem()).getLevel() || slot < bag.slot){
                        bag = new BagRecord(item.copy(), slot, false);
                    }
                }
            }
        }
        if (bag != null){
            return bag;
        }

        PlayerEnderChestContainer enderChest = player.getEnderChestInventory();
        for (int slot = 0; slot < enderChest.getContainerSize(); slot++) {
            ItemStack item = enderChest.getItem(slot);
            if (item.getItem() instanceof AntiqueBagItem bagItem){
                if (bag == null){
                    bag = new BagRecord(item.copy(), slot, true);
                }else {
                    int level = bagItem.getLevel();
                    if (level > ((AntiqueBagItem)bag.stack.getItem()).getLevel() || slot < bag.slot){
                        bag = new BagRecord(item.copy(), slot, true);
                    }
                }
            }
        }
        return bag;
    }

    public static ItemStack getRealBag(BagRecord bagRecord,Player player) {
        if (bagRecord == null) {
            return ItemStack.EMPTY;
        }
        if (bagRecord.isEnderChest()) {
            PlayerEnderChestContainer enderChest = player.getEnderChestInventory();
            if (bagRecord.slot < 0 || bagRecord.slot >= enderChest.getContainerSize()) {
                return ItemStack.EMPTY;
            }
            return enderChest.getItem(bagRecord.slot);
        } else {
            if (bagRecord.slot < 0 || bagRecord.slot >= player.getInventory().getContainerSize()) {
                return ItemStack.EMPTY;
            }
            return player.getInventory().getItem(bagRecord.slot);
        }
    }

    public record BagRecord(ItemStack stack, int slot,boolean isEnderChest) {

    }

}
