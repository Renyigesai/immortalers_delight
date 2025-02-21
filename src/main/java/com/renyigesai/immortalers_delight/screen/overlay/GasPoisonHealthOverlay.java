package com.renyigesai.immortalers_delight.screen.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.renyigesai.immortalers_delight.Config;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.GuiOverlayManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;

public class GasPoisonHealthOverlay
{
    protected static int healthIconsOffset;
    private static final ResourceLocation HEALTH_ICONS_TEXTURE = new ResourceLocation(ImmortalersDelightMod.MODID, "textures/gui/icons/gas_poison_icons.png");

    public static void init() {
        MinecraftForge.EVENT_BUS.register(new WeakPoisonHealthOverlay());
    }


    static ResourceLocation PLAYER_HEALTH_ELEMENT = new ResourceLocation("minecraft", "player_health");

    @SubscribeEvent
    public void onRenderGuiOverlayPost(RenderGuiOverlayEvent.Post event) {
        if (event.getOverlay() == GuiOverlayManager.findOverlay(PLAYER_HEALTH_ELEMENT)) {
            Minecraft mc = Minecraft.getInstance();
            ForgeGui gui = (ForgeGui) mc.gui;
            if (!mc.options.hideGui && gui.shouldDrawSurvivalElements()) {
                renderWeakPoisonOverlay(gui, event.getGuiGraphics());
            }
        }
    }

    public static void renderWeakPoisonOverlay(ForgeGui gui, GuiGraphics graphics) {
        if (!Config.weakPoisonHealthOverlay) {
            return;
        }

        healthIconsOffset = gui.leftHeight;
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        if (player == null) {
            return;
        }

        FoodData stats = player.getFoodData();
        int top = minecraft.getWindow().getGuiScaledHeight() - healthIconsOffset + 10;
        int left = minecraft.getWindow().getGuiScaledWidth() / 2 - 91;

        if (player.getEffect(ImmortalersDelightMobEffect.GAS_POISON.get()) != null) {
            drawWeakPoisonOverlay(player, minecraft, graphics, left, top);
        }
    }

    public static void drawWeakPoisonOverlay(Player player, Minecraft minecraft, GuiGraphics graphics, int left, int top) {
        int ticks = minecraft.gui.getGuiTicks();
        Random rand = new Random();
        rand.setSeed((long) (ticks * 312871));

        int health = Mth.ceil(player.getHealth());
        float absorb = Mth.ceil(player.getAbsorptionAmount());
        AttributeInstance attrMaxHealth = player.getAttribute(Attributes.MAX_HEALTH);
        float healthMax = (float) attrMaxHealth.getValue();

        int regen = -1;
        if (player.hasEffect(MobEffects.REGENERATION)) regen = ticks % 25;

        int healthRows = Mth.ceil((healthMax + absorb) / 2.0F / 10.0F);
        int rowHeight = Math.max(10 - (healthRows - 2), 3);

        int comfortSheen = ticks % 50;
        int comfortHeartFrame = comfortSheen % 2;
        int[] textureWidth = {5, 9};

        RenderSystem.setShaderTexture(0, HEALTH_ICONS_TEXTURE);
        RenderSystem.enableBlend();

        int healthMaxSingleRow = Mth.ceil(Math.min(healthMax, 20) / 2.0F);
        int leftHeightOffset = ((healthRows - 1) * rowHeight); // This keeps the overlay on the bottommost row of hearts

        for (int i = 0; i < healthMaxSingleRow; ++i) {
            int column = i % 10;
            int x = left + column * 8;
            int y = top + leftHeightOffset;

            if (health <= 4) y += rand.nextInt(2);
            if (i == regen) y -= 2;

            if (column == comfortSheen / 2) {
                graphics.blit(HEALTH_ICONS_TEXTURE, x, y, 0, 9, textureWidth[comfortHeartFrame], 9);
            }
            if (column == (comfortSheen / 2) - 1 && comfortHeartFrame == 0) {
                graphics.blit(HEALTH_ICONS_TEXTURE, x + 5, y, 5, 9, 4, 9);
            }
            float effectiveHealthOfBar = (health / 2.0F - i);
            if (effectiveHealthOfBar >= 1) {
                graphics.blit(HEALTH_ICONS_TEXTURE, x, y, 9, 9, 9, 9);
            } else if (effectiveHealthOfBar >= .5) {
                graphics.blit(HEALTH_ICONS_TEXTURE, x, y, 18, 9, 9, 9);
            }
        }

        RenderSystem.disableBlend();
    }
}

