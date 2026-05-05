package com.renyigesai.immortalers_delight.potion.immortaleffects;

import com.renyigesai.immortalers_delight.util.datautil.EffectData;
import com.renyigesai.immortalers_delight.util.task.TimekeepingTask;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@EventBusSubscriber
public class DeathlessEffect {

    private static final Map<UUID, EffectData> entityHasEffect = new ConcurrentHashMap<>();

    public static Map<UUID, EffectData> getEntityMap() {
        return entityHasEffect;
    }

    public static void applyImmortalEffect(LivingEntity entity, int duration, int amplifier) {
        if (entity == null || entity.isRemoved() || entity.level().isClientSide()) {
            return;
        }
        UUID uuid = entity.getUUID();
        long expireTime = TimekeepingTask.getImmortalTickTime() + duration * 50L;
        EffectData effectData = new EffectData(entity.blockPosition(), expireTime, amplifier, entity.getRandom().nextInt());
        entityHasEffect.put(uuid, effectData);
    }

    public static void removeImmortalEffect(LivingEntity entity) {
        if (entity == null || entity.isRemoved() || entity.level().isClientSide()) {
            return;
        }
        UUID uuid = entity.getUUID();
        if (entityHasEffect.get(uuid) != null) {
            entityHasEffect.remove(uuid);
        }
        onImmortalEffectRemove(entity);
    }

    private static void onImmortalEffectRemove(LivingEntity entity) {
    }

    @SubscribeEvent
    public static void onLevelTick(@Nonnull LevelTickEvent.Pre evt) {
        HashMap<UUID, EffectData> map = new HashMap<>(entityHasEffect);
        if (!map.isEmpty() && TimekeepingTask.getImmortalTickTime() % 1000 * (map.size() + 1) <= 50) {
            CheckAndClearMap(evt.getLevel());
        }
    }

    public static void CheckAndClearMap(Level level) {
        Map<UUID, EffectData> needClearMap = new HashMap<>();
        for (UUID uuid : entityHasEffect.keySet()) {
            EffectData effectData = entityHasEffect.get(uuid);
            if (effectData == null) {
                continue;
            }
            if (TimekeepingTask.getImmortalTickTime() > effectData.getTime()) {
                needClearMap.put(uuid, effectData);
            }
        }
        for (UUID uuid : needClearMap.keySet()) {
            if (level instanceof ServerLevel serverLevel && serverLevel.getEntity(uuid) instanceof LivingEntity living) {
                removeImmortalEffect(living);
                entityHasEffect.remove(uuid);
            }
            if (level.isClientSide()) {
                entityHasEffect.remove(uuid);
            }
        }
    }

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof LivingEntity entity)) {
            return;
        }
        if (entity.isRemoved() || !entity.isAlive()) {
            return;
        }
        if (!entity.level().isClientSide()) {
            UUID uuid = entity.getUUID();
            HashMap<UUID, EffectData> map = new HashMap<>(entityHasEffect);
            if (map.get(uuid) == null) {
                return;
            }
            Long expireTime = map.get(uuid).getTime();
            if (TimekeepingTask.getImmortalTickTime() <= expireTime) {
                if (entity.invulnerableTime < 20) {
                    entity.invulnerableTime = 20;
                }
            } else {
                removeImmortalEffect(entity);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void unAttackAble(LivingIncomingDamageEvent event) {
        LivingEntity hurtOne = event.getEntity();
        if (hurtOne.level().isClientSide()) {
            return;
        }
        if (entityHasEffect.get(hurtOne.getUUID()) != null) {
            Long expireTime = entityHasEffect.get(hurtOne.getUUID()).getTime();
            if (TimekeepingTask.getImmortalTickTime() <= expireTime) {
                if (event.getSource().getEntity() != null) {
                    spawnParticle(hurtOne, 0);
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void unDamageAble(LivingDamageEvent.Pre event) {
        LivingEntity hurtOne = event.getEntity();
        if (hurtOne.level().isClientSide()) {
            return;
        }
        if (entityHasEffect.get(hurtOne.getUUID()) != null) {
            Long expireTime = entityHasEffect.get(hurtOne.getUUID()).getTime();
            if (TimekeepingTask.getImmortalTickTime() <= expireTime) {
                event.setNewDamage(0.0F);
                spawnParticle(hurtOne, 1);
            }
        }
    }

    @SubscribeEvent
    public static void unEffectAble(MobEffectEvent.Applicable event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide()) {
            return;
        }
        if (entityHasEffect.get(entity.getUUID()) != null) {
            Long expireTime = entityHasEffect.get(entity.getUUID()).getTime();
            if (TimekeepingTask.getImmortalTickTime() <= expireTime) {
                if (!event.getEffectInstance().getEffect().value().isBeneficial()) {
                    event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
                }
            }
        }
    }

    private static void spawnParticle(@Nonnull Entity entity, int type) {
        Level level = entity.level();
        BlockPos pPos = entity.blockPosition().above(1 + (int) entity.getEyeHeight());
        if (level instanceof ServerLevel serverLevel && entity.tickCount % 3 == 0) {
            Vec3 center = new Vec3(pPos.getX() + 0.5, pPos.getY() + 0.5, pPos.getZ() + 0.5);
            double radius = entity.getBbWidth();
            for (int i = 0; i < (1 + entity.getBbWidth() * 3); i++) {
                double angle = 2 * Math.PI * Math.random();
                double r = radius * Math.sqrt(Math.random());
                double x = center.x + r * Math.cos(angle);
                double z = center.z + r * Math.sin(angle);
                double y = center.y;
                if (type == 0) {
                    serverLevel.sendParticles(
                            ParticleTypes.HAPPY_VILLAGER, x, y, z, 1, 0, 0, 0, 0.025
                    );
                }
                if (type == 1) {
                    serverLevel.sendParticles(
                            ParticleTypes.TOTEM_OF_UNDYING, x, y, z, 1, 0, 0, 0, 0.025
                    );
                }
            }
        }
    }
}
