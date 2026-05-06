package com.renyigesai.immortalers_delight.api.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.Event;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class SnifferDropSeedEvent extends Event {
    private final Level level;
    private final BlockPos blockPos;
    @Nullable
    private final Sniffer sniffer;

    private List<ItemStack> stacks;

    public SnifferDropSeedEvent(Level level, BlockPos blockPos, List<ItemStack> stacks) {
        this(level, blockPos, stacks, null);
    }

    public SnifferDropSeedEvent(Level level, BlockPos blockPos, List<ItemStack> stacks, @Nullable Sniffer sniffer) {
        this.level = level;
        this.blockPos = blockPos;
        this.stacks = stacks;
        this.sniffer = sniffer;
    }

    public Level getLevel() {
        return level;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    /**
     * Present when fired from {@link com.renyigesai.immortalers_delight.mixin.SnifferMixin} (normal digging flow).
     */
    @Nullable
    public Sniffer getSniffer() {
        return sniffer;
    }

    public List<ItemStack> getStacks() {return stacks;}

    public void setStacks(List<ItemStack> stacks){
        this.stacks = stacks;
    }


}
