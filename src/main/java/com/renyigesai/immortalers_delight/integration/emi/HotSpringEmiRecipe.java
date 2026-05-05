package com.renyigesai.immortalers_delight.integration.emi;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.recipe.HotSpringRecipe;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class HotSpringEmiRecipe extends BasicEmiRecipe {

    public static final ResourceLocation BACKGROUND =
            ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "textures/gui/hot_spring_jei.png");

    public HotSpringEmiRecipe(HotSpringRecipe recipe, ResourceLocation id) {
        super(EMIImmortalersDelightPlugin.HOT_SPRING_CATEGORY, id, 186, 119);
        List<Ingredient> ingredients = recipe.getIngredients();
        for (Ingredient ingredient : ingredients) {
            this.inputs.add(EmiIngredient.of(ingredient));
        }
        var access = Minecraft.getInstance().level.registryAccess();
        this.outputs.add(EmiStack.of(recipe.getResultItem(access)));
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(new EmiTexture(BACKGROUND, 0, 0, 186, 119), 0, 0);
        int x0 = 25;
        int y0 = 39;
        int step = 18;
        if (!inputs.isEmpty()) {
            widgets.addSlot(inputs.get(0), x0 + 19, y0 - 8);
        }
        int inputIndex = 1;
        outer:
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (inputIndex >= inputs.size()) {
                    break outer;
                }
                int yOff = col == 1 ? 10 : 1;
                widgets.addSlot(inputs.get(inputIndex), x0 + col * step + 1, y0 + row * step + yOff);
                inputIndex++;
            }
        }
        widgets.addSlot(outputs.get(0), 144, 57).recipeContext(this);
    }
}
