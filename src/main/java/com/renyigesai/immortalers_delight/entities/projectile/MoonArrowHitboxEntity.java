package com.renyigesai.immortalers_delight.entities.projectile;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightEntities;
import com.renyigesai.immortalers_delight.potion.immortaleffects.KuuvahkiEffect;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.UUID;

public class MoonArrowHitboxEntity extends BaseHitboxEntity implements TraceableEntity {

    private static final int ATTACK_DURATION = 25; // 等待结束后的最大存活时间（游戏刻）
    public static final int LIFE_OFFSET = 2;
    public static final int ATTACK_TRIGGER_TICKS = 10; // 等待状态结束后，到造成伤害前的前摇时间（游戏刻）

    public MoonArrowHitboxEntity(EntityType<? extends MoonArrowHitboxEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public MoonArrowHitboxEntity(Level pLevel, double pX, double pY, double pZ, float pYRot, int pWarmupDelay, LivingEntity pOwner) {
        this(ImmortalersDelightEntities.MOON_ARROW_HITBOX.get(), pLevel);
        this.warmupDelayTicks = pWarmupDelay;
        this.lifeTicks = ATTACK_DURATION;
        this.setOwner(pOwner);
        this.setYRot(pYRot * (180F / (float)Math.PI));
        this.setPos(pX, pY, pZ);
    }

    public MoonArrowHitboxEntity(Level pLevel, double pX, double pY, double pZ, float pYRot, int pWarmupDelay, float damage, LivingEntity pOwner) {
        this(ImmortalersDelightEntities.MOON_ARROW_HITBOX.get(), pLevel);
        this.warmupDelayTicks = pWarmupDelay;
        this.setOwner(pOwner);
        this.damage = damage;
        this.setYRot(pYRot * (180F / (float)Math.PI));
        this.setPos(pX, pY, pZ);
    }

    @Override
    protected void doOnSpikeEvent() {
        //一次性释放前摇粒子
        for (int j = 0; j < 32; ++j) {
            ParticleOptions type = (j % 4 == 0) ? ParticleTypes.WITCH : ParticleTypes.PORTAL;
            double d0 = j >= 20 ? (this.random.nextDouble() * 2.0D - 1.0D) * 0.3D : 0;
            double d1 = j >= 20 ? 0.3D + this.random.nextDouble() * 0.3D : 0;
            double d2 = j >= 20 ? (this.random.nextDouble() * 2.0D - 1.0D) * 0.3D : 0;
            this.level().addParticle(type, this.xo, this.getRandomY() + this.random.nextDouble() * 2.0D, this.zo, d0, d1, d2);
        }
        super.doOnSpikeEvent();
    }

    @Override
    protected void clientTick() {
        //根据动画进度释放粒子
        if (this.animationTicks == ATTACK_TRIGGER_TICKS) {
            for(int i = 0; i < 32; ++i) {
                ParticleOptions type = (i >= 12) ? ParticleTypes.END_ROD : ParticleTypes.CRIT;
                double d0 = this.getX() + (this.random.nextDouble() * 2.0D - 1.0D) * (double)this.getBbWidth() * 0.5D;
                double d1 = this.getY() + 0.05D + this.random.nextDouble();
                double d2 = this.getZ() + (this.random.nextDouble() * 2.0D - 1.0D) * (double)this.getBbWidth() * 0.5D;
                double d3 = i >= 12 ? 0D : (this.random.nextDouble() * 2.0D - 1.0D) * 0.3D;
                double d4 = i >= 12 ? 0D : 0.3D + this.random.nextDouble() * 0.3D;
                double d5 = i >= 12 ? 0D : (this.random.nextDouble() * 2.0D - 1.0D) * 0.3D;
                this.level().addParticle(type, d0, d1 + 1.0D, d2, d3, d4, d5);
            }
        }
    }

    @Override
    protected void attackTick() {
        if (this.tickCount == this.warmupDelayTicks + ATTACK_TRIGGER_TICKS) {
            AABB aabb = this.getBoundingBox().inflate(0.3D);
            AABB selection = new AABB(aabb.minX, this.getY() - 0.6D, aabb.minZ, aabb.maxX, this.getY() + 3.5D, aabb.maxZ);
            for(LivingEntity livingentity : this.level().getEntitiesOfClass(LivingEntity.class, selection)) {
                LivingEntity caster = this.getOwner(); // 获取效果所有者
                // 只对未记录或已超过重应用延迟的生物施加效果
                if (livingentity.isAlive() && livingentity != caster) {
                    if (caster == null || (!caster.isAlliedTo(livingentity) && !livingentity.isAlliedTo(caster))) {

                        // 计算生物与效果云中心的水平距离平方（优化：避免开方）
                        double d8 = livingentity.getX() - this.getX();
                        double d1 = livingentity.getZ() - this.getZ();
                        double d3 = d8 * d8 + d1 * d1;

                        // 距离小于等于半径平方（在范围内）
                        double range = getRadius();
                        if (d3 <= (double)(range * range)) {
                            //对生物造成伤害或施加其他效果
                            this.dealDamageTo(livingentity, caster);
                        }
                    }
                }
            }
            //System.out.println("看看伤害:" + getDamage());
            //System.out.println("现在在造成伤害，当前lifeTicks：" + animationTicks + ",这里是客户端吗？" + this.level().isClientSide);
        }
    }

}
