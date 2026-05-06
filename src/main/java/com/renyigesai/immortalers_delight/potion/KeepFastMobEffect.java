package com.renyigesai.immortalers_delight.potion;
import net.neoforged.fml.common.EventBusSubscriber;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;

public class KeepFastMobEffect extends BaseMobEffect {

    public KeepFastMobEffect() {
        super(MobEffectCategory.BENEFICIAL, -39424);
    }
    @Override
    public void applyEffectTickInControl(LivingEntity pEntity, int amplifier) {
        if (!pEntity.getCommandSenderWorld().isClientSide && pEntity instanceof Player player) {
            FoodData foodData = player.getFoodData();
            int foodLevel = foodData.getFoodLevel();
            float saturation = foodData.getSaturationLevel();
            int differenceValue = foodData.getLastFoodLevel() - foodData.getFoodLevel();
            if (differenceValue > 0 && player.getRandom().nextInt(amplifier + 2) != 0) {
                player.getFoodData().eat(differenceValue, 0.1F);
            }
            boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
            int time = player.hasEffect(ImmortalersDelightMobEffect.KEEP_A_FAST) ? player.getEffect(ImmortalersDelightMobEffect.KEEP_A_FAST).getDuration() : 0;
            if (time > 0) {
                if (time == 1) {
                    foodData.eat(foodLevel, saturation / (foodLevel * 2));
                    if (foodLevel * 2 > 20 && amplifier > 1) player.heal(foodLevel * 2 - 20);
                    if (saturation * 2 >= 20 && amplifier > 0) player.heal(saturation * 2 - 20);
                }
                if (isPowerful && player.isHurt()) {
                    if (time % (10/(amplifier + 1)) == 0 && foodLevel >= 9) {
                        float health = 0;
                        if (time % 40 == 0) {
                            foodData.setFoodLevel(foodLevel - 1);
                            health +=1.0f;
                        }
                        if (saturation > 0.0F) {
                            health += (saturation > 1.0f ? 1.0f : saturation) * (amplifier + 1);
                            foodData.setSaturation(saturation - 1.5F > 0.0F ? saturation - 1.5F : 0.0F);
                        }
                        player.heal(health);
                    }
                }
            }
        }
    }

    @Override
    public boolean isDurationEffectTickInControl(int duration, int amplifier) {
        return true;
    }


    @EventBusSubscriber(
            modid = ImmortalersDelightMod.MODID)
    public static class KeepFastPotionEffect {
        @SubscribeEvent
        public static void onAddToEntity(MobEffectEvent.Added event) {
            if (event != null && event.getEntity() != null) {
                Entity entity = event.getEntity();
                if (!entity.getCommandSenderWorld().isClientSide
                        && event.getEffectInstance().getEffect() == ImmortalersDelightMobEffect.KEEP_A_FAST
                        && entity instanceof Player player) {
                    if ( !player.hasEffect(ImmortalersDelightMobEffect.KEEP_A_FAST)
                            || event.getOldEffectInstance() == null
                            || event.getOldEffectInstance().getEffect() != ImmortalersDelightMobEffect.KEEP_A_FAST
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
                        && event.getEffectInstance().getEffect() == ImmortalersDelightMobEffect.KEEP_A_FAST
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
}
