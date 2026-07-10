package com.renyigesai.immortalers_delight.block.crops;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.CommonHooks;

public class TravastrugglerLeavesBlock extends LeavesBlock implements BonemealableBlock {
    public TravastrugglerLeavesBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return !state.getValue(PERSISTENT);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
        if (!level.getBlockState(pos).is(this)) {
            return;
        }
        if (!level.isAreaLoaded(pos, 1)) {
            return;
        }
        BlockState current = level.getBlockState(pos);
        if (CommonHooks.canCropGrow(level, pos, current, random.nextInt(20) == 0)) {
            growTravarice(current, level, pos);
        }
    }

    private static void growTravarice(BlockState state, ServerLevel level, BlockPos pos) {
        level.setBlockAndUpdate(pos, ImmortalersDelightBlocks.TRAVASTRUGGLER_LEAVES_TRAVARICE.get().defaultBlockState()
                .setValue(DISTANCE, state.getValue(DISTANCE))
                .setValue(PERSISTENT, state.getValue(PERSISTENT))
                .setValue(WATERLOGGED, state.getValue(WATERLOGGED)));
        CommonHooks.fireCropGrowPost(level, pos, state);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return !state.getValue(PERSISTENT);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        growTravarice(state, level, pos);
    }
}
