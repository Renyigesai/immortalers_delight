package com.renyigesai.immortalers_delight.message;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.network.ImmortalersNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SnifferSaddleMessage {
    private final int entityId;
    private final boolean hasSaddle;

    public SnifferSaddleMessage(int entityId, boolean hasSaddle) {
        this.entityId = entityId;
        this.hasSaddle = hasSaddle;
    }

    public static SnifferSaddleMessage read(FriendlyByteBuf buf) {
        int entityId = buf.readInt();
        boolean hasSaddle = buf.readBoolean();
        ImmortalersDelightMod.LOGGER.debug("Read SnifferSaddleMessage: entityId={}, hasSaddle={}", entityId, hasSaddle);
        return new SnifferSaddleMessage(entityId, hasSaddle);
    }

    public static void write(SnifferSaddleMessage message, FriendlyByteBuf buf) {
        ImmortalersDelightMod.LOGGER.debug("Write SnifferSaddleMessage: entityId={}, hasSaddle={}", message.entityId, message.hasSaddle);
        buf.writeInt(message.entityId);
        buf.writeBoolean(message.hasSaddle);
    }

    public static void handle(SnifferSaddleMessage msg, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        ImmortalersDelightMod.LOGGER.debug("Handle SnifferSaddleMessage: direction={}, entityId={}, hasSaddle={}", 
            context.getDirection(), msg.entityId, msg.hasSaddle);
        
        if (context.getDirection() != NetworkDirection.PLAY_TO_CLIENT) {
            ImmortalersDelightMod.LOGGER.warn("Ignoring SnifferSaddleMessage from direction {}", context.getDirection());
            context.setPacketHandled(true);
            return;
        }

        context.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                Minecraft mc = Minecraft.getInstance();
                if (mc.level != null) {
                    Entity entity = mc.level.getEntity(msg.entityId);
                    if (entity instanceof Sniffer sniffer) {
                        sniffer.getPersistentData().putBoolean("HasSaddle", msg.hasSaddle);
                        ImmortalersDelightMod.LOGGER.info("Client: Set sniffer {} HasSaddle to {}", msg.entityId, msg.hasSaddle);
                    } else {
                        ImmortalersDelightMod.LOGGER.warn("Entity {} is not a Sniffer or not found", msg.entityId);
                    }
                }
            });
        });
        context.setPacketHandled(true);
    }
}
