package com.renyigesai.immortalers_delight.mixin;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci){
//        Minecraft mc = Minecraft.getInstance();
//        if (mc.hitResult instanceof BlockHitResult blockHitResult) {
//            BlockPos pos = blockHitResult.getBlockPos();
//            BlockState state = mc.level.getBlockState(pos);
//            PlayerLookBlockEvent playerLookBlockEvent = new PlayerLookBlockEvent(mc.player, pos,state);
//            MinecraftForge.EVENT_BUS.post(playerLookBlockEvent);
//        }
        System.out.println("yes");
    }
}
