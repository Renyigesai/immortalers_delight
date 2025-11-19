package com.renyigesai.immortalers_delight.client.renderer.special_item;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.renyigesai.immortalers_delight.item.BoneKnifeItem;
import com.renyigesai.immortalers_delight.util.task.TimekeepingTask;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class BoneKnifeItemRenderer extends BlockEntityWithoutLevelRenderer {
    private static int degree = 0;
    private static Long LAST_TIME = 0L;
    public BoneKnifeItemRenderer(){
        super(null,null);
    }

    @Override
    public void renderByItem(ItemStack pStack, ItemDisplayContext pDisplayContext, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
//        if(degree==360){
//            degree=0;
//        }
//        if (TimekeepingTask.getImmortalTickTime()>LAST_TIME) {
//            degree++;
//            LAST_TIME = TimekeepingTask.getImmortalTickTime();
//        }

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        float partialTick = Minecraft.getInstance().getPartialTick();
        BakedModel bakedModel = itemRenderer.getModel(pStack,null,null,1);
        float ageInTicks = Minecraft.getInstance().player == null ? 0F : Minecraft.getInstance().player.tickCount + partialTick;
        float pullAmount = BoneKnifeItem.getPullingAmount(pStack, partialTick);

        pPoseStack.pushPose();

        float xOffset = 0;
        float yOffset = 0;
        float zOffset = 0;
        int xRotation = 0;
        int yRotation = 0;
        int zRotation = 0;
        float scale = 0.5f;
        if (pDisplayContext == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND) {
            xOffset = 12 / 32f;
            yOffset = 8 / 32f;
            zOffset = 1 / 32f;
            xRotation = 335;
            yRotation = 270;
            scale = 0.34f;
        }
        if (pullAmount > 0.5f) {
            pullAmount -= 0.5f;
            pullAmount *= 2;
            if (pullAmount <= 0.15f) yOffset -= pullAmount;
            if (pullAmount > 0.15f && pullAmount <= 0.85f) {
                yOffset += Math.sin(getRotation((pullAmount - 0.18f) * 280));
                if (pDisplayContext == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND) {
                    zRotation += (pullAmount - 0.15f) * 640;
                } else zRotation += (pullAmount - 0.15f) * 385;
            }
            if (pullAmount > 0.85f) {
                yOffset += pullAmount -1;
                if (pDisplayContext == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND) {
                    zRotation += 90;
                } else zRotation += 270;
            }
        }

        pPoseStack.translate(xOffset, yOffset, zOffset);
        pPoseStack.translate(scale,scale,scale);
        pPoseStack.mulPose(Axis.XP.rotationDegrees(xRotation));
        pPoseStack.mulPose(Axis.YP.rotationDegrees(yRotation));
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(zRotation));
        //pPoseStack.translate(xOffset, 0, zOffset);
        itemRenderer.render(pStack, ItemDisplayContext.NONE, false, pPoseStack, pBuffer, pPackedLight, pPackedOverlay, bakedModel);
        pPoseStack.popPose();
    }

    private static float getRotation(float pDegrees) {
        return pDegrees * ((float)Math.PI / 180F);
    }

}
