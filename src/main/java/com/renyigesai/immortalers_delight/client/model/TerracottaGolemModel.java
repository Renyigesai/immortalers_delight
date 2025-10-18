package com.renyigesai.immortalers_delight.client.model;
import com.renyigesai.immortalers_delight.client.animation.TerracottaGolemAnimation;
import com.renyigesai.immortalers_delight.entities.living.TerracottaGolem;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

public class TerracottaGolemModel<T extends Entity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation TERRACOTTA_GOLEM = new ModelLayerLocation(new ResourceLocation(ImmortalersDelightMod.MODID, "terracotta_golem"), "main");
	private final ModelPart Body;
	private final ModelPart right_arm;
	private final ModelPart right_bristles_1;
	private final ModelPart right_bristles_2;
	private final ModelPart right_blade;
	private final ModelPart right_bristles_3;
	private final ModelPart left_arm;
	private final ModelPart left_bristles_1;
	private final ModelPart left_bristles_2;
	private final ModelPart left_blade;
	private final ModelPart left_bristles_3;
	private final ModelPart RightLeg;
	private final ModelPart LeftLeg;

	public TerracottaGolemModel(ModelPart root) {
		this.Body = root.getChild("Body");
		this.right_arm = this.Body.getChild("right_arm");
		this.right_bristles_1 = this.right_arm.getChild("right_bristles_1");
		this.right_bristles_2 = this.right_arm.getChild("right_bristles_2");
		this.right_blade = this.right_bristles_2.getChild("right_blade");
		this.right_bristles_3 = this.right_arm.getChild("right_bristles_3");
		this.left_arm = this.Body.getChild("left_arm");
		this.left_bristles_1 = this.left_arm.getChild("left_bristles_1");
		this.left_bristles_2 = this.left_arm.getChild("left_bristles_2");
		this.left_blade = this.left_bristles_2.getChild("left_blade");
		this.left_bristles_3 = this.left_arm.getChild("left_bristles_3");
		this.RightLeg = root.getChild("RightLeg");
		this.LeftLeg = root.getChild("LeftLeg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, -18.0F, -8.0F, 18.0F, 18.0F, 18.0F, new CubeDeformation(0.0F))
		.texOffs(88, 0).addBox(-5.0F, -20.0F, -4.0F, 10.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 20.0F, 0.0F));

		PartDefinition right_arm = Body.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(0.0F, -9.0F, 0.0F));

		PartDefinition right_bristles_1 = right_arm.addOrReplaceChild("right_bristles_1", CubeListBuilder.create().texOffs(64, 36).addBox(-15.5F, -8.0F, -11.0F, 6.0F, 20.0F, 6.0F, new CubeDeformation(0.25F))
		.texOffs(88, 49).addBox(-15.5F, 12.0F, -11.0F, 6.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 0.0F));

		PartDefinition right_bristles_2 = right_arm.addOrReplaceChild("right_bristles_2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = right_bristles_2.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(64, 46).addBox(0.0F, 0.0F, -4.0F, 0.0F, 18.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-14.0F, -1.0F, -13.0F, -0.7854F, 0.0F, 1.5708F));

		PartDefinition right_blade = right_bristles_2.addOrReplaceChild("right_blade", CubeListBuilder.create(), PartPose.offset(-9.0F, 0.0F, 0.0F));

		PartDefinition cube_r2 = right_blade.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(96, 46).addBox(0.0F, 0.0F, -4.0F, 0.0F, 18.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -1.0F, 15.0F, 0.7854F, 0.0F, 1.5708F));

		PartDefinition right_bristles_3 = right_arm.addOrReplaceChild("right_bristles_3", CubeListBuilder.create().texOffs(64, 84).addBox(30.0F, -2.0F, -1.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(76, 84).addBox(-15.0F, -2.0F, -1.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(88, 84).addBox(7.0F, -2.0F, 21.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(100, 84).addBox(7.0F, -2.0F, -24.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-9.0F, 4.0F, 0.0F));

		PartDefinition cubeLayer4_r1 = right_bristles_3.addOrReplaceChild("cubeLayer4_r1", CubeListBuilder.create().texOffs(100, 90).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.5F, -0.5F, -22.5F, -0.7854F, -0.7854F, 0.7854F));

		PartDefinition cubeLayer3_r1 = right_bristles_3.addOrReplaceChild("cubeLayer3_r1", CubeListBuilder.create().texOffs(88, 90).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.5F, -0.5F, 22.5F, -0.7854F, -0.7854F, 0.7854F));

		PartDefinition cubeLayer2_r1 = right_bristles_3.addOrReplaceChild("cubeLayer2_r1", CubeListBuilder.create().texOffs(76, 90).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-13.5F, -0.5F, 0.5F, -0.7854F, -0.7854F, 0.7854F));

		PartDefinition cubeLayer1_r1 = right_bristles_3.addOrReplaceChild("cubeLayer1_r1", CubeListBuilder.create().texOffs(64, 90).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(31.5F, -0.5F, 0.5F, -0.7854F, -0.7854F, 0.7854F));

		PartDefinition left_arm = Body.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(0.0F, -9.0F, 0.0F));

		PartDefinition left_bristles_1 = left_arm.addOrReplaceChild("left_bristles_1", CubeListBuilder.create().texOffs(0, 36).addBox(9.5F, -8.0F, -11.0F, 6.0F, 20.0F, 6.0F, new CubeDeformation(0.25F))
		.texOffs(24, 49).addBox(9.5F, 12.0F, -11.0F, 6.0F, 3.0F, 10.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -2.0F, 0.0F));

		PartDefinition left_bristles_2 = left_arm.addOrReplaceChild("left_bristles_2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r3 = left_bristles_2.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 46).addBox(0.0F, 0.0F, -12.0F, 0.0F, 18.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(14.0F, -1.0F, -13.0F, -0.7854F, 0.0F, -1.5708F));

		PartDefinition left_blade = left_bristles_2.addOrReplaceChild("left_blade", CubeListBuilder.create(), PartPose.offset(-8.0F, 39.0F, 9.0F));

		PartDefinition cube_r4 = left_blade.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(32, 46).addBox(0.0F, 0.0F, -12.0F, 0.0F, 18.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(20.0F, -40.0F, 5.0F, 0.7854F, 0.0F, -1.5708F));

		PartDefinition left_bristles_3 = left_arm.addOrReplaceChild("left_bristles_3", CubeListBuilder.create().texOffs(0, 90).addBox(-4.0F, -1.0F, -25.0F, 8.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(24, 90).addBox(-4.0F, -1.0F, 21.0F, 8.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 80).addBox(21.0F, -1.0F, -4.0F, 4.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(24, 80).addBox(-25.0F, -1.0F, -4.0F, 4.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 0.0F));

		PartDefinition RightLeg = partdefinition.addOrReplaceChild("RightLeg", CubeListBuilder.create().texOffs(108, 17).addBox(-2.0F, 12.0F, -2.5F, 4.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 7.0F, 0.0F));

		PartDefinition LeftLeg = partdefinition.addOrReplaceChild("LeftLeg", CubeListBuilder.create().texOffs(88, 17).addBox(-2.0F, 12.0F, -2.5F, 4.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 7.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.animateWalk(TerracottaGolemAnimation.WALK, limbSwing, limbSwingAmount,2f,2.5f);
		this.animate(TerracottaGolem.idleAnimationState, TerracottaGolemAnimation.IDLE,ageInTicks, 0.5f);
		this.animate(TerracottaGolem.attackAnimationState, TerracottaGolemAnimation.ATTACK_LEFT,ageInTicks, 1.0f);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		RightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		LeftLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public @NotNull ModelPart root() {
		return Body;
	}
}