package com.renyigesai.immortalers_delight.block.food;

import com.mojang.datafixers.util.Pair;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightFoodProperties;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.item.DrinkItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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

public class SuperKwatBurgerBlock extends HorizontalDirectionalBlock {

    public static final IntegerProperty BITES = IntegerProperty.create("bites",0,3);
    public static final VoxelShape BOX = box(1.0D,0.0D,1.0D,15.0D,12.0D,15.0D);

    public SuperKwatBurgerBlock(Properties p_54120_) {
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
        if (!(hand_stack.getItem() instanceof DrinkItem)){
            return eat(state, level, pos, player);
        }
        return super.use(state, level, pos, player, hand, hitResult);
    }

    public InteractionResult eat(BlockState state, Level level, BlockPos pos, Player player){
        int bites = state.getValue(BITES);
        if (bites < 4){
            if (player.canEat(false)){
                FoodProperties foodproperties = ImmortalersDelightFoodProperties.SUPER_KWAT_WHEAT_HAMBURGER_SLICE;
                player.getFoodData().eat(foodproperties.getNutrition(), foodproperties.getSaturationModifier());
                addFoodPoisonEffect(foodproperties, level, player);
                level.gameEvent(player, GameEvent.EAT, pos);
                level.playSound(null, pos, SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 0.8F, 0.8F);
                setOrRemoveBlock(bites, state, level, pos);
            }else {
                return InteractionResult.PASS;
            }
        }
        return InteractionResult.SUCCESS;
    }
    /**
     * 该方法用于处理实体食用物品后添加对应的药水效果。
     * 当实体食用某个可食用物品时，会根据物品的属性尝试为实体添加相应的药水效果。
     * @param level 实体所在的游戏世界，用于判断是否为客户端，以及获取随机数生成器。
     * @param entity 食用物品的实体，即要添加药水效果的对象。
     */
    private void addFoodPoisonEffect(FoodProperties foodProperties, Level level, LivingEntity entity) {
        // 检查该物品是否为可食用物品
        if (foodProperties != null) {
            // 遍历物品的食物属性中定义的所有药水效果及其概率
            for (Pair<MobEffectInstance, Float> pair : foodProperties.getEffects()) {
                // 条件判断：
                // 1. 当前不是客户端，因为药水效果的添加通常在服务器端处理，以保证数据一致性。
                // 2. 药水效果实例不为空，确保有有效的药水效果。
                if (!level.isClientSide && pair.getFirst() != null && pair.getFirst().getEffect() == ImmortalersDelightMobEffect.GAS_POISON.get()) {
                    // 创建一个新的药水效果实例，使用原有的药水效果实例作为模板。
                    // 然后将该药水效果添加到食用物品的实体上。
                    entity.addEffect(new MobEffectInstance(pair.getFirst()));
                }
            }
        }
    }

    public void setOrRemoveBlock(int variate, BlockState state, Level level, BlockPos pos){
        int bites = state.getValue(BITES);
        if (bites == 3){
            level.destroyBlock(pos, false);
            vectorwing.farmersdelight.common.utility.ItemUtils.spawnItemEntity(level,
                    new ItemStack(Items.STICK),pos.getX() + 0.5,pos.getY() + 0.5,pos.getZ() + 0.5,0.0,0.0,0.0);
            level.playSound(null,pos, SoundEvents.WOOL_BREAK, SoundSource.PLAYERS, 0.8F, 0.8F);
        } else level.setBlock(pos, state.setValue(BITES, variate + 1), 3);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
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

