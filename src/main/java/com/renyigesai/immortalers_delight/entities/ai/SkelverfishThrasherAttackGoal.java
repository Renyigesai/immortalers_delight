package com.renyigesai.immortalers_delight.entities.ai;

import com.renyigesai.immortalers_delight.entities.living.ArmetSpiritBase;
import com.renyigesai.immortalers_delight.entities.living.SkelverfishThrasher;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class SkelverfishThrasherAttackGoal extends MeleeAttackGoal {
    private final int ANIMATION_DURATION = 10;//动画时长
    private final int ATTACK_DELAY = 3;//攻击前摇,希望在动画开始后的第几帧造成伤害
    private final int ATTACK_INTERVAL = 20;//攻击间隔，不应该短于动画时长，原版默认的攻击间隔为20
    private final SkelverfishThrasher entity;//限定实体，用于调用动画状态
    private int attackTimer = 0;//攻击计时，在攻击动画开始时开始计时，表征目前攻击进行到哪一刻
    private boolean shouldCountAttackTiming = false;//是否应该进行攻击计时
    private int cooldown = ATTACK_INTERVAL;//攻击冷却，用于实际的冷却计时
    private boolean shouldCountTillNextAttack = false;//是否应该进行攻击冷却计时

    /**
     * 构造函数，初始化 SkelverfishThrasherAttackGoal 实例。
     *
     * @param pMob                     执行攻击行为的 PathfinderMob 实体
     * @param pSpeedModifier           实体移动到目标时的速度修正值
     * @param pFollowingTargetEvenIfNotSeen  是否在看不到目标时仍然跟随目标
     */
    public SkelverfishThrasherAttackGoal(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
        super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        this.entity = (SkelverfishThrasher) pMob;
    }

    /**
     * 当目标开始执行时调用此方法，重置攻击计时和攻击冷却。
     */
    @Override
    public void start() {
        super.start();
        this.attackTimer = 0;
        this.cooldown = ATTACK_DELAY;
        this.mob.setAggressive(false);
    }

    /**
     * 每游戏刻调用一次此方法，用于更新计时与结束攻击动画。
     */
    @Override
    public void tick() {
        super.tick();
        if (shouldCountTillNextAttack) {
            this.cooldown = this.cooldown - 1 > 0 ? this.cooldown - 1 : 0;
        }
        //处理攻击计时，如果攻击计时已满，则将攻击计时重置为0，结束攻击动画。
        if (shouldCountAttackTiming) {
            if (this.attackTimer != ANIMATION_DURATION) {
                this.attackTimer = this.attackTimer + 1 > ANIMATION_DURATION ? ANIMATION_DURATION : this.attackTimer + 1;
            }else {
                shouldCountAttackTiming = false;
                this.attackTimer = 0;
                entity.setAggressive(false);
            }
        }
    }

    /**
     * 当目标停止执行时调用此方法，将实体的攻击状态设置为非攻击状态。
     */
    @Override
    public void stop() {
        super.stop();
        entity.setAggressive(false);
    }

    /**
     * 检查并执行攻击行为的核心方法。
     *这个方法在super类中被调用，每tick执行
     * 注意这里只开启攻击动画，结束攻击动画因为不依赖索敌所以放在tick方法中处理。
     * @param target         攻击目标实体
     * @param squaredDistance  与目标的平方距离
     */
    @Override
    protected void checkAndPerformAttack(@NotNull LivingEntity target, double squaredDistance) {
        //如果实体在攻击范围内，启动冷却计时。
        if (isEnemyWithinAttackDistanse(target, squaredDistance)) {
            shouldCountTillNextAttack = true;
            //是否冷却完毕？是则启动攻击动画与攻击计时
            if (isTimeToStartAttackAnimation()) {
                entity.setAggressive(true);
                shouldCountAttackTiming = true;
            }
            //是否攻击前摇完毕？是则进行攻击，重置攻击间隔。
            if (delayedEnough()) {
                this.mob.getLookControl().setLookAt(target.getX(), target.getY(), target.getZ());
                preformAttack(target);
                resetAttackCoolDown();
            }
        } else {
            //目标不在范围内，重置冷却计时。
            resetAttackCoolDown();
            shouldCountTillNextAttack = false;
        }
    }

    /**
     * 重置攻击冷却时间，将冷却时间设置为攻击前摇的两倍。
     */
    private void resetAttackCoolDown() {
        this.cooldown = this.adjustedTickDelay(ATTACK_INTERVAL);
    }

    /**
     * 执行攻击行为，对攻击目标进行伤害。
     * @param target  攻击目标实体
     */
    private void preformAttack(LivingEntity target) {
        this.resetAttackCoolDown();
        this.mob.swing(InteractionHand.MAIN_HAND);
        this.mob.doHurtTarget(target);
    }

    /**
     * 检查是否已前摇完毕，可以实际造成伤害
     * @return 如果已前摇完毕，则返回 true；否则返回 false。
     */
    private boolean delayedEnough() {
        return this.attackTimer == ATTACK_DELAY;
    }

    /**
     * 检查是否已冷却完毕，可以开始攻击动画
     * @return 如果已冷却完毕，则返回 true；否则返回 false。
     */
    private boolean isTimeToStartAttackAnimation() {
        return this.cooldown <= 0;
    }

    /**
     * 检查攻击目标是否在攻击范围内。
     * @param target  攻击目标实体
     * @param squaredDistance  与目标的平方距离
     * @return 如果攻击目标在攻击范围内，则返回 true；否则返回 false。
     */
    private boolean isEnemyWithinAttackDistanse(LivingEntity target, double squaredDistance) {
        return squaredDistance <= this.getAttackReachSqr(target);
    }
    @Override
    protected double getAttackReachSqr(@NotNull LivingEntity pAttackTarget) {
        return (double)(4.0F + super.getAttackReachSqr(pAttackTarget) + pAttackTarget.getBbWidth());
    }
}

