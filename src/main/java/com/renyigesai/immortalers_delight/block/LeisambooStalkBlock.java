package com.renyigesai.immortalers_delight.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;

public class LeisambooStalkBlock extends Block implements IPlantable,SimpleWaterloggedBlock,BonemealableBlock {
    public static final BooleanProperty IS_LEAVES = BooleanProperty.create("is_leaves");
    public static final BooleanProperty IS_TEA = BooleanProperty.create("is_tea");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final VoxelShape[] BOX = new VoxelShape[]{
            box(6.0D,0.0D,6.0D,10.0D,16.0D,10.0D),
            box(5.0D,0.0D,5.0D,11.0D,16.0D,11.0D)
    };

    public LeisambooStalkBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(IS_LEAVES,true).setValue(IS_TEA,false).setValue(WATERLOGGED,false));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        VoxelShape box = BOX[pState.getValue(IS_TEA) ? 1 : 0];
        Vec3 vec3 = pState.getOffset(pLevel, pPos);
        return box.move(vec3.x,vec3.y,vec3.z);
    }

    public void tick(BlockState p_222543_, ServerLevel p_222544_, BlockPos p_222545_, RandomSource p_222546_) {
        if (!p_222543_.canSurvive(p_222544_, p_222545_)) {
            p_222544_.destroyBlock(p_222545_, true);
        }

    }

    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource randomSource) {
        if (level.isEmptyBlock(pos.above())) {
            int i;
            for(i = 1; level.getBlockState(pos.below(i)).is(this); ++i) {
            }
            if (ForgeHooks.onCropsGrowPre(level, pos, state, randomSource.nextInt(3) == 0)) {
                /*最大高度小于3时尝试向上生长一次*/
//                if (i < 2){
//                    level.setBlockAndUpdate(pos.above(), this.defaultBlockState().setValue(IS_LEAVES,true));
//                    level.setBlock(pos,state.setValue(IS_LEAVES,false),3);
//                    ForgeHooks.onCropsGrowPost(level, pos.above(), this.defaultBlockState());
//                }
//                if (i == 2){
//                    level.setBlock(pos,state.setValue(IS_TEA,true),3);
//                }
                growBamboo(state,level,pos,i);
            }
        }
    }

    public void growBamboo(BlockState state, ServerLevel level, BlockPos pos,int i){
        if (i < 2){
            level.setBlockAndUpdate(pos.above(), this.defaultBlockState().setValue(IS_LEAVES,true));
            level.setBlock(pos,state.setValue(IS_LEAVES,false),3);
            ForgeHooks.onCropsGrowPost(level, pos.above(), this.defaultBlockState());
        }
        if (i == 2){
            level.setBlock(pos,state.setValue(IS_TEA,true),3);
        }
    }

    public BlockState updateShape(BlockState p_57179_, Direction p_57180_, BlockState p_57181_, LevelAccessor p_57182_, BlockPos p_57183_, BlockPos p_57184_) {
        if (!p_57179_.canSurvive(p_57182_, p_57183_)) {
            p_57182_.scheduleTick(p_57183_, this, 1);
        }

        return super.updateShape(p_57179_, p_57180_, p_57181_, p_57182_, p_57183_, p_57184_);
    }


    public boolean canSurvive(BlockState p_57175_, LevelReader p_57176_, BlockPos p_57177_) {
        BlockState soil = p_57176_.getBlockState(p_57177_.below());
        if (soil.canSustainPlant(p_57176_, p_57177_.below(), Direction.UP, this)) return true;
        BlockState blockstate = p_57176_.getBlockState(p_57177_.below());
        if (blockstate.is(this)) {
            return true;
        } else {
            if (blockstate.is(BlockTags.DIRT) || blockstate.is(BlockTags.SAND)) {
                BlockPos blockpos = p_57177_.below();

                for(Direction direction : Direction.Plane.HORIZONTAL) {
                    BlockState blockstate1 = p_57176_.getBlockState(blockpos.relative(direction));
                    FluidState fluidstate = p_57176_.getFluidState(blockpos.relative(direction));
                    if (p_57175_.canBeHydrated(p_57176_, p_57177_, fluidstate, blockpos.relative(direction)) || blockstate1.is(Blocks.FROSTED_ICE)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        boolean flag = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
        return this.defaultBlockState().setValue(WATERLOGGED, flag);
    }


    public FluidState getFluidState(BlockState pState) {
        return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(IS_LEAVES,IS_TEA,WATERLOGGED);
    }

    @Override
    public BlockState getPlant(BlockGetter blockGetter, BlockPos blockPos) {
        return defaultBlockState();
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader pLevel, BlockPos pPos, BlockState pState, boolean pIsClient) {
        int i;
        for (i = 1; pLevel.getBlockState(pPos.below(i)).is(this); i++) {

        }
        if (i == 1){
            return pLevel.getBlockState(pPos.above()).isAir();
        }else if (i == 2){
            return !pState.getValue(IS_LEAVES) || !pState.getValue(IS_TEA);
        }
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource pRandom, BlockPos pos, BlockState pState) {
        return isValidBonemealTarget(level,pos,pState,level.isClientSide);
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource pRandom, BlockPos pos, BlockState pState) {
        int i;
        for(i = 1; level.getBlockState(pos.below(i)).is(this); ++i) {
        }
        growBamboo(pState,level,pos,i);

    }
}
