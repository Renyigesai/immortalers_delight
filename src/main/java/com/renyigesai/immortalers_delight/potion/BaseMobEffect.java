package com.renyigesai.immortalers_delight.potion;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.level.Level;

import java.util.Random;

public class BaseMobEffect extends MobEffect {

    public BaseMobEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }
//    @Override
//    public boolean isDurationEffectTick(int duration, int amplifier) {
//        return true;
//    }

}
