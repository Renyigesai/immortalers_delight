package com.renyigesai.immortalers_delight.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.renyigesai.immortalers_delight.api.ILivingEntityExtension;
import com.renyigesai.immortalers_delight.potion.KeepFastMobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

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

    @Unique
    private MobEffectInstance immortalers_delight$effectBypassingApplicability;

    @Shadow
    public abstract boolean addEffect(MobEffectInstance effect, @Nullable Entity source);

    @Override
    public boolean immortalers_delight$addEffectWithoutApplicabilityCheck(MobEffectInstance effect, @Nullable Entity source) {
        MobEffectInstance previous = this.immortalers_delight$effectBypassingApplicability;
        this.immortalers_delight$effectBypassingApplicability = effect;
        try {
            return this.addEffect(effect, source);
        } finally {
            this.immortalers_delight$effectBypassingApplicability = previous;
        }
    }

    @WrapOperation(
            method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;canBeAffected(Lnet/minecraft/world/effect/MobEffectInstance;)Z")
    )
    private boolean immortalers_delight$checkEffectApplicability(LivingEntity entity, MobEffectInstance effect, Operation<Boolean> original) {
        if (this.immortalers_delight$effectBypassingApplicability == effect) {
            // Consume the bypass before addEffect posts callbacks that can apply other effects.
            this.immortalers_delight$effectBypassingApplicability = null;
            return true;
        }
        return original.call(entity, effect);
    }

    @Inject(method = "onEffectRemoved", at = @At("TAIL"))
    private void immortalers_delight$restoreKeepFastFood(MobEffectInstance effect, CallbackInfo ci) {
        if (!this.level().isClientSide() && (Object) this instanceof Player player && effect.getEffect() instanceof KeepFastMobEffect keepFast) {
            keepFast.restoreFood(player, effect.getAmplifier());
        }
    }

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
}
