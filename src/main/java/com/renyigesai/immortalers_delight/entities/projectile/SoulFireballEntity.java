package com.renyigesai.immortalers_delight.entities.projectile;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightEntities;
import com.renyigesai.immortalers_delight.potion.immortaleffects.StunEffect;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.UUID;

public class SoulFireballEntity extends AbstractArrow {

    @Nullable
    private Entity target;
    @Nullable
    private UUID targetUUID;

    // 空间坐标目标（支持向方块/空地射击）
    @Nullable
    private Vec3 targetPos;

    // 兜底的发射朝向（支持从方块向指定方向直接发射）
    @Nullable
    private Vec3 fallbackDirection;
    private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(SoulFireballEntity.class, EntityDataSerializers.BOOLEAN);

    private static final int LAUNCH_TICK = 100;
    private boolean launched = false;

    private static final EntityDataAccessor<Boolean> DATA_DANGEROUS =
            SynchedEntityData.defineId(SoulFireballEntity.class, EntityDataSerializers.BOOLEAN);

    public SoulFireballEntity(EntityType<? extends SoulFireballEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    /**
     * 实体发射构造函数（锁定实体）
     */
    public SoulFireballEntity(Level pLevel, LivingEntity shooter, @Nullable Entity target) {
        super(ImmortalersDelightEntities.SOUL_FIREBALL.get(), pLevel);
        this.setOwner(shooter);
        this.target = target;
        this.setPos(shooter.getX(), shooter.getEyeY() - 0.2D, shooter.getZ());
        this.fallbackDirection = shooter.getLookAngle().normalize();
        this.setDangerous(true);

        // 初始微弱散射炸开
        Vec3 forward = shooter.getLookAngle().scale(0.3D);
        Vec3 randomSpread = new Vec3(
                (this.random.nextDouble() - 0.5D) * 0.8D,
                (this.random.nextDouble() - 0.2D) * 0.6D,
                (this.random.nextDouble() - 0.5D) * 0.8D
        );
        this.setDeltaMovement(forward.add(randomSpread));
    }

    /**
     * 适配外部工具方法的坐标构造函数（由方块、技能点触发）
     */
    public SoulFireballEntity(double pX, double pY, double pZ, double pOffsetX, double pOffsetY, double pOffsetZ, Level pLevel) {
        super(ImmortalersDelightEntities.SOUL_FIREBALL.get(), pLevel);
        this.setPos(pX, pY, pZ);

        Vec3 initialMotion = new Vec3(pOffsetX, pOffsetY, pOffsetZ);
        if (initialMotion.lengthSqr() > 1.0E-4) {
            this.fallbackDirection = initialMotion.normalize();
        } else {
            this.fallbackDirection = new Vec3(0, 0, 1); // 极端防零除保护
        }

        // 初始悬停前给予轻微的散布抖动
        Vec3 randomSpread = new Vec3(
                (this.random.nextDouble() - 0.5D),
                (this.random.nextDouble() - 0.2D),
                (this.random.nextDouble() - 0.5D)
        );
        this.setDeltaMovement(initialMotion.add(randomSpread));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_DANGEROUS, false);
        this.entityData.define(ID_FOIL,false);
    }

    public void setDangerous(boolean dangerous) {
        this.entityData.set(DATA_DANGEROUS, dangerous);
    }

    public boolean isDangerous() {
        return this.entityData.get(DATA_DANGEROUS);
    }

    public void setFoil(boolean isFoil) {
        this.entityData.set(ID_FOIL, isFoil);
    }
    public boolean isFoil() {return this.entityData.get(ID_FOIL);}
    public void setTargetPos(@Nullable Vec3 targetPos) {
        this.targetPos = targetPos;
    }

    public void setFallbackDirection(@Nullable Vec3 fallbackDirection) {
        if (fallbackDirection != null && fallbackDirection.lengthSqr() > 1.0E-4) {
            this.fallbackDirection = fallbackDirection.normalize();
        }
    }

    @Override
    public void tick() {
        super.tick();

        // 阶段一：散射与悬停阶段 (0 ~ 9 tick)
        if (this.tickCount < LAUNCH_TICK) {
            // 阻力衰减以浮游在半空；抵消原版箭矢重力导致的过快下坠
            this.setDeltaMovement(this.getDeltaMovement().scale(0.72).add(0, 0.04D, 0));

            if (this.level().isClientSide) {
                this.level().addParticle(ParticleTypes.SMOKE,
                        this.getX(), this.getY(), this.getZ(),
                        (this.random.nextDouble() - 0.5D) * 0.05D,
                        (this.random.nextDouble() - 0.5D) * 0.05D,
                        (this.random.nextDouble() - 0.5D) * 0.05D);
            }
            return;
        }

        // 阶段二：第 10 tick 爆发点射出
        if (!this.level().isClientSide && !this.launched) {
            this.launched = true;
            this.launchTowardsTarget();
            //如果是危险级实体，设为无视重力
            if (isDangerous()) this.setNoGravity(true);
        }

        // 阶段三：飞行超时销毁与拖尾渲染
        if (!this.level().isClientSide) {
            if (this.tickCount > 120) {
                this.discard();
            }
        } else {
            Vec3 motion = this.getDeltaMovement();
            this.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME,
                    this.getX() - motion.x * 0.25D,
                    this.getY() - motion.y * 0.25D,
                    this.getZ() - motion.z * 0.25D,
                    0, 0, 0);
        }
    }

    /**
     * 瞄准核心逻辑：优先级 实体 -> 空间坐标 -> 保底预设方向 -> 朝向
     */
    private void launchTowardsTarget() {
        if (this.target == null && this.targetUUID != null && this.level() instanceof ServerLevel serverLevel) {
            this.target = serverLevel.getEntity(this.targetUUID);
        }

        Vec3 launchDir;

        // 1. 实体追踪优先
        if (this.target != null && this.target.isAlive()) {
            Vec3 aimPos = this.target.position().add(0, this.target.getBbHeight() * 0.55D, 0);
            launchDir = aimPos.subtract(this.position()).normalize();
        }
        // 2. 空间点追踪次之
        else if (this.targetPos != null) {
            Vec3 delta = this.targetPos.subtract(this.position());
            launchDir = delta.lengthSqr() > 1.0E-4 ? delta.normalize() : new Vec3(0, -1, 0);
        }
        // 3. 方向性保底（避免呆滞正南发射）
        else if (this.fallbackDirection != null) {
            launchDir = this.fallbackDirection;
        }
        // 4. 最终兜底
        else {
            launchDir = this.getLookAngle().lengthSqr() > 1.0E-4 ? this.getLookAngle().normalize() : new Vec3(0, 1, 0);
        }

        double boltSpeed = 1.6D;
        this.setDeltaMovement(launchDir.scale(boltSpeed));
        this.hasImpulse = true;

        this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                SoundEvents.BLAZE_SHOOT, SoundSource.HOSTILE, 1.0F, 1.6F);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);

        if (!this.level().isClientSide) {
            Entity hitEntity = result.getEntity();
            Entity owner = this.getOwner();

            float damage = (float) this.getBaseDamage();
            if (damage < 6) damage = 6;

            boolean hurtSuccess = hitEntity.hurt(this.damageSources().indirectMagic(this, owner), damage);

            if (hurtSuccess && hitEntity instanceof LivingEntity livingHit) {
                livingHit.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN));
            }

//            if (this.isDangerous()) this.triggerExplosion();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!this.level().isClientSide) {
            this.triggerExplosion();
        }
    }

    private void triggerExplosion() {
        float explosionPower = DifficultyModeUtil.isPowerBattleMode() ? 2.0F : 1.0F;
        this.level().explode(this, this.getX(), this.getY(), this.getZ(), explosionPower, false, Level.ExplosionInteraction.NONE);
        this.discard();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Launched", this.launched);
        tag.putBoolean("Dangerous", this.isDangerous());
        if (this.target != null) {
            tag.putUUID("Target", this.target.getUUID());
        }
        if (this.targetPos != null) {
            tag.putDouble("TargetX", this.targetPos.x);
            tag.putDouble("TargetY", this.targetPos.y);
            tag.putDouble("TargetZ", this.targetPos.z);
        }
        if (this.fallbackDirection != null) {
            tag.putDouble("FallbackDirX", this.fallbackDirection.x);
            tag.putDouble("FallbackDirY", this.fallbackDirection.y);
            tag.putDouble("FallbackDirZ", this.fallbackDirection.z);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.launched = tag.getBoolean("Launched");
        this.setDangerous(tag.getBoolean("Dangerous"));
        if (tag.hasUUID("Target")) {
            this.targetUUID = tag.getUUID("Target");
        }
        if (tag.contains("TargetX")) {
            this.targetPos = new Vec3(tag.getDouble("TargetX"), tag.getDouble("TargetY"), tag.getDouble("TargetZ"));
        }
        if (tag.contains("FallbackDirX")) {
            this.fallbackDirection = new Vec3(tag.getDouble("FallbackDirX"), tag.getDouble("FallbackDirY"), tag.getDouble("FallbackDirZ"));
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(Items.AIR);
    }


//    public SoulFireballEntity(double pX, double pY, double pZ, double pOffsetX, double pOffsetY, double pOffsetZ, Level pLevel) {
//        super(ImmortalersDelightEntities.SOUL_FIREBALL.get(), pX,pY,pZ,pOffsetX,pOffsetY,pOffsetZ,pLevel);
//    }

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
//    @Override
//    protected @NotNull ParticleOptions getTrailParticle() {return ParticleTypes.GLOW_SQUID_INK;}
//
//    protected float getInertia() {
//        return 0.96F;
//    }
//
//    public boolean isOnFire() {
//        return false;
//    }
//
//    public float getBlockExplosionResistance(Explosion pExplosion, BlockGetter pLevel, BlockPos pPos, BlockState pBlockState, FluidState pFluidState, float pExplosionPower) {
//        return this.isDangerous() && pBlockState.canEntityDestroy(pLevel, pPos, this) ? pExplosionPower : 50.0f;
//    }
//
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
//    public boolean isPickable() {
//        return false;
//    }
//
//    public boolean hurt(DamageSource pSource, float pAmount) {
//        return false;
//    }
//
//    protected void defineSynchedData() {
//        // 初始化同步数据，默认该凋灵头颅为非危险级（false）
//        this.entityData.define(DATA_DANGEROUS, false);
//    }
//
//    public boolean isDangerous() {
//        // 从同步实体数据中获取危险级标记
//        return this.entityData.get(DATA_DANGEROUS);
//    }
//
//    public void setDangerous(boolean pInvulnerable) {
//        // 向同步实体数据中写入危险级标记，自动同步到客户端
//        this.entityData.set(DATA_DANGEROUS, pInvulnerable);
//    }
//    protected boolean shouldBurn() {
//        return false;
//    }
}
