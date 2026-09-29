package com.renyigesai.immortalers_delight.screen.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.capabilitiy.EffectOverlayPlayerCapability;
import com.renyigesai.immortalers_delight.capabilitiy.ImmortalersCapabilities;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightTags;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.common.capabilities.Capability;

@OnlyIn(Dist.CLIENT)
public class FoodProgressIndicatorHUD implements IGuiOverlay {
    // 定义一个静态常量 hud，用于存储 Hud 的单例对象
    private static final FoodProgressIndicatorHUD hud = new FoodProgressIndicatorHUD();
    // 定义两个 ResourceLocation 对象，用于存储 HUD 纹理的路径
    private static final ResourceLocation HUD_BACKGROUND = new ResourceLocation(ImmortalersDelightMod.MODID, "textures/gui/icons/eat_warming.png");
    private static final ResourceLocation HUD_LAYER = new ResourceLocation(ImmortalersDelightMod.MODID, "textures/gui/icons/eat_warming_layer.png");
    public FoodProgressIndicatorHUD() {
    }

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {

        Player player = gui.getMinecraft().player;
        if (player == null) {
            return;
        }

        if (player.getItemInHand(InteractionHand.MAIN_HAND).is(ImmortalersDelightTags.Items.NEED_PROGRESS)
        || player.getItemInHand(InteractionHand.OFF_HAND).is(ImmortalersDelightTags.Items.NEED_PROGRESS)) {
            //计算食用进度
            float f1 = 1;
            float f2 = 0;
            ItemStack using = player.getUseItem();
            if (using.is(ImmortalersDelightTags.Items.NEED_PROGRESS)) {
                f2 = f1 - (partialTick + player.getUseItemRemainingTicks()) / using.getItem().getUseDuration(using);
            }
            renderProgressBar(guiGraphics,f2,screenWidth,screenHeight);
        }
    }

    protected void renderProgressBar(GuiGraphics guiGraphics, float f2, int screenWidth, int screenHeight) {
        //计算坐标与尺寸
        int totalWidth = 32;
        int totalHeight = 32;
        int x = screenWidth / 2 - totalWidth / 2; // 居中: screenWidth / 2 - 16
        int y = screenHeight / 2 + 32;           // 下方偏移 32 像素

        //限制并计算进度对应的动态像素宽度 (0 ~ 32)
        float clampedProgress = Math.max(0.0F, Math.min(1.0F, f2));
        int progressWidth = (int) (totalWidth * clampedProgress);

        //准备渲染状态（支持半透明Alpha混合）
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);

        //设置透明度
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);

        //绘制底层背景 (完整 32x32)
        guiGraphics.blit(HUD_BACKGROUND, x, y, 0, 0, totalWidth, totalHeight, totalWidth, totalHeight);

        //绘制顶层进度 (从左到右裁剪)
        if (progressWidth > 0) {
            // 参数依次为: 纹理, 屏幕X, 屏幕Y, U起点, V起点, 绘制宽度, 绘制高度, 纹理总宽, 纹理总高
            guiGraphics.blit(HUD_LAYER, x, y, 0, 0, progressWidth, totalHeight, totalWidth, totalHeight);
        }

        //重置状态
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
    }

    // 提供一个静态方法用于获取 Hud 的单例对象
    public static FoodProgressIndicatorHUD getInstance() {
        return hud;
    }

}
