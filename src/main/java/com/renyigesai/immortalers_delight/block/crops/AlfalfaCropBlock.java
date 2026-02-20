package com.renyigesai.immortalers_delight.block.crops;

import com.renyigesai.immortalers_delight.block.ReapCropBlock;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AlfalfaCropBlock extends ReapCropBlock {

    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
            Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D),
            Block.box(2.0D, 2.0D, 2.0D, 14.0D, 16.0D, 14.0D),
            Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
    };

    public AlfalfaCropBlock(Properties p_52247_) {
        super(p_52247_);
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ImmortalersDelightItems.ALFALFA_SEEDS.get();
    }

    public VoxelShape getShape(BlockState p_51330_, BlockGetter p_51331_, BlockPos p_51332_, CollisionContext p_51333_) {
        return SHAPE_BY_AGE[this.getAge(p_51330_)];
    }

    /**
     * 随机刻逻辑（游戏随机触发的生长行为）
     * 注：@SuppressWarnings("deprecation") 屏蔽MC旧API的弃用警告
     * @param state 方块当前状态
     * @param worldIn 服务端世界实例（仅服务端执行随机刻）
     * @param pos 方块坐标
     * @param random 随机数源
     */
    @Override
    @SuppressWarnings("deprecation")
    public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource random) {
        if (!worldIn.isAreaLoaded(pos, 1)) return;
        if (!isValidBonemealTarget(worldIn, pos, state, false)) return;

        if (worldIn.getRawBrightness(pos,0) >= 9) {
            float f = CropBlock.getGrowthSpeed(this, worldIn, pos);
            if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, random.nextInt((int) (25.0F / f) + 1) == 0)) {
                int currentAge = state.getValue(AGE);
                if (currentAge < this.getMaxAge()) {
                    //实现作物的生长，在生长速度不足时不能达到成熟
                    int newAge = currentAge + 1;
                    if (f < 3 && newAge >= this.getMaxAge()) newAge = this.getMaxAge() - 1;
                    worldIn.setBlock(pos, state.setValue(AGE, newAge), Block.UPDATE_CLIENTS);

                    //实现蔓延到别处的特性，在生长速度不足时仅会蔓延到耕地上
                    if (currentAge > 0) {
                        BlockPos randomPos = pos.relative(Direction.Plane.HORIZONTAL.getRandomDirection(random));
                        BlockState randomPosState = worldIn.getBlockState(randomPos);
                        if (randomPosState.isAir() || randomPosState.is(Blocks.DEAD_BUSH)) {
                            boolean canPlant = false;
                            if (this.canSurvive(state, worldIn, randomPos)) {canPlant = true;}
                            else if (f > 1){
                                BlockState blockstate1 = worldIn.getBlockState(randomPos.below());
                                if (blockstate1.is(BlockTags.DIRT) || blockstate1.is(BlockTags.SAND) || blockstate1.getBlock() instanceof FarmBlock) canPlant = true;
                            }

                            if (canPlant) worldIn.setBlockAndUpdate(randomPos, this.defaultBlockState());
                        }
                    }
                }
                net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state);
            }
        }
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        BlockState blockstate1 = pLevel.getBlockState(pCurrentPos.below());
        if(blockstate1.is(BlockTags.DIRT) || blockstate1.is(BlockTags.SAND) || blockstate1.getBlock() instanceof FarmBlock) return pState;
        return !pState.canSurvive(pLevel, pCurrentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }
}
