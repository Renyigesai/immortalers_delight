package com.renyigesai.immortalers_delight.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.RecipeMatcher;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SoulInfuserRecipe implements Recipe<SimpleContainer> {
    private final NonNullList<Ingredient> inputItems;
    private final ItemStack output;
    private final int cookingTime;
    private final ResourceLocation id;

    public SoulInfuserRecipe(NonNullList<Ingredient> ingredient, ItemStack output, int cookingTime, ResourceLocation id) {
        this.inputItems = ingredient;
        this.output = output;
        this.cookingTime = cookingTime;
        this.id = id;
    }

    public int getCookingTime() {
        return this.cookingTime;
    }

    @Override
    public boolean matches(@NotNull SimpleContainer inv, @NotNull Level pLevel) {
        List<ItemStack> inputs = new ArrayList<>();
        int count = 0;
        for (int i = 0; i < 9; ++i) {
            ItemStack itemstack = inv.getItem(i);
            if (!itemstack.isEmpty()) {
                ++count;
                inputs.add(itemstack);
            }
        }
        return count == this.inputItems.size() && RecipeMatcher.findMatches(inputs, this.inputItems) != null;
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer, RegistryAccess pRegistryAccess) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return inputItems;
    }

    public static class Type implements RecipeType<SoulInfuserRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "soul_infuser";
    }

    public static class Serializer implements RecipeSerializer<SoulInfuserRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(ImmortalersDelightMod.MODID, "soul_infuser");

        @Override
        public SoulInfuserRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));
            int cookingTime = GsonHelper.getAsInt(pSerializedRecipe, "cookingtime", 200);

            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.create();

            if (ingredients.size() > 9) {
                throw new JsonParseException("Too many ingredients for soul infuser recipe! The max is 9");
            }
            for (int i = 0; i < ingredients.size(); i++) {
                inputs.add(Ingredient.fromJson(ingredients.get(i)));
            }

            return new SoulInfuserRecipe(inputs, output, cookingTime, pRecipeId);
        }

        @Override
        public @Nullable SoulInfuserRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            int count = pBuffer.readInt();
            NonNullList<Ingredient> inputs = NonNullList.withSize(count, Ingredient.EMPTY);
            for (int i = 0; i < count; i++) {
                inputs.set(i, Ingredient.fromNetwork(pBuffer));
            }
            ItemStack output = pBuffer.readItem();
            int cookingTime = pBuffer.readInt();
            return new SoulInfuserRecipe(inputs, output, cookingTime, pRecipeId);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, SoulInfuserRecipe pRecipe) {
            pBuffer.writeInt(pRecipe.inputItems.size());
            for (Ingredient ingredient : pRecipe.getIngredients()) {
                ingredient.toNetwork(pBuffer);
            }
            pBuffer.writeItemStack(pRecipe.getResultItem(null), false);
            pBuffer.writeInt(pRecipe.cookingTime);
        }
    }
}
