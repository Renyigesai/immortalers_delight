package com.renyigesai.immortalers_delight.potion;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.message.ImmortalersEffectMessage;
import com.renyigesai.immortalers_delight.network.ImmortalersNetwork;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CrispFortitudeMobEffect extends BaseMobEffect{
    public CrispFortitudeMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 9055202);
    }

    //表示记录的伤害总数值，这个表仅服务端
    private static final Map<UUID,Double> entitiesLostHealthSum = new HashMap<>();
    //表示记录的伤害次数，这个表理论上双端起效
    private static final ConcurrentHashMap<UUID,Byte> entitiesHurt = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<UUID,Byte> getEntitiesHurt() {return entitiesHurt;}

    public static int getMaxCharges(int lv) {
        return 30 - lv * 3;
    }

    /*====实现基础功能支持：在buff去除时清除缓存数据====*/
    public void removeAttributeModifiers(LivingEntity livingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        //buff去除时清除缓存数据
        entitiesLostHealthSum.remove(livingEntity.getUUID());
        entitiesHurt.remove(livingEntity.getUUID());
        super.removeAttributeModifiers(livingEntity,pAttributeMap,pAmplifier);
    }

    @Mod.EventBusSubscriber
    public static class CrispFortitudePotionEffect {
        private static final Map<UUID,Float> entitiesLostHealth = new HashMap<>();
        //酥质效果的具体实现：在此处实现记录伤害，并处理回血
        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void onCreatureAttack(LivingAttackEvent evt) {
            LivingEntity hurtOne = evt.getEntity();
            float oldDamage = evt.getAmount();
            DamageSource source = evt.getSource();

            //判断有源伤害
            if (source.getEntity() != null && source.getEntity() instanceof LivingEntity attacker) {
                //判断buff
                MobEffectInstance instance = hurtOne.getEffect(ImmortalersDelightMobEffect.CRISP_FORTITUDE.get());
                if (instance != null && instance.getEffect() instanceof BaseMobEffect base) {
                    //拿生效等级
                    int truthLv = base.getTruthUsingAmplifier(instance.getAmplifier());

                    //拿受击层数
                    byte count = entitiesHurt.getOrDefault(hurtOne.getUUID(),(byte)0);
                    //判断是否叠加到最大层数
                    if (count >= getMaxCharges(truthLv)) {
                        //清空叠层并回血
                        // 服务端行为
                        if (!hurtOne.level().isClientSide()) {
                            boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
                            //清空当前总计的伤害值回血
                            double heal = entitiesLostHealthSum.getOrDefault(hurtOne.getUUID(),30.0d);
                            if (isPowerful) hurtOne.setHealth((float) (hurtOne.getHealth() + heal));
                            else hurtOne.heal((float) heal);

                            entitiesLostHealthSum.remove(hurtOne.getUUID());
                            entitiesLostHealth.remove(hurtOne.getUUID());
                        }
                        //清空叠层
                        entitiesLostHealthSum.remove(hurtOne.getUUID());
                        entitiesHurt.remove(hurtOne.getUUID());

                    } else {
                        //否则进行一次叠层
                        // 通用行为：增加伤害计数
                        entitiesHurt.put(hurtOne.getUUID(), (byte) (count + 1));
                        // 服务端行为
                        if (!hurtOne.level().isClientSide()) {
                            //记录当前阶段的伤害值
                            entitiesLostHealth.put(hurtOne.getUUID(), oldDamage);
                        }
                    }
                } else entitiesLostHealth.remove(hurtOne.getUUID());
            }
        }

        //对伤害进行取低或取高
        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void onCreatureHurt(LivingHurtEvent evt) {
            LivingEntity hurtOne = evt.getEntity();
            float oldDamage = evt.getAmount();
            DamageSource source = evt.getSource();

            //判断有源伤害
            if (source.getEntity() != null && source.getEntity() instanceof LivingEntity attacker) {
                //判断buff
                MobEffectInstance instance = hurtOne.getEffect(ImmortalersDelightMobEffect.CRISP_FORTITUDE.get());
                if (instance != null){
                    // 服务端行为
                    if (!hurtOne.level().isClientSide()) {
                        //记录当前阶段的伤害值，对伤害值进行取大或取小
                        float last = entitiesLostHealth.getOrDefault(hurtOne.getUUID(), 0f);
                        if (last != oldDamage) {
                            boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
                            boolean lower = last < oldDamage;
                            if (isPowerful == lower) entitiesLostHealth.put(hurtOne.getUUID(), oldDamage);

                        }
                    }
                }
            }
        }

        //实际进行累加伤害，并对伤害进行取低或取高
        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void onCreatureDamage(LivingDamageEvent evt) {
            LivingEntity hurtOne = evt.getEntity();
            float oldDamage = evt.getAmount();
            DamageSource source = evt.getSource();

            //判断有源伤害
            if (source.getEntity() != null && source.getEntity() instanceof LivingEntity attacker) {
                //判断buff
                MobEffectInstance instance = hurtOne.getEffect(ImmortalersDelightMobEffect.CRISP_FORTITUDE.get());
                if (instance != null){
                    // 服务端行为
                    if (!hurtOne.level().isClientSide()) {
                        //记录当前阶段的伤害值，对伤害值进行取大或取小
                        float last = entitiesLostHealth.getOrDefault(hurtOne.getUUID(), 0f);
                        double damage = last;
                        if (last != oldDamage) {
                            boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
                            boolean lower = last < oldDamage;
                            if (isPowerful == lower) damage = oldDamage;
                        }
                        //实际累加受伤总数
                        damage += entitiesLostHealthSum.getOrDefault(hurtOne.getUUID(), 0d);
                        entitiesLostHealthSum.put(hurtOne.getUUID(), damage);
                    }
                }
            }
        }
    }

}
