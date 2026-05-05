package com.renyigesai.immortalers_delight.mixin;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ItemEntity.class, remap = false)
public abstract class ItemEntityMixin extends Entity implements TraceableEntity {

    public ItemEntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    /** 冰姣果不会被仙人掌摧毁 */
    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true, remap = false)
    private void immortalers$hurt(DamageSource pSource, float pAmount, CallbackInfoReturnable<Boolean> cir) {
        ItemEntity self = (ItemEntity) (Object) this;
        if (self.getItem().is(ImmortalersDelightItems.GELPITAYA.get()) && pSource.is(DamageTypes.CACTUS)) {
            cir.setReturnValue(false);
        }
    }
}
