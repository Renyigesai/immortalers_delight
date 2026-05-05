package com.renyigesai.immortalers_delight.block.crops;

import com.renyigesai.immortalers_delight.block.ReapCropBlock;
import com.renyigesai.immortalers_delight.util.BlockItemInteraction;
import com.renyigesai.immortalers_delight.block.tree.ModTreeGrowers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.world.tree.BigOakTreeGrower;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;

import java.util.List;


public class HimekaidoShrubBlock extends ReapCropBlock {
    public static final int FRUITED_AGE = 3;
    public HimekaidoShrubBlock(Properties p_52247_) {
        super(p_52247_);
    }
    protected static final int EXTRA_HARVEST_AGE = 6;
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(2.0D, 0.0D, 2.0D, 14.0D, 4.0D, 14.0D),
            Block.box(2.0D, 0.0D, 2.0D, 14.0D, 8.0D, 14.0D),
            Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)
    };
    public VoxelShape getShape(BlockState p_51330_, BlockGetter p_51331_, BlockPos p_51332_, CollisionContext p_51333_) {
        return SHAPE_BY_AGE[this.getAge(p_51330_)];
    }

    /*
    生长逻辑，在生长时如果大于阶段3（默认结果状态），反而回退生长阶段
     */
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (!pLevel.isAreaLoaded(pPos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
        if (pLevel.getRawBrightness(pPos, 0) >= 9) {
            int i = this.getAge(pState);
            if (i < this.getMaxAge()) {
                if (i > FRUITED_AGE) {
                    pLevel.setBlock(pPos, this.getStateForAge(i - 1), 2);
                    net.neoforged.neoforge.common.CommonHooks.fireCropGrowPost(pLevel, pPos, pState);
                }
                if (i < FRUITED_AGE) {
                    float f = getGrowthSpeed(pState, pLevel, pPos);
                    if (net.neoforged.neoforge.common.CommonHooks.canCropGrow(pLevel, pPos, pState, pRandom.nextInt((int)(100.0F / f) + 1) == 0)) {
                        pLevel.setBlock(pPos, this.getStateForAge(i + 1), 2);
                        net.neoforged.neoforge.common.CommonHooks.fireCropGrowPost(pLevel, pPos, pState);
                    }
                }
            }
        }
    }
    @Override
    protected ItemLike getBaseSeedId() {
        return ImmortalersDelightItems.HIMEKAIDO_SEED.get();
    }
    /**
     * 巨大化
     */
    public void performBonemeal(ServerLevel serverLevel, RandomSource source, BlockPos pos, BlockState state) {
        /*
        巨大化条件：此处要求阶段6且生长速度不小于9
         */
        if (this.getAge(state) == EXTRA_HARVEST_AGE && getGrowthSpeed(state, serverLevel, pos) >= 3.0f) {
            ModTreeGrowers.HIMEKAIDO.growTree(serverLevel, serverLevel.getChunkSource().getGenerator(), pos, state, source);
        }
        else serverLevel.setBlock(pos, this.getStateForAge( this.getAge(state) + 1), 2);
    }
    /**
     * 定义收获的行为逻辑，并给玩家进行巨大化操作的空间
     */
    private InteractionResult shrubHarvestUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand) {
        int age = state.getValue(AGE);
        if (age == FRUITED_AGE) {
            ItemStack hand_stack = player.getItemInHand(hand);
            if (!(hand_stack.getItem() instanceof BoneMealItem) || getGrowthSpeed(state, level, pos) <= 3.0f) {
                harvest(state,level,pos,1);
                return InteractionResult.SUCCESS;
            }
        } else if (age == EXTRA_HARVEST_AGE || age == MAX_AGE) {
            ItemStack hand_stack = player.getItemInHand(hand);
            if (!(hand_stack.getItem() instanceof BoneMealItem)) {
                harvest(state,level,pos,1);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        InteractionResult result = shrubHarvestUse(state, level, pos, player, hand);
        if (result != InteractionResult.PASS) {
            return BlockItemInteraction.from(level, result);
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        InteractionResult result = shrubHarvestUse(state, level, pos, player, InteractionHand.MAIN_HAND);
        if (result != InteractionResult.PASS) {
            return result;
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }
    /*
    收获时用于生成掉落物，以及设置到指定的生长阶段，在超生长阶段回到阶段3也就是能马上再收获一次
     */
    public void harvest(BlockState state, Level level, BlockPos pos,int pAge) {
        boolean temp = false;
        if (level instanceof ServerLevel level1) {
            /*
            遍历战利品表
             */
            List<ItemStack> stacks = getDrops(state, level1, pos, null);
            if (!stacks.isEmpty()){
                for (ItemStack stack : stacks) {
                    popResource(level, pos, stack);
                }
                temp = true;
            }
        }
        if (temp){
            /*
            设置收获后的生长阶段
             */
            level.setBlock(pos,state.setValue(AGE,pAge),3);
            level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_BREAK, SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
            return;
        }
        return;
     }
}
