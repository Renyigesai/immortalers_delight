package com.renyigesai.immortalers_delight.init;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public class ImmortalersDelightPotions {
    public static final DeferredRegister<Potion> REGISTRY = DeferredRegister.create(Registries.POTION, ImmortalersDelightMod.MODID);

    public static <T extends Potion> DeferredHolder<Potion, T> register(String name, Supplier<T> supplier){
        return REGISTRY.register(name, supplier);
    }

    public static void register(IEventBus eventBus){
        REGISTRY.register(eventBus);
    }
    public static final DeferredHolder<Potion, Potion> GAS = register("gas", () ->
            new Potion("gas", new MobEffectInstance[]{new MobEffectInstance(ImmortalersDelightMobEffect.GAS_POISON, 600)}));
    public static final DeferredHolder<Potion, Potion> LONG_GAS = register("long_gas", () ->
            new Potion("gas", new MobEffectInstance[]{new MobEffectInstance(ImmortalersDelightMobEffect.GAS_POISON, 1200)}));
    public static final DeferredHolder<Potion, Potion> STRONG_GAS = register("strong_gas", () ->
            new Potion("gas", new MobEffectInstance[]{new MobEffectInstance(ImmortalersDelightMobEffect.GAS_POISON, 300, 1)}));
}
