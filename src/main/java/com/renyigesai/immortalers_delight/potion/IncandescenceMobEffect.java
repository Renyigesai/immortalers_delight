package com.renyigesai.immortalers_delight.potion;
import net.neoforged.fml.common.EventBusSubscriber;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;

public class IncandescenceMobEffect extends BaseMobEffect {

    public IncandescenceMobEffect() {
        super(MobEffectCategory.BENEFICIAL, -39424);
    }

    @Override
    public boolean isDurationEffectTickInControl(int duration, int amplifier) {
        return true;
    }

    @EventBusSubscriber(
            modid = ImmortalersDelightMod.MODID)
    public static class IncandescencePotionEffect {
        @SubscribeEvent
        public static void onUseItemFinish(LivingEntityUseItemEvent.Finish event) {
            if (event != null && event.getEntity() != null) {
                ItemStack stack = event.getResultStack();
                LivingEntity entity = event.getEntity();
                if (stack.getFoodProperties(entity) != null && noIncandescenceFood(stack, entity)) {
                    MobEffectInstance thisEffect = entity.getEffect(ImmortalersDelightMobEffect.INCANDESCENCE);
                    if (thisEffect != null && thisEffect.getEffect().value() instanceof BaseMobEffect baseMobEffect) {
                        int lv = baseMobEffect.getTruthUsingAmplifier(thisEffect.getAmplifier());
                        MobEffectInstance effectInstance = entity.getEffect(MobEffects.DAMAGE_BOOST);
                        int time = effectInstance != null ? effectInstance.getDuration() : 0;

                        FoodProperties food = stack.getFoodProperties(entity);
                        if (food != null) {
                            int nutrition = food.nutrition();
                            float saturation = food.saturation();
                            float foodValue = nutrition * 2.0f * saturation + nutrition;
                            time += foodValue * 20 * (lv <= 3 ? (4 - lv) : 1);
                            entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, time + 100, thisEffect.getAmplifier()));
                        }
                    }

                }
            }
        }

        @SubscribeEvent
        public static void onCreatureHurt(LivingDamageEvent.Pre evt) {
            if (evt.getSource().is(DamageTypeTags.BYPASSES_RESISTANCE)) {
                return;
            }
            LivingEntity hurtOne = evt.getEntity();
            LivingEntity attacker = null;
            boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
            if (evt.getSource().getEntity() instanceof LivingEntity livingEntity){
                attacker = livingEntity;
            }

            if (!hurtOne.level().isClientSide) {
                if (attacker != null && isPowerful){
                    MobEffectInstance thisEffect = attacker.getEffect(ImmortalersDelightMobEffect.INCANDESCENCE);
                    if (thisEffect != null && thisEffect.getEffect().value() instanceof BaseMobEffect baseMobEffect) {
                        int lv = baseMobEffect.getTruthUsingAmplifier(thisEffect.getAmplifier());

                        if (hurtOne.hasEffect(MobEffects.FIRE_RESISTANCE)) hurtOne.removeEffect(MobEffects.FIRE_RESISTANCE);

                        hurtOne.setRemainingFireTicks((lv+1) * 700 - 200);
                        if (attacker instanceof Player player) {
                            FoodData foodData = player.getFoodData();
                            if (foodData.getSaturationLevel() >= 1) {
                                foodData.setSaturation(foodData.getSaturationLevel() - 1);
                            } else if (foodData.getFoodLevel() >= 1) {
                                foodData.setFoodLevel(foodData.getFoodLevel() - 1);
                            }
                        }
                    }
                }
            }
        }

        public static boolean noIncandescenceFood(ItemStack stack, LivingEntity entity){
            FoodProperties fp = stack.getFoodProperties(entity);
            if (fp == null) {
                return true;
            }
            for (FoodProperties.PossibleEffect pe : fp.effects()) {
                if (pe.effect().getEffect().is(ImmortalersDelightMobEffect.INCANDESCENCE)) {
                    return false;
                }
            }
            return true;
        }
    }
}
