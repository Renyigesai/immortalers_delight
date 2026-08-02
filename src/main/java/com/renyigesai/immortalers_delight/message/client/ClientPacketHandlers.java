package com.renyigesai.immortalers_delight.message.client;

import com.renyigesai.immortalers_delight.capabilitiy.EffectOverlayPlayerCapability;
import com.renyigesai.immortalers_delight.capabilitiy.ImmortalersCapabilities;
import com.renyigesai.immortalers_delight.message.ImmortalersEffectMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;

@OnlyIn(Dist.CLIENT)
public class ClientPacketHandlers {

    public static void handleImmortalersEffect(ImmortalersEffectMessage message) {
//        System.out.println("客户端处理器被调用");
        if (Minecraft.getInstance().player != null) {
            handleClient(Minecraft.getInstance().player, message.getType(), message.getData());

        } //else System.out.println("玩家不存在！处理玩家数据失败！");
    }
    public static void handleClient(Player entity, int type, float data) {
//        System.out.println("正在尝试修改能力");
        Capability<EffectOverlayPlayerCapability> playerEffectOverlay = ImmortalersCapabilities.PLAYER_EFFECT_OVERLAY;
        entity.getCapability(playerEffectOverlay).ifPresent((cap) -> {
            if (type == 1) cap.setDeathlessData(data);
            else if (type == 2) cap.setInfernalForgingData(data);

        });
    }
}
