package com.renyigesai.immortalers_delight.client;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = ImmortalersDelightMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventHelper {
//    @SubscribeEvent
//    public void onPostRenderLiving(RenderLivingEvent.Post event) {
//        LivingEntity entity = event.getEntity();
//
//        if(data!=null&&data.getFrozen()){
//            IceRenderer.render(event.getEntity(), event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight(), data.getFrozenTick());
//        }
//    }
}
