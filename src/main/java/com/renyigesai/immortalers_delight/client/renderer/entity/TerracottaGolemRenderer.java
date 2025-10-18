package com.renyigesai.immortalers_delight.client.renderer.entity;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.client.model.TerracottaGolemModel;
import com.renyigesai.immortalers_delight.client.model_layers.TerracottaGolemSideLayer;
import com.renyigesai.immortalers_delight.entities.living.TerracottaGolem;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class TerracottaGolemRenderer extends MobRenderer<TerracottaGolem, TerracottaGolemModel<TerracottaGolem>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ImmortalersDelightMod.MODID, "textures/entity/terracotta_golem/terracotta_golem.png");

    public TerracottaGolemRenderer(EntityRendererProvider.Context pContext) {
        super(pContext,
                new TerracottaGolemModel<>(pContext.bakeLayer(TerracottaGolemModel.TERRACOTTA_GOLEM)),
                0.6F);
        this.addLayer(new TerracottaGolemSideLayer<>(this, pContext.getModelSet()));
    }

    @Override
    public ResourceLocation getTextureLocation(TerracottaGolem pEntity) {
        return TEXTURE;
    }
}
