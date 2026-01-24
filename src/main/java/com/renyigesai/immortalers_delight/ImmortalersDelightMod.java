package com.renyigesai.immortalers_delight;

import com.mojang.logging.LogUtils;
import com.renyigesai.immortalers_delight.client.model.*;
import com.renyigesai.immortalers_delight.client.model.projectile.EffectCloudModel;
import com.renyigesai.immortalers_delight.client.model.projectile.HugeSmokeParticleModel;
import com.renyigesai.immortalers_delight.client.model.projectile.SurveyorFangModel;
import com.renyigesai.immortalers_delight.client.model.projectile.WarpedLaurelHitBoxModel;
import com.renyigesai.immortalers_delight.client.renderer.*;
import com.renyigesai.immortalers_delight.client.renderer.entity.*;
import com.renyigesai.immortalers_delight.client.renderer.entity.projectile.*;
import com.renyigesai.immortalers_delight.compat.init.Ltc2Items;
import com.renyigesai.immortalers_delight.fluid.ImmortalersDelightFluidTypes;
import com.renyigesai.immortalers_delight.fluid.ImmortalersDelightFluids;
import com.renyigesai.immortalers_delight.init.*;
import com.renyigesai.immortalers_delight.item.weapon.RepeatingCrossbowItem;
import com.renyigesai.immortalers_delight.network.ImmortalersNetwork;
import com.renyigesai.immortalers_delight.screen.EnchantalCoolerScreen;
import com.renyigesai.immortalers_delight.screen.TerracottaGolemScreen;
import com.renyigesai.immortalers_delight.screen.overlay.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.IllagerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CompoundIngredient;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.slf4j.Logger;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.*;
import java.util.function.Supplier;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ImmortalersDelightMod.MODID)
public class ImmortalersDelightMod {

    public static final String MODID = "immortalers_delight";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final boolean isLtc2 = ModList.get().isLoaded("ltc2");
    private static final String PROTOCOL_VERSION = Integer.toString(1);
    public static SimpleChannel NETWORK_WRAPPER = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(ImmortalersDelightMod.MODID, "main_channel"), // 通道唯一标识
            () -> PROTOCOL_VERSION, // 获取当前协议版本
            PROTOCOL_VERSION::equals, // 客户端是否接受该协议版本
            PROTOCOL_VERSION::equals  // 服务端是否接受该协议版本
    );

    public ImmortalersDelightMod() {
        MinecraftForge.EVENT_BUS.register(this);
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(CommonSetup::init);

        if (isLtc2){
            Ltc2Items.ITEMS.register(bus);
        }
        ImmortalersNetwork.initChannel();
        ImmortalersNetwork.registerPackets();

        ImmortalersDelightItems.REGISTER.register(bus);
        ImmortalersDelightBlocks.register(bus);
        ImmortalersDelightBlockEntityTypes.TILES.register(bus);
        ImmortalersDelightBlocks.BLOCK_ENTITY_REGISTRY.register(bus);
        ImmortalersDelightGroup.CREATIVE_TABS.register(bus);
        ImmortalersDelightFluidTypes.REGISTRY.register(bus);
        ImmortalersDelightFluids.REGISTRY.register(bus);
        ImmortalersDelightMobEffect.REGISTRY.register(bus);
        ImmortalersDelightParticleTypes.REGISTRY.register(bus);
        ImmortalersDelightMenuTypes.MENUS.register(FMLJavaModLoadingContext.get().getModEventBus());

        ImmortalersDelightEntities.ENTITY_TYPES.register(bus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    public static void registerItemSetAdditions() {
        Ingredient newPigFood = Ingredient.of(ModItems.CABBAGE.get(), ModItems.TOMATO.get());
        Pig.FOOD_ITEMS = new CompoundIngredient(Arrays.asList(Pig.FOOD_ITEMS, newPigFood))
        {
        };
    }

    @Mod.EventBusSubscriber(modid = ImmortalersDelightMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class CommonModSetup {
        //通用初始化事件，客户端服务端都会执行
        @SubscribeEvent
        public void commonSetup(final FMLCommonSetupEvent event) {
            event.enqueueWork(
                    () -> {
                        // 通用初始化内容
                        ImmortalersDelightMod.LOGGER.info("IMMORTALERS DELIGHT SETUP");
                        registerItemSetAdditions();
                        // 依次注册自定义包：示例 - 玩家打开生物背包界面的包（客户端→服务端）
//                        ImmortalersNetwork.initChannel();
//                        ImmortalersNetwork.registerPackets();
                    }
            );
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
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
            //modelLayers.put(ScavengerModel.SCARVENGER_MODEL, ScavengerModel::createBodyLayer);
            modelLayers.put(ScavengerModel.SCARVENGER_MODEL, IllagerModel::createBodyLayer);
            modelLayers.put(SurveyorFangModel.SURVEYOR_FANG, SurveyorFangModel::createBodyLayer);
            modelLayers.put(EffectCloudModel.EFFECT_CLOUD_BASE, EffectCloudModel::createBodyLayer);
            modelLayers.put(HugeSmokeParticleModel.HUGE_SMOKE_PARTICLE, HugeSmokeParticleModel::createBodyLayer);
            modelLayers.put(AlfalfaDababaModel.ALFALFA_DABABA, AlfalfaDababaModel::createBodyLayer);
            modelLayers.put(KiBlastRenderer.MODEL_LOCATION, KiBlastRenderer::createSkullLayer);
            modelLayers.put(WarpedLaurelHitBoxModel.LAYER_LOCATION, WarpedLaurelHitBoxModel::createBodyLayer);
            modelLayers.put(ToxicGasGrenadeRenderer.MODEL_LOCATION, ToxicGasGrenadeRenderer::createSkullLayer);

            for (Map.Entry<ModelLayerLocation, Supplier<LayerDefinition>> entry : modelLayers.entrySet()) {
                event.registerLayerDefinition(entry.getKey(), entry.getValue());
            }
        }

        @SubscribeEvent
        public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(ImmortalersDelightBlockEntityTypes.SIGN.get(), ImmortalersDelightSignRenderer::new);
            event.registerBlockEntityRenderer(ImmortalersDelightBlockEntityTypes.HANGING_SIGN.get(), ImmortalersDelightHangingSignRenderer::new);
            event.registerBlockEntityRenderer(ImmortalersDelightBlocks.ANCIENT_STOVE_ENTITY.get(), AncientStoveBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(ImmortalersDelightBlocks.SUPPORT_BLOCK_ENTITY.get(), SupportBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(ImmortalersDelightBlocks.UNFINISHED_TANGYUAN_ENTITY.get(),TangyuanBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(ImmortalersDelightBlocks.SUSPICIOUS_ASH_PILE_BLOCK_ENTITY.get(),NaanPitBlockRenderer::new);

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

        }

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ImmortalersDelightEntities.IMMORTAL_BOAT.get(), pContext -> new ImmortalersBoatRenderer(pContext, false));
            EntityRenderers.register(ImmortalersDelightEntities.ANCIENT_WOOD_BOAT.get(), pContext -> new AncientWoodBoatRenderer(pContext, false));
            EntityRenderers.register(ImmortalersDelightEntities.IMMORTAL_CHEST_BOAT.get(), pContext -> new ImmortalersBoatRenderer(pContext, true));
            EntityRenderers.register(ImmortalersDelightEntities.ANCIENT_WOOD_CHEST_BOAT.get(), pContext -> new AncientWoodBoatRenderer(pContext, true));
            ItemBlockRenderTypes.setRenderLayer(ImmortalersDelightBlocks.LEISAMBOO_DOOR.get(), RenderType.cutout());
            MenuScreens.register(ImmortalersDelightMenuTypes.ENCHANTAL_COOLER_MENU.get(), EnchantalCoolerScreen::new);
            // 注册UI界面
            MenuScreens.register(ImmortalersDelightMenuTypes.TERRACOTTA_GOLEM_MENU.get(), TerracottaGolemScreen::new);

            registerItemProperties();
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
            WeakWitherHealthOverlay.init();
            WeakPoisonHealthOverlay.init();
            AftertasteHungerOverlay.init();
            KeepFastHungerOverlay.init();
            BurnTheBoatsHealthOverlay.init();
        }

        public static void registerItemProperties() {
            ItemProperties.register(ImmortalersDelightItems.DEBUG_ITEM.get(), new ResourceLocation(MODID + "_" + "model_type"), (stack, world, entity, seed) -> stack.getDamageValue());
            ItemProperties.register(ImmortalersDelightItems.REPEATING_CROSSBOW.get(), new ResourceLocation(MODID + "_" + "pull"), (stack, world, entity, seed) -> {
                if (entity == null || entity.getUseItem() != stack) {
                    return 0.0F;
                } else {
                    return RepeatingCrossbowItem.isModCharged(stack) ? 0.0F : (float)(stack.getUseDuration() - entity.getUseItemRemainingTicks()) / RepeatingCrossbowItem.getChargeDuration(stack);
                }
            });
            ItemProperties.register(ImmortalersDelightItems.REPEATING_CROSSBOW.get(), new ResourceLocation(MODID + "_" + "pulling"), (stack, world, entity, seed) -> {
                return entity != null && entity.isUsingItem() && entity.getUseItem() == stack && !RepeatingCrossbowItem.isModCharged(stack) ? 1.0F : 0.0F;
            });
            ItemProperties.register(ImmortalersDelightItems.REPEATING_CROSSBOW.get(), new ResourceLocation(MODID + "_" + "charged"), (stack, world, entity, seed) -> {
                return RepeatingCrossbowItem.isModCharged(stack) ? 1.0F : 0.0F;
            });
            ItemProperties.register(ImmortalersDelightItems.REPEATING_CROSSBOW.get(), new ResourceLocation(MODID + "_" + "firework"), (stack, world, entity, seed) -> {
                return RepeatingCrossbowItem.isModCharged(stack) && RepeatingCrossbowItem.containsChargedModProjectile(stack,Items.FIREWORK_ROCKET) ? 1.0F : 0.0F;
            });
            ItemProperties.register(ImmortalersDelightItems.LARGE_COLUMN.get(), new ResourceLocation(MODID + "_" + "blocking"), (stack, world, entity, seed) -> {
                return  entity != null &&  entity.isUsingItem() &&  entity.getUseItem() == stack ? 1.0F : 0.0F;
            });
        }
    }

    public static ResourceLocation prefix(String name) {
        return new ResourceLocation(MODID, name.toLowerCase(Locale.ROOT));
    }
}
