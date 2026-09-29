package com.renyigesai.immortalers_delight.mixin;

import com.renyigesai.immortalers_delight.api.ILivingEntityExtension;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements ILivingEntityExtension {

    public LivingEntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    @Unique
    public void immDelight$cabuSubaperShunck() {
        super.baseTick();
    }

    @Unique
    private boolean immortalers_delight$ziWenGuiTian = false;

    @Override
    public boolean immortalers_delight$ziWenGuiTianing() {
        return this.immortalers_delight$ziWenGuiTian;
    }
    @Override
    public void immortalers_delight$ziWenGuiTian(boolean ziWen) {
        this.immortalers_delight$ziWenGuiTian = ziWen;
    }
    @Inject(method = "isDeadOrDying", at = @At("HEAD"), cancellable = true)
    public void isDeadOrDying(CallbackInfoReturnable<Boolean> cir) {
        if (this.immortalers_delight$ziWenGuiTian) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "isAlive", at = @At("HEAD"), cancellable = true)
    public void isAlive(CallbackInfoReturnable<Boolean> cir) {
        if (this.immortalers_delight$ziWenGuiTian) {
            cir.setReturnValue(false);
        }
    }

//
//    @Inject(method = "getHealth", at = @At("HEAD"), cancellable = true)
//    public void getHealth(CallbackInfoReturnable<Float> cir) {
//        if (this.getPersistentData().contains("immortalers_delight_zi_wen_gui_tian")) {
//            cir.setReturnValue(0f);
//        }
//    }


}
