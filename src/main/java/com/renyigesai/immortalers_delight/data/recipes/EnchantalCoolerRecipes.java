package com.renyigesai.immortalers_delight.data.recipes;

import com.renyigesai.immortalers_delight.data.builder.EnchantalCoolerBuilder;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightTags;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Items;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.function.Consumer;

public class EnchantalCoolerRecipes{

    public static void register(Consumer<FinishedRecipe> consumer) {
        onEnchantalCooler(consumer);
    }
    private static void onEnchantalCooler(Consumer<FinishedRecipe> consumer){
        EnchantalCoolerBuilder.enchantalCoolerRecipe(ImmortalersDelightItems.ABC_OFFEE.get(),1, Items.GLASS_BOTTLE)
                .addIngredient(ImmortalersDelightItems.AB_ASH.get())
                .addIngredient(ImmortalersDelightItems.AB_ASH.get())
                .build(consumer);

        EnchantalCoolerBuilder.enchantalCoolerRecipe(ImmortalersDelightItems.ABBLUE_BEAUTY_C_OFFEE.get(),1, Items.GLASS_BOTTLE)
                .addIngredient(ImmortalersDelightItems.ABC_OFFEE.get())
                .addIngredient(Items.CORNFLOWER)
                .addIngredient(Items.COD)
                .addIngredient(ImmortalersDelightItems.WARPED_LAUREL.get())
                .build(consumer);
    }
}
