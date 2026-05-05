package com.renyigesai.immortalers_delight.item.food;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.common.item.DrinkableItem;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class InebriatedToxicFoodItem extends DrinkableItem {

    private final boolean foil;
    private final boolean can_feed;
    public InebriatedToxicFoodItem(Properties properties, boolean hasFoodEffectTooltip) {
        super(properties,hasFoodEffectTooltip,false);
        foil = false;
        can_feed = false;
    }
    public InebriatedToxicFoodItem(Properties properties, boolean hasFoodEffectTooltip, boolean hasCustomTooltip) {
        super(properties,hasFoodEffectTooltip,hasCustomTooltip);
        foil = false;
        can_feed = false;
    }
    //弃用
    public InebriatedToxicFoodItem(Properties properties, boolean hasFoodEffectTooltip, boolean hasCustomTooltip, boolean isFoil, boolean canFeed) {
        super(properties,hasFoodEffectTooltip,hasCustomTooltip);
        foil = isFoil;
        can_feed = canFeed;
    }
    public boolean isFoil(ItemStack p_41172_) {
        return foil;
    }
    @Override
    public @NotNull ItemStack finishUsingItem (@NotNull ItemStack stack, @NotNull Level level, LivingEntity livingEntity) {
        addInebriatedEffect(stack,level,livingEntity);
        return super.finishUsingItem(stack, level, livingEntity);
    }
    /**
     * 该方法用于处理实体食用物品后添加对应的药水效果。
     * 当实体食用某个可食用物品时，会根据物品的属性尝试为实体添加相应的药水效果。
     *
     * @param stack 被食用的物品栈，包含了具体的物品及其数量等信息。
     * @param level 实体所在的游戏世界，用于判断是否为客户端，以及获取随机数生成器。
     * @param livingEntity 食用物品的实体，即要添加药水效果的对象。
     */
    public static void addInebriatedEffect(ItemStack stack, Level level, LivingEntity livingEntity) {
        net.minecraft.world.food.FoodProperties fp = stack.getFoodProperties(livingEntity);
        if (fp != null) {
            for (net.minecraft.world.food.FoodProperties.PossibleEffect pe : fp.effects()) {
                MobEffectInstance first = pe.effect();
                if (!level.isClientSide && first != null) {
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

    public static boolean addEffectWithoutCanBeAffected(LivingEntity livingEntity, MobEffectInstance pEffectInstance, @Nullable Entity source) {
        Map<Holder<MobEffect>, MobEffectInstance> activeEffects = livingEntity.getActiveEffectsMap();
        Holder<MobEffect> holder = pEffectInstance.getEffect();
        MobEffectInstance mobeffectinstance = activeEffects.get(holder);
        if (mobeffectinstance == null) {
            return livingEntity.addEffect(pEffectInstance);
        }
        if (mobeffectinstance.update(pEffectInstance)) {
            if (!livingEntity.level().isClientSide()) {
                MobEffect mobeffect = holder.value();
                mobeffect.removeAttributeModifiers(livingEntity.getAttributes());
                mobeffect.addAttributeModifiers(livingEntity.getAttributes(), pEffectInstance.getAmplifier());
                livingEntity.sendEffectToPassengers(pEffectInstance);
            }
            return true;
        }
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag isAdvanced) {

        if (this.can_feed) {
            MutableComponent textValue = Component.translatable(
                    "tooltip." + ImmortalersDelightMod.MODID+ ".is_can_feed"
            );
            tooltip.add(textValue.withStyle(ChatFormatting.YELLOW));
        }
        super.appendHoverText(stack, context, tooltip, isAdvanced);
    }
}
