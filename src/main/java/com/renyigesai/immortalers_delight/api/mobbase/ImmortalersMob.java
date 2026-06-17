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

    float getAttackProportion();

    float getMinDamage();

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
