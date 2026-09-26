package com.renyigesai.immortalers_delight.event;

import com.mojang.datafixers.util.Pair;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightTags;
import com.renyigesai.immortalers_delight.recipe.BreadJamRecipe;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = ImmortalersDelightMod.MODID)
public class BreadJamEvents {

    @SubscribeEvent
    public static void onFinishUsingItem(LivingEntityUseItemEvent.Finish event) {
        ItemStack stack = event.getItem();
        LivingEntity entity = event.getEntity();

        // 服务端执行，且必须是带成分的面包
        if (entity.level().isClientSide() || !stack.is(ImmortalersDelightTags.Items.FORGE_BREAD) || !stack.hasTag()) return;

        CompoundTag tag = stack.getTag();
        if (!tag.contains("BreadToppings", Tag.TAG_LIST)) return;

        ListTag jamList = tag.getList("BreadToppings", Tag.TAG_COMPOUND);

        //缓存所有食物的buff效果
        Map<MobEffect,MobEffectInstance> buffer = new HashMap<MobEffect,MobEffectInstance>();

        //对每个食物的处理
        for (int i = 0; i < jamList.size(); i++) {
            ItemStack ingredient = ItemStack.of(jamList.getCompound(i));
            if (ingredient.isEmpty()) continue;

            // 恢复饱食度、饱和度与累积药水效果
            FoodProperties food = ingredient.getItem().getFoodProperties(ingredient, entity);
            if (food != null) {
                //立即吃掉饱食度
                if (entity instanceof Player player) {
                    player.getFoodData().eat(food.getNutrition(), food.getSaturationModifier());
                }
                for (Pair<MobEffectInstance, Float> pair : food.getEffects()) {
                    if (entity.level().random.nextFloat() < pair.getSecond()) {
                        MobEffectInstance newOne = pair.getFirst();
                        MobEffect newEffect = newOne.getEffect();
                        if (buffer.containsKey(newEffect)) {
                            MobEffectInstance oldOne = buffer.get(newEffect);
                            int lv = Math.max(oldOne.getAmplifier(),newOne.getAmplifier());
                            int oldTime = oldOne.getDuration();
                            int newTime = newOne.getDuration();
                            if (newOne.getAmplifier() > oldOne.getAmplifier()) {
                                oldTime = oldTime >> (newOne.getAmplifier() - oldOne.getAmplifier());
                            }
                            if (newOne.getAmplifier() < oldOne.getAmplifier()) {
                                newTime = newTime >> (oldOne.getAmplifier() - newOne.getAmplifier());
                            }
                            int time = oldTime + newTime;
                            buffer.put(newEffect,new MobEffectInstance(newEffect,time,lv));
                        } else buffer.put(newEffect,newOne);
                    }
                }
            }
            // 注：此处不再调用 finishUsingItem 或返回容器物品，避免吃面包吐玻璃瓶/铁桶
        }
        for (Map.Entry<MobEffect,MobEffectInstance> entry : buffer.entrySet()) {
            entity.addEffect(entry.getValue());
        }
    }

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (!stack.is(ImmortalersDelightTags.Items.FORGE_BREAD) || !stack.hasTag()) return;

        CompoundTag tag = stack.getTag();
        if (tag.contains("BreadToppings", Tag.TAG_LIST)) {
            ListTag jamList = tag.getList("BreadToppings", Tag.TAG_COMPOUND);
            if (!jamList.isEmpty()) {
                // 显示容量提示
                event.getToolTip().add(
                        Component.translatable("tooltip.immortalers_delight.bread_jam.toppings", jamList.size(), BreadJamRecipe.MAX_TOPPINGS)
                                .withStyle(ChatFormatting.GOLD)
                );
                for (int i = 0; i < jamList.size(); i++) {
                    ItemStack ingredient = ItemStack.of(jamList.getCompound(i));
                    if (!ingredient.isEmpty()) {
                        event.getToolTip().add(
                                Component.literal(" + ").withStyle(ChatFormatting.DARK_GRAY)
                                        .append(ingredient.getHoverName().copy().withStyle(ChatFormatting.YELLOW))
                        );
                    }
                }
            }
        }
    }
}
