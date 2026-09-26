package com.renyigesai.immortalers_delight.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.block.soul_infuser.SoulInfuserBlock;
import com.renyigesai.immortalers_delight.block.soul_infuser.SoulInfuserBlockEntity;
import com.renyigesai.immortalers_delight.client.model.SoulBoomModel;
import com.renyigesai.immortalers_delight.client.model.SoulInfuserModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class SoulInfuserRenderer implements BlockEntityRenderer<SoulInfuserBlockEntity> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(ImmortalersDelightMod.MODID, "textures/entity/soul_infuser.png");
    public static final ResourceLocation BOOM_TEXTURE = new ResourceLocation(ImmortalersDelightMod.MODID, "textures/entity/soul_boom.png");
    private final SoulInfuserModel model;
    private final SoulBoomModel layerModel;

    public SoulInfuserRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new SoulInfuserModel(context.bakeLayer(SoulInfuserModel.LAYER_LOCATION));
        this.layerModel = new SoulBoomModel(context.bakeLayer(SoulBoomModel.LAYER_LOCATION));
    }

    @Override
    public void render(SoulInfuserBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        //身体图层
        poseStack.pushPose();

        //修正倒转
        poseStack.translate(0.5D, 1.5D, 0.5D);
        poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));

        //按照方块的facing旋转
        Direction facing = blockEntity.getBlockState().getValue(SoulInfuserBlock.FACING);
        poseStack.mulPose(Axis.YP.rotationDegrees(-facing.toYRot()));

        //动画
        if (blockEntity.getBlockState().getValue(SoulInfuserBlock.LIT)) {
//            float time = (blockEntity.getLevel() != null ? blockEntity.getLevel().getGameTime() : 0) + partialTick;
//            this.model.getRingUp().yRot = time * 0.05F;
//            this.model.getRingDown().yRot = -time * 0.05F;
        } else {
//            this.model.getRingUp().yRot = 0.0F;
//            this.model.getRingDown().yRot = 0.0F;
        }

        // 4. 执行渲染
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();

        //爆炸火球特效图层
        if (blockEntity.getAnimationTime() > 50 && blockEntity.getAnimationTime() <= 70) {
            poseStack.pushPose();
            // 计算平滑插值 Tick
            float ageInTicks = (float) blockEntity.getAnimationTime() + partialTick - 50;

            // 驱动动画逻辑（自动绕几何中心转动与悬浮）
            this.layerModel.explosionAnim(0.0F, 0.0F, ageInTicks, 1.0f, 1.0f);
            this.layerModel.setupAnim(0.0F, 0.0F, ageInTicks, 1.0f, 1.0f);

            VertexConsumer vertexConsumer2 = buffer.getBuffer(ModRenderTypes.UNLIT_TRANSLUCENT.apply(BOOM_TEXTURE));
            this.layerModel.renderToBuffer(poseStack, vertexConsumer2, 0xF000F0, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            poseStack.popPose();
        }

    }
}
