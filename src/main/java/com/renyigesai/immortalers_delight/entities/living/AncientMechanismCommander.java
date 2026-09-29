package com.renyigesai.immortalers_delight.entities.living;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.tags.DamageTypeTags;

public class AncientMechanismCommander extends Monster {
    private static final EntityDataAccessor<Integer> ACTION = SynchedEntityData.defineId(AncientMechanismCommander.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ACTION_TICK = SynchedEntityData.defineId(AncientMechanismCommander.class, EntityDataSerializers.INT);
    private int action;
    private int actionTick;
    private int attack3Cooldown;
    private int attack4Cooldown;
    private int summonCooldown;
    private int shootCooldown;
    private int throwCooldown;
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState startAttackAnimationState = new AnimationState();
    public final AnimationState attack1AnimationState = new AnimationState();
    public final AnimationState attack2AnimationState = new AnimationState();
    public final AnimationState attack3AnimationState = new AnimationState();
    public final AnimationState attack4AnimationState = new AnimationState();
    public final AnimationState summonAnimationState = new AnimationState();
    public final AnimationState shootAnimationState = new AnimationState();
    public final AnimationState throwAnimationState = new AnimationState();
    public final AnimationState reviveAnimationState = new AnimationState();

    public AncientMechanismCommander(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(ACTION, 0);
        entityData.define(ACTION_TICK, 0);
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide) {
            updateClientAnimation();
            return;
        }
        setAggressive(getTarget() != null && getTarget().isAlive());
        if (attack3Cooldown > 0) attack3Cooldown--;
        if (attack4Cooldown > 0) attack4Cooldown--;
        if (summonCooldown > 0) summonCooldown--;
        if (shootCooldown > 0) shootCooldown--;
        if (throwCooldown > 0) throwCooldown--;
        if (action != 0) {
            actionTick++;
            if (actionTick == 8 || (action == 3 && (actionTick == 28 || actionTick == 48))) performMeleeAction();
            if (actionFinished()) setAction(0);
        } else if (getTarget() != null) {
            chooseSkill(getTarget());
        }
    }

    private void chooseSkill(LivingEntity target) {
        double distance = distanceToSqr(target);
        if (throwCooldown == 0 && distance > 16) { setAction(7); throwDynamite(target); throwCooldown = 60; }
        else if (shootCooldown == 0 && distance > 25) { setAction(6); shootAt(target); shootCooldown = 200; }
        else if (summonCooldown == 0 && random.nextInt(100) < 10) { setAction(5); summonSupport(); summonCooldown = 600; }
        else if (attack4Cooldown == 0 && getHealth() < getMaxHealth() * .5F && random.nextInt(100) < 8) { setAction(4); grantRavagerBuff(); attack4Cooldown = 1200; }
        else if (attack3Cooldown == 0 && distance < 16 && random.nextInt(100) < 15) { setAction(3); attack3Cooldown = 600; }
        else if (distance < 12 && random.nextInt(100) < 75) setAction(random.nextBoolean() ? 1 : 2);
    }

    private void setAction(int value) { action = value; actionTick = 0; entityData.set(ACTION, value); entityData.set(ACTION_TICK, tickCount); }
    private boolean actionFinished() { return switch (action) { case 1 -> actionTick > 30; case 2 -> actionTick > 27; case 3 -> actionTick > 56; case 4 -> actionTick > 40; case 5 -> actionTick > 80; case 6 -> actionTick > 198; case 7 -> actionTick > 28; default -> actionTick > 12; }; }

    private void updateClientAnimation() {
        int next = entityData.get(ACTION);
        if (next != action) { action = next; actionTick = 0; stopAnimations(); }
        if (next == 0) { if (!idleAnimationState.isStarted()) idleAnimationState.start(tickCount); }
        else if (next == 1 && !attack1AnimationState.isStarted()) attack1AnimationState.start(entityData.get(ACTION_TICK));
        else if (next == 2 && !attack2AnimationState.isStarted()) attack2AnimationState.start(entityData.get(ACTION_TICK));
        else if (next == 3 && !attack3AnimationState.isStarted()) attack3AnimationState.start(entityData.get(ACTION_TICK));
        else if (next == 4 && !attack4AnimationState.isStarted()) attack4AnimationState.start(entityData.get(ACTION_TICK));
        else if (next == 5 && !summonAnimationState.isStarted()) summonAnimationState.start(entityData.get(ACTION_TICK));
        else if (next == 6 && !shootAnimationState.isStarted()) shootAnimationState.start(entityData.get(ACTION_TICK));
        else if (next == 7 && !throwAnimationState.isStarted()) throwAnimationState.start(entityData.get(ACTION_TICK));
        else if (next == 8 && !reviveAnimationState.isStarted()) reviveAnimationState.start(entityData.get(ACTION_TICK));
    }
    private void stopAnimations() { idleAnimationState.stop(); startAttackAnimationState.stop(); attack1AnimationState.stop(); attack2AnimationState.stop(); attack3AnimationState.stop(); attack4AnimationState.stop(); summonAnimationState.stop(); shootAnimationState.stop(); throwAnimationState.stop(); reviveAnimationState.stop(); }

    private void throwDynamite(LivingEntity target) { var bomb = new com.renyigesai.immortalers_delight.entities.projectile.AncientMechanismDynamiteEntity(level(), this); bomb.setPos(getX(), getEyeY() - .2, getZ()); bomb.shoot(target.getX() - getX(), target.getEyeY() - getEyeY(), target.getZ() - getZ(), 0.8F, 0.2F); level().addFreshEntity(bomb); }
    private void shootAt(LivingEntity target) { for (int i = -1; i <= 1; i++) { Arrow arrow = new Arrow(EntityType.ARROW, level()); arrow.setOwner(this); arrow.setPos(getX(), getEyeY(), getZ()); arrow.shoot(target.getX() - getX(), target.getEyeY() - getEyeY(), target.getZ() - getZ(), 1.8F, 10.0F); level().addFreshEntity(arrow); } }
    private void summonSupport() { int count = 3 + random.nextInt(3); for (int i = 0; i < count; i++) { Raider mob = random.nextInt(100) < 65 ? new Pillager(EntityType.PILLAGER, level()) : new Vindicator(EntityType.VINDICATOR, level()); mob.moveTo(getX() + random.nextGaussian() * 2, getY(), getZ() + random.nextGaussian() * 2, random.nextFloat() * 360, 0); mob.setTarget(getTarget()); level().addFreshEntity(mob); } }
    private void grantRavagerBuff() { for (Raider raider : level().getEntitiesOfClass(Raider.class, new AABB(getX()-8, getY()-4, getZ()-8, getX()+8, getY()+4, getZ()+8))) { raider.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.DAMAGE_BOOST, 400, 1)); raider.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.DAMAGE_RESISTANCE, 400, 1)); } }
    private void performMeleeAction() {
        LivingEntity target = getTarget();
        if (target == null || distanceToSqr(target) > 16) return;
        float damage = (float)getAttributeValue(Attributes.ATTACK_DAMAGE) * (action == 1 ? 1.5F : 1.0F);
        if (action == 2) {
            for (LivingEntity victim : level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(3.0D))) {
                if (victim != this && hasLineOfSight(victim)) victim.hurt(damageSources().mobAttack(this), damage);
            }
        } else {
            target.hurt(damageSources().mobAttack(this), damage);
        }
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.35D, true));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 150.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.28D)
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.ARMOR, 6.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8D);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypeTags.IS_PROJECTILE)) {
            return false;
        }
        return super.hurt(source, amount * 0.5F);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.PILLAGER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.PILLAGER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PILLAGER_DEATH;
    }
}
