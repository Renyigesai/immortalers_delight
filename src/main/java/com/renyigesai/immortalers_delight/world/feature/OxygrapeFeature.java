package com.renyigesai.immortalers_delight.world.feature;

import com.mojang.serialization.Codec;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;

public class OxygrapeFeature extends Feature<ProbabilityFeatureConfiguration> {
    public OxygrapeFeature() {
        super(ProbabilityFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<ProbabilityFeatureConfiguration> featureContext) {
        boolean isPlace = false;
        RandomSource randomSource = featureContext.random();
        WorldGenLevel level = featureContext.level();
        BlockPos pos = featureContext.origin();
        int $$6 = randomSource.nextInt(8) - randomSource.nextInt(8);
        int $$7 = randomSource.nextInt(8) - randomSource.nextInt(8);
        int $$8 = level.getHeight(Heightmap.Types.OCEAN_FLOOR, pos.getX() + $$6, pos.getZ() + $$7);
        BlockPos newPos = new BlockPos(pos.getX() + $$6, $$8, pos.getZ() + $$7);
        if (level.getBlockState(newPos).is(Blocks.WATER) && (level.getBlockState(newPos.below()).is(Blocks.PRISMARINE) || level.getBlockState(newPos.below()).is(Blocks.PRISMARINE_BRICKS))) {
            int maxAge = featureContext.random().nextInt(1, 5);
            BlockState state = ImmortalersDelightBlocks.OXYGRAPE.get().defaultBlockState();
            for (int i = 1; i <= maxAge; i++) {
                if (level.getBlockState(newPos.below(1 + i)).is(Blocks.WATER)) {
                    isPlace = true;
                    level.setBlock(newPos.below(1 + i), state, 2);
                } else {
                    break;
                }
            }
        }
        return isPlace;
    }
}
