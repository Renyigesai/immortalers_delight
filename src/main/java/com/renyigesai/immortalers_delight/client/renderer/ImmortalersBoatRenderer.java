package com.renyigesai.immortalers_delight.client.renderer;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.entities.ImmortalersBoat;
import com.renyigesai.immortalers_delight.entities.ImmortalersChestBoat;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.Boat;
import org.joml.Quaternionf;

import java.util.Map;
import java.util.stream.Stream;

public class ImmortalersBoatRenderer extends EntityRenderer<ImmortalersBoat> {
    private final Map<ImmortalersBoat.Type, Pair<ResourceLocation, ListModel<Boat>>> boatResources;

    public ImmortalersBoatRenderer(EntityRendererProvider.Context pContext, boolean pChestBoat) {
        super(pContext);
        this.shadowRadius = 0.8F;
        this.boatResources = Stream.of(ImmortalersBoat.Type.values()).collect(ImmutableMap.toImmutableMap((p_173938_) -> p_173938_, (p_247941_) -> Pair.of(new ResourceLocation(getTextureLocation(p_247941_, pChestBoat)), this.createBoatModel(pContext, p_247941_, pChestBoat))));
    }


    private ListModel<Boat> createBoatModel(EntityRendererProvider.Context pContext, ImmortalersBoat.Type pType, boolean pChestBoat) {
        ModelLayerLocation modellayerlocation = pChestBoat ? createChestBoatModelName(pType) : createBoatModelName(pType);
        ModelPart modelpart = pContext.bakeLayer(modellayerlocation);
        return (pChestBoat ? new ChestBoatModel(modelpart) : new BoatModel(modelpart));
    }
    public static ModelLayerLocation createBoatModelName(ImmortalersBoat.Type pType) {
        return createLocation("boat/" + pType.getName(), "main");
    }
    public static ModelLayerLocation createChestBoatModelName(ImmortalersBoat.Type pType) {
        return createLocation("chest_boat/" + pType.getName(), "main");
    }
    private static ModelLayerLocation createLocation(String pPath, String pModel) {
        return new ModelLayerLocation(new ResourceLocation(ImmortalersDelightMod.MODID, pPath), pModel);
    }
    private static String getTextureLocation(ImmortalersBoat.Type pType, boolean pChestBoat) {
        return pChestBoat ? "textures/entity/chest_boat/" + pType.getName() + ".png" : "textures/entity/boat/" + pType.getName() + ".png";
    }

    public void render(ImmortalersBoat pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        pPoseStack.translate(0.0F, 0.375F, 0.0F);
        pPoseStack.mulPose(Axis.YP.rotationDegrees(180.0F - pEntityYaw));
        float f = (float)pEntity.getHurtTime() - pPartialTicks;
        float f1 = pEntity.getDamage() - pPartialTicks;
        if (f1 < 0.0F) {
            f1 = 0.0F;
        }

        if (f > 0.0F) {
            pPoseStack.mulPose(Axis.XP.rotationDegrees(Mth.sin(f) * f * f1 / 10.0F * (float)pEntity.getHurtDir()));
        }

        float f2 = pEntity.getBubbleAngle(pPartialTicks);
        if (!Mth.equal(f2, 0.0F)) {
            pPoseStack.mulPose((new Quaternionf()).setAngleAxis(pEntity.getBubbleAngle(pPartialTicks) * 0.017453292F, 1.0F, 0.0F, 1.0F));
        }

        Pair<ResourceLocation, ListModel<Boat>> pair = this.getModelWithLocation(pEntity);
        ResourceLocation resourcelocation = pair.getFirst();
        ListModel<Boat> listmodel = pair.getSecond();
        pPoseStack.scale(-1.0F, -1.0F, 1.0F);
        pPoseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
        listmodel.setupAnim(pEntity, pPartialTicks, 0.0F, -0.1F, 0.0F, 0.0F);
        VertexConsumer vertexconsumer = pBuffer.getBuffer(listmodel.renderType(resourcelocation));
        listmodel.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        if (!pEntity.isUnderWater()) {
            VertexConsumer vertexconsumer1 = pBuffer.getBuffer(RenderType.waterMask());
            if (listmodel instanceof WaterPatchModel waterpatchmodel) {
                waterpatchmodel.waterPatch().render(pPoseStack, vertexconsumer1, pPackedLight, OverlayTexture.NO_OVERLAY);
            }
        }

        pPoseStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }

    /** @deprecated */
    @Deprecated
    public ResourceLocation getTextureLocation(ImmortalersBoat pEntity) {
        return this.getModelWithLocation(pEntity).getFirst();
    }

    public Pair<ResourceLocation, ListModel<Boat>> getModelWithLocation(ImmortalersBoat boat) {
        return this.boatResources.get(boat.getBoatVariant());
    }

}
