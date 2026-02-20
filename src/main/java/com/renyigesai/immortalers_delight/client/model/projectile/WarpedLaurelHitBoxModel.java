package com.renyigesai.immortalers_delight.client.model.projectile;
// Made with Blockbench 5.0.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.renyigesai.immortalers_delight.client.animation.BaseAnimationHelper;
import com.renyigesai.immortalers_delight.entities.projectile.EffectCloudBaseEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.EntityModel;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class WarpedLaurelHitBoxModel <T extends EffectCloudBaseEntity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(ImmortalersDelightMod.MODID, "warped_laurel_hitbox"), "main");
    private final ModelPart all;
    private final ModelPart core;
    private final ModelPart layer_inside;
    private final ModelPart layer_outside;

    public WarpedLaurelHitBoxModel(ModelPart root) {
        this.all = root.getChild("all");
        this.core = this.all.getChild("core");
        this.layer_inside = this.all.getChild("layer_inside");
        this.layer_outside = this.all.getChild("layer_outside");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition all = partdefinition.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition core = all.addOrReplaceChild("core", CubeListBuilder.create().texOffs(32, 96).addBox(-16.0F, -2.25F, -16.0F, 32.0F, 0.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = core.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(48, 48).addBox(-12.0F, -8.0F, 0.0F, 24.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.0F, -8.0F, 15.0F, -0.7854F, 0.7854F, 0.0F));

        PartDefinition cube_r2 = core.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(48, 32).addBox(-12.0F, -8.0F, 0.0F, 24.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 21.25F, -0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r3 = core.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(48, 16).addBox(-12.0F, -8.0F, 0.0F, 24.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.0F, -8.0F, 15.0F, -0.7854F, -0.7854F, 0.0F));

        PartDefinition cube_r4 = core.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(48, -24).addBox(0.0F, -8.0F, -12.0F, 0.0F, 16.0F, 24.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-21.25F, -8.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition cube_r5 = core.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 48).addBox(-12.0F, -8.0F, 0.0F, 24.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.0F, -8.0F, -15.0F, 0.7854F, 0.7854F, 0.0F));

        PartDefinition cube_r6 = core.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 32).addBox(-12.0F, -8.0F, 0.0F, 24.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, -21.25F, 0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r7 = core.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 16).addBox(-12.0F, -8.0F, 0.0F, 24.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.0F, -8.0F, -15.0F, 0.7854F, -0.7854F, 0.0F));

        PartDefinition cube_r8 = core.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, -24).addBox(0.0F, -8.0F, -12.0F, 0.0F, 16.0F, 24.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(21.25F, -8.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition layer_inside = all.addOrReplaceChild("layer_inside", CubeListBuilder.create().texOffs(96, -16).addBox(19.2F, -8.0F, -8.0F, 0.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(96, 16).addBox(-8.0F, -8.0F, -19.2F, 16.0F, 8.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(96, 16).addBox(-19.2F, -8.0F, -8.0F, 0.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(96, 48).addBox(-8.0F, -8.0F, 19.2F, 16.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -28.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

        PartDefinition cube_r9 = layer_inside.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(96, 40).addBox(0.0F, -4.0F, -8.0F, 0.0F, 8.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(13.6F, -4.0F, 13.6F, 0.0F, -0.7854F, 0.0F));

        PartDefinition cube_r10 = layer_inside.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(96, 24).addBox(0.0F, -4.0F, -8.0F, 0.0F, 8.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-13.6F, -4.0F, 13.6F, 0.0F, 0.7854F, 0.0F));

        PartDefinition cube_r11 = layer_inside.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(96, 8).addBox(0.0F, -4.0F, -8.0F, 0.0F, 8.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-13.6F, -4.0F, -13.6F, 0.0F, -0.7854F, 0.0F));

        PartDefinition cube_r12 = layer_inside.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(96, -8).addBox(0.0F, -4.0F, -8.0F, 0.0F, 8.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(13.6F, -4.0F, -13.6F, 0.0F, 0.7854F, 0.0F));

        PartDefinition layer_outside = all.addOrReplaceChild("layer_outside", CubeListBuilder.create().texOffs(0, 32).addBox(38.6F, -8.0F, -16.0F, 0.0F, 8.0F, 32.0F, new CubeDeformation(0.0F))
                .texOffs(0, 80).addBox(-16.0F, -8.0F, -38.6F, 32.0F, 8.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 64).addBox(-38.6F, -8.0F, -16.0F, 0.0F, 8.0F, 32.0F, new CubeDeformation(0.0F))
                .texOffs(0, 112).addBox(-16.0F, -8.0F, 38.6F, 32.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -26.0F, 0.0F, 0.0F, 0.0F, -0.2618F));

        PartDefinition cube_r13 = layer_outside.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(0, 88).addBox(0.0F, -4.0F, -16.0F, 0.0F, 8.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(27.3F, -4.0F, 27.3F, 0.0F, -0.7854F, 0.0F));

        PartDefinition cube_r14 = layer_outside.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(0, 72).addBox(0.0F, -4.0F, -16.0F, 0.0F, 8.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-27.3F, -4.0F, 27.3F, 0.0F, 0.7854F, 0.0F));

        PartDefinition cube_r15 = layer_outside.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(0, 56).addBox(0.0F, -4.0F, -16.0F, 0.0F, 8.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-27.3F, -4.0F, -27.3F, 0.0F, -0.7854F, 0.0F));

        PartDefinition cube_r16 = layer_outside.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(0, 40).addBox(0.0F, -4.0F, -16.0F, 0.0F, 8.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(27.3F, -4.0F, -27.3F, 0.0F, 0.7854F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        float f = pLimbSwing;
        //每个PI是180度
        if (f < 1f) {
            float f1 = BaseAnimationHelper.easeOutSine(f);
            this.all.yRot = f * 0.5f * (float) Math.PI;
            this.core.yRot = f1 * (float) Math.PI;
            this.layer_inside.yRot = f1 * (float) Math.PI;
            this.layer_outside.yRot = -f1 * 2 * (float) Math.PI;;
            this.core.y = -f * 2;
            this.layer_inside.y = -f * 4;
            this.layer_outside.y = -f * 6;
        } else {
            float f1 = f - 1;
            this.all.yRot = f * 0.5f * (float) Math.PI;
            this.core.yRot = (float) Math.PI;
            this.layer_inside.yRot = (1 + f1 / 3) * (float) Math.PI;
            this.layer_outside.yRot = -(1 + f1 / 3) * 2 * (float) Math.PI;;
            this.core.y = -2 -f1;
            this.layer_inside.y = -4 -f1 * 2;
            this.layer_outside.y = -6 -f1 * 3;
        }
    }

    @Override
    public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
        all.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
    }
}
