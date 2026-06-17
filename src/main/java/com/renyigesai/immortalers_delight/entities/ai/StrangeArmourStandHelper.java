package com.renyigesai.immortalers_delight.entities.ai;

import com.renyigesai.immortalers_delight.entities.living.StrangeArmourStand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StrangeArmourStandHelper {
    private static final Map<UUID, Integer> strangeArmourStandHurt = new HashMap<>();
    public static boolean isStrangeArmourStandLost(Level level, UUID uuid) {
        if (strangeArmourStandHurt.isEmpty()) return false;
        if (level instanceof ServerLevel serverLevel) {
            Entity entity = serverLevel.getEntity(uuid);
            if (entity != null && !entity.isRemoved() && entity instanceof StrangeArmourStand stand) {
                if (strangeArmourStandHurt.get(uuid) != null
                        && strangeArmourStandHurt.get(uuid) < stand.tickCount) strangeArmourStandHurt.remove(uuid);
            }
        }
        return strangeArmourStandHurt.containsKey(uuid);
    }
    public static void setStrangeArmourStandLost(StrangeArmourStand entity, int time) {
        int ticks = entity.tickCount;
        strangeArmourStandHurt.put(entity.getUUID(), ticks + time);
    }
}
