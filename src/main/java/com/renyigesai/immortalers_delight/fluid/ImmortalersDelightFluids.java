package com.renyigesai.immortalers_delight.fluid;
import net.neoforged.fml.common.EventBusSubscriber;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ImmortalersDelightFluids {
    public static final DeferredRegister<Fluid> REGISTRY = DeferredRegister.create(Registries.FLUID, ImmortalersDelightMod.MODID);
    public static final DeferredHolder<Fluid, HotSpringFluid.Source> HOT_SPRING = REGISTRY.register("hot_spring", HotSpringFluid.Source::new);
    public static final DeferredHolder<Fluid, HotSpringFluid.Flowing> FLOWING_HOT_SPRING = REGISTRY.register("flowing_hot_spring", HotSpringFluid.Flowing::new);

    @EventBusSubscriber(modid = ImmortalersDelightMod.MODID, value = Dist.CLIENT)
    public static class ClientSideHandler {
        @SubscribeEvent
        public static void registerFluidExtensions(RegisterClientExtensionsEvent event) {
            event.registerFluidType(new IClientFluidTypeExtensions() {
                private final ResourceLocation stillTexture = ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "block/hot_spring_still");
                private final ResourceLocation flowingTexture = ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "block/hot_spring_flow");

                @Override
                public ResourceLocation getStillTexture() {
                    return this.stillTexture;
                }

                @Override
                public ResourceLocation getFlowingTexture() {
                    return this.flowingTexture;
                }
            }, ImmortalersDelightFluidTypes.HOT_SPRING_TYPE.get());
        }

        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            ItemBlockRenderTypes.setRenderLayer(HOT_SPRING.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(FLOWING_HOT_SPRING.get(), RenderType.translucent());
        }
    }
}

