package com.renyigesai.immortalers_delight.client.particle;

/* imports omitted */

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PurpleLightParticle extends TextureSheetParticle {
    public static PurpleLightParticleProvider provider(SpriteSet spriteSet) {
        return new PurpleLightParticleProvider(spriteSet);
    }

    public static class PurpleLightParticleProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public PurpleLightParticleProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new PurpleLightParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }

    private final SpriteSet spriteSet;

    protected PurpleLightParticle(ClientLevel world, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteSet) {
        super(world, x, y, z);
        this.spriteSet = spriteSet;
        this.setSize(0.3f, 0.3f);
        this.quadSize *= 3f;
        this.lifetime = 8 + world.random.nextInt(3);
        this.gravity = -0.1f;
        this.hasPhysics = false;
        this.xd = vx * 1;
        this.yd = vy * 1;
        this.zd = vz * 1;
        this.setSpriteFromAge(spriteSet);
    }

    public int getLightColor(float pPartialTick) {
        return 240;
    }
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    protected float quadSize0 = 0;
    @Override
    public void tick() {
        super.tick();
        if (!this.removed) {
            this.setSprite(this.spriteSet.get((this.age / 2) % 12 + 1, 12));

            if (this.quadSize0 <= 0) this.quadSize0 = this.quadSize;
            float lastQuadSize = this.quadSize0;

            float f = (float)this.age / (float)this.lifetime;
            if (this.age <= 3) {
                float f1 = (float) this.age / 5;
                float f2 = lastQuadSize * (f1 * f * 20 * this.lifetime / 64);
                this.quadSize = f2 > 1f ? 1 : f2;
            } else {
                if (f > 0.5f) {
                    f -= 0.5f;
                    this.quadSize *= (1.3F + f * 0.6f);
                } else this.quadSize *= (0.8F + f);
                this.quadSize += 0.5f * Mth.sin((f - 0.05f) * 0.5f * (float)Math.PI);
            }

            // 根据正弦函数计算透明度（实现淡入淡出效果：0→1→0）
            float f1 = 0.05f;

            if (f < 0.5f) {
                f1 += 0.8f;
            } else f1 += (f <= 0.5f ? 0 : 0.3f) + 0.5f * Mth.sin(f * (float)Math.PI);
            setAlpha(f1);
        }
    }

}