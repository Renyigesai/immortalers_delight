package com.renyigesai.immortalers_delight.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class EnchantalCoolerScreen extends AbstractContainerScreen<EnchantalCoolerMenu> {

    // GUI 纹理路径
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(ImmortalersDelightMod.MODID, "textures/gui/enchantal_cooler.png");

    public EnchantalCoolerScreen(EnchantalCoolerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176; // GUI 宽度
        this.imageHeight = 166; // GUI 高度
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float partialTicks, int mouseX, int mouseY) {
        // 设置渲染系统
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        // 计算 GUI 的左上角位置
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        // 绘制背景
        pGuiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        // 绘制进度条
        int width = menu.getBlockEntity().cookingTotalTime;
        int progressWidth = (int) (24 * (width / 100.0f));
        pGuiGraphics.blit(TEXTURE, x + 103, y + 26, 177, 19, progressWidth, 17); // 绘制进度条
        int residualDye = menu.getBlockEntity().residualDye;
        int pngNewY = 86 - 10 * residualDye;
        int newY = y + 48 - 10 * residualDye;
        pGuiGraphics.blit(TEXTURE, x + 27, newY, 176, pngNewY, 9, menu.getBlockEntity().residualDye*10);

    }

    @Override
    public void render(GuiGraphics poseStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(poseStack);

        // 调用父类渲染方法
        super.render(poseStack, mouseX, mouseY, partialTicks);

        // 渲染物品栏提示
        renderTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics p_281635_, int p_282681_, int p_283686_) {
        super.renderLabels(p_281635_, p_282681_, p_283686_);
    }
}