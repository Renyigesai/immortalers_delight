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

import java.util.HashMap;
import java.util.Map;

public class PiecesCoreModel <T extends Entity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation PIECES_CORE = new ModelLayerLocation(new ResourceLocation(ImmortalersDelightMod.MODID, "pieces_core"), "main");
    private final ModelPart root;
    private final ModelPart core;

    public PiecesCoreModel(ModelPart root) {
        this.root = root.getChild("root");
        this.core = this.root.getChild("core");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition core = root.addOrReplaceChild("core", CubeListBuilder.create().texOffs(0, 40).addBox(-8.0F, -8.0F, -8.0F, 16.0F, 8.0F, 16.0F, new CubeDeformation(-0.5F))
                .texOffs(64, 40).addBox(8.0F, 0.0F, 8.0F, -16.0F, -24.0F, -16.0F, new CubeDeformation(-0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    //动画效果
    //limbSwing: 判断是否是攻击状态，0为idle，1为attack
    //limbSwingAmount：判断棋子类型，3为瑕，2为白子，1为黑子
    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root.getAllParts().forEach(ModelPart::resetPose);
        if (limbSwing > 0) {
            if (limbSwingAmount >= 3) {
                AnimationHelper coreRotations = new AnimationHelper(this.coreRotationAttackXia);
                this.core.xRot = (float) coreRotations.getPositionAtTime(ageInTicks).x() * ((float)Math.PI / 180F);
                this.core.yRot = (float) coreRotations.getPositionAtTime(ageInTicks).y() * ((float)Math.PI / 180F);
                this.core.zRot = (float) coreRotations.getPositionAtTime(ageInTicks).z() * ((float)Math.PI / 180F);
                AnimationHelper coreScales = new AnimationHelper(this.coreScaleAttackXia);
                this.core.xScale = (float) coreScales.getPositionAtTime(ageInTicks).x();
                this.core.yScale = (float) coreScales.getPositionAtTime(ageInTicks).y();
                this.core.zScale = (float) coreScales.getPositionAtTime(ageInTicks).z();

            } else if (limbSwingAmount >= 2) {
                AnimationHelper coreRotations = new AnimationHelper(this.coreRotationAttackWrite);
                this.core.xRot = (float) coreRotations.getPositionAtTime(ageInTicks).x() * ((float)Math.PI / 180F);
                this.core.yRot = (float) coreRotations.getPositionAtTime(ageInTicks).y() * ((float)Math.PI / 180F);
                this.core.zRot = (float) coreRotations.getPositionAtTime(ageInTicks).z() * ((float)Math.PI / 180F);
                AnimationHelper coreScales = new AnimationHelper(this.coreScaleAttackWrite);
                this.core.xScale = (float) coreScales.getPositionAtTime(ageInTicks).x();
                this.core.yScale = (float) coreScales.getPositionAtTime(ageInTicks).y();
                this.core.zScale = (float) coreScales.getPositionAtTime(ageInTicks).z();

            } else if (limbSwingAmount >= 1) {
                AnimationHelper coreRotations = new AnimationHelper(this.coreRotationAttackBlack);
                this.core.xRot = (float) coreRotations.getPositionAtTime(ageInTicks).x() * ((float)Math.PI / 180F);
                this.core.yRot = (float) coreRotations.getPositionAtTime(ageInTicks).y() * ((float)Math.PI / 180F);
                this.core.zRot = (float) coreRotations.getPositionAtTime(ageInTicks).z() * ((float)Math.PI / 180F);
                AnimationHelper coreScales = new AnimationHelper(this.coreScaleAttackBlack);
                this.core.xScale = (float) coreScales.getPositionAtTime(ageInTicks).x();
                this.core.yScale = (float) coreScales.getPositionAtTime(ageInTicks).y();
                this.core.zScale = (float) coreScales.getPositionAtTime(ageInTicks).z();

            }
        } else {
            if (limbSwingAmount >= 3) {
                HashMap<Integer,Vec3> corePositionIdle = new HashMap<>();
                corePositionIdle.putAll(this.corePositionIdleXia1);
                corePositionIdle.putAll(this.corePositionIdleXia2);

                AnimationHelper corePositions = new AnimationHelper(corePositionIdle);
                this.core.x = (float) corePositions.getPositionAtTime(ageInTicks).x() * -1;
                this.core.y = (float) corePositions.getPositionAtTime(ageInTicks).y() * -1;
                this.core.z = (float) corePositions.getPositionAtTime(ageInTicks).z();
                AnimationHelper coreRotations = new AnimationHelper(this.coreRotationIdleXia);
                this.core.xRot = (float) coreRotations.getPositionAtTime(ageInTicks).x() * ((float)Math.PI / 180F);
                this.core.yRot = (float) coreRotations.getPositionAtTime(ageInTicks).y() * ((float)Math.PI / 180F);
                this.core.zRot = (float) coreRotations.getPositionAtTime(ageInTicks).z() * ((float)Math.PI / 180F);
                AnimationHelper coreScales = new AnimationHelper(this.coreScaleIdleXia);
                this.core.xScale = (float) coreScales.getPositionAtTime(ageInTicks).x();
                this.core.yScale = (float) coreScales.getPositionAtTime(ageInTicks).y();
                this.core.zScale = (float) coreScales.getPositionAtTime(ageInTicks).z();

            } else if (limbSwingAmount >= 2) {
                HashMap<Integer,Vec3> corePositionIdle = new HashMap<>();
                corePositionIdle.putAll(this.corePositionIdle1);
                corePositionIdle.putAll(this.corePositionIdle2);

                AnimationHelper corePositions = new AnimationHelper(corePositionIdle);
                this.core.x = (float) corePositions.getPositionAtTime(ageInTicks).x() * -1;
                this.core.y = (float) corePositions.getPositionAtTime(ageInTicks).y() * -1;
                this.core.z = (float) corePositions.getPositionAtTime(ageInTicks).z();
                AnimationHelper coreRotations = new AnimationHelper(this.coreRotationIdle);
                this.core.xRot = (float) coreRotations.getPositionAtTime(ageInTicks).x() * ((float)Math.PI / 180F);
                this.core.yRot = (float) coreRotations.getPositionAtTime(ageInTicks).y() * ((float)Math.PI / 180F);
                this.core.zRot = (float) coreRotations.getPositionAtTime(ageInTicks).z() * ((float)Math.PI / 180F);
                AnimationHelper coreScales = new AnimationHelper(this.coreScaleIdle);
                this.core.xScale = (float) coreScales.getPositionAtTime(ageInTicks).x();
                this.core.yScale = (float) coreScales.getPositionAtTime(ageInTicks).y();
                this.core.zScale = (float) coreScales.getPositionAtTime(ageInTicks).z();

            } else if (limbSwingAmount >= 1) {
                HashMap<Integer,Vec3> corePositionIdle = new HashMap<>();
                corePositionIdle.putAll(this.corePositionIdle1);
                corePositionIdle.putAll(this.corePositionIdle2);

                AnimationHelper corePositions = new AnimationHelper(corePositionIdle);
                this.core.x = (float) corePositions.getPositionAtTime(ageInTicks).x() * -1;
                this.core.y = (float) corePositions.getPositionAtTime(ageInTicks).y() * -1;
                this.core.z = (float) corePositions.getPositionAtTime(ageInTicks).z();
                AnimationHelper coreRotations = new AnimationHelper(this.coreRotationIdle);
                this.core.xRot = (float) coreRotations.getPositionAtTime(ageInTicks).x() * ((float)Math.PI / 180F);
                this.core.yRot = (float) coreRotations.getPositionAtTime(ageInTicks).y() * ((float)Math.PI / 180F);
                this.core.zRot = (float) coreRotations.getPositionAtTime(ageInTicks).z() * ((float)Math.PI / 180F);
                AnimationHelper coreScales = new AnimationHelper(this.coreScaleIdle);
                this.core.xScale = (float) coreScales.getPositionAtTime(ageInTicks).x();
                this.core.yScale = (float) coreScales.getPositionAtTime(ageInTicks).y();
                this.core.zScale = (float) coreScales.getPositionAtTime(ageInTicks).z();
            }
        }

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    //黑白子通用动画

    Map<Integer, Vec3> coreRotationIdle = Map.of(
            0, new Vec3(0.0F, 0.0F, 0.0F),
            100, new Vec3(0.0F, -360.0F, 0.0F)
    );

    Map<Integer, Vec3> corePositionIdle1 = Map.of(
            0, new Vec3(0.0F, 2.0F, 0.0F),
            6, new Vec3(0.0F, 1.56F, 0.0F),
            12, new Vec3(0.0F, 1.0F, 0.0F),
            18, new Vec3(0.0F, 0.4F, 0.0F),
            24, new Vec3(0.0F, 0.0F, 0.0F),
            25, new Vec3(0.0F, 0.0F, 0.0F),
            31, new Vec3(0.0F, 0.41F, 0.0F),
            37, new Vec3(0.0F, 1.0F, 0.0F),
            43, new Vec3(0.0F, 1.54F, 0.0F),
            49, new Vec3(0.0F, 2.0F, 0.0F)
    );
    Map<Integer, Vec3> corePositionIdle2 = Map.of(
            50, new Vec3(0.0F, 2.0F, 0.0F),
            56, new Vec3(0.0F, 1.56F, 0.0F),
            62, new Vec3(0.0F, 1.0F, 0.0F),
            68, new Vec3(0.0F, 0.4F, 0.0F),
            74, new Vec3(0.0F, 0.0F, 0.0F),
            75, new Vec3(0.0F, 0.0F, 0.0F),
            81, new Vec3(0.0F, 0.41F, 0.0F),
            87, new Vec3(0.0F, 1.0F, 0.0F),
            93, new Vec3(0.0F, 1.54F, 0.0F),
            99, new Vec3(0.0F, 2.0F, 0.0F)
    );

    Map<Integer, Vec3> coreScaleIdle = Map.of(
            0, new Vec3(0.5F, 0.5F, 0.5F),
            100, new Vec3(0.5F, 0.5F, 0.5F)
    );

    //黑子动画

    Map<Integer, Vec3> coreRotationAttackBlack = Map.of(
            0, new Vec3(0.0F, 0.0F, 0.0F),
            18, new Vec3(0.0F, -180.0F, 0.0F),
            35, new Vec3(0.0F, -360.0F, 0.0F)
    );

    Map<Integer, Vec3> coreScaleAttackBlack = Map.of(
            0, new Vec3(0.5F, 0.5F, 0.5F),
            4, new Vec3(0.778F, 0.778F, 0.778F),
            8, new Vec3(0.945F, 0.945F, 0.945F),
            12, new Vec3(1.0F, 1.0F, 1.0F),
            15, new Vec3(0.875F, 0.875F, 0.875F),
            18, new Vec3(0.5F, 0.5F, 0.5F),
            23, new Vec3(0.5F, 0.5F, 0.5F),
            31, new Vec3(0.125F, 0.125F, 0.125F),
            35, new Vec3(0.0F, 0.0F, 0.0F)
    );
    //白子动画

    Map<Integer, Vec3> coreRotationAttackWrite = Map.of(
            0, new Vec3(0.0F, 0.0F, 0.0F),
            12, new Vec3(0.0F, -180.0F, 0.0F),
            30, new Vec3(0.0F, -360.0F, 0.0F)
    );
    Map<Integer, Vec3> coreScaleAttackWrite = Map.of(
            0, new Vec3(0.5F, 0.5F, 0.5F),
            3, new Vec3(0.778F, 0.778F, 0.778F),
            5, new Vec3(0.945F, 0.945F, 0.945F),
            7, new Vec3(1.0F, 1.0F, 1.0F),
            9, new Vec3(0.875F, 0.875F, 0.875F),
            12, new Vec3(0.5F, 0.5F, 0.5F),
            20, new Vec3(0.5F, 0.5F, 0.5F),
            28, new Vec3(0.125F, 0.125F, 0.125F),
            30, new Vec3(0.0F, 0.0F, 0.0F)
    );

    //"瑕"动画
    Map<Integer, Vec3> coreRotationIdleXia = Map.of(
            0, new Vec3(0.0F, 0.0F, 0.0F),
            100, new Vec3(0.0F, -360.0F, 0.0F)
    );

    Map<Integer, Vec3> corePositionIdleXia1 = Map.of(
            0, new Vec3(0.0F, 2.0F, 0.0F),
            6, new Vec3(0.0F, 1.56F, 0.0F),
            12, new Vec3(0.0F, 1.0F, 0.0F),
            18, new Vec3(0.0F, 0.4F, 0.0F),
            24, new Vec3(0.0F, 0.0F, 0.0F),
            25, new Vec3(0.0F, 0.0F, 0.0F),
            31, new Vec3(0.0F, 0.41F, 0.0F),
            37, new Vec3(0.0F, 1.0F, 0.0F),
            43, new Vec3(0.0F, 1.54F, 0.0F),
            49, new Vec3(0.0F, 2.0F, 0.0F)
    );
    Map<Integer, Vec3> corePositionIdleXia2 = Map.of(
            50, new Vec3(0.0F, 2.0F, 0.0F),
            56, new Vec3(0.0F, 1.56F, 0.0F),
            62, new Vec3(0.0F, 1.0F, 0.0F),
            68, new Vec3(0.0F, 0.4F, 0.0F),
            74, new Vec3(0.0F, 0.0F, 0.0F),
            75, new Vec3(0.0F, 0.0F, 0.0F),
            81, new Vec3(0.0F, 0.41F, 0.0F),
            87, new Vec3(0.0F, 1.0F, 0.0F),
            93, new Vec3(0.0F, 1.54F, 0.0F),
            99, new Vec3(0.0F, 2.0F, 0.0F)
    );

    Map<Integer, Vec3> coreScaleIdleXia = Map.of(
            0, new Vec3(1.0F, 1.0F, 1.0F),
            100, new Vec3(1.0F, 1.0F, 1.0F)
    );

    Map<Integer, Vec3> coreRotationAttackXia = Map.of(
            0, new Vec3(0.0F, 0.0F, 0.0F),
            21, new Vec3(0.0F, -180.0F, 0.0F),
            38, new Vec3(0.0F, -360.0F, 0.0F)
    );

    Map<Integer, Vec3> coreScaleAttackXia = Map.of(
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
}