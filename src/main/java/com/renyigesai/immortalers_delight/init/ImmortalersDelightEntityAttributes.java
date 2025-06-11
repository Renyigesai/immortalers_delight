package com.renyigesai.immortalers_delight.init;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.entities.living.*;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid =ImmortalersDelightMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ImmortalersDelightEntityAttributes {
    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(ImmortalersDelightEntities.SKELVERFISH_AMBUSHER.get(), SkelverfishAmbusher.createSkelverfishAmbusherAttributes().build());
        event.put(ImmortalersDelightEntities.SKELVERFISH_BOMBER.get(), SkelverfishBomber.createSkelverfishBomberAttributes().build());
        event.put(ImmortalersDelightEntities.SKELVERFISH_THRASHER.get(), SkelverfishThrasher.createSkelverfishThrasherAttributes().build());
        event.put(ImmortalersDelightEntities.STRANGE_ARMOUR_STAND.get(), StrangeArmourStand.createAttributes().build());
    }
}
