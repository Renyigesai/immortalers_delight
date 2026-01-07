package com.renyigesai.immortalers_delight.data;

import com.renyigesai.immortalers_delight.data.recipes.CookingRecipes;
import com.renyigesai.immortalers_delight.data.recipes.EnchantalCoolerRecipes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class Recipes extends RecipeProvider {
    public Recipes(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        EnchantalCoolerRecipes.register(consumer);
        CookingRecipes.register(consumer);
    }
}
