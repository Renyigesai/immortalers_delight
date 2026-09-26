package com.renyigesai.immortalers_delight.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.client.model.projectile.ScreenLayerParticleModel;
import com.renyigesai.immortalers_delight.item.food.AlfalfaPopsicleItem;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = ImmortalersDelightMod.MODID, value = Dist.CLIENT)
public class AlfalfaPopsicleRingEffectRenderer {
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(ImmortalersDelightMod.MODID, "textures/entity/custom/green_layer.png");
    private static ScreenLayerParticleModel<?> model = null;

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        // 建议在半透明物体渲染后渲染，以支持 Alpha 混合
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) {
            return;
        }

        // 初始化模型
        if (model == null) {
            model = new ScreenLayerParticleModel<>(mc.getEntityModels().bakeLayer(ScreenLayerParticleModel.SCREEN_LAYER_PARTICLE));
        }

        Camera camera = event.getCamera();
        Vec3 cameraPos = camera.getPosition();
        PoseStack poseStack = event.getPoseStack();
        float partialTick = event.getPartialTick();
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();

        if (shouldRenderEffect(mc.player)) renderRingForPlayer(poseStack, bufferSource, mc.player, cameraPos, partialTick);
        // 遍历世界中的所有玩家（若只想针对本地玩家渲染，可直接使用 mc.player）
//        for (Player player : mc.level.players()) {
//            if (shouldRenderEffect(player)) {
//
//            }
//        }
    }

    private static boolean shouldRenderEffect(Player player) {
        return player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof AlfalfaPopsicleItem
                || player.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof AlfalfaPopsicleItem;
    }

    private static void renderRingForPlayer(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource,
                                            Player player, Vec3 cameraPos, float partialTick) {
        // 1. 计算玩家插值坐标（消除帧率不匹配导致的画面抖动）
        double playerX = Mth.lerp(partialTick, player.xOld, player.getX());
        double playerY = Mth.lerp(partialTick, player.yOld, player.getY());
        double playerZ = Mth.lerp(partialTick, player.zOld, player.getZ());
        float playerRotY = Mth.lerp(partialTick, player.yRotO, player.getYRot());

        // 2. 将坐标原点平移到玩家所在的世界相对位置
        poseStack.pushPose();
        poseStack.translate(playerX - cameraPos.x, playerY - cameraPos.y, playerZ - cameraPos.z);

        // 3. 按照玩家朝向旋转
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F - playerRotY));
        poseStack.scale(-1.0F, -1.0F, 1.0F);

        // 4. 依次绘制 8 个方位的环状模型
        renderSingleModel(poseStack, bufferSource, -309f / 16, -1.5f, 0, 90);
        renderSingleModel(poseStack, bufferSource, 309f / 16, -1.5f, 0, 90);
        renderSingleModel(poseStack, bufferSource, 0, -1.5f, -309f / 16, 0);
        renderSingleModel(poseStack, bufferSource, 0, -1.5f, 309f / 16, 0);
        renderSingleModel(poseStack, bufferSource, 218.5f / 16, -1.5f, 218.5f / 16, 45);
        renderSingleModel(poseStack, bufferSource, -218.5f / 16, -1.5f, -218.5f / 16, 45);
        renderSingleModel(poseStack, bufferSource, 218.5f / 16, -1.5f, -218.5f / 16, 135);
        renderSingleModel(poseStack, bufferSource, -218.5f / 16, -1.5f, 218.5f / 16, 135);

        poseStack.popPose();
    }

    private static void renderSingleModel(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource,
                                          float dx, float dy, float dz, int degrees) {
        if (model == null) return;

        poseStack.pushPose();
        poseStack.translate(dx, dy, dz);
        if (degrees != 0) {
            poseStack.mulPose(Axis.YP.rotationDegrees(degrees));
        }

        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityTranslucent(TEXTURE_LOCATION));
        // 15728880 为最大亮度 (packedLight)
        model.renderToBuffer(poseStack, vertexConsumer, 15728880, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
    }
}
