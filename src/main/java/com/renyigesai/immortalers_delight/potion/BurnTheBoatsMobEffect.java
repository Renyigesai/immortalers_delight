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
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;

import java.util.Objects;

public class BurnTheBoatsMobEffect extends BaseMobEffect {
    public BurnTheBoatsMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 11829585);
    }

    @Override
    public boolean isDurationEffectTickInControl(int duration, int amplifier) {
        return true;
    }

    @EventBusSubscriber(
            modid = ImmortalersDelightMod.MODID)
    public static class BurnTheBoatsPotionEffect {
        @SubscribeEvent
        public static void onCreatureHurt(LivingDamageEvent.Pre evt) {
            boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
            LivingEntity hurtOne = evt.getEntity();
            MobEffectInstance burnTheBoats = hurtOne.getEffect(ImmortalersDelightMobEffect.BURN_THE_BOATS);
            if (burnTheBoats != null && burnTheBoats.getEffect() instanceof BaseMobEffect) {
                int lv = burnTheBoats.getAmplifier();
                int truthLv = ((BaseMobEffect) burnTheBoats.getEffect()).getTruthUsingAmplifier(lv);
                lv++;
                truthLv++;
                float workHealth = (hurtOne.getMaxHealth() * truthLv) / (2 * (truthLv + 1)) > 3 * (truthLv + 1) ? 3 * (truthLv + 1) : (hurtOne.getMaxHealth() * truthLv / (2 * (truthLv + 1)));
                if (isPowerful) {
                    if (hurtOne.getHealth() - evt.getNewDamage() < workHealth) {

                        int lvStrong = hurtOne.hasEffect(MobEffects.DAMAGE_BOOST) ? Objects.requireNonNull(hurtOne.getEffect(MobEffects.DAMAGE_BOOST)).getAmplifier() + 1 : 0;
                        int lvSpeed = hurtOne.hasEffect(MobEffects.MOVEMENT_SPEED)? Objects.requireNonNull(hurtOne.getEffect(MobEffects.MOVEMENT_SPEED)).getAmplifier()+1 :0;
                        int lvDigSpeed = hurtOne.hasEffect(MobEffects.DIG_SPEED)? Objects.requireNonNull(hurtOne.getEffect(MobEffects.DIG_SPEED)).getAmplifier()+1 :0;
                        int lvJump = hurtOne.hasEffect(MobEffects.JUMP)? Objects.requireNonNull(hurtOne.getEffect(MobEffects.JUMP)).getAmplifier()+1 :0;
                        MobEffectInstance strong = new MobEffectInstance(MobEffects.DAMAGE_BOOST,300, lvStrong >= lv * 3 ? lv * 3 : lvStrong + 2);
                        MobEffectInstance speed = new MobEffectInstance(MobEffects.MOVEMENT_SPEED,300, lvSpeed >= lv * 3 ? lv * 3 : lvSpeed + 2);
                        MobEffectInstance dig = new MobEffectInstance(MobEffects.DIG_SPEED,300, lvDigSpeed >= lv * 3 ? lv * 3 : lvDigSpeed + 2);
                        MobEffectInstance jump = new MobEffectInstance(MobEffects.JUMP,300, lvJump >= lv * 3 ? lv * 3 : lvJump + 2);
                        MobEffectInstance resist = new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,300, lv > 1 ? 3 : 2);
                        hurtOne.addEffect(strong);
                        hurtOne.addEffect(speed);
                        hurtOne.addEffect(dig);
                        hurtOne.addEffect(jump);
                        hurtOne.addEffect(resist);
                        //ImmortalersDelightMod.LOGGER.info("伤害来源是：" + Objects.requireNonNull(evt.getSource().getEntity()).getName().toString());
                        if (evt.getSource().getEntity()==null || !(evt.getSource().getEntity() instanceof LivingEntity) || evt.getSource().is(DamageTypeTags.BYPASSES_RESISTANCE)){
                            int lvRegen = hurtOne.hasEffect(MobEffects.REGENERATION)? Objects.requireNonNull(hurtOne.getEffect(MobEffects.REGENERATION)).getAmplifier()+1 :0;
                            MobEffectInstance regen = new MobEffectInstance(MobEffects.REGENERATION,100,(lvRegen > lv ? lv : lvRegen) + 1 );
                            hurtOne.addEffect(regen);
                        }
                    }
                } else if (hurtOne.getHealth() < workHealth) {
                    hurtOne.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST,300, 2 ));
                    hurtOne.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED,300, 2 ));
                    hurtOne.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED,300, 2 ));
                    hurtOne.addEffect(new MobEffectInstance(MobEffects.JUMP,300, 2 ));
                    hurtOne.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,300, 2 ));
                    if (evt.getSource().getEntity()==null || !(evt.getSource().getEntity() instanceof LivingEntity) || evt.getSource().is(DamageTypeTags.BYPASSES_RESISTANCE)){
                        hurtOne.addEffect(new MobEffectInstance(MobEffects.REGENERATION,100,1));
                    }
                }
            }
        }

        @SubscribeEvent
        public static void onCreatureLostHealth(LivingDamageEvent.Pre evt) {
            boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
            LivingEntity hurtOne = evt.getEntity();
            MobEffectInstance burnTheBoats = hurtOne.getEffect(ImmortalersDelightMobEffect.BURN_THE_BOATS);
            if (isPowerful && burnTheBoats != null && burnTheBoats.getEffect() instanceof BaseMobEffect) {
                int lv = burnTheBoats.getAmplifier();
                int truthLv = ((BaseMobEffect) burnTheBoats.getEffect()).getTruthUsingAmplifier(lv);
                for (int i = 0; i < lv; i++) {
                    evt.setNewDamage(evt.getNewDamage() * 0.8f);
                }
                lv++;
                float workHealth = (hurtOne.getMaxHealth() * truthLv) / (2 * (truthLv + 1)) > 3 * (truthLv + 1) ? 3 * (truthLv + 1) : (hurtOne.getMaxHealth() * truthLv / (2 * (truthLv + 1)));
                if (hurtOne.getHealth() >= workHealth && hurtOne.getHealth() < evt.getNewDamage()) {
                    hurtOne.removeEffect(ImmortalersDelightMobEffect.BURN_THE_BOATS);
                    evt.setNewDamage(0);
                    hurtOne.setHealth(workHealth);
                }
            }
        }
    }
}
