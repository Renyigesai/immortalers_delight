package com.renyigesai.immortalers_delight.client.renderer.entity.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.client.model.projectile.SurveyorFangModel;
import com.renyigesai.immortalers_delight.entities.projectile.PierceSoulEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.TridentModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class PierceSoulEntityRenderer extends EntityRenderer<PierceSoulEntity> {
    public static final ResourceLocation TRIDENT_LOCATION = new ResourceLocation(ImmortalersDelightMod.MODID,"textures/entity/projectile/pierce_soul.png");
    private final SurveyorFangModel model;

    public PierceSoulEntityRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new SurveyorFangModel(pContext.bakeLayer(SurveyorFangModel.SURVEYOR_FANG));
    }

    public void render(PierceSoulEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        pPoseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(pPartialTicks, pEntity.yRotO, pEntity.getYRot()) - 90.0F));
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(pPartialTicks, pEntity.xRotO, pEntity.getXRot()) + 90.0F));
        VertexConsumer vertexconsumer = ItemRenderer.getFoilBufferDirect(pBuffer, this.model.renderType(this.getTextureLocation(pEntity)), false, pEntity.isFoil());
        this.model.upSideDown();
        this.model.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack stack = pEntity.getTridentItem();
        pPoseStack.mulPose(Axis.YP.rotationDegrees(90.0F)); // 示例：绕X轴旋转90度
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(135.0F));
        BakedModel bakedModel = itemRenderer.getModel(stack,pEntity.level(),null,0);
        itemRenderer.render(stack, ItemDisplayContext.FIXED,true,pPoseStack,pBuffer,pPackedLight, OverlayTexture.NO_OVERLAY,bakedModel);


        pPoseStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getTextureLocation(PierceSoulEntity pEntity) {
        return TRIDENT_LOCATION;
    }
}
