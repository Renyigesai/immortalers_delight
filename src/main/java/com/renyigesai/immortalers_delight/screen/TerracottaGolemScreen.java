package com.renyigesai.immortalers_delight.screen;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.entities.living.TerracottaGolem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
@OnlyIn(Dist.CLIENT)
public class TerracottaGolemScreen extends AbstractContainerScreen<TerracottaGolemMenu> {
    private static final ResourceLocation HORSE_INVENTORY_LOCATION = new ResourceLocation(ImmortalersDelightMod.MODID,"textures/gui/terracotta_golem_chest.png");
    private final TerracottaGolem golem;
    private float xMouse;
    private float yMouse;

    // 统一构造函数，如果需要获取golem的Name，这里已经从menu拿到了golem
    public TerracottaGolemScreen(TerracottaGolemMenu p_98817_, Inventory p_98818_, Component title) {
        super(p_98817_, p_98818_, title);
        this.golem = getMenu().getHorse();
    }


    protected void renderBg(GuiGraphics p_282553_, float p_282998_, int p_282929_, int p_283133_) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        p_282553_.blit(HORSE_INVENTORY_LOCATION, i, j, 0, 0, this.imageWidth, this.imageHeight);
        if (golem.hasChest()) {
            p_282553_.blit(HORSE_INVENTORY_LOCATION, i + 79, j + 7, 0, this.imageHeight, golem.getInventoryColumns() * 18, 18*4);
        }
        InventoryScreen.renderEntityInInventoryFollowsMouse(p_282553_, i + 51, j + 60, 17, (float)(i + 51) - this.xMouse, (float)(j + 75 - 50) - this.yMouse, golem);

    }

    // 重写getTitle()允许在这里传入参数给Component
    @Override
    public Component getTitle()
    {
        return Component.translatable(ImmortalersDelightMod.MODID + ".container.terracotta_golem");
    }

    // 重写标题渲染，让标题渲染使用getTitle()
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, getTitle(), this.titleLabelX, this.titleLabelY, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);
    }

    public void render(GuiGraphics p_281697_, int p_282103_, int p_283529_, float p_283079_) {
        this.renderBackground(p_281697_);
        this.xMouse = (float)p_282103_;
        this.yMouse = (float)p_283529_;
        super.render(p_281697_, p_282103_, p_283529_, p_283079_);
        this.renderTooltip(p_281697_, p_282103_, p_283529_);
    }
}
