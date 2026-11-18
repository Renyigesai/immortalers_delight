package com.renyigesai.immortalers_delight.entities.projectile;

import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

//这是一个使用平滑弧线追踪目标的跟踪弹实体
public class HomingMissileEntity extends Projectile {
    // 基础运动配置
    private static final double BASE_SPEED = 0.65D;        // 标量飞行速度（方块/tick）
    private static final double TURN_RATE = 0.12D;         // 转向系数（0.0 ~ 1.0），越大转向越灵敏，越小转弯半径越大
    private static final int MAX_LIFETIME = 200;           // 最长存活 10 秒（200 ticks）防永久绕圈

    @Nullable
    private Entity target;
    @Nullable
    private UUID targetUUID;
    private int lifeTicks = 0;

    public HomingMissileEntity(EntityType<? extends HomingMissileEntity> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true; // 开启无物理状态，自主接管运动与碰撞计算
    }

    public HomingMissileEntity(EntityType<? extends HomingMissileEntity> entityType, Level level, LivingEntity shooter, Entity target) {
        this(entityType, level);
        this.setOwner(shooter);
        this.setPos(shooter.getX(), shooter.getEyeY() - 0.1D, shooter.getZ());
        this.target = target;
        this.targetUUID = target.getUUID();

        // 赋予初始速度（朝向发射者当前视线方向）
        Vec3 initialDir = shooter.getLookAngle().normalize().scale(BASE_SPEED);
        this.setDeltaMovement(initialDir);
    }

    @Override
    public void tick() {
        super.tick();
        this.lifeTicks++;

        // 寿命耗尽自毁，防止未命中时在卸载边缘持续耗能
        if (!this.level().isClientSide && this.lifeTicks > MAX_LIFETIME) {
            this.discard();
            return;
        }

        // 服务端：追踪逻辑与碰撞判定
        if (!this.level().isClientSide) {
            this.updateTarget();
            this.applyHomingLogic();

            // 射线碰撞检测
            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hitresult.getType() != HitResult.Type.MISS) {
                this.onHit(hitresult);
            }
        }

        // 应用位置位移
        Vec3 move = this.getDeltaMovement();
        this.setPos(this.getX() + move.x, this.getY() + move.y, this.getZ() + move.z);

        // 视线朝向平滑旋转匹配飞行速度矢量
        ProjectileUtil.rotateTowardsMovement(this, 0.5F);

        // 客户端：渲染拖尾火焰/烟雾粒子
        if (this.level().isClientSide) {
            this.level().addParticle(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            this.level().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
        }
    }

    /**
     * 核心算法：球形/线性偏角插值，限制最大角速度
     */
    private void applyHomingLogic() {
        if (this.target == null || !this.target.isAlive()) {
            // 丢失目标后退化：保持原有惯性飞行，略带重力下坠
            this.setDeltaMovement(this.getDeltaMovement().add(0, -0.02D, 0));
            return;
        }

        // 1. 获取目标中心位置（瞄准腰部偏上，避免射在脚底下方块）
        Vec3 targetPos = this.target.position().add(0, this.target.getBbHeight() * 0.6D, 0);

        // 2. 计算当前指向目标的理想归一化向量
        Vec3 desiredDirection = targetPos.subtract(this.position()).normalize();

        // 3. 获取当前弹体飞行的方向
        Vec3 currentMovement = this.getDeltaMovement();
        Vec3 currentDirection = currentMovement.normalize();

        // 4. 向量线性插值（LERP）模拟受限的转向角速度
        // newDir = current + (desired - current) * TURN_RATE
        Vec3 steerVector = desiredDirection.subtract(currentDirection).scale(TURN_RATE);
        Vec3 newDirection = currentDirection.add(steerVector).normalize();

        // 5. 重新约束为恒定标量速率
        this.setDeltaMovement(newDirection.scale(BASE_SPEED));
    }

    /**
     * 处理跨区块或重载后的 UUID 目标还原
     */
    private void updateTarget() {
        if (this.target == null && this.targetUUID != null && this.level() instanceof ServerLevel serverLevel) {
            this.target = serverLevel.getEntity(this.targetUUID);
            if (this.target == null) {
                this.targetUUID = null; // 目标彻底不可达，清空
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity hitEntity = result.getEntity();
        Entity owner = this.getOwner();
        DamageSource source = this.damageSources().indirectMagic(this, owner);

        hitEntity.hurt(source, 6.0F); // 造成 6 点（3心）魔法伤害
        this.discard();
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            // 生成小爆炸效果（不破坏方块）
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), 1.0F, Level.ExplosionInteraction.NONE);
            this.discard();
        }
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        // 不自残且无视不可见旁观者
        return super.canHitEntity(entity) && entity != this.getOwner() && !entity.isSpectator();
    }

    // ----------------- NBT 数据持久化 -----------------

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("LifeTicks", this.lifeTicks);
        if (this.target != null) {
            tag.putUUID("Target", this.target.getUUID());
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.lifeTicks = tag.getInt("LifeTicks");
        if (tag.hasUUID("Target")) {
            this.targetUUID = tag.getUUID("Target");
        }
    }

    @Override
    protected void defineSynchedData() {
        // 如有同步发光或特定形态状态在此注册 DataSerializers
    }
}
