package com.renyigesai.immortalers_delight.init;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.entities.boat.AncientWoodBoat;
import com.renyigesai.immortalers_delight.entities.boat.AncientWoodChestBoat;
import com.renyigesai.immortalers_delight.entities.boat.ImmortalersBoat;
import com.renyigesai.immortalers_delight.entities.boat.ImmortalersChestBoat;
import com.renyigesai.immortalers_delight.entities.living.*;
import com.renyigesai.immortalers_delight.entities.living.illager_archaeological_team.Scavenger;
import com.renyigesai.immortalers_delight.entities.projectile.*;
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

    public static final RegistryObject<EntityType<Scavenger>> SCAVENGER =
            ENTITY_TYPES.register("scavenger", () -> EntityType.Builder.of(Scavenger::new, MobCategory.MONSTER)
                    .sized(0.6f, 2.0f).build("scavenger"));

    public static final RegistryObject<EntityType<SurveyorFangEntity>> SURVEYOR_FANG =
            ENTITY_TYPES.register("sword_tipped_long_pole", () -> EntityType.Builder.<SurveyorFangEntity>of(SurveyorFangEntity::new, MobCategory.MISC)
            .sized(0.6F, 2.5F)
            .clientTrackingRange(6)
            .updateInterval(2)
            .fireImmune()
            .build("sword_tipped_long_pole"));

    public static final RegistryObject<EntityType<EffectCloudBaseEntity>> BASE_EFFECT_CLOUD =
            ENTITY_TYPES.register("base_effect_cloud", () -> EntityType.Builder.<EffectCloudBaseEntity>of(EffectCloudBaseEntity::new, MobCategory.MISC)
                    .fireImmune()
                    .sized(6.0F, 6.0F)
                    .clientTrackingRange(10)
                    .updateInterval(Integer.MAX_VALUE)
                    .build("base_effect_cloud")
            );

    public static final RegistryObject<EntityType<GasCloudEntity>> GAS_EFFECT_CLOUD =
            ENTITY_TYPES.register("gas_effect_cloud", () -> EntityType.Builder.<GasCloudEntity>of(GasCloudEntity::new, MobCategory.MISC)
                    .fireImmune()
                    .sized(6.0F, 6.0F)
                    .clientTrackingRange(10)
                    .updateInterval(Integer.MAX_VALUE)
                    .build("gas_effect_cloud")
            );

    public static final RegistryObject<EntityType<WarpedLaurelHitBoxEntity>> WARPED_LAUREL_HITBOX =
            ENTITY_TYPES.register("warped_laurel_hitbox", () -> EntityType.Builder.<WarpedLaurelHitBoxEntity>of(WarpedLaurelHitBoxEntity::new, MobCategory.MISC)
                    .fireImmune()
                    .sized(6.0F, 6.0F)
                    .clientTrackingRange(10)
                    .updateInterval(Integer.MAX_VALUE)
                    .build("warped_laurel_hitbox")
            );

    public static final RegistryObject<EntityType<KiBlastEntity>> KI_BLAST =
            ENTITY_TYPES.register("ki_blast", () -> EntityType.Builder.<KiBlastEntity>of(KiBlastEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("ki_blast"));

    public static final RegistryObject<EntityType<ToxicGasGrenadeEntity>> CAUSTIC_ESSENTIAL_OIL =
            ENTITY_TYPES.register("caustic_essential_oil", () -> EntityType.Builder.<ToxicGasGrenadeEntity>of(ToxicGasGrenadeEntity::new, MobCategory.MISC)
                    .sized(0.375F, 0.375F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("caustic_essential_oil"));


    public static final RegistryObject<EntityType<MoonArrowHitboxEntity>> MOON_ARROW_HITBOX =
            ENTITY_TYPES.register("moon_arrow_hitbox", () -> EntityType.Builder.<MoonArrowHitboxEntity>of(MoonArrowHitboxEntity::new, MobCategory.MISC)
                    .fireImmune()
                    .sized(6.0F, 6.0F)
                    .clientTrackingRange(6)
                    .updateInterval(20)
                    .build("moon_arrow_hitbox"));

    public static final RegistryObject<EntityType<TerracottaGolem>> TERRACOTTA_GOLEM =
            ENTITY_TYPES.register("terracotta_golem", () -> EntityType.Builder.of(TerracottaGolem::new, MobCategory.MISC)
                    .sized(2.0f, 2.0f).clientTrackingRange(10).build("terracotta_golem"));
}
