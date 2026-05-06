package com.renyigesai.immortalers_delight.data.recipes;

import com.renyigesai.immortalers_delight.data.builder.ImmCookingPotRecipeBuilder;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;
import vectorwing.farmersdelight.common.registry.ModItems;

public class CookingRecipes {

    public static void register(RecipeOutput output) {
        onCooking(output);
    }

    private static void onCooking(RecipeOutput output) {
        ImmCookingPotRecipeBuilder.cookingPotRecipe(ImmortalersDelightItems.STINKY_DOUFU.get(), 2, 200, 0.35f)
                .addIngredient(ImmortalersDelightItems.FRY_KWAT_WHEAT_DOUFU.get())
                .addIngredient(ImmortalersDelightItems.FRY_KWAT_WHEAT_DOUFU.get())
                .addIngredient(ImmortalersDelightItems.TORCHFLOWER_MUSTARD.get())
                .addIngredient(Items.FERMENTED_SPIDER_EYE).save(output);

        ImmCookingPotRecipeBuilder.cookingPotRecipe(ImmortalersDelightItems.MAPO_DOUFU.get(), 1, 200, 0.35f, Items.BOWL)
                .addIngredient(ImmortalersDelightItems.KWAT_WHEAT_DOUFU.get())
                .addIngredient(ImmortalersDelightItems.KWAT_WHEAT_DOUFU.get())
                .addIngredient(ImmortalersDelightItems.TORCHFLOWER_MUSTARD.get())
                .addIngredient(Items.TORCHFLOWER).save(output);

        ImmCookingPotRecipeBuilder.cookingPotRecipe(ImmortalersDelightItems.ROASTED_MUSHROOM_PIZZA.get(), 1, 200, 0.35f)
                .addIngredient(Items.BROWN_MUSHROOM)
                .addIngredient(Items.BROWN_MUSHROOM)
                .addIngredient(Items.TORCHFLOWER)
                .addIngredient(ImmortalersDelightItems.PITCHER_POD_PETAL.get())
                .addIngredient(ImmortalersDelightItems.PITCHER_POD_PETAL.get())
                .addIngredient(ModItems.WHEAT_DOUGH.get()).save(output);
    }

}
