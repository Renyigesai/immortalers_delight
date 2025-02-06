package com.renyigesai.immortalers_delight.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import static com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks.HIMEKAIDO_LEAVES;

public class HimekaidoLeavesFruited extends LeavesBlock {
    public HimekaidoLeavesFruited(Properties p_49795_) {
        super(p_49795_);
    }

    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        /*
        执行树叶的腐烂行为
         */
        if (this.decaying(pState)) {
            dropResources(pState, pLevel, pPos);
            pLevel.removeBlock(pPos, false);
            return;
        }
        /*
        执行生长逻辑
         */
        if (!pLevel.isAreaLoaded(pPos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
        if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(pLevel, pPos, pState, pRandom.nextInt(10) == 0)) {
            HimekaidoLeavesGrowing leavesToSet = new HimekaidoLeavesGrowing(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES));
            pLevel.setBlock(pPos, leavesToSet.getStateForAge(HimekaidoLeavesGrowing.MAX_AGE), 2);
            net.minecraftforge.common.ForgeHooks.onCropsGrowPost(pLevel, pPos, pState);
        } else {
            pLevel.setBlock(pPos, HIMEKAIDO_LEAVES.get().defaultBlockState(),2);
        }
    }
    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return true;
    }
}
