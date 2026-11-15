package com.renyigesai.immortalers_delight.world.structure;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightStructureTypes;
import net.minecraft.core.Holder;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;

import java.util.Optional;

public final class TerrainCheckedRuinsStructure extends Structure {
    public static final Codec<TerrainCheckedRuinsStructure> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            settingsCodec(instance),
            StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(structure -> structure.startPool),
            HeightProvider.CODEC.fieldOf("start_height").forGetter(structure -> structure.startHeight),
            Heightmap.Types.CODEC.fieldOf("project_start_to_heightmap").forGetter(structure -> structure.heightmap),
            Codec.intRange(1, 7).fieldOf("size").forGetter(structure -> structure.size),
            Codec.intRange(1, 116).fieldOf("max_distance_from_center").forGetter(structure -> structure.maxDistanceFromCenter),
            Codec.intRange(0, 32).fieldOf("max_terrain_height_difference").forGetter(structure -> structure.maxTerrainHeightDifference)
    ).apply(instance, TerrainCheckedRuinsStructure::new));

    private final Holder<StructureTemplatePool> startPool;
    private final HeightProvider startHeight;
    private final Heightmap.Types heightmap;
    private final int size;
    private final int maxDistanceFromCenter;
    private final int maxTerrainHeightDifference;
    private final JigsawStructure jigsaw;

    private TerrainCheckedRuinsStructure(StructureSettings settings, Holder<StructureTemplatePool> startPool,
                                         HeightProvider startHeight, Heightmap.Types heightmap, int size,
                                         int maxDistanceFromCenter, int maxTerrainHeightDifference) {
        super(settings);
        this.startPool = startPool;
        this.startHeight = startHeight;
        this.heightmap = heightmap;
        this.size = size;
        this.maxDistanceFromCenter = maxDistanceFromCenter;
        this.maxTerrainHeightDifference = maxTerrainHeightDifference;
        this.jigsaw = new JigsawStructure(settings, startPool, Optional.empty(), size,
                startHeight, false, Optional.of(heightmap), maxDistanceFromCenter);
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        return jigsaw.findGenerationPoint(context);
    }

    @Override
    public Optional<GenerationStub> findValidGenerationPoint(GenerationContext context) {
        return super.findValidGenerationPoint(context).flatMap(stub -> {
            StructurePiecesBuilder pieces = stub.getPiecesBuilder();
            if (pieces.isEmpty() || !hasSuitableTerrain(context, pieces.getBoundingBox())) {
                return Optional.empty();
            }
            // Keep the pieces already selected by vanilla so the terrain check and placement use the same rotation.
            return Optional.of(new GenerationStub(stub.position(), Either.right(pieces)));
        });
    }

    private boolean hasSuitableTerrain(GenerationContext context, BoundingBox footprint) {
        int[] sampleX = {footprint.minX(), (footprint.minX() + footprint.maxX()) / 2, footprint.maxX()};
        int[] sampleZ = {footprint.minZ(), (footprint.minZ() + footprint.maxZ()) / 2, footprint.maxZ()};
        ChunkGenerator generator = context.chunkGenerator();
        int minHeight = Integer.MAX_VALUE;
        int maxHeight = Integer.MIN_VALUE;
        for (int x : sampleX) {
            for (int z : sampleZ) {
                int surface = generator.getBaseHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG,
                        context.heightAccessor(), context.randomState());
                minHeight = Math.min(minHeight, surface);
                maxHeight = Math.max(maxHeight, surface);
                if (surface <= context.heightAccessor().getMinBuildHeight()
                        || maxHeight - minHeight > maxTerrainHeightDifference
                        || !generator.getBaseColumn(x, z, context.heightAccessor(), context.randomState())
                                .getBlock(surface - 1).getFluidState().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public StructureType<?> type() {
        return ImmortalersDelightStructureTypes.TERRAIN_CHECKED_RUINS.get();
    }
}
