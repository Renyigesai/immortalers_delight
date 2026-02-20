package com.renyigesai.immortalers_delight.block.sextlotus_lantern;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;

public class SextlotusLanternBlock extends LanternBlock implements EntityBlock {

    /**
     * 红石供电状态属性：true=接收到红石信号（范围照明关闭），false=未接收（范围照明开启）
     */
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public SextlotusLanternBlock(Properties pProperties) {
        super(pProperties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return null;
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        BlockState blockstate1 = pLevel.getBlockState(pCurrentPos.below());
        if(blockstate1.is(BlockTags.DIRT) || blockstate1.is(BlockTags.SAND) || blockstate1.getBlock() instanceof FarmBlock) return pState;
        return !pState.canSurvive(pLevel, pCurrentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }
}
