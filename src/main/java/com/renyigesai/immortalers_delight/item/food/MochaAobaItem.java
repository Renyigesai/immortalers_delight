package com.renyigesai.immortalers_delight.item.food;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightTags;
import com.renyigesai.immortalers_delight.item.EnchantAbleFoodItem;
import com.renyigesai.immortalers_delight.item.PowerfulAbleFoodItem;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MochaAobaItem extends EnchantAbleFoodItem {
    public MochaAobaItem(Properties properties, boolean hasFoodEffectTooltip, boolean hasCustomTooltip, int color) {
        super(properties, hasFoodEffectTooltip, hasCustomTooltip,color);
    }


    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, Level level, @NotNull LivingEntity consumer) {
        //定位另一只手的物品
        ItemStack inhand = consumer.getItemInHand(InteractionHand.MAIN_HAND);
        if (inhand.is(stack.getItem())) inhand = consumer.getItemInHand(InteractionHand.OFF_HAND);
        eatAllInHand(inhand,consumer);
        return super.finishUsingItem(stack,level,consumer);
    }
    public void eatAllInHand(ItemStack stack,LivingEntity entity) {
        if (stack.isEmpty()) return;
        if (stack.getItem() instanceof MochaAobaItem) return;
        if (stack.getCount() <= 1) return;

        //遍历所有可能的面包食物
        ResourceLocation resourcelocation = new ResourceLocation("forge","bread");
        TagKey<Item> tagkey = TagKey.create(Registries.ITEM, resourcelocation);
        Collection<Item> items = getItemsFromTagKey(tagkey);
        if (items.contains(stack.getItem())) {
            eatAll(stack,entity);
        } else {
            resourcelocation = new ResourceLocation("forge","bread_slices");
            tagkey = TagKey.create(Registries.ITEM, resourcelocation);
            items = getItemsFromTagKey(tagkey);
            if (items.contains(stack.getItem())) {
                eatAll(stack,entity);
            } else if (DifficultyModeUtil.isPowerBattleMode()) {
                //超凡模式下也适用于甜点
                resourcelocation = new ResourceLocation("farmersdelight","sweets");
                tagkey = TagKey.create(Registries.ITEM, resourcelocation);
                //遍历可能存在的农夫乐事甜品标签（旧版不存在）
                items = getItemsFromTagKey(tagkey);
                if (items.contains(stack.getItem())) {
                    eatAll(stack,entity);
                } else {
                    //如果这标签查不到（大概率说明是1.2乐事），启用备用方案，查千古的本地备份
                    if (stack.is(ImmortalersDelightTags.Items.FA_SWEETS)) {
                        eatAll(stack,entity);
                    }
                }
            }
        }
    }

    public void eatAll(ItemStack stack, LivingEntity eater) {
        //判定消耗的数量
        int count = stack.getCount();
        //强力效果的食物的食用上限更低
        if (stack.getItem() instanceof PowerfulAbleFoodItem) {
            int max = stack.getMaxStackSize() >= 64 ? stack.getMaxStackSize() / 4 : stack.getMaxStackSize() / 2;
            if (count > max) count = max;
        }
        //计算消耗后还剩的物品
        int newCount = stack.getCount() - count;

        //叠加食物的效果
        if (!eater.level().isClientSide()) {

            if (stack.getItem().isEdible()) {
                FoodProperties food = stack.getFoodProperties(eater);

                if (food != null) {

                    //计算总饱食度
                    int h = food.getNutrition() * count;
                    float s = h * 2 * food.getSaturationModifier();
                    //为玩家回饱食度
                    if (eater instanceof Player player) {
                        int needH = 20 - player.getFoodData().getFoodLevel();
                        float needS = 20 - player.getFoodData().getSaturationLevel();
                        //计算吃饱后还溢出的饱食度
                        h -= needH;
                        s -= needS;
                    }

                    //根据饱食度计算补益的额外时间
                    int satTime = (int) ((s + h) * 20);
                    if (satTime < 0) satTime = 0;
                    //施加时间叠加的药水效果
                    boolean hasSat = false;
                    for (Pair<MobEffectInstance, Float> pair : food.getEffects()) {
                        if (pair.getFirst() != null) {
                            ///复刻药水效果
                            MobEffect effect = pair.getFirst().getEffect();
                            int time = (int) (pair.getFirst().getDuration() * count * pair.getSecond());
                            if (time < 1) time = 1;
                            int lv = pair.getFirst().getAmplifier();
                            if (lv < 0) lv = 0;
                            //应用额外的补益时间
                            if (effect == ImmortalersDelightMobEffect.SATIATED.get()) {
                                time += satTime >> lv;
                                hasSat = true;
                            }
                            //特殊处理伤害吸收效果（因为这buff叠时间意义不大）
                            if (effect == MobEffects.ABSORPTION) {
                                time = pair.getFirst().getDuration();
                                lv = (int) ((pair.getFirst().getAmplifier() + 1) * count * pair.getSecond());
                                if (lv > 1) lv -= 1;
                            }
                            MobEffectInstance instance = new MobEffectInstance(effect, time, lv);
                            eater.addEffect(instance);
                        }
                    }
                    //如果食物本身不带补益效果，提供额外的补益效果
                    if (satTime > 0 && !hasSat) eater.addEffect(new MobEffectInstance(ImmortalersDelightMobEffect.SATIATED.get(),satTime));
                }
            }
        }

        //执行使用物品的方法
        for (int i = 0;i < count;i++) {
            stack.getItem().finishUsingItem(stack,eater.level(),eater);
        }
        //生存模式下消耗物品
        if (!(eater instanceof Player player && player.getAbilities().instabuild)) {
            int need = stack.getCount() - newCount;
            if (need > 0) stack.shrink(need);
        }
    }

    //乐事的部分Tag在1.3有，在1.2是没有的，所以我们用类似合成配方的方式检测乐事的tag
    public Collection<Item> getItemsFromTagKey(TagKey<Item> tag) {
        List<Item> list = Lists.newArrayList();

        for(Holder<Item> holder : BuiltInRegistries.ITEM.getTagOrEmpty(tag)) {
            list.add(new ItemStack(holder).getItem());
        }

        if (list.size() == 0) {
            list.add(new ItemStack(net.minecraft.world.level.block.Blocks.BARRIER).getItem());
        }
        return list;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {

        if (DifficultyModeUtil.isPowerBattleMode()){
            List<Component> tooltips = new ArrayList<>();
            tooltips.add(Component.translatable("tooltip.immortalers_delight." + this + "_0").withStyle(ChatFormatting.YELLOW));
            tooltips.add(Component.translatable("tooltip.immortalers_delight." + this + "_1").withStyle(ChatFormatting.GRAY));

            pTooltip.addAll(tooltips);
        }else {
            super.appendHoverText(pStack,pLevel,pTooltip,pFlag);
        }
    }
}
