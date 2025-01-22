package com.renyigesai.immortalers_delight.block;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;

public class BananaStalkBlock extends Block implements IPlantable {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_7;
    public static final BooleanProperty IS_FRUIT = BooleanProperty.create("is_fruit");
    public BananaStalkBlock(Properties p_49795_) {
        super(p_49795_);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0).setValue(IS_FRUIT,false));
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
                int age = state.getValue(AGE);
                boolean is_fruit = state.getValue(IS_FRUIT);
                /*最大高度小于4时尝试向上生长一次*/
                if (i < 4){
                    /*每个茎age需达到7才能向上生长*/
                    if (age < 7){
                        level.setBlock(pos, state.setValue(AGE, age + 1), 3);
                        ForgeHooks.onCropsGrowPost(level, pos.above(), this.defaultBlockState());
                    }else {
                            level.setBlock(pos, state.setValue(AGE, 0), 3);
                            level.setBlockAndUpdate(pos.above(),
                                    ImmortalersDelightBlocks.BANANA_STALK.get().defaultBlockState());
                    }
                    /*到达最大高度时再开始尝试结果（单独的一个方块状态后面调用模型会更简单）*/
                }else {
                    level.setBlock(pos, state.setValue(IS_FRUIT, true), 3);
                }
                if (is_fruit){
                    /*尝试结果，不太会方向，后期需要更改*/
                    Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(randomSource);
                    BlockPos blockpos = pos.relative(direction);
                    BlockState blockstate1 = level.getBlockState(blockpos.east());
                    BlockState blockstate2 = level.getBlockState(blockpos.south());
                    BlockState blockstate3 = level.getBlockState(blockpos.west());
                    BlockState blockstate4 = level.getBlockState(blockpos.north());
                    boolean temp = blockstate1.isAir() ||
                                   blockstate2.isAir() ||
                                   blockstate3.isAir() ||
                                   blockstate4.isAir();
                    if (temp){
                        level.setBlockAndUpdate(blockpos, Blocks.STONE.defaultBlockState());
                    }
                }
            }
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(AGE,IS_FRUIT);
    }

    @Override
    public BlockState getPlant(BlockGetter world, BlockPos pos) {
        return defaultBlockState();
    }
}
