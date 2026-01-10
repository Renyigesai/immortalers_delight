package com.renyigesai.immortalers_delight.network;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.message.DeathlessEffectPacket;
import com.renyigesai.immortalers_delight.message.TerracottaGolemMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.server.ServerLifecycleHooks;

public class ImmortalersNetwork {
    // 协议版本，修改时需同步客户端与服务端
    private static final String PROTOCOL_VERSION = Integer.toString(1);
    // 1. 定义常量（协议版本、通道ID），不主动创建通道
    public static final String CHANNEL_ID = "main_channel";

    // 2. 提供获取通道实例的静态方法（仅在注册阶段调用）
//    public static SimpleChannel NETWORK_WRAPPER = null;
//            NetworkRegistry.newSimpleChannel(
//            new ResourceLocation(ImmortalersDelightMod.MODID, "main_channel"), // 通道唯一标识
//            () -> PROTOCOL_VERSION, // 获取当前协议版本
//            PROTOCOL_VERSION::equals, // 客户端是否接受该协议版本
//            PROTOCOL_VERSION::equals  // 服务端是否接受该协议版本
//    );
public static SimpleChannel getChannel() {
//    if (NETWORK_WRAPPER == null) {
//        // 仅在首次获取时创建通道（确保在注册阶段调用）
//        NETWORK_WRAPPER = NetworkRegistry.newSimpleChannel(
//                new ResourceLocation("immortalers_delight", CHANNEL_ID),
//                () -> PROTOCOL_VERSION,
//                PROTOCOL_VERSION::equals,
//                PROTOCOL_VERSION::equals
//        );
//    }
    return ImmortalersDelightMod.NETWORK_WRAPPER;
}
    public static CommonProxy PROXY = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
    // 2. 定义包 ID（每个自定义包对应一个唯一ID，避免冲突）
    private static int packetsRegistered;
    public static int nextPacketId() {return packetsRegistered++;}
//    static {
//        NetworkRegistry.ChannelBuilder channel = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(ImmortalersDelightMod.MODID, "main_channel"));
//        String version = PROTOCOL_VERSION;
//        version.getClass();
//        channel = channel.clientAcceptedVersions(version::equals);
//        version = PROTOCOL_VERSION;
//        version.getClass();
//        NETWORK_WRAPPER = channel.serverAcceptedVersions(version::equals).networkProtocolVersion(() -> {
//            return PROTOCOL_VERSION;
//        }).simpleChannel();
//    }


    public static <MSG> void sendMSGToServer(MSG message) {
        getChannel().sendToServer(message);
    }

    public static <MSG> void sendMSGToAll(MSG message) {
        for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
            sendNonLocal(message, player);
        }
    }

    public static <MSG> void sendNonLocal(MSG msg, ServerPlayer player) {
        getChannel().sendTo(msg, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void registerPackets() {
        // 此处注册你的所有自定义网络包（如 PlayerMessagePacket）
        getChannel().registerMessage(nextPacketId(),
                TerracottaGolemMessage.class,
                TerracottaGolemMessage::write,
                TerracottaGolemMessage::read,
                TerracottaGolemMessage.Handler::handle
        );
        getChannel().registerMessage(
                nextPacketId(), // 包唯一ID
                DeathlessEffectPacket.class, // 自定义包类
                DeathlessEffectPacket::write, // 序列化方法（写入 PacketBuffer）
                DeathlessEffectPacket::read, // 反序列化方法（从 PacketBuffer 读取）
                DeathlessEffectPacket::handle // 包处理方法（服务端/客户端处理逻辑）
        );
    }

    public static void initChannel() {
        getChannel();
    }
}
