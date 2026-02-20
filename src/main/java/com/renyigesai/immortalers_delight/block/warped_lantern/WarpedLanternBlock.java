package com.renyigesai.immortalers_delight.block.warped_lantern;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class WarpedLanternBlock extends LanternBlock implements EntityBlock{
    //复制六朝向方块，即FaceAttachedHorizontalDirectionalBlock与其父类HorizontalDirectionalBlock的代码，实现可六方向悬挂
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<AttachFace> FACE = BlockStateProperties.ATTACH_FACE;
    protected static final VoxelShape CEILING_SHAPE = Block.box(3, 3, 3, 13, 16, 13);
    protected static final VoxelShape FLOOR_SHAPE = Block.box(3, 0, 3, 13, 13, 13);
    protected static final VoxelShape NORTH_SHAPE = Block.box(3, 3, 3, 13, 13, 16);
    protected static final VoxelShape SOUTH_SHAPE = Block.box(3, 3, 0, 13, 13, 13);
    protected static final VoxelShape WEST_SHAPE = Block.box(3, 3, 3, 16, 13, 13);
    protected static final VoxelShape EAST_SHAPE = Block.box(0, 3, 3, 13, 13, 13);

    public WarpedLanternBlock(Properties pProperties) {
        super(pProperties);

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(HANGING, Boolean.valueOf(false))
                .setValue(WATERLOGGED, Boolean.valueOf(false))
                .setValue(FACING, Direction.NORTH)
                .setValue(FACE, AttachFace.FLOOR)
        );
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, FACE);
        super.createBlockStateDefinition(pBuilder);
    }
    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return getVoxelShape(pState);
    }

    private VoxelShape getVoxelShape(BlockState pState) {
        Direction direction = pState.getValue(FACING);
        switch ((AttachFace)pState.getValue(FACE)) {
            case FLOOR:
                return FLOOR_SHAPE;
            case WALL:
                if (direction == Direction.NORTH) {
                    return NORTH_SHAPE;
                } else if (direction == Direction.SOUTH) {
                    return SOUTH_SHAPE;
                } else if (direction == Direction.EAST) {
                return EAST_SHAPE;
                } else  {
                    return WEST_SHAPE;
                }
            case CEILING:
                return CEILING_SHAPE;
            default:
                return FLOOR_SHAPE;
        }
    }
    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        if (pState.getValue(WATERLOGGED)) {
            pLevel.scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
        }

        return getDirection(pState).getOpposite() == pFacing && !pState.canSurvive(pLevel, pCurrentPos) ?
                Blocks.AIR.defaultBlockState() : pState;
    }
    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        if (state.getValue(HANGING)) return canHangingAt(world, pos, getDirection(state).getOpposite());
        else return canPlaceAt(world, pos, getDirection(state).getOpposite());
    }
    //工具方法，从当前的方块状态获取所需支持面的朝向
    protected static Direction getDirection(BlockState state) {
        switch ((AttachFace)state.getValue(FACE)) {
            case CEILING:
                return Direction.DOWN;
            case FLOOR:
                return Direction.UP;
            default:
                return (Direction)state.getValue(FACING);
        }
    }
    //放置条件：所选的面相邻的方块有完整的碰撞箱
    public static boolean canPlaceAt(LevelReader world, BlockPos pos, Direction direction) {
        BlockPos blockPos = pos.relative(direction);
        return world.getBlockState(blockPos).isFaceSturdy(world, blockPos, direction.getOpposite());
    }
    //悬挂条件：所选的面相邻的方块有中心的碰撞箱(例如横着的锁链、末地烛等等)
    public static boolean canHangingAt(LevelReader world, BlockPos pos, Direction direction) {
        BlockPos blockPos = pos.relative(direction);
        return Block.canSupportCenter(world, blockPos, direction.getOpposite());
    }

    /*
    决定放置时的状态：综合六方向方块的放置逻辑与灯的放置逻辑
    在点击的Pos遍历六个面，根据顶面/底面/侧面决定FACE为天花板、墙壁或地面状态
    如果是墙壁状态，设置对应的水平方向FACING
    如果当前的面不满足通常放置但满足悬挂放置，改为悬挂状态
    如果能让灯存活，确定为该状态，遍历完后都不满足，返还null，放置失败
     */
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        FluidState fluidstate = ctx.getLevel().getFluidState(ctx.getClickedPos());

        Direction[] var2 = ctx.getNearestLookingDirections();
        BlockState blockState = this.defaultBlockState().setValue(WATERLOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.WATER));

        for (Direction direction : var2) {
            BlockState tempState;

            if (direction.getAxis() == Direction.Axis.Y) {
                tempState = blockState.setValue(FACE, direction == Direction.UP ? AttachFace.CEILING : AttachFace.FLOOR)
                        .setValue(FACING, ctx.getHorizontalDirection());
            } else {
                 tempState = blockState.setValue(FACE, AttachFace.WALL)
                        .setValue(FACING, direction.getOpposite());
            }

            if (!canPlaceAt(ctx.getLevel(), ctx.getClickedPos(),direction) && canHangingAt(ctx.getLevel(), ctx.getClickedPos(),direction)) {
                tempState = tempState.setValue(HANGING, true);
            }

            if (tempState.canSurvive(ctx.getLevel(), ctx.getClickedPos())) {
                return tempState;
            }
        }

        return null;
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRot) {
        return pState.setValue(FACING, pRot.rotate(pState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    //=============================这一部分是实现方块功能需要的方法================================//
    @Deprecated
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity instanceof WarpedLanternBlockEntity warpedLanternBlockEntity) {
            warpedLanternBlockEntity.addToPoses();
        }
        super.onPlace(pState, pLevel, pPos, pOldState, pMovedByPiston);
    }

    /** @deprecated */
    @Deprecated
    public void onRemove(BlockState state, Level pLevel, BlockPos pPos, BlockState newState, boolean pMovedByPiston) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof WarpedLanternBlockEntity warpedLanternBlockEntity) {
                warpedLanternBlockEntity.removeFromPoses();
                pLevel.updateNeighbourForOutputSignal(pPos, this);
            }
            super.onRemove(state, pLevel, pPos, newState, pMovedByPiston);
        }

    }
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new WarpedLanternBlockEntity(pPos,pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return EntityBlock.super.getTicker(pLevel, pState, pBlockEntityType);
    }


}
