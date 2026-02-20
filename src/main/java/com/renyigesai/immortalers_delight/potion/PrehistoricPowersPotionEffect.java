package com.renyigesai.immortalers_delight.potion;

import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

@Mod.EventBusSubscriber
public class PrehistoricPowersPotionEffect {
    @SubscribeEvent
    public static void onCreatureHurt(LivingDamageEvent evt) {
        if (evt.isCanceled() || evt.getSource().is(DamageTypeTags.BYPASSES_EFFECTS)) {
            return;
        }
        LivingEntity hurtOne = evt.getEntity();
        LivingEntity attacker = null;
        boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
        if (evt.getSource().getEntity() instanceof LivingEntity livingEntity){
            attacker = livingEntity;
        }

        if (!hurtOne.level().isClientSide && attacker != null) {
            MobEffectInstance powers = attacker.getEffect(ImmortalersDelightMobEffect.PREHISTORIC_POWERS.get());
            MobEffectInstance strength = attacker.getEffect(MobEffects.DAMAGE_BOOST);
            if (powers != null && strength != null){
                int lv = Math.min(powers.getAmplifier() + 1, strength.getAmplifier() + 1);
                if (isPowerful) lv *= 2;
                double damage = (Math.pow(1.3,lv) - 1)/0.3;
                evt.setAmount(evt.getAmount() + (float)damage);
            }
        }
    }
}
