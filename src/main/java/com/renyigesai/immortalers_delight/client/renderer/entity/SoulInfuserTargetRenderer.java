package com.renyigesai.immortalers_delight.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.client.model.SoulBoomModel;
import com.renyigesai.immortalers_delight.client.model.projectile.ModSkullModel;
import com.renyigesai.immortalers_delight.client.renderer.ModRenderTypes;
import com.renyigesai.immortalers_delight.entities.living.SoulInfuserTargetEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;


public class SoulInfuserTargetRenderer extends EntityRenderer<SoulInfuserTargetEntity> {
    public static final ResourceLocation BEAM_OUTSIDE_LOCATION = new ResourceLocation(ImmortalersDelightMod.MODID,"textures/entity/beam/soul_beam.png");
    private static final RenderType BEAM = ModRenderTypes.UNLIT_TRANSLUCENT_NO_CULL.apply(BEAM_OUTSIDE_LOCATION);
    public static final ModelLayerLocation MODEL_LOCATION = new ModelLayerLocation(new ResourceLocation(ImmortalersDelightMod.MODID, "focused_soul"), "main");
    private static final ResourceLocation LAYER_LOCATION = new ResourceLocation(ImmortalersDelightMod.MODID,"textures/entity/focused_soul.png");
    private static final ResourceLocation BOOM_LOCATION = new ResourceLocation(ImmortalersDelightMod.MODID, "textures/entity/soul_boom.png");;

    // 凋灵头颅的模型实例，基于SkullModel（骷髅头模型）实现
    private final ModSkullModel model;
    private SoulBoomModel boomModel;

    /**
     * 创建凋灵头颅的模型层定义（用于模型烘焙）
     * 定义模型的网格结构、UV坐标映射、部件位置等，是渲染模型的基础数据
     * @return 凋灵头颅的模型层定义，包含完整的网格信息
     */
    public static LayerDefinition createSkullLayer() {
        // 1. 创建网格定义实例，作为模型的根容器
        MeshDefinition meshdefinition = new MeshDefinition();
        // 2. 获取模型根部件定义，所有模型部件都依附于根部件
        PartDefinition partdefinition = meshdefinition.getRoot();
        // 3. 向根部件添加/替换"head"（头部）部件
        //    - CubeListBuilder.create()：创建立方体列表构建器，定义模型的立方体形状
        //    - texOffs(0, 35)：设置该立方体的UV坐标起始点（对应纹理图的X=0，Y=35）
        //    - addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F)：定义立方体的尺寸和位置
        //      前三个参数：立方体的起始坐标（相对部件原点）
        //      后三个参数：立方体的长宽高（X=8F，Y=8F，Z=8F，对应一个正方体头颅）
        //    - PartPose.ZERO：设置部件的姿态（无旋转、无平移、无缩放，默认原点姿态）
        partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 20).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.ZERO);
        // 4. 创建模型层定义，指定网格定义、纹理宽度64、纹理高度64（对应原版凋灵纹理的尺寸）
        return LayerDefinition.create(meshdefinition, 64, 64);
    }
    public SoulInfuserTargetRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new ModSkullModel(pContext.bakeLayer(MODEL_LOCATION));
        this.boomModel = new SoulBoomModel<>(pContext.bakeLayer(SoulBoomModel.LAYER_LOCATION));
    }

    @Override
    public void render(SoulInfuserTargetEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        //渲染本体的模型
        pPoseStack.pushPose();
        pPoseStack.scale(-1.0F, -1.0F, 1.0F);
        float f = Mth.rotLerp(pPartialTick, pEntity.yRotO, pEntity.getYRot());
        float f1 = Mth.lerp(pPartialTick, pEntity.xRotO, pEntity.getXRot());

        VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.entitySmoothCutout(LAYER_LOCATION));
        this.model.setupAnim(0.0F, f, f1);
        this.model.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        this.model.getHead().translateAndRotate(pPoseStack);

        pPoseStack.popPose();

        //渲染外层光束模型
        BlockPos blockpos = pEntity.getBeamTarget();
        if (blockpos != null) {
            float f3 = (float)blockpos.getX() + 0.5F;
            float f4 = (float)blockpos.getY() + 0.5F;
            float f5 = (float)blockpos.getZ() + 0.5F;
            float f6 = (float)((double)f3 - pEntity.getX());
            float f7 = (float)((double)f4 - pEntity.getY());
            float f8 = (float)((double)f5 - pEntity.getZ());

            pPoseStack.pushPose();
            pPoseStack.translate(f6, f7, f8);
//            if (pEntity.isCharging()) {
                //渲染光束
//            renderCrystalBeams(f6, f7, f8, pPartialTick, pEntity.tickCount, pPoseStack, pBuffer, pPackedLight);
                renderFocusedSoulBeam(-f6, -f7, -f8, pPartialTick, pEntity.tickCount, pPoseStack, pBuffer);
//            }

            //渲染光束起点模型
            float boomRot = ((float)pEntity.tickCount + pPartialTick) * 4.0F; // 可自定义自转速度
            this.boomModel.setupAnim(0.0F, 0.0F, boomRot, f, f1);
            VertexConsumer boomVertexConsumer = pBuffer.getBuffer(ModRenderTypes.UNLIT_TRANSLUCENT.apply(BOOM_LOCATION));
            this.boomModel.renderToBuffer(pPoseStack, boomVertexConsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            pPoseStack.popPose();

        }
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
    }

    /**
     * 仿守卫者风格的十字形激光束渲染。
     * @param pXOffset 终点相对起点的X偏移
     * @param pYOffset 终点相对起点的Y偏移
     * @param pZOffset 终点相对起点的Z偏移
     * @param pPartialTick 分帧插值
     * @param pTickCount 实体存活tick数（用于驱动动画）
     * @param pPoseStack 位姿栈
     * @param pBuffer 多缓冲源
     */
    private static void renderFocusedSoulBeam(float pXOffset, float pYOffset, float pZOffset,
                                              float pPartialTick, int pTickCount,
                                              PoseStack pPoseStack, MultiBufferSource pBuffer) {
        pPoseStack.pushPose();

        // ---- 1. 旋转对齐：把局部+Y轴旋转到指向目标的方向 ----
        Vec3 dir = new Vec3(pXOffset, pYOffset, pZOffset);
        float beamLength = (float) dir.length();
        // 防止除0 / 极短距离导致十字形挤成一团或法线计算NaN
        if (beamLength < 0.05F) {
            pPoseStack.popPose();
            return;
        }
        Vec3 normalizedDir = dir.normalize();
        float pitch = (float) Math.acos(normalizedDir.y);
        float yaw = (float) Math.atan2(normalizedDir.z, normalizedDir.x);
        pPoseStack.mulPose(Axis.YP.rotationDegrees((((float) Math.PI / 2F) - yaw) * (180F / (float) Math.PI)));
        pPoseStack.mulPose(Axis.XP.rotationDegrees(pitch * (180F / (float) Math.PI)));

        // ---- 2. 基础参数（颜色先固定，变色逻辑注释保留） ----
        float f1 = (float) pTickCount + pPartialTick;
        float f2 = f1 * 0.325F % 1.0F; // 末端UV纵向滚动相位（配合贴图纹理重复）

        // === 固定颜色（示例：灵魂青色，可自行调整）===
        int red = 255;
        int green = 255;
        int blue = 255;
    /* 如果后续要做"随蓄力进度变色"，参考原版写法：
    float f8 = chargeProgress * chargeProgress;
    int red   = 64  + (int)(f8 * 191.0F);
    int green = 32  + (int)(f8 * 191.0F);
    int blue  = 128 - (int)(f8 * 64.0F);
    */

        float f7 = f1 * 0.05F * -1.5F; // 自转角速度，保留原版系数

        // ---- 3. 十字形内层（2片交叉面）----
        float innerRadius = 0.4F;
        float f19 = Mth.cos(f7 + (float) Math.PI) * innerRadius;
        float f20 = Mth.sin(f7 + (float) Math.PI) * innerRadius;
        float f21 = Mth.cos(f7 + 0.0F) * innerRadius;
        float f22 = Mth.sin(f7 + 0.0F) * innerRadius;
        float f23 = Mth.cos(f7 + ((float) Math.PI / 2F)) * innerRadius;
        float f24 = Mth.sin(f7 + ((float) Math.PI / 2F)) * innerRadius;
        float f25 = Mth.cos(f7 + ((float) Math.PI * 1.5F)) * innerRadius;
        float f26 = Mth.sin(f7 + ((float) Math.PI * 1.5F)) * innerRadius;

        // ---- 4. 外层护罩（4点，用于末端封口面）----
        float outerRadius = 1.0F;
        float f11 = Mth.cos(f7 + 2.3561945F) * outerRadius;
        float f12 = Mth.sin(f7 + 2.3561945F) * outerRadius;
        float f13 = Mth.cos(f7 + ((float) Math.PI / 4F)) * outerRadius;
        float f14 = Mth.sin(f7 + ((float) Math.PI / 4F)) * outerRadius;
        float f15 = Mth.cos(f7 + 3.926991F) * outerRadius;
        float f16 = Mth.sin(f7 + 3.926991F) * outerRadius;
        float f17 = Mth.cos(f7 + 5.4977875F) * outerRadius;
        float f18 = Mth.sin(f7 + 5.4977875F) * outerRadius;

        // ---- 5. UV纵向滚动（纹理沿光柱长度重复拉伸）----
        float uvScale = innerRadius / 0.2f; // 半径相对基准的缩放倍数
//        float uHalf = 0.4999F * uvScale; // 横向UV跨度跟着半径等比例缩放

        float vStart = (1.0F - f2) / 2;
        vStart += (pTickCount % 6 <= 2) ? 0.5F : 0.0F;
        float vEnd = beamLength * 1.0F/ uvScale + vStart; // 纵向重复密度同样乘上uvScale
//        float vStart = -1.0F + f2;
//        float vEnd = beamLength * 2.5F + vStart;

        VertexConsumer vertexconsumer = pBuffer.getBuffer(BEAM);
        PoseStack.Pose posestack$pose = pPoseStack.last();
        Matrix4f matrix4f = posestack$pose.pose();
        Matrix3f matrix3f = posestack$pose.normal();

//        // 面片A（左右两侧）
//        vertex(vertexconsumer, matrix4f, matrix3f, f19, beamLength, f20, red, green, blue, uHalf, vEnd);
//        vertex(vertexconsumer, matrix4f, matrix3f, f19, 0.0F, f20, red, green, blue, uHalf, vStart);
//        vertex(vertexconsumer, matrix4f, matrix3f, f21, 0.0F, f22, red, green, blue, 0.0F, vStart);
//        vertex(vertexconsumer, matrix4f, matrix3f, f21, beamLength, f22, red, green, blue, 0.0F, vEnd);
//
//        // 面片B（前后两侧，与A垂直交叉）
//        vertex(vertexconsumer, matrix4f, matrix3f, f23, beamLength, f24, red, green, blue, uHalf, vEnd);
//        vertex(vertexconsumer, matrix4f, matrix3f, f23, 0.0F, f24, red, green, blue, uHalf, vStart);
//        vertex(vertexconsumer, matrix4f, matrix3f, f25, 0.0F, f26, red, green, blue, 0.0F, vStart);
//        vertex(vertexconsumer, matrix4f, matrix3f, f25, beamLength, f26, red, green, blue, 0.0F, vEnd);
        // 面片A（左右两侧）
        vertex(vertexconsumer, matrix4f, matrix3f, f19, beamLength, f20, red, green, blue, 0.4999F, vEnd);
        vertex(vertexconsumer, matrix4f, matrix3f, f19, 0.0F, f20, red, green, blue, 0.4999F, vStart);
        vertex(vertexconsumer, matrix4f, matrix3f, f21, 0.0F, f22, red, green, blue, 0.0F, vStart);
        vertex(vertexconsumer, matrix4f, matrix3f, f21, beamLength, f22, red, green, blue, 0.0F, vEnd);

        // 面片B（前后两侧，与A垂直交叉）
        vertex(vertexconsumer, matrix4f, matrix3f, f23, beamLength, f24, red, green, blue, 0.4999F, vEnd);
        vertex(vertexconsumer, matrix4f, matrix3f, f23, 0.0F, f24, red, green, blue, 0.4999F, vStart);
        vertex(vertexconsumer, matrix4f, matrix3f, f25, 0.0F, f26, red, green, blue, 0.0F, vStart);
        vertex(vertexconsumer, matrix4f, matrix3f, f25, beamLength, f26, red, green, blue, 0.0F, vEnd);

        // 面片C（末端封口光斑，双帧闪烁动画）
        float f31 = (pTickCount % 6 <= 2) ? 0.5F : 0.0F;
        vertex(vertexconsumer, matrix4f, matrix3f, f11, beamLength, f12, red, green, blue, 0.5F, f31 + 0.5F);
        vertex(vertexconsumer, matrix4f, matrix3f, f13, beamLength, f14, red, green, blue, 1.0F, f31 + 0.5F);
        vertex(vertexconsumer, matrix4f, matrix3f, f17, beamLength, f18, red, green, blue, 1.0F, f31);
        vertex(vertexconsumer, matrix4f, matrix3f, f15, beamLength, f16, red, green, blue, 0.5F, f31);

        pPoseStack.popPose();
    }

    private static void vertex(VertexConsumer pConsumer, Matrix4f pPose, Matrix3f pNormal,
                               float pX, float pY, float pZ,
                               int pRed, int pGreen, int pBlue, float pU, float pV) {
        pConsumer.vertex(pPose, pX, pY, pZ)
                .color(pRed, pGreen, pBlue, 255)
                .uv(pU, pV)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(15728880) // LightTexture.FULL_BRIGHT，强制全亮不受环境光影响
                .normal(pNormal, 0.0F, 1.0F, 0.0F)
                .endVertex();
    }
    //这个方法用于渲染末影水晶光柱
    public static void renderCrystalBeams(float pX, float pY, float pZ, float pPartialTick, int pTickCount, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        float f = Mth.sqrt(pX * pX + pZ * pZ);
        float f1 = Mth.sqrt(pX * pX + pY * pY + pZ * pZ);
        pPoseStack.pushPose();
        pPoseStack.translate(0.0F, 2.0F, 0.0F);
        pPoseStack.mulPose(Axis.YP.rotation((float)(-Math.atan2((double)pZ, (double)pX)) - ((float)Math.PI / 2F)));
        pPoseStack.mulPose(Axis.XP.rotation((float)(-Math.atan2((double)f, (double)pY)) - ((float)Math.PI / 2F)));
        VertexConsumer vertexconsumer = pBuffer.getBuffer(BEAM);
        float f2 = 0.0F - ((float)pTickCount + pPartialTick) * 0.01F;
        float f3 = Mth.sqrt(pX * pX + pY * pY + pZ * pZ) / 32.0F - ((float)pTickCount + pPartialTick) * 0.01F;
        int i = 8;
        float f4 = 0.0F;
        float f5 = 0.75F;
        float f6 = 0.0F;
        PoseStack.Pose posestack$pose = pPoseStack.last();
        Matrix4f matrix4f = posestack$pose.pose();
        Matrix3f matrix3f = posestack$pose.normal();

        for(int j = 1; j <= 8; ++j) {
            float f7 = Mth.sin((float)j * ((float)Math.PI * 2F) / 8.0F) * 0.75F;
            float f8 = Mth.cos((float)j * ((float)Math.PI * 2F) / 8.0F) * 0.75F;
            float f9 = (float)j / 8.0F;
            vertexconsumer.vertex(matrix4f, f4 * 0.2F, f5 * 0.2F, 0.0F).color(55, 35, 0, 255).uv(f6, f2).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(pPackedLight).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
            vertexconsumer.vertex(matrix4f, f4, f5, f1).color(255, 255, 255, 255).uv(f6, f3).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
            vertexconsumer.vertex(matrix4f, f7, f8, f1).color(255, 255, 255, 255).uv(f9, f3).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
            vertexconsumer.vertex(matrix4f, f7 * 0.2F, f8 * 0.2F, 0.0F).color(55, 35, 0, 255).uv(f9, f2).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(pPackedLight).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
            f4 = f7;
            f5 = f8;
            f6 = f9;
        }

        pPoseStack.popPose();
    }
    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull SoulInfuserTargetEntity pEntity) {
        return LAYER_LOCATION;
    }
    @Override
    public boolean shouldRender(@NotNull SoulInfuserTargetEntity pLivingEntity, @NotNull Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return super.shouldRender(pLivingEntity, pCamera, pCamX, pCamY, pCamZ) || pLivingEntity.getBeamTarget() != null;
    }
}
