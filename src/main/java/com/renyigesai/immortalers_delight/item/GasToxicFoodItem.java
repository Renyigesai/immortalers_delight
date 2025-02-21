package com.renyigesai.immortalers_delight.item;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.potion.immortaleffects.GasPoisonEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import com.mojang.datafixers.util.Pair;

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
    /**
     * 该方法用于处理实体食用物品后添加对应的药水效果。
     * 当实体食用某个可食用物品时，会根据物品的属性尝试为实体添加相应的药水效果。
     *
     * @param p_21064_ 被食用的物品栈，包含了具体的物品及其数量等信息。
     * @param p_21065_ 实体所在的游戏世界，用于判断是否为客户端，以及获取随机数生成器。
     * @param p_21066_ 食用物品的实体，即要添加药水效果的对象。
     */
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
                    GasPoisonEffect.applyImmortalEffect(p_21066_,time + 0.1,lv);
                }
            }
        }
    }
}
