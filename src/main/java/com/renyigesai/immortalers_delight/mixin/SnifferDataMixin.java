package com.renyigesai.immortalers_delight.mixin;

import com.renyigesai.immortalers_delight.api.ISnifferSaddleData;
import com.renyigesai.immortalers_delight.api.SnifferAnimations;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Sniffer.class)
public abstract class SnifferDataMixin extends Animal implements ISnifferSaddleData {
    @Unique
    private static final EntityDataAccessor<Boolean> immortalersDelight$HAS_SADDLE = 
        SynchedEntityData.defineId(Sniffer.class, EntityDataSerializers.BOOLEAN);
    
    @Unique
    private static final EntityDataAccessor<Boolean> immortalersDelight$SADDLE_UPGRADED = 
        SynchedEntityData.defineId(Sniffer.class, EntityDataSerializers.BOOLEAN);
    
    protected SnifferDataMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }
    
    @Inject(method = "<init>", at = @At("RETURN"))
    private void immortalersDelight$initSnifferData(EntityType entityType, Level level, CallbackInfo ci) {
        this.entityData.define(SnifferAnimations.HAPPY_TICKS, 0);
        this.entityData.define(immortalersDelight$HAS_SADDLE, false);
        this.entityData.define(immortalersDelight$SADDLE_UPGRADED, false);
    }
    
    @Override
    public EntityDataAccessor<Boolean> immortalersDelight$getHasSaddleAccessor() {
        return immortalersDelight$HAS_SADDLE;
    }
    
    @Override
    public EntityDataAccessor<Boolean> immortalersDelight$getSaddleUpgradedAccessor() {
        return immortalersDelight$SADDLE_UPGRADED;
    }
}
