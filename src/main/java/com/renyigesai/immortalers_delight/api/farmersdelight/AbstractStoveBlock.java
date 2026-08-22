//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.renyigesai.immortalers_delight.api.farmersdelight;

import java.util.Optional;
import javax.annotation.Nullable;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FireChargeItem;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ToolActions;
import vectorwing.farmersdelight.common.block.StoveBlock;
import vectorwing.farmersdelight.common.registry.ModDamageTypes;
import vectorwing.farmersdelight.common.utility.ItemUtils;
import vectorwing.farmersdelight.common.utility.MathUtils;

public abstract class AbstractStoveBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING;
    public static final BooleanProperty LIT;
    private static final VoxelShape GRILLING_AREA;

    public AbstractStoveBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH)).setValue(LIT, false));
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        InteractionResult extinguishResult;
        if ((Boolean)state.getValue(LIT)) {
            extinguishResult = this.tryToExtinguish(state, level, pos, player, hand, hit);
            if (extinguishResult != InteractionResult.PASS) {
                return extinguishResult;
            }
        } else {
            extinguishResult = this.tryToIgnite(state, level, pos, player, hand, hit);
            if (extinguishResult != InteractionResult.PASS) {
                return extinguishResult;
            }
        }

        return this.tryToPlaceFoodItem(state, level, pos, player, hand, hit);
    }

    protected InteractionResult tryToIgnite(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldStack = player.getItemInHand(hand);
        Item heldItem = heldStack.getItem();
        if (heldItem instanceof FlintAndSteelItem) {
            if (!level.isClientSide()) {
                level.playSound((Player)null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, MathUtils.RAND.nextFloat() * 0.4F + 0.8F);
            }

            this.ignite(player, level, pos, state);
            heldStack.hurtAndBreak(1, player, (action) -> {
                action.broadcastBreakEvent(hand);
            });
            return InteractionResult.SUCCESS;
        } else if (heldItem instanceof FireChargeItem) {
            if (!level.isClientSide()) {
                level.playSound((Player)null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F, (MathUtils.RAND.nextFloat() - MathUtils.RAND.nextFloat()) * 0.2F + 1.0F);
            }

            this.ignite(player, level, pos, state);
            if (!player.getAbilities().instabuild) {
                heldStack.shrink(1);
            }

            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }

    protected InteractionResult tryToExtinguish(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldStack = player.getItemInHand(hand);
        if (heldStack.canPerformAction(ToolActions.SHOVEL_DIG)) {
            if (!level.isClientSide()) {
                level.levelEvent((Player)null, 1009, pos, 0);
            }

            this.extinguish(player, level, pos, state);
            heldStack.hurtAndBreak(1, player, (action) -> {
                action.broadcastBreakEvent(hand);
            });
            return InteractionResult.sidedSuccess(level.isClientSide());
        } else if (heldStack.is(ImmortalersDelightTags.Items.BUCKETS_WATER)) {
            if (!level.isClientSide()) {
                level.playSound((Player)null, pos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0F, 1.0F);
            }

            this.extinguish(player, level, pos, state);
            if (!player.getAbilities().instabuild) {
                player.setItemInHand(hand, heldStack.getCraftingRemainingItem());
            }

            return InteractionResult.sidedSuccess(level.isClientSide());
        } else {
            return InteractionResult.PASS;
        }
    }

    protected InteractionResult tryToPlaceFoodItem(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (isStoveTopCovered(level, pos, state)) {
            return InteractionResult.PASS;
        } else {
            BlockEntity var8 = level.getBlockEntity(pos);
            if (var8 instanceof AbstractStoveBlockEntity) {
                AbstractStoveBlockEntity stoveEntity = (AbstractStoveBlockEntity)var8;
                ItemStack heldStack = player.getItemInHand(hand);
                Optional maybeRecipe = stoveEntity.getCookingRecipe(heldStack);
                if (maybeRecipe.isEmpty()) {
                    return InteractionResult.PASS;
                } else if (level.isClientSide) {
                    return InteractionResult.CONSUME;
                } else {
                    boolean placeFoodSuccess = stoveEntity.placeFood(player, player.getAbilities().instabuild ? heldStack.copy() : heldStack, ((AbstractCookingRecipe)maybeRecipe.get()).getCookingTime());
                    if (!placeFoodSuccess) {
                        return InteractionResult.CONSUME;
                    } else {
                        level.playSound((Player)null, pos, SoundEvents.LANTERN_PLACE, SoundSource.BLOCKS, 0.5F, 1.0F);
                        return InteractionResult.SUCCESS;
                    }
                }
            } else {
                return InteractionResult.PASS;
            }
        }
    }

    public void ignite(@Nullable Entity entity, LevelAccessor level, BlockPos pos, BlockState state) {
        BlockEntity var6 = level.getBlockEntity(pos);
        if (var6 instanceof AbstractStoveBlockEntity stoveEntity) {
            stoveEntity.ignite();
        }

        if (!level.isClientSide()) {
            BlockState newState = (BlockState)state.setValue(LIT, true);
            level.setBlock(pos, newState, 11);
            level.gameEvent(entity, GameEvent.BLOCK_CHANGE, pos);
        }
    }

    public void extinguish(@Nullable Entity entity, LevelAccessor level, BlockPos pos, BlockState state) {
        BlockEntity var6 = level.getBlockEntity(pos);
        if (var6 instanceof AbstractStoveBlockEntity stoveEntity) {
            stoveEntity.extinguish();
        }

        if (!level.isClientSide()) {
            BlockState newState = (BlockState)state.setValue(LIT, false);
            level.setBlock(pos, newState, 11);
            level.gameEvent(entity, GameEvent.BLOCK_CHANGE, pos);
        }
    }

    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState)((BlockState)this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite())).setValue(LIT, true);
    }

    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        this.burnEntitySteppingOnStove(level, pos, state, entity);
        super.stepOn(level, pos, state, entity);
    }

    protected void burnEntitySteppingOnStove(Level level, BlockPos pos, BlockState state, Entity entity) {
        if ((Boolean)state.getValue(LIT)) {
            if (entity.getBoundingBox().intersects(GRILLING_AREA.bounds().move(pos.above()))) {
                if (!entity.isSteppingCarefully()) {
                    if (!entity.fireImmune()) {
                        if (entity instanceof LivingEntity) {
                            LivingEntity livingEntity = (LivingEntity)entity;
                            if (!EnchantmentHelper.hasFrostWalker(livingEntity)) {
                                entity.hurt(ModDamageTypes.getSimpleDamageSource(level, ModDamageTypes.STOVE_BURN), 1.0F);
                            }
                        }
                    }
                }
            }
        }
    }

    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity var7 = level.getBlockEntity(pos);
            if (var7 instanceof AbstractStoveBlockEntity) {
                AbstractStoveBlockEntity stoveEntity = (AbstractStoveBlockEntity)var7;
                ItemUtils.dropItems(level, pos, stoveEntity.getItems());
            }

            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    public static boolean isStoveTopCovered(Level level, BlockPos pos, BlockState stoveState) {
        if (!(stoveState.getBlock() instanceof StoveBlock)) {
            return false;
        } else {
            BlockPos abovePos = pos.above();
            BlockState aboveState = level.getBlockState(abovePos);
            return Shapes.joinIsNotEmpty(GRILLING_AREA, aboveState.getShape(level, abovePos), BooleanOp.AND);
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(new Property[]{FACING, LIT});
    }

    @Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> createStoveTicker(Level level, BlockEntityType<T> serverType, BlockEntityType<? extends com.renyigesai.immortalers_delight.api.farmersdelight.AbstractStoveBlockEntity> clientType) {
        return level.isClientSide ? null : createTickerHelper(serverType, clientType, com.renyigesai.immortalers_delight.api.farmersdelight.AbstractStoveBlockEntity::serverTick);
    }

    @Nullable
    public BlockPathTypes getBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob entity) {
        return (Boolean)state.getValue(LIT) ? BlockPathTypes.DAMAGE_FIRE : null;
    }

    public BlockState rotate(BlockState state, Rotation rotation) {
        return (BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation((Direction)state.getValue(FACING)));
    }

    static {
        FACING = HorizontalDirectionalBlock.FACING;
        LIT = BlockStateProperties.LIT;
        GRILLING_AREA = Block.box(3.0, 0.0, 3.0, 13.0, 1.0, 13.0);
    }
}
