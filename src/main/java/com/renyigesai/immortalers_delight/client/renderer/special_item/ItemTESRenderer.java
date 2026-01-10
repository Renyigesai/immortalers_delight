package com.renyigesai.immortalers_delight.client.renderer.special_item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.client.model.AlfalfaDababaModel;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
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

    public ItemTESRenderer(BlockEntityRenderDispatcher pBlockEntityRenderDispatcher, EntityModelSet pEntityModelSet) {
        super(pBlockEntityRenderDispatcher,  pEntityModelSet);
        this.entityModelSet = pEntityModelSet;
        this.itemModelShaper = new net.minecraftforge.client.model.ForgeItemModelShaper(Minecraft.getInstance().getModelManager());
        this.alfalfaDababaModel = new AlfalfaDababaModel(this.entityModelSet.bakeLayer(AlfalfaDababaModel.ALFALFA_DABABA));
    }
    @Override
    public void onResourceManagerReload(ResourceManager pResourceManager) {
        this.alfalfaDababaModel = new AlfalfaDababaModel(this.entityModelSet.bakeLayer(AlfalfaDababaModel.ALFALFA_DABABA));
    }

    @Override
    public void renderByItem(ItemStack pStack, ItemDisplayContext pDisplayContext, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        Item item = pStack.getItem();

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        float partialTick = Minecraft.getInstance().getPartialTick();
        BakedModel bakedModel = itemRenderer.getModel(pStack,null,null,1);
        float ageInTicks = Minecraft.getInstance().player == null ? 0F : Minecraft.getInstance().player.tickCount + partialTick;
        float pullAmount = BoneKnifeItem.getPullingAmount(pStack, partialTick);

        if (item  == ImmortalersDelightItems.LARGE_COLUMN_SLICE.get()) {
            if (pDisplayContext == ItemDisplayContext.GUI ||  pDisplayContext == ItemDisplayContext.GROUND || pDisplayContext == ItemDisplayContext.FIXED) {

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
}
