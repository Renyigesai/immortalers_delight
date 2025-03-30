package com.renyigesai.immortalers_delight.init;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.entities.ImmortalersBoat;
import com.renyigesai.immortalers_delight.entities.ImmortalersChestBoat;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ImmortalersDelightEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ImmortalersDelightMod.MODID);
    public static final RegistryObject<EntityType<ImmortalersBoat>> HIMEKAIDO_BOAT =
            ENTITY_TYPES.register("immortal_boat", () -> EntityType.Builder.<ImmortalersBoat>of(ImmortalersBoat::new, MobCategory.MISC)
                    .sized(1.375f, 0.5625f).build("immortal_boat"));
    public static final RegistryObject<EntityType<ImmortalersChestBoat>> HIMEKAIDO_CHEST_BOAT =
            ENTITY_TYPES.register("immortal_chest_boat", () -> EntityType.Builder.<ImmortalersChestBoat>of(ImmortalersChestBoat::new, MobCategory.MISC)
                    .sized(1.375f, 0.5625f).build("immortal_chest_boat"));

}
