package com.renyigesai.immortalers_delight.init;

import com.mojang.serialization.MapCodec;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.loot.SnifferBiomeDigLootModifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public final class ImmortalersDelightLootModifierSerializers {
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> REGISTRY =
            DeferredRegister.create(NeoForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS.key(), ImmortalersDelightMod.MODID);

    public static final Supplier<MapCodec<SnifferBiomeDigLootModifier>> SNIFFER_BIOME_DIGGING =
            REGISTRY.register("sniffer_biome_digging", () -> SnifferBiomeDigLootModifier.CODEC);

    private ImmortalersDelightLootModifierSerializers() {
    }

    public static void register(IEventBus modEventBus) {
        REGISTRY.register(modEventBus);
    }
}
