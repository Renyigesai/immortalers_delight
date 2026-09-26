package com.renyigesai.immortalers_delight.client.particle;

/* imports omitted */

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PurpleGlimmerParticle extends TextureSheetParticle {
    public static PurpleGlimmerParticleProvider provider(SpriteSet spriteSet) {
        return new PurpleGlimmerParticleProvider(spriteSet);
    }

    public static class PurpleGlimmerParticleProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public PurpleGlimmerParticleProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new PurpleGlimmerParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }

    private final SpriteSet spriteSet;

    protected PurpleGlimmerParticle(ClientLevel world, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteSet) {
        super(world, x, y, z);
        this.spriteSet = spriteSet;
        this.setSize(0.3f, 0.3f);
        this.quadSize *= 3f;
        this.lifetime = 6 + world.random.nextInt(3);
        this.gravity = -0.1f;
        this.hasPhysics = false;
        this.xd = vx * 1;
        this.yd = vy * 1;
        this.zd = vz * 1;
        this.setSpriteFromAge(spriteSet);
    }

    public int getLightColor(float pPartialTick) {
        float f = ((float)this.age + pPartialTick) / (float)this.lifetime;
        f *= 3;
        f = Mth.clamp(f, 0.0F, 1.0F);
        int i = super.getLightColor(pPartialTick);
        int j = i & 255;
        int k = i >> 16 & 255;
        j += (int)(f * 15.0F * 16.0F);
        if (j > 240) {
            j = 240;
        }

        return j | k << 16;
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

            this.quadSize = lastQuadSize * (1 + 0.2f * Mth.sin(this.age) );
        }
    }

}