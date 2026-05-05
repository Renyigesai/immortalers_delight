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

public class ShockWaveParticleOption implements ParticleOptions {
    public static final MapCodec<ShockWaveParticleOption> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.INT.fieldOf("extra_size").forGetter(ShockWaveParticleOption::getCountdown)
            ).apply(instance, ShockWaveParticleOption::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, ShockWaveParticleOption> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            ShockWaveParticleOption::getCountdown,
            ShockWaveParticleOption::new
    );

    private final int extra_size;

    public ShockWaveParticleOption(int extra_size) {
        this.extra_size = extra_size;
    }

    @Override
    public @NotNull ParticleType<ShockWaveParticleOption> getType() {
        return ImmortalersDelightParticleTypes.SHOCK_WAVE.get();
    }

    public int getCountdown() {
        return this.extra_size;
    }
}
