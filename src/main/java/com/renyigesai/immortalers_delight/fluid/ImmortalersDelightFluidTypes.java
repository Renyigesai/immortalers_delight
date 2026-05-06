package com.renyigesai.immortalers_delight.fluid;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ImmortalersDelightFluidTypes {
    public static final DeferredRegister<FluidType> REGISTRY = DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, ImmortalersDelightMod.MODID);
    public static final DeferredHolder<FluidType, HotSpringFluidType> HOT_SPRING_TYPE = REGISTRY.register("hot_spring", HotSpringFluidType::new);
}
