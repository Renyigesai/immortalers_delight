package com.renyigesai.immortalers_delight.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChangeBlockRecipe implements Recipe<SimpleContainerRecipeInput> {

    private final NonNullList<AggregatedBlock> inputBlocks;
    private final NonNullList<String> propertiesNeedDefault;
    private final NonNullList<String> propertiesKeepDefault;
    private final NonNullList<BlockPos> blockPoss;
    private final String recipeType;
    private final BlockState target;

    public ChangeBlockRecipe(String recipeType,
                             NonNullList<AggregatedBlock> ingredient,
                             NonNullList<String> propertiesNeedDefault,
                             NonNullList<String> propertiesKeepDefault,
                             NonNullList<BlockPos> blockPoss,
                             BlockState target) {
        this.inputBlocks = ingredient;
        this.recipeType = recipeType;
        this.propertiesNeedDefault = propertiesNeedDefault;
        this.propertiesKeepDefault = propertiesKeepDefault;
        this.blockPoss = blockPoss;
        this.target = target.isAir() ? Blocks.AIR.defaultBlockState() : target;
    }

    private static ChangeBlockRecipe fromCodec(String recipeType,
                                               List<AggregatedBlock> needBlocks,
                                               List<String> propertiesNeedDefault,
                                               List<String> propertiesKeepDefault,
                                               BlockState output) {
        if (needBlocks.size() > 9) {
            throw new IllegalStateException("Too many needblocks for block change recipe! The max is 9");
        }
        return new ChangeBlockRecipe(
                recipeType,
                NonNullList.copyOf(needBlocks),
                NonNullList.copyOf(propertiesNeedDefault),
                NonNullList.copyOf(propertiesKeepDefault),
                NonNullList.create(),
                output
        );
    }

    @Override
    public boolean matches(SimpleContainerRecipeInput pContainer, Level pLevel) {
        return false;
    }

    public boolean matchBlocks(@NotNull NonNullList<BlockState> states, @NotNull Level pLevel) {
        return states.size() == this.inputBlocks.size()
                && net.neoforged.neoforge.common.util.RecipeMatcher.findMatches(states, this.inputBlocks) != null;
    }

    @Override
    public ItemStack assemble(SimpleContainerRecipeInput pContainer, HolderLookup.Provider registries) {
        return new ItemStack(this.target.getBlock().asItem());
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    public @NotNull NonNullList<BlockPos> getBlockPoss() {
        return this.blockPoss;
    }

    public @NotNull NonNullList<String> getPropertiesNeedDefault() {
        return this.propertiesNeedDefault;
    }

    public @NotNull NonNullList<String> getPropertiesKeepDefault() {
        return this.propertiesKeepDefault;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.Provider registries) {
        return new ItemStack(this.target.getBlock().asItem());
    }

    public @NotNull BlockState getResultBlock() {
        return this.target;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public @NotNull NonNullList<AggregatedBlock> getInputBlocks() {
        return this.inputBlocks;
    }

    public String getChangeType() {
        return this.recipeType;
    }

    public static class Type implements RecipeType<ChangeBlockRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "change_block";
    }

    public static class Serializer implements RecipeSerializer<ChangeBlockRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "change_block");

        public static final MapCodec<ChangeBlockRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.STRING.optionalFieldOf("change_type", "").forGetter(ChangeBlockRecipe::getChangeType),
                AggregatedBlock.CODEC.listOf(1, 9).fieldOf("needblocks").forGetter(r -> List.copyOf(r.inputBlocks)),
                // Keep 1201 JSON field typos for compatibility
                Codec.STRING.listOf().optionalFieldOf("properties_need_defult", List.of()).forGetter(r -> List.copyOf(r.propertiesNeedDefault)),
                Codec.STRING.listOf().optionalFieldOf("properties_keep_defult", List.of()).forGetter(r -> List.copyOf(r.propertiesKeepDefault)),
                BlockState.CODEC.optionalFieldOf("output", Blocks.AIR.defaultBlockState()).forGetter(r -> r.target)
        ).apply(instance, ChangeBlockRecipe::fromCodec));

        private static final StreamCodec<RegistryFriendlyByteBuf, ChangeBlockRecipe> STREAM_CODEC =
                StreamCodec.of(Serializer::writeNetwork, Serializer::readNetwork);

        private static void writeNetwork(RegistryFriendlyByteBuf buf, ChangeBlockRecipe recipe) {
            buf.writeUtf(recipe.recipeType);
            buf.writeVarInt(recipe.inputBlocks.size());
            for (AggregatedBlock ingredient : recipe.inputBlocks) {
                AggregatedBlock.STREAM_CODEC.encode(buf, ingredient);
            }
            ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()).encode(buf, List.copyOf(recipe.propertiesNeedDefault));
            ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()).encode(buf, List.copyOf(recipe.propertiesKeepDefault));
            AggregatedBlock.BLOCK_STATE_STREAM_CODEC.encode(buf, recipe.target);
        }

        private static ChangeBlockRecipe readNetwork(RegistryFriendlyByteBuf buf) {
            String recipeType = buf.readUtf();
            int size = buf.readVarInt();
            NonNullList<AggregatedBlock> inputs = NonNullList.withSize(size, AggregatedBlock.EMPTY);
            for (int i = 0; i < size; i++) {
                inputs.set(i, AggregatedBlock.STREAM_CODEC.decode(buf));
            }
            List<String> propertiesNeedDefault = ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()).decode(buf);
            List<String> propertiesKeepDefault = ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()).decode(buf);
            BlockState output = AggregatedBlock.BLOCK_STATE_STREAM_CODEC.decode(buf);
            return new ChangeBlockRecipe(
                    recipeType,
                    inputs,
                    NonNullList.copyOf(propertiesNeedDefault),
                    NonNullList.copyOf(propertiesKeepDefault),
                    NonNullList.create(),
                    output
            );
        }

        @Override
        public MapCodec<ChangeBlockRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ChangeBlockRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
