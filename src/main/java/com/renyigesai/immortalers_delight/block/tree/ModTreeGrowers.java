package com.renyigesai.immortalers_delight.block.tree;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.world.feature.ModConfigureFeature;
import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

public final class ModTreeGrowers {
    private ModTreeGrowers() {}

    public static final TreeGrower HIMEKAIDO = new TreeGrower(
            ImmortalersDelightMod.MODID + ":himekaido",
            Optional.of(ModConfigureFeature.HIMEKAIDO_TREE_KEY),
            Optional.empty(),
            Optional.empty()
    );

    public static final TreeGrower TRAVASTRUGGLER = new TreeGrower(
            ImmortalersDelightMod.MODID + ":travastruggler",
            Optional.of(ModConfigureFeature.TRAVASTRUGGLER_TREE_KYE),
            Optional.empty(),
            Optional.empty()
    );
}
