package com.renyigesai.immortalers_delight.message;

import com.renyigesai.immortalers_delight.entities.living.TerracottaGolem;
import com.renyigesai.immortalers_delight.screen.TerracottaGolemMenu;
import com.renyigesai.immortalers_delight.screen.TerracottaGolemScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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
        System.out.println("正在解码陶傀儡的包");
        return new TerracottaGolemMessage(buf.readUnsignedByte(), buf.readVarInt(), buf.readInt());
    }

    public static void write(TerracottaGolemMessage message, FriendlyByteBuf buf) {
        System.out.println("正在构建陶傀儡的包");
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

        public static void handle(TerracottaGolemMessage msg, Supplier<NetworkEvent.Context> context) {
            context.get().enqueueWork(() -> {
                openInventory(msg);
            });
            context.get().setPacketHandled(true);
        }


        @OnlyIn(Dist.CLIENT)
        public static void openInventory(TerracottaGolemMessage packet) {
            // 这个类以及其相关的注册代码可以删除，用不到
//            Player player = Minecraft.getInstance().player;
//            if (player != null) {
//                Entity entity = player.level().getEntity(packet.getEntityId());
//                if (entity instanceof TerracottaGolem golem) {
//                    LocalPlayer clientplayerentity = Minecraft.getInstance().player;
//                    TerracottaGolemMenu container = new TerracottaGolemMenu(packet.getId(), player.getInventory(), golem.getInventory(), golem);
//                    clientplayerentity.containerMenu = container;
//                    Minecraft.getInstance().setScreen(new TerracottaGolemScreen(container, player.getInventory(), golem));
//                }
//            }
        }
    }
}
