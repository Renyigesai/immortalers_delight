package com.renyigesai.immortalers_delight.potion;
import net.neoforged.fml.common.EventBusSubscriber;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;

import java.util.Objects;

import static com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect.*;

public class WarmCurrentSurgesMobEffect extends BaseMobEffect {


    public WarmCurrentSurgesMobEffect() {
        super(MobEffectCategory.BENEFICIAL, -39424);
    }

    @Override
    public void applyEffectTickInControl(LivingEntity pEntity, int amplifier) {
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
    public boolean isDurationEffectTickInControl(int duration, int amplifier) {
        return true;
    }



    @EventBusSubscriber(
            modid = ImmortalersDelightMod.MODID)
    public static class WarmCurrentSurgesPotionEffect {
        @SubscribeEvent
        public static void onEntityAddEffect(MobEffectEvent.Applicable event) {
            if (event != null && event.getEntity() != null) {
                Entity entity = event.getEntity();
                if (entity instanceof LivingEntity livingEntity
                        && livingEntity.hasEffect(ImmortalersDelightMobEffect.WARM_CURRENT_SURGES)
                        && event.getEffectInstance().getEffect().is(MobEffects.MOVEMENT_SLOWDOWN)) {
                    //int time = Objects.requireNonNull(livingEntity.getEffect(ImmortalersDelightMobEffect.WARM_CURRENT_SURGES)).getDuration();
                    MobEffectInstance thisEffect = livingEntity.getEffect(ImmortalersDelightMobEffect.WARM_CURRENT_SURGES);
                    int lv = thisEffect != null && thisEffect.getEffect() instanceof BaseMobEffect effect ? effect.getTruthUsingAmplifier(thisEffect.getAmplifier()) : 0;
                    //int timeEvt = event.getEffectInstance().getDuration();
                    int lvEvt = event.getEffectInstance().getAmplifier();
                    if (lv >= lvEvt){
                        event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
                    }
                }
            }
        }

        @SubscribeEvent
        public static void onCreatureHurt(LivingDamageEvent.Pre evt) {
            if (evt.getSource().is(DamageTypeTags.BYPASSES_RESISTANCE)) {
                return;
            }
            LivingEntity hurtOne = evt.getEntity();
            LivingEntity attacker = null;
            Boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
            if (evt.getSource().getEntity() instanceof LivingEntity livingEntity){
                attacker = livingEntity;
            }

            if (!hurtOne.level().isClientSide) {
                if (attacker != null){
                    MobEffectInstance thisEffect = attacker.getEffect(ImmortalersDelightMobEffect.WARM_CURRENT_SURGES);
                    if (thisEffect != null && thisEffect.getEffect() instanceof BaseMobEffect effect) {
                        int lv = effect.getTruthUsingAmplifier(thisEffect.getAmplifier());
                        float damage = hurtOne.getRemainingFireTicks() > 1 ? 4 << lv : 2 << lv;
                        //立即结算目标的着火伤害，结算上限为每级8点伤害
                        if (isPowerful) {
                            damage += Math.min(hurtOne.getRemainingFireTicks() / 20, (lv + 1) * 8);
                            hurtOne.setRemainingFireTicks(0);
                        }
                        hurtOne.invulnerableTime = 0;
                        hurtOne.hurt(hurtOne.damageSources().onFire(), damage);
                        hurtOne.invulnerableTime = 0;
                    }
                }
            }
        }
    }
}
