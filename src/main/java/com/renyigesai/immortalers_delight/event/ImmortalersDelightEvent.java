package com.renyigesai.immortalers_delight.event;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.renyigesai.immortalers_delight.Config;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@Mod.EventBusSubscriber

public class ImmortalersDelightEvent {

    @SubscribeEvent
    public static void furnaceFuelBurnTimeEvent(FurnaceFuelBurnTimeEvent event) {
        Integer integer = getFuels().get(event.getItemStack().getItem());
        if (integer != null){
            event.setBurnTime(integer);
        }
    }
    
    private static Map<Item,Integer> getFuels(){
        Map<Item,Integer> fuels = Maps.newLinkedHashMap();
        fuels.put(ImmortalersDelightItems.A_BUSH_LOG.get(),1200);
        fuels.put(ImmortalersDelightItems.STRIPPED_A_BUSH_LOG.get(),1200);
        fuels.put(ImmortalersDelightItems.A_BUSH_WOOD.get(),1200);
        fuels.put(ImmortalersDelightItems.STRIPPED_A_BUSH_WOOD.get(),1200);
        fuels.put(ImmortalersDelightItems.A_BUSH_PLANKS.get(),1200);
        fuels.put(ImmortalersDelightItems.A_BUSH_CABINET.get(),1200);
        fuels.put(ImmortalersDelightItems.MOON_OIL.get(),1600);
        return fuels;
    }

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
