package com.renyigesai.immortalers_delight.recipe;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import com.renyigesai.immortalers_delight.screen.EnchantalCoolerScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;
import java.util.Objects;

@JeiPlugin
public class JEIImmortalersDelightPlugin implements IModPlugin {
    public static final mezz.jei.api.recipe.RecipeType<EnchantalCoolerRecipe> ENCHANTAL_COOLER_TYPE = new mezz.jei.api.recipe.RecipeType<>(EnchantalCoolerCategory.UID, EnchantalCoolerRecipe.class);
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(ImmortalersDelightMod.MODID,"jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new EnchantalCoolerCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
        List<EnchantalCoolerRecipe> enchantalCoolerRecipes = recipeManager.getAllRecipesFor(EnchantalCoolerRecipe.Type.INSTANCE);
        registration.addRecipes(ENCHANTAL_COOLER_TYPE,enchantalCoolerRecipes);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(EnchantalCoolerScreen.class,110,16,8,54,
                ENCHANTAL_COOLER_TYPE);
    }
    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ImmortalersDelightBlocks.ENCHANTAL_COOLER.get()), ENCHANTAL_COOLER_TYPE);
    }
}
