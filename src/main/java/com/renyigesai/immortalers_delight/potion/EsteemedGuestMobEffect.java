package com.renyigesai.immortalers_delight.potion;

import com.renyigesai.immortalers_delight.event.DifficultyModeHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;

import static com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect.INEBRIATED;
import static com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect.WEAK_POISON;

public class EsteemedGuestMobEffect extends MobEffect {
    public EsteemedGuestMobEffect() {
        super(MobEffectCategory.HARMFUL, -39424);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
            return true;
    }
}
