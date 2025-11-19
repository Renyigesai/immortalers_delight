package com.renyigesai.immortalers_delight.potion;

import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

@Mod.EventBusSubscriber
public class PrehistoricPowersPotionEffect {
    @SubscribeEvent
    public static void onCreatureHurt(LivingHurtEvent evt) {
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
            if (attacker.hasEffect(ImmortalersDelightMobEffect.PREHISTORIC_POWERS.get()) && attacker.hasEffect(MobEffects.DAMAGE_BOOST)){
                int lvStrong = Objects.requireNonNull(attacker.getEffect(MobEffects.DAMAGE_BOOST)).getAmplifier();
                int lv = Objects.requireNonNull(attacker.getEffect(ImmortalersDelightMobEffect.PREHISTORIC_POWERS.get())).getAmplifier();
                float d0 = evt.getAmount();
                float d1 = 1.3F;
                int n = (isPowerful && lv > 0 ? 1 : 0) + (lv > lvStrong ? lvStrong : lv);
                for (int i = 0; i < n; i++) {
                    d1 *= !(attacker instanceof Player) ? d1 : 1.3F;
                }
                if (isPowerful) ++d1;
                float dn = d0 * d1 + (d1-1)/0.3F;
                System.out.println("n=" + n + " d1=" + d1);
                evt.setAmount(dn);
            }
        }
    }
}
