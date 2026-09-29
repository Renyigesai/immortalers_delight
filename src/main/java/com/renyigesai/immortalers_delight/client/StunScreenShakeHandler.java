package com.renyigesai.immortalers_delight.client;


import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class StunScreenShakeHandler {

    private static final float MAX_AMPLITUDE = 25f;
    private static final int FADE_WINDOW = 20; // 剩余不足这个tick数时开始淡出，1秒

    @SubscribeEvent
    public static void onCameraSetup(ViewportEvent.ComputeCameraAngles event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || mc.isPaused()) return;
        if (!ClientStunState.isStunned()) return;

        float amplitude = getShakeAmplitude();
        if (amplitude <= 0f) return;

        float delta = mc.getFrameTime();
        float ticksExistedDelta = player.tickCount + delta;

        event.setPitch((float) (event.getPitch() + amplitude * Math.cos(ticksExistedDelta * 3 + 2) * MAX_AMPLITUDE));
        event.setYaw((float) (event.getYaw() + amplitude * Math.cos(ticksExistedDelta * 5 + 1) * MAX_AMPLITUDE));
        event.setRoll((float) (event.getRoll() + amplitude * Math.cos(ticksExistedDelta * 4) * MAX_AMPLITUDE));
    }

    /** 固定基础强度，末尾按剩余时间做平方衰减，避免结束瞬间的生硬感 */
    private static float getShakeAmplitude() {
        float baseAmplitude = 0.03f;
        int remaining = ClientStunState.getRemainingTicks();
        float buffer = ClientStunState.getMagnitude();
        if (buffer > baseAmplitude) baseAmplitude = buffer;
        if (remaining >= FADE_WINDOW) return baseAmplitude;
        float frac = remaining / (float) FADE_WINDOW;
        return baseAmplitude * frac * frac;
    }
}
