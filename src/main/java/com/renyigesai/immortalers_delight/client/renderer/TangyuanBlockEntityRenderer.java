package com.renyigesai.immortalers_delight.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.renyigesai.immortalers_delight.block.support.SupportBlockEntity;
import com.renyigesai.immortalers_delight.block.tangyuan.TangyuanBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import vectorwing.farmersdelight.common.registry.ModBlocks;

public class TangyuanBlockEntityRenderer implements BlockEntityRenderer<TangyuanBlockEntity> {
    public TangyuanBlockEntityRenderer(BlockEntityRendererProvider.Context pContext){

    }
    @Override
    public void render(TangyuanBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        pPoseStack.pushPose();
        //pPoseStack.translate(0,0,0);
        BlockRenderDispatcher blockRenderDispatcher = Minecraft.getInstance().getBlockRenderer();
        BlockState state = pBlockEntity.getItem(TangyuanBlockEntity.CONTAINER_SLOT).isEmpty() ? ModBlocks.CUTTING_BOARD.get().defaultBlockState() : ModBlocks.COOKING_POT.get().defaultBlockState();
        blockRenderDispatcher.renderSingleBlock(state,pPoseStack,pBuffer,pPackedLight,pPackedOverlay);
        pPoseStack.popPose();

        //这一部分为渲染容器中的物品
        pPoseStack.pushPose();
        pPoseStack.translate(0.5,1,0.5);
        // 抵消模型的旋转，使其与相机视角对齐
        pPoseStack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
        // 修正物品的默认旋转（部分物品需要180度Y轴旋转才能正面朝向）
        pPoseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        // 缩放（根据需要调整钻石大小）
        pPoseStack.scale(0.5F, 0.5F, 0.5F); // 缩小到50%，避免过大
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack stack = pBlockEntity.getItem(TangyuanBlockEntity.OUTPUT_SLOT);
        BakedModel bakedModel = itemRenderer.getModel(stack,pBlockEntity.getLevel(),null,0);
        itemRenderer.render(stack, ItemDisplayContext.FIXED,true,pPoseStack,pBuffer,pPackedLight,pPackedOverlay,bakedModel);
        pPoseStack.popPose();
    }
}
