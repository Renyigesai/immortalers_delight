package com.renyigesai.immortalers_delight.recipe;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = ImmortalersDelightMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ImmortalersDelightRecipeTypes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ImmortalersDelightMod.MODID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPE = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, ImmortalersDelightMod.MODID);

    public static final DeferredRegister<RecipeSerializer<?>> KNIFE_POTION_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ImmortalersDelightMod.MODID);
    public static final DeferredRegister<RecipeType<?>> KNIFE_POTION_RECIPE_TYPE = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, ImmortalersDelightMod.MODID);

    @SuppressWarnings("removal")
    @SubscribeEvent
    public static void register(FMLConstructModEvent event) {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        event.enqueueWork(() -> {
            SERIALIZERS.register(bus);
            RECIPE_TYPE.register(bus);
			SERIALIZERS.register(EnchantalCoolerRecipe.Type.ID,() ->EnchantalCoolerRecipe.Serializer.INSTANCE);
			RECIPE_TYPE.register(EnchantalCoolerRecipe.Type.ID,() ->EnchantalCoolerRecipe.Type.INSTANCE);
            SERIALIZERS.register(HotSpringRecipe.Type.ID,() ->HotSpringRecipe.Serializer.INSTANCE);
            RECIPE_TYPE.register(HotSpringRecipe.Type.ID,() ->HotSpringRecipe.Type.INSTANCE);
            KNIFE_POTION_SERIALIZERS.register(bus);
            KNIFE_POTION_RECIPE_TYPE.register(bus);
            KNIFE_POTION_SERIALIZERS.register(PillagerKnifeAddPotionRecipe.Type.ID,() ->PillagerKnifeAddPotionRecipe.Serializer.INSTANCE);
            KNIFE_POTION_RECIPE_TYPE.register(PillagerKnifeAddPotionRecipe.Type.ID,() ->PillagerKnifeAddPotionRecipe.Type.INSTANCE);
        });
    }
}
