package com.renyigesai.immortalers_delight.message;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.network.ImmortalersNetwork;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

// 建议删除整个类
public class TerracottaGolemMessage {
    private final int id;
    private final int size;
    private final int entityId;

    public TerracottaGolemMessage(int id, int size, int entityId) {
        this.id = id;
        this.size = size;
        this.entityId = entityId;
    }

    public static TerracottaGolemMessage read(FriendlyByteBuf buf) {
        TerracottaGolemMessage message = new TerracottaGolemMessage(buf.readUnsignedByte(), buf.readVarInt(), buf.readInt());
        ImmortalersDelightMod.LOGGER.debug("Read {} discriminator={} direction=unknown id={} size={} entityId={}", TerracottaGolemMessage.class.getSimpleName(), ImmortalersNetwork.TERRACOTTA_GOLEM_MESSAGE_ID, message.id, message.size, message.entityId);
        return message;
    }

    public static void write(TerracottaGolemMessage message, FriendlyByteBuf buf) {
        ImmortalersDelightMod.LOGGER.debug("Write {} discriminator={} direction=unknown id={} size={} entityId={}", TerracottaGolemMessage.class.getSimpleName(), ImmortalersNetwork.TERRACOTTA_GOLEM_MESSAGE_ID, message.id, message.size, message.entityId);
        buf.writeByte(message.id);
        buf.writeVarInt(message.size);
        buf.writeInt(message.entityId);
    }


    public int getId() {
        return this.id;
    }

    public int getSize() {
        return this.size;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public static class Handler {
        public Handler() {
        }

        public static void handle(TerracottaGolemMessage msg, Supplier<NetworkEvent.Context> supplier) {
            NetworkEvent.Context context = supplier.get();
            ImmortalersDelightMod.LOGGER.debug("Handle {} discriminator={} direction={} id={} size={} entityId={}", TerracottaGolemMessage.class.getSimpleName(), ImmortalersNetwork.TERRACOTTA_GOLEM_MESSAGE_ID, context.getDirection(), msg.id, msg.size, msg.entityId);
            if (context.getDirection() != NetworkDirection.PLAY_TO_CLIENT) {
                ImmortalersDelightMod.LOGGER.warn("Ignoring {} from direction {}", TerracottaGolemMessage.class.getSimpleName(), context.getDirection());
                context.setPacketHandled(true);
                return;
            }
            context.enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> com.renyigesai.immortalers_delight.message.client.ClientPacketHandlers.handleTerracottaGolem(msg)));
            context.setPacketHandled(true);
        }
    }
}
