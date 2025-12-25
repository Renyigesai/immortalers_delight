package com.renyigesai.immortalers_delight.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class GasSmokeParticle extends HugeSmokeParticle{
    public static GasSmokeParticleProvider gasSmokeProvider(SpriteSet spriteSet) {
        return new GasSmokeParticleProvider(spriteSet);
    }
    /**
     * 粒子提供器类，用于创建MobAppearanceParticle实例
     * 实现ParticleProvider接口，作为粒子系统的工厂
     */
    @OnlyIn(Dist.CLIENT)
    public static class GasSmokeParticleProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;
        public GasSmokeParticleProvider (SpriteSet spriteSet) {
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
        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new GasSmokeParticle(pLevel, pX, pY, pZ);
        }
    }
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(ImmortalersDelightMod.MODID,"textures/particle/gas_smoke.png");

    private static final ResourceLocation LAYER_TEXTURE_LOCATION = new ResourceLocation(ImmortalersDelightMod.MODID,"textures/particle/gas_smoke_layer.png");

    /**
     * 构造方法，初始化实体外观粒子
     * @param pLevel 客户端世界对象
     * @param pX     粒子初始X坐标
     * @param pY     粒子初始Y坐标
     * @param pZ     粒子初始Z坐标
     */
    GasSmokeParticle(ClientLevel pLevel, double pX, double pY, double pZ) {
        super(pLevel, pX, pY, pZ);
        // 设置重力
        this.gravity = -(0.002F + this.random.nextInt(2) * 0.001f);
        // 设置粒子生命周期
        this.lifetime = 60 + this.random.nextInt(240);
    }

    @Override
    protected void doOnTick() {
        return;
    }

    @Override
    public float getQuadSize(float pScaleFactor) {
        float f = ((float)this.age + pScaleFactor) / (float)this.lifetime;
        if (this.age <= 6) {
            float[] ex = {0,0,0,0.05f,0.3f,0.8f,1f};
            float f1 = (this.age - ex[this.age]) / 5;
            float f2 = (f1 * f * 10 * this.lifetime / 64);
            return this.quadSize * (f2 > 1f ? 1 : f2);
        } else if (f > 0.5f) {
            f -= 0.5f;
            return this.quadSize * (1.0F + f);
        }
        return this.quadSize;
    }

    @Override
    protected void doOnRender(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {
        // 计算粒子生命周期进度（0.0到1.0）
        float f = ((float)this.age + pPartialTicks) / (float)this.lifetime;
        float f1 = 0.05f;
        float f1_layer = f1;
        if (f <= 0.05f) {
            f *= 10;
            f1 += 0.2f + 0.45F * f;
        } else if (f < 0.5f) {
            f1 += 0.7F;
            f1_layer +=(f - 0.05F) * Mth.sin((f - 0.05f) * (float)Math.PI);
        } else {
            if (f < 0.6f) f1 += 0.2f + 0.5F * Mth.sin((f - 0.4f) * 5 * (float) Math.PI);
            f1_layer += 0.2f + 0.5F * Mth.sin(f * (float) Math.PI);
        }
        setAlpha(f1);
        setLayerAlpha(f1_layer);
    }
    @Override
    protected boolean needLayerRender(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {
        return this.layer_alpha > 0.1f;
    }
    @Override
    public ResourceLocation getTextureLocation(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {
        return TEXTURE_LOCATION;
    }
    @Override
    public ResourceLocation getLayerTextureLocation(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {
        return LAYER_TEXTURE_LOCATION;
    }
}
