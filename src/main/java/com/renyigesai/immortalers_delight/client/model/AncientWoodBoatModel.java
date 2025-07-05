package com.renyigesai.immortalers_delight.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.WaterPatchModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class AncientWoodBoatModel extends ListModel<Boat> implements WaterPatchModel {

    public static final ModelLayerLocation ANCIENT_BOAT = new ModelLayerLocation(new ResourceLocation(ImmortalersDelightMod.MODID, "ancient_wood_boat"), "main");

    private static final String PADDLE_LEFT = "paddle_left";
    private static final String PADDLE_RIGHT = "paddle_right";
    private static final String WATER_PATCH = "water_patch";
    private static final String BOTTOM = "bottom";
    private static final String BACK = "back";
    private static final String FRONT = "front";
    private static final String RIGHT = "right";
    private static final String LEFT = "left";
    private final ModelPart leftPaddle;
    private final ModelPart rightPaddle;
    private final ModelPart waterPatch;
    private final ImmutableList<ModelPart> parts;

    public AncientWoodBoatModel(ModelPart pRoot) {
        this.leftPaddle = pRoot.getChild("paddle_left");
        this.rightPaddle = pRoot.getChild("paddle_right");
        this.waterPatch = pRoot.getChild("water_patch");
        this.parts = this.createPartsBuilder(pRoot).build();
    }

    protected ImmutableList.Builder<ModelPart> createPartsBuilder(ModelPart pRoot) {
        ImmutableList.Builder<ModelPart> builder = new ImmutableList.Builder<>();
        builder.add(pRoot.getChild("bottom"),
                pRoot.getChild("back"),
                pRoot.getChild("front"),
                pRoot.getChild("right"),
                pRoot.getChild("left"),
                this.leftPaddle,
                this.rightPaddle
        );
        return builder;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bottom = partdefinition.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(0, 32).addBox(-32.0F, -8.0F, 0.5F, 64.0F, 16.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(48, 114).addBox(-19.0F, -6.0F, -1.5F, 38.0F, 12.0F, 2.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition front = partdefinition.addOrReplaceChild("front", CubeListBuilder.create().texOffs(0, 76).addBox(-11.0F, -3.0F, -10.0F, 22.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 51).addBox(-12.0F, -3.0F, 8.0F, 24.0F, 2.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.0F, -2.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition front_r1 = front.addOrReplaceChild("front_r1", CubeListBuilder.create().texOffs(0, 66).addBox(-12.0F, -4.0F, -1.0F, 24.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 19.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition back = partdefinition.addOrReplaceChild("back", CubeListBuilder.create().texOffs(74, 70).addBox(-11.0F, -3.0F, 0.0F, 22.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(74, 51).addBox(-12.0F, -3.0F, 14.0F, 24.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.0F, -2.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition back_r1 = back.addOrReplaceChild("back_r1", CubeListBuilder.create().texOffs(74, 60).addBox(-15.0F, -5.0F, -1.0F, 24.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 1.0F, 18.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition right = partdefinition.addOrReplaceChild("right", CubeListBuilder.create().texOffs(0, 16).addBox(-39.0F, -4.0F, 2.0F, 78.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, -9.0F, 0.0F, -3.1416F, 0.0F));

        PartDefinition right_r1 = right.addOrReplaceChild("right_r1", CubeListBuilder.create().texOffs(0, 22).addBox(-56.0F, -5.0F, -1.0F, 70.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(21.0F, 1.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition left = partdefinition.addOrReplaceChild("left", CubeListBuilder.create().texOffs(0, 0).addBox(-39.0F, -4.0F, 2.0F, 78.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 9.0F));

        PartDefinition left_r1 = left.addOrReplaceChild("left_r1", CubeListBuilder.create().texOffs(0, 6).addBox(-56.0F, -5.0F, -1.0F, 70.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(21.0F, 1.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition paddle_left = partdefinition.addOrReplaceChild("paddle_left", CubeListBuilder.create().texOffs(200, 28).addBox(-1.0F, -1.0F, -13.0F, 2.0F, 2.0F, 26.0F, new CubeDeformation(0.0F))
                .texOffs(170, 28).addBox(-0.01F, -4.0F, 9.0F, 1.0F, 6.0F, 14.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(-13.5F, -8.0F, 15.0F, -0.5236F, 0.0F, 0.0F));

        PartDefinition paddle_right = partdefinition.addOrReplaceChild("paddle_right", CubeListBuilder.create().texOffs(200, 0).addBox(-1.0F, -1.0F, -13.0F, 2.0F, 2.0F, 26.0F, new CubeDeformation(0.0F))
                .texOffs(170, 0).addBox(-0.99F, -4.0F, 9.0F, 1.0F, 6.0F, 14.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(-13.5F, -8.0F, -15.0F, -0.5236F, 3.1416F, 0.0F));

        PartDefinition water_patch = partdefinition.addOrReplaceChild("water_patch", CubeListBuilder.create().texOffs(0, 0).addBox(-32.0F, -10.0F, -3.0F, 64.0F, 20.0F, 3.0F), PartPose.offsetAndRotation(0.0F, -3.0F, 1.0F, ((float)Math.PI / 2F), 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    /**
     * Sets this entity's model rotation angles
     */
    public void setupAnim(Boat pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        animatePaddle(pEntity, 0, this.leftPaddle, pLimbSwing);
        animatePaddle(pEntity, 1, this.rightPaddle, pLimbSwing);
    }

    public ImmutableList<ModelPart> parts() {
        return this.parts;
    }

    public ModelPart waterPatch() {
        return this.waterPatch;
    }

    private static void animatePaddle(Boat pBoat, int pSide, ModelPart pPaddle, float pLimbSwing) {
        float f = pBoat.getRowingTime(pSide, pLimbSwing);
        pPaddle.xRot = Mth.clampedLerp((-(float)Math.PI / 3F), -0.2617994F, (Mth.sin(-f) + 1.0F) / 2.0F);
        pPaddle.yRot = Mth.clampedLerp((-(float)Math.PI / 4F), ((float)Math.PI / 4F), (Mth.sin(-f + 1.0F) + 1.0F) / 2.0F);
        if (pSide == 1) {
            pPaddle.yRot = (float)Math.PI - pPaddle.yRot;
        }

    }
}