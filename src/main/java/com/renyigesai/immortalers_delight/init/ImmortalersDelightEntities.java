package com.renyigesai.immortalers_delight.init;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.entities.ImmortalersBoat;
import com.renyigesai.immortalers_delight.entities.ImmortalersChestBoat;
import com.renyigesai.immortalers_delight.entities.living.SkelverfishBase;
import com.renyigesai.immortalers_delight.entities.living.SkelverfishBomber;
import com.renyigesai.immortalers_delight.entities.living.SkelverfishThrasher;
import com.renyigesai.immortalers_delight.entities.living.StrangeArmourStand;
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
    public static final RegistryObject<EntityType<ImmortalersBoat>> HIMEKAIDO_BOAT =
            ENTITY_TYPES.register("immortal_boat", () -> EntityType.Builder.<ImmortalersBoat>of(ImmortalersBoat::new, MobCategory.MISC)
                    .sized(1.375f, 0.5625f).build("immortal_boat"));
    public static final RegistryObject<EntityType<ImmortalersChestBoat>> HIMEKAIDO_CHEST_BOAT =
            ENTITY_TYPES.register("immortal_chest_boat", () -> EntityType.Builder.<ImmortalersChestBoat>of(ImmortalersChestBoat::new, MobCategory.MISC)
                    .sized(1.375f, 0.5625f).build("immortal_chest_boat"));

    public static final RegistryObject<EntityType<SkelverfishBase>> SKELVERFISH_AMBUSHER =
            ENTITY_TYPES.register("skelverfish_ambusher", () -> EntityType.Builder.of(SkelverfishBase::new, MobCategory.MONSTER)
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
