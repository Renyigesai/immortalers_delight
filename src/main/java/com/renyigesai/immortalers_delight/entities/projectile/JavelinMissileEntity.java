package com.renyigesai.immortalers_delight.entities.projectile;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class JavelinMissileEntity extends Projectile {
    private enum FlightPhase { CLIMBING, DIVING }

    private FlightPhase phase = FlightPhase.CLIMBING;
    private Entity target;
    private UUID targetUUID;
    private int flightTicks = 0;

    // 爬升锚点的高空偏移（比如目标头顶 24 格高空）
    private static final double CRUISE_ALTITUDE_OFFSET = 24.0D;

    public JavelinMissileEntity(EntityType<? extends JavelinMissileEntity> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    public JavelinMissileEntity(EntityType<? extends JavelinMissileEntity> entityType, Level level, LivingEntity shooter, Entity target) {
        this(entityType, level);
        this.setOwner(shooter);
        this.setPos(shooter.getX(), shooter.getEyeY() - 0.1D, shooter.getZ());
        this.target = target;
        this.targetUUID = target.getUUID();

        // 赋予初始速度（朝向发射者当前视线方向）
//        Vec3 initialDir = shooter.getLookAngle().normalize().scale(BASE_SPEED);
//        this.setDeltaMovement(initialDir);
    }
    @Override
    protected void defineSynchedData() {
        // 如有同步发光或特定形态状态在此注册 DataSerializers
    }

    @Override
    public void tick() {
        super.tick();
        this.flightTicks++;

        if (!this.level().isClientSide) {
            if (this.target == null || !this.target.isAlive() || this.flightTicks > 240) {
                this.discard();
                return;
            }

            this.updateJavelinTrajectory();

            // 碰撞检测
            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hitresult.getType() != HitResult.Type.MISS) {
                this.onHit(hitresult);
            }
        }

        // 物理位移
        Vec3 motion = this.getDeltaMovement();
        this.setPos(this.getX() + motion.x, this.getY() + motion.y, this.getZ() + motion.z);
        ProjectileUtil.rotateTowardsMovement(this, 0.5F);

        // 粒子：高空拉烟
        if (this.level().isClientSide) {
            this.level().addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
        }
    }

    private void updateJavelinTrajectory() {
        Vec3 currentMovement = this.getDeltaMovement();
        Vec3 currentDirection = currentMovement.normalize();
        Vec3 targetPos;
        double currentSpeed;
        double turnRate;

        if (this.phase == FlightPhase.CLIMBING) {
            // 阶段 1：向“目标上方空中”的虚拟锚点拉升
            targetPos = this.target.position().add(0, this.target.getBbHeight() + CRUISE_ALTITUDE_OFFSET, 0);
            currentSpeed = 0.7D;
            turnRate = 0.08D; // 爬升期弧线柔和

            // 转换条件：弹体高度接近目标上空，或者在 XZ 水平距离上已经非常接近
            double horizontalDistSq = this.distanceToSqr(targetPos.x, this.getY(), targetPos.z);
            boolean reachedApex = this.getY() >= targetPos.y - 4.0D;
            if (reachedApex || horizontalDistSq < 16.0D || this.flightTicks > 40) {
                this.phase = FlightPhase.DIVING; // 转入末端俯冲
            }
        } else {
            // 阶段 2：末端俯冲打击目标本身
            targetPos = this.target.position().add(0, this.target.getBbHeight() * 0.5D, 0);
            currentSpeed = 1.35D; // 俯冲期大幅加速（突破近防）
            turnRate = 0.25D;     // 俯冲期锁定过载加大，防止因速度太快脱靶
        }

        // 向量平滑偏转
        Vec3 desiredDirection = targetPos.subtract(this.position()).normalize();
        Vec3 steer = desiredDirection.subtract(currentDirection).scale(turnRate);
        Vec3 newDirection = currentDirection.add(steer).normalize();

        this.setDeltaMovement(newDirection.scale(currentSpeed));
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        // 灌顶打击附加穿甲/暴击效果
        result.getEntity().hurt(this.damageSources().explosion(this, this.getOwner()), 12.0F);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), 2.0F, Level.ExplosionInteraction.MOB);
            this.discard();
        }
    }
}
