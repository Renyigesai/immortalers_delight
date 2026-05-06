package com.renyigesai.immortalers_delight.block;

import com.renyigesai.immortalers_delight.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import com.renyigesai.immortalers_delight.util.BlockItemInteraction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class ReapCropBlock extends CropBlock {
    public ReapCropBlock(Properties p_52247_) {
        super(p_52247_);
    }

    private InteractionResult tryReap(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!canReap(state, level, pos, player, hand, hitResult)) {
            return null;
        }
        if (level.isClientSide){
            return InteractionResult.SUCCESS;
        }
        boolean temp = false;
        if (level instanceof ServerLevel level1) {
            LootParams.Builder lootBuilder = new LootParams.Builder(level1)
                    .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                    .withParameter(LootContextParams.THIS_ENTITY, player)
                    .withParameter(LootContextParams.BLOCK_STATE, state)
                    .withOptionalParameter(LootContextParams.TOOL, player.getMainHandItem());
            List<ItemStack> stacks = new ArrayList<>(this.getDrops(state, lootBuilder));
            stacks.removeIf(ItemStack::isEmpty);
            if (stacks.isEmpty()) {
                stacks = new ArrayList<>(this.getDrops(state, level1, pos, null, player, player.getMainHandItem()));
                stacks.removeIf(ItemStack::isEmpty);
            }
            if (!stacks.isEmpty()) {
                for (ItemStack stack : stacks) {
                    popResource(level, pos, stack);
                }
                temp = true;
            }
        }
        if (temp) {
            BlockState blockState = state.setValue(AGE, 0);
            level.setBlock(pos, blockState, 2);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, blockState));
            level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
            return InteractionResult.SUCCESS;
        }
        return null;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        InteractionResult reap = tryReap(state, level, pos, player, hand, hitResult);
        if (reap != null) {
            return BlockItemInteraction.from(level, reap);
        }
        if (stack.is(Items.BONE_MEAL)){
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        InteractionResult reap = tryReap(state, level, pos, player, InteractionHand.MAIN_HAND, hitResult);
        if (reap != null) {
            return reap;
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    //收获条件。通过配置文件决定是否使用右键收获并判断其他收获条件，方便子类重写
    public boolean canReap(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        boolean flag = Config.rightClickHarvest;
        return flag && state.getValue(AGE) == getMaxAge();
    }
    @Override
    public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return 20;
    }
    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return 5;
    }
}
