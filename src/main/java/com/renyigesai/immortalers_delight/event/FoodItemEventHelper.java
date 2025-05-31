package com.renyigesai.immortalers_delight.event;

import com.mojang.datafixers.util.Pair;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightFoodProperties;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
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
                        if (livingEntity.getRandom().nextInt(3) == 0) {
                            livingEntity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 3000));
                            livingEntity.addEffect(new MobEffectInstance(MobEffects.LUCK, 600));
                        }
                    }
                    //西竹茶饼的解除buff
                    if (stack.getFoodProperties(livingEntity) == ImmortalersDelightFoodProperties.LEISAMBOO_TEA_CAKE) {
                        livingEntity.curePotionEffects(new ItemStack(Items.MILK_BUCKET));
                        for (Pair<MobEffectInstance, Float> pair : Objects.requireNonNull(stack.getFoodProperties(livingEntity)).getEffects()) {
                            livingEntity.addEffect(new MobEffectInstance(pair.getFirst()));
                        }
                    }
                }
            }
        }
    }
}
