package com.renyigesai.immortalers_delight.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.client.model.ScavengerModel;
import com.renyigesai.immortalers_delight.entities.living.illager_archaeological_team.Scavenger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.IllagerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.IllagerRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

public class ScavengerRenderer extends IllagerRenderer<Scavenger> {
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(ImmortalersDelightMod.MODID, "textures/entity/scavenger.png");
    private static int degree = 0;
    public ScavengerRenderer(EntityRendererProvider.Context context) {
        super(context, new IllagerModel<>(context.bakeLayer(ScavengerModel.SCARVENGER_MODEL)),
                0.5f);
        //this.addLayer(new CustomHeadLayer(this, context.getModelSet(), context.getItemInHandRenderer()));
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()) {

            @Override
            public void render(PoseStack pPoseStack, MultiBufferSource vertexConsumerProvider, int i, Scavenger vindicatorEntity, float f, float g, float h, float j, float k, float l) {
                if (vindicatorEntity.getSpellCastingType() == 3) {
                    if(degree==30){
                        degree=0;
                    }
                    degree++;

                    if (!vindicatorEntity.isTeleporting()) {
                        pPoseStack.pushPose();
                        pPoseStack.translate(0.0F, 0.5F, 0.0F);
                        float xOffset = -1 / 32f;
                        float zOffset = 0;
                        pPoseStack.translate(-xOffset, 0, -zOffset);
                        pPoseStack.mulPose(Axis.YP.rotationDegrees(degree * 6));
                        pPoseStack.translate(xOffset, 0, zOffset);
                        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
                        ItemStack stack = new ItemStack(Items.ENDER_PEARL);
                        BakedModel bakedModel = itemRenderer.getModel(stack,vindicatorEntity.level(),null,0);
                        itemRenderer.render(stack, ItemDisplayContext.FIXED,true,pPoseStack,vertexConsumerProvider,i, OverlayTexture.NO_OVERLAY,bakedModel);
                        pPoseStack.popPose();
                    }
                }
                if (vindicatorEntity.getSpellCastingType() != 3) {
                    super.render(pPoseStack, vertexConsumerProvider, i, (Scavenger) vindicatorEntity, f, g, h, j, k, l);
                }
            }
        });
        this.model.getHat().visible = true;
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Scavenger vindicatorEntity) {
        return TEXTURE_LOCATION;
    }
}