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

public class EnchantalCoolerRecipe implements Recipe<SimpleContainerRecipeInput> {
    private final NonNullList<Ingredient> inputItems;
    protected final ItemStack output;
    protected final ItemStack container;

    public EnchantalCoolerRecipe(NonNullList<Ingredient> ingredient, ItemStack output, ItemStack container) {
        this.inputItems = ingredient;
        this.output = output;
        if (container.isEmpty()) {
            this.container = ItemStack.EMPTY;
        } else {
            this.container = container;
        }
    }

    private static EnchantalCoolerRecipe fromCodec(List<Ingredient> ingredients, ItemStack output, ItemStack container) {
        return new EnchantalCoolerRecipe(NonNullList.copyOf(ingredients), output, container);
    }

    @Override
    public boolean matches(SimpleContainerRecipeInput recipeInput, Level pLevel) {
        SimpleContainer inv = recipeInput.container();
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

    public ItemStack getContainer() {
        return container.copy();
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

    public static class Type implements RecipeType<EnchantalCoolerRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "enchantal_cooler";
    }

    public static class Serializer implements RecipeSerializer<EnchantalCoolerRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "enchantal_cooler");

        public static final MapCodec<EnchantalCoolerRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.CODEC.listOf(1, 4).fieldOf("ingredients").forGetter(r -> List.copyOf(r.inputItems)),
                ItemStack.STRICT_CODEC.fieldOf("output").forGetter(r -> r.output),
                ItemStack.STRICT_CODEC.optionalFieldOf("container", ItemStack.EMPTY).forGetter(r -> r.container)
        ).apply(instance, EnchantalCoolerRecipe::fromCodec));

        private static final StreamCodec<RegistryFriendlyByteBuf, EnchantalCoolerRecipe> STREAM_CODEC =
                StreamCodec.of(Serializer::writeNetwork, Serializer::readNetwork);

        private static void writeNetwork(RegistryFriendlyByteBuf buf, EnchantalCoolerRecipe recipe) {
            buf.writeVarInt(recipe.inputItems.size());
            for (Ingredient ingredient : recipe.inputItems) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buf, ingredient);
            }
            ItemStack.STREAM_CODEC.encode(buf, recipe.output);
            ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, recipe.container);
        }

        private static EnchantalCoolerRecipe readNetwork(RegistryFriendlyByteBuf buf) {
            int size = buf.readVarInt();
            NonNullList<Ingredient> inputs = NonNullList.withSize(size, Ingredient.EMPTY);
            for (int i = 0; i < size; i++) {
                inputs.set(i, Ingredient.CONTENTS_STREAM_CODEC.decode(buf));
            }
            ItemStack output = ItemStack.STREAM_CODEC.decode(buf);
            ItemStack container = ItemStack.OPTIONAL_STREAM_CODEC.decode(buf);
            return new EnchantalCoolerRecipe(inputs, output, container);
        }

        @Override
        public MapCodec<EnchantalCoolerRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, EnchantalCoolerRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return inputItems;
    }
}
