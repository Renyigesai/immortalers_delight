package com.renyigesai.immortalers_delight.client.renderer.entity.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.client.model.AncientMechanismDynamiteModel;
import com.renyigesai.immortalers_delight.entities.projectile.AncientMechanismDynamiteEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class AncientMechanismDynamiteRenderer extends EntityRenderer<AncientMechanismDynamiteEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ImmortalersDelightMod.MODID, "textures/entity/ancient_mechanism_dynamite.png");
    private final AncientMechanismDynamiteModel<AncientMechanismDynamiteEntity> model;

    public AncientMechanismDynamiteRenderer(EntityRendererProvider.Context context) {
        super(context);
        model = new AncientMechanismDynamiteModel<>(context.bakeLayer(AncientMechanismDynamiteModel.LAYER));
    }

    @Override
    public void render(AncientMechanismDynamiteEntity entity, float yaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int light) {
        poseStack.pushPose();
        poseStack.translate(0, 0.35F, 0);
        poseStack.scale(-0.5F, -0.5F, 0.5F);
        VertexConsumer vertices = buffer.getBuffer(model.renderType(TEXTURE));
        model.setupAnim(entity, 0, 0, entity.tickCount + partialTick, 0, 0);
        model.renderToBuffer(poseStack, vertices, light, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        poseStack.popPose();
        super.render(entity, yaw, partialTick, poseStack, buffer, light);
    }

    @Override
    public ResourceLocation getTextureLocation(AncientMechanismDynamiteEntity entity) { return TEXTURE; }
}
