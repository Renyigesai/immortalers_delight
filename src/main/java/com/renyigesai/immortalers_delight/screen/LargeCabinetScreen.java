package com.renyigesai.immortalers_delight.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMenuTypes;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class LargeCabinetScreen extends AbstractContainerScreen<LargeCabinetMenu> {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(ImmortalersDelightMod.MODID, "textures/gui/large_cabinet.png");

    public LargeCabinetScreen(LargeCabinetMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 230;
        this.imageHeight = 258;
    }
    // 对应提供的玉米与木框 GUI 贴图路径（图片建议分辨率 256x256 或 512x512）
//    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation("yourmod", "textures/gui/container/large_cabinet.png");

//    public LargeCabinetScreen(LargeCabinetMenu menu, Inventory playerInventory, Component title) {
//        super(menu, playerInventory, title);
//        // 适应 12 列与 9 行以及玩家背包所需的尺寸
//        this.imageWidth = 232;
//        this.imageHeight = 268;
//        this.inventoryLabelY = 172; // "物品栏" 文本渲染位置
//    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);

//        int left = this.leftPos;
//        int top = this.topPos;
        // 计算 GUI 的左上角位置
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        // 渲染主背景贴图
        guiGraphics.blit(GUI_TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight,512,512);
    }

//    @Override
//    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
//        // 渲染容器标题
//        guiGraphics.drawString(this.font, this.title, 8, 6, 4210752, false);
//        // 渲染下方玩家背包标题（居中对齐到背包槽位）
//        guiGraphics.drawString(this.font, this.playerInventoryTitle, 35, this.inventoryLabelY, 4210752, false);
//    }
    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        // 注意：AbstractContainerScreen 在调用 renderLabels 前，
        // 已经在 pose 中 translate(leftPos, topPos, 0) 了，
        // 这里的局部坐标 (0, 0) 即为 GUI 窗口贴图的左上角。

        // 假设天蓝色与深绿色侧条在 Y 轴方向对应的高度区域（可根据你贴图中的具体像素位置微调）
        int barStartY = 184; // 竖条顶部起始 Y 坐标（对应下方背包网格的顶端）
        int barHeight = 72;  // 竖条总高度（对应 4 行槽位的大致跨度）

        // ========================================================
        // 1. 左侧天蓝色竖条：旋转 90 度竖排显示 "玩家背包" (playerInventoryTitle)
        // ========================================================
        int leftBarCenterX = 18; // 贴图左侧天蓝色区域的中心 X 坐标
        int leftBarCenterY = barStartY + (barHeight / 2);

        int playerTitleWidth = this.font.width(this.playerInventoryTitle);

        guiGraphics.pose().pushPose();
        // (1) 将原点平移到天蓝竖条的视觉中心
        guiGraphics.pose().translate(leftBarCenterX, leftBarCenterY, 0.0F);
        // (2) 顺时针旋转 90 度（文字由上至下阅读）
        // 如果希望自下而上阅读，可改为 -90.0F
        guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(-90.0F));
        // (3) 居中绘制：以旋转后的中心点向左偏移半个文字宽度，向上偏移半个文字高度
        guiGraphics.drawString(
                this.font,
                this.playerInventoryTitle,
                -playerTitleWidth / 2,
                -this.font.lineHeight / 2,
                0x1B3F54, // 深海蓝文字颜色，保证在天蓝底色上清晰可见
                false
        );
        guiGraphics.pose().popPose();

        // ========================================================
        // 2. 右侧深绿色竖条：对称旋转 90 度竖排显示 "容器标题" (title)
        // ========================================================
        int rightBarCenterX = 211; // 贴图右侧深绿色区域的中心 X 坐标
        int rightBarCenterY = barStartY + (barHeight / 2);

        int containerTitleWidth = this.font.width(this.title);

        guiGraphics.pose().pushPose();
        // (1) 将原点平移到深绿竖条的视觉中心
        guiGraphics.pose().translate(rightBarCenterX, rightBarCenterY, 0.0F);
        // (2) 顺时针旋转 90 度
        guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(90.0F));
        // (3) 居中绘制
        guiGraphics.drawString(
                this.font,
                this.title,
                -containerTitleWidth / 2,
                -this.font.lineHeight / 2,
                0xEEFDEB, // 浅嫩绿/近白色文字颜色，保证在深绿底色上对比鲜明
                false
        );
        guiGraphics.pose().popPose();
    }

//    @Override
//    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
//        // 背景绘制逻辑
//        guiGraphics.blit(GUI_TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
//    }
}
