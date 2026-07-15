package com.renyigesai.immortalers_delight.block.tree;

import com.renyigesai.immortalers_delight.world.feature.ModConfigureFeature;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;

public class TravastrugglerSaplingBlock extends SaplingBlock {
    public TravastrugglerSaplingBlock(Properties properties) {
        super(ModTreeGrowers.TRAVASTRUGGLER, properties);
    }

    @Override
    public void advanceTree(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
        for (int xOffset = 0; xOffset >= -1; --xOffset) {
            for (int zOffset = 0; zOffset >= -1; --zOffset) {
                if (isTwoByTwoSapling(state, level, pos, xOffset, zOffset)
                        && growTwoByTwoTree(level, pos, state, random, xOffset, zOffset)) {
                    return;
                }
            }
        }

        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 4);
        if (!ConfiguredTreeGrowerHelper.growConfiguredFeature(
                level,
                pos,
                random,
                ModConfigureFeature.TRAVASTRUGGLER_TREE_KYE
        )) {
            level.setBlock(pos, state, 4);
        }
    }

    private boolean growTwoByTwoTree(ServerLevel level, BlockPos pos, BlockState state, RandomSource random, int xOffset, int zOffset) {
        BlockPos corner = pos.offset(xOffset, 0, zOffset);
        for (int dx = 0; dx < 2; ++dx) {
            for (int dz = 0; dz < 2; ++dz) {
                BlockPos saplingPos = corner.offset(dx, 0, dz);
                if (!level.getBlockState(saplingPos).is(state.getBlock())) {
                    return false;
                }
            }
        }

        for (int dx = 0; dx < 2; ++dx) {
            for (int dz = 0; dz < 2; ++dz) {
                level.setBlock(corner.offset(dx, 0, dz), Blocks.AIR.defaultBlockState(), 4);
            }
        }

        if (ConfiguredTreeGrowerHelper.growConfiguredFeature(
                level,
                corner,
                random,
                ModConfigureFeature.TRAVASTRUGGLER_TREE_KYE
        )) {
            return true;
        }

        for (int dx = 0; dx < 2; ++dx) {
            for (int dz = 0; dz < 2; ++dz) {
                level.setBlock(corner.offset(dx, 0, dz), state, 4);
            }
        }
        return false;
    }

    private static boolean isTwoByTwoSapling(BlockState state, BlockGetter level, BlockPos pos, int xOffset, int zOffset) {
        return level.getBlockState(pos.offset(xOffset, 0, zOffset)).is(state.getBlock())
                && level.getBlockState(pos.offset(xOffset + 1, 0, zOffset)).is(state.getBlock())
                && level.getBlockState(pos.offset(xOffset, 0, zOffset + 1)).is(state.getBlock())
                && level.getBlockState(pos.offset(xOffset + 1, 0, zOffset + 1)).is(state.getBlock());
    }
}
