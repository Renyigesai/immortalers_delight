package com.renyigesai.immortalers_delight.api.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.Event;

import java.util.List;


public class SnifferDropSeedEvent extends Event {
    private final Level level;
    private final BlockPos blockPos;

    private List<ItemStack> stacks;

    public SnifferDropSeedEvent(Level level, BlockPos blockPos, List<ItemStack> stacks) {
        this.level = level;
        this.blockPos = blockPos;
        this.stacks = stacks;
    }

    public Level getLevel() {
        return level;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public List<ItemStack> getStacks() {return stacks;}

    public void setStacks(List<ItemStack> stacks){
        this.stacks = stacks;
    }


}
