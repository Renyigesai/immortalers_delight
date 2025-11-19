package com.renyigesai.immortalers_delight.potion;

import com.mojang.datafixers.util.Pair;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber
public class CoolPotionEffect {
    @SubscribeEvent
    public static void onUseItemFinish(LivingEntityUseItemEvent.Finish event) {
        if (event != null && event.getEntity() != null) {
            ItemStack stack = event.getResultStack();
            LivingEntity livingEntity = event.getEntity();
            if (stack.getItem().isEdible() && livingEntity.hasEffect(ImmortalersDelightMobEffect.COOL.get()) && noCoolFood(stack,livingEntity)) {
                int lv = livingEntity.hasEffect(ImmortalersDelightMobEffect.COOL.get()) ? livingEntity.getEffect(ImmortalersDelightMobEffect.COOL.get()).getAmplifier() : 0;
                int time = livingEntity.hasEffect(MobEffects.DAMAGE_RESISTANCE) ? livingEntity.getEffect(MobEffects.DAMAGE_RESISTANCE).getDuration() : 0;
                livingEntity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, time + (lv > 3 ? 100 * lv : 100), Math.min(lv, 3)));
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
                if (hurtOne.hasEffect(ImmortalersDelightMobEffect.COOL.get())) {
                    int lv = hurtOne.getEffect(ImmortalersDelightMobEffect.COOL.get()).getAmplifier();
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
