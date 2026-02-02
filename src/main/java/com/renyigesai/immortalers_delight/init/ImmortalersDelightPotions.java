package com.renyigesai.immortalers_delight.init;

import com.doggystudio.chirencqr.ltc.server.registry.LTCEffects;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ImmortalersDelightPotions {
    public static final DeferredRegister<Potion> REGISTRY = DeferredRegister.create(Registries.POTION, ImmortalersDelightMod.MODID);

    public static <T extends Potion> RegistryObject<Potion> register(String name, Supplier<T> supplier){
        return REGISTRY.register(name, supplier);
    }

    public static void register(IEventBus eventBus){
        REGISTRY.register(eventBus);
    }
    public static final RegistryObject<Potion> GAS = register("gas", () ->
            new Potion("gas", new MobEffectInstance[]{new MobEffectInstance((MobEffect) ImmortalersDelightMobEffect.GAS_POISON.get(), 600)}));
    public static final RegistryObject<Potion> LONG_GAS = register("long_gas", () ->
            new Potion("gas", new MobEffectInstance[]{new MobEffectInstance((MobEffect) ImmortalersDelightMobEffect.GAS_POISON.get(), 1200)}));
    public static final RegistryObject<Potion> STRONG_GAS = register("strong_gas", () ->
            new Potion("gas", new MobEffectInstance[]{new MobEffectInstance((MobEffect) ImmortalersDelightMobEffect.GAS_POISON.get(), 300, 1)}));
}
