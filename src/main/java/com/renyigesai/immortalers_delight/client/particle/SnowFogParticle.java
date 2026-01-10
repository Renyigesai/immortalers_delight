package com.renyigesai.immortalers_delight.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


@OnlyIn(Dist.CLIENT)
public class SnowFogParticle extends TextureSheetParticle {

    public static SnowFogParticleProvider snowFogParticleProvider(SpriteSet spriteSet) {
        return new SnowFogParticleProvider(spriteSet);
    }

    public static class SnowFogParticleProvider implements ParticleProvider<SnowFogParticleOption> {
        private final SpriteSet spriteSet;

        public SnowFogParticleProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SnowFogParticleOption typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SnowFogParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet,typeIn.getCountdown());
        }
    }

    private final SpriteSet spriteSet;
    // 圆周运动参数：半径、角速度（控制旋转速度）
    private final double radius;
    private double angularSpeed;
    // 初始角度（随机化，使多个粒子旋转不同步）
    private double initialAngle;
    private int extra_radius;

    protected SnowFogParticle(ClientLevel world, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteSet, int ex_radius) {
        super(world, x, y, z);
        this.spriteSet = spriteSet;
        this.setSize(0.3f, 0.3f);
        this.quadSize *= 5f;
        this.lifetime = 53;
        this.gravity = -0.45f;
        this.hasPhysics = false;
        this.xd = vx * 1;
        this.yd = vy * 1;
        this.zd = vz * 1;
        //this.yd += 0.1D;
        this.radius = 1.0;
        this.extra_radius = ex_radius;
        this.angularSpeed = 0.35;
        this.initialAngle = Math.random() * Math.PI * 2;
        //this.setSpriteFromAge(spriteSet);
        this.pickSprite(spriteSet);
    }
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        if (this.age % 5 == 0) this.gravity *= -1;
        if (!this.removed) {
            if (this.age <= 10) this.angularSpeed *= 0.9;
            else this.angularSpeed *= 0.99;
            //this.setSprite(this.spriteSet.get((this.age / 2) % 12 + 1, 12));
        }

        super.tick();
    }

    public float getRadius(float pScaleFactor) {
        // 动画进度，取值从0增长到1,
        float animation  = Mth.clamp((this.age + pScaleFactor) / (float)this.lifetime * 0.75F, 0.0F, 1.0F);
        return (float) (this.radius + animation * extra_radius);
    }

    /**
     * 获取粒子的光照值（固定最大亮度）
     * @param pPartialTick 部分刻时间
     * @return 打包的光照值（240=最大亮度，不受场景光照影响）
     */
    public int getLightColor(float pPartialTick) {
        return 240;
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
        double xOffset = getRadius(0.5f) * Math.cos(currentAngle);
        double zOffset = getRadius(0.5f) * Math.sin(currentAngle);
        // 更新粒子位置（以碰撞箱位置为圆心）
        this.x = Cx + xOffset;
        this.y = Cy;
        this.z = Cz + zOffset;
    }
}
