package com.renyigesai.immortalers_delight.block.brushable;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BrushableBlock;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ModBrushableBlock extends BrushableBlock {
    public ModBrushableBlock(Block pTurnsInto, Properties pProperties, SoundEvent pBrushSound, SoundEvent pBrushCompletedSound) {
        super(pTurnsInto, pProperties, pBrushSound, pBrushCompletedSound);
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        BlockEntity blockentity = pLevel.getBlockEntity(pPos);
        if (blockentity instanceof ModBrushableBlockEntity brushableblockentity) {
            brushableblockentity.checkReset();
        }
        super.tick(pState, pLevel, pPos, pRandom);
    }

    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BrushableBlockEntity(pPos, pState);
    }
}
