package com.renyigesai.immortalers_delight.init;



import com.mojang.serialization.MapCodec;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;

import com.renyigesai.immortalers_delight.client.particle.ShockWaveParticleOption;

import com.renyigesai.immortalers_delight.client.particle.SnowFogParticleOption;

import com.renyigesai.immortalers_delight.client.particle.SpiralSoulParticleOption;

import net.minecraft.core.particles.ParticleType;

import net.minecraft.core.particles.SimpleParticleType;

import net.minecraft.core.registries.Registries;

import net.minecraft.network.RegistryFriendlyByteBuf;

import net.minecraft.network.codec.StreamCodec;

import net.neoforged.neoforge.registries.DeferredHolder;

import net.neoforged.neoforge.registries.DeferredRegister;



public class ImmortalersDelightParticleTypes {

    public static final DeferredRegister<ParticleType<?>> REGISTRY = DeferredRegister.create(Registries.PARTICLE_TYPE, ImmortalersDelightMod.MODID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> KWAT = REGISTRY.register("kwat", () -> new SimpleParticleType(false));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SNIFFER_FUR = REGISTRY.register("sniffer_fur", () -> new SimpleParticleType(false));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> STUN = REGISTRY.register("stun", () -> new SimpleParticleType(false));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> HUGE_SMOKE = REGISTRY.register("huge_smoke", () -> new SimpleParticleType(false));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GAS_SMOKE = REGISTRY.register("gas_smoke", () -> new SimpleParticleType(false));

    public static final DeferredHolder<ParticleType<?>, ParticleType<ShockWaveParticleOption>> SHOCK_WAVE =

            REGISTRY.register("shock_wave",

                    () -> new ParticleType<>(false) {

                        @Override

                        public MapCodec<ShockWaveParticleOption> codec() {

                            return ShockWaveParticleOption.CODEC;

                        }



                        @Override

                        public StreamCodec<? super RegistryFriendlyByteBuf, ShockWaveParticleOption> streamCodec() {

                            return ShockWaveParticleOption.STREAM_CODEC;

                        }

                    }

            );

    public static final DeferredHolder<ParticleType<?>, ParticleType<SpiralSoulParticleOption>> SPIRAL_SOUL =

            REGISTRY.register("spiral_soul",

                    () -> new ParticleType<>(false) {

                        @Override

                        public MapCodec<SpiralSoulParticleOption> codec() {

                            return SpiralSoulParticleOption.CODEC;

                        }



                        @Override

                        public StreamCodec<? super RegistryFriendlyByteBuf, SpiralSoulParticleOption> streamCodec() {

                            return SpiralSoulParticleOption.STREAM_CODEC;

                        }

                    }

            );

    public static final DeferredHolder<ParticleType<?>, ParticleType<SnowFogParticleOption>> SNOW_FOG =

            REGISTRY.register("snow_fog",

                    () -> new ParticleType<>(false) {

                        @Override

                        public MapCodec<SnowFogParticleOption> codec() {

                            return SnowFogParticleOption.CODEC;

                        }



                        @Override

                        public StreamCodec<? super RegistryFriendlyByteBuf, SnowFogParticleOption> streamCodec() {

                            return SnowFogParticleOption.STREAM_CODEC;

                        }

                    }

            );

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MOONLIGHT_BEAM = REGISTRY.register("moonlight_beam", () -> new SimpleParticleType(false));

}

