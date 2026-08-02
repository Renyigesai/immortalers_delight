package com.renyigesai.immortalers_delight.potion;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import com.renyigesai.immortalers_delight.util.LivingDamageUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

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



    @Mod.EventBusSubscriber(
            modid = ImmortalersDelightMod.MODID,
            bus = Mod.EventBusSubscriber.Bus.FORGE
    )
    public static class WarmCurrentSurgesPotionEffect {
        @SubscribeEvent
        public static void onEntityAddEffect(MobEffectEvent.Applicable event) {
            if (event != null && event.getEntity() != null) {
                Entity entity = event.getEntity();
                if (entity instanceof LivingEntity livingEntity
                        && livingEntity.hasEffect(ImmortalersDelightMobEffect.WARM_CURRENT_SURGES.get())
                        && event.getEffectInstance().getEffect() == MobEffects.MOVEMENT_SLOWDOWN) {
                    //int time = Objects.requireNonNull(livingEntity.getEffect(ImmortalersDelightMobEffect.WARM_CURRENT_SURGES.get())).getDuration();
                    MobEffectInstance thisEffect = livingEntity.getEffect(ImmortalersDelightMobEffect.WARM_CURRENT_SURGES.get());
                    int lv = thisEffect != null && thisEffect.getEffect() instanceof BaseMobEffect effect ? effect.getTruthUsingAmplifier(thisEffect.getAmplifier()) : 0;
                    //int timeEvt = event.getEffectInstance().getDuration();
                    int lvEvt = event.getEffectInstance().getAmplifier();
                    if (lv >= lvEvt){
                        event.setResult(Event.Result.DENY);
                    }
                }
            }
        }
        private static final Map<UUID,Float> entityDamage = new HashMap<UUID,Float>();

        //使用事件强化伤害
        //造成附加的火焰伤害，出伤方式为一半概率setHealth一般概率actuallyHurt
        @SubscribeEvent
        public static void onCreatureAttack(LivingAttackEvent evt) {
            if (evt.isCanceled() || evt.getSource().is(DamageTypeTags.BYPASSES_EFFECTS)) {
                return;
            }
            LivingEntity hurtOne = evt.getEntity();
            LivingEntity attacker = null;
            Boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
            if (evt.getSource().getEntity() instanceof LivingEntity livingEntity){
                attacker = livingEntity;
            }

            //火焰伤害不能触发火成的附加伤害，以免出现有源火焰伤害导致无限问题
            if (!evt.getSource().is(DamageTypeTags.IS_FIRE)) {
                if (attacker != null){
                    MobEffectInstance thisEffect = attacker.getEffect(ImmortalersDelightMobEffect.WARM_CURRENT_SURGES.get());
                    if (thisEffect != null && thisEffect.getEffect() instanceof BaseMobEffect effect) {

                        boolean blackSnake = false;
                        MobEffectInstance poYanEffect = attacker.getEffect(SMOKE_ABSTINENCE.get());
                        if (poYanEffect != null) blackSnake = true;
                        //实现破烟去除生物火焰抗性
                        if (blackSnake) hurtOne.getPersistentData().putBoolean("immortalers_delight_fear_fire",true);

                        //触发附加火焰伤害要求伤害值不能过低，以限制连点器
                        if (!isPowerful) {
                            AttributeInstance attr = attacker.getAttribute(Attributes.ATTACK_DAMAGE);
                            double atk = attr == null ? 0 : attr.getValue();
                            if (evt.getAmount() < 1 || (evt.getAmount() < atk * 0.6f)) return;
                        }

                        if (!hurtOne.level().isClientSide) {
                            int lv = effect.getTruthUsingAmplifier(thisEffect.getAmplifier());
                            float damage = hurtOne.getRemainingFireTicks() > 1 ? 4 << lv : 2 << lv;
                            //立即结算目标的着火伤害，结算上限为每级8点伤害
                            if (isPowerful) {
                                damage += Math.min(hurtOne.getRemainingFireTicks() / 20, (lv + 1) * 8);
                                hurtOne.setRemainingFireTicks(0);
                            }
                            //火焰伤害无视免疫的逻辑
                            if (blackSnake) {
                                blackSnake(hurtOne,attacker,damage);
                            } else {
                                //普通火焰伤害
                                hurtOne.invulnerableTime = 0;
                                hurtOne.hurt(hurtOne.damageSources().onFire(),damage);
                                hurtOne.invulnerableTime = 0;
                            }
                        }
                    }
                }
                //如果目标是被标注了破火炕的，同时这个伤害是火焰伤害
            } else if (isPowerful && hurtOne.getPersistentData().contains("immortalers_delight_fear_fire")){
                float health = hurtOne.getHealth();
                //启动无视免疫
                blackSnake(hurtOne,attacker,evt.getAmount());
                //如果生物掉血了，取消原伤害
                if (hurtOne.getHealth() < health) evt.setCanceled(true);
            }


        }

        public static void blackSnake(LivingEntity hurtOne, @Nullable LivingEntity attacker, float damage) {
            //先记录目标的血量
            float health = hurtOne.getHealth();

            hurtOne.invulnerableTime = 0;
            //一半概率使用反射actuallyHurt方法出伤害（但此处仍能获取伤害类型，可能被写免疫）
            if (hurtOne.getRandom().nextBoolean()) {
                LivingDamageUtil.callActuallyHurt(hurtOne,hurtOne.damageSources().onFire(), damage);
                if (hurtOne.isDeadOrDying()) {
                    if (attacker != null) hurtOne.die(hurtOne.damageSources().mobAttack(attacker));
                    else hurtOne.die(hurtOne.damageSources().genericKill());
                }
                //一半概率使用setHealth出伤害，但有的实体可能免疫setHealth
            } else LivingDamageUtil.hurtEntity(hurtOne,hurtOne.damageSources().onFire(),damage);
            hurtOne.invulnerableTime = 0;

            //保底逻辑，如果没有成功出伤，原样使用hurt方法打一个伪装成火伤的真实伤害
            entityDamage.put(hurtOne.getUUID(), health - damage);
        }
        //使用事件强化伤害
        //保底逻辑，如果没有成功出伤，原样使用hurt方法打一个伪装成火伤的真实伤害
        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public static void onLivingTick(LivingEvent.LivingTickEvent event) {
            boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
            LivingEntity pEntity = event.getEntity();

            if (entityDamage.containsKey(pEntity.getUUID())) {

                float health = pEntity.getHealth();
                float needHealth = entityDamage.get(pEntity.getUUID());

                if (isPowerful && health > needHealth) {
                    if (needHealth > 0) pEntity.hurt(pEntity.damageSources().genericKill(),health - needHealth);
                    else pEntity.hurt(new DamageSource(pEntity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("immortalers_delight:black_snake")))), health - needHealth);
                }
                entityDamage.remove(pEntity.getUUID());
            }
        }


    }
}
