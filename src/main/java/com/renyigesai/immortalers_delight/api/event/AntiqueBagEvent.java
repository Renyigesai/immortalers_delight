package com.renyigesai.immortalers_delight.api.event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
public class AntiqueBagEvent extends Event {
    public final Player player;
    public final ItemStack antique;
    private AntiqueBagEvent(Player player, ItemStack antique){
        this.player = player;
        this.antique = antique;
    }

    @Cancelable
    public static class Place extends AntiqueBagEvent{
        public Place(Player player, ItemStack antique) {
            super(player, antique);
        }
    }

    @Cancelable
    public static class Take extends AntiqueBagEvent{

        public Take(Player player, ItemStack antique) {
            super(player, antique);
        }
    }
}
