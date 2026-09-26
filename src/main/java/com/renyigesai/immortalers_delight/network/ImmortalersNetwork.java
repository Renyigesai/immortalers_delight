package com.renyigesai.immortalers_delight.network;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.message.ImmortalersEffectMessage;
import com.renyigesai.immortalers_delight.message.KeyAuxiliaryMessage;
import com.renyigesai.immortalers_delight.message.TerracottaGolemMessage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.server.ServerLifecycleHooks;

public class ImmortalersNetwork {
    public static final String CHANNEL_ID = "main_channel";
    public static final int TERRACOTTA_GOLEM_MESSAGE_ID = 0;
    public static final int EFFECT_MESSAGE_ID = 1;
    public static final int KEY_AUXILIARY_MESSAGE_ID = 2;
    public static CommonProxy PROXY = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
    public static SimpleChannel getChannel() { return ImmortalersDelightMod.NETWORK_WRAPPER; }
    public static <MSG> void sendMSGToServer(MSG message) { ImmortalersDelightMod.LOGGER.debug("Send {} direction={}", message.getClass().getSimpleName(), NetworkDirection.PLAY_TO_SERVER); getChannel().sendToServer(message); }
    public static <MSG> void sendMSGToAll(MSG message) { for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) sendNonLocal(message, player); }
    public static <MSG> void sendNonLocal(MSG message, ServerPlayer player) { ImmortalersDelightMod.LOGGER.debug("Send {} direction={} player={}", message.getClass().getSimpleName(), NetworkDirection.PLAY_TO_CLIENT, player.getGameProfile().getName()); getChannel().sendTo(message, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT); }
    public static void registerPackets() {
        ImmortalersDelightMod.LOGGER.debug("Register message {} discriminator={}", TerracottaGolemMessage.class.getSimpleName(), TERRACOTTA_GOLEM_MESSAGE_ID);
        getChannel().registerMessage(TERRACOTTA_GOLEM_MESSAGE_ID, TerracottaGolemMessage.class, TerracottaGolemMessage::write, TerracottaGolemMessage::read, TerracottaGolemMessage.Handler::handle);
        ImmortalersDelightMod.LOGGER.debug("Register message {} discriminator={}", ImmortalersEffectMessage.class.getSimpleName(), EFFECT_MESSAGE_ID);
        getChannel().registerMessage(EFFECT_MESSAGE_ID, ImmortalersEffectMessage.class, ImmortalersEffectMessage::write, ImmortalersEffectMessage::read, ImmortalersEffectMessage::handle);
        ImmortalersDelightMod.LOGGER.debug("Register message {} discriminator={}", KeyAuxiliaryMessage.class.getSimpleName(), KEY_AUXILIARY_MESSAGE_ID);
        getChannel().registerMessage(KEY_AUXILIARY_MESSAGE_ID, KeyAuxiliaryMessage.class, KeyAuxiliaryMessage::write, KeyAuxiliaryMessage::read, KeyAuxiliaryMessage::handle);
    }
    public static void initChannel() { getChannel(); }
}
