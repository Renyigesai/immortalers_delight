package com.renyigesai.immortalers_delight.entities.ai;

import com.renyigesai.immortalers_delight.entities.living.ArmetSpiritBase;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class ArmorSpiritAttackGoal extends MeleeAttackGoal {
    private final ArmetSpiritBase entity;
    private int attackDelay = 20;//攻击前摇
    private int cooldown = 20;//攻击间隔
    private boolean shouldCountTillNextAttack = false;
    public ArmorSpiritAttackGoal(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
        super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        this.entity = (ArmetSpiritBase) pMob;
    }

    @Override
    public void start() {
        super.start();
        this.attackDelay = 20;
        this.cooldown = 20;
    }

    @Override
    public void tick() {
        super.tick();
        if (shouldCountTillNextAttack) {
            this.cooldown = this.cooldown - 1 > 0 ? this.cooldown - 1 : 0;
            System.out.println("这里是盔甲架的攻击AI，当前攻击前摇为：" + this.attackDelay + ", 攻击后摇为：" + this.cooldown);
        }
    }

    @Override
    public void stop() {
        super.stop();
        entity.setAggressive(false);
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity target, double squaredDistance) {
        if (isEnemyWithinAttackDistanse(target, squaredDistance)) {
            shouldCountTillNextAttack = true;

            if (isTimeToStartAttackAnimation()) {
                entity.setAggressive(true);
            }
            if (isCooledDown()) {
                this.mob.getLookControl().setLookAt(target.getX(), target.getY(), target.getZ());
                preformAttack(target);
            }
        } else {
            resetAttackCoolDown();
            shouldCountTillNextAttack = false;
            entity.setAggressive(false);
            entity.attackAnimationTimeOut = 0;
        }
    }


    private void resetAttackCoolDown() {
        this.cooldown = this.adjustedTickDelay(attackDelay * 2);
    }

    private void preformAttack(LivingEntity target) {
        this.resetAttackCoolDown();
        this.mob.swing(InteractionHand.MAIN_HAND);
        this.mob.doHurtTarget(target);
    }

    private boolean isCooledDown() {
        return this.cooldown <= 0;
    }

    private boolean isTimeToStartAttackAnimation() {
        return this.cooldown <= attackDelay;
    }

    private boolean isEnemyWithinAttackDistanse(LivingEntity target, double squaredDistance) {
        return squaredDistance <= 4.0 + this.getAttackReachSqr(target);
    }
}
