package com.renyigesai.immortalers_delight.block.food;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.util.ItemUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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

public class PearlipRiceRollBoatBlock extends HorizontalDirectionalBlock {
    public static final IntegerProperty SERVINGS = IntegerProperty.create("servings",0,5);
    public PearlipRiceRollBoatBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(defaultBlockState().setValue(SERVINGS,5).setValue(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            default -> box(1, 0, 3, 15, 2, 14);
            case NORTH -> box(1, 0, 2, 15, 2, 13);
            case EAST -> box(3, 0, 1, 14, 2, 15);
            case WEST -> box(2, 0, 1, 13, 2, 15);
        };
    }

    private ItemStack getRiceRoll(int i){
        return !(i % 2 == 0)?new ItemStack(ImmortalersDelightItems.BANANA_BOX_SALMON.get()):new ItemStack(ImmortalersDelightItems.BANANA_BOX_COD.get());
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        return take(pState, pLevel, pPos, pPlayer, pHand);
    }

    private InteractionResult take(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand){
        int servings = pState.getValue(SERVINGS);
        if (servings == 0){
            vectorwing.farmersdelight.common.utility.ItemUtils.spawnItemEntity(pLevel,new ItemStack(ImmortalersDelightItems.PEARLIP_SHELL.get()),(double)pPos.getX() + 0.5, (double)pPos.getY() + 0.3, (double)pPos.getZ() + 0.5, 0.0,0.0,0.0);
            vectorwing.farmersdelight.common.utility.ItemUtils.spawnItemEntity(pLevel,new ItemStack(Items.WHITE_BANNER),(double)pPos.getX() + 0.5, (double)pPos.getY() + 0.3, (double)pPos.getZ() + 0.5, 0.0,0.0,0.0);
            pLevel.removeBlock(pPos,false);
        }else {
            pLevel.setBlock(pPos,pState.setValue(SERVINGS,servings-1),3);
            ItemStack stack = this.getRiceRoll(servings);
            ItemUtils.givePlayerItem(pPlayer,stack);
        }
        pLevel.playSound(null,pPos, SoundEvents.WOOL_BREAK, SoundSource.BLOCKS);
        return InteractionResult.SUCCESS;
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

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(SERVINGS,FACING);
    }
}
