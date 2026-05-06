package com.renyigesai.immortalers_delight.item.food;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.item.EnchantAbleFoodItem;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class GasToxicFoodItem extends EnchantAbleFoodItem {
    public GasToxicFoodItem(Properties properties, boolean hasFoodEffectTooltip, boolean hasCustomTooltip) {
        super(properties,hasFoodEffectTooltip,hasCustomTooltip);
    }
    public GasToxicFoodItem(Properties properties, boolean hasFoodEffectTooltip, boolean hasCustomTooltip, boolean isFoil) {
        super(properties,hasFoodEffectTooltip,hasCustomTooltip,isFoil);
    }
    @Override
    public ItemStack finishUsingItem (ItemStack pStack, Level level, LivingEntity pLivingEntity) {
        addGasPoisonEffect(pStack,level,pLivingEntity);
        return super.finishUsingItem(pStack,level,pLivingEntity);
    }

    private void addGasPoisonEffect(ItemStack p_21064_, Level p_21065_, LivingEntity p_21066_) {
        FoodProperties fp = p_21064_.getFoodProperties(p_21066_);
        if (fp == null) {
            return;
        }
        for (FoodProperties.PossibleEffect pe : fp.effects()) {
            MobEffectInstance inst = pe.effect();
            if (!p_21065_.isClientSide && inst != null && inst.getEffect().is(ImmortalersDelightMobEffect.GAS_POISON)) {
                if (p_21065_.random.nextFloat() >= pe.probability()) {
                    continue;
                }
                p_21066_.addEffect(new MobEffectInstance(inst));
            }
        }
    }
}
