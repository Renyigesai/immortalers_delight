package com.renyigesai.immortalers_delight.block;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class BasicsLogsBlock extends RotatedPillarBlock {
    public BasicsLogsBlock(Properties p_49795_) {
        super(p_49795_);
    }

    public @Nullable BlockState getToolModifiedState (BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate) {
        ItemStack handStack = context.getItemInHand();
        if (handStack.is(ItemTags.AXES)) {
            if (BasicsLogsBlock.getLog().get(state) != null){
                return BasicsLogsBlock.getLog().get(state).defaultBlockState().setValue(AXIS, state.getValue(AXIS));
            }
            return super.getToolModifiedState(state, context, toolAction, simulate);
        }

        return super.getToolModifiedState(state, context, toolAction, simulate);
    }

    public static Map<BlockState, Block> getLog(){
        Map<BlockState,Block> LOG = new HashMap<>();

        LOG.put(ImmortalersDelightBlocks.HIMEKAIDO_LOG.get().defaultBlockState(),ImmortalersDelightBlocks.STRIPPED_HIMEKAIDO_LOG.get());

        LOG.put(ImmortalersDelightBlocks.HIMEKAIDO_WOOD.get().defaultBlockState(),ImmortalersDelightBlocks.STRIPPED_HIMEKAIDO_WOOD.get());

        LOG.put(ImmortalersDelightBlocks.ANCIENT_WOOD_LOG.get().defaultBlockState(),ImmortalersDelightBlocks.STRIPPED_ANCIENT_WOOD_LOG.get());

        LOG.put(ImmortalersDelightBlocks.ANCIENT_WOOD.get().defaultBlockState(),ImmortalersDelightBlocks.STRIPPED_ANCIENT_WOOD.get());

        return LOG;
    }
}
