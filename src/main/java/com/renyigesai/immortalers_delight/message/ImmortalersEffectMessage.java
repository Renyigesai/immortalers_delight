package com.renyigesai.immortalers_delight.message;

import com.renyigesai.immortalers_delight.capabilitiy.EffectOverlayPlayerCapability;
import com.renyigesai.immortalers_delight.capabilitiy.ImmortalersCapabilities;
import com.renyigesai.immortalers_delight.message.client.ClientPacketHandlers;
import com.renyigesai.immortalers_delight.network.ImmortalersNetwork;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ImmortalersEffectMessage {
    // 包需要传输的数据（根据需求定义，支持基本类型、String、ResourceLocation、NBT 等）
    private final int type;
    private final float data;

    // 1. 构造方法（用于创建包实例，填充要传输的数据）
    public ImmortalersEffectMessage(int pType, float pData) {
        this.type = pType;
        this.data = pData;
    }

    // 2. 序列化方法：将数据写入 PacketBuffer（网络传输前调用）
    public static void write(ImmortalersEffectMessage packet, FriendlyByteBuf buffer) {
        // 按顺序写入数据，读取时需保持顺序一致
        buffer.writeInt(packet.type);
        buffer.writeFloat(packet.data);
    }

    // 3. 反序列化方法：从 PacketBuffer 读取数据，创建包实例（网络接收后调用）
    public static ImmortalersEffectMessage read(FriendlyByteBuf buffer) {
        // 读取顺序必须与写入一致，否则会出现数据错乱
        int type = buffer.readInt();
        float data = buffer.readFloat();
        return new ImmortalersEffectMessage(type,data);
    }

    // 4. 核心：包处理方法（区分服务端/客户端，处理接收到的数据）
    public static void handle(ImmortalersEffectMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        System.out.println("火成包处理方法");
        NetworkEvent. Context context = contextSupplier. get();
        Player entity = context.getSender();

        int type = message.type;
        float data = message.data;
        //服务端处理方法
        if (entity != null) {
            context.enqueueWork(() -> handleServer(entity, type, data));
        }
        //客户端处理方法
//        else context.enqueueWork(() -> {
//            if (contextSupplier.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
//                // 关键：用DistExecutor惰性调用客户端处理器，不会直接引用客户端类
//                DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandlers.handleImmortalersEffect(message));
//            } else System.out.println("你这是发哪的？" + contextSupplier.get().getDirection());
//        });
        else {
            context.enqueueWork(() -> {
                if (contextSupplier.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
                    ImmortalersNetwork.PROXY.handleEffectMessage(message);
                }
            });
        }

        context.setPacketHandled(true);
    }
    public static void handleServer(Player entity, int type, float data) {
        Capability<EffectOverlayPlayerCapability> playerEffectOverlay = ImmortalersCapabilities.PLAYER_EFFECT_OVERLAY;
        entity.getCapability(playerEffectOverlay).ifPresent((cap) -> {
            if (type == 1) cap.setDeathlessData(data);
            else if (type == 2) cap.setInfernalForgingData(data);

        });
    }

    // Getter（用于外部获取包数据，如广播时）
    public int getType() {
        return type;
    }
    public float getData() {
        return data;
    }

}
