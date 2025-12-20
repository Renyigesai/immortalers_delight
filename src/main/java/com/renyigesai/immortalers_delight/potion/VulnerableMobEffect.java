package com.renyigesai.immortalers_delight.potion;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import com.renyigesai.immortalers_delight.util.task.TimekeepingTask;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class VulnerableMobEffect extends MobEffect {
    //=================数据管理部分。这里维护了一个map，用于“次数限制”版的脆弱buff===================//
    private static boolean needClear = false;

    public static void isNeedClear(boolean pFirst) {needClear = pFirst;}
    private static final Map<UUID, Byte> entitiesWithFrequencyLimit = new ConcurrentHashMap<>();
    public static boolean isVulnerableHasLimit(LivingEntity entity) {
        return entitiesWithFrequencyLimit.containsKey(entity.getUUID());
    }
    public static void addVulnerableEntityWithFrequencyLimit(LivingEntity entity, Byte frequencyLimit) {
        entitiesWithFrequencyLimit.put(entity.getUUID(), frequencyLimit);
    }
    public static void removeVulnerableEntityWithFrequencyLimit(LivingEntity entity) {
        entitiesWithFrequencyLimit.remove(entity.getUUID());
    }
    public static Map<UUID, Byte> getEntitiesWithFrequencyLimit() {
        return entitiesWithFrequencyLimit;
    }

    //====================工具方法部分，提供了“添加或消除带次数限制的脆弱buff”的方法========================//
    public VulnerableMobEffect() {
        super(MobEffectCategory.HARMFUL, 11457405);
    }

    public static void addEffectWithFrequencyLimit(LivingEntity entity, MobEffectInstance effect, Byte frequencyLimit) {
        if (entity != null && !entity.level().isClientSide() && entity.isAlive()) {
            addVulnerableEntityWithFrequencyLimit(entity, frequencyLimit);
            entity.addEffect(effect);
        }

    }

    public static void removeEffectWithFrequencyLimit(LivingEntity entity, MobEffect effect) {
        if (entity != null && !entity.level().isClientSide() && entity.isAlive()) {
            removeVulnerableEntityWithFrequencyLimit(entity);
            entity.removeEffect(effect);
        }

    }

    //====================实际功能部分，实现buff本身的易伤功能与次数限制系统，以及清表========================//
    @Override
    public void removeAttributeModifiers(@NotNull LivingEntity pLivingEntity, @NotNull AttributeMap pAttributeMap, int pAmplifier) {

        if (pLivingEntity.level().isClientSide || !entitiesWithFrequencyLimit.containsKey(pLivingEntity.getUUID())) {
            super.removeAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
            return;
        }
        if (pLivingEntity.isAlive()) {
            removeVulnerableEntityWithFrequencyLimit(pLivingEntity);
        }
        super.removeAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
    }

    @Override
    public void addAttributeModifiers(@NotNull LivingEntity pLivingEntity, @NotNull AttributeMap pAttributeMap, int pAmplifier) {

        if (pLivingEntity.level().isClientSide || entitiesWithFrequencyLimit.containsKey(pLivingEntity.getUUID())) {
            super.addAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
            return;
        }
        if (pLivingEntity.isAlive()) {
            boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
            int lv = (isPowerful || pAmplifier >= 255) ? 255 : pAmplifier + 1;
            addVulnerableEntityWithFrequencyLimit(pLivingEntity, (byte) lv);
        }
        super.addAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity pLivingEntity, int pAmplifier) {
        //super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Mod.EventBusSubscriber(
            modid = ImmortalersDelightMod.MODID,
            bus = Mod.EventBusSubscriber.Bus.FORGE
    )
    public static class VulnerablePotionEffect {

        @SubscribeEvent
        public static void onCreatureHurt(LivingHurtEvent evt) {
            if (evt.isCanceled() || evt.getSource().is(DamageTypeTags.BYPASSES_EFFECTS)) {
                return;
            }
            LivingEntity hurtOne = evt.getEntity();
            LivingEntity attacker = null;
            boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
            if (evt.getSource().getEntity() instanceof LivingEntity livingEntity){
                attacker = livingEntity;
            }

            if (!hurtOne.level().isClientSide && attacker != null) {
                if (hurtOne.hasEffect(ImmortalersDelightMobEffect.VULNERABLE.get())){
                    int amplifier = Objects.requireNonNull(hurtOne.getEffect(ImmortalersDelightMobEffect.VULNERABLE.get())).getAmplifier();
                    int number = (isPowerful || amplifier >= 255) ? 255 : amplifier + 1;
                    int lv = amplifier + 1;
                    if (isVulnerableHasLimit(hurtOne)) {
                        if (getEntitiesWithFrequencyLimit().get(hurtOne.getUUID()) > 0) {
                            evt.setAmount(evt.getAmount() * (lv * 0.5F + 1));
                            getEntitiesWithFrequencyLimit().put(hurtOne.getUUID(), (byte) (entitiesWithFrequencyLimit.get(hurtOne.getUUID()) - 1));
                        } else {
                            isNeedClear(true);
                            hurtOne.removeEffect(ImmortalersDelightMobEffect.VULNERABLE.get());
                            removeVulnerableEntityWithFrequencyLimit(hurtOne);
                        }
                    } else addVulnerableEntityWithFrequencyLimit(hurtOne, (byte) number);

                }
            }
        }

        //计划清理易伤map
        @SubscribeEvent
        public static void onTick(@Nonnull TickEvent.ServerTickEvent evt) {
            if (evt.phase.equals(TickEvent.Phase.START)) {
                if (entitiesWithFrequencyLimit.size() > 0 && needClear && TimekeepingTask.getImmortalTickTime() % 1000 <= 50) {
                    Map<UUID, Byte> entitiesNeedClear = new HashMap<>();
                    for (UUID uuid : entitiesWithFrequencyLimit.keySet()) {
                        if (entitiesWithFrequencyLimit.get(uuid) <= 0) {
                            entitiesNeedClear.put(uuid, entitiesWithFrequencyLimit.get(uuid));
                        }
                    }
                    for (UUID uuid : entitiesNeedClear.keySet()) {
                        entitiesWithFrequencyLimit.remove(uuid);
                    }
                    isNeedClear(false);
                }
            }
        }

    }
}
