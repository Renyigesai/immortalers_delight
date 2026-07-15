package com.renyigesai.immortalers_delight.compat;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.neoforged.fml.ModList;

/**
 * Registers leisamboo as a bamboo-like wood set for Moonlight (used by Sawmill and similar mods).
 * Without this, auto-detection pairs {@code leisamboo_planks} with {@code leisamboo_stalk} instead of
 * {@code leisamboo_block}, breaking sawmill recipes that should mirror bamboo block → bamboo planks.
 */
public final class MoonlightWoodCompat {

    private MoonlightWoodCompat() {
    }

    public static void register() {
        if (!ModList.get().isLoaded("moonlight")) {
            return;
        }

        try {
            Class<?> registryClass = Class.forName("net.mehvahdjukaar.moonlight.api.set.wood.WoodTypeRegistry");
            Object registry = registryClass.getField("INSTANCE").get(null);
            Object finder = registryClass
                    .getMethod("addSimpleFinder", String.class, String.class)
                    .invoke(registry, ImmortalersDelightMod.MODID, "leisamboo");

            Class<?> finderClass = finder.getClass();
            finderClass.getMethod("planks", String.class).invoke(finder, "leisamboo_planks");
            finderClass.getMethod("log", String.class).invoke(finder, "leisamboo_block");
            finderClass.getMethod("bambooLike", boolean.class).invoke(finder, true);
            ImmortalersDelightMod.LOGGER.info("Registered leisamboo WoodType for Moonlight/Sawmill compat");
        } catch (ReflectiveOperationException exception) {
            ImmortalersDelightMod.LOGGER.warn("Failed to register leisamboo WoodType for Moonlight compat", exception);
        }
    }
}
