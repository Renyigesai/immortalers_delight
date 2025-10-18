package com.renyigesai.immortalers_delight.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PillagerKnifeAddPotionRecipe extends CustomRecipe {
    public PillagerKnifeAddPotionRecipe(ResourceLocation pId, CraftingBookCategory pCategory) {
        super(pId, pCategory);
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(CraftingContainer pInv, Level pLevel) {
        boolean hasPotion = false;
        boolean hasKnife = false;
        for(int i = 0; i < pInv.getWidth(); ++i) {
            for(int j = 0; j < pInv.getHeight(); ++j) {
                ItemStack itemstack = pInv.getItem(i + j * pInv.getWidth());
                if (itemstack.is(Items.POTION)) {
                    if (hasPotion) {
                        return false;
                    } else {
                        hasPotion = true;
                    }
                }
                if (itemstack.is(ImmortalersDelightItems.PILLAGER_KNIFE.get())) {
                    if (hasKnife) {
                        return false;
                    } else {
                        hasKnife = true;
                    }
                }
            }
        }
        System.out.println("hasPotion:"+hasPotion+" hasKnife:"+hasKnife);
        return hasPotion && hasKnife;
    }

    public @NotNull ItemStack assemble(CraftingContainer pInv, @NotNull RegistryAccess pRegistryAccess) {
        ItemStack potion = ItemStack.EMPTY;
        ItemStack knife = ItemStack.EMPTY;
        for(int i = 0; i < pInv.getWidth(); ++i) {
            for(int j = 0; j < pInv.getHeight(); ++j) {
                ItemStack itemstack = pInv.getItem(i + j * pInv.getWidth());
                if (itemstack.is(Items.POTION)) {
                    potion = itemstack;
                }
                if (itemstack.is(ImmortalersDelightItems.PILLAGER_KNIFE.get())) {
                    knife = itemstack;
                }
            }
        }
        System.out.println("potion_stack:"+potion+" knife_stack:"+knife);
        if (potion.isEmpty() || knife.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            ItemStack itemstack1 = knife.copy();
            PotionUtils.setPotion(itemstack1, PotionUtils.getPotion(potion));
            PotionUtils.setCustomEffects(itemstack1, PotionUtils.getCustomEffects(potion));
            return itemstack1;
        }
    }

    /**
     * Used to determine if this recipe can fit in a grid of the given width/height
     */
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return pWidth >= 2 && pHeight >= 2;
    }

    @Override
    public @NotNull RecipeType<?> getType() {return Type.INSTANCE;}
    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeSerializer.TIPPED_ARROW;
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
            return new PillagerKnifeAddPotionRecipe(pRecipeId, CraftingBookCategory.MISC);
        }

        @Override
        public @Nullable PillagerKnifeAddPotionRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            return new PillagerKnifeAddPotionRecipe(pRecipeId, CraftingBookCategory.MISC);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, PillagerKnifeAddPotionRecipe pRecipe) {

        }
    }
}
