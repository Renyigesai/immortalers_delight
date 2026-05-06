package com.renyigesai.immortalers_delight.data.recipes;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class SmeltingRecipes {

    public static void register(RecipeOutput output) {
        foodSmeltingRecipes(ImmortalersDelightItems.EVOLUTCORN.get(), ImmortalersDelightItems.ROAST_EVOLUTCORN.get(), 0.35f, output);
        foodSmeltingRecipes(ImmortalersDelightItems.EVOLUTCORN_GRAINS.get(), ImmortalersDelightItems.ROAST_EVOLUTCORN_CHOPS.get(), 0.35f, output);
        foodSmeltingRecipes(ImmortalersDelightItems.EVOLUTCORN_PASTE.get(), ImmortalersDelightItems.EVOLUTCORN_BREAD.get(), 0.35f, output);
        foodSmeltingRecipes(ImmortalersDelightItems.KWAT_WHEAT_DOUGH.get(), ImmortalersDelightItems.KWAT_WHEAT_TOAST.get(), 0.35f, output);
        foodSmeltingRecipes(ImmortalersDelightItems.OXYGRAPE.get(), ImmortalersDelightItems.OXYRAISINS.get(), 0.35f, output);
        foodSmeltingRecipes(Items.SNIFFER_EGG, ImmortalersDelightItems.FRIED_SNIFFER_EGG.get(), 0.35f, output);
        foodSmeltingRecipes(ImmortalersDelightItems.RAW_SNIFFER_STEAK.get(), ImmortalersDelightItems.COOKED_SNIFFER_STEAK.get(), 0.35f, output);
        foodSmeltingRecipes(ImmortalersDelightItems.RAW_SNIFFER_TAIL.get(), ImmortalersDelightItems.COOKED_SNIFFER_TAIL.get(), 0.35f, output);
    }

    private static void foodSmeltingRecipes(ItemLike ingredient, ItemLike result, float experience, RecipeOutput output) {
        String path = BuiltInRegistries.ITEM.getKey(result.asItem()).getPath();
        String namePrefix = ResourceLocation.fromNamespaceAndPath("immortalers_delight", path).toString();
        var hasItem = InventoryChangeTrigger.TriggerInstance.hasItems(ingredient);
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ingredient), RecipeCategory.FOOD, result, experience, 200)
                .unlockedBy(path, hasItem)
                .save(output);
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(ingredient), RecipeCategory.FOOD, result, experience, 600)
                .unlockedBy(path, hasItem)
                .save(output, ResourceLocation.parse(namePrefix + "_from_campfire_cooking"));
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(ingredient), RecipeCategory.FOOD, result, experience, 100)
                .unlockedBy(path, hasItem)
                .save(output, ResourceLocation.parse(namePrefix + "_from_smoking"));
    }
}
