package com.renyigesai.immortalers_delight.client.renderer.entity.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.client.model.projectile.SoulFireballEntityModel;
import com.renyigesai.immortalers_delight.client.renderer.ModRenderTypes;
import com.renyigesai.immortalers_delight.entities.projectile.SoulFireballEntity;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SoulFireballEntityRenderer extends EntityRenderer<SoulFireballEntity> {
    private static final ResourceLocation LOCATION = new ResourceLocation(ImmortalersDelightMod.MODID,"textures/entity/soul_fireball.png");
    private final SoulFireballEntityModel model;
    public SoulFireballEntityRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new SoulFireballEntityModel<>(pContext.bakeLayer(SoulFireballEntityModel.LAYER_LOCATION));
    }

    public void render(SoulFireballEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {

        pPoseStack.pushPose();
        pPoseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(pPartialTicks, pEntity.yRotO, pEntity.getYRot()) - 90.0F));
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(pPartialTicks, pEntity.xRotO, pEntity.getXRot()) + 90.0F));
        // 标记：实体是否处于"发光且隐身"状态（如玩家隐身但持发光物品）
        Minecraft minecraft = Minecraft.getInstance();
        boolean flag = minecraft.shouldEntityAppearGlowing(pEntity) && pEntity.isInvisible();
        VertexConsumer vertexconsumer; // 顶点数据消费者，用于向缓冲区写入渲染数据
        ResourceLocation location = this.getTextureLocation(pEntity); // 纹理路径
        // 渲染条件：实体非隐身 或 处于"发光且隐身"状态（保证发光隐身时仍能看到外层轮廓）
        if (!pEntity.isInvisible() || flag) {
            // 根据渲染状态选择对应的渲染类型
            if (flag) {
                // 发光隐身状态：使用轮廓渲染类型（仅绘制外层轮廓）
                vertexconsumer = pBuffer.getBuffer(RenderType.outline(location));
            } else {
                // 正常状态：使用剔除背面实体渲染类型
                vertexconsumer = pBuffer.getBuffer(RenderType.entityTranslucentCull(location));
            }
        } else vertexconsumer = pBuffer.getBuffer(this.model.renderType(location));
//        VertexConsumer vertexconsumer = ItemRenderer.getFoilBufferDirect(pBuffer, this.model.renderType(this.getTextureLocation(pEntity)), false, pEntity.isFoil());
        //让模型翻转过来
        this.model.setupAnim(pPartialTicks,0,180,0, -8,0);
        this.model.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        pPoseStack.mulPose(Axis.XP.rotationDegrees(-90));
        pPoseStack.translate(-0.5D, 0.25D, -0.75D);
        pPoseStack.scale(0.99F, 0.99F, 0.99F);
        //渲染一个方块模型
        BlockRenderDispatcher blockRenderDispatcher = Minecraft.getInstance().getBlockRenderer();
        BlockState state = ImmortalersDelightBlocks.OBSIDIAN_WALNUT.get().defaultBlockState().setValue(BlockStateProperties.AGE_7,6);
        blockRenderDispatcher.renderSingleBlock(state,pPoseStack,pBuffer,pPackedLight,OverlayTexture.NO_OVERLAY);
//        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
//        ItemStack stack = pEntity.getTridentItem();
//        pPoseStack.mulPose(Axis.YP.rotationDegrees(90.0F)); // 示例：绕X轴旋转90度
//        pPoseStack.mulPose(Axis.ZP.rotationDegrees(135.0F));
//        BakedModel bakedModel = itemRenderer.getModel(stack,pEntity.level(),null,0);
//        itemRenderer.render(stack, ItemDisplayContext.FIXED,true,pPoseStack,pBuffer,pPackedLight, OverlayTexture.NO_OVERLAY,bakedModel);

        pPoseStack.popPose();

//        // 1. 保存当前矩阵堆栈状态（入栈），避免影响后续其他实体的渲染
//        pPoseStack.pushPose();
//        // 2. 对模型进行缩放变换：X轴和Y轴翻转（-1.0F），Z轴保持不变，修正模型的朝向
//        pPoseStack.scale(-1.0F, -1.0F, 1.0F);
//        // 3. 计算偏航角（水平旋转）的平滑插值，基于部分游戏刻，实现旋转动作的平滑过渡
//        float f = Mth.rotLerp(pPartialTicks, pEntity.yRotO, pEntity.getYRot());
//        // 4. 计算俯仰角（垂直旋转）的平滑插值，实现上下摆动的平滑过渡
//        float f1 = Mth.lerp(pPartialTicks, pEntity.xRotO, pEntity.getXRot());
//        VertexConsumer vertexconsumer2 = pBuffer.getBuffer(RenderType.entityCutout(this.getTextureLocation(pEntity)));
//        this.model.setupAnim(0.0F, f, f1);
//        this.model.renderToBuffer(pPoseStack, vertexconsumer2, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
//        this.model.getAll().translateAndRotate(pPoseStack);
//        pPoseStack.popPose();
//
//        pPoseStack.pushPose();

//        pPoseStack.popPose();


        //pPoseStack.mulPose(Axis.YP.rotationDegrees(90.0F));

        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }

    /**
     * 获取凋灵头颅对应的方块光照等级（用于渲染时的光照计算）
     * 凋灵头颅作为特殊投射物，强制返回最高光照等级15，确保在任何场景下都清晰可见
     * @param pEntity 凋灵头颅实体实例
     * @param pPos 凋灵头颅所在的方块坐标
     * @return 固定返回15（最高光照等级，无明暗衰减）
     */
    protected int getBlockLightLevel(SoulFireballEntity pEntity, BlockPos pPos) {
        return 15;
    }
   public ResourceLocation getTextureLocation(SoulFireballEntity pEntity) {
        return LOCATION;
    }
}
