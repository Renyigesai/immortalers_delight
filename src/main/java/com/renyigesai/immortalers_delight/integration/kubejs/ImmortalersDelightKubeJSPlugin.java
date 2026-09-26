package com.renyigesai.immortalers_delight.integration.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RegisterRecipeSchemasEvent;

public final class ImmortalersDelightKubeJSPlugin extends KubeJSPlugin {
    @Override
    public void registerRecipeSchemas(RegisterRecipeSchemasEvent event) {
        EnchantalCoolerKubeJSSchema.register(event);
        HotSpringKubeJSSchema.register(event);
    }
}
