package com.renyigesai.immortalers_delight.client.model;

import com.renyigesai.immortalers_delight.client.animation.StrangeArmourStandAnimation;
import com.renyigesai.immortalers_delight.entities.living.StrangeArmourStand;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class StrangeArmourStandModel<T extends Entity> extends HierarchicalModel<T> {
    public static final ModelLayerLocation STRANGE_ARMOUR_STAND = new ModelLayerLocation(new ResourceLocation(ImmortalersDelightMod.MODID, "strange_armour_stand"), "main");
    private final ModelPart Body;
    private final ModelPart Waist;
    private final ModelPart Head;
    private final ModelPart LeftArm;
    private final ModelPart leftItem;
    private final ModelPart LeftLeg;
    private final ModelPart RightArm;
    private final ModelPart Sword;
    private final ModelPart rightItem;
    private final ModelPart RightLeg;
    private final ModelPart Baseplate;

    public StrangeArmourStandModel(ModelPart root) {
        this.Body = root.getChild("Body");
        this.Waist = this.Body.getChild("Waist");
        this.Head = this.Body.getChild("Head");
        this.LeftArm = this.Body.getChild("LeftArm");
        this.leftItem = this.LeftArm.getChild("leftItem");
        this.LeftLeg = this.Body.getChild("LeftLeg");
        this.RightArm = this.Body.getChild("RightArm");
        this.Sword = this.RightArm.getChild("Sword");
        this.rightItem = this.RightArm.getChild("rightItem");
        this.RightLeg = this.Body.getChild("RightLeg");
        this.Baseplate = this.Body.getChild("Baseplate");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(0, 26).addBox(-6.0F, 0.0F, -1.5F, 12.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(16, 0).addBox(-3.0F, 3.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(48, 16).addBox(1.0F, 3.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 48).addBox(-4.0F, 10.0F, -1.0F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(80, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Waist = Body.addOrReplaceChild("Waist", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

        PartDefinition Head = Body.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -7.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(64, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(96, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.25F))
                .texOffs(0, 53).addBox(-2.5F, -5.5F, -2.5F, 5.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition LeftArm = Body.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(32, 16).addBox(0.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(104, 16).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(5.0F, 2.0F, 0.0F));

        PartDefinition leftItem = LeftArm.addOrReplaceChild("leftItem", CubeListBuilder.create(), PartPose.offset(1.0F, 7.0F, 1.0F));

        PartDefinition LeftLeg = Body.addOrReplaceChild("LeftLeg", CubeListBuilder.create().texOffs(40, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(64, 16).addBox(-1.9F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(1.9F, 12.0F, 0.0F));

        PartDefinition RightArm = Body.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(24, 0).addBox(-2.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(96, 48).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

        PartDefinition Sword = RightArm.addOrReplaceChild("Sword", CubeListBuilder.create().texOffs(0, 64).addBox(-0.5F, -1.0F, -26.75F, 1.0F, 6.0F, 19.0F, new CubeDeformation(0.0F))
                .texOffs(0, 64).addBox(-1.0F, -3.0F, -7.75F, 2.0F, 10.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(21, 64).addBox(-1.0F, 1.0F, -5.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(-0.25F))
                .texOffs(21, 77).addBox(-1.0F, 0.0F, 0.75F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 7.0F, 1.0F));

        PartDefinition rightItem = RightArm.addOrReplaceChild("rightItem", CubeListBuilder.create(), PartPose.offset(-1.0F, 7.0F, 1.0F));

        PartDefinition RightLeg = Body.addOrReplaceChild("RightLeg", CubeListBuilder.create().texOffs(8, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(80, 48).addBox(-2.1F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

        PartDefinition Baseplate = Body.addOrReplaceChild("Baseplate", CubeListBuilder.create().texOffs(0, 32).addBox(-6.0F, -1.0F, -6.0F, 12.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public ModelPart root() {
        return Body;
    }

    @Override
    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.animateHeadLookTarget(pNetHeadYaw, pHeadPitch);

        //this.animateWalk(StrangeArmourStandAnimation.WALK, pLimbSwing, pLimbSwingAmount,2f,2.5f);
        this.animate(StrangeArmourStand.idleAnimationState, StrangeArmourStandAnimation.IDLE, 0.5f);
        this.animate(StrangeArmourStand.attackAnimationState, StrangeArmourStandAnimation.ATTACK,pAgeInTicks, 1.0f);
    }

    /*
     * 设置头部的运动。这里使用的是监守者的，如果需要更活泼的头部运动，可以参见骆驼（Camel）的动画。
     */
    private void animateHeadLookTarget(float pYaw, float pPitch) {
        this.Head.xRot = pPitch * ((float)Math.PI / 180F);
        this.Head.yRot = pYaw * ((float)Math.PI / 180F);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        Body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
