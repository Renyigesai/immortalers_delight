package com.renyigesai.immortalers_delight.entities.living;

import com.renyigesai.immortalers_delight.api.mobbase.AntiCheesedMob;
import com.renyigesai.immortalers_delight.entities.ai.ArmorSpiritAttackGoal;
import com.renyigesai.immortalers_delight.entities.ai.StrangeArmourStandHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.ClimbOnTopOfPowderSnowGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class StrangeArmourStand extends ArmetSpiritBase implements AntiCheesedMob {
    private int hurtTimeOut;

    public StrangeArmourStand(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(1, new ClimbOnTopOfPowderSnowGoal(this, this.level()));
        this.goalSelector.addGoal(4, new ArmorSpiritAttackGoal(this, 1.5D, true));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 320.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.24D)
                .add(Attributes.ATTACK_DAMAGE, 16.0D)
                .add(Attributes.ARMOR, 2.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE,0.2D);
    }

    private void setUpAnimationState(){
        if (this.idleAnimationTimeOut <= 0){
            this.idleAnimationTimeOut = this.random.nextInt(40) + 80;
            this.idleAnimationState.start(this.tickCount);
        }else {
            --this.idleAnimationTimeOut;
        }
        if (this.isAggressive() && this.attackAnimationTimeOut <= 0){
            attackAnimationTimeOut = 40;
            attackAnimationState.start(this.tickCount);
        }else {
            --this.attackAnimationTimeOut;
        }
        if (!this.isAggressive()){
            attackAnimationState.stop();
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            setUpAnimationState();
        }
    }

    /**
     *
     */
    @Override
    protected void updateWalkAnimation(float pPartialTick) {
        float f;
        if (this.getPose() == Pose.STANDING) {
            f = Math.min(pPartialTick * 6.0F, 1.0F);
        } else {
            f = 0.0F;
        }

        this.walkAnimation.update(f, 0.2F);
    }

    @Override
    public void setHealth(float health) {
        if (health <= this.getHealth()) {
            if (this.canLoseHealth()) StrangeArmourStandHelper.setStrangeArmourStandLost(this, 6);
            else return;
        }
        super.setHealth(health);
    }
    @Override
    public boolean canBeHurt() {
        return this.hurtTimeOut >= 10;
    }

    @Override
    public boolean canLoseHealth() {
        if (this.level().isClientSide()) return false;
        return StrangeArmourStandHelper.isStrangeArmourStandLost( this.level(), this.getUUID());
    }

    @Override
    public float getAttackProportion() {
        return 0;
    }

    @Override
    public float getMinDamage() {
        return 0;
    }

    @Override
    public int getDamageDivisor() {
        return 0;
    }

    @Override
    public boolean shouldBlastDamageHurt(LivingEntity target) {
        return false;
    }
}
