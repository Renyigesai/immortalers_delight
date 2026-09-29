package com.renyigesai.immortalers_delight.data;

import com.renyigesai.immortalers_delight.data.builder.SeriesBlockRecipeBuilder;
import com.renyigesai.immortalers_delight.data.recipes.CookingRecipes;
import com.renyigesai.immortalers_delight.data.recipes.EnchantalCoolerRecipes;
import com.renyigesai.immortalers_delight.data.recipes.SmeltingRecipes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

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
        SmeltingRecipes.register(consumer);
        SeriesBlockRecipeBuilder.buildRecipes(consumer);
    }

    public static InventoryChangeTrigger.TriggerInstance has(MinMaxBounds.Ints pCount, ItemLike pItem) {
        return RecipeProvider.has(pCount,pItem);
    }

    /**
     * Creates a new {@link InventoryChangeTrigger} that checks for a player having a certain item.
     */
    public static InventoryChangeTrigger.TriggerInstance has(ItemLike pItemLike) {
        return RecipeProvider.has(pItemLike);
    }

    /**
     * Creates a new {@link InventoryChangeTrigger} that checks for a player having an item within the given tag.
     */
    public static InventoryChangeTrigger.TriggerInstance has(TagKey<Item> pTag) {
        return RecipeProvider.has(pTag);
    }
}
