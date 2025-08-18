package com.renyigesai.immortalers_delight.fluid;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ImmortalersDelightFluids {
    public static final DeferredRegister<Fluid> REGISTRY = DeferredRegister.create(ForgeRegistries.FLUIDS, ImmortalersDelightMod.MODID);
    public static final RegistryObject<FlowingFluid> HOT_SPRING = REGISTRY.register("hot_spring", HotSpringFluid.Source::new);
    public static final RegistryObject<FlowingFluid> FLOWING_HOT_SPRING = REGISTRY.register("flowing_hot_spring", HotSpringFluid.Flowing::new);

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientSideHandler {
        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            ItemBlockRenderTypes.setRenderLayer(HOT_SPRING.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(FLOWING_HOT_SPRING.get(), RenderType.translucent());
        }
    }
}

