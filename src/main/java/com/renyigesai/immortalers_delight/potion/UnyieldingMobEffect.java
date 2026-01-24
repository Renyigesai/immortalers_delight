package com.renyigesai.immortalers_delight.potion;

import com.renyigesai.immortalers_delight.Config;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.potion.immortaleffects.DeathlessEffect;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import com.renyigesai.immortalers_delight.util.task.TimekeepingTask;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class UnyieldingMobEffect extends MobEffect {
    public UnyieldingMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 11123525);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level().isClientSide){
            if (entity.hurtTime > 0) entity.deathTime = -2;
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Mod.EventBusSubscriber(
            modid = ImmortalersDelightMod.MODID,
            bus = Mod.EventBusSubscriber.Bus.FORGE
    )
    public static class UnyieldingEvents {
        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void addUnDamageTime(LivingDamageEvent event) {
            LivingEntity hurtOne = event.getEntity();
            if (hurtOne.level().isClientSide()) return;
            MobEffectInstance unyielding = hurtOne.getEffect(ImmortalersDelightMobEffect.UNYIELDING.get());
            if (unyielding != null) {
                int lv = unyielding.getAmplifier();
                boolean isPowered = DifficultyModeUtil.isPowerBattleMode();
                if (isPowered || event.getSource().getEntity() != null) {
                    DeathlessEffect.applyImmortalEffect(hurtOne,(isPowered ? 24 : 12) + 6 * lv, lv);
                }
            }
        }

    }
}
