package com.renyigesai.immortalers_delight.client.renderer.special_item;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.ModelEvent;

import java.util.Map;

public final class FlatItemIconModels {
    public static final ModelResourceLocation LARGE_COLUMN_ICON = ModelResourceLocation.standalone(
            ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "item/large_column_gui"));
    public static final ModelResourceLocation JENG_NANU_ICON = ModelResourceLocation.standalone(
            ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "item/jeng_nanu_gui"));

    private static BakedModel largeColumnIcon;
    private static BakedModel jengNanuIcon;

    private FlatItemIconModels() {
    }

    public static void onRegisterAdditionalModels(ModelEvent.RegisterAdditional event) {
        event.register(LARGE_COLUMN_ICON);
        event.register(JENG_NANU_ICON);
    }

    public static void onModelBake(ModelEvent.ModifyBakingResult event) {
        Map<ModelResourceLocation, BakedModel> models = event.getModels();
        largeColumnIcon = models.get(LARGE_COLUMN_ICON);
        jengNanuIcon = models.get(JENG_NANU_ICON);
    }

    public static BakedModel getIcon(ItemStack stack) {
        Item item = stack.getItem();
        if (item == ImmortalersDelightItems.LARGE_COLUMN.get()) {
            return resolveRuntimeModel(largeColumnIcon, LARGE_COLUMN_ICON);
        }
        if (item == ImmortalersDelightItems.JENG_NANU.get()) {
            return resolveRuntimeModel(jengNanuIcon, JENG_NANU_ICON);
        }
        return Minecraft.getInstance().getModelManager().getMissingModel();
    }

    private static BakedModel resolveRuntimeModel(BakedModel cached, ModelResourceLocation location) {
        if (cached != null) {
            return cached;
        }
        return Minecraft.getInstance().getModelManager().getModel(location);
    }
}
