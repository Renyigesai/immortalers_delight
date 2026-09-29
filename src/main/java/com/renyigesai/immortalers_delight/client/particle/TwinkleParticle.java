package com.renyigesai.immortalers_delight.client.particle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.client.model.projectile.TwinkleParticleModel;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

/**
 * 实体外观粒子类，用于渲染类似远古守卫者的实体外观粒子效果
 * 仅在客户端运行
 */
@OnlyIn(Dist.CLIENT)
public class TwinkleParticle extends Particle {
    public static TwinkleParticleProvider baseSmokeProvider(SpriteSet spriteSet) {
        return new TwinkleParticleProvider(spriteSet);
    }
    /**
     * 粒子提供器类，用于创建MobAppearanceParticle实例
     * 实现ParticleProvider接口，作为粒子系统的工厂
     */
    @OnlyIn(Dist.CLIENT)
    public static class TwinkleParticleProvider implements ParticleProvider<TwinkleParticleOption> {
        private final SpriteSet spriteSet;
        public TwinkleParticleProvider (SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }
        /**
         * 创建粒子实例
         * @param pType 粒子类型
         * @param pLevel 客户端世界
         * @param pX X坐标
         * @param pY Y坐标
         * @param pZ Z坐标
         * @param pXSpeed X方向速度（此处未使用）
         * @param pYSpeed Y方向速度（此处未使用）
         * @param pZSpeed Z方向速度（此处未使用）
         * @return 创建的MobAppearanceParticle实例
         */
        public Particle createParticle(TwinkleParticleOption pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new TwinkleParticle(pLevel, pX, pY, pZ,pType.getCountdown());
        }

    }
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(ImmortalersDelightMod.MODID,"textures/particle/twinkle_particle.png");

    private static final ResourceLocation LAYER_TEXTURE_LOCATION = new ResourceLocation(ImmortalersDelightMod.MODID,"textures/particle/huge_smoke_layer.png");
    // 用于渲染粒子的模型对象
    private final TwinkleParticleModel<?> model;
    protected float quadSize = 1.0F + (this.random.nextFloat() * 0.5F + (this.random.nextBoolean() ? 1 : -1) * 0.5F) * 0.2F;

    protected float xRot = 0.0F;
    protected float yRot = 0.0F;
    protected float zRot = 0.0F;

    protected int color;

    /**
     * 构造方法，初始化实体外观粒子
     * @param pLevel 客户端世界对象
     * @param pX 粒子初始X坐标
     * @param pY 粒子初始Y坐标
     * @param pZ 粒子初始Z坐标
     */
    TwinkleParticle(ClientLevel pLevel, double pX, double pY, double pZ, int pColor) {
        super(pLevel, pX, pY, pZ);
        // 初始化模型（基于模型图层）
        this.model = new TwinkleParticleModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(TwinkleParticleModel.TWINKLE_PARTICLE));
        //设置颜色、动画方向、动画速度、缩放的控制数
        this.color = pColor;
        // 设置重力
        this.gravity = 0;
        // 设置粒子生命周期
        int i = pColor >= 0 ? pColor : pColor * -1;
        this.lifetime = i % 16 >= 8 ? 6 : 12;
//        this.lifetime = 99;
    }

    /**
     * 获取粒子的渲染类型
     * @return 自定义渲染类型
     */
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }


    public void tick() {
        super.tick(); // 执行父类的基础逻辑（位置、生命周期等）
        doOnTick();
    }

    protected void doOnTick() {

        if (this.age == 1) {
            float f = this.random.nextFloat();
            boolean flag = this.random.nextBoolean();
            boolean flag2 = this.random.nextBoolean();
            if (flag2) {
                this.yRot = (this.quadSize - 1) * 5 * f;
                if (flag) {
                    this.xRot = (this.quadSize - 1) * 5 * (1 - f);
                }
                else {
                    this.zRot = (this.quadSize - 1) * 5 * (1 - f);
                }
            } else {
                if (f < 0.5) f-= 1;
                this.yRot = 4 * f;
                if (flag) this.xRot = 4 * (1 - f);
                else this.zRot = 4 * (1 - f);
            }
        }
    }
    /**
     * 渲染粒子的核心方法
     * @param pBuffer 顶点消费者，用于写入顶点数据
     * @param pRenderInfo 相机信息，包含视角相关数据
     * @param pPartialTicks 部分tick时间，用于平滑动画过渡
     */
    public void render(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {

        // 创建模型渲染的矩阵堆栈，用于处理模型的变换（旋转、缩放、平移等）
        // 1. 计算粒子相对于相机的坐标
        Vec3 cameraPos = pRenderInfo.getPosition();
        // 插值计算粒子当前帧的世界坐标
        double particleX = Mth.lerp(pPartialTicks, this.xo, this.x);
        double particleY = Mth.lerp(pPartialTicks, this.yo, this.y);
        double particleZ = Mth.lerp(pPartialTicks, this.zo, this.z);
        // 粒子相对相机的偏移量
        double relX = particleX - cameraPos.x();
        double relY = particleY - cameraPos.y();
        double relZ = particleZ - cameraPos.z();

        // 2. 将偏移量应用到模型的PoseStack
        PoseStack posestack = new PoseStack();
        posestack.translate(relX, relY, relZ); // 关键：添加相对相机的平移
        posestack.scale(-1.0F, -1.0F, 1.0F);
        float zoom = getQuadSize(pPartialTicks);
        posestack.scale(zoom,zoom,zoom);
        posestack.translate(0, -zoom, 0);
        //执行额外操作，方便子类重写
        doOnRender(pBuffer,pRenderInfo,pPartialTicks);

        // 实现模型的动画效果
        float f2 = (float)this.age + pPartialTicks;
        int col = this.color;
        if (col < 0) {
            f2 *= -1;
            f2 += this.lifetime + 1;
            col *= -1;
        }
        col = col % 16;
        if (col >= 8) f2 *= 2;
//        posestack.mulPose(Axis.YP.rotationDegrees(f2 * 0.1F * 180.0F));
//        posestack.mulPose(Axis.XP.rotationDegrees(Mth.cos(f2 * 0.01F) * 180.0F));
//        posestack.mulPose(Axis.ZP.rotationDegrees(Mth.sin(f2 * 0.015F) * 360.0F));
        this.coreAnim(pBuffer,pRenderInfo,pPartialTicks);
        this.model.setupAnim(f2,f2,f2,f2,f2);

        // 获取渲染缓冲区源
        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        // 从缓冲区源获取对应渲染类型的顶点消费者
        // 将模型渲染到缓冲区
        // 参数说明：矩阵堆栈、顶点消费者、光照值、叠加纹理、RGBA颜色值（最后一个是透明度）
        float xOff = (float) (color % 8) / 8;
//        float yOff = color >= 8 ? 0.5f : 0;
//        float xOff =this.age * 0.01F % 1.0F;
//                ((float) col / 8) % 1.0F;
        float yOff = 0f;

//        System.out.println("当前偏移量" + xOff +", " + yOff);
        VertexConsumer vertexconsumer = multibuffersource$buffersource.getBuffer(RenderType.energySwirl(getTextureLocation(pBuffer,pRenderInfo,pPartialTicks),xOff,yOff));
        this.model.renderToBuffer(posestack, vertexconsumer, 15728880, OverlayTexture.NO_OVERLAY, this.rCol, this.gCol, this.bCol, this.alpha);

        // 结束当前渲染批次，提交渲染数据
        multibuffersource$buffersource.endBatch();
    }

    /**
     * 获取粒子的缩放倍率
     * @param pScaleFactor
     * @return
     */
    public float getQuadSize(float pScaleFactor) {
//        float f = ((float)this.age + pScaleFactor) / (float)this.lifetime;
//        if (this.age <= 5) {
//            float f1 = this.age / 5;
//            float f2 = this.quadSize * (f1 * f * 10 * this.lifetime / 64);
//            return f2 > 1f ? 1 : f2;
//        } else {
////            if (f > 0.5f) {
////                f -= 0.5f;
////                return this.quadSize * (1.3F + f * 0.6f);
////            } else return this.quadSize * (0.8F + f);
//            return this.quadSize + 0.5f * Mth.sin((f - 0.05f) * 0.5f * (float)Math.PI);
//        }
        int col = this.color >> 4;
        if (col < 0) col *= -1;
        return this.quadSize + 0.25f * (col);
    }

    /**
     * 每次渲染时进行的额外操作，这里用于计算透明度的变化
     * @param pBuffer
     * @param pRenderInfo
     * @param pPartialTicks
     */
    protected void doOnRender(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {
//        // 计算粒子生命周期进度（0.0到1.0）
//        float f = ((float)this.age + pPartialTicks) / (float)this.lifetime;
//
//        // 根据正弦函数计算透明度（实现淡入淡出效果：0→1→0）
//        float f1 = 0.05f;
//        //float f1_layer = f1;
//
//        if (f < 0.5f) {
//            f1 += 0.8f;
//        } else f1 += (f <= 0.5f ? 0 : 0.3f) + 0.5f * Mth.sin(f * (float)Math.PI);
//        setAlpha(f1);
    }

    protected void coreAnim(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {
        this.model.getAllBone().xRot = this.xRot;
        this.model.getAllBone().yRot = this.yRot;
        this.model.getAllBone().zRot = this.zRot;
    }

    public ResourceLocation getTextureLocation(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {
        // 计算粒子生命周期进度（0.0到1.0）
        float f = ((float)this.age + pPartialTicks) / (float)this.lifetime;

//        if (f < 0.5f) {
            return TEXTURE_LOCATION;
//        } else return LAYER_TEXTURE_LOCATION;
    }

//    public ResourceLocation getLayerTextureLocation(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {
//        return LAYER_TEXTURE_LOCATION;
//    }
}
