package com.renyigesai.immortalers_delight.network;

import com.renyigesai.immortalers_delight.message.DeathlessEffectPayload;
import com.renyigesai.immortalers_delight.message.ImmortalersEffectPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public final class ImmortalersNetwork {
    private static final String PROTOCOL_VERSION = "1";

    private ImmortalersNetwork() {}

    public static void register(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar(PROTOCOL_VERSION);
        registrar.playBidirectional(
                DeathlessEffectPayload.TYPE,
                DeathlessEffectPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(DeathlessEffectPayload::handleClient, DeathlessEffectPayload::handleServer)
        );
        registrar.playToClient(
                ImmortalersEffectPayload.TYPE,
                ImmortalersEffectPayload.STREAM_CODEC,
                ImmortalersEffectPayload::handleClient
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

    public static void sendNonLocal(ImmortalersEffectPayload msg, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, msg);
    }
}
