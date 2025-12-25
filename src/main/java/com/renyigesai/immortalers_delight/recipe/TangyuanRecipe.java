package com.renyigesai.immortalers_delight.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
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

public class TangyuanRecipe implements Recipe<SimpleContainer> {
    private final NonNullList<Ingredient> inputItems;
    private final ItemStack output;
    private final ResourceLocation id;
    private final ItemStack container;
    private final ItemStack result_cache;
    private final boolean finished;

    public TangyuanRecipe(NonNullList<Ingredient> ingredient, ItemStack output, ItemStack container, ItemStack last_result, boolean finish, ResourceLocation id) {
        this.inputItems = ingredient;
        this.output = output;
        this.id = id;
        if (container.isEmpty()){
            this.container = ItemStack.EMPTY;
        }else {
            this.container = container;
        }
        if (last_result.isEmpty()){
            this.result_cache = ItemStack.EMPTY;
        }else {
            this.result_cache = last_result;
        }
        this.finished = finish;
    }

    @Override
    public boolean matches(SimpleContainer inv, Level pLevel) {
        java.util.List<ItemStack> inputs = new java.util.ArrayList<>();
        int i = 0;

        if (this.container.isEmpty() || this.getContainer().is(inv.getItem(4).getItem())) {
            ItemStack stack = inv.getItem(5);
            boolean correct_order = this.getCacheItem().is(stack.getItem()) && this.getCacheItem().getOrCreateTag().equals(stack.getOrCreateTag());
            if (this.result_cache.isEmpty() || correct_order) {
                for (int j = 0; j < 4; ++j) {
                    ItemStack itemstack = inv.getItem(j);
                    if (!itemstack.isEmpty()) {
                        ++i;
                        inputs.add(itemstack);
                    }
                }

            }
        }
        return i == this.inputItems.size() && net.minecraftforge.common.util.RecipeMatcher.findMatches(inputs, this.inputItems) != null;
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer, RegistryAccess pRegistryAccess) {
        ItemStack result = output.copy();
        CompoundTag outputNBT = result.getTag();
        if (outputNBT != null && !outputNBT.isEmpty()) {
            result.setTag(outputNBT); // 附加NBT标签
            // 若需要合并现有NBT，使用：result.getTag().merge(outputNBT);
        }
        return result;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    public ItemStack getContainer() {
        return container.copy();
    }
    public ItemStack getCacheItem() {
        return result_cache.copy();
    }

    public boolean isFinished() {
        return finished;
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
        return TangyuanRecipe.Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return TangyuanRecipe.Type.INSTANCE;
    }

    public static class Type implements RecipeType<TangyuanRecipe> {
        public static final TangyuanRecipe.Type INSTANCE = new TangyuanRecipe.Type();
        public static final String ID = "tangyuan";
    }

    public static class Serializer implements RecipeSerializer<TangyuanRecipe> {
        public static final TangyuanRecipe.Serializer INSTANCE = new TangyuanRecipe.Serializer();
        public static final ResourceLocation ID = new ResourceLocation(ImmortalersDelightMod.MODID, "tangyuan");

        @Override
        public TangyuanRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            // 动态获取原料数量
            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.create();

            for (int i = 0; i < ingredients.size(); i++) {
                inputs.add(Ingredient.fromJson(ingredients.get(i)));
            }
            // 读取结果物品及NBT
            ItemStack output = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"),true);
            //读取容器和上一步缓存物品
            ItemStack container = GsonHelper.isValidNode(pSerializedRecipe, "container") ? CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(pSerializedRecipe, "container"), true) : ItemStack.EMPTY;
            ItemStack last_result = GsonHelper.isValidNode(pSerializedRecipe, "previous_result") ? CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(pSerializedRecipe, "previous_result"), true) : ItemStack.EMPTY;
            boolean finished = !GsonHelper.isValidNode(pSerializedRecipe, "finished") || GsonHelper.getAsBoolean(pSerializedRecipe, "finished");
            return new TangyuanRecipe(inputs, output,container,last_result,finished,pRecipeId);
        }

        @Override
        public @Nullable TangyuanRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            int ingredientCount = pBuffer.readInt();
            NonNullList<Ingredient> inputs = NonNullList.withSize(ingredientCount, Ingredient.EMPTY);

            for (int i = 0; i < ingredientCount; i++) {
                inputs.set(i, Ingredient.fromNetwork(pBuffer));
            }
            ItemStack output = pBuffer.readItem();
            ItemStack container = pBuffer.readItem();
            ItemStack last_result = pBuffer.readItem();
            boolean finished = pBuffer.readBoolean();
            return new TangyuanRecipe(inputs, output,container,last_result,finished,pRecipeId);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, TangyuanRecipe pRecipe) {
            pBuffer.writeInt(pRecipe.inputItems.size());

            for (Ingredient ingredient : pRecipe.getIngredients()) {
                ingredient.toNetwork(pBuffer);
            }
            pBuffer.writeItemStack(pRecipe.getResultItem(null), false);
            pBuffer.writeItem(pRecipe.getContainer());
            pBuffer.writeItemStack(pRecipe.getCacheItem(),false);
            pBuffer.writeBoolean(pRecipe.isFinished());
        }
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return inputItems;
    }
}
