package com.renyigesai.immortalers_delight.client.renderer.special_item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.client.model.AlfalfaDababaModel;
import com.renyigesai.immortalers_delight.client.model.BreadOfWarModel;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.item.weapon.BoneKnifeItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class ItemTESRenderer extends BlockEntityWithoutLevelRenderer {
    private static ItemTESRenderer instance;

    private final EntityModelSet entityModelSet;
    private static final ResourceLocation ALFALFA_DABABA_TEXTURE = ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "textures/entity/custom/alfalfa_dababa.png");
    private static final ResourceLocation BREAD_OF_WAR_TEXTURE = ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "textures/entity/custom/jeng_nanu.png");
    private AlfalfaDababaModel alfalfaDababaModel;
    private BreadOfWarModel breadOfWarModel;

    public ItemTESRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher, EntityModelSet entityModelSet) {
        super(blockEntityRenderDispatcher, entityModelSet);
        this.entityModelSet = entityModelSet;
        this.alfalfaDababaModel = new AlfalfaDababaModel(this.entityModelSet.bakeLayer(AlfalfaDababaModel.ALFALFA_DABABA));
        this.breadOfWarModel = new BreadOfWarModel(this.entityModelSet.bakeLayer(BreadOfWarModel.BREAD_OF_WAR));
    }

    public static ItemTESRenderer getInstance() {
        if (instance == null) {
            Minecraft minecraft = Minecraft.getInstance();
            instance = new ItemTESRenderer(
                    minecraft.getBlockEntityRenderDispatcher(),
                    minecraft.getEntityModels());
        }
        return instance;
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        this.alfalfaDababaModel = new AlfalfaDababaModel(this.entityModelSet.bakeLayer(AlfalfaDababaModel.ALFALFA_DABABA));
        this.breadOfWarModel = new BreadOfWarModel(this.entityModelSet.bakeLayer(BreadOfWarModel.BREAD_OF_WAR));
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        Item item = stack.getItem();
        Minecraft minecraft = Minecraft.getInstance();
        ItemRenderer itemRenderer = minecraft.getItemRenderer();

        if (item == ImmortalersDelightItems.JENG_NANU.get()) {
            if (isFlatDisplay(displayContext)) {
                renderFlatItem(poseStack, buffer, packedLight, packedOverlay, itemRenderer, stack, displayContext);
            } else {
                poseStack.pushPose();
                poseStack.scale(1.0F, -1.0F, -1.0F);
                VertexConsumer vertexConsumer = ItemRenderer.getFoilBufferDirect(buffer, this.breadOfWarModel.renderType(BREAD_OF_WAR_TEXTURE), false, stack.hasFoil());
                this.breadOfWarModel.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay, -1);
                poseStack.popPose();
            }
            return;
        }

        if (item == ImmortalersDelightItems.BONE_KNIFE.get()) {
            float partialTick = minecraft.getTimer().getGameTimeDeltaPartialTick(false);
            float pullAmount = BoneKnifeItem.getPullingAmount(stack, partialTick);
            poseStack.pushPose();
            poseStack.translate(0.5F, 0.5F, 0.5F);
            float yOffset = 0;
            int zRotation = 0;
            if (pullAmount > 0.5f) {
                pullAmount -= 0.5f;
                pullAmount *= 2;
                if (pullAmount <= 0.15f) {
                    yOffset -= pullAmount;
                } else if (pullAmount > 0.15f && pullAmount <= 0.85f) {
                    yOffset += Math.sin(getRotation((pullAmount - 0.18f) * 280));
                    if (isInHand(displayContext, false)) {
                        zRotation += (pullAmount - 0.15f) * 640;
                    } else {
                        zRotation += (pullAmount - 0.15f) * 385;
                    }
                } else {
                    yOffset += pullAmount - 1;
                    zRotation += isInHand(displayContext, false) ? 90 : 270;
                }
                if (isInHand(displayContext, false) && !isInHand(displayContext, true)) {
                    float extraY = -0.2f * Mth.lerp(pullAmount, 0.0f, 1.0f);
                    poseStack.translate(2 * extraY, extraY, 0);
                }
            }

            poseStack.translate(0, yOffset, 0);
            poseStack.mulPose(Axis.ZP.rotationDegrees(zRotation));
            BakedModel usingModel = itemRenderer.getModel(stack, minecraft.level, null, 0);
            itemRenderer.render(stack, displayContext, false, poseStack, buffer, packedLight, packedOverlay, usingModel);
            poseStack.popPose();
            return;
        }

        if (item == ImmortalersDelightItems.LARGE_COLUMN.get()) {
            if (isFlatDisplay(displayContext)) {
                renderFlatItem(poseStack, buffer, packedLight, packedOverlay, itemRenderer, stack, displayContext);
            } else {
                poseStack.pushPose();
                poseStack.scale(1.0F, -1.0F, -1.0F);
                VertexConsumer vertexConsumer = ItemRenderer.getFoilBufferDirect(buffer, this.alfalfaDababaModel.renderType(ALFALFA_DABABA_TEXTURE), false, stack.hasFoil());
                this.alfalfaDababaModel.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay, -1);
                poseStack.popPose();
            }
        }
    }

    private static void renderFlatItem(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay,
                                       ItemRenderer itemRenderer, ItemStack stack, ItemDisplayContext displayContext) {
        BakedModel iconModel = FlatItemIconModels.getIcon(stack);
        BakedModel missingModel = Minecraft.getInstance().getModelManager().getMissingModel();
        if (iconModel == null || iconModel == missingModel) {
            return;
        }

        poseStack.pushPose();
        if (displayContext == ItemDisplayContext.FIXED) {
            // Cutting board already positions and rotates the stack in CuttingBoardRenderer.
            renderFlatModelLists(itemRenderer, iconModel, stack, packedLight, packedOverlay, poseStack, buffer);
        } else {
            if (displayContext != ItemDisplayContext.GUI) {
                poseStack.translate(0.5F, 0.5F, 0.5F);
            }

            boolean leftHand = displayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND
                    || displayContext == ItemDisplayContext.THIRD_PERSON_LEFT_HAND;
            iconModel.getTransforms().getTransform(displayContext).apply(leftHand, poseStack);
            renderFlatModelLists(itemRenderer, iconModel, stack, packedLight, packedOverlay, poseStack, buffer);
        }

        poseStack.popPose();
    }

    private static void renderFlatModelLists(ItemRenderer itemRenderer, BakedModel iconModel, ItemStack stack,
                                             int packedLight, int packedOverlay, PoseStack poseStack,
                                             MultiBufferSource buffer) {
        var renderTypes = iconModel.getRenderTypes(stack, true);
        if (renderTypes.isEmpty()) {
            RenderType fallback = Sheets.translucentItemSheet();
            VertexConsumer vertexConsumer = ItemRenderer.getFoilBuffer(buffer, fallback, true, stack.hasFoil());
            itemRenderer.renderModelLists(iconModel, stack, packedLight, packedOverlay, poseStack, vertexConsumer);
        } else {
            for (RenderType renderType : renderTypes) {
                VertexConsumer vertexConsumer = ItemRenderer.getFoilBuffer(buffer, renderType, true, stack.hasFoil());
                itemRenderer.renderModelLists(iconModel, stack, packedLight, packedOverlay, poseStack, vertexConsumer);
            }
        }
    }

    private static boolean isFlatDisplay(ItemDisplayContext displayContext) {
        return displayContext == ItemDisplayContext.GUI
                || displayContext == ItemDisplayContext.GROUND
                || displayContext == ItemDisplayContext.FIXED;
    }

    private static boolean isInHand(ItemDisplayContext displayContext, boolean needFirstPerson) {
        if (needFirstPerson) {
            return displayContext.firstPerson();
        }
        return displayContext == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND
                || displayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND
                || displayContext == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND
                || displayContext == ItemDisplayContext.THIRD_PERSON_LEFT_HAND;
    }

    private static float getRotation(float degrees) {
        return degrees * ((float) Math.PI / 180F);
    }
}
