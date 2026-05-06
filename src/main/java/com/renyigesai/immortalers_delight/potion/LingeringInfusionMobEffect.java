package com.renyigesai.immortalers_delight.potion;

import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;

public class LingeringInfusionMobEffect extends BaseMobEffect {
    public LingeringInfusionMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 11325584);
    }

    @Override
    public void applyEffectTickInControl(LivingEntity pEntity, int amplifier) {
        boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
        float healByFoodLevel = isPowerful ? 0.5F : 0.375F;
        float healBySaturation = isPowerful ? 1.5F : 0.5F;

        if (!pEntity.getCommandSenderWorld().isClientSide){
            if (pEntity instanceof Player player) {
                if (!player.isHurt()) return;
                boolean hasKeepFast = pEntity.hasEffect(ImmortalersDelightMobEffect.KEEP_A_FAST);
                int time = player.hasEffect(ImmortalersDelightMobEffect.LINGERING_INFUSION) ?
                        player.getEffect(ImmortalersDelightMobEffect.LINGERING_INFUSION).getDuration() : 0;
                FoodData foodData = player.getFoodData();
                boolean canHealBySaturation = isPowerful ? foodData.getFoodLevel() >= (hasKeepFast ? 10 : 20)
                        : foodData.getFoodLevel() >= 20 && foodData.getSaturationLevel() > 0;
                boolean canHealByFoodLevel = isPowerful ? foodData.getFoodLevel() >= (hasKeepFast ? 9 : 18)
                        : foodData.getFoodLevel() >= 18 && time % 20 == 0;
                if (canHealBySaturation) {
                    player.heal(healBySaturation*(amplifier + 1));
                } else if (canHealByFoodLevel) {
                    player.heal(healByFoodLevel*(amplifier + 1));
                }
            } else {
                if (pEntity.getHealth() > 0.7 * pEntity.getMaxHealth()) {
                    pEntity.heal(1.0F*(amplifier + 1));
                }
            }
        }
    }

    @Override
    public boolean isDurationEffectTickInControl(int duration, int amplifier) {
        return duration % 10 == 0;
    }
}
