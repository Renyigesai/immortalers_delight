package com.renyigesai.immortalers_delight.fluid;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ImmortalersDelightFluidTypes {
    public static final DeferredRegister<FluidType> REGISTRY = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, ImmortalersDelightMod.MODID);
    public static final RegistryObject<FluidType> HOT_SPRING_TYPE = REGISTRY.register("hot_spring", HotSpringFluidType::new);
}
