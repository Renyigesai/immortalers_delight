package com.renyigesai.immortalers_delight.client.renderer.entity.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.client.model.projectile.MoonArrowHitboxModel;
import com.renyigesai.immortalers_delight.entities.projectile.MoonArrowHitboxEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class MoonArrowHitboxRenderer extends EntityRenderer<MoonArrowHitboxEntity> {
    private static final ResourceLocation TEXTURE_LOCATION_0 = new ResourceLocation(ImmortalersDelightMod.MODID,"textures/entity/projectile/moon_arrow_hitbox_0.png");
    private static final ResourceLocation TEXTURE_LOCATION_1 = new ResourceLocation(ImmortalersDelightMod.MODID,"textures/entity/projectile/moon_arrow_hitbox_1.png");
    private static final ResourceLocation TEXTURE_LOCATION_2 = new ResourceLocation(ImmortalersDelightMod.MODID,"textures/entity/projectile/moon_arrow_hitbox_2.png");
    private static final ResourceLocation TEXTURE_LOCATION_3 = new ResourceLocation(ImmortalersDelightMod.MODID,"textures/entity/projectile/moon_arrow_hitbox_3.png");
    private static final ResourceLocation TEXTURE_LOCATION_4 = new ResourceLocation(ImmortalersDelightMod.MODID,"textures/entity/projectile/moon_arrow_hitbox_4.png");
    private static final ResourceLocation TEXTURE_LOCATION_5 = new ResourceLocation(ImmortalersDelightMod.MODID,"textures/entity/projectile/moon_arrow_hitbox_5.png");
    private final MoonArrowHitboxModel<MoonArrowHitboxEntity> model;
    private static int degree = 0;

    public MoonArrowHitboxRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new MoonArrowHitboxModel<>(pContext.bakeLayer(MoonArrowHitboxModel.LAYER_LOCATION));
    }

    public void render(MoonArrowHitboxEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        float r = pEntity.getRadius();
        if (pEntity.getAnimationProgress(pPartialTicks) >= 0.0F) {
            // 计算偏航角（水平旋转）的平滑插值
            float f = Mth.rotLerp(pPartialTicks, pEntity.yRotO, pEntity.getYRot());
            // 计算俯仰角（垂直旋转）的平滑插值
            float f1 = Mth.lerp(pPartialTicks, pEntity.xRotO, pEntity.getXRot());

            pPoseStack.pushPose();
            // 旋转模型以匹配实体的旋转
            pPoseStack.mulPose(Axis.YP.rotationDegrees(90.0F - pEntity.getYRot()));
            float f2 = 1.50F * r - 0.5f;
            pPoseStack.translate(0.0D, f2, 0.0D);
            pPoseStack.scale(-r, -r, r);

            // 实现模型动画
            if (pEntity.getAnimationProgress(pPartialTicks) == 0.0F) {
                this.model.setHide(false);
                if(degree==30){
                    degree=0;
                }
                degree++;
                pPoseStack.mulPose(Axis.YP.rotationDegrees(f * 6));
            } else this.model.setHide(true);
            this.model.setupAnim(pEntity, f, 1.0F, pEntity.getAnimationProgress(pPartialTicks), pEntity.getYRot(), pEntity.getXRot());

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
            this.model.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            pPoseStack.popPose();

            super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
        }
    }
    @Override
    protected int getBlockLightLevel(MoonArrowHitboxEntity pEntity, BlockPos pPos) {return 15;}
    /**
     * Returns the location of an entity's texture.
     */
    public @NotNull ResourceLocation getTextureLocation(@NotNull MoonArrowHitboxEntity pEntity) {
        float f = pEntity.getAnimationProgress(0);
        if (f < 10.0F) return TEXTURE_LOCATION_0;
        else if (f < 12.0F) return TEXTURE_LOCATION_1;
        else if (f < 15.0F) return TEXTURE_LOCATION_2;
        else if (f < 17.0F) return TEXTURE_LOCATION_3;
        else if (f < 20.0F) return TEXTURE_LOCATION_4;
        else return TEXTURE_LOCATION_5;
    }
}
