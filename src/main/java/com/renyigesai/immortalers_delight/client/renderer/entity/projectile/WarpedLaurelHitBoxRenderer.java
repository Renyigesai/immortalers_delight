package com.renyigesai.immortalers_delight.client.renderer.entity.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.client.model.projectile.EffectCloudModel;
import com.renyigesai.immortalers_delight.client.model.projectile.WarpedLaurelHitBoxModel;
import com.renyigesai.immortalers_delight.entities.projectile.EffectCloudBaseEntity;
import com.renyigesai.immortalers_delight.entities.projectile.WarpedLaurelHitBoxEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class WarpedLaurelHitBoxRenderer extends EntityRenderer<WarpedLaurelHitBoxEntity> {
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(ImmortalersDelightMod.MODID,"textures/entity/projectile/warped_laurel_hitbox.png");
    private final WarpedLaurelHitBoxModel<WarpedLaurelHitBoxEntity> model;

    public WarpedLaurelHitBoxRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new WarpedLaurelHitBoxModel<>(pContext.bakeLayer(WarpedLaurelHitBoxModel.LAYER_LOCATION));
    }

    public void render(WarpedLaurelHitBoxEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        float f = pEntity.getAnimationProgress(pPartialTicks);
        //System.out.println("实体动画进度" + f);
        if (f > 0.0F) {
            //随着实体的半径缩放模型
            float f1 = (float)pEntity.getRadius();
            if (f <= 0.9F) {
                if (pEntity.getWaitTime() >= 16) f1 *= f * f * f;
                else f1 *= f * f;
            } else if (f <= 1) f1 *= f;

            pPoseStack.pushPose();
            pPoseStack.mulPose(Axis.YP.rotationDegrees(90.0F - pEntity.getYRot()));
            pPoseStack.scale(-f1, -f1, f1);
            float f2 = 0.03125F;
            pPoseStack.translate(0.0D, -0.626D, 0.0D);
            pPoseStack.scale(0.5F, 0.5F, 0.5F);
            this.model.setupAnim(pEntity, f, 0.0F, 0.0F, pEntity.getYRot(), pEntity.getXRot());
            Minecraft minecraft = Minecraft.getInstance();
            boolean flag = minecraft.shouldEntityAppearGlowing(pEntity) && pEntity.isInvisible();
            VertexConsumer vertexconsumer; // 顶点数据消费者，用于向缓冲区写入渲染数据
            if (!pEntity.isInvisible() || flag) {
                // 根据渲染状态选择对应的渲染类型
                if (flag) {
                    // 发光隐身状态：使用轮廓渲染类型（仅绘制外层轮廓）
                    vertexconsumer = pBuffer.getBuffer(RenderType.outline(this.getTextureLocation(pEntity)));
                } else {
                    // 正常状态：使用半透明实体渲染类型（贴合史莱姆外层果冻的半透明质感）
                    vertexconsumer = pBuffer.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(pEntity)));
                }
            } else vertexconsumer = pBuffer.getBuffer(this.model.renderType(TEXTURE_LOCATION));
            this.model.renderToBuffer(pPoseStack, vertexconsumer, 15728880, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            pPoseStack.popPose();

            super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(WarpedLaurelHitBoxEntity pEntity) {
        return TEXTURE_LOCATION;
    }
}
