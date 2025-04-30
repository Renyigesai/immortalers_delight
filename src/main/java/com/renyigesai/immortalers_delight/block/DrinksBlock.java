package com.renyigesai.immortalers_delight.block;

import com.renyigesai.immortalers_delight.util.ItemUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class DrinksBlock extends HorizontalDirectionalBlock {
    public static final IntegerProperty PILE = IntegerProperty.create("pile",1,4);
    public DrinksBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(PILE, 1));
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return Block.box(1.0D, 0.0D, 1.0D, 15.0D, 10.0D, 15.0D);
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection());
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState stateIn, @NotNull Direction facing, @NotNull BlockState facingState, @NotNull LevelAccessor level, @NotNull BlockPos currentPos, @NotNull BlockPos facingPos) {
        return facing == Direction.DOWN && !stateIn.canSurvive(level, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, level, currentPos, facingPos);
    }
    @Override
    public boolean canSurvive(@NotNull BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).isSolid();
    }

    public int getMaxPile(){
        return 4;
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        ItemStack hand = pPlayer.getItemInHand(pHand);
        if (!pPlayer.isShiftKeyDown()){
            if (hand.is(this.asItem())){
                return pileUp(pState, pLevel, pPos, pPlayer, pHand);
            }
        }else {
            return take(pState, pLevel, pPos, pPlayer, pHand);
        }
        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    public InteractionResult pileUp(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand){
        int pile = pState.getValue(PILE);
        ItemStack hand = pPlayer.getItemInHand(pHand);
        if (pile < this.getMaxPile()){
            pLevel.setBlock(pPos,pState.setValue(PILE,pile+1),3);
            if (!pPlayer.isCreative()){
                hand.shrink(1);
            }
            pLevel.playSound(null,pPos, SoundEvents.GLASS_PLACE, SoundSource.BLOCKS);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    private InteractionResult take(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand){
        int pile = pState.getValue(PILE);
        if (pile == 1){
            pLevel.removeBlock(pPos,false);
        }else {
            pLevel.setBlock(pPos,pState.setValue(PILE,pile-1),3);
        }
        ItemUtils.givePlayerItem(pPlayer,new ItemStack(this.asItem()));
        pLevel.playSound(null,pPos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS);
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PILE,FACING);
    }
}
