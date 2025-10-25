package com.renyigesai.immortalers_delight;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.mojang.logging.LogUtils;
import com.renyigesai.immortalers_delight.client.model.*;
import com.renyigesai.immortalers_delight.client.renderer.entity.*;
import com.renyigesai.immortalers_delight.client.renderer.AncientStoveBlockEntityRenderer;
import com.renyigesai.immortalers_delight.client.renderer.ImmortalersBoatRenderer;
import com.renyigesai.immortalers_delight.client.renderer.ImmortalersDelightHangingSignRenderer;
import com.renyigesai.immortalers_delight.client.renderer.ImmortalersDelightSignRenderer;
import com.renyigesai.immortalers_delight.compat.init.Ltc2Items;
import com.renyigesai.immortalers_delight.fluid.ImmortalersDelightFluidTypes;
import com.renyigesai.immortalers_delight.fluid.ImmortalersDelightFluids;
import com.renyigesai.immortalers_delight.init.*;
import com.renyigesai.immortalers_delight.item.RepeatingCrossbowItem;
import com.renyigesai.immortalers_delight.message.TerracottaGolemMessage;
import com.renyigesai.immortalers_delight.network.CommonProxy;
import com.renyigesai.immortalers_delight.network.ClientProxy;
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
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CompoundIngredient;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.slf4j.Logger;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.*;
import java.util.function.Supplier;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ImmortalersDelightMod.MODID)
public class ImmortalersDelightMod {

    public static final String MODID = "immortalers_delight";
    public static final Logger LOGGER = LogUtils.getLogger();

    private static final String PROTOCOL_VERSION = Integer.toString(1);
    public static final SimpleChannel NETWORK_WRAPPER;
    public static CommonProxy PROXY = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
    private static int packetsRegistered;
    public static final boolean isLtc2 = ModList.get().isLoaded("ltc2");

    static {
        NetworkRegistry.ChannelBuilder channel = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(MODID, "main_channel"));
        String version = PROTOCOL_VERSION;
        version.getClass();
        channel = channel.clientAcceptedVersions(version::equals);
        version = PROTOCOL_VERSION;
        version.getClass();
        NETWORK_WRAPPER = channel.serverAcceptedVersions(version::equals).networkProtocolVersion(() -> {
            return PROTOCOL_VERSION;
        }).simpleChannel();
    }
    public ImmortalersDelightMod() {
        MinecraftForge.EVENT_BUS.register(this);
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(CommonSetup::init);

        if (isLtc2){
            Ltc2Items.ITEMS.register(bus);
        }

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


    public static <MSG> void sendMSGToServer(MSG message) {
        NETWORK_WRAPPER.sendToServer(message);
    }

    public static <MSG> void sendMSGToAll(MSG message) {
        for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
            sendNonLocal(message, player);
        }
    }

    public static <MSG> void sendNonLocal(MSG msg, ServerPlayer player) {
        NETWORK_WRAPPER.sendTo(msg, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class CommonModSetup {

        @SubscribeEvent
        public void commonSetup(final FMLCommonSetupEvent event) {
            LOGGER.info("IMMORTALERS DELIGHT SETUP");
            event.enqueueWork(ImmortalersDelightMod::registerItemSetAdditions);
            NETWORK_WRAPPER.registerMessage(packetsRegistered++,
                    TerracottaGolemMessage.class,
                    TerracottaGolemMessage::write,
                    TerracottaGolemMessage::read,
                    TerracottaGolemMessage.Handler::handle
            );
        }
    }

    public static void registerItemSetAdditions() {
        Ingredient newPigFood = Ingredient.of(ModItems.CABBAGE.get(), ModItems.TOMATO.get());
        Pig.FOOD_ITEMS = new CompoundIngredient(Arrays.asList(Pig.FOOD_ITEMS, newPigFood))
        {
        };
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

            for (Map.Entry<ModelLayerLocation, Supplier<LayerDefinition>> entry : modelLayers.entrySet()) {
                event.registerLayerDefinition(entry.getKey(), entry.getValue());
            }
        }

        @SubscribeEvent
        public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(ImmortalersDelightBlockEntityTypes.SIGN.get(), ImmortalersDelightSignRenderer::new);
            event.registerBlockEntityRenderer(ImmortalersDelightBlockEntityTypes.HANGING_SIGN.get(), ImmortalersDelightHangingSignRenderer::new);
            event.registerBlockEntityRenderer(ImmortalersDelightBlocks.ANCIENT_STOVE_ENTITY.get(), AncientStoveBlockEntityRenderer::new);

            event.registerEntityRenderer(ImmortalersDelightEntities.SKELVERFISH_AMBUSHER.get(), SkelverfishRenderer::new);
            event.registerEntityRenderer(ImmortalersDelightEntities.SKELVERFISH_BOMBER.get(), SkelverfishBomberRenderer::new);
            event.registerEntityRenderer(ImmortalersDelightEntities.SKELVERFISH_THRASHER.get(), SkelverfishThrasherRenderer::new);
            event.registerEntityRenderer(ImmortalersDelightEntities.STRANGE_ARMOUR_STAND.get(), StrangeArmourStandRenderer::new);
            event.registerEntityRenderer(ImmortalersDelightEntities.SCAVENGER.get(), ScavengerRenderer::new);
            event.registerEntityRenderer(ImmortalersDelightEntities.TERRACOTTA_GOLEM.get(), TerracottaGolemRenderer::new);


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

        }
    }

    public static ResourceLocation prefix(String name) {
        return new ResourceLocation(MODID, name.toLowerCase(Locale.ROOT));
    }
}
