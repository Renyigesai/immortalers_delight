package com.renyigesai.immortalers_delight.event;
import net.neoforged.fml.common.EventBusSubscriber;

import com.renyigesai.immortalers_delight.Config;
import com.renyigesai.immortalers_delight.api.mobbase.ImmortalersMob;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightEntities;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightTags;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import com.renyigesai.immortalers_delight.util.EffectUtils;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@EventBusSubscriber
public class DifficultyModeEventHelper {

    private static final Map<UUID, Float> entityDeathless = new ConcurrentHashMap<>();
    public static Map<String,Float[]> mobDynamicDamage = new HashMap<String,Float[]>();

    public static Map<String,Float[]> mobMinDamage = new HashMap<String,Float[]>();

    public static Map<String,Float[]> mobHighDamageCounteraction = new HashMap<String,Float[]>();
    //初始化
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onWorldLoad(LevelEvent.Load event) {
        if (mobDynamicDamage.isEmpty()) {
            mobDynamicDamage = getMobStrengthenConfig(Config.MOB_DYNAMIC_DAMAGE.get());
        }
        if (mobMinDamage.isEmpty()) {
            mobMinDamage = getMobStrengthenConfig(Config.MOB_MIN_DAMAGE.get());
        }
        if (mobHighDamageCounteraction.isEmpty()) {
            mobHighDamageCounteraction = getMobStrengthenConfig(Config.MOB_HIGH_DAMAGE_COUNTERACTION.get());
        }
    }

    public static Map<String, Float[]> getMobStrengthenConfig(List<? extends List<?>> source)
    {
        Map<String, Float[]> map = new HashMap<>();

        for (List<?> entry : source)
        {
            String inputEntityName = null;
            float NumberA = 0;
            float NumberB = 1;

            if (!entry.isEmpty() && entry.get(0) instanceof String) {
                inputEntityName = (String) entry.get(0);
            }
            if (entry.size() > 1 && entry.get(1) instanceof Number) {
                NumberA = ((Number) entry.get(1)).floatValue();
            }
            if (entry.size() > 2 && entry.get(2) instanceof Number) {
                NumberB = ((Number) entry.get(2)).floatValue();
            }

            if (inputEntityName != null) {
                float isTag = 0;
                // 判断是否为Tag
                if (inputEntityName.startsWith("#")) {
                    isTag = 1;
                    // 去除 # 前缀，获取正确可用的名称
                    inputEntityName = inputEntityName.substring(1);
                }
                Float[] effectAdd = new Float[]{isTag, NumberA, NumberB};
                map.put(inputEntityName, effectAdd);
            }
        }
        return map;
    }

    /**
     * 从 Map<String, Float[]> 中提取 数组第一个元素 Float[0] > 0 的所有键值对
     * 用以区分出EntityTag 和 EntityName
     */
    public static Map<String, Float[]> filterByFirstElementGreaterThanZero(Map<String, Float[]> originalMap) {
        if (originalMap == null) {
            return new HashMap<>();
        }

        return originalMap.entrySet().stream()
                .filter(entry -> {
                    Float[] array = entry.getValue();
                    // 安全判断：数组不为空 && 第一个元素存在 && > 0
                    return array != null && array.length > 0 && array[0] != null && array[0] > 0.0F;
                })
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
    }
    // 配置文件的列表可以输入更多元素，但我们仅取其中最大的三个
//    public static List<Float> getTopThreeByStream(List<Float> floatList) {
//        if (floatList == null || floatList.isEmpty()) {
//            List<Float> emptyList = new ArrayList<>();
//            emptyList.add(0.0F);
//            return emptyList;
//        }
//
//        return floatList.stream()
//                .sorted(Collections.reverseOrder()) // 降序排序
//                .limit(3) // 限制前3个
//                .collect(Collectors.toList());
//    }
//
//    public static float getFromList(List<Float> pList, int index, float defaultValue) {
//        List<Float> list = getTopThreeByStream(pList);
//        if (list == null || list.isEmpty()) {
//            return defaultValue;
//        }
//        if (index >= list.size()) {
//            return list.get(list.size() - 1) > 0.0F ? list.get(list.size() - 1) : defaultValue;
//        }
//        return list.get(index) > 0.0F ? list.get(index) : defaultValue;
//    }
    /**
     *  超凡模式下，怪物的伤害随目标血量提升，作用类似百分比伤害。
     *  使用梯度计算方法：例：普通怪物默认的最大增伤为1.5倍(在250点生命值时达到上限)，精英怪物最大增伤为3倍，
     *  则对精英怪，会先按照普通怪物的增伤系数增大到1.5倍，若目标的生命值大于250，溢出的再应用精英怪的增伤系数计算
     *  这个增伤不影响保底伤害。
     */
    @SubscribeEvent
    public static void ImmortalrsMobAttackProgressDamage(LivingDamageEvent.Pre event) {
        LivingEntity hurtOne = event.getEntity();
        if (!hurtOne.level().isClientSide) {
            if (DifficultyModeUtil.isPowerBattleMode() && Config.powerBattleModeStrengthenTheEnemies) {
                if (event.getSource().getEntity() instanceof LivingEntity attacker) {

                    float oldDamage = event.getNewDamage();
                    float buffer = 1;
                    float healthHaveUsed = 0;

                    ResourceLocation entityId = BuiltInRegistries.ENTITY_TYPE.getKey(attacker.getType());
                    if (entityId != null) {
                        String idString = entityId.toString();
                        //优先判断是否能实名匹配
                        if (mobDynamicDamage.containsKey(idString)) {
                            float a = 0;
                            float b = 1;
                            Float[] configNumbers = mobDynamicDamage.get(idString);
                            if (configNumbers.length > 1) a = configNumbers[1];
                            if (configNumbers.length > 2) b = configNumbers[2];
                            buffer *= Math.min(1 + hurtOne.getMaxHealth() * a,b);
                        } else {
                            //如果不能，则尝试匹配Tag
                            //这是获取配置文件List中所有的Tag
                            Map<String, Float[]> tagMap = filterByFirstElementGreaterThanZero(mobDynamicDamage);
                            for (Map.Entry<String, Float[]> entry : tagMap.entrySet()) {
                                ResourceLocation resourcelocation = ResourceLocation.parse(entry.getKey());
                                TagKey<EntityType<?>> tagkey = TagKey.create(Registries.ENTITY_TYPE, resourcelocation);
                                //从String中获取TagKey对实体进行匹配
                                if (attacker.getType().is(tagkey)) {
                                    float a = 0;
                                    float b = 1;
                                    Float[] configNumbers = entry.getValue();
                                    if (configNumbers.length > 1) a = configNumbers[1];
                                    if (configNumbers.length > 2) b = configNumbers[2];
                                    buffer *= Math.min(1 + hurtOne.getMaxHealth() * a,b);
                                }
                            }
                        }
                    }

//                    boolean isNormal = attacker.getType().is(ImmortalersDelightTags.IMMORTAL_NORMAL_MOBS);
//                    boolean isElite = attacker.getType().is(ImmortalersDelightTags.IMMORTAL_ELITE_MOBS);
//                    boolean isMidBoss = attacker.getType().is(ImmortalersDelightTags.IMMORTAL_MID_BOSS);
//                    int id = -1;
//
//                    //小怪的伤害倍率；因为精英怪和小boss也用到，所以用或条件
//                    if (isNormal || isElite || isMidBoss) id = 2;
//                    if (id >= 0 && healthHaveUsed < hurtOne.getMaxHealth()) {
//                        //从配置文件获取最大增伤倍率和每点生命的增伤倍率
//                        float maxBuffer = getFromList(Config.maximumAttackDamageMultiplier, id, 1);
//                        float BPH = getFromList(Config.attackDamageMultiplierPerHealth, id, 0);
//                        //排除默认值
//                        if (maxBuffer != 1 && BPH != 0) {
//                            //计算增伤
//                            float bufferEx = Math.min(maxBuffer, (hurtOne.getMaxHealth() - healthHaveUsed) * BPH);
//                            buffer += bufferEx;
//                            //减去已使用的生命值，实现梯度计算
//                            if (bufferEx == maxBuffer) healthHaveUsed += maxBuffer / BPH;
//                            else healthHaveUsed += hurtOne.getMaxHealth();
//                        }
//                    }
//                    if (isElite || isMidBoss) id = 1;
//                    if (id == 1 && healthHaveUsed < hurtOne.getMaxHealth()) {
//                        float maxBuffer = getFromList(Config.maximumAttackDamageMultiplier, id, 1);
//                        float BPH = getFromList(Config.attackDamageMultiplierPerHealth, id, 0);
//
//                        if (maxBuffer != 1 && BPH != 0) {
//                            float bufferEx = Math.min(maxBuffer, (hurtOne.getMaxHealth() - healthHaveUsed) * BPH);
//                            buffer += bufferEx;
//
//                            if (bufferEx == maxBuffer) healthHaveUsed += maxBuffer / BPH;
//                            else healthHaveUsed += hurtOne.getMaxHealth();
//                        }
//                    }
//                    if (isMidBoss) id = 0;
//                    if (id == 0 && healthHaveUsed < hurtOne.getMaxHealth()) {
//                        float maxBuffer = getFromList(Config.maximumAttackDamageMultiplier, id, 1);
//                        float BPH = getFromList(Config.attackDamageMultiplierPerHealth, id, 0);
//
//                        if (maxBuffer != 1 && BPH != 0) {
//                            float bufferEx = Math.min(maxBuffer, (hurtOne.getMaxHealth() - healthHaveUsed) * BPH);
//                            buffer += bufferEx;
//
//                            if (bufferEx == maxBuffer) healthHaveUsed += maxBuffer / BPH;
//                            else healthHaveUsed += hurtOne.getMaxHealth();
//                        }
//                    }
                    event.setNewDamage(Math.max(oldDamage * buffer, 0.0F));
                }
            }
        }
    }

    /**
     *  保底伤害，用以制裁抗性V
     *  修改了实现原理，现在的原理为最终加算伤害值
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void ImmortalrsMobAttackMinDamage(LivingDamageEvent.Pre event) {

        if (DifficultyModeUtil.isPowerBattleMode() && Config.powerBattleModeStrengthenTheEnemies) {
            if (event.getSource().getEntity() instanceof LivingEntity attacker && !attacker.level().isClientSide) {
                LivingEntity hurtOne = event.getEntity();

                float minDamage = 0;

                ResourceLocation entityId = BuiltInRegistries.ENTITY_TYPE.getKey(attacker.getType());
                if (entityId != null) {
                    String idString = entityId.toString();
                    //优先判断是否能实名匹配
                    if (mobMinDamage.containsKey(idString)) {
                        float a = 0;
                        float b = 1;
                        Float[] configNumbers = mobMinDamage.get(idString);
                        if (configNumbers.length > 1) a = configNumbers[1];
                        if (configNumbers.length > 2) b = configNumbers[2];
                        minDamage += a * hurtOne.getMaxHealth() + b;
                    } else {
                        //如果不能，则尝试匹配Tag
                        //这是获取配置文件List中所有的Tag
                        Map<String, Float[]> tagMap = filterByFirstElementGreaterThanZero(mobMinDamage);
                        for (Map.Entry<String, Float[]> entry : tagMap.entrySet()) {
                            ResourceLocation resourcelocation = ResourceLocation.parse(entry.getKey());
                            TagKey<EntityType<?>> tagkey = TagKey.create(Registries.ENTITY_TYPE, resourcelocation);
                            //从String中获取TagKey对实体进行匹配
                            if (attacker.getType().is(tagkey)) {
                                float a = 0;
                                float b = 1;
                                Float[] configNumbers = entry.getValue();
                                if (configNumbers.length > 1) a = configNumbers[1];
                                if (configNumbers.length > 2) b = configNumbers[2];
                                minDamage += a * hurtOne.getMaxHealth() + b;
                            }
                        }
                    }
                }

//                boolean needMinDamage = false;
//                int id = -1;
//                if (attacker.getType().is(ImmortalersDelightTags.IMMORTAL_NORMAL_MOBS)) id = 2;
//                if (id == 2) {
//                    if (attacker instanceof ImmortalersMob immMob) {
//                        minDamage += immMob.getMinDamage();
//                    } else minDamage += getFromList(Config.minDamage, id, 0);
//                    needMinDamage = true;
//                }
//
//                if (attacker.getType().is(ImmortalersDelightTags.IMMORTAL_ELITE_MOBS)) id = 1;
//                if (id == 1) {
//                    if (attacker instanceof ImmortalersMob immMob) {
//                        minDamage += immMob.getMinDamage();
//                    } else minDamage += getFromList(Config.minDamage, id, 0);
//                    needMinDamage = true;
//                }
//
//                if (attacker.getType().is(ImmortalersDelightTags.IMMORTAL_MID_BOSS)) id = 0;
//                if (id == 0) {
//                    if (attacker instanceof ImmortalersMob immMob) {
//                        minDamage += immMob.getMinDamage();
//                    } else minDamage += getFromList(Config.minDamage, id, 0);
//                    needMinDamage = true;
//                }

                if (hurtOne.hasEffect(ImmortalersDelightMobEffect.VULNERABLE)) {
                    int amplifier = Objects.requireNonNull(hurtOne.getEffect(ImmortalersDelightMobEffect.VULNERABLE)).getAmplifier();
                    minDamage *= (amplifier + 2);
                }

                if (minDamage > 0) {
                    event.setNewDamage(event.getNewDamage() + minDamage);
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
    public static void ImmortalrsMobHurtDamageDecay(LivingDamageEvent.Pre event) {
        if (DifficultyModeUtil.isPowerBattleMode() && Config.powerBattleModeStrengthenTheEnemies) {
            LivingEntity hurtOne = event.getEntity();
            if (!hurtOne.level().isClientSide) {
                float oldDamage = event.getNewDamage();
                float damage = oldDamage;
                float damageDivisor = 0;


                ResourceLocation entityId = BuiltInRegistries.ENTITY_TYPE.getKey(hurtOne.getType());
                if (entityId != null) {
                    String idString = entityId.toString();
                    //优先判断是否能实名匹配
                    if (mobHighDamageCounteraction.containsKey(idString)) {
                        float a = 0;
                        float b = 1;
                        Float[] configNumbers = mobHighDamageCounteraction.get(idString);
                        if (configNumbers.length > 1) a = configNumbers[1];
                        if (configNumbers.length > 2) b = configNumbers[2];
                        damageDivisor = Math.min(1 + oldDamage * a,b);
                        damage *= 1f / damageDivisor;
                    } else {
                        //如果不能，则尝试匹配Tag
                        //这是获取配置文件List中所有的Tag
                        Map<String, Float[]> tagMap = filterByFirstElementGreaterThanZero(mobHighDamageCounteraction);
                        for (Map.Entry<String, Float[]> entry : tagMap.entrySet()) {
                            ResourceLocation resourcelocation = ResourceLocation.parse(entry.getKey());
                            TagKey<EntityType<?>> tagkey = TagKey.create(Registries.ENTITY_TYPE, resourcelocation);
                            //从String中获取TagKey对实体进行匹配
                            if (hurtOne.getType().is(tagkey)) {
                                float a = 0;
                                float b = 1;
                                Float[] configNumbers = entry.getValue();
                                if (configNumbers.length > 1) a = configNumbers[1];
                                if (configNumbers.length > 2) b = configNumbers[2];
                                damageDivisor = Math.min(1 + oldDamage * a,b);
                                damage *= 1f / damageDivisor;
                            }
                        }
                    }
                }

//                int id = -1;
//
//                boolean needLimitDamage = false;
//                if (hurtOne.getType().is(ImmortalersDelightTags.IMMORTAL_NORMAL_MOBS)) id = 2;
//                if (id == 2) {
//                    damageDivisor = getFromList(Config.damageCounteractionPerDamage,id,0);
//                    if (hurtOne instanceof ImmortalersMob immMob) {
//                        damageDivisor = immMob.getDamageDivisor();
//                    }
//                    if (oldDamage > 100*damageDivisor) {
//                        float buffer = Math.min(getFromList(Config.maximumDamageCounteraction,id,1),1 + damageDivisor * (oldDamage - 100 * damageDivisor));
//                        damage *= 1 / buffer;
//                        needLimitDamage = true;
//                    }
//                }
//
//                if (hurtOne.getType().is(ImmortalersDelightTags.IMMORTAL_ELITE_MOBS)) id = 1;
//                if (id == 1) {
//                    damageDivisor = getFromList(Config.damageCounteractionPerDamage,id,0);
//                    if (hurtOne instanceof ImmortalersMob immMob) {
//                        damageDivisor = immMob.getDamageDivisor();
//                    }
//                    if (oldDamage > 100*damageDivisor) {
//                        float buffer = Math.min(getFromList(Config.maximumDamageCounteraction,id,1),1 + damageDivisor * (oldDamage - 100 * damageDivisor));
//                        damage *= 1 / buffer;
//                        needLimitDamage = true;
//                    }
//                }
//
//                if (hurtOne.getType().is(ImmortalersDelightTags.IMMORTAL_MID_BOSS)) id = 0;
//                if (id == 0) {
//                    damageDivisor = getFromList(Config.damageCounteractionPerDamage,id,0);
//                    if (hurtOne instanceof ImmortalersMob immMob) {
//                        damageDivisor = immMob.getDamageDivisor();
//                    }
//                    if (oldDamage > 100*damageDivisor) {
//                        float buffer = Math.min(getFromList(Config.maximumDamageCounteraction,id,1),1 + damageDivisor * (oldDamage - 100 * damageDivisor));
//                        damage *= 1 / buffer;
//                        needLimitDamage = true;
//                    }
//                }
                event.setNewDamage(damage);
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
//            entity.hurt(new DamageSource(entity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath("immortalers_delight:debug")))), 0);
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
