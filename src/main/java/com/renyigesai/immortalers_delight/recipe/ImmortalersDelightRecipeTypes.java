package com.renyigesai.immortalers_delight.recipe;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Custom recipe serializers/types must be registered on the mod event bus during construction.
 * DeferredRegister must not be subscribed inside {@code FMLConstructModEvent#enqueueWork}, or registration may run too late.
 */
public final class ImmortalersDelightRecipeTypes {

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, ImmortalersDelightMod.MODID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPE =
            DeferredRegister.create(Registries.RECIPE_TYPE, ImmortalersDelightMod.MODID);

    static {
        SERIALIZERS.register(EnchantalCoolerRecipe.Type.ID, () -> EnchantalCoolerRecipe.Serializer.INSTANCE);
        RECIPE_TYPE.register(EnchantalCoolerRecipe.Type.ID, () -> EnchantalCoolerRecipe.Type.INSTANCE);

        SERIALIZERS.register(HotSpringRecipe.Type.ID, () -> HotSpringRecipe.Serializer.INSTANCE);
        RECIPE_TYPE.register(HotSpringRecipe.Type.ID, () -> HotSpringRecipe.Type.INSTANCE);

        SERIALIZERS.register(PillagerKnifeAddPotionRecipe.Type.ID, () -> PillagerKnifeAddPotionRecipe.Serializer.INSTANCE);
        RECIPE_TYPE.register(PillagerKnifeAddPotionRecipe.Type.ID, () -> PillagerKnifeAddPotionRecipe.Type.INSTANCE);

        SERIALIZERS.register(TangyuanRecipe.Type.ID, () -> TangyuanRecipe.Serializer.INSTANCE);
        RECIPE_TYPE.register(TangyuanRecipe.Type.ID, () -> TangyuanRecipe.Type.INSTANCE);
    }

    public static void register(IEventBus modEventBus) {
        SERIALIZERS.register(modEventBus);
        RECIPE_TYPE.register(modEventBus);
    }

    private ImmortalersDelightRecipeTypes() {
    }
}
