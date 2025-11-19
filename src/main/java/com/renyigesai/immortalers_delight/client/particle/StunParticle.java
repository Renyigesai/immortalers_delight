package com.renyigesai.immortalers_delight.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


@OnlyIn(Dist.CLIENT)
public class StunParticle extends TextureSheetParticle {

    public static StunParticleProvider provider(SpriteSet spriteSet) {
        return new StunParticleProvider(spriteSet);
    }

    public static class  StunParticleProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public  StunParticleProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new StunParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }

    private final SpriteSet spriteSet;
    // 圆周运动参数：半径、角速度（控制旋转速度）
    private final double radius;
    private final double angularSpeed;
    // 初始角度（随机化，使多个粒子旋转不同步）
    private double initialAngle;

    protected StunParticle(ClientLevel world, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteSet) {
        super(world, x, y, z);
        this.spriteSet = spriteSet;
        this.setSize(0.2f, 0.2f);
        this.quadSize *= 3f;
        this.lifetime = 28;
        this.gravity = -0.45f;
        this.hasPhysics = true;
        this.xd = vx * 1;
        this.yd = vy * 1;
        this.zd = vz * 1;
        this.radius = 1.0;
        this.angularSpeed = 0.3;
        this.setSpriteFromAge(spriteSet);
    }
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        if (this.age % 5 == 0) this.gravity *= -1;
        super.tick();
        if (!this.removed) {
            this.setSprite(this.spriteSet.get((this.age / 2) % 12 + 1, 12));
        }
    }

    @Override
    protected void setLocationFromBoundingbox() {
        AABB aabb = this.getBoundingBox();
        double Cx = (aabb.minX + aabb.maxX) / 2.0D;
        double Cy = aabb.minY;
        double Cz = (aabb.minZ + aabb.maxZ) / 2.0D;
        // 计算当前角度：初始角度 + 角速度 * 已存在时间（实现匀速圆周运动）
        double currentAngle = initialAngle + angularSpeed * this.age;
        // 基于角度计算XZ平面上的圆周坐标（Y轴位置不变，可根据需求调整）
        double xOffset = radius * Math.cos(currentAngle);
        double zOffset = radius * Math.sin(currentAngle);
        // 更新粒子位置（以碰撞箱位置为圆心）
        this.x = Cx + xOffset;
        this.y = Cy;
        this.z = Cz + zOffset;
    }
}
