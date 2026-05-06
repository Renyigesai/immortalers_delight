package com.renyigesai.immortalers_delight.client.model;// Made with Blockbench 5.1.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class RotatingRoastMeatModel<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation ROTATING_ROAST_MEAT = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "rotating_roast_meat"), "main");
	private final ModelPart all;
	private final ModelPart meat;
	private final ModelPart row;
	private final ModelPart row_extend;

	public RotatingRoastMeatModel(ModelPart root) {
		this.all = root.getChild("all");
		this.meat = this.all.getChild("meat");
		this.row = this.all.getChild("row");
		this.row_extend = this.row.getChild("row_extend");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition all = partdefinition.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition meat = all.addOrReplaceChild("meat", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -13.0F, -4.0F, 8.0F, 7.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 25).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 15).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, 0.0F));

		PartDefinition row = all.addOrReplaceChild("row", CubeListBuilder.create().texOffs(24, 15).addBox(-1.0F, -18.0F, -1.0F, 2.0F, 18.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition row_extend = row.addOrReplaceChild("row_extend", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int packedColor) {
		all.render(poseStack, vertexConsumer, packedLight, packedOverlay, packedColor);
	}

	public ModelPart getAll() {
		return all;
	}

	public ModelPart getMeat() {
		return meat;
	}

	public ModelPart getRow() {
		return row;
	}

	public ModelPart getRowExtend() {
		return row_extend;
	}
}