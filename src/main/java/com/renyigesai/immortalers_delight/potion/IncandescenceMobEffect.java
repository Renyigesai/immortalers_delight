package com.renyigesai.immortalers_delight.potion;

import com.mojang.datafixers.util.Pair;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Objects;

public class IncandescenceMobEffect extends BaseMobEffect {

    public IncandescenceMobEffect() {
        super(MobEffectCategory.BENEFICIAL, -39424);
    }

    @Override
    public boolean isDurationEffectTickInControl(int duration, int amplifier) {
        return true;
    }

    @Mod.EventBusSubscriber(
            modid = ImmortalersDelightMod.MODID,
            bus = Mod.EventBusSubscriber.Bus.FORGE
    )
    public static class IncandescencePotionEffect {
        @SubscribeEvent
        public static void onUseItemFinish(LivingEntityUseItemEvent.Finish event) {
            if (event != null && event.getEntity() != null) {
                ItemStack stack = event.getResultStack();
                LivingEntity entity = event.getEntity();
                if (stack.getItem().isEdible() && noIncandescenceFood(stack,entity)) {
                    MobEffectInstance thisEffect = entity.getEffect(ImmortalersDelightMobEffect.INCANDESCENCE.get());
                    if (thisEffect != null && thisEffect.getEffect() instanceof BaseMobEffect baseMobEffect) {
                        int lv = baseMobEffect.getTruthUsingAmplifier(thisEffect.getAmplifier());
                        MobEffectInstance effectInstance = entity.getEffect(MobEffects.DAMAGE_BOOST);
                        int time = effectInstance != null ? effectInstance.getDuration() : 0;

                        FoodProperties food = stack.getItem().getFoodProperties(stack, entity);
                        if (food != null) {
                            int nutrition = food.getNutrition();
                            float saturation = food.getSaturationModifier();
                            float foodValue = nutrition * 2.0f * saturation + nutrition;
                            time += foodValue * 20 * (lv <= 3 ? (4 - lv) : 1);
                            entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, time + 100, thisEffect.getAmplifier()));
                        }
                    }

                }
            }
        }

        @SubscribeEvent
        public static void onCreatureHurt(LivingHurtEvent evt) {
            if (evt.isCanceled() || evt.getSource().is(DamageTypeTags.BYPASSES_RESISTANCE)) {
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
                    MobEffectInstance thisEffect = attacker.getEffect(ImmortalersDelightMobEffect.INCANDESCENCE.get());
                    if (thisEffect != null && thisEffect.getEffect() instanceof BaseMobEffect baseMobEffect) {
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
            List<Pair<MobEffectInstance, Float>> effects = Objects.requireNonNull(stack.getFoodProperties(entity)).getEffects();
            for (Pair<MobEffectInstance, Float> effect : effects) {
                if (effect.getFirst().getEffect() == ImmortalersDelightMobEffect.INCANDESCENCE.get()) {
                    return false;
                }
            }
            return true;
        }
    }
}
