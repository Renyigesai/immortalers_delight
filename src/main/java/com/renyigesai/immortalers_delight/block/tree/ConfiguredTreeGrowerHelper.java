package com.renyigesai.immortalers_delight.block.tree;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

final class ConfiguredTreeGrowerHelper {
    private ConfiguredTreeGrowerHelper() {}

    static boolean growConfiguredFeature(
            ServerLevel level,
            BlockPos pos,
            RandomSource random,
            ResourceKey<ConfiguredFeature<?, ?>> featureKey
    ) {
        var registry = level.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE);
        var holder = registry.getHolder(featureKey).orElse(null);
        if (holder == null) {
            return false;
        }

        ChunkGenerator generator = level.getChunkSource().getGenerator();
        return holder.value().place(level, generator, random, pos);
    }
}
