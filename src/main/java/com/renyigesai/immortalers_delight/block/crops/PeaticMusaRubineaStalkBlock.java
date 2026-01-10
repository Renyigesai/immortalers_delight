package com.renyigesai.immortalers_delight.block.crops;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

public class PeaticMusaRubineaStalkBlock extends StemBlock {
    public static final int JOINTING_STAGE_AGE = 3;
    public static final int FRUIT_STAGE_AGE = 6;
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D),
            Block.box(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D),
            Block.box(5.0D, 0.0D, 5.0D, 11.0D, 16.0D, 11.0D),
            Block.box(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D),
            Block.box(3.0D, 0.0D, 3.0D, 13.0D, 16.0D, 13.0D),
            Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D),
            Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)
    };

    /**
     * 仙人掌的碰撞箱。
     */
    protected static final VoxelShape COLLISION_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 15.0D, 15.0D);
    public PeaticMusaRubineaStalkBlock(StemGrownBlock pFruit, Supplier<Item> pSeedSupplier, Properties pProperties) {
        super(pFruit, pSeedSupplier, pProperties);
    }
//    public CretaceousZeaMaysBlock(Properties p_52247_) {
//        super(p_52247_);
//    }
//    @Override
//    protected ItemLike getBaseSeedId() {
//        return ImmortalersDelightItems.CRETACEOUS_ZEA_MAYS.get();
//    }

    /**
     * 仙人掌的碰撞箱，注册的时候记得不要加.noCollission()
     */
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return COLLISION_SHAPE;
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE_BY_AGE[pState.getValue(AGE)];
    }

    /**
     * @return 什么情况下这个方块需要生长刻。阶段0~6是生长中的，7是作为下半部分的梗，不需要生长。
     */
    public boolean isRandomlyTicking(BlockState pState) {
        return pState.getValue(AGE) < 7;
    }

    /**
     * @return 如果不能成活，安排一个tick处理。
     */
    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        if (!pState.canSurvive(pLevel, pCurrentPos)) {
            pLevel.scheduleTick(pCurrentPos, this, 1);
        }

        return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    /**
     * 使用了甘蔗的存活条件，并且还要求满足仙人掌的不可有相邻方块
     */
    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        BlockState soil = pLevel.getBlockState(pPos.below());
        if (soil.canSustainPlant(pLevel, pPos.below(), Direction.UP, this)) return true;

        /*
         *如果下方也是香蕉梗，判定仙人掌的存活条件
         */
        BlockState blockstate = pLevel.getBlockState(pPos.below());
        if (blockstate.is(this)) {
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                blockstate = pLevel.getBlockState(pPos.relative(direction));
                if (blockstate.isSolid() || pLevel.getFluidState(pPos.relative(direction)).is(FluidTags.LAVA)) {
                    return false;
                }
            }
            return true;
            /*
             *如果不是，判断甘蔗的存活条件
             */
        } else {
            if (blockstate.is(BlockTags.DIRT) || blockstate.is(BlockTags.SAND)) {
                BlockPos blockpos = pPos.below();

                for(Direction direction : Direction.Plane.HORIZONTAL) {
                    BlockState blockstate1 = pLevel.getBlockState(blockpos.relative(direction));
                    FluidState fluidstate = pLevel.getFluidState(blockpos.relative(direction));
                    if (pState.canBeHydrated(pLevel, pPos, fluidstate, blockpos.relative(direction)) || blockstate1.is(Blocks.FROSTED_ICE)) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    /**
     * 重写的生长刻行为，修改了生长速度并且使得香蕉拔高为2格高的茎秆，以及向上结果
     */
    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (!pLevel.isAreaLoaded(pPos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
        if (pLevel.getRawBrightness(pPos, 0) >= 9) {
            if (pLevel.getBlockState(pPos.below()).is(this) && !(pLevel.getBlockState(pPos.below()).getValue(AGE) == 7)) {
                pLevel.setBlock(pPos.below(), pState.setValue(AGE, MAX_AGE), 2);
            }
            /*
            *使用了竹子的ForgeHooks，肥肠合理的生长速度，主要是不能密植已经失去了getGrowthSpeed的意义
             */
            if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(pLevel, pPos, pState, pRandom.nextInt(3) == 0)) {
                int i = pState.getValue(AGE);
                /*
                 *0 1 2 3 4 5 6为生长中的阶段，其中3和6分别执行拔高和结果的操作。
                 */
                if (i < FRUIT_STAGE_AGE && i != JOINTING_STAGE_AGE) {
                    /*
                     *不在特殊的生长阶段，AGE+1此事平平无奇。
                     */
                    pLevel.setBlock(pPos, pState.setValue(AGE, Integer.valueOf(i + 1)), 2);
                } else if (pLevel.isEmptyBlock(pPos.above())) {
                    /*
                      达到生长阶段3，使向上拔高：在上方一格复制自身，然后将自身设置为AGE_7的下半部分梗
                     */
                    if (pLevel.getBlockState(pPos.below()).getValue(AGE) == JOINTING_STAGE_AGE) {
                        pLevel.setBlockAndUpdate(pPos.above(), pState);
                        pLevel.setBlock(pPos, pState.setValue(AGE, MAX_AGE), 2);
                    }
                    /*
                      达到生长阶段6，使向上结果
                     */
                    if (pLevel.getBlockState(pPos.below()).getValue(AGE) == FRUIT_STAGE_AGE) {
                        pLevel.setBlockAndUpdate(pPos.above(), this.getFruit().defaultBlockState());
                    }
                }
                net.minecraftforge.common.ForgeHooks.onCropsGrowPost(pLevel, pPos, pState);
            }

        }
    }

    @Override
    public InteractionResult use(BlockState p_60503_, Level p_60504_, BlockPos p_60505_, Player p_60506_, InteractionHand p_60507_, BlockHitResult p_60508_) {
        return super.use(p_60503_, p_60504_, p_60505_, p_60506_, p_60507_, p_60508_);
    }
}