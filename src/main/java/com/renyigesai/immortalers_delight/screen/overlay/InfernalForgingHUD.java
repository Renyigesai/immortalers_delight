package com.renyigesai.immortalers_delight.screen.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.message.ImmortalersEffectPayload;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

@OnlyIn(Dist.CLIENT)
public class InfernalForgingHUD {
    private static final ResourceLocation HUD = ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "textures/gui/icons/infernal_forging.png");
    public static final ResourceLocation LAYER_ID = ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "infernal_forging");

    public static void register(RegisterGuiLayersEvent event) {
        event.registerAboveAll(LAYER_ID, InfernalForgingHUD::render);
    }

    public static void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui) {
            return;
        }
        float f2 = ImmortalersEffectPayload.getClientInfernalForgingData();
        if (f2 > 0.05 && f2 <= 1) {
            int screenWidth = mc.getWindow().getGuiScaledWidth();
            int screenHeight = mc.getWindow().getGuiScaledHeight();
            renderTextureOverlay(guiGraphics, HUD, f2, screenWidth, screenHeight);
        }
    }

    protected static void renderTextureOverlay(GuiGraphics pGuiGraphics, ResourceLocation pShaderLocation, float pAlpha, int screenWidth, int screenHeight) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, pAlpha);
        pGuiGraphics.blit(pShaderLocation, 0, 0, -90, 0.0F, 0.0F, screenWidth, screenHeight, screenWidth, screenHeight);
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
