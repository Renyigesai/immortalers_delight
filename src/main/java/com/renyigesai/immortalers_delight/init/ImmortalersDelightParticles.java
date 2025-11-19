package com.renyigesai.immortalers_delight.init;

import com.renyigesai.immortalers_delight.client.particle.KwatParticle;
import com.renyigesai.immortalers_delight.client.particle.SnifferFurParticle;
import com.renyigesai.immortalers_delight.client.particle.StunParticle;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ImmortalersDelightParticles {
    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ImmortalersDelightParticleTypes.KWAT.get(), KwatParticle::provider);
        event.registerSpriteSet(ImmortalersDelightParticleTypes.SNIFFER_FUR.get(), SnifferFurParticle::provider);
        event.registerSpriteSet(ImmortalersDelightParticleTypes.STUN.get(), StunParticle::provider);
    }

}
