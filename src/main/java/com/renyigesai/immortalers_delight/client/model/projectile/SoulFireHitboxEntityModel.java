package com.renyigesai.immortalers_delight.client.model.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.renyigesai.immortalers_delight.client.animation.custom.AnimationHelper;
import com.renyigesai.immortalers_delight.entities.projectile.SoulFireHitboxEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.Map;
public class SoulFireHitboxEntityModel<T extends Entity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(ImmortalersDelightMod.MODID, "soul_fire_hitbox"), "main");
    private final ModelPart all;
    private final ModelPart outside;
    public ModelPart getOutside() {return outside;}

    private final ModelPart inside;
    public ModelPart getInside() {return inside;}

    public SoulFireHitboxEntityModel(ModelPart root) {
        this.all = root.getChild("all");
        this.outside = this.all.getChild("outside");
        this.inside = this.all.getChild("inside");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition all = partdefinition.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition outside = all.addOrReplaceChild("outside", CubeListBuilder.create().texOffs(-64, 0).addBox(-32.0F, -0.5F, -32.0F, 64.0F, 0.0F, 64.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition inside = all.addOrReplaceChild("inside", CubeListBuilder.create().texOffs(-64, 64).addBox(-32.0F, -1.0F, -32.0F, 64.0F, 0.0F, 64.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.all.getAllParts().forEach(ModelPart::resetPose);
        //待机状态动画，显示环状
        if (ageInTicks < SoulFireHitboxEntity.ATTACK_TRIGGER_TICKS) {
            this.inside.visible = ageInTicks > 0;
            this.outside.visible = true;
            int degree = (int) (limbSwingAmount / 5);

            // 角度 = degree * 90度
            this.outside.yRot = degree * 90 * Mth.DEG_TO_RAD;
            if (ageInTicks > 0) {
                this.inside.xScale = Mth.sin(ageInTicks * 15 * Mth.DEG_TO_RAD);
                this.inside.yScale = Mth.sin(ageInTicks * 15 * Mth.DEG_TO_RAD);
                this.inside.zScale = Mth.sin(ageInTicks * 15 * Mth.DEG_TO_RAD);
            }
        } else {
            this.inside.visible = true;
            this.outside.visible = false;
            if (ageInTicks > 20) {
                this.inside.xScale = Mth.cos((ageInTicks - 20) * 3 * Mth.DEG_TO_RAD);
                this.inside.yScale = Mth.cos((ageInTicks - 20) * 3 * Mth.DEG_TO_RAD);
                this.inside.zScale = Mth.cos((ageInTicks - 20) * 3 * Mth.DEG_TO_RAD);
            }
        }
    }

    public void scaleModel(float f) {
        this.all.xScale *= f;
        this.all.yScale *= f;
        this.all.zScale *= f;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        all.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
