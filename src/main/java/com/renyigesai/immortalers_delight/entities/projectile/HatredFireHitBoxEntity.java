package com.renyigesai.immortalers_delight.entities.projectile;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightEntities;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightParticleTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class HatredFireHitBoxEntity extends BaseHitboxEntity{

    private static final int ATTACK_DURATION = 24; // 等待结束后的最大存活时间（游戏刻）
    public static final int LIFE_OFFSET = 2;
    public static final int ATTACK_TRIGGER_TICKS = 5; // 等待状态结束后，到造成伤害前的前摇时间（游戏刻）
    public HatredFireHitBoxEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public HatredFireHitBoxEntity(Level pLevel, double pX, double pY, double pZ, float pYRot, int pWarmupDelay, LivingEntity pOwner) {
        this(ImmortalersDelightEntities.HATREDFIRE_HITBOX.get(), pLevel);
        this.warmupDelayTicks = pWarmupDelay;
        this.lifeTicks = ATTACK_DURATION;
        this.setOwner(pOwner);
        this.setYRot(pYRot * (180F / (float)Math.PI));
        this.setPos(pX, pY, pZ);
    }

    public HatredFireHitBoxEntity(Level pLevel, double pX, double pY, double pZ, float pYRot, int pWarmupDelay, float damage, LivingEntity pOwner) {
        this(ImmortalersDelightEntities.HATREDFIRE_HITBOX.get(), pLevel);
        this.warmupDelayTicks = pWarmupDelay;
        this.setOwner(pOwner);
        this.damage = damage;
        this.setYRot(pYRot * (180F / (float)Math.PI));
        this.setPos(pX, pY, pZ);
    }

    @Override
    protected void doOnSpikeEvent() {
        float r = this.getRadius();
        //一次性释放前摇粒子
        for (int j = 0; j < 32; ++j) {
            ParticleOptions type = (j % 4 == 0) ? ParticleTypes.EXPLOSION_EMITTER : ImmortalersDelightParticleTypes.PURPLE_GLIMMER.get();
            double d0 = (this.random.nextDouble() * 2.0D - 1.0D) * (j >= 20 ? 0.3D * r : r);
            double d1 = 0.3D + this.random.nextDouble() * (j >= 20 ? 0.3D * r : r);
            double d2 = (this.random.nextDouble() * 2.0D - 1.0D) * (j >= 20 ? 0.3D * r : r);
            this.level().addParticle(type, this.xo, this.getRandomY() + this.random.nextDouble() * 2.0D, this.zo, d0, d1, d2);
        }
        super.doOnSpikeEvent();
    }

    @Override
    protected void clientTick() {
        //根据动画进度释放粒子
        if (this.animationTicks > 0) {
            updateParticles(this.getRadius());
        }

    }

    //客户端逻辑，这个方法每tick执行
    protected void updateParticles(float f) {

        int i = Mth.ceil((float)Math.PI * f * f);// 粒子数量
        float f1 = f; // 粒子生成范围半径

        // 生成粒子
        for(int j = 0; j < i; ++j) {
            ParticleOptions particleoptions = ImmortalersDelightParticleTypes.HATRED_FLAME.get();
            if (level().getRandom().nextBoolean()) particleoptions = ParticleTypes.PORTAL;
            if (level().getRandom().nextInt(4) > 0) particleoptions = ParticleTypes.WITCH;
            // 随机计算粒子在圆上的位置（极坐标转直角坐标）
            float f2 = this.random.nextFloat() * ((float)Math.PI * 2F); // 随机角度
            float f3 = Mth.sqrt(this.random.nextFloat()) * f1; // 随机距离（确保在圆内均匀分布）
            double d0 = this.getX() + (double)(Mth.cos(f2) * f3); // X坐标
            double d2 = this.getY() + (double)(this.random.nextFloat() * f1) ; // Y坐标
            double d4 = this.getZ() + (double)(Mth.sin(f2) * f3); // Z坐标

            // 粒子运动速度
            double d5, d6, d7;
            // 随机轻微速度
            d5 = (0.5D - this.random.nextDouble()) * 0.15D;
            d6 = (double)0.01F;
            d7 = (0.5D - this.random.nextDouble()) * 0.15D;

            // 向世界添加粒子（始终可见）
            this.level().addAlwaysVisibleParticle(particleoptions, d0, d2, d4, d5, d6, d7);
        }
    }
    @Override
    protected void attackTick() {
        int attackTime = this.tickCount - (this.warmupDelayTicks + ATTACK_TRIGGER_TICKS);
        if (attackTime >= 0 && attackTime % 5 == 0) {
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
