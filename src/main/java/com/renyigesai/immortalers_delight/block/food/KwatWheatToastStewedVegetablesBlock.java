package com.renyigesai.immortalers_delight.block.food;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.util.ItemUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
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

public class KwatWheatToastStewedVegetablesBlock extends HorizontalDirectionalBlock {
    public static final IntegerProperty BITES = IntegerProperty.create("bites",0,6);
    public static final VoxelShape BOX = box(3.0D,0.0D,3.0D,13.0D,10.0D,13.0D);

    public KwatWheatToastStewedVegetablesBlock(Properties pProperties) {
        super(pProperties);
        super.registerDefaultState(defaultBlockState().setValue(BITES,0).setValue(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return BOX;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return eat(state, level, pos, player,hand);
    }

    public InteractionResult eat(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand){
        ItemStack handItem = player.getItemInHand(hand);
        if (!handItem.is(Items.BOWL)){
            player.displayClientMessage(Component.translatable("block.immortalers_delight.spoon_block.tips.1"), true);
            return InteractionResult.PASS;
        }
        int bites = state.getValue(BITES);
        if (bites < 3){
            ItemUtils.givePlayerItem(player,new ItemStack(ImmortalersDelightItems.NETHER_CREAM_SOUP.get()));
        }else {
            ItemUtils.givePlayerItem(player,new ItemStack(ImmortalersDelightItems.NETHER_CREAM_BREAD.get()));
        }
        if (!player.getAbilities().instabuild){
            handItem.shrink(1);
        }
        if (bites < getMaxBites() - 1){
            level.setBlock(pos, state.setValue(BITES, bites + 1), 3);
        }else {
            level.removeBlock(pos, false);
        }
        level.playSound(null,pos, SoundEvents.WOOL_BREAK, SoundSource.BLOCKS, 0.8F, 0.8F);
        return InteractionResult.SUCCESS;
    }

    public int getMaxBites(){
        return 6;
    }

    public void setBlock(int variate,BlockState state, Level level, BlockPos pos){
        level.setBlock(pos, state.setValue(BITES, variate + 1), 3);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection());
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        return facing == Direction.DOWN && !stateIn.canSurvive(level, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).isSolid();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BITES,FACING);
    }
}
