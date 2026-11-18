package com.renyigesai.immortalers_delight.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod; // 替换为你的Mod主类
import com.renyigesai.immortalers_delight.block.soul_infuser.SoulInfuserBlockEntity;
import com.renyigesai.immortalers_delight.client.animation.SoulInfuserAnimation;
import com.renyigesai.immortalers_delight.client.animation.custom.AnimationHelper;
import com.renyigesai.immortalers_delight.client.animation.custom.SoulInfuserAnimations;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.animation.definitions.SnifferAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.Map;

public class SoulInfuserModel<T extends Entity> extends HierarchicalModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            new ResourceLocation(ImmortalersDelightMod.MODID, "soul_infuser"), "main");
    public final ModelPart base;
    public final ModelPart all;
    public final ModelPart head;
    public final ModelPart body;
    public final ModelPart ring_down;
    public final ModelPart upper_body_parts11;
    public final ModelPart upper_body_parts10;
    public final ModelPart upper_body_parts9;
    public final ModelPart upper_body_parts8;
    public final ModelPart ring_center;
    public final ModelPart upper_body_parts7;
    public final ModelPart upper_body_parts6;
    public final ModelPart upper_body_parts5;
    public final ModelPart upper_body_parts4;
    public final ModelPart ring_up;
    public final ModelPart upper_body_parts3;
    public final ModelPart upper_body_parts2;
    public final ModelPart upper_body_parts1;
    public final ModelPart upper_body_parts0;

    public SoulInfuserModel(ModelPart root) {
        this.base = root.getChild("base");
        this.all = root.getChild("all");
        this.head = this.all.getChild("head");
        this.body = this.all.getChild("body");
        this.ring_down = this.body.getChild("ring_down");
        this.upper_body_parts11 = this.ring_down.getChild("upper_body_parts11");
        this.upper_body_parts10 = this.ring_down.getChild("upper_body_parts10");
        this.upper_body_parts9 = this.ring_down.getChild("upper_body_parts9");
        this.upper_body_parts8 = this.ring_down.getChild("upper_body_parts8");
        this.ring_center = this.body.getChild("ring_center");
        this.upper_body_parts7 = this.ring_center.getChild("upper_body_parts7");
        this.upper_body_parts6 = this.ring_center.getChild("upper_body_parts6");
        this.upper_body_parts5 = this.ring_center.getChild("upper_body_parts5");
        this.upper_body_parts4 = this.ring_center.getChild("upper_body_parts4");
        this.ring_up = this.all.getChild("ring_up");
        this.upper_body_parts3 = this.ring_up.getChild("upper_body_parts3");
        this.upper_body_parts2 = this.ring_up.getChild("upper_body_parts2");
        this.upper_body_parts1 = this.ring_up.getChild("upper_body_parts1");
        this.upper_body_parts0 = this.ring_up.getChild("upper_body_parts0");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition base = partdefinition.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, -4.0F, -9.0F, 18.0F, 4.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 20.0F, 0.0F));

        PartDefinition all = partdefinition.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition head = all.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 22).addBox(-7.0F, -9.0F, -7.0F, 14.0F, 16.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(0, 52).addBox(-5.0F, -9.0F, -5.0F, 10.0F, 14.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(20, 52).addBox(-5.0F, -4.1F, -5.0F, 10.0F, 0.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -9.0F, 0.0F));

        PartDefinition body = all.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -7.0F, 0.0F));

        PartDefinition ring_down = body.addOrReplaceChild("ring_down", CubeListBuilder.create(), PartPose.offset(0.0F, 5.0F, 0.0F));

        PartDefinition upper_body_parts11 = ring_down.addOrReplaceChild("upper_body_parts11", CubeListBuilder.create().texOffs(0, 116).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 0.0F, -8.0F));

        PartDefinition upper_body_parts10 = ring_down.addOrReplaceChild("upper_body_parts10", CubeListBuilder.create().texOffs(0, 104).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-8.0F, 0.0F, 8.0F));

        PartDefinition upper_body_parts9 = ring_down.addOrReplaceChild("upper_body_parts9", CubeListBuilder.create().texOffs(0, 92).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-8.0F, 0.0F, -8.0F));

        PartDefinition upper_body_parts8 = ring_down.addOrReplaceChild("upper_body_parts8", CubeListBuilder.create().texOffs(0, 80).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 0.0F, 8.0F));

        PartDefinition ring_center = body.addOrReplaceChild("ring_center", CubeListBuilder.create(), PartPose.offset(0.0F, 5.0F, 0.0F));

        PartDefinition upper_body_parts7 = ring_center.addOrReplaceChild("upper_body_parts7", CubeListBuilder.create().texOffs(32, 116).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -8.0F));

        PartDefinition upper_body_parts6 = ring_center.addOrReplaceChild("upper_body_parts6", CubeListBuilder.create().texOffs(32, 104).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 8.0F));

        PartDefinition upper_body_parts5 = ring_center.addOrReplaceChild("upper_body_parts5", CubeListBuilder.create().texOffs(32, 92).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-8.0F, 0.0F, 0.0F));

        PartDefinition upper_body_parts4 = ring_center.addOrReplaceChild("upper_body_parts4", CubeListBuilder.create().texOffs(32, 80).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 0.0F, 0.0F));

        PartDefinition ring_up = all.addOrReplaceChild("ring_up", CubeListBuilder.create(), PartPose.offset(0.0F, -10.0F, 0.0F));

        PartDefinition upper_body_parts3 = ring_up.addOrReplaceChild("upper_body_parts3", CubeListBuilder.create().texOffs(85, 114).addBox(0.5F, -3.0F, 0.5F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(92, 114).addBox(-4.5F, -3.0F, -4.5F, 9.0F, 5.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.5F, 0.0F, -4.5F));

        PartDefinition upper_body_parts2 = ring_up.addOrReplaceChild("upper_body_parts2", CubeListBuilder.create().texOffs(85, 100).addBox(-4.5F, -3.0F, -4.5F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(92, 100).addBox(-4.5F, -3.0F, -4.5F, 9.0F, 5.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(4.5F, 0.0F, 4.5F));

        PartDefinition upper_body_parts1 = ring_up.addOrReplaceChild("upper_body_parts1", CubeListBuilder.create().texOffs(85, 86).addBox(0.5F, -3.0F, -4.5F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(92, 86).addBox(-4.5F, -3.0F, -4.5F, 9.0F, 5.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.5F, 0.0F, 4.5F));

        PartDefinition upper_body_parts0 = ring_up.addOrReplaceChild("upper_body_parts0", CubeListBuilder.create().texOffs(85, 72).addBox(-4.5F, -3.0F, 0.5F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(92, 72).addBox(-4.5F, -3.0F, -4.5F, 9.0F, 5.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(4.5F, 0.0F, -4.5F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    //用于生物模型渲染
    @Override
    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float ageInTicks, float pNetHeadYaw, float pHeadPitch) {

    }

    //用于方块实体渲染
    public void setupAnim(SoulInfuserBlockEntity blockEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
//        this.head.xRot = headPitch * ((float)Math.PI / 180F);
//        this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        resetAnim();
        this.animate(blockEntity.getActions().awakenAnimationState, SoulInfuserAnimation.awaken, ageInTicks);
        this.animate(blockEntity.getActions().idleAnimationState, SoulInfuserAnimation.idle, ageInTicks);
        this.animate(blockEntity.getActions().attackFireRainAnimationState, SoulInfuserAnimation.attack_fire_rain, ageInTicks);
        this.animate(blockEntity.getActions().attackExplodeAnimationState, SoulInfuserAnimation.attack_explode, ageInTicks);
        this.animate(blockEntity.getActions().attackColumnAnimationState, SoulInfuserAnimation.attack_column, ageInTicks);
        this.animate(blockEntity.getActions().attackSummonAnimationState, SoulInfuserAnimation.attack_summon, ageInTicks);
        this.animate(blockEntity.getActions().attackFireRingAnimationState, SoulInfuserAnimation.attack_fire_ring, ageInTicks);
        this.animate(blockEntity.getActions().attackFireballAnimationState, SoulInfuserAnimation.attack_fireball, ageInTicks);
        this.animate(blockEntity.getActions().dieAnimationState, SoulInfuserAnimation.die, ageInTicks);
    }

    public void resetAnim() {
        this.base.getAllParts().forEach(ModelPart::resetPose);
        this.all.getAllParts().forEach(ModelPart::resetPose);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        all.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        base.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public @NotNull ModelPart root() {
        return all;
    }

    public void animate(int animId, float ageInTicks) {
        // 关键：每帧开始前将所有部件重置回初始绑定姿势（避免上一动画或上一状态残留导致错位）
        this.resetAnim();

        switch (animId) {
            case 0: SoulInfuserAnimations.Awaken.apply(this, ageInTicks); break;
            case 1: SoulInfuserAnimations.Idle.apply(this, ageInTicks); break;
            case 2: SoulInfuserAnimations.AttackFireRain.apply(this, ageInTicks); break;
            case 3: SoulInfuserAnimations.AttackExplode.apply(this, ageInTicks); break;
            case 4: SoulInfuserAnimations.AttackColumn.apply(this, ageInTicks); break;
            case 5: SoulInfuserAnimations.AttackSummon.apply(this, ageInTicks); break;
            case 6: SoulInfuserAnimations.AttackFireRing.apply(this, ageInTicks); break;
            case 7: SoulInfuserAnimations.AttackFireball.apply(this, ageInTicks); break;
            case 14: SoulInfuserAnimations.Die.apply(this, ageInTicks); break;
            default: break; // ID 15 (ID_NONE) 保持 resetAnim 的初始静止姿势
//            case 0: SoulInfuserAnimations.Awaken.apply(this, ageInTicks); break;
//            case 1: SoulInfuserAnimations.Idle.apply(this, ageInTicks); break;
//            case 2: SoulInfuserAnimations.AttackFireRain.apply(this, ageInTicks); break;
//            case 3: SoulInfuserAnimations.AttackExplode.apply(this, ageInTicks); break;
//            case 4: SoulInfuserAnimations.AttackColumn.apply(this, ageInTicks); break;
//            case 5: SoulInfuserAnimations.AttackSummon.apply(this, ageInTicks); break;
//            case 6: SoulInfuserAnimations.AttackFireRing.apply(this, ageInTicks); break;
//            case 7: SoulInfuserAnimations.AttackFireball.apply(this, ageInTicks); break;
//            case 14: SoulInfuserAnimations.Die.apply(this, ageInTicks); break;
//            default: break; // ID 15 (ID_NONE) 保持 resetAnim 的初始静止姿势
        }
    }


}
