package com.renyigesai.immortalers_delight.message;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ImmortalersEffectPayload(int effectType, float data) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ImmortalersEffectPayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "immortalers_effect"));

    public static final StreamCodec<FriendlyByteBuf, ImmortalersEffectPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ImmortalersEffectPayload::effectType,
            ByteBufCodecs.FLOAT, ImmortalersEffectPayload::data,
            ImmortalersEffectPayload::new
    );

    /** Client-side overlay progress for infernal forging (effectType == 2). */
    private static float clientInfernalForgingData;

    public static float getClientInfernalForgingData() {
        return clientInfernalForgingData;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleClient(ImmortalersEffectPayload payload, IPayloadContext context) {
        if (payload.effectType == 2) {
            clientInfernalForgingData = payload.data;
        }
    }
}
