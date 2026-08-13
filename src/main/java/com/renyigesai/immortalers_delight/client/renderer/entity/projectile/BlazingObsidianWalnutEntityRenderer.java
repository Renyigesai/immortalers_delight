package com.renyigesai.immortalers_delight.client.renderer.entity.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.client.model.projectile.ModSkullModel;
import com.renyigesai.immortalers_delight.entities.projectile.BlazingObsidianWalnutThrowingEntity;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BlazingObsidianWalnutEntityRenderer extends EntityRenderer<BlazingObsidianWalnutThrowingEntity> {
    public static final ModelLayerLocation MODEL_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "blazing_obsidian_walnut_bomb"), "main");
    private static final ResourceLocation LOCATION = ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "textures/entity/custom/empty.png");
    private final ModSkullModel model;

    public BlazingObsidianWalnutEntityRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new ModSkullModel(pContext.bakeLayer(MODEL_LOCATION));
    }

    public static LayerDefinition createSkullLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 20).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.ZERO);
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    public void render(BlazingObsidianWalnutThrowingEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        if (pEntity.isBoom()) return;

        pPoseStack.pushPose();
        pPoseStack.scale(-1.0F, -1.0F, 1.0F);
        float f = Mth.rotLerp(pPartialTicks, pEntity.yRotO, pEntity.getYRot());
        float f1 = Mth.lerp(pPartialTicks, pEntity.xRotO, pEntity.getXRot());
        VertexConsumer vertexconsumer = pBuffer.getBuffer(this.model.renderType(this.getTextureLocation(pEntity)));
        this.model.setupAnim(0.0F, f, f1);
        this.model.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, -1);
        this.model.getHead().translateAndRotate(pPoseStack);
        pPoseStack.popPose();

        pPoseStack.pushPose();
        pPoseStack.mulPose(Axis.YP.rotationDegrees(f));
        pPoseStack.mulPose(Axis.XP.rotationDegrees(f1 - 90));
        pPoseStack.translate(0.0D, 0.25D, 0.1875D);
        pPoseStack.scale(0.99F, 0.99F, 0.99F);
        BlockRenderDispatcher blockRenderDispatcher = Minecraft.getInstance().getBlockRenderer();
        BlockState state = ImmortalersDelightBlocks.OBSIDIAN_WALNUT.get().defaultBlockState().setValue(BlockStateProperties.AGE_7, pEntity.isDangerous() ? 6 : 7);
        blockRenderDispatcher.renderSingleBlock(state, pPoseStack, pBuffer, pPackedLight, OverlayTexture.NO_OVERLAY);
        pPoseStack.popPose();

        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }

    protected int getBlockLightLevel(BlazingObsidianWalnutThrowingEntity pEntity, BlockPos pPos) {
        return 15;
    }

    public ResourceLocation getTextureLocation(BlazingObsidianWalnutThrowingEntity pEntity) {
        return LOCATION;
    }
}
