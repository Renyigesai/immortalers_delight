package com.renyigesai.immortalers_delight.block;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class PeaticMusaRubineaBundleBlock extends StemGrownBlock implements BonemealableBlock {
    /**
     * 注册香蕉梗标签，用于判断与香蕉梗的依附关系。标签的注册方法大概是这样？添加方块到标签似乎在数据包实现。如果要统一管理，你可以移到Init去。
     */
    public static final TagKey<Block> BANANA_STALK = BlockTags.create(new ResourceLocation("maintains_farmland"));
    public static final int MAX_AGE = 2;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_2;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    /**
     * 一堆不知所谓的东西，由于是复制了可可豆类而不是继承，所以原来的代码即使没有用到也没有删除。
     */
    protected static final int AGE_0_WIDTH = 4;
    protected static final int AGE_0_HEIGHT = 5;
    protected static final int AGE_0_HALFWIDTH = 2;
    protected static final int AGE_1_WIDTH = 6;
    protected static final int AGE_1_HEIGHT = 7;
    protected static final int AGE_1_HALFWIDTH = 3;
    protected static final int AGE_2_WIDTH = 8;
    protected static final int AGE_2_HEIGHT = 9;
    protected static final int AGE_2_HALFWIDTH = 4;
    protected static final VoxelShape[] EAST_AABB = new VoxelShape[]{
            Block.box(7.0D, 0.0D, 4.0D, 15.0D, 12.0D, 12.0D),
            Block.box(5.0D, 0.0D, 3.0D, 15.0D, 14.0D, 13.0D),
            Block.box(3.0D, 0.0D, 2.0D, 15.0D, 16.0D, 14.0D)
    };
    protected static final VoxelShape[] WEST_AABB = new VoxelShape[]{
            Block.box(1.0D, 0.0D, 4.0D, 9.0D, 12.0D, 12.0D),
            Block.box(1.0D, 0.0D, 3.0D, 11.0D, 14.0D, 13.0D),
            Block.box(1.0D, 0.0D, 2.0D, 13.0D, 16.0D, 14.0D)
    };
    protected static final VoxelShape[] NORTH_AABB = new VoxelShape[]{
            Block.box(4.0D, 0.0D, 1.0D, 12.0D, 12.0D, 9.0D),
            Block.box(3.0D, 0.0D, 1.0D, 13.0D, 14.0D, 11.0D),
            Block.box(2.0D, 0.0D, 1.0D, 14.0D, 16.0D, 13.0D)
    };
    protected static final VoxelShape[] SOUTH_AABB = new VoxelShape[]{
            Block.box(4.0D, 0.0D, 7.0D, 12.0D, 12.0D, 15.0D),
            Block.box(3.0D, 0.0D, 5.0D, 13.0D, 14.0D, 15.0D),
            Block.box(2.0D, 0.0D, 3.0D, 14.0D, 16.0D, 15.0D)
    };

    public PeaticMusaRubineaBundleBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(AGE, Integer.valueOf(0)));
    }

    @Override
    public StemBlock getStem() {
        return (StemBlock)ImmortalersDelightBlocks.PEARLIPEARL_STALK.get();
    }

    @Override
    public AttachedStemBlock getAttachedStem() {
        return (AttachedStemBlock)Blocks.ATTACHED_PUMPKIN_STEM;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, AGE);
    }

    /**
     * @return 可否寻路。
     */
    public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType) {
        return false;
    }

    public int getMaxAge() {
        return MAX_AGE;
    }


    public @NotNull IntegerProperty getAgeProperty() {
        return AGE;
    }

    /**
     * @return 什么情况下这个方块需要生长刻。
     */
    public boolean isRandomlyTicking(BlockState pState) {
        return pState.getValue(AGE) < 2;
    }

    /**
     * 生长刻的行为，如果生长阶段不满，且满足光照，长大（AGE+1），并随机变换方向（取random设置FACING）。生长速度沿用可可豆。
     */
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (isRandomlyTicking) {
            int i = pState.getValue(AGE);
            if (pLevel.getRawBrightness(pPos, 0) >= 8 || pLevel.canSeeSky(pPos) &&
                    net.minecraftforge.common.ForgeHooks.onCropsGrowPre(pLevel, pPos, pState, i == 0 || pLevel.random.nextInt(5) == 0)) {
                Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(pRandom);
                pLevel.setBlock(pPos, pState.setValue(AGE, i + 1).setValue(FACING, direction), 2);
                net.minecraftforge.common.ForgeHooks.onCropsGrowPost(pLevel, pPos, pState);
            }
        }

    }

    /**
     * 判断是否能成活，需要依附于香蕉梗。
     */
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        BlockState blockstate = pLevel.getBlockState(pPos.below());
        return blockstate.is(BANANA_STALK);
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        int i = pState.getValue(AGE);
        switch ((Direction)pState.getValue(FACING)) {
            case SOUTH:
                return SOUTH_AABB[i];
            case NORTH:
            default:
                return NORTH_AABB[i];
            case WEST:
                return WEST_AABB[i];
            case EAST:
                return EAST_AABB[i];
        }
    }

    /**
     * 根据放置上下文获取方块的放置状态，尝试将方块放置在合适的方向（水平方向）并检查是否能存活，应该是使用可可豆种植其方块使用，但这个方块不打算注册对应物品。
     */
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockState blockstate = this.defaultBlockState();
        LevelReader levelreader = pContext.getLevel();
        BlockPos blockpos = pContext.getClickedPos();

        for(Direction direction : pContext.getNearestLookingDirections()) {
            if (direction.getAxis().isHorizontal()) {
                blockstate = blockstate.setValue(FACING, direction);
                if (blockstate.canSurvive(levelreader, blockpos)) {
                    return blockstate;
                }
            }
        }

        return null;
    }

    /**
     * 根据邻居方块状态更新当前方块状态，如果方块不能存活且邻居方块方向与自身相同，则变为空气方块，否则调用父类方法更新。沿用可可豆的。
     */
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        return pFacing == pState.getValue(FACING) && !pState.canSurvive(pLevel, pCurrentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    /**
     * @return 可否使用骨粉。
     */
    public boolean isValidBonemealTarget(LevelReader pLevel, BlockPos pPos, BlockState pState, boolean pIsClient) {
        return pState.getValue(AGE) < 2;
    }

    public boolean isBonemealSuccess(Level pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
        return true;
    }

    public void performBonemeal(ServerLevel pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
        pLevel.setBlock(pPos, pState.setValue(AGE, Integer.valueOf(pState.getValue(AGE) + 1)), 2);
    }
}
