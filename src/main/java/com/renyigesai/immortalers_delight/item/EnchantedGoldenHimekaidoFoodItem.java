package com.renyigesai.immortalers_delight.item;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
//import com.renyigesai.immortalers_delight.potion.immortaleffects.BaseImmortalEffect;
//import com.renyigesai.immortalers_delight.potion.immortaleffects.GasPoisonEffect;
//import com.renyigesai.immortalers_delight.potion.immortaleffects.InebriatedEffect;
//import com.renyigesai.immortalers_delight.potion.immortaleffects.MagicalReverseEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class EnchantedGoldenHimekaidoFoodItem extends EnchantAbleFoodItem{
    private static final int EAT_DURATION = 20;
    private final double reverseDuration;

    private final int reverseAmplifier;
    public EnchantedGoldenHimekaidoFoodItem(Properties properties, boolean hasFoodEffectTooltip, boolean hasCustomTooltip) {
        super(properties, hasFoodEffectTooltip, hasCustomTooltip);
        this.reverseDuration = 0;
        this.reverseAmplifier = 0;
    }

    public EnchantedGoldenHimekaidoFoodItem(Properties properties, boolean hasFoodEffectTooltip, boolean hasCustomTooltip, boolean isFoil, int reverseAmplifier, double reverseTime) {
        super(properties, hasFoodEffectTooltip, hasCustomTooltip, isFoil);
        this.reverseDuration = reverseTime;
        this.reverseAmplifier = reverseAmplifier + 1;
    }

    public int getUseDuration(ItemStack p_42933_) {
        return EAT_DURATION;
    }

    public UseAnim getUseAnimation(ItemStack p_42931_) {
        return UseAnim.EAT;
    }

    public double getReverseDuration() {return this.reverseDuration;}

    public int getReverseAmplifier() {return this.reverseAmplifier;}

    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        ItemStack outStack = pStack.copy();
        if (!pLevel.isClientSide) {
            if (outStack.getItem() instanceof EnchantedGoldenHimekaidoFoodItem thisItem) {
                if (thisItem.getReverseDuration() > 0 && thisItem.getReverseAmplifier() > 0) {
                    //MagicalReverseEffect.applyImmortalEffect(pLivingEntity,getReverseDuration(),getReverseAmplifier());
                    //System.out.println("这里是物品，已为实体添加不朽效果");
                    //BaseImmortalEffect.removeImmortalEffect(pLivingEntity);
                    pLivingEntity.removeEffect(ImmortalersDelightMobEffect.LINGERING_FLAVOR.get());
                    //GasPoisonEffect.removeImmortalEffect(pLivingEntity);
                    pLivingEntity.removeEffect(ImmortalersDelightMobEffect.GAS_POISON.get());
                    //InebriatedEffect.removeImmortalEffect(pLivingEntity);
                    pLivingEntity.removeEffect(ImmortalersDelightMobEffect.INEBRIATED.get());
                }
            }
        }
        return super.finishUsingItem(outStack,pLevel,pLivingEntity);
    }
}
