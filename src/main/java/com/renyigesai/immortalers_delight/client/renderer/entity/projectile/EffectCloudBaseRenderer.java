package com.renyigesai.immortalers_delight.client.renderer.entity.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.client.model.projectile.EffectCloudModel;
import com.renyigesai.immortalers_delight.client.model.projectile.SurveyorFangModel;
import com.renyigesai.immortalers_delight.entities.projectile.EffectCloudBaseEntity;
import com.renyigesai.immortalers_delight.entities.projectile.SurveyorFang;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class EffectCloudBaseRenderer extends EntityRenderer<EffectCloudBaseEntity> {
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(ImmortalersDelightMod.MODID,"textures/entity/projectile/effect_cloud_base.png");
    private final EffectCloudModel<EffectCloudBaseEntity> model;


    public EffectCloudBaseRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new EffectCloudModel<>(pContext.bakeLayer(EffectCloudModel.EFFECT_CLOUD_BASE));
    }

    public void render(EffectCloudBaseEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        float f = pEntity.getAnimationProgress(pPartialTicks);
        //System.out.println("实体动画进度" + f);
        if (f > 0.0F) {
            float f1 = (float)pEntity.getRadius();
            if (f <= 0.9F) {
                if (pEntity.getWaitTime() >= 16) f1 *= f * f * f;
                else f1 *= f * f;
            } else f1 *= f;

            pPoseStack.pushPose();
            pPoseStack.mulPose(Axis.YP.rotationDegrees(90.0F - pEntity.getYRot()));
            pPoseStack.scale(-f1, -f1, f1);
            float f2 = 0.03125F;
            pPoseStack.translate(0.0D, -0.626D, 0.0D);
            pPoseStack.scale(0.5F, 0.5F, 0.5F);
            //scaleAnimation(pEntity,pPoseStack,pPartialTicks);
            //this.model.setupAnim(pEntity, f, 1.0F, pEntity.tickCount + pPartialTicks, pEntity.getYRot(), pEntity.getXRot());
            this.model.setupAnim(pEntity, f, 0.0F, 0.0F, pEntity.getYRot(), pEntity.getXRot());
            // 获取Minecraft客户端实例，用于判断实体渲染状态
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
            } else vertexconsumer = pBuffer.getBuffer(this.model.renderType(TEXTURE_LOCATION));
            this.model.renderToBuffer(pPoseStack, vertexconsumer, 15728880, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            pPoseStack.popPose();

            super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
        }
    }

//    protected void scaleAnimation(EffectCloudBaseEntity entity, PoseStack pPoseStack, float pPartialTickTime) {
//        float f = entity.getAnimationProgress(pPartialTickTime);
//        pPoseStack.scale(0.999F, 0.999F, 0.999F);
//        pPoseStack.translate(0.0F, 0.001F, 0.0F);
//        //System.out.println("实体动画进度" + f);
//        if (f > 0.0F) {
//            float f1 = (float)entity.getRadius() * 0.25f;
//            if (f <= 0.9F) {
//                f = f * f * f;
//                f1 *= f;
//            }
////            float f2 = Mth.lerp(pPartialTickTime, entity.oSquish, entity.squish) / (f1 * 0.5F + 1.0F);
////            float f3 = 1.0F / (f2 + 1.0F);
//            pPoseStack.scale(f1, f1, f1);
//        }
    //}

    /**
     * Returns the location of an entity's texture.
     */
    public @NotNull ResourceLocation getTextureLocation(@NotNull EffectCloudBaseEntity pEntity) {
        return TEXTURE_LOCATION;
    }
}
