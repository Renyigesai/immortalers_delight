package com.renyigesai.immortalers_delight.block.crops;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class HimekaidoLeavesGrowing extends LeavesBlock {
    private final LeavesBlock fruit;
    public static final BooleanProperty GROW = BooleanProperty.create("grow");
    public HimekaidoLeavesGrowing(LeavesBlock pFruit, Properties p_49795_) {
        super(p_49795_);
        fruit = pFruit;
        this.registerDefaultState(this.stateDefinition.any().setValue(DISTANCE, Integer.valueOf(7)).setValue(PERSISTENT, Boolean.valueOf(false)).setValue(WATERLOGGED, Boolean.valueOf(false)).setValue(GROW,true));
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
        if (!pState.getValue(GROW)) return;
        if (!pLevel.isAreaLoaded(pPos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
        if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(pLevel, pPos, pState, pRandom.nextInt(35) == 0)) {
            int distance = pState.getValue(DISTANCE);
            pLevel.setBlockAndUpdate(pPos, this.fruit.defaultBlockState().setValue(HimekaidoLeavesGrowing.DISTANCE, distance));
            net.minecraftforge.common.ForgeHooks.onCropsGrowPost(pLevel, pPos, pState);
        }
    }
    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return true;
    }

    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
//        super.getStateForPlacement()
        FluidState fluidstate = pContext.getLevel().getFluidState(pContext.getClickedPos());
//        BlockState blockstate = this.defaultBlockState().setValue(PERSISTENT, Boolean.valueOf(true)).setValue(WATERLOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.WATER));
//        return super.getStateForPlacement();
        return this.defaultBlockState().setValue(PERSISTENT,true).setValue(WATERLOGGED,fluidstate.getType() == Fluids.WATER).setValue(GROW,false);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(DISTANCE, PERSISTENT, WATERLOGGED, GROW);
    }
}
