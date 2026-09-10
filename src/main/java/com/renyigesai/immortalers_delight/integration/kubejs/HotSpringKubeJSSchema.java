package com.renyigesai.immortalers_delight.integration.kubejs;

import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.recipe.schema.RegisterRecipeSchemasEvent;

public final class HotSpringKubeJSSchema {
    private HotSpringKubeJSSchema() {
    }

    public static void register(RegisterRecipeSchemasEvent event) {
        event.namespace("immortalers_delight").register("hot_spring", new RecipeSchema(
                ItemComponents.OUTPUT.key("output"),
                ItemComponents.INPUT_ARRAY.key("ingredients")
        ));
    }
}
