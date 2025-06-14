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

import static com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect.*;

public class InebriatedMobEffect extends MobEffect {
    public InebriatedMobEffect() {
        super(MobEffectCategory.HARMFUL, -39424);
    }

    @Override
    public void applyEffectTick(LivingEntity pEntity, int amplifier) {
        if (this == INEBRIATED.get() && !pEntity.level().isClientSide()) {
            int time = pEntity.hasEffect(INEBRIATED.get()) ? Objects.requireNonNull(pEntity.getEffect(INEBRIATED.get()).getDuration()):0;
            if (time > 3600) {
                boolean isPowerful = DifficultyModeHelper.isPowerBattleMode();
                float health = pEntity.getHealth();
                float damage = pEntity.getMaxHealth() * 0.08F > 1.6F ? pEntity.getMaxHealth() * 0.08F : 1.6F;
                boolean isOP = pEntity instanceof Player player && player.isCreative();
                if (!isOP || isPowerful) {
                    pEntity.invulnerableTime = 0;
                    pEntity.hurt(new DamageSource(pEntity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("immortalers_delight:drunk")))), damage);
                    pEntity.invulnerableTime = 0;
                    if (isPowerful && (health - damage) > 0 && pEntity.getHealth() > (health - damage)) {
                        pEntity.setHealth(health - damage);
                    }
                    pEntity.addEffect(new MobEffectInstance(WEAK_POISON.get(), 902, amplifier + 1));
                    pEntity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, time, amplifier));
                    pEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, time, amplifier));
                    pEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, time, amplifier));
                    pEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, time, amplifier));
                }
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        int j = 64 >> pAmplifier;
        if (j > 0) {
            return pDuration % j == 0;
        } else {
            return true;
        }
    }
}
