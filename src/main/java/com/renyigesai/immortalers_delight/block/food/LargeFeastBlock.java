package com.renyigesai.immortalers_delight.block.food;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.event.ForgeEventFactory;
import vectorwing.farmersdelight.common.registry.ModSounds;
import vectorwing.farmersdelight.common.utility.ItemUtils;

import java.util.Map;
import java.util.function.Supplier;

//public class LargeFeastBlock extends Block {
//    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
//    // 包含 8 种食用量状态 (0 到 7, 共 8 个阶段)
//    public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, 7);
//
//    // Forge 刀具标签: forge:tools/knives
//    public static final TagKey<Item> KNIVES_TAG = ItemTags.create(new ResourceLocation("forge", "tools/knives"));
//
//    protected static final VoxelShape[] SHAPES;
//    private static final VoxelShape[][] ROTATED_SHAPES;
//    public final Supplier<Item> sliceItemSupplier;
//
//    public LargeFeastBlock(BlockBehaviour.Properties properties, Supplier<Item> sliceItemSupplier) {
//        super(properties);
//        this.sliceItemSupplier = sliceItemSupplier;
//        this.registerDefaultState(this.stateDefinition.any()
//                .setValue(FACING, Direction.NORTH)
//                .setValue(BITES, 0));
//    }
//
//    public ItemStack getSliceItem() {
//        return new ItemStack(this.sliceItemSupplier.get());
//    }
//
//    public int getMaxBites() {
//        return 8; // 支持 8 次食用/切片
//    }
//
//    @Override
//    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
//        return ROTATED_SHAPES[state.getValue(BITES)][state.getValue(FACING).get2DDataValue()];
//    }
//
//    private static VoxelShape[][] buildShapes() {
//        VoxelShape[][] result = new VoxelShape[SHAPES.length][4];
//        for (int i = 0; i < SHAPES.length; ++i) {
//            Map<Direction, VoxelShape> rotated = ShapeUtils.getShapesRotatedFromNorth(SHAPES[i]);
//            for (Map.Entry<Direction, VoxelShape> entry : rotated.entrySet()) {
//                result[i][entry.getKey().get2DDataValue()] = entry.getValue();
//            }
//        }
//        return result;
//    }
//
//    @Override
//    public BlockState getStateForPlacement(BlockPlaceContext context) {
//        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
//    }
//
//    protected boolean isKnife(ItemStack stack) {
//        return stack.is(KNIVES_TAG) || ItemUtils.isKnife(stack);
//    }
//
//    @Override
//    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
//        ItemStack heldStack = player.getItemInHand(hand);
//
//        if (level.isClientSide) {
//            if (this.isKnife(heldStack)) {
//                return this.cutSlice(level, pos, state, player, heldStack.getItem());
//            }
//            if (this.consumeBite(level, pos, state, player) == InteractionResult.SUCCESS) {
//                return InteractionResult.SUCCESS;
//            }
//            if (heldStack.isEmpty()) {
//                return InteractionResult.CONSUME;
//            }
//        }
//
//        return this.isKnife(heldStack)
//                ? this.cutSlice(level, pos, state, player, heldStack.getItem())
//                : this.consumeBite(level, pos, state, player);
//    }
//
//    protected InteractionResult consumeBite(Level level, BlockPos pos, BlockState state, Player player) {
//        if (!player.canEat(false)) {
//            return InteractionResult.PASS;
//        }
//
//        ItemStack sliceStack = this.getSliceItem();
//        ItemStack sliceCopy = sliceStack.copy();
//        FoodProperties sliceFood = sliceStack.getItem().getFoodProperties();
//
//        player.getFoodData().eat(sliceStack.getItem(), sliceStack);
//        ForgeEventFactory.onItemUseFinish(player, sliceCopy, 0, ItemStack.EMPTY);
//
//        if (this.getSliceItem().getItem().isEdible() && sliceFood != null) {
//            for (Pair<MobEffectInstance, Float> pair : sliceFood.getEffects()) {
//                if (!level.isClientSide && pair.getFirst() != null && level.random.nextFloat() < pair.getSecond()) {
//                    player.addEffect(new MobEffectInstance(pair.getFirst()));
//                }
//            }
//        }
//
//        int bites = state.getValue(BITES);
//        if (bites < this.getMaxBites() - 1) {
//            level.setBlock(pos, state.setValue(BITES, bites + 1), 3);
//        } else {
//            level.removeBlock(pos, false);
//        }
//
//        level.playSound(null, pos, SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 0.8F, 0.8F);
//        if (level instanceof ServerLevel serverLevel) {
//            serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, state),
//                    pos.getX() + 0.5, pos.getY() + 0.3, pos.getZ() + 0.5,
//                    3, 0.1, 0.1, 0.1, 0.001);
//        }
//
//        return InteractionResult.SUCCESS;
//    }
//
//    protected InteractionResult cutSlice(Level level, BlockPos pos, BlockState state, Player player, Item knife) {
//        int bites = state.getValue(BITES);
//        if (bites < this.getMaxBites() - 1) {
//            level.setBlock(pos, state.setValue(BITES, bites + 1), 3);
//        } else {
//            level.removeBlock(pos, false);
//        }
//
//        Direction direction = player.getDirection().getOpposite();
//        ItemUtils.spawnItemEntity(level, this.getSliceItem(),
//                pos.getX() + 0.5, pos.getY() + 0.3, pos.getZ() + 0.5,
//                direction.getStepX() * 0.15, 0.05, direction.getStepZ() * 0.15);
//
//        level.playSound(null, pos, (SoundEvent) ModSounds.BLOCK_FOOD_SLICE.get(), SoundSource.PLAYERS, 0.8F, 0.8F);
//        if (level instanceof ServerLevel serverLevel) {
//            serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, state),
//                    pos.getX() + 0.5, pos.getY() + 0.3, pos.getZ() + 0.5,
//                    3, 0.1, 0.1, 0.1, 0.001);
//        }
//
//        player.awardStat(Stats.ITEM_USED.get(knife));
//        return InteractionResult.SUCCESS;
//    }
//
//    @Override
//    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
//        return facing == Direction.DOWN && !state.canSurvive(level, currentPos)
//                ? Blocks.AIR.defaultBlockState()
//                : super.updateShape(state, facing, facingState, level, currentPos, facingPos);
//    }
//
//    @Override
//    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
//        return canSupportRigidBlock(level, pos.below());
//    }
//
//    @Override
//    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
//        builder.add(FACING, BITES);
//    }
//
//    @Override
//    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
//        // 红石比较器输出随剩余份数衰减 (8 -> 1)
//        return this.getMaxBites() - state.getValue(BITES);
//    }
//
//    @Override
//    public boolean hasAnalogOutputSignal(BlockState state) {
//        return true;
//    }
//
//    @Override
//    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
//        return false;
//    }
//
//    static {
//        // 定义 8 个不同阶段的碰撞箱（示例采用由完整到逐步变小的箱体）
//        SHAPES = new VoxelShape[]{
//                Block.box(1.0, 0.0, 1.0, 15.0, 6.0, 15.0),
//                Block.box(1.0, 0.0, 1.0, 15.0, 6.0, 13.0),
//                Block.box(1.0, 0.0, 1.0, 15.0, 6.0, 11.0),
//                Block.box(1.0, 0.0, 1.0, 15.0, 6.0, 9.0),
//                Block.box(1.0, 0.0, 1.0, 15.0, 6.0, 7.0),
//                Block.box(1.0, 0.0, 1.0, 15.0, 6.0, 5.0),
//                Block.box(1.0, 0.0, 1.0, 15.0, 6.0, 3.0),
//                Block.box(1.0, 0.0, 1.0, 8.0, 6.0, 3.0)
//        };
//        ROTATED_SHAPES = buildShapes();
//    }
//}
