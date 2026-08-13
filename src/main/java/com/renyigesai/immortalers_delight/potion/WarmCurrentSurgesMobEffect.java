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
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect.SMOKE_ABSTINENCE;
import static com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect.WARM_CURRENT_SURGES;

public class WarmCurrentSurgesMobEffect extends BaseMobEffect {

    public WarmCurrentSurgesMobEffect() {
        super(MobEffectCategory.BENEFICIAL, -39424);
    }

    @Override
    public void applyEffectTickInControl(LivingEntity pEntity, int amplifier) {
        if (this == WARM_CURRENT_SURGES.get()) {
            if (pEntity.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
                int lv = pEntity.hasEffect(MobEffects.MOVEMENT_SLOWDOWN) ? Objects.requireNonNull(pEntity.getEffect(MobEffects.MOVEMENT_SLOWDOWN)).getAmplifier() : 0;
                if (amplifier >= lv) {
                    pEntity.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
                }
            }

            if (!(pEntity.level().isClientSide)) {
                Level level = pEntity.level();
                removeSnow(level, pEntity.getX(), pEntity.getY(), pEntity.getZ());
            }
        }
    }

    public void removeSnow(Level pLevel, double x, double y, double z) {
        BlockPos blockPos = BlockPos.containing(x, y, z);
        BlockState blockstate = pLevel.getBlockState(blockPos);
        if (blockstate.is(BlockTags.SNOW)) {
            for (int i = 0; i < 2; i++) {
                pLevel.destroyBlock(BlockPos.containing(x, y + i, z), false);
            }
        }
    }

    @Override
    public boolean isDurationEffectTickInControl(int duration, int amplifier) {
        return true;
    }

    @EventBusSubscriber(modid = ImmortalersDelightMod.MODID)
    public static class WarmCurrentSurgesPotionEffect {
        private static final Map<UUID, Float> entityDamage = new HashMap<>();

        @SubscribeEvent
        public static void onEntityAddEffect(MobEffectEvent.Applicable event) {
            if (event != null && event.getEntity() != null) {
                Entity entity = event.getEntity();
                if (entity instanceof LivingEntity livingEntity
                        && livingEntity.hasEffect(ImmortalersDelightMobEffect.WARM_CURRENT_SURGES)
                        && event.getEffectInstance().getEffect().is(MobEffects.MOVEMENT_SLOWDOWN)) {
                    MobEffectInstance thisEffect = livingEntity.getEffect(ImmortalersDelightMobEffect.WARM_CURRENT_SURGES);
                    int lv = thisEffect != null && thisEffect.getEffect().value() instanceof BaseMobEffect effect ? effect.getTruthUsingAmplifier(thisEffect.getAmplifier()) : 0;
                    int lvEvt = event.getEffectInstance().getAmplifier();
                    if (lv >= lvEvt) {
                        event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
                    }
                }
            }
        }

        @SubscribeEvent
        public static void onCreatureAttack(LivingIncomingDamageEvent evt) {
            if (evt.isCanceled() || evt.getSource().is(DamageTypeTags.BYPASSES_EFFECTS)) {
                return;
            }
            LivingEntity hurtOne = evt.getEntity();
            LivingEntity attacker = null;
            boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
            if (evt.getSource().getEntity() instanceof LivingEntity livingEntity) {
                attacker = livingEntity;
            }

            if (!evt.getSource().is(DamageTypeTags.IS_FIRE)) {
                if (attacker != null) {
                    MobEffectInstance thisEffect = attacker.getEffect(ImmortalersDelightMobEffect.WARM_CURRENT_SURGES);
                    if (thisEffect != null && thisEffect.getEffect().value() instanceof BaseMobEffect effect) {

                        boolean blackSnake = attacker.hasEffect(SMOKE_ABSTINENCE);
                        if (blackSnake) {
                            hurtOne.getPersistentData().putBoolean("immortalers_delight_fear_fire", true);
                        }

                        if (!isPowerful) {
                            AttributeInstance attr = attacker.getAttribute(Attributes.ATTACK_DAMAGE);
                            double atk = attr == null ? 0 : attr.getValue();
                            if (evt.getAmount() < 1 || (evt.getAmount() < atk * 0.6f)) return;
                        }

                        if (!hurtOne.level().isClientSide) {
                            int lv = effect.getTruthUsingAmplifier(thisEffect.getAmplifier());
                            float damage = hurtOne.getRemainingFireTicks() > 1 ? 4 << lv : 2 << lv;
                            if (isPowerful) {
                                damage += Math.min(hurtOne.getRemainingFireTicks() / 20, (lv + 1) * 8);
                                hurtOne.setRemainingFireTicks(0);
                            }
                            if (blackSnake) {
                                blackSnake(hurtOne, attacker, damage);
                            } else {
                                hurtOne.invulnerableTime = 0;
                                hurtOne.hurt(hurtOne.damageSources().onFire(), damage);
                                hurtOne.invulnerableTime = 0;
                            }
                        }
                    }
                }
            } else if (isPowerful && hurtOne.getPersistentData().contains("immortalers_delight_fear_fire")) {
                float health = hurtOne.getHealth();
                blackSnake(hurtOne, attacker, evt.getAmount());
                if (hurtOne.getHealth() < health) evt.setCanceled(true);
            }
        }

        public static void blackSnake(LivingEntity hurtOne, @Nullable LivingEntity attacker, float damage) {
            float health = hurtOne.getHealth();

            hurtOne.invulnerableTime = 0;
            if (hurtOne.getRandom().nextBoolean()) {
                LivingDamageUtil.callActuallyHurt(hurtOne, hurtOne.damageSources().onFire(), damage);
                if (hurtOne.isDeadOrDying()) {
                    if (attacker != null) hurtOne.die(hurtOne.damageSources().mobAttack(attacker));
                    else hurtOne.die(hurtOne.damageSources().genericKill());
                }
            } else {
                LivingDamageUtil.hurtEntity(hurtOne, hurtOne.damageSources().onFire(), damage);
            }
            hurtOne.invulnerableTime = 0;

            entityDamage.put(hurtOne.getUUID(), health - damage);
        }

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public static void onLivingTick(EntityTickEvent.Post event) {
            if (!(event.getEntity() instanceof LivingEntity pEntity)) return;
            boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();

            if (entityDamage.containsKey(pEntity.getUUID())) {
                float health = pEntity.getHealth();
                float needHealth = entityDamage.get(pEntity.getUUID());

                if (isPowerful && health > needHealth) {
                    if (needHealth > 0) {
                        pEntity.hurt(pEntity.damageSources().genericKill(), health - needHealth);
                    } else {
                        pEntity.hurt(new DamageSource(pEntity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(
                                ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath("immortalers_delight", "black_snake"))
                        )), health - needHealth);
                    }
                }
                entityDamage.remove(pEntity.getUUID());
            }
        }
    }
}
