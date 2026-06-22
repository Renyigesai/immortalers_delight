package com.renyigesai.immortalers_delight.client;

import com.renyigesai.immortalers_delight.client.renderer.special_item.ItemTESRenderer;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

public final class ClientEventHelper {

    private static final IClientItemExtensions SPECIAL_ITEM_RENDERER = new IClientItemExtensions() {
        @Override
        public BlockEntityWithoutLevelRenderer getCustomRenderer() {
            return ItemTESRenderer.getInstance();
        }
    };

    private ClientEventHelper() {
    }

    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(
                SPECIAL_ITEM_RENDERER,
                ImmortalersDelightItems.LARGE_COLUMN.get(),
                ImmortalersDelightItems.JENG_NANU.get(),
                ImmortalersDelightItems.BONE_KNIFE.get());
    }
}
