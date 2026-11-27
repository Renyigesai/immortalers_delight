package com.renyigesai.immortalers_delight.entities.projectile;


import com.renyigesai.immortalers_delight.init.ImmortalersDelightEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class FireMonolithEntity extends PathfinderMob implements TraceableEntity {

    protected static final EntityDataAccessor<Integer> DATA_VARIANT_ID =
            SynchedEntityData.defineId(FireMonolithEntity.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Direction> DATA_ATTACH_FACE_ID =
            SynchedEntityData.defineId(FireMonolithEntity.class, EntityDataSerializers.DIRECTION);

    @javax.annotation.Nullable
    Mob owner;
    @javax.annotation.Nullable
    private BlockPos boundOrigin;
    private boolean hasLimitedLife;
    private int limitedLifeTicks;

    protected FireMonolithEntity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public FireMonolithEntity(Level pLevel) {
        super(ImmortalersDelightEntities.HIJACKED_SOUL_INFUSER.get(), pLevel);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 35.0D)
                .add(Attributes.ARMOR, 10.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 10.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_VARIANT_ID, 0);
        this.entityData.define(DATA_ATTACH_FACE_ID, Direction.DOWN);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 8.0F, 0.02F, true));
        this.goalSelector.addGoal(4, new FireMonolithAttackGoal());
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    // ---------- 变种 ----------

    public int getVariant() {
        return this.entityData.get(DATA_VARIANT_ID);
    }

    public void setVariant(int id) {
        this.entityData.set(DATA_VARIANT_ID, id);
    }

    @Override
    public Component getName() {
        Component custom = this.getCustomName();
        return custom != null ? custom : Component.translatable(this.getTranslationKeyByVariant());
    }

    private String getTranslationKeyByVariant() {
        return "";
    }

    // ---------- NBT ----------

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("Variant", this.getVariant());
        pCompound.putByte("AttachFace", (byte) this.getAttachFace().get3DDataValue());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("Variant", 99)) {
            this.setVariant(pCompound.getInt("Variant"));
        }
        if (pCompound.contains("AttachFace")) {
            this.setAttachFace(Direction.from3DDataValue(pCompound.getByte("AttachFace")));
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, net.minecraft.world.DifficultyInstance pDifficulty,
                                        MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        if (this.random.nextInt(10) == 0) {
            int roll = this.random.nextInt(3);
            this.setVariant(roll);
        }
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    // ---------- 固着 + 瞬移移动（参考 Shulker） ----------

    public Direction getAttachFace() {
        return this.entityData.get(DATA_ATTACH_FACE_ID);
    }

    private void setAttachFace(Direction pAttachFace) {
        this.entityData.set(DATA_ATTACH_FACE_ID, pAttachFace);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide && !this.isPassenger() && !this.canStayAt(this.blockPosition(), this.getAttachFace())) {
            Direction direction = findAttachableSurface(this.blockPosition());
            if (direction != null) {
                this.setAttachFace(direction);
            } else {
                this.teleportSomewhere();
            }
        }
    }

    @Override
    public Vec3 getDeltaMovement() {
        return Vec3.ZERO;
    }

    @Override
    public void setDeltaMovement(Vec3 pDeltaMovement) {
    }

    @Override
    public void move(MoverType pType, Vec3 pPos) {
        // 固定不动，不响应常规移动
    }

    @Nullable
    protected Direction findAttachableSurface(BlockPos pPos) {
        for (Direction direction : Direction.values()) {
            if (canStayAt(pPos, direction)) {
                return direction;
            }
        }
        return null;
    }

    boolean canStayAt(BlockPos pPos, Direction pFacing) {
        BlockState blockstate = this.level().getBlockState(pPos);
        if (!blockstate.isAir()) {
            return false;
        }
        Direction direction = pFacing.getOpposite();
        return this.level().loadedAndEntityCanStandOnFace(pPos.relative(pFacing), this, direction);
    }

    protected boolean teleportSomewhere() {
        if (this.isNoAi() || !this.isAlive()) {
            return false;
        }
        BlockPos blockpos = this.blockPosition();
        for (int i = 0; i < 5; ++i) {
            BlockPos blockpos1 = blockpos.offset(
                    Mth.randomBetweenInclusive(this.random, -8, 8),
                    Mth.randomBetweenInclusive(this.random, -8, 8),
                    Mth.randomBetweenInclusive(this.random, -8, 8));
            if (blockpos1.getY() > this.level().getMinBuildHeight()
                    && this.level().isEmptyBlock(blockpos1)
                    && this.level().getWorldBorder().isWithinBounds(blockpos1)) {
                Direction direction = findAttachableSurface(blockpos1);
                if (direction != null) {
                    this.setAttachFace(direction);
                    this.setPos(blockpos1.getX() + 0.5D, blockpos1.getY(), blockpos1.getZ() + 0.5D);
                    this.level().gameEvent(GameEvent.TELEPORT, blockpos, GameEvent.Context.of(this));
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void push(Entity pEntity) {
        // 固着实体不受推挤
    }

    @Nullable
    @Override
    public Entity getOwner() {
        return this.owner;
    }

    // ---------- 攻击 Goal ----------

    class FireMonolithAttackGoal extends Goal {
        private int attackCooldown;

        public FireMonolithAttackGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = FireMonolithEntity.this.getTarget();
            return target != null && target.isAlive();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void start() {
            this.attackCooldown = 20;
        }

        @Override
        public void tick() {
            LivingEntity target = FireMonolithEntity.this.getTarget();
            if (target == null) return;

            FireMonolithEntity.this.getLookControl().setLookAt(target, 30.0F, 30.0F);

            double distSq = FireMonolithEntity.this.distanceToSqr(target);
            if (distSq > 400.0D) {
                return;
            }

            if (--this.attackCooldown <= 0) {
                this.attackCooldown = 40;
                performAttack(target);
            }
        }

        public static final int FURIOUS = 0;
        public static final int OBSESSED = 1;
        public static final int LOST = 2;
        public static final int NORMAL = 3;
        private void performAttack(LivingEntity target) {
            int variant = FireMonolithEntity.this.getVariant();
            switch (variant) {
                case FURIOUS -> doFuriousAttack();
                case OBSESSED -> doObsessedAttack(target);
                case LOST -> doLostAttack();
                default -> doNormalAttack(target);
            }
        }

        // 普通种：单发火焰弹
        private void doNormalAttack(LivingEntity target) {
            shootFireballAt(target);
        }

        // 愤怒变种：前后左右四个方向的条形 AOE（长17 宽3 高14）
        private void doFuriousAttack() {
            BlockPos origin = FireMonolithEntity.this.blockPosition();
            Direction[] horizontals = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};
            for (Direction dir : horizontals) {
                AABB aoe = buildStripAabb(origin, dir, 17, 3, 14);
                applyAoeDamage(aoe);
            }
            FireMonolithEntity.this.level().broadcastEntityEvent(FireMonolithEntity.this, (byte) 4);
        }

        private AABB buildStripAabb(BlockPos origin, Direction dir, int length, int width, int height) {
            double halfWidth = width / 2.0D;
            Vec3 center = Vec3.atCenterOf(origin);
            Vec3 forward = new Vec3(dir.getStepX(), 0, dir.getStepZ()).scale(length);
            Vec3 lateral = new Vec3(-dir.getStepZ(), 0, dir.getStepX()).scale(halfWidth); // 垂直于方向的横向偏移
            Vec3 p1 = center.add(lateral).add(0, 0, 0);
            Vec3 p2 = center.subtract(lateral).add(forward).add(0, height, 0);
            return new AABB(p1.x, center.y, p1.z, p2.x, center.y + height, p2.z);
        }

        private void applyAoeDamage(AABB aabb) {
            for (var entity : FireMonolithEntity.this.level().getEntities(FireMonolithEntity.this, aabb,
                    e -> e instanceof LivingEntity && e != FireMonolithEntity.this)) {
                ((LivingEntity) entity).hurt(
                        FireMonolithEntity.this.damageSources().mobAttack(FireMonolithEntity.this), 8.0F);
                if (entity instanceof LivingEntity le) {
                    le.setSecondsOnFire(5);
                }
            }
        }

        // 痴迷变种：目标方向 + 该方向的正后方 + 左右两侧，共四发
        private void doObsessedAttack(LivingEntity target) {
            Vec3 toTarget = target.position().subtract(FireMonolithEntity.this.position()).normalize();
            Vec3 back = toTarget.scale(-1);
            Vec3 left = new Vec3(-toTarget.z, 0, toTarget.x);
            Vec3 right = new Vec3(toTarget.z, 0, -toTarget.x);

            shootFireballInDirection(toTarget);
            shootFireballInDirection(back);
            shootFireballInDirection(left);
            shootFireballInDirection(right);
        }

        // 迷惘变种：四个斜角方向，各以水平/45度俯角/45度仰角发射，共12发
        private void doLostAttack() {
            double diag = Math.sqrt(0.5D);
            Vec3[] diagonals = {
                    new Vec3(diag, 0, diag),   // 东南
                    new Vec3(-diag, 0, diag),  // 西南
                    new Vec3(-diag, 0, -diag), // 西北
                    new Vec3(diag, 0, -diag)   // 东北
            };
            double pitchComponent = Math.sqrt(0.5D);
            for (Vec3 dir : diagonals) {
                Vec3 horizontalScaled = dir.scale(pitchComponent);
                shootFireballInDirection(dir);                                  // 水平
                shootFireballInDirection(horizontalScaled.add(0, pitchComponent, 0)); // 45度仰角
                shootFireballInDirection(horizontalScaled.add(0, -pitchComponent, 0)); // 45度俯角
            }
        }

        private void shootFireballAt(LivingEntity target) {
            Vec3 dir = target.position().add(0, target.getEyeHeight() * 0.5, 0)
                    .subtract(FireMonolithEntity.this.position()).normalize();
            shootFireballInDirection(dir);
        }

        private void shootFireballInDirection(Vec3 direction) {
            // TODO: 替换为你的具体火焰弹实体类（例如 FireballProjectileEntity）
            // 下面以 net.minecraft.world.entity.projectile.SmallFireball 的构造模式示意：
            Level level = FireMonolithEntity.this.level();
            if (level.isClientSide) return;

            double speed = 1.5D;
            net.minecraft.world.entity.projectile.SmallFireball fireball =
                    new net.minecraft.world.entity.projectile.SmallFireball(level, FireMonolithEntity.this,
                            direction.x * speed, direction.y * speed, direction.z * speed);
            fireball.setPos(FireMonolithEntity.this.getX() + direction.x,
                    FireMonolithEntity.this.getEyeY(),
                    FireMonolithEntity.this.getZ() + direction.z);
            level.addFreshEntity(fireball);
        }
    }
}
//
//import com.renyigesai.immortalers_delight.init.ImmortalersDelightEntities;
//import net.minecraft.core.BlockPos;
//import net.minecraft.world.entity.*;
//import net.minecraft.world.level.Level;
//import org.jetbrains.annotations.Nullable;
//
//public class FireMonolithEntity extends Mob implements TraceableEntity {
//    protected FireMonolithEntity(EntityType<? extends Mob> pEntityType, Level pLevel) {super(pEntityType, pLevel);}
//
//    public FireMonolithEntity(Level pLevel) {
//        super(ImmortalersDelightEntities.HIJACKED_SOUL_INFUSER.get(), pLevel);
//    }
//
//    @javax.annotation.Nullable
//    Mob owner;
//    @javax.annotation.Nullable
//    private BlockPos boundOrigin;
//    private boolean hasLimitedLife;
//    private int limitedLifeTicks;
//    @Nullable
//    @Override
//    public Entity getOwner() {
//        return null;
//    }
//}
