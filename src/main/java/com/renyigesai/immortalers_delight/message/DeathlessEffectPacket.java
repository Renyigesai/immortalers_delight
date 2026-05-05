package com.renyigesai.immortalers_delight.message;

import com.renyigesai.immortalers_delight.network.ImmortalersNetwork;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Backwards-compatible facade for {@link DeathlessEffectPayload} used by older call sites.
 */
public final class DeathlessEffectPacket {
    private DeathlessEffectPacket() {}

    public static void broadcastFromServerPlayer(ServerPlayer sender, Map<UUID, Float> map) {
        if (map == null || map.isEmpty()) {
            return;
        }
        ImmortalersNetwork.sendMSGToAll(new DeathlessEffectPayload(new HashMap<>(map)));
    }
}
