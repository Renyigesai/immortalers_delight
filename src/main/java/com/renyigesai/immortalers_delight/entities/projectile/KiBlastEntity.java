package com.renyigesai.immortalers_delight.entities.projectile;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightEntities;
import com.renyigesai.immortalers_delight.potion.immortaleffects.StunEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

/**
 * 凋灵头颅实体类，继承自抽象伤害投射物（AbstractHurtingProjectile）
 * 对应凋灵BOSS发射的头颅投射物，具有造成凋零伤害、触发爆炸的特性
 */
public class KiBlastEntity extends AbstractHurtingProjectile {
    // 同步实体数据访问器：标记该凋灵头颅是否为"危险级"（来自无敌状态的凋灵BOSS光环）
    // 使用SynchedEntityData实现服务端与客户端的数据同步，序列化类型为布尔值
    private static final EntityDataAccessor<Boolean> DATA_DANGEROUS = SynchedEntityData.defineId(KiBlastEntity.class, EntityDataSerializers.BOOLEAN);

    /**
     * 构造方法1：通过实体类型和世界实例创建凋灵头颅
     * 主要用于实体注册和自动生成场景
     * @param pEntityType 凋灵头颅的实体类型
     * @param pLevel 所在的游戏世界实例
     */
    public KiBlastEntity(EntityType<? extends KiBlastEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    /**
     * 构造方法2：通过发射者和偏移量创建凋灵头颅
     * 凋灵BOSS发射头颅时的核心构造方法，指定发射者和初始运动偏移
     * @param pLevel 所在的游戏世界实例
     * @param pShooter 发射该头颅的活体实体（通常是凋灵BOSS）
     * @param pOffsetX X轴运动偏移量
     * @param pOffsetY Y轴运动偏移量
     * @param pOffsetZ Z轴运动偏移量
     */
    public KiBlastEntity(Level pLevel, LivingEntity pShooter, double pOffsetX, double pOffsetY, double pOffsetZ) {
        super(ImmortalersDelightEntities.KI_BLAST.get(), pShooter, pOffsetX, pOffsetY, pOffsetZ, pLevel);
    }

    @Override
    protected @NotNull ParticleOptions getTrailParticle() {
        return ParticleTypes.GLOW_SQUID_INK;
    }

    /**
     * 获取该投射物的运动惯性系数（原版重写方法）
     * 该系数会与原始运动速度相乘，控制投射物的运动衰减/增强
     * @return 惯性系数，此处返回0.96F（轻微的运动衰减，保持头颅飞行的稳定性）
     */
    protected float getInertia() {
        return 0.96F;
    }

    /**
     * 判断该实体是否处于着火状态（原版重写方法）
     * 用于客户端渲染着火特效，凋灵头颅本身不会着火，故返回false
     * @return 是否着火，固定返回false
     */
    public boolean isOnFire() {
        return false;
    }

    /**
     * 获取方块对该凋灵头颅爆炸的抗性（原版重写方法）
     * 用于计算爆炸对方块的破坏程度，区分危险级头颅和普通头颅的破坏能力
     * @param pExplosion 爆炸实例
     * @param pLevel 方块所在的世界视图
     * @param pPos 方块的坐标
     * @param pBlockState 方块的状态
     * @param pFluidState 方块所在位置的流体状态
     * @param pExplosionPower 原始爆炸威力
     * @return 修正后的爆炸抗性（影响方块是否被破坏）
     */
    public float getBlockExplosionResistance(Explosion pExplosion, BlockGetter pLevel, BlockPos pPos, BlockState pBlockState, FluidState pFluidState, float pExplosionPower) {
        return this.isDangerous() && pBlockState.canEntityDestroy(pLevel, pPos, this) ? pExplosionPower : 50.0f;
    }

    /**
     * 当投射物命中实体时的处理逻辑（原版重写方法）
     * 负责处理对命中实体的伤害、凋零效果附加，以及对发射者的回血反馈
     * @param pResult 实体命中结果（包含被命中的实体等信息）
     */
    protected void onHitEntity(EntityHitResult pResult) {
        // 调用父类的实体命中处理逻辑（基础的投射物命中逻辑）
        super.onHitEntity(pResult);

        // 仅在服务端执行逻辑（避免客户端与服务端数据不一致）
        if (!this.level().isClientSide) {
            Entity hitEntity = pResult.getEntity(); // 获取被命中的实体
            Entity ownerEntity = this.getOwner(); // 获取投射物的发射者（凋灵BOSS）
            boolean isHurtSuccess; // 标记是否成功对命中实体造成伤害

            // 分支1：发射者是活体实体
            if (ownerEntity instanceof LivingEntity livingOwner) {
                // 对被命中实体造成攻击力130%的伤害，伤害值至少为8.0F
                float damage = (float) livingOwner.getAttributeValue(Attributes.ATTACK_DAMAGE);
                damage = Math.min(damage, 5.0f) * 1.3f;
                if (isDangerous()) damage *= 1.7F;
                isHurtSuccess = hitEntity.hurt(this.damageSources().explosion(this, livingOwner), damage);

                // 若伤害成功触发
                if (isHurtSuccess) {
                    // 若被命中实体仍存活，执行附魔相关的附加伤害效果（如火焰、锋利等）
                    if (hitEntity.isAlive()) {
                        this.doEnchantDamageEffects(livingOwner, hitEntity);
                    }
                }
            }
            // 分支2：发射者不是活体实体（特殊场景），造成5.0F的魔法伤害
            else {
                isHurtSuccess = hitEntity.hurt(this.damageSources().magic(), 8.0F);
            }

            // 若伤害成功，且被命中实体是活体实体，附加眩晕效果
            if (isHurtSuccess && hitEntity instanceof LivingEntity livingHitEntity) {
                int duration = 0;

                double knockbackResistance = livingHitEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
                if (knockbackResistance == 0) {
                    duration = 40;
                } else if (knockbackResistance < 0.4D) {
                    duration = 10;
                }

                if (duration > 0) {
                    StunEffect.applyImmortalEffect(livingHitEntity, duration, 0);
                }
            }
        }
    }

    /**
     * 当投射物命中方块或实体时的通用处理逻辑（原版重写方法）
     * 凋灵头颅命中后会触发爆炸，随后销毁自身
     * @param pResult 命中结果（包含命中类型、坐标等信息）
     */
    protected void onHit(HitResult pResult) {
        // 调用父类的通用命中处理逻辑
        super.onHit(pResult);

        // 仅在服务端执行爆炸逻辑（客户端仅做渲染反馈）
        if (!this.level().isClientSide) {
            // 在头颅当前位置触发爆炸，爆炸威力1.0F，不破坏方块（根据isDangerous修正），爆炸交互类型为生物
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), 1.0F, false, Level.ExplosionInteraction.MOB);
            // 销毁该凋灵头颅实体，从世界中移除
            this.discard();
        }
    }

    /**
     * 判断其他实体是否应被阻止穿过该实体（原版重写方法）
     * 凋灵头颅为投射物，允许其他实体穿过，故返回false
     * @return 是否阻止其他实体穿过，固定返回false
     */
    public boolean isPickable() {
        return false;
    }

    /**
     * 当该实体受到攻击时的处理逻辑（原版重写方法）
     * 凋灵头颅无法被攻击伤害，故返回false，忽略所有攻击
     * @param pSource 伤害来源
     * @param pAmount 伤害值
     * @return 是否受到伤害，固定返回false
     */
    public boolean hurt(DamageSource pSource, float pAmount) {
        return false;
    }

    /**
     * 定义该实体的同步数据（原版重写方法，实体初始化时调用）
     * 用于注册需要在服务端和客户端之间同步的实体数据，此处注册危险级标记
     */
    protected void defineSynchedData() {
        // 初始化同步数据，默认该凋灵头颅为非危险级（false）
        this.entityData.define(DATA_DANGEROUS, false);
    }

    /**
     * 判断该凋灵头颅是否为危险级（来自无敌状态的凋灵BOSS光环）
     * 危险级头颅具有更强的方块破坏能力
     * @return 是否为危险级凋灵头颅
     */
    public boolean isDangerous() {
        // 从同步实体数据中获取危险级标记
        return this.entityData.get(DATA_DANGEROUS);
    }

    /**
     * 设置该凋灵头颅是否为危险级
     * 通常由凋灵BOSS在发射头颅时根据自身状态设置
     * @param pInvulnerable 是否为危险级（对应凋灵BOSS的无敌状态）
     */
    public void setDangerous(boolean pInvulnerable) {
        // 向同步实体数据中写入危险级标记，自动同步到客户端
        this.entityData.set(DATA_DANGEROUS, pInvulnerable);
    }

    /**
     * 判断该投射物是否应该在飞行中被火焰点燃（原版重写方法）
     * 凋灵头颅不会被火焰点燃，故返回false
     * @return 是否应该被点燃，固定返回false
     */
    protected boolean shouldBurn() {
        return false;
    }
}
