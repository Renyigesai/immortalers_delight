package com.renyigesai.immortalers_delight.data;

import com.renyigesai.immortalers_delight.data.recipes.CookingRecipes;
import com.renyigesai.immortalers_delight.data.recipes.EnchantalCoolerRecipes;
import com.renyigesai.immortalers_delight.data.recipes.SmeltingRecipes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.CompletableFuture;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class Recipes extends RecipeProvider {
    public Recipes(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(pOutput, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        EnchantalCoolerRecipes.register(recipeOutput);
        CookingRecipes.register(recipeOutput);
        SmeltingRecipes.register(recipeOutput);
    }
}
