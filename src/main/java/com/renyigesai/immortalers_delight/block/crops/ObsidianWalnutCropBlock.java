package com.renyigesai.immortalers_delight.block.crops;

import com.renyigesai.immortalers_delight.Config;
import com.renyigesai.immortalers_delight.block.ReapCropBlock;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.util.TriState;
import org.jetbrains.annotations.NotNull;

public class ObsidianWalnutCropBlock extends ReapCropBlock {
    public ObsidianWalnutCropBlock(Properties p_52247_) {
        super(p_52247_);
        this.registerDefaultState(this.defaultBlockState().setValue(AGE, 0));
    }

    @Override
    protected @NotNull ItemLike getBaseSeedId() {
        return ImmortalersDelightItems.OBSIDIAN_WALNUT.get();
    }

    @Override
    protected boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        BlockPos blockpos = pPos.below();
        BlockState soil = pLevel.getBlockState(blockpos);
        if (pState.getBlock() == this) {
            TriState soilDecision = soil.canSustainPlant(pLevel, blockpos, Direction.UP, pState);
            if (!soilDecision.isDefault()) {
                return soilDecision.isTrue();
            }
        }
        return this.mayPlaceOn(soil, pLevel, blockpos);
    }

    @Override
    public boolean isRandomlyTicking(BlockState pState) {
        return pState.getValue(this.getAgeProperty()) < this.getMaxAge();
    }

    @Override
    public int getMaxAge() {
        return 5;
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (!pLevel.isAreaLoaded(pPos, 1)) return;
        if (pLevel.getRawBrightness(pPos, 0) >= 12) {
            int i = this.getAge(pState);
            if (i < this.getMaxAge()) {
                float f = getGrowthSpeed(pState, pLevel, pPos);
                if (CommonHooks.canCropGrow(pLevel, pPos, pState, pRandom.nextInt((int) (35.0F / f) + 1) == 0)) {
                    pLevel.setBlock(pPos, this.getStateForAge(i + 1), 2);
                    CommonHooks.fireCropGrowPost(pLevel, pPos, pState);
                }
            }
        }
    }

    @Override
    public boolean canReap(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return Config.rightClickHarvest && state.getValue(AGE) >= getMaxAge();
    }

    @Override
    protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return pState.is(Tags.Blocks.NETHERRACKS) || pState.is(Blocks.SOUL_SAND) || pState.is(Blocks.SOUL_SOIL);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
    }
}
