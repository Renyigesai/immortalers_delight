package com.renyigesai.immortalers_delight.potion;

import com.renyigesai.immortalers_delight.event.DifficultyModeHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;

import static com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect.MAGICAL_REVERSE;

public class SatiatedMobEffect extends MobEffect {
    public static boolean isPowerful = true;

    public SatiatedMobEffect() {
        super(MobEffectCategory.BENEFICIAL, -39424);
    }

    @Override
    public void applyEffectTick(LivingEntity pEntity, int amplifier) {
        isPowerful = DifficultyModeHelper.isPowerBattleMode();
        if (!pEntity.level().isClientSide()) {
            pEntity.addEffect(new MobEffectInstance(MobEffects.SATURATION, 1, amplifier));
            if (isPowerful) {
                if (pEntity instanceof Player player) {
                    FoodData foodData = player.getFoodData();
                    if (foodData.getFoodLevel() >= 20) {player.heal(amplifier + 1);}
                    if (foodData.getSaturationLevel() >= 20) {player.heal(amplifier + 1);}
                } else if (pEntity.getHealth() > 0.8 * pEntity.getMaxHealth()) {
                    pEntity.heal(2.5F*(amplifier + 1));
                }
            }
        }
    }
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % (isPowerful ? 20 : 40) == 0;
    }
}
