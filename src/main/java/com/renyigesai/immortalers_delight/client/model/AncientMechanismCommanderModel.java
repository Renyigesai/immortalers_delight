package com.renyigesai.immortalers_delight.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.client.animation.AncientMechanismCommanderAnimation;
import com.renyigesai.immortalers_delight.entities.living.AncientMechanismCommander;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;


public class AncientMechanismCommanderModel<T extends AncientMechanismCommander> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation ANCIENT_MECHANISM_COMMANDER = new ModelLayerLocation(new ResourceLocation(ImmortalersDelightMod.MODID, "ancient_mechanism_commander"), "main");
	private final ModelPart root;
	private final ModelPart waist;
	private final ModelPart head;
	private final ModelPart hats;
	private final ModelPart body;
	private final ModelPart bone;
	private final ModelPart arms;
	private final ModelPart right_arm;
	private final ModelPart right_item;
	private final ModelPart hammer;
	private final ModelPart core;
	private final ModelPart left_arm;
	private final ModelPart left_item;
	private final ModelPart crossbow;
	private final ModelPart bowstring;
	private final ModelPart bowstring_right;
	private final ModelPart bowstring_left;
	private final ModelPart bow_limb_left;
	private final ModelPart bow_limb_right;
	private final ModelPart flywheel;
	private final ModelPart arrow;
	private final ModelPart cylinder;
	private final ModelPart boomer;
	private final ModelPart fire;
	private final ModelPart legs;
	private final ModelPart right_leg;
	private final ModelPart left_leg;

	public AncientMechanismCommanderModel(ModelPart root) {
		this.root = root.getChild("root");
		this.waist = this.root.getChild("waist");
		this.head = this.waist.getChild("head");
		this.hats = this.head.getChild("hats");
		this.body = this.waist.getChild("body");
		this.bone = this.body.getChild("bone");
		this.arms = this.body.getChild("arms");
		this.right_arm = this.body.getChild("right_arm");
		this.right_item = this.right_arm.getChild("right_item");
		this.hammer = this.right_item.getChild("hammer");
		this.core = this.hammer.getChild("core");
		this.left_arm = this.body.getChild("left_arm");
		this.left_item = this.left_arm.getChild("left_item");
		this.crossbow = this.left_item.getChild("crossbow");
		// Keep the crossbow hierarchy for animation, but hide its geometry temporarily.
		this.crossbow.visible = false;
		this.bowstring = this.crossbow.getChild("bowstring");
		this.bowstring_right = this.bowstring.getChild("bowstring_right");
		this.bowstring_left = this.bowstring.getChild("bowstring_left");
		this.bow_limb_left = this.crossbow.getChild("bow_limb_left");
		this.bow_limb_right = this.crossbow.getChild("bow_limb_right");
		this.flywheel = this.crossbow.getChild("flywheel");
		this.arrow = this.crossbow.getChild("arrow");
		this.cylinder = this.crossbow.getChild("cylinder");
		this.boomer = this.left_item.getChild("boomer");
		this.fire = this.boomer.getChild("fire");
		this.legs = this.root.getChild("legs");
		this.right_leg = this.legs.getChild("right_leg");
		this.left_leg = this.legs.getChild("left_leg");
	}

	@Override
	public ModelPart root() {
		return root;
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

		PartDefinition waist = root.addOrReplaceChild("waist", CubeListBuilder.create(), PartPose.offset(0.0F, -3.0F, 0.0F));

		PartDefinition head = waist.addOrReplaceChild("head", CubeListBuilder.create().texOffs(64, 14).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(96, 14).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.25F))
		.texOffs(120, 14).addBox(-1.0F, -3.0F, -6.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(90, 14).addBox(-2.0F, -3.0F, -6.0F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, -10.0F, 0.0F, -0.1047F, 0.0873F, 0.0F));

		PartDefinition hats = head.addOrReplaceChild("hats", CubeListBuilder.create().texOffs(114, 5).addBox(-2.0F, 0.5F, -7.5F, 4.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(72, 0).addBox(-4.0F, -3.0F, -4.0F, 8.0F, 5.0F, 8.0F, new CubeDeformation(0.5F))
		.texOffs(64, 0).addBox(-1.0F, -6.0F, -6.5F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(120, 0).addBox(-0.5F, -7.0F, -6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -9.0F, 0.0F));

		PartDefinition heml1_r1 = hats.addOrReplaceChild("heml1_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, 0.0F, -9.0F, 18.0F, 1.0F, 18.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.1309F, -0.7854F, -0.0873F));

		PartDefinition body = waist.addOrReplaceChild("body", CubeListBuilder.create().texOffs(40, 67).addBox(-5.0F, -10.0F, -2.5F, 10.0F, 13.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bone = body.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 19).addBox(-7.0F, -11.0F, -2.5F, 12.0F, 18.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 1.0F, -1.0F));

		PartDefinition arms = body.addOrReplaceChild("arms", CubeListBuilder.create().texOffs(88, 45).addBox(-10.0F, -2.0F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.2F))
		.texOffs(56, 45).addBox(6.0F, -2.0F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.2F))
		.texOffs(50, 37).addBox(-6.0F, 3.0F, -2.0F, 12.0F, 4.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition leftArmOvercoat = arms.addOrReplaceChild("Left Arm Overcoat_r1", CubeListBuilder.create().texOffs(96, 91).addBox(-3.0F, -4.0F, -3.0F, 6.0F, 8.0F, 6.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(8.0F, 1.0F, 0.0F, 0.0F, 0.0F, -0.1745F));

		PartDefinition rightArmOvercoat = arms.addOrReplaceChild("Right Arm Overcoat_r1", CubeListBuilder.create().texOffs(72, 91).addBox(-3.0F, -4.0F, -3.0F, 6.0F, 8.0F, 6.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(-8.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.1745F));

		PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(72, 45).addBox(-4.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(72, 61).addBox(-4.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(-6.0F, -8.0F, 0.0F, -0.1745F, 0.0F, 0.0F));

		PartDefinition Right_Arm_Overcoat_r2 = right_arm.addOrReplaceChild("Right_Arm_Overcoat_r2", CubeListBuilder.create().texOffs(72, 77).addBox(-3.0F, -4.0F, -3.0F, 6.0F, 8.0F, 6.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(-2.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.1745F));

		PartDefinition right_item = right_arm.addOrReplaceChild("right_item", CubeListBuilder.create(), PartPose.offset(-2.0F, 9.0F, 0.0F));

		PartDefinition hammer = right_item.addOrReplaceChild("hammer", CubeListBuilder.create().texOffs(156, 64).addBox(-7.0F, -29.0F, -2.0F, 2.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(128, 48).addBox(-7.0F, -35.0F, -3.0F, 2.0F, 6.0F, 6.0F, new CubeDeformation(0.2F))
		.texOffs(162, 48).addBox(-4.0F, -35.0F, -3.0F, 2.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(144, 48).addBox(-5.0F, -36.0F, -4.0F, 1.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(128, 80).addBox(-1.0F, -13.0F, -1.0F, 2.0F, 16.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(192, 80).addBox(-1.5F, -10.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.25F))
		.texOffs(144, 80).addBox(-2.0F, 2.5F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(-0.5F))
		.texOffs(160, 80).addBox(-2.0F, 2.5F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(-0.25F))
		.texOffs(176, 80).addBox(-2.0F, 2.5F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(194, 88).addBox(-6.5F, -22.5F, -10.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(192, 85).addBox(-7.0F, -23.0F, -4.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(204, 85).addBox(-6.0F, -22.0F, -3.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(204, 80).addBox(-6.0F, -22.0F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.15F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r1 = hammer.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(130, 26).addBox(0.0F, -11.0F, 0.0F, 0.0F, 11.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(168, 62).addBox(0.0F, -1.0F, 0.0F, 6.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -21.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cube_r2 = hammer.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(164, 26).addBox(0.0F, -11.0F, -8.0F, 0.0F, 11.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(168, 71).addBox(0.0F, -1.0F, -8.0F, 6.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -21.0F, 0.0F, -0.1309F, 0.0F, 0.0F));

		PartDefinition cube_r3 = hammer.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(136, 80).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(-0.5F, -13.0F, 0.0F, 0.0F, 0.0F, -0.1745F));

		PartDefinition cube_r4 = hammer.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(140, 64).addBox(-1.0F, -5.0F, -3.0F, 2.0F, 9.0F, 6.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(-4.5F, -24.0F, 0.0F, 0.0F, 0.0F, -0.1745F));

		PartDefinition cube_r5 = hammer.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(128, 64).addBox(-1.0F, -4.0F, -2.0F, 2.0F, 7.0F, 4.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(-3.0F, -16.0F, 0.0F, 0.0F, 0.0F, -0.1745F));

		PartDefinition core = hammer.addOrReplaceChild("core", CubeListBuilder.create().texOffs(128, 0).addBox(-4.0F, -6.0F, -6.0F, 8.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, -32.0F, 0.0F));

		PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(104, 45).addBox(0.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(104, 61).addBox(0.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(6.0F, -8.0F, 0.0F, 0.2094F, 0.0F, 0.0F));

		PartDefinition Left_Arm_Overcoat_r2 = left_arm.addOrReplaceChild("Left_Arm_Overcoat_r2", CubeListBuilder.create().texOffs(96, 77).addBox(-3.0F, -4.0F, -3.0F, 6.0F, 8.0F, 6.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(2.0F, 1.0F, 0.0F, 0.0F, 0.0F, -0.1745F));

		PartDefinition left_item = left_arm.addOrReplaceChild("left_item", CubeListBuilder.create(), PartPose.offsetAndRotation(2.0F, 9.0F, 0.0F, 1.4835F, 0.0F, 0.0F));

		PartDefinition crossbow = left_item.addOrReplaceChild("crossbow", CubeListBuilder.create().texOffs(212, 35).addBox(1.0F, 2.0F, -11.0F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(212, 32).addBox(-6.0F, 2.0F, -11.0F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(192, 0).addBox(-2.0F, 1.0F, -12.0F, 4.0F, 4.0F, 14.0F, new CubeDeformation(0.15F))
		.texOffs(226, 18).addBox(-1.5F, 2.5F, 4.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(226, 23).addBox(-1.5F, 4.0F, 10.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.1F)), PartPose.offset(-1.0F, -3.0F, -8.0F));

		PartDefinition cube_r6 = crossbow.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(212, 22).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 8.5F, -0.3927F, 0.0F, 0.0F));

		PartDefinition cube_r7 = crossbow.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(192, 20).addBox(-2.5F, -1.0F, -2.5F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.5F, 3.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition bowstring = crossbow.addOrReplaceChild("bowstring", CubeListBuilder.create(), PartPose.offset(0.0F, 2.5F, -5.5F));

		PartDefinition bowstring_right = bowstring.addOrReplaceChild("bowstring_right", CubeListBuilder.create().texOffs(194, 18).addBox(-13.0F, -0.5F, 0.0F, 14.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bowstring_left = bowstring.addOrReplaceChild("bowstring_left", CubeListBuilder.create().texOffs(194, 19).addBox(-1.0F, -0.5F, 0.0F, 14.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bow_limb_left = crossbow.addOrReplaceChild("bow_limb_left", CubeListBuilder.create().texOffs(244, 60).addBox(6.0F, -1.0F, 3.5F, 4.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 3.0F, -10.5F));

		PartDefinition cube_r8 = bow_limb_left.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(192, 38).addBox(7.0F, -1.5F, -0.5F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.1F))
		.texOffs(192, 35).addBox(-2.0F, -0.5F, -0.5F, 9.0F, 2.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(1.0F, -0.5F, 0.0F, 0.0F, -0.5672F, 0.0F));

		PartDefinition bow_limb_right = crossbow.addOrReplaceChild("bow_limb_right", CubeListBuilder.create().texOffs(244, 60).mirror().addBox(-10.0F, -1.0F, 3.5F, 4.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-5.0F, 3.0F, -10.5F));

		PartDefinition cube_r9 = bow_limb_right.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(198, 38).addBox(-9.0F, -2.0F, -0.5F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.1F))
		.texOffs(192, 32).addBox(-7.0F, -1.0F, -0.5F, 9.0F, 2.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, 0.5672F, 0.0F));

		PartDefinition flywheel = crossbow.addOrReplaceChild("flywheel", CubeListBuilder.create().texOffs(248, 52).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(240, 47).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.1F))
		.texOffs(236, 40).addBox(-1.0F, -1.0F, 2.2F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.1F))
		.texOffs(246, 40).addBox(-1.0F, -1.0F, -5.2F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.1F))
		.texOffs(246, 44).addBox(2.2F, -1.0F, -1.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.1F))
		.texOffs(236, 44).addBox(-5.2F, -1.0F, -1.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.1F))
		.texOffs(240, 16).addBox(-3.0F, -1.5F, 5.3F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.03F))
		.texOffs(240, 20).addBox(-3.0F, -1.5F, -7.3F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.03F))
		.texOffs(240, 24).addBox(-7.3F, -1.5F, -3.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.03F))
		.texOffs(240, 32).addBox(5.3F, -1.5F, -3.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.03F)), PartPose.offset(0.0F, 0.0F, 3.0F));

		PartDefinition cube_r10 = flywheel.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(240, 12).addBox(-3.0F, -1.5F, -7.2933F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.02F))
		.texOffs(240, 4).addBox(-3.0F, -1.5F, 5.2933F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.02F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition cube_r11 = flywheel.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(240, 8).addBox(-3.0F, -1.5F, 5.2933F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.02F))
		.texOffs(240, 0).addBox(-3.0F, -1.5F, -7.2933F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.02F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition arrow = crossbow.addOrReplaceChild("arrow", CubeListBuilder.create().texOffs(214, 1).addBox(-0.5F, -2.0F, -7.0F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(230, 0).addBox(-1.5F, -1.5F, 1.0F, 3.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, -4.0F));

		PartDefinition cylinder = crossbow.addOrReplaceChild("cylinder", CubeListBuilder.create().texOffs(214, 42).addBox(0.0F, -4.0F, 0.0F, 0.0F, 4.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(192, 46).addBox(-2.0F, -5.0F, 10.0F, 4.0F, 4.0F, 14.0F, new CubeDeformation(0.05F)), PartPose.offset(0.0F, 8.0F, -22.0F));

		PartDefinition boomer = left_item.addOrReplaceChild("boomer", CubeListBuilder.create().texOffs(224, 102).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(224, 118).addBox(0.0F, -4.0F, 1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(232, 118).addBox(-2.0F, -4.0F, 1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(224, 90).addBox(0.0F, -10.0F, -3.0F, 0.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -3.0F));

		PartDefinition fire = boomer.addOrReplaceChild("fire", CubeListBuilder.create(), PartPose.offset(0.0F, -7.0F, 0.0F));

		PartDefinition cube_r12 = fire.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(232, 106).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(232, 98).addBox(0.0F, -2.0F, -2.0F, 0.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition legs = root.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition right_leg = legs.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(16, 45).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(16, 61).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(-1.9F, 0.0F, 0.0F, 0.192F, 0.0F, 0.0349F));

		PartDefinition left_leg = legs.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 45).addBox(-1.9F, 0.0F, -2.0F, 3.9F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 61).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(1.9F, 0.0F, 0.0F, -0.1745F, 0.0F, -0.0349F));

		return LayerDefinition.create(meshdefinition, 256, 128);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		// Reapply the Blockbench bind pose before adding this frame's head and walk animation.
		root.getAllParts().forEach(ModelPart::resetPose);
		this.animate(entity.idleAnimationState, AncientMechanismCommanderAnimation.IDLE, ageInTicks, 1.0F);
		this.animateWalk(AncientMechanismCommanderAnimation.WALK, limbSwing, limbSwingAmount, 1.0F, 1.0F);
		this.animate(entity.startAttackAnimationState, AncientMechanismCommanderAnimation.ATTACK_START, ageInTicks, 1.0F);
		this.animate(entity.attack1AnimationState, AncientMechanismCommanderAnimation.ATTACK_1, ageInTicks, 1.0F);
		this.animate(entity.attack2AnimationState, AncientMechanismCommanderAnimation.ATTACK_2, ageInTicks, 1.0F);
		this.animate(entity.attack3AnimationState, AncientMechanismCommanderAnimation.ATTACK_3, ageInTicks, 1.0F);
		this.animate(entity.attack4AnimationState, AncientMechanismCommanderAnimation.ATTACK_4, ageInTicks, 1.0F);
		this.animate(entity.summonAnimationState, AncientMechanismCommanderAnimation.SUMMON, ageInTicks, 1.0F);
		this.animate(entity.shootAnimationState, AncientMechanismCommanderAnimation.SHOOT, ageInTicks, 1.0F);
		this.animate(entity.shootAnimationState, AncientMechanismCommanderAnimation.CROSSBOW_SHOOT, ageInTicks, 1.0F);
		this.animate(entity.throwAnimationState, AncientMechanismCommanderAnimation.THROW, ageInTicks, 1.0F);
		this.animate(entity.reviveAnimationState, AncientMechanismCommanderAnimation.REVIVE, ageInTicks, 1.0F);
		head.yRot = netHeadYaw * ((float) Math.PI / 180F);
		head.xRot = -0.1047F + headPitch * ((float) Math.PI / 180F);
		float walk = Mth.cos(limbSwing * 0.6662F) * 1.2F * limbSwingAmount;
		right_leg.xRot = 0.192F + walk;
		left_leg.xRot = -0.1745F - walk;
		right_arm.xRot = -0.1745F - walk * 0.6F;
		left_arm.xRot = 0.2094F + walk * 0.6F;
		// Keep the Blockbench left-item animation active while its geometry is hidden.
		float idle = Mth.sin(ageInTicks * 0.08F) * 0.04F;
		float attack = entity.getAttackAnim(ageInTicks - entity.tickCount);
		left_item.xRot += idle + attack * 0.35F;
		left_arm.xRot += idle;
		if (attack > 0.0F) {
			AnimationUtils.swingWeaponDown(right_arm, left_arm, entity, attack, ageInTicks);
		}
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
