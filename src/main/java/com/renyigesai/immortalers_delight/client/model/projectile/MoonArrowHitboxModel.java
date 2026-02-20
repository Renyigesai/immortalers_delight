package com.renyigesai.immortalers_delight.client.model.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.renyigesai.immortalers_delight.client.AnimationHelper;
import com.renyigesai.immortalers_delight.entities.projectile.MoonArrowHitboxEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class MoonArrowHitboxModel<T extends MoonArrowHitboxEntity> extends HierarchicalModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(ImmortalersDelightMod.MODID, "moon_arrow_hitbox"), "main");
    private final ModelPart all;
    private final ModelPart base;
    private final ModelPart edge_left;
    private final ModelPart edge_right;
    private final ModelPart core;
    private final ModelPart center;

    public MoonArrowHitboxModel(ModelPart root) {
        this.all = root.getChild("all");
        this.base = this.all.getChild("base");
        this.edge_left = this.base.getChild("edge_left");
        this.edge_right = this.base.getChild("edge_right");
        this.core = this.all.getChild("core");
        this.center = this.all.getChild("center");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition all = partdefinition.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition base = all.addOrReplaceChild("base", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition edge_left = base.addOrReplaceChild("edge_left", CubeListBuilder.create().texOffs(0, 16).addBox(-3.0F, -12.0F, 15.0F, 6.0F, 12.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 22).addBox(-15.0F, -12.0F, -3.0F, 0.0F, 12.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = edge_left.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(36, 22).addBox(-15.0F, -12.0F, -3.0F, 0.0F, 12.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(36, 16).addBox(-3.0F, -12.0F, 15.0F, 6.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.1781F, 0.0F));

        PartDefinition cube_r2 = edge_left.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(24, 22).addBox(-15.0F, -12.0F, -3.0F, 0.0F, 12.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(24, 16).addBox(-3.0F, -12.0F, 15.0F, 6.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition cube_r3 = edge_left.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(12, 22).addBox(-15.0F, -12.0F, -3.0F, 0.0F, 12.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(12, 16).addBox(-3.0F, -12.0F, 15.0F, 6.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));

        PartDefinition edge_right = base.addOrReplaceChild("edge_right", CubeListBuilder.create().texOffs(0, 40).addBox(-3.0F, -12.0F, 15.0F, 6.0F, 12.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 46).addBox(-15.0F, -12.0F, -3.0F, 0.0F, 12.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        PartDefinition cube_r4 = edge_right.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(36, 46).addBox(-15.0F, -12.0F, -3.0F, 0.0F, 12.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(36, 40).addBox(-3.0F, -12.0F, 15.0F, 6.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.1781F, 0.0F));

        PartDefinition cube_r5 = edge_right.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(24, 46).addBox(-15.0F, -12.0F, -3.0F, 0.0F, 12.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(24, 40).addBox(-3.0F, -12.0F, 15.0F, 6.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition cube_r6 = edge_right.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(12, 46).addBox(-15.0F, -12.0F, -3.0F, 0.0F, 12.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(12, 40).addBox(-3.0F, -12.0F, 15.0F, 6.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));

        PartDefinition core = all.addOrReplaceChild("core", CubeListBuilder.create().texOffs(48, 16).addBox(-6.0F, -24.0F, 0.0F, 6.0F, 24.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(48, 40).addBox(0.0F, -24.0F, 0.0F, 6.0F, 24.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(48, 10).addBox(0.0F, -24.0F, 0.0F, 0.0F, 24.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(48, 34).addBox(0.0F, -24.0F, -6.0F, 0.0F, 24.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition center = all.addOrReplaceChild("center", CubeListBuilder.create().texOffs(60, 15).addBox(-0.5F, -48.0F, -0.5F, 1.0F, 48.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        AnimationHelper centerScales = new AnimationHelper(this.centerScale);
        this.center.xScale = (float) centerScales.getPositionAtTime(ageInTicks).x();
        this.center.yScale = (float) centerScales.getPositionAtTime(ageInTicks).y();
        this.center.zScale = (float) centerScales.getPositionAtTime(ageInTicks).z();
        AnimationHelper baseRotations = new AnimationHelper(this.baseRotation);
        this.base.xRot = (float) baseRotations.getPositionAtTime(ageInTicks).x() * ((float)Math.PI / 180F);
        this.base.yRot = (float) baseRotations.getPositionAtTime(ageInTicks).y() * ((float)Math.PI / 180F);
        this.base.zRot = (float) baseRotations.getPositionAtTime(ageInTicks).z() * ((float)Math.PI / 180F);
        AnimationHelper baseScales = new AnimationHelper(this.baseScale);
        this.base.xScale = (float) baseScales.getPositionAtTime(ageInTicks).x();
        this.base.yScale = (float) baseScales.getPositionAtTime(ageInTicks).y();
        this.base.zScale = (float) baseScales.getPositionAtTime(ageInTicks).z();
        AnimationHelper corePositions = new AnimationHelper(this.corePosition);
        this.core.x = (float) corePositions.getPositionAtTime(ageInTicks).x() * -1;
        this.core.y = (float) corePositions.getPositionAtTime(ageInTicks).y() * -1;
        this.core.z = (float) corePositions.getPositionAtTime(ageInTicks).z();
        AnimationHelper coreScales = new AnimationHelper(this.coreScale);
        this.core.xScale = (float) coreScales.getPositionAtTime(ageInTicks).x();
        this.core.yScale = (float) coreScales.getPositionAtTime(ageInTicks).y();
        this.core.zScale = (float) coreScales.getPositionAtTime(ageInTicks).z();
    }

    public void setHide(boolean hide) {
        this.center.visible = hide;
        this.core.visible = hide;
    }
    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        all.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return all;
    }

    public void translateToRoot(PoseStack pPoseStack) {
        this.root().translateAndRotate(pPoseStack);
    }
    Map<Integer, Vec3> centerScale = Map.of(
            0, new Vec3(2, 1.5, 2),
            9, new Vec3(1.2, 1.8, 1.2),
            10, new Vec3(2.4, 1.85, 2.4),
            12, new Vec3(2.5, 2.4, 2.5),
            15, new Vec3(2.4, 2.2, 2.4),
            25, new Vec3(2, 1.5, 2)
    );
    Map<Integer, Vec3> baseRotation = Map.of(
            0, new Vec3(0, 0, 0),
            9, new Vec3(0, 90, 0),
            12, new Vec3(0, 114, 0),
            15, new Vec3(0, 135, 0),
            18, new Vec3(0, 150, 0),
            21, new Vec3(0, 162, 0),
            30, new Vec3(0, 180, 0)
    );
    Map<Integer, Vec3> baseScale = Map.of(
            0, new Vec3(0.8, 0.4, 0.8),
            9, new Vec3(0.5, 0.25, 0.5),
            10, new Vec3(0.8, 0.72, 0.8),
            12, new Vec3(1.1, 1.1, 1.1),
            15, new Vec3(1.35, 1, 1.35),
            25, new Vec3(1.5, 0.85, 1.5)
    );
    Map<Integer, Vec3> corePosition = Map.of(
            0, new Vec3(0, 0, 0),
            3, new Vec3(0, 100, 0),
            7, new Vec3(0, 50, 0),
            10, new Vec3(0, -12, 0),
            17, new Vec3(0, 0, 0)
    );
    Map<Integer, Vec3> coreScale = Map.of(
            0, new Vec3(0, 0, 0),
            4, new Vec3(1.0, 1.0, 1.0),
            8, new Vec3(1.2, 1.2, 1.2),
            11, new Vec3(1.0, 1.0, 1.0),
            13, new Vec3(0.8, 0.8, 0.8),
            15, new Vec3(0.5, 0.5, 0.5),
            17, new Vec3(0, 0, 0)
    );
}
