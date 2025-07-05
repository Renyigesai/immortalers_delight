package com.renyigesai.immortalers_delight;

import com.mojang.logging.LogUtils;
import com.renyigesai.immortalers_delight.client.model.AncientWoodBoatModel;
import com.renyigesai.immortalers_delight.client.model.AncientWoodChestBoatModel;
import com.renyigesai.immortalers_delight.client.model.SkelverfishBomberModel;
import com.renyigesai.immortalers_delight.client.model.SkelverfishThrasherModel;
import com.renyigesai.immortalers_delight.client.model.StrangeArmourStandModel;
import com.renyigesai.immortalers_delight.client.renderer.entity.SkelverfishBomberRenderer;
import com.renyigesai.immortalers_delight.client.renderer.entity.SkelverfishRenderer;
import com.renyigesai.immortalers_delight.client.renderer.entity.SkelverfishThrasherRenderer;
import com.renyigesai.immortalers_delight.client.renderer.entity.StrangeArmourStandRenderer;
import com.renyigesai.immortalers_delight.client.renderer.entity.AncientWoodBoatRenderer;
import com.renyigesai.immortalers_delight.client.renderer.AncientStoveBlockEntityRenderer;
import com.renyigesai.immortalers_delight.client.renderer.ImmortalersBoatRenderer;
import com.renyigesai.immortalers_delight.client.renderer.ImmortalersDelightHangingSignRenderer;
import com.renyigesai.immortalers_delight.client.renderer.ImmortalersDelightSignRenderer;
import com.renyigesai.immortalers_delight.init.*;
import com.renyigesai.immortalers_delight.screen.EnchantalCoolerScreen;
import com.renyigesai.immortalers_delight.screen.overlay.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ImmortalersDelightMod.MODID)
public class ImmortalersDelightMod {

    public static final String MODID = "immortalers_delight";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ImmortalersDelightMod() {
        MinecraftForge.EVENT_BUS.register(this);
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        ImmortalersDelightItems.REGISTER.register(bus);
        ImmortalersDelightBlocks.register(bus);
        ImmortalersDelightBlockEntityTypes.TILES.register(bus);
        ImmortalersDelightBlocks.BLOCK_ENTITY_REGISTRY.register(bus);
        ImmortalersDelightGroup.CREATIVE_TABS.register(bus);
        ImmortalersDelightMobEffect.REGISTRY.register(bus);
        ImmortalersDelightParticleTypes.REGISTRY.register(bus);
        ImmortalersDelightMenuTypes.MENUS.register(FMLJavaModLoadingContext.get().getModEventBus());

        ImmortalersDelightEntities.ENTITY_TYPES.register(bus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
        LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        if (Config.logDirtBlock) LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
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
            modelLayers.put(AncientWoodBoatModel.ANCIENT_BOAT, AncientWoodBoatModel::createBodyLayer);
            modelLayers.put(AncientWoodChestBoatModel.ANCIENT_CHEST_BOAT, AncientWoodChestBoatModel::createBodyLayer);       for (Map.Entry<ModelLayerLocation, Supplier<LayerDefinition>> entry : modelLayers.entrySet()) {
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
        }

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ImmortalersDelightEntities.IMMORTAL_BOAT.get(), pContext -> new ImmortalersBoatRenderer(pContext, false));
            EntityRenderers.register(ImmortalersDelightEntities.ANCIENT_WOOD_BOAT.get(), pContext -> new AncientWoodBoatRenderer(pContext, false));
            EntityRenderers.register(ImmortalersDelightEntities.IMMORTAL_CHEST_BOAT.get(), pContext -> new ImmortalersBoatRenderer(pContext, true));
            EntityRenderers.register(ImmortalersDelightEntities.ANCIENT_WOOD_CHEST_BOAT.get(), pContext -> new AncientWoodBoatRenderer(pContext, true));
            ItemBlockRenderTypes.setRenderLayer(ImmortalersDelightBlocks.LEISAMBOO_DOOR.get(), RenderType.cutout());
            MenuScreens.register(ImmortalersDelightMenuTypes.ENCHANTAL_COOLER_MENU.get(), EnchantalCoolerScreen::new);
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
            WeakWitherHealthOverlay.init();
            WeakPoisonHealthOverlay.init();
            AftertasteHungerOverlay.init();
            KeepFastHungerOverlay.init();
            BurnTheBoatsHealthOverlay.init();
        }
    }

    public static ResourceLocation prefix(String name) {
        return new ResourceLocation(MODID, name.toLowerCase(Locale.ROOT));
    }
}
