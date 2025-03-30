package com.renyigesai.immortalers_delight.event;

import com.renyigesai.immortalers_delight.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber
public class BlockBreakEvent {
    @SubscribeEvent
    public static void onBreakEvent(BlockEvent.BreakEvent event){
        LevelAccessor level = event.getLevel();
        BlockState state = event.getState();
        BlockPos pos = event.getPos();
        Block block = state.getBlock();
        Player player = event.getPlayer();
        if (level instanceof Level level1) {
            List<ItemStack> stacks = null;
            if (block == Blocks.PITCHER_CROP) {
                stacks = WorldUtils.getFromLootTableItemStack(WorldUtils.getLootTables("immortalers_delight:blocks/pitcher_add",level1),level1,pos);
            } else if (block == Blocks.TORCHFLOWER_CROP) {
                stacks = WorldUtils.getFromLootTableItemStack(WorldUtils.getLootTables("immortalers_delight:blocks/torchflower_crop_add",level1),level1,pos);
            } else if (block == Blocks.TORCHFLOWER) {
                stacks = WorldUtils.getFromLootTableItemStack(WorldUtils.getLootTables("immortalers_delight:blocks/torchflower_add",level1),level1,pos);
            }
            if (stacks != null) {
                popResource(stacks, level1, pos);
            }
        }
    }

    private static void popResource(List<ItemStack> stacks,Level level,BlockPos pos){
        for (ItemStack stack : stacks) {
            Block.popResource(level,pos,stack);
        }
    }
}
