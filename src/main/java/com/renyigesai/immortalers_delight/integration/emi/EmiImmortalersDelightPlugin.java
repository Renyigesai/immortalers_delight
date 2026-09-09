package com.renyigesai.immortalers_delight.integration.emi;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.recipe.EnchantalCoolerRecipe;
import com.renyigesai.immortalers_delight.recipe.HotSpringRecipe;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;

@EmiEntrypoint
public final class EmiImmortalersDelightPlugin implements dev.emi.emi.api.EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {
        registerCategory(registry, EmiIDRecipes.ENCHANTAL_COOLER, EmiStack.of(ImmortalersDelightBlocks.ENCHANTAL_COOLER.get()));
        registerCategory(registry, EmiIDRecipes.HOT_SPRING, EmiStack.of(ImmortalersDelightItems.HOT_SPRING_BUCKET.get()));

        registry.getRecipeManager().getAllRecipesFor(EnchantalCoolerRecipe.Type.INSTANCE)
                .forEach(recipe -> registry.addRecipe(EmiIDRecipes.enchantalCooler(recipe)));
        registry.getRecipeManager().getAllRecipesFor(HotSpringRecipe.Type.INSTANCE)
                .forEach(recipe -> registry.addRecipe(EmiIDRecipes.hotSpring(recipe)));
    }

    private void registerCategory(EmiRegistry registry, EmiRecipeCategory category, EmiStack workstation) {
        registry.addCategory(category);
        registry.addWorkstation(category, workstation);
    }
}
