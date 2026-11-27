package com.renyigesai.immortalers_delight.client.renderer.special_item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.RenderType;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.client.renderer.ModRenderTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LightsaberItemRenderer extends BlockEntityWithoutLevelRenderer {

    // 单例模式
    public static final LightsaberItemRenderer INSTANCE = new LightsaberItemRenderer();

    // 假设这是光刃的模型位置（需在客户端模型加载事件中注册）
    public static final ModelResourceLocation BLADE_MODEL_LOC =
            new ModelResourceLocation(new ResourceLocation(ImmortalersDelightMod.MODID, "soul_sword_light"), "inventory");
    // 光刃绑定的纯色/光效贴图位置
    public static final ResourceLocation BLADE_TEXTURE =
            new ResourceLocation(ImmortalersDelightMod.MODID, "textures/item/soul_sword.png");

    public LightsaberItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.pushPose();

        Minecraft mc = Minecraft.getInstance();
        ItemRenderer itemRenderer = mc.getItemRenderer();

        // 1. 获取原版物品模型/剑柄
        BakedModel baseModel = itemRenderer.getModel(stack, null, null, 0);

        // 2. 正常渲染常规部件（剑柄）：使用环境光 packedLight 和原版通道
        // 如果不需要拆分，整把剑都是能量体，可跳过此步
        VertexConsumer regularRenderType = ItemRenderer.getFoilBufferDirect(buffer, RenderType.cutout(), true, stack.hasFoil());
        itemRenderer.renderModelLists(baseModel, stack, packedLight, packedOverlay, poseStack, regularRenderType);

        // -------------------------------------------------------------
        // 3. 核心步骤：渲染光刃能量体
        // -------------------------------------------------------------
        BakedModel bladeModel = mc.getModelManager().getModel(BLADE_MODEL_LOC);
//        BakedModel missingModel = mc.getModelManager().getMissingModel();

//        if (bladeModel == missingModel) {
//            ImmortalersDelightMod.LOGGER.error("【模型加载失败】BLADE_MODEL_LOC 匹配失败，获取到了兜底方块！当前查询标识: {}", BLADE_MODEL_LOC);
//        } else {
//            ImmortalersDelightMod.LOGGER.info("【模型加载成功】成功获取光刃模型: {}, 包含网格面数量: {}",
//                    BLADE_MODEL_LOC, bladeModel.getQuads(null, null, mc.level.random).size());
//        }
        // 获取自定义的 RenderType 并取得对应的 VertexConsumer

        RenderType customRenderType = ModRenderTypes.UNLIT_TRANSLUCENT_BLOCK.apply(InventoryMenu.BLOCK_ATLAS);
        VertexConsumer bladeConsumer = buffer.getBuffer(customRenderType);
//        RenderType customRenderType = ModRenderTypes.UNLIT_TRANSLUCENT_BLOCK.apply(BLADE_TEXTURE);
//        VertexConsumer bladeConsumer = buffer.getBuffer(customRenderType);

        // 关键点：Light 传入 0xF000F0 (满级天光 15 与满级块光 15)
        // 配合 RENDERTYPE_EYES_SHADER，能彻底抹去环境阴影，实现纯色能量质感
        int fullBright = 0xF000F0;

        // 手动将挤出的多边形网格渲染进自定义通道
        itemRenderer.renderModelLists(bladeModel, stack, fullBright, packedOverlay, poseStack, bladeConsumer);

        poseStack.popPose();
    }
}
