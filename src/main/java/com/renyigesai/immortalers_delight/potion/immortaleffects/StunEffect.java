package com.renyigesai.immortalers_delight.potion.immortaleffects;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightParticleTypes;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.warden.AngerLevel;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.Mod;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.util.datautil.EffectData;
import com.renyigesai.immortalers_delight.util.datautil.datasaveloadhelper.ExitTimeSaveLoadHelper;
import com.renyigesai.immortalers_delight.util.task.ScheduledExecuteTask;
import com.renyigesai.immortalers_delight.util.task.TimekeepingTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vectorwing.farmersdelight.common.utility.TextUtils;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber
public class StunEffect {
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
                Component.translatable("message." +ImmortalersDelightMod.MODID+ ".effect.stun", new Object[0]),
                true);
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
        if (!(entityHasEffect.get(uuid) == null)) {entityHasEffect.remove(uuid);}
        onImmortalEffectRemove(entity);
    }
    /**
     * 用于去除指定实体的特殊效果时进行善后的方法，比如涉及到状态修饰符之类标记在实体中的数据，需要处理
     * @param entity
     */
    private static void onImmortalEffectRemove (LivingEntity entity) {
        //UUID uuid = entity.getUUID();
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

    /**
     * 在生物的tick事件处理眩晕效果的逻辑
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
        if (entityHasEffect.get(uuid) == null) {return;}
        Long expireTime = entityHasEffect.get(uuid).getTime();
        /* 具体效果的实现逻辑 */
        if (TimekeepingTask.getImmortalTickTime() <= expireTime) {
            int time = (int) ((expireTime - TimekeepingTask.getImmortalTickTime()) / 50);
            if (entity instanceof Player player) {
                player.getCooldowns().addCooldown(player.getItemInHand(InteractionHand.MAIN_HAND).getItem(), time);
                player.getCooldowns().addCooldown(player.getItemInHand(InteractionHand.OFF_HAND).getItem(), time);
            } else if (entity instanceof Mob mob) {
                mob.setTarget(null);
                mob.getBrain().setMemory(MemoryModuleType.ANGRY_AT, Optional.empty());
                mob.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, Optional.empty());
            }
            spawnParticle(entity, 1);
            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, time, 9, true, false));
            entity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, time, 9, true, false));
        } else removeImmortalEffect(entity);
    }

    @SubscribeEvent
    public static void undoStunningAttack(LivingDamageEvent event) {
        LivingEntity hurtOne = event.getEntity();
        if (hurtOne.level().isClientSide()) return;
        if (event.getSource().getEntity() instanceof LivingEntity attacker) {
            if (entityHasEffect.get(attacker.getUUID()) != null) {
                event.setAmount(0.0F);
                event.setCanceled(true);
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
                            ParticleTypes.ANGRY_VILLAGER, x, y, z, 1, 0, 0, 0, 0.025
                    );
                }if (type == 1) {
                    serverLevel.sendParticles(
                            ImmortalersDelightParticleTypes.STUN.get(), x, y, z, 1, 0, 0, 0, 0.025
                    );
                }
            }
        }
    }
}
