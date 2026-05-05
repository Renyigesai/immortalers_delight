package com.renyigesai.immortalers_delight.integration.emi;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.recipe.EnchantalCoolerRecipe;
import com.renyigesai.immortalers_delight.recipe.HotSpringRecipe;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.Objects;

@EmiEntrypoint
public class EMIImmortalersDelightPlugin implements EmiPlugin {

    private static final EmiStack ENCHANTAL_COOLER_STACK = EmiStack.of(ImmortalersDelightBlocks.ENCHANTAL_COOLER.get());
    private static final EmiStack HOT_SPRING_STACK = EmiStack.of(ImmortalersDelightItems.HOT_SPRING_BUCKET.get());

    public static final EmiRecipeCategory ENCHANTAL_COOLER_CATEGORY = new EmiRecipeCategory(
            ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "enchantal_cooler"),
            ENCHANTAL_COOLER_STACK,
            ENCHANTAL_COOLER_STACK
    );

    public static final EmiRecipeCategory HOT_SPRING_CATEGORY = new EmiRecipeCategory(
            ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "hot_spring"),
            HOT_SPRING_STACK,
            HOT_SPRING_STACK
    );

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(ENCHANTAL_COOLER_CATEGORY);
        registry.addWorkstation(ENCHANTAL_COOLER_CATEGORY, EmiStack.of(ImmortalersDelightBlocks.ENCHANTAL_COOLER.get()));

        registry.addCategory(HOT_SPRING_CATEGORY);
        registry.addWorkstation(HOT_SPRING_CATEGORY, EmiStack.of(ImmortalersDelightItems.HOT_SPRING_BUCKET.get()));

        var manager = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
        for (RecipeHolder<EnchantalCoolerRecipe> holder : manager.getAllRecipesFor(EnchantalCoolerRecipe.Type.INSTANCE)) {
            registry.addRecipe(new EnchantalCoolerEmiRecipe(holder.value(), holder.id()));
        }
        for (RecipeHolder<HotSpringRecipe> holder : manager.getAllRecipesFor(HotSpringRecipe.Type.INSTANCE)) {
            registry.addRecipe(new HotSpringEmiRecipe(holder.value(), holder.id()));
        }
    }
}
