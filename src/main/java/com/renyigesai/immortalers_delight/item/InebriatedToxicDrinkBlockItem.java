package com.renyigesai.immortalers_delight.item;

import com.mojang.datafixers.util.Pair;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class InebriatedToxicDrinkBlockItem extends DrinkItem {


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
        // 从物品栈中获取具体的物品
        Item item = stack.getItem();
        // 检查该物品是否为可食用物品
        if (item.isEdible()) {
            // 遍历物品的食物属性中定义的所有药水效果及其概率
            for (Pair<MobEffectInstance, Float> pair : stack.getFoodProperties(livingEntity).getEffects()) {
                // 条件判断：
                // 1. 当前不是客户端，因为药水效果的添加通常在服务器端处理，以保证数据一致性。
                // 2. 药水效果实例不为空，确保有有效的药水效果。
                // 3. 药水效果为我们指定的酒精效果
                if (!level.isClientSide && pair.getFirst() != null) {
                    if (pair.getFirst().getEffect() == ImmortalersDelightMobEffect.INEBRIATED.get()) {
                        // 创建一个新的药水效果实例，使用原有的药水效果实例作为模板。
                        // 然后将该药水效果添加到食用物品的实体上。
                        int oldLv = livingEntity.hasEffect(ImmortalersDelightMobEffect.INEBRIATED.get()) ? livingEntity.getEffect(ImmortalersDelightMobEffect.INEBRIATED.get()).getAmplifier() : 0;
                        int oldTime = livingEntity.hasEffect(ImmortalersDelightMobEffect.INEBRIATED.get()) ? livingEntity.getEffect(ImmortalersDelightMobEffect.INEBRIATED.get()).getDuration() : 0;
                        int time = pair.getFirst().getDuration() + oldTime;
                        int lv = pair.getFirst().getAmplifier() > oldLv ? pair.getFirst().getAmplifier() : oldLv;
                        livingEntity.addEffect(new MobEffectInstance(pair.getFirst().getEffect(),time,lv));
                        //InebriatedEffect.applyImmortalEffect(livingEntity,(double) time / 20 + 0.1,lv);
                    }
                    else  livingEntity.addEffect(pair.getFirst());
                }
            }
        }
    }
}
