package com.renyigesai.immortalers_delight.block;

import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class BasicsLogsBlock extends RotatedPillarBlock {

    public static final Supplier<BiMap<Block, Block>> LOGS;
    public BasicsLogsBlock(Properties p_49795_) {
        super(p_49795_);
    }

    public @Nullable BlockState getToolModifiedState (BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate) {
        ItemStack handStack = context.getItemInHand();
        if (handStack.is(ItemTags.AXES)) {
            Block block = BasicsLogsBlock.LOGS.get().get(state.getBlock());
            if (block != null){
                BlockState newState = block.defaultBlockState().setValue(AXIS, state.getValue(AXIS));
                return newState;
            }
            return super.getToolModifiedState(state, context, toolAction, simulate);
        }
        return super.getToolModifiedState(state, context, toolAction, simulate);
    }

    static {
        LOGS = Suppliers.memoize(() -> ImmutableBiMap.<Block, Block>builder()
                .put(ImmortalersDelightBlocks.HIMEKAIDO_LOG.get(),ImmortalersDelightBlocks.STRIPPED_HIMEKAIDO_LOG.get())
                .put(ImmortalersDelightBlocks.HIMEKAIDO_WOOD.get(),ImmortalersDelightBlocks.STRIPPED_HIMEKAIDO_WOOD.get())
                .put(ImmortalersDelightBlocks.ANCIENT_WOOD_LOG.get(),ImmortalersDelightBlocks.STRIPPED_ANCIENT_WOOD_LOG.get())
                .put(ImmortalersDelightBlocks.ANCIENT_WOOD.get(),ImmortalersDelightBlocks.STRIPPED_ANCIENT_WOOD.get())
                .put(ImmortalersDelightBlocks.TRAVASTRUGGLER_LOG.get(),ImmortalersDelightBlocks.STRIPPED_TRAVASTRUGGLER_LOG.get())
                .put(ImmortalersDelightBlocks.A_BUSH_LOG.get(),ImmortalersDelightBlocks.STRIPPED_A_BUSH_LOG.get())
                .put(ImmortalersDelightBlocks.A_BUSH_WOOD.get(),ImmortalersDelightBlocks.STRIPPED_A_BUSH_WOOD.get())
                .build());
    }
}
