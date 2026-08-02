package com.renyigesai.immortalers_delight.mixin;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.commands.CommandSource;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityFireImmuneMixin extends net.minecraftforge.common.capabilities.CapabilityProvider<Entity> implements Nameable, EntityAccess, CommandSource, net.minecraftforge.common.extensions.IForgeEntity {


    protected EntityFireImmuneMixin(Class<Entity> baseClass) {super(baseClass);}

    protected EntityFireImmuneMixin(Class<Entity> baseClass, boolean isLazy) {super(baseClass, isLazy);}

    @Inject(method = "fireImmune", at = @At("HEAD"), cancellable = true)
    private void checkCustomFireImmunity(CallbackInfoReturnable<Boolean> cir) {
        // 从实体的持久化NBT中读取自定义标记
        if (this.getPersistentData().contains("immortalers_delight_fear_fire")) {
            // 强制返回 false，即“不抗火”
            cir.setReturnValue(false);
        }
        // 否则继续执行原方法（返回类型火抗）
    }
}
