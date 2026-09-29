package com.renyigesai.immortalers_delight.client.renderer.entity.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.client.model.HatredfireBoltModel;
import com.renyigesai.immortalers_delight.client.renderer.ModRenderTypes;
import com.renyigesai.immortalers_delight.entities.projectile.HatredfireBoltEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Quaternionf;

@OnlyIn(Dist.CLIENT)
public class HatredfireBoltRenderer extends EntityRenderer<HatredfireBoltEntity> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(ImmortalersDelightMod.MODID, "textures/entity/hatredfire_bolt.png");

    // 使用半透明且不剔除背面的通道渲染多层火焰
    private static final RenderType RENDER_TYPE = RenderType.eyes(TEXTURE);
    private static final RenderType RENDER_TYPE_2 = RenderType.entityTranslucent(TEXTURE);
    private static final float SIN_45 = (float) Math.sin(Math.PI / 4D);

    private final HatredfireBoltModel<HatredfireBoltEntity> model;

    public HatredfireBoltRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.3F;
        this.model = new HatredfireBoltModel<>(context.bakeLayer(HatredfireBoltModel.LAYER_LOCATION));
    }

    @Override
    public void render(HatredfireBoltEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        // 计算平滑插值 Tick
        float ageInTicks = (float) entity.tickCount + partialTicks;

        // 3. 计算偏航角（水平旋转）的平滑插值，基于部分游戏刻，实现旋转动作的平滑过渡
        float f = Mth.rotLerp(partialTicks, entity.yRotO, entity.getYRot());
        // 4. 计算俯仰角（垂直旋转）的平滑插值，实现上下摆动的平滑过渡
        float f1 = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
        // 驱动动画逻辑（自动绕几何中心转动与悬浮）
        this.model.setupAnim(entity, 0.0F, 0.0F, ageInTicks, f, f1);

        VertexConsumer vertexConsumer = buffer.getBuffer(ModRenderTypes.UNLIT_TRANSLUCENT.apply(this.getTextureLocation(entity)));
        this.model.renderToBuffer(poseStack, vertexConsumer, 0xF000F0, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

//
//        // 1. 获取时间与悬浮位移插值 (末影水晶非线性正弦波)
//        float ageInTicks = (float) entity.tickCount + partialTicks;
//        float hoverY = getHoverY(ageInTicks);
//
//        // 基础旋转角速度 (每 tick 4 度)
//        float baseRotation = ageInTicks * 4.0F;
//
//        // 2. 将整个模型中心对齐到实体锚点，并施加悬浮位移
//        // 注意：原模型 PartPose offset Y=24，因此向上偏移 1.5F 调整回模型几何中心
//        poseStack.translate(0.0F, 1.5F + hoverY, 0.0F);
//
//        // 如果是弹射物，也可以根据运动方向让整体发生倾斜（可选），这里先维持末影水晶的竖直自转体系
//        VertexConsumer vertexConsumer = buffer.getBuffer(RENDER_TYPE);
//        VertexConsumer vertexConsumer2 = buffer.getBuffer(RENDER_TYPE_2);
//        int overlay = OverlayTexture.NO_OVERLAY;
//
//        // --- 第 1 层：最外层火焰 (layer3, 尺寸 16) ---
//        poseStack.pushPose();
//        poseStack.mulPose(Axis.YP.rotationDegrees(baseRotation));
//        // 沿空间 (1, 0, 1) 向量倾斜 60 度
//        poseStack.mulPose(new Quaternionf().setAngleAxis((float) (Math.PI / 3D), SIN_45, 0.0F, SIN_45));
//        // 抵消 PartPose 默认的 24.0F 偏移以绕自身中心旋转
//        poseStack.translate(0.0F, -1.5F, 0.0F);
//        this.model.getLayer3().render(poseStack, vertexConsumer2, packedLight, overlay, 1.0F, 1.0F, 1.0F, 0.6F); // 稍带透明
//        poseStack.popPose();
//
//        // --- 第 2 层：次外层火焰 (layer2, 尺寸 12) ---
//        poseStack.pushPose();
//        // 反向自转 + 倾斜 45 度产生交错感
//        poseStack.mulPose(Axis.YP.rotationDegrees(-baseRotation * 1.25F));
//        poseStack.mulPose(new Quaternionf().setAngleAxis((float) (Math.PI / 4D), -SIN_45, 0.0F, SIN_45));
//        poseStack.translate(0.0F, -1.5F, 0.0F);
//        this.model.getLayer2().render(poseStack, vertexConsumer2, packedLight, overlay, 1.0F, 1.0F, 1.0F, 0.8F);
//        poseStack.popPose();
//
//        // --- 第 3 层：内层火焰 (layer, 尺寸 10) ---
//        poseStack.pushPose();
//        // 2 倍顺时针自转 + 倾斜
//        poseStack.mulPose(Axis.YP.rotationDegrees(baseRotation * 2.0F));
//        poseStack.mulPose(new Quaternionf().setAngleAxis((float) (Math.PI / 3D), SIN_45, 0.0F, -SIN_45));
//        poseStack.translate(0.0F, -1.5F, 0.0F);
//        this.model.getLayer().render(poseStack, vertexConsumer2, packedLight, overlay, 1.0F, 1.0F, 1.0F, 0.9F);
//        poseStack.popPose();
//
//        // --- 第 4 层：高亮核心 (core, 尺寸 8) ---
//        poseStack.pushPose();
//        // 核心快速反转 (3倍速) + 翻滚
//        poseStack.mulPose(Axis.YP.rotationDegrees(-baseRotation * 3.0F));
//        poseStack.mulPose(Axis.XP.rotationDegrees(ageInTicks * 2.0F));
//        poseStack.translate(0.0F, -1.5F, 0.0F);
//        // 核心使用满级光照 0xF000F0 (自发光，不受夜间黑暗影响)
//        int fullBright = 0xF000F0;
//        this.model.getCore().render(poseStack, vertexConsumer, fullBright, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
//        poseStack.popPose();

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    /**
     * 末影水晶的悬浮计算公式
     */
    private static float getHoverY(float ageInTicks) {
        float wave = Mth.sin(ageInTicks * 0.15F) / 2.0F + 0.5F;
        wave = (wave * wave + wave) * 0.2F; // 悬浮幅度缩放到 0.2 格左右
        return wave - 0.1F;
    }

    @Override
    public ResourceLocation getTextureLocation(HatredfireBoltEntity entity) {
        return TEXTURE;
    }
}
