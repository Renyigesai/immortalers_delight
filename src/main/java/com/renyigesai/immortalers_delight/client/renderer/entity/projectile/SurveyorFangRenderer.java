package com.renyigesai.immortalers_delight.client.renderer.entity.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.client.model.projectile.SurveyorFangModel;
import com.renyigesai.immortalers_delight.entities.projectile.SurveyorFang;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EvokerFangsModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class SurveyorFangRenderer extends EntityRenderer<SurveyorFang> {
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(ImmortalersDelightMod.MODID,"textures/entity/projectile/sword_tipped_long_pole.png");
    private final SurveyorFangModel<SurveyorFang> model;

    public SurveyorFangRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new SurveyorFangModel<>(pContext.bakeLayer(SurveyorFangModel.SURVEYOR_FANG));
    }

    public void render(SurveyorFang pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        float f = pEntity.getAnimationProgress(pPartialTicks);
        //System.out.println("实体动画进度" + f);
        if (f != 0.0F) {
            float f1 = 2.0F;
            if (f > 0.9F) {
                f1 *= (1.0F - f) / 0.1F;
            }

            pPoseStack.pushPose();
            pPoseStack.mulPose(Axis.YP.rotationDegrees(90.0F - pEntity.getYRot()));
            pPoseStack.scale(-f1, -f1, f1);
            float f2 = 0.03125F;
            pPoseStack.translate(0.0D, -0.626D, 0.0D);
            pPoseStack.scale(0.5F, 0.5F, 0.5F);
            //this.model.setupAnim(pEntity, f, 1.0F, pEntity.tickCount + pPartialTicks, pEntity.getYRot(), pEntity.getXRot());
            this.model.setupAnim(pEntity, f, 0.0F, 0.0F, pEntity.getYRot(), pEntity.getXRot());
            VertexConsumer vertexconsumer = pBuffer.getBuffer(this.model.renderType(TEXTURE_LOCATION));
            this.model.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            pPoseStack.popPose();

//            pPoseStack.pushPose();
////            pPoseStack.translate(0.0F, 0.5F, 0.0F);
////            float xOffset = -1 / 32f;
////            float zOffset = 0;
////            pPoseStack.translate(-xOffset, 0, -zOffset);
////            pPoseStack.translate(xOffset, 0, zOffset);
//            this.model.translateToRoot(pPoseStack);
//            //pPoseStack.scale(-1, -1, 1);
//            //pPoseStack.mulPose(Axis.ZP.rotationDegrees(45.0F));
//            //pPoseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
//            pPoseStack.translate((float)-1 / 16.0F, 0.125F, -0.625F);
            // ========== 2. 渲染铁剑（关键修复：复用base骨骼的变换） ==========
            pPoseStack.pushPose();
            // 第一步：先应用与尖牙本体完全相同的基础变换（旋转+缩放+位移）
            pPoseStack.mulPose(Axis.YP.rotationDegrees(90.0F - pEntity.getYRot()));
            pPoseStack.scale(-f1, -f1, f1); // 同步尖牙的负缩放（关键：修正坐标系翻转）
            pPoseStack.translate(0.0D, -0.626D, 0.0D);
            pPoseStack.scale(0.5F, 0.5F, 0.5F);

            // 第二步：将PoseStack移动到base骨骼的位置（核心：复用骨骼动画的变换）
            this.model.translateToRoot(pPoseStack);

            // 第三步：调整铁剑相对于base骨骼的偏移（根据需求微调）
            // 注意：此处的偏移是基于base骨骼的本地坐标系，无需再处理翻转
            pPoseStack.translate(-1/16F, 0.625F, 0.0f); // 可根据需要调整X/Y/Z偏移
            // 可选：调整铁剑的旋转角度（比如让剑垂直/水平）
            pPoseStack.mulPose(Axis.YP.rotationDegrees(90.0F)); // 示例：绕X轴旋转90度
            pPoseStack.mulPose(Axis.ZP.rotationDegrees(-45.0F));
            //pPoseStack.scale(1.0F, 1.0F, 1.0F); // 铁剑的缩放（可根据需求调整）
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            ItemStack stack = new ItemStack(Items.IRON_SWORD);
            BakedModel bakedModel = itemRenderer.getModel(stack,pEntity.level(),null,0);
            itemRenderer.render(stack, ItemDisplayContext.FIXED,true,pPoseStack,pBuffer,pPackedLight, OverlayTexture.NO_OVERLAY,bakedModel);
            pPoseStack.popPose();
            super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
        }
    }

    /**
     * Returns the location of an entity's texture.
     */
    public @NotNull ResourceLocation getTextureLocation(@NotNull SurveyorFang pEntity) {
        return TEXTURE_LOCATION;
    }
}
