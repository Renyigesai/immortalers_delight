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

public class SnowFogParticleOption implements ParticleOptions {
    public static final MapCodec<SnowFogParticleOption> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.INT.fieldOf("extra_raidus").forGetter(SnowFogParticleOption::getCountdown)
            ).apply(instance, SnowFogParticleOption::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, SnowFogParticleOption> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            SnowFogParticleOption::getCountdown,
            SnowFogParticleOption::new
    );

    private final int extra_raidus;

    public SnowFogParticleOption(int extra_r) {
        this.extra_raidus = extra_r;
    }

    @Override
    public @NotNull ParticleType<SnowFogParticleOption> getType() {
        return ImmortalersDelightParticleTypes.SNOW_FOG.get();
    }

    public int getCountdown() {
        return this.extra_raidus;
    }
}
