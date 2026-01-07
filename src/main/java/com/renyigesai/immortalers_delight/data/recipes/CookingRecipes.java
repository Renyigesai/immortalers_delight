package com.renyigesai.immortalers_delight.data.recipes;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

public class CookingRecipes {

    public static void register(Consumer<FinishedRecipe> consumer) {
        onCooking(consumer);
    }

    private static void onCooking(Consumer<FinishedRecipe> consumer){

    }

}
