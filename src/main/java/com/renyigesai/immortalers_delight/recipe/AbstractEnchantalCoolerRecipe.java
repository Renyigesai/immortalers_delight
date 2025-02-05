package com.renyigesai.immortalers_delight.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.openjdk.nashorn.internal.objects.annotations.Getter;

public class AbstractEnchantalCoolerRecipe implements Recipe<Container> {
    private  final RecipeType<?> type;
    private final RecipeSerializer<?> serializer;
    protected final ResourceLocation id;
    protected final ItemStack output;
    protected final int time;
    protected final Ingredient recipeItems;
    public AbstractEnchantalCoolerRecipe(RecipeType<?> recipeType, RecipeSerializer<?> serializer, ResourceLocation id, ItemStack output, int time, Ingredient recipeItems) {
        this.type = recipeType;
        this.serializer = serializer;
        this.id = id;
        this.output = output;
        this.time = time;
        this.recipeItems = recipeItems;
    }
    @Override
    public boolean matches(Container pInv, Level pLevel) {
        if(pLevel.isClientSide()){
            return false;
        }
        return this.recipeItems.test(pInv.getItem(0))
                || this.recipeItems.test(pInv.getItem(1))
                || this.recipeItems.test(pInv.getItem(2))
                || this.recipeItems.test(pInv.getItem(3));
    }
    @Override
    public ItemStack assemble(Container pContainer, RegistryAccess pRegistryAccess) {
        return this.output.copy();
    }
    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.add(this.recipeItems);
        return nonnulllist;
    }
    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess access) {
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeType<?> getType() {
        return type;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return serializer;
    }
}
