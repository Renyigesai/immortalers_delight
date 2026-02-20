package com.renyigesai.immortalers_delight.entities.projectile;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightEntities;
import com.renyigesai.immortalers_delight.potion.immortaleffects.KuuvahkiEffect;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.UUID;

//这个类相当于是结合了区域效果云和唤魔者尖牙写出来的地刺类实体
public class MoonArrowHitboxEntity extends Entity implements TraceableEntity {
     private static final EntityDataAccessor<Float> DATA_RADIUS = SynchedEntityData.defineId(MoonArrowHitboxEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> DATA_WAITING = SynchedEntityData.defineId(MoonArrowHitboxEntity.class, EntityDataSerializers.BOOLEAN);
    float damage = 6.0F;//伤害值，注意set方法需要双端调用才能在客户端生效，但一般不应在客户端调用这个
    private int warmupDelayTicks = 12;//攻击开始前的等待时间，注意set方法需要双端调用才能在客户端生效
    private boolean sentSpikeEvent;//是否正在开始攻击，这个值仅在客户端生效
    private int animationTicks = 0;//动画已经进行的时间，仅在客户端生效
    private static final int ATTACK_DURATION = 25; // 等待结束后的最大存活时间（游戏刻）
    public static final int LIFE_OFFSET = 2;
    public static final int ATTACK_TRIGGER_TICKS = 10; // 等待状态结束后，到造成伤害前的前摇时间（游戏刻）
    @Nullable
    private LivingEntity owner;
    @Nullable
    private UUID ownerUUID;
    private float dropChance = 0F;

    public MoonArrowHitboxEntity(EntityType<? extends MoonArrowHitboxEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public MoonArrowHitboxEntity(Level pLevel, double pX, double pY, double pZ, float pYRot, int pWarmupDelay, LivingEntity pOwner) {
        this(ImmortalersDelightEntities.MOON_ARROW_HITBOX.get(), pLevel);
        this.warmupDelayTicks = pWarmupDelay;
        this.setOwner(pOwner);
        this.setYRot(pYRot * (180F / (float)Math.PI));
        this.setPos(pX, pY, pZ);
    }

    public MoonArrowHitboxEntity(Level pLevel, double pX, double pY, double pZ, float pYRot, int pWarmupDelay, float damage, LivingEntity pOwner) {
        this(ImmortalersDelightEntities.MOON_ARROW_HITBOX.get(), pLevel);
        this.warmupDelayTicks = pWarmupDelay;
        this.setOwner(pOwner);
        this.damage = damage;
        this.setYRot(pYRot * (180F / (float)Math.PI));
        this.setPos(pX, pY, pZ);
    }

    protected void defineSynchedData() {
        this.entityData.define(DATA_RADIUS, 3.0F);
        this.entityData.define(DATA_WAITING, true);
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    /**
     * 获取当前半径（从同步数据中读取）
     * @return 当前半径值
     */
    public float getRadius() {
        return this.getEntityData().get(DATA_RADIUS);
    }

    /**
     * 设置效果云的半径（服务器端）
     * @param pRadius 目标半径（会被限制在0.0-8.0之间）
     */
    public void setRadius(float pRadius) {
        if (!this.level().isClientSide) { // 仅在服务器端执行（保证数据一致性）
            this.getEntityData().set(DATA_RADIUS, Mth.clamp(pRadius, 0.0F, 8.0F));
        }
    }

    /**
     * 刷新实体碰撞箱尺寸（当半径变化时调用）
     * 先保存当前位置，刷新后重新设置位置（防止因尺寸变化导致位置偏移）
     */
    public void refreshDimensions() {
        double d0 = this.getX();
        double d1 = this.getY();
        double d2 = this.getZ();
        super.refreshDimensions();
        this.setPos(d0, d1, d2);
    }
    public void setWaiting(boolean waiting) {entityData.set(DATA_WAITING, waiting);}
    public boolean isWaiting() {return this.getEntityData().get(DATA_WAITING);}

    public void setOwner(@Nullable LivingEntity pOwner) {
        this.owner = pOwner;
        this.ownerUUID = pOwner == null ? null : pOwner.getUUID();
    }

    public void setOwner(@Nullable UUID ownerID) {
        this.ownerUUID = ownerID;
    }

    /**
     * Returns null or the entityliving it was ignited by
     */
    @Nullable
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUUID != null && this.level() instanceof ServerLevel) {
            Entity entity = ((ServerLevel)this.level()).getEntity(this.ownerUUID);
            if (entity instanceof LivingEntity) {
                this.owner = (LivingEntity)entity;
            }
        }

        return this.owner;
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        this.warmupDelayTicks = pCompound.getInt("Warmup");
        this.setDamage(pCompound.getFloat("Damage"));
        this.setRadius(pCompound.getFloat("Radius"));
        if (pCompound.hasUUID("Owner")) {
            this.ownerUUID = pCompound.getUUID("Owner");
        }
    }

    protected void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putInt("Warmup", this.warmupDelayTicks);
        pCompound.putFloat("Damage", this.damage);
        pCompound.putFloat("Radius", this.getRadius());
        if (this.ownerUUID != null) {
            pCompound.putUUID("Owner", this.ownerUUID);
        }
    }
    /**
     * 当同步数据更新时调用（如半径变化时刷新碰撞箱）
     * @param pKey 更新的数据键
     */
    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        if (DATA_RADIUS.equals(pKey)) {
            this.refreshDimensions(); // 半径变化时刷新尺寸
        }
        super.onSyncedDataUpdated(pKey);
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick() {
        super.tick();
        boolean flag = this.isWaiting(); // 当前是否处于等待状态

        //客户端逻辑，主要用于生成粒子
        if (this.level().isClientSide) {
            if (this.sentSpikeEvent) {
                //一次性释放前摇粒子
                for (int j = 0; j < 32; ++j) {
                    ParticleOptions type = (j % 4 == 0) ? ParticleTypes.WITCH : ParticleTypes.PORTAL;
                    double d0 = j >= 20 ? (this.random.nextDouble() * 2.0D - 1.0D) * 0.3D : 0;
                    double d1 = j >= 20 ? 0.3D + this.random.nextDouble() * 0.3D : 0;
                    double d2 = j >= 20 ? (this.random.nextDouble() * 2.0D - 1.0D) * 0.3D : 0;
                    this.level().addParticle(type, this.xo, this.getRandomY() + this.random.nextDouble() * 2.0D, this.zo, d0, d1, d2);
                }
                this.sentSpikeEvent = false;
            }
            //非等待状态，开始动画计时
            if (!flag) {
                ++this.animationTicks;
            }
            //根据动画进度释放粒子
            if (this.animationTicks == ATTACK_TRIGGER_TICKS) {
                for(int i = 0; i < 32; ++i) {
                    ParticleOptions type = (i >= 12) ? ParticleTypes.END_ROD : ParticleTypes.CRIT;
                    double d0 = this.getX() + (this.random.nextDouble() * 2.0D - 1.0D) * (double)this.getBbWidth() * 0.5D;
                    double d1 = this.getY() + 0.05D + this.random.nextDouble();
                    double d2 = this.getZ() + (this.random.nextDouble() * 2.0D - 1.0D) * (double)this.getBbWidth() * 0.5D;
                    double d3 = i >= 12 ? 0D : (this.random.nextDouble() * 2.0D - 1.0D) * 0.3D;
                    double d4 = i >= 12 ? 0D : 0.3D + this.random.nextDouble() * 0.3D;
                    double d5 = i >= 12 ? 0D : (this.random.nextDouble() * 2.0D - 1.0D) * 0.3D;
                    this.level().addParticle(type, d0, d1 + 1.0D, d2, d3, d4, d5);
                }
            }
        } else {
            // 检查是否超过生命周期（等待时间 + 持续时间），超过则移除实体
            if (this.tickCount >= this.warmupDelayTicks + ATTACK_DURATION) {
                this.discard();
                return;
            }

            // 更新等待状态（是否处于等待时间内）
            boolean flag1 = this.tickCount < this.warmupDelayTicks;

            //如果等待状态变化了，那么改变等待状态并执行开始时的效果
            if (flag != flag1) {
                this.level().broadcastEntityEvent(this, (byte)4);
                this.setWaiting(flag1);
            }

            if (!this.isWaiting()) {
                if (this.tickCount == this.warmupDelayTicks + ATTACK_TRIGGER_TICKS) {
                    AABB aabb = this.getBoundingBox().inflate(0.3D);
                    AABB selection = new AABB(aabb.minX, this.getY() - 0.6D, aabb.minZ, aabb.maxX, this.getY() + 3.5D, aabb.maxZ);
                    for(LivingEntity livingentity : this.level().getEntitiesOfClass(LivingEntity.class, selection)) {
                        LivingEntity caster = this.getOwner(); // 获取效果所有者
                        // 只对未记录或已超过重应用延迟的生物施加效果
                        if (livingentity.isAlive() && livingentity != caster) {
                            if (caster == null || (!caster.isAlliedTo(livingentity) && !livingentity.isAlliedTo(caster))) {

                                // 计算生物与效果云中心的水平距离平方（优化：避免开方）
                                double d8 = livingentity.getX() - this.getX();
                                double d1 = livingentity.getZ() - this.getZ();
                                double d3 = d8 * d8 + d1 * d1;

                                // 距离小于等于半径平方（在范围内）
                                double range = getRadius();
                                if (d3 <= (double)(range * range)) {
                                    //对生物造成伤害或施加其他效果
                                    this.dealDamageTo(livingentity, caster);
                                }
                            }
                        }
                    }
                    //System.out.println("看看伤害:" + getDamage());
                    //System.out.println("现在在造成伤害，当前lifeTicks：" + animationTicks + ",这里是客户端吗？" + this.level().isClientSide);
                }
            }
        }
    }

    private void dealDamageTo(LivingEntity pTarget, LivingEntity owner) {
        float damage = getDamage();
        if (damage <= 0) damage = 6;
        if (pTarget.isAlive() && !pTarget.isInvulnerable()) {
            DamageSource source;
            if (pTarget.getHealth() <= damage) {
                source = KuuvahkiEffect.getDamageSource(pTarget, owner);
            } else source = this.damageSources().freeze();
            pTarget.hurt(source, damage);
            pTarget.invulnerableTime = 0;
        }
    }

    /**
     * Handles an entity event received from a {@link net.minecraft.network.protocol.game.ClientboundEntityEventPacket}.
     */
    public void handleEntityEvent(byte pId) {
        super.handleEntityEvent(pId);
        if (pId == 4) {
            this.sentSpikeEvent = true;
            if (!this.isSilent()) {
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.IRON_GOLEM_ATTACK, this.getSoundSource(), 1.0F, this.random.nextFloat() * 0.2F + 0.85F, false);
            }
        }

    }

    /**
     * 获取活塞推动反应（效果云不受活塞影响）
     * @return 推动反应类型（IGNORE表示忽略）
     */
    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    /**
     * 获取实体的碰撞箱尺寸（根据当前半径动态计算）
     * @param pPose 实体姿势（效果云不区分姿势）
     * @return 实体尺寸（宽度为直径，高度固定）
     */
    public EntityDimensions getDimensions(Pose pPose) {
        return EntityDimensions.scalable(this.getRadius() * 2.0F, this.getRadius() * 0.25F);
    }

    public float getAnimationProgress(float pPartialTicks) {
        if (this.isWaiting()) {return 0.0F;}
        return (float) this.animationTicks + pPartialTicks;
    }
}
