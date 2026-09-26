package com.renyigesai.immortalers_delight.integration.kubejs;

import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.recipe.schema.RegisterRecipeSchemasEvent;

public final class EnchantalCoolerKubeJSSchema {
    private EnchantalCoolerKubeJSSchema() {
    }

    public static void register(RegisterRecipeSchemasEvent event) {
        event.namespace("immortalers_delight").register("enchantal_cooler", new RecipeSchema(
                ItemComponents.OUTPUT.key("output"),
                ItemComponents.INPUT_ARRAY.key("ingredients"),
                ItemComponents.OUTPUT.key("container").defaultOptional()
        ));
    }
}
