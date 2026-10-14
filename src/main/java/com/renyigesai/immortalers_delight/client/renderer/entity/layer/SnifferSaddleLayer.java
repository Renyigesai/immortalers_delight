package com.renyigesai.immortalers_delight.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.renyigesai.immortalers_delight.api.ISnifferSaddleData;
import com.renyigesai.immortalers_delight.client.model.entity.SnifferSaddleModel;
import com.renyigesai.immortalers_delight.client.model.entity.SnifferSaddleUpgradedModel;
import com.renyigesai.immortalers_delight.item.SnifferSaddleItem;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.SnifferModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.sniffer.Sniffer;

public class SnifferSaddleLayer extends RenderLayer<Sniffer, SnifferModel<Sniffer>> {
    private static final ResourceLocation SADDLE_TEXTURE = new ResourceLocation("immortalers_delight", "textures/entity/sniffer_saddle.png");
    private static final ResourceLocation SADDLE_UPGRADED_TEXTURE = new ResourceLocation("immortalers_delight", "textures/entity/sniffer_saddle_upgraded.png");
    
    private final SnifferSaddleModel<Sniffer> saddleModel;
    private final SnifferSaddleUpgradedModel<Sniffer> saddleUpgradedModel;

    public SnifferSaddleLayer(RenderLayerParent<Sniffer, SnifferModel<Sniffer>> parent, 
                             SnifferSaddleModel<Sniffer> saddleModel,
                             SnifferSaddleUpgradedModel<Sniffer> saddleUpgradedModel) {
        super(parent);
        this.saddleModel = saddleModel;
        this.saddleUpgradedModel = saddleUpgradedModel;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, Sniffer sniffer, 
                      float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        // 检查嗅探兽是否装备了鞍
        if (SnifferSaddleItem.hasSaddle(sniffer)) {
            poseStack.pushPose();
            // 策划要求：整个鞍具放大1.2倍
            poseStack.scale(1.2F, 1.2F, 1.2F);
            
            // 检查是否升级
            boolean isUpgraded = sniffer.getEntityData().get(((ISnifferSaddleData)sniffer).immortalersDelight$getSaddleUpgradedAccessor());
            
            // 根据升级状态选择模型和纹理
            EntityModel<Sniffer> currentModel = isUpgraded ? this.saddleUpgradedModel : this.saddleModel;
            ResourceLocation texture = isUpgraded ? SADDLE_UPGRADED_TEXTURE : SADDLE_TEXTURE;
            
            VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(texture));
            currentModel.prepareMobModel(sniffer, limbSwing, limbSwingAmount, partialTick);
            this.getParentModel().copyPropertiesTo(currentModel);
            currentModel.setupAnim(sniffer, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            currentModel.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            
            poseStack.popPose();
        }
    }
}
