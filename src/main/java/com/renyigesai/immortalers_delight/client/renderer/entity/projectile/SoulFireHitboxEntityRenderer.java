package com.renyigesai.immortalers_delight.client.renderer.entity.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.client.model.projectile.SoulFireHitboxEntityModel;
import com.renyigesai.immortalers_delight.entities.projectile.MoonArrowHitboxEntity;
import com.renyigesai.immortalers_delight.entities.projectile.SoulFireHitboxEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class SoulFireHitboxEntityRenderer extends EntityRenderer<SoulFireHitboxEntity> {
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(ImmortalersDelightMod.MODID,"textures/entity/projectile/soul_fire_hitbox.png");
    private final SoulFireHitboxEntityModel<SoulFireHitboxEntity> model;
    public SoulFireHitboxEntityRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new SoulFireHitboxEntityModel<>(pContext.bakeLayer(SoulFireHitboxEntityModel.LAYER_LOCATION));
    }

    private static int degree = 0;
    public void render(SoulFireHitboxEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        float r = pEntity.getRadius() / 2;
        if (pEntity.getAnimationProgress(pPartialTicks) >= 0.0F) {
            // 计算偏航角（水平旋转）的平滑插值
            float f = Mth.rotLerp(pPartialTicks, pEntity.yRotO, pEntity.getYRot());
            // 计算俯仰角（垂直旋转）的平滑插值
            float f1 = Mth.lerp(pPartialTicks, pEntity.xRotO, pEntity.getXRot());

            pPoseStack.pushPose();
            // 旋转模型以匹配实体的旋转
            pPoseStack.mulPose(Axis.YP.rotationDegrees(90.0F - pEntity.getYRot()));
            float f2 = 1.50F;
            pPoseStack.translate(0.0D, f2, 0.0D);
            pPoseStack.scale(-1, -1, 1);

            // 实现模型动画
            if (pEntity.getAnimationProgress(pPartialTicks) == 0.0F) {
                if(degree>=30){
                    degree=0;
                }
                degree++;
            } else degree=0;
            this.model.setupAnim(pEntity, f, degree, pEntity.getAnimationProgress(pPartialTicks), pEntity.getYRot(), pEntity.getXRot());
            float r2 = (float) (0.3f * Math.cos(degree * 6 * Mth.DEG_TO_RAD)) - 0.2f;
            this.model.scaleModel(r * (1+r2));
            //实现半透明渲染
            Minecraft minecraft = Minecraft.getInstance();
            // 标记：实体是否处于"发光且隐身"状态（如玩家隐身但持发光物品）
            boolean flag = minecraft.shouldEntityAppearGlowing(pEntity) && pEntity.isInvisible();
            VertexConsumer vertexconsumer; // 顶点数据消费者，用于向缓冲区写入渲染数据
            // 渲染条件：实体非隐身 或 处于"发光且隐身"状态（保证发光隐身时仍能看到外层轮廓）
            if (!pEntity.isInvisible() || flag) {
                // 根据渲染状态选择对应的渲染类型
                if (flag) {
                    // 发光隐身状态：使用轮廓渲染类型（仅绘制外层轮廓）
                    vertexconsumer = pBuffer.getBuffer(RenderType.outline(this.getTextureLocation(pEntity)));
                } else {
                    // 正常状态：使用半透明实体渲染类型（贴合史莱姆外层果冻的半透明质感）
                    vertexconsumer = pBuffer.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(pEntity)));
                }
            } else vertexconsumer = pBuffer.getBuffer(this.model.renderType(this.getTextureLocation(pEntity)));
            float apl = 1.0f - 0.05f * (pEntity.getAnimationProgress(pPartialTicks) - 20);
            if (apl > 1) apl = 1.0f;
            this.model.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, apl);
            pPoseStack.popPose();

            super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
        }
    }
    @Override
    protected int getBlockLightLevel(SoulFireHitboxEntity pEntity, BlockPos pPos) {return 15;}
    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull SoulFireHitboxEntity pEntity) {
        return TEXTURE_LOCATION;
    }
}
