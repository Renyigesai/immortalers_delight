package com.renyigesai.immortalers_delight.block;

import com.renyigesai.immortalers_delight.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import vectorwing.farmersdelight.common.registry.ModSounds;

import java.util.List;

public class ReapCropBlock extends CropBlock {
    public ReapCropBlock(Properties p_52247_) {
        super(p_52247_);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (Config.rightClickHarvest) {//通过配置文件决定是否使用右键收获
            int age = state.getValue(AGE);
            if (age == getMaxAge()) {
                boolean temp = false;
                if (level instanceof ServerLevel level1) {
//                    dropResources(state,level1,pos,null,player,player.getMainHandItem());
                    List<ItemStack> stacks = getDrops(state, level1, pos, null,player,player.getMainHandItem());
                    if (!stacks.isEmpty()) {
                        for (ItemStack stack : stacks) {
                            popResource(level, pos, stack);
                        }
                        temp = true;
                    }
                }
                if (temp) {
                    level.setBlock(pos, state.setValue(AGE, 0), 3);
                    level.playSound(null, pos, ModSounds.ITEM_TOMATO_PICK_FROM_BUSH.get(), SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return super.use(state, level, pos, player, hand, hitResult);
    }
}
