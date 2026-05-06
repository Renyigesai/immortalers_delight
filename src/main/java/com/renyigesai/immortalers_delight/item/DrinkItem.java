package com.renyigesai.immortalers_delight.item;

import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.List;

public class DrinkItem extends ItemNameBlockItem {

    public final boolean hasPotionEffectTooltip;
    public final boolean hasCustomToolTip;
    public DrinkItem(Block pBlock, Properties pProperties, boolean hasPotionEffectTooltip, boolean hasCustomToolTip) {
        super(pBlock, pProperties);
        this.hasPotionEffectTooltip = hasPotionEffectTooltip;
        this.hasCustomToolTip = hasCustomToolTip;
    }
    public DrinkItem(Block pBlock, Properties pProperties, boolean hasPotionEffectTooltip) {
        super(pBlock, pProperties);
        this.hasPotionEffectTooltip = hasPotionEffectTooltip;
        this.hasCustomToolTip = false;
    }

    public DrinkItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
        this.hasPotionEffectTooltip = false;
        this.hasCustomToolTip = false;
    }

    public @NotNull ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity consumer) {
        ItemStack containerStack = stack.getCraftingRemainingItem();
        Player player;
        if (stack.getFoodProperties(consumer) != null) {
            super.finishUsingItem(stack, level, consumer);
        } else {
            player = consumer instanceof Player ? (Player) consumer : null;
            if (player instanceof ServerPlayer) {
                CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) player, stack);
            }

            if (player != null) {
                player.awardStat(Stats.ITEM_USED.get(this));
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            }
        }

        if (stack.isEmpty()) {
            return containerStack;
        } else {
            if (consumer instanceof Player) {
                player = (Player) consumer;
                if (!((Player) consumer).getAbilities().instabuild && !player.getInventory().add(containerStack)) {
                    player.drop(containerStack, false);
                }
            }

            return stack;
        }
    }

    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 32;
    }

    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack heldStack = player.getItemInHand(hand);
        FoodProperties food = heldStack.getFoodProperties(player);
        if (food != null) {
            if (player.canEat(food.canAlwaysEat())) {
                player.startUsingItem(hand);
                return InteractionResultHolder.consume(heldStack);
            } else {
                return InteractionResultHolder.fail(heldStack);
            }
        } else {
            return ItemUtils.startUsingInstantly(level, player, hand);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag isAdvanced) {
        tooltip.add(Component.translatable("farmersdelight.tooltip.drink_block_item").withStyle(ChatFormatting.GRAY));
        if (Configuration.ENABLE_FOOD_EFFECT_TOOLTIP.get()) {
            if (this.hasCustomToolTip) {
                MutableComponent textEmpty = TextUtils.getTranslation("tooltip." + this, new Object[0]);
                tooltip.add(textEmpty.withStyle(ChatFormatting.BLUE));
            }
            if (this.hasPotionEffectTooltip) {
                FoodProperties fp = stack.getFoodProperties(null);
                if (fp != null) {
                    for (FoodProperties.PossibleEffect pe : fp.effects()) {
                        MobEffectInstance instance = pe.effect();
                        MutableComponent line = Component.translatable(instance.getDescriptionId());
                        MobEffect eff = instance.getEffect().value();
                        if (instance.getAmplifier() > 0) {
                            line = Component.translatable("potion.withAmplifier", line, Component.translatable("potion.potency." + instance.getAmplifier()));
                        }
                        if (instance.getDuration() > 20) {
                            line = Component.translatable("potion.withDuration", line, MobEffectUtil.formatDuration(instance, 1.0F, 20.0F));
                        }
                        tooltip.add(line.withStyle(eff.getCategory().getTooltipFormatting()));
                    }
                }
            }
        }
        super.appendHoverText(stack, context, tooltip, isAdvanced);
    }
}
