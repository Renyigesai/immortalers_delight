package com.renyigesai.immortalers_delight.item.weapon;

import com.renyigesai.immortalers_delight.client.renderer.special_item.LightsaberItemRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.TridentItem;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class SoulSwordItem extends TridentItem {
    public SoulSwordItem(Properties pProperties) {
        super(pProperties);
    }


    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return LightsaberItemRenderer.INSTANCE;
            }
        });
    }

}
