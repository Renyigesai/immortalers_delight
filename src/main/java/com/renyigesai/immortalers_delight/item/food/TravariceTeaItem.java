package com.renyigesai.immortalers_delight.item.food;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightFoodProperties;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import vectorwing.farmersdelight.common.item.DrinkableItem;

import java.util.ArrayList;
import java.util.Iterator;

public class TravariceTeaItem extends DrinkableItem {
    public TravariceTeaItem() {
        super(new Item.Properties().craftRemainder(Items.GLASS_BOTTLE).stacksTo(16).food(ImmortalersDelightFoodProperties.TRAVARICE_TEA),true,false);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity consumer) {
        removeAllEffect(level,consumer);
        return super.finishUsingItem(stack, level, consumer);
    }

    private void removeAllEffect(Level level, LivingEntity consumer){
        if (!level.isClientSide()){
            Iterator<MobEffectInstance> itr = consumer.getActiveEffects().iterator();
            ArrayList<MobEffect> compatibleEffects = new ArrayList<>();
            MobEffectInstance selectedEffect;
            while(itr.hasNext()) {
                selectedEffect = itr.next();
                if (selectedEffect.getEffect().getCategory() == MobEffectCategory.HARMFUL) {
                    compatibleEffects.add(selectedEffect.getEffect());
                }
            }
            if (!compatibleEffects.isEmpty()){
                for (MobEffect compatibleEffect : compatibleEffects) {
                    consumer.removeEffect(compatibleEffect);
                    break;
                }
            }
        }
    }
}
