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

public class ScreenLayerParticleOption implements ParticleOptions {
    public static final MapCodec<ScreenLayerParticleOption> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.INT.fieldOf("transparency").forGetter(ScreenLayerParticleOption::getTransparency)
            ).apply(instance, ScreenLayerParticleOption::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, ScreenLayerParticleOption> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            ScreenLayerParticleOption::getTransparency,
            ScreenLayerParticleOption::new
    );

    private final int transparency;

    public ScreenLayerParticleOption(int transparency) {
        this.transparency = transparency;
    }

    @Override
    public @NotNull ParticleType<ScreenLayerParticleOption> getType() {
        return ImmortalersDelightParticleTypes.INFERNAL_FORGING_SCREEN_LAYER.get();
    }

    public int getTransparency() {
        return this.transparency;
    }
}
