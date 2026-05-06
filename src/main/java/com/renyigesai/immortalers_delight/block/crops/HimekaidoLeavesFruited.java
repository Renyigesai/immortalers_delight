package com.renyigesai.immortalers_delight.block.crops;

import com.renyigesai.immortalers_delight.Config;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import com.renyigesai.immortalers_delight.util.BlockItemInteraction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import java.util.List;

public class HimekaidoLeavesFruited extends LeavesBlock {
    public HimekaidoLeavesFruited(Properties p_49795_) {
        super(p_49795_);
    }
    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return true;
    }

    private InteractionResult leavesUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (Config.rightClickHarvest) {
            if (level.isClientSide){
                return InteractionResult.SUCCESS;
            }
            if (level instanceof ServerLevel level1) {
                List<ItemStack> stacks = getDrops(state, level1, pos, null);
                if (!stacks.isEmpty()) {
                    for (ItemStack stack : stacks) {
                        popResource(level, pos, stack);
                    }
                    int distance = state.getValue(DISTANCE);
                    level.setBlockAndUpdate(pos, ImmortalersDelightBlocks.HIMEKAIDO_LEAVES.get().defaultBlockState().setValue(HimekaidoLeavesGrowing.DISTANCE, distance));
                    level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        InteractionResult result = leavesUse(state, level, pos, player, hand, hitResult);
        if (result != InteractionResult.PASS) {
            return BlockItemInteraction.from(level, result);
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        InteractionResult result = leavesUse(state, level, pos, player, InteractionHand.MAIN_HAND, hitResult);
        if (result != InteractionResult.PASS) {
            return result;
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }
}
