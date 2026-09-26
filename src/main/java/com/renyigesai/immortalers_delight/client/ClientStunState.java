package com.renyigesai.immortalers_delight.client;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.capabilitiy.EffectOverlayPlayerCapability;
import com.renyigesai.immortalers_delight.capabilitiy.ImmortalersCapabilities;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
public class ClientStunState {
    private static int remainingTicks = 0;
    private static float magnitude = 0;

    public static void setMagnitude(float magnitude) {ClientStunState.magnitude = magnitude;}

    public static float getMagnitude() {return magnitude;}

    public static void setRemainingTicks(int ticks) {
        remainingTicks = Math.max(ticks, 0);
    }

    public static int getRemainingTicks() {
        return remainingTicks;
    }

    public static boolean isStunned() {
        return remainingTicks > 0;
    }

    /** 每客户端tick自然递减，避免依赖高频网络同步 */
    public static void tickDown() {
        if (remainingTicks > 0) remainingTicks--;
    }

    @OnlyIn(Dist.CLIENT)
    @Mod.EventBusSubscriber(modid = ImmortalersDelightMod.MODID, value = Dist.CLIENT)
    public static class StunClientEvents {
        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase != TickEvent.Phase.END) return;
            if (Minecraft.getInstance().player == null) return;

            //拿能力，输入渲染数据
            Capability<EffectOverlayPlayerCapability> playerEffectOverlay = ImmortalersCapabilities.PLAYER_EFFECT_OVERLAY;
            Minecraft.getInstance().player.getCapability(playerEffectOverlay).ifPresent((cap) -> {
                float f2 = cap.getStunData();
                if (f2 > 1) {
                    int time = (int) f2;
                    setRemainingTicks(time);
                    setMagnitude(f2-time);
                    cap.setStunData(0);
                }
            });
            ClientStunState.tickDown();
        }
    }
}
