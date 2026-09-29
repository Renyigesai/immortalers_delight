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

public class SoulBoomModel <T extends Entity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(ImmortalersDelightMod.MODID, "hatredfire_bolt_model"), "main");
    private final ModelPart core;
    private final ModelPart layer;
    private final ModelPart layer2;
    private final ModelPart layer3;

    public SoulBoomModel(ModelPart root) {
        this.core = root.getChild("core");
        this.layer = root.getChild("layer");
        this.layer2 = root.getChild("layer2");
        this.layer3 = root.getChild("layer3");
    }


    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition core = partdefinition.addOrReplaceChild("core", CubeListBuilder.create().texOffs(76, 22).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 0.0F));

        PartDefinition layer = partdefinition.addOrReplaceChild("layer", CubeListBuilder.create().texOffs(76, 46).addBox(2.0F, 2.0F, 2.0F, -4.0F, -4.0F, -4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 0.0F));

        PartDefinition layer2 = partdefinition.addOrReplaceChild("layer2", CubeListBuilder.create().texOffs(40, 52).addBox(4.0F, 4.0F, 4.0F, -8.0F, -8.0F, -8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 0.0F));

        PartDefinition layer3 = partdefinition.addOrReplaceChild("layer3", CubeListBuilder.create().texOffs(64, 32).addBox(8.0F, 8.0F, 8.0F, -16.0F, -16.0F, -16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

//    public static LayerDefinition createBodyLayer() {
//        MeshDefinition meshdefinition = new MeshDefinition();
//        PartDefinition partdefinition = meshdefinition.getRoot();
//
//        PartDefinition core = partdefinition.addOrReplaceChild("core", CubeListBuilder.create().texOffs(64, 16).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 0.0F));
//
//        PartDefinition layer = partdefinition.addOrReplaceChild("layer", CubeListBuilder.create().texOffs(88, 52).addBox(5.0F, 5.0F, 5.0F, -10.0F, -10.0F, -10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 0.0F));
//
//        PartDefinition layer2 = partdefinition.addOrReplaceChild("layer2", CubeListBuilder.create().texOffs(48, 56).addBox(6.0F, 6.0F, 6.0F, -12.0F, -12.0F, -12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 0.0F));
//
//        PartDefinition layer3 = partdefinition.addOrReplaceChild("layer3", CubeListBuilder.create().texOffs(64, 32).addBox(8.0F, 8.0F, 8.0F, -16.0F, -16.0F, -16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 0.0F));
//
//        return LayerDefinition.create(meshdefinition, 128, 128);
//    }

//    public static LayerDefinition createBodyLayer() {
//        MeshDefinition meshdefinition = new MeshDefinition();
//        PartDefinition partdefinition = meshdefinition.getRoot();
//
//        PartDefinition core = partdefinition.addOrReplaceChild("core", CubeListBuilder.create().texOffs(64, 16).addBox(-4.0F, -12.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
//
//        PartDefinition layer = partdefinition.addOrReplaceChild("layer", CubeListBuilder.create().texOffs(88, 52).addBox(5.0F, -3.0F, 5.0F, -10.0F, -10.0F, -10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
//
//        PartDefinition layer2 = partdefinition.addOrReplaceChild("layer2", CubeListBuilder.create().texOffs(48, 56).addBox(6.0F, -2.0F, 6.0F, -12.0F, -12.0F, -12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
//
//        PartDefinition layer3 = partdefinition.addOrReplaceChild("layer3", CubeListBuilder.create().texOffs(64, 32).addBox(8.0F, 0.0F, 8.0F, -16.0F, -16.0F, -16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
//
//        return LayerDefinition.create(meshdefinition, 128, 128);
//    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // 1. 末影水晶上下悬浮正弦波公式 (作用于所有部件的 Y 轴 offset)
//        float wave = Mth.sin(ageInTicks * 0.15F) / 2.0F + 0.5F;
//        wave = (wave * wave + wave) * 0.2F; // 波动位移量
//        float hoverYOffset = (wave - 0.1F) * 16.0F; // 转换至像素单位
//        float baseY = 16.0F + hoverYOffset;
//
        this.core.getAllParts().forEach(ModelPart::resetPose);
        this.layer.getAllParts().forEach(ModelPart::resetPose);
        this.layer2.getAllParts().forEach(ModelPart::resetPose);
        this.layer3.getAllParts().forEach(ModelPart::resetPose);

        float baseY = 8.0F;
        this.core.y = baseY;
        this.layer.y = baseY;
        this.layer2.y = baseY;
        this.layer3.y = baseY;

        // 2. 各层差速自转与空间倾角计算 (单位为弧度)
        float baseAngle = ageInTicks * 0.08F; // 基础角速度

        // 最外层 layer3：正向旋转 + 倾角 (45度倾斜)
        this.layer3.yRot = baseAngle;
        this.layer3.xRot = (float) (Math.PI / 4D);
        this.layer3.zRot = (float) (Math.PI / 6D);

        // 次外层 layer2：反向 1.25 倍速旋转 + 反向空间倾角
        this.layer2.yRot = -baseAngle * 1.25F;
        this.layer2.xRot = -(float) (Math.PI / 4D);
        this.layer2.zRot = (float) (Math.PI / 4D);

        // 核心 core：正向 2 倍速旋转 + 倾角错位
        this.core.yRot = baseAngle * 2.0F;
        this.core.xRot = (float) (Math.PI / 3D);
        this.core.zRot = -(float) (Math.PI / 4D);

        // 内层 layer：不自转，对准运动方向
        this.layer.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.layer.xRot = headPitch * ((float)Math.PI / 180F);
//        this.core.yRot = -baseAngle * 3.0F;
//        this.core.xRot = ageInTicks * 0.05F;
//        this.core.zRot = ageInTicks * 0.03F;

        //缩放
        this.core.xScale = this.quadSize;
        this.core.yScale = this.quadSize;
        this.core.zScale = this.quadSize;
        this.layer.xScale = this.quadSize;
        this.layer.yScale = this.quadSize;
        this.layer.zScale = this.quadSize;
        this.layer2.xScale = this.quadSize;
        this.layer2.yScale = this.quadSize;
        this.layer2.zScale = this.quadSize;
        this.layer3.xScale = this.quadSize;
        this.layer3.yScale = this.quadSize;
        this.layer3.zScale = this.quadSize;
    }

    //用于方块实体渲染
    public void setupAnim(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
// 1. 末影水晶上下悬浮正弦波公式 (作用于所有部件的 Y 轴 offset)
//        float wave = Mth.sin(ageInTicks * 0.15F) / 2.0F + 0.5F;
//        wave = (wave * wave + wave) * 0.2F; // 波动位移量
//        float hoverYOffset = (wave - 0.1F) * 16.0F; // 转换至像素单位
//        float baseY = 16.0F + hoverYOffset;
//
        this.core.getAllParts().forEach(ModelPart::resetPose);
        this.layer.getAllParts().forEach(ModelPart::resetPose);
        this.layer2.getAllParts().forEach(ModelPart::resetPose);
        this.layer3.getAllParts().forEach(ModelPart::resetPose);

        float baseY = 8.0F;
        this.core.y = baseY;
        this.layer.y = baseY;
        this.layer2.y = baseY;
        this.layer3.y = baseY;

        // 2. 各层差速自转与空间倾角计算 (单位为弧度)
        float baseAngle = ageInTicks * 0.08F; // 基础角速度

        // 最外层 layer3：正向旋转 + 倾角 (45度倾斜)
        this.layer3.yRot = baseAngle;
        this.layer3.xRot = (float) (Math.PI / 4D);
        this.layer3.zRot = (float) (Math.PI / 6D);

        // 次外层 layer2：反向 1.25 倍速旋转 + 反向空间倾角
        this.layer2.yRot = -baseAngle * 1.25F;
        this.layer2.xRot = -(float) (Math.PI / 4D);
        this.layer2.zRot = (float) (Math.PI / 4D);

        // 核心 core：正向 2 倍速旋转 + 倾角错位
        this.core.yRot = baseAngle * 2.0F;
        this.core.xRot = (float) (Math.PI / 3D);
        this.core.zRot = -(float) (Math.PI / 4D);

        // 内层 layer：不自转，对准运动方向
        this.layer.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.layer.xRot = headPitch * ((float)Math.PI / 180F);
//        this.core.yRot = -baseAngle * 3.0F;
//        this.core.xRot = ageInTicks * 0.05F;
//        this.core.zRot = ageInTicks * 0.03F;

        //缩放
        this.core.xScale = this.quadSize;
        this.core.yScale = this.quadSize;
        this.core.zScale = this.quadSize;
        this.layer.xScale = this.quadSize;
        this.layer.yScale = this.quadSize;
        this.layer.zScale = this.quadSize;
        this.layer2.xScale = this.quadSize;
        this.layer2.yScale = this.quadSize;
        this.layer2.zScale = this.quadSize;
        this.layer3.xScale = this.quadSize;
        this.layer3.yScale = this.quadSize;
        this.layer3.zScale = this.quadSize;
    }
    protected float quadSize = 0.75F;
    protected float quadSize0 = 0;
    public void explosionAnim(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

        if (this.quadSize0 <= 0) this.quadSize0 = this.quadSize;
        float lastQuadSize = this.quadSize0;

        if (ageInTicks <= 14) {
            float f = (float)ageInTicks / (float)14;
            if (ageInTicks <= 3) {
                float f1 = (float) ageInTicks / 5;
                float f2 = lastQuadSize * (f1 * f * 20 * 14 / 64);
                this.quadSize = f2 > 1f ? 1 : f2;
            } else {
                if (f > 0.5f) {
                    f -= 0.5f;
                    this.quadSize *= (1.3F + f * 0.6f);
                } else this.quadSize *= (0.8F + f);
                this.quadSize += 0.5f * Mth.sin((f - 0.05f) * 0.5f * (float)Math.PI);
            }
        } else if (ageInTicks < 20) {
            this.quadSize *= 0.5f;
        } else {
            this.quadSize = 0.75f;
            this.quadSize0 = 0;
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        core.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        layer.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        layer2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        layer3.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
    public ModelPart getCore() { return core; }
    public ModelPart getLayer() { return layer; }
    public ModelPart getLayer2() { return layer2; }
    public ModelPart getLayer3() { return layer3; }

}
