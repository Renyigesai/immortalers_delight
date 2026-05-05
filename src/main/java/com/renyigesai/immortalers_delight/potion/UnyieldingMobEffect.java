package com.renyigesai.immortalers_delight.potion;
import net.neoforged.fml.common.EventBusSubscriber;

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
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;

public class UnyieldingMobEffect extends BaseMobEffect {
    public UnyieldingMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 11123525);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level().isClientSide()){
            if (entity.hurtTime > 0) entity.deathTime = -2;
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    //无敌效果其实没有等级之分，所以在这里设置
    @EventBusSubscriber(
            modid = ImmortalersDelightMod.MODID)
    public static class UnyieldingEvents {
        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void addUnDamageTime(LivingDamageEvent.Pre event) {
            LivingEntity hurtOne = event.getEntity();
            if (hurtOne.level().isClientSide()) return;
            MobEffectInstance unyielding = hurtOne.getEffect(ImmortalersDelightMobEffect.UNYIELDING);
            if (unyielding != null && unyielding.getEffect() instanceof BaseMobEffect effect) {
                int lv = effect.getTruthUsingAmplifier(unyielding.getAmplifier());
                boolean isPowered = DifficultyModeUtil.isPowerBattleMode();
                if (isPowered || event.getSource().getEntity() != null) {
                    DeathlessEffect.applyImmortalEffect(hurtOne,(isPowered ? 24 : 12) + 6 * lv, lv);
                }
            }
        }

    }
}
