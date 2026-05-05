package com.renyigesai.immortalers_delight.block.food;

import com.mojang.serialization.MapCodec;
import com.renyigesai.immortalers_delight.util.BlockItemInteraction;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightFoodProperties;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
//import com.renyigesai.immortalers_delight.potion.immortaleffects.GasPoisonEffect;
import com.renyigesai.immortalers_delight.util.ItemUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
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

public class TartareChickenBigMealBlock extends HorizontalDirectionalBlock {

    public static final MapCodec<TartareChickenBigMealBlock> CODEC = simpleCodec(TartareChickenBigMealBlock::new);
    public static final IntegerProperty BITES = IntegerProperty.create("bites",0,8);
    public static final VoxelShape BOX = box(1.0D,0.0D,1.0D,15.0D,12.0D,15.0D);

    public TartareChickenBigMealBlock(Properties p_54120_) {
        super(p_54120_);
        super.registerDefaultState(defaultBlockState().setValue(BITES,0).setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return BOX;
    }

    private InteractionResult mealUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand) {
        ItemStack hand_stack = player.getItemInHand(hand);
        if (hand_stack.is(Items.BOWL)) {
            return takeServing(state, level, pos, player);
        }
        if (!hand_stack.is(Items.BOWL)) {
            return eat(state, level, pos, player);
        }
        return InteractionResult.PASS;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        InteractionResult result = mealUse(state, level, pos, player, hand);
        if (result != InteractionResult.PASS) {
            return BlockItemInteraction.from(level, result);
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        InteractionResult result = mealUse(state, level, pos, player, InteractionHand.MAIN_HAND);
        return result != InteractionResult.PASS ? result : super.useWithoutItem(state, level, pos, player, hitResult);
    }

    public InteractionResult eat(BlockState state, Level level, BlockPos pos, Player player){
        int bites = state.getValue(BITES);
        if (!player.canEat(false)) {return InteractionResult.PASS;}
        int eatCount = 1;
        for (int i = 1;bites + i <= 9; i++){
            if (player.canEat(false)){
                eatCount = i;
                player.eat(level, new ItemStack(ImmortalersDelightItems.TARTARE_CHICKEN.get()));
                level.gameEvent(player, GameEvent.EAT, pos);
                level.playSound(null, pos, SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 0.8F, 0.8F);
            }else {
                break;
            }
        }
        addFoodPoisonEffect(new ItemStack(ImmortalersDelightItems.TARTARE_CHICKEN.get()),level,player,eatCount);
        if (bites + eatCount < 9) {
            setBlock(bites + eatCount,state,level,pos);
        }else {
            level.destroyBlock(pos, false);
            vectorwing.farmersdelight.common.utility.ItemUtils.spawnItemEntity(level,
                    new ItemStack(Items.BOWL),pos.getX() + 0.5,pos.getY() + 0.5,pos.getZ() + 0.5,0.0,0.0,0.0);
            level.playSound(null,pos, SoundEvents.WOOL_BREAK, SoundSource.PLAYERS, 0.8F, 0.8F);
        }
        return InteractionResult.SUCCESS;
    }
    /**
     * 该方法用于处理实体食用物品后添加对应的药水效果。
     * 当实体食用某个可食用物品时，会根据物品的属性尝试为实体添加相应的药水效果。
     *
     * @param p_21064_ 被食用的物品栈，包含了具体的物品及其数量等信息。
     * @param p_21065_ 实体所在的游戏世界，用于判断是否为客户端，以及获取随机数生成器。
     * @param p_21066_ 食用物品的实体，即要添加药水效果的对象。
     */
    private void addFoodPoisonEffect(ItemStack stack, Level level, LivingEntity entity, int timeBuffer) {
        FoodProperties props = stack.getFoodProperties(entity);
        if (level.isClientSide || props == null) {
            return;
        }
        for (FoodProperties.PossibleEffect pe : props.effects()) {
            if (entity.getRandom().nextFloat() >= pe.probability()) {
                continue;
            }
            MobEffectInstance base = pe.effectSupplier().get();
            int duration = base.getDuration() > 0 ? base.getDuration() : 200;
            int amplifier = base.getAmplifier();
            if (timeBuffer > 1) {
                duration *= timeBuffer;
            }
            entity.addEffect(new MobEffectInstance(base.getEffect(), duration, amplifier));
        }
    }
    public InteractionResult takeServing(BlockState state, Level level, BlockPos pos, Player player){
        int bites = state.getValue(BITES);
        ItemUtils.givePlayerItem(player,new ItemStack(ImmortalersDelightItems.TARTARE_CHICKEN.get()));
        if (bites < 8){
            setBlock(bites + 1,state,level,pos);
        }else {
            level.destroyBlock(pos, false);
            vectorwing.farmersdelight.common.utility.ItemUtils.spawnItemEntity(level,
                    new ItemStack(Items.BOWL),pos.getX() + 0.5,pos.getY() + 0.5,pos.getZ() + 0.5,0.0,0.0,0.0);
        }
        level.playSound(null,pos, SoundEvents.WOOL_BREAK, SoundSource.PLAYERS, 0.8F, 0.8F);
        return InteractionResult.SUCCESS;
    }

    public void setBlock(int variate,BlockState state, Level level, BlockPos pos){
        level.setBlock(pos, state.setValue(BITES, variate), 3);
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
