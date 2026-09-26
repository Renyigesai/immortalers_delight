package com.renyigesai.immortalers_delight.item.food.obsidian_walnut;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.item.EnchantAbleFoodItem;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class PieBoostFoodItem extends EnchantAbleFoodItem {
    public static final String GAIN_PIE = ImmortalersDelightMod.MODID + "_gain_pie";
    private final boolean isAmplifier;
    public PieBoostFoodItem(Properties properties, boolean hasFoodEffectTooltip, boolean isAmplifier) {
        super(properties, hasFoodEffectTooltip, false);
        this.isAmplifier = isAmplifier;
    }

    public PieBoostFoodItem(Properties properties, boolean hasFoodEffectTooltip, boolean isAmplifier, int tooltipColorId) {
        super(properties, hasFoodEffectTooltip, false, tooltipColorId);
        this.isAmplifier = isAmplifier;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, Level level, @NotNull LivingEntity consumer) {
        if (!level.isClientSide()) {
            //向使用者的nbt中添加强化派的标记
            CompoundTag tag = consumer.getPersistentData();
            tag.putBoolean(GAIN_PIE,this.isAmplifier);
        }
        return super.finishUsingItem(stack,level,consumer);
    }


    @Mod.EventBusSubscriber(
            modid = ImmortalersDelightMod.MODID,
            bus = Mod.EventBusSubscriber.Bus.FORGE
    )
    public static class PieBoostFoodItemEvents {
        @SubscribeEvent
        public static void onUseFinish(LivingEntityUseItemEvent.Finish event) {
            if (event != null && event.getEntity() != null) {
                ItemStack stack = event.getItem();
                Entity entity = event.getEntity();
                if (entity instanceof LivingEntity livingEntity && !livingEntity.level().isClientSide() && stack.getItem().isEdible()) {

                    //查1.3乐事新增的派标签
                    ResourceLocation resourcelocation = new ResourceLocation("farmersdelight","pies");
                    TagKey<Item> tagkey = TagKey.create(Registries.ITEM, resourcelocation);
                    Collection<Item> items = getItemsFromTagKey(tagkey);
                    if (items.contains(stack.getItem())) {
                        CompoundTag tag = livingEntity.getPersistentData();
                        if (tag.contains(GAIN_PIE)) {
                            //是增幅，把buff等级+1；否则把时间翻倍
                            addEffects(stack,livingEntity,tag.getBoolean(GAIN_PIE));
                        }
                    }
                    //派标签百科都查不到，估计兼容的附属不多，所以直接上ID检测吧
                    else if (isCakeId(ForgeRegistries.ITEMS.getKey(stack.getItem()).getPath())
                    || isPieId(ForgeRegistries.ITEMS.getKey(stack.getItem()).getPath())
                    ){
                        CompoundTag tag = livingEntity.getPersistentData();
                        if (tag.contains(GAIN_PIE)) {
                            //是增幅，把buff等级+1；否则把时间翻倍
                            addEffects(stack,livingEntity,tag.getBoolean(GAIN_PIE));
                        }
                    }
                }
            }
        }

        public static void addEffects(ItemStack stack, LivingEntity livingEntity, boolean isAmp) {
            FoodProperties food = stack.getFoodProperties(livingEntity);
            if (food != null) {
                for (Pair<MobEffectInstance, Float> pair : food.getEffects()) {
                    if (pair.getFirst() != null) {
                        ///复刻药水效果
                        MobEffect effect = pair.getFirst().getEffect();
                        int time = (int) (pair.getFirst().getDuration());
                        if (time < 1) time = 1;
                        int lv = pair.getFirst().getAmplifier();
                        if (lv < 0) lv = 0;

                        //翻倍或提升
                        if (isAmp) lv++;
                        else time *= 2;

                        MobEffectInstance instance = new MobEffectInstance(effect, time, lv);
                        livingEntity.addEffect(instance);
                    }
                }
            }
        }

        //乐事的部分Tag在1.3有，在1.2是没有的，所以我们用类似合成配方的方式检测乐事的tag
        public static Collection<Item> getItemsFromTagKey(TagKey<Item> tag) {
            List<Item> list = Lists.newArrayList();

            for(Holder<Item> holder : BuiltInRegistries.ITEM.getTagOrEmpty(tag)) {
                list.add(new ItemStack(holder).getItem());
            }

            if (list.size() == 0) {
                list.add(new ItemStack(net.minecraft.world.level.block.Blocks.BARRIER).getItem());
            }
            return list;
        }

        /**
         * 判断物品ID是否符合“蛋糕”的命名规则
         */
        private static boolean isCakeId(String path) {
            return path.endsWith("_cake") || path.equals("cake") || path.contains("_cake_");
        }

        /**
         * 判断物品ID是否符合“派”的命名规则
         */
        private static boolean isPieId(String path) {
            // 以 "_pie" 结尾，或者直接叫 "pie"
            // apple_pie, sweet_berry_pie, shepherds_pie_slice
            return path.endsWith("_pie") || path.equals("pie") || path.contains("_pie_");
        }
    }
}
