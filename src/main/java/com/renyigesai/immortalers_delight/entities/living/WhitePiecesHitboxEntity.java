 package com.renyigesai.immortalers_delight.entities.living;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightEntities;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.Map;

 public class WhitePiecesHitboxEntity extends PiecesHitboxEntity{
    public WhitePiecesHitboxEntity(EntityType<? extends WhitePiecesHitboxEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.attackDuration = 132; // 等待结束后的最大存活时间（游戏刻）
        this.attackTriggerTicks = 12; // 等待状态结束后，到造成伤害前的前摇时间（游戏刻）
        this.findRange = 1.5f;//默认索敌半径，实为自身半径的乘数
        this.attackRange = 2f;//默认伤害半径，实为自身半径的乘数
    }

    public WhitePiecesHitboxEntity(Level pLevel, double pX, double pY, double pZ, float pYRot, boolean isWaiting) {
        this(ImmortalersDelightEntities.WHITE_PIECES.get(), pLevel);
        this.setYRot(pYRot * (180F / (float)Math.PI));
        this.setPos(pX, pY, pZ);
        this.setWaiting(isWaiting);
        if (!isWaiting) {
            this.warmupDelayTicks = this.attackTriggerTicks + 1;
        }
    }

    public WhitePiecesHitboxEntity(Level pLevel, double pX, double pY, double pZ, float pYRot, LivingEntity pOwner) {
        this(ImmortalersDelightEntities.WHITE_PIECES.get(), pLevel);
        this.setOwner(pOwner);
        this.setYRot(pYRot * (180F / (float)Math.PI));
        this.setPos(pX, pY, pZ);
    }

    @Override
    protected void doOnPersist() {
        // 检查是否超过生命周期（等待时间 + 持续时间），超过则移除实体
        if (this.tickCount >= this.warmupDelayTicks + attackDuration) {
            this.discard();
            return;
        }
        //实际执行范围攻击
        if (this.tickCount >= this.warmupDelayTicks + attackTriggerTicks
                && (this.tickCount - this.warmupDelayTicks - attackTriggerTicks) % 5 == 0
        ) {
            findTarget();
            if ((this.tickCount - this.warmupDelayTicks - attackTriggerTicks) % 20 == 0) {
                doRangeAttack();
            }
        }
    }

    public void findTarget() {
        AABB aabb = this.getBoundingBox().inflate(this.getRadius() * this.attackRange);
        AABB selection = new AABB(aabb.minX, this.getY() - 0.6D, aabb.minZ, aabb.maxX, this.getY() + 3.5D, aabb.maxZ);
        for(LivingEntity livingentity : this.level().getEntitiesOfClass(LivingEntity.class, selection)) {
            LivingEntity caster = this.getOwner(); // 获取效果所有者
            // 只对未记录或已超过重应用延迟的生物施加效果
            if (livingentity.isAlive() && !this.victims.containsKey(livingentity) && livingentity != caster) {
                if (caster == null || (!caster.isAlliedTo(livingentity) && !livingentity.isAlliedTo(caster))) {

                    // 计算生物与效果云中心的水平距离平方（优化：避免开方）
                    double d8 = livingentity.getX() - this.getX();
                    double d1 = livingentity.getZ() - this.getZ();
                    double d3 = d8 * d8 + d1 * d1;

                    // 距离小于等于半径平方（在范围内）
                    double range = getRadius() * this.attackRange;
                    if (d3 <= (double)(range * range)) {
                        this.victims.put(livingentity, this.tickCount + 15);

                    }
                }
            }
        }
    }
    @Override
     public void doRangeAttack() {
        for (Map.Entry<Entity,Integer> entry : this.victims.entrySet()) {
            if (entry.getKey() instanceof LivingEntity livingentity) {
                LivingEntity caster = this.getOwner(); // 获取效果所有者

                if (livingentity.isAlive() && livingentity.level().dimension().equals(this.level().dimension()) && livingentity != caster) {
                    if (caster == null || (!caster.isAlliedTo(livingentity) && !livingentity.isAlliedTo(caster))) {
                        //对生物造成伤害或施加其他效果
                        this.dealDamageTo(livingentity, caster);
                    }
                }
            }
        }

         //System.out.println("看看伤害:" + getDamage());
         //System.out.println("现在在造成伤害，当前lifeTicks：" + animationTicks + ",这里是客户端吗？" + this.level().isClientSide);

     }

     @Override
     public float getAnimationProgress(float pPartialTicks) {
         if (this.isWaiting()) {return 0.0F;}
         if (this.animationTicks > 30) return -1;
         return (float) this.animationTicks + pPartialTicks;
     }
}
