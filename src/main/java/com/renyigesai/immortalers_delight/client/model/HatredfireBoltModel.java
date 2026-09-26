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
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HatredfireBoltModel<T extends Entity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "hatredfireboltmodel"), "main");
    private final ModelPart core;
    private final ModelPart layer;
    private final ModelPart layer2;
    private final ModelPart layer3;

    public HatredfireBoltModel(ModelPart root) {
        this.core = root.getChild("core");
        this.layer = root.getChild("layer");
        this.layer2 = root.getChild("layer2");
        this.layer3 = root.getChild("layer3");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition core = partdefinition.addOrReplaceChild("core", CubeListBuilder.create().texOffs(64, 16).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 0.0F));

        PartDefinition layer = partdefinition.addOrReplaceChild("layer", CubeListBuilder.create().texOffs(96, 52).addBox(5.0F, 5.0F, 5.0F, -10.0F, -10.0F, -10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 0.0F));

        PartDefinition layer2 = partdefinition.addOrReplaceChild("layer2", CubeListBuilder.create().texOffs(56, 60).addBox(7.0F, 7.0F, 7.0F, -14.0F, -14.0F, -14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 0.0F));

        PartDefinition layer3 = partdefinition.addOrReplaceChild("layer3", CubeListBuilder.create().texOffs(64, 32).addBox(8.0F, 8.0F, 8.0F, -16.0F, -16.0F, -16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float baseY = 8.0F;
        this.core.y = baseY;
        this.layer.y = baseY;
        this.layer2.y = baseY;
        this.layer3.y = baseY;

        // 整体滚转
        float baseAngle = ageInTicks * 0.08F; // 基础角速度
        float nextAngle = (ageInTicks + 1) * 0.08F; // 基础角速度

        // 外层 layer3 + layer2：正向旋转 + 倾角 (45度倾斜)
        this.layer3.yRot = baseAngle;
        this.layer3.xRot = (float) (Math.PI / 4D);
        this.layer3.zRot = (float) (Math.PI / 6D);
        this.layer2.yRot = baseAngle;
        this.layer2.xRot = (float) (Math.PI / 4D);
        this.layer2.zRot = (float) (Math.PI / 6D);

        // 内层 layer + core：正向旋转 + 倾角 (45度倾斜)
        this.layer.yRot =nextAngle;
        this.layer.xRot = (float) (Math.PI / 4D);
        this.layer.zRot = (float) (Math.PI / 6D);
        this.core.yRot = nextAngle;
        this.core.xRot = (float) (Math.PI / 4D);
        this.core.zRot = (float) (Math.PI / 6D);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        core.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        layer.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        layer2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        layer3.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
    public ModelPart getCore() { return core; }
    public ModelPart getLayer() { return layer; }
    public ModelPart getLayer2() { return layer2; }
    public ModelPart getLayer3() { return layer3; }
}
