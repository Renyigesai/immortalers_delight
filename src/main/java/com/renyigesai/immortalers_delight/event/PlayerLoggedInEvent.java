package com.renyigesai.immortalers_delight.event;

import com.renyigesai.immortalers_delight.Config;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class PlayerLoggedInEvent {
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (Config.powerBattleModeHint && DifficultyModeUtil.isPowerBattleMode()){
            Player entity = event.getEntity();
            if (!entity.level().isClientSide){
                entity.displayClientMessage(Component.translatable("tooltip.immortalers_delight.power_battle_mode_hint"), false);
            }
        }
    }
}
