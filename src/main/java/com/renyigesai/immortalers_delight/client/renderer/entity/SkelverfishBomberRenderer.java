package com.renyigesai.immortalers_delight.client.renderer.entity;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.client.model.SkelverfishBomberModel;
import com.renyigesai.immortalers_delight.client.model_layers.SkelverfishBomberCoreLayer;
import com.renyigesai.immortalers_delight.entities.living.SkelverfishBomber;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SkelverfishBomberRenderer extends MobRenderer<SkelverfishBomber, SkelverfishBomberModel<SkelverfishBomber>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ImmortalersDelightMod.MODID, "textures/entity/skelverfish_bomber.png");
    public SkelverfishBomberRenderer(EntityRendererProvider.Context pContext) {
        super(pContext,
                new SkelverfishBomberModel<>(pContext.bakeLayer(SkelverfishBomberModel.SKELVERFISH_BOMBER)),
                0.3F);
        this.addLayer(new SkelverfishBomberCoreLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(SkelverfishBomber pEntity) {
        return TEXTURE;
    }
}
