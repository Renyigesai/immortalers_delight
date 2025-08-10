package com.renyigesai.immortalers_delight.screen;

import com.renyigesai.immortalers_delight.entities.living.TerracottaGolem;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
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

    public TerracottaGolemScreen(TerracottaGolemMenu p_98817_, Inventory p_98818_, TerracottaGolem p_98819_) {
        super(p_98817_, p_98818_, p_98819_.getDisplayName());
        this.golem = p_98819_;
    }


    protected void renderBg(GuiGraphics p_282553_, float p_282998_, int p_282929_, int p_283133_) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        p_282553_.blit(HORSE_INVENTORY_LOCATION, i, j, 0, 0, this.imageWidth, this.imageHeight);
        if (golem.hasChest()) {
            p_282553_.blit(HORSE_INVENTORY_LOCATION, i + 79, j + 17, 0, this.imageHeight, golem.getInventoryColumns() * 18, 63);
        }
        InventoryScreen.renderEntityInInventoryFollowsMouse(p_282553_, i + 51, j + 60, 17, (float)(i + 51) - this.xMouse, (float)(j + 75 - 50) - this.yMouse, golem);

    }

    public void render(GuiGraphics p_281697_, int p_282103_, int p_283529_, float p_283079_) {
        this.renderBackground(p_281697_);
        this.xMouse = (float)p_282103_;
        this.yMouse = (float)p_283529_;
        super.render(p_281697_, p_282103_, p_283529_, p_283079_);
        this.renderTooltip(p_281697_, p_282103_, p_283529_);
    }
}
