package com.renyigesai.immortalers_delight.potion.immortaleffects;

import com.renyigesai.immortalers_delight.Config;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightParticleTypes;
import com.renyigesai.immortalers_delight.message.DeathlessEffectPacket;
import com.renyigesai.immortalers_delight.network.ImmortalersNetwork;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import com.renyigesai.immortalers_delight.util.datautil.EffectData;
import com.renyigesai.immortalers_delight.util.task.TimekeepingTask;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber
public class DeathlessEffect {
    /**
     * 这个类能对实体进行标记（以及解除标记），
     * 用Map存储受到当前效果的实体与对应的持续时间（表示为结束时刻）
     * 需要注意的是这个类的信息不存盘，跟摔落高度一样退出游戏刷新
     * 所以仅适用于短时间(几秒)的一些状态（例如这是眩晕，如果你希望眩晕一分钟，那可阴的没边了）
     **/
    private static final Map<UUID, EffectData> entityHasEffect = new ConcurrentHashMap<>();
    private static final Map<UUID, Float> entityWithHealth = new ConcurrentHashMap<>();

    public static Map<UUID, EffectData> getEntityMap() {return entityHasEffect;}
    public static Map<UUID, Float> getEntityHealth() {return entityWithHealth;}

//==========================================状态处理部分，处理map实体的添加与去除===============================================//

    /**
     * 对指定的生物实体应用特殊效果，输入实体与持续时间（tick）
     * @param entity
     * @param duration
     * @param amplifier
     */
    public static void applyImmortalEffect(LivingEntity entity, int duration, int amplifier) {
        /* 判断合理的实体目标 */
        if (entity == null || entity.isRemoved() || entity.level().isClientSide()) {return;}
        /* 获取实体UUID以唯一标记对应实体 */
        UUID uuid = entity.getUUID();
        /* 计算效果的结束时刻，使用自定义的计时器以绕开WorldTime相关操作 */
        long expireTime = TimekeepingTask.getImmortalTickTime() + duration * 50L;
        /* 将实体与Buff相关数据保存到Map */
        EffectData effectData = new EffectData(entity.blockPosition(),expireTime,amplifier,entity.getRandom().nextInt());
        entityHasEffect.put(uuid,effectData);
        entityWithHealth.put(uuid,entity.getHealth());
        HashMap<UUID, Float> mapClone = new HashMap<>(entityWithHealth);
        DeathlessEffectPacket packet = new DeathlessEffectPacket(mapClone);
        ImmortalersNetwork.sendMSGToAll(packet);
        System.out.println("[ImmortalersDelight] " + entity.getName().getString() + " has been marked as immortal.");
    }

    /**
     * 用于主动去除指定实体的特殊效果的方法
     * 因为要保证实体状态的善后，这里要求实体必须是活的
     * 超时解除与这个不同的是，超时解除无论有没找到实体都清表
     * @param entity
     */
    public static void removeImmortalEffect(LivingEntity entity) {
        if (entity == null || entity.isRemoved() || entity.level().isClientSide()) {return;}
        UUID uuid = entity.getUUID();
        if (!(entityHasEffect.get(uuid) == null || entityWithHealth.get(uuid) == null)) {
            entityHasEffect.remove(uuid);
            entityWithHealth.remove(uuid);

        }
        onImmortalEffectRemove(entity);
    }
    /**
     * 用于去除指定实体的特殊效果时进行善后的方法，比如涉及到状态修饰符之类标记在实体中的数据，需要处理
     * @param entity
     */
    private static void onImmortalEffectRemove (LivingEntity entity) {
        HashMap<UUID, Float> mapClone = new HashMap<>(entityWithHealth);
        DeathlessEffectPacket packet = new DeathlessEffectPacket(mapClone);
        ImmortalersNetwork.sendMSGToAll(packet);
    }

    /**
     * 定期检查map，并清除过期的实体
     * @param evt
     */
    @SubscribeEvent
    public static void onTick(@Nonnull TickEvent.LevelTickEvent evt) {
        if (evt.phase.equals(TickEvent.Phase.START)) {
            HashMap<UUID, EffectData> map = new HashMap<>(entityHasEffect);
            if (map.size() > 0 && TimekeepingTask.getImmortalTickTime() % 1000 * (map.size() + 1) <= 50) {
                CheckAndClearMap(evt.level);
            }
        }
    }

    private static boolean changed = false;
    private static Map<UUID, Float> bufferMap = new HashMap<>();
    public static void getMapFromNetwork(Map<UUID, Float> map) {
        bufferMap.putAll(map);
        changed = true;
    }

    public static void CheckAndClearMap(Level level) {
        System.out.println("[ImmortalersDelight] Checking map...");
        Map<UUID, EffectData> needClearMap = new HashMap<>();
        for (UUID uuid : entityHasEffect.keySet()) {
            EffectData effectData = entityHasEffect.get(uuid);
            if (effectData == null) {continue;}
            if (TimekeepingTask.getImmortalTickTime() > effectData.getTime()) {
                needClearMap.put(uuid, effectData);
            }
        }
        for (UUID uuid : needClearMap.keySet()) {
            if (level instanceof ServerLevel serverLevel && serverLevel.getEntity(uuid) instanceof LivingEntity living) {
                removeImmortalEffect(living);
                entityHasEffect.remove(uuid);
                entityWithHealth.remove(uuid);
                HashMap<UUID, Float> mapClone = new HashMap<>(entityWithHealth);
                DeathlessEffectPacket packet = new DeathlessEffectPacket(mapClone);
                ImmortalersNetwork.sendMSGToAll(packet);
            }
            if (level.isClientSide()) {
                entityHasEffect.remove(uuid);
                entityWithHealth.remove(uuid);
            }
        }
    }

//====================================具体逻辑部分，通过外部调用或事件执行具体的效果=========================================//
public static final String LAST_HEALTH = ImmortalersDelightMod.MODID + "_last_health";


    /**
     * 在生物的tick事件处理眩晕效果的逻辑
     * @param event
     */
    @SubscribeEvent
    public static void onTick(LivingEvent.LivingTickEvent event) {
        /* 判断当前实体是否合法 */
        LivingEntity entity = event.getEntity();
        if (entity == null || entity.isRemoved() || !entity.isAlive()) {return;}

        /*防改血代码*/
        if (!DifficultyModeUtil.isPowerBattleMode()) return;
        if (entity.level().isClientSide()) {
            UUID uuid = entity.getUUID();
            HashMap<UUID, Float> map1 = new HashMap<>(bufferMap);
            if (map1.get(uuid) == null) return;
            entity.deathTime = -2;
            if (reSpawnEntity(entity)) {
                entity.invulnerableTime = 20;
                entity.hurtDuration = 10;
                entity.hurtTime = 10;
            }
        } else {
            /* 获取当前实体的效果结束时刻 */
            UUID uuid = entity.getUUID();
            HashMap<UUID, EffectData> map = new HashMap<>(entityHasEffect);
            if (map.get(uuid) == null) {return;}
            Long expireTime = map.get(uuid).getTime();
            /* 具体效果的实现逻辑 */
            if (TimekeepingTask.getImmortalTickTime() <= expireTime) {
                reSpawnEntity(entity);
                if (entity.invulnerableTime < 20) entity.invulnerableTime = 20;
            } else removeImmortalEffect(entity);
        }
    }

    public static boolean reSpawnEntity(LivingEntity entity) {
        if (entity == null) return false;
        UUID uuid = entity.getUUID();
        CompoundTag tag = entity.getPersistentData();
        boolean flag = false;
        if (!tag.contains(LAST_HEALTH, Tag.TAG_FLOAT)) {
            tag.putFloat(LAST_HEALTH, entity.getHealth());
            HashMap<UUID, Float> map1 = new HashMap<>(entityWithHealth);
            if (map1.get(uuid) != null && entity.getHealth() < map1.get(uuid)) {
                entity.setHealth(map1.get(uuid));
                flag = true;
            } else if (entity.getHealth() < 1) {
                entity.setHealth(1);
                flag = true;
            }
        } else if (tag.getFloat(LAST_HEALTH) > entity.getHealth()) {
            entity.setHealth(tag.getFloat(LAST_HEALTH));
            flag = true;
        } else if (entity.getHealth() < 1) {
            entity.setHealth(1);
            flag = true;
        }
        if (flag) {
            spawnParticle(entity, 1);
            entity.hurt(new DamageSource(entity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("immortalers_delight:debug")))), 0);
        }
        entity.deathTime = -2;
        System.out.println("reSpawnEntity-is client?" + entity.level().isClientSide + ", entity's deathTime:" + entity.deathTime);
        return flag;
    }

//    protected static boolean controlEntityIsDead(LivingEntity entity,boolean isDead,boolean isGet) {
//        try {
//            Class<?> entityClass = entity.getClass();
//            Field deadField = entityClass.getDeclaredField("dead");
//            deadField.setAccessible(true);
//            if (isGet) return (boolean) deadField.get(entity);
//            else {
//                deadField.set(entity, isDead);
//                return true;
//            }
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
//    }

    @SubscribeEvent
    public static void unDying(LivingDeathEvent event) {
        if (!DifficultyModeUtil.isPowerBattleMode()) return;
        LivingEntity deadEntity = event.getEntity();
        if (deadEntity == null) {return;}
        if (deadEntity.level().isClientSide()) {return;}
        UUID uuid = deadEntity.getUUID();
        if (entityHasEffect.get(uuid) != null) {
            Long expireTime = entityHasEffect.get(uuid).getTime();
            /* 具体效果的实现逻辑 */
            if (TimekeepingTask.getImmortalTickTime() <= expireTime) {
                reSpawnEntity(deadEntity);
                event.setCanceled(true);
            }
        }
    }

    /**
     * 订阅生物掉落事件，再次实现复活逻辑
     * @param event 生物掉落事件（包含掉落列表、死亡生物、伤害来源等信息）
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingDrops(LivingDropsEvent event) {
        if (!DifficultyModeUtil.isPowerBattleMode()) return;
        LivingEntity deadEntity = event.getEntity();
        if (deadEntity == null) {return;}
        if (deadEntity.level().isClientSide()) {return;}
        UUID uuid = deadEntity.getUUID();
        if (entityHasEffect.get(uuid) != null) {
            Long expireTime = entityHasEffect.get(uuid).getTime();
            /* 具体效果的实现逻辑 */
            if (TimekeepingTask.getImmortalTickTime() <= expireTime) {
                reSpawnEntity(deadEntity);
                event.getDrops().clear();
                event.setCanceled(true);
            }
        }
    }
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void unAttackAble(LivingAttackEvent event) {
        LivingEntity hurtOne = event.getEntity();
        if (hurtOne.level().isClientSide()) return;
        if (entityHasEffect.get(hurtOne.getUUID()) != null) {
            Long expireTime = entityHasEffect.get(hurtOne.getUUID()).getTime();
            /* 具体效果的实现逻辑 */
            if (TimekeepingTask.getImmortalTickTime() <= expireTime) {
                if (event.getSource().getEntity() != null) {
                    spawnParticle(hurtOne, 0);
                    boolean isPowered = DifficultyModeUtil.isPowerBattleMode();
//                    int time = (isPowered ? 24 : 12) + entityHasEffect.get(hurtOne.getUUID()).getAmplifier() * 6;
//                    hurtOne.invulnerableTime = time;
//                    hurtOne.hurtDuration = time;
//                    hurtOne.hurtTime = time;
                    event.setCanceled(true);
                }
            }
        }
    }
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void unDamageAble(LivingDamageEvent event) {
        LivingEntity hurtOne = event.getEntity();
        if (hurtOne.level().isClientSide()) return;
        if (entityHasEffect.get(hurtOne.getUUID()) != null) {
            Long expireTime = entityHasEffect.get(hurtOne.getUUID()).getTime();
            /* 具体效果的实现逻辑 */
            if (TimekeepingTask.getImmortalTickTime() <= expireTime) {
                event.setAmount(0.0F);
                spawnParticle(hurtOne, 0);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void unEffectAble(MobEffectEvent.Applicable event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide()) return;
        if (entityHasEffect.get(entity.getUUID()) != null) {
            Long expireTime = entityHasEffect.get(entity.getUUID()).getTime();
            /* 具体效果的实现逻辑 */
            if (TimekeepingTask.getImmortalTickTime() <= expireTime) {
                if (!event.getEffectInstance().getEffect().isBeneficial()) {
                    event.setResult(Event.Result.DENY);
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
            for (int i = 0; i < (1 + entity.getBbWidth()*3); i++) {
                double angle = 2 * Math.PI * Math.random();
                double r = radius * Math.sqrt(Math.random());
                double x = center.x + r * Math.cos(angle);
                double z = center.z + r * Math.sin(angle);
                double y = center.y;
                if (type == 0) {
                    serverLevel.sendParticles(
                            ParticleTypes.HAPPY_VILLAGER, x, y, z, 1, 0, 0, 0, 0.025
                    );
                }if (type == 1) {
                    serverLevel.sendParticles(
                            ParticleTypes.TOTEM_OF_UNDYING, x, y, z, 1, 0, 0, 0, 0.025
                    );
                }
            }
        }
    }
}
