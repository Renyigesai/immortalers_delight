package com.renyigesai.immortalers_delight.integration.emi;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.recipe.EnchantalCoolerRecipe;
import com.renyigesai.immortalers_delight.recipe.HotSpringRecipe;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

final class EmiIDRecipes {
    private static final ResourceLocation ENCHANTAL_COOLER_TEXTURE =
            new ResourceLocation(ImmortalersDelightMod.MODID, "textures/gui/enchantal_cooler_jei.png");
    private static final ResourceLocation HOT_SPRING_TEXTURE =
            new ResourceLocation(ImmortalersDelightMod.MODID, "textures/gui/hot_spring_jei.png");

    static final EmiRecipeCategory ENCHANTAL_COOLER = category("enchantal_cooler", new ItemStack(ImmortalersDelightItems.ENCHANTAL_COOLER.get()));
    static final EmiRecipeCategory HOT_SPRING = category("hot_spring", new ItemStack(ImmortalersDelightItems.HOT_SPRING_BUCKET.get()));

    private EmiIDRecipes() {
    }

    static BasicEmiRecipe enchantalCooler(EnchantalCoolerRecipe recipe) {
        IngredientRecipe emiRecipe = new EnchantalCoolerEmiRecipe(recipe.getId());
        recipe.getIngredients().forEach(ingredient -> emiRecipe.addInput(EmiIngredient.of(ingredient)));
        emiRecipe.addInput(recipe.getContainer());
        emiRecipe.addOutput(EmiStack.of(recipe.getResultItem(null)));
        return emiRecipe;
    }

    static BasicEmiRecipe hotSpring(HotSpringRecipe recipe) {
        IngredientRecipe emiRecipe = new HotSpringEmiRecipe(recipe.getId());
        recipe.getIngredients().forEach(ingredient -> emiRecipe.addInput(EmiIngredient.of(ingredient)));
        emiRecipe.addOutput(EmiStack.of(recipe.getResultItem(null)));
        return emiRecipe;
    }

    private static EmiRecipeCategory category(String path, ItemStack icon) {
        return new EmiRecipeCategory(new ResourceLocation(ImmortalersDelightMod.MODID, path), EmiStack.of(icon));
    }

    private static class IngredientRecipe extends BasicEmiRecipe {
        private IngredientRecipe(EmiRecipeCategory category, ResourceLocation id) {
            super(category, id, 134, 54);
        }

        void addInput(EmiIngredient ingredient) {
            inputs.add(ingredient);
        }

        void addInput(ItemStack stack) {
            if (!stack.isEmpty()) {
                inputs.add(EmiStack.of(stack));
            }
        }

        void addCatalyst(EmiIngredient ingredient) {
            catalysts.add(ingredient);
        }

        void addCatalyst(ItemStack stack) {
            if (!stack.isEmpty()) {
                catalysts.add(EmiStack.of(stack));
            }
        }

        void addOutput(EmiStack stack) {
            outputs.add(stack);
        }

        @Override
        public void addWidgets(WidgetHolder widgets) {
            addIngredientSlots(widgets, inputs);
            addOutputSlots(widgets, outputs);
        }
    }

    private static final class EnchantalCoolerEmiRecipe extends IngredientRecipe {
        private EnchantalCoolerEmiRecipe(ResourceLocation id) {
            super(ENCHANTAL_COOLER, id);
            width = 138;
            height = 86;
        }

        @Override
        public void addWidgets(WidgetHolder widgets) {
            addEnchantalCoolerBackground(widgets);
            for (int index = 0; index < inputs.size() - 1 && index < 4; index++) {
                widgets.addSlot(inputs.get(index), 14 + (index % 2) * 18, 17 + (index / 2) * 18)
                        .drawBack(false);
            }
            if (!inputs.isEmpty()) {
                widgets.addSlot(inputs.get(inputs.size() - 1), 99, 55).drawBack(false);
            }
            addOutputSlots(widgets, outputs, 99, 25);
        }
    }

    private static final class HotSpringEmiRecipe extends IngredientRecipe {
        private HotSpringEmiRecipe(ResourceLocation id) {
            super(HOT_SPRING, id);
            width = 186;
            height = 119;
        }

        @Override
        public void addWidgets(WidgetHolder widgets) {
            addHotSpringBackground(widgets);
            if (!inputs.isEmpty()) {
                widgets.addSlot(inputs.get(0), 44, 31).drawBack(false);
            }
            for (int index = 1; index < inputs.size() && index < 10; index++) {
                int gridIndex = index - 1;
                widgets.addSlot(inputs.get(index), 26 + (gridIndex % 3) * 18,
                        40 + (gridIndex / 3) * 18 + (gridIndex % 3 == 1 ? 10 : 0)).drawBack(false);
            }
            addOutputSlots(widgets, outputs, 144, 57);
        }
    }

    private static void addIngredientSlots(WidgetHolder widgets, java.util.List<EmiIngredient> ingredients) {
        for (int index = 0; index < ingredients.size() && index < 9; index++) {
            widgets.addSlot(ingredients.get(index), (index % 5) * 18, (index / 5) * 18);
        }
    }

    private static void addEnchantalCoolerBackground(WidgetHolder widgets) {
        widgets.addTexture(ENCHANTAL_COOLER_TEXTURE, 0, 0, 138, 86, 0, 0);
    }

    private static void addHotSpringBackground(WidgetHolder widgets) {
        widgets.addTexture(HOT_SPRING_TEXTURE, 0, 0, 186, 119, 0, 0);
    }

    private static void addOutputSlots(WidgetHolder widgets, java.util.List<EmiStack> outputs) {
        addOutputSlots(widgets, outputs, 116, 0);
    }

    private static void addOutputSlots(WidgetHolder widgets, java.util.List<EmiStack> outputs, int x, int y) {
        for (int index = 0; index < outputs.size() && index < 2; index++) {
            widgets.addSlot(outputs.get(index), x, y + index * 18).drawBack(false);
        }
    }
}
