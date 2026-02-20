package com.renyigesai.immortalers_delight.potion.immortaleffects;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.entities.projectile.MoonArrowHitboxEntity;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightParticleTypes;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import com.renyigesai.immortalers_delight.util.datautil.EffectData;
import com.renyigesai.immortalers_delight.util.task.TimekeepingTask;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber
public class KuuvahkiEffect {
    /**
     * 这个类能对实体进行标记（以及解除标记），
     * 用Map存储受到当前效果的实体与对应的持续时间（表示为结束时刻）
     * 需要注意的是这个类的信息不存盘，跟摔落高度一样退出游戏刷新
     * 所以仅适用于短时间(几秒)的一些状态（例如这是眩晕，如果你希望眩晕一分钟，那可阴的没边了）
     **/
    private static final Map<UUID, EffectData> entityHasEffect = new ConcurrentHashMap<>();

    public static Map<UUID, EffectData> getEntityMap() {return entityHasEffect;}

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
        if (entity instanceof Player player) player.displayClientMessage(
                Component.translatable("message." +ImmortalersDelightMod.MODID+ ".effect.kuuvahki", new Object[0]),
                true);

        //为实体累积伤害标签，用于移除时进行范围轰击
        int lv = amplifier + 1;
        long damage = 2L << lv;
        if (duration < 40 || DifficultyModeUtil.isPowerBattleMode()) {
            CompoundTag tag = entity.getPersistentData();
            if (!tag.contains(DEATH_BOMBARDMENT_COUNT, Tag.TAG_LONG)) {
                tag.putLong(DEATH_BOMBARDMENT_COUNT, damage * duration / 2 + 160L * lv);
            } else {
                long lastDamage = tag.getLong(DEATH_BOMBARDMENT_COUNT);
                tag.putLong(DEATH_BOMBARDMENT_COUNT, lastDamage + damage * duration / 2);
            }
        }
    }

    public static void addImmortalEffectWithSource(LivingEntity entity, int duration, int amplifier, @Nullable LivingEntity source) {
        /* 判断合理的实体目标 */
        if (entity == null || entity.isRemoved() || entity.level().isClientSide()) {return;}
        applyImmortalEffect(entity, duration, amplifier);
        if (source != null) {
            CompoundTag nbt = entity.getPersistentData();
            nbt.putUUID(MOON_ARROW_TAG, source.getUUID());
        }
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
        int lv = -1;
        if (!(entityHasEffect.get(uuid) == null)) {
            lv = entityHasEffect.get(uuid).getAmplifier() + 1;
            entityHasEffect.remove(uuid);
        }
        onImmortalEffectRemove(entity,lv);
    }

    public static void safeRemoveEffect(LivingEntity entity) {
        CompoundTag nbt = entity.getPersistentData();
        if (nbt.contains(DEATH_BOMBARDMENT_COUNT)) {nbt.remove(DEATH_BOMBARDMENT_COUNT);}
        if (nbt.contains(MOON_ARROW_TAG)) {nbt.remove(MOON_ARROW_TAG);}
        removeImmortalEffect(entity);
    }
    /**
     * 用于去除指定实体的特殊效果时进行善后的方法，比如涉及到状态修饰符之类标记在实体中的数据，需要处理
     * 此处用于移除时进行范围轰击
     */
    private static void onImmortalEffectRemove (LivingEntity hurtOne, int lv) {
        if (lv < 0) return;
        //UUID uuid = entity.getUUID();
        spawnBomb(hurtOne, lv);
    }

    /**
     * 定期检查map，并清除过期的实体
     * @param evt
     */
    @SubscribeEvent
    public static void onTick(@Nonnull TickEvent.LevelTickEvent evt) {
        if (evt.phase.equals(TickEvent.Phase.START)) {
            if (entityHasEffect.size() > 0 && TimekeepingTask.getImmortalTickTime() % 1000 * (entityHasEffect.size() + 1) <= 50) {
                CheckAndClearMap(evt.level);
            }
        }
    }

    public static void CheckAndClearMap(Level level) {
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
            }
            if (level.isClientSide()) {entityHasEffect.remove(uuid);}
        }
    }

//====================================具体逻辑部分，通过外部调用或事件执行具体的效果=========================================//
    public static final String DEATH_BOMBARDMENT_COUNT = ImmortalersDelightMod.MODID + "moonlight_bombardment_count";
    public static final String MOON_ARROW_TAG = ImmortalersDelightMod.MODID + "hit_by_moon_arrow";

    public static DamageSource getDamageSource(Entity hurtOne, @Nullable Entity attacker) {
        if (attacker != null) {
            return new DamageSource(hurtOne.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("immortalers_delight:moon_arrow"))), attacker);
        }
        return new DamageSource(hurtOne.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("immortalers_delight:moon_arrow"))));
    }
    /**
     * 在生物的tick事件处理效果的逻辑
     * @param event
     */
    @SubscribeEvent
    public static void onTick(LivingEvent.LivingTickEvent event) {
        /* 判断当前实体是否合法 */
        LivingEntity entity = event.getEntity();
        if (entity == null || entity.isRemoved() || !entity.isAlive()) {return;}
        if (entity.level().isClientSide()) {return;}
        /* 获取当前实体的效果结束时刻 */
        UUID uuid = entity.getUUID();
        HashMap<UUID, EffectData> map = new HashMap<>(entityHasEffect);
        if (map.get(uuid) == null) {return;}
        Long expireTime = map.get(uuid).getTime();
        /* 具体效果的实现逻辑 */
        if (TimekeepingTask.getImmortalTickTime() <= expireTime) {
            // 每秒对实体造成2点伤害，每级翻倍
            if (entity.tickCount % 40 == 0) {
                int lv = map.get(uuid).getAmplifier() + 1;
                long damage = 1L << lv;
                spawnParticle(entity, 1);

                // 为实体累积伤害标签, 超凡模式的结束爆炸伤害是一开始就叠满的，所以这里只在普通模式处理
                if (!DifficultyModeUtil.isPowerBattleMode()) {
                    CompoundTag tag = entity.getPersistentData();
                    if (!tag.contains(DEATH_BOMBARDMENT_COUNT, Tag.TAG_LONG)) {
                        tag.putLong(DEATH_BOMBARDMENT_COUNT, (damage + 16L * lv) * 10);
                    } else {
                        long lastDamage = tag.getLong(DEATH_BOMBARDMENT_COUNT);
                        tag.putLong(DEATH_BOMBARDMENT_COUNT, lastDamage + damage * 10);
                    }
                }

                //实际造成伤害，若在超凡模式下或将造成致命伤害，伤害类型会变为自定义的法术伤害
                DamageSource source;
                if (DifficultyModeUtil.isPowerBattleMode() || entity.getHealth() <= damage) {
                    source = KuuvahkiEffect.getDamageSource(entity, null);
                } else source = entity.damageSources().freeze();

                entity.hurt(source, damage);
            }
        } else removeImmortalEffect(entity);
    }

    //在生物死亡时，读取nbt中积累的伤害标签，触发轰击造成范围伤害
    @SubscribeEvent
    public static void onLivingDead(LivingDeathEvent event) {
        LivingEntity deadOne = event.getEntity();
        if (deadOne.level().isClientSide()) return;
        Map<UUID, EffectData> map = new HashMap<>(entityHasEffect);
        if (map.get(deadOne.getUUID()) != null) {
            Long expireTime = map.get(deadOne.getUUID()).getTime();
            /* 具体效果的实现逻辑 */
            if (TimekeepingTask.getImmortalTickTime() <= expireTime) {

                spawnBomb(deadOne, map.get(deadOne.getUUID()).getAmplifier());
            }
        }
    }

    private static void spawnBomb(LivingEntity hurtOne, int lv) {
        spawnParticle(hurtOne, 0);
        // 读取nbt中积累的伤害标签
        CompoundTag nbt = hurtOne.getPersistentData();
        if (nbt.contains(DEATH_BOMBARDMENT_COUNT, Tag.TAG_LONG)) {
            float damage = (float) nbt.getLong(DEATH_BOMBARDMENT_COUNT) / 20;
            nbt.remove(DEATH_BOMBARDMENT_COUNT);
            // 读取伤害的来源
            UUID uuid = null;
            if (nbt.hasUUID(MOON_ARROW_TAG)) {
                uuid = nbt.getUUID(MOON_ARROW_TAG);
                nbt.remove(MOON_ARROW_TAG);
            }

            // 创建一个月光箭的范围轰击, 超凡模式下每级会有75%的概率额外进行一次轰击
            RandomSource random = hurtOne.level().getRandom();
            if (damage > 0) {
                float radius = lv * 0.5f + 1.5f;
                int count = 1;
                if (DifficultyModeUtil.isPowerBattleMode()) {
                    for (int j = 0; j < 1 + lv; j++) {
                        if (random.nextInt(4) != 0) count++;
                    }
                }
                for (int i = 0; i < count; i++) {
                    BlockPos pos = hurtOne.blockPosition();

                    float R = 0;
                    for (int j = 1; j < count; j++) {R += radius / j;}
                    if (R <= 0) R = radius;

                    if (i != 0) pos = pos.offset(random.nextInt((int)R * 2) - (int)R, 0, random.nextInt((int)R * 2) - (int)R);

                    if (hurtOne.level().isAreaLoaded(pos, 1)) {
                        createSpellEntity(i == 0 ? radius : radius * 0.666f, damage, uuid, hurtOne.level(),
                                pos.getX(), pos.getZ(),
                                hurtOne.getY() - 8, hurtOne.getY() + 8,
                                hurtOne.getYRot(), i * 6);
                    }

                }

            }
        }
    }

    private static void createSpellEntity(float radius, float damage, UUID uuid, Level level,
                                   double pX, double pZ, double pMinY, double pMaxY,
                                   float pYRot, int pWarmupDelay) {
        if (level.isClientSide) return;

        BlockPos blockpos = BlockPos.containing(pX, pMaxY, pZ);
        boolean flag = false;
        double d0 = 0.0D;

        do {
            BlockPos blockpos1 = blockpos.below();
            BlockState blockstate = level.getBlockState(blockpos1);
            if (blockstate.isFaceSturdy(level, blockpos1, Direction.UP)) {
                if (level.isEmptyBlock(blockpos)) {
                    BlockState blockstate1 = level.getBlockState(blockpos);
                    VoxelShape voxelshape = blockstate1.getCollisionShape(level, blockpos);
                    if (!voxelshape.isEmpty()) {
                        d0 = voxelshape.max(Direction.Axis.Y);
                    }
                }

                flag = true;
                break;
            }

            blockpos = blockpos.below();
        } while(blockpos.getY() >= Mth.floor(pMinY) - 1);

        if (flag) {
            MoonArrowHitboxEntity moonArrowHitboxEntity = new MoonArrowHitboxEntity(level,
                    pX, (double)blockpos.getY() + d0, pZ, pYRot,
                    2 + pWarmupDelay, damage, null);
            if (uuid != null) moonArrowHitboxEntity.setOwner(uuid);
            moonArrowHitboxEntity.setRadius(radius);

            level.addFreshEntity(moonArrowHitboxEntity);
            if (pWarmupDelay > 0) spawnParticle(moonArrowHitboxEntity, 1);
        }

    }
    public static float getDecimalPart(float num) {
        // 步骤1：获取num的整数部分（强制类型转换会直接截断小数）
        int integerPart = (int) num;
        // 步骤2：原数减去整数部分，得到小数部分
        float decimalPart = num - integerPart;
        return decimalPart;
    }

    private static void spawnParticle(@Nonnull Entity entity, int type) {
        Level level = entity.level();
        BlockPos pPos = entity.blockPosition();
        if (level instanceof ServerLevel serverLevel) {
            Vec3 center = new Vec3(pPos.getX() + 0.5, pPos.getY() + 0.05, pPos.getZ() + 0.5);
            double radius = entity.getBbWidth();
            if (type == 1 && radius > 0.6) radius = 0.6;
            for (int i = 0; i < (1 + entity.getBbWidth()*3); i++) {
                double angle = 2 * Math.PI * Math.random();
                double r = radius * Math.sqrt(Math.random());
                double x = center.x + r * Math.cos(angle);
                double z = center.z + r * Math.sin(angle);
                double y = center.y;
                if (type == 0) {
                    serverLevel.sendParticles(
                            ParticleTypes.END_ROD, x, y + entity.getEyeHeight(), z, 1, 0, 0, 0, 0.025
                    );
                }if (type == 1 && i % 3 == 0) {
                    serverLevel.sendParticles(
                            ImmortalersDelightParticleTypes.MOONLIGHT_BEAM.get(), x, y, z, 1, 0, 0, 0, 0.025
                    );
                }
            }
        }
    }
}
