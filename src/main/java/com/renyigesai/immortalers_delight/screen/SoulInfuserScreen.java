package com.renyigesai.immortalers_delight.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SoulInfuserScreen extends AbstractContainerScreen<SoulInfuserMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(ImmortalersDelightMod.MODID, "textures/gui/soul_infuser.png");

    public SoulInfuserScreen(SoulInfuserMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 176;
        this.inventoryLabelY = this.imageHeight - 91; // 调整 "Inventory" 标题位置
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        // 绘制 176x176 基础背景
        guiGraphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);

        // 燃料小进度条（16宽 32高，UV 定位于 (176, 0)）
        int fuelHeight = this.menu.getChargeScaled();
        if (fuelHeight > 0) {
            guiGraphics.blit(TEXTURE, x + 8, y + 33 + (32 - fuelHeight), 176, 32 - fuelHeight, 16, fuelHeight);
        }

        // 燃料填充进度条（16宽 16高，UV 定位于 (176, 32)）
        int fuelAddHeight = this.menu.getChargeAddScaled();
        if (fuelAddHeight > 0) {
            guiGraphics.blit(TEXTURE, x + 8, y + 68 + (16 - fuelAddHeight), 176, 48 - fuelAddHeight, 16, fuelAddHeight);
        }

        // 72高 128宽的大合成进度条 UV 定位于 (0, 176)）
        int progressWidth = this.menu.getProgressScaled();
        if (progressWidth > 0) {
            guiGraphics.blit(TEXTURE, x + 28, y + 13, 0, 176, progressWidth, 72);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
}