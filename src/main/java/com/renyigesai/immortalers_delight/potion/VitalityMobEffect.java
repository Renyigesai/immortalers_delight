package com.renyigesai.immortalers_delight.potion;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;

public class VitalityMobEffect extends MobEffect {
    public VitalityMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 11325584);
    }

    @Override
    public void applyEffectTick(LivingEntity pEntity, int amplifier) {
        if (!pEntity.getCommandSenderWorld().isClientSide){
            if (pEntity instanceof Player player) {
                if (!player.isHurt()) return;
                FoodData foodData = player.getFoodData();
                if (foodData.getFoodLevel() >= 20) {
                    player.heal((amplifier * 0.5F + 1));
                } else if (foodData.getFoodLevel() >= 18) {
                    player.heal(0.75F*(amplifier*0.5F + 1));
                }
            } else {
                if (pEntity.getHealth() > 0.7 * pEntity.getMaxHealth()) {
                    pEntity.heal(1.0F*(amplifier + 1));
                }
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % (amplifier > 0 ? 10 : 20) == 0;
    }
}
