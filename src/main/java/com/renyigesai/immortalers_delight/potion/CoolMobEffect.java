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
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Objects;

public class CoolMobEffect extends BaseMobEffect {

    public CoolMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 7969524);
    }

    @Override
    public boolean isDurationEffectTickInControl(int duration, int amplifier) {
        return true;
    }

    @Mod.EventBusSubscriber(
            modid = ImmortalersDelightMod.MODID,
            bus = Mod.EventBusSubscriber.Bus.FORGE
    )
    public static class CoolPotionEffect {
        @SubscribeEvent
        public static void onUseItemFinish(LivingEntityUseItemEvent.Finish event) {
            if (event != null && event.getEntity() != null) {
                ItemStack stack = event.getResultStack();
                LivingEntity livingEntity = event.getEntity();
                if (stack.getItem().isEdible() && noCoolFood(stack,livingEntity)) {
                    MobEffectInstance thisEffect = livingEntity.getEffect(ImmortalersDelightMobEffect.COOL.get());
                    if (thisEffect != null && thisEffect.getEffect() instanceof BaseMobEffect) {
                        int lv = ((BaseMobEffect)thisEffect.getEffect()).getTruthUsingAmplifier(thisEffect.getAmplifier());
                        int time = 0;
                        MobEffectInstance effectInstance = livingEntity.getEffect(MobEffects.DAMAGE_RESISTANCE);
                        if (effectInstance != null) time = effectInstance.getDuration();

                        FoodProperties food = stack.getItem().getFoodProperties(stack, livingEntity);
                        if (food != null) {
                            int nutrition = food.getNutrition();
                            float saturation = food.getSaturationModifier();
                            float foodValue = nutrition * 2.0f * saturation + nutrition;
                            time += foodValue * 20 * (lv > 3 ? (lv - 2) : 1);
                            livingEntity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, time, Math.min(lv, 3)));
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

            if (!hurtOne.level().isClientSide && DifficultyModeUtil.isPowerBattleMode()) {
                if (!evt.getSource().is(DamageTypeTags.BYPASSES_ARMOR) || !evt.getSource().is(DamageTypeTags.BYPASSES_SHIELD)){
                    MobEffectInstance thisEffect = hurtOne.getEffect(ImmortalersDelightMobEffect.COOL.get());
                    if (thisEffect != null && thisEffect.getEffect() instanceof BaseMobEffect) {
                        int lv = thisEffect.getAmplifier();
                        lv++;
                        float buffer = hurtOne.getRemainingFireTicks() <= 1 ? (float) (0.9675*Math.exp(-0.0372*lv)) : (float) (0.9344*Math.exp(-0.1154*lv));
                        evt.setAmount(Math.min(evt.getAmount()*buffer, evt.getAmount()));
                    }
                }
            }
        }

        public static boolean noCoolFood(ItemStack stack, LivingEntity entity){
            List<Pair<MobEffectInstance, Float>> effects = Objects.requireNonNull(stack.getFoodProperties(entity)).getEffects();
            for (Pair<MobEffectInstance, Float> effect : effects) {
                if (effect.getFirst().getEffect() == ImmortalersDelightMobEffect.COOL.get()) {
                    return false;
                }
            }
            return true;
        }
    }
}
