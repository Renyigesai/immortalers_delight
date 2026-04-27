package com.renyigesai.immortalers_delight.potion;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class SmokeAbstinenceEffect extends BaseMobEffect {
    public SmokeAbstinenceEffect() {
        super(MobEffectCategory.BENEFICIAL,-6710887);
    }

    @Override
    public void applyEffectTickInControl(LivingEntity pLivingEntity, int pAmplifier) {
        // 这里的 pAmplifier 已经是 BaseMobEffect 换算后的真实等级，不能再回调父类入口。
        if (pLivingEntity.level().isClientSide()){
            return;
        }
        if (pLivingEntity instanceof Player player && player instanceof ServerPlayer serverPlayer){
            /*重置玩家的上一次睡觉时间为0*/
            serverPlayer.getStats().setValue(serverPlayer, Stats.CUSTOM.get(Stats.TIME_SINCE_REST),0);
        }
        //在下界时添加效果
        this.removeAttributeModifiers(pLivingEntity, pLivingEntity.getAttributes(), pAmplifier);
        if (pLivingEntity.level().dimension() == Level.NETHER) {
            removeEffects(pLivingEntity);
            //力量急迫属性加成
            this.addAttributeModifiers(pLivingEntity, pLivingEntity.getAttributes(), pAmplifier);
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

    //获取属性修饰符的数值
    @Override
    public double getAttributeModifierValue(int pAmplifier, AttributeModifier pModifier) {
        boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
        if (pModifier.getOperation().toValue() == 2) {
            if (isPowerful) return pModifier.getAmount() * (double)(pAmplifier * 3 + 3);
            return pModifier.getAmount() * (double)(pAmplifier + 3);
        }
        if (pModifier.getOperation().toValue() == 0) {
            if (isPowerful) return pModifier.getAmount() * (double)(pAmplifier * 2 + 2);
            return pModifier.getAmount() * (double)(pAmplifier + 2);
        }
        return pModifier.getAmount() * (double)(pAmplifier + 1);
    }

    //添加效果，效果等级随本效果等级提升
    //I级破烟的效果为力量II、抗火、急迫III、抗性II、生命恢复，普通模式为加算等级，否则为乘算等级
    //不使用抗性提升以免出现抗性V
    static void removeEffects(LivingEntity entity){
        entity.removeEffect(MobEffects.DAMAGE_BOOST);
        entity.removeEffect(MobEffects.FIRE_RESISTANCE);
        entity.removeEffect(MobEffects.DAMAGE_RESISTANCE);
        entity.removeEffect(MobEffects.REGENERATION);
        entity.removeEffect(MobEffects.DIG_SPEED);
    }

    @Mod.EventBusSubscriber(
            modid = ImmortalersDelightMod.MODID,
            bus = Mod.EventBusSubscriber.Bus.FORGE
    )
    public static class SmokeAbstinenceEffectEvent {
        //与先其派生的效果互斥
        @SubscribeEvent
        public static void onEntityAddEffect(MobEffectEvent.Applicable event) {
            if (event != null && event.getEntity() != null) {
                Entity entity = event.getEntity();
                if (entity instanceof LivingEntity livingEntity
                        && livingEntity.level().dimension() == Level.NETHER
                        && livingEntity.hasEffect(ImmortalersDelightMobEffect.SMOKE_ABSTINENCE.get())
                        && (event.getEffectInstance().getEffect() == MobEffects.DAMAGE_BOOST
                        || event.getEffectInstance().getEffect() == MobEffects.FIRE_RESISTANCE
                        || event.getEffectInstance().getEffect() == MobEffects.DAMAGE_RESISTANCE
                        || event.getEffectInstance().getEffect() == MobEffects.REGENERATION
                        || event.getEffectInstance().getEffect() == MobEffects.DIG_SPEED
                        )) {
                    event.setResult(Event.Result.DENY);
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
            MobEffectInstance thisEffect = player.getEffect(ImmortalersDelightMobEffect.SMOKE_ABSTINENCE.get());
            if (thisEffect != null && thisEffect.getEffect() instanceof BaseMobEffect baseMobEffect) {
                int amplifier = baseMobEffect.getTruthUsingAmplifier(thisEffect.getAmplifier());

                boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
                float multiplier = 1.0F + (isPowerful ? 0.2F * (amplifier * 3 + 3) : 0.2F * (amplifier + 3));

                // 设置新速度（原速度 × 倍率）
                event.setNewSpeed(event.getNewSpeed() * multiplier);
            }
        }
        // 实现全维度减伤，并在下界额外模拟抗火效果。
        @SubscribeEvent
        public static void onCreatureHurt(LivingHurtEvent evt) {
            if (evt.isCanceled() || evt.getSource().is(DamageTypeTags.BYPASSES_RESISTANCE)) {
                return;
            }
            LivingEntity hurtOne = evt.getEntity();
            if (!hurtOne.level().isClientSide()) {
                float damage = evt.getAmount();
                MobEffectInstance effect = hurtOne.getEffect(ImmortalersDelightMobEffect.SMOKE_ABSTINENCE.get());
                if (effect != null && effect.getEffect() instanceof BaseMobEffect baseMobEffect) {
                    boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
                    int lv = baseMobEffect.getTruthUsingAmplifier(effect.getAmplifier());
                    // 词条语义是“破烟减伤”与抗性提升互斥，抗性更高时破烟不再额外提供减伤。
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
                    // 词条中的“持续获得抗火”只应拦截火焰类伤害，不能把所有伤害都归零。
                    if (hurtOne.level().dimension() == Level.NETHER && evt.getSource().is(DamageTypeTags.IS_FIRE)) {
                        evt.setAmount(0);
                        evt.setCanceled(true);
                    }
                }
            }
        }
    }

}
