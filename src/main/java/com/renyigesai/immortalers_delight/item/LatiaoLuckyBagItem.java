package com.renyigesai.immortalers_delight.item;

import com.renyigesai.immortalers_delight.util.ItemUtils;
import com.renyigesai.immortalers_delight.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.List;

public class LatiaoLuckyBagItem extends Item {
    public LatiaoLuckyBagItem() {
        super(new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemInHand = pPlayer.getItemInHand(pUsedHand);
        LootTable lootTables = WorldUtils.getLootTables("immortalers_delight:gameplay/latiao_lucky_bag", pLevel);
        List<ItemStack> fromLootTableItemStack = WorldUtils.getFromLootTableItemStack(lootTables, pLevel, pPlayer.getOnPos());
        if (!fromLootTableItemStack.isEmpty()){
            ItemUtils.givePlayerItem(pPlayer,fromLootTableItemStack.get(0));
        }
        if (!pPlayer.getAbilities().instabuild){
            itemInHand.shrink(1);
        }
        pPlayer.startUsingItem(pUsedHand);
        return InteractionResultHolder.success(itemInHand);
    }
}
