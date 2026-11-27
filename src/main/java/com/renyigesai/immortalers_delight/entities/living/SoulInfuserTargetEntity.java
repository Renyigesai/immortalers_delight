package com.renyigesai.immortalers_delight.entities.living;

import java.util.EnumSet;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.*;

//这个实体作为激光的目标地实体
public class SoulInfuserTargetEntity extends Monster implements TraceableEntity {
    //这个代码的大部分是原样照搬了恼鬼的代码，此外引入了末影水晶控制激光的代码(会注明)
    public static final float FLAP_DEGREES_PER_TICK = 45.836624F;
    public static final int TICKS_PER_FLAP = Mth.ceil(3.9269907F);
    protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(SoulInfuserTargetEntity.class, EntityDataSerializers.BYTE);
    private static final int FLAG_IS_CHARGING = 1;
    private static final double RIDING_OFFSET = 0.4D;
    @Nullable
    Mob owner;
    @Nullable
    private BlockPos boundOrigin;
    private boolean hasLimitedLife;
    private int limitedLifeTicks;
    //引入的末影水晶的代码//
    private static final EntityDataAccessor<Optional<BlockPos>> DATA_BEAM_TARGET = SynchedEntityData.defineId(SoulInfuserTargetEntity.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
    private static final EntityDataAccessor<Boolean> DATA_SHOW_BOTTOM = SynchedEntityData.defineId(SoulInfuserTargetEntity.class, EntityDataSerializers.BOOLEAN);
    //=====分隔线=====//

    public SoulInfuserTargetEntity(EntityType<? extends SoulInfuserTargetEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new SoulInfuserTargetEntity.SoulInfuserTargetEntityMoveControl(this);
        this.xpReward = 3;
    }

    public boolean shouldRender(double pX, double pY, double pZ) {
        return true;
    }
    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pDimensions) {
        return pDimensions.height - 0.28125F;
    }

    public boolean isFlapping() {
        return this.tickCount % TICKS_PER_FLAP == 0;
    }

    public void move(MoverType pType, Vec3 pPos) {
        super.move(pType, pPos);
        this.checkInsideBlocks();
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick() {
        this.noPhysics = true;
        super.tick();
        this.noPhysics = false;
        this.setNoGravity(true);
        if (this.hasLimitedLife && --this.limitedLifeTicks <= 0) {
            this.limitedLifeTicks = 20;
            this.hurt(this.damageSources().starve(), 1.0F);
        }
        if (this.getBeamTarget() == null) {
            this.setBeamTarget(this.getOnPos());
        }
        if (this.level().isClientSide() && this.isCharging() && this.tickCount % 3 == 0) {
            spawnBurstParticles(this.level(), ImmortalersDelightParticleTypes.HATRED_FLAME.get(), this.position().add(0,this.getEyeHeight(),0), 3, 0.3, 0.1);
        }
    }


    /**
     * 生成"向外扩散"的粒子团：以某点为中心，向随机方向发散。
     */
    private void spawnBurstParticles(Level level, ParticleOptions particle,
                                     Vec3 origin, int count, double spread, double speed) {
        RandomSource random = level.random;
        for (int i = 0; i < count; i++) {
            double offsetX = random.nextGaussian() * spread;
            double offsetY = random.nextGaussian() * spread;
            double offsetZ = random.nextGaussian() * spread;

            double velX = random.nextGaussian() * speed;
            double velY = random.nextGaussian() * speed;
            double velZ = random.nextGaussian() * speed;

            level.addParticle(particle,
                    origin.x + offsetX, origin.y + offsetY, origin.z + offsetZ,
                    velX, velY, velZ);
        }
    }
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(4, new SoulInfuserTargetEntity.SoulInfuserTargetEntityChargeAttackGoal());
        this.goalSelector.addGoal(8, new SoulInfuserTargetEntity.SoulInfuserTargetEntityRandomMoveGoal());
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Raider.class)).setAlertOthers());
        this.targetSelector.addGoal(2, new SoulInfuserTargetEntity.SoulInfuserTargetEntityCopyOwnerTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 999.0D).add(Attributes.ATTACK_DAMAGE, 4.0D);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS_ID, (byte)0);
        //末影水晶的代码
        this.getEntityData().define(DATA_BEAM_TARGET, Optional.empty());
        this.getEntityData().define(DATA_SHOW_BOTTOM, true);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("BoundX")) {
            this.boundOrigin = new BlockPos(pCompound.getInt("BoundX"), pCompound.getInt("BoundY"), pCompound.getInt("BoundZ"));
        }

        if (pCompound.contains("LifeTicks")) {
            this.setLimitedLife(pCompound.getInt("LifeTicks"));
        }
        //末影水晶的代码
        if (pCompound.contains("BeamTarget", 10)) {
            this.setBeamTarget(NbtUtils.readBlockPos(pCompound.getCompound("BeamTarget")));
        }

        if (pCompound.contains("ShowBottom", 1)) {
            this.setShowBottom(pCompound.getBoolean("ShowBottom"));
        }

    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        if (this.boundOrigin != null) {
            pCompound.putInt("BoundX", this.boundOrigin.getX());
            pCompound.putInt("BoundY", this.boundOrigin.getY());
            pCompound.putInt("BoundZ", this.boundOrigin.getZ());
        }

        if (this.hasLimitedLife) {
            pCompound.putInt("LifeTicks", this.limitedLifeTicks);
        }
        //末影水晶的代码
        if (this.getBeamTarget() != null) {
            pCompound.put("BeamTarget", NbtUtils.writeBlockPos(this.getBeamTarget()));
        }

        pCompound.putBoolean("ShowBottom", this.showsBottom());
    }

    /**
     * Returns null or the entityliving it was ignited by
     */
    @Nullable
    public Mob getOwner() {
        return this.owner;
    }

    @Nullable
    public BlockPos getBoundOrigin() {
        return this.boundOrigin;
    }

    public void setBoundOrigin(@Nullable BlockPos pBoundOrigin) {
        this.boundOrigin = pBoundOrigin;
    }

    private boolean getSoulInfuserTargetEntityFlag(int pMask) {
        int i = this.entityData.get(DATA_FLAGS_ID);
        return (i & pMask) != 0;
    }

    private void setSoulInfuserTargetEntityFlag(int pMask, boolean pValue) {
        int i = this.entityData.get(DATA_FLAGS_ID);
        if (pValue) {
            i |= pMask;
        } else {
            i &= ~pMask;
        }

        this.entityData.set(DATA_FLAGS_ID, (byte)(i & 255));
    }

    public boolean isCharging() {
        return this.getSoulInfuserTargetEntityFlag(1);
    }

    public void setIsCharging(boolean pCharging) {
        this.setSoulInfuserTargetEntityFlag(1, pCharging);
    }

    public void setOwner(Mob pOwner) {
        this.owner = pOwner;
    }

    public void setLimitedLife(int pLimitedLifeTicks) {
        this.hasLimitedLife = true;
        this.limitedLifeTicks = pLimitedLifeTicks;
    }


    //引入的末影水晶的代码//

    public void setBeamTarget(@Nullable BlockPos pBeamTarget) {
        this.getEntityData().set(DATA_BEAM_TARGET, Optional.ofNullable(pBeamTarget));
    }

    @Nullable
    public BlockPos getBeamTarget() {
        return this.getEntityData().get(DATA_BEAM_TARGET).orElse((BlockPos)null);
    }

    public void setShowBottom(boolean pShowBottom) {
        this.getEntityData().set(DATA_SHOW_BOTTOM, pShowBottom);
    }

    public boolean showsBottom() {
        return this.getEntityData().get(DATA_SHOW_BOTTOM);
    }

    /**
     * Checks if the entity is in range to render.
     */
    public boolean shouldRenderAtSqrDistance(double pDistance) {
        return super.shouldRenderAtSqrDistance(pDistance) || this.getBeamTarget() != null;
    }
    //=====分隔线=====//

    protected SoundEvent getAmbientSound() {
        return SoundEvents.VEX_AMBIENT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.VEX_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.VEX_HURT;
    }

    public float getLightLevelDependentMagicValue() {
        return 1.0F;
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        RandomSource randomsource = pLevel.getRandom();
        this.populateDefaultEquipmentSlots(randomsource, pDifficulty);
        this.populateDefaultEquipmentEnchantments(randomsource, pDifficulty);
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    protected void populateDefaultEquipmentSlots(RandomSource pRandom, DifficultyInstance pDifficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
        this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
    }

    /**
     * Returns the Y Offset of this entity.
     */
    public double getMyRidingOffset() {
        return 0.4D;
    }

    class SoulInfuserTargetEntityChargeAttackGoal extends Goal {
        public SoulInfuserTargetEntityChargeAttackGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            LivingEntity livingentity = SoulInfuserTargetEntity.this.getTarget();
            if (livingentity != null && livingentity.isAlive() && !SoulInfuserTargetEntity.this.getMoveControl().hasWanted() && SoulInfuserTargetEntity.this.random.nextInt(reducedTickDelay(7)) == 0) {
                return SoulInfuserTargetEntity.this.distanceToSqr(livingentity) > 4.0D;
            } else {
                return false;
            }
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse() {
            return SoulInfuserTargetEntity.this.getMoveControl().hasWanted() && SoulInfuserTargetEntity.this.isCharging() && SoulInfuserTargetEntity.this.getTarget() != null && SoulInfuserTargetEntity.this.getTarget().isAlive();
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            LivingEntity livingentity = SoulInfuserTargetEntity.this.getTarget();
            if (livingentity != null) {
                Vec3 vec3 = livingentity.getEyePosition();
                SoulInfuserTargetEntity.this.moveControl.setWantedPosition(vec3.x, vec3.y, vec3.z, 1.0D);
            }

            SoulInfuserTargetEntity.this.setIsCharging(true);
            SoulInfuserTargetEntity.this.playSound(SoundEvents.VEX_CHARGE, 1.0F, 1.0F);
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void stop() {
            SoulInfuserTargetEntity.this.setIsCharging(false);
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            LivingEntity livingentity = SoulInfuserTargetEntity.this.getTarget();
            if (livingentity != null) {
                if (SoulInfuserTargetEntity.this.getBoundingBox().intersects(livingentity.getBoundingBox())) {
                    SoulInfuserTargetEntity.this.doHurtTarget(livingentity);
                    SoulInfuserTargetEntity.this.setIsCharging(false);
                } else {
                    double d0 = SoulInfuserTargetEntity.this.distanceToSqr(livingentity);
                    if (d0 < 9.0D) {
                        Vec3 vec3 = livingentity.getEyePosition();
                        SoulInfuserTargetEntity.this.moveControl.setWantedPosition(vec3.x, vec3.y, vec3.z, 1.0D);
                    }
                }

            }
        }
    }

    class SoulInfuserTargetEntityCopyOwnerTargetGoal extends TargetGoal {
        private final TargetingConditions copyOwnerTargeting = TargetingConditions.forNonCombat().ignoreLineOfSight().ignoreInvisibilityTesting();

        public SoulInfuserTargetEntityCopyOwnerTargetGoal(PathfinderMob pMob) {
            super(pMob, false);
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            return SoulInfuserTargetEntity.this.owner != null && SoulInfuserTargetEntity.this.owner.getTarget() != null && this.canAttack(SoulInfuserTargetEntity.this.owner.getTarget(), this.copyOwnerTargeting);
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            SoulInfuserTargetEntity.this.setTarget(SoulInfuserTargetEntity.this.owner.getTarget());
            super.start();
        }
    }

    class SoulInfuserTargetEntityMoveControl extends MoveControl {
        public SoulInfuserTargetEntityMoveControl(SoulInfuserTargetEntity pSoulInfuserTargetEntity) {
            super(pSoulInfuserTargetEntity);
        }

        public void tick() {
            if (this.operation == MoveControl.Operation.MOVE_TO) {
                Vec3 vec3 = new Vec3(this.wantedX - SoulInfuserTargetEntity.this.getX(), this.wantedY - SoulInfuserTargetEntity.this.getY(), this.wantedZ - SoulInfuserTargetEntity.this.getZ());
                double d0 = vec3.length();
                if (d0 < SoulInfuserTargetEntity.this.getBoundingBox().getSize()) {
                    this.operation = MoveControl.Operation.WAIT;
                    SoulInfuserTargetEntity.this.setDeltaMovement(SoulInfuserTargetEntity.this.getDeltaMovement().scale(0.5D));
                } else {
                    SoulInfuserTargetEntity.this.setDeltaMovement(SoulInfuserTargetEntity.this.getDeltaMovement().add(vec3.scale(this.speedModifier * 0.05D / d0)));
                    if (SoulInfuserTargetEntity.this.getTarget() == null) {
                        Vec3 vec31 = SoulInfuserTargetEntity.this.getDeltaMovement();
                        SoulInfuserTargetEntity.this.setYRot(-((float)Mth.atan2(vec31.x, vec31.z)) * (180F / (float)Math.PI));
                        SoulInfuserTargetEntity.this.yBodyRot = SoulInfuserTargetEntity.this.getYRot();
                    } else {
                        double d2 = SoulInfuserTargetEntity.this.getTarget().getX() - SoulInfuserTargetEntity.this.getX();
                        double d1 = SoulInfuserTargetEntity.this.getTarget().getZ() - SoulInfuserTargetEntity.this.getZ();
                        SoulInfuserTargetEntity.this.setYRot(-((float)Mth.atan2(d2, d1)) * (180F / (float)Math.PI));
                        SoulInfuserTargetEntity.this.yBodyRot = SoulInfuserTargetEntity.this.getYRot();
                    }
                }

            }
        }
    }

    class SoulInfuserTargetEntityRandomMoveGoal extends Goal {
        public SoulInfuserTargetEntityRandomMoveGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            return !SoulInfuserTargetEntity.this.getMoveControl().hasWanted() && SoulInfuserTargetEntity.this.random.nextInt(reducedTickDelay(7)) == 0;
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse() {
            return false;
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            BlockPos blockpos = SoulInfuserTargetEntity.this.getBoundOrigin();
            if (blockpos == null) {
                blockpos = SoulInfuserTargetEntity.this.blockPosition();
            }

            for(int i = 0; i < 3; ++i) {
                BlockPos blockpos1 = blockpos.offset(SoulInfuserTargetEntity.this.random.nextInt(15) - 7, SoulInfuserTargetEntity.this.random.nextInt(11) - 5, SoulInfuserTargetEntity.this.random.nextInt(15) - 7);
                if (SoulInfuserTargetEntity.this.level().isEmptyBlock(blockpos1)) {
                    SoulInfuserTargetEntity.this.moveControl.setWantedPosition((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 0.25D);
                    if (SoulInfuserTargetEntity.this.getTarget() == null) {
                        SoulInfuserTargetEntity.this.getLookControl().setLookAt((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
                    }
                    break;
                }
            }

        }
    }
}
