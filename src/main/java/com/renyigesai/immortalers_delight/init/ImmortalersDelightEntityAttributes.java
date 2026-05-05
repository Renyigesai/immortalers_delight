package com.renyigesai.immortalers_delight.init;
import net.neoforged.fml.common.EventBusSubscriber;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.entities.living.*;
import com.renyigesai.immortalers_delight.entities.living.illager_archaeological_team.Scavenger;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;

@EventBusSubscriber(modid =ImmortalersDelightMod.MODID)
public class ImmortalersDelightEntityAttributes {
    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(ImmortalersDelightEntities.SKELVERFISH_AMBUSHER.get(), SkelverfishAmbusher.createSkelverfishAmbusherAttributes().build());
        event.put(ImmortalersDelightEntities.SKELVERFISH_BOMBER.get(), SkelverfishBomber.createSkelverfishBomberAttributes().build());
        event.put(ImmortalersDelightEntities.SKELVERFISH_THRASHER.get(), SkelverfishThrasher.createSkelverfishThrasherAttributes().build());
        event.put(ImmortalersDelightEntities.STRANGE_ARMOUR_STAND.get(), StrangeArmourStand.createAttributes().build());
        event.put(ImmortalersDelightEntities.SCAVENGER.get(), Scavenger.createScavengerAttributes().build());
        event.put(ImmortalersDelightEntities.TERRACOTTA_GOLEM.get(), TerracottaGolem.createTerracottaGolemAttributes().build());
    }
}
