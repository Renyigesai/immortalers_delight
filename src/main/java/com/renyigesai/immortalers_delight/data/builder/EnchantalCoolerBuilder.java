package com.renyigesai.immortalers_delight.data.builder;

import com.google.common.collect.Lists;
import com.renyigesai.immortalers_delight.recipe.EnchantalCoolerRecipe;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class EnchantalCoolerBuilder {
    private final List<Ingredient> ingredients = Lists.newArrayList();
    private final Item result;
    private final int count;
    private final Item container;

    private EnchantalCoolerBuilder(ItemLike resultIn, int count, @Nullable ItemLike container) {
        this.result = resultIn.asItem();
        this.count = count;
        this.container = container != null ? container.asItem() : null;
    }

    public static EnchantalCoolerBuilder enchantalCoolerRecipe(ItemLike mainResult, int count) {
        return new EnchantalCoolerBuilder(mainResult, count, null);
    }

    public static EnchantalCoolerBuilder enchantalCoolerRecipe(ItemLike mainResult, int count, ItemLike container) {
        return new EnchantalCoolerBuilder(mainResult, count, container);
    }

    public EnchantalCoolerBuilder addIngredient(TagKey<Item> tagIn) {
        return this.addIngredient(Ingredient.of(tagIn));
    }

    public EnchantalCoolerBuilder addIngredient(ItemLike itemIn) {
        return this.addIngredient(itemIn, 1);
    }

    public EnchantalCoolerBuilder addIngredient(ItemLike itemIn, int quantity) {
        for (int i = 0; i < quantity; ++i) {
            this.addIngredient(Ingredient.of(itemIn));
        }
        return this;
    }

    public EnchantalCoolerBuilder addIngredient(Ingredient ingredientIn) {
        return this.addIngredient(ingredientIn, 1);
    }

    public EnchantalCoolerBuilder addIngredient(Ingredient ingredientIn, int quantity) {
        for (int i = 0; i < quantity; ++i) {
            this.ingredients.add(ingredientIn);
        }
        return this;
    }

    public void save(RecipeOutput output) {
        ResourceLocation location = BuiltInRegistries.ITEM.getKey(this.result);
        this.save(output, ResourceLocation.fromNamespaceAndPath("immortalers_delight", "enchantal_cooler/" + location.getPath()));
    }

    public void save(RecipeOutput output, String save) {
        ResourceLocation resourcelocation = BuiltInRegistries.ITEM.getKey(this.result);
        ResourceLocation parsed = ResourceLocation.parse(save);
        if (parsed.equals(resourcelocation)) {
            throw new IllegalStateException("Enchantal Cooler Recipe " + save + " should remove its 'save' argument");
        }
        this.save(output, parsed);
    }

    public void save(RecipeOutput output, ResourceLocation id) {
        NonNullList<Ingredient> list = NonNullList.create();
        list.addAll(this.ingredients);
        ItemStack outStack = new ItemStack(this.result, this.count);
        ItemStack containerStack = this.container != null ? new ItemStack(this.container) : ItemStack.EMPTY;
        EnchantalCoolerRecipe recipe = new EnchantalCoolerRecipe(list, outStack, containerStack);
        output.accept(id, recipe, null);
    }
}
