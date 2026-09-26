package com.renyigesai.immortalers_delight.screen.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.capabilitiy.EffectOverlayPlayerCapability;
import com.renyigesai.immortalers_delight.capabilitiy.ImmortalersCapabilities;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.potion.BaseMobEffect;
import com.renyigesai.immortalers_delight.potion.CrispFortitudeMobEffect;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.common.capabilities.Capability;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@OnlyIn(Dist.CLIENT)
public class CrispFortitudeHUD implements IGuiOverlay {
    // 定义一个静态常量 hud，用于存储 Hud 的单例对象
    private static final CrispFortitudeHUD hud = new CrispFortitudeHUD();
    // 定义一个 ResourceLocation 对象，用于存储 HUD 纹理的路径
    private final ResourceLocation HUD = new ResourceLocation(ImmortalersDelightMod.MODID, "textures/gui/icons/crisp_fortitude.png");
    //缓存从玩家能力那获取的数据值


    public CrispFortitudeHUD() {
    }
    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {

        //System.out.println("火成HUD渲染");
        Player player = gui.getMinecraft().player;
        if (player == null) {
            System.out.println("内在酥质HUD：错误的GUI渲染上下文");
            return;
        }

        MobEffectInstance effectInstance = player.getEffect(ImmortalersDelightMobEffect.CRISP_FORTITUDE.get());
        if (effectInstance != null && effectInstance.getEffect() instanceof BaseMobEffect effect) {
            //拿生效等级
            int lv = effect.getTruthUsingAmplifier(effectInstance.getAmplifier());
            //计算叠层上限
            int maxCharge = CrispFortitudeMobEffect.getMaxCharges(lv);
            //计算透明度
            float f2 = (float) CrispFortitudeMobEffect.getEntitiesHurt().get(player.getUUID()) / maxCharge;
            if (f2 > 0.05 && f2 <= 1) {
                this.renderTextureOverlay(guiGraphics, HUD, f2, screenWidth, screenHeight);
            }

        }
        //获取能力，绘制hud
//        Capability<EffectOverlayPlayerCapability> playerEffectOverlay = ImmortalersCapabilities.PLAYER_EFFECT_OVERLAY;
//
//
//        gui.getMinecraft().player.getCapability(playerEffectOverlay).ifPresent((cap) -> {
//            float f2 = cap.getInfernalForgingData();
//            if (f2 > 0.05 && f2 <= 1) {
//                this.renderTextureOverlay(guiGraphics, HUD, f2, screenWidth, screenHeight);
//            }
//        });

//        // 在屏幕中央上方绘制 HUD 纹理，宽高为 32x32 像素
//        //参数是含义，x坐标，y坐标，u，v坐标，宽高，uv宽高。
//        guiGraphics.blit(HUD,screenWidth/2-16,screenHeight/2-64,0,0,32,32,32,32);
    }

    protected void renderTextureOverlay(GuiGraphics pGuiGraphics, ResourceLocation pShaderLocation, float pAlpha, int screenWidth, int screenHeight) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, pAlpha);
        pGuiGraphics.blit(pShaderLocation, 0, 0, -90, 0.0F, 0.0F, screenWidth,screenHeight, screenWidth, screenHeight);
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
    // 提供一个静态方法用于获取 Hud 的单例对象
    public static CrispFortitudeHUD getInstance() {
        return hud;
    }

}
