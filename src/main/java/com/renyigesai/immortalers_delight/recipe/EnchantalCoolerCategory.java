package com.renyigesai.immortalers_delight.recipe;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class EnchantalCoolerCategory implements IRecipeCategory<EnchantalCoolerRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(ImmortalersDelightMod.MODID, "enchantal_cooler");
    public static final ResourceLocation TEXTURE =
            new ResourceLocation(ImmortalersDelightMod.MODID, "textures/gui/enchantal_cooler_jei.png");

    public final IDrawable back;
    public final IDrawable icon;

    public EnchantalCoolerCategory(IGuiHelper helper) {
        this.back = helper.createDrawable(TEXTURE,0, 0, 138, 86);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK,new ItemStack(ImmortalersDelightItems.ENCHANTAL_COOLER.get()));
    }

    @Override
    public RecipeType<EnchantalCoolerRecipe> getRecipeType() {
        return JEIImmortalersDelightPlugin.ENCHANTAL_COOLER_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("container.enchantal_cooler");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return this.icon;
    }

    @SuppressWarnings("removal")
    @Override
    public IDrawable getBackground() {
        return this.back;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, EnchantalCoolerRecipe recipe, IFocusGroup iFocusGroup) {
        NonNullList<Ingredient> recipeIngredients = recipe.getIngredients();
        int borderSlotSize = 18;
        //x和y轴的初始坐标，取值为gui贴图的x,y初始位置减一
        int x = 13;
        int y = 16;
        //添加原料槽
        for (int row = 0; row < 2; ++row) {
            for (int column = 0; column < 2; ++column) {
                int inputIndex = row * 2 + column;
                if (inputIndex < recipeIngredients.size()) {
                    builder.addSlot(RecipeIngredientRole.INPUT, x + (column * borderSlotSize) + 1, y + (row * borderSlotSize) + 1)
                            .addItemStacks(Arrays.asList(recipeIngredients.get(inputIndex).getItems()));
                }
            }
        }
        //添加容器槽
        builder.addSlot(RecipeIngredientRole.INPUT,99,55).addItemStack(recipe.getContainer());
        //添加输出槽
        builder.addSlot(RecipeIngredientRole.OUTPUT,99,25).addItemStack(recipe.getResultItem(null));
    }
}
