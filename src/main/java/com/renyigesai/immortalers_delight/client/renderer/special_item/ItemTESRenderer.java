package com.renyigesai.immortalers_delight.client.renderer.special_item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.client.model.AlfalfaDababaModel;
import com.renyigesai.immortalers_delight.client.model.BreadOfWarModel;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.item.DebugItem;
import com.renyigesai.immortalers_delight.item.weapon.BoneKnifeItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ShieldModel;
import net.minecraft.client.model.TridentModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class ItemTESRenderer extends BlockEntityWithoutLevelRenderer {
    //这个类用于处理使用实体模型的物品的模型
    private final EntityModelSet entityModelSet;
    private final ItemModelShaper itemModelShaper;
    private static final ResourceLocation ALFALFA_DABABA_TEXTURE = new ResourceLocation(ImmortalersDelightMod.MODID,"textures/entity/custom/alfalfa_dababa.png");
    private AlfalfaDababaModel alfalfaDababaModel;
    private static final ResourceLocation BREAD_OF_WAR_TEXTURE = new ResourceLocation(ImmortalersDelightMod.MODID,"textures/entity/custom/jeng_nanu.png");
    private BreadOfWarModel breadOfWarModel;

    public ItemTESRenderer(BlockEntityRenderDispatcher pBlockEntityRenderDispatcher, EntityModelSet pEntityModelSet) {
        super(pBlockEntityRenderDispatcher,  pEntityModelSet);
        this.entityModelSet = pEntityModelSet;
        this.itemModelShaper = new net.minecraftforge.client.model.ForgeItemModelShaper(Minecraft.getInstance().getModelManager());
        this.alfalfaDababaModel = new AlfalfaDababaModel(this.entityModelSet.bakeLayer(AlfalfaDababaModel.ALFALFA_DABABA));
        this.breadOfWarModel = new BreadOfWarModel(this.entityModelSet.bakeLayer(BreadOfWarModel.BREAD_OF_WAR));
    }
    @Override
    public void onResourceManagerReload(ResourceManager pResourceManager) {
        this.alfalfaDababaModel = new AlfalfaDababaModel(this.entityModelSet.bakeLayer(AlfalfaDababaModel.ALFALFA_DABABA));
        this.breadOfWarModel = new BreadOfWarModel(this.entityModelSet.bakeLayer(BreadOfWarModel.BREAD_OF_WAR));
    }

    @Override
    public void renderByItem(ItemStack pStack, ItemDisplayContext pDisplayContext, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        Item item = pStack.getItem();

        Minecraft minecraft = Minecraft.getInstance();
        ItemRenderer itemRenderer = minecraft.getItemRenderer();
        float partialTick = minecraft.getPartialTick();
        BakedModel bakedModel = itemRenderer.getModel(pStack,null,null,1);
        float ageInTicks = minecraft.player == null ? 0F : minecraft.player.tickCount + partialTick;
        float pullAmount = BoneKnifeItem.getPullingAmount(pStack, partialTick);


        if (item  == ImmortalersDelightItems.JENG_NANU.get()) {
            if (pDisplayContext == ItemDisplayContext.GUI ||  pDisplayContext == ItemDisplayContext.GROUND || pDisplayContext == ItemDisplayContext.FIXED) {
                ItemStack stack = new ItemStack(ImmortalersDelightItems.DEBUG_ITEM.get());
                stack.setDamageValue(DebugItem.JENG_NANU_MODEL);
                BakedModel usingbakedmodel = itemRenderer.getModel(stack,minecraft.level,null,0);
                pPoseStack.pushPose();
                pPoseStack.translate(0.5F, 0.5F, 0.5F);
                itemRenderer.render(pStack, pDisplayContext, false, pPoseStack, pBuffer, pPackedLight, pPackedOverlay, usingbakedmodel);
                pPoseStack.popPose();
            } else {
                pPoseStack.pushPose();
                pPoseStack.scale(1.0F, -1.0F, -1.0F);
                VertexConsumer vertexconsumer1 = ItemRenderer.getFoilBufferDirect(pBuffer, this.breadOfWarModel.renderType(BREAD_OF_WAR_TEXTURE), false, pStack.hasFoil());
                this.breadOfWarModel.renderToBuffer(pPoseStack, vertexconsumer1, pPackedLight, pPackedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
                pPoseStack.popPose();
            }

        }
        if (item  == ImmortalersDelightItems.BONE_KNIFE.get()) {
            pPoseStack.pushPose();
            pPoseStack.translate(0.5F, 0.5F, 0.5F);
            float xOffset = 0;
            float yOffset = 0;
            float zOffset = 0;
            int xRotation = 0;
            int yRotation = 0;
            int zRotation = 0;
            //实现物品动画，分视角做变换。
            if (pullAmount > 0.5f) {
                pullAmount -= 0.5f;
                pullAmount *= 2;
                //抛刀前蓄力动画
                if (pullAmount <= 0.15f) yOffset -= pullAmount;
                //抛刀动画
                if (pullAmount > 0.15f && pullAmount <= 0.85f) {
                    yOffset += Math.sin(getRotation((pullAmount - 0.18f) * 280));
                    if (isInHand(pDisplayContext,false)) {
                        zRotation += (pullAmount - 0.15f) * 640;
                    } else zRotation += (pullAmount - 0.15f) * 385;
                }
                //接刀动画
                if (pullAmount > 0.85f) {
                    yOffset += pullAmount -1;
                    if (isInHand(pDisplayContext,false)) {
                        zRotation += 90;
                    } else zRotation += 270;
                }
                //第三人称下接刀的落点更低
                if (isInHand(pDisplayContext,false) && !isInHand(pDisplayContext,true)) {
                    float extraY = -0.2f * Mth.lerp(pullAmount, 0.0f, 1.0f);
                    pPoseStack.translate(2 * extraY, extraY, 0);
                }
            }

            pPoseStack.translate(xOffset, yOffset, zOffset);
            pPoseStack.mulPose(Axis.XP.rotationDegrees(xRotation));
            pPoseStack.mulPose(Axis.YP.rotationDegrees(yRotation));
            pPoseStack.mulPose(Axis.ZP.rotationDegrees(zRotation));
            ItemStack stack = //new ItemStack(ImmortalersDelightItems.WARPED_LAUREL.get());
                    new ItemStack(ImmortalersDelightItems.DEBUG_ITEM.get());
            stack.setDamageValue(DebugItem.BONE_KNIFE_MODEL);
            BakedModel usingbakedmodel = itemRenderer.getModel(stack,minecraft.level,null,0);
            itemRenderer.render(pStack, ItemDisplayContext.NONE, false, pPoseStack, pBuffer, pPackedLight, pPackedOverlay, usingbakedmodel);
            pPoseStack.popPose();
        }
        if (item  == ImmortalersDelightItems.LARGE_COLUMN.get()) {
            if (pDisplayContext == ItemDisplayContext.GUI ||  pDisplayContext == ItemDisplayContext.GROUND || pDisplayContext == ItemDisplayContext.FIXED) {
//                ModelManager modelManager = minecraft.getModelManager();
//                BakedModel bakedmodel = modelManager.getModel(new ModelResourceLocation(ImmortalersDelightMod.MODID,"item/large_column_slice_in_gui", "inventory"));
//                if (bakedmodel == this.itemModelShaper.getModelManager().getMissingModel()) System.out.println("model location is not work");
//                BakedModel bakedmodel1 = bakedmodel.getOverrides().resolve(bakedmodel, pStack, null, null, 1);
//                if (bakedmodel1 == null) System.out.println("bakedmodel1 is null");
//                BakedModel usingbakedmodel = bakedmodel1 == null ? this.itemModelShaper.getModelManager().getMissingModel() : bakedmodel1;
                ItemStack stack = //new ItemStack(ImmortalersDelightItems.WARPED_LAUREL.get());
                        new ItemStack(ImmortalersDelightItems.DEBUG_ITEM.get());
                stack.setDamageValue(DebugItem.LARGE_COLUMN_MODEL);
                BakedModel usingbakedmodel = itemRenderer.getModel(stack,minecraft.level,null,0);
                pPoseStack.pushPose();
                pPoseStack.translate(0.5F, 0.5F, 0.5F);
                itemRenderer.render(pStack, pDisplayContext, false, pPoseStack, pBuffer, pPackedLight, pPackedOverlay, usingbakedmodel);
                pPoseStack.popPose();
            } else {
                pPoseStack.pushPose();
                //pModel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(pPoseStack, pModel, pDisplayContext, pLeftHand);
                pPoseStack.scale(1.0F, -1.0F, -1.0F);
                VertexConsumer vertexconsumer1 = ItemRenderer.getFoilBufferDirect(pBuffer, this.alfalfaDababaModel.renderType(ALFALFA_DABABA_TEXTURE), false, pStack.hasFoil());
                this.alfalfaDababaModel.renderToBuffer(pPoseStack, vertexconsumer1, pPackedLight, pPackedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
                pPoseStack.popPose();
            }

        }
    }

//    public BakedModel getModel(ItemStack pStack, @Nullable Level pLevel, @Nullable LivingEntity pEntity, int pSeed) {
//        BakedModel bakedmodel;
//        if (pStack.is(Items.TRIDENT)) {
//            bakedmodel = this.itemModelShaper.getModelManager().getModel(TRIDENT_IN_HAND_MODEL);
//        } else if (pStack.is(Items.SPYGLASS)) {
//            bakedmodel = this.itemModelShaper.getModelManager().getModel(SPYGLASS_IN_HAND_MODEL);
//        } else {
//            bakedmodel = this.itemModelShaper.getItemModel(pStack);
//        }
//
//        ClientLevel clientlevel = pLevel instanceof ClientLevel ? (ClientLevel)pLevel : null;
//        BakedModel bakedmodel1 = bakedmodel.getOverrides().resolve(bakedmodel, pStack, clientlevel, pEntity, pSeed);
//        return bakedmodel1 == null ? this.itemModelShaper.getModelManager().getMissingModel() : bakedmodel1;
//    }
    private static boolean isInHand(ItemDisplayContext pDisplayContext,boolean needFirstPerson) {
        if (needFirstPerson) return pDisplayContext.firstPerson();
        return pDisplayContext == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND || pDisplayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND || pDisplayContext == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND || pDisplayContext == ItemDisplayContext.THIRD_PERSON_LEFT_HAND;
    }
    private static float getRotation(float pDegrees) {
    return pDegrees * ((float)Math.PI / 180F);
}

}
