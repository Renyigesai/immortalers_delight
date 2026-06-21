package com.renyigesai.immortalers_delight.world.structure;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.Map;

/**
 * Rejects structure placement on void / floating-island gaps where {@link Heightmap.Types#WORLD_SURFACE_WG}
 * does not correspond to reachable solid terrain (common on sky/island datapacks).
 */
public final class StructureGroundChecks {
    private static final Map<String, GroundCheck> CHECKS_BY_POOL_PREFIX = Map.of(
            "immortalers_relic/immortalers_relic", new GroundCheck(4, 9),
            "ruins_with_cherry_tree/ruins_with_cherry_tree", new GroundCheck(6, 20)
    );

    private StructureGroundChecks() {
    }

    public static GroundCheck groundCheckForPool(ResourceLocation startPoolId) {
        if (!ImmortalersDelightMod.MODID.equals(startPoolId.getNamespace())) {
            return null;
        }
        return CHECKS_BY_POOL_PREFIX.get(startPoolId.getPath());
    }

    public static boolean hasSolidGround(Structure.GenerationContext context, BlockPos origin, GroundCheck check) {
        return hasSolidGround(context, origin, check.radius(), check.minSolidColumns());
    }

    public static boolean hasSolidGround(Structure.GenerationContext context, BlockPos origin, int radius, int minSolidColumns) {
        if (minSolidColumns <= 0) {
            return true;
        }

        int solidColumns = 0;
        int originX = origin.getX();
        int originZ = origin.getZ();

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                int x = originX + dx;
                int z = originZ + dz;

                int surfaceY = context.chunkGenerator().getFirstFreeHeight(
                        x,
                        z,
                        Heightmap.Types.WORLD_SURFACE_WG,
                        context.heightAccessor(),
                        context.randomState()
                );

                if (isSolidSupportColumn(context, x, surfaceY, z)) {
                    solidColumns++;
                }
            }
        }

        return solidColumns >= minSolidColumns;
    }

    private static boolean isSolidSupportColumn(Structure.GenerationContext context, int x, int surfaceY, int z) {
        var column = context.chunkGenerator().getBaseColumn(x, z, context.heightAccessor(), context.randomState());

        BlockState footing = column.getBlock(surfaceY - 1);
        if (!isSupportBlock(footing)) {
            return false;
        }

        int solidBelow = 0;
        for (int dy = 2; dy <= 5; dy++) {
            if (isSupportBlock(column.getBlock(surfaceY - dy))) {
                solidBelow++;
            }
        }

        return solidBelow >= 2;
    }

    private static boolean isSupportBlock(BlockState state) {
        return !state.isAir() && !state.liquid() && state.isSolid();
    }

    public record GroundCheck(int radius, int minSolidColumns) {
    }
}
