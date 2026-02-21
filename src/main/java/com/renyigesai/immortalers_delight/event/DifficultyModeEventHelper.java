package com.renyigesai.immortalers_delight.event;

import com.renyigesai.immortalers_delight.Config;
import com.renyigesai.immortalers_delight.api.mobbase.ImmortalersMob;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightTags;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber
public class DifficultyModeEventHelper {

    private static final Map<UUID, Float> entityDeathless = new ConcurrentHashMap<>();
    // 配置文件的列表可以输入更多元素，但我们仅取其中最大的三个
    public static List<Float> getTopThreeByStream(List<Float> floatList) {
        if (floatList == null || floatList.isEmpty()) {
            List<Float> emptyList = new ArrayList<>();
            emptyList.add(0.0F);
            return emptyList;
        }

        return floatList.stream()
                .sorted(Collections.reverseOrder()) // 降序排序
                .limit(3) // 限制前3个
                .collect(Collectors.toList());
    }

    public static float getFromList(List<Float> pList, int index, float defaultValue) {
        List<Float> list = getTopThreeByStream(pList);
        if (list == null || list.isEmpty()) {
            return defaultValue;
        }
        if (index >= list.size()) {
            return list.get(list.size() - 1) > 0.0F ? list.get(list.size() - 1) : defaultValue;
        }
        return list.get(index) > 0.0F ? list.get(index) : defaultValue;
    }
    /**
     *  超凡模式下，怪物的伤害随目标血量提升，作用类似百分比伤害。
     *  使用梯度计算方法：例：普通怪物默认的最大增伤为1.5倍(在250点生命值时达到上限)，精英怪物最大增伤为3倍，
     *  则对精英怪，会先按照普通怪物的增伤系数增大到1.5倍，若目标的生命值大于250，溢出的再应用精英怪的增伤系数计算
     *  这个增伤不影响保底伤害。
     */
    @SubscribeEvent
    public static void ImmortalrsMobAttackProgressDamage(LivingHurtEvent event) {
        LivingEntity hurtOne = event.getEntity();
        if (!hurtOne.level().isClientSide) {
            if (DifficultyModeUtil.isPowerBattleMode() && Config.powerBattleModeStrengthenTheEnemies && Config.useDynamicDamage) {
                if (event.getSource().getEntity() instanceof LivingEntity attacker) {

                    int id = -1;
                    if (attacker.getType().is(ImmortalersDelightTags.IMMORTAL_NORMAL_MOBS)) id = 2;
                    if (attacker.getType().is(ImmortalersDelightTags.IMMORTAL_ELITE_MOBS)) id = 1;
                    if (attacker.getType().is(ImmortalersDelightTags.IMMORTAL_MID_BOSS)) id = 0;

                    if (id >= 0) {
                        float oldDamage = event.getAmount();
                        float buffer = Math.min(getFromList(Config.maximumAttackDamageMultiplier, id, 1), 1 + hurtOne.getMaxHealth() * getFromList(Config.attackDamageMultiplierPerHealth, id, 0));
                        event.setAmount(Math.max(oldDamage * buffer, 0.0F));
                    }
                }
            }
        }
    }

    /**
     *  保底伤害，用以制裁抗性V
     *  修改了实现原理，现在的原理为最终加算伤害值
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void ImmortalrsMobAttackMinDamage(LivingDamageEvent event) {

        if (DifficultyModeUtil.isPowerBattleMode() && Config.powerBattleModeStrengthenTheEnemies && Config.useMinDamage) {
            if (event.getSource().getEntity() instanceof LivingEntity attacker && !attacker.level().isClientSide) {
                LivingEntity hurtOne = event.getEntity();

                float minDamage = 0;
                boolean needMinDamage = false;
                int id = -1;
                if (attacker.getType().is(ImmortalersDelightTags.IMMORTAL_NORMAL_MOBS)) id = 2;
                if (id == 2) {
                    if (attacker instanceof ImmortalersMob immMob) {
                        minDamage += immMob.getMinDamage();
                    } else minDamage += getFromList(Config.minDamage, id, 0);
                    needMinDamage = true;
                }

                if (attacker.getType().is(ImmortalersDelightTags.IMMORTAL_ELITE_MOBS)) id = 1;
                if (id == 1) {
                    if (attacker instanceof ImmortalersMob immMob) {
                        minDamage += immMob.getMinDamage();
                    } else minDamage += getFromList(Config.minDamage, id, 0);
                    needMinDamage = true;
                }

                if (attacker.getType().is(ImmortalersDelightTags.IMMORTAL_MID_BOSS)) id = 0;
                if (id == 0) {
                    if (attacker instanceof ImmortalersMob immMob) {
                        minDamage += immMob.getMinDamage();
                    } else minDamage += getFromList(Config.minDamage, id, 0);
                    needMinDamage = true;
                }

                if (hurtOne.hasEffect(ImmortalersDelightMobEffect.VULNERABLE.get())) {
                    int amplifier = Objects.requireNonNull(hurtOne.getEffect(ImmortalersDelightMobEffect.VULNERABLE.get())).getAmplifier();
                    minDamage *= (amplifier + 2);
                }

                if (needMinDamage && minDamage > 0) {
                    event.setAmount(event.getAmount() + minDamage);
                }
            }
        }
    }

    /**
     *  伤害衰减：伤害大于x时，将衰减为[原伤害值/(1+(原伤害值*0.01x))]。
     *  根据怪物品级的不同，分母的上限分别为7,11,15。
     *  该事件触发时生物自定义hurt方法、Forge伤害事件、原版护甲法抗已经完成计算
     */
    @SubscribeEvent
    public static void ImmortalrsMobHurtDamageDecay(LivingDamageEvent event) {
        if (DifficultyModeUtil.isPowerBattleMode() && Config.powerBattleModeStrengthenTheEnemies && Config.useHighDamageCounteraction) {
            LivingEntity hurtOne = event.getEntity();
            if (!hurtOne.level().isClientSide) {
                float oldDamage = event.getAmount();
                float damage = oldDamage;
                float damageDivisor = 0;
                int id = -1;

                boolean needLimitDamage = false;
                if (hurtOne.getType().is(ImmortalersDelightTags.IMMORTAL_NORMAL_MOBS)) id = 2;
                if (id == 2) {
                    damageDivisor = getFromList(Config.damageCounteractionPerDamage,id,0);
                    if (hurtOne instanceof ImmortalersMob immMob) {
                        damageDivisor = immMob.getDamageDivisor();
                    }
                    if (oldDamage > 100*damageDivisor) {
                        float buffer = Math.min(getFromList(Config.maximumDamageCounteraction,id,1),1 + damageDivisor * (oldDamage - 100 * damageDivisor));
                        damage = oldDamage / buffer;
                        needLimitDamage = true;
                    }
                }

                if (hurtOne.getType().is(ImmortalersDelightTags.IMMORTAL_ELITE_MOBS)) id = 1;
                if (id == 1) {
                    damageDivisor = getFromList(Config.damageCounteractionPerDamage,id,0);
                    if (hurtOne instanceof ImmortalersMob immMob) {
                        damageDivisor = immMob.getDamageDivisor();
                    }
                    if (oldDamage > 100*damageDivisor) {
                        float buffer = Math.min(getFromList(Config.maximumDamageCounteraction,id,1),1 + damageDivisor * (oldDamage - 100 * damageDivisor));
                        damage = oldDamage / buffer;
                        needLimitDamage = true;
                    }
                }

                if (hurtOne.getType().is(ImmortalersDelightTags.IMMORTAL_MID_BOSS)) id = 0;
                if (id == 0) {
                    damageDivisor = getFromList(Config.damageCounteractionPerDamage,id,0);
                    if (hurtOne instanceof ImmortalersMob immMob) {
                        damageDivisor = immMob.getDamageDivisor();
                    }
                    if (oldDamage > 100*damageDivisor) {
                        float buffer = Math.min(getFromList(Config.maximumDamageCounteraction,id,1),1 + damageDivisor * (oldDamage - 100 * damageDivisor));
                        damage = oldDamage / buffer;
                        needLimitDamage = true;
                    }
                }
                if (needLimitDamage) event.setAmount(damage);
            }
        }
    }

    //这里用来实现实体的最小击杀次数，这个Map用于记录特殊无敌帧的实体
//    private static final Map<UUID, Float> entityWithHealth = new ConcurrentHashMap<>();
//
//    public static Map<UUID, Float> getEntityHealth() {return entityWithHealth;}
//
//    public static final String LAST_HEALTH = ImmortalersDelightMod.MODID + "_Kast_Meif";
//
//    public static void addToMap(LivingEntity entity) {
//        /* 判断合理的实体目标 */
//        if (entity == null || entity.isRemoved() || entity.level().isClientSide()) {return;}
//        /* 获取实体UUID以唯一标记对应实体 */
//        UUID uuid = entity.getUUID();
//        entityWithHealth.put(uuid,entity.getHealth());
//        HashMap<UUID, Float> mapClone = new HashMap<>(entityWithHealth);
//        DeathlessEffectPacket packet = new DeathlessEffectPacket(mapClone);
//        ImmortalersNetwork.sendMSGToAll(packet);
//    }
//
//    private static void removeFromMap (LivingEntity entity) {
//        if (entity == null || entity.isRemoved() || entity.level().isClientSide()) {return;}
//        UUID uuid = entity.getUUID();
//        if (entityWithHealth.get(uuid) != null) {entityWithHealth.remove(uuid);}
//        HashMap<UUID, Float> mapClone = new HashMap<>(entityWithHealth);
//        DeathlessEffectPacket packet = new DeathlessEffectPacket(mapClone);
//        ImmortalersNetwork.sendMSGToAll(packet);
//    }
//
//
//    public static void CheckAndClearMap(Level level) {
//        Map<UUID, EffectData> needClearMap = new HashMap<>();
//        for (UUID uuid : needClearMap.keySet()) {
//            if (level instanceof ServerLevel serverLevel && serverLevel.getEntity(uuid) instanceof LivingEntity living) {
//                HashMap<UUID, Float> mapClone = new HashMap<>(entityWithHealth);
//                DeathlessEffectPacket packet = new DeathlessEffectPacket(mapClone);
//                ImmortalersNetwork.sendMSGToAll(packet);
//            }
//            if (level.isClientSide()) {
//                entityWithHealth.remove(uuid);
//            }
//        }
//    }
//
//    public static boolean reSpawnEntity(LivingEntity entity) {
//        if (entity == null) return false;
//        UUID uuid = entity.getUUID();
//        CompoundTag tag = entity.getPersistentData();
//        boolean flag = false;
//        if (!tag.contains(LAST_HEALTH, Tag.TAG_FLOAT)) {
//            tag.putFloat(LAST_HEALTH, entity.getHealth());
//            HashMap<UUID, Float> map1 = new HashMap<>(entityWithHealth);
//            if (map1.get(uuid) != null && entity.getHealth() < map1.get(uuid)) {
//                entity.setHealth(map1.get(uuid));
//                flag = true;
//            } else if (entity.getHealth() < 1) {
//                entity.setHealth(1);
//                flag = true;
//            }
//        } else if (tag.getFloat(LAST_HEALTH) > entity.getHealth()) {
//            entity.setHealth(tag.getFloat(LAST_HEALTH));
//            flag = true;
//        } else if (entity.getHealth() < 1) {
//            entity.setHealth(1);
//            flag = true;
//        }
//        if (flag) {
//            spawnParticle(entity, 1);
//            entity.hurt(new DamageSource(entity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("immortalers_delight:debug")))), 0);
//        }
//        entity.deathTime = -2;
//        return flag;
//    }
//
//    /**
//     * 在生物的tick事件处理防改血逻辑
//     * @param event
//     */
//    @SubscribeEvent
//    public static void onTick(LivingEvent.LivingTickEvent event) {
//        /* 判断当前实体是否合法 */
//        LivingEntity entity = event.getEntity();
//        if (entity == null || entity.isRemoved() || !entity.isAlive()) {return;}
//
//        /*防改血代码*/
//        if (entity.level().isClientSide()) {
//            UUID uuid = entity.getUUID();
//            HashMap<UUID, Float> map1 = new HashMap<>(bufferMap);
//            if (map1.get(uuid) == null) return;
//            entity.deathTime = -2;
//            if (reSpawnEntity(entity)) {
//                entity.invulnerableTime = 20;
//                entity.hurtDuration = 10;
//                entity.hurtTime = 10;
//            }
//        } else {
//            /* 获取当前实体的效果结束时刻 */
//            UUID uuid = entity.getUUID();
//            HashMap<UUID, EffectData> map = new HashMap<>(entityHasEffect);
//            if (map.get(uuid) == null) {return;}
//            Long expireTime = map.get(uuid).getTime();
//            /* 具体效果的实现逻辑 */
//            if (TimekeepingTask.getImmortalTickTime() <= expireTime) {
//                reSpawnEntity(entity);
//                if (entity.invulnerableTime < 20) entity.invulnerableTime = 20;
//            } else removeImmortalEffect(entity);
//        }
//    }
//
//
//    @SubscribeEvent
//    public static void unDying(LivingDeathEvent event) {
//        if (!DifficultyModeUtil.isPowerBattleMode()) return;
//        LivingEntity deadEntity = event.getEntity();
//        if (deadEntity == null) {return;}
//        if (deadEntity.level().isClientSide()) {return;}
//        UUID uuid = deadEntity.getUUID();
//        if (entityHasEffect.get(uuid) != null) {
//            Long expireTime = entityHasEffect.get(uuid).getTime();
//            /* 具体效果的实现逻辑 */
//            if (TimekeepingTask.getImmortalTickTime() <= expireTime) {
//                reSpawnEntity(deadEntity);
//                event.setCanceled(true);
//            }
//        }
//    }
//
//    /**
//     * 订阅生物掉落事件，再次实现复活逻辑
//     * @param event 生物掉落事件（包含掉落列表、死亡生物、伤害来源等信息）
//     */
//    @SubscribeEvent(priority = EventPriority.HIGHEST)
//    public static void onLivingDrops(LivingDropsEvent event) {
//        if (!DifficultyModeUtil.isPowerBattleMode()) return;
//        LivingEntity deadEntity = event.getEntity();
//        if (deadEntity == null) {return;}
//        if (deadEntity.level().isClientSide()) {return;}
//        UUID uuid = deadEntity.getUUID();
//        if (entityHasEffect.get(uuid) != null) {
//            Long expireTime = entityHasEffect.get(uuid).getTime();
//            /* 具体效果的实现逻辑 */
//            if (TimekeepingTask.getImmortalTickTime() <= expireTime) {
//                reSpawnEntity(deadEntity);
//                event.getDrops().clear();
//                event.setCanceled(true);
//            }
//        }
//    }














}
