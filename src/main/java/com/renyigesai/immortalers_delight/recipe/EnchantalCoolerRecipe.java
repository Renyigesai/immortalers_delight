package com.renyigesai.immortalers_delight.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.openjdk.nashorn.internal.objects.annotations.Getter;

import java.util.ArrayList;
import java.util.List;

public class EnchantalCoolerRecipe implements Recipe<SimpleContainer> {
    private final NonNullList<Ingredient> inputItems;
    private final ItemStack output;
    private final ResourceLocation id;
    private final ItemStack container;

    public EnchantalCoolerRecipe(NonNullList<Ingredient> ingredient, ItemStack output,ItemStack container, ResourceLocation id) {
        this.inputItems = ingredient;
        this.output = output;
        this.id = id;
        if (container.isEmpty()){
            this.container = ItemStack.EMPTY;
        }else {
            this.container = container;
        }
    }

    @Override
    public boolean matches(SimpleContainer inv, Level pLevel) {
        java.util.List<ItemStack> inputs = new java.util.ArrayList<>();
        int i = 0;

        if (this.container.isEmpty() || this.getContainer().is(inv.getItem(4).getItem())) {
            for (int j = 0; j < 4; ++j) {
                ItemStack itemstack = inv.getItem(j);
                if (!itemstack.isEmpty()) {
                    ++i;
                    inputs.add(itemstack);
                }
            }
            System.out.println("输入物品数量：" + i + ",输入物品：" + inputs);
        }else System.out.println("容器不对");
        return i == this.inputItems.size() && net.minecraftforge.common.util.RecipeMatcher.findMatches(inputs, this.inputItems) != null;
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer, RegistryAccess pRegistryAccess) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    public ItemStack getContainer() {
        return container.copy();
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

    public static class Type implements RecipeType<EnchantalCoolerRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "enchantal_cooler";
    }

    public static class Serializer implements RecipeSerializer<EnchantalCoolerRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(ImmortalersDelightMod.MODID, "enchantal_cooler");

        @Override
        public EnchantalCoolerRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));

            // 动态获取原料数量
            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.create();

            for (int i = 0; i < ingredients.size(); i++) {
                inputs.add(Ingredient.fromJson(ingredients.get(i)));
            }
            ItemStack container = GsonHelper.isValidNode(pSerializedRecipe, "container") ? CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(pSerializedRecipe, "container"), true) : ItemStack.EMPTY;

            return new EnchantalCoolerRecipe(inputs, output,container,pRecipeId);
        }

        @Override
        public @Nullable EnchantalCoolerRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            int ingredientCount = pBuffer.readInt();
            NonNullList<Ingredient> inputs = NonNullList.withSize(ingredientCount, Ingredient.EMPTY);

            for (int i = 0; i < ingredientCount; i++) {
                inputs.set(i, Ingredient.fromNetwork(pBuffer));
            }
            ItemStack output = pBuffer.readItem();
            ItemStack container = pBuffer.readItem();
            return new EnchantalCoolerRecipe(inputs, output,container,pRecipeId);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, EnchantalCoolerRecipe pRecipe) {
            pBuffer.writeInt(pRecipe.inputItems.size());

            for (Ingredient ingredient : pRecipe.getIngredients()) {
                ingredient.toNetwork(pBuffer);
            }
            pBuffer.writeItemStack(pRecipe.getResultItem(null), false);
            pBuffer.writeItem(pRecipe.container);
        }
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return inputItems;
    }
}
