package com.renyigesai.immortalers_delight.entities.living;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.SwellGoal;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collection;
import java.util.Objects;

@Mod.EventBusSubscriber
public class SkelverfishBomber extends SkelverfishBase{
    // 同步数据ID：膨胀状态方向、是否带电、是否被点燃
    private static final EntityDataAccessor<Integer> DATA_SWELL_DIR = SynchedEntityData.defineId(SkelverfishBomber.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_IS_POWERED = SynchedEntityData.defineId(SkelverfishBomber.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_IGNITED = SynchedEntityData.defineId(SkelverfishBomber.class, EntityDataSerializers.BOOLEAN);


    private int oldSwell;         // 上一时刻的膨胀值
    private int swell;            // 当前膨胀值（自爆倒计时）
    private int maxSwell = 12;    // 最大膨胀值（爆炸前的总时间）
    private int explosionRadius = 1; // 爆炸半径
    private int maxFireDamage = 0;   // 最大所受火焰伤害
    public SkelverfishBomber(EntityType<? extends Silverfish> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    /**
     * 定义同步数据（网络同步的实体属性）
     */
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SWELL_DIR, -1); // 默认未膨胀状态
        this.entityData.define(DATA_IS_POWERED, false); // 默认不带电
        this.entityData.define(DATA_IS_IGNITED, false); // 默认未点燃
    }

    /**
     * 将实体数据保存到NBT标签
     */
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        if (this.entityData.get(DATA_IS_POWERED)) {
            pCompound.putBoolean("powered", true); // 保存带电状态
        }
        pCompound.putShort("Fuse", (short)this.maxSwell); // 保存爆炸倒计时最大值
        pCompound.putShort("ExtraDamage", (short)this.maxFireDamage); // 保存火焰伤害记录
        pCompound.putByte("ExplosionRadius", (byte)this.explosionRadius); // 保存爆炸半径
        pCompound.putBoolean("ignited", this.isIgnited()); // 保存点燃状态
    }

    /**
     * 从NBT标签读取实体数据
     */
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.entityData.set(DATA_IS_POWERED, pCompound.getBoolean("powered")); // 读取带电状态
        if (pCompound.contains("Fuse", 99)) {
            this.maxSwell = pCompound.getShort("Fuse"); // 读取爆炸倒计时最大值
        }
        if (pCompound.contains("ExtraDamage", 99)) {
            this.maxFireDamage = pCompound.getShort("ExtraDamage"); // 读取火焰伤害记录
        }
        if (pCompound.contains("ExplosionRadius", 99)) {
            this.explosionRadius = pCompound.getByte("ExplosionRadius"); // 读取爆炸半径
        }
        if (pCompound.getBoolean("ignited")) {
            this.ignite(); // 恢复点燃状态
        }
    }

    /**
     * 攻击时触发的行为，令自身进入引燃状态
     */
    public boolean doHurtTarget(Entity pEntity) {
        if (!super.doHurtTarget(pEntity)) {
            return false;
        } else {
            if (pEntity instanceof LivingEntity) {
                this.ignite();
            }
            return true;
        }
    }

    /**
     * 自身受到攻击时触发的行为
     * 免疫火焰、爆炸伤害，受到火焰伤害时为自身记录受到过的火焰伤害，用于强化爆炸
     * 若受到非火焰非爆炸且不是同类造成的伤害，令自身退出引燃状态
     */
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.isInvulnerableTo(pSource)) {
            return false;
        } else if (!pSource.is(DamageTypeTags.IS_EXPLOSION) && !pSource.is(DamageTypeTags.IS_FIRE)) {
            if (pSource.getEntity() instanceof LivingEntity && !(pSource.getEntity() instanceof Silverfish)) {
                this.setSwellDir(-1);
                return super.hurt(pSource, pAmount);
            } else {
                return super.hurt(pSource, pAmount);
            }
        } else {
            if (pSource.is(DamageTypeTags.IS_FIRE)) {
                int safeAmount = pAmount > Short.MAX_VALUE ? Short.MAX_VALUE : (int)pAmount;
                this.maxFireDamage = safeAmount > this.maxFireDamage ? safeAmount : this.maxFireDamage;
            }
            return false;
        }
    }
    /**
     * 每游戏刻更新实体的位置和逻辑
     */
    public void tick() {
        if (this.isAlive()) {
            this.oldSwell = this.swell; // 保存上一时刻的膨胀值
            if (this.isIgnited()) {
                this.setSwellDir(1); // 被点燃时开始膨胀
            }

            int i = this.getSwellDir();
            if (i > 0 && this.swell == 0) {
                this.playSound(SoundEvents.SOUL_SAND_FALL, 1.0F, 0.5F); // 播放准备爆炸的音效
                this.gameEvent(GameEvent.PRIME_FUSE); // 触发游戏事件（用于音效和粒子效果）
            }

            this.swell += i; // 累积膨胀值，完成爆炸倒计时
            if (this.swell < 0) {
                this.swell = 0; // 确保膨胀值非负
            }

            if (this.swell >= this.maxSwell) {
                this.swell = this.maxSwell;
                this.explodeCreeper(); // 达到最大膨胀值时爆炸
            }
        }
        super.tick();
    }

    /**
     * 判断爬行者是否被点燃
     */
    public boolean isIgnited() {
        return this.entityData.get(DATA_IS_IGNITED);
    }

    /**
     * 点燃爬行者（开始爆炸倒计时）
     */
    public void ignite() {
        this.entityData.set(DATA_IS_IGNITED, true);
    }

    /**
     * 获取爬行者爆炸前的闪烁强度（用于渲染）
     */
    public float getSwelling(float pPartialTicks) {
        return Mth.lerp(pPartialTicks, (float)this.oldSwell, (float)this.swell) / (float)(this.maxSwell - 2);
    }

    /**
     * 获取爬行者的膨胀状态（-1: 未膨胀, 1: 正在膨胀）
     */
    public int getSwellDir() {
        return this.entityData.get(DATA_SWELL_DIR);
    }

    /**
     * 设置爬行者的膨胀状态
     */
    public void setSwellDir(int pState) {
        this.entityData.set(DATA_SWELL_DIR, pState);
    }

    /**
     * 判断爬行者是否带电（被闪电击中后）
     */
    public boolean isPowered() {
        return this.entityData.get(DATA_IS_POWERED);
    }

    /**
     * 被闪电击中时触发，使爬行者带电
     */
    public void thunderHit(ServerLevel pLevel, LightningBolt pLightning) {
        super.thunderHit(pLevel, pLightning);
        this.entityData.set(DATA_IS_POWERED, true); // 设置为带电状态
    }

    /**
     * 获取爬行者的膨胀状态（-1: 未膨胀, 1: 正在膨胀）
     */
    public int getMaxFireDamage() {
        return this.maxFireDamage;
    }

    private void explodeCreeper() {
        if (!this.level().isClientSide) {
            float f = this.isPowered() ? 2.0F : 1.0F; // 带电爬行者爆炸威力加倍
            this.dead = true;

            // 创建爆炸
            this.level().explode(this, this.getX(), this.getY(), this.getZ(),
                    (float)this.explosionRadius * f, Level.ExplosionInteraction.MOB);

            this.discard(); // 移除实体
            this.spawnLingeringCloud(); // 生成滞留云（如果爬行者有状态效果）
        }
    }

    /**
     * 生成带有爬行者状态效果的滞留云
     */
    private void spawnLingeringCloud() {
        Collection<MobEffectInstance> collection = this.getActiveEffects();
        if (!collection.isEmpty()) {
            // 创建滞留云实体
            AreaEffectCloud areaeffectcloud = new AreaEffectCloud(this.level(), this.getX(), this.getY(), this.getZ());
            areaeffectcloud.setRadius(2.5F); // 设置半径
            areaeffectcloud.setRadiusOnUse(-0.5F); // 使用后半径减小
            areaeffectcloud.setWaitTime(10); // 等待时间
            areaeffectcloud.setDuration(areaeffectcloud.getDuration() / 2); // 持续时间减半
            areaeffectcloud.setRadiusPerTick(-areaeffectcloud.getRadius() / (float)areaeffectcloud.getDuration()); // 随时间缩小半径

            // 添加爬行者的所有状态效果到滞留云
            for(MobEffectInstance mobeffectinstance : collection) {
                areaeffectcloud.addEffect(new MobEffectInstance(mobeffectinstance));
            }

            this.level().addFreshEntity(areaeffectcloud); // 添加到世界
        }
    }
}
