package com.renyigesai.immortalers_delight.block;

import com.renyigesai.immortalers_delight.util.ItemUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

public class SpoonBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty SERVINGS = IntegerProperty.create("servings", 0, 3);
    public static final VoxelShape BOX = box(0.0D,0.0D,0.0D,16.0D,16.0D,16.0D);

    public final Supplier<Item> spoonItem;

    public SpoonBlock(BlockBehaviour.Properties p_49795_, Supplier<Item> spoonItem) {
        super(p_49795_);
        this.spoonItem = spoonItem;
        this.registerDefaultState(defaultBlockState().setValue(SERVINGS,3).setValue(FACING, Direction.NORTH));

    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return BOX;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult p_60508_) {
        return level.isClientSide && this.takeServing(state, level, pos, player, hand).consumesAction() ? InteractionResult.SUCCESS : this.takeServing(state, level, pos, player, hand);
    }

//    public ItemStack getSpoonItem(){
//        return spoonItem;
//    }

    public ItemStack getSpoonItem() {
        return new ItemStack(this.spoonItem.get());
    }

    public ItemStack getRemainItem() {
        return new ItemStack(Items.BOWL);
    }

//    public ItemStack getRemainItem(){
//        return remainItem;
//    }

    public InteractionResult takeServing(BlockState state, Level level, BlockPos pos, Player player,InteractionHand hand) {
        int servings = state.getValue(SERVINGS);
        if (servings > 0) {
            ItemStack hand_stack = player.getItemInHand(hand);
            if (hand_stack.is(Items.BOWL)) {
                if (!player.getAbilities().instabuild) {
                    hand_stack.shrink(1);
                }
                level.setBlock(pos, state.setValue(SERVINGS, servings - 1), 3);
                ItemUtils.givePlayerItem(player,this.getSpoonItem());
                level.playSound(null,pos, SoundEvents.WOOL_BREAK, SoundSource.PLAYERS, 0.8F, 0.8F);
            }else {
                player.displayClientMessage(Component.translatable("block.immortalers_delight.spoon_block.tips.1"), true);
                return InteractionResult.FAIL;
            }
            return InteractionResult.SUCCESS;
        }
        if (servings == 0){
            level.destroyBlock(pos, false);
            dropRemainItem(level,pos);
            level.playSound(null,pos, SoundEvents.WOOL_BREAK, SoundSource.PLAYERS, 0.8F, 0.8F);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public void dropRemainItem(Level level, BlockPos pos){
        vectorwing.farmersdelight.common.utility.ItemUtils.spawnItemEntity(level,
                this.getRemainItem(),pos.getX() + 0.5,pos.getY() + 0.5,pos.getZ() + 0.5,0.0,0.0,0.0);
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
        builder.add(SERVINGS,FACING);
    }
}
