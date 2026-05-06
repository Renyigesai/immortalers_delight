package com.renyigesai.immortalers_delight.init;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.block.enchantal_cooler.EnchantalCoolerBlockEntity;
import com.renyigesai.immortalers_delight.block.tangyuan.TangyuanBlockEntity;
import com.renyigesai.immortalers_delight.entities.living.TerracottaGolem;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.wrapper.InvWrapper;

@EventBusSubscriber(modid = ImmortalersDelightMod.MODID)
public final class ImmortalersDelightCapabilities {
    private ImmortalersDelightCapabilities() {}

    @SubscribeEvent
    public static void register(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ImmortalersDelightBlocks.ENCHANTAL_COOLER_ENTITY.get(),
                (blockEntity, side) -> blockEntity instanceof EnchantalCoolerBlockEntity cooler ? cooler.getAutomationHandler(side) : null);
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ImmortalersDelightBlocks.UNFINISHED_TANGYUAN_ENTITY.get(),
                (blockEntity, side) -> blockEntity instanceof TangyuanBlockEntity tangyuan ? tangyuan.getAutomationHandler(side) : null);
        event.registerEntity(
                Capabilities.ItemHandler.ENTITY,
                ImmortalersDelightEntities.TERRACOTTA_GOLEM.get(),
                (entity, ctx) -> entity instanceof TerracottaGolem golem && golem.isAlive()
                        ? new InvWrapper(golem.getInventoryContainer())
                        : null);
    }
}
