package com.renyigesai.immortalers_delight.potion;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SmokeAbstinenceEffect extends MobEffect {
    public static final List<Supplier<MobEffectInstance>> EFFECTS;
    public SmokeAbstinenceEffect() {
        super(MobEffectCategory.BENEFICIAL,-6710887);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        super.applyEffectTick(pLivingEntity, pAmplifier);
        if (pLivingEntity.level().isClientSide()){
            return;
        }
        if (pLivingEntity instanceof Player player && player instanceof ServerPlayer serverPlayer){
            /*重置玩家的上一次睡觉时间为0*/
            serverPlayer.getStats().setValue(serverPlayer, Stats.CUSTOM.get(Stats.TIME_SINCE_REST),0);
            /*添加派生药水效果*/
            EFFECTS.forEach(effect -> pLivingEntity.addEffect(effect.get()));
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        /*不频繁执行，每5秒执行一次*/
        return pDuration % 100 == 0;
    }

    static {
        EFFECTS = new ArrayList<>();
        EFFECTS.add(()-> new MobEffectInstance(MobEffects.DAMAGE_BOOST,300,1));
        EFFECTS.add(()-> new MobEffectInstance(MobEffects.FIRE_RESISTANCE,300));
        EFFECTS.add(()-> new MobEffectInstance(MobEffects.DIG_SPEED,300,2));
        EFFECTS.add(()-> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,300,1));
        EFFECTS.add(()-> new MobEffectInstance(MobEffects.REGENERATION,300));
    }
}
