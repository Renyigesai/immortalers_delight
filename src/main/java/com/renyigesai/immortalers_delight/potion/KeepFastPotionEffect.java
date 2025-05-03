package com.renyigesai.immortalers_delight.potion;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class KeepFastPotionEffect {
    @SubscribeEvent
    public static void onAddToEntity(MobEffectEvent.Added event) {
        if (event != null && event.getEntity() != null) {
            Entity entity = event.getEntity();
            if (!entity.getCommandSenderWorld().isClientSide
                    && event.getEffectInstance().getEffect() == ImmortalersDelightMobEffect.KEEP_A_FAST.get()
                    && entity instanceof Player player) {
                if ( !player.hasEffect(ImmortalersDelightMobEffect.KEEP_A_FAST.get())
                        || event.getOldEffectInstance() == null
                        || event.getOldEffectInstance().getEffect() != ImmortalersDelightMobEffect.KEEP_A_FAST.get()
                ) {
                    FoodData foodData = player.getFoodData();
                    foodData.setFoodLevel(foodData.getFoodLevel() / 2);
                    foodData.setSaturation(foodData.getSaturationLevel() / 2);
                }
            }
        }
    }
    @SubscribeEvent
    public static void onRemoveFromEntity(MobEffectEvent.Remove event) {
        if (event != null && event.getEntity() != null) {
            Entity entity = event.getEntity();
            if (!entity.getCommandSenderWorld().isClientSide
                    && event.getEffectInstance() != null
                    && event.getEffectInstance().getEffect() == ImmortalersDelightMobEffect.KEEP_A_FAST.get()
                    && entity instanceof Player player) {
                FoodData foodData = player.getFoodData();
                int foodLevel = foodData.getFoodLevel();
                float saturation = foodData.getSaturationLevel();
                foodData.eat(foodLevel, saturation / (foodLevel * 2));
                if (foodLevel * 2 > 20 && event.getEffectInstance().getAmplifier() > 1) player.heal(foodLevel * 2 - 20);
                if (saturation * 2 >= 20 && event.getEffectInstance().getAmplifier() > 0) player.heal(saturation * 2 - 20);
            }
        }
    }
}
