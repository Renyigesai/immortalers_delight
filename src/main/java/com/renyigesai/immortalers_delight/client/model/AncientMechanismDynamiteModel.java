package com.renyigesai.immortalers_delight.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

/** Static projectile model exported from Dynamite.bbmodel. */
public class AncientMechanismDynamiteModel<T extends Entity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(
            new ResourceLocation(ImmortalersDelightMod.MODID, "ancient_mechanism_dynamite"), "main");
    private final ModelPart head;

    public AncientMechanismDynamiteModel(ModelPart root) {
        this.head = root.getChild("head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0, 4, 0));
        head.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 6)
                .addBox(-1, -4, -1, 2, 8, 2, new CubeDeformation(0)), PartPose.ZERO);
        PartDefinition fire = head.addOrReplaceChild("fire", CubeListBuilder.create(), PartPose.offset(0, 7, 0));
        fire.addOrReplaceChild("flame_a", CubeListBuilder.create().texOffs(8, 2)
                .addBox(0, -2, -2, 0, 4, 4, new CubeDeformation(0)), PartPose.rotation(0, (float)Math.PI / 4, 0));
        fire.addOrReplaceChild("flame_b", CubeListBuilder.create().texOffs(8, 10)
                .addBox(-2, -2, 0, 4, 4, 0, new CubeDeformation(0)), PartPose.rotation(0, (float)Math.PI / 4, 0));
        head.addOrReplaceChild("side_a", CubeListBuilder.create().texOffs(0, 22)
                .addBox(-2, -4, 1, 2, 8, 2, new CubeDeformation(0)), PartPose.ZERO);
        head.addOrReplaceChild("side_b", CubeListBuilder.create().texOffs(8, 22)
                .addBox(0, -4, 1, 2, 8, 2, new CubeDeformation(0)), PartPose.ZERO);
        return LayerDefinition.create(mesh, 32, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        head.xRot = headPitch * ((float)Math.PI / 180F);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
                               float red, float green, float blue, float alpha) {
        head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
