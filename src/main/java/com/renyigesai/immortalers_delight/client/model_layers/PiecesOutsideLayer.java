package com.renyigesai.immortalers_delight.client.model_layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.renyigesai.immortalers_delight.client.model.projectile.PiecesOutsideLayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class PiecesOutsideLayer <T extends Entity, M extends PiecesOutsideLayerModel<T>> extends RenderLayer<T, M> {
    private static final RenderType PIECES_OUTSIDE = RenderType.entityTranslucentEmissive(new ResourceLocation("textures/entity/enderman/enderman_eyes.png"));
    private static final RenderType XIA_PIECES_OUTSIDE = RenderType.entityTranslucentCull(new ResourceLocation("textures/entity/enderman/enderman_eyes.png"));

    public PiecesOutsideLayer(RenderLayerParent<T, M> pRenderer) {super(pRenderer);}

    @Override
    public void render(@NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBuffer, int pPackedLight, @NotNull T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {

    }


    public RenderType renderType(T pLivingEntity) {
        return PIECES_OUTSIDE;
    }
}
