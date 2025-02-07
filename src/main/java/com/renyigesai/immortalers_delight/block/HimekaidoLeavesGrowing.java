package com.renyigesai.immortalers_delight.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import static com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks.HIMEKAIDO_LEAVES;

public class HimekaidoLeavesGrowing extends LeavesBlock {
    private final LeavesBlock fruit;
    public HimekaidoLeavesGrowing(LeavesBlock pFruit, Properties p_49795_) {
        super(p_49795_);
        fruit = pFruit;
    }
    protected BooleanProperty getPersistentProperty() {
        return PERSISTENT;
    }

    public boolean getMaxAge() {
        return true;
    }

    public boolean getPersistent(BlockState p_52306_) {
        return p_52306_.getValue(this.getPersistentProperty());
    }

    public BlockState getStateForAge(boolean p_52290_) {
        return this.defaultBlockState().setValue(this.getPersistentProperty(), p_52290_);
    }

    public final boolean isMaxAge(BlockState p_52308_) {
        return !this.getPersistent(p_52308_);
    }
    /*
    重写一下随机刻申请，以便实现生长方法
     */
    @Override
    public boolean isRandomlyTicking(BlockState p_54449_) {
        return !p_54449_.getValue(PERSISTENT);
    }
    @Override
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
        if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(pLevel, pPos, pState, pRandom.nextInt(35) == 0)) {
            int distance = pState.getValue(DISTANCE);
            pLevel.setBlockAndUpdate(pPos, this.fruit.defaultBlockState().setValue(HimekaidoLeavesGrowing.DISTANCE, distance));
            System.out.println("结果方法执行完毕，当前位置" + pPos);
            net.minecraftforge.common.ForgeHooks.onCropsGrowPost(pLevel, pPos, pState);
        }
    }
    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return true;
    }
}
