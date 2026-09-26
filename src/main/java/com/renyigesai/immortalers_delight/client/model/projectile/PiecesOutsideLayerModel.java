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
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public class PiecesOutsideLayerModel <T extends Entity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation PIECES_OUTSIDE = new ModelLayerLocation(new ResourceLocation(ImmortalersDelightMod.MODID, "pieces_outside"), "main");
    private final ModelPart all;
    private final ModelPart layer;
    private final ModelPart outside;

    public PiecesOutsideLayerModel(ModelPart root) {
        this.all = root.getChild("all");
        this.layer = this.all.getChild("layer");
        this.outside = this.layer.getChild("outside");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition all = partdefinition.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition layer = all.addOrReplaceChild("layer", CubeListBuilder.create().texOffs(-64, 0).addBox(-32.0F, -0.25F, -32.0F, 64.0F, 0.0F, 64.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition outside = layer.addOrReplaceChild("outside", CubeListBuilder.create().texOffs(7, 46).addBox(-29.0F, -57.0F, -12.5F, 0.0F, 57.0F, 25.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.25F, 0.0F));

        PartDefinition cube_r1 = outside.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(8, 46).addBox(-29.0F, -57.0F, -12.0F, 0.0F, 57.0F, 24.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition cube_r2 = outside.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(7, 46).addBox(-29.0F, -57.0F, -12.5F, 0.0F, 57.0F, 25.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition cube_r3 = outside.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(8, 46).addBox(-29.0F, -57.0F, -12.0F, 0.0F, 57.0F, 24.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 2.3562F, 0.0F));

        PartDefinition cube_r4 = outside.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(7, 46).addBox(-29.0F, -57.0F, -12.5F, 0.0F, 57.0F, 25.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        PartDefinition cube_r5 = outside.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(8, 46).addBox(-29.0F, -57.0F, -12.0F, 0.0F, 57.0F, 24.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -2.3562F, 0.0F));

        PartDefinition cube_r6 = outside.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(7, 46).addBox(-29.0F, -57.0F, -12.5F, 0.0F, 57.0F, 25.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition cube_r7 = outside.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(8, 46).addBox(-29.0F, -57.0F, -12.0F, 0.0F, 57.0F, 24.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    //动画效果
    //limbSwing: 判断是否是攻击状态，0为idle，1为attack
    //limbSwingAmount：判断棋子类型，3为瑕，2为白子，1为黑子
    @Override
    public void setupAnim(@NotNull Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.all.getAllParts().forEach(ModelPart::resetPose);
        if (limbSwing > 0) {
            if (limbSwingAmount >= 3) {
                if (entity.tickCount % 5 == 0)System.out.println("setupAnim 为" + entity.getUUID() + "设置攻击动画：" + ageInTicks);
                AnimationHelper coreRotations = new AnimationHelper(this.allRotationAttackXia);
                this.all.xRot = (float) coreRotations.getPositionAtTime(ageInTicks).x() * ((float)Math.PI / 180F);
                this.all.yRot = (float) coreRotations.getPositionAtTime(ageInTicks).y() * ((float)Math.PI / 180F);
                this.all.zRot = (float) coreRotations.getPositionAtTime(ageInTicks).z() * ((float)Math.PI / 180F);
                AnimationHelper coreScales = new AnimationHelper(this.allScaleAttackXia);
                this.all.xScale = (float) coreScales.getPositionAtTime(ageInTicks).x();
                this.all.yScale = (float) coreScales.getPositionAtTime(ageInTicks).y();
                this.all.zScale = (float) coreScales.getPositionAtTime(ageInTicks).z();

                AnimationHelper layerRotations = new AnimationHelper(this.layerRotationAttackXia);
                this.layer.xRot = (float) layerRotations.getPositionAtTime(ageInTicks).x() * ((float)Math.PI / 180F);
                this.layer.yRot = (float) layerRotations.getPositionAtTime(ageInTicks).y() * ((float)Math.PI / 180F);
                this.layer.zRot = (float) layerRotations.getPositionAtTime(ageInTicks).z() * ((float)Math.PI / 180F);
                AnimationHelper layerScales = new AnimationHelper(this.layerScaleAttackXia);
                this.layer.xScale = (float) layerScales.getPositionAtTime(ageInTicks).x();
                this.layer.yScale = (float) layerScales.getPositionAtTime(ageInTicks).y();
                this.layer.zScale = (float) layerScales.getPositionAtTime(ageInTicks).z();

                AnimationHelper outsideScales = new AnimationHelper(this.outsideScaleAttackXia);
                this.outside.xScale = (float) outsideScales.getPositionAtTime(ageInTicks).x();
                this.outside.yScale = (float) outsideScales.getPositionAtTime(ageInTicks).y();
                this.outside.zScale = (float) outsideScales.getPositionAtTime(ageInTicks).z();

            } else if (limbSwingAmount >= 2) {
                AnimationHelper allRotations = new AnimationHelper(this.allRotationAttackWrite);
                this.all.xRot = (float) allRotations.getPositionAtTime(ageInTicks).x() * ((float)Math.PI / 180F);
                this.all.yRot = (float) allRotations.getPositionAtTime(ageInTicks).y() * ((float)Math.PI / 180F);
                this.all.zRot = (float) allRotations.getPositionAtTime(ageInTicks).z() * ((float)Math.PI / 180F);

                AnimationHelper layerScales = new AnimationHelper(this.layerScaleAttackWrite);
                this.layer.xScale = (float) layerScales.getPositionAtTime(ageInTicks).x();
                this.layer.yScale = (float) layerScales.getPositionAtTime(ageInTicks).y();
                this.layer.zScale = (float) layerScales.getPositionAtTime(ageInTicks).z();

                AnimationHelper outsideScales = new AnimationHelper(this.outsideScaleAttackWrite);
                this.outside.xScale = (float) outsideScales.getPositionAtTime(ageInTicks).x();
                this.outside.yScale = (float) outsideScales.getPositionAtTime(ageInTicks).y();
                this.outside.zScale = (float) outsideScales.getPositionAtTime(ageInTicks).z();

            } else if (limbSwingAmount >= 1) {
                AnimationHelper layerScales = new AnimationHelper(this.layerScaleAttackBlack);
                this.layer.xScale = (float) layerScales.getPositionAtTime(ageInTicks).x();
                this.layer.yScale = (float) layerScales.getPositionAtTime(ageInTicks).y();
                this.layer.zScale = (float) layerScales.getPositionAtTime(ageInTicks).z();

                AnimationHelper outsidePositions = new AnimationHelper(this.outsidePositionAttackBlack);
                this.outside.x = (float) outsidePositions.getPositionAtTime(ageInTicks).x() * -1;
                this.outside.y = (float) outsidePositions.getPositionAtTime(ageInTicks).y() * -1;
                this.outside.z = (float) outsidePositions.getPositionAtTime(ageInTicks).z();
                AnimationHelper outsideScales = new AnimationHelper(this.outsideScaleAttackBlack);
                this.outside.xScale = (float) outsideScales.getPositionAtTime(ageInTicks).x();
                this.outside.yScale = (float) outsideScales.getPositionAtTime(ageInTicks).y();
                this.outside.zScale = (float) outsideScales.getPositionAtTime(ageInTicks).z();

            }
        } else {
            if (limbSwingAmount >= 3) {
                AnimationHelper coreRotations = new AnimationHelper(this.allRotationIdleXia);
                this.all.xRot = (float) coreRotations.getPositionAtTime(ageInTicks).x() * ((float)Math.PI / 180F);
                this.all.yRot = (float) coreRotations.getPositionAtTime(ageInTicks).y() * ((float)Math.PI / 180F);
                this.all.zRot = (float) coreRotations.getPositionAtTime(ageInTicks).z() * ((float)Math.PI / 180F);

            } else if (limbSwingAmount >= 2) {
                AnimationHelper coreRotations = new AnimationHelper(this.allRotationIdle);
                this.all.xRot = (float) coreRotations.getPositionAtTime(ageInTicks).x() * ((float)Math.PI / 180F);
                this.all.yRot = (float) coreRotations.getPositionAtTime(ageInTicks).y() * ((float)Math.PI / 180F);
                this.all.zRot = (float) coreRotations.getPositionAtTime(ageInTicks).z() * ((float)Math.PI / 180F);

            } else if (limbSwingAmount >= 1) {
                AnimationHelper coreRotations = new AnimationHelper(this.allRotationIdle);
                this.all.xRot = (float) coreRotations.getPositionAtTime(ageInTicks).x() * ((float)Math.PI / 180F);
                this.all.yRot = (float) coreRotations.getPositionAtTime(ageInTicks).y() * ((float)Math.PI / 180F);
                this.all.zRot = (float) coreRotations.getPositionAtTime(ageInTicks).z() * ((float)Math.PI / 180F);

            }
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        all.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }


    //黑白子通用动画
    Map<Integer, Vec3> allRotationIdle = Map.of(
            0, new Vec3(0.0F, 0.0F, 0.0F),
            100, new Vec3(0.0F, -360.0F, 0.0F)
    );

    //黑子动画

    Map<Integer, Vec3> layerScaleAttackBlack= Map.of(
            9, new Vec3(1.0F, 1.0F, 1.0F),
            13, new Vec3(0.0F, 0.0F, 0.0F),
            16, new Vec3(0.0F, 0.0F, 0.0F),
            19, new Vec3(0.675F, 1.0F, 0.675F),
            22, new Vec3(0.9F, 1.0F, 0.9F),
            23, new Vec3(0.9F, 1.0F, 0.9F),
            25, new Vec3(1.0F, 1.0F, 1.0F),
            27, new Vec3(1.2F, 1.0F, 1.2F),
            29, new Vec3(1.41F, 1.0F, 1.41F),
            31, new Vec3(1.5F, 1.0F, 1.5F)
    );

    Map<Integer, Vec3> outsidePositionAttackBlack = Map.of(
            0, new Vec3(0.0F, 0.0F, 0.0F),
            23, new Vec3(0.0F, 0.0F, 0.0F),
            25, new Vec3(0.0F, 8.55F, 0.0F),
            27, new Vec3(0.0F, 28.5F, 0.0F),
            29, new Vec3(0.0F, 48.5F, 0.0F),
            31, new Vec3(0.0F, 57.0F, 0.0F)
    );

    Map<Integer, Vec3> outsideScaleAttackBlack = Map.of(
            0, new Vec3(0.0F, 0.0F, 0.0F),
            16, new Vec3(0.0F, 0.0F, 0.0F),
            19, new Vec3(1.0F, 0.75F, 1.0F),
            22, new Vec3(1.0F, 1.0F, 1.0F),
            23, new Vec3(1.0F, 1.0F, 1.0F),
            25, new Vec3(1.0F, 0.85F, 1.0F),
            27, new Vec3(1.0F, 0.5F, 1.0F),
            29, new Vec3(1.0F, 0.15F, 1.0F),
            31, new Vec3(1.0F, 0.0F, 1.0F),
            35, new Vec3(1.0F, 0.0F, 1.0F)
    );

    //白子动画

    Map<Integer, Vec3> allRotationAttackWrite = Map.of(
            0, new Vec3(0.0F, 0.0F, 0.0F),
            11, new Vec3(0.0F, 0.0F, 0.0F),
            28, new Vec3(0.0F, -360.0F, 0.0F),
            30, new Vec3(0.0F, 0.0F, 0.0F)
    );

    Map<Integer, Vec3> layerScaleAttackWrite = Map.of(
            6, new Vec3(1.0F, 1.0F, 1.0F),
            9, new Vec3(0.0F, 0.0F, 0.0F),
            11, new Vec3(0.0F, 0.0F, 0.0F),
            15, new Vec3(0.45F, 1.0F, 0.45F),
            18, new Vec3(0.6F, 1.0F, 0.6F),
            20, new Vec3(0.6F, 1.0F, 0.6F),
            22, new Vec3(0.66F, 1.0F, 0.66F),
            24, new Vec3(0.8F, 1.0F, 0.8F),
            26, new Vec3(0.94F, 1.0F, 0.94F),
            28, new Vec3(1.0F, 1.0F, 1.0F)
    );

    Map<Integer, Vec3> outsideScaleAttackWrite = Map.of(
            0, new Vec3(0.0F, 0.0F, 0.0F),
            11, new Vec3(0.0F, 0.0F, 0.0F),
            15, new Vec3(1.0F, 0.75F, 1.0F),
            18, new Vec3(1.0F, 1.0F, 1.0F),
            20, new Vec3(1.0F, 1.0F, 1.0F),
            22, new Vec3(1.0F, 0.85F, 1.0F),
            24, new Vec3(1.0F, 0.5F, 1.0F),
            26, new Vec3(1.0F, 0.15F, 1.0F),
            28, new Vec3(1.0F, 0.0F, 1.0F),
            30, new Vec3(1.0F, 0.0F, 1.0F)
    );

    //"瑕"动画

    Map<Integer, Vec3> allRotationIdleXia = Map.of(
            0, new Vec3(0.0F, 0.0F, 0.0F),
            100, new Vec3(0.0F, -360.0F, 0.0F)
    );

    Map<Integer, Vec3> allRotationAttackXia = Map.of(
            0, new Vec3(0.0F, 0.0F, 0.0F),
            21, new Vec3(0.0F, -180.0F, 0.0F),
            38, new Vec3(0.0F, -360.0F, 0.0F)
    );

    Map<Integer, Vec3> layerRotationAttackXia = Map.of(
            0, new Vec3(0.0F, 0.0F, 0.0F),
            21, new Vec3(0.0F, -180.0F, 0.0F),
            38, new Vec3(0.0F, -360.0F, 0.0F)
    );

    Map<Integer, Vec3> allScaleAttackXia = Map.of(
            0, new Vec3(1.0F, 1.0F, 1.0F),
            5, new Vec3(1.556F, 1.556F, 1.556F),
            9, new Vec3(1.889F, 1.889F, 1.889F),
            13, new Vec3(2.0F, 2.0F, 2.0F),
            17, new Vec3(1.75F, 1.75F, 1.75F),
            21, new Vec3(1.0F, 1.0F, 1.0F),
            26, new Vec3(1.0F, 1.0F, 1.0F),
            34, new Vec3(0.25F, 0.25F, 0.25F),
            38, new Vec3(0.0F, 0.0F, 0.0F)
    );

    Map<Integer, Vec3> layerScaleAttackXia = Map.of(
            0, new Vec3(1.0F, 1.0F, 1.0F),
            3, new Vec3(0.623F, 1.0F, 0.623F),
            6, new Vec3(0.4F, 1.0F, 0.4F),
            10, new Vec3(0.33F, 1.0F, 0.33F),
            15, new Vec3(0.33F, 1.0F, 0.33F),
            17, new Vec3(0.495F, 1.0F, 0.495F),
            19, new Vec3(1.0F, 1.0F, 1.0F),
            21, new Vec3(1.5F, 1.0F, 1.5F)
    );

    Map<Integer, Vec3> outsideScaleAttackXia = Map.of(
            0, new Vec3(1.0F, 1.0F, 1.0F),
            3, new Vec3(1.0F, 1.2F, 1.0F),
            6, new Vec3(1.0F, 1.85F, 1.0F),
            10, new Vec3(1.0F, 3.0F, 1.0F),
            15, new Vec3(1.0F, 3.0F, 1.0F),
            17, new Vec3(1.0F, 2.5F, 1.0F),
            19, new Vec3(1.0F, 1.0F, 1.0F),
            21, new Vec3(1.0F, 0.0F, 1.0F)
    );
}