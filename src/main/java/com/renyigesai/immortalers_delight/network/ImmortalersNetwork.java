package com.renyigesai.immortalers_delight.network;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.message.DeathlessEffectPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public final class ImmortalersNetwork {
    private static final String PROTOCOL_VERSION = "1";

    private ImmortalersNetwork() {}

    public static void register(RegisterPayloadHandlersEvent event) {
        event.registrar(PROTOCOL_VERSION).playBidirectional(
                DeathlessEffectPayload.TYPE,
                DeathlessEffectPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(DeathlessEffectPayload::handleClient, DeathlessEffectPayload::handleServer)
        );
    }

    public static void sendMSGToServer(DeathlessEffectPayload message) {
        PacketDistributor.sendToServer(message);
    }

    public static void sendMSGToAll(DeathlessEffectPayload message) {
        var server = ServerLifecycleHooks.getCurrentServer();
        if (server != null) {
            PacketDistributor.sendToAllPlayers(message);
        }
    }

    public static void sendNonLocal(DeathlessEffectPayload msg, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, msg);
    }
}
