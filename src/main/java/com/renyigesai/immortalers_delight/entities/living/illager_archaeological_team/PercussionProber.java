package com.renyigesai.immortalers_delight.entities.living.illager_archaeological_team;

import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

/**
 * 定义了"恼鬼（PercussionProber）"实体类，继承自Monster并实现TraceableEntity接口
 * 恼鬼是一种敌对生物，具有漂浮移动、冲锋攻击等特性，通常由唤魔者召唤
 */
public class PercussionProber extends Monster implements TraceableEntity {
    // 翅膀扇动的角度（每 tick）
    public static final float FLAP_DEGREES_PER_TICK = 45.836624F;
    // 完成一次扇动的 ticks 数（基于圆周率计算，约 4  ticks）
    public static final int TICKS_PER_FLAP = Mth.ceil(3.9269907F);
    // 用于同步实体状态的网络数据访问器（存储各种状态标记）
    protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(PercussionProber.class, EntityDataSerializers.BYTE);
    // 冲锋状态的标记位（用于DATA_FLAGS_ID）
    private static final int FLAG_IS_CHARGING = 1;
    // 骑乘时的 Y 轴偏移量
    private static final double RIDING_OFFSET = 0.4D;
    // 该恼鬼的所有者（通常是唤魔者）
    @Nullable
    Mob owner;
    // 绑定的原点位置（通常是召唤它的位置）
    @Nullable
    private BlockPos boundOrigin;
    // 是否有生命周期限制（被召唤出的恼鬼有存在时间限制）
    private boolean hasLimitedLife;
    // 生命周期计时器（倒计时，为 0 时开始扣血）
    private int limitedLifeTicks;

    /**
     * 构造函数
     * @param pEntityType 实体类型
     * @param pLevel 世界对象
     */
    public PercussionProber(EntityType<? extends PercussionProber> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        // 设置自定义移动控制器
        this.moveControl = new PercussionProber.PercussionProberMoveControl(this);
        // 击杀后掉落的经验值
        this.xpReward = 5;
    }

    /**
     * 获取站立时的眼睛高度
     * @param pPose 实体姿态
     * @param pDimensions 实体尺寸
     * @return 眼睛高度
     */
    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pDimensions) {
        return pDimensions.height - 0.28125F;
    }

    /**
     * 判断当前是否在扇动翅膀（基于固定周期）
     * @return 是否扇动翅膀
     */
    public boolean isFlapping() {
        return this.tickCount % TICKS_PER_FLAP == 0;
    }

    /**
     * 移动实体的方法（重写以检查碰撞）
     * @param pType 移动类型
     * @param pPos 移动向量
     */
    public void move(MoverType pType, Vec3 pPos) {
        super.move(pType, pPos);
        this.checkInsideBlocks(); // 检查是否进入方块内部并调整位置
    }

    /**
     * 每 tick 更新实体状态的方法
     */
    public void tick() {
        this.noPhysics = true; // 暂时禁用物理碰撞
        super.tick();
        this.noPhysics = false; // 恢复物理碰撞
        this.setNoGravity(true); // 始终禁用重力（漂浮特性）

        // 处理有限生命周期：倒计时减少，为 0 时开始每秒扣 1 血
        if (this.hasLimitedLife && --this.limitedLifeTicks <= 0) {
            this.limitedLifeTicks = 20; // 重置为 20 tick（1 秒）
            this.hurt(this.damageSources().starve(), 1.0F); // 因"饥饿"扣血（实际是生命周期结束）
        }

    }

    /**
     * 注册 AI 目标（行为逻辑）
     */
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this)); // 0号优先级：在水中漂浮的目标
        this.goalSelector.addGoal(4, new PercussionProber.PercussionProberChargeAttackGoal()); // 4号优先级：冲锋攻击目标
        this.goalSelector.addGoal(8, new PercussionProber.PercussionProberRandomMoveGoal()); // 8号优先级：随机移动目标
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F)); // 9号优先级：看向玩家（近距离）
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F)); // 10号优先级：看向生物（远距离）

        // 目标选择器（攻击逻辑）
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Raider.class)).setAlertOthers()); // 1号优先级：被攻击后反击（包括袭击者）
        this.targetSelector.addGoal(2, new PercussionProber.PercussionProberCopyOwnerTargetGoal(this)); // 2号优先级：复制所有者的目标
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true)); // 3号优先级：攻击最近的玩家
    }

    /**
     * 创建实体的属性构建器（设置基础属性）
     * @return 属性构建器
     */
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 24.0D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D);
    }

    /**
     * 定义需要同步的网络数据（客户端与服务器同步）
     */
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS_ID, (byte)0); // 初始化状态标记为 0
    }

    /**
     * 从 NBT 标签读取额外数据（保存/加载时使用）
     * @param pCompound NBT 标签
     */
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        // 读取绑定的原点位置
        if (pCompound.contains("BoundX")) {
            this.boundOrigin = new BlockPos(
                    pCompound.getInt("BoundX"),
                    pCompound.getInt("BoundY"),
                    pCompound.getInt("BoundZ")
            );
        }
        // 读取生命周期数据
        if (pCompound.contains("LifeTicks")) {
            this.setLimitedLife(pCompound.getInt("LifeTicks"));
        }

    }

    /**
     * 向 NBT 标签写入额外数据（保存时使用）
     * @param pCompound NBT 标签
     */
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        // 保存绑定的原点位置
        if (this.boundOrigin != null) {
            pCompound.putInt("BoundX", this.boundOrigin.getX());
            pCompound.putInt("BoundY", this.boundOrigin.getY());
            pCompound.putInt("BoundZ", this.boundOrigin.getZ());
        }
        // 保存生命周期数据
        if (this.hasLimitedLife) {
            pCompound.putInt("LifeTicks", this.limitedLifeTicks);
        }

    }

    /**
     * 获取该恼鬼的所有者
     * @return 所有者实体（可能为 null）
     */
    @Nullable
    public Mob getOwner() {
        return this.owner;
    }

    /**
     * 获取绑定的原点位置
     * @return 绑定的方块位置（可能为 null）
     */
    @Nullable
    public BlockPos getBoundOrigin() {
        return this.boundOrigin;
    }

    /**
     * 设置绑定的原点位置
     * @param pBoundOrigin 方块位置
     */
    public void setBoundOrigin(@Nullable BlockPos pBoundOrigin) {
        this.boundOrigin = pBoundOrigin;
    }

    /**
     * 检查指定的状态标记是否激活
     * @param pMask 标记掩码（如 FLAG_IS_CHARGING）
     * @return 是否激活
     */
    private boolean getPercussionProberFlag(int pMask) {
        int i = this.entityData.get(DATA_FLAGS_ID);
        return (i & pMask) != 0;
    }

    /**
     * 设置指定的状态标记
     * @param pMask 标记掩码
     * @param pValue 是否激活
     */
    private void setPercussionProberFlag(int pMask, boolean pValue) {
        int i = this.entityData.get(DATA_FLAGS_ID);
        if (pValue) {
            i |= pMask; // 激活标记
        } else {
            i &= ~pMask; // 关闭标记
        }
        this.entityData.set(DATA_FLAGS_ID, (byte)(i & 255)); // 确保在 byte 范围内
    }

    /**
     * 判断是否正在冲锋
     * @return 是否冲锋中
     */
    public boolean isCharging() {
        return this.getPercussionProberFlag(1);
    }

    /**
     * 设置是否正在冲锋
     * @param pCharging 是否冲锋
     */
    public void setIsCharging(boolean pCharging) {
        this.setPercussionProberFlag(1, pCharging);
    }

    /**
     * 设置所有者
     * @param pOwner 所有者实体
     */
    public void setOwner(Mob pOwner) {
        this.owner = pOwner;
    }

    /**
     * 设置有限生命周期
     * @param pLimitedLifeTicks 生命周期的 ticks 数
     */
    public void setLimitedLife(int pLimitedLifeTicks) {
        this.hasLimitedLife = true;
        this.limitedLifeTicks = pLimitedLifeTicks;
    }

    /**
     * 获取 ambient 音效（环境音效）
     * @return 环境音效事件
     */
    protected SoundEvent getAmbientSound() {
        return SoundEvents.VEX_AMBIENT;
    }

    /**
     * 获取死亡音效
     * @return 死亡音效事件
     */
    protected SoundEvent getDeathSound() {
        return SoundEvents.VEX_DEATH;
    }

    /**
     * 获取受伤音效
     * @param pDamageSource 伤害来源
     * @return 受伤音效事件
     */
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.VEX_HURT;
    }

    /**
     * 获取与光照相关的魔法值（影响生成等逻辑）
     * @return 固定返回 1.0F
     */
    public float getLightLevelDependentMagicValue() {
        return 1.0F;
    }

    /**
     * 完成实体生成的最后步骤（装备等）
     * @param pLevel 世界访问器
     * @param pDifficulty 难度实例
     * @param pReason 生成类型
     * @param pSpawnData 生成数据
     * @param pDataTag NBT 标签
     * @return 生成数据
     */
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        RandomSource randomsource = pLevel.getRandom();
        this.populateDefaultEquipmentSlots(randomsource, pDifficulty); // 生成默认装备
        this.populateDefaultEquipmentEnchantments(randomsource, pDifficulty); // 生成装备附魔
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    /**
     * 填充默认装备槽位
     * @param pRandom 随机数生成器
     * @param pDifficulty 难度实例
     */
    protected void populateDefaultEquipmentSlots(RandomSource pRandom, DifficultyInstance pDifficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD)); // 主手装备铁剑
        this.setDropChance(EquipmentSlot.MAINHAND, 0.0F); // 铁剑不会掉落
    }

    /**
     * 获取骑乘时的 Y 轴偏移量
     * @return 偏移量
     */
    public double getMyRidingOffset() {
        return 0.4D;
    }

    /**
     * 恼鬼的冲锋攻击目标 AI
     */
    class PercussionProberChargeAttackGoal extends Goal {
        public PercussionProberChargeAttackGoal() {
            // 设置该目标影响"移动"状态
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        /**
         * 判断该目标是否可以开始执行
         * @return 是否满足执行条件
         */
        public boolean canUse() {
            LivingEntity livingentity = PercussionProber.this.getTarget();
            // 条件：有目标、目标存活、当前没有移动任务、随机概率触发（约每 7 tick 一次机会）
            if (livingentity != null && livingentity.isAlive() && !PercussionProber.this.getMoveControl().hasWanted() && PercussionProber.this.random.nextInt(reducedTickDelay(7)) == 0) {
                return PercussionProber.this.distanceToSqr(livingentity) > 4.0D; // 距离目标大于 2 格（平方为 4）
            } else {
                return false;
            }
        }

        /**
         * 判断该目标是否可以继续执行
         * @return 是否继续执行
         */
        public boolean canContinueToUse() {
            // 条件：有移动任务、正在冲锋、目标存在且存活
            return PercussionProber.this.getMoveControl().hasWanted() && PercussionProber.this.isCharging() && PercussionProber.this.getTarget() != null && PercussionProber.this.getTarget().isAlive();
        }

        /**
         * 开始执行目标（初始化冲锋）
         */
        public void start() {
            LivingEntity livingentity = PercussionProber.this.getTarget();
            if (livingentity != null) {
                // 向目标的眼睛位置移动
                Vec3 vec3 = livingentity.getEyePosition();
                PercussionProber.this.moveControl.setWantedPosition(vec3.x, vec3.y, vec3.z, 1.0D);
            }
            PercussionProber.this.setIsCharging(true); // 标记为冲锋状态
            PercussionProber.this.playSound(SoundEvents.VEX_CHARGE, 1.0F, 1.0F); // 播放冲锋音效
        }

        /**
         * 停止执行目标（结束冲锋）
         */
        public void stop() {
            PercussionProber.this.setIsCharging(false); // 取消冲锋状态
        }

        /**
         * 是否需要每 tick 更新
         * @return 始终返回 true
         */
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        /**
         * 每 tick 执行的逻辑（持续冲锋）
         */
        public void tick() {
            LivingEntity livingentity = PercussionProber.this.getTarget();
            if (livingentity != null) {
                // 如果碰撞箱重叠，执行攻击并结束冲锋
                if (PercussionProber.this.getBoundingBox().intersects(livingentity.getBoundingBox())) {
                    PercussionProber.this.doHurtTarget(livingentity);
                    PercussionProber.this.setIsCharging(false);
                } else {
                    double d0 = PercussionProber.this.distanceToSqr(livingentity);
                    // 如果距离小于 3 格（平方为 9），继续向目标眼睛位置移动
                    if (d0 < 9.0D) {
                        Vec3 vec3 = livingentity.getEyePosition();
                        PercussionProber.this.moveControl.setWantedPosition(vec3.x, vec3.y, vec3.z, 1.0D);
                    }
                }

            }
        }
    }

    /**
     * 复制所有者目标的 AI（跟随主人的攻击目标）
     */
    class PercussionProberCopyOwnerTargetGoal extends TargetGoal {
        // 目标选择条件：非战斗状态、忽略视线、忽略隐身
        private final TargetingConditions copyOwnerTargeting = TargetingConditions.forNonCombat().ignoreLineOfSight().ignoreInvisibilityTesting();

        public PercussionProberCopyOwnerTargetGoal(PathfinderMob pMob) {
            super(pMob, false);
        }

        /**
         * 判断是否可以开始执行
         * @return 是否有所有者且所有者有目标，且可以攻击该目标
         */
        public boolean canUse() {
            return PercussionProber.this.owner != null && PercussionProber.this.owner.getTarget() != null && this.canAttack(PercussionProber.this.owner.getTarget(), this.copyOwnerTargeting);
        }

        /**
         * 开始执行目标（设置自身目标为所有者的目标）
         */
        public void start() {
            PercussionProber.this.setTarget(PercussionProber.this.owner.getTarget());
            super.start();
        }
    }

    /**
     * 恼鬼的移动控制器（实现漂浮移动逻辑）
     */
    class PercussionProberMoveControl extends MoveControl {
        public PercussionProberMoveControl(PercussionProber pPercussionProber) {
            super(pPercussionProber);
        }

        /**
         * 每 tick 更新移动状态
         */
        public void tick() {
            if (this.operation == MoveControl.Operation.MOVE_TO) { // 如果有移动任务
                // 计算当前位置到目标位置的向量
                Vec3 vec3 = new Vec3(this.wantedX - PercussionProber.this.getX(), this.wantedY - PercussionProber.this.getY(), this.wantedZ - PercussionProber.this.getZ());
                double d0 = vec3.length(); // 距离目标的直线距离

                // 如果距离小于自身碰撞箱大小，停止移动
                if (d0 < PercussionProber.this.getBoundingBox().getSize()) {
                    this.operation = MoveControl.Operation.WAIT;
                    PercussionProber.this.setDeltaMovement(PercussionProber.this.getDeltaMovement().scale(0.5D)); // 减速
                } else {
                    // 按比例向目标移动（速度 * 0.05 / 距离，确保移动平滑）
                    PercussionProber.this.setDeltaMovement(PercussionProber.this.getDeltaMovement().add(vec3.scale(this.speedModifier * 0.05D / d0)));

                    // 调整朝向：无目标时朝移动方向，有目标时朝目标方向
                    if (PercussionProber.this.getTarget() == null) {
                        Vec3 vec31 = PercussionProber.this.getDeltaMovement();
                        PercussionProber.this.setYRot(-((float)Mth.atan2(vec31.x, vec31.z)) * (180F / (float)Math.PI)); // 计算水平旋转角
                        PercussionProber.this.yBodyRot = PercussionProber.this.getYRot();
                    } else {
                        double d2 = PercussionProber.this.getTarget().getX() - PercussionProber.this.getX();
                        double d1 = PercussionProber.this.getTarget().getZ() - PercussionProber.this.getZ();
                        PercussionProber.this.setYRot(-((float)Mth.atan2(d2, d1)) * (180F / (float)Math.PI));
                        PercussionProber.this.yBodyRot = PercussionProber.this.getYRot();
                    }
                }

            }
        }
    }

    /**
     * 恼鬼的随机移动目标（无目标时围绕绑定点移动）
     */
    class PercussionProberRandomMoveGoal extends Goal {
        public PercussionProberRandomMoveGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE)); // 影响"移动"状态
        }

        /**
         * 判断是否可以开始执行
         * @return 无移动任务且随机概率触发（约每 7 tick 一次机会）
         */
        public boolean canUse() {
            return !PercussionProber.this.getMoveControl().hasWanted() && PercussionProber.this.random.nextInt(reducedTickDelay(7)) == 0;
        }

        /**
         * 判断是否可以继续执行（随机移动是一次性任务，不持续）
         * @return 始终返回 false
         */
        public boolean canContinueToUse() {
            return false;
        }

        /**
         * 执行随机移动逻辑
         */
        public void tick() {
            BlockPos blockpos = PercussionProber.this.getBoundOrigin();
            if (blockpos == null) {
                blockpos = PercussionProber.this.blockPosition(); // 无绑定点时使用自身当前位置
            }

            // 尝试 3 次寻找随机位置（防止卡在不可移动的位置）
            for(int i = 0; i < 3; ++i) {
                // 在绑定点周围 7 格范围内随机生成位置
                BlockPos blockpos1 = blockpos.offset(
                        PercussionProber.this.random.nextInt(15) - 7, // X 轴：-7 ~ +7
                        PercussionProber.this.random.nextInt(11) - 5, // Y 轴：-5 ~ +5
                        PercussionProber.this.random.nextInt(15) - 7  // Z 轴：-7 ~ +7
                );
                // 如果该位置是空气方块（可移动）
                if (PercussionProber.this.level().isEmptyBlock(blockpos1)) {
                    // 向该位置移动（中心坐标）
                    PercussionProber.this.moveControl.setWantedPosition(
                            (double)blockpos1.getX() + 0.5D,
                            (double)blockpos1.getY() + 0.5D,
                            (double)blockpos1.getZ() + 0.5D,
                            0.25D // 移动速度
                    );
                    // 无目标时看向移动目标
                    if (PercussionProber.this.getTarget() == null) {
                        PercussionProber.this.getLookControl().setLookAt(
                                (double)blockpos1.getX() + 0.5D,
                                (double)blockpos1.getY() + 0.5D,
                                (double)blockpos1.getZ() + 0.5D,
                                180.0F, // 水平视野范围
                                20.0F   // 垂直视野范围
                        );
                    }
                    break; // 找到位置后退出循环
                }
            }

        }
    }
}
