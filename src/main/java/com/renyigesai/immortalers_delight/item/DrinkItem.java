package com.renyigesai.immortalers_delight.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.List;

public class DrinkItem extends ItemNameBlockItem {

    public final boolean hasPotionEffectTooltip;

    public DrinkItem(Block pBlock, Properties pProperties, boolean hasPotionEffectTooltip) {
        super(pBlock, pProperties);
        this.hasPotionEffectTooltip = hasPotionEffectTooltip;
    }

    public DrinkItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
        this.hasPotionEffectTooltip = false;
    }

    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity consumer) {
//        if (!level.isClientSide) {
//            this.affectConsumer(stack, level, consumer);
//        }

        ItemStack containerStack = stack.getCraftingRemainingItem();
        Player player;
        if (stack.isEdible()) {
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

//    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving) {
//        if (pEntityLiving instanceof Player player && !player.getAbilities().instabuild){
//            com.renyigesai.immortalers_delight.util.ItemUtils.givePlayerItem(player,pStack.getCraftingRemainingItem());
//        }
//        return super.finishUsingItem(pStack,pLevel,pEntityLiving);
//        ItemStack itemstack = super.finishUsingItem(pStack, pLevel, pEntityLiving);
//        return pEntityLiving instanceof Player && ((Player)pEntityLiving).getAbilities().instabuild ? itemstack : new ItemStack(itemstack.getCraftingRemainingItem().getItem());
//    }

    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack heldStack = player.getItemInHand(hand);
        if (heldStack.isEdible()) {
            if (player.canEat(heldStack.getFoodProperties(player).canAlwaysEat())) {
                player.startUsingItem(hand);
                return InteractionResultHolder.consume(heldStack);
            } else {
                return InteractionResultHolder.fail(heldStack);
            }
        } else {
            return ItemUtils.startUsingInstantly(level, player, hand);
        }
    }

    public void appendHoverText(ItemStack stack, @javax.annotation.Nullable Level level, List<Component> tooltip, TooltipFlag isAdvanced) {
        if (Configuration.FOOD_EFFECT_TOOLTIP.get()) {
            if (this.hasPotionEffectTooltip) {
                TextUtils.addFoodEffectTooltip(stack, tooltip, 1.0F);
            }
        }
    }

//    @Override
//    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
//        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
//        List<MobEffectInstance> list = new ArrayList<>();
//        listPotionEffects(pStack, list::add);
//        PotionUtils.addPotionTooltip(list, pTooltip, 1.0F);
//    }
}
