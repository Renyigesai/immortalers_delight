package com.renyigesai.immortalers_delight.client.renderer;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.util.FastColor;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.Map;

public class ImmortalersDelightSignRenderer extends SignRenderer {
    public ImmortalersDelightSignRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
        this.font = context.getFont();
        this.signModels = WoodType.values().collect(ImmutableMap.toImmutableMap((woodType) -> {
            return woodType;
        }, (woodType) -> {
            return new SignRenderer.SignModel(context.bakeLayer(ModelLayers.createSignModelName(woodType)));
        }));
    }

    private static final String STICK = "stick";
    private static final int BLACK_TEXT_OUTLINE_COLOR = -988212;
    private static final int OUTLINE_RENDER_DISTANCE = Mth.square(16);
    private static final float RENDER_SCALE = 0.6666667F;
    private static final Vec3 TEXT_OFFSET = new Vec3(0.0D, (double)0.33333334F, (double)0.046666667F);
    private final Map<WoodType, SignRenderer.SignModel> signModels;
    private final Font font;

    public void render(SignBlockEntity signBlockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlayColor) {
        BlockState blockState = signBlockEntity.getBlockState();
        SignBlock signBlock = (SignBlock)blockState.getBlock();
        WoodType woodType = SignBlock.getWoodType(signBlock);
        SignRenderer.SignModel signModel = this.signModels.get(woodType);
        signModel.stick.visible = blockState.getBlock() instanceof StandingSignBlock;
        this.renderSignWithText(signBlockEntity, poseStack, bufferSource, light, overlayColor, blockState, signBlock, woodType, signModel);
    }

    public float getSignModelRenderScale() {
        return 0.6666667F;
    }

    public float getSignTextRenderScale() {
        return 0.6666667F;
    }

    void renderSignWithText(SignBlockEntity signBlockEntity, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlayColor, BlockState blockState, SignBlock signBlock, WoodType woodType, Model signModel) {
        poseStack.pushPose();
        this.translateSign(poseStack, -signBlock.getYRotationDegrees(blockState), blockState);
        this.renderSign(poseStack, bufferSource, light, overlayColor, woodType, signModel);
        this.renderSignText(signBlockEntity.getBlockPos(), signBlockEntity.getFrontText(), poseStack, bufferSource, light, signBlockEntity.getTextLineHeight(), signBlockEntity.getMaxTextLineWidth(), true);
        this.renderSignText(signBlockEntity.getBlockPos(), signBlockEntity.getBackText(), poseStack, bufferSource, light, signBlockEntity.getTextLineHeight(), signBlockEntity.getMaxTextLineWidth(), false);
        poseStack.popPose();
    }

    void translateSign(PoseStack poseStack, float rotationAngle, BlockState blockState) {
        poseStack.translate(0.5F, 0.75F * this.getSignModelRenderScale(), 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(rotationAngle));
        if (!(blockState.getBlock() instanceof StandingSignBlock)) {
            poseStack.translate(0.0F, -0.3125F, -0.4375F);
        }
    }

    void renderSign(PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlayColor, WoodType woodType, Model signModel) {
        poseStack.pushPose();
        float scale = this.getSignModelRenderScale();
        poseStack.scale(scale, -scale, -scale);
        Material material = this.getSignMaterial(woodType);
        VertexConsumer vertexConsumer = material.buffer(bufferSource, signModel::renderType);
        this.renderSignModel(poseStack, light, overlayColor, signModel, vertexConsumer);
        poseStack.popPose();
    }

    void renderSignModel(PoseStack poseStack, int light, int overlayColor, Model signModel, VertexConsumer vertexConsumer) {
        SignRenderer.SignModel signRendererSignModel = (SignRenderer.SignModel) signModel;
        signRendererSignModel.root.render(poseStack, vertexConsumer, light, overlayColor);
    }

    Material getSignMaterial(WoodType woodType) {
        return Sheets.getSignMaterial(woodType);
    }

    void renderSignText(BlockPos blockPos, SignText signText, PoseStack poseStack, MultiBufferSource bufferSource, int light, int lineHeight, int maxLineWidth, boolean isFrontText) {
        poseStack.pushPose();
        this.translateSignText(poseStack, isFrontText, this.getTextOffset());
        int darkColor = getDarkColor(signText);
        int outlineOffset = 4 * lineHeight / 2;
        FormattedCharSequence[] renderMessages = signText.getRenderMessages(Minecraft.getInstance().isTextFilteringEnabled(), (message) -> {
            List<FormattedCharSequence> lines = this.font.split(message, maxLineWidth);
            return lines.isEmpty() ? FormattedCharSequence.EMPTY : lines.get(0);
        });
        int textColor;
        boolean outlineVisible;
        int renderTypeColor;
        if (signText.hasGlowingText()) {
            textColor = signText.getColor().getTextColor();
            outlineVisible = isOutlineVisible(blockPos, textColor);
            renderTypeColor = 15728880;
        } else {
            textColor = darkColor;
            outlineVisible = false;
            renderTypeColor = light;
        }

        for (int i = 0; i < 4; ++i) {
            FormattedCharSequence formattedMessage = renderMessages[i];
            float xOffset = (float)(-this.font.width(formattedMessage) / 2);
            if (outlineVisible) {
                this.font.drawInBatch8xOutline(formattedMessage, xOffset, (float)(i * lineHeight - outlineOffset), textColor, darkColor, poseStack.last().pose(), bufferSource, renderTypeColor);
            } else {
                this.font.drawInBatch(formattedMessage, xOffset, (float)(i * lineHeight - outlineOffset), textColor, false, poseStack.last().pose(), bufferSource, Font.DisplayMode.POLYGON_OFFSET, 0, renderTypeColor);
            }
        }

        poseStack.popPose();
    }

    private void translateSignText(PoseStack poseStack, boolean isFrontText, Vec3 textOffset) {
        if (!isFrontText) {
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        }

        float scale = 0.015625F * this.getSignTextRenderScale();
        poseStack.translate(textOffset.x, textOffset.y, textOffset.z);
        poseStack.scale(scale, -scale, scale);
    }

    Vec3 getTextOffset() {
        return TEXT_OFFSET;
    }

    static boolean isOutlineVisible(BlockPos blockPos, int textColor) {
        if (textColor == DyeColor.BLACK.getTextColor()) {
            return true;
        } else {
            Minecraft minecraft = Minecraft.getInstance();
            LocalPlayer localPlayer = minecraft.player;
            if (localPlayer != null && minecraft.options.getCameraType().isFirstPerson() && localPlayer.isScoping()) {
                return true;
            } else {
                Entity entity = minecraft.getCameraEntity();
                return entity != null && entity.distanceToSqr(Vec3.atCenterOf(blockPos)) < (double)OUTLINE_RENDER_DISTANCE;
            }
        }
    }

    static int getDarkColor(SignText text) {
        int i = text.getColor().getTextColor();
        if (i == DyeColor.BLACK.getTextColor() && text.hasGlowingText()) {
            return -988212;
        } else {
            double d0 = 0.4D;
            int j = (int)((double) FastColor.ARGB32.red(i) * 0.4D);
            int k = (int)((double)FastColor.ARGB32.green(i) * 0.4D);
            int l = (int)((double)FastColor.ARGB32.blue(i) * 0.4D);
            return FastColor.ARGB32.color(0, j, k, l);
        }
    }

    public static SignRenderer.SignModel createSignModel(EntityModelSet modelSet, WoodType woodType) {
        return new SignRenderer.SignModel(modelSet.bakeLayer(ModelLayers.createSignModelName(woodType)));
    }

    public static LayerDefinition createSignLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("sign", CubeListBuilder.create().texOffs(0, 0).addBox(-12.0F, -14.0F, -1.0F, 24.0F, 12.0F, 2.0F), PartPose.ZERO);
        partdefinition.addOrReplaceChild("stick", CubeListBuilder.create().texOffs(0, 14).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 14.0F, 2.0F), PartPose.ZERO);
        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @OnlyIn(Dist.CLIENT)
    public static final class SignModel extends Model {
        public final ModelPart root;
        public final ModelPart stick;

        public SignModel(ModelPart p_173657_) {
            super(RenderType::entityCutoutNoCull);
            this.root = p_173657_;
            this.stick = p_173657_.getChild("stick");
        }

        public void renderToBuffer(PoseStack poseStack, VertexConsumer consumer, int p_112512_, int p_112513_, float p_112514_, float p_112515_, float p_112516_, float p_112517_) {
            this.root.render(poseStack, consumer, p_112512_, p_112513_, p_112514_, p_112515_, p_112516_, p_112517_);
        }
    }
}