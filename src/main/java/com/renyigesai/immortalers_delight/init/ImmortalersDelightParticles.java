package com.renyigesai.immortalers_delight.init;
import net.neoforged.fml.common.EventBusSubscriber;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.client.particle.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;

@EventBusSubscriber(modid = ImmortalersDelightMod.MODID, value = Dist.CLIENT)
public class ImmortalersDelightParticles {
    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ImmortalersDelightParticleTypes.KWAT.get(), KwatParticle::provider);
        event.registerSpriteSet(ImmortalersDelightParticleTypes.SNIFFER_FUR.get(), SnifferFurParticle::provider);
        event.registerSpriteSet(ImmortalersDelightParticleTypes.STUN.get(), StunParticle::provider);
        event.registerSpriteSet(ImmortalersDelightParticleTypes.HUGE_SMOKE.get(), HugeSmokeParticle::baseSmokeProvider);
        event.registerSpriteSet(ImmortalersDelightParticleTypes.GAS_SMOKE.get(), GasSmokeParticle::gasSmokeProvider);
        event.registerSpriteSet(ImmortalersDelightParticleTypes.SHOCK_WAVE.get(),ShockWaveParticle::shockWaveParticleProvider);
        event.registerSpriteSet(ImmortalersDelightParticleTypes.SPIRAL_SOUL.get(), SpiralSoulParticle::spiralSoulParticleProvider);
        event.registerSpriteSet(ImmortalersDelightParticleTypes.SNOW_FOG.get(), SnowFogParticle::snowFogParticleProvider);
        event.registerSpriteSet(ImmortalersDelightParticleTypes.MOONLIGHT_BEAM.get(), MoonlightBeamParticle::moonlightBeamParticleProvider);
    }

}
