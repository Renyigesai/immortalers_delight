package com.renyigesai.immortalers_delight.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SnifferSaddleUpgradeItem extends Item {
    public SnifferSaddleUpgradeItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (target instanceof Sniffer sniffer) {
            // Check if sniffer has basic saddle
            if (sniffer.getPersistentData().getBoolean("HasSaddle")) {
                // Check if not already upgraded
                if (!sniffer.getPersistentData().getBoolean("SaddleUpgraded")) {
                    // Upgrade the saddle
                    sniffer.getPersistentData().putBoolean("SaddleUpgraded", true);
                    
                    // Consume the upgrade item
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                    
                    return InteractionResult.sidedSuccess(player.level().isClientSide);
                }
            }
        }
        return InteractionResult.PASS;
    }
}
