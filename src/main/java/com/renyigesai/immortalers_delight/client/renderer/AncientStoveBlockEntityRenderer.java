package com.renyigesai.immortalers_delight.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.renyigesai.immortalers_delight.block.ancient_stove.AncientStoveBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.items.ItemStackHandler;
import vectorwing.farmersdelight.common.block.StoveBlock;

public class AncientStoveBlockEntityRenderer implements BlockEntityRenderer<AncientStoveBlockEntity> {
    public AncientStoveBlockEntityRenderer(BlockEntityRendererProvider.Context context){

    }
    @Override
    public void render(AncientStoveBlockEntity cncientStoveBlockEntity, float v, PoseStack poseStack, MultiBufferSource buffer, int combinedOverlayIn, int i1) {
        Direction direction = ((Direction)cncientStoveBlockEntity.getBlockState().getValue(StoveBlock.FACING)).getOpposite();
        ItemStackHandler inventory = cncientStoveBlockEntity.getInventory();
        int posLong = (int)cncientStoveBlockEntity.getBlockPos().asLong();

        for(int i = 0; i < inventory.getSlots(); ++i) {
            ItemStack stoveStack = inventory.getStackInSlot(i);
            if (!stoveStack.isEmpty()) {
                poseStack.pushPose();
                poseStack.translate(0.5, 1.02, 0.5);
                float f = -direction.toYRot();
                poseStack.mulPose(Axis.YP.rotationDegrees(f));
                poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
                Vec2 itemOffset = cncientStoveBlockEntity.getStoveItemOffset(i);
                poseStack.translate((double)itemOffset.x, (double)itemOffset.y, 0.0);
                poseStack.scale(0.375F, 0.375F, 0.375F);
                if (cncientStoveBlockEntity.getLevel() != null) {
                    Minecraft.getInstance().getItemRenderer().renderStatic(stoveStack, ItemDisplayContext.FIXED, LevelRenderer.getLightColor(cncientStoveBlockEntity.getLevel(), cncientStoveBlockEntity.getBlockPos().above()), combinedOverlayIn, poseStack, buffer, cncientStoveBlockEntity.getLevel(), posLong + i);
                }

                poseStack.popPose();
            }
        }
    }
}
