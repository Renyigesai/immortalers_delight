package com.renyigesai.immortalers_delight.entities.projectile;

import com.google.common.collect.Lists;
import com.renyigesai.immortalers_delight.client.particle.ShockWaveParticleOption;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightEntities;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightParticleTypes;
import com.renyigesai.immortalers_delight.potion.GasPoisonMobEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.level.Level;

import java.util.List;

public class GasCloudEntity extends EffectCloudBaseEntity{
    public GasCloudEntity(EntityType<? extends GasCloudEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public GasCloudEntity(Level pLevel, double pX, double pY, double pZ) {
        this(ImmortalersDelightEntities.GAS_EFFECT_CLOUD.get(), pLevel);
        this.setPos(pX, pY, pZ); // 设置初始位置
    }

    @Override
    public void doOnStart(float range) {
        float f = range;
        if (this.level() instanceof ServerLevel serverLevel) {

            ParticleOptions particleoptions = ImmortalersDelightParticleTypes.GAS_SMOKE.get();
            int i; // 粒子数量
            float f1; // 粒子生成范围半径

            // 非等待状态下，粒子数量与圆面积成正比（πr²）
            i = Mth.ceil((float)Math.PI * f * f);
            f1 = f - 0.2f; // 生成范围等于效果云半径

            // 生成粒子
            for(int j = 0; j < i; ++j) {
                // 随机计算粒子在圆上的位置（极坐标转直角坐标）
                float f2 = this.random.nextFloat() * ((float)Math.PI * 2F); // 随机角度
                float f3 = Mth.sqrt(this.random.nextFloat()) * f1; // 随机距离（确保在圆内均匀分布）
                double d0 = this.getX() + (double)(Mth.cos(f2) * f3); // X坐标
                double d2 = this.getY() + 0.25; // Y坐标
                double d4 = this.getZ() + (double)(Mth.sin(f2) * f3); // Z坐标

                // 粒子运动速度（根据粒子类型和状态调整）
                double d5, d6, d7;
                // 非等待状态下其他粒子：随机轻微速度
                d5 = (0.5D - this.random.nextDouble()) * 0.15D;
                d6 = (double)0.01F;
                d7 = (0.5D - this.random.nextDouble()) * 0.15D;
                serverLevel.sendParticles(
                        particleoptions, d0, d2, d4, 1, d5, d6, d7, 0.025
                );
                if (j + 1 == i) spawnShriekParticle(serverLevel,this.getX(), d2, this.getZ(), (int) this.getRadius(),0);
            }
        }
    }

    @Override
    protected void doOnAddEffect(LivingEntity livingentity, List<MobEffectInstance> list) {
        //戴口罩免疫瓦斯
        if (livingentity.getItemBySlot(EquipmentSlot.HEAD).is(ImmortalersDelightItems.GOLDEN_FABRIC_VEIL.get())) {
            livingentity.getItemBySlot(EquipmentSlot.HEAD).hurtAndBreak((this.isDangerous() ? 3 : 1), livingentity, (action) -> {
                action.broadcastBreakEvent(EquipmentSlot.HEAD);
            });
        } else super.doOnAddEffect(livingentity, list);
    }

    @Override
    protected boolean doAdditionalAction(float range) {
        //危险级的额外效果：刷新嗅探兽CD，对免疫瓦斯毒的生物直接造成伤害（相当于灾变凋零烟雾）
        if (!isDangerous()) return false;
        // 获取碰撞箱2倍半径内的所有生物实体
        List<LivingEntity> list1 = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(this.getRadius()));
        if (!list1.isEmpty()) {
            boolean flag = false;
            for(LivingEntity livingentity : list1) {
                LivingEntity caster = this.getOwner(); // 获取效果所有者
                // 只对未记录或已超过重应用延迟的生物施加效果
                if (livingentity.isAlive() && !this.victims.containsKey(livingentity) && livingentity != caster) {
                    if (livingentity instanceof Sniffer || caster == null || (!caster.isAlliedTo(livingentity) && !livingentity.isAlliedTo(caster))) {

                        // 计算生物与效果云中心的水平距离平方（优化：避免开方）
                        double d8 = livingentity.getX() - this.getX();
                        double d1 = livingentity.getZ() - this.getZ();
                        double d3 = d8 * d8 + d1 * d1;

                        // 距离小于等于半径平方（在范围内）
                        if (d3 <= (double)(range * range)) {
                            if (!livingentity.hasEffect(ImmortalersDelightMobEffect.GAS_POISON.get()) && !livingentity.getItemBySlot(EquipmentSlot.HEAD).is(ImmortalersDelightItems.GOLDEN_FABRIC_VEIL.get())) {
                                livingentity.hurt(GasPoisonMobEffect.getDamageSource(livingentity, caster), 2);
                                if (livingentity instanceof Mob mob) mob.getNavigation().moveTo(this, 0.3);
                            }
                            flag = true;//判断是否执行成功
                        } else if (livingentity instanceof Sniffer sniffer && d3 <= (double)( 2 * range * 2 * range)) {
                            // 记录该生物下次可受影响的刻数
                            this.victims.put(livingentity, this.tickCount + this.reapplicationDelay);
                            sniffer.getBrain().setMemoryWithExpiry(MemoryModuleType.SNIFF_COOLDOWN, Unit.INSTANCE, 96L);
                            BlockPos pos = sniffer.getNavigation().getTargetPos();
                            if (pos != null) {sniffer.getNavigation().moveTo(pos.getX(), pos.getY(), pos.getZ(), 3.0);}
                            if (sniffer.level() instanceof ServerLevel serverLevel) spawnShriekParticle(serverLevel,sniffer.getX(), sniffer.getX(), sniffer.getX(), 0,1);
                            flag = true;//判断是否执行成功
                        }
                    }
                }
            }

            return flag;
        }
        return false;
    }

    public static void spawnShriekParticle(ServerLevel serverLevel, double x, double y, double z, int delay, int type) {
        if (type == 0) {
            ShockWaveParticleOption particleOption = new ShockWaveParticleOption(delay);
            serverLevel.sendParticles(
                    particleOption,  // 粒子参数（含SHRIEK类型+delay）
                    x, y, z,         // 生成位置
                    1,               // 生成数量
                    0.0D, 0.0D, 0.0D,// 位置无偏移
                    0.0D             // 速度（无作用）
            );
        }
        if (type == 1) {
            for (int i = 0; i < 25; i++) {
                serverLevel.sendParticles(
                        ParticleTypes.ANGRY_VILLAGER,
                        x - 1 + serverLevel.random.nextFloat() * 2,
                        y - 0.5 + serverLevel.random.nextFloat(),
                        z - 1 + serverLevel.random.nextFloat() * 2,
                        1,               // 生成数量
                        0.0D, 0.0D, 0.0D,// 位置无偏移
                        0.0D             // 速度（无作用）
                );
            }
        }
    }
}
