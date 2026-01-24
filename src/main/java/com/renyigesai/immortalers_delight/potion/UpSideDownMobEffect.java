package com.renyigesai.immortalers_delight.potion;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.potion.immortaleffects.DeathlessEffect;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class UpSideDownMobEffect extends MobEffect {
    public UpSideDownMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 12875208);
    }
    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level().isClientSide()) return;
        if (entity instanceof Player player) {
            if (player.isShiftKeyDown()) {
                MobEffectInstance levitation = player.getEffect(MobEffects.LEVITATION);
                if (levitation != null && levitation.getAmplifier() <= amplifier) player.removeEffect(MobEffects.LEVITATION);
                if (player.tickCount % 20 == 0 || !player.hasEffect(MobEffects.SLOW_FALLING)) player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 25, 0));
            } else {
                if (player.hasEffect(MobEffects.SLOW_FALLING)) player.removeEffect(MobEffects.SLOW_FALLING);
                if (player.tickCount % 20 == 0 || !player.hasEffect(MobEffects.LEVITATION)) player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 25, 0));
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
            if (!entity.hasEffect(MobEffects.LEVITATION)) entity.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 20, 0));
        } else {
            if (entity.hasEffect(MobEffects.LEVITATION)) entity.removeEffect(MobEffects.LEVITATION);
            if (!entity.hasEffect(MobEffects.SLOW_FALLING)) entity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 20, 0));
        }
    }
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {return true; }


    @Mod.EventBusSubscriber(
            modid = ImmortalersDelightMod.MODID,
            bus = Mod.EventBusSubscriber.Bus.FORGE
    )
    public static class UpSideDownEvents {
        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void addJumpSpeed(LivingEvent.LivingJumpEvent event) {
            LivingEntity entity = event.getEntity();
            MobEffectInstance thisEffect = entity.getEffect(ImmortalersDelightMobEffect.UP_SIDE_DOWN.get());
            if (thisEffect != null && !entity.level().isClientSide()) {
                int lv = 1;
                MobEffectInstance jumpEffect = entity.getEffect(MobEffects.JUMP);
                if (jumpEffect != null) lv += jumpEffect.getAmplifier();
                float vx = (jumpEffect != null ? 1.0F : 0.1F) + 0.1f * (float)lv;
                float vy = 0.2F;
                float f = entity.getYRot() * ((float)Math.PI / 180F);
                entity.setDeltaMovement(entity.getDeltaMovement().add((double)(-Mth.sin(f) * vx), (double)vy, (double)(Mth.cos(f) *vx)));
                entity.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 15, thisEffect.getAmplifier() + 1));
            }
        }

    }
}
