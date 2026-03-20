package com.renyigesai.immortalers_delight.mixin;

import com.renyigesai.immortalers_delight.item.weapon.PlaceableShieldItem;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {


    protected PlayerMixin(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "disableShield" ,at = @At("HEAD"), cancellable = true)
    public void disableShield(boolean pBecauseOfAxe, CallbackInfo ci) {
        if (this.getUseItem().getItem() instanceof PlaceableShieldItem) {
            ci.cancel();
        }
    }
}
