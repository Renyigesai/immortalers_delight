package com.renyigesai.immortalers_delight.data.recipes;

import com.renyigesai.immortalers_delight.data.builder.EnchantalCoolerBuilder;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Items;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.function.Consumer;

public class EnchantalCoolerRecipes{

    public static void register(Consumer<FinishedRecipe> consumer) {
        onEnchantalCooler(consumer);
    }
    private static void onEnchantalCooler(Consumer<FinishedRecipe> consumer){
//        EnchantalCoolerBuilder.enchantalCoolerRecipe(ImmortalersDelightItems.AROMATIC_POD_AFFOGATO.get(),1, ModItems.HOT_COCOA.get())
//                .addIngredient(ModItems.MILK_BOTTLE.get())
//                .addIngredient(ImmortalersDelightItems.PITCHER_POD_PETAL.get())
//                .addIngredient(ImmortalersDelightItems.TORCHFLOWER_MUSTARD.get())
//                .addIngredient(Items.EGG).build(consumer);
    }
}
