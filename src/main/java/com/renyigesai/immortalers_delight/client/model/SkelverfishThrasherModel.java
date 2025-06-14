package com.renyigesai.immortalers_delight.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.client.animation.SkelverfishThrasherAnimation;
import com.renyigesai.immortalers_delight.client.animation.StrangeArmourStandAnimation;
import com.renyigesai.immortalers_delight.entities.living.SkelverfishThrasher;
import com.renyigesai.immortalers_delight.entities.living.StrangeArmourStand;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class SkelverfishThrasherModel<T extends SkelverfishThrasher> extends HierarchicalModel<T> {
    public static final ModelLayerLocation SKELVERFISH_THRASHER = new ModelLayerLocation(new ResourceLocation(ImmortalersDelightMod.MODID, "skelverfish_thrasher"), "main");
    private final ModelPart bodyPart_2;
    private final ModelPart bodyPart_0;
    private final ModelPart bodyPart_1;
    private final ModelPart bodyLayer_2;
    private final ModelPart hand_1;
    private final ModelPart arm_1;
    private final ModelPart forearm_1;
    private final ModelPart hand_2;
    private final ModelPart arm_2;
    private final ModelPart forearm_2;
    private final ModelPart bodyPart_3;
    private final ModelPart bodyPart_4;
    private final ModelPart bodyLayer_1;
    private final ModelPart bodyPart_5;
    private final ModelPart bodyPart_6;
    private final ModelPart bodyLayer_0;
    // 身体部分和层的数组，用于简化动画处理（适配蠹虫的动画）
    private final ModelPart[] bodyParts = new ModelPart[7];
    private final ModelPart[] bodyLayers = new ModelPart[3];

    public SkelverfishThrasherModel(ModelPart root) {
        this.bodyPart_2 = root.getChild("bodyPart_2");
        this.bodyPart_0 = this.bodyPart_2.getChild("bodyPart_0");
        this.bodyPart_1 = this.bodyPart_2.getChild("bodyPart_1");
        this.bodyLayer_2 = this.bodyPart_1.getChild("bodyLayer_2");
        this.hand_1 = this.bodyPart_1.getChild("hand_1");
        this.arm_1 = this.hand_1.getChild("arm_1");
        this.forearm_1 = this.arm_1.getChild("forearm_1");
        this.hand_2 = this.bodyPart_1.getChild("hand_2");
        this.arm_2 = this.hand_2.getChild("arm_2");
        this.forearm_2 = this.arm_2.getChild("forearm_2");
        this.bodyPart_3 = this.bodyPart_2.getChild("bodyPart_3");
        this.bodyPart_4 = this.bodyPart_2.getChild("bodyPart_4");
        this.bodyLayer_1 = this.bodyPart_4.getChild("bodyLayer_1");
        this.bodyPart_5 = this.bodyPart_2.getChild("bodyPart_5");
        this.bodyPart_6 = this.bodyPart_2.getChild("bodyPart_6");
        this.bodyLayer_0 = this.bodyPart_2.getChild("bodyLayer_0");

        // 初始化数组，建立与蠹虫模型的对应关系
        bodyParts[0] = bodyPart_0;
        bodyParts[1] = bodyPart_1;
        bodyParts[2] = bodyPart_2;
        bodyParts[3] = bodyPart_3;
        bodyParts[4] = bodyPart_4;
        bodyParts[5] = bodyPart_5;
        bodyParts[6] = bodyPart_6;

        bodyLayers[0] = bodyLayer_0;
        bodyLayers[1] = bodyLayer_1;
        bodyLayers[2] = bodyLayer_2;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bodyPart_2 = partdefinition.addOrReplaceChild("bodyPart_2", CubeListBuilder.create().texOffs(28, 32).addBox(-3.0F, 0.0F, -1.5F, 6.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 20.0F, 1.0F));

        PartDefinition bodyPart_0 = bodyPart_2.addOrReplaceChild("bodyPart_0", CubeListBuilder.create().texOffs(28, 46).addBox(-2.0F, 0.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, -4.5F));

        PartDefinition bodyPart_1 = bodyPart_2.addOrReplaceChild("bodyPart_1", CubeListBuilder.create().texOffs(0, 42).addBox(-3.0F, 0.0F, -1.0F, 6.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, -2.5F));

        PartDefinition bodyLayer_2 = bodyPart_1.addOrReplaceChild("bodyLayer_2", CubeListBuilder.create().texOffs(28, 25).addBox(-4.0F, 0.0F, -1.5F, 8.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 0.0F));

        PartDefinition hand_1 = bodyPart_1.addOrReplaceChild("hand_1", CubeListBuilder.create(), PartPose.offsetAndRotation(1.0F, 1.0F, -2.0F, 0.0F, 0.0873F, -0.0873F));

        PartDefinition arm_1 = hand_1.addOrReplaceChild("arm_1", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 13.0F, new CubeDeformation(0.05F))
                .texOffs(6, 47).addBox(-0.5F, -0.5F, 12.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, 0.5F, 0.0F));

        PartDefinition forearm_1 = arm_1.addOrReplaceChild("forearm_1", CubeListBuilder.create().texOffs(0, 14).addBox(-0.5F, -0.5F, -12.5F, 1.0F, 1.0F, 13.0F, new CubeDeformation(0.1F))
                .texOffs(46, 37).addBox(-0.5F, -0.5F, -14.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 12.5F));

        PartDefinition hand_2 = bodyPart_1.addOrReplaceChild("hand_2", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.0F, 1.0F, -2.0F, 0.0F, -0.0873F, 0.0873F));

        PartDefinition arm_2 = hand_2.addOrReplaceChild("arm_2", CubeListBuilder.create().texOffs(28, 0).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 13.0F, new CubeDeformation(0.05F))
                .texOffs(10, 47).addBox(-0.5F, -0.5F, 12.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, 0.5F, 0.0F));

        PartDefinition forearm_2 = arm_2.addOrReplaceChild("forearm_2", CubeListBuilder.create().texOffs(0, 28).addBox(-0.5F, -0.5F, -12.5F, 1.0F, 1.0F, 13.0F, new CubeDeformation(0.1F))
                .texOffs(40, 46).addBox(-1.5F, -0.5F, -14.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 12.5F));

        PartDefinition bodyPart_3 = bodyPart_2.addOrReplaceChild("bodyPart_3", CubeListBuilder.create().texOffs(16, 42).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 3.0F));

        PartDefinition bodyPart_4 = bodyPart_2.addOrReplaceChild("bodyPart_4", CubeListBuilder.create().texOffs(46, 32).addBox(-1.0F, 0.0F, -1.5F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 6.0F));

        PartDefinition bodyLayer_1 = bodyPart_4.addOrReplaceChild("bodyLayer_1", CubeListBuilder.create().texOffs(28, 39).addBox(-3.0F, 0.0F, -1.5F, 6.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 0.0F));

        PartDefinition bodyPart_5 = bodyPart_2.addOrReplaceChild("bodyPart_5", CubeListBuilder.create().texOffs(46, 41).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, 8.5F));

        PartDefinition bodyPart_6 = bodyPart_2.addOrReplaceChild("bodyPart_6", CubeListBuilder.create().texOffs(0, 47).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, 10.5F));

        PartDefinition bodyLayer_0 = bodyPart_2.addOrReplaceChild("bodyLayer_0", CubeListBuilder.create().texOffs(28, 14).addBox(-5.0F, 0.0F, -1.5F, 10.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }
    @Override
    public ModelPart root() {
        return this.bodyPart_2;
    }

    @Override
    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        if (pEntity.attackAnimationDuration <= 0) {
            // 循环处理每个身体部分
            for (int i = 0; i < bodyParts.length; ++i) {
                // 计算身体部分的 y 轴旋转角度（与蠹虫模型相同）
                bodyParts[i].yRot = Mth.cos(pAgeInTicks * 0.9F + i * 0.15F * (float) Math.PI) * (float) Math.PI * 0.05F * (1 + Math.abs(i - 2));
                // 计算身体部分的 x 轴偏移量（与蠹虫模型相同）
                bodyParts[i].x = Mth.sin(pAgeInTicks * 0.9F + i * 0.15F * (float) Math.PI) * (float) Math.PI * 0.2F * Math.abs(i - 2);
            }

            // 让身体层的旋转和偏移与对应的身体部分保持一致（根据你的模型结构调整）
            bodyLayers[0].yRot = bodyParts[2].yRot * 0.9F;  // layer0 对应 segment2
            bodyLayers[1].yRot = bodyParts[4].yRot * 0.9F;  // layer1 对应 segment4
            bodyLayers[1].x = bodyParts[4].x;
            bodyLayers[2].yRot = bodyParts[1].yRot * 0.9F;  // layer2 对应 segment1
            bodyLayers[2].x = bodyParts[1].x;
        }
        this.animate(pEntity.attackAnimationState, SkelverfishThrasherAnimation.ATTACK,pAgeInTicks, 1.0f);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        bodyPart_2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
