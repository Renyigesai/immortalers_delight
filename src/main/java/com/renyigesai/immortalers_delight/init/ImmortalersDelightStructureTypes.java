package com.renyigesai.immortalers_delight.init;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.world.structure.TerrainCheckedRuinsStructure;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class ImmortalersDelightStructureTypes {
    public static final DeferredRegister<StructureType<?>> REGISTRY =
            DeferredRegister.create(Registries.STRUCTURE_TYPE, ImmortalersDelightMod.MODID);
    public static final RegistryObject<StructureType<TerrainCheckedRuinsStructure>> TERRAIN_CHECKED_RUINS =
            REGISTRY.register("terrain_checked_ruins", () -> () -> TerrainCheckedRuinsStructure.CODEC);

    private ImmortalersDelightStructureTypes() {
    }
}
