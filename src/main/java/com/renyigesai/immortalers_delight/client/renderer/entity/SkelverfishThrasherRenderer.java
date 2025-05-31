package com.renyigesai.immortalers_delight.client.renderer.entity;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.client.model.SkelverfishThrasherModel;
import com.renyigesai.immortalers_delight.entities.living.SkelverfishThrasher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SkelverfishThrasherRenderer extends MobRenderer<SkelverfishThrasher, SkelverfishThrasherModel<SkelverfishThrasher>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ImmortalersDelightMod.MODID, "textures/entity/skelverfish_thrasher.png");

    public SkelverfishThrasherRenderer(EntityRendererProvider.Context pContext) {
        super(pContext,
                new SkelverfishThrasherModel<>(pContext.bakeLayer(SkelverfishThrasherModel.SKELVERFISH_THRASHER)),
                0.3F);
    }

    @Override
    public ResourceLocation getTextureLocation(SkelverfishThrasher pEntity) {
        return TEXTURE;
    }
}
