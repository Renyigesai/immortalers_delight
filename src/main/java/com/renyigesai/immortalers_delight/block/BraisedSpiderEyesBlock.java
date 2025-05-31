package com.renyigesai.immortalers_delight.block;

import com.mojang.datafixers.util.Pair;
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
                    addFoodPoisonEffect(new ItemStack(ImmortalersDelightItems.BRAISED_SPIDER_EYES_IN_GRAVY.get()),level,player);
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
    /**
     * 该方法用于处理实体食用物品后添加对应的药水效果。
     * 当实体食用某个可食用物品时，会根据物品的属性尝试为实体添加相应的药水效果。
     *
     * @param p_21064_ 被食用的物品栈，包含了具体的物品及其数量等信息。
     * @param p_21065_ 实体所在的游戏世界，用于判断是否为客户端，以及获取随机数生成器。
     * @param p_21066_ 食用物品的实体，即要添加药水效果的对象。
     */
    private void addFoodPoisonEffect(ItemStack p_21064_, Level p_21065_, LivingEntity p_21066_) {
        // 从物品栈中获取具体的物品
        Item item = p_21064_.getItem();
        // 检查该物品是否为可食用物品
        if (item.isEdible()) {
            // 遍历物品的食物属性中定义的所有药水效果及其概率
            for (Pair<MobEffectInstance, Float> pair : p_21064_.getFoodProperties(p_21066_).getEffects()) {
                // 条件判断：
                // 1. 当前不是客户端，因为药水效果的添加通常在服务器端处理，以保证数据一致性。
                // 2. 药水效果实例不为空，确保有有效的药水效果。
                if (!p_21065_.isClientSide && pair.getFirst() != null && pair.getFirst().getEffect() == ImmortalersDelightMobEffect.GAS_POISON.get()) {
                    // 创建一个新的药水效果实例，使用原有的药水效果实例作为模板。
                    // 然后将该药水效果添加到食用物品的实体上。
                    p_21066_.addEffect(new MobEffectInstance(pair.getFirst()));
                }
            }
        }
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
