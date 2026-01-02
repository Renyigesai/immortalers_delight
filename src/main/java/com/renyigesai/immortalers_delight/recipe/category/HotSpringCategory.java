package com.renyigesai.immortalers_delight.recipe.category;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.recipe.EnchantalCoolerRecipe;
import com.renyigesai.immortalers_delight.recipe.HotSpringRecipe;
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

public class HotSpringCategory implements IRecipeCategory<HotSpringRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(ImmortalersDelightMod.MODID, "hot_spring");
    public static final ResourceLocation TEXTURE =
            new ResourceLocation(ImmortalersDelightMod.MODID, "textures/gui/hot_spring_jei.png");

    public final IDrawable back;
    public final IDrawable icon;

    public HotSpringCategory(IGuiHelper helper) {
        this.back = helper.createDrawable(TEXTURE,0, 0, 186, 119);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK,new ItemStack(ImmortalersDelightItems.HOT_SPRING_BUCKET.get()));
    }

    @Override
    public RecipeType<HotSpringRecipe> getRecipeType() {
        return JEIImmortalersDelightPlugin.HOT_SPRING_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("container.immortalers_delight.hot_spring");
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
    public void setRecipe(IRecipeLayoutBuilder builder, HotSpringRecipe recipe, IFocusGroup iFocusGroup) {
        NonNullList<Ingredient> recipeIngredients = recipe.getIngredients();
        int borderSlotSize = 18;
        //x和y轴的初始坐标，取值为gui贴图的x,y初始位置减一
        int x = 25;
        int y = 39;
        //添加原料槽
        for (int row = 0; row < 3; ++row) {
            for (int column = 0; column < 3; ++column) {
                int inputIndex = row * 3 + column;
                if (inputIndex < recipeIngredients.size()) {
                    builder.addSlot(RecipeIngredientRole.INPUT, x + (column * borderSlotSize) + 1, y + (row * borderSlotSize) + 1)
                            .addItemStacks(Arrays.asList(recipeIngredients.get(inputIndex).getItems()));
                }
            }
        }
        //添加输出槽
        builder.addSlot(RecipeIngredientRole.OUTPUT,144,57).addItemStack(recipe.getResultItem(null));
    }
}
