package com.renyigesai.immortalers_delight.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod; // 替换为你的Mod主类
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class SoulInfuserModel<T extends Entity> extends EntityModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            new ResourceLocation(ImmortalersDelightMod.MODID, "soul_infuser"), "main");
    private final ModelPart all;
    private final ModelPart head;
    private final ModelPart base;
    private final ModelPart ring_down;
    private final ModelPart upper_body_parts11;
    private final ModelPart upper_body_parts10;
    private final ModelPart upper_body_parts9;
    private final ModelPart upper_body_parts8;
    private final ModelPart ring_center;
    private final ModelPart upper_body_parts7;
    private final ModelPart upper_body_parts6;
    private final ModelPart upper_body_parts5;
    private final ModelPart upper_body_parts4;
    private final ModelPart ring_up;
    private final ModelPart upper_body_parts3;
    private final ModelPart upper_body_parts2;
    private final ModelPart upper_body_parts1;
    private final ModelPart upper_body_parts0;

    public SoulInfuserModel(ModelPart root) {
        this.all = root.getChild("all");
        this.head = this.all.getChild("head");
        this.base = this.all.getChild("base");
        this.ring_down = this.base.getChild("ring_down");
        this.upper_body_parts11 = this.ring_down.getChild("upper_body_parts11");
        this.upper_body_parts10 = this.ring_down.getChild("upper_body_parts10");
        this.upper_body_parts9 = this.ring_down.getChild("upper_body_parts9");
        this.upper_body_parts8 = this.ring_down.getChild("upper_body_parts8");
        this.ring_center = this.base.getChild("ring_center");
        this.upper_body_parts7 = this.ring_center.getChild("upper_body_parts7");
        this.upper_body_parts6 = this.ring_center.getChild("upper_body_parts6");
        this.upper_body_parts5 = this.ring_center.getChild("upper_body_parts5");
        this.upper_body_parts4 = this.ring_center.getChild("upper_body_parts4");
        this.ring_up = this.all.getChild("ring_up");
        this.upper_body_parts3 = this.ring_up.getChild("upper_body_parts3");
        this.upper_body_parts2 = this.ring_up.getChild("upper_body_parts2");
        this.upper_body_parts1 = this.ring_up.getChild("upper_body_parts1");
        this.upper_body_parts0 = this.ring_up.getChild("upper_body_parts0");
    }



    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition all = partdefinition.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition head = all.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 22).addBox(-7.0F, -9.0F, -7.0F, 14.0F, 16.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(0, 52).addBox(-5.0F, -9.0F, -5.0F, 10.0F, 14.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(20, 52).addBox(-5.0F, -4.1F, -5.0F, 10.0F, 0.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -9.0F, 0.0F));

        PartDefinition base = all.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, -1.0F, -9.0F, 18.0F, 4.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, 0.0F));

        PartDefinition ring_down = base.addOrReplaceChild("ring_down", CubeListBuilder.create(), PartPose.offset(0.0F, 5.0F, 0.0F));

        PartDefinition upper_body_parts11 = ring_down.addOrReplaceChild("upper_body_parts11", CubeListBuilder.create().texOffs(0, 116).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 0.0F, -8.0F));

        PartDefinition upper_body_parts10 = ring_down.addOrReplaceChild("upper_body_parts10", CubeListBuilder.create().texOffs(0, 104).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-8.0F, 0.0F, 8.0F));

        PartDefinition upper_body_parts9 = ring_down.addOrReplaceChild("upper_body_parts9", CubeListBuilder.create().texOffs(0, 92).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-8.0F, 0.0F, -8.0F));

        PartDefinition upper_body_parts8 = ring_down.addOrReplaceChild("upper_body_parts8", CubeListBuilder.create().texOffs(0, 80).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 0.0F, 8.0F));

        PartDefinition ring_center = base.addOrReplaceChild("ring_center", CubeListBuilder.create(), PartPose.offset(0.0F, 5.0F, 0.0F));

        PartDefinition upper_body_parts7 = ring_center.addOrReplaceChild("upper_body_parts7", CubeListBuilder.create().texOffs(32, 116).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -8.0F));

        PartDefinition upper_body_parts6 = ring_center.addOrReplaceChild("upper_body_parts6", CubeListBuilder.create().texOffs(32, 104).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 8.0F));

        PartDefinition upper_body_parts5 = ring_center.addOrReplaceChild("upper_body_parts5", CubeListBuilder.create().texOffs(32, 92).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-8.0F, 0.0F, 0.0F));

        PartDefinition upper_body_parts4 = ring_center.addOrReplaceChild("upper_body_parts4", CubeListBuilder.create().texOffs(32, 80).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 0.0F, 0.0F));

        PartDefinition ring_up = all.addOrReplaceChild("ring_up", CubeListBuilder.create(), PartPose.offset(0.0F, -10.0F, 0.0F));

        PartDefinition upper_body_parts3 = ring_up.addOrReplaceChild("upper_body_parts3", CubeListBuilder.create().texOffs(85, 114).addBox(0.5F, -3.0F, 0.5F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(92, 114).addBox(-4.5F, -3.0F, -4.5F, 9.0F, 5.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.5F, 0.0F, -4.5F));

        PartDefinition upper_body_parts2 = ring_up.addOrReplaceChild("upper_body_parts2", CubeListBuilder.create().texOffs(85, 100).addBox(-4.5F, -3.0F, -4.5F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(92, 100).addBox(-4.5F, -3.0F, -4.5F, 9.0F, 5.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(4.5F, 0.0F, 4.5F));

        PartDefinition upper_body_parts1 = ring_up.addOrReplaceChild("upper_body_parts1", CubeListBuilder.create().texOffs(85, 86).addBox(0.5F, -3.0F, -4.5F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(92, 86).addBox(-4.5F, -3.0F, -4.5F, 9.0F, 5.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.5F, 0.0F, 4.5F));

        PartDefinition upper_body_parts0 = ring_up.addOrReplaceChild("upper_body_parts0", CubeListBuilder.create().texOffs(85, 72).addBox(-4.5F, -3.0F, 0.5F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(92, 72).addBox(-4.5F, -3.0F, -4.5F, 9.0F, 5.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(4.5F, 0.0F, -4.5F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    //用于生物模型渲染
    @Override
    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {

    }

    //用于方块实体渲染
    public void setupAnim(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        all.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public ModelPart getRingUp() {
        return ring_up;
    }

    public ModelPart getRingDown() {
        return ring_down;
    }
}
