package com.renyigesai.immortalers_delight.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;

public class SpikeBarBlock extends IronBarsBlock {
    public SpikeBarBlock(Properties p_54198_) {
        super(p_54198_);
    }

    @Override
    public void stepOn(Level pLevel, BlockPos pPos, BlockState pState, Entity pEntity) {
        if (!(pEntity instanceof LivingEntity)){
            return;
        }
        LivingEntity entity = (LivingEntity) pEntity;
        if (entity instanceof Player ? !((Player) entity).isCreative() : entity.getHealth() > 3.0f) {
            entity.hurt(entity.damageSources().cactus(), 3.0f);
        }
        super.stepOn(pLevel, pPos, pState, pEntity);
    }
}
