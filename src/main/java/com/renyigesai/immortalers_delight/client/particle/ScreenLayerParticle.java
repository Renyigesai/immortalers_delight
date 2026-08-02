package com.renyigesai.immortalers_delight.client.particle;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.client.model.projectile.ScreenLayerParticleModel;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class ScreenLayerParticle extends Particle {
    // 用于渲染粒子的模型对象
    private final ScreenLayerParticleModel<?> model;
    private static final ResourceLocation INFERNAL_FORGING_TEXTURE_LOCATION = new ResourceLocation(ImmortalersDelightMod.MODID,"textures/particle/infernal_forging_screen_layer.png");

    private int transparency;
    /**
     * 构造方法，初始化实体外观粒子
     * @param pLevel 客户端世界对象
     * @param pX 粒子初始X坐标
     * @param pY 粒子初始Y坐标
     * @param pZ 粒子初始Z坐标
     */
    ScreenLayerParticle(ClientLevel pLevel, double pX, double pY, double pZ, int pTransparency) {
        super(pLevel, pX, pY, pZ);
        // 初始化模型（基于模型图层）
        this.model = new ScreenLayerParticleModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ScreenLayerParticleModel.SCREEN_LAYER_PARTICLE));
        // 设置重力为0，粒子不会受重力影响下落
        this.gravity = 0.0F;
        // 设置粒子生命周期为30tick
        this.lifetime = 30;
        this.transparency = pTransparency;
    }
//    ScreenLayerParticle(ClientLevel pLevel, double pX, double pY, double pZ) {
//        super(pLevel, pX, pY, pZ);
//        // 初始化模型（基于模型图层）
//        this.model = new ScreenLayerParticleModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ScreenLayerParticleModel.SCREEN_LAYER_PARTICLE));
//        // 设置重力为0，粒子不会受重力影响下落
//        this.gravity = 0.0F;
//        // 设置粒子生命周期为30tick
//        this.lifetime = 30;
//        this.transparency = new Random().nextInt(100);
//    }

    /**
     * 获取粒子的渲染类型
     * @return 自定义渲染类型
     */
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    /**
     * 渲染粒子的核心方法
     * @param pBuffer 顶点消费者，用于写入顶点数据
     * @param pRenderInfo 相机信息，包含视角相关数据
     * @param pPartialTicks 部分tick时间，用于平滑动画过渡
     */
    public void render(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {
        // 计算粒子生命周期进度（0.0到1.0）
        float f = ((float)this.age + pPartialTicks) / (float)this.lifetime;
        float g = f;
        // 根据正弦函数计算透明度（实现淡出效果：1→0）
        float f1 = 0.05F + 0.5F * Mth.sin(f * (float)Math.PI);
        if (f < 0.5) f1 = (float) (this.transparency % 100) / 100;
        else f1 *= (float) (this.transparency % 100) / 100;

        // 创建模型渲染的矩阵堆栈，用于处理模型的变换（旋转、缩放、平移等）
        PoseStack posestack = new PoseStack();
        // 应用相机的旋转，使粒子始终面向玩家视角
        posestack.mulPose(pRenderInfo.rotation());
        // 沿X轴旋转，实现粒子的摆动动画
        //posestack.mulPose(Axis.XP.rotationDegrees(150.0F * f - 60.0F));
        // 缩放模型（X和Y轴翻转，因为模型默认坐标系与渲染坐标系有差异）
        posestack.scale(-1.0F, -1.0F, 1.0F);
        // 平移模型位置，调整显示在粒子坐标处的偏移
        posestack.translate(0.0F, -1.5F, 3.0F + 0.03 * g);

        // 获取渲染缓冲区源
        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        // 从缓冲区源获取对应渲染类型的顶点消费者
        VertexConsumer vertexconsumer = multibuffersource$buffersource.getBuffer(RenderType.entityTranslucent(getTextureLocation(pBuffer,pRenderInfo,pPartialTicks)));
        // 将模型渲染到缓冲区
        // 参数说明：矩阵堆栈、顶点消费者、光照值、叠加纹理、RGBA颜色值（最后一个是透明度）
        this.model.renderToBuffer(posestack, vertexconsumer, 15728880, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, f1);
        // 结束当前渲染批次，提交渲染数据
        multibuffersource$buffersource.endBatch();
    }


    public ResourceLocation getTextureLocation(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {
        return INFERNAL_FORGING_TEXTURE_LOCATION;
    }

//    public static ScreenLayerParticle.ScreenLayerProvider screenLayerParticleProvider(SpriteSet spriteSet) {
//        return new ScreenLayerParticle.ScreenLayerProvider();
//    }
//    /**
//     * 粒子提供器类，用于创建MobAppearanceParticle实例
//     * 实现ParticleProvider接口，作为粒子系统的工厂
//     */
//    @OnlyIn(Dist.CLIENT)
//    public static class ScreenLayerProvider implements ParticleProvider<SimpleParticleType> {
//        /**
//         * 创建粒子实例
//         * @param pType 粒子类型
//         * @param pLevel 客户端世界
//         * @param pX X坐标
//         * @param pY Y坐标
//         * @param pZ Z坐标
//         * @param pXSpeed X方向速度（此处未使用）
//         * @param pYSpeed Y方向速度（此处未使用）
//         * @param pZSpeed Z方向速度（此处未使用）
//         * @return 创建的MobAppearanceParticle实例
//         */
//        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
//            return new ScreenLayerParticle(pLevel, pX, pY, pZ);
//        }
//    }
    public static ScreenLayerParticle.ScreenLayerParticleProvider screenLayerParticleProvider(SpriteSet sprite) {
        return new ScreenLayerParticle.ScreenLayerParticleProvider(sprite);
    }

    /**
     * 粒子提供器类，用于创建MobAppearanceParticle实例
     * 实现ParticleProvider接口，作为粒子系统的工厂
     */
    @OnlyIn(Dist.CLIENT)
    public static class ScreenLayerParticleProvider implements ParticleProvider<ScreenLayerParticleOption> {
        private final SpriteSet sprite; // 粒子纹理精灵集（存储多个纹理帧）

        /**
         * 工厂类构造方法
         * @param pSprite 纹理精灵集
         */
        public ScreenLayerParticleProvider(SpriteSet pSprite) {
            this.sprite = pSprite;
        }

        /**
         * 创建尖啸粒子实例
         * @param pType 粒子配置参数（包含延迟时间）
         * @param pLevel 客户端世界
         * @param pX 粒子X坐标
         * @param pY 粒子Y坐标
         * @param pZ 粒子Z坐标
         * @param pXSpeed X轴速度（未使用）
         * @param pYSpeed Y轴速度（未使用）
         * @param pZSpeed Z轴速度（未使用）
         * @return 初始化后的尖啸粒子实例
         */
        public Particle createParticle(ScreenLayerParticleOption pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            ScreenLayerParticle screenLayerparticle = new ScreenLayerParticle(pLevel, pX, pY, pZ, pType.getTransparency());
            screenLayerparticle.setAlpha(1.0F);          // 初始化透明度为1.0F（完全不透明）
            return screenLayerparticle;
        }
    }
}
