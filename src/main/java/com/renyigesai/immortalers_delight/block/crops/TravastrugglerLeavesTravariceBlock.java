package com.renyigesai.immortalers_delight.block.crops;

import com.renyigesai.immortalers_delight.Config;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import vectorwing.farmersdelight.common.registry.ModSounds;

import java.util.List;

public class TravastrugglerLeavesTravariceBlock extends LeavesBlock {
    public TravastrugglerLeavesTravariceBlock(Properties pProperties) {
        super(pProperties);
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
