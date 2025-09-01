package com.renyigesai.immortalers_delight.compat.item;

import com.doggystudio.chirencqr.ltc.server.item.ItemLatiaoBase;
import com.doggystudio.chirencqr.ltc.server.misc.EnumLatiaoGrade;
import com.mojang.datafixers.util.Pair;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class KwatWheatLatiaoItem extends LatiaoItem {
    public KwatWheatLatiaoItem(FoodProperties foodProperties, EnumLatiaoGrade grade) {
        super(foodProperties, grade);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity living) {
        addGasPoisonEffect(stack,level,living);
        return super.finishUsingItem(stack, level, living);
    }
    private void addGasPoisonEffect(ItemStack p_21064_, Level p_21065_, LivingEntity p_21066_) {
        // 从物品栈中获取具体的物品
        Item item = p_21064_.getItem();
        // 检查该物品是否为可食用物品
        if (item.isEdible()) {
            // 遍历物品的食物属性中定义的所有药水效果及其概率
            for (Pair<MobEffectInstance, Float> pair : p_21064_.getFoodProperties(p_21066_).getEffects()) {
                // 条件判断：
                // 1. 当前不是客户端，因为药水效果的添加通常在服务器端处理，以保证数据一致性。
                // 2. 药水效果实例不为空，确保有有效的药水效果。
                // 3. 药水效果为我们指定的瓦斯毒效果
                if (!p_21065_.isClientSide && pair.getFirst() != null && pair.getFirst().getEffect() == ImmortalersDelightMobEffect.GAS_POISON.get()) {
                    // 创建一个新的药水效果实例，使用原有的药水效果实例作为模板。
                    // 然后将该药水效果添加到食用物品的实体上。
                    p_21066_.addEffect(new MobEffectInstance(pair.getFirst()));
                    double time = (double) pair.getFirst().getDuration() / 20;
                    int lv = pair.getFirst().getAmplifier();
                    //GasPoisonEffect.applyImmortalEffect(p_21066_,time + 0.1,lv);
                }
            }
        }
    }
}
