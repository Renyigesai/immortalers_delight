package com.renyigesai.immortalers_delight.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.entity.CatRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Cat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(CatRenderer.class)
public class CatRendererMixin {
    @Inject(method = "getTextureLocation(Lnet/minecraft/world/entity/animal/Cat;)Lnet/minecraft/resources/ResourceLocation;",
            at = @At(value = "HEAD"),locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void getTextureLocation(Cat pEntity, CallbackInfoReturnable<ResourceLocation> cir){
        String s = ChatFormatting.stripFormatting(pEntity.getName().getString());
        if ("之之".equals(s)){
            cir.setReturnValue(new ResourceLocation("minecraft","textures/entity/cat/zizi.png"));
            cir.cancel();
        }

    }
}
