package com.renyigesai.immortalers_delight.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class WarmingLayerModel<T extends Entity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(ImmortalersDelightMod.MODID, "warming_layer"), "main");
    private final ModelPart all;
    private final ModelPart inside;
    private final ModelPart outside;

    public WarmingLayerModel(ModelPart root) {
        this.all = root.getChild("all");
        this.inside = this.all.getChild("inside");
        this.outside = this.all.getChild("outside");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition all = partdefinition.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition inside = all.addOrReplaceChild("inside", CubeListBuilder.create().texOffs(-128, 0).addBox(-64.0F, 0.0F, -64.0F, 128.0F, 0.0F, 128.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 0.0F));

        PartDefinition outside = all.addOrReplaceChild("outside", CubeListBuilder.create().texOffs(-128, 128).addBox(-64.0F, 0.0F, -64.0F, 128.0F, 0.0F, 128.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 0.0F));

        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }
    /**
     * 设置警告圈的动态动画参数
     *
     * @param rMin        显示的起始半径（世界/模型单位）
     * @param rMax        显示的最大半径（世界/模型单位）
     * @param ageInTicks  当前动画 tick 进度（支持分刻度浮点数）
     * @param totalTicks  当前预设动画总时长
     * @param omega       角速度（单位：度/tick）
     * @return            返还当前的缩放半径
     */
    public float setupAnim(float rMin, float rMax, float ageInTicks, float totalTicks, float omega) {
        this.all.getAllParts().forEach(ModelPart::resetPose);
        // 模型盒体尺寸为 128x128，从 -64 到 64，基准初始半径为4格宽
        final float BASE_MODEL_RADIUS = 4.0F;

        // 避免除以 0 导致 NaN
        if (totalTicks <= 0.0F) {
            this.inside.visible = false;
            this.outside.visible = false;
            return 0;
        }

        // 计算归一化的动画进度 0.0 ~ 1.0
        float progress = Mth.clamp(ageInTicks, 0.0F, totalTicks) / totalTicks;

        // ========================================================
        // 1. Outside 部件计算逻辑
        // 进度 0.0 ~ 0.3 缩放从 rMin -> rMax，此后保持 rMax
        // ========================================================
        this.outside.visible = true;
        float outsideCurrentRadius;
        float outsideEndTick = totalTicks * 0.3F;

        if (progress <= 0.3F) {
            float outsideT = progress / 0.3F;
            outsideCurrentRadius = Mth.lerp(outsideT, rMin, rMax);
            // 匀速旋转：角度 = tick数 * omega
            float currentAngleDeg = ageInTicks * omega;
            this.outside.yRot = currentAngleDeg * Mth.DEG_TO_RAD;
        } else {
            outsideCurrentRadius = rMax;
            // 停驻在缩放结束点后的“下一个 90 度倍数”
            float finalAngleDeg = calculateSnappedAngle(outsideEndTick * omega, omega);
            this.outside.yRot = finalAngleDeg * Mth.DEG_TO_RAD;
        }

        float outsideScale = outsideCurrentRadius / BASE_MODEL_RADIUS;
        // X 和 Z 方向水平缩放（Y轴保持1.0避免压扁厚度或翻转法线）
        this.outside.xScale = outsideScale;
        this.outside.yScale = 1.0F;
        this.outside.zScale = outsideScale;

        // ========================================================
        // 2. Inside 部件计算逻辑
        // 进度 0.0 ~ 0.4 隐藏
        // 进度 0.4 ~ 0.9 缩放从 rMin -> rMax，此后保持 rMax
        // ========================================================
        if (progress < 0.4F) {
            this.inside.visible = false;
        } else {
            this.inside.visible = true;
            float insideCurrentRadius;
            float insideEndTick = totalTicks * 0.9F;

            if (progress <= 0.9F) {
                float insideT = (progress - 0.4F) / (0.9F - 0.4F);
                insideCurrentRadius = Mth.lerp(insideT, rMin, rMax);
                // 仅在自身开始运动时旋转（自 0.4 进度对应的 tick 开始）
                float insideActiveTicks = ageInTicks - (totalTicks * 0.4F);
                float currentAngleDeg = insideActiveTicks * omega;
                this.inside.yRot = currentAngleDeg * Mth.DEG_TO_RAD;
            } else {
                insideCurrentRadius = rMax;
                // 停驻在缩放结束时的下一个 90 度倍数
                float totalActiveTicks = insideEndTick - (totalTicks * 0.4F);
                float finalAngleDeg = calculateSnappedAngle(totalActiveTicks * omega, omega);
                this.inside.yRot = finalAngleDeg * Mth.DEG_TO_RAD;
            }

            float insideScale = insideCurrentRadius / BASE_MODEL_RADIUS;
            this.inside.xScale = insideScale;
            this.inside.yScale = 1.0F;
            this.inside.zScale = insideScale;
        }
        return outsideCurrentRadius;
    }

    /**
     * 辅助算法：计算旋转终止时下一个 90 度的倍数
     *
     * @param endAngleDeg 达到最大缩放时刚好转过的角度
     * @param omega       角速度（用于判断旋转方向）
     * @return 贴合停靠的整 90 度角
     */
    private static float calculateSnappedAngle(float endAngleDeg, float omega) {
        if (omega > 0.0F) {
            // 顺时针旋转：沿运动方向取下一个 90 度的倍数（向上取整）
            return (float) Math.ceil(endAngleDeg / 90.0F) * 90.0F;
        } else if (omega < 0.0F) {
            // 逆时针旋转：向下取整
            return (float) Math.floor(endAngleDeg / 90.0F) * 90.0F;
        } else {
            return 0.0F;
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        inside.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        outside.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha*0.6f);
    }
}
