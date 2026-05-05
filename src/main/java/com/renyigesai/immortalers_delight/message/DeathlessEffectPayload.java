package com.renyigesai.immortalers_delight.message;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public record DeathlessEffectPayload(Map<UUID, Float> entitiesWithHealth) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<DeathlessEffectPayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "deathless_effect"));

    public static final StreamCodec<FriendlyByteBuf, DeathlessEffectPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(HashMap::new, UUIDUtil.STREAM_CODEC, ByteBufCodecs.FLOAT),
            DeathlessEffectPayload::entitiesWithHealth,
            DeathlessEffectPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleServer(DeathlessEffectPayload payload, IPayloadContext context) {
        if (!(context.player() instanceof ServerPlayer) || payload.entitiesWithHealth == null || payload.entitiesWithHealth.isEmpty()) {
            return;
        }
        PacketDistributor.sendToAllPlayers(payload);
    }

    public static void handleClient(DeathlessEffectPayload payload, IPayloadContext context) {
        // Reserved for client-side reaction if needed later.
    }
}
