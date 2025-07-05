package com.renyigesai.immortalers_delight.init;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.entities.boat.AncientWoodBoat;
import com.renyigesai.immortalers_delight.entities.boat.AncientWoodChestBoat;
import com.renyigesai.immortalers_delight.entities.boat.ImmortalersBoat;
import com.renyigesai.immortalers_delight.entities.boat.ImmortalersChestBoat;
import com.renyigesai.immortalers_delight.entities.living.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
@Mod.EventBusSubscriber
public class ImmortalersDelightEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ImmortalersDelightMod.MODID);
    public static final RegistryObject<EntityType<ImmortalersBoat>> IMMORTAL_BOAT =
            ENTITY_TYPES.register("immortal_boat", () -> EntityType.Builder.<ImmortalersBoat>of(ImmortalersBoat::new, MobCategory.MISC)
                    .sized(1.375f, 0.5625f).build("immortal_boat"));
    public static final RegistryObject<EntityType<ImmortalersChestBoat>> IMMORTAL_CHEST_BOAT =
            ENTITY_TYPES.register("immortal_chest_boat", () -> EntityType.Builder.<ImmortalersChestBoat>of(ImmortalersChestBoat::new, MobCategory.MISC)
                    .sized(1.375f, 0.5625f).build("immortal_chest_boat"));

    public static final RegistryObject<EntityType<AncientWoodBoat>> ANCIENT_WOOD_BOAT =
            ENTITY_TYPES.register("ancient_wood_boat", () -> EntityType.Builder.<AncientWoodBoat>of(AncientWoodBoat::new, MobCategory.MISC)
                    .sized(2.875f, 0.625f).build("ancient_wood_boat"));
    public static final RegistryObject<EntityType<AncientWoodChestBoat>> ANCIENT_WOOD_CHEST_BOAT =
            ENTITY_TYPES.register("ancient_wood_chest_boat", () -> EntityType.Builder.<AncientWoodChestBoat>of(AncientWoodChestBoat::new, MobCategory.MISC)
                    .sized(2.875f, 0.625f).build("ancient_wood_chest_boat"));

    public static final RegistryObject<EntityType<SkelverfishAmbusher>> SKELVERFISH_AMBUSHER =
            ENTITY_TYPES.register("skelverfish_ambusher", () -> EntityType.Builder.of(SkelverfishAmbusher::new, MobCategory.MONSTER)
                    .sized(0.4f, 0.3f).build("skelverfish_ambusher"));
    public static final RegistryObject<EntityType<SkelverfishBomber>> SKELVERFISH_BOMBER =
            ENTITY_TYPES.register("skelverfish_bomber", () -> EntityType.Builder.of(SkelverfishBomber::new, MobCategory.MONSTER)
                    .sized(0.5f, 0.4f).build("skelverfish_bomber"));
    public static final RegistryObject<EntityType<SkelverfishThrasher>> SKELVERFISH_THRASHER =
            ENTITY_TYPES.register("skelverfish_thrasher", () -> EntityType.Builder.of(SkelverfishThrasher::new, MobCategory.MONSTER)
                    .sized(0.5f, 0.4f).build("skelverfish_thrasher"));

    public static final RegistryObject<EntityType<StrangeArmourStand>> STRANGE_ARMOUR_STAND =
            ENTITY_TYPES.register("strange_armour_stand", () -> EntityType.Builder.of(StrangeArmourStand::new, MobCategory.MONSTER)
                    .sized(0.6f, 2.0f).build("strange_armour_stand"));

}
