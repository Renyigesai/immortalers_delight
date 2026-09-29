package com.renyigesai.immortalers_delight.client.renderer.entity.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.client.model.projectile.BladeEnergyModel;
import com.renyigesai.immortalers_delight.entities.projectile.BladeEnergy;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class BladeEnergyRender extends EntityRenderer<BladeEnergy> {
    private final BladeEnergyModel<?> model;
    public static final ResourceLocation TEXTURE = new ResourceLocation(ImmortalersDelightMod.MODID,"textures/entity/projectile/blade_energy.png");
    public BladeEnergyRender(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new BladeEnergyModel<>(pContext.bakeLayer(BladeEnergyModel.BLADE_ENERGY));
    }

    @Override
    public void render(BladeEnergy pEntity, float entityYaw, float pPartialTick,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        float tickDelta = 1.0f / 20f;
        float smoothAge = pEntity.getAge() + pPartialTick * tickDelta;
        float offset = smoothAge * 10f;  // 10 格

        poseStack.pushPose();

        // 1. 旋转到实体朝向
        poseStack.mulPose(Axis.YP.rotationDegrees(entityYaw));

        // 2. 沿实体前方移动（局部 -Z 是世界中的"前方"）
        poseStack.translate(0, 0, -offset);

        // 3. 居中模型并缩放
        poseStack.translate(0.5, 0.5, 0.5);
        poseStack.scale(2, 2, 2);
        poseStack.translate(-0.5, -0.5, -0.5);

        VertexConsumer consumer = buffer.getBuffer(RenderType.eyes(TEXTURE));
        this.model.renderToBuffer(poseStack, consumer, packedLight,
                OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(BladeEnergy bladeEnergy) {
        return TEXTURE;
    }
}
