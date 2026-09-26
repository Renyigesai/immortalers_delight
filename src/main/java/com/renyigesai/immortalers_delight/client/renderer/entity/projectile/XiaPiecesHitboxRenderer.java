package com.renyigesai.immortalers_delight.client.renderer.entity.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.client.model.projectile.PiecesCoreModel;
import com.renyigesai.immortalers_delight.client.model.projectile.PiecesOutsideLayerModel;
import com.renyigesai.immortalers_delight.client.model.projectile.PiecesPeripheryLayerModel;
import com.renyigesai.immortalers_delight.entities.living.PiecesHitboxEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class XiaPiecesHitboxRenderer extends EntityRenderer<PiecesHitboxEntity> {
    private static final ResourceLocation TEXTURE_LOCATION_CORE_ATTACK = new ResourceLocation(ImmortalersDelightMod.MODID,"textures/entity/projectile/xia_pieces_core_attack.png");
    private static final ResourceLocation TEXTURE_LOCATION_CORE_IDLE = new ResourceLocation(ImmortalersDelightMod.MODID,"textures/entity/projectile/xia_pieces_core_idle.png");
    private static final ResourceLocation TEXTURE_LOCATION_OUTSIDE_ATTACK = new ResourceLocation(ImmortalersDelightMod.MODID,"textures/entity/projectile/xia_pieces_outside_attack.png");
    private static final ResourceLocation TEXTURE_LOCATION_OUTSIDE_IDLE = new ResourceLocation(ImmortalersDelightMod.MODID,"textures/entity/projectile/xia_pieces_outside_idle.png");
    private static final ResourceLocation TEXTURE_LOCATION_PERIPHERY_ATTACK = new ResourceLocation(ImmortalersDelightMod.MODID,"textures/entity/projectile/xia_pieces_periphery_attack.png");
    private static final ResourceLocation TEXTURE_LOCATION_PERIPHERY_IDLE = new ResourceLocation(ImmortalersDelightMod.MODID,"textures/entity/projectile/xia_pieces_periphery_idle.png");
    private final PiecesCoreModel<PiecesHitboxEntity> coreModel;
    private final PiecesOutsideLayerModel<PiecesHitboxEntity> outsideLayerModel;
    private final PiecesPeripheryLayerModel<PiecesHitboxEntity> peripheryLayerModel;

    private static int degree = 0;

    public XiaPiecesHitboxRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.coreModel = new PiecesCoreModel<>(pContext.bakeLayer(PiecesCoreModel.PIECES_CORE));
        this.outsideLayerModel = new PiecesOutsideLayerModel<>(pContext.bakeLayer(PiecesOutsideLayerModel.PIECES_OUTSIDE));
        this.peripheryLayerModel = new PiecesPeripheryLayerModel<>(pContext.bakeLayer(PiecesPeripheryLayerModel.XIA_PIECES_LAYER));
    }

    public void render(PiecesHitboxEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        float r = pEntity.getRadius() / 2;
        if (pEntity.getAnimationProgress(pPartialTicks) >= 0.0F) {
            // 计算偏航角（水平旋转）的平滑插值
            float f = Mth.rotLerp(pPartialTicks, pEntity.yRotO, pEntity.getYRot());
            // 计算俯仰角（垂直旋转）的平滑插值
            float f1 = Mth.lerp(pPartialTicks, pEntity.xRotO, pEntity.getXRot());

            pPoseStack.pushPose();
            // 旋转模型以匹配实体的旋转
            pPoseStack.mulPose(Axis.YP.rotationDegrees(90.0F - pEntity.getYRot()));
            float f2 = 1.50F * r - 0.5f;
            pPoseStack.translate(0.0D, f2, 0.0D);
            pPoseStack.scale(-r, -r, r);

            // 实现模型动画
            if (pEntity.isWaiting()) {
                //实现循环动画，注意在等待状态动画计数是不动的，所以我们直接拉取实体的tickCount做计数
                float f3 = (pEntity.tickCount + pPartialTicks) % 100;
                this.coreModel.setupAnim(pEntity, 0, 3, f3, f, f1);
                this.outsideLayerModel.setupAnim(pEntity, 0, 3, f3, f, f1);
                this.peripheryLayerModel.setupAnim(pEntity, 0, 3, f3, f, f1);

            } else {
                //实现攻击动画
                float f3 = pEntity.getAnimationProgress(pPartialTicks);
                this.coreModel.setupAnim(pEntity, 1, 3, f3, f, f1);
                this.outsideLayerModel.setupAnim(pEntity, 1, 3, f3, f, f1);
                this.peripheryLayerModel.setupAnim(pEntity, 1, 3, f3, f, f1);

            }


            //实现核心图层渲染
            Minecraft minecraft = Minecraft.getInstance();
            // 标记：实体是否处于"发光且隐身"状态（如玩家隐身但持发光物品）
            boolean flag = minecraft.shouldEntityAppearGlowing(pEntity) && pEntity.isInvisible();
            VertexConsumer vertexconsumer; // 顶点数据消费者，用于向缓冲区写入渲染数据
            ResourceLocation location = this.getTextureLocation(pEntity); // 纹理路径
            // 渲染条件：实体非隐身 或 处于"发光且隐身"状态（保证发光隐身时仍能看到外层轮廓）
            if (!pEntity.isInvisible() || flag) {
                // 根据渲染状态选择对应的渲染类型
                if (flag) {
                    // 发光隐身状态：使用轮廓渲染类型（仅绘制外层轮廓）
                    vertexconsumer = pBuffer.getBuffer(RenderType.outline(location));
                } else {
                    // 正常状态：使用剔除背面实体渲染类型
                    vertexconsumer = pBuffer.getBuffer(RenderType.entityCutout(location));
                }
            } else vertexconsumer = pBuffer.getBuffer(this.coreModel.renderType(location));
            this.coreModel.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

            //实现小特效图层渲染，特效图层不受隐身buff影响
            location = this.getTextureLocationOutside(this.getTextureLocation(pEntity));
            // 使用剔除背面半透明实体渲染类型（仅绘制朝向相机的面）
            vertexconsumer = pBuffer.getBuffer(RenderType.entityTranslucentCull(location));
            this.outsideLayerModel.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

            //实现大特效图层渲染，特效图层不受隐身buff影响
            location = this.getTextureLocationPeriphery(this.getTextureLocation(pEntity));
            // 使用剔除背面半透明实体渲染类型（仅绘制朝向相机的面）
            vertexconsumer = pBuffer.getBuffer(RenderType.entityTranslucentCull(location));
            this.peripheryLayerModel.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

            pPoseStack.popPose();

            super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
        }
    }
    @Override
    protected int getBlockLightLevel(PiecesHitboxEntity pEntity, BlockPos pPos) {return 15;}
    /**
     * Returns the location of an entity's texture.
     */
    public @NotNull ResourceLocation getTextureLocation(@NotNull PiecesHitboxEntity pEntity) {
        if (pEntity.isWaiting()) return TEXTURE_LOCATION_CORE_IDLE;
        return TEXTURE_LOCATION_CORE_ATTACK;
    }
    public @NotNull ResourceLocation getTextureLocationOutside(ResourceLocation location) {
        if (location.equals(TEXTURE_LOCATION_CORE_ATTACK)) return TEXTURE_LOCATION_OUTSIDE_ATTACK;
        return TEXTURE_LOCATION_OUTSIDE_IDLE;
    }
    public @NotNull ResourceLocation getTextureLocationPeriphery(ResourceLocation location) {
        if (location.equals(TEXTURE_LOCATION_CORE_ATTACK)) return TEXTURE_LOCATION_PERIPHERY_ATTACK;
        return TEXTURE_LOCATION_PERIPHERY_IDLE;
    }
}
