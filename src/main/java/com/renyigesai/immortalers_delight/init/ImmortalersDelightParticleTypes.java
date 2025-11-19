package com.renyigesai.immortalers_delight.init;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ImmortalersDelightParticleTypes {
    public static final DeferredRegister<ParticleType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, ImmortalersDelightMod.MODID);
    public static final RegistryObject<SimpleParticleType> KWAT = REGISTRY.register("kwat", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> SNIFFER_FUR = REGISTRY.register("sniffer_fur", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> STUN = REGISTRY.register("stun", () -> new SimpleParticleType(false));
}
