package com.renyigesai.immortalers_delight.entities.living;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.InfestedBlock;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class SkelverfishBase extends Silverfish {
    public SkelverfishBase(EntityType<? extends Silverfish> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }//声明为亡灵生物
    /**
     * 被动技能：免疫火焰和爆炸伤害
     * @param pSource
     * @param pAmount
     * @return
     */
    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.isInvulnerableTo(pSource)) {
            return false;
        } else if (pSource.is(DamageTypeTags.IS_EXPLOSION) || pSource.is(DamageTypeTags.IS_FIRE)) {
            return false;
        } else return super.hurt(pSource, pAmount);
    }
}
