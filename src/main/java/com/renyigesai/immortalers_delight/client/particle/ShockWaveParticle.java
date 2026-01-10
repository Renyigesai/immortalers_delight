package com.renyigesai.immortalers_delight.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.function.Consumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * 尖啸粒子类（对应监守者/潜声尖啸的视觉特效）
 * 继承自纹理图集粒子，实现延迟触发、双旋转层叠、渐隐消失的十字形尖啸视觉效果
 * 仅在客户端运行
 */
@OnlyIn(Dist.CLIENT)
public class ShockWaveParticle extends TextureSheetParticle {
    // 旋转基准向量（归一化的0.5,0.5,0.5向量，用于初始化旋转四元数）
    private static final Vector3f ROTATION_VECTOR = (new Vector3f(0.5F, 0.5F, 0.5F)).normalize();
    // 顶点变换基准向量（用于旋转后的顶点偏移）
    private static final Vector3f TRANSFORM_VECTOR = new Vector3f(-1.0F, -1.0F, 0.0F);
    // X轴旋转角度常量（1.0472弧度 = π/3 = 60°）
    private static final float MAGICAL_X_ROT = 1.0472F;
    // 粒子延迟触发时间（刻数，延迟期间粒子不渲染、不执行运动逻辑）
    private int extra_size;
    private final SpriteSet spriteSet;

    /**
     * 构造方法：初始化尖啸粒子基础属性
     * @param pLevel 客户端世界实例
     * @param pX 粒子初始X坐标
     * @param pY 粒子初始Y坐标
     * @param pZ 粒子初始Z坐标
     */
    ShockWaveParticle(ClientLevel pLevel, double pX, double pY, double pZ, SpriteSet spriteSet, int pSize) {
        super(pLevel, pX, pY, pZ, 0.0D, 0.0D, 0.0D);
        this.spriteSet = spriteSet;
        this.quadSize = 2.0F;        // 粒子初始四边形尺寸
        this.extra_size = pSize;      // 设置额外缩放尺寸
        this.lifetime = 35;           // 粒子总生命周期（30刻=1.5秒）
        this.gravity = 0.0F;          // 无重力（粒子悬浮，不下落）
        this.xd = 0.0D;               // X轴速度为0（无水平移动）
        this.yd = 0.01D;               // Y轴轻微向上速度（模拟升腾效果）
        this.zd = 0.0D;               // Z轴速度为0（无水平移动）
        this.setSpriteFromAge(this.spriteSet);
    }

    /**
     * 获取粒子当前帧的四边形尺寸（动态缩放）
     * @param pScaleFactor 部分刻时间（0~1，用于帧间插值）
     * @return 缩放后的粒子尺寸
     */
    public float getQuadSize(float pScaleFactor) {
        // 动画进度，取值从0增长到1,
        float animation  = Mth.clamp((this.age + pScaleFactor) / (float)this.lifetime * 0.75F, 0.0F, 1.0F);
        // 初始粒子尺寸
        float quadSize0 = 0.5F;
        // 根据额外缩放尺寸，动态计算粒子的最终扩散尺寸
        float quadSize1 = 1.0F + (float) extra_size / 4;
        // 计算粒子尺寸变化
        float quadSize2 = quadSize1 - quadSize0;
        // 计算粒子的缩放，其将从quadSize0到quadSize1变化
        return this.quadSize * (easeOutExpo(animation) * quadSize2 + quadSize0);
    }

    public static float easeOutExpo(float t) {
        return t >= 1.0f ? 1.0f : 1 - (float) Math.pow(2, -10 * t);
    }

    /**
     * 核心渲染方法：实现延迟渲染、双旋转层叠、渐隐效果
     * @param pBuffer 顶点消费者，用于写入顶点数据到渲染管线
     * @param pRenderInfo 相机信息（包含视角位置、旋转等）
     * @param pPartialTicks 部分刻时间（0~1，用于帧间插值）
     */
    public void render(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {
       if (this.age > this.lifetime / 2) {
           // 计算透明度：随生命周期从1.0F线性降为0.5F（实现渐隐消失）
           this.alpha = 1.0F - Mth.clamp(2 * ((float)this.age - (this.lifetime / 2) + pPartialTicks) / (float)this.lifetime, 0.0F, 0.75F);
       }
        // 渲染粒子四边形，因为粒子与方块一样仅渲染面向相机的面，所以需要两个反向的面使得不始终面向相机的粒子能在各个方向都能观察到
        // 渲染第一层粒子：绕X轴旋转-60°（形成十字形的一个臂）
        this.renderRotatedParticle(pBuffer, pRenderInfo, pPartialTicks, (p_253347_) -> {
            p_253347_.mul((new Quaternionf()).rotationX(-(float)Math.PI/2));
        });

        // 渲染第二层粒子：绕Y轴旋转180° + 绕X轴旋转60°（形成十字形的另一个臂）
        this.renderRotatedParticle(pBuffer, pRenderInfo, pPartialTicks, (p_253346_) -> {
            p_253346_.mul((new Quaternionf()).rotationYXZ(-(float)Math.PI, (float)Math.PI/2, 0.0F));
        });

    }

    /**
     * 渲染带自定义旋转的粒子四边形（核心顶点构建逻辑）
     * @param pConsumer 顶点消费者
     * @param pCamera 相机信息
     * @param pPartialTick 部分刻时间
     * @param pQuaternion 旋转四元数的自定义逻辑（用于实现不同层的旋转）
     */
    private void renderRotatedParticle(VertexConsumer pConsumer, Camera pCamera, float pPartialTick, Consumer<Quaternionf> pQuaternion) {
        // 1. 计算粒子相对于相机的位置（锚定自身XYZ坐标，不随相机漂移）
        Vec3 vec3 = pCamera.getPosition(); // 获取相机当前世界坐标
        // 插值计算粒子当前帧的X坐标（xo=上一帧，x=当前帧，pPartialTick=帧间进度）
        float f = (float)(Mth.lerp((double)pPartialTick, this.xo, this.x) - vec3.x());
        float f1 = (float)(Mth.lerp((double)pPartialTick, this.yo, this.y) - vec3.y()); // Y坐标
        float f2 = (float)(Mth.lerp((double)pPartialTick, this.zo, this.z) - vec3.z()); // Z坐标

        // 2. 初始化旋转四元数（0角度旋转），并应用自定义旋转逻辑
        Quaternionf quaternionf = (new Quaternionf()).setAngleAxis(0.0F, ROTATION_VECTOR.x(), ROTATION_VECTOR.y(), ROTATION_VECTOR.z());
        pQuaternion.accept(quaternionf); // 执行自定义旋转（如绕X/Y轴旋转）
        quaternionf.transform(TRANSFORM_VECTOR); // 应用顶点变换向量

        // 3. 构建四边形的四个顶点（左下、左上、右上、右下）
        Vector3f[] avector3f = new Vector3f[]{
                new Vector3f(-1.0F, -1.0F, 0.0F), // 左下顶点
                new Vector3f(-1.0F, 1.0F, 0.0F),  // 左上顶点
                new Vector3f(1.0F, 1.0F, 0.0F),   // 右上顶点
                new Vector3f(1.0F, -1.0F, 0.0F)   // 右下顶点
        };
        float f3 = this.getQuadSize(pPartialTick); // 获取当前帧粒子尺寸

        // 4. 对每个顶点应用旋转、缩放、平移
        for(int i = 0; i < 4; ++i) {
            Vector3f vector3f = avector3f[i];
            vector3f.rotate(quaternionf); // 应用旋转
            vector3f.mul(f3);             // 应用缩放
            vector3f.add(f, f1, f2);      // 平移到粒子相对于相机的位置
        }

        // 5. 获取粒子光照值，写入四个顶点数据
        int j = this.getLightColor(pPartialTick);
        this.makeCornerVertex(pConsumer, avector3f[0], this.getU1(), this.getV1(), j); // 左下顶点
        this.makeCornerVertex(pConsumer, avector3f[1], this.getU1(), this.getV0(), j); // 左上顶点
        this.makeCornerVertex(pConsumer, avector3f[2], this.getU0(), this.getV0(), j); // 右上顶点
        this.makeCornerVertex(pConsumer, avector3f[3], this.getU0(), this.getV1(), j); // 右下顶点
    }

    /**
     * 封装顶点写入逻辑，统一传递顶点坐标、UV、光照等参数
     * @param pConsumer 顶点消费者
     * @param pVertex 顶点坐标
     * @param pU 纹理U坐标
     * @param pV 纹理V坐标
     * @param pPackedLight 打包的光照信息（天空光+方块光）
     */
    private void makeCornerVertex(VertexConsumer pConsumer, Vector3f pVertex, float pU, float pV, int pPackedLight) {
        pConsumer.vertex((double)pVertex.x(), (double)pVertex.y(), (double)pVertex.z()) // 顶点坐标
                .uv(pU, pV)                                                             // 纹理UV坐标
                .color(this.rCol, this.gCol, this.bCol, this.alpha)                     // 粒子颜色+透明度
                .uv2(pPackedLight)                                                      // 光照信息
                .endVertex();                                                           // 结束顶点写入
    }

    /**
     * 获取粒子的光照值（固定最大亮度）
     * @param pPartialTick 部分刻时间
     * @return 打包的光照值（240=最大亮度，不受场景光照影响）
     */
    public int getLightColor(float pPartialTick) {
        return 240;
    }

    /**
     * 获取粒子渲染类型（半透明粒子层）
     * @return 半透明粒子渲染类型，支持透明度渐变
     */
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    /**
     * 粒子每帧更新逻辑
     */
    public void tick() {
        super.tick();
        if (!this.removed) {
            this.setSprite(this.spriteSet.get((this.age / 3) % 12 + 1, 12));
        }
    }

    public static ShockWaveParticleProvider shockWaveParticleProvider(SpriteSet sprite) {
        return new ShockWaveParticleProvider(sprite);
    }

    /**
     * 尖啸粒子工厂类，用于创建粒子实例（适配Forge粒子注册规范）
     */
    @OnlyIn(Dist.CLIENT)
    public static class ShockWaveParticleProvider implements ParticleProvider<ShockWaveParticleOption> {
        private final SpriteSet sprite; // 粒子纹理精灵集（存储多个纹理帧）

        /**
         * 工厂类构造方法
         * @param pSprite 纹理精灵集
         */
        public ShockWaveParticleProvider(SpriteSet pSprite) {
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
        public Particle createParticle(ShockWaveParticleOption pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            ShockWaveParticle shockwaveparticle = new ShockWaveParticle(pLevel, pX, pY, pZ, sprite, pType.getCountdown());
            shockwaveparticle.setAlpha(1.0F);          // 初始化透明度为1.0F（完全不透明）
            return shockwaveparticle;
        }
    }
}
