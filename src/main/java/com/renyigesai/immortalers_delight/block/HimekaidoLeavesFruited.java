package com.renyigesai.immortalers_delight.block;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AttachedStemBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import vectorwing.farmersdelight.common.registry.ModSounds;

import java.util.List;

public class HimekaidoLeavesFruited extends LeavesBlock {
    public HimekaidoLeavesFruited(Properties p_49795_) {
        super(p_49795_);
    }
    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return true;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
            if (level instanceof ServerLevel level1) {
                List<ItemStack> stacks = getDrops(state, level1, pos, null);
                if (!stacks.isEmpty()){
                    for (ItemStack stack : stacks) {
                        popResource(level, pos, stack);
                    }
                    level.setBlockAndUpdate(pos, ImmortalersDelightBlocks.HIMEKAIDO_LEAVES.get().defaultBlockState());
                    return InteractionResult.SUCCESS;
                }
            }
        return super.use(state, level, pos, player, hand, hitResult);
    }
}
