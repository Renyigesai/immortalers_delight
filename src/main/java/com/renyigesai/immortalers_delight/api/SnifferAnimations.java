package com.renyigesai.immortalers_delight.api;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.animal.sniffer.Sniffer;

public final class SnifferAnimations {
    public static final int HAPPY_DURATION_TICKS = 40;

    public static final EntityDataAccessor<Integer> HAPPY_TICKS =
            SynchedEntityData.defineId(Sniffer.class, EntityDataSerializers.INT);

    private SnifferAnimations() {
    }
}
