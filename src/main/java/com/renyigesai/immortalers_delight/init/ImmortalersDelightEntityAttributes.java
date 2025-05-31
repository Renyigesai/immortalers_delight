package com.renyigesai.immortalers_delight.init;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.entities.living.SkelverfishBase;
import com.renyigesai.immortalers_delight.entities.living.SkelverfishBomber;
import com.renyigesai.immortalers_delight.entities.living.StrangeArmourStand;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid =ImmortalersDelightMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ImmortalersDelightEntityAttributes {
    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(ImmortalersDelightEntities.SKELVERFISH_AMBUSHER.get(), SkelverfishBase.createAttributes().build());
        event.put(ImmortalersDelightEntities.SKELVERFISH_BOMBER.get(), SkelverfishBomber.createAttributes().build());
        event.put(ImmortalersDelightEntities.SKELVERFISH_THRASHER.get(), SkelverfishBomber.createAttributes().build());
        event.put(ImmortalersDelightEntities.STRANGE_ARMOUR_STAND.get(), StrangeArmourStand.createAttributes().build());
    }
}
