package com.renyigesai.immortalers_delight.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.renyigesai.immortalers_delight.block.support.SupportBlock;
import com.renyigesai.immortalers_delight.block.support.SupportBlockEntity;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class SupportBlockEntityRenderer implements BlockEntityRenderer<SupportBlockEntity> {
    public SupportBlockEntityRenderer(BlockEntityRendererProvider.Context pContext){

    }
    @Override
    public void render(SupportBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {

        pPoseStack.pushPose();
        pPoseStack.translate(0.5,1.5,0.5);
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack stack = new ItemStack(Items.ENDER_PEARL);
        BakedModel bakedModel = itemRenderer.getModel(stack,pBlockEntity.getLevel(),null,0);
        itemRenderer.render(stack, ItemDisplayContext.FIXED,true,pPoseStack,pBuffer,pPackedLight,pPackedOverlay,bakedModel);
        pPoseStack.popPose();

        pPoseStack.pushPose();
        //pPoseStack.translate(1,0,0);
        BlockRenderDispatcher blockRenderDispatcher = Minecraft.getInstance().getBlockRenderer();
        BlockState state = ImmortalersDelightBlocks.SUPPORT_BLOCK.get().defaultBlockState().setValue(SupportBlock.LIT, true);
        blockRenderDispatcher.renderSingleBlock(state,pPoseStack,pBuffer,pPackedLight,pPackedOverlay);
        pPoseStack.popPose();

    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
    public class ClientEventHandler {
        @SubscribeEvent
        public static void onClientEvent(FMLClientSetupEvent event){
            event.enqueueWork(()->{
                BlockEntityRenderers.register(ImmortalersDelightBlocks.SUPPORT_BLOCK_ENTITY.get(),SupportBlockEntityRenderer::new);
            });
        }

    }
}
