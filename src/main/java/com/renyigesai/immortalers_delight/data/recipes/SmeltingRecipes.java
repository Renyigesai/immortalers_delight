//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.renyigesai.immortalers_delight.data.recipes;

import java.util.function.Consumer;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import net.minecraft.advancements.critereon.InventoryChangeTrigger.TriggerInstance;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.common.registry.ModItems;

public class SmeltingRecipes {
    public SmeltingRecipes() {
    }

    /**一次性生成熔炉，烟熏炉，营火三种配方，感谢农夫乐事！！！*/
    public static void register(Consumer<FinishedRecipe> consumer) {
        foodSmeltingRecipes(ImmortalersDelightItems.EVOLUTCORN.get(),ImmortalersDelightItems.ROAST_EVOLUTCORN.get(), 0.35f,consumer);
        foodSmeltingRecipes(ImmortalersDelightItems.EVOLUTCORN_GRAINS.get(),ImmortalersDelightItems.ROAST_EVOLUTCORN_CHOPS.get(), 0.35f,consumer);
        foodSmeltingRecipes(ImmortalersDelightItems.EVOLUTCORN_PASTE.get(),ImmortalersDelightItems.EVOLUTCORN_BREAD.get(), 0.35f,consumer);
        foodSmeltingRecipes(ImmortalersDelightItems.KWAT_WHEAT_DOUGH.get(),ImmortalersDelightItems.KWAT_WHEAT_TOAST.get(), 0.35f,consumer);
        foodSmeltingRecipes(ImmortalersDelightItems.OXYGRAPE.get(),ImmortalersDelightItems.OXYRAISINS.get(), 0.35f,consumer);
        foodSmeltingRecipes(Items.SNIFFER_EGG,ImmortalersDelightItems.FRIED_SNIFFER_EGG.get(), 0.35f,consumer);
        foodSmeltingRecipes(ImmortalersDelightItems.RAW_SNIFFER_STEAK.get(),ImmortalersDelightItems.COOKED_SNIFFER_STEAK.get(), 0.35f,consumer);
        foodSmeltingRecipes(ImmortalersDelightItems.RAW_SNIFFER_TAIL.get(),ImmortalersDelightItems.COOKED_SNIFFER_TAIL.get(), 0.35f,consumer);
    }

    private static void foodSmeltingRecipes(ItemLike ingredient, ItemLike result, float experience, Consumer<FinishedRecipe> consumer) {
        String path = ForgeRegistries.ITEMS.getKey((Item) result).getPath();
        String namePrefix = (new ResourceLocation("immortalers_delight", path)).toString();
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[]{ingredient}), RecipeCategory.FOOD, result, experience, 200).unlockedBy(path, TriggerInstance.hasItems(new ItemLike[]{ingredient})).save(consumer);
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(new ItemLike[]{ingredient}), RecipeCategory.FOOD, result, experience, 600).unlockedBy(path, TriggerInstance.hasItems(new ItemLike[]{ingredient})).save(consumer, namePrefix + "_from_campfire_cooking");
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(new ItemLike[]{ingredient}), RecipeCategory.FOOD, result, experience, 100).unlockedBy(path, TriggerInstance.hasItems(new ItemLike[]{ingredient})).save(consumer, namePrefix + "_from_smoking");
    }
}
