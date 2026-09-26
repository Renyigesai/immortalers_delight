package com.renyigesai.immortalers_delight.mixin;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.api.IFearFireEntity;
import net.minecraft.commands.CommandSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityFireImmuneMixin extends net.minecraftforge.common.capabilities.CapabilityProvider<Entity> implements Nameable, EntityAccess, CommandSource, net.minecraftforge.common.extensions.IForgeEntity, IFearFireEntity {

    @Unique
    private boolean immortalers_delight$fearFire = false;

    protected EntityFireImmuneMixin(Class<Entity> baseClass) {super(baseClass);}
//
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
//
//
//    protected EntityFireImmuneMixin(Class<Entity> baseClass) {super(baseClass);}
//
//    protected EntityFireImmuneMixin(Class<Entity> baseClass, boolean isLazy) {super(baseClass, isLazy);}
//
//    @Inject(method = "fireImmune", at = @At("HEAD"), cancellable = true)
//    private void checkCustomFireImmunity(CallbackInfoReturnable<Boolean> cir) {
//        // 从实体的持久化NBT中读取自定义标记
//        if (this.getPersistentData().contains("immortalers_delight_fear_fire")) {
//            // 强制返回 false，即“不抗火”
//            cir.setReturnValue(false);
//        }
//        // 否则继续执行原方法（返回类型火抗）
//    }
//}
