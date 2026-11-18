package com.renyigesai.immortalers_delight.entities.projectile;


import com.google.common.collect.Lists;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightEntities;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightParticleTypes;
import com.renyigesai.immortalers_delight.potion.immortaleffects.StunEffect;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class CollectiveSoulBoltEntity extends Projectile {
    @Nullable
    private Entity target;
    @Nullable
    private UUID targetUUID;

    private int spreadDuration = 25;
    private double baseDamage = 2.0D;
    public void setBaseDamage(double pBaseDamage) {this.baseDamage = pBaseDamage;}

    public double getBaseDamage() {return this.baseDamage;}
    private static final EntityDataAccessor<Boolean> DATA_LAUNCHED =
            SynchedEntityData.defineId(CollectiveSoulBoltEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> DATA_DANGEROUS =
            SynchedEntityData.defineId(CollectiveSoulBoltEntity.class, EntityDataSerializers.BOOLEAN);
    @Nullable
    private Direction customMoveDirection;
    private int customFlightSteps;
    private double customTargetDeltaX;
    private double customTargetDeltaY;
    private double customTargetDeltaZ;

    public CollectiveSoulBoltEntity(EntityType<? extends CollectiveSoulBoltEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noPhysics = true; // 关键：关闭原版通用重力与方块物理卡位
    }

    public CollectiveSoulBoltEntity(Level level, LivingEntity shooter, @Nullable Entity target, int spreadDuration) {
        this(ImmortalersDelightEntities.COLLECTIVE_SOUL_BOLT.get(), level);
        this.setOwner(shooter);
        this.target = target;
        this.spreadDuration = spreadDuration;

        this.moveTo(shooter.getX(), shooter.getEyeY() - 0.2D, shooter.getZ(), shooter.getYRot(), shooter.getXRot());
        this.selectSpreadDirection(Direction.Axis.Y);
    }

    public CollectiveSoulBoltEntity(Level level, LivingEntity shooter, @Nullable Entity target) {
        this(level, shooter, target, 20);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_LAUNCHED, false);
        this.entityData.define(DATA_DANGEROUS, false);
    }

    public boolean isLaunched() {
        return this.entityData.get(DATA_LAUNCHED);
    }

    public void setLaunched(boolean launched) {
        if (!this.level().isClientSide()) this.entityData.set(DATA_LAUNCHED, launched);
    }

    public void setDangerous(boolean dangerous) {
        this.entityData.set(DATA_DANGEROUS, dangerous);
    }

    public boolean isDangerous() {return this.entityData.get(DATA_DANGEROUS);}
    // ========================================================
    // 关键补齐 1：覆写网络包接收时的插值机制，杜绝二次拉扯
    // ========================================================
    @Override
    public void lerpTo(double pX, double pY, double pZ, float pYaw, float pPitch, int pPosRotationIncrements, boolean pTeleport) {
        // 直接设置坐标，拒绝原版 Entity 默认的 3 步平滑排队拉扯
        this.setPos(pX, pY, pZ);
        this.setRot(pYaw, pPitch);
    }

    //实现不同等级大小不同
    public void refreshDimensions() {
        double d0 = this.getX();
        double d1 = this.getY();
        double d2 = this.getZ();
        super.refreshDimensions();
        this.setPos(d0, d1, d2);
    }
    public @NotNull EntityDimensions getDimensions(@NotNull Pose pPose) {
        return this.isDangerous() ? super.getDimensions(pPose) : super.getDimensions(pPose).scale(0.5f);
    }

    @Override
    protected boolean canHitEntity(@NotNull Entity pTarget) {
        // 关键补齐 3：判定哪些实体可以被撞击（继承自 Projectile 的标准过滤规范）
        return super.canHitEntity(pTarget) && !pTarget.noPhysics;
    }

    @Override
    public void tick() {
        super.tick();

        // 目标反序列化恢复 (服务端)
        if (!this.level().isClientSide && this.target == null && this.targetUUID != null && this.level() instanceof ServerLevel serverLevel) {
            this.target = serverLevel.getEntity(this.targetUUID);
            if (this.target == null) {
                this.targetUUID = null;
            }
        }

        // 阶段一：扩散避障期
        if (this.tickCount < this.spreadDuration) {
            if (!this.level().isClientSide) {
                this.customTargetDeltaX = Mth.clamp(this.customTargetDeltaX * 1.025D, -1.0D, 1.0D);
                this.customTargetDeltaY = Mth.clamp(this.customTargetDeltaY * 1.025D, -1.0D, 1.0D);
                this.customTargetDeltaZ = Mth.clamp(this.customTargetDeltaZ * 1.025D, -1.0D, 1.0D);

                Vec3 curMotion = this.getDeltaMovement();
                this.setDeltaMovement(curMotion.add(
                        (this.customTargetDeltaX - curMotion.x) * 0.2D,
                        (this.customTargetDeltaY - curMotion.y) * 0.2D,
                        (this.customTargetDeltaZ - curMotion.z) * 0.2D
                ));

                if (this.customFlightSteps > 0) {
                    --this.customFlightSteps;
                    if (this.customFlightSteps == 0) {
                        this.selectSpreadDirection(this.customMoveDirection == null ? null : this.customMoveDirection.getAxis());
                    }
                }

                if (this.customMoveDirection != null) {
                    BlockPos nextPos = this.blockPosition().relative(this.customMoveDirection);
                    if (this.level().loadedAndEntityCanStandOn(nextPos, this) || !this.level().isEmptyBlock(nextPos)) {
                        this.selectSpreadDirection(this.customMoveDirection.getAxis());
                    }
                }

                // 碰撞判定
                HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
                if (hitresult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
                    this.onHit(hitresult);
                }
            }

            // 手动推进位移与朝向
            this.checkInsideBlocks();
            Vec3 motion = this.getDeltaMovement();
            this.setPos(this.getX() + motion.x, this.getY() + motion.y, this.getZ() + motion.z);
            ProjectileUtil.rotateTowardsMovement(this, 0.5F);

            if (this.tickCount + 11 >= this.spreadDuration && this.level() instanceof ServerLevel serverLevel) {
                if (this.tickCount + 11 != this.spreadDuration) {
                    this.setDeltaMovement(this.getDeltaMovement().scale(0.72).add(0, 0.04D, 0));
                } else serverLevel.sendParticles(ImmortalersDelightParticleTypes.WARMING_RING_BIG.get(),
                        this.getX() + motion.x, this.getY() + motion.y + 0.45D, this.getZ() + motion.z,
                        1,0,0,0,0);
            }
            clientTick(motion);
            return;
        }

        // 阶段二：冲刺爆发
        if (!this.level().isClientSide && !this.isLaunched()) {
            this.setLaunched(true);
            //不是危险级(大的）直接射向目标
            if (!this.isDangerous()) this.launchTowardsTargetOrLastMotion();
            //否则进行分裂
            else this.fission();
        }

        // 阶段三：超时清除
        if (!this.level().isClientSide) {
            if (this.tickCount > this.spreadDuration + 100) {
                this.discard();
                return;
            }

            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hitresult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
                this.onHit(hitresult);
                return;
            }
        }

        this.checkInsideBlocks();
        Vec3 motion = this.getDeltaMovement();
        this.setPos(this.getX() + motion.x, this.getY() + motion.y, this.getZ() + motion.z);
        ProjectileUtil.rotateTowardsMovement(this, 1.0F);

        clientTick(motion);
    }

    private void clientTick(Vec3 motion) {

        if (this.level().isClientSide) {
            ParticleOptions particleOptions = ImmortalersDelightParticleTypes.HATRED_FLAME.get();
            RandomSource source = this.random;
            float dx = 0;
            float dy = 0;
            float dz = 0;
            if (!this.isLaunched()) {

                this.level().addParticle(ParticleTypes.SOUL,
                        this.getX() - motion.x, this.getY() - motion.y + 0.45D, this.getZ() - motion.z,
                        0.0D, 0.0D, 0.0D);

                dx = source.nextFloat() - 0.5f;
                dy = source.nextFloat() - 0.5f;
                dz = source.nextFloat() - 0.5f;
                particleOptions = ParticleTypes.WITCH;
            }

            this.level().addParticle(particleOptions,
                    this.getX() - motion.x * 0.25D + dx,
                    this.getY() + 0.45D + - motion.y * 0.25D + dy,
                    this.getZ() - motion.z * 0.25D + dz,
                    0, 0, 0);
        }
    }

    private void selectSpreadDirection(@Nullable Direction.Axis pAxis) {
        BlockPos currentPos = this.blockPosition();
        List<Direction> openDirections = Lists.newArrayList();

        for (Direction dir : Direction.values()) {
            if (pAxis != null && dir.getAxis() == pAxis) {
                continue;
            }
            BlockPos targetPos = currentPos.relative(dir);
            if (this.level().isEmptyBlock(targetPos) && !this.level().loadedAndEntityCanStandOn(targetPos, this)) {
                openDirections.add(dir);
            }
        }

        Direction chosenDir = !openDirections.isEmpty()
                ? openDirections.get(this.random.nextInt(openDirections.size()))
                : Direction.getRandom(this.random);

        this.customMoveDirection = chosenDir;

        this.customFlightSteps = 10 + this.random.nextInt(10);

        double spreadSpeed = 0.19D;

        this.customTargetDeltaX = (double) chosenDir.getStepX() * spreadSpeed;
        this.customTargetDeltaY = (double) chosenDir.getStepY() * spreadSpeed;
        this.customTargetDeltaZ = (double) chosenDir.getStepZ() * spreadSpeed;

    }

    //分裂
    private void fission() {
        if (this.level().isClientSide) return;
        Vec3 baseDir;
        Vec3 currentMovement = this.getDeltaMovement();
        if (currentMovement.lengthSqr() > 1.0E-4D) {
            baseDir = currentMovement.normalize();
        } else if (this.customMoveDirection != null) {
            baseDir = new Vec3(this.customMoveDirection.getStepX(), this.customMoveDirection.getStepY(), this.customMoveDirection.getStepZ()).normalize();
        } else {
            baseDir = this.getLookAngle().normalize();
        }
        if (this.target != null && this.target.isAlive()) {
            baseDir = target.getEyePosition().subtract(this.getEyePosition()).normalize();

        }

        Vec3 spawnPos = new Vec3(this.getX(), this.getEyeY(), this.getZ());
        for (int i = 0; i < 4; i++) {
            double yawOffsetDeg = (this.random.nextDouble() - 0.5) * 30.0;
            double pitchOffsetDeg = (this.random.nextDouble() - 0.5) * 20.0;
            Vec3 spreadDir = rotateVec(baseDir, yawOffsetDeg, pitchOffsetDeg);
            spawnFireballWithDir(spreadDir);
        }
        this.playSound(SoundEvents.BLAZE_SHOOT, 1.2F, 0.9F);

        if (this.level() instanceof ServerLevel serverLevel) {
            // 发射点：小团灵魂粒子（非灵魂火，普通SOUL粒子体现"汇聚能量"感）
            spawnBurstParticles(serverLevel, ParticleTypes.SOUL, spawnPos, 10, 0.2, 0.02);

            // 自身周围：向外扩散的灵魂火粒子，体现散射的"发散"视觉主题
            Vec3 bodyCenter = this.position().add(0, this.getBbHeight() / 2, 0);
            spawnBurstParticles(serverLevel, ParticleTypes.SOUL_FIRE_FLAME, bodyCenter, 20, 0.6, 0.05);
        }
    }

    //发射单发的火球
    private void spawnFireballWithDir(Vec3 lookDirection) {
        SoulFireballEntity fireball = new SoulFireballEntity(this.getX(), this.getEyeY(), this.getZ(),
                lookDirection.x * 0.5D, lookDirection.y * 0.5D,lookDirection.z * 0.5D,
                this.level()
        );
        fireball.setPos(this.getX(), this.getEyeY(), this.getZ());
        this.level().addFreshEntity(fireball);
    }

    /**
     * 将方向向量按给定的水平（yaw）和俯仰（pitch）角度偏移（角度制）。
     */
    private Vec3 rotateVec(Vec3 dir, double yawOffsetDeg, double pitchOffsetDeg) {
        double yaw = Math.atan2(dir.z, dir.x) + Math.toRadians(yawOffsetDeg);
        double horizontalLen = Math.sqrt(dir.x * dir.x + dir.z * dir.z);
        double pitch = Math.atan2(dir.y, horizontalLen) + Math.toRadians(pitchOffsetDeg);

        double newHorizontalLen = Math.cos(pitch);
        double x = Math.cos(yaw) * newHorizontalLen;
        double y = Math.sin(pitch);
        double z = Math.sin(yaw) * newHorizontalLen;
        return new Vec3(x, y, z).normalize();
    }

    /**
     * 生成"向外扩散"的粒子团：以某点为中心，向随机方向发散。
     */
    private void spawnBurstParticles(ServerLevel serverLevel, net.minecraft.core.particles.ParticleOptions particle,
                                     Vec3 origin, int count, double spread, double speed) {
        serverLevel.sendParticles(particle, origin.x, origin.y, origin.z, count, spread, spread, spread, speed);
    }

    private void launchTowardsTargetOrLastMotion() {
        Vec3 launchDir;

        if (this.target != null && this.target.isAlive()) {
            Vec3 targetCenter = this.target.position().add(0, this.target.getBbHeight() * 0.5D, 0);
            launchDir = targetCenter.subtract(this.position()).normalize();
        } else {
            Vec3 currentMovement = this.getDeltaMovement();
            if (currentMovement.lengthSqr() > 1.0E-4D) {
                launchDir = currentMovement.normalize();
            } else if (this.customMoveDirection != null) {
                launchDir = new Vec3(this.customMoveDirection.getStepX(), this.customMoveDirection.getStepY(), this.customMoveDirection.getStepZ()).normalize();
            } else {
                launchDir = this.getLookAngle().normalize();
            }
        }

        double rushSpeed = 1.6D;
        this.setDeltaMovement(launchDir.scale(rushSpeed));
        this.hasImpulse = true;

        this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                SoundEvents.SHULKER_BULLET_HIT, SoundSource.HOSTILE, 1.0F, 0.5F);
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                SoundEvents.BLAZE_SHOOT, SoundSource.HOSTILE, 0.8F, 1.7F);
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        if (!this.level().isClientSide) {
            Entity hitEntity = pResult.getEntity();
            Entity owner = this.getOwner();

            float damage = (float) this.getBaseDamage();
            if (damage < 8) damage = 8.0F;

            boolean hurtSuccess = hitEntity.hurt(this.damageSources().explosion(this, owner), damage);

            if (hurtSuccess && hitEntity instanceof LivingEntity livingHit) {
                int stunDuration = DifficultyModeUtil.isPowerBattleMode() ? 40 : 15;
                double knockbackResist = livingHit.getAttribute(Attributes.KNOCKBACK_RESISTANCE) != null ?
                        livingHit.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE) : 0;
                stunDuration *= (1.0 - knockbackResist);
                if (stunDuration > 0) {
                    StunEffect.applyImmortalEffect(livingHit, stunDuration, 0);
                }
            }

            this.spawnHitBox(damage,target.blockPosition());
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        if (!this.level().isClientSide) {
            Entity owner = this.getOwner();

            float damage = (float) this.getBaseDamage();
            if (damage < 8) damage = 8.0F;

            this.spawnHitBox(damage,pResult.getBlockPos());
        }
    }

    private void spawnHitBox(float damage, BlockPos pos) {
        SoulFireHitboxEntity hitBoxEntity = new SoulFireHitboxEntity(
                this.level(),
                pos.getX() + 0.5f,
                pos.getY(),
                pos.getZ() + 0.5f,
                this.getYHeadRot(),
                10,
                damage,
                this.getOwner() instanceof LivingEntity ? (LivingEntity) this.getOwner() : null
        );
        hitBoxEntity.setRadius(this.isDangerous() ? 4.0f : 2.5F);
        this.level().addFreshEntity(hitBoxEntity);
        this.discard();
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putBoolean("Launched", this.isLaunched());
        pCompound.putInt("SpreadDuration", this.spreadDuration);
        pCompound.putDouble("damage", this.baseDamage);
        pCompound.putInt("CustomSteps", this.customFlightSteps);
        pCompound.putDouble("CustomTXD", this.customTargetDeltaX);
        pCompound.putDouble("CustomTYD", this.customTargetDeltaY);
        pCompound.putDouble("CustomTZD", this.customTargetDeltaZ);
        if (this.customMoveDirection != null) {
            pCompound.putInt("CustomDir", this.customMoveDirection.get3DDataValue());
        }
        if (this.target != null) {
            pCompound.putUUID("CustomTarget", this.target.getUUID());
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setLaunched(pCompound.getBoolean("Launched"));
        if (pCompound.contains("damage", 99)) {
            this.baseDamage = pCompound.getDouble("damage");
        }
        if (pCompound.contains("SpreadDuration")) {
            this.spreadDuration = pCompound.getInt("SpreadDuration");
        }
        this.customFlightSteps = pCompound.getInt("CustomSteps");
        this.customTargetDeltaX = pCompound.getDouble("CustomTXD");
        this.customTargetDeltaY = pCompound.getDouble("CustomTYD");
        this.customTargetDeltaZ = pCompound.getDouble("CustomTZD");
        if (pCompound.contains("CustomDir", 99)) {
            this.customMoveDirection = Direction.from3DDataValue(pCompound.getInt("CustomDir"));
        }
        if (pCompound.hasUUID("CustomTarget")) {
            this.targetUUID = pCompound.getUUID("CustomTarget");
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
//
//import com.google.common.base.MoreObjects;
//import com.google.common.collect.Lists;
//import com.renyigesai.immortalers_delight.init.ImmortalersDelightEntities;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.core.particles.ParticleTypes;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.sounds.SoundEvents;
//import net.minecraft.sounds.SoundSource;
//import net.minecraft.util.Mth;
//import net.minecraft.world.Difficulty;
//import net.minecraft.world.damagesource.DamageSource;
//import net.minecraft.world.effect.MobEffectInstance;
//import net.minecraft.world.effect.MobEffects;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.entity.projectile.Projectile;
//import net.minecraft.world.entity.projectile.ProjectileUtil;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.gameevent.GameEvent;
//import net.minecraft.world.phys.BlockHitResult;
//import net.minecraft.world.phys.EntityHitResult;
//import net.minecraft.world.phys.HitResult;
//import net.minecraft.world.phys.Vec3;
//
//import javax.annotation.Nullable;
//import java.util.List;
//import java.util.UUID;
//
//public class HatredfireBoltEntity extends Projectile {
//    private static final double SPEED = 0.15D;
//    @Nullable
//    private Entity finalTarget;
//    @Nullable
//    private Direction currentMoveDirection;
//    private int flightSteps;
//    private double targetDeltaX;
//    private double targetDeltaY;
//    private double targetDeltaZ;
//    @Nullable
//    private UUID targetId;
//
//    public HatredfireBoltEntity(EntityType<? extends HatredfireBoltEntity> pEntityType, Level pLevel) {
//        super(pEntityType, pLevel);
//        this.noPhysics = true;
//    }
//
//    public HatredfireBoltEntity(Level pLevel, LivingEntity pShooter, @org.jetbrains.annotations.Nullable Entity pFinalTarget, Direction.Axis pAxis) {
//        this(ImmortalersDelightEntities.HATREDFIRE_BOLT.get(), pLevel);
//        this.setOwner(pShooter);
//        BlockPos blockpos = pShooter.blockPosition();
//        double d0 = (double)blockpos.getX() + 0.5D;
//        double d1 = (double)blockpos.getY() + 0.5D;
//        double d2 = (double)blockpos.getZ() + 0.5D;
//        this.moveTo(d0, d1, d2, this.getYRot(), this.getXRot());
//        this.finalTarget = pFinalTarget;
//        this.currentMoveDirection = Direction.UP;
//        this.selectNextMoveDirection(pAxis);
//    }
//
//    public SoundSource getSoundSource() {
//        return SoundSource.HOSTILE;
//    }
//
//    protected void addAdditionalSaveData(CompoundTag pCompound) {
//        super.addAdditionalSaveData(pCompound);
//        if (this.finalTarget != null) {
//            pCompound.putUUID("Target", this.finalTarget.getUUID());
//        }
//
//        if (this.currentMoveDirection != null) {
//            pCompound.putInt("Dir", this.currentMoveDirection.get3DDataValue());
//        }
//
//        pCompound.putInt("Steps", this.flightSteps);
//        pCompound.putDouble("TXD", this.targetDeltaX);
//        pCompound.putDouble("TYD", this.targetDeltaY);
//        pCompound.putDouble("TZD", this.targetDeltaZ);
//    }
//
//    /**
//     * (abstract) Protected helper method to read subclass entity data from NBT.
//     */
//    protected void readAdditionalSaveData(CompoundTag pCompound) {
//        super.readAdditionalSaveData(pCompound);
//        this.flightSteps = pCompound.getInt("Steps");
//        this.targetDeltaX = pCompound.getDouble("TXD");
//        this.targetDeltaY = pCompound.getDouble("TYD");
//        this.targetDeltaZ = pCompound.getDouble("TZD");
//        if (pCompound.contains("Dir", 99)) {
//            this.currentMoveDirection = Direction.from3DDataValue(pCompound.getInt("Dir"));
//        }
//
//        if (pCompound.hasUUID("Target")) {
//            this.targetId = pCompound.getUUID("Target");
//        }
//
//    }
//
//    protected void defineSynchedData() {
//    }
//
//    @Nullable
//    private Direction getMoveDirection() {
//        return this.currentMoveDirection;
//    }
//
//    private void setMoveDirection(@Nullable Direction pDirection) {
//        this.currentMoveDirection = pDirection;
//    }
//
//    private void selectNextMoveDirection(@Nullable Direction.Axis pAxis) {
//        double d0 = 0.5D;
//        BlockPos blockpos;
//        if (this.finalTarget == null) {
//            blockpos = this.blockPosition().below();
//        } else {
//            d0 = (double)this.finalTarget.getBbHeight() * 0.5D;
//            blockpos = BlockPos.containing(this.finalTarget.getX(), this.finalTarget.getY() + d0, this.finalTarget.getZ());
//        }
//
//        double d1 = (double)blockpos.getX() + 0.5D;
//        double d2 = (double)blockpos.getY() + d0;
//        double d3 = (double)blockpos.getZ() + 0.5D;
//        Direction direction = null;
//        if (!blockpos.closerToCenterThan(this.position(), 2.0D)) {
//            BlockPos blockpos1 = this.blockPosition();
//            List<Direction> list = Lists.newArrayList();
//            if (pAxis != Direction.Axis.X) {
//                if (blockpos1.getX() < blockpos.getX() && this.level().isEmptyBlock(blockpos1.east())) {
//                    list.add(Direction.EAST);
//                } else if (blockpos1.getX() > blockpos.getX() && this.level().isEmptyBlock(blockpos1.west())) {
//                    list.add(Direction.WEST);
//                }
//            }
//
//            if (pAxis != Direction.Axis.Y) {
//                if (blockpos1.getY() < blockpos.getY() && this.level().isEmptyBlock(blockpos1.above())) {
//                    list.add(Direction.UP);
//                } else if (blockpos1.getY() > blockpos.getY() && this.level().isEmptyBlock(blockpos1.below())) {
//                    list.add(Direction.DOWN);
//                }
//            }
//
//            if (pAxis != Direction.Axis.Z) {
//                if (blockpos1.getZ() < blockpos.getZ() && this.level().isEmptyBlock(blockpos1.south())) {
//                    list.add(Direction.SOUTH);
//                } else if (blockpos1.getZ() > blockpos.getZ() && this.level().isEmptyBlock(blockpos1.north())) {
//                    list.add(Direction.NORTH);
//                }
//            }
//
//            direction = Direction.getRandom(this.random);
//            if (list.isEmpty()) {
//                for(int i = 5; !this.level().isEmptyBlock(blockpos1.relative(direction)) && i > 0; --i) {
//                    direction = Direction.getRandom(this.random);
//                }
//            } else {
//                direction = list.get(this.random.nextInt(list.size()));
//            }
//
//            d1 = this.getX() + (double)direction.getStepX();
//            d2 = this.getY() + (double)direction.getStepY();
//            d3 = this.getZ() + (double)direction.getStepZ();
//        }
//
//        this.setMoveDirection(direction);
//        double d6 = d1 - this.getX();
//        double d7 = d2 - this.getY();
//        double d4 = d3 - this.getZ();
//        double d5 = Math.sqrt(d6 * d6 + d7 * d7 + d4 * d4);
//        if (d5 == 0.0D) {
//            this.targetDeltaX = 0.0D;
//            this.targetDeltaY = 0.0D;
//            this.targetDeltaZ = 0.0D;
//        } else {
//            this.targetDeltaX = d6 / d5 * 0.15D;
//            this.targetDeltaY = d7 / d5 * 0.15D;
//            this.targetDeltaZ = d4 / d5 * 0.15D;
//        }
//
//        this.hasImpulse = true;
//        this.flightSteps = 10 + this.random.nextInt(5) * 10;
//    }
//
//    /**
//     * Makes the entity despawn if requirements are reached
//     */
//    public void checkDespawn() {
//        if (this.level().getDifficulty() == Difficulty.PEACEFUL) {
//            this.discard();
//        }
//
//    }
//
//    /**
//     * Called to update the entity's position/logic.
//     */
//    public void tick() {
//        super.tick();
//        if (!this.level().isClientSide) {
//            if (this.finalTarget == null && this.targetId != null) {
//                this.finalTarget = ((ServerLevel)this.level()).getEntity(this.targetId);
//                if (this.finalTarget == null) {
//                    this.targetId = null;
//                }
//            }
//
//            if (this.finalTarget == null || !this.finalTarget.isAlive() || this.finalTarget instanceof Player && this.finalTarget.isSpectator()) {
//                if (!this.isNoGravity()) {
//                    this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.04D, 0.0D));
//                }
//            } else {
//                this.targetDeltaX = Mth.clamp(this.targetDeltaX * 1.025D, -1.0D, 1.0D);
//                this.targetDeltaY = Mth.clamp(this.targetDeltaY * 1.025D, -1.0D, 1.0D);
//                this.targetDeltaZ = Mth.clamp(this.targetDeltaZ * 1.025D, -1.0D, 1.0D);
//                Vec3 vec3 = this.getDeltaMovement();
//                this.setDeltaMovement(vec3.add((this.targetDeltaX - vec3.x) * 0.2D, (this.targetDeltaY - vec3.y) * 0.2D, (this.targetDeltaZ - vec3.z) * 0.2D));
//            }
//
//            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
//            if (hitresult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
//                this.onHit(hitresult);
//            }
//        }
//
//        this.checkInsideBlocks();
//        Vec3 vec31 = this.getDeltaMovement();
//        this.setPos(this.getX() + vec31.x, this.getY() + vec31.y, this.getZ() + vec31.z);
//        ProjectileUtil.rotateTowardsMovement(this, 0.5F);
//        if (this.level().isClientSide) {
//            this.level().addParticle(ParticleTypes.END_ROD, this.getX() - vec31.x, this.getY() - vec31.y + 0.15D, this.getZ() - vec31.z, 0.0D, 0.0D, 0.0D);
//        } else if (this.finalTarget != null && !this.finalTarget.isRemoved()) {
//            if (this.flightSteps > 0) {
//                --this.flightSteps;
//                if (this.flightSteps == 0) {
//                    this.selectNextMoveDirection(this.currentMoveDirection == null ? null : this.currentMoveDirection.getAxis());
//                }
//            }
//
//            if (this.currentMoveDirection != null) {
//                BlockPos blockpos = this.blockPosition();
//                Direction.Axis direction$axis = this.currentMoveDirection.getAxis();
//                if (this.level().loadedAndEntityCanStandOn(blockpos.relative(this.currentMoveDirection), this)) {
//                    this.selectNextMoveDirection(direction$axis);
//                } else {
//                    BlockPos blockpos1 = this.finalTarget.blockPosition();
//                    if (direction$axis == Direction.Axis.X && blockpos.getX() == blockpos1.getX() || direction$axis == Direction.Axis.Z && blockpos.getZ() == blockpos1.getZ() || direction$axis == Direction.Axis.Y && blockpos.getY() == blockpos1.getY()) {
//                        this.selectNextMoveDirection(direction$axis);
//                    }
//                }
//            }
//        }
//
//    }
//
//    protected boolean canHitEntity(Entity p_37341_) {
//        return super.canHitEntity(p_37341_) && !p_37341_.noPhysics;
//    }
//
//    /**
//     * Returns {@code true} if the entity is on fire. Used by render to add the fire effect on rendering.
//     */
//    public boolean isOnFire() {
//        return false;
//    }
//
//    /**
//     * Checks if the entity is in range to render.
//     */
//    public boolean shouldRenderAtSqrDistance(double pDistance) {
//        return pDistance < 16384.0D;
//    }
//
//    public float getLightLevelDependentMagicValue() {
//        return 1.0F;
//    }
//
//    /**
//     * Called when the arrow hits an entity
//     */
//    protected void onHitEntity(EntityHitResult pResult) {
//        super.onHitEntity(pResult);
//        Entity entity = pResult.getEntity();
//        Entity entity1 = this.getOwner();
//        LivingEntity livingentity = entity1 instanceof LivingEntity ? (LivingEntity)entity1 : null;
//        boolean flag = entity.hurt(this.damageSources().mobProjectile(this, livingentity), 4.0F);
//        if (flag) {
//            this.doEnchantDamageEffects(livingentity, entity);
//            if (entity instanceof LivingEntity) {
//                LivingEntity livingentity1 = (LivingEntity)entity;
//                livingentity1.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 200), MoreObjects.firstNonNull(entity1, this));
//            }
//        }
//
//    }
//
//    protected void onHitBlock(BlockHitResult pResult) {
//        super.onHitBlock(pResult);
//        ((ServerLevel)this.level()).sendParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 2, 0.2D, 0.2D, 0.2D, 0.0D);
//        this.playSound(SoundEvents.SHULKER_BULLET_HIT, 1.0F, 1.0F);
//    }
//
//    private void destroy() {
//        this.discard();
//        this.level().gameEvent(GameEvent.ENTITY_DAMAGE, this.position(), GameEvent.Context.of(this));
//    }
//
//    /**
//     * Called when this EntityFireball hits a block or entity.
//     */
//    protected void onHit(HitResult pResult) {
//        super.onHit(pResult);
//        this.destroy();
//    }
//
//    /**
//     * Returns {@code true} if other Entities should be prevented from moving through this Entity.
//     */
//    public boolean isPickable() {
//        return true;
//    }
//
//    /**
//     * Called when the entity is attacked.
//     */
//    public boolean hurt(DamageSource pSource, float pAmount) {
//        if (!this.level().isClientSide) {
//            this.playSound(SoundEvents.SHULKER_BULLET_HURT, 1.0F, 1.0F);
//            ((ServerLevel)this.level()).sendParticles(ParticleTypes.CRIT, this.getX(), this.getY(), this.getZ(), 15, 0.2D, 0.2D, 0.2D, 0.0D);
//            this.destroy();
//        }
//
//        return true;
//    }
//
//    public void recreateFromPacket(ClientboundAddEntityPacket pPacket) {
//        super.recreateFromPacket(pPacket);
//        double d0 = pPacket.getXa();
//        double d1 = pPacket.getYa();
//        double d2 = pPacket.getZa();
//        this.setDeltaMovement(d0, d1, d2);
//    }
//}
//import com.google.common.collect.Lists;
//import com.renyigesai.immortalers_delight.init.ImmortalersDelightEntities;
//import com.renyigesai.immortalers_delight.potion.immortaleffects.StunEffect;
//import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.core.particles.ParticleTypes;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.network.protocol.Packet;
//import net.minecraft.network.protocol.game.ClientGamePacketListener;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.sounds.SoundEvents;
//import net.minecraft.sounds.SoundSource;
//import net.minecraft.util.Mth;
//import net.minecraft.world.damagesource.DamageSource;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.ai.attributes.Attributes;
//import net.minecraft.world.entity.projectile.Projectile;
//import net.minecraft.world.entity.projectile.ProjectileUtil;
//import net.minecraft.world.entity.projectile.ShulkerBullet;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.phys.BlockHitResult;
//import net.minecraft.world.phys.EntityHitResult;
//import net.minecraft.world.phys.HitResult;
//import net.minecraft.world.phys.Vec3;
//import net.minecraftforge.network.NetworkHooks;
//import org.jetbrains.annotations.NotNull;
//
//import javax.annotation.Nullable;
//import java.util.List;
//import java.util.UUID;
//
//public class HatredfireBoltEntity extends Projectile {
//    @Nullable
//    private Entity target;
//    @Nullable
//    private UUID targetUUID;
//
//    // 状态与扩散时长控制
//    private int spreadDuration = 25; // 扩散避障时长 (ticks)
//    private boolean launched = false;
//
//    // 扩散阶段的轴向移动控制参数
//    @Nullable
//    private Direction customMoveDirection;
//    private int customFlightSteps;
//    private double customTargetDeltaX;
//    private double customTargetDeltaY;
//    private double customTargetDeltaZ;
//
//    public HatredfireBoltEntity(EntityType<? extends HatredfireBoltEntity> pEntityType, Level pLevel) {
//        super(pEntityType, pLevel);
////        this.noPhysics = true;
//    }
//
//
//    public HatredfireBoltEntity(Level level, LivingEntity pShooter, Entity pFinalTarget, Direction.Axis pAxis) {
//        super(ImmortalersDelightEntities.HATREDFIRE_BOLT.get(), level);
//    }
//
//    public HatredfireBoltEntity(Level level, LivingEntity shooter, @Nullable Entity target, int spreadDuration) {
//        this(ImmortalersDelightEntities.HATREDFIRE_BOLT.get(), level);
//        this.setOwner(shooter);
//        this.target = target;
//        this.spreadDuration = spreadDuration;
//
//        // 设置起始位置在发射者胸前偏上
//        this.moveTo(shooter.getX(), shooter.getEyeY() - 0.2D, shooter.getZ(), shooter.getYRot(), shooter.getXRot());
//
//        // 初始随机挑一个发散方向
//        this.selectSpreadDirection(Direction.Axis.Y);
//    }
//
//    public HatredfireBoltEntity(Level level, LivingEntity shooter, @Nullable Entity target) {
//        this(level, shooter, target, 20); // 默认 1 秒
//    }
//
//    public void setSpreadDuration(int ticks) {
//        this.spreadDuration = ticks;
//    }
//
//    @Override
//    protected void defineSynchedData() {
//
//    }
//
//    @Override
//    public void tick() {
//        // 目标反序列化恢复
//        if (!this.level().isClientSide && this.target == null && this.targetUUID != null && this.level() instanceof ServerLevel serverLevel) {
//            this.target = serverLevel.getEntity(this.targetUUID);
//            if (this.target == null) {
//                this.targetUUID = null;
//            }
//        }
//
//        // ========================================================
//        // 阶段一：扩散避障期（0 ~ spreadDuration）
//        // 完全复用潜影贝平滑阻尼更新，但方向改为自由避障与发散
//        // ========================================================
//        if (this.tickCount < this.spreadDuration) {
//            if (!this.level().isClientSide) {
//                // 潜影贝独有的渐进式逼近，彻底杜绝位置瞬移
//                this.customTargetDeltaX = Mth.clamp(this.customTargetDeltaX * 1.025D, -1.0D, 1.0D);
//                this.customTargetDeltaY = Mth.clamp(this.customTargetDeltaY * 1.025D, -1.0D, 1.0D);
//                this.customTargetDeltaZ = Mth.clamp(this.customTargetDeltaZ * 1.025D, -1.0D, 1.0D);
//
//                Vec3 curMotion = this.getDeltaMovement();
//                this.setDeltaMovement(curMotion.add(
//                        (this.customTargetDeltaX - curMotion.x) * 0.2D,
//                        (this.customTargetDeltaY - curMotion.y) * 0.2D,
//                        (this.customTargetDeltaZ - curMotion.z) * 0.2D
//                ));
//
//                // 潜影贝换向步长与避障
//                if (this.customFlightSteps > 0) {
//                    --this.customFlightSteps;
//                    if (this.customFlightSteps == 0) {
//                        this.selectSpreadDirection(this.customMoveDirection == null ? null : this.customMoveDirection.getAxis());
//                    }
//                }
//
//                if (this.customMoveDirection != null) {
//                    BlockPos nextPos = this.blockPosition().relative(this.customMoveDirection);
//                    // 遇到实心方块立刻规避转向
//                    if (this.level().loadedAndEntityCanStandOn(nextPos, this) || !this.level().isEmptyBlock(nextPos)) {
//                        this.selectSpreadDirection(this.customMoveDirection.getAxis());
//                    }
//                }
//
//                // 扩散期间的碰撞检测（防止直接贴脸引爆遗漏）
//                HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
//                if (hitresult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
//                    this.onHit(hitresult);
//                }
//            }
//
//            // 推进位移与朝向平滑旋转
//            this.checkInsideBlocks();
//            Vec3 motion = this.getDeltaMovement();
//            this.setPos(this.getX() + motion.x, this.getY() + motion.y, this.getZ() + motion.z);
//            ProjectileUtil.rotateTowardsMovement(this, 0.5F);
//
//            // 扩散期粒子
//            if (this.level().isClientSide) {
//                this.level().addParticle(ParticleTypes.SOUL,
//                        this.getX() - motion.x, this.getY() - motion.y + 0.15D, this.getZ() - motion.z,
//                        0.0D, 0.0D, 0.0D);
//            }
//            return; // 拦截潜影贝默认的锁敌寻路，阻止 super.tick()
//        }
//
//        // ========================================================
//        // 阶段二：冲刺点爆发（在扩散结束的第一刻执行）
//        // ========================================================
//        if (!this.level().isClientSide && !this.launched) {
//            this.launched = true;
//            this.launchTowardsTargetOrLastMotion();
//        }
//
//        // ========================================================
//        // 阶段三：直线高速冲击期
//        // ========================================================
//        if (!this.level().isClientSide) {
//            // 超时 5 秒自毁
//            if (this.tickCount > this.spreadDuration + 100) {
//                this.discard();
//                return;
//            }
//
//            // 高速直线推进下的精准碰撞检测
//            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
//            if (hitresult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
//                this.onHit(hitresult);
//                return;
//            }
//        }
//
//        this.checkInsideBlocks();
//        Vec3 motion = this.getDeltaMovement();
//        this.setPos(this.getX() + motion.x, this.getY() + motion.y, this.getZ() + motion.z);
//        ProjectileUtil.rotateTowardsMovement(this, 1.0F);
//
//        // 冲刺期炫酷拖尾
//        if (this.level().isClientSide) {
//            this.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME,
//                    this.getX() - motion.x * 0.25D,
//                    this.getY() - motion.y * 0.25D,
//                    this.getZ() - motion.z * 0.25D,
//                    0, 0, 0);
//        }
//    }
//
//    /**
//     * 扩散阶段专属选向逻辑（带智能防撞避障）
//     */
//    private void selectSpreadDirection(@Nullable Direction.Axis pAxis) {
//        BlockPos currentPos = this.blockPosition();
//        List<Direction> openDirections = Lists.newArrayList();
//
//        // 找出所有未被实体方块封堵的方向
//        for (Direction dir : Direction.values()) {
//            if (pAxis != null && dir.getAxis() == pAxis) {
//                continue; // 不在原轴上来回往复，更显灵活
//            }
//            BlockPos targetPos = currentPos.relative(dir);
//            if (this.level().isEmptyBlock(targetPos) && !this.level().loadedAndEntityCanStandOn(targetPos, this)) {
//                openDirections.add(dir);
//            }
//        }
//
//        Direction chosenDir = !openDirections.isEmpty()
//                ? openDirections.get(this.random.nextInt(openDirections.size()))
//                : Direction.getRandom(this.random);
//
//        this.customMoveDirection = chosenDir;
//
//        // 设定扩散巡航速度 (0.18 格/刻)
//        double spreadSpeed = 0.18D;
//        this.customTargetDeltaX = (double) chosenDir.getStepX() * spreadSpeed;
//        this.customTargetDeltaY = (double) chosenDir.getStepY() * spreadSpeed;
//        this.customTargetDeltaZ = (double) chosenDir.getStepZ() * spreadSpeed;
//
//        // 不发强制纠偏包，由潜影贝自身插值完成！
//        this.customFlightSteps = 10 + this.random.nextInt(10);
//    }
//
//    /**
//     * 锁敌直射冲刺；若无目标，按扩散末端惯性速度暴射
//     */
//    private void launchTowardsTargetOrLastMotion() {
//        Vec3 launchDir;
//
//        if (this.target != null && this.target.isAlive()) {
//            Vec3 targetCenter = this.target.position().add(0, this.target.getBbHeight() * 0.5D, 0);
//            launchDir = targetCenter.subtract(this.position()).normalize();
//        } else {
//            // 没有目标：继承最后这一时刻的运动矢量！
//            Vec3 currentMovement = this.getDeltaMovement();
//            if (currentMovement.lengthSqr() > 1.0E-4D) {
//                launchDir = currentMovement.normalize();
//            } else if (this.customMoveDirection != null) {
//                launchDir = new Vec3(this.customMoveDirection.getStepX(), this.customMoveDirection.getStepY(), this.customMoveDirection.getStepZ()).normalize();
//            } else {
//                launchDir = this.getLookAngle().normalize();
//            }
//        }
//
//        // 高初速度冲刺 (1.6 格/刻)
//        double rushSpeed = 1.6D;
//        this.setDeltaMovement(launchDir.scale(rushSpeed));
//        this.hasImpulse = true; // 冲刺瞬间允许通知客户端同步突变
//
//        // 破空与轰鸣音效
//        this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
//                SoundEvents.SHULKER_BULLET_HIT, SoundSource.HOSTILE, 1.0F, 0.5F);
//        this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
//                SoundEvents.BLAZE_SHOOT, SoundSource.HOSTILE, 0.8F, 1.7F);
//    }
//
//    // ========================================================
//    // 自定义命中与伤害控制（覆盖潜影贝原版的漂浮效果）
//    // ========================================================
//    @Override
//    protected void onHitEntity(@NotNull EntityHitResult pResult) {
//        if (!this.level().isClientSide) {
//            Entity hitEntity = pResult.getEntity();
//            Entity owner = this.getOwner();
//
//            float damage = 8.0F;
//            if (owner instanceof LivingEntity livingOwner && livingOwner.getAttribute(Attributes.ATTACK_DAMAGE) != null) {
//                damage = (float) livingOwner.getAttributeValue(Attributes.ATTACK_DAMAGE) * 1.3F;
//            }
//
//            boolean hurtSuccess = hitEntity.hurt(this.damageSources().explosion(this, owner), damage);
//
//            if (hurtSuccess && hitEntity instanceof LivingEntity livingHit) {
//                int stunDuration = DifficultyModeUtil.isPowerBattleMode() ? 40 : 15;
//                double knockbackResist = livingHit.getAttribute(Attributes.KNOCKBACK_RESISTANCE) != null ?
//                        livingHit.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE) : 0;
//                stunDuration *= (1.0 - knockbackResist);
//                if (stunDuration > 0) {
//                    StunEffect.applyImmortalEffect(livingHit, stunDuration, 0);
//                }
//            }
//
//            this.triggerExplosion();
//        }
//    }
//
//    @Override
//    protected void onHitBlock(@NotNull BlockHitResult pResult) {
//        super.onHitBlock(pResult);
//        if (!this.level().isClientSide) {
//            this.triggerExplosion();
//        }
//    }
//
//    private void triggerExplosion() {
//        this.level().explode(this, this.getX(), this.getY(), this.getZ(), 1.0F, false, Level.ExplosionInteraction.NONE);
//        this.discard();
//    }
//
//    // 重写让其可被玩家攻击反弹/格挡销毁
//    @Override
//    public boolean hurt(@NotNull DamageSource pSource, float pAmount) {
//        if (!this.level().isClientSide) {
//            this.playSound(SoundEvents.SHULKER_BULLET_HURT, 1.0F, 1.0F);
//            this.triggerExplosion();
//        }
//        return true;
//    }
//
//    // ========================================================
//    // NBT 序列化与网络
//    // ========================================================
//    @Override
//    protected void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
//        super.addAdditionalSaveData(pCompound);
//        pCompound.putBoolean("Launched", this.launched);
//        pCompound.putInt("SpreadDuration", this.spreadDuration);
//        pCompound.putInt("CustomSteps", this.customFlightSteps);
//        pCompound.putDouble("CustomTXD", this.customTargetDeltaX);
//        pCompound.putDouble("CustomTYD", this.customTargetDeltaY);
//        pCompound.putDouble("CustomTZD", this.customTargetDeltaZ);
//        if (this.customMoveDirection != null) {
//            pCompound.putInt("CustomDir", this.customMoveDirection.get3DDataValue());
//        }
//        if (this.target != null) {
//            pCompound.putUUID("CustomTarget", this.target.getUUID());
//        }
//    }
//
//    @Override
//    protected void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
//        super.readAdditionalSaveData(pCompound);
//        this.launched = pCompound.getBoolean("Launched");
//        if (pCompound.contains("SpreadDuration")) {
//            this.spreadDuration = pCompound.getInt("SpreadDuration");
//        }
//        this.customFlightSteps = pCompound.getInt("CustomSteps");
//        this.customTargetDeltaX = pCompound.getDouble("CustomTXD");
//        this.customTargetDeltaY = pCompound.getDouble("CustomTYD");
//        this.customTargetDeltaZ = pCompound.getDouble("CustomTZD");
//        if (pCompound.contains("CustomDir", 99)) {
//            this.customMoveDirection = Direction.from3DDataValue(pCompound.getInt("CustomDir"));
//        }
//        if (pCompound.hasUUID("CustomTarget")) {
//            this.targetUUID = pCompound.getUUID("CustomTarget");
//        }
//    }
//
//    @Override
//    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
//        return NetworkHooks.getEntitySpawningPacket(this);
//    }
//}
//
//import com.google.common.collect.Lists;
//import com.renyigesai.immortalers_delight.init.ImmortalersDelightEntities;
//import com.renyigesai.immortalers_delight.potion.immortaleffects.StunEffect;
//import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.core.particles.ParticleTypes;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.network.protocol.Packet;
//import net.minecraft.network.protocol.game.ClientGamePacketListener;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.sounds.SoundEvents;
//import net.minecraft.sounds.SoundSource;
//import net.minecraft.util.Mth;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.ai.attributes.Attributes;
//import net.minecraft.world.entity.projectile.AbstractArrow;
//import net.minecraft.world.entity.projectile.ProjectileUtil;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.phys.BlockHitResult;
//import net.minecraft.world.phys.EntityHitResult;
//import net.minecraft.world.phys.Vec3;
//import net.minecraftforge.network.NetworkHooks;
//
//import javax.annotation.Nullable;
//import java.util.List;
//import java.util.UUID;
//
//public class HatredfireBoltEntity extends AbstractArrow {
//    @Nullable
//    private Entity target;
//    @Nullable
//    private UUID targetUUID;
//
//    // ================= 自定义参数 =================
//    private int spreadDuration = 25; // 可配置的扩散避障时长 (ticks)
//    private boolean launched = false;
//
//    // ================= 潜影贝避障参数 =================
//    @Nullable
//    private Direction currentMoveDirection;
//    private int flightSteps;
//    private double targetDeltaX;
//    private double targetDeltaY;
//    private double targetDeltaZ;
//
//    public HatredfireBoltEntity(EntityType<? extends HatredfireBoltEntity> type, Level level) {
//        super(type, level);
//        this.pickup = Pickup.DISALLOWED;
//        this.setNoGravity(true);
//    }
//
//    /**
//     * 基础构造函数
//     *
//     * @param level          世界
//     * @param shooter        发射者
//     * @param target         追踪目标（可为 null）
//     * @param spreadDuration 扩散避障维持时长（例如 20 ~ 40 ticks）
//     */
//    public HatredfireBoltEntity(Level level, LivingEntity shooter, @Nullable Entity target, int spreadDuration) {
//        this(ImmortalersDelightEntities.HATREDFIRE_BOLT.get(), level);
//        this.setOwner(shooter);
//        this.target = target;
//        this.spreadDuration = spreadDuration;
//        this.setPos(shooter.getX(), shooter.getEyeY() - 0.2D, shooter.getZ());
//
//        // 初始朝向随机选择一个轴进行散射避障
//        this.selectNextSpreadDirection(Direction.Axis.Y);
//    }
//
//    public HatredfireBoltEntity(Level level, LivingEntity shooter, @Nullable Entity target) {
//        this(level, shooter, target, 20); // 默认 1 秒扩散
//    }
//
//    /**
//     * 设置自定义扩散避障持续时长
//     */
//    public void setSpreadDuration(int ticks) {
//        this.spreadDuration = ticks;
//    }
//
//    @Override
//    public void tick() {
//        super.tick();
//
//        // 目标反序列化恢复
//        if (!this.level().isClientSide && this.target == null && this.targetUUID != null && this.level() instanceof ServerLevel serverLevel) {
//            this.target = serverLevel.getEntity(this.targetUUID);
//            if (this.target == null) {
//                this.targetUUID = null;
//            }
//        }
//
//        // ==========================================
//        // 阶段一：类似潜影贝的自主发散与避障移动 (0 ~ spreadDuration)
//        // ==========================================
//        if (this.tickCount < this.spreadDuration) {
//            this.tickSpreadShulkerMovement();
//            return;
//        }
//
//        // ==========================================
//        // 阶段二：冲刺爆发触发点（扩散完毕后仅触发一次）
//        // ==========================================
//        if (!this.level().isClientSide && !this.launched) {
//            this.launched = true;
//            this.launchTowardsTargetOrLastMotion();
//        }
//
//        // ==========================================
//        // 阶段三：直线高速突进阶段
//        // ==========================================
//        if (!this.level().isClientSide) {
//            if (this.tickCount > this.spreadDuration + 100) { // 超时 5 秒未命中自动销毁
//                this.discard();
//            }
//        } else {
//            // 冲刺阶段高亮拖尾
//            Vec3 motion = this.getDeltaMovement();
//            this.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME,
//                    this.getX() - motion.x * 0.2D,
//                    this.getY() - motion.y * 0.2D,
//                    this.getZ() - motion.z * 0.2D,
//                    0, 0, 0);
//        }
//    }
//
//    /**
//     * 潜影贝避障移动逻辑（扩散阶段定制版）
//     */
//    private void tickSpreadShulkerMovement() {
//        if (!this.level().isClientSide) {
//            // 速度平滑加速向 targetDelta 靠近
//            this.targetDeltaX = Mth.clamp(this.targetDeltaX * 1.025D, -0.6D, 0.6D);
//            this.targetDeltaY = Mth.clamp(this.targetDeltaY * 1.025D, -0.6D, 0.6D);
//            this.targetDeltaZ = Mth.clamp(this.targetDeltaZ * 1.025D, -0.6D, 0.6D);
//
//            Vec3 curMotion = this.getDeltaMovement();
//            this.setDeltaMovement(curMotion.add(
//                    (this.targetDeltaX - curMotion.x) * 0.2D,
//                    (this.targetDeltaY - curMotion.y) * 0.2D,
//                    (this.targetDeltaZ - curMotion.z) * 0.2D
//            ));
//
//            // 步长倒计时，步长为 0 则重新换向
//            if (this.flightSteps > 0) {
//                --this.flightSteps;
//                if (this.flightSteps == 0) {
//                    this.selectNextSpreadDirection(this.currentMoveDirection == null ? null : this.currentMoveDirection.getAxis());
//                }
//            }
//
//            // 障碍阻挡判定：如果正前方是实心方块，立即重新换向避障！
//            if (this.currentMoveDirection != null) {
//                BlockPos nextPos = this.blockPosition().relative(this.currentMoveDirection);
//                if (this.level().loadedAndEntityCanStandOn(nextPos, this) || !this.level().isEmptyBlock(nextPos)) {
//                    this.selectNextSpreadDirection(this.currentMoveDirection.getAxis());
//                }
//            }
//        }
//
//        // 扩散阶段粒子与转向
//        ProjectileUtil.rotateTowardsMovement(this, 0.5F);
//        if (this.level().isClientSide) {
//            this.level().addParticle(ParticleTypes.SOUL,
//                    this.getX(), this.getY() + 0.15D, this.getZ(),
//                    0.0D, 0.02D, 0.0D);
//        }
//    }
//
//    /**
//     * 扩散阶段的核心：选取避开墙体并向外发散的方向
//     */
//    private void selectNextSpreadDirection(@Nullable Direction.Axis pAxis) {
//        BlockPos currentPos = this.blockPosition();
//        List<Direction> openDirections = Lists.newArrayList();
//
//        // 收集所有通畅且非实心墙面的方向
//        for (Direction dir : Direction.values()) {
//            if (pAxis != null && dir.getAxis() == pAxis) {
//                continue; // 避免来回 180° 折返，提高流动感
//            }
//            BlockPos targetPos = currentPos.relative(dir);
//            if (this.level().isEmptyBlock(targetPos) && !this.level().loadedAndEntityCanStandOn(targetPos, this)) {
//                openDirections.add(dir);
//            }
//        }
//
//        Direction chosenDir;
//        if (!openDirections.isEmpty()) {
//            // 在空旷方向中随机挑选
//            chosenDir = openDirections.get(this.random.nextInt(openDirections.size()));
//        } else {
//            // 实在无路可退时盲选一个随机方向
//            chosenDir = Direction.getRandom(this.random);
//        }
//
//        this.currentMoveDirection = chosenDir;
//
//        // 设置潜影贝扩散速度（0.22 格/刻，比原版 0.15 略快，散射更飘逸）
//        double spreadSpeed = 0.22D;
//        this.targetDeltaX = (double) chosenDir.getStepX() * spreadSpeed;
//        this.targetDeltaY = (double) chosenDir.getStepY() * spreadSpeed;
//        this.targetDeltaZ = (double) chosenDir.getStepZ() * spreadSpeed;
//
//        this.hasImpulse = true;
//        // 在该方向游走的持续 Tick 数 (8 ~ 14 ticks 换一次朝向)
//        this.flightSteps = 8 + this.random.nextInt(6);
//    }
//
//    /**
//     * 扩散完毕：锁敌冲刺。若无目标，按扩散结束时的末速度（惯性方向）高速直射
//     */
//    private void launchTowardsTargetOrLastMotion() {
//        Vec3 launchDir;
//
//        if (this.target != null && this.target.isAlive()) {
//            // 锁定目标中心
//            Vec3 targetCenter = this.target.position().add(0, this.target.getBbHeight() * 0.5D, 0);
//            launchDir = targetCenter.subtract(this.position()).normalize();
//        } else {
//            // 没有目标：继承最后这一时刻的运动矢量！
//            Vec3 currentMovement = this.getDeltaMovement();
//            if (currentMovement.lengthSqr() > 1.0E-4D) {
//                launchDir = currentMovement.normalize();
//            } else if (this.currentMoveDirection != null) {
//                launchDir = new Vec3(this.currentMoveDirection.getStepX(), this.currentMoveDirection.getStepY(), this.currentMoveDirection.getStepZ()).normalize();
//            } else {
//                launchDir = this.getLookAngle().normalize();
//            }
//        }
//
//        // 赋予冲刺高速度 (1.6 格/刻)
//        double rushSpeed = 1.6D;
//        this.setDeltaMovement(launchDir.scale(rushSpeed));
//        this.hasImpulse = true;
//
//        // 姿态强制对齐新速度
//        ProjectileUtil.rotateTowardsMovement(this, 1.0F);
//
//        // 爆发音效提示
//        this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
//                SoundEvents.SHULKER_BULLET_HIT, SoundSource.HOSTILE, 1.2F, 0.5F);
//        this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
//                SoundEvents.BLAZE_SHOOT, SoundSource.HOSTILE, 0.8F, 1.8F);
//    }
//
//    @Override
//    protected void onHitEntity(EntityHitResult result) {
//        super.onHitEntity(result);
//
//        if (!this.level().isClientSide) {
//            Entity hitEntity = result.getEntity();
//            Entity owner = this.getOwner();
//
//            // 若在扩散阶段就碰到了实体，直接引爆
//            float damage = 8.0F;
//            if (owner instanceof LivingEntity livingOwner && livingOwner.getAttribute(Attributes.ATTACK_DAMAGE) != null) {
//                damage = (float) livingOwner.getAttributeValue(Attributes.ATTACK_DAMAGE) * 1.3F;
//            }
//
//            boolean hurtSuccess = hitEntity.hurt(this.damageSources().explosion(this, owner), damage);
//
//            if (hurtSuccess && hitEntity instanceof LivingEntity livingHit) {
//                int stunDuration = DifficultyModeUtil.isPowerBattleMode() ? 40 : 15;
//                double knockbackResist = livingHit.getAttribute(Attributes.KNOCKBACK_RESISTANCE) != null ?
//                        livingHit.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE) : 0;
//                stunDuration *= (1.0 - knockbackResist);
//                if (stunDuration > 0) {
//                    StunEffect.applyImmortalEffect(livingHit, stunDuration, 0);
//                }
//            }
//
//            this.triggerExplosion();
//        }
//    }
//
//    @Override
//    protected void onHitBlock(BlockHitResult result) {
//        super.onHitBlock(result);
//        // 避障逻辑能防止大部分撞墙。如果冲刺阶段撞墙，或者死胡同无可奈何时撞墙，则触发自毁爆炸
//        if (!this.level().isClientSide) {
//            this.triggerExplosion();
//        }
//    }
//
//    private void triggerExplosion() {
//        this.level().explode(this, this.getX(), this.getY(), this.getZ(), 1.0F, false, Level.ExplosionInteraction.NONE);
//        this.discard();
//    }
//
//    @Override
//    public void addAdditionalSaveData(CompoundTag tag) {
//        super.addAdditionalSaveData(tag);
//        tag.putBoolean("Launched", this.launched);
//        tag.putInt("SpreadDuration", this.spreadDuration);
//        tag.putInt("Steps", this.flightSteps);
//        tag.putDouble("TXD", this.targetDeltaX);
//        tag.putDouble("TYD", this.targetDeltaY);
//        tag.putDouble("TZD", this.targetDeltaZ);
//        if (this.currentMoveDirection != null) {
//            tag.putInt("Dir", this.currentMoveDirection.get3DDataValue());
//        }
//        if (this.target != null) {
//            tag.putUUID("Target", this.target.getUUID());
//        }
//    }
//
//    @Override
//    public void readAdditionalSaveData(CompoundTag tag) {
//        super.readAdditionalSaveData(tag);
//        this.launched = tag.getBoolean("Launched");
//        if (tag.contains("SpreadDuration")) {
//            this.spreadDuration = tag.getInt("SpreadDuration");
//        }
//        this.flightSteps = tag.getInt("Steps");
//        this.targetDeltaX = tag.getDouble("TXD");
//        this.targetDeltaY = tag.getDouble("TYD");
//        this.targetDeltaZ = tag.getDouble("TZD");
//        if (tag.contains("Dir", 99)) {
//            this.currentMoveDirection = Direction.from3DDataValue(tag.getInt("Dir"));
//        }
//        if (tag.hasUUID("Target")) {
//            this.targetUUID = tag.getUUID("Target");
//        }
//    }
//
//    @Override
//    public Packet<ClientGamePacketListener> getAddEntityPacket() {
//        return NetworkHooks.getEntitySpawningPacket(this);
//    }
//
//    @Override
//    protected ItemStack getPickupItem() {
//        return new ItemStack(Items.AIR);
//    }
//}
//
//import com.renyigesai.immortalers_delight.init.ImmortalersDelightEntities;
//import com.renyigesai.immortalers_delight.potion.immortaleffects.StunEffect;
//import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
//import net.minecraft.core.particles.ParticleTypes;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.network.protocol.Packet;
//import net.minecraft.network.protocol.game.ClientGamePacketListener;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.sounds.SoundEvents;
//import net.minecraft.sounds.SoundSource;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.ai.attributes.Attributes;
//import net.minecraft.world.entity.projectile.AbstractArrow;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.phys.BlockHitResult;
//import net.minecraft.world.phys.EntityHitResult;
//import net.minecraft.world.phys.Vec3;
//import net.minecraftforge.network.NetworkHooks;
//
//import javax.annotation.Nullable;
//import java.util.UUID;
//
//public class HatredfireBoltEntity extends AbstractArrow {
//    @Nullable
//    private Entity target;
//    @Nullable
//    private UUID targetUUID;
//
//    // 悬停结束、发动爆发冲刺的 Tick 节点
//    private static final int LAUNCH_TICK = 16;
//    private boolean launched = false;
//
//    public HatredfireBoltEntity(EntityType<? extends HatredfireBoltEntity> type, Level level) {
//        super(type, level);
//        this.pickup = Pickup.DISALLOWED;
//        this.setNoGravity(true); // 始终巡航无重力
//    }
//
//    public HatredfireBoltEntity(Level level, LivingEntity shooter, @org.jetbrains.annotations.Nullable Entity target) {
//        this(ImmortalersDelightEntities.HATREDFIRE_BOLT.get(), level);
//        this.setOwner(shooter);
//        this.target = target;
//        this.setPos(shooter.getX(), shooter.getEyeY() - 0.2D, shooter.getZ());
//
//        // 初始微弱散射：给一个初速度向周围炸开
//        Vec3 forward = shooter.getLookAngle().scale(0.3D);
//        Vec3 randomSpread = new Vec3(
//                (this.random.nextDouble() - 0.5D) * 0.8D,
//                (this.random.nextDouble() - 0.2D) * 0.6D,
//                (this.random.nextDouble() - 0.5D) * 0.8D
//        );
//        this.setDeltaMovement(forward.add(randomSpread));
//    }
//
//
//    @Override
//    public void tick() {
//        // 交由原版处理基础位移和网络姿态推算
//        super.tick();
//
//        // 阶段一：散射与悬停阶段 (0 ~ 15 tick)
//        if (this.tickCount < LAUNCH_TICK) {
//            // 施加强空气阻力，让散射迅速停滞在半空（浮游状态）
//            this.setDeltaMovement(this.getDeltaMovement().scale(0.8D));
//            // 悬停聚能粒子效果（双端渲染）
//            if (this.level().isClientSide) {
//                this.level().addParticle(ParticleTypes.SMOKE,
//                        this.getX(), this.getY(), this.getZ(),
//                        (this.random.nextDouble() - 0.5D) * 0.05D,
//                        (this.random.nextDouble() - 0.5D) * 0.05D,
//                        (this.random.nextDouble() - 0.5D) * 0.05D);
//            }
//            return;
//        }
//
//        // 阶段二：冲刺爆发点（第 16 tick 执行唯一一次定点发射）
//        if (!this.level().isClientSide && !this.launched) {
//            this.launched = true;
//            this.launchTowardsTarget();
//        }
//
//        // 阶段三：飞行阶段超时与粒子
//        if (!this.level().isClientSide) {
//            if (this.tickCount > 120) { // 发射后飞行超时销毁
//                this.discard();
//            }
//        } else {
//            // 冲刺阶段的拖尾火焰
//            Vec3 motion = this.getDeltaMovement();
//            this.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME,
//                    this.getX() - motion.x * 0.25D,
//                    this.getY() - motion.y * 0.25D,
//                    this.getZ() - motion.z * 0.25D,
//                    0, 0, 0);
//        }
//    }
//
//    /**
//     * 仅在第 16 tick 执行一次：瞄准并锁定方向冲刺
//     */
//    private void launchTowardsTarget() {
//        // 目标恢复
//        if (this.target == null && this.targetUUID != null && this.level() instanceof ServerLevel serverLevel) {
//            this.target = serverLevel.getEntity(this.targetUUID);
//        }
//
//        Vec3 launchDir;
//        if (this.target != null && this.target.isAlive()) {
//            Vec3 aimPos = this.target.position().add(0, this.target.getBbHeight() * 0.55D, 0);
//            launchDir = aimPos.subtract(this.position()).normalize();
//        } else {
//            // 目标已死亡或无目标，顺着当前面朝方向射出
//            launchDir = this.getLookAngle().normalize();
//        }
//
//        // 赋予高初速度冲刺爆发（1.6 格/刻）
//        double boltSpeed = 1.6D;
//        this.setDeltaMovement(launchDir.scale(boltSpeed));
//        this.hasImpulse = true; // 强制触发服务端向客户端下发速度同步包
//
//        // 播放破空音效提示玩家规避
//        this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
//                SoundEvents.BLAZE_SHOOT, SoundSource.HOSTILE, 1.0F, 1.6F);
//    }
//
//    @Override
//    protected void onHitEntity(EntityHitResult result) {
//        super.onHitEntity(result);
//
//        if (!this.level().isClientSide) {
//            Entity hitEntity = result.getEntity();
//            Entity owner = this.getOwner();
//
//            float damage = 8.0F;
//            if (owner instanceof LivingEntity livingOwner) {
//                if (livingOwner.getAttribute(Attributes.ATTACK_DAMAGE) != null) {
//                    damage = (float) livingOwner.getAttributeValue(Attributes.ATTACK_DAMAGE) * 1.3F;
//                }
//            }
//
//            boolean hurtSuccess = hitEntity.hurt(this.damageSources().explosion(this, owner), damage);
//
//            if (hurtSuccess && hitEntity instanceof LivingEntity livingHit) {
//                int stunDuration = DifficultyModeUtil.isPowerBattleMode() ? 40 : 15;
//                double knockbackResist = livingHit.getAttribute(Attributes.KNOCKBACK_RESISTANCE) != null ?
//                        livingHit.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE) : 0;
//                stunDuration *= (1.0 - knockbackResist);
//                if (stunDuration > 0) {
//                    StunEffect.applyImmortalEffect(livingHit, stunDuration, 0);
//                }
//            }
//
//            this.triggerExplosion();
//        }
//    }
//
//    @Override
//    protected void onHitBlock(BlockHitResult result) {
//        super.onHitBlock(result);
//        if (!this.level().isClientSide) {
//            this.triggerExplosion();
//        }
//    }
//
//    private void triggerExplosion() {
//        this.level().explode(this, this.getX(), this.getY(), this.getZ(), 1.0F, false, Level.ExplosionInteraction.NONE);
//        this.discard();
//    }
//
//    @Override
//    public void addAdditionalSaveData(CompoundTag tag) {
//        super.addAdditionalSaveData(tag);
//        tag.putBoolean("Launched", this.launched);
//        if (this.target != null) {
//            tag.putUUID("Target", this.target.getUUID());
//        }
//    }
//
//    @Override
//    public void readAdditionalSaveData(CompoundTag tag) {
//        super.readAdditionalSaveData(tag);
//        this.launched = tag.getBoolean("Launched");
//        if (tag.hasUUID("Target")) {
//            this.targetUUID = tag.getUUID("Target");
//        }
//    }
//
//    @Override
//    public Packet<ClientGamePacketListener> getAddEntityPacket() {
//        return NetworkHooks.getEntitySpawningPacket(this);
//    }
//
//    @Override
//    protected ItemStack getPickupItem() {
//        return new ItemStack(Items.AIR);
//    }
//}
//

//package com.renyigesai.immortalers_delight.entities.projectile;
//
//import com.renyigesai.immortalers_delight.init.ImmortalersDelightEntities;
//import com.renyigesai.immortalers_delight.potion.immortaleffects.StunEffect;
//import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
//import net.minecraft.core.particles.ParticleTypes;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.network.protocol.Packet;
//import net.minecraft.network.protocol.game.ClientGamePacketListener;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.util.Mth;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.ai.attributes.Attributes;
//import net.minecraft.world.entity.projectile.AbstractArrow;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.phys.BlockHitResult;
//import net.minecraft.world.phys.EntityHitResult;
//import net.minecraft.world.phys.Vec3;
//import net.minecraftforge.network.NetworkHooks;
//
//import javax.annotation.Nullable;
//import java.util.UUID;
//
//public class HatredfireBoltEntity extends AbstractArrow {
//    @Nullable
//    private Entity target;
//    @Nullable
//    private UUID targetUUID;
//
//    // 阶段计时
//    private static final int DISPERSAL_TICKS = 4;
//    private static final int ENGAGE_TICKS = 12;
//
//    // 【客户端平滑插值变量】：用于消除网络纠偏带来的硬折线与瞬移
//    private double clientLerpX;
//    private double clientLerpY;
//    private double clientLerpZ;
//    private int clientLerpSteps = 0;
//
//    public HatredfireBoltEntity(EntityType<? extends HatredfireBoltEntity> type, Level level) {
//        super(type, level);
//        this.pickup = Pickup.DISALLOWED;
//        this.setNoGravity(true);
//    }
//
//    public HatredfireBoltEntity(Level level, LivingEntity shooter, Entity target) {
//        this(ImmortalersDelightEntities.HATREDFIRE_BOLT.get(), level);
//        this.setOwner(shooter);
//        this.target = target;
//        this.setPos(shooter.getX(), shooter.getEyeY() - 0.2D, shooter.getZ());
//
//        Vec3 forward = shooter.getLookAngle().normalize().scale(0.4D);
//        Vec3 randomSpread = new Vec3(
//                (this.random.nextDouble() - 0.5D),
//                (this.random.nextDouble() - 0.5D),
//                (this.random.nextDouble() - 0.5D)
//        ).normalize().scale(0.6D);
//
//        this.setDeltaMovement(forward.add(randomSpread));
//    }
//
//    /**
//     * 重写网络插值接口：
//     * 原版 AbstractArrow 在收到网络包时是直接 setPos 造成画面瞬间跳变折线。
//     * 这里将其改造为缓冲 3 个 tick 平滑平移，彻底消除折线感。
//     */
//    @Override
//    public void lerpTo(double x, double y, double z, float yaw, float pitch, int steps, boolean flag) {
//        this.clientLerpX = x;
//        this.clientLerpY = y;
//        this.clientLerpZ = z;
//        this.clientLerpSteps = 3; // 分摊到 3 刻内平滑完成位移过渡
//        this.setRot(yaw, pitch);
//    }
//
//    private int animationTick = 0;
//    @Override
//    public void tick() {
//        // 客户端插值平滑处理：
//        if (this.level().isClientSide && this.clientLerpSteps > 0) {
//            double curX = this.getX() + (this.clientLerpX - this.getX()) / (double) this.clientLerpSteps;
//            double curY = this.getY() + (this.clientLerpY - this.getY()) / (double) this.clientLerpSteps;
//            double curZ = this.getZ() + (this.clientLerpZ - this.getZ()) / (double) this.clientLerpSteps;
//            --this.clientLerpSteps;
//            this.setPos(curX, curY, curZ);
//        }
//
//        super.tick();
//
//        if (!this.level().isClientSide) {
//            if (this.target == null && this.targetUUID != null) {
//                this.target = ((ServerLevel) this.level()).getEntity(this.targetUUID);
//                if (this.target == null) {
//                    this.targetUUID = null;
//                }
//            }
//
//            if (this.tickCount > 160 || (this.target != null && !this.target.isAlive())) {
//                this.discard();
//                return;
//            }
//
//            this.updateHomingTrajectory();
//            System.out.println("当前tick数：" + this.tickCount + "服务端当前位置：" +", "+ this.getX() +", "+ this.getY() +", "+ this.getZ() );
//        } else {
//            animationTick++;
//            // 客户端渲染火焰粒子
//            Vec3 motion = this.getDeltaMovement();
//            this.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME,
//                    this.getX() - motion.x * 0.2D,
//                    this.getY() - motion.y * 0.2D,
//                    this.getZ() - motion.z * 0.2D,
//                    0, 0, 0);
//            System.out.println("当前tick数：" + this.animationTick + "客户端当前位置：" +", "+ this.getX() +", "+ this.getY() +", "+ this.getZ() );
//        }
//    }
//
//    /**
//     * 柔和化向心加速度：保留 70% 原始动量，使每刻变化平稳可被网络吸收
//     */
//    private void updateHomingTrajectory() {
//        Vec3 currentMovement = this.getDeltaMovement();
//
//        if (this.tickCount < DISPERSAL_TICKS) {
//            this.setDeltaMovement(currentMovement.scale(0.96D));
//            return;
//        }
//
//        if (this.target == null || !this.target.isAlive()) return;
//
//        Vec3 targetCenter = this.target.position().add(0, this.target.getBbHeight() * 0.6D, 0);
//        Vec3 toTarget = targetCenter.subtract(this.position());
//
//        // 极近距离保护，防止贴身瞬间发生极度偏转
//        if (toTarget.lengthSqr() < 0.25D) {
//            return;
//        }
//
//        Vec3 desiredDirection = toTarget.normalize();
//
//        float progress = (float)(this.tickCount - DISPERSAL_TICKS) / (float)(ENGAGE_TICKS - DISPERSAL_TICKS);
//        float engageFactor = Mth.clamp(progress, 0.0F, 1.0F);
//
//        // 适度调和向心力：最大转向权重降到 0.22 左右
//        double homingStrength = engageFactor * 0.22D;
//
//        // 仿照 Phantom Arrow 优雅的保留原速度 + 叠加偏向冲量方式：
//        Vec3 smoothedMotion = currentMovement.scale(0.75D).add(desiredDirection.scale(homingStrength));
//
//        // 限制最大巡航速度，平滑提速
//        double targetSpeed = Mth.lerp(engageFactor, 0.45D, 1.15D);
//        this.setDeltaMovement(smoothedMotion.normalize().scale(targetSpeed));
//    }
//
//    @Override
//    protected void onHitEntity(EntityHitResult result) {
//        super.onHitEntity(result);
//
//        if (!this.level().isClientSide) {
//            Entity hitEntity = result.getEntity();
//            Entity owner = this.getOwner();
//
//            float damage = 8.0F;
//            if (owner instanceof LivingEntity livingOwner) {
//                if (livingOwner.getAttribute(Attributes.ATTACK_DAMAGE) != null) {
//                    damage = (float) livingOwner.getAttributeValue(Attributes.ATTACK_DAMAGE) * 1.3F;
//                }
//            }
//
//            boolean hurtSuccess = hitEntity.hurt(this.damageSources().explosion(this, owner), damage);
//
//            if (hurtSuccess && hitEntity instanceof LivingEntity livingHit) {
//                int stunDuration = DifficultyModeUtil.isPowerBattleMode() ? 40 : 15;
//                double knockbackResist = livingHit.getAttribute(Attributes.KNOCKBACK_RESISTANCE) != null ?
//                        livingHit.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE) : 0;
//                stunDuration *= (1.0 - knockbackResist);
//                if (stunDuration > 0) {
//                    StunEffect.applyImmortalEffect(livingHit, stunDuration, 0);
//                }
//            }
//
//            this.triggerExplosion();
//        }
//    }
//
//    @Override
//    protected void onHitBlock(BlockHitResult result) {
//        super.onHitBlock(result);
//        if (!this.level().isClientSide) {
//            this.triggerExplosion();
//        }
//    }
//
//    private void triggerExplosion() {
//        this.level().explode(this, this.getX(), this.getY(), this.getZ(), 1.0F, false, Level.ExplosionInteraction.NONE);
//        this.discard();
//    }
//
//    @Override
//    public void addAdditionalSaveData(CompoundTag tag) {
//        super.addAdditionalSaveData(tag);
//        if (this.target != null) {
//            tag.putUUID("Target", this.target.getUUID());
//        }
//    }
//
//    @Override
//    public void readAdditionalSaveData(CompoundTag tag) {
//        super.readAdditionalSaveData(tag);
//        if (tag.hasUUID("Target")) {
//            this.targetUUID = tag.getUUID("Target");
//        }
//    }
//
//    @Override
//    public Packet<ClientGamePacketListener> getAddEntityPacket() {
//        return NetworkHooks.getEntitySpawningPacket(this);
//    }
//
//    @Override
//    protected ItemStack getPickupItem() {
//        return new ItemStack(Items.AIR);
//    }
//}


//package com.renyigesai.immortalers_delight.entities.projectile;
//
//import com.renyigesai.immortalers_delight.init.ImmortalersDelightEntities;
//import com.renyigesai.immortalers_delight.potion.immortaleffects.StunEffect;
//import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
//import net.minecraft.core.particles.ParticleTypes;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.network.protocol.Packet;
//import net.minecraft.network.protocol.game.ClientGamePacketListener;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.util.Mth;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.ai.attributes.Attributes;
//import net.minecraft.world.entity.projectile.AbstractArrow;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.phys.BlockHitResult;
//import net.minecraft.world.phys.EntityHitResult;
//import net.minecraft.world.phys.HitResult;
//import net.minecraft.world.phys.Vec3;
//import net.minecraftforge.network.NetworkHooks;
//
//import javax.annotation.Nullable;
//import java.util.UUID;
//
//public class HatredfireBoltEntity extends AbstractArrow {
//    @Nullable
//    private Entity target;
//    @Nullable
//    private UUID targetUUID;
//
//    // 阶段计时常量
//    private static final int DISPERSAL_TICKS = 4; // 阶段1：微弱散射与蓄势期
//    private static final int ENGAGE_TICKS = 12;    // 阶段2：逐步达到峰值索敌转向力的过渡刻
//
//    public HatredfireBoltEntity(EntityType<? extends HatredfireBoltEntity> type, Level level) {
//        super(type, level);
//        this.pickup = Pickup.DISALLOWED;
//        // 关键点1：启用无重力，让飞弹呈动力巡航状态，但不碰 noPhysics（保留碰撞与双端自主位移推算）
//        this.setNoGravity(true);
//    }
//
//    /**
//     * 发射构造函数（常规指定坐标与初速度）
//     */
//    public HatredfireBoltEntity(double x, double y, double z, double vx, double vy, double vz, Level level) {
//        this(ImmortalersDelightEntities.HATREDFIRE_BOLT.get(), level);
//        this.setPos(x, y, z);
//        this.setDeltaMovement(vx, vy, vz);
//    }
//
//    /**
//     * 智能寻敌与散射发射构造函数
//     */
//    public HatredfireBoltEntity(Level level, LivingEntity shooter, @org.jetbrains.annotations.Nullable Entity target) {
//        this(ImmortalersDelightEntities.HATREDFIRE_BOLT.get(), level);
//        this.setOwner(shooter);
//        this.target = target;
//        this.setPos(shooter.getX(), shooter.getEyeY() - 0.2D, shooter.getZ());
//
//        // 1. 基准向前速度
//        Vec3 forward = shooter.getLookAngle().normalize().scale(0.45D);
//
//        // 2. 环状锥面随机散射向量 (Cone Spreading)
//        Vec3 randomSpread = new Vec3(
//                (this.random.nextDouble() - 0.5D),
//                (this.random.nextDouble() - 0.5D),
//                (this.random.nextDouble() - 0.5D)
//        ).normalize().scale(0.65D);
//
//        this.setDeltaMovement(forward.add(randomSpread));
//        // 注意：不要在构造函数内部调用 level.addFreshEntity(this)，应由外部生成方负责添加！
//    }
//    private int animationTick = 0;
//    @Override
//    public void tick() {
//        // 调用父类的 tick()，底层会自动完成：
//        // 双端位移推算、平滑旋转角度更新、撞击检测、水下逻辑等
//        super.tick();
//
//        if (!this.level().isClientSide) {
//            // 【服务端阶段性索敌与变轨逻辑】
//            // 1. 存盘重载实体恢复
//            if (this.target == null && this.targetUUID != null) {
//                this.target = ((ServerLevel) this.level()).getEntity(this.targetUUID);
//                if (this.target == null) {
//                    this.targetUUID = null;
//                }
//            }
//
//            // 2. 超时销毁或目标丢失注销
//            if (this.tickCount > 160 || (this.target != null && !this.target.isAlive())) {
//                this.discard();
//                return;
//            }
//
//            // 3. 执行平滑曲线变轨数学算法
//            this.updateHomingTrajectory();
//
//            System.out.println("当前tick数：" + this.tickCount + "服务端当前位置：" +", "+ this.getX() +", "+ this.getY() +", "+ this.getZ() );
//        } else {
//            System.out.println("当前tick数：" + this.animationTick + "客户端当前位置：" +", "+ this.getX() +", "+ this.getY() +", "+ this.getZ() );
//            // 【客户端表现层】：依靠自身平滑推算的位置生成粒子
//            Vec3 motion = this.getDeltaMovement();
//            animationTick++;
//            this.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME,
//                    this.getX() - motion.x * 0.2D,
//                    this.getY() - motion.y * 0.2D,
//                    this.getZ() - motion.z * 0.2D,
//                    0, 0, 0);
//        }
//    }
//
//    /**
//     * 核心平滑追踪数学算法
//     * 避免暴力覆写向量，采用“动量守恒 + 动态渐进力”模型
//     */
//    private void updateHomingTrajectory() {
//        Vec3 currentMovement = this.getDeltaMovement();
//
//        // 阶段 1：散射阶段，轻微空气阻力，任由其散开
//        if (this.tickCount < DISPERSAL_TICKS) {
//            this.setDeltaMovement(currentMovement.scale(0.95D));
//            return;
//        }
//
//        if (this.target == null || !this.target.isAlive()) return;
//
//        // 阶段 2 & 3：计算目标胸部方向
//        Vec3 targetCenter = this.target.position().add(0, this.target.getBbHeight() * 0.6D, 0);
//        Vec3 desiredDirection = targetCenter.subtract(this.position()).normalize();
//
//        // 计算当前索敌强度的渐进因子 (0.0 -> 1.0)
//        float progress = (float)(this.tickCount - DISPERSAL_TICKS) / (float)(ENGAGE_TICKS - DISPERSAL_TICKS);
//        float engageFactor = Mth.clamp(progress, 0.0F, 1.0F);
//
//        // 转向系数与速度动态插值
//        double homingStrength = engageFactor * 0.42D;
//        double currentSpeed = currentMovement.length();
//        double targetSpeed = Mth.lerp(engageFactor, 0.5D, 1.35D); // 散开后开始爆发加速
//
//        // 【关键平滑模型】：保留原有方向冲量，叠加微小的偏向冲量后重新归一化
//        Vec3 newDirection = currentMovement.normalize().add(desiredDirection.scale(homingStrength)).normalize();
//
//        // 缓动提升速度
//        double finalSpeed = Mth.lerp(0.2D, currentSpeed, targetSpeed);
//        this.setDeltaMovement(newDirection.scale(finalSpeed));
//    }
//
//    @Override
//    protected void onHitEntity(EntityHitResult result) {
//        super.onHitEntity(result);
//
//        if (!this.level().isClientSide) {
//            Entity hitEntity = result.getEntity();
//            Entity owner = this.getOwner();
//
//            float damage = 8.0F;
//            if (owner instanceof LivingEntity livingOwner) {
//                if (livingOwner.getAttribute(Attributes.ATTACK_DAMAGE) != null) {
//                    damage = (float) livingOwner.getAttributeValue(Attributes.ATTACK_DAMAGE) * 1.3F;
//                }
//            }
//
//            boolean hurtSuccess = hitEntity.hurt(this.damageSources().explosion(this, owner), damage);
//
//            if (hurtSuccess && hitEntity instanceof LivingEntity livingHit) {
//                // 眩晕效果附加
//                int stunDuration = DifficultyModeUtil.isPowerBattleMode() ? 40 : 15;
//                double knockbackResist = livingHit.getAttribute(Attributes.KNOCKBACK_RESISTANCE) != null ?
//                        livingHit.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE) : 0;
//                stunDuration *= (1.0 - knockbackResist);
//                if (stunDuration > 0) {
//                    StunEffect.applyImmortalEffect(livingHit, stunDuration, 0);
//                }
//            }
//
//            this.triggerExplosion();
//        }
//    }
//
//    @Override
//    protected void onHitBlock(BlockHitResult result) {
//        super.onHitBlock(result);
//        if (!this.level().isClientSide) {
//            this.triggerExplosion();
//        }
//    }
//
//    private void triggerExplosion() {
//        this.level().explode(this, this.getX(), this.getY(), this.getZ(), 1.0F, false, Level.ExplosionInteraction.NONE);
//        this.discard();
//    }
//
//    @Override
//    public void addAdditionalSaveData(CompoundTag tag) {
//        super.addAdditionalSaveData(tag);
//        if (this.target != null) {
//            tag.putUUID("Target", this.target.getUUID());
//        }
//    }
//
//    @Override
//    public void readAdditionalSaveData(CompoundTag tag) {
//        super.readAdditionalSaveData(tag);
//        if (tag.hasUUID("Target")) {
//            this.targetUUID = tag.getUUID("Target");
//        }
//    }
//
//    @Override
//    public Packet<ClientGamePacketListener> getAddEntityPacket() {
//        // 使用 Forge 标准发包通道，确保出生时的朝向与速度精准同步
//        return NetworkHooks.getEntitySpawningPacket(this);
//    }
//
//    @Override
//    protected ItemStack getPickupItem() {
//        return new ItemStack(Items.AIR);
//    }
//}


//package com.renyigesai.immortalers_delight.entities.projectile;
//
//import com.renyigesai.immortalers_delight.init.ImmortalersDelightEntities;
//import com.renyigesai.immortalers_delight.potion.immortaleffects.StunEffect;
//import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.particles.ParticleOptions;
//import net.minecraft.core.particles.ParticleTypes;
//import net.minecraft.network.syncher.EntityDataAccessor;
//import net.minecraft.network.syncher.EntityDataSerializers;
//import net.minecraft.network.syncher.SynchedEntityData;
//import net.minecraft.world.damagesource.DamageSource;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.ai.attributes.Attributes;
//import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
//import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
//import net.minecraft.world.level.BlockGetter;
//import net.minecraft.world.level.Explosion;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.material.FluidState;
//import net.minecraft.world.phys.EntityHitResult;
//import net.minecraft.world.phys.HitResult;
//import org.jetbrains.annotations.NotNull;
//
//public class HatredfireBoltEntity extends SwarmMissileEntity {
//    public double xPower;
//    public double yPower;
//    public double zPower;
////    // 同步实体数据访问器：标记该凋灵头颅是否为"危险级"（来自无敌状态的凋灵BOSS光环）
////    // 使用SynchedEntityData实现服务端与客户端的数据同步，序列化类型为布尔值
////    private static final EntityDataAccessor<Boolean> DATA_DANGEROUS = SynchedEntityData.defineId(SoulFireballEntity.class, EntityDataSerializers.BOOLEAN);
////
//    public HatredfireBoltEntity(EntityType<? extends SwarmMissileEntity> pEntityType, Level pLevel) {
//        super(pEntityType, pLevel);
//    }
//
//    public HatredfireBoltEntity(double pX, double pY, double pZ, double pOffsetX, double pOffsetY, double pOffsetZ, Level pLevel) {
//        super(ImmortalersDelightEntities.HATREDFIRE_BOLT.get(), pLevel);
//        this.moveTo(pX, pY, pZ, this.getYRot(), this.getXRot());
//        this.reapplyPosition();
//        double d0 = Math.sqrt(pOffsetX * pOffsetX + pOffsetY * pOffsetY + pOffsetZ * pOffsetZ);
//        if (d0 != 0.0D) {
//            this.xPower = pOffsetX / d0 * 0.1D;
//            this.yPower = pOffsetY / d0 * 0.1D;
//            this.zPower = pOffsetZ / d0 * 0.1D;
//        }
//
//    }
//
//    public HatredfireBoltEntity(LivingEntity pShooter, double pOffsetX, double pOffsetY, double pOffsetZ, Level pLevel) {
//        this(pShooter.getX(), pShooter.getY(), pShooter.getZ(), pOffsetX, pOffsetY, pOffsetZ, pLevel);
//        this.setOwner(pShooter);
//        this.setRot(pShooter.getYRot(), pShooter.getXRot());
//    }
//
//    public HatredfireBoltEntity(Level level, LivingEntity shooter, Entity target) {
//        super(ImmortalersDelightEntities.HATREDFIRE_BOLT.get(),level,shooter,target);
//    }
//
//
//    @Override
//    protected @NotNull ParticleOptions getTrailParticle() {return
//            ParticleTypes.SOUL_FIRE_FLAME;
//    }
//
//    /**
//     * 获取该投射物的运动惯性系数（原版重写方法）
//     * 该系数会与原始运动速度相乘，控制投射物的运动衰减/增强
//     * @return 惯性系数，此处返回0.96F（轻微的运动衰减，保持头颅飞行的稳定性）
//     */
//    protected float getInertia() {
//        return 0.96F;
//    }
//
//    /**
//     * 判断该实体是否处于着火状态（原版重写方法）
//     * 用于客户端渲染着火特效，凋灵头颅本身不会着火，故返回false
//     * @return 是否着火，固定返回false
//     */
//    public boolean isOnFire() {
//        return false;
//    }
//
//    /**
//     * 获取方块对该凋灵头颅爆炸的抗性（原版重写方法）
//     * 用于计算爆炸对方块的破坏程度，区分危险级头颅和普通头颅的破坏能力
//     * @param pExplosion 爆炸实例
//     * @param pLevel 方块所在的世界视图
//     * @param pPos 方块的坐标
//     * @param pBlockState 方块的状态
//     * @param pFluidState 方块所在位置的流体状态
//     * @param pExplosionPower 原始爆炸威力
//     * @return 修正后的爆炸抗性（影响方块是否被破坏）
//     */
//    public float getBlockExplosionResistance(Explosion pExplosion, BlockGetter pLevel, BlockPos pPos, BlockState pBlockState, FluidState pFluidState, float pExplosionPower) {
//        return this.isDangerous() && pBlockState.canEntityDestroy(pLevel, pPos, this) ? pExplosionPower : 50.0f;
//    }
//
//    /**
//     * 当投射物命中实体时的处理逻辑（原版重写方法）
//     * 负责处理对命中实体的伤害、凋零效果附加，以及对发射者的回血反馈
//     * @param pResult 实体命中结果（包含被命中的实体等信息）
//     */
//    protected void onHitEntity(EntityHitResult pResult) {
//        // 调用父类的实体命中处理逻辑（基础的投射物命中逻辑）
//        super.onHitEntity(pResult);
//
//        // 仅在服务端执行逻辑（避免客户端与服务端数据不一致）
//        if (!this.level().isClientSide) {
//            Entity hitEntity = pResult.getEntity(); // 获取被命中的实体
//            Entity ownerEntity = this.getOwner(); // 获取投射物的发射者（凋灵BOSS）
//            boolean isHurtSuccess; // 标记是否成功对命中实体造成伤害
//
//            // 分支1：发射者是活体实体
//            if (ownerEntity instanceof LivingEntity livingOwner) {
//                // 对被命中实体造成攻击力130%的伤害，伤害值至少为8.0F
//                float damage = livingOwner.getAttribute(Attributes.ATTACK_DAMAGE) != null ? (float) livingOwner.getAttributeValue(Attributes.ATTACK_DAMAGE) : 5.0f;
//                damage = Math.max(damage, 5.0f) * 1.3f;
//                if (isDangerous()) damage *= 1.7F;
//                isHurtSuccess = hitEntity.hurt(this.damageSources().explosion(this, livingOwner), damage);
//
//                // 若伤害成功触发
//                if (isHurtSuccess) {
//                    // 若被命中实体仍存活，执行附魔相关的附加伤害效果（如火焰、锋利等）
//                    if (hitEntity.isAlive()) {
//                        this.doEnchantDamageEffects(livingOwner, hitEntity);
//                    }
//                }
//            }
//            // 分支2：发射者不是活体实体（特殊场景），造成5.0F的魔法伤害
//            else {
//                isHurtSuccess = hitEntity.hurt(this.damageSources().magic(), 8.0F);
//            }
//
//            // 若伤害成功，且被命中实体是活体实体，附加眩晕效果
//            if (isHurtSuccess && hitEntity instanceof LivingEntity livingHitEntity) {
//                int duration = DifficultyModeUtil.isPowerBattleMode() ? 40 : 10;
//                double knockbackResistance = livingHitEntity.getAttribute(Attributes.KNOCKBACK_RESISTANCE) != null ? livingHitEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE) : 0;
//                duration *= 1 - knockbackResistance;
//                if (duration > 0) {
//                    StunEffect.applyImmortalEffect(livingHitEntity, duration, 0);
//                }
//            }
//        }
//    }
//
//    /**
//     * 当投射物命中方块或实体时的通用处理逻辑（原版重写方法）
//     * 凋灵头颅命中后会触发爆炸，随后销毁自身
//     * @param pResult 命中结果（包含命中类型、坐标等信息）
//     */
//    protected void onHit(HitResult pResult) {
//        // 调用父类的通用命中处理逻辑
//        super.onHit(pResult);
//
//        // 仅在服务端执行爆炸逻辑（客户端仅做渲染反馈）
//        if (!this.level().isClientSide) {
//            // 在头颅当前位置触发爆炸，爆炸威力1.0F，不破坏方块（根据isDangerous修正），爆炸交互类型为生物
//            this.level().explode(this, this.getX(), this.getY(), this.getZ(), 1.0F, false, Level.ExplosionInteraction.MOB);
//            // 销毁该凋灵头颅实体，从世界中移除
//            this.discard();
//        }
//    }
//
//    /**
//     * 判断其他实体是否应被阻止穿过该实体（原版重写方法）
//     * 凋灵头颅为投射物，允许其他实体穿过，故返回false
//     * @return 是否阻止其他实体穿过，固定返回false
//     */
//    public boolean isPickable() {
//        return false;
//    }
//
//    /**
//     * 当该实体受到攻击时的处理逻辑（原版重写方法）
//     * 凋灵头颅无法被攻击伤害，故返回false，忽略所有攻击
//     * @param pSource 伤害来源
//     * @param pAmount 伤害值
//     * @return 是否受到伤害，固定返回false
//     */
//    public boolean hurt(DamageSource pSource, float pAmount) {
//        return false;
//    }
//
//    /**
//     * 定义该实体的同步数据（原版重写方法，实体初始化时调用）
//     * 用于注册需要在服务端和客户端之间同步的实体数据，此处注册危险级标记
//     */
//    protected void defineSynchedData() {
//        // 初始化同步数据，默认该凋灵头颅为非危险级（false）
//        this.entityData.define(DATA_DANGEROUS, false);
//    }
//
//    /**
//     * 判断该凋灵头颅是否为危险级（来自无敌状态的凋灵BOSS光环）
//     * 危险级头颅具有更强的方块破坏能力
//     * @return 是否为危险级凋灵头颅
//     */
//    public boolean isDangerous() {
//        // 从同步实体数据中获取危险级标记
//        return this.entityData.get(DATA_DANGEROUS);
//    }
//
//    /**
//     * 设置该凋灵头颅是否为危险级
//     * 通常由凋灵BOSS在发射头颅时根据自身状态设置
//     * @param pInvulnerable 是否为危险级（对应凋灵BOSS的无敌状态）
//     */
//    public void setDangerous(boolean pInvulnerable) {
//        // 向同步实体数据中写入危险级标记，自动同步到客户端
//        this.entityData.set(DATA_DANGEROUS, pInvulnerable);
//    }
//
//    /**
//     * 判断该投射物是否应该在飞行中被火焰点燃（原版重写方法）
//     * 凋灵头颅不会被火焰点燃，故返回false
//     * @return 是否应该被点燃，固定返回false
//     */
//    protected boolean shouldBurn() {
//        return false;
//    }
//}
