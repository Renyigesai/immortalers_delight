package com.renyigesai.immortalers_delight.potion;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SmokeAbstinenceEffect extends MobEffect {
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
        }
        //在下界时添加效果
        if (pLivingEntity.level().dimension() == Level.NETHER) {
            /*添加派生药水效果*/
            addEffects(pLivingEntity,pAmplifier);
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        /*不频繁执行，每5秒执行一次*/
        return pDuration % 100 == 0;
    }

    //添加效果，效果等级随本效果等级提升
    //I级破烟的效果为力量II、抗火、急迫III、抗性II、生命恢复，普通模式为加算等级，否则为乘算等级
    //不使用抗性提升以免出现抗性V
    static void addEffects(LivingEntity entity, int pAmplifier){
        boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
        entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST,300,isPowerful ? pAmplifier * 2 + 1 : pAmplifier + 1));
        entity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE,300));
        entity.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED,300,isPowerful ? pAmplifier * 3 + 2 : pAmplifier + 2));
        entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION,300,pAmplifier));
    }

    @Mod.EventBusSubscriber(
            modid = ImmortalersDelightMod.MODID,
            bus = Mod.EventBusSubscriber.Bus.FORGE
    )
    public static class SmokeAbstinenceEffectEvent {
        @SubscribeEvent
        public static void onCreatureHurt(LivingHurtEvent evt) {
            if (evt.isCanceled() || evt.getSource().is(DamageTypeTags.BYPASSES_RESISTANCE)) {
                return;
            }
            LivingEntity hurtOne = evt.getEntity();

            if (!hurtOne.level().isClientSide) {
                float damage = evt.getAmount();
                MobEffectInstance effect = hurtOne.getEffect(ImmortalersDelightMobEffect.SMOKE_ABSTINENCE.get());
                if (effect != null) {
                    boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
                    //因为原意为派生抗性进行减伤，因此其减伤与抗性提升互斥
                    int lv = effect.getAmplifier();
                    MobEffectInstance effect2 = hurtOne.getEffect(MobEffects.DAMAGE_RESISTANCE);
                    if (effect2 != null) {
                        lv -= effect2.getAmplifier();
                    }
                    if (lv >= 0) {
                        float buffer = 1.0F;
                        for (int i = 0; i <= (isPowerful ? lv * 2 + 1: lv + 1); i++) {
                            buffer = buffer * 0.8F;
                        }
                        evt.setAmount(Math.min(damage * buffer, damage));
                    }
                }
            }
        }
    }

}
