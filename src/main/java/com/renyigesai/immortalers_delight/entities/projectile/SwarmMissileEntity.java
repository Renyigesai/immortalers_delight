package com.renyigesai.immortalers_delight.entities.projectile;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

//散射飞弹的代码
public class SwarmMissileEntity extends Projectile {
    private Entity target;
    private int ticksExisted = 0;

    // 阶段计时常数
    private static final int DISPERSAL_TICKS = 3; // 纯散射阶段
    private static final int ENGAGE_TICKS = 8;    // 完成完全锁定的过渡时刻

    public SwarmMissileEntity(EntityType<? extends SwarmMissileEntity> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    public SwarmMissileEntity(EntityType<? extends SwarmMissileEntity> type, Level level, LivingEntity shooter, Entity target) {
        super(type, level);
        this.noPhysics = true;
        shootSwarm(level,shooter,target);
    }
    /**
     * 发射构造函数：赋予散射速度
     */
    public void shootSwarm(Level level, LivingEntity shooter, Entity target) {
        SwarmMissileEntity missile = this;
//                new SwarmMissileEntity(/* 你的EntityType */ null, level);
        missile.setOwner(shooter);
        missile.setPos(shooter.getX(), shooter.getEyeY() - 0.2D, shooter.getZ());
        missile.target = target;

        // 1. 基准向前速度
        Vec3 forward = shooter.getLookAngle().normalize().scale(0.4D);

        // 2. 构造垂直于前进方向的随机环状散射力 (Cone Spreading)
        Vec3 randomSpread = new Vec3(
                (missile.random.nextDouble() - 0.5D),
                (missile.random.nextDouble() - 0.5D),
                (missile.random.nextDouble() - 0.5D)
        ).normalize().scale(0.8D); // 扩散横向冲量

        missile.setDeltaMovement(forward.add(randomSpread));
        level.addFreshEntity(missile);
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    public void tick() {
        super.tick();
        this.ticksExisted++;

        if (!this.level().isClientSide) {
            if (this.ticksExisted > 160 || (this.target != null && !this.target.isAlive())) {
                this.discard();
                return;
            }

            this.updateSwarmTrajectory();

            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hitresult.getType() != HitResult.Type.MISS) {
                this.onHit(hitresult);
            }
        }

        // 运动更新
        Vec3 motion = this.getDeltaMovement();
        this.setPos(this.getX() + motion.x, this.getY() + motion.y, this.getZ() + motion.z);
        ProjectileUtil.rotateTowardsMovement(this, 0.5F);

        // 客户端微光拖尾
        if (this.level().isClientSide) {
            this.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
        }
    }

    private void updateSwarmTrajectory() {
        Vec3 currentMovement = this.getDeltaMovement();

        // 阶段 1：散射期，只给微弱空气阻力，任由其散开
        if (this.ticksExisted < DISPERSAL_TICKS) {
            this.setDeltaMovement(currentMovement.scale(0.96D)); // 略微减速，蓄势待发
            return;
        }

        // 阶段 2 & 3：介入寻敌与加速
        if (this.target == null) return;

        Vec3 targetCenter = this.target.position().add(0, this.target.getBbHeight() * 0.5D, 0);
        Vec3 desiredDirection = targetCenter.subtract(this.position()).normalize();
        Vec3 currentDirection = currentMovement.normalize();

        // 动态计算混合因子：从 DISPERSAL_TICKS 到 ENGAGE_TICKS 从 0 渐进增大到 0.18
        float progress = (float)(this.ticksExisted - DISPERSAL_TICKS) / (float)(ENGAGE_TICKS - DISPERSAL_TICKS);
        float homingStrength = Mth.clamp(progress, 0.0F, 1.0F) * 0.20F; // 峰值转向系数 0.2

        // 动态标量速度：散开时慢，一旦重新锁定就极速爆发
        double speed = Mth.lerp(Mth.clamp(progress, 0.0F, 1.0F), 0.35D, 1.1D);

        // 平滑变轨
        Vec3 steer = desiredDirection.subtract(currentDirection).scale(homingStrength);
        Vec3 finalDirection = currentDirection.add(steer).normalize();

        this.setDeltaMovement(finalDirection.scale(speed));
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), 0.8F, Level.ExplosionInteraction.NONE);
            this.discard();
        }
    }
}
