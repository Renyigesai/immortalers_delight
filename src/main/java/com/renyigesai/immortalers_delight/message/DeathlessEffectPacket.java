package com.renyigesai.immortalers_delight.message;

import com.renyigesai.immortalers_delight.network.ImmortalersNetwork;
import com.renyigesai.immortalers_delight.potion.immortaleffects.DeathlessEffect;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * 自定义网络包：玩家消息包（客户端→服务端→所有客户端）
 * 包含：玩家UUID、消息内容
 */
public class DeathlessEffectPacket {
    // 包需要传输的数据（根据需求定义，支持基本类型、String、ResourceLocation、NBT 等）
    private final Map<UUID, Float> entitiesWithHealeh;

    // 1. 构造方法（用于创建包实例，填充要传输的数据）
    public DeathlessEffectPacket(Map<UUID, Float> map) {
        this.entitiesWithHealeh = map == null ? new HashMap<>() : new HashMap<>(map);;
    }

    // 2. 序列化方法：将数据写入 PacketBuffer（网络传输前调用）
    public static void write(DeathlessEffectPacket packet, FriendlyByteBuf buffer) {
        // 按顺序写入数据，读取时需保持顺序一致
        buffer.writeMap(packet.entitiesWithHealeh, FriendlyByteBuf::writeUUID, FriendlyByteBuf::writeFloat);
    }

    // 3. 反序列化方法：从 PacketBuffer 读取数据，创建包实例（网络接收后调用）
    public static DeathlessEffectPacket read(FriendlyByteBuf buffer) {
        // 读取顺序必须与写入一致，否则会出现数据错乱
        Map<UUID, Float> map = buffer.readMap(FriendlyByteBuf::readUUID, FriendlyByteBuf::readFloat);
        return new DeathlessEffectPacket(map);
    }

    // 4. 核心：包处理方法（区分服务端/客户端，处理接收到的数据）
    public static void handle(DeathlessEffectPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        // 获取网络上下文（包含发送者、网络通道等信息）
        NetworkEvent.Context context = contextSupplier.get();

        // 关键：切换到游戏主线程处理（避免线程安全问题，尤其是客户端渲染、服务端修改世界）
        context.enqueueWork(() -> {
            // 区分服务端与客户端（通过上下文判断是否有服务器玩家）
            if (context.getSender() != null) {
                // ========== 服务端处理逻辑 ==========
                ServerPlayer senderPlayer = context.getSender(); // 获取发送包的客户端玩家（仅服务端可用）

                // 1. 验证数据（防止恶意客户端发送非法数据）
                if (packet.entitiesWithHealeh == null || packet.entitiesWithHealeh.isEmpty()) {
                    return;
                }

                // 2. 处理业务逻辑

                // 3. 服务端→所有客户端广播（转发该包给其他客户端）
                ImmortalersNetwork.sendMSGToAll(packet);

            } else {
                // ========== 客户端处理逻辑 ==========
                DeathlessEffect.getMapFromNetwork(packet.entitiesWithHealeh);
                // 示例：在客户端聊天窗口显示接收到的消息
//                String displayMessage = String.format("[%s] %s", packet.playerName, packet.message);
//                net.minecraft.client.Minecraft.getInstance().player.sendSystemMessage(
//                        net.minecraft.network.chat.Component.literal(displayMessage)
//                );
            }
        });

        // 关键：标记上下文已处理，释放网络资源
        context.setPacketHandled(true);
    }

    // Getter（用于外部获取包数据，如广播时）
    public Map<UUID, Float> getEntitiesWithHealeh() {
        return entitiesWithHealeh;
    }

}
