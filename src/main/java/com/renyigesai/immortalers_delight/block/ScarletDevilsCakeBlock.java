package com.renyigesai.immortalers_delight.block;

import com.mojang.datafixers.util.Pair;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.util.ItemUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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

public class ScarletDevilsCakeBlock extends HorizontalDirectionalBlock {

    public static final IntegerProperty BITES = IntegerProperty.create("bites",0,8);
    public static final VoxelShape BOX = box(1.0D,0.0D,1.0D,15.0D,12.0D,15.0D);

    public ScarletDevilsCakeBlock(Properties p_54120_) {
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
            if (hand_stack.is(ModTags.KNIVES)) {
                return takeServing(state, level, pos, player);
            }
            if (!hand_stack.is(ModTags.KNIVES)){
                return eat(state, level, pos, player);
            }
            return super.use(state, level, pos, player, hand, hitResult);
    }

    public InteractionResult eat(BlockState state, Level level, BlockPos pos, Player player){
        int bites = state.getValue(BITES);
        if (!player.canEat(false)) {return InteractionResult.PASS;}
        int eatCount = 1;
        for (int i = 1;bites + i <= 8; i++){
            if (player.canEat(false)){
                eatCount = i;
                player.getFoodData().eat(ImmortalersDelightItems.SCARLET_DEVILS_CAKE_SLICE.get(),
                        new ItemStack(ImmortalersDelightItems.SCARLET_DEVILS_CAKE_SLICE.get()));
                level.gameEvent(player, GameEvent.EAT, pos);
                level.playSound(null, pos, SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 0.8F, 0.8F);
            }else {
                break;
            }
        }
        addFoodPoisonEffect(new ItemStack(ImmortalersDelightItems.SCARLET_DEVILS_CAKE_SLICE.get()),level,player,eatCount);
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
    private void addFoodPoisonEffect(ItemStack p_21064_, Level p_21065_, LivingEntity p_21066_, int timeBuffer) {
        // 从物品栈中获取具体的物品
        Item item = p_21064_.getItem();
        // 检查该物品是否为可食用物品
        if (item.isEdible()) {
            // 遍历物品的食物属性中定义的所有药水效果及其概率
            if (!p_21065_.isClientSide && p_21064_.getFoodProperties(p_21066_) != null) {
                for (Pair<MobEffectInstance, Float> pair : p_21064_.getFoodProperties(p_21066_).getEffects()) {
                    // 条件判断：
                    // 1. 当前不是客户端，因为药水效果的添加通常在服务器端处理，以保证数据一致性。
                    // 2. 药水效果实例不为空，确保有有效的药水效果。
                    if (pair.getFirst() != null) {
                        // 创建一个新的药水效果实例，使用原有的药水效果实例作为模板。
                        // 然后将该药水效果添加到食用物品的实体上。
                        if (timeBuffer > 1) {
                            int time = pair.getFirst().getDuration() * timeBuffer;
                            int lv = pair.getFirst().getAmplifier();
                            p_21066_.addEffect(new MobEffectInstance(pair.getFirst().getEffect(),time,lv));
                            //如果时间倍率大于1，会将持续时间乘以倍率
                        } else p_21066_.addEffect(new MobEffectInstance(pair.getFirst()));
                    }
                }
                p_21066_.addEffect(new MobEffectInstance(MobEffects.HEAL,1));
            }
        }
    }
    public InteractionResult takeServing(BlockState state, Level level, BlockPos pos, Player player){
        int bites = state.getValue(BITES);
        if (bites < 8){
            ItemUtils.givePlayerItem(player,new ItemStack(ImmortalersDelightItems.SCARLET_DEVILS_CAKE_SLICE.get()));
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
