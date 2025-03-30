package com.renyigesai.immortalers_delight.potion;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;

import static com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect.*;

public class WarmCurrentSurgesMobEffect extends MobEffect {


    public WarmCurrentSurgesMobEffect() {
        super(MobEffectCategory.HARMFUL, -39424);
    }

    @Override
    public void applyEffectTick(LivingEntity pEntity, int amplifier) {
        if (this == WARM_CURRENT_SURGES.get()) {
            //免疫缓慢的逻辑实现
            if (pEntity.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
                int lv = pEntity.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)? Objects.requireNonNull(pEntity.getEffect(MobEffects.MOVEMENT_SLOWDOWN)).getAmplifier():0;
                if (amplifier >= lv) {
                    pEntity.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
                }
            }

            //除雪的逻辑实现
            if (!(pEntity.level().isClientSide)) {
                Level level = pEntity.level();
                removeSnow(level,pEntity.getX(),pEntity.getY(),pEntity.getZ());
            }
        }
    }
    public void removeSnow(Level pLevel, double x,double y,double z) {
        BlockPos blockPos = BlockPos.containing(x,y,z);
        BlockState blockstate = pLevel.getBlockState(blockPos);
        if (blockstate.is(BlockTags.SNOW)) {
            for (int i = 0; i < 2; i++) {
                pLevel.destroyBlock(BlockPos.containing(x,y+i,z),false);
            }
        }
    }
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
