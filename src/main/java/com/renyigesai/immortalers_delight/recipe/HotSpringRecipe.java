package com.renyigesai.immortalers_delight.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HotSpringRecipe implements Recipe<SimpleContainerRecipeInput> {
    private final NonNullList<Ingredient> inputItems;
    private final ItemStack output;

    public HotSpringRecipe(NonNullList<Ingredient> ingredient, ItemStack output) {
        this.inputItems = ingredient;
        this.output = output;
    }

    private static HotSpringRecipe fromCodec(List<Ingredient> ingredients, ItemStack output) {
        if (ingredients.size() > 10) {
            throw new IllegalStateException("Too many ingredients for hot spring recipe! The max is 10");
        }
        return new HotSpringRecipe(NonNullList.copyOf(ingredients), output);
    }

    @Override
    public boolean matches(SimpleContainerRecipeInput recipeInput, Level pLevel) {
        SimpleContainer pContainer = recipeInput.container();
        java.util.List<ItemStack> inputs = new java.util.ArrayList<>();
        int i = 0;
        for (int j = 0; j < 9; ++j) {
            ItemStack itemstack = pContainer.getItem(j);
            if (!itemstack.isEmpty()) {
                ++i;
                inputs.add(itemstack);
            }
        }
        return i == this.inputItems.size() && net.neoforged.neoforge.common.util.RecipeMatcher.findMatches(inputs, this.inputItems) != null;
    }

    @Override
    public ItemStack assemble(SimpleContainerRecipeInput pContainer, HolderLookup.Provider registries) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return output.copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<HotSpringRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "hot_spring";
    }

    public static class Serializer implements RecipeSerializer<HotSpringRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "hot_spring");

        public static final MapCodec<HotSpringRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.CODEC.listOf(1, 10).fieldOf("ingredients").forGetter(r -> List.copyOf(r.inputItems)),
                ItemStack.STRICT_CODEC.fieldOf("output").forGetter(r -> r.output)
        ).apply(instance, HotSpringRecipe::fromCodec));

        private static final StreamCodec<RegistryFriendlyByteBuf, HotSpringRecipe> STREAM_CODEC =
                StreamCodec.of(Serializer::writeNetwork, Serializer::readNetwork);

        private static void writeNetwork(RegistryFriendlyByteBuf buf, HotSpringRecipe recipe) {
            buf.writeVarInt(recipe.inputItems.size());
            for (Ingredient ingredient : recipe.getIngredients()) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buf, ingredient);
            }
            ItemStack.STREAM_CODEC.encode(buf, recipe.output);
        }

        private static HotSpringRecipe readNetwork(RegistryFriendlyByteBuf buf) {
            int size = buf.readVarInt();
            NonNullList<Ingredient> inputs = NonNullList.withSize(size, Ingredient.EMPTY);
            for (int i = 0; i < size; i++) {
                inputs.set(i, Ingredient.CONTENTS_STREAM_CODEC.decode(buf));
            }
            ItemStack output = ItemStack.STREAM_CODEC.decode(buf);
            return new HotSpringRecipe(inputs, output);
        }

        @Override
        public MapCodec<HotSpringRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, HotSpringRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return inputItems;
    }
}
