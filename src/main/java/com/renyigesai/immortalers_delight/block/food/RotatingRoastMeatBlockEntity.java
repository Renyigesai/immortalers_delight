package com.renyigesai.immortalers_delight.block.food;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class RotatingRoastMeatBlockEntity extends BlockEntity {
    public State state = State.CLOSE;
    public float rProgress;
    public float rProgressOld;
    public RotatingRoastMeatBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ImmortalersDelightBlocks.ROTATING_ROAST_MEAT_ENTITY.get(), pPos, pBlockState);
    }

    public float getRprogress(float pPartialTicks) {
        return Mth.lerp(pPartialTicks, this.rProgressOld, this.rProgress);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, RotatingRoastMeatBlockEntity blockEntity){
        blockEntity.rProgressOld = blockEntity.rProgress;
        if (state.getValue(RotatingRoastMeatBlock.STATE) == RotatingRoastMeatBlock.State.LIT) {
            blockEntity.rProgress += 0.1F;
            if (blockEntity.rProgress >= 1.0F){
                blockEntity.rProgress = 0.0F;
            }
        }
    }

    public enum State {
        OPEN_PROCESS,
        OPEN,
        CLOSE_PROCESS,
        CLOSE,
    }

    public static void tick(Level level, BlockPos pos, BlockState state, RotatingRoastMeatBlockEntity blockEntity){

    }
}
