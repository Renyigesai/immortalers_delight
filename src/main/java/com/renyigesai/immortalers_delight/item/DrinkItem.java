package com.renyigesai.immortalers_delight.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
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
