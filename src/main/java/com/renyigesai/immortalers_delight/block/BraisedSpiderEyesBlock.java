package com.renyigesai.immortalers_delight.block;

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
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.util.ItemUtils;
public class BraisedSpiderEyesBlock extends HorizontalDirectionalBlock {

    public static final IntegerProperty BITES = IntegerProperty.create("bites",0,4);
    public static final VoxelShape BOX = box(1.0D,0.0D,1.0D,15.0D,2.0D,15.0D);

    public BraisedSpiderEyesBlock(Properties p_54120_) {
        super(p_54120_);
        super.registerDefaultState(defaultBlockState().setValue(BITES,0).setValue(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return BOX;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack hand_stack = player.getItemInHand(hand);
            if (hand_stack.is(Items.BOWL)) {
                return takeServing(state, level, pos, player);
            }
            if (!hand_stack.is(Items.BOWL)){
                return eat(state, level, pos, player);
            }
            return super.use(state, level, pos, player, hand, hitResult);
    }

    public InteractionResult eat(BlockState state, Level level, BlockPos pos, Player player){
            int bites = state.getValue(BITES);
            if (bites < 4){
                if (player.canEat(false)){
                    setBlock(bites, state, level, pos);
                    player.getFoodData().eat(ImmortalersDelightItems.BRAISED_SPIDER_EYES_IN_GRAVY.get(), new ItemStack(ImmortalersDelightItems.BRAISED_SPIDER_EYES_IN_GRAVY.get()));
                    level.gameEvent(player, GameEvent.EAT, pos);
                    level.playSound(null, pos, SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 0.8F, 0.8F);
                }else {
                    return InteractionResult.PASS;
                }
            }else {
                level.destroyBlock(pos, false);
                vectorwing.farmersdelight.common.utility.ItemUtils.spawnItemEntity(level,
                        new ItemStack(Items.BOWL),pos.getX() + 0.5,pos.getY() + 0.5,pos.getZ() + 0.5,0.0,0.0,0.0);
                level.playSound(null,pos, SoundEvents.WOOL_BREAK, SoundSource.PLAYERS, 0.8F, 0.8F);
            }
            return InteractionResult.SUCCESS;
    }

    public InteractionResult takeServing(BlockState state, Level level, BlockPos pos, Player player){
        int bites = state.getValue(BITES);
        if (bites < 4){
            setBlock(bites,state,level,pos);
            ItemUtils.givePlayerItem(player,new ItemStack(ImmortalersDelightItems.BRAISED_SPIDER_EYES_IN_GRAVY.get()));
        }else {
            level.destroyBlock(pos, false);
            vectorwing.farmersdelight.common.utility.ItemUtils.spawnItemEntity(level,
                    new ItemStack(Items.BOWL),pos.getX() + 0.5,pos.getY() + 0.5,pos.getZ() + 0.5,0.0,0.0,0.0);
        }
        level.playSound(null,pos, SoundEvents.WOOL_BREAK, SoundSource.PLAYERS, 0.8F, 0.8F);
        return InteractionResult.SUCCESS;
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
