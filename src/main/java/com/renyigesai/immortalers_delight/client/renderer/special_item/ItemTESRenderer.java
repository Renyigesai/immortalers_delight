package com.renyigesai.immortalers_delight.client.renderer.special_item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.client.model.AlfalfaDababaModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class ItemTESRenderer extends BlockEntityWithoutLevelRenderer {
    //这个类用于处理使用实体模型的物品的模型
    //private static final AlfalfaDababaModel ALFALFA_DABABA_MODEL= new AlfalfaDababaModel<>();
    private static final ResourceLocation ALFALFA_DABABA_TEXTURE = new ResourceLocation(ImmortalersDelightMod.MODID,"textures/item/alfalfa_dababa.png");

    public ItemTESRenderer(BlockEntityRenderDispatcher pBlockEntityRenderDispatcher, EntityModelSet pEntityModelSet) {
        super(pBlockEntityRenderDispatcher, pEntityModelSet);
    }

    @Override
    public void renderByItem(ItemStack pStack, ItemDisplayContext pDisplayContext, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {

    }
}
