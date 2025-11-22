package com.renyigesai.immortalers_delight.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.item.PillagersKnifeItem;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PillagerKnifeAddPotionRecipe extends EnchantalCoolerRecipe {
    public PillagerKnifeAddPotionRecipe(NonNullList<Ingredient> ingredient, ItemStack output,ItemStack container, ResourceLocation id) {
        super(ingredient, new ItemStack(ImmortalersDelightItems.PILLAGER_KNIFE.get()), new ItemStack(ImmortalersDelightItems.PILLAGER_KNIFE.get()), id);
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(SimpleContainer inv, Level pLevel) {
        boolean hasPotion = false;

        if (this.getContainer().isEmpty() || this.getContainer().is(inv.getItem(4).getItem())) {
            for (int j = 0; j < 4; ++j) {
                ItemStack itemstack = inv.getItem(j);
                if (PotionUtils.getPotion(itemstack) != Potions.EMPTY || PotionUtils.getCustomEffects(itemstack).size() > 0) {
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

    public @NotNull ItemStack assemble(SimpleContainer pInv, RegistryAccess pRegistryAccess) {
        ItemStack potion = ItemStack.EMPTY;
        ItemStack knife = ItemStack.EMPTY;
        for (int j = 0; j < 4; ++j) {
            ItemStack itemstack = pInv.getItem(j);
            if (PotionUtils.getPotion(itemstack) != Potions.EMPTY || PotionUtils.getCustomEffects(itemstack).size() > 0) {
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
            PotionUtils.setPotion(itemstack1, PotionUtils.getPotion(potion));
            PotionUtils.setCustomEffects(itemstack1, PotionUtils.getCustomEffects(potion));
            if (PotionUtils.getCustomEffects(potion).isEmpty()) {
                itemstack1.getOrCreateTag().putInt(PillagersKnifeItem.MAX_POTION_COUNT, 8);
            } else itemstack1.getOrCreateTag().putInt(PillagersKnifeItem.MAX_POTION_COUNT, 3);
            return itemstack1;
        }
    }

    /**
     * Used to determine if this recipe can fit in a grid of the given width/height
     */
//    public boolean canCraftInDimensions(int pWidth, int pHeight) {
//        return pWidth >= 2 && pHeight >= 2;
//    }

    @Override
    public @NotNull RecipeType<?> getType() {return Type.INSTANCE;}
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
        public static final ResourceLocation ID = new ResourceLocation(ImmortalersDelightMod.MODID, "pillagers_knife_add_potion");

        @Override
        public PillagerKnifeAddPotionRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));

            // 动态获取原料数量
            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.create();

            for (int i = 0; i < ingredients.size(); i++) {
                inputs.add(Ingredient.fromJson(ingredients.get(i)));
            }
            ItemStack container = GsonHelper.isValidNode(pSerializedRecipe, "container") ? CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(pSerializedRecipe, "container"), true) : ItemStack.EMPTY;

            return new PillagerKnifeAddPotionRecipe(inputs, output,container,pRecipeId);
        }

        @Override
        public @Nullable PillagerKnifeAddPotionRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            int ingredientCount = pBuffer.readInt();
            NonNullList<Ingredient> inputs = NonNullList.withSize(ingredientCount, Ingredient.EMPTY);

            for (int i = 0; i < ingredientCount; i++) {
                inputs.set(i, Ingredient.fromNetwork(pBuffer));
            }
            ItemStack container = pBuffer.readItem();
            ItemStack output = pBuffer.readItem();
            return new PillagerKnifeAddPotionRecipe(inputs, output,container,pRecipeId);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, PillagerKnifeAddPotionRecipe pRecipe) {
            pBuffer.writeInt(pRecipe.getIngredients().size());

            for (Ingredient ingredient : pRecipe.getIngredients()) {
                ingredient.toNetwork(pBuffer);
            }
            pBuffer.writeItemStack(pRecipe.getResultItem(null), false);
            pBuffer.writeItem(pRecipe.getContainer());
        }
    }
}
