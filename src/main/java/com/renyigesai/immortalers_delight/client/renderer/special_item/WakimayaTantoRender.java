package com.renyigesai.immortalers_delight.client.renderer.special_item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.item.weapon.WakimayaTantoItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;

import java.util.function.Function;

public class WakimayaTantoRender extends BlockEntityWithoutLevelRenderer {
    public WakimayaTantoRender(BlockEntityRenderDispatcher pBlockEntityRenderDispatcher, EntityModelSet pEntityModelSet) {
        super(pBlockEntityRenderDispatcher, pEntityModelSet);
    }

    @Override
    public void renderByItem(ItemStack pStack, ItemDisplayContext pDisplayContext,
                             PoseStack poseStack, MultiBufferSource buffer,
                             int light, int overlay) {
        Item item = pStack.getItem();
        if (!(item instanceof WakimayaTantoItem)) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return;

        float partialTick = mc.getPartialTick();
        float progress = Mth.clamp(WakimayaTantoItem.getProgress(pStack, partialTick), 0f, 1f);

        ItemRenderer itemRenderer = mc.getItemRenderer();
        BakedModel model = mc.getModelManager().getModel(
                new ResourceLocation(ImmortalersDelightMod.MODID, "item/wakimaya_tanto_render"));
        RenderType renderType = Sheets.translucentItemSheet();

        boolean firstPerson = pDisplayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND
                || pDisplayContext == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND;

        poseStack.pushPose();

        if (firstPerson && progress > 0f && progress < 0.5f) {
            // ================== 前半段（0 ~ 0.5）：现有动画 ==================
            float zRot0 = remapProgress(0f, 0.25f, progress,
                    time -> easeOutCubic(time) * -360f);
            float yOffset0 = remapProgress(0f, 0.25f, progress,
                    time -> (float) Math.sin(time * Math.PI) * 0.5f);
            float xOffset0 = remapProgress(0.25f, 0.35f, progress, WakimayaTantoRender::easeOutCubic);
            float xOffset1 = remapProgress(0.35f, 0.45f, progress,
                    time -> easeOutCubic(time) * -2.5f);
            float yOffset1 = remapProgress(0.35f, 0.45f, progress,
                    time -> easeOutCubic(time) * 0.5f);
            float zOffset0 = remapProgress(0.35f, 0.45f, progress,
                    time -> easeOutCubic(time) * 0.5f);
            float zRot1 = remapProgress(0.35f, 0.45f, progress,
                    time -> easeOutCubic(time) * -135f);

            // ================== 后半段（0.5 ~ 1.0）：骨刀旋转效果 ==================
            // 把 progress 的 0.5~1.0 映射到 0~1
            float t = Mth.clamp((progress - 0.5f) * 2f, 0f, 1f);

            float yOffset2 = 0f;
            float zRot2 = 0f;

            // 抛刀前蓄力：0 ~ 0.15
            if (t <= 0.15f) {
                yOffset2 -= t;
            }

            // 抛刀：0.15 ~ 0.85
            if (t > 0.15f && t <= 0.85f) {
                yOffset2 += (float) Math.sin(Math.toRadians((t - 0.18f) * 280));  // ★ 补上
                zRot2 += (t - 0.15f) * 640f;   // ★ 第一人称的旋转速度
            }

            // 接刀：0.85 ~ 1.0
            if (t > 0.85f) {
                yOffset2 += t - 1f;
                zRot2 += 90f;
            }

            // ================== 应用变换 ==================
            poseStack.translate(0.5, 0.5, 0.5);
            poseStack.translate(zOffset0,
                    yOffset0 + xOffset0 + xOffset1 + yOffset2,
                    yOffset1);
            poseStack.mulPose(Axis.ZP.rotationDegrees(zRot0 + zRot1 + zRot2));
            poseStack.translate(-0.5, -0.5, -0.5);
        }
        // 非第一人称：不做任何变换，直接渲染

        itemRenderer.renderModelLists(model, pStack, light, overlay,
                poseStack, buffer.getBuffer(renderType));

        poseStack.popPose();
    }

    private static float easeOutCubic(float t) {
        float inv = 1f - t;
        return 1f - inv * inv * inv;
    }

    public float remapProgress(float from, float to, float progress, Function<Float,Float> transform){
        if (from == to){
            return transform.apply(0f);
        }
        float interval = Mth.clamp((progress - from) / (to - from),0f,1f);
        return transform.apply(interval);
    }

}
