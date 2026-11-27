package com.renyigesai.immortalers_delight.entities.projectile;

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
public class BaseHitboxEntity extends Entity implements TraceableEntity {
    private static final EntityDataAccessor<Float> DATA_RADIUS = SynchedEntityData.defineId(MoonArrowHitboxEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> DATA_WAITING = SynchedEntityData.defineId(MoonArrowHitboxEntity.class, EntityDataSerializers.BOOLEAN);
    float damage = 6.0F;//伤害值，注意set方法需要双端调用才能在客户端生效，但一般不应在客户端调用这个
    protected int warmupDelayTicks = 12;//攻击开始前的等待时间，注意set方法需要双端调用才能在客户端生效
    protected int lifeTicks = 25;//攻击开始后的生命周期，或者说攻击动画的长度，这个值仅在服务端生效
    private boolean sentSpikeEvent;//是否正在开始攻击，这个值仅在客户端生效
    protected int animationTicks = 0;//动画已经进行的时间，仅在客户端生效
    @javax.annotation.Nullable
    private LivingEntity owner;
    @javax.annotation.Nullable
    private UUID ownerUUID;
    private float dropChance = 0F;
    public BaseHitboxEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
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

    public void setOwner(@javax.annotation.Nullable LivingEntity pOwner) {
        this.owner = pOwner;
        this.ownerUUID = pOwner == null ? null : pOwner.getUUID();
    }

    public void setOwner(@javax.annotation.Nullable UUID ownerID) {
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
                doOnSpikeEvent();
            }
            //非等待状态，开始动画计时
            if (!flag) {
                ++this.animationTicks;
            }
            clientTick();
        } else {
            // 检查是否超过生命周期（等待时间 + 持续时间），超过则移除实体
            if (this.tickCount >= this.warmupDelayTicks + this.lifeTicks) {
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
                attackTick();
            }
        }
    }

    //工具方法，在等待状态变化的瞬间触发，仅客户端
    protected void doOnSpikeEvent() {

        this.sentSpikeEvent = false;
    }

    //工具方法，在等待状态变化后每tick触发，仅客户端
    protected void clientTick() {

    }

    //工具方法，在等待结束后每tick触发，仅服务端
    protected void attackTick() {

    }

    protected void dealDamageTo(LivingEntity pTarget, LivingEntity owner) {
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
