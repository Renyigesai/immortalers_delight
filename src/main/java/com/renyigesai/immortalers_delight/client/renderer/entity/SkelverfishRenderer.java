package com.renyigesai.immortalers_delight.client.renderer.entity;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.client.model_layers.SkelverfishAmbusherEyesLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SilverfishRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Silverfish;

public class SkelverfishRenderer extends SilverfishRenderer {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ImmortalersDelightMod.MODID, "textures/entity/skelverfish_ambusher.png");
    public SkelverfishRenderer(EntityRendererProvider.Context p_174378_) {
        super(p_174378_);
        this.shadowRadius = 0.3F;
        this.addLayer(new SkelverfishAmbusherEyesLayer<>(this));
    }

//    @Override
//    protected void scale(SkelverfishBase pLivingEntity, PoseStack pMatrixStack, float pPartialTickTime) {
//        pMatrixStack.scale(0.5F, 0.5F, 0.5F);
//    }
//
    @Override
    public ResourceLocation getTextureLocation(Silverfish pEntity) {
        return TEXTURE;
    }
}
