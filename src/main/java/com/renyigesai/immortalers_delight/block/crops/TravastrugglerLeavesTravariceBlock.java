package com.renyigesai.immortalers_delight.block.crops;

import com.renyigesai.immortalers_delight.Config;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import vectorwing.farmersdelight.common.registry.ModSounds;

import java.util.List;

public class TravastrugglerLeavesTravariceBlock extends LeavesBlock {
    public TravastrugglerLeavesTravariceBlock(Properties pProperties) {
        super(pProperties);
    }
    public boolean isRandomlyTicking(BlockState pState) {
        return !pState.getValue(PERSISTENT);
    }
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        super.randomTick(pState, pLevel, pPos, pRandom);

        if (!pLevel.isAreaLoaded(pPos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
        if (pLevel.getRawBrightness(pPos, 0) >= 9) {
            if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(pLevel, pPos, pState, pRandom.nextInt(14) == 0)) {
                Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(pRandom);
                BlockPos blockpos = pPos.relative(direction);
                BlockState blockstate = pLevel.getBlockState(blockpos);
                BlockState belowState = pLevel.getBlockState(blockpos.below());
                boolean f = false;
                if (blockstate.is(ImmortalersDelightBlocks.TRAVASTRUGGLER_LEAVES.get())) {
                    pLevel.setBlockAndUpdate(blockpos, pState);
                    f = true;
                }
                if (belowState.is(ImmortalersDelightBlocks.TRAVASTRUGGLER_LEAVES.get())
                        || belowState.is(Blocks.BAMBOO)
                        || belowState.is(BlockTags.LEAVES)) {
                    pLevel.setBlockAndUpdate(blockpos.below(), pState);
                    f = true;
                }
                if (f) {
                    List<ItemStack> stacks = getDrops(pState, pLevel, pPos, null);
                    if (!stacks.isEmpty()) {
                        int below = getHeightBelowUpToMax(pLevel, blockpos);
                        for (ItemStack stack : stacks) {
                            spawnDropItemEntity(pLevel, stack, pPos.getX() + 0.5, pPos.getY() - below + 0.5, pPos.getZ() + 0.5, 0.05, 0.05, 0.05);
                        }
                    }
                    pLevel.setBlockAndUpdate(pPos, ImmortalersDelightBlocks.TRAVASTRUGGLER_LEAVES.get().defaultBlockState().setValue(TravastrugglerLeavesTravariceBlock.DISTANCE, pState.getValue(DISTANCE)).setValue(TravastrugglerLeavesTravariceBlock.PERSISTENT, pState.getValue(PERSISTENT)).setValue(TravastrugglerLeavesTravariceBlock.WATERLOGGED, pState.getValue(WATERLOGGED)));
                }
                net.minecraftforge.common.ForgeHooks.onCropsGrowPost(pLevel, pPos, pState);
            }

        }
    }
    public static void spawnDropItemEntity(Level level, ItemStack stack, double x, double y, double z, double xMotion, double yMotion, double zMotion) {
        ItemEntity entity = new ItemEntity(level, x, y, z, stack);
        entity.setNoPickUpDelay();
        entity.lifespan = 600;
        level.addFreshEntity(entity);
    }
    protected int getHeightBelowUpToMax(BlockGetter pLevel, BlockPos pPos) {
        int max = 0;
        for(int i = 0; i < 16 ; ++i) {
            if (pLevel.getBlockState(pPos.below(i)).is(BlockTags.LEAVES)) max = i + 1;
        }
        return max;
    }
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (Config.rightClickHarvest) {//通过配置文件决定是否使用右键收获
                boolean temp = false;
                if (level instanceof ServerLevel level1) {
                    List<ItemStack> stacks = getDrops(state, level1, pos, null);
                    if (!stacks.isEmpty()) {
                        for (ItemStack stack : stacks) {
                            popResource(level, pos, stack);
                        }
                        temp = true;
                    }
                }
                if (temp) {
                    BlockState blockstate = ImmortalersDelightBlocks.TRAVASTRUGGLER_LEAVES.get().defaultBlockState().setValue(TravastrugglerLeavesTravariceBlock.DISTANCE,state.getValue(DISTANCE)).setValue(TravastrugglerLeavesTravariceBlock.PERSISTENT,state.getValue(PERSISTENT)).setValue(TravastrugglerLeavesTravariceBlock.WATERLOGGED,state.getValue(WATERLOGGED));
                    level.setBlockAndUpdate(pos, blockstate);
                    level.playSound(null, pos, ModSounds.ITEM_TOMATO_PICK_FROM_BUSH.get(), SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
                    return InteractionResult.SUCCESS;
                }
        }
        return super.use(state, level, pos, player, hand, hit);
    }
}
