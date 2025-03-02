package com.renyigesai.immortalers_delight.potion;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
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
            if (player.getEffect(ImmortalersDelightMobEffect.KEEP_A_FAST.get()) != null
                    && Objects.requireNonNull(player.getEffect(ImmortalersDelightMobEffect.KEEP_A_FAST.get())).getDuration() == 1) {
                foodData.eat(foodLevel, saturation / (foodLevel * 2));
                if (foodLevel * 2 > 20 && amplifier > 1) player.heal(foodLevel * 2 - 20);
                if (saturation * 2 >= 20 && amplifier > 0) player.heal(saturation * 2 - 20);
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
