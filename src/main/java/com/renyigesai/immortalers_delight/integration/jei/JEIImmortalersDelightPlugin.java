package com.renyigesai.immortalers_delight.integration.jei;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.integration.jei.category.EnchantalCoolerCategory;
import com.renyigesai.immortalers_delight.integration.jei.category.HotSpringCategory;
import com.renyigesai.immortalers_delight.integration.jei.category.SoulInfuserCategory;
import com.renyigesai.immortalers_delight.recipe.EnchantalCoolerRecipe;
import com.renyigesai.immortalers_delight.recipe.HotSpringRecipe;
import com.renyigesai.immortalers_delight.recipe.SoulInfuserRecipe;
import com.renyigesai.immortalers_delight.screen.EnchantalCoolerScreen;
import com.renyigesai.immortalers_delight.screen.SoulInfuserScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
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
    public static final mezz.jei.api.recipe.RecipeType<HotSpringRecipe> HOT_SPRING_TYPE = new mezz.jei.api.recipe.RecipeType<>(HotSpringCategory.UID, HotSpringRecipe.class);
    public static final mezz.jei.api.recipe.RecipeType<SoulInfuserRecipe> SOUL_INFUSING_TYPE = new mezz.jei.api.recipe.RecipeType<>(SoulInfuserCategory.UID, SoulInfuserRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(ImmortalersDelightMod.MODID,"jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new EnchantalCoolerCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new HotSpringCategory(registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(
                new SoulInfuserCategory(registration.getJeiHelpers().getGuiHelper())
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
        List<EnchantalCoolerRecipe> enchantalCoolerRecipes = recipeManager.getAllRecipesFor(EnchantalCoolerRecipe.Type.INSTANCE);
        registration.addRecipes(ENCHANTAL_COOLER_TYPE,enchantalCoolerRecipes);

        List<HotSpringRecipe> hotSpringRecipes = recipeManager.getAllRecipesFor(HotSpringRecipe.Type.INSTANCE);
        registration.addRecipes(HOT_SPRING_TYPE,hotSpringRecipes);

        // 从 RecipeManager 动态读取所有注册的 SoulInfuser 配方
        List<SoulInfuserRecipe> recipes = recipeManager.getAllRecipesFor(SoulInfuserRecipe.Type.INSTANCE);
        registration.addRecipes(SOUL_INFUSING_TYPE, recipes);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(EnchantalCoolerScreen.class,110,16,8,54,
                ENCHANTAL_COOLER_TYPE);

        // 注册 GUI 点击区域（点击箭头直接打开 JEI 配方总览）
        registration.addRecipeClickArea(SoulInfuserScreen.class, 98, 31, 26, 35, SOUL_INFUSING_TYPE);
    }
    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ImmortalersDelightBlocks.ENCHANTAL_COOLER.get()), ENCHANTAL_COOLER_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ImmortalersDelightItems.HOT_SPRING_BUCKET.get()), HOT_SPRING_TYPE);

        // 方块作为工作台 Catalyst，允许按 R / U 查看该机器的配方
        registration.addRecipeCatalyst(new ItemStack(ImmortalersDelightBlocks.SOUL_INFUSER.get()), SOUL_INFUSING_TYPE);
    }

}
