package com.renyigesai.immortalers_delight.potion;

import com.mojang.datafixers.util.Pair;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber
public class IncandescencePotionEffect {
    @SubscribeEvent
    public static void onUseItemFinish(LivingEntityUseItemEvent.Finish event) {
        if (event != null && event.getEntity() != null) {
            ItemStack stack = event.getResultStack();
            LivingEntity entity = event.getEntity();
            if (stack.getItem().isEdible() && entity.hasEffect(ImmortalersDelightMobEffect.INCANDESCENCE.get()) && noIncandescenceFood(stack,entity)) {
                int lv = entity.hasEffect(ImmortalersDelightMobEffect.INCANDESCENCE.get()) ? entity.getEffect(ImmortalersDelightMobEffect.INCANDESCENCE.get()).getAmplifier() : 0;
                int time = entity.hasEffect(MobEffects.DAMAGE_BOOST) ? entity.getEffect(MobEffects.DAMAGE_BOOST).getDuration() : 0;
                entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, time + 100, lv));
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
                if (attacker.hasEffect(ImmortalersDelightMobEffect.INCANDESCENCE.get())) {
                    if (hurtOne.hasEffect(MobEffects.FIRE_RESISTANCE)) hurtOne.removeEffect(MobEffects.FIRE_RESISTANCE);
                    int lv = attacker.getEffect(ImmortalersDelightMobEffect.INCANDESCENCE.get()).getAmplifier();
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
