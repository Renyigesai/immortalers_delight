package com.renyigesai.immortalers_delight.potion;

import com.renyigesai.immortalers_delight.event.DifficultyModeHelper;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

@Mod.EventBusSubscriber
public class PrehistoricPowersPotionEffect {
    public static boolean isPowerful = DifficultyModeHelper.isPowerBattleMode();
    @SubscribeEvent
    public static void onCreatureHurt(LivingHurtEvent evt) {
        if (evt.isCanceled() || evt.getSource().is(DamageTypeTags.BYPASSES_EFFECTS)) {
            return;
        }
        LivingEntity hurtOne = evt.getEntity();
        LivingEntity attacker = null;

        if (evt.getSource().getEntity() instanceof LivingEntity livingEntity){
            attacker = livingEntity;
        }

        if (!hurtOne.level().isClientSide && attacker != null) {
            if (attacker.hasEffect(ImmortalersDelightMobEffect.PREHISTORIC_POWERS.get()) && attacker.hasEffect(MobEffects.DAMAGE_BOOST)){
                int lvStrong = hurtOne.hasEffect(MobEffects.DAMAGE_BOOST) ? Objects.requireNonNull(hurtOne.getEffect(MobEffects.DAMAGE_BOOST)).getAmplifier() + 1 : 0;
                int lv = attacker.getEffect(ImmortalersDelightMobEffect.PREHISTORIC_POWERS.get()).getAmplifier();
                float d0 = evt.getAmount();
                float d1 = 1.3F;
                int n = (isPowerful ? 1 : 0) + (lv > lvStrong ? lvStrong : lv);
                for (int i = 0; i < n; i++) {
                    d1 *= d1;
                }
                float dn = d0 * d1 + (d1-(isPowerful ? 0 : 1))/0.3F;
                if (isPowerful) {dn += d0;}
                evt.setAmount(dn);
            }
        }
    }
}
