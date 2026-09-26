package com.renyigesai.immortalers_delight.client.model.projectile;
// Made with Blockbench 5.0.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.client.AnimationHelper;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class TwinkleParticleModel<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation TWINKLE_PARTICLE = new ModelLayerLocation(new ResourceLocation(ImmortalersDelightMod.MODID, "twinkle_particle"), "main");
	private final ModelPart root;
	private final ModelPart all;
	private final ModelPart bone;
	private final ModelPart bone2;

	public TwinkleParticleModel(ModelPart root) {
		this.root = root.getChild("root");
		this.all = this.root.getChild("all");
		this.bone = this.all.getChild("bone");
		this.bone2 = this.all.getChild("bone2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition all = root.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bone = all.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -15.0F, -0.5F, 1.0F, 15.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bone2 = all.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -30.0F, -0.5F, 1.0F, 15.0F, 1.0F, new CubeDeformation(0.02F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}


	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	public void setupAnim(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		AnimationHelper corePositions = new AnimationHelper(this.bone1Position);
		this.bone.x = (float) corePositions.getPositionAtTime(ageInTicks).x() * -1;
		this.bone.y = (float) corePositions.getPositionAtTime(ageInTicks).y() * -1;
		this.bone.z = (float) corePositions.getPositionAtTime(ageInTicks).z();
		AnimationHelper corePositions2 = new AnimationHelper(this.bone2Position);
		this.bone2.x = (float) corePositions2.getPositionAtTime(ageInTicks).x() * -1;
		this.bone2.y = (float) corePositions2.getPositionAtTime(ageInTicks).y() * -1;
		this.bone2.z = (float) corePositions2.getPositionAtTime(ageInTicks).z();
	}
	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public ModelPart getBone1() {
		return this.bone;
	}

	public ModelPart getBone2() {return bone2;}

	public ModelPart getAllBone() {return all;}

	Map<Integer, Vec3> bone1Position = Map.of(
			0, new Vec3(0, 32, 0),
			1, new Vec3(0, 32, 0),
			5, new Vec3(0, 10.2, 0),
			7, new Vec3(0, 6.8, 0),
			11, new Vec3(0, 0, 0),
			17, new Vec3(0, 0, 0)
	);
	Map<Integer, Vec3> bone2Position = Map.of(
			0, new Vec3(0, 17, 0),
			1, new Vec3(0, 17, 0),
			5, new Vec3(0, 10.2, 0),
			7, new Vec3(0, 6.8, 0),
			11, new Vec3(0, -15, 0),
			17, new Vec3(0, -15, 0)
	);
}