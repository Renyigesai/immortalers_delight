package com.renyigesai.immortalers_delight.entities.projectile;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.logging.LogUtils;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;

import com.renyigesai.immortalers_delight.client.particle.ShockWaveParticleOption;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightEntities;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightParticleTypes;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.Util;
import net.minecraft.commands.arguments.ParticleArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.ShriekParticleOption;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

/**
 * 区域效果云实体类，用于实现游戏中具有范围效果的云状实体（如药水喷雾、龙息等）
 * 继承自Entity类并实现TraceableEntity接口，管理效果范围、持续时间、药水效果等属性
 */
public class EffectCloudBaseEntity extends Entity implements TraceableEntity {
    private static final Logger LOGGER = LogUtils.getLogger(); // 日志记录器
    // 实体同步数据定义：半径、颜色、等待状态、粒子效果
    private static final EntityDataAccessor<Boolean> DATA_DANGEROUS = SynchedEntityData.defineId(EffectCloudBaseEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> DATA_RADIUS = SynchedEntityData.defineId(EffectCloudBaseEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> DATA_WAITING = SynchedEntityData.defineId(EffectCloudBaseEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<ParticleOptions> DATA_PARTICLE = SynchedEntityData.defineId(EffectCloudBaseEntity.class, EntityDataSerializers.PARTICLE);
    private Potion potion = Potions.EMPTY; // 关联的药水（默认空药水）
    private final List<MobEffectInstance> effects = Lists.newArrayList(); // 额外的药水效果列表
    protected final Map<Entity, Integer> victims = Maps.newHashMap(); // 记录受影响实体及下次可再次受影响的刻数
    private boolean isStarting = false; // 是否在结束等待状态（注意：这个字段在客户端与服务端有不同的行为）
    private int duration = 600; // 持续时间（游戏刻）
    protected int lifeTicks = 0; //已存活时间，这个字段仅用于客户端
    private int waitTime = 20; // 等待时间（生效前的准备时间，游戏刻）
    protected int reapplicationDelay = 20; // 效果重应用延迟（防止频繁生效，游戏刻）
    private int durationOnUse; // 每次应用效果时的持续时间变化量
    private float radiusOnUse; // 每次应用效果时的半径变化量
    private float radiusPerTick; // 每刻的半径变化量
    @Nullable
    private LivingEntity owner; // 效果云的所有者（实体）
    @Nullable
    private UUID ownerUUID; // 所有者的UUID（用于持久化和同步）

    /**
     * 构造方法：通过实体类型和世界创建区域效果云
     * @param pEntityType 实体类型
     * @param pLevel 世界对象
     */
    public EffectCloudBaseEntity(EntityType<? extends EffectCloudBaseEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noPhysics = true; // 禁用物理模拟（不受重力等影响）
    }

    /**
     * 构造方法：在指定位置创建区域效果云
     * @param pLevel 世界对象
     * @param pX X坐标
     * @param pY Y坐标
     * @param pZ Z坐标
     */
    public EffectCloudBaseEntity(Level pLevel, double pX, double pY, double pZ) {
        this(ImmortalersDelightEntities.BASE_EFFECT_CLOUD.get(), pLevel);
        this.setPos(pX, pY, pZ); // 设置初始位置
    }

    /**
     * 定义需要在客户端和服务器之间同步的实体数据
     * （如半径、颜色等视觉和关键逻辑属性）
     */
    protected void defineSynchedData() {
        this.entityData.define(DATA_DANGEROUS, false);
        this.getEntityData().define(DATA_RADIUS, 3.0F); // 初始半径3.0F
        this.getEntityData().define(DATA_WAITING, false); // 初始不处于等待状态
        this.getEntityData().define(DATA_PARTICLE, ParticleTypes.ENTITY_EFFECT); // 初始粒子效果为实体效果粒子
    }

    public boolean isDangerous() {
        // 从同步实体数据中获取危险级标记
        return this.entityData.get(DATA_DANGEROUS);
    }

    public void setDangerous(boolean pInvulnerable) {
        // 向同步实体数据中写入危险级标记，自动同步到客户端
        this.entityData.set(DATA_DANGEROUS, pInvulnerable);
    }

    /**
     * 获取当前半径（从同步数据中读取）
     * @return 当前半径值
     */
    public float getRadius() {
        return this.getEntityData().get(DATA_RADIUS);
    }

    /**
     * 设置效果云的半径（服务器端）
     * @param pRadius 目标半径（会被限制在0.0-8.0之间）
     */
    public void setRadius(float pRadius) {
        if (!this.level().isClientSide) { // 仅在服务器端执行（保证数据一致性）
            this.getEntityData().set(DATA_RADIUS, Mth.clamp(pRadius, 0.0F, 7.995F));
        }
    }

    /**
     * 刷新实体碰撞箱尺寸（当半径变化时调用）
     * 先保存当前位置，刷新后重新设置位置（防止因尺寸变化导致位置偏移）
     */
    public void refreshDimensions() {
        double d0 = this.getX();
        double d1 = this.getY();
        double d2 = this.getZ();
        super.refreshDimensions();
        this.setPos(d0, d1, d2);
    }

    /**
     * 设置关联的药水，并更新颜色（如果未固定颜色）
     * @param pPotion 目标药水
     */
    public void setPotion(Potion pPotion) {
        this.potion = pPotion;
    }


    /**
     * 向效果云添加额外的药水效果，并更新颜色（如果未固定颜色）
     * @param pEffectInstance 药水效果实例
     */
    public void addEffect(MobEffectInstance pEffectInstance) {
        this.effects.add(pEffectInstance);
    }

    /**
     * 获取当前使用的粒子效果（从同步数据中读取）
     * @return 粒子效果选项
     */
    public ParticleOptions getParticle() {
        return this.getEntityData().get(DATA_PARTICLE);
    }

    /**
     * 设置效果云的粒子效果
     * @param pParticleOption 目标粒子效果
     */
    public void setParticle(ParticleOptions pParticleOption) {
        this.getEntityData().set(DATA_PARTICLE, pParticleOption);
    }

    /**
     * 设置效果云是否处于等待状态
     * 等待状态下：忽略半径逻辑，显示更少粒子
     * @param pWaiting 是否等待
     */
    protected void setWaiting(boolean pWaiting) {
        this.getEntityData().set(DATA_WAITING, pWaiting);
    }

    /**
     * 判断效果云是否处于等待状态
     * @return 是否等待
     */
    public boolean isWaiting() {
        return this.getEntityData().get(DATA_WAITING);
    }

    /**
     * 获取效果云的持续时间（游戏刻）
     * @return 持续时间
     */
    public int getDuration() {
        return this.duration;
    }

    /**
     * 设置效果云的持续时间（游戏刻）
     * @param pDuration 目标持续时间
     */
    public void setDuration(int pDuration) {
        this.duration = pDuration;
    }

    //客户端逻辑，这个方法每tick执行
    protected void updateParticles(float f) {
        boolean flag = this.isWaiting();
        if (flag && this.random.nextBoolean()) {
            return; // 等待状态下有50%概率不生成粒子（减少粒子数量）
        }

        ParticleOptions particleoptions = this.getParticle();
        int i; // 粒子数量
        float f1; // 粒子生成范围半径

        // 根据等待状态调整粒子参数
        if (flag) {
            i = 2; // 等待状态下固定生成2个粒子
            f1 = 0.2F; // 生成范围缩小
        } else {
            // 非等待状态下，粒子数量与圆面积成正比（πr²）
            i = Mth.ceil((float)Math.PI * f * f);
            f1 = f; // 生成范围等于效果云半径
        }

        // 生成粒子
        for(int j = 0; j < i; ++j) {
            // 随机计算粒子在圆上的位置（极坐标转直角坐标）
            float f2 = this.random.nextFloat() * ((float)Math.PI * 2F); // 随机角度
            float f3 = Mth.sqrt(this.random.nextFloat()) * f1; // 随机距离（确保在圆内均匀分布）
            double d0 = this.getX() + (double)(Mth.cos(f2) * f3); // X坐标
            double d2 = this.getY() + (double)((this.random.nextBoolean() ? 0.5 : -0.5) * f3) ; // Y坐标
            double d4 = this.getZ() + (double)(Mth.sin(f2) * f3); // Z坐标

            // 粒子运动速度（根据粒子类型和状态调整）
            double d5, d6, d7;
            if (flag) {
                // 等待状态下其他粒子：无速度
                d5 = 0.0D;
                d6 = 0.0D;
                d7 = 0.0D;
            } else {
                // 非等待状态下其他粒子：随机轻微速度
                d5 = (0.5D - this.random.nextDouble()) * 0.15D;
                d6 = (double)0.01F;
                d7 = (0.5D - this.random.nextDouble()) * 0.15D;
            }

            // 向世界添加粒子（始终可见）
            this.level().addAlwaysVisibleParticle(particleoptions, d0, d2, d4, d5, d6, d7);
        }
    }


    /**
     * 每游戏刻更新实体逻辑（核心方法）
     * 客户端：处理粒子效果渲染
     * 服务器：处理生命周期、半径变化、效果施加等逻辑
     */
    public void tick() {
        super.tick();
        boolean flag = this.isWaiting(); // 当前是否处于等待状态
        float f = this.getRadius(); // 当前半径
        System.out.println(" ");
        System.out.println("client tick?" + this.level().isClientSide);
        System.out.println("waitTime:" + this.waitTime);
        System.out.println("lifeTicks:" + this.lifeTicks);
        System.out.println("isStarting:" + this.isStarting);
        System.out.println("isWaiting?" + flag);

        // 客户端逻辑：渲染粒子效果并处理动画进度
        if (this.level().isClientSide) {
            ++this.lifeTicks;
            updateParticles(f);
        }
        // 服务器端逻辑：处理效果和生命周期
        else {
            // 检查是否超过生命周期（等待时间 + 持续时间），超过则移除实体
            if (this.tickCount >= this.waitTime + this.duration) {
                this.discard();
                return;
            }

            // 更新等待状态（是否处于等待时间内）
            boolean flag1 = this.tickCount < this.waitTime;

            //如果等待状态变化了，那么改变等待状态并执行开始时的效果
            if (flag != flag1) {
                this.isStarting = true;
                this.setWaiting(flag1);
            }

            // 等待状态下不处理效果逻辑
            if (flag1) {
                return;
            }

            if (this.isStarting) {
                doOnStart(f);
                this.isStarting = false;
            }

            // 处理半径随时间变化
            if (this.radiusPerTick != 0.0F) {
                f += this.radiusPerTick;
                // 半径小于最小值时移除实体
                if (f < 0.5F) {
                    this.discard();
                    return;
                }
                this.setRadius(f); // 更新半径
            }

            doOnPersist(f);
        }
    }

    /**
     * 当等待时间结束时触发的方法
     * 这个方法仅服务端
     * @param range
     */
    public void doOnStart(float range) {

    }

    /**
     * 当持续存在时这个实体周期性触发的方法
     * 这个方法仅服务端
     * @param range
     */
    public void doOnPersist(float range) {
        float f = range;
        // 每5刻处理一次效果施加逻辑（减少性能消耗）
        if (this.tickCount % 5 == 0) {
            // 移除超过重应用延迟的实体记录（允许再次受影响）
            this.victims.entrySet().removeIf((p_287380_) -> {
                return this.tickCount >= p_287380_.getValue();
            });
            boolean flag1 = addAreaEffect(f);
            boolean flag2 = doAdditionalAction(f);
            if (flag1 || flag2) {
                // 处理每次应用效果时的半径变化
                if (this.radiusOnUse != 0.0F) {
                    f += this.radiusOnUse;
                    if (f < 0.5F) {
                        this.discard(); // 半径过小则移除
                        return;
                    }
                    this.setRadius(f);
                }

                // 处理每次应用效果时的持续时间变化
                if (this.durationOnUse != 0) {
                    this.duration += this.durationOnUse;
                    if (this.duration <= 0) {
                        this.discard(); // 持续时间结束则移除
                        return;
                    }
                }
            }
        }
    }

    /**
     * 处理药水云施加buff的逻辑
     * 这个方法仅服务端
     * @param range
     * @return
     */
    protected boolean addAreaEffect(float range) {
        List<MobEffectInstance> list =this.getAllEffects();
        if (list.isEmpty()) return false;
        // 获取碰撞箱半径内的所有生物实体
        List<LivingEntity> list1 = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox());
        if (!list1.isEmpty()) {
            boolean flag = false;
            for(LivingEntity livingentity : list1) {
                LivingEntity caster = this.getOwner(); // 获取效果所有者
                // 只对未记录或已超过重应用延迟的生物施加效果
                if (livingentity.isAlive() && !this.victims.containsKey(livingentity) && livingentity != caster) {
                    if (caster == null || (!caster.isAlliedTo(livingentity) && !livingentity.isAlliedTo(caster))) {

                        // 计算生物与效果云中心的水平距离平方（优化：避免开方）
                        double d8 = livingentity.getX() - this.getX();
                        double d1 = livingentity.getZ() - this.getZ();
                        double d3 = d8 * d8 + d1 * d1;

                        // 距离小于等于半径平方（在范围内）
                        if (d3 <= (double)(range * range)) {
                            // 记录该生物下次可受影响的刻数
                            this.victims.put(livingentity, this.tickCount + this.reapplicationDelay);

                            doOnAddEffect(livingentity, list);
                            flag = true;//判断是否执行成功
                        }
                    }
                }
            }

            return flag;
        }
        return false;
    }

    public List<MobEffectInstance> getAllEffects() {
        // 收集所有生效的药水效果（关联药水的效果 + 额外添加的效果）
        List<MobEffectInstance> list = Lists.newArrayList();
        for(MobEffectInstance mobeffectinstance : this.potion.getEffects()) {
            // 药水自带效果的持续时间缩短为1/4（因为效果云会周期性施加）,若是超凡模式，效果时间先提升至5分之8
            list.add(new MobEffectInstance(mobeffectinstance.getEffect(), mobeffectinstance.mapDuration((p_267926_) -> {
                return (DifficultyModeUtil.isPowerBattleMode() ? p_267926_ * 2 : p_267926_) / (DifficultyModeUtil.isPowerBattleMode() ? 5 : 4);
            }), mobeffectinstance.getAmplifier(), mobeffectinstance.isAmbient(), mobeffectinstance.isVisible()));
        }
        list.addAll(this.effects); // 添加额外效果
        return list;
    }

    protected void doOnAddEffect(LivingEntity livingentity, List<MobEffectInstance> list) {
        // 对生物施加所有效果
        for(MobEffectInstance mobeffectinstance1 : list) {
            if (mobeffectinstance1.getEffect().isInstantenous()) {
                // 瞬时效果（如瞬间治疗）直接应用
                mobeffectinstance1.getEffect().applyInstantenousEffect(this, this.getOwner(), livingentity, mobeffectinstance1.getAmplifier(), 0.5D);
            } else {
                // 持续效果添加到生物身上
                livingentity.addEffect(new MobEffectInstance(mobeffectinstance1), this);
            }
        }
    }

    /**
     * 除施加buff以外进行的一些额外操作
     * @param range
     * @return
     */
    protected boolean doAdditionalAction(float range) {
//        LivingEntity caster = this.getOwner();
//        if (hurtOne.isAlive() && !hurtOne.isInvulnerable() && hurtOne != caster) {
//            if (caster == null) {
//
//            } else {
//                if (!caster.isAlliedTo(hurtOne) && !hurtOne.isAlliedTo(caster)) {
//
//                }
//            }
//        }
        return false;
    }

    /**
     * 获取每次应用效果时的半径变化量
     * @return 半径变化量
     */
    public float getRadiusOnUse() {
        return this.radiusOnUse;
    }

    /**
     * 设置每次应用效果时的半径变化量（可为正负）
     * @param pRadiusOnUse 半径变化量
     */
    public void setRadiusOnUse(float pRadiusOnUse) {
        this.radiusOnUse = pRadiusOnUse;
    }

    /**
     * 获取每刻的半径变化量
     * @return 每刻半径变化量
     */
    public float getRadiusPerTick() {
        return this.radiusPerTick;
    }

    /**
     * 设置每刻的半径变化量（可为正负，用于实现扩大或缩小效果）
     * @param pRadiusPerTick 每刻半径变化量
     */
    public void setRadiusPerTick(float pRadiusPerTick) {
        this.radiusPerTick = pRadiusPerTick;
    }

    /**
     * 获取每次应用效果时的持续时间变化量
     * @return 持续时间变化量
     */
    public int getDurationOnUse() {
        return this.durationOnUse;
    }

    /**
     * 设置每次应用效果时的持续时间变化量（可为正负）
     * @param pDurationOnUse 持续时间变化量
     */
    public void setDurationOnUse(int pDurationOnUse) {
        this.durationOnUse = pDurationOnUse;
    }

    /**
     * 获取等待时间（生效前的准备时间）
     * @return 等待时间（游戏刻）
     */
    public int getWaitTime() {
        return this.waitTime;
    }

    /**
     * 设置等待时间（生效前的准备时间）
     * @param pWaitTime 等待时间（游戏刻）
     */
    public void setWaitTime(int pWaitTime) {
        this.waitTime = pWaitTime;
    }

    /**
     * 设置效果云的所有者（通常是产生该效果云的实体）
     * @param pOwner 所有者实体（可为null）
     */
    public void setOwner(@Nullable LivingEntity pOwner) {
        this.owner = pOwner;
        this.ownerUUID = pOwner == null ? null : pOwner.getUUID(); // 同步UUID
    }

    /**
     * 获取效果云的所有者（如果UUID存在但实体未加载，会尝试从世界中查找）
     * @return 所有者实体（可为null）
     */
    @Nullable
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUUID != null && this.level() instanceof ServerLevel) {
            Entity entity = ((ServerLevel)this.level()).getEntity(this.ownerUUID);
            if (entity instanceof LivingEntity) {
                this.owner = (LivingEntity)entity; // 缓存查找到的实体
            }
        }
        return this.owner;
    }

    /**
     * 从NBT标签读取实体数据（用于加载保存的实体）
     * @param pCompound NBT复合标签
     */
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        this.tickCount = pCompound.getInt("Age"); // 实体已存在的刻数
        this.duration = pCompound.getInt("Duration");
        this.waitTime = pCompound.getInt("WaitTime");
        this.reapplicationDelay = pCompound.getInt("ReapplicationDelay");
        this.durationOnUse = pCompound.getInt("DurationOnUse");
        this.radiusOnUse = pCompound.getFloat("RadiusOnUse");
        this.radiusPerTick = pCompound.getFloat("RadiusPerTick");
        this.setRadius(pCompound.getFloat("Radius"));

        // 读取所有者UUID
        if (pCompound.hasUUID("Owner")) {
            this.ownerUUID = pCompound.getUUID("Owner");
        }

        // 读取粒子效果
        if (pCompound.contains("Particle", 8)) {
            try {
                this.setParticle(ParticleArgument.readParticle(new StringReader(pCompound.getString("Particle")), BuiltInRegistries.PARTICLE_TYPE.asLookup()));
            } catch (CommandSyntaxException commandsyntaxexception) {
                LOGGER.warn("Couldn't load custom particle {}", pCompound.getString("Particle"), commandsyntaxexception);
            }
        }

        // 读取关联药水
        if (pCompound.contains("Potion", 8)) {
            this.setPotion(PotionUtils.getPotion(pCompound));
        }

        // 读取额外药水效果
        if (pCompound.contains("Effects", 9)) {
            ListTag listtag = pCompound.getList("Effects", 10);
            this.effects.clear();
            for(int i = 0; i < listtag.size(); ++i) {
                MobEffectInstance mobeffectinstance = MobEffectInstance.load(listtag.getCompound(i));
                if (mobeffectinstance != null) {
                    this.addEffect(mobeffectinstance);
                }
            }
        }
    }

    /**
     * 将实体数据写入NBT标签（用于保存实体）
     * @param pCompound NBT复合标签
     */
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putInt("Age", this.tickCount);
        pCompound.putInt("Duration", this.duration);
        pCompound.putInt("WaitTime", this.waitTime);
        pCompound.putInt("ReapplicationDelay", this.reapplicationDelay);
        pCompound.putInt("DurationOnUse", this.durationOnUse);
        pCompound.putFloat("RadiusOnUse", this.radiusOnUse);
        pCompound.putFloat("RadiusPerTick", this.radiusPerTick);
        pCompound.putFloat("Radius", this.getRadius());
        pCompound.putString("Particle", this.getParticle().writeToString()); // 粒子效果序列化

        // 写入所有者UUID
        if (this.ownerUUID != null) {
            pCompound.putUUID("Owner", this.ownerUUID);
        }

        // 写入关联药水（非空时）
        if (this.potion != Potions.EMPTY) {
            pCompound.putString("Potion", BuiltInRegistries.POTION.getKey(this.potion).toString());
        }

        // 写入额外药水效果（非空时）
        if (!this.effects.isEmpty()) {
            ListTag listtag = new ListTag();
            for(MobEffectInstance mobeffectinstance : this.effects) {
                listtag.add(mobeffectinstance.save(new CompoundTag()));
            }
            pCompound.put("Effects", listtag);
        }
    }

    /**
     * 当同步数据更新时调用（如半径变化时刷新碰撞箱）
     * @param pKey 更新的数据键
     */
    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        if (DATA_RADIUS.equals(pKey)) {
            this.refreshDimensions(); // 半径变化时刷新尺寸
        }
        super.onSyncedDataUpdated(pKey);
    }

    /**
     * 获取关联的药水
     * @return 药水对象
     */
    public Potion getPotion() {
        return this.potion;
    }

    /**
     * 获取活塞推动反应（效果云不受活塞影响）
     * @return 推动反应类型（IGNORE表示忽略）
     */
    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    /**
     * 获取实体的碰撞箱尺寸（根据当前半径动态计算）
     * @param pPose 实体姿势（效果云不区分姿势）
     * @return 实体尺寸（宽度为直径，高度固定）
     */
    public EntityDimensions getDimensions(Pose pPose) {
        return EntityDimensions.scalable(this.getRadius() * 2.0F, this.getRadius() * 0.5F);
    }

    public float getAnimationProgress(float pPartialTicks) {
        int i = this.waitTime - this.lifeTicks;
        return i <= 0 ? 1.0F : 1.0F - ((float)i - pPartialTicks) / this.waitTime;
    }
}
