package com.renyigesai.immortalers_delight.potion;

import com.renyigesai.immortalers_delight.event.DifficultyModeHelper;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CoolPotionEffect {
    @SubscribeEvent
    public static void onUseItemFinish(LivingEntityUseItemEvent.Finish event) {
        if (event != null && event.getEntity() != null) {
            ItemStack stack = event.getItem();
            Entity entity = event.getEntity();
            if (entity instanceof LivingEntity livingEntity) {
                if (stack.getItem().isEdible() && livingEntity.hasEffect(ImmortalersDelightMobEffect.COOL.get())) {
                    int lv = livingEntity.hasEffect(ImmortalersDelightMobEffect.COOL.get()) ? livingEntity.getEffect(ImmortalersDelightMobEffect.COOL.get()).getAmplifier() : 0;
                    int time = livingEntity.hasEffect(MobEffects.DAMAGE_RESISTANCE) ? livingEntity.getEffect(MobEffects.DAMAGE_RESISTANCE).getDuration() : 0;
//                    livingEntity.removeEffect(ImmortalersDelightMobEffect.INCANDESCENCE.get());
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, time + (lv > 3 ? 100 * lv : 100), Math.min(lv, 3)));
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

        if (!hurtOne.level().isClientSide && DifficultyModeHelper.isPowerBattleMode()) {
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
}
