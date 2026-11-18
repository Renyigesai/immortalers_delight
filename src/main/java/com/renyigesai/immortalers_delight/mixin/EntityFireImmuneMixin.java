package com.renyigesai.immortalers_delight.mixin;

import com.renyigesai.immortalers_delight.api.IFearFireEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityFireImmuneMixin implements IFearFireEntity {

    @Unique
    private boolean immortalers_delight$fearFire = false;

    @Override
    public boolean immortalers_delight$isFearFire() {
        return this.immortalers_delight$fearFire;
    }

    @Override
    public void immortalers_delight$setFearFire(boolean fearFire) {
        this.immortalers_delight$fearFire = fearFire;
    }

    @Inject(method = "fireImmune", at = @At("HEAD"), cancellable = true)
    private void checkCustomFireImmunity(CallbackInfoReturnable<Boolean> cir) {
        if (this.immortalers_delight$fearFire) {
            cir.setReturnValue(false);
        }
    }

    // 保存实体时持久化
    @Inject(method = "saveWithoutId", at = @At("HEAD"))
    private void onSave(CompoundTag compound, CallbackInfoReturnable<CompoundTag> cir) {
        if (this.immortalers_delight$fearFire) {
            compound.putBoolean("immortalers_delight_fear_fire", true);
        }
    }

    // 加载实体时读取
    @Inject(method = "load", at = @At("HEAD"))
    private void onLoad(CompoundTag compound, CallbackInfo ci) {
        this.immortalers_delight$fearFire = compound.getBoolean("immortalers_delight_fear_fire");
    }
}
