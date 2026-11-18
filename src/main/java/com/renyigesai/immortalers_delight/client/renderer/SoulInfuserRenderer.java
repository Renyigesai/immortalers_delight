package com.renyigesai.immortalers_delight.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.block.soul_infuser.SoulInfuserAttackHelper;
import com.renyigesai.immortalers_delight.block.soul_infuser.SoulInfuserBlock;
import com.renyigesai.immortalers_delight.block.soul_infuser.SoulInfuserBlockEntity;
import com.renyigesai.immortalers_delight.client.model.SoulBoomModel;
import com.renyigesai.immortalers_delight.client.model.SoulInfuserModel;
import com.renyigesai.immortalers_delight.client.model.WarmingLayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class SoulInfuserRenderer implements BlockEntityRenderer<SoulInfuserBlockEntity> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(ImmortalersDelightMod.MODID, "textures/entity/soul_infuser.png");
    public static final ResourceLocation BOOM_TEXTURE = new ResourceLocation(ImmortalersDelightMod.MODID, "textures/entity/soul_boom.png");
    public static final ResourceLocation WARMING_SMALL_TEXTURE = new ResourceLocation(ImmortalersDelightMod.MODID, "textures/entity/warming/warming_small.png");
    public static final ResourceLocation WARMING_TEXTURE = new ResourceLocation(ImmortalersDelightMod.MODID, "textures/entity/warming/warming.png");
    public static final ResourceLocation WARMING_BIG_TEXTURE = new ResourceLocation(ImmortalersDelightMod.MODID, "textures/entity/warming/warming_big.png");
    public static final ResourceLocation SAFE_SMALL_TEXTURE = new ResourceLocation(ImmortalersDelightMod.MODID, "textures/entity/warming/safe_small.png");
    public static final ResourceLocation SAFE_TEXTURE = new ResourceLocation(ImmortalersDelightMod.MODID, "textures/entity/warming/safe.png");
    public static final ResourceLocation SAFE_BIG_TEXTURE = new ResourceLocation(ImmortalersDelightMod.MODID, "textures/entity/warming/safe_big.png");
    public static ResourceLocation getWarmingTexture(boolean isSafe,float r) {
        if (isSafe) {
            if (r <= 8) return SAFE_SMALL_TEXTURE;
            else if (r <= 16) return SAFE_TEXTURE;
            else return SAFE_BIG_TEXTURE;
        } else {
            if (r <= 8) return WARMING_SMALL_TEXTURE;
            else if (r <= 16) return WARMING_TEXTURE;
            else return WARMING_BIG_TEXTURE;
        }
    }
    private final SoulInfuserModel model;
    private final SoulBoomModel boomModel;
    private final WarmingLayerModel warmingModel;

    public SoulInfuserRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new SoulInfuserModel(context.bakeLayer(SoulInfuserModel.LAYER_LOCATION));
        this.boomModel = new SoulBoomModel(context.bakeLayer(SoulBoomModel.LAYER_LOCATION));
        this.warmingModel = new WarmingLayerModel(context.bakeLayer(WarmingLayerModel.LAYER_LOCATION));
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
        if (blockEntity.getBlockState().getValue(SoulInfuserBlock.AGE) != SoulInfuserAttackHelper.ID_NONE) {
            float animTime = (float) blockEntity.getActions().getAnimationTime() + partialTick;
            this.model.setupAnim(blockEntity,0,1,animTime,0.5f,0.5f);
        } else {
            this.model.resetAnim();
        }

        // 4. 执行渲染
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();

        //爆炸火球特效图层
        int animType = blockEntity.getActions().getAnimationType();
        if (animType == SoulInfuserAttackHelper.ID_AWAKEN || animType == SoulInfuserAttackHelper.ID_ATTACK_EXPLODE) {
            int startTime = 30 + animType * 3;

            if (blockEntity.getActions().getAnimationTime() > startTime && blockEntity.getActions().getAnimationTime() <= startTime + 20) {
                poseStack.pushPose();
                //修正倒转
                poseStack.translate(0.5D, 1.5D, 0.5D);
                poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));

                // 计算平滑插值 Tick
                float ageInTicks = (float) blockEntity.getActions().getAnimationTime() + partialTick - startTime;

                // 驱动动画逻辑（自动绕几何中心转动与悬浮）
                if (animType == SoulInfuserAttackHelper.ID_AWAKEN) this.boomModel.awakenAnim(0.0F, 0.0F, ageInTicks, 1.0f, 1.0f);
                else this.boomModel.explosionAnim(0.0F, 0.0F, ageInTicks, 1.0f, 1.0f);
                this.boomModel.setupAnim(0.0F, 0.0F, ageInTicks, 1.0f, 1.0f);

                VertexConsumer vertexConsumer2 = buffer.getBuffer(ModRenderTypes.UNLIT_TRANSLUCENT.apply(BOOM_TEXTURE));
                this.boomModel.renderToBuffer(poseStack, vertexConsumer2, 0xF000F0, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
                poseStack.popPose();
            }
        }

        //AOE警告特效图层
        if (animType == SoulInfuserAttackHelper.ID_AWAKEN || animType == SoulInfuserAttackHelper.ID_ATTACK_EXPLODE || animType == SoulInfuserAttackHelper.ID_ATTACK_FIRE_RING) {
            int endTime = 55 - 5 * animType;

            if (blockEntity.getActions().getAnimationTime() > 0 && blockEntity.getActions().getAnimationTime() <= endTime) {
                poseStack.pushPose();
                System.out.println("开始渲染警告图层");
                //修正倒转
                poseStack.translate(0.5D, 0.5D, 0.5D);
                poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
                // 计算平滑插值 Tick
                float ageInTicks = (float) blockEntity.getActions().getAnimationTime() + partialTick;

                // 驱动动画逻辑
                float range = 4;
                if (animType == SoulInfuserAttackHelper.ID_AWAKEN) {
                    range = this.warmingModel.setupAnim(6,15,ageInTicks,endTime,3);
                }
                if (animType == SoulInfuserAttackHelper.ID_ATTACK_EXPLODE) {
                    range = this.warmingModel.setupAnim(4,10,ageInTicks,endTime,4.5f);
                }
                if (animType == SoulInfuserAttackHelper.ID_ATTACK_FIRE_RING) {
                    range = this.warmingModel.setupAnim(10,25,ageInTicks,endTime,6);
                }

                VertexConsumer vertexConsumer2 = buffer.getBuffer(ModRenderTypes.UNLIT_TRANSLUCENT.apply(getWarmingTexture(false,range)));
                this.warmingModel.renderToBuffer(poseStack, vertexConsumer2, 0xF000F0, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.5F);

                if (animType == SoulInfuserAttackHelper.ID_ATTACK_FIRE_RING) {
                    range = this.warmingModel.setupAnim(8,11,ageInTicks,endTime,6);
                    VertexConsumer vertexConsumer3 = buffer.getBuffer(ModRenderTypes.UNLIT_TRANSLUCENT.apply(getWarmingTexture(true,range)));
                    this.warmingModel.renderToBuffer(poseStack, vertexConsumer3, 0xF000F0, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.8F);
                }
                poseStack.popPose();
            }
        }
    }

    //视角剔除
    @Override
    public boolean shouldRenderOffScreen(@NotNull SoulInfuserBlockEntity pBlockEntity) {
//        int animType = pBlockEntity.getActions().getAnimationType();
//        //在出现警告层这种巨幅渲染的时候不应用视角剔除，以免背向的时候看不到警告层
//        if (animType == SoulInfuserAttackHelper.ID_AWAKEN
//                || animType == SoulInfuserAttackHelper.ID_ATTACK_EXPLODE
//                || animType == SoulInfuserAttackHelper.ID_ATTACK_FIRE_RING) {
//            System.out.println("正确的动画类型");
            return true;
//        }
//        return BlockEntityRenderer.super.shouldRenderOffScreen(pBlockEntity);
    }
    //无视渲染距离
    @Override
    public boolean shouldRender(@NotNull SoulInfuserBlockEntity pBlockEntity, @NotNull Vec3 pCameraPos) {
        int animType = pBlockEntity.getActions().getAnimationType();
        //在出现警告层这种巨幅渲染的时候不应用视角剔除，以免背向的时候看不到警告层
        if (animType == SoulInfuserAttackHelper.ID_AWAKEN
                || animType == SoulInfuserAttackHelper.ID_ATTACK_EXPLODE
                || animType == SoulInfuserAttackHelper.ID_ATTACK_FIRE_RING) {
            return true;
        }
        return BlockEntityRenderer.super.shouldRender(pBlockEntity,pCameraPos);
    }
}
