package com.renyigesai.immortalers_delight.block.crops;

import com.renyigesai.immortalers_delight.block.BasicsLogsBlock;
import com.renyigesai.immortalers_delight.block.SimpleLavaloggedBlock;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;

public class AbushBlock extends Block implements SimpleLavaloggedBlock, BonemealableBlock {

    private static final VoxelShape BOX_0;
    private static final VoxelShape BOX_1;
    private static final VoxelShape BOX_2;
    private static final VoxelShape BOX_3;
    private static final VoxelShape[] SHAPE_BY_AGE;

    public static final IntegerProperty STAGE;
    public static final BooleanProperty LAVALOGGED;
    public AbushBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(STAGE,0).setValue(LAVALOGGED,false));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        Vec3 vec3 = pState.getOffset(pLevel, pPos);
        return SHAPE_BY_AGE[pState.getValue(STAGE)].move(vec3.x,vec3.y,vec3.z);
    }

    @Override
    public boolean isRandomlyTicking(BlockState pState) {
        return (pState.getValue(STAGE) < 3) || (pState.getValue(LAVALOGGED));
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        onRandomTick(pState, pLevel, pPos, pRandom);
    }

    private void onRandomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom){
        if (ForgeHooks.onCropsGrowPre(pLevel, pPos, pState, pRandom.nextInt(6) == 0)) {
            if (pState.getValue(STAGE) < 3) {
                pLevel.setBlock(pPos, pState.setValue(STAGE, pState.getValue(STAGE) + 1), 3);
            }else if (pState.getValue(LAVALOGGED)){
                BlockState newBlockState = ImmortalersDelightBlocks.A_BUSH_LOG.get().defaultBlockState().setValue(BasicsLogsBlock.AXIS, Direction.Axis.Y);
                pLevel.setBlockAndUpdate(pPos,newBlockState);
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(STAGE,LAVALOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        boolean flag = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.LAVA;
        return this.defaultBlockState().setValue(LAVALOGGED, flag);
    }


    public FluidState getFluidState(BlockState pState) {
        return pState.getValue(LAVALOGGED) ? Fluids.LAVA.getSource(false) : super.getFluidState(pState);
    }

    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return this.mayPlaceOn(pLevel.getBlockState(pPos.below()));
    }

    protected boolean mayPlaceOn(BlockState pState) {
        return pState.is(Blocks.NETHERRACK);
    }

    @Override
    public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, net.minecraftforge.common.IPlantable plantable){
        if (mayPlaceOn(state)){
            return super.canSustainPlant(state, world, pos, facing, plantable);
        }
        return false;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader, BlockPos blockPos, BlockState blockState, boolean b) {
        return blockState.getValue(STAGE) < 3;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        int value = blockState.getValue(STAGE);
        serverLevel.setBlock(blockPos,blockState.setValue(STAGE,value + 1),3);
    }

    static {
        STAGE  = IntegerProperty.create("stage",0,3);
        LAVALOGGED = SimpleLavaloggedBlock.LAVALOGGED;
        BOX_0 = box(7, 0, 7, 9, 5, 9);
        BOX_1 = box(6, 0, 6, 10, 7, 10);
        BOX_2 = box(6, 0, 6, 10, 11, 10);
        BOX_3 = box(6d,0d,6d,10d,15d,10d);
        SHAPE_BY_AGE = new VoxelShape[]{
                BOX_0,BOX_1,BOX_2,BOX_3
        };
    }
}
