package com.renyigesai.immortalers_delight.api.mobbase;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public interface ImmortalersMob {

    //获取生物的伤害百分比，用于超凡模式保底伤害
    float getAttackProportion();

    //获取生物的最小伤害，用于超凡模式保底伤害
    float getMinDamage();

    //获取生物的伤害除数，用于超凡模式的高伤害衰减
    int getDamageDivisor();

    boolean shouldBlastDamageHurt(LivingEntity target);

    default void performBlastAttack(LivingEntity attacker, Vec3 pos, float range,
                                    DamageSource source, float damage,
                                    boolean isExplosion, boolean bypassCooldown, boolean isCircular) {
        if (!attacker.level().isClientSide) {
            ServerLevel serverLevel = (ServerLevel) attacker.level();

            if (isExplosion) {
                serverLevel.sendParticles(ParticleTypes.EXPLOSION,
                        pos.x, pos.y, pos.z,
                        10,
                        0.5, 0.5, 0.5,
                        0.1
                );
            }

            doBlastDamage(getTargetsOfBlastDamage(attacker, pos, range, isCircular), pos, range, source, damage, true, bypassCooldown);
        }
    }

    /**
     * 获取指定范围内所有 LivingEntity 实体，用于范围伤害。
     * 具有两种效果，圆形和正方形。
     * @param attacker
     * @param pos
     * @param range
     * @param isCircular
     * @return
     */
    default List<LivingEntity> getTargetsOfBlastDamage(LivingEntity attacker, Vec3 pos, float range, boolean isCircular) {
        if (!attacker.level().isClientSide) {
            ServerLevel serverLevel = (ServerLevel) attacker.level();

            AABB boundingBox = new AABB(
                    pos.x - range, pos.y - range, pos.z - range,
                    pos.x + range, pos.y + range, pos.z + range
            );

            List<LivingEntity> entitiesInRange = serverLevel.getEntitiesOfClass(
                    LivingEntity.class,
                    boundingBox,
                    LivingEntity::isAlive
            );

            if (isCircular) {
                entitiesInRange.removeIf(entity -> entity.distanceToSqr(pos) > range * range);
            }
            return entitiesInRange;
        }
        return new ArrayList<>();
    }

    /**
     * 实际处理爆炸伤害与击退。默认范围越大的范围伤害击退越强。
     * 具有两种模式，类似爆炸的伤害随范围衰减，另一种为全额伤害。
     * @param targets
     * @param pos
     * @param range
     * @param source
     * @param damage
     * @param isExplosion
     * @param bypassCooldown
     */
    default void doBlastDamage(List<LivingEntity> targets, Vec3 pos, float range, DamageSource source, float damage, boolean isExplosion, boolean bypassCooldown) {
        for (LivingEntity target : targets) {
            if (shouldBlastDamageHurt(target)) {
                if (bypassCooldown) {target.invulnerableTime = 0;}
                if (isExplosion) {
                    target.hurt(source, (float) (damage * damage / target.distanceToSqr(pos)));
                } else target.hurt(source, damage);

                double knockbackX = (target.getX() - pos.x) * 0.1;
                double knockbackY = (target.getY() - pos.y > 0 ? range - (target.getY() - pos.y) : (range * -1) - (target.getY() - pos.y)) * 0.1;
                double knockbackZ = (target.getZ() - pos.z) * 0.1;
                target.setDeltaMovement(knockbackX, knockbackY, knockbackZ);
            }

        }
    }
}
