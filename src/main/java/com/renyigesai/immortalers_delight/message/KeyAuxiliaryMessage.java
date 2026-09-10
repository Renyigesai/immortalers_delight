package com.renyigesai.immortalers_delight.message;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.capabilitiy.ImmortalersCapabilities;
import com.renyigesai.immortalers_delight.capabilitiy.KeyAuxiliaryPlayerCapability;
import com.renyigesai.immortalers_delight.network.ImmortalersNetwork;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public class KeyAuxiliaryMessage {
    private final int type;
    public KeyAuxiliaryMessage(int type){this.type=type;}
    public static void write(KeyAuxiliaryMessage message,FriendlyByteBuf buffer){ImmortalersDelightMod.LOGGER.debug("Write {} discriminator={} direction=unknown type={}",KeyAuxiliaryMessage.class.getSimpleName(),ImmortalersNetwork.KEY_AUXILIARY_MESSAGE_ID,message.type);buffer.writeInt(message.type);}
    public static KeyAuxiliaryMessage read(FriendlyByteBuf buffer){KeyAuxiliaryMessage message=new KeyAuxiliaryMessage(buffer.readInt());ImmortalersDelightMod.LOGGER.debug("Read {} discriminator={} direction=unknown type={}",KeyAuxiliaryMessage.class.getSimpleName(),ImmortalersNetwork.KEY_AUXILIARY_MESSAGE_ID,message.type);return message;}
    public static void handle(KeyAuxiliaryMessage message,Supplier<NetworkEvent.Context> supplier){NetworkEvent.Context context=supplier.get();NetworkDirection direction=context.getDirection();ImmortalersDelightMod.LOGGER.debug("Handle {} discriminator={} direction={} type={}",KeyAuxiliaryMessage.class.getSimpleName(),ImmortalersNetwork.KEY_AUXILIARY_MESSAGE_ID,direction,message.type);if(direction!=NetworkDirection.PLAY_TO_SERVER){ImmortalersDelightMod.LOGGER.warn("Ignoring {} from direction {}",KeyAuxiliaryMessage.class.getSimpleName(),direction);}else{Player player=context.getSender();if(player==null)ImmortalersDelightMod.LOGGER.warn("Ignoring {} without sender",KeyAuxiliaryMessage.class.getSimpleName());else context.enqueueWork(()->handle(player,message.type));}context.setPacketHandled(true);}
    public static void handle(Player player,int type){Capability<KeyAuxiliaryPlayerCapability> cap=ImmortalersCapabilities.PLAYER_KEY_AUXILIARY;player.getCapability(cap).ifPresent(value->value.setKey(type!=1));}
    public int getType(){return type;}
}
