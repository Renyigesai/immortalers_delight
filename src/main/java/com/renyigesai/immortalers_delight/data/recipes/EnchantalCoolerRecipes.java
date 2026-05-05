package com.renyigesai.immortalers_delight.data.recipes;

import com.renyigesai.immortalers_delight.data.builder.EnchantalCoolerBuilder;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightTags;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;

public class EnchantalCoolerRecipes {

    public static void register(RecipeOutput output) {
        onEnchantalCooler(output);
    }

    private static void onEnchantalCooler(RecipeOutput output) {
        EnchantalCoolerBuilder.enchantalCoolerRecipe(ImmortalersDelightItems.ABC_OFFEE.get(), 1, Items.GLASS_BOTTLE)
                .addIngredient(ImmortalersDelightItems.AB_ASH.get())
                .addIngredient(ImmortalersDelightItems.AB_ASH.get())
                .save(output);

        EnchantalCoolerBuilder.enchantalCoolerRecipe(ImmortalersDelightItems.ABBLUE_BEAUTY_C_OFFEE.get(), 1, Items.GLASS_BOTTLE)
                .addIngredient(ImmortalersDelightItems.ABC_OFFEE.get())
                .addIngredient(Items.CORNFLOWER)
                .addIngredient(Items.COD)
                .addIngredient(ImmortalersDelightItems.WARPED_LAUREL.get())
                .save(output);
    }
}
