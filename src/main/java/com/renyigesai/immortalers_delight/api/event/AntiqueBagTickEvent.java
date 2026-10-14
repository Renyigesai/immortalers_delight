package com.renyigesai.immortalers_delight.api.event;

import com.renyigesai.immortalers_delight.item.antique.AntiqueBagHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.LogicalSide;

public class AntiqueBagTickEvent extends TickEvent {
    public final Player player;
    public final AntiqueBagHandler.BagRecord bagRecord;

    public AntiqueBagTickEvent(Phase phase, Player player, AntiqueBagHandler.BagRecord bagRecord) {
        super(TickEvent.Type.PLAYER, player instanceof ServerPlayer ? LogicalSide.SERVER : LogicalSide.CLIENT, phase);
        this.player = player;
        this.bagRecord = bagRecord;
    }
}
