package com.renyigesai.immortalers_delight.potion;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

public class UpSideDownMobEffect extends MobEffect {
    public UpSideDownMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 12875208);
    }
    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level().isClientSide()) return;
        if (entity instanceof Player player) {
            if (player.isShiftKeyDown()) {
                if (player.hasEffect(MobEffects.LEVITATION)) player.removeEffect(MobEffects.LEVITATION);
                if (!player.hasEffect(MobEffects.SLOW_FALLING)) player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 20, amplifier));
            } else {
                if (player.hasEffect(MobEffects.SLOW_FALLING)) player.removeEffect(MobEffects.SLOW_FALLING);
                if (!player.hasEffect(MobEffects.LEVITATION)) player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 20, amplifier));
            }
        } else if (entity.tickCount % 5 == 0 && entity instanceof Mob mob) {
            LivingEntity target = mob.getTarget();
            if (target != null) {
                controlUpOrDown(mob,amplifier, (mob.getEyeY()) < target.position().y);
            }
        }
    }

    private void controlUpOrDown(LivingEntity entity, int amplifier, boolean up) {
        if (entity.level().isClientSide()) return;
        if (up) {
            if (entity.hasEffect(MobEffects.SLOW_FALLING)) entity.removeEffect(MobEffects.SLOW_FALLING);
            if (!entity.hasEffect(MobEffects.LEVITATION)) entity.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 20, amplifier));
        } else {
            if (entity.hasEffect(MobEffects.LEVITATION)) entity.removeEffect(MobEffects.LEVITATION);
            if (!entity.hasEffect(MobEffects.SLOW_FALLING)) entity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 20, amplifier));
        }
    }
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {return true; }
}
