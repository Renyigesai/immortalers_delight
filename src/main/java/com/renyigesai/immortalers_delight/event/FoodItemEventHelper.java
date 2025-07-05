package com.renyigesai.immortalers_delight.event;

import com.mojang.datafixers.util.Pair;
import com.renyigesai.immortalers_delight.entities.living.SkelverfishBomber;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightFoodProperties;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

@Mod.EventBusSubscriber
public class FoodItemEventHelper {
    @SubscribeEvent
    public static void onUseItemFinish(LivingEntityUseItemEvent.Finish event) {
        if (event != null && event.getEntity() != null) {
            ItemStack stack = event.getItem();
            Entity entity = event.getEntity();
            if (entity instanceof LivingEntity livingEntity && !livingEntity.level().isClientSide()) {
                if (stack.getItem().isEdible()) {
                    //大红包子的隐藏幸运效果
                    if (stack.getFoodProperties(livingEntity) == ImmortalersDelightFoodProperties.RED_STUFFED_BUN) {
                        if (DifficultyModeHelper.isPowerBattleMode()) {
                            if (livingEntity.getRandom().nextInt(3) == 0) {
                                livingEntity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 6000,3));
                                livingEntity.addEffect(new MobEffectInstance(MobEffects.LUCK, 2700));
                            }
                        }else if (livingEntity.getRandom().nextInt(3) == 0) {
                            livingEntity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 6000,1));
                            livingEntity.addEffect(new MobEffectInstance(MobEffects.LUCK, 600));
                        }
                    }
                    //棱珠牛奶的解除buff
                    if (stack.getFoodProperties(livingEntity) == ImmortalersDelightFoodProperties.PEARLIP_BUBBLE_MILK) {
                        livingEntity.curePotionEffects(new ItemStack(Items.MILK_BUCKET));
                        for (Pair<MobEffectInstance, Float> pair : Objects.requireNonNull(stack.getFoodProperties(livingEntity)).getEffects()) {
                            livingEntity.addEffect(new MobEffectInstance(pair.getFirst()));
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntityHurt(LivingHurtEvent evt) {
        if (evt.isCanceled() || evt.getSource().is(DamageTypeTags.BYPASSES_RESISTANCE)) {
            return;
        }
        boolean isPowerful = DifficultyModeHelper.isPowerBattleMode();
        LivingEntity hurtOne = evt.getEntity();
        LivingEntity attacker = null;
        if (evt.getSource().getEntity() instanceof LivingEntity livingEntity){
            attacker = livingEntity;
        }

        if (!hurtOne.level().isClientSide) {
            if (hurtOne.getUseItem().getItem() == ImmortalersDelightItems.EVOLUTCORN_HARD_CANDY.get()) {
                if (hurtOne.getTicksUsingItem() > (isPowerful ? 16 : 32)) {
                    if (isPowerful) {
                        float buffer = 0.4f - (0.6F * hurtOne.getTicksUsingItem() / hurtOne.getUseItem().getUseDuration());
                        if (buffer > 0) {
                            evt.setAmount(evt.getAmount() * buffer);
                        } else {
                            hurtOne.heal(evt.getAmount() * buffer * (-1));
                            evt.setCanceled(true);
                        }
                    } else evt.setAmount(evt.getAmount() * 0.4f);
                }
            }
        }
    }
}
