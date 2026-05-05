package com.renyigesai.immortalers_delight.potion;
import net.neoforged.fml.common.EventBusSubscriber;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;

public class DeepnessMobEffect extends BaseMobEffect {
    public DeepnessMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 955261);
    }

    @Override
    public void applyEffectTickInControl(LivingEntity pEntity, int amplifier) {
        if (DifficultyModeUtil.isPowerBattleMode() && !pEntity.hasEffect(MobEffects.WEAKNESS)) {
            float buffer = (float) (0.9675*Math.exp(-0.0372*(amplifier+1)));
            pEntity.heal(pEntity.getMaxHealth() * (1 - buffer) * 0.1f);
        }
    }
    @Override
    public boolean isDurationEffectTickInControl(int duration, int amplifier) {
        return duration % 20 == 0;
    }

    @EventBusSubscriber(
            modid = ImmortalersDelightMod.MODID)
    public static class DeepnessPotionEffect {
        @SubscribeEvent
        public static void onCreatureHurt(LivingDamageEvent.Pre evt) {
            if (evt.getSource().is(DamageTypeTags.BYPASSES_RESISTANCE)) {
                return;
            }
            LivingEntity hurtOne = evt.getEntity();
            LivingEntity attacker = null;
            if (evt.getSource().getEntity() instanceof LivingEntity livingEntity){
                attacker = livingEntity;
            }

            if (!hurtOne.level().isClientSide) {
                if (attacker != null){
                    float damage = evt.getNewDamage();
                    MobEffectInstance thsEffect = hurtOne.getEffect(ImmortalersDelightMobEffect.DEEPNESS);
                    if (thsEffect != null && thsEffect.getEffect() instanceof BaseMobEffect baseMobEffect){
                        int lv = baseMobEffect.getTruthUsingAmplifier(thsEffect.getAmplifier());
                        attacker.addEffect(new MobEffectInstance(MobEffects.WEAKNESS,40 + thsEffect.getAmplifier() * 20,thsEffect.getAmplifier()));
                        if (DifficultyModeUtil.isPowerBattleMode() && attacker.hasEffect(MobEffects.WEAKNESS)) {
                            lv++;
                            float buffer = 1.0F;
                            for (int i = 0; i < lv; i++) {
                                buffer  = buffer * 0.8F;
                            }
                            evt.setNewDamage(Math.min(damage*buffer, damage));
                        }
                    }
                }
            }
        }
    }
}
