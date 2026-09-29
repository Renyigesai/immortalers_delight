package com.renyigesai.immortalers_delight.mixin;

import com.renyigesai.immortalers_delight.api.ISnifferSaddleData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class SnifferNBTMixin {
    
    @Inject(method = "load", at = @At("TAIL"))
    private void immortalersDelight$loadSnifferSaddle(CompoundTag tag, CallbackInfo ci) {
        Entity entity = (Entity)(Object)this;
        if (entity instanceof Sniffer sniffer && sniffer instanceof ISnifferSaddleData saddleData) {
            if (tag.contains("HasSaddle", Tag.TAG_BYTE)) {
                sniffer.getEntityData().set(saddleData.immortalersDelight$getHasSaddleAccessor(), tag.getBoolean("HasSaddle"));
            }
            if (tag.contains("SaddleUpgraded", Tag.TAG_BYTE)) {
                sniffer.getEntityData().set(saddleData.immortalersDelight$getSaddleUpgradedAccessor(), tag.getBoolean("SaddleUpgraded"));
            }
        }
    }
    
    @Inject(method = "saveWithoutId", at = @At("TAIL"))
    private void immortalersDelight$saveSnifferSaddle(CompoundTag tag, CallbackInfoReturnable<CompoundTag> cir) {
        Entity entity = (Entity)(Object)this;
        if (entity instanceof Sniffer sniffer && sniffer instanceof ISnifferSaddleData saddleData) {
            boolean hasSaddle = sniffer.getEntityData().get(saddleData.immortalersDelight$getHasSaddleAccessor());
            tag.putBoolean("HasSaddle", hasSaddle);
            
            boolean saddleUpgraded = sniffer.getEntityData().get(saddleData.immortalersDelight$getSaddleUpgradedAccessor());
            tag.putBoolean("SaddleUpgraded", saddleUpgraded);
        }
    }
}
