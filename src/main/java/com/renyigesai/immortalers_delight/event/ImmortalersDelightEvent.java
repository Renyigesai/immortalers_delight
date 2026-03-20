package com.renyigesai.immortalers_delight.event;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.renyigesai.immortalers_delight.Config;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.item.CustomTooltipItem;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
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

    @SubscribeEvent
    public static void addTooltip(ItemTooltipEvent event){
        ItemStack itemStack = event.getItemStack();
        if (itemStack.getItem() instanceof CustomTooltipItem tooltip){
            List<Component> component = tooltip.getComponent();
            if (component != null) {
                if (component.size() == 1) {
                    event.getToolTip().add(component.get(0));
                }else {
                    if (Screen.hasShiftDown()){
                        event.getToolTip().addAll(component);
                    }else {
                        event.getToolTip().add(Component.translatable("tooltip.immortalers_delight.tooltip_item_name_block_item").withStyle(ChatFormatting.GRAY));
                    }
                }
            }
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
            Level level = event.getEntity().level();
            level.players().forEach(player -> {
                if (player instanceof ServerPlayer serverPlayer){
                    ImmortalersDelightMod.POWER_BATTLE_MODE_TRIGGER.trigger(serverPlayer);
                }
            });
            if (!entity.level().isClientSide){
                entity.displayClientMessage(Component.translatable("tooltip.immortalers_delight.power_battle_mode_hint"), false);
            }
        }
    }
}
