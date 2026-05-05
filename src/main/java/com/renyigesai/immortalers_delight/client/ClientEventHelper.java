package com.renyigesai.immortalers_delight.client;
import net.neoforged.fml.common.EventBusSubscriber;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.client.renderer.special_item.ItemTESRenderer;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = ImmortalersDelightMod.MODID, value = Dist.CLIENT)
public class ClientEventHelper {

    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        IClientItemExtensions specialItemRenderer = new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return new ItemTESRenderer(
                        Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                        Minecraft.getInstance().getEntityModels());
            }
        };
        event.registerItem(
                specialItemRenderer,
                ImmortalersDelightItems.LARGE_COLUMN.get(),
                ImmortalersDelightItems.JENG_NANU.get(),
                ImmortalersDelightItems.BONE_KNIFE.get());
    }

//    @SubscribeEvent
//    public void onPostRenderLiving(RenderLivingEvent.Post event) {
//        LivingEntity entity = event.getEntity();
//
//        if(data!=null&&data.getFrozen()){
//            IceRenderer.render(event.getEntity(), event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight(), data.getFrozenTick());
//        }
//    }
}
