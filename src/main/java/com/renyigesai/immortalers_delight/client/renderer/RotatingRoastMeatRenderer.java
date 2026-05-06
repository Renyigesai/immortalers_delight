package com.renyigesai.immortalers_delight.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.block.food.RotatingRoastMeatBlockEntity;
import com.renyigesai.immortalers_delight.client.model.RotatingRoastMeatModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RotatingRoastMeatRenderer implements BlockEntityRenderer<RotatingRoastMeatBlockEntity> {
    private final RotatingRoastMeatModel<?> model;
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "textures/entity/rotating_roast_meat.png");

    public RotatingRoastMeatRenderer(BlockEntityRendererProvider.Context pContext) {
        this.model = new RotatingRoastMeatModel<>(pContext.bakeLayer(RotatingRoastMeatModel.ROTATING_ROAST_MEAT));
    }

    @Override
    public void render(RotatingRoastMeatBlockEntity entity, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {
        poseStack.pushPose();
        poseStack.translate(0.5F, 1.5F, 0.5F);
        poseStack.mulPose(Axis.XP.rotationDegrees(180F));
        poseStack.scale(0.9995F, 0.9995F, 0.9995F);
        VertexConsumer vertexConsumer = multiBufferSource.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
//        this.model.getUp().xRot = (float) Math.toRadians(blender.getProgress(pPartialTick) * -25);
//        if (!BakeriesMod.aprilFoolsDay){
//            this.model.getHead().yRot = (float) Math.toRadians(blender.getRprogress(pPartialTick) * 360);
//        }else {
//            this.model.getAll().yRot = (float) Math.toRadians(blender.getRprogress(pPartialTick) * 360);
//        }
        this.model.renderToBuffer(poseStack, vertexConsumer, i, i1, -1);
        poseStack.popPose();
    }
}
