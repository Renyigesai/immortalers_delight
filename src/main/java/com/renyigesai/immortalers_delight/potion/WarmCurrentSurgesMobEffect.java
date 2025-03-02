package com.renyigesai.immortalers_delight.potion;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.block.CulturalLegacyEffectToolBlock;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
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
                int randPos = pEntity.getRandom().nextInt(9);
                for(int i = -1; i <= 1; ++i) {
                    for(int j = -1; j <= 1; ++j) {
                        BlockPos blockPos = pEntity.getOnPos().offset(i, 1, j);
                        if (pEntity.isInPowderSnow) {
                            removeSnow(level,pEntity.getOnPos().above());
                            removeSnow(level,pEntity.getOnPos().above().above());
                            break;
                        }
                        if (i + 1 + (j + 1) * 3 == randPos){
                            removeSnow(level,blockPos);
                            removeSnow(level,blockPos.above());
                            break;
                        }
                    }
                }
            }
        }
    }
    public void removeSnow(Level pLevel, BlockPos blockPos) {
        BlockState blockstate = pLevel.getBlockState(blockPos);
        if (blockstate.is(BlockTags.SNOW)) {
            pLevel.setBlock(blockPos, Blocks.AIR.defaultBlockState(),2);
        }
    }
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 40 == 0;
    }
}
