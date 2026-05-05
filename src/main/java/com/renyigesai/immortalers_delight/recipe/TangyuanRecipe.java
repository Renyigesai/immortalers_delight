package com.renyigesai.immortalers_delight.recipe;

import com.mojang.serialization.Codec;
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

public class TangyuanRecipe implements Recipe<SimpleContainerRecipeInput> {
    private final NonNullList<Ingredient> inputItems;
    private final ItemStack output;
    private final ItemStack container;
    private final Ingredient tool;
    private final ItemStack result_cache;
    private final boolean finished;

    public TangyuanRecipe(NonNullList<Ingredient> ingredient, ItemStack output, ItemStack container, ItemStack last_result, boolean finish, Ingredient tool_item) {
        this.inputItems = ingredient;
        this.output = output;
        if (container.isEmpty()) {
            this.container = ItemStack.EMPTY;
        } else {
            this.container = container;
        }
        if (last_result.isEmpty()) {
            this.result_cache = ItemStack.EMPTY;
        } else {
            this.result_cache = last_result;
        }
        this.finished = finish;
        this.tool = tool_item;
    }

    private static TangyuanRecipe fromCodec(List<Ingredient> ingredients, ItemStack output, ItemStack container, ItemStack previousResult, Boolean finished, Ingredient tool) {
        return new TangyuanRecipe(NonNullList.copyOf(ingredients), output, container, previousResult, finished, tool);
    }

    @Override
    public boolean matches(SimpleContainerRecipeInput recipeInput, Level pLevel) {
        SimpleContainer inv = recipeInput.container();
        java.util.List<ItemStack> inputs = new java.util.ArrayList<>();
        int i = 0;

        if (this.container.isEmpty() || this.getContainer().is(inv.getItem(4).getItem())) {
            ItemStack stack = inv.getItem(6);
            boolean correct_order = ItemStack.isSameItemSameComponents(this.getCacheItem(), stack);
            if (this.result_cache.isEmpty() || correct_order) {
                for (int j = 0; j < 4; ++j) {
                    ItemStack itemstack = inv.getItem(j);
                    if (!itemstack.isEmpty()) {
                        ++i;
                        inputs.add(itemstack);
                    }
                }
                System.out.println("输入物品数量：" + i + ",输入物品：" + inputs);
            } else System.out.println("缓存不对");
        } else System.out.println("容器不对");
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

    public ItemStack getCacheItem() {
        return result_cache.copy();
    }

    public Ingredient getTool() {
        return this.tool;
    }

    public boolean isFinished() {
        return finished;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return output.copy();
    }

    public ItemStack copyOutputForNetwork() {
        return output.copy();
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
        public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "tangyuan");

        public static final MapCodec<TangyuanRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.CODEC.listOf(1, 9).fieldOf("ingredients").forGetter(r -> List.copyOf(r.inputItems)),
                ItemStack.STRICT_CODEC.fieldOf("output").forGetter(r -> r.output),
                ItemStack.STRICT_CODEC.optionalFieldOf("container", ItemStack.EMPTY).forGetter(r -> r.container),
                ItemStack.STRICT_CODEC.optionalFieldOf("previous_result", ItemStack.EMPTY).forGetter(r -> r.result_cache),
                Codec.BOOL.optionalFieldOf("finished", true).forGetter(TangyuanRecipe::isFinished),
                Ingredient.CODEC.fieldOf("tool").forGetter(TangyuanRecipe::getTool)
        ).apply(instance, TangyuanRecipe::fromCodec));

        private static final StreamCodec<RegistryFriendlyByteBuf, TangyuanRecipe> STREAM_CODEC =
                StreamCodec.of(Serializer::writeNetwork, Serializer::readNetwork);

        private static void writeNetwork(RegistryFriendlyByteBuf buf, TangyuanRecipe recipe) {
            buf.writeVarInt(recipe.inputItems.size());
            for (Ingredient ingredient : recipe.getIngredients()) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buf, ingredient);
            }
            ItemStack.STREAM_CODEC.encode(buf, recipe.copyOutputForNetwork());
            ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, recipe.getContainer());
            ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, recipe.getCacheItem());
            Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.getTool());
            buf.writeBoolean(recipe.isFinished());
        }

        private static TangyuanRecipe readNetwork(RegistryFriendlyByteBuf buf) {
            int size = buf.readVarInt();
            NonNullList<Ingredient> inputs = NonNullList.withSize(size, Ingredient.EMPTY);
            for (int i = 0; i < size; i++) {
                inputs.set(i, Ingredient.CONTENTS_STREAM_CODEC.decode(buf));
            }
            ItemStack output = ItemStack.STREAM_CODEC.decode(buf);
            ItemStack container = ItemStack.OPTIONAL_STREAM_CODEC.decode(buf);
            ItemStack last_result = ItemStack.OPTIONAL_STREAM_CODEC.decode(buf);
            Ingredient toolIn = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
            boolean finished = buf.readBoolean();
            return new TangyuanRecipe(inputs, output, container, last_result, finished, toolIn);
        }

        @Override
        public MapCodec<TangyuanRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, TangyuanRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return inputItems;
    }
}
