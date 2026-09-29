package com.renyigesai.immortalers_delight.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

/**
 * 嗅探兽升级鞍座模型 - 带储物箱版本
 */
public class SnifferSaddleUpgradedModel<T extends Entity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = 
        new ModelLayerLocation(new ResourceLocation("immortalers_delight", "sniffer_saddle_upgraded"), "main");
    
    private final ModelPart bone3;
    private final ModelPart bone4;
    private final ModelPart bone5;
    private final ModelPart bb_main;

    public SnifferSaddleUpgradedModel(ModelPart root) {
        this.bone3 = root.getChild("bone3");
        this.bone4 = root.getChild("bone4");
        this.bone5 = root.getChild("bone5");
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bone3 = partdefinition.addOrReplaceChild("bone3", 
            CubeListBuilder.create()
                .texOffs(64, 125).addBox(-13.0F, -2.0F, -19.0F, 26.0F, 2.0F, 38.0F, new CubeDeformation(0.0F)), 
            PartPose.offset(0.0F, -9.0F, 0.0F));

        PartDefinition cube_r1 = bone3.addOrReplaceChild("cube_r1", 
            CubeListBuilder.create()
                .texOffs(8, 162).addBox(-10.0F, -9.0F, -1.0F, 21.0F, 9.0F, 5.0F, new CubeDeformation(0.0F)), 
            PartPose.offsetAndRotation(0.0F, -2.0F, 12.0F, -0.2182F, 0.0F, 0.0F));

        PartDefinition cube_r2 = bone3.addOrReplaceChild("cube_r2", 
            CubeListBuilder.create()
                .texOffs(7, 181).addBox(-11.0F, -4.0F, -1.0F, 20.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)), 
            PartPose.offsetAndRotation(1.0F, -1.0F, -15.0F, -0.48F, 0.0F, 0.0F));

        PartDefinition cube_r3 = bone3.addOrReplaceChild("cube_r3", 
            CubeListBuilder.create()
                .texOffs(66, 129).addBox(-1.0F, -2.0F, -19.0F, 0.0F, 25.0F, 38.0F, new CubeDeformation(0.0F)), 
            PartPose.offsetAndRotation(14.25F, 0.0F, 0.0F, 0.0F, 0.0F, -0.0873F));

        PartDefinition cube_r4 = bone3.addOrReplaceChild("cube_r4", 
            CubeListBuilder.create()
                .texOffs(66, 129).addBox(-1.0F, -2.0F, -19.0F, 0.0F, 25.0F, 38.0F, new CubeDeformation(0.0F)), 
            PartPose.offsetAndRotation(-12.25F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0873F));

        PartDefinition bone4 = partdefinition.addOrReplaceChild("bone4", 
            CubeListBuilder.create()
                .texOffs(144, 101).addBox(7.0F, -27.0F, -13.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), 
            PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition cube_r5 = bone4.addOrReplaceChild("cube_r5", 
            CubeListBuilder.create()
                .texOffs(144, 101).addBox(-7.0F, -5.0F, -6.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), 
            PartPose.offsetAndRotation(-13.0F, -22.0F, -8.0F, 0.0F, 3.1416F, 0.0F));

        PartDefinition bone5 = partdefinition.addOrReplaceChild("bone5", 
            CubeListBuilder.create()
                .texOffs(184, 164).addBox(10.0F, -27.0F, -20.0F, 2.0F, 26.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(184, 164).addBox(-12.0F, -27.0F, -20.0F, 2.0F, 26.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(184, 170).addBox(10.0F, -19.0F, 18.0F, 2.0F, 18.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(184, 170).addBox(-12.0F, -19.0F, 18.0F, 2.0F, 18.0F, 2.0F, new CubeDeformation(0.0F)), 
            PartPose.offset(0.0F, -8.0F, 0.0F));

        PartDefinition cube_r6 = bone5.addOrReplaceChild("cube_r6", 
            CubeListBuilder.create()
                .texOffs(65, 140).addBox(-1.0F, -2.0F, -19.0F, 0.0F, 14.0F, 38.0F, new CubeDeformation(0.0F)), 
            PartPose.offsetAndRotation(15.25F, -22.0F, 0.0F, -0.2182F, 0.0F, -0.0873F));

        PartDefinition cube_r7 = bone5.addOrReplaceChild("cube_r7", 
            CubeListBuilder.create()
                .texOffs(66, 140).addBox(-1.0F, -2.0F, -19.0F, 0.0F, 14.0F, 38.0F, new CubeDeformation(0.0F)), 
            PartPose.offsetAndRotation(-13.5F, -22.0F, 0.0F, -0.2182F, 0.0F, 0.0873F));

        PartDefinition cube_r8 = bone5.addOrReplaceChild("cube_r8", 
            CubeListBuilder.create()
                .texOffs(133, 52).addBox(-13.0F, -2.0F, -22.0F, 27.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), 
            PartPose.offsetAndRotation(-0.5F, -9.8601F, -7.537F, -0.9163F, 0.0F, 0.0F));

        PartDefinition cube_r9 = bone5.addOrReplaceChild("cube_r9", 
            CubeListBuilder.create()
                .texOffs(42, 0).addBox(-14.0F, -2.0F, -20.0F, 28.0F, 2.0F, 47.0F, new CubeDeformation(0.0F)), 
            PartPose.offsetAndRotation(0.0F, -22.25F, -0.25F, -0.2182F, 0.0F, 0.0F));

        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", 
            CubeListBuilder.create()
                .texOffs(144, 76).addBox(13.0F, -27.0F, 3.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(107, 88).addBox(-25.0F, -27.0F, 3.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), 
            PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 192, 192);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // 鞍座是静态的，不需要动画
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        bone3.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        bone4.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        bone5.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}

