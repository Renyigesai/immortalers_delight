package com.renyigesai.immortalers_delight.event;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.tags.DamageTypeTags;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityInvulnerabilityCheckEvent;

/**
 * When an entity is marked with immortalers_delight_fear_fire (破烟 + 燃起来了),
 * fire damage must not be blocked by vanilla fire immunity.
 */
@EventBusSubscriber(modid = ImmortalersDelightMod.MODID)
public final class FearFireImmunityHandler {
    private FearFireImmunityHandler() {}

    @SubscribeEvent
    public static void onInvulnerabilityCheck(EntityInvulnerabilityCheckEvent event) {
        if (event.getSource().is(DamageTypeTags.IS_FIRE)
                && event.getEntity().getPersistentData().contains("immortalers_delight_fear_fire")) {
            event.setInvulnerable(false);
        }
    }
}
