package com.renyigesai.immortalers_delight.entities.projectile;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightEntities;
import com.renyigesai.immortalers_delight.potion.immortaleffects.StunEffect;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.UUID;

public class SurveyorFang extends Entity implements TraceableEntity {
    public static final int ATTACK_DURATION = 20;
    public static final int LIFE_OFFSET = 2;
    public static final int ATTACK_TRIGGER_TICKS = 14;
    private static final EntityDataAccessor<ItemStack> DATA_ITEM = SynchedEntityData.defineId(SurveyorFang.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Float> DAMAGE = SynchedEntityData.defineId(SurveyorFang.class, EntityDataSerializers.FLOAT);

    private int warmupDelayTicks;
    private boolean sentSpikeEvent;
    private int lifeTicks = 24;
    private boolean clientSideAttackStarted;
    private boolean hasSpawnParticle;
    @Nullable
    private LivingEntity owner;
    @Nullable
    private UUID ownerUUID;
    private float dropChance = 0F;

    public SurveyorFang(EntityType<? extends SurveyorFang> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public SurveyorFang(Level pLevel, double pX, double pY, double pZ, float pYRot, int pWarmupDelay,float damage, LivingEntity pOwner) {
        this(ImmortalersDelightEntities.SURVEYOR_FANG.get(), pLevel);
        this.warmupDelayTicks = pWarmupDelay;
        this.setOwner(pOwner);
        this.setDamage(damage);
        this.setYRot(pYRot * (180F / (float)Math.PI));
        this.setPos(pX, pY, pZ);
    }

    public SurveyorFang(Level pLevel, double pX, double pY, double pZ, float pYRot, int pWarmupDelay,float damage,ItemStack itemStack, LivingEntity pOwner) {
        this(ImmortalersDelightEntities.SURVEYOR_FANG.get(), pLevel);
        this.warmupDelayTicks = pWarmupDelay;
        this.setOwner(pOwner);
        this.setDamage(damage);
        this.setItem(itemStack);
        this.setYRot(pYRot * (180F / (float)Math.PI));
        this.setPos(pX, pY, pZ);
    }

    protected void defineSynchedData() {
        this.entityData.define(DAMAGE, 0f);
        this.getEntityData().define(DATA_ITEM, ItemStack.EMPTY);
    }

    public float getDamage() {
        return entityData.get(DAMAGE);
    }

    public void setDamage(float damage) {
        entityData.set(DAMAGE, damage);
    }

    public void setItem(ItemStack pStack) {
        if (!pStack.isEmpty()) {
            pStack = pStack.copyWithCount(1);
        }

        this.getEntityData().set(DATA_ITEM, pStack);

    }

    public ItemStack getItem() {
        return this.getEntityData().get(DATA_ITEM);
    }

    public void setOwner(@Nullable LivingEntity pOwner) {
        this.owner = pOwner;
        this.ownerUUID = pOwner == null ? null : pOwner.getUUID();
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
        CompoundTag compoundtag = pCompound.getCompound("Item");
        if (compoundtag != null && !compoundtag.isEmpty()) {
            ItemStack itemstack = ItemStack.of(compoundtag);
            if (itemstack.isEmpty()) {
                ImmortalersDelightMod.LOGGER.warn("Unable to load item from: {}", (Object)compoundtag);
            }

            this.setItem(itemstack);
            if (pCompound.contains("ItemDropChance", 99)) {
                this.dropChance = pCompound.getFloat("ItemDropChance");
            }
        }
        this.warmupDelayTicks = pCompound.getInt("Warmup");
        if (pCompound.hasUUID("Owner")) {
            this.ownerUUID = pCompound.getUUID("Owner");
        }
        this.setDamage(pCompound.getFloat("damage"));
    }

    protected void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putInt("Warmup", this.warmupDelayTicks);
        if (this.ownerUUID != null) {
            pCompound.putUUID("Owner", this.ownerUUID);
        }
        pCompound.putFloat("damage", this.getDamage());
        pCompound.put("Item", this.getItem().save(new CompoundTag()));
        pCompound.putFloat("ItemDropChance", this.dropChance);
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            if (this.clientSideAttackStarted) {
                if (!this.hasSpawnParticle) {
                    this.hasSpawnParticle = true;
                    for (int j = 0; j < 32; ++j) {
                        ParticleOptions type = (j % 4 == 0) ? ParticleTypes.WITCH : ParticleTypes.PORTAL;
                        double d0 = j >= 20 ? (this.random.nextDouble() * 2.0D - 1.0D) * 0.3D : 0;
                        double d1 = j >= 20 ? 0.3D + this.random.nextDouble() * 0.3D : 0;
                        double d2 = j >= 20 ? (this.random.nextDouble() * 2.0D - 1.0D) * 0.3D : 0;
                        this.level().addParticle(type, this.xo, this.getRandomY() + this.random.nextDouble() * 2.0D, this.zo, d0, d1, d2);
                    }
                }
                --this.lifeTicks;
                if (this.lifeTicks == 12) {
                    for(int i = 0; i < 32; ++i) {
                        ParticleOptions type = (i >= 12) ? ParticleTypes.ENTITY_EFFECT : ParticleTypes.CRIT;
                        double d0 = this.getX() + (this.random.nextDouble() * 2.0D - 1.0D) * (double)this.getBbWidth() * 0.5D;
                        double d1 = this.getY() + 0.05D + this.random.nextDouble();
                        double d2 = this.getZ() + (this.random.nextDouble() * 2.0D - 1.0D) * (double)this.getBbWidth() * 0.5D;
                        double d3 = i >= 12 ? 0.4D : (this.random.nextDouble() * 2.0D - 1.0D) * 0.3D;
                        double d4 = i >= 12 ? 0.3D : 0.3D + this.random.nextDouble() * 0.3D;
                        double d5 = i >= 12 ? 0.35D :(this.random.nextDouble() * 2.0D - 1.0D) * 0.3D;
                        this.level().addParticle(type, d0, d1 + 1.0D, d2, d3, d4, d5);
                    }
                    //System.out.println("看看伤害:" + getDamage());
                    //System.out.println("现在在刷新粒子，当前lifeTicks：" + lifeTicks);
                }
            }
        } else if (--this.warmupDelayTicks < 0) {
            if (this.warmupDelayTicks == -12) {
                AABB aabb = this.getBoundingBox().inflate(0.3D);
                AABB selection = new AABB(aabb.minX, this.getY() - 0.6D, aabb.minZ, aabb.maxX, this.getY() + 3.5D, aabb.maxZ);
                for(LivingEntity livingentity : this.level().getEntitiesOfClass(LivingEntity.class, selection)) {
                    this.dealDamageTo(livingentity);
                }
                //System.out.println("看看伤害:" + getDamage());
                //System.out.println("现在在造成伤害，当前lifeTicks：" + lifeTicks + ",这里是客户端吗？" + this.level().isClientSide);
            }

            if (!this.sentSpikeEvent) {
                this.level().broadcastEntityEvent(this, (byte)4);
                this.sentSpikeEvent = true;
            }

            if (--this.lifeTicks < 0) {
                this.discard();
            }
        }

    }

    private void dealDamageTo(LivingEntity pTarget) {
        LivingEntity livingentity = this.getOwner();
        float damage = getDamage();
        if (damage <= 0) damage = 6;
        if (pTarget.isAlive() && !pTarget.isInvulnerable() && pTarget != livingentity) {
            if (livingentity == null) {
                pTarget.hurt(this.damageSources().cactus(), damage);
                pTarget.addEffect(new MobEffectInstance(MobEffects.WITHER,110,1));
                StunEffect.applyImmortalEffect(pTarget,20,0);
                pTarget.invulnerableTime = 0;
                pTarget.hurt(this.damageSources().magic(),3);
            } else {
                if (livingentity.isAlliedTo(pTarget)) {
                    return;
                }

                pTarget.hurt(this.damageSources().mobProjectile(this, livingentity), getDamage());
                pTarget.addEffect(new MobEffectInstance(MobEffects.WITHER,110,1));
                StunEffect.applyImmortalEffect(pTarget,20,0);
                pTarget.invulnerableTime = 0;
                pTarget.hurt(this.damageSources().indirectMagic(this,livingentity),3);
            }

        }
    }

    /**
     * Handles an entity event received from a {@link net.minecraft.network.protocol.game.ClientboundEntityEventPacket}.
     */
    public void handleEntityEvent(byte pId) {
        super.handleEntityEvent(pId);
        if (pId == 4) {
            this.clientSideAttackStarted = true;
            if (!this.isSilent()) {
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.IRON_GOLEM_ATTACK, this.getSoundSource(), 1.0F, this.random.nextFloat() * 0.2F + 0.85F, false);
            }
        }

    }

    public float getAnimationProgress(float pPartialTicks) {
        if (!this.clientSideAttackStarted) {
            return 0.0F;
        } else {
            int i = this.lifeTicks - 2;
            return i <= 0 ? 1.0F : 1.0F - ((float)i - pPartialTicks) / 20.0F;
        }
    }
}
