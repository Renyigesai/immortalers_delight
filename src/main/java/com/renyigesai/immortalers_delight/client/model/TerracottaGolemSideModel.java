package com.renyigesai.immortalers_delight.client.model;
import com.renyigesai.immortalers_delight.client.animation.TerracottaGolemAnimation;
import com.renyigesai.immortalers_delight.entities.living.TerracottaGolem;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class TerracottaGolemSideModel <T extends Entity> extends HierarchicalModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation TERRACOTTA_GOLEM_SIDE_LAYER = new ModelLayerLocation(new ResourceLocation("modid", "terracotta_golem_side_model"), "main");
    private final ModelPart root;
    private final ModelPart back;
    private final ModelPart left;
    private final ModelPart right;

    public TerracottaGolemSideModel(ModelPart pRoot) {
        this.root = pRoot;
        this.back = pRoot.getChild("back");
        this.left = pRoot.getChild("left");
        this.right = pRoot.getChild("right");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition back = partdefinition.addOrReplaceChild("back", CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-8.0F, -17.0F, -9.0F, 16.0F, 16.0F, 0.0F, EnumSet.of(Direction.NORTH)),
                PartPose.offsetAndRotation(0.0F, 20.0F, 1.0F, 0.0F, 3.1416F, 0.0F)
        );

        PartDefinition left = partdefinition.addOrReplaceChild("left", CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-8.0F, -17.0F, -9.0F, 16.0F, 16.0F, 0.0F, EnumSet.of(Direction.NORTH)),
                PartPose.offsetAndRotation(0.0F, 20.0F, 1.0F, 0.0F, 1.5708F, 0.0F)
        );

        PartDefinition right = partdefinition.addOrReplaceChild("right", CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-8.0F, -17.0F, -9.0F, 16.0F, 16.0F, 0.0F, EnumSet.of(Direction.NORTH)),
                PartPose.offsetAndRotation(0.0F, 20.0F, 1.0F, 0.0F, -1.5708F, 0.0F)
        );

        return LayerDefinition.create(meshdefinition, 16, 16);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        back.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        left.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        right.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void renderSide(int pSlotID, PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (pSlotID == 0){
            left.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        }
        else if (pSlotID == 1){
            back.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        }
        else if (pSlotID == 2){
            right.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        }
    }

    @Override
    public ModelPart root() {
        return root;
    }
}
