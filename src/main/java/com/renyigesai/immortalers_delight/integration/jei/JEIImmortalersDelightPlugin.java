package com.renyigesai.immortalers_delight.integration.jei;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.api.annotation.ItemData;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.integration.jei.category.EnchantalCoolerCategory;
import com.renyigesai.immortalers_delight.integration.jei.category.HotSpringCategory;
import com.renyigesai.immortalers_delight.recipe.EnchantalCoolerRecipe;
import com.renyigesai.immortalers_delight.recipe.HotSpringRecipe;
import com.renyigesai.immortalers_delight.screen.EnchantalCoolerScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.registries.RegistryObject;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

@JeiPlugin
public class JEIImmortalersDelightPlugin implements IModPlugin {
    public static final mezz.jei.api.recipe.RecipeType<EnchantalCoolerRecipe> ENCHANTAL_COOLER_TYPE = new mezz.jei.api.recipe.RecipeType<>(EnchantalCoolerCategory.UID, EnchantalCoolerRecipe.class);
    public static final mezz.jei.api.recipe.RecipeType<HotSpringRecipe> HOT_SPRING_TYPE = new mezz.jei.api.recipe.RecipeType<>(HotSpringCategory.UID, HotSpringRecipe.class);
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(ImmortalersDelightMod.MODID,"jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new EnchantalCoolerCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new HotSpringCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
        List<EnchantalCoolerRecipe> enchantalCoolerRecipes = recipeManager.getAllRecipesFor(EnchantalCoolerRecipe.Type.INSTANCE);
        registration.addRecipes(ENCHANTAL_COOLER_TYPE,enchantalCoolerRecipes);

        List<HotSpringRecipe> hotSpringRecipes = recipeManager.getAllRecipesFor(HotSpringRecipe.Type.INSTANCE);
        registration.addRecipes(HOT_SPRING_TYPE,hotSpringRecipes);
        registerIngredientInfos(registration);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(EnchantalCoolerScreen.class,110,16,8,54,
                ENCHANTAL_COOLER_TYPE);
    }
    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ImmortalersDelightBlocks.ENCHANTAL_COOLER.get()), ENCHANTAL_COOLER_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ImmortalersDelightItems.HOT_SPRING_BUCKET.get()), HOT_SPRING_TYPE);
    }

    private void registerIngredientInfos(IRecipeRegistration registration){
        for (Field field : ImmortalersDelightItems.class.getDeclaredFields()) {
            boolean isAnnotationPresent = field.isAnnotationPresent(ItemData.class);
            if (isAnnotationPresent){
                try {
                    Object object = field.get(null);
                    RegistryObject<Item> deferredItem = null;
                    if (object instanceof RegistryObject<?> registryObject){
                        if (Item.class.isAssignableFrom(registryObject.get().getClass())){
                            deferredItem = (RegistryObject<Item>) registryObject;
                        }
                        if (deferredItem != null){
                            ItemData annotation = field.getAnnotation(ItemData.class);
                            if (annotation != null){
                                if (!annotation.enInfo().isEmpty() && !annotation.zhInfo().isEmpty()){
                                    Item item = deferredItem.get();
                                    ResourceLocation key = BuiltInRegistries.ITEM.getKey(item);
                                    registration.addIngredientInfo(item,Component.translatable("jei.immortalers_delight." + key.getPath()));
                                }
                            }
                        }
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
