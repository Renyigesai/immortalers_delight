package com.renyigesai.immortalers_delight;

import net.neoforged.fml.common.EventBusSubscriber;



import com.mojang.logging.LogUtils;

import com.renyigesai.immortalers_delight.advancement.*;

import com.renyigesai.immortalers_delight.client.model.*;

import com.renyigesai.immortalers_delight.client.model.projectile.*;

import com.renyigesai.immortalers_delight.client.renderer.*;

import com.renyigesai.immortalers_delight.client.renderer.entity.*;

import com.renyigesai.immortalers_delight.client.renderer.entity.projectile.*;

import com.renyigesai.immortalers_delight.fluid.ImmortalersDelightFluidTypes;

import com.renyigesai.immortalers_delight.fluid.ImmortalersDelightFluids;

import com.renyigesai.immortalers_delight.init.*;

import com.renyigesai.immortalers_delight.item.weapon.RepeatingCrossbowItem;

import com.renyigesai.immortalers_delight.recipe.ImmortalersDelightRecipeTypes;

import com.renyigesai.immortalers_delight.network.ImmortalersNetwork;

import com.renyigesai.immortalers_delight.screen.EnchantalCoolerScreen;

import com.renyigesai.immortalers_delight.screen.TerracottaGolemScreen;

import com.renyigesai.immortalers_delight.screen.overlay.*;

import net.minecraft.client.Minecraft;

import net.minecraft.core.registries.Registries;

import net.minecraft.resources.ResourceKey;

import net.minecraft.client.model.IllagerModel;

import net.minecraft.client.model.geom.ModelLayerLocation;

import net.minecraft.client.model.geom.builders.LayerDefinition;

import net.minecraft.client.renderer.ItemBlockRenderTypes;

import net.minecraft.client.renderer.RenderType;

import net.minecraft.client.renderer.entity.EntityRenderers;

import net.minecraft.client.renderer.item.ItemProperties;

import net.minecraft.resources.ResourceLocation;

import net.minecraft.world.item.Items;

import net.neoforged.api.distmarker.Dist;

import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import net.neoforged.neoforge.common.NeoForge;

import net.neoforged.neoforge.event.server.ServerStartingEvent;

import net.neoforged.neoforge.registries.RegisterEvent;

import net.neoforged.bus.api.IEventBus;

import net.neoforged.bus.api.SubscribeEvent;

import net.neoforged.fml.ModList;

import net.neoforged.fml.ModLoadingContext;

import net.neoforged.fml.common.Mod;

import net.neoforged.fml.config.ModConfig;

import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

import org.slf4j.Logger;

import java.util.*;

import java.util.function.Supplier;



@Mod(ImmortalersDelightMod.MODID)

public class ImmortalersDelightMod {



    public static final String MODID = "immortalers_delight";

    public static final Logger LOGGER = LogUtils.getLogger();



    public static final LevelUpEnchantmentTrigger LEVEL_UP_ENCHANTMENT_TRIGGER = new LevelUpEnchantmentTrigger();

    public static final PassSnifferCoolDownTrigger PASS_SNIFFER_COOLDOWN_TRIGGER = new PassSnifferCoolDownTrigger();

    public static final ResistGasPoisoningTrigger RESIST_GAS_POISONING_TRIGGER = new ResistGasPoisoningTrigger();

    public static final PowerBattleModeTrigger POWER_BATTLE_MODE_TRIGGER = new PowerBattleModeTrigger();

    public static final ImmBoatUpgradeTrigger IMM_BOAT_UPGRADE_TRIGGER = new ImmBoatUpgradeTrigger();





    public static final long RANDOM_SEED = System.currentTimeMillis();

    public static final Random RANDOM = new Random(RANDOM_SEED);



    public ImmortalersDelightMod(IEventBus modEventBus) {

        NeoForge.EVENT_BUS.register(this);

        modEventBus.addListener(CommonSetup::init);

        modEventBus.addListener(ImmortalersDelightMod::registerCriterionTriggers);

        modEventBus.addListener(ImmortalersNetwork::register);



        ImmortalersDelightItems.REGISTER.register(modEventBus);

        ImmortalersDelightBlocks.register(modEventBus);

        ImmortalersDelightBlockEntityTypes.TILES.register(modEventBus);

        ImmortalersDelightBlocks.BLOCK_ENTITY_REGISTRY.register(modEventBus);

        ImmortalersDelightGroup.CREATIVE_TABS.register(modEventBus);

        ImmortalersDelightFluidTypes.REGISTRY.register(modEventBus);

        ImmortalersDelightFluids.REGISTRY.register(modEventBus);

        ImmortalersDelightMobEffect.REGISTRY.register(modEventBus);

        ImmortalersDelightParticleTypes.REGISTRY.register(modEventBus);

        ImmortalersDelightPotions.REGISTRY.register(modEventBus);

        ImmortalersDelightBiomeFeatures.FEATURES.register(modEventBus);

        ImmortalersDelightMenuTypes.MENUS.register(modEventBus);



        ImmortalersDelightEntities.ENTITY_TYPES.register(modEventBus);

        ImmortalersDelightLootModifierSerializers.register(modEventBus);

        ImmortalersDelightRecipeTypes.register(modEventBus);

        ModLoadingContext.get().getActiveContainer().registerConfig(ModConfig.Type.COMMON, Config.SPEC);

    }



    private static void registerCriterionTriggers(RegisterEvent event) {

        event.register(Registries.TRIGGER_TYPE, helper -> {

            helper.register(ResourceKey.create(Registries.TRIGGER_TYPE, ResourceLocation.fromNamespaceAndPath(MODID, "level_up_enchantment")), LEVEL_UP_ENCHANTMENT_TRIGGER);

            helper.register(ResourceKey.create(Registries.TRIGGER_TYPE, ResourceLocation.fromNamespaceAndPath(MODID, "pass_sniffer_cooldown")), PASS_SNIFFER_COOLDOWN_TRIGGER);

            helper.register(ResourceKey.create(Registries.TRIGGER_TYPE, ResourceLocation.fromNamespaceAndPath(MODID, "resist_gas_poisoning")), RESIST_GAS_POISONING_TRIGGER);

            helper.register(ResourceKey.create(Registries.TRIGGER_TYPE, ResourceLocation.fromNamespaceAndPath(MODID, "power_battle_mode")), POWER_BATTLE_MODE_TRIGGER);

            helper.register(ResourceKey.create(Registries.TRIGGER_TYPE, ResourceLocation.fromNamespaceAndPath(MODID, "imm_boat_upgrade")), IMM_BOAT_UPGRADE_TRIGGER);

        });

    }



    @SubscribeEvent

    public void onServerStarting(ServerStartingEvent event) {

        LOGGER.info("HELLO from server starting");

    }



    @EventBusSubscriber(modid = MODID, value = Dist.CLIENT)

    public static class ClientModEvents {

        private static final Map<ModelLayerLocation, Supplier<LayerDefinition>> modelLayers = new HashMap<>();

        @SubscribeEvent

        public static void registerModelLayerListener(EntityRenderersEvent.RegisterLayerDefinitions event) {

            modelLayers.put(SkelverfishBomberModel.SKELVERFISH_BOMBER, SkelverfishBomberModel::createBodyLayer);

            modelLayers.put(SkelverfishThrasherModel.SKELVERFISH_THRASHER, SkelverfishThrasherModel::createBodyLayer);

            modelLayers.put(StrangeArmourStandModel.STRANGE_ARMOUR_STAND, StrangeArmourStandModel::createBodyLayer);

            modelLayers.put(TerracottaGolemModel.TERRACOTTA_GOLEM, TerracottaGolemModel::createBodyLayer);

            modelLayers.put(TerracottaGolemSideModel.TERRACOTTA_GOLEM_SIDE_LAYER, TerracottaGolemSideModel::createBodyLayer);

            modelLayers.put(AncientWoodBoatModel.ANCIENT_BOAT, AncientWoodBoatModel::createBodyLayer);

            modelLayers.put(AncientWoodChestBoatModel.ANCIENT_CHEST_BOAT, AncientWoodChestBoatModel::createBodyLayer);

            modelLayers.put(ScavengerModel.SCARVENGER_MODEL, IllagerModel::createBodyLayer);

            modelLayers.put(SurveyorFangModel.SURVEYOR_FANG, SurveyorFangModel::createBodyLayer);

            modelLayers.put(EffectCloudModel.EFFECT_CLOUD_BASE, EffectCloudModel::createBodyLayer);

            modelLayers.put(HugeSmokeParticleModel.HUGE_SMOKE_PARTICLE, HugeSmokeParticleModel::createBodyLayer);

            modelLayers.put(AlfalfaDababaModel.ALFALFA_DABABA, AlfalfaDababaModel::createBodyLayer);

            modelLayers.put(KiBlastRenderer.MODEL_LOCATION, KiBlastRenderer::createSkullLayer);

            modelLayers.put(WarpedLaurelHitBoxModel.LAYER_LOCATION, WarpedLaurelHitBoxModel::createBodyLayer);

            modelLayers.put(ToxicGasGrenadeRenderer.MODEL_LOCATION, ToxicGasGrenadeRenderer::createSkullLayer);

            modelLayers.put(MoonlightBeamModel.LAYER_LOCATION, MoonlightBeamModel::createBodyLayer);

            modelLayers.put(MoonArrowHitboxModel.LAYER_LOCATION, MoonArrowHitboxModel::createBodyLayer);

            modelLayers.put(BreadOfWarModel.BREAD_OF_WAR, BreadOfWarModel::createBodyLayer);

            modelLayers.put(RotatingRoastMeatModel.ROTATING_ROAST_MEAT,RotatingRoastMeatModel::createBodyLayer);



            for (Map.Entry<ModelLayerLocation, Supplier<LayerDefinition>> entry : modelLayers.entrySet()) {

                event.registerLayerDefinition(entry.getKey(), entry.getValue());

            }

        }

        @SubscribeEvent

        public static void registerMenuScreens(RegisterMenuScreensEvent event) {

            event.register(ImmortalersDelightMenuTypes.ENCHANTAL_COOLER_MENU.get(), EnchantalCoolerScreen::new);

            event.register(ImmortalersDelightMenuTypes.TERRACOTTA_GOLEM_MENU.get(), TerracottaGolemScreen::new);

        }



        @SubscribeEvent

        public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {

            event.registerBlockEntityRenderer(ImmortalersDelightBlockEntityTypes.SIGN.get(), ImmortalersDelightSignRenderer::new);

            event.registerBlockEntityRenderer(ImmortalersDelightBlockEntityTypes.HANGING_SIGN.get(), ImmortalersDelightHangingSignRenderer::new);

            event.registerBlockEntityRenderer(ImmortalersDelightBlocks.ANCIENT_STOVE_ENTITY.get(), AncientStoveBlockEntityRenderer::new);

            event.registerBlockEntityRenderer(ImmortalersDelightBlocks.SUPPORT_BLOCK_ENTITY.get(), SupportBlockEntityRenderer::new);

            event.registerBlockEntityRenderer(ImmortalersDelightBlocks.UNFINISHED_TANGYUAN_ENTITY.get(),TangyuanBlockEntityRenderer::new);

            event.registerBlockEntityRenderer(ImmortalersDelightBlocks.SUSPICIOUS_ASH_PILE_BLOCK_ENTITY.get(),NaanPitBlockRenderer::new);

            event.registerBlockEntityRenderer(ImmortalersDelightBlocks.ROTATING_ROAST_MEAT_ENTITY.get(),RotatingRoastMeatRenderer::new);



            event.registerEntityRenderer(ImmortalersDelightEntities.SKELVERFISH_AMBUSHER.get(), SkelverfishRenderer::new);

            event.registerEntityRenderer(ImmortalersDelightEntities.SKELVERFISH_BOMBER.get(), SkelverfishBomberRenderer::new);

            event.registerEntityRenderer(ImmortalersDelightEntities.SKELVERFISH_THRASHER.get(), SkelverfishThrasherRenderer::new);

            event.registerEntityRenderer(ImmortalersDelightEntities.STRANGE_ARMOUR_STAND.get(), StrangeArmourStandRenderer::new);

            event.registerEntityRenderer(ImmortalersDelightEntities.SCAVENGER.get(), ScavengerRenderer::new);

            event.registerEntityRenderer(ImmortalersDelightEntities.TERRACOTTA_GOLEM.get(), TerracottaGolemRenderer::new);

            event.registerEntityRenderer(ImmortalersDelightEntities.SURVEYOR_FANG.get(),SurveyorFangRenderer::new);

            event.registerEntityRenderer(ImmortalersDelightEntities.BASE_EFFECT_CLOUD.get(),EffectCloudBaseRenderer::new);

            event.registerEntityRenderer(ImmortalersDelightEntities.GAS_EFFECT_CLOUD.get(), GasCloudRenderer::new);

            event.registerEntityRenderer(ImmortalersDelightEntities.WARPED_LAUREL_HITBOX.get(), WarpedLaurelHitBoxRenderer::new);

            event.registerEntityRenderer(ImmortalersDelightEntities.KI_BLAST.get(), KiBlastRenderer::new);

            event.registerEntityRenderer(ImmortalersDelightEntities.CAUSTIC_ESSENTIAL_OIL.get(), ToxicGasGrenadeRenderer::new);

            event.registerEntityRenderer(ImmortalersDelightEntities.MOON_ARROW_HITBOX.get(),MoonArrowHitboxRenderer::new);



        }



        @SubscribeEvent

        public static void onClientSetup(FMLClientSetupEvent event) {

            EntityRenderers.register(ImmortalersDelightEntities.IMMORTAL_BOAT.get(), pContext -> new ImmortalersBoatRenderer(pContext, false));

            EntityRenderers.register(ImmortalersDelightEntities.ANCIENT_WOOD_BOAT.get(), pContext -> new AncientWoodBoatRenderer(pContext, false));

            EntityRenderers.register(ImmortalersDelightEntities.IMMORTAL_CHEST_BOAT.get(), pContext -> new ImmortalersBoatRenderer(pContext, true));

            EntityRenderers.register(ImmortalersDelightEntities.ANCIENT_WOOD_CHEST_BOAT.get(), pContext -> new AncientWoodBoatRenderer(pContext, true));

            ItemBlockRenderTypes.setRenderLayer(ImmortalersDelightBlocks.LEISAMBOO_DOOR.get(), RenderType.cutout());

            registerItemProperties();

            LOGGER.info("HELLO FROM CLIENT SETUP");

            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());

            WeakWitherHealthOverlay.init();

            WeakPoisonHealthOverlay.init();

            AftertasteHungerOverlay.init();

            KeepFastHungerOverlay.init();

            BurnTheBoatsHealthOverlay.init();

        }



        public static void registerItemProperties() {

            ItemProperties.register(ImmortalersDelightItems.DEBUG_ITEM.get(), ResourceLocation.fromNamespaceAndPath(MODID, "model_type"), (stack, world, entity, seed) -> stack.getDamageValue());

            ItemProperties.register(ImmortalersDelightItems.REPEATING_CROSSBOW.get(), ResourceLocation.fromNamespaceAndPath(MODID, "pull"), (stack, world, entity, seed) -> {

                if (entity == null || entity.getUseItem() != stack) {

                    return 0.0F;

                } else {

                    return RepeatingCrossbowItem.isModCharged(stack) ? 0.0F : (float)(stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / RepeatingCrossbowItem.getModChargeDuration(stack);

                }

            });

            ItemProperties.register(ImmortalersDelightItems.REPEATING_CROSSBOW.get(), ResourceLocation.fromNamespaceAndPath(MODID, "pulling"), (stack, world, entity, seed) -> {

                return entity != null && entity.isUsingItem() && entity.getUseItem() == stack && !RepeatingCrossbowItem.isModCharged(stack) ? 1.0F : 0.0F;

            });

            ItemProperties.register(ImmortalersDelightItems.REPEATING_CROSSBOW.get(), ResourceLocation.fromNamespaceAndPath(MODID, "charged"), (stack, world, entity, seed) -> {

                return RepeatingCrossbowItem.isModCharged(stack) ? 1.0F : 0.0F;

            });

            ItemProperties.register(ImmortalersDelightItems.REPEATING_CROSSBOW.get(), ResourceLocation.fromNamespaceAndPath(MODID, "firework"), (stack, world, entity, seed) -> {

                return RepeatingCrossbowItem.isModCharged(stack) && RepeatingCrossbowItem.containsChargedModProjectile(stack,Items.FIREWORK_ROCKET) ? 1.0F : 0.0F;

            });

            ItemProperties.register(ImmortalersDelightItems.LARGE_COLUMN.get(), ResourceLocation.fromNamespaceAndPath(MODID, "blocking"), (stack, world, entity, seed) -> {

                return  entity != null &&  entity.isUsingItem() &&  entity.getUseItem() == stack ? 1.0F : 0.0F;

            });

            ItemProperties.register(ImmortalersDelightItems.JENG_NANU.get(), ResourceLocation.fromNamespaceAndPath(MODID, "blocking"), (stack, world, entity, seed) -> {

                return  entity != null &&  entity.isUsingItem() &&  entity.getUseItem() == stack ? 1.0F : 0.0F;

            });

        }

    }



    public static ResourceLocation prefix(String name) {

        return ResourceLocation.fromNamespaceAndPath(MODID, name.toLowerCase(Locale.ROOT));

    }

}

