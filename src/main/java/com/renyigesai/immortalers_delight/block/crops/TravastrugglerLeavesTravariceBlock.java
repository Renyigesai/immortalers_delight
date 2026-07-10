package com.renyigesai.immortalers_delight.block.crops;

import com.renyigesai.immortalers_delight.Config;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.util.BlockItemInteraction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.CommonHooks;

import java.util.ArrayList;
import java.util.List;

public class TravastrugglerLeavesTravariceBlock extends LeavesBlock implements BonemealableBlock {
    public TravastrugglerLeavesTravariceBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean isRandomlyTicking(BlockState pState) {
        return !pState.getValue(PERSISTENT);
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        super.randomTick(pState, pLevel, pPos, pRandom);

        if (!pLevel.isAreaLoaded(pPos, 1)) {
            return;
        }
        if (CommonHooks.canCropGrow(pLevel, pPos, pState, pRandom.nextInt(10) == 0)) {
            trySpread(pState, pLevel, pPos, pRandom);
        }
    }

    private boolean trySpread(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(pRandom);
        BlockPos blockpos = pPos.relative(direction);
        BlockState blockstate = pLevel.getBlockState(blockpos);
        BlockState belowState = pLevel.getBlockState(blockpos.below());
        boolean spread = false;
        if (blockstate.is(ImmortalersDelightBlocks.TRAVASTRUGGLER_LEAVES.get())) {
            pLevel.setBlockAndUpdate(blockpos, pState);
            spread = true;
        }
        if (belowState.is(ImmortalersDelightBlocks.TRAVASTRUGGLER_LEAVES.get())
                || belowState.is(Blocks.BAMBOO)
                || belowState.is(BlockTags.LEAVES)) {
            pLevel.setBlockAndUpdate(blockpos.below(), pState);
            spread = true;
        }
        if (spread) {
            List<ItemStack> stacks = collectHarvestDrops(pState, pLevel, pPos, null);
            if (!stacks.isEmpty()) {
                int below = getHeightBelowUpToMax(pLevel, blockpos);
                for (ItemStack stack : stacks) {
                    spawnDropItemEntity(pLevel, stack, pPos.getX() + 0.5, pPos.getY() - below + 0.5, pPos.getZ() + 0.5, 0.05, 0.05, 0.05);
                }
            }
            pLevel.setBlockAndUpdate(pPos, ImmortalersDelightBlocks.TRAVASTRUGGLER_LEAVES.get().defaultBlockState()
                    .setValue(DISTANCE, pState.getValue(DISTANCE))
                    .setValue(PERSISTENT, pState.getValue(PERSISTENT))
                    .setValue(WATERLOGGED, pState.getValue(WATERLOGGED)));
            CommonHooks.fireCropGrowPost(pLevel, pPos, pState);
        }
        return spread;
    }

    public static void spawnDropItemEntity(Level level, ItemStack stack, double x, double y, double z, double xMotion, double yMotion, double zMotion) {
        ItemEntity entity = new ItemEntity(level, x, y, z, stack);
        entity.setNoPickUpDelay();
        entity.lifespan = 600;
        level.addFreshEntity(entity);
    }

    protected int getHeightBelowUpToMax(BlockGetter pLevel, BlockPos pPos) {
        int max = 0;
        for (int i = 0; i < 16; ++i) {
            if (pLevel.getBlockState(pPos.below(i)).is(BlockTags.LEAVES)) {
                max = i + 1;
            }
        }
        return max;
    }

    private List<ItemStack> collectHarvestDrops(BlockState state, ServerLevel level, BlockPos pos, Player player) {
        ItemStack tool = player != null ? player.getMainHandItem() : ItemStack.EMPTY;
        LootParams.Builder lootBuilder = new LootParams.Builder(level)
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                .withParameter(LootContextParams.BLOCK_STATE, state)
                .withOptionalParameter(LootContextParams.TOOL, tool)
                .withOptionalParameter(LootContextParams.THIS_ENTITY, player);
        List<ItemStack> stacks = new ArrayList<>(getDrops(state, lootBuilder));
        stacks.removeIf(ItemStack::isEmpty);
        if (stacks.isEmpty() && player != null) {
            stacks = new ArrayList<>(getDrops(state, level, pos, null, player, tool));
            stacks.removeIf(ItemStack::isEmpty);
        }
        stacks.removeIf(stack -> stack.is(this.asItem()));
        if (stacks.isEmpty()) {
            stacks.add(new ItemStack(ImmortalersDelightItems.TRAVARICE.get(), 1 + level.getRandom().nextInt(3)));
        }
        return stacks;
    }

    private InteractionResult travariceHarvest(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!Config.rightClickHarvest) {
            return InteractionResult.PASS;
        }
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        if (level instanceof ServerLevel serverLevel) {
            List<ItemStack> stacks = collectHarvestDrops(state, serverLevel, pos, player);
            if (!stacks.isEmpty()) {
                for (ItemStack stack : stacks) {
                    popResource(level, pos, stack);
                }
                BlockState blockstate = ImmortalersDelightBlocks.TRAVASTRUGGLER_LEAVES.get().defaultBlockState()
                        .setValue(DISTANCE, state.getValue(DISTANCE))
                        .setValue(PERSISTENT, state.getValue(PERSISTENT))
                        .setValue(WATERLOGGED, state.getValue(WATERLOGGED));
                level.setBlockAndUpdate(pos, blockstate);
                level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        InteractionResult result = travariceHarvest(state, level, pos, player, hand, hit);
        if (result != InteractionResult.PASS) {
            return BlockItemInteraction.from(level, result);
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hit);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        InteractionResult result = travariceHarvest(state, level, pos, player, InteractionHand.MAIN_HAND, hit);
        return result != InteractionResult.PASS ? result : super.useWithoutItem(state, level, pos, player, hit);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return !state.getValue(PERSISTENT);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        if (!trySpread(state, level, pos, random)) {
            level.setBlockAndUpdate(pos, ImmortalersDelightBlocks.TRAVASTRUGGLER_LEAVES.get().defaultBlockState()
                    .setValue(DISTANCE, state.getValue(DISTANCE))
                    .setValue(PERSISTENT, state.getValue(PERSISTENT))
                    .setValue(WATERLOGGED, state.getValue(WATERLOGGED)));
            popResource(level, pos, new ItemStack(ImmortalersDelightItems.TRAVARICE.get(), 1 + random.nextInt(3)));
        }
    }
}
