package com.renyigesai.immortalers_delight.message.client;

import com.renyigesai.immortalers_delight.capabilitiy.EffectOverlayPlayerCapability;
import com.renyigesai.immortalers_delight.capabilitiy.ImmortalersCapabilities;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.entities.living.TerracottaGolem;
import com.renyigesai.immortalers_delight.message.ImmortalersEffectMessage;
import com.renyigesai.immortalers_delight.message.TerracottaGolemMessage;
import com.renyigesai.immortalers_delight.screen.TerracottaGolemMenu;
import com.renyigesai.immortalers_delight.screen.TerracottaGolemScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;

@OnlyIn(Dist.CLIENT)
public class ClientPacketHandlers {

    public static void handleImmortalersEffect(ImmortalersEffectMessage message) {
        if (Minecraft.getInstance().player != null) {
            handleClient(Minecraft.getInstance().player, message.getType(), message.getData());
        }
    }
    public static void handleTerracottaGolem(TerracottaGolemMessage message) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            ImmortalersDelightMod.LOGGER.warn("Ignoring TerracottaGolemMessage: no client player is available");
            return;
        }
        Entity entity = player.level().getEntity(message.getEntityId());
        if (!(entity instanceof TerracottaGolem golem)) {
            ImmortalersDelightMod.LOGGER.warn("Ignoring TerracottaGolemMessage: entity {} is not a TerracottaGolem", message.getEntityId());
            return;
        }
        TerracottaGolemMenu menu = new TerracottaGolemMenu(message.getId(), player.getInventory(), golem);
        player.containerMenu = menu;
        Minecraft.getInstance().setScreen(new TerracottaGolemScreen(menu, player.getInventory(), golem.getDisplayName()));
        ImmortalersDelightMod.LOGGER.debug("Opened TerracottaGolem screen id={} size={} entityId={}", message.getId(), message.getSize(), message.getEntityId());
    }

    public static void handleClient(Player entity, int type, float data) {
        Capability<EffectOverlayPlayerCapability> playerEffectOverlay = ImmortalersCapabilities.PLAYER_EFFECT_OVERLAY;
        entity.getCapability(playerEffectOverlay).ifPresent((cap) -> {
            if (type == 1) cap.setDeathlessData(data);
            else if (type == 2) cap.setInfernalForgingData(data);

        });
    }
}
