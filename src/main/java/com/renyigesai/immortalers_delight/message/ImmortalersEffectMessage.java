package com.renyigesai.immortalers_delight.message;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.capabilitiy.EffectOverlayPlayerCapability;
import com.renyigesai.immortalers_delight.capabilitiy.ImmortalersCapabilities;
import com.renyigesai.immortalers_delight.network.ImmortalersNetwork;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public class ImmortalersEffectMessage {
    private final int type; private final float data;
    public ImmortalersEffectMessage(int type, float data) { this.type=type; this.data=data; }
    public static void write(ImmortalersEffectMessage message, FriendlyByteBuf buffer) { ImmortalersDelightMod.LOGGER.debug("Write {} discriminator={} direction=unknown type={} data={}", ImmortalersEffectMessage.class.getSimpleName(), ImmortalersNetwork.EFFECT_MESSAGE_ID, message.type, message.data); buffer.writeInt(message.type); buffer.writeFloat(message.data); }
    public static ImmortalersEffectMessage read(FriendlyByteBuf buffer) { ImmortalersEffectMessage message=new ImmortalersEffectMessage(buffer.readInt(),buffer.readFloat()); ImmortalersDelightMod.LOGGER.debug("Read {} discriminator={} direction=unknown type={} data={}", ImmortalersEffectMessage.class.getSimpleName(), ImmortalersNetwork.EFFECT_MESSAGE_ID, message.type, message.data); return message; }
    public static void handle(ImmortalersEffectMessage message, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context=supplier.get(); NetworkDirection direction=context.getDirection();
        ImmortalersDelightMod.LOGGER.debug("Handle {} discriminator={} direction={} type={} data={}", ImmortalersEffectMessage.class.getSimpleName(), ImmortalersNetwork.EFFECT_MESSAGE_ID, direction, message.type, message.data);
        if (direction == NetworkDirection.PLAY_TO_SERVER) { Player player=context.getSender(); if (player == null) ImmortalersDelightMod.LOGGER.warn("Ignoring {} without sender", ImmortalersEffectMessage.class.getSimpleName()); else context.enqueueWork(() -> handleServer(player,message.type,message.data)); }
        else if (direction == NetworkDirection.PLAY_TO_CLIENT) context.enqueueWork(() -> ImmortalersNetwork.PROXY.handleEffectMessage(message));
        else ImmortalersDelightMod.LOGGER.warn("Ignoring {} from direction {}", ImmortalersEffectMessage.class.getSimpleName(), direction);
        context.setPacketHandled(true);
    }
    public static void handleServer(Player player,int type,float data) { Capability<EffectOverlayPlayerCapability> cap=ImmortalersCapabilities.PLAYER_EFFECT_OVERLAY; player.getCapability(cap).ifPresent(value->{if(type==1)value.setDeathlessData(data);else if(type==2)value.setInfernalForgingData(data);else ImmortalersDelightMod.LOGGER.warn("Ignoring effect message with unknown type {}",type);}); }
    public int getType(){return type;} public float getData(){return data;}
}
