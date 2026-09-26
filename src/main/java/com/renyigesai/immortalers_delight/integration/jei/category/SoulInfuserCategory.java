package com.renyigesai.immortalers_delight.integration.jei.category;

import com.google.common.collect.Lists;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import com.renyigesai.immortalers_delight.integration.jei.JEIImmortalersDelightPlugin;
import com.renyigesai.immortalers_delight.recipe.SoulInfuserRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class SoulInfuserCategory implements IRecipeCategory<SoulInfuserRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(ImmortalersDelightMod.MODID, "soul_infusing");
    public static final ResourceLocation TEXTURE = new ResourceLocation(ImmortalersDelightMod.MODID, "textures/gui/soul_infuser_jei.png");

    private final IDrawable background;
    private final IDrawable icon;
    private final IGuiHelper helper;

    public SoulInfuserCategory(IGuiHelper helper) {
        this.helper = helper;
        // 背景尺寸
        this.background = helper.createDrawable(TEXTURE, 0, 0, 186, 119);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ImmortalersDelightBlocks.SOUL_INFUSER.get()));
    }

    @Override
    public RecipeType<SoulInfuserRecipe> getRecipeType() {
        return JEIImmortalersDelightPlugin.SOUL_INFUSING_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.immortalers_delight.soul_infuser");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return this.icon;
    }

    @SuppressWarnings("removal")
    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SoulInfuserRecipe recipe, IFocusGroup focuses) {
        NonNullList<Ingredient> ingredients = recipe.getIngredients();
        int slotSize = 22;

        // 3x3 矩阵槽位起点
        int startX = 31;
        int startY = 19;

        // 放置至多 9 个输入槽
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 3; ++col) {
                int index = row * 3 + col;
                if (index < ingredients.size()) {
                    builder.addSlot(RecipeIngredientRole.INPUT, startX + (col * slotSize), startY + (row * slotSize))
                            .addItemStacks(Arrays.asList(ingredients.get(index).getItems()));
                }
            }
        }

        // 输出槽位 (右侧中心)
        builder.addSlot(RecipeIngredientRole.OUTPUT, 134, 41)
                .addItemStack(recipe.getResultItem(null));

        // 灵魂沙
        Collection<ItemStack> items = getItemsFromTagKey(ItemTags.SOUL_FIRE_BASE_BLOCKS);
        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 104, 89)
                .addItemStacks(new ArrayList<>(items));
    }
    public Collection<ItemStack> getItemsFromTagKey(TagKey<Item> tag) {
        List<ItemStack> list = Lists.newArrayList();

        for(Holder<Item> holder : BuiltInRegistries.ITEM.getTagOrEmpty(tag)) {
            list.add(new ItemStack(holder));
        }

        if (list.size() == 0) {
            list.add(new ItemStack(net.minecraft.world.level.block.Blocks.BARRIER));
        }
        return list;
    }
    @Override
    public void draw(SoulInfuserRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        // 动态进度条渲染（根据各配方耗时计算动画周期）
        int cookingTime = recipe.getCookingTime() > 0 ? recipe.getCookingTime() : 200;
        IDrawableAnimated arrow = helper.drawableBuilder(TEXTURE, 160, 0, 24, 17)
                .buildAnimated(cookingTime, IDrawableAnimated.StartDirection.LEFT, false);
        arrow.draw(guiGraphics, 82, 29);

        // 绘制配方耗时文字（显示为秒数）
        Font font = Minecraft.getInstance().font;
        float timeInSeconds = cookingTime / 20.0F;
        Component timeText = Component.literal(String.format("%.1fs", timeInSeconds));
        guiGraphics.drawString(font, timeText, 100, 64, 0x808080, false);
    }
}
