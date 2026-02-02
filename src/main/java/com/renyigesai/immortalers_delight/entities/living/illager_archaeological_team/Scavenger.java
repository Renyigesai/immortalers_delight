package com.renyigesai.immortalers_delight.entities.living.illager_archaeological_team;

import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class Scavenger extends SpellcasterIllager implements RangedAttackMob {
    private static final EntityDataAccessor<Boolean> DATA_TELEPORTING = SynchedEntityData.defineId(Scavenger.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Byte> DATA_MOD_SPELL_CASTING_ID = SynchedEntityData.defineId(Scavenger.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> DATA_TAKE_POISON_COUNT = SynchedEntityData.defineId(Scavenger.class, EntityDataSerializers.BYTE);

    @Nullable
    private Sheep wololoTarget;

    public Scavenger(EntityType<? extends Scavenger> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.xpReward = 10;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new Scavenger.ScavengerCastingSpellGoal());
        //this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Player.class, 8.0F, 0.6D, 1.0D));
        this.goalSelector.addGoal(4, new Scavenger.ScavengerRandemTeleportSpellGoal());
        this.goalSelector.addGoal(5, new Scavenger.ScavengerAttackSpellGoal());
        this.goalSelector.addGoal(6, new Scavenger.ScavengerWololoSpellGoal());
        this.goalSelector.addGoal(7, new AttackGoal(this));
        this.goalSelector.addGoal(8, new RandomStrollGoal(this, 0.6D));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Raider.class)).setAlertOthers());
        this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<>(this, Player.class, true)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, (new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, false));
    }

    public static AttributeSupplier.Builder createScavengerAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.24D)
                .add(Attributes.ATTACK_DAMAGE, 7.0D)
                .add(Attributes.FOLLOW_RANGE, 25.0D)
                .add(Attributes.MAX_HEALTH, 24.0D);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_MOD_SPELL_CASTING_ID, (byte)0);
        this.entityData.define(DATA_TELEPORTING, false);
        this.entityData.define(DATA_TAKE_POISON_COUNT, (byte)1);
    }

    public boolean isSpellCasting() {
        return this.entityData.get(DATA_MOD_SPELL_CASTING_ID) > 0;
    }

    public void setIsSpellCasting(Byte pCurrentSpell) {
        this.entityData.set(DATA_MOD_SPELL_CASTING_ID, (byte) pCurrentSpell);
    }

    public Byte getSpellCastingType() {
        return this.entityData.get(DATA_MOD_SPELL_CASTING_ID);
    }

    public boolean isTeleporting() {
        return this.entityData.get(DATA_TELEPORTING);
    }

    public void setTeleporting(Boolean isTeleporting) {
        this.entityData.set(DATA_TELEPORTING, isTeleporting);
    }

    public void setPotionCount(Byte potionCount) {
        this.entityData.set(DATA_TAKE_POISON_COUNT, (byte) potionCount);
    }

    public Byte getPotionCount() {
        return this.entityData.get(DATA_TAKE_POISON_COUNT);
    }
    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
    }

    public SoundEvent getCelebrateSound() {
        return SoundEvents.EVOKER_CELEBRATE;
    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        if (!super.doHurtTarget(pEntity)) {
            return false;
        } else {
            if (pEntity instanceof LivingEntity livingEntity && this.getPotionCount() > 0) {
                pEntity.invulnerableTime = 0;
                float f = DifficultyModeUtil.isPowerBattleMode() ? livingEntity.getMaxHealth() * 0.1f : 2;
                pEntity.hurt(this.damageSources().indirectMagic(this, this),
                        f * (1.2f + this.random.nextFloat() * 0.6f));
                pEntity.invulnerableTime = 0;
                this.setPotionCount((byte) (this.getPotionCount() - 1));
            }

            return true;
        }
    }
    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.getSpellCastingType() == 3) return false;
        return super.hurt(pSource, pAmount);
    }
//    @Override
//    public void setHealth(float pHealth) {
//        if (this.getSpellCastingType() == 3) return;
//        if (pHealth <= this.getMaxHealth() * 0.5F && this.getHealth() >= this.getMaxHealth() * 0.5F) pHealth = this.getMaxHealth() / 2;
//        super.setHealth(pHealth);
//    }
    public void tick() {

        //System.out.println("Scavenger最近收到伤害的时间：" + this.getLastHurtByMobTimestamp());
        //System.out.println("Scavenger最近收到伤害的来源：" + this.getLastHurtByMob());
        super.tick();
        //System.out.println("Scavenger 正在 tick,当前的spellCastingTickCount：" + this.getSpellCastingTime());
    }

    /**
     * Returns whether this Entity is on the same team as the given Entity.
     */
    public boolean isAlliedTo(@NotNull Entity pEntity) {
        if (pEntity == this) {
            return true;
        } else if (super.isAlliedTo(pEntity)) {
            return true;
        } else if (pEntity instanceof Vex vex && vex.getOwner() != null) {
            return this.isAlliedTo(vex.getOwner());
        } else if (pEntity instanceof LivingEntity && ((LivingEntity)pEntity).getMobType() == MobType.ILLAGER) {
            return this.getTeam() == null && pEntity.getTeam() == null;
        } else {
            return false;
        }
    }

    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(ImmortalersDelightItems.REPEATING_CROSSBOW.get()));
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ImmortalersDelightItems.PILLAGER_KNIFE.get()));
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    public AbstractIllager.@NotNull IllagerArmPose getArmPose() {
        if (this.isCastingSpell() && !this.isSpellCasting()) {
            return IllagerArmPose.SPELLCASTING;
        } else if (this.getSpellCastingType() == 1) {
            return IllagerArmPose.CROSSBOW_CHARGE;
        } else if (this.getSpellCastingType() == 2) {
            return IllagerArmPose.BOW_AND_ARROW;
        } else {
            return this.isAggressive() ? AbstractIllager.IllagerArmPose.ATTACKING : IllagerArmPose.NEUTRAL;
        }
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.EVOKER_AMBIENT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.EVOKER_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.EVOKER_HURT;
    }

    void setWololoTarget(@Nullable Sheep pWololoTarget) {
        this.wololoTarget = pWololoTarget;
    }

    @Nullable
    Sheep getWololoTarget() {
        return this.wololoTarget;
    }

    protected SoundEvent getCastingSoundEvent() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    public void applyRaidBuffs(int pWave, boolean pUnusedFalse) {
    }

    @Override
    public void performRangedAttack(LivingEntity pTarget, float pVelocity) {
        ItemStack itemstack = this.getProjectile(this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof net.minecraft.world.item.BowItem)));
        AbstractArrow abstractarrow = ProjectileUtil.getMobArrow(this, itemstack, pVelocity);
        ItemStack bow = new ItemStack(Items.BOW);
        if (this.getMainHandItem().getItem() instanceof net.minecraft.world.item.BowItem) {
            bow = this.getMainHandItem();
        }
        if (bow.getItem() instanceof BowItem bowItem) {
            abstractarrow = bowItem.customArrow(abstractarrow);
        }
        double d0 = pTarget.getX() - this.getX();
        double d1 = pTarget.getY(0.3333333333333333D) - abstractarrow.getY();
        double d2 = pTarget.getZ() - this.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        abstractarrow.shoot(d0, d1 + d3 * (double)0.2F, d2, 1.6F, (float)(14 - this.level().getDifficulty().getId() * 4));
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(abstractarrow);
    }

//    protected boolean teleport() {
//        if (!this.level().isClientSide() && this.isAlive()) {
//            double d0 = this.getX() + (this.random.nextDouble() - 0.5D) * 64.0D;
//            double d1 = this.getY() + (double)(this.random.nextInt(64) - 32);
//            double d2 = this.getZ() + (this.random.nextDouble() - 0.5D) * 64.0D;
//            return this.teleport(d0, d1, d2);
//        } else {
//            return false;
//        }
//    }
//
//    /**
//     * Teleport the entity
//     */
//    private boolean teleport(double pX, double pY, double pZ) {
//        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(pX, pY, pZ);
//
//        while(blockpos$mutableblockpos.getY() > this.level().getMinBuildHeight() && !this.level().getBlockState(blockpos$mutableblockpos).blocksMotion()) {
//            blockpos$mutableblockpos.move(Direction.DOWN);
//        }
//
//        BlockState blockstate = this.level().getBlockState(blockpos$mutableblockpos);
//        boolean flag = blockstate.blocksMotion();
//        boolean flag1 = blockstate.getFluidState().is(FluidTags.WATER);
//        if (flag && !flag1) {
//            net.minecraftforge.event.entity.EntityTeleportEvent.EnderEntity event = net.minecraftforge.event.ForgeEventFactory.onEnderTeleport(this, pX, pY, pZ);
//            if (event.isCanceled()) return false;
//            Vec3 vec3 = this.position();
//            boolean flag2 = this.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
//            if (flag2) {
//                this.level().gameEvent(GameEvent.TELEPORT, vec3, GameEvent.Context.of(this));
//                if (!this.isSilent()) {
//                    this.level().playSound((Player)null, this.xo, this.yo, this.zo, SoundEvents.ENDERMAN_TELEPORT, this.getSoundSource(), 1.0F, 1.0F);
//                    this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
//                }
//            }
//
//            return flag2;
//        } else {
//            return false;
//        }
//    }

//    private boolean teleportRandomly() {
//        if (!this.level().isClientSide() && this.isAlive()) {
//            RandomSource random = this.random;
//            double d0 = this.getX() + (random.nextDouble() - 0.5D) * 64.0D;
//            double d1 = this.getY() + (double) (random.nextInt(64) - 32);
//            double d2 = this.getZ() + (random.nextDouble() - 0.5D) * 64.0D;
//            return this.teleport(d0, d1, d2);
//        }
//        System.out.println("teleportRandomly失败");
//        return false;
//    }
//
//    private boolean teleport(double pX, double pY, double pZ) {
//        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(pX, pY, pZ);
//
//        while (blockpos$mutableblockpos.getY() > this.level().getMinBuildHeight() && !this.level().getBlockState(blockpos$mutableblockpos).blocksMotion()) {
//            blockpos$mutableblockpos.move(Direction.DOWN);
//        }
//
//        BlockState blockstate = this.level().getBlockState(blockpos$mutableblockpos);
//        boolean flag = blockstate.blocksMotion();
//        if (flag) {
//            EntityTeleportEvent.EnderEntity event = ForgeEventFactory.onEnderTeleport(this, pX, pY, pZ);
//
//            if (event.isCanceled()) {
//                System.out.println("传送事件被取消");
//                return false;
//            }
//            Vec3 vec3 = this.position();
//            boolean flag2 = this.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
//            if (flag2) {
//                this.level().gameEvent(GameEvent.TELEPORT, vec3, GameEvent.Context.of(this));
//                if (!this.isSilent()) {
//                    this.level().playSound(null, this.xo, this.yo, this.zo, SoundEvents.ENDERMAN_TELEPORT, this.getSoundSource(), 1.0F, 1.0F);
//                    this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
//                }
//                for (int i = 0; i < 32; ++i) {
//                    this.level().addParticle(ParticleTypes.PORTAL, this.xo, this.getRandomY() + this.random.nextDouble() * 2.0D, this.zo, this.random.nextGaussian(), 0.0D, this.random.nextGaussian());
//                }
//            } else {
//                System.out.println("randomTeleport传送失败");
//            }
//            return flag2;
//        }
//        System.out.println("没有找到安全表面");
//        return false;
//    }

    protected boolean teleportSomewhere() {
        if (!this.isNoAi() && this.isAlive()) {
            BlockPos blockpos = this.blockPosition();

            for(int i = 0; i < 5; ++i) {
                BlockPos blockpos1 = blockpos.offset(Mth.randomBetweenInclusive(this.random, -8, 8), Mth.randomBetweenInclusive(this.random, -8, 8), Mth.randomBetweenInclusive(this.random, -8, 8));
                if (blockpos1.getY() > this.level().getMinBuildHeight()
                        && this.level().isEmptyBlock(blockpos1)
                        && this.level().getWorldBorder().isWithinBounds(blockpos1)
                        && this.level().noCollision(this, (new AABB(blockpos1)).deflate(1.0E-6D))) {
                    Direction direction = this.findAttachableSurface(blockpos1);
                    if (direction != null) {
                        net.minecraftforge.event.entity.EntityTeleportEvent.EnderEntity event = net.minecraftforge.event.ForgeEventFactory.onEnderTeleport(this, blockpos1.getX(), blockpos1.getY(), blockpos1.getZ());
                        if (event.isCanceled()) direction = null;
                        blockpos1 = BlockPos.containing(event.getTargetX(), event.getTargetY(), event.getTargetZ());
                    }

                    if (direction != null) {
                        this.unRide();
                        this.playSound(SoundEvents.SHULKER_TELEPORT, 1.0F, 1.0F);
                        this.setPos((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY(), (double)blockpos1.getZ() + 0.5D);
                        this.level().gameEvent(GameEvent.TELEPORT, blockpos, GameEvent.Context.of(this));
                        this.setTarget((LivingEntity)null);
                        return true;
                    }
                }
            }

            return false;
        } else {
            return false;
        }
    }

    @Nullable
    protected Direction findAttachableSurface(BlockPos pPos) {
        for(Direction direction : Direction.values()) {
            if (this.canStayAt(pPos, direction)) {
                return direction;
            }
        }

        return null;
    }

    boolean canStayAt(BlockPos pPos, Direction pFacing) {
        if (this.isPositionBlocked(pPos)) {
            return false;
        } else {
            Direction direction = pFacing.getOpposite();
            if (!this.level().loadedAndEntityCanStandOnFace(pPos.relative(pFacing), this, direction)) {
                return false;
            } else {
                AABB aabb = getProgressAabb(direction, 1.0F).move(pPos).deflate(1.0E-6D);
                return this.level().noCollision(this, aabb);
            }
        }
    }

    private boolean isPositionBlocked(BlockPos pPos) {
        BlockState blockstate = this.level().getBlockState(pPos);
        if (blockstate.isAir()) {
            return false;
        } else {
            boolean flag = blockstate.is(Blocks.MOVING_PISTON) && pPos.equals(this.blockPosition());
            return !flag;
        }
    }

    public static AABB getProgressAabb(Direction pDirection, float pDelta) {
        return getProgressDeltaAabb(pDirection, -1.0F, pDelta);
    }

    public static AABB getProgressDeltaAabb(Direction pDirection, float pDelta, float pDeltaO) {
        double d0 = (double)Math.max(pDelta, pDeltaO);
        double d1 = (double)Math.min(pDelta, pDeltaO);
        return (new AABB(BlockPos.ZERO)).expandTowards((double)pDirection.getStepX() * d0, (double)pDirection.getStepY() * d0, (double)pDirection.getStepZ() * d0).contract((double)(-pDirection.getStepX()) * (1.0D + d1), (double)(-pDirection.getStepY()) * (1.0D + d1), (double)(-pDirection.getStepZ()) * (1.0D + d1));
    }


    static class AttackGoal extends MeleeAttackGoal {

//        @Override
//        public boolean canUse() {
//            if (this.mob instanceof Scavenger scavenger) {
//                if (scavenger.isCastingSpell()) return false;
//            }
//            return super.canUse();
//        }
//
//        @Override
//        public boolean canContinueToUse() {
//            if (this.mob instanceof Scavenger scavenger) {
//                if (scavenger.isCastingSpell()) return false;
//            }
//            return super.canContinueToUse();
//        }

        public AttackGoal(Scavenger vindicator) {
            super(vindicator, 1.25, false);

        }

        @Override
        protected double getAttackReachSqr(LivingEntity entity) {
            if (this.mob.getVehicle() instanceof Ravager) {
                float f = this.mob.getVehicle().getBbWidth() - 0.1f;
                return f * 2.0f * (f * 2.0f) + entity.getBbWidth();
            }
            return super.getAttackReachSqr(entity);
        }
    }

    class ScavengerAttackSpellGoal extends SpellcasterIllager.SpellcasterUseSpellGoal {
//        public ScavengerAttackSpellGoal() {
//            super();
//        }

        private int attackStep;
        private int attackTime;
        private int defAttackTime;
        protected int getCastingTime() {
            return 80;
        }

        protected int getCastingInterval() {
            return 200;
        }

        protected void performSpellCasting() {
            LivingEntity livingentity = Scavenger.this.getTarget();
            if (livingentity == null || !livingentity.isAlive()) return;
            if (this.attackStep <= 0) return;
            if (isHoldingBow()) {
                this.attackStep -= 1;
                Scavenger.this.performRangedAttack(livingentity, BowItem.getPowerForTime(1));
                this.attackTime = this.defAttackTime;
            } else {
                this.attackStep -= 1;

//                double d0 = Math.min(livingentity.getY(), Scavenger.this.getY());
//                double d1 = Math.max(livingentity.getY(), Scavenger.this.getY()) + 1.0D;
//                float f = (float) Mth.atan2(livingentity.getZ() - Scavenger.this.getZ(), livingentity.getX() - Scavenger.this.getX());
//                if (Scavenger.this.distanceToSqr(livingentity) < 9.0D) {
////                    for(int i = 0; i < 5; ++i) {
////                        float f1 = f + (float)i * (float)Math.PI * 0.4F;
////                        this.createSpellEntity(Scavenger.this.getX() + (double)Mth.cos(f1) * 1.5D, Scavenger.this.getZ() + (double)Mth.sin(f1) * 1.5D, d0, d1, f1, 0);
////                    }
//
//                    for(int k = 0; k < 8; ++k) {
//                        float f2 = f + (float)k * (float)Math.PI * 2.0F / 8.0F + 1.2566371F;
//                        this.createSpellEntity(Scavenger.this.getX() + (double)Mth.cos(f2) * 2.5D, Scavenger.this.getZ() + (double)Mth.sin(f2) * 2.5D, d0, d1, f2, 3);
//                    }
//                } else {
//                    for(int l = 0; l < 8; ++l) {
//                        double d2 = 1.25D * (double)(l + 1);
//                        int j = 2 * l;
//                        this.createSpellEntity(Scavenger.this.getX() + (double)Mth.cos(f) * d2, Scavenger.this.getZ() + (double)Mth.sin(f) * d2, d0, d1, f, j);
//                    }
//                }

                this.attackTime = this.defAttackTime;
            }

        }

        protected boolean isHoldingBow() {
            return  Scavenger.this.isHolding(is -> is.getItem() instanceof CrossbowItem);
        }

//        private void createSpellEntity(double pX, double pZ, double pMinY, double pMaxY, float pYRot, int pWarmupDelay) {
//            BlockPos blockpos = BlockPos.containing(pX, pMaxY, pZ);
//            boolean flag = false;
//            double d0 = 0.0D;
//
//            do {
//                BlockPos blockpos1 = blockpos.below();
//                BlockState blockstate = Scavenger.this.level().getBlockState(blockpos1);
//                if (blockstate.isFaceSturdy(Scavenger.this.level(), blockpos1, Direction.UP)) {
//                    if (!Scavenger.this.level().isEmptyBlock(blockpos)) {
//                        BlockState blockstate1 = Scavenger.this.level().getBlockState(blockpos);
//                        VoxelShape voxelshape = blockstate1.getCollisionShape(Scavenger.this.level(), blockpos);
//                        if (!voxelshape.isEmpty()) {
//                            d0 = voxelshape.max(Direction.Axis.Y);
//                        }
//                    }
//
//                    flag = true;
//                    break;
//                }
//
//                blockpos = blockpos.below();
//            } while(blockpos.getY() >= Mth.floor(pMinY) - 1);
//
//            if (flag) {
//                Scavenger.this.level().addFreshEntity(new EvokerFangs(Scavenger.this.level(), pX, (double)blockpos.getY() + d0, pZ, pYRot, pWarmupDelay, Scavenger.this));
//            }
//
//        }

        @Override
        public void start() {
            if (!(Scavenger.this.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof CrossbowItem)) changeHands();
            Scavenger.this.startUsingItem(InteractionHand.MAIN_HAND);
            Scavenger.this.setIsSpellCasting((byte)1);
            int fraction = 3;
            if (Scavenger.this.getCurrentRaid() != null) {
                fraction = 1;
            }
            if (Scavenger.this.getRandom().nextFloat() <= 1.0F / fraction) {
                this.attackStep = 6;
                this.defAttackTime = 6;
            } else {
                this.attackStep = 3;
                this.defAttackTime = 12;
            }
            this.attackTime = this.adjustedTickDelay(20);
            super.start();
        }
        @Override
        public void tick() {
            if (this.attackTime > 0) {
                --this.attackTime;
            }
            if (this.attackTime == 10 && Scavenger.this.getTarget() != null && Scavenger.this.distanceToSqr(Scavenger.this.getTarget()) < 16.0D) {
                if (Scavenger.this.onGround()) {
                    Vec3 targetEntityPos = Scavenger.this.getTarget().position();
                    Vec3 thisEntityPos = Scavenger.this.position();

                    // 计算方向矢量
                    Vec3 directionVector = thisEntityPos.subtract(targetEntityPos);
                    double distance = Scavenger.this.distanceToSqr(Scavenger.this.getTarget());
                    Scavenger.this.setDeltaMovement(Scavenger.this.getDeltaMovement().add(
                            directionVector.x / (1 + 0.2 * distance), 0.5D, directionVector.z / (1 + 0.2 * distance)));
                    Scavenger.this.setYRot(Scavenger.this.yHeadRot);
                    Scavenger.this.setOnGround(false);
                    Scavenger.this.hasImpulse = true;
                }
            }
            if (this.attackTime <= 6) {
                Scavenger.this.setIsSpellCasting((byte)2);
            }
            if (this.attackTime == 0) {
                this.performSpellCasting();
            }
            super.tick();
            //System.out.println("ScavengerAttackSpellGoal的tick，当前attackWarmupDelay：" + this.attackWarmupDelay);
        }

        public void stop() {
            super.stop();
            this.attackStep = 0;
            Scavenger.this.setIsSpellCasting((byte)0);
            Scavenger.this.stopUsingItem();
            if (Scavenger.this.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof CrossbowItem) changeHands();
        }

        private void changeHands() {
            ItemStack tool = Scavenger.this.getItemInHand(InteractionHand.OFF_HAND).copy();
            Scavenger.this.setItemInHand(InteractionHand.OFF_HAND, Scavenger.this.getItemInHand(InteractionHand.MAIN_HAND));
            Scavenger.this.setItemInHand(InteractionHand.MAIN_HAND,tool);
        }

        @Override
        protected int getCastWarmupTime() {
            return 60;
        }
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_ATTACK;
        }

        protected SpellcasterIllager.IllagerSpell getSpell() {
            return IllagerSpell.NONE;
        }
    }

    class ScavengerCastingSpellGoal extends SpellcasterIllager.SpellcasterCastingSpellGoal {

        @Override
        public void start() {
            super.start();
        }
        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            if (Scavenger.this.getTarget() != null) {
                Scavenger.this.getLookControl().setLookAt(Scavenger.this.getTarget(), (float)Scavenger.this.getMaxHeadYRot(), (float)Scavenger.this.getMaxHeadXRot());
            } else if (Scavenger.this.getWololoTarget() != null) {
                Scavenger.this.getLookControl().setLookAt(Scavenger.this.getWololoTarget(), (float)Scavenger.this.getMaxHeadYRot(), (float)Scavenger.this.getMaxHeadXRot());
            }

        }
    }

    class ScavengerRandemTeleportSpellGoal extends SpellcasterIllager.SpellcasterUseSpellGoal {
        private boolean hasTeleported;
        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            if (!super.canUse()) {
                return false;
            } else {
                return Scavenger.this.getLastHurtByMob() != null;
            }
        }

        protected int getCastingTime() {
            return 40;
        }

        protected int getCastingInterval() {
            return random.nextInt(121) + 240;
        }

        protected void performSpellCasting() {
            //ServerLevel serverlevel = (ServerLevel)Scavenger.this.level();
//            for(int i = 0; i < 3; ++i) {
//                BlockPos blockpos = Scavenger.this.blockPosition().offset(-2 + Scavenger.this.random.nextInt(5), 1, -2 + Scavenger.this.random.nextInt(5));
//                Vex vex = EntityType.VEX.create(Scavenger.this.level());
//                if (vex != null) {
//                    vex.moveTo(blockpos, 0.0F, 0.0F);
//                    vex.finalizeSpawn(serverlevel, Scavenger.this.level().getCurrentDifficultyAt(blockpos), MobSpawnType.MOB_SUMMONED, (SpawnGroupData)null, (CompoundTag)null);
//                    vex.setOwner(Scavenger.this);
//                    vex.setBoundOrigin(blockpos);
//                    vex.setLimitedLife(20 * (30 + Scavenger.this.random.nextInt(90)));
//                    serverlevel.addFreshEntityWithPassengers(vex);
//                }
//            }

        }

        @Override
        public void start() {
            spawnPreTeleportParticle(Scavenger.this.level(), Scavenger.this.blockPosition());
            super.start();
        }
        @Override
        public void tick() {
            if (this.attackWarmupDelay == 15) {
                Scavenger.this.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 30, 1,false,false));
                Scavenger.this.setIsSpellCasting((byte)3);
                Scavenger.this.setIsCastingSpell(SpellcasterIllager.IllagerSpell.NONE);
            }
            if (this.attackWarmupDelay <= 10 && this.attackWarmupDelay >= 5) {
                if (this.attackWarmupDelay == 10) Scavenger.this.setTeleporting(true);
                if (!this.hasTeleported) {
                    boolean flag = Scavenger.this.teleportSomewhere();
                    if (flag) {
                        if (Scavenger.this.level() instanceof ServerLevel serverLevel) {
                            for (int i = 0; i < 32; ++i) {
                                ParticleOptions type = (i % 4 == 0) ? ParticleTypes.WITCH : ParticleTypes.PORTAL;
                                serverLevel.sendParticles(type, Scavenger.this.xo, Scavenger.this.getRandomY() + Scavenger.this.random.nextDouble() * 2.0D, Scavenger.this.zo,1, 0, 0, 0, 0.05);
                            }
                        }
                        this.hasTeleported = true;
                    }
                }
                if (this.attackWarmupDelay == 5) Scavenger.this.setTeleporting(false);
            }
            super.tick();
        }

        public void stop() {
            super.stop();
            Scavenger.this.setIsSpellCasting((byte)0);
            Scavenger.this.setPotionCount((byte)(Scavenger.this.getPotionCount() + 1));
            spawnPreTeleportParticle(Scavenger.this.level(), Scavenger.this.blockPosition());
        }

        //以方框的形状生成粒子
        private void spawnPreTeleportParticle(Level level, BlockPos pPos) {
            if (level instanceof ServerLevel serverLevel) {
                for (int i = 0;i<10;i++){
                    ParticleOptions type = (i == 3 || i == 5 || i == 8) ? ParticleTypes.WITCH : ParticleTypes.PORTAL;
                    Vec3 pos = new Vec3(pPos.getX() + 0.5, pPos.getY() + 0.5, pPos.getZ() + 0.5);
                    double flunc = Math.random() * 2 - 1;
                    serverLevel.sendParticles(type,pos.x + 1,    pos.y + 0.1,pos.z + flunc,1,0,0,0,0.03);
                    serverLevel.sendParticles(type,pos.x - 1,    pos.y + 0.1,pos.z + flunc,1,0,0,0,0.03);
                    serverLevel.sendParticles(type,pos.x + flunc,pos.y + 0.1, pos.z + 1,    1,0,0,0,0.03);
                    serverLevel.sendParticles(type,pos.x + flunc,pos.y + 0.1, pos.z - 1,    1,0,0,0,0.03);
                }
            }
        }

        @Override
        protected int getCastWarmupTime() {
            return 40;
        }
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_SUMMON;
        }

        protected SpellcasterIllager.IllagerSpell getSpell() {
            return IllagerSpell.BLINDNESS;
        }
    }

    public class ScavengerWololoSpellGoal extends SpellcasterIllager.SpellcasterUseSpellGoal {
        private final TargetingConditions wololoTargeting = TargetingConditions.forNonCombat().range(16.0D).selector((p_32710_) -> {
            return ((Sheep)p_32710_).getColor() == DyeColor.BLUE;
        });

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            if (Scavenger.this.getTarget() != null) {
                return false;
            } else if (Scavenger.this.isCastingSpell()) {
                return false;
            } else if (Scavenger.this.tickCount < this.nextAttackTickCount) {
                return false;
            } else if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(Scavenger.this.level(), Scavenger.this)) {
                return false;
            } else {
                List<Sheep> list = Scavenger.this.level().getNearbyEntities(Sheep.class, this.wololoTargeting, Scavenger.this, Scavenger.this.getBoundingBox().inflate(16.0D, 4.0D, 16.0D));
                if (list.isEmpty()) {
                    return false;
                } else {
                    Scavenger.this.setWololoTarget(list.get(Scavenger.this.random.nextInt(list.size())));
                    return true;
                }
            }
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse() {
            return Scavenger.this.getWololoTarget() != null && this.attackWarmupDelay > 0;
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void stop() {
            super.stop();
            Scavenger.this.setWololoTarget((Sheep)null);
        }

        protected void performSpellCasting() {
            Sheep sheep = Scavenger.this.getWololoTarget();
            if (sheep != null && sheep.isAlive()) {
                sheep.setColor(DyeColor.RED);
            }

        }

        protected int getCastWarmupTime() {
            return 40;
        }

        protected int getCastingTime() {
            return 60;
        }

        protected int getCastingInterval() {
            return 140;
        }

        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_WOLOLO;
        }

        protected SpellcasterIllager.IllagerSpell getSpell() {
            return SpellcasterIllager.IllagerSpell.WOLOLO;
        }
    }
}
