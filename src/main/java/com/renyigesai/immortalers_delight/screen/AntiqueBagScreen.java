package com.renyigesai.immortalers_delight.screen;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.item.antique.AntiqueBagItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class AntiqueBagScreen extends AbstractContainerScreen<AntiqueBagMenu> {

    public static final ResourceLocation TEXTURE;

    public AntiqueBagScreen(AntiqueBagMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        // 绘制背景
        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        //动态绘制古物槽
        int initX = x + 52;
        int initY = y + 19;
        int slotSizeX = 25;
        for (int index = 0; index < Math.max(menu.getAntiqueSlots().getSlots(), AntiqueBagItem.MAX_ANTIQUEL_SLOT - 1); index++) {
            int addX = index > 3 ? 12 : 0;
            int addY = index > 3 ? 22 : 0;
            int start = index > 3 ? AntiqueBagMenu.INDEX_MAP.get(index) : index;
            guiGraphics.blit(TEXTURE,initX + (slotSizeX * start) + addX,initY + addY,0,166,18,19,256,256);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        int titleWidth = this.font.width(this.title);
        int x = (this.imageWidth - titleWidth) / 2;
        pGuiGraphics.drawString(this.font, ((MutableComponent)this.title).withStyle(ChatFormatting.BOLD), x, this.titleLabelY, 0XB0653C, false);
    }

    static {
        TEXTURE = new ResourceLocation(ImmortalersDelightMod.MODID,"textures/gui/antique_bag.png");
    }
}
