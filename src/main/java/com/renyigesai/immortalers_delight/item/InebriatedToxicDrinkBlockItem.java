package com.renyigesai.immortalers_delight.item;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class InebriatedToxicDrinkBlockItem extends DrinkItem {

    public InebriatedToxicDrinkBlockItem(Block pBlock, Properties pProperties, boolean hasPotionEffectTooltip, boolean hasCustomToolTip) {
        super(pBlock, pProperties, hasPotionEffectTooltip, hasCustomToolTip);
    }
    public InebriatedToxicDrinkBlockItem(Block pBlock, Properties pProperties, boolean hasPotionEffectTooltip) {
        super(pBlock, pProperties, hasPotionEffectTooltip);
    }

    public InebriatedToxicDrinkBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity consumer) {
        addInebriatedEffect(stack,level,consumer);
        return super.finishUsingItem(stack,level,consumer);
    }

    private void addInebriatedEffect(ItemStack stack, Level level, LivingEntity livingEntity) {
        FoodProperties fp = stack.getFoodProperties(livingEntity);
        if (fp == null) {
            return;
        }
        for (FoodProperties.PossibleEffect pe : fp.effects()) {
            MobEffectInstance first = pe.effect();
            if (!level.isClientSide && first != null) {
                if (level.random.nextFloat() >= pe.probability()) {
                    continue;
                }
                if (first.getEffect().is(ImmortalersDelightMobEffect.INEBRIATED)) {
                    int oldLv = livingEntity.hasEffect(ImmortalersDelightMobEffect.INEBRIATED) ? livingEntity.getEffect(ImmortalersDelightMobEffect.INEBRIATED).getAmplifier() : 0;
                    int oldTime = livingEntity.hasEffect(ImmortalersDelightMobEffect.INEBRIATED) ? livingEntity.getEffect(ImmortalersDelightMobEffect.INEBRIATED).getDuration() : 0;
                    int time = first.getDuration() + oldTime;
                    int lv = first.getAmplifier() > oldLv ? first.getAmplifier() : oldLv;
                    livingEntity.addEffect(new MobEffectInstance(first.getEffect(), time, lv));
                } else {
                    livingEntity.addEffect(first);
                }
            }
        }
    }
}
