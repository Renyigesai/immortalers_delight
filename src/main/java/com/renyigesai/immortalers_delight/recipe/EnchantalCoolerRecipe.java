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
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
//        if (pLevel.isClientSide) {
//            return false;
//        }
//
//        // 检查输入容器中的物品是否与配方匹配
//        for (int i = 0; i < inputItems.size(); i++) {
//            if (!inputItems.get(i).test(pContainer.getItem(i))) {
//                return false;
//            }
//        }
//        return true;
        if (pLevel.isClientSide) return false;

        // 转换为列表以便修改
        List<ItemStack> inputs = new ArrayList<>();
        for (int i = 0; i < pContainer.getContainerSize(); i++) {
            ItemStack item = pContainer.getItem(i);
            if (!item.isEmpty()) inputs.add(item);
        }

        // 检查原料数量是否匹配
        if (inputs.size() != this.inputItems.size()) return false;

        // 复制配方原料用于匹配消耗
        List<Ingredient> ingredientsToCheck = new ArrayList<>(this.inputItems);

        // 无序匹配逻辑
        outer:
        for (ItemStack input : inputs) {
            for (Ingredient ingredient : ingredientsToCheck) {
                if (ingredient.test(input)) {
                    ingredientsToCheck.remove(ingredient);
                    continue outer;
                }
            }
            return false;
        }
        return true;
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
            ItemStack container = pBuffer.readItem();
            ItemStack output = pBuffer.readItem();
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
