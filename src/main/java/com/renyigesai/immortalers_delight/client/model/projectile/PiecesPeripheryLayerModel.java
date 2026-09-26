package com.renyigesai.immortalers_delight.client.model.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.client.AnimationHelper;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class PiecesPeripheryLayerModel <T extends Entity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation XIA_PIECES_LAYER = new ModelLayerLocation(new ResourceLocation(ImmortalersDelightMod.MODID, "xia_pieces_layer"), "main");
    private final ModelPart root;
    private final ModelPart base;
    private final ModelPart periphery;

    public PiecesPeripheryLayerModel(ModelPart root) {
        this.root = root.getChild("root");
        this.base = this.root.getChild("base");
        this.periphery = this.root.getChild("periphery");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition base = root.addOrReplaceChild("base", CubeListBuilder.create().texOffs(-192, 64).addBox(-96.0F, 0.0F, -96.0F, 192.0F, 0.0F, 192.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition periphery = root.addOrReplaceChild("periphery", CubeListBuilder.create().texOffs(0, -32).addBox(-80.0F, -64.0F, -16.0F, 0.0F, 64.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = periphery.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(192, -32).addBox(-80.0F, -64.0F, -16.0F, 0.0F, 64.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition cube_r2 = periphery.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(192, -32).addBox(-80.0F, -64.0F, -16.0F, 0.0F, 64.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.1781F, 0.0F));

        PartDefinition cube_r3 = periphery.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(192, -32).addBox(-80.0F, -64.0F, -16.0F, 0.0F, 64.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition cube_r4 = periphery.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(192, -32).addBox(-80.0F, -64.0F, -16.0F, 0.0F, 64.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3927F, 0.0F));

        PartDefinition cube_r5 = periphery.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(128, -32).addBox(-80.0F, -64.0F, -16.0F, 0.0F, 64.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.9635F, 0.0F));

        PartDefinition cube_r6 = periphery.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(128, -32).addBox(-80.0F, -64.0F, -16.0F, 0.0F, 64.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 2.3562F, 0.0F));

        PartDefinition cube_r7 = periphery.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(128, -32).addBox(-80.0F, -64.0F, -16.0F, 0.0F, 64.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 2.7489F, 0.0F));

        PartDefinition cube_r8 = periphery.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(128, -32).addBox(-80.0F, -64.0F, -16.0F, 0.0F, 64.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        PartDefinition cube_r9 = periphery.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(64, -32).addBox(-80.0F, -64.0F, -16.0F, 0.0F, 64.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition cube_r10 = periphery.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(64, -32).addBox(-80.0F, -64.0F, -16.0F, 0.0F, 64.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.9635F, 0.0F));

        PartDefinition cube_r11 = periphery.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(64, -32).addBox(-80.0F, -64.0F, -16.0F, 0.0F, 64.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -2.3562F, 0.0F));

        PartDefinition cube_r12 = periphery.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(64, -32).addBox(-80.0F, -64.0F, -16.0F, 0.0F, 64.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -2.7489F, 0.0F));

        PartDefinition cube_r13 = periphery.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(0, -32).addBox(-80.0F, -64.0F, -16.0F, 0.0F, 64.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.1781F, 0.0F));

        PartDefinition cube_r14 = periphery.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(0, -32).addBox(-80.0F, -64.0F, -16.0F, 0.0F, 64.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition cube_r15 = periphery.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(0, -32).addBox(-80.0F, -64.0F, -16.0F, 0.0F, 64.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));

        return LayerDefinition.create(meshdefinition, 512, 256);
    }

    //动画效果
    //limbSwing: 判断是否是攻击状态，0为idle，1为attack
    //limbSwingAmount：判断棋子类型，3为瑕，2为白子，1为黑子
    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root.getAllParts().forEach(ModelPart::resetPose);
        if (limbSwing > 0) {
            AnimationHelper coreRotations = new AnimationHelper(this.rootRotationAttackXia);
            this.root.xRot = (float) coreRotations.getPositionAtTime(ageInTicks).x() * ((float)Math.PI / 180F);
            this.root.yRot = (float) coreRotations.getPositionAtTime(ageInTicks).y() * ((float)Math.PI / 180F);
            this.root.zRot = (float) coreRotations.getPositionAtTime(ageInTicks).z() * ((float)Math.PI / 180F);
            AnimationHelper baseScales = new AnimationHelper(this.baseScaleAttackXia);
            this.base.xScale = (float) baseScales.getPositionAtTime(ageInTicks).x();
            this.base.yScale = (float) baseScales.getPositionAtTime(ageInTicks).y();
            this.base.zScale = (float) baseScales.getPositionAtTime(ageInTicks).z();

            AnimationHelper coreScales = new AnimationHelper(this.peripheryScaleAttackXia);
            this.periphery.xScale = (float) coreScales.getPositionAtTime(ageInTicks).x();
            this.periphery.yScale = (float) coreScales.getPositionAtTime(ageInTicks).y();
            this.periphery.zScale = (float) coreScales.getPositionAtTime(ageInTicks).z();

//            if (limbSwingAmount >= 3) {
//
//            } else if (limbSwingAmount >= 2) {
//
//            } else if (limbSwingAmount >= 1) {
//
//            }
        } else {
            AnimationHelper coreRotations = new AnimationHelper(this.rootRotationIdleXia);
            this.root.xRot = (float) coreRotations.getPositionAtTime(ageInTicks).x() * ((float)Math.PI / 180F);
            this.root.yRot = (float) coreRotations.getPositionAtTime(ageInTicks).y() * ((float)Math.PI / 180F);
            this.root.zRot = (float) coreRotations.getPositionAtTime(ageInTicks).z() * ((float)Math.PI / 180F);

//            if (limbSwingAmount >= 3) {
//
//            } else if (limbSwingAmount >= 2) {
//
//            } else if (limbSwingAmount >= 1) {
//
//            }
        }

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }


    //黑白子通用动画
    //黑子动画
    //白子动画
    //"瑕"动画

    Map<Integer, Vec3> rootRotationIdleXia = Map.of(
            0, new Vec3(0.0F, 0.0F, 0.0F),
            100, new Vec3(0.0F, -360.0F, 0.0F)
    );

    Map<Integer, Vec3> rootRotationAttackXia = Map.of(
            0, new Vec3(0.0F, 0.0F, 0.0F),
            19, new Vec3(0.0F, 0.0F, 0.0F),
            34, new Vec3(0.0F, -360.0F, 0.0F),
            38, new Vec3(0.0F, 0.0F, 0.0F)
    );

    Map<Integer, Vec3> peripheryScaleAttackXia = Map.of(
            0, new Vec3(0.0F, 0.0F, 0.0F),
            19, new Vec3(0.45F, 0.75F, 0.45F),
            22, new Vec3(0.6F, 1.0F, 0.6F),
            25, new Vec3(0.6F, 1.0F, 0.6F),
            26, new Vec3(0.66F, 0.85F, 0.66F),
            28, new Vec3(0.8F, 0.5F, 0.8F),
            30, new Vec3(0.94F, 0.15F, 0.94F),
            32, new Vec3(1.0F, 0.0F, 1.0F),
            34, new Vec3(1.0F, 0.0F, 1.0F),
            38, new Vec3(1.0F, 0.0F, 1.0F)
    );

    Map<Integer, Vec3> baseScaleAttackXia = Map.of(
            10, new Vec3(1.0F, 1.0F, 1.0F),
            15, new Vec3(0.0F, 0.0F, 0.0F),
            19, new Vec3(0.0F, 0.0F, 0.0F),
            22, new Vec3(0.45F, 1.0F, 0.45F),
            25, new Vec3(0.6F, 1.0F, 0.6F),
            26, new Vec3(0.6F, 1.0F, 0.6F),
            28, new Vec3(0.66F, 1.0F, 0.66F),
            30, new Vec3(0.8F, 1.0F, 0.8F),
            32, new Vec3(0.94F, 1.0F, 0.94F),
            34, new Vec3(1.0F, 1.0F, 1.0F)
    );
}
