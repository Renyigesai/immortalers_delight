package com.renyigesai.immortalers_delight.mixin;

import com.renyigesai.immortalers_delight.item.weapon.PlaceableShieldItem;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** {@code disableShield} is declared on {@link Player}, not on {@link net.minecraft.server.level.ServerPlayer}. */
@Mixin(Player.class)
public abstract class PlayerMixin {

    @Inject(method = "disableShield", at = @At("HEAD"), cancellable = true, remap = false)
    private void immortalers$disableShield(CallbackInfo ci) {
        Player self = (Player) (Object) this;
        if (self.getUseItem().getItem() instanceof PlaceableShieldItem) {
            ci.cancel();
        }
    }
}
