package com.renyigesai.immortalers_delight.client.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightParticleTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

public class SpiralSoulParticleOption implements ParticleOptions {
    public static final MapCodec<SpiralSoulParticleOption> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.INT.fieldOf("extra_raidus").forGetter(SpiralSoulParticleOption::getCountdown)
            ).apply(instance, SpiralSoulParticleOption::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, SpiralSoulParticleOption> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            SpiralSoulParticleOption::getCountdown,
            SpiralSoulParticleOption::new
    );

    private final int extra_raidus;

    public SpiralSoulParticleOption(int extra_r) {
        this.extra_raidus = extra_r;
    }

    @Override
    public @NotNull ParticleType<SpiralSoulParticleOption> getType() {
        return ImmortalersDelightParticleTypes.SPIRAL_SOUL.get();
    }

    public int getCountdown() {
        return this.extra_raidus;
    }
}
