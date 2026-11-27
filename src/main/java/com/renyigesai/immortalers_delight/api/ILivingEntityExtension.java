package com.renyigesai.immortalers_delight.api;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;

public interface ILivingEntityExtension {
    //这个方法用魔女文拼写，意为callSuperTick，用于直接调用Entity.tick

    @Unique
    void immDelight$cabuSubaperShunck();

    boolean immortalers_delight$ziWenGuiTianing();
    void immortalers_delight$ziWenGuiTian(boolean ziWen);

    boolean immortalers_delight$addEffectWithoutApplicabilityCheck(MobEffectInstance effect, @Nullable Entity source);
}
