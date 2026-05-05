package com.renyigesai.immortalers_delight.init;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;
import java.util.List;

public final class ImmortalersArmorMaterials {
    private ImmortalersArmorMaterials() {}

    public static final ArmorMaterial GOLDEN_FABRIC = new ArmorMaterial(
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                for (ArmorItem.Type type : ArmorItem.Type.values()) {
                    map.put(type, 0);
                }
            }),
            28,
            SoundEvents.ARMOR_EQUIP_LEATHER,
            () -> Ingredient.of(ImmortalersDelightItems.GOLDEN_FABRIC.get()),
            List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "golden_fabric"))),
            0.5F,
            0.01F
    );
}
