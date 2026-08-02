package com.renyigesai.immortalers_delight.message;

import com.renyigesai.immortalers_delight.capabilitiy.KeyAuxiliaryPlayerCapability;
import com.renyigesai.immortalers_delight.capabilitiy.ImmortalersCapabilities;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * 自定义网络包：玩家消息包（客户端→服务端→所有客户端）
 * 包含：玩家UUID、消息内容
 */
public class KeyAuxiliaryMessage {
    // 包需要传输的数据（根据需求定义，支持基本类型、String、ResourceLocation、NBT 等）
    private final int type;

    // 1. 构造方法（用于创建包实例，填充要传输的数据）
    public KeyAuxiliaryMessage(int pType) {
        this.type = pType;
    }

    // 2. 序列化方法：将数据写入 PacketBuffer（网络传输前调用）
    public static void write(KeyAuxiliaryMessage packet, FriendlyByteBuf buffer) {
        // 按顺序写入数据，读取时需保持顺序一致
        buffer.writeInt(packet.type);
    }

    // 3. 反序列化方法：从 PacketBuffer 读取数据，创建包实例（网络接收后调用）
    public static KeyAuxiliaryMessage read(FriendlyByteBuf buffer) {
        // 读取顺序必须与写入一致，否则会出现数据错乱
        int type = buffer.readInt();
        return new KeyAuxiliaryMessage(type);
    }

    // 4. 核心：包处理方法（区分服务端/客户端，处理接收到的数据）
    public static void handle(KeyAuxiliaryMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent. Context context = contextSupplier. get();
        Player entity = context. getSender();
        int type = message.type;
        if (entity != null) {
            context.enqueueWork(() -> handle(entity, type));
        }
        context.setPacketHandled(true);
    }
    public static void handle(Player entity, int type) {
        Capability<KeyAuxiliaryPlayerCapability> playerKeyAuxiliary = ImmortalersCapabilities.PLAYER_KEY_AUXILIARY;
        entity.getCapability(playerKeyAuxiliary).ifPresent((cap) -> cap.setKey(type != 1));
    }

    // Getter（用于外部获取包数据，如广播时）
    public int getType() {
        return this.type;
    }

}
