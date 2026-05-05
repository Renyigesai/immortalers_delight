package com.renyigesai.immortalers_delight.potion;
import net.neoforged.fml.common.EventBusSubscriber;

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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SmokeAbstinenceEffect extends BaseMobEffect {
    public SmokeAbstinenceEffect() {
        super(MobEffectCategory.BENEFICIAL,-6710887);
    }

    @Override
    public void applyEffectTickInControl(LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity.level().isClientSide()){
            return;
        }
        if (pLivingEntity instanceof Player player && player instanceof ServerPlayer serverPlayer){
            /*重置玩家的上一次睡觉时间为0*/
            serverPlayer.getStats().setValue(serverPlayer, Stats.CUSTOM.get(Stats.TIME_SINCE_REST),0);
        }
        //在下界时添加效果
        this.removeAttributeModifiers(pLivingEntity.getAttributes());
        if (pLivingEntity.level().dimension() == Level.NETHER) {
            removeEffects(pLivingEntity, pAmplifier);
            //力量急迫属性加成
            this.addAttributeModifiers(pLivingEntity.getAttributes(), pAmplifier);
            //生命回复回血
            float health = 1 << pAmplifier;
            pLivingEntity.heal(health);
        }
    }

    @Override
    public boolean isDurationEffectTickInControl(int pDuration, int pAmplifier) {
        /*不频繁执行，每2.5秒执行一次*/
        return pDuration % 50 == 0;
    }

    //获取属性修饰符的数值（自定义曲线；非 Mojang MobEffect 覆盖）
    public double getAttributeModifierValue(int pAmplifier, AttributeModifier pModifier) {
        boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
        if (pModifier.operation() == AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
            if (isPowerful) return pModifier.amount() * (double)(pAmplifier * 3 + 3);
            return pModifier.amount() * (double)(pAmplifier + 3);
        }
        if (pModifier.operation() == AttributeModifier.Operation.ADD_VALUE) {
            if (isPowerful) return pModifier.amount() * (double)(pAmplifier * 2 + 2);
            return pModifier.amount() * (double)(pAmplifier + 2);
        }
        return pModifier.amount() * (double)(pAmplifier + 1);
    }

    //添加效果，效果等级随本效果等级提升
    //I级破烟的效果为力量II、抗火、急迫III、抗性II、生命恢复，普通模式为加算等级，否则为乘算等级
    //不使用抗性提升以免出现抗性V
    static void removeEffects(LivingEntity entity, int pAmplifier){
        entity.removeEffect(MobEffects.DAMAGE_BOOST);
        entity.removeEffect(MobEffects.FIRE_RESISTANCE);
        entity.removeEffect(MobEffects.DAMAGE_RESISTANCE);
        entity.removeEffect(MobEffects.REGENERATION);
        entity.removeEffect(MobEffects.DIG_SPEED);
    }

    @EventBusSubscriber(
            modid = ImmortalersDelightMod.MODID)
    public static class SmokeAbstinenceEffectEvent {
        //与先其派生的效果互斥
        @SubscribeEvent
        public static void onEntityAddEffect(MobEffectEvent.Applicable event) {
            if (event != null && event.getEntity() != null) {
                Entity entity = event.getEntity();
                if (entity instanceof LivingEntity livingEntity
                        && livingEntity.level().dimension() == Level.NETHER
                        && livingEntity.hasEffect(ImmortalersDelightMobEffect.SMOKE_ABSTINENCE)
                        && (event.getEffectInstance().getEffect().is(MobEffects.DAMAGE_BOOST)
                        || event.getEffectInstance().getEffect().is(MobEffects.FIRE_RESISTANCE)
                        || event.getEffectInstance().getEffect().is(MobEffects.DAMAGE_RESISTANCE)
                        || event.getEffectInstance().getEffect().is(MobEffects.REGENERATION)
                        || event.getEffectInstance().getEffect().is(MobEffects.DIG_SPEED)
                        )) {
                    event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
                }
            }
        }
        //实现急迫III加挖掘速度的效果
        @SubscribeEvent
        public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
            Player player = event.getEntity();
            if (player.level().dimension() != Level.NETHER) {
                return;
            }

            // 检查玩家是否有破烟效果
            MobEffectInstance thisEffect = player.getEffect(ImmortalersDelightMobEffect.SMOKE_ABSTINENCE);
            if (thisEffect != null && thisEffect.getEffect() instanceof BaseMobEffect baseMobEffect) {
                int amplifier = baseMobEffect.getTruthUsingAmplifier(thisEffect.getAmplifier());

                boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
                float multiplier = 1.0F + (isPowerful ? 0.2F * (amplifier * 3 + 3) : 0.2F * (amplifier + 3));

                // 设置新速度（原速度 × 倍率）
                event.setNewSpeed(event.getNewSpeed() * multiplier);
            }
        }
        //实现抗性II与抗火的效果
        @SubscribeEvent
        public static void onCreatureHurt(LivingDamageEvent.Pre evt) {
            if (evt.getSource().is(DamageTypeTags.BYPASSES_RESISTANCE)) {
                return;
            }
            LivingEntity hurtOne = evt.getEntity();
            if (!hurtOne.level().isClientSide() && hurtOne.level().dimension() == Level.NETHER) {
                float damage = evt.getNewDamage();
                MobEffectInstance effect = hurtOne.getEffect(ImmortalersDelightMobEffect.SMOKE_ABSTINENCE);
                if (effect != null && effect.getEffect() instanceof BaseMobEffect baseMobEffect) {
                    boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
                    int lv = baseMobEffect.getTruthUsingAmplifier(effect.getAmplifier());
                    //因为原意为派生抗性进行减伤，因此其减伤与抗性提升互斥
                    MobEffectInstance effect2 = hurtOne.getEffect(MobEffects.DAMAGE_RESISTANCE);
                    if (effect2 != null) {
                        lv -= effect2.getAmplifier();
                    }
                    if (lv >= 0) {
                        float buffer = 1.0F;
                        for (int i = 0; i <= (isPowerful ? lv * 2 + 1: lv + 1); i++) {
                            buffer = buffer * 0.8F;
                        }
                        evt.setNewDamage(Math.min(damage * buffer, damage));
                    }
                    //因为原意为派生抗火进行防火，因此其防火效果与抗火互斥
                    MobEffectInstance effect3 = hurtOne.getEffect(MobEffects.FIRE_RESISTANCE);
                    if (effect3 == null) {
                        evt.setNewDamage(0);
                    }
                }
            }
        }
    }

}
