package com.renyigesai.immortalers_delight.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.item.weapon.PillagersKnifeItem;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PillagerKnifeAddPotionRecipe extends EnchantalCoolerRecipe {
    public PillagerKnifeAddPotionRecipe(NonNullList<Ingredient> ingredient, ItemStack jsonOutput, ItemStack jsonContainer) {
        super(ingredient, new ItemStack(ImmortalersDelightItems.PILLAGER_KNIFE.get()), new ItemStack(ImmortalersDelightItems.PILLAGER_KNIFE.get()));
    }

    private static PillagerKnifeAddPotionRecipe fromCodec(List<Ingredient> ingredients, ItemStack jsonOutput, ItemStack jsonContainer) {
        return new PillagerKnifeAddPotionRecipe(NonNullList.copyOf(ingredients), jsonOutput, jsonContainer);
    }

    @Override
    public boolean matches(SimpleContainerRecipeInput recipeInput, Level pLevel) {
        SimpleContainer inv = recipeInput.container();
        boolean hasPotion = false;

        if (this.getContainer().isEmpty() || this.getContainer().is(inv.getItem(4).getItem())) {
            for (int j = 0; j < 4; ++j) {
                ItemStack itemstack = inv.getItem(j);
                if (PillagersKnifeItem.hasPotionCoating(itemstack)) {
                    if (hasPotion) {
                        return false;
                    } else {
                        hasPotion = true;
                    }
                }
            }
        }

        return hasPotion;
    }

    @Override
    public @NotNull ItemStack assemble(SimpleContainerRecipeInput recipeInput, HolderLookup.Provider registries) {
        SimpleContainer pInv = recipeInput.container();
        ItemStack potion = ItemStack.EMPTY;
        ItemStack knife = ItemStack.EMPTY;
        for (int j = 0; j < 4; ++j) {
            ItemStack itemstack = pInv.getItem(j);
            if (PillagersKnifeItem.hasPotionCoating(itemstack)) {
                potion = itemstack;
            }
        }
        if (pInv.getItem(4).is(ImmortalersDelightItems.PILLAGER_KNIFE.get())) {
            knife = pInv.getItem(4);
        }
        if (potion.isEmpty() || knife.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            ItemStack itemstack1 = knife.copy();
            PotionContents src = potion.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
            itemstack1.set(DataComponents.POTION_CONTENTS, src);
            int maxCount = src.customEffects().isEmpty() ? 8 : 3;
            CompoundTag tag = itemstack1.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
            tag.putInt(PillagersKnifeItem.MAX_POTION_COUNT, maxCount);
            itemstack1.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
            return itemstack1;
        }
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    public static class Type implements RecipeType<PillagerKnifeAddPotionRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "pillagers_knife_add_potion";
    }

    public static class Serializer implements RecipeSerializer<PillagerKnifeAddPotionRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "pillagers_knife_add_potion");

        public static final MapCodec<PillagerKnifeAddPotionRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.CODEC.listOf(1, 9).fieldOf("ingredients").forGetter(r -> List.copyOf(r.getIngredients())),
                ItemStack.STRICT_CODEC.fieldOf("output").forGetter(r -> r.output),
                ItemStack.STRICT_CODEC.optionalFieldOf("container", ItemStack.EMPTY).forGetter(r -> r.container)
        ).apply(instance, PillagerKnifeAddPotionRecipe::fromCodec));

        private static final StreamCodec<RegistryFriendlyByteBuf, PillagerKnifeAddPotionRecipe> STREAM_CODEC =
                StreamCodec.of(Serializer::writeNetwork, Serializer::readNetwork);

        private static void writeNetwork(RegistryFriendlyByteBuf buf, PillagerKnifeAddPotionRecipe recipe) {
            buf.writeVarInt(recipe.getIngredients().size());
            for (Ingredient ingredient : recipe.getIngredients()) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buf, ingredient);
            }
            ItemStack.STREAM_CODEC.encode(buf, new ItemStack(ImmortalersDelightItems.PILLAGER_KNIFE.get()));
            ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, recipe.container);
        }

        private static PillagerKnifeAddPotionRecipe readNetwork(RegistryFriendlyByteBuf buf) {
            int size = buf.readVarInt();
            NonNullList<Ingredient> inputs = NonNullList.withSize(size, Ingredient.EMPTY);
            for (int i = 0; i < size; i++) {
                inputs.set(i, Ingredient.CONTENTS_STREAM_CODEC.decode(buf));
            }
            ItemStack ignoredOutput = ItemStack.STREAM_CODEC.decode(buf);
            ItemStack container = ItemStack.OPTIONAL_STREAM_CODEC.decode(buf);
            return new PillagerKnifeAddPotionRecipe(inputs, ignoredOutput, container);
        }

        @Override
        public MapCodec<PillagerKnifeAddPotionRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, PillagerKnifeAddPotionRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
