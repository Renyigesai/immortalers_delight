package com.renyigesai.immortalers_delight.potion;

import com.renyigesai.immortalers_delight.event.DifficultyModeHelper;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.GameRules;

import java.util.Objects;

public class KeepFastMobEffect extends MobEffect {

    public KeepFastMobEffect() {
        super(MobEffectCategory.BENEFICIAL, -39424);
    }
    @Override
    public void applyEffectTick(LivingEntity pEntity, int amplifier) {
        if (!pEntity.getCommandSenderWorld().isClientSide && pEntity instanceof Player player) {
            FoodData foodData = player.getFoodData();
            int foodLevel = foodData.getFoodLevel();
            float saturation = foodData.getSaturationLevel();
            int differenceValue = foodData.getLastFoodLevel() - foodData.getFoodLevel();
            if (differenceValue > 0 && player.getRandom().nextInt(amplifier + 2) != 0) {
                player.getFoodData().eat(differenceValue, 0.1F);
            }
            boolean isPowerful = DifficultyModeHelper.isPowerBattleMode();
            int time = player.hasEffect(ImmortalersDelightMobEffect.KEEP_A_FAST.get()) ? player.getEffect(ImmortalersDelightMobEffect.KEEP_A_FAST.get()).getDuration() : 0;
            if (time > 0) {
                if (time == 1) {
                    foodData.eat(foodLevel, saturation / (foodLevel * 2));
                    if (foodLevel * 2 > 20 && amplifier > 1) player.heal(foodLevel * 2 - 20);
                    if (saturation * 2 >= 20 && amplifier > 0) player.heal(saturation * 2 - 20);
                }
                if (isPowerful) {
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
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
