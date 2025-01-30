package com.renyigesai.immortalers_delight.block;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;

import javax.annotation.Nullable;
public class BasicsLogsBlock extends RotatedPillarBlock {
    public BasicsLogsBlock(Properties p_49795_) {
        super(p_49795_);
    }

    public @Nullable BlockState getToolModifiedState (BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate) {
        ItemStack handStack = context.getItemInHand();
        if (handStack.is(ItemTags.AXES)) {
            if (state.is(ImmortalersDelightBlocks.HIMEKAIDO_LOG.get())) {
                return ImmortalersDelightBlocks.STRIPPED_HIMEKAIDO_LOG.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
            } else if (state.is(ImmortalersDelightBlocks.HIMEKAIDO_WOOD.get())) {
                return ImmortalersDelightBlocks.STRIPPED_HIMEKAIDO_WOOD.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
            }
            return super.getToolModifiedState(state, context, toolAction, simulate);
        }

        return super.getToolModifiedState(state, context, toolAction, simulate);
    }
}
