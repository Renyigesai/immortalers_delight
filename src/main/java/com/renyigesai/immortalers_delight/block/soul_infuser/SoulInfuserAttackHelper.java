package com.renyigesai.immortalers_delight.block.soul_infuser;

import com.renyigesai.immortalers_delight.entities.living.SoulInfuserEntity;
import com.renyigesai.immortalers_delight.entities.projectile.SoulFireballEntity;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

//这个类用于管理失控炸锅的攻击行为与动画行为，差不多可以理解为给方块实体配的AI。
public class SoulInfuserAttackHelper {
    // ====================== 状态与字段管理 ========================= //
    public final AnimationState awakenAnimationState = new AnimationState();
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState attackFireRainAnimationState = new AnimationState();
    public final AnimationState attackExplodeAnimationState = new AnimationState();
    public final AnimationState attackColumnAnimationState = new AnimationState();
    public final AnimationState attackSummonAnimationState = new AnimationState();
    public final AnimationState attackFireRingAnimationState = new AnimationState();
    public final AnimationState attackFireballAnimationState = new AnimationState();
    public final AnimationState dieAnimationState = new AnimationState();
    public static final int ID_AWAKEN = 0;
    public static final int ID_IDLE = 1;
    public static final int ID_ATTACK_FIRE_RAIN = 2;
    public static final int ID_ATTACK_EXPLODE = 3;
    public static final int ID_ATTACK_COLUMN = 4;
    public static final int ID_ATTACK_SUMMON = 5;
    public static final int ID_ATTACK_IGNITE = 6;
    public static final int ID_ATTACK_FIREBALL = 7;
    public static final int ID_ATTACK_FIRE_RING = 8;
    public static final int ID_DIE = 14;
    public static final int ID_NONE = 15;
    private int animationType = 1;//当前进行的动画的ID
    public int getAnimationType() {return animationType;}

    //    private int lastAnimation = 1;//上一个动画的id，用于判断动画状态是否发生了改变
    private int animationTime = 0;//当前动画进行的时间，这个值仅客户端，注意这个值每次切换动画都重置
    public int getAnimationTime() {return animationTime;}

    private int attackType = 1;//当前进行的攻击的ID
    void setAttackType(int type) {this.attackType = type;}
    private int attackTime = 0;//当前战斗已开始的时间，这个值仅服务端
    public int getAttackTime() {return attackTime;}
    private Level level; // 所处维度
    public Level level() {return level;}
    public void setLevel(Level level) {this.level = level;}
    private BlockPos blockPos; // 位置
    public BlockPos getBlockPos() {return blockPos;}
    public void setBlockPos(BlockPos pos) {blockPos = pos;}
    private boolean isFighting = false; // 判定是否还在战斗状态，类似实体的死亡判定
    public boolean isFighting() {return isFighting;}
    private void setFighting(boolean fighting) {isFighting = fighting;}
    private int checkmateProgress = 0; // 击破进度，功能相当于实体中的血量。
    public int getCheckmateProgress() {return checkmateProgress;}
    @Nullable
    public SoulInfuserEntity toolEntity = null;    // 绑定的工具实体，用于记录血量等信息
    @Nullable
    private UUID toolEntityUUID;   // 工具实体的 UUID
    private int progress = 0;      // 当前战斗的进度，用于表示百分比血量。当进度达到100战斗结束
    private int brokenDeathArmor;  //已被击破的死亡护甲，用于计数

    public int getBrokenDeathArmor() {return brokenDeathArmor;}

    private int deathArmor;        //死亡护甲，相当于寄生虫的最小击杀数

    @Nullable
    private LivingEntity destroyTarget;   // 当前攻击锁定的敌对实体目标
    public LivingEntity getDestroyTarget() {return destroyTarget;}
    public void setDestroyTarget(LivingEntity destroyTarget) {this.destroyTarget = destroyTarget;}
    @Nullable
    private UUID destroyTargetUUID;   // 攻击目标的 UUID
    @NotNull
    private final Map<Integer,SoulInfuserAttackAction> actionMap = new ConcurrentHashMap<>(); //存储和管理行为。注意这个map可以双端，但初始化之后应当仅在服务端更新。
    public Map<Integer, SoulInfuserAttackAction> getActionMap() {return new HashMap<>(actionMap);}
    public float attackDamage = 0;
    public int lostShootCount = 0;//空过的随机普通攻击的次数，用于火球技能一次性结算
    public List<Integer> nextActions = new LinkedList<Integer>(); //下一次指定使用的技能，用于连招或者转阶段技能，仅服务端
    public int weakTime = 0; //不能释放技能的冷却时间，用于防止无缝连招

    public SoulInfuserAttackHelper(Level level, BlockPos blockPos) {
        this.level = level;
        this.blockPos = blockPos;
        this.brokenDeathArmor = 0;
        this.deathArmor = DifficultyModeUtil.isPowerBattleMode() ? 18 : 20;
    }
    //载入行为
    public void initAttackHelper() {

        System.out.println("初始化行为列表");
        this.actionMap.put(ID_AWAKEN, new AwakenAttackAction(this));
        this.actionMap.put(ID_ATTACK_FIRE_RAIN, new FireRainAttackAction(this));
        this.actionMap.put(ID_ATTACK_EXPLODE, new ExplodeAttackAction(this));
        this.actionMap.put(ID_ATTACK_COLUMN, new FireColumnAttackAction(this));
        this.actionMap.put(ID_ATTACK_IGNITE, new IgniteAttackAction(this));
        this.actionMap.put(ID_ATTACK_FIREBALL, new FireballAttackAction(this));
        this.actionMap.put(ID_ATTACK_FIRE_RING, new FireRingAttackAction(this));
        this.actionMap.put(ID_DIE, new DeathAttackAction(this));
    }

    // ======================= Tick主逻辑 ======================== //

    //客户端tick
    //用于管理动画状态和粒子
    public void AnimationTick() {
//        System.out.println("AnimationTick：开始");
        BlockPos pos = this.blockPos;
        if (this.level == null) {
//            System.out.println("世界是null");
            return;
        }
        if (!this.level.isClientSide()) {
//            System.out.println("世界是服务端");
            return;
        }
        //初始化行为
        if (this.actionMap.isEmpty()) {
//            System.out.println("行为列表是空的");
            initAttackHelper();
        }
        else {
            //实际行为逻辑。
            if (this.actionMap.containsKey(animationType)) {
                actionMap.get(animationType).animationTick();
            } else animationType = ID_IDLE;
            //客户端管理死亡护甲的上限
//            if (this.animationTime % 40 == 2) {
//                if (this.toolEntity.isPowerMode()) this.deathArmor = 18;
//                else this.deathArmor = 20;
//            }
//            System.out.println("AnimationTick：当前tick数" + animationTime + "animationType：" + animationType);

            this.animationTime++;
        }
//        System.out.println("AnimationTick：结束");
    }

    public void checkAnimationType(int newAnimation) {
//        BlockPos pos = this.blockPos;
        if (this.level == null) return;
        if (!level.isClientSide()) return;

//        BlockState state = this.level.getBlockState(pos);
        //查当前动画状态是否和方块状态指示的动画状态相同，不同则说明切换动画了要重置
        if (animationType != newAnimation) {
            animationTime = 0;
            animationType = newAnimation;
        }
        onChangeAnimation(animationType,animationTime);
    }


    public void onChangeAnimation(int typeID, int tickCount) {
        if (this.level.isClientSide()) {
            this.resetAnimations();
            switch (typeID) {
                case ID_AWAKEN:
                    this.awakenAnimationState.startIfStopped(tickCount);
                    break;
                case ID_IDLE:
                    this.idleAnimationState.startIfStopped(tickCount);
                    break;
                case ID_ATTACK_FIRE_RAIN, ID_ATTACK_IGNITE://点燃和火雨共用动画
                    this.attackFireRainAnimationState.startIfStopped(tickCount);
                    break;
                case ID_ATTACK_EXPLODE:
                    this.attackExplodeAnimationState.startIfStopped(tickCount);
                    break;
                case ID_ATTACK_COLUMN:
                    this.attackColumnAnimationState.startIfStopped(tickCount);
                    break;
                case ID_ATTACK_SUMMON:
                    this.attackSummonAnimationState.startIfStopped(tickCount);
                    break;
                case ID_ATTACK_FIRE_RING:
                    this.attackFireRingAnimationState.startIfStopped(tickCount);
                    break;
                case ID_ATTACK_FIREBALL:
                    this.attackFireballAnimationState.startIfStopped(tickCount);
                    break;
                case ID_DIE:
                    this.dieAnimationState.startIfStopped(tickCount);
                    break;
            }
        }

    }

    private void resetAnimations() {
        this.awakenAnimationState.stop();
        this.idleAnimationState.stop();
        this.attackFireRainAnimationState.stop();
        this.attackExplodeAnimationState.stop();
        this.attackColumnAnimationState.stop();
        this.attackSummonAnimationState.stop();
        this.attackFireRingAnimationState.stop();
        this.attackFireballAnimationState.stop();
        this.dieAnimationState.stop();
    }

    //战斗开始时的初始化方法
    public void startFighting() {
        setFighting(true);
        this.brokenDeathArmor = 0;
        this.deathArmor = DifficultyModeUtil.isPowerBattleMode() ? 18 : 20;
        this.progress = 0;
//        System.out.println("初始化战斗：" + brokenDeathArmor + " " + deathArmor + " " + progress);
    }

    //判定战斗是否结束
    public boolean needEndFighting() {
        if (this.toolEntity != null && SoulInfuserEntity.SoulInfuserEntityEvents.needExemptEntity(this.toolEntity)) {
//            System.out.println("实体被kill了战斗结束");
            return true;
        }
        if (this.progress >= 100) {
//            System.out.println("进度条打满了:" + this.progress + this.level.isClientSide());
            if (this.getBrokenDeathArmor() >= this.deathArmor) {
//                System.out.println("死亡护甲打空了" + this.getBrokenDeathArmor() + this.level.isClientSide() + this.deathArmor);

                return this.toolEntity == null || !this.toolEntity.isAlive();
            }
        }
        return false;
    }

    //战斗结束时的善后方法
    public void endFighting() {
        setFighting(false);
        this.attackTime=0;
        this.progress = 0;
        this.brokenDeathArmor = 0;
        this.attackType = ID_NONE;
        if (this.toolEntity != null) this.toolEntity.discard();
    }

    //服务端tick
    //在这里执行管理冷却与进行攻击的实际逻辑
    public void AttackTick() {
        BlockPos pos = this.blockPos;
        if (this.level == null) return;
        if (level.isClientSide()) return;
        //初始化行为
        if (this.actionMap.isEmpty()) {
            initAttackHelper();
            checkToolEntity();
        } else { //管理技能状态与冷却

            //如果当前处于待机状态
            if (attackType == ID_IDLE) {
                //查找下一个指定的行为，无视冷却启动
                if (!nextActions.isEmpty()) {
                    int id = nextActions.get(0);
                    if (this.actionMap.containsKey(id)) {
                        checkToolEntity();
                        attackType = id;
                        this.actionMap.get(id).startAction();
                        //利用方块状态同步到客户端，动画状态切换为当前攻击动画
                        BlockState state = level.getBlockState(pos);
                        if (state.hasProperty(SoulInfuserBlock.AGE)) level.setBlockAndUpdate(pos, state.setValue(SoulInfuserBlock.AGE,id));
                        this.nextActions.remove(0);
                    }
                } else if (this.weakTime <= 0){
                    //遍历查找就绪的行为
                    for (Map.Entry<Integer,SoulInfuserAttackAction> entry : actionMap.entrySet()) {
                        if (entry.getValue().isReady()) {
                            checkToolEntity();
                            //如果找到，设置当前攻击为对应的ID，开始攻击
//                        System.out.println("开始攻击：" + entry.getKey());
                            attackType = entry.getKey();
                            entry.getValue().startAction();
                            //利用方块状态同步到客户端，动画状态切换为当前攻击动画
                            BlockState state = level.getBlockState(pos);
                            if (state.hasProperty(SoulInfuserBlock.AGE)) level.setBlockAndUpdate(pos, state.setValue(SoulInfuserBlock.AGE,entry.getKey()));
                            break;
                        }
                    }
                }
                this.weakTime--;

                //空闲时随机攻击
                if (attackTime % 6 == 0){
                    if (level.getRandom().nextInt(5) == 0) shootSoulFireBallRandom(level,pos.above(2));
                    else System.out.println("运气不好");
                } else System.out.println("当前服务端tick数：" + attackTime);
            } else {
                //如果当前有进行中的攻击
                if (actionMap.containsKey(attackType)) {
                    //tick当前攻击
                    actionMap.get(attackType).actionTick();
                    //检查与结束当前攻击
                    if (actionMap.get(attackType).needStop()) {
                        actionMap.get(attackType).endAction();
                        this.attackType = ID_IDLE;
                        //利用方块状态同步到客户端，动画状态切换为待机动画
                        BlockState state = level.getBlockState(pos);
                        if (state.hasProperty(SoulInfuserBlock.AGE)) level.setBlockAndUpdate(pos, state.setValue(SoulInfuserBlock.AGE,ID_IDLE));
                    }
                    //兜底异常ID
                } else attackType = ID_IDLE;

                //非空闲时如果随机攻击，增加空过的次数
                if (attackTime % 6 == 0){
                    if (level.getRandom().nextInt(5) == 0) this.lostShootCount++;
                }
            }

            //从工具实体同步信息（仇恨和血量）
            if (this.toolEntity != null) {
                //同步仇恨
                if (this.toolEntity.getTarget() != null) {
//                    System.out.println("同步仇恨");
                    this.destroyTarget = toolEntity.getTarget();
                } //else System.out.println("没锁到敌");
                //同步血量
                float f = this.toolEntity.getHealth() / this.toolEntity.getMaxHealth();
                //同步攻击力
                this.attackDamage = this.toolEntity.getATK();

                //检测死亡护甲并定期更新护甲上限
                int dhType = isAddOrLost(100 - (int)(100 * f));
                if (dhType < 0) this.tryBreakDeathArmor(this.toolEntity);
                if (dhType > 0) this.tryAddDeathArmor(f);

                if (this.attackTime % 40 == 1) {
                    boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
                    if (isPowerful) this.deathArmor = 18;
                    else this.deathArmor = 20;
                }

            }
            if (this.attackTime % 10 == 1) {
                System.out.println("当前死亡护甲" + getBrokenDeathArmor() + " " + this.deathArmor);
                if (this.toolEntity == null || !this.toolEntity.isAlive()) {
                    boolean f1 = this.toolEntity == null;
                    boolean f2 = !f1 && !this.toolEntity.isAlive();
//                    System.out.println("为什么检查实体" + f1 + "还是？" + f2);
                    checkToolEntity();
                }
            }

//            System.out.println("AttackTick：当前tick数" + attackTime + "attackType：" + attackType);
            attackTime++;
        }
    }

    //判定进度的变化——如果进度增加，意味着血量减少，所以返回-1；反之意味着血量增加，返回1
    public int isAddOrLost(int i) {
        System.out.println("查血量：传入什么？" + i + " 现在进度是什么？" + this.progress);
        if (i != this.progress) {
            if (i < this.progress) {
                this.progress = i;
                return 1;
            }
            this.progress = i;
            return -1;
        }
        return 0;
    }


    public boolean tryAddDeathArmor(float p) {
        int breakedDeathArmor = getBrokenDeathArmor();
        float f = (float) (deathArmor - breakedDeathArmor) / deathArmor;
        if (p < f) {
            int newOne = (int) (( 1 - p ) * deathArmor);
            if (newOne > breakedDeathArmor) {
                this.brokenDeathArmor = newOne;
                return true;
            }
        }
        return false;
    }
    /** 击破一层死亡护甲，这个方法仅服务端 */
    public boolean tryBreakDeathArmor(@NotNull SoulInfuserEntity entity) {
        if (!entity.isAlive()) return false;
        if (entity.level().isClientSide()) return false;

        System.out.println("检查死亡护甲：" + this.attackTime + " " + entity.tickCount);
        int breakedDeathArmor = getBrokenDeathArmor();
        float f = (float) (deathArmor - breakedDeathArmor) / deathArmor;

        if (entity.getHealth() / entity.getMaxHealth() <= f || entity.getHealth() <= 1) {
            boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
            float ex = Math.max(entity.getMaxHealth() * (isPowerful ? 0.0125f : 0.01f), isPowerful ? 15 : 5);

            // 直接调用父类，不触发重写的 setHealth
            if (this.brokenDeathArmor == 0) {
                entity.setAbsorptionAmount(entity.getAbsorptionAmount() + ex);
                entity.setOldHealth(entity.getMaxHealth());
            } else entity.setOldHealth(entity.getMaxHealth() * f + ex);

            this.brokenDeathArmor++;
            entity.setIgnoreDamageTime(entity.getIgnoreDamageTime() + (isPowerful ? 2 : 6));
            System.out.println("成功破除死亡护甲");
            return true;
        }
        return false;
    }

    public void checkToolEntity() {

        System.out.println("检查实体");
        if (this.level instanceof ServerLevel serverLevel) {
            if (this.level.isClientSide()) {
                System.out.println("这TM是客户端世界");
                return;
            }

            //先试图从UUID还原绑定的实体
            if (this.toolEntity == null) {
                if (this.toolEntityUUID != null && serverLevel.getEntity(toolEntityUUID) instanceof SoulInfuserEntity entity) {
                    System.out.println("从UUID还原实体");
                    this.toolEntity = entity;
//                    this.toolEntityUUID = entity.getUUID();
                } else {
                    System.out.println("在附近查找实体");
                    //查找附近实体
                    List<SoulInfuserEntity> list = this.level.getEntitiesOfClass(SoulInfuserEntity.class, new AABB(this.blockPos).inflate(2), e -> e.isAlive());
                    if (!list.isEmpty()) {
                        SoulInfuserEntity needOne = null;
                        double distance = 99999;
                        for (SoulInfuserEntity infuser : list) {
                            if (needOne != null) distance = needOne.distanceToSqr(this.blockPos.getCenter());
                            if (distance > infuser.distanceToSqr(this.blockPos.getCenter())) needOne = infuser;
                        }
                        if (needOne != null) {
                            System.out.println("找到了");
                            needOne.moveTo(this.blockPos, 0.0F, 0.0F);
                            needOne.setHomePos(this.blockPos.getCenter().add(0,-0.5,0));
                            this.toolEntity = needOne;
                            this.toolEntityUUID = needOne.getUUID();
                        }

                    } else {
                        System.out.println("找不到，复活");
                        //还找不到，new一个新的
                        reNewTool(serverLevel);
                    }

                }
            } else if (!this.toolEntity.isAlive()) {
                System.out.println("战斗还没结束实体死了");
                //战斗还没结束实体死了，也复活一个
                reNewTool(serverLevel);
            }
        } else System.out.println("这tm不是服务端世界");

    }

    private void reNewTool(ServerLevel serverLevel) {
        System.out.println("尝试复活");
        SoulInfuserEntity entity = new SoulInfuserEntity(this.level);
        entity.moveTo(this.blockPos, 0.0F, 0.0F);
        entity.setHomePos(this.blockPos.getCenter().add(0,-0.5,0));
        entity.finalizeSpawn(serverLevel, this.level.getCurrentDifficultyAt(this.blockPos), MobSpawnType.MOB_SUMMONED, (SpawnGroupData) null, (CompoundTag) null);
        this.level.addFreshEntity(entity);
        this.toolEntity = entity;
        this.toolEntityUUID = entity.getUUID();
    }

    public void shootSoulFireBallRandom(Level level, BlockPos pos) {
        //发射灵魂弹实体
        // 1. 随机生成射弹的方向向量
        int i = level.getRandom().nextBoolean() ? 1 : -1;
        int j = level.getRandom().nextBoolean() ? 1 : -1;
        int k = level.getRandom().nextBoolean() ? 1 : -1;
        Vec3 lookDirection = new Vec3(
//                    1,1,1
                level.getRandom().nextFloat() * i,
                level.getRandom().nextFloat() * j,
                level.getRandom().nextFloat() * k
        );
        // 2. 后续逻辑：沿该方向生成投射物（示例）
        double spawnX = pos.getX() + 0.5f + lookDirection.x;
        double spawnY = pos.getY() + 0.5f + lookDirection.y;
        double spawnZ = pos.getZ() + 0.5f + lookDirection.z;
        SoulFireballEntity fireball = new SoulFireballEntity(spawnX,spawnY,spawnZ,
                lookDirection.x * 0.5D, lookDirection.y * 0.5D,lookDirection.z * 0.5D,
                level
        );

//            fireball.shootFromRotation(livingEntity, livingEntity.getXRot(), livingEntity.getYRot(), 0.0F, 1.5F, 1.0F);
//            fireball.setPos(spawnX, spawnY, spawn4 Z);
        level.addFreshEntity(fireball);
    }
    public void shootSoulFireBallToPos(Level level, BlockPos pos, BlockPos targetPos) {
        if (level.isClientSide) return;

        double spawnX = pos.getX() + 0.5D;
        double spawnY = pos.getY() + 0.5D;
        double spawnZ = pos.getZ() + 0.5D;

        Vec3 targetCenter = new Vec3(targetPos.getX() + 0.5D, targetPos.getY() + 0.5D, targetPos.getZ() + 0.5D);
        Vec3 spawnPos = new Vec3(spawnX, spawnY, spawnZ);
        Vec3 delta = targetCenter.subtract(spawnPos);

        if (delta.lengthSqr() < 1.0E-4) return;
        Vec3 lookDirection = delta.normalize();

        // 初始速度赋予轻微扩散
        SoulFireballEntity fireball = new SoulFireballEntity(
                spawnX, spawnY, spawnZ,
                lookDirection.x * 0.3D, lookDirection.y * 0.3D, lookDirection.z * 0.3D,
                level
        );

        // 绑定目标方块中心点
        fireball.setTargetPos(targetCenter);
        fireball.setFallbackDirection(lookDirection);

        if (DifficultyModeUtil.isPowerBattleMode()) {
            fireball.setDangerous(true);
        }
        level.addFreshEntity(fireball);
    }


    // =========================== 批量生成粒子的工具方法 =========================== //
    /**
     * 生成"向中心聚拢"的粒子效果：在球面上随机取点，计算指向中心的速度向量。
     */
    private void spawnConvergingParticles(Level level, ParticleOptions particle,
                                          Vec3 center, double radius, int count, double speed) {
        RandomSource random = level.random;
        for (int i = 0; i < count; i++) {
            double theta = random.nextDouble() * Math.PI * 2;
            double phi = Math.acos(2 * random.nextDouble() - 1);
            double px = center.x + radius * Math.sin(phi) * Math.cos(theta);
            double py = center.y + radius * Math.cos(phi);
            double pz = center.z + radius * Math.sin(phi) * Math.sin(theta);

            Vec3 toCenter = center.subtract(px, py, pz).normalize().scale(speed);
            level.addParticle(particle, px, py, pz, toCenter.x, toCenter.y, toCenter.z);
        }
    }


    /**
     * 生成放射状（类似烟花）粒子：从中心沿多个方向直线状喷出。
     */
    private void spawnRadialParticles(Level level, ParticleOptions particle,
                                      Vec3 center, int rays, int particlesPerRay, double maxDist) {
        RandomSource random = level.random;
        for (int i = 0; i < rays; i++) {
            double angle = (Math.PI * 2 / rays) * i;
            double dx = Math.cos(angle);
            double dz = Math.sin(angle);
            for (int j = 1; j <= particlesPerRay; j++) {
                double dist = maxDist * j / particlesPerRay;
                double px = center.x + dx * dist;
                double py = center.y + random.nextDouble() * dist;
                double pz = center.z + dz * dist;
                level.addParticle(particle, px, py, pz, dx * 0.02, 0.01, dz * 0.02);
            }
        }
    }

}
