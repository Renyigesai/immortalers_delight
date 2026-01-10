package com.renyigesai.immortalers_delight.init;

import com.mojang.serialization.Codec;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.client.particle.ShockWaveParticleOption;
import com.renyigesai.immortalers_delight.client.particle.SnowFogParticleOption;
import com.renyigesai.immortalers_delight.client.particle.SpiralSoulParticleOption;
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
    public static final RegistryObject<SimpleParticleType> HUGE_SMOKE = REGISTRY.register("huge_smoke", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> GAS_SMOKE = REGISTRY.register("gas_smoke", () -> new SimpleParticleType(false));
    // 2. 注册带参数的粒子类型：ShockWaveParticle
    // 第一个参数：粒子ID（yourmod:extra_size）
    // 第二个参数：是否为“永远显示”（false=按常规视距渲染）
    // 第三个参数：关联ParticleOption的反序列化器
    public static final RegistryObject<ParticleType<ShockWaveParticleOption>> SHOCK_WAVE =
            REGISTRY.register("shock_wave",
                    () -> new ParticleType<>(false, ShockWaveParticleOption.DESERIALIZER) {
                        @Override
                        public Codec<ShockWaveParticleOption> codec() {
                            return ShockWaveParticleOption.CODEC;
                        }
                    }
            );
    public static final RegistryObject<ParticleType<SpiralSoulParticleOption>> SPIRAL_SOUL =
            REGISTRY.register("spiral_soul",
                    () -> new ParticleType<>(false, SpiralSoulParticleOption.DESERIALIZER) {
                        @Override
                        public Codec<SpiralSoulParticleOption> codec() {
                            return SpiralSoulParticleOption.CODEC;
                        }
                    }
            );
    public static final RegistryObject<ParticleType<SnowFogParticleOption>> SNOW_FOG =
            REGISTRY.register("snow_fog",
                    () -> new ParticleType<>(false, SnowFogParticleOption.DESERIALIZER) {
                        @Override
                        public Codec<SnowFogParticleOption> codec() {
                            return SnowFogParticleOption.CODEC;
                        }
                    }
            );
}
