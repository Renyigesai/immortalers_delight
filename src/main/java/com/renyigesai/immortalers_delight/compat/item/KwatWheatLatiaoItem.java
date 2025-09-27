package com.renyigesai.immortalers_delight.compat.item;

import com.doggystudio.chirencqr.ltc.server.item.UpgradableLatiaoItem;
import com.doggystudio.chirencqr.ltc.server.misc.EnumLatiaoGrade;
import com.mojang.datafixers.util.Pair;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class KwatWheatLatiaoItem extends UpgradableLatiaoItem {

    public KwatWheatLatiaoItem(int nutrition, float saturation, int effctTime, int effectLevel, EnumLatiaoGrade grade) {
        super(nutrition, saturation, effctTime, effectLevel, grade);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity living) {
        addGasPoisonEffect(stack,level,living);
        return super.finishUsingItem(stack, level, living);
    }
    private void addGasPoisonEffect(ItemStack p_21064_, Level p_21065_, LivingEntity p_21066_) {
        Item item = p_21064_.getItem();
        if (item.isEdible()) {
            if (!p_21065_.isClientSide){
                p_21066_.addEffect(new MobEffectInstance(ImmortalersDelightMobEffect.GAS_POISON.get(),100));
            }
        }
    }
}
