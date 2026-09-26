package com.renyigesai.immortalers_delight.client.model_layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.renyigesai.immortalers_delight.client.model.BakaHelmModel;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BakaHelmLayer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("immortalers_delight", "textures/entity/baka_heml.png");
    private final BakaHelmModel<T> model;

    public BakaHelmLayer(RenderLayerParent<T, M> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.model = new BakaHelmModel<>(modelSet.bakeLayer(BakaHelmModel.LAYER_LOCATION));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity,
                       float limbSwing, float limbSwingAmount, float partialTicks,
                       float ageInTicks, float netHeadYaw, float headPitch) {

        // 1. 在这里做渲染条件判断（例如检测是否穿戴了特定饰品/头盔/拥有特定状态）

         if (!shouldRender(entity)) return;

        // 2. 将父级 HumanoidModel 的头部旋转和位移同步给当前附加模型的 head 部件
        this.getParentModel().getHead().translateAndRotate(poseStack);

        // 3. 如果使用的是 copyFrom 方式同步姿态（二选一即可）：
        // this.model.head.copyFrom(this.getParentModel().getHead());

        poseStack.pushPose();

        // 获取顶点缓冲并渲染
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        this.model.head.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
    }

    private boolean shouldRender(T entity) {
        return entity.getItemBySlot(EquipmentSlot.HEAD).is(ImmortalersDelightItems.BAKA_SANDWICH.get());
    }
}
