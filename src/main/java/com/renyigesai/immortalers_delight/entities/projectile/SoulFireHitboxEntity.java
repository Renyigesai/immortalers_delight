package com.renyigesai.immortalers_delight.entities.projectile;

import com.google.common.collect.Maps;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightEntities;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class SoulFireHitboxEntity extends BaseHitboxEntity{

    private static final int ATTACK_DURATION = 30; // 等待结束后的最大存活时间（游戏刻）
    public static final int LIFE_OFFSET = 2;
    public static final int ATTACK_TRIGGER_TICKS = 6; // 等待状态结束后，到造成伤害前的前摇时间（游戏刻）
    public SoulFireHitboxEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    public SoulFireHitboxEntity(Level pLevel, double pX, double pY, double pZ, float pYRot, int pWarmupDelay, LivingEntity pOwner) {
        this(ImmortalersDelightEntities.SOUL_FIRE_HITBOX.get(), pLevel);
        this.warmupDelayTicks = pWarmupDelay;
        this.lifeTicks = ATTACK_DURATION;
        this.setOwner(pOwner);
        this.setYRot(pYRot * (180F / (float)Math.PI));
        this.setPos(pX, pY, pZ);
    }

    public SoulFireHitboxEntity(Level pLevel, double pX, double pY, double pZ, float pYRot, int pWarmupDelay, float damage, LivingEntity pOwner) {
        this(ImmortalersDelightEntities.SOUL_FIRE_HITBOX.get(), pLevel);
        this.warmupDelayTicks = pWarmupDelay;
        this.setOwner(pOwner);
        this.damage = damage;
        this.setYRot(pYRot * (180F / (float)Math.PI));
        this.setPos(pX, pY, pZ);
    }

    @Override
    public EntityDimensions getDimensions(Pose pPose) {
        return EntityDimensions.scalable(this.getRadius() * 2.0F, this.getRadius() * 3.5F);
    }
    @Override
    public void setRadius(float pRadius) {
        super.setRadius(Mth.clamp(pRadius, 0.0F, 4.0F));
    }

    @Override
    protected void clientTick() {
        //根据动画进度释放粒子
        if (this.animationTicks > 0) {
            spawnConeEruption( level(),
                    position(),
                    new Vec3(0, 1, 0),
                    animationTicks,
                    ATTACK_DURATION,    // 持续 1.5 秒
                    getRadius());
        }
    }

    protected final Map<Entity, Integer> victims = Maps.newHashMap(); // 记录受影响实体及下次可再次受影响的刻数
    @Override
    protected void attackTick() {
        int attackTime = this.tickCount - this.warmupDelayTicks + ATTACK_TRIGGER_TICKS;
        if (attackTime >= 0 && attackTime <= 15 && attackTime % 5 == 0) {
            AABB aabb = this.getBoundingBox().inflate(0.3D);
            AABB selection = new AABB(aabb.minX, this.getY() - 0.6D, aabb.minZ, aabb.maxX, this.getY() + 3.5D, aabb.maxZ);
            for(LivingEntity livingentity : this.level().getEntitiesOfClass(LivingEntity.class, selection)) {
                LivingEntity caster = this.getOwner(); // 获取效果所有者
                // 只对未记录或已超过重应用延迟的生物施加效果
                if (livingentity.isAlive() && livingentity != caster) {
                    //只对没有伤害过的实体造成伤害
                    if (victims.containsKey(livingentity)) continue;

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
                            //缓存伤害过的实体避免重复伤害
                            victims.put(livingentity, livingentity.tickCount + 20);
                        }
                    }
                }
            }
            //System.out.println("看看伤害:" + getDamage());
            //System.out.println("现在在造成伤害，当前lifeTicks：" + animationTicks + ",这里是客户端吗？" + this.level().isClientSide);
        }
    }

    /**
     * 锥形喷发特效（火焰 + 末地烛），仅客户端有效。
     *
     * @param level         客户端世界
     * @param origin        喷发起点
     * @param axis          喷发轴方向（如 new Vec3(0,1,0)），内部会归一化
     * @param animationTick 当前动画计时，从 0 开始
     * @param duration      喷发总时长（tick）
     * @param scale         整体缩放，1.0 约为喷 4~6 格高
     */
    public static void spawnConeEruption(Level level, Vec3 origin, Vec3 axis,
                                         int animationTick, int duration, double scale) {
        if (!level.isClientSide || duration <= 0 || animationTick < 0 || animationTick > duration) {
            return;
        }

        float t = animationTick / (float) duration;
        // 包络：前 20% 快速增强，后 33% 逐渐减弱
        float envelope = Math.min(1.0F, t * 5.0F) * Math.min(1.0F, (1.0F - t) * 3.0F);
        envelope *= scale;
        if (envelope <= 0.0F) return;

        RandomSource random = level.random;

        // 火焰层：窄锥、较慢、数量多
        emitLayer(level, random, ParticleTypes.SOUL_FIRE_FLAME, origin, axis,
                stochasticCount(random, 5 * envelope),
                Math.toRadians(7 + 7 * scale * 0.25f), 0.10 * scale, 0.22 * scale, 0.15 * scale);

        // 光点层：宽锥、较快、数量少
        emitLayer(level, random, ParticleTypes.END_ROD, origin, axis,
                stochasticCount(random, 3.5F * envelope),
                Math.toRadians(16 + 16 * scale * 0.25f), 0.25 * scale, 0.55 * scale, 0.10 * scale);
    }

    private static void emitLayer(Level level, RandomSource random, ParticleOptions particle,
                                  Vec3 origin, Vec3 axisIn, int count,
                                  double halfAngle, double minSpeed, double maxSpeed,
                                  double baseRadius) {
        Vec3 axis = axisIn.normalize();

        // 构造与 axis 垂直的两个单位向量 u、v
        Vec3 helper = Math.abs(axis.y) < 0.99 ? new Vec3(0, 1, 0) : new Vec3(1, 0, 0);
        Vec3 u = axis.cross(helper).normalize();
        Vec3 v = axis.cross(u).normalize();

        for (int i = 0; i < count; i++) {
            // sqrt 使方向在锥口截面上分布更均匀
            double theta = halfAngle * Math.sqrt(random.nextDouble());
            double phi = random.nextDouble() * Mth.TWO_PI;
            double sinT = Math.sin(theta);

            Vec3 dir = axis.scale(Math.cos(theta))
                    .add(u.scale(Math.cos(phi) * sinT))
                    .add(v.scale(Math.sin(phi) * sinT));

            double speed = Mth.lerp(random.nextDouble(), minSpeed, maxSpeed);
            Vec3 vel = dir.scale(speed);

            // 起点在底部小圆盘内随机，让火柱根部有一定粗细
            double r = baseRadius * Math.sqrt(random.nextDouble());
            double a = random.nextDouble() * Mth.TWO_PI;
            Vec3 pos = origin
                    .add(u.scale(Math.cos(a) * r))
                    .add(v.scale(Math.sin(a) * r));

            level.addParticle(particle, pos.x, pos.y, pos.z, vel.x, vel.y, vel.z);
        }
    }

    /** 随机取整：3.4 有 40% 概率得到 4，否则得到 3，让小数强度也能平滑过渡 */
    private static int stochasticCount(RandomSource random, float value) {
        int base = (int) value;
        return base + (random.nextFloat() < (value - base) ? 1 : 0);
    }
}
