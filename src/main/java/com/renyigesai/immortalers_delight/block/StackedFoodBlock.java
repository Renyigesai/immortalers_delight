package com.renyigesai.immortalers_delight.block;

import com.mojang.datafixers.util.Pair;
import com.renyigesai.immortalers_delight.api.PlateBaseBlock;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
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
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.common.utility.ItemUtils;

import java.util.function.Supplier;

public class StackedFoodBlock extends HorizontalDirectionalBlock implements PlateBaseBlock {

    public static final IntegerProperty BITES = IntegerProperty.create("bites",0,16);
    public static final VoxelShape BOX = box(1.0D,0.0D,1.0D,15.0D,12.0D,15.0D);
    public final Supplier<Item> servingItem;
    public final Supplier<Item> pileItem;
    public final int pilePerItem;

    public StackedFoodBlock(Properties p_54120_, Supplier<Item> servingItem,Supplier<Item> pileItem, int pilePerItem) {
        super(p_54120_);
        this.servingItem = servingItem;
        this.pilePerItem = pilePerItem;
        this.pileItem = pileItem;
        super.registerDefaultState(defaultBlockState().setValue(BITES,0).setValue(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return BOX;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack hand_stack = player.getItemInHand(hand);
        if (!player.isShiftKeyDown()){
            if (hand_stack.is(this.pileItem.get())){
                return pileUp(state, level, pos, player, hand);
            }else if (isCuttable(hand_stack)) {
                return cut(state, level, pos, player);
            }
        }else if (isEdible()){
            return eat(state, level, pos, player);
        }
        return super.use(state, level, pos, player, hand, hitResult);
    }
    public boolean isCuttable(ItemStack stack){
        return stack.is(ModTags.KNIVES);
    }
    public boolean isEdible(){
        return true;
    }
    public int getMaxBites(){
        return BITES.getPossibleValues().size() - 1;
    }
    public int getPileBitesPerItem(){
        return this.pilePerItem;
    }
    public ItemStack getPileItem(){
        return new ItemStack(this.pileItem.get());
    }
    public InteractionResult eat(BlockState state, Level level, BlockPos pos, Player player){
            int bites = state.getValue(BITES);
            if (bites < getMaxBites()){
                if (player.canEat(false)){
                    setBlock(bites, state, level, pos);
                    player.getFoodData().eat(this.servingItem.get(), new ItemStack(this.servingItem.get()));
                    addFoodPoisonEffect(new ItemStack(this.servingItem.get()), level, player);
                    level.gameEvent(player, GameEvent.EAT, pos);
                    level.playSound(null, pos, SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 0.8F, 0.8F);
                }else {
                    return InteractionResult.PASS;
                }
            }else {
                level.destroyBlock(pos, false);
                ItemUtils.spawnItemEntity(level,
                        new ItemStack(Items.BOWL),pos.getX() + 0.5,pos.getY() + 0.5,pos.getZ() + 0.5,0.0,0.0,0.0);
                level.playSound(null,pos, SoundEvents.WOOL_BREAK, SoundSource.PLAYERS, 0.8F, 0.8F);
            }
            return InteractionResult.SUCCESS;
    }

    public InteractionResult cut(BlockState state, Level level, BlockPos pos, Player player){
        int bites = state.getValue(BITES);
        Direction direction = player.getDirection().getOpposite();
        if (bites < getMaxBites()){
            setBlock(bites,state,level,pos);
            ItemUtils.spawnItemEntity(level,new ItemStack(this.servingItem.get()),(double)pos.getX() + 0.5, (double)pos.getY() + 0.3, (double)pos.getZ() + 0.5, (double)direction.getStepX() * 0.15, 0.05, (double)direction.getStepZ() * 0.15);
        }else {
            level.removeBlock(pos, false);
            ItemUtils.spawnItemEntity(level,new ItemStack(Items.BOWL),(double)pos.getX() + 0.5, (double)pos.getY() + 0.3, (double)pos.getZ() + 0.5, (double)direction.getStepX() * 0.15, 0.05, (double)direction.getStepZ() * 0.15);
        }
        level.playSound(null,pos, SoundEvents.WOOL_BREAK, SoundSource.PLAYERS, 0.8F, 0.8F);
        return InteractionResult.SUCCESS;
    }

    public InteractionResult pileUp(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand){
        int pile = this.pilePerItem;
        int bites = pState.getValue(BITES);
        ItemStack hand = pPlayer.getItemInHand(pHand);
        if (bites - pile >= 0){
            this.setBlock(bites - 1 - pile,pState,pLevel,pPos);
            if (!pPlayer.isCreative()){
                hand.shrink(1);
            }
            pLevel.playSound(null,pPos, SoundEvents.WOOD_FALL, SoundSource.BLOCKS);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    private void addFoodPoisonEffect(ItemStack p_21064_, Level p_21065_, LivingEntity p_21066_) {
        Item item = p_21064_.getItem();
        if (item.isEdible()) {
            for (Pair<MobEffectInstance, Float> pair : p_21064_.getFoodProperties(p_21066_).getEffects()) {
                if (!p_21065_.isClientSide && pair.getFirst() != null) {
                    if (pair.getFirst().getEffect() == ImmortalersDelightMobEffect.INEBRIATED.get() && p_21066_.hasEffect(ImmortalersDelightMobEffect.INEBRIATED.get())) {
                        MobEffectInstance instance = p_21066_.getEffect(ImmortalersDelightMobEffect.INEBRIATED.get());
                        p_21066_.addEffect(new MobEffectInstance(pair.getFirst().getEffect(), pair.getFirst().getDuration() + instance.getDuration(), Math.max(pair.getFirst().getAmplifier(), instance.getAmplifier())));
                    } else p_21066_.addEffect(new MobEffectInstance(pair.getFirst()));
                }
            }
        }
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

    @Override
    public boolean isEmptyPlate(BlockState state) {
        return state.getValue(BITES) == this.getMaxBites();
    }
}
