package com.renyigesai.immortalers_delight.integration.emi;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.recipe.EnchantalCoolerRecipe;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class EnchantalCoolerEmiRecipe extends BasicEmiRecipe {

    public static final ResourceLocation BACKGROUND =
            ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "textures/gui/enchantal_cooler_jei.png");

    private final EmiIngredient containerSlot;
    private final boolean hasContainer;

    public EnchantalCoolerEmiRecipe(EnchantalCoolerRecipe recipe, ResourceLocation id) {
        super(EMIImmortalersDelightPlugin.ENCHANTAL_COOLER_CATEGORY, id, 138, 86);
        List<Ingredient> ingredients = recipe.getIngredients();
        for (Ingredient ingredient : ingredients) {
            this.inputs.add(EmiIngredient.of(ingredient));
        }
        if (!recipe.getContainer().isEmpty()) {
            this.hasContainer = true;
            this.containerSlot = EmiIngredient.of(Ingredient.of(recipe.getContainer()));
        } else {
            this.hasContainer = false;
            this.containerSlot = EmiIngredient.of(Ingredient.EMPTY);
        }
        var access = Minecraft.getInstance().level.registryAccess();
        this.outputs.add(EmiStack.of(recipe.getResultItem(access)));
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(new EmiTexture(BACKGROUND, 0, 0, 138, 86), 0, 0);
        int x0 = 13;
        int y0 = 16;
        int step = 18;
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 2; col++) {
                int idx = row * 2 + col;
                if (idx < inputs.size()) {
                    widgets.addSlot(inputs.get(idx), x0 + col * step + 1, y0 + row * step + 1);
                }
            }
        }
        if (hasContainer) {
            widgets.addSlot(containerSlot, 99, 55);
        }
        widgets.addSlot(outputs.get(0), 99, 25).recipeContext(this);
    }
}
