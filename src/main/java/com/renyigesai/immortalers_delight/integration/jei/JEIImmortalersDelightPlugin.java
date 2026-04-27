package com.renyigesai.immortalers_delight.integration.jei;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.integration.jei.category.EnchantalCoolerCategory;
import com.renyigesai.immortalers_delight.integration.jei.category.HotSpringCategory;
import com.renyigesai.immortalers_delight.recipe.EnchantalCoolerRecipe;
import com.renyigesai.immortalers_delight.recipe.HotSpringRecipe;
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
import net.minecraft.world.level.Level;

import java.util.List;

@JeiPlugin
public class JEIImmortalersDelightPlugin implements IModPlugin {
    public static final mezz.jei.api.recipe.RecipeType<EnchantalCoolerRecipe> ENCHANTAL_COOLER_TYPE = new mezz.jei.api.recipe.RecipeType<>(EnchantalCoolerCategory.UID, EnchantalCoolerRecipe.class);
    public static final mezz.jei.api.recipe.RecipeType<HotSpringRecipe> HOT_SPRING_TYPE = new mezz.jei.api.recipe.RecipeType<>(HotSpringCategory.UID, HotSpringRecipe.class);
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(ImmortalersDelightMod.MODID,"jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new EnchantalCoolerCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new HotSpringCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        Level level = Minecraft.getInstance().level;
        if (level == null) {
            ImmortalersDelightMod.LOGGER.warn("JEI 配方注册时客户端世界尚未初始化，跳过本次注册。");
            return;
        }

        RecipeManager recipeManager = level.getRecipeManager();
        List<EnchantalCoolerRecipe> enchantalCoolerRecipes = recipeManager.getAllRecipesFor(EnchantalCoolerRecipe.Type.INSTANCE);
        registration.addRecipes(ENCHANTAL_COOLER_TYPE,enchantalCoolerRecipes);

        List<HotSpringRecipe> hotSpringRecipes = recipeManager.getAllRecipesFor(HotSpringRecipe.Type.INSTANCE);
        registration.addRecipes(HOT_SPRING_TYPE,hotSpringRecipes);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(EnchantalCoolerScreen.class,110,16,8,54,
                ENCHANTAL_COOLER_TYPE);
    }
    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ImmortalersDelightBlocks.ENCHANTAL_COOLER.get()), ENCHANTAL_COOLER_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ImmortalersDelightItems.HOT_SPRING_BUCKET.get()), HOT_SPRING_TYPE);
    }
}
