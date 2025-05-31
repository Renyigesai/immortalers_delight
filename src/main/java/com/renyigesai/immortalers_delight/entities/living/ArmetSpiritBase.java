package com.renyigesai.immortalers_delight.entities.living;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class ArmetSpiritBase extends Monster {
    private static final EntityDataAccessor<Boolean> IS_ATTACKING = SynchedEntityData.defineId(ArmetSpiritBase.class, EntityDataSerializers.BOOLEAN);
    public static final AnimationState idleAnimationState = new AnimationState();
    public int idleAnimationTimeOut = 0;
    public static final AnimationState attackAnimationState = new AnimationState();
    public int attackAnimationTimeOut = 0;

    protected ArmetSpiritBase(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_ATTACKING, false);
    }
    @Override
    public boolean isAggressive() {
        return this.entityData.get(IS_ATTACKING);
    }

    @Override
    public void setAggressive(boolean isAttacking) {
        this.entityData.set(IS_ATTACKING, isAttacking);
    }
}
