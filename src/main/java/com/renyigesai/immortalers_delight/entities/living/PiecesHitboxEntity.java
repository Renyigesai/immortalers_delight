package com.renyigesai.immortalers_delight.entities.living;

import com.google.common.collect.Maps;
import com.renyigesai.immortalers_delight.api.ILivingEntityExtension;
import com.renyigesai.immortalers_delight.client.particle.CircleTwinkleParticleOption;
import com.renyigesai.immortalers_delight.client.particle.TwinkleParticleOption;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightEntities;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightParticleTypes;
import com.renyigesai.immortalers_delight.item.food.InebriatedToxicFoodItem;
import com.renyigesai.immortalers_delight.potion.BaseMobEffect;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import com.renyigesai.immortalers_delight.util.LivingDamageUtil;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.warden.AngerLevel;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class PiecesHitboxEntity extends Mob implements TraceableEntity {
    private static final EntityDataAccessor<Float> DATA_RADIUS = SynchedEntityData.defineId(PiecesHitboxEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> DATA_WAITING = SynchedEntityData.defineId(PiecesHitboxEntity.class, EntityDataSerializers.BOOLEAN);
    protected float damage = 0;//已受到的伤害值（用于独立血条），注意set方法需要双端调用才能在客户端生效，但一般不应在客户端调用这个
    protected int warmupDelayTicks = -1;//攻击开始前的等待时间，注意set方法需要双端调用才能在客户端生效
    protected boolean sentSpikeEvent;//是否正在开始攻击，这个值仅在客户端生效
    protected int animationTicks = 0;//动画已经进行的时间，仅在客户端生效
    protected int attackDuration = 48; // 等待结束后的最大存活时间（游戏刻）
    protected int attackTriggerTicks = 21; // 等待状态结束后，到造成伤害前的前摇时间（游戏刻）
    protected float findRange = 2.5f;//默认索敌半径，实为自身半径的乘数
    protected float attackRange = 3f;//默认伤害半径，实为自身半径的乘数
    @Nullable
    private LivingEntity owner;
    @Nullable
    private UUID ownerUUID;
    private final String Warmup = "Warmup";
    private final String Damage = "Damage";
    private final String Radius = "Radius";

    public PiecesHitboxEntity(EntityType<? extends PiecesHitboxEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public PiecesHitboxEntity(Level pLevel, double pX, double pY, double pZ, float pYRot, boolean isWaiting) {
        this(ImmortalersDelightEntities.XIA_PIECES.get(), pLevel);
        this.setYRot(pYRot * (180F / (float)Math.PI));
        this.setPos(pX, pY, pZ);
        this.setWaiting(isWaiting);
        if (!isWaiting) {
            this.warmupDelayTicks = this.attackTriggerTicks + 1;
        }
    }

    public PiecesHitboxEntity(Level pLevel, double pX, double pY, double pZ, float pYRot, LivingEntity pOwner) {
        this(ImmortalersDelightEntities.XIA_PIECES.get(), pLevel);
        this.setOwner(pOwner);
        this.setYRot(pYRot * (180F / (float)Math.PI));
        this.setPos(pX, pY, pZ);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_RADIUS, 1.0F);
        this.entityData.define(DATA_WAITING, true);
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
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
     * @param pRadius 目标半径（会被限制在0.0-16.0之间）
     */
    public void setRadius(float pRadius) {
        if (!this.level().isClientSide) { // 仅在服务器端执行（保证数据一致性）
            this.getEntityData().set(DATA_RADIUS, Mth.clamp(pRadius, 0.0F, 16.0F));
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
    public void setWaiting(boolean waiting) {entityData.set(DATA_WAITING, waiting);}
    public boolean isWaiting() {
        if (this.level().isClientSide()) return this.getEntityData().get(DATA_WAITING);
        return warmupDelayTicks <= this.attackTriggerTicks;
    }

    public void setOwner(@Nullable LivingEntity pOwner) {
        this.owner = pOwner;
        this.ownerUUID = pOwner == null ? null : pOwner.getUUID();
    }

    public void setOwner(@Nullable UUID ownerID) {
        this.ownerUUID = ownerID;
    }

    /**
     * Returns null or the entityliving it was ignited by
     */
    @Nullable
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUUID != null && this.level() instanceof ServerLevel) {
            Entity entity = ((ServerLevel)this.level()).getEntity(this.ownerUUID);
            if (entity instanceof LivingEntity) {
                this.owner = (LivingEntity)entity;
            }
        }

        return this.owner;
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);

        String str = this.uuid.toString();
//        String keyW = str.substring(0, str.length() - 1) + "1";
//        String keyD = str.substring(0, str.length() - 1) + "2";
//        String keyR = str.substring(0, str.length() - 1) + "3";

        this.warmupDelayTicks = pCompound.getInt(Warmup);
        this.setDamage(pCompound.getFloat(Damage));
        this.setRadius(pCompound.getFloat(Radius));
        if (pCompound.hasUUID("Owner")) {
            this.ownerUUID = pCompound.getUUID("Owner");
        }
    }

    public void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt(Warmup, this.warmupDelayTicks);
        pCompound.putFloat(Damage, this.damage);
        pCompound.putFloat(Radius, this.getRadius());
        if (this.ownerUUID != null) {
            pCompound.putUUID("Owner", this.ownerUUID);
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
    public @NotNull EntityDimensions getDimensions(@NotNull Pose pPose) {
        if (this.getRadius() > 4.0f) return EntityDimensions.scalable(this.getRadius() * 2.0F, 12.0F - this.getRadius());
        return EntityDimensions.scalable(this.getRadius() * 2.0F, this.getRadius() * 2.0F);
    }

    public float getAnimationProgress(float pPartialTicks) {
        if (this.isWaiting()) {return 0.0F;}
        if (this.animationTicks > 38) return -1;
        return (float) this.animationTicks + pPartialTicks;
    }

    /*====================核心工作方法，通过tick与其他工具方法实现棋子的功能=======================*/

    /**
     * Called to update the entity's position/logic.
     */
    public void tick() {
//        if (this.tickCount == 1) System.out.println("棋子ID" + this.getUUID());
        //双端逻辑，处理一些基础事务

        ((ILivingEntityExtension) this).immDelight$cabuSubaperShunck();
        if (this.tickCount % 20 == 0) {
            Map<MobEffect, MobEffectInstance> effects = new HashMap<>(this.getActiveEffectsMap());
            for (Map.Entry<MobEffect, MobEffectInstance> entry : effects.entrySet()) {
                if (!entry.getKey().isBeneficial())this.removeEffect(entry.getKey());
            }
        }

        boolean flag = this.isWaiting(); // 当前是否处于等待状态

        if (!this.isAlive()) this.discard();

        //客户端逻辑，主要用于生成粒子
        doOnClient(flag);
        //服务端逻辑，用于执行实际功能
        doOnServer(flag);
    }

    protected void doOnClient(boolean flag) {
        if (this.level().isClientSide) {
//            System.out.println("瑕棋子" + this.getUUID().toString() + "客户端tick：");
//            System.out.println("瑕棋子客户端参数sentSpikeEvent：" + this.sentSpikeEvent);
//            System.out.println("瑕棋子客户端参数isWaiting：" + this.isWaiting());
//            System.out.println("瑕棋子服务端参数WaitingTime：" + this.warmupDelayTicks);
//            System.out.println("瑕棋子客户端参数animationTicks：" + this.animationTicks);
//            System.out.println("瑕棋子客户端tickCount：" + this.tickCount);
//            System.out.println("瑕棋子客户端Range：" + this.getRadius());
            if (this.sentSpikeEvent) {
                //一次性释放前摇粒子
                for (int j = 0; j < 32; ++j) {
                    ParticleOptions type = (j % 4 == 0) ? ImmortalersDelightParticleTypes.GOLDEN_GLIMMER.get()
                            : new TwinkleParticleOption(38);
                    double d0 = j >= 20 ? (this.random.nextDouble() * 2.0D - 1.0D) * 0.3D : 0;
                    double d1 = j >= 20 ? 0.3D + this.random.nextDouble() * 0.3D : 0;
                    double d2 = j >= 20 ? (this.random.nextDouble() * 2.0D - 1.0D) * 0.3D : 0;
                    this.level().addParticle(type, this.xo, this.getRandomY() + this.random.nextDouble() * 2.0D, this.zo, d0, d1, d2);
                }
                this.sentSpikeEvent = false;
            }
            //非等待状态，开始动画计时
            if (!flag) {
                ++this.animationTicks;
            }
            //根据动画进度释放粒子

            //此处为爆发中心的粒子
            if (this.animationTicks == attackTriggerTicks) {
                //爆发瞬间释放闪光粒子
                this.level().addParticle(ImmortalersDelightParticleTypes.GOLDEN_LIGHT.get(),
                        this.getX(),
                        this.getY() + this.getEyeHeight(),
                        this.getZ(),
                        0, 0, 0);
            }
            if (this.animationTicks == attackTriggerTicks + 5) {
                //释放金光粒子
                int max = (int) (32 * this.getRadius());
                for(int i = 0; i < max; ++i) {
                    ParticleOptions type = (i >= (0.4 * max)) ? ImmortalersDelightParticleTypes.GOLDEN_GLIMMER.get() : ParticleTypes.EXPLOSION;
                    double d0 = this.getX() + (this.random.nextDouble() * 2.0D - 1.0D) * (double)this.getBbWidth() * 2D;
                    double d1 = this.getY() + 0.05D + this.random.nextDouble();
                    double d2 = this.getZ() + (this.random.nextDouble() * 2.0D - 1.0D) * (double)this.getBbWidth() * 2D;
                    double d3 = i >= 12 ? 0D : (this.random.nextDouble() * 2.0D - 1.0D) * 0.3D;
                    double d4 = i >= 12 ? 0.2D : 0.3D + this.random.nextDouble() * 0.3D;
                    double d5 = i >= 12 ? 0D : (this.random.nextDouble() * 2.0D - 1.0D) * 0.3D;
                    this.level().addParticle(type, d0, d1 + 1.0D, d2, d3, d4, d5);
                }
            }
            if (this.animationTicks > attackTriggerTicks + 5 && this.animationTicks <= attackTriggerTicks + 10) {
                //爆发期间释放大圆形闪光粒子
                ParticleOptions cir = new CircleTwinkleParticleOption(86);
                this.level().addParticle(cir,
                        this.getX(),
                        this.getY() - 1,
                        this.getZ(),
                        0, 0, 0);
            }

            //下面是多中心的粒子
            if (this.animationTicks > attackTriggerTicks && this.animationTicks <= attackTriggerTicks + 10) {
                this.setTwinkleParticles(0,1,0,-22,-50);
            }
            if (this.animationTicks > attackTriggerTicks + 5 && this.animationTicks <= attackTriggerTicks + 13) {
                this.setTwinkleParticles(3,1,0,-22,-18);
                this.setTwinkleParticles(-3,1,0,-22,-18);
                this.setTwinkleParticles(0,1,3,-22,-18);
                this.setTwinkleParticles(0,1,-3,-22,-18);
            }
            if (this.animationTicks > attackTriggerTicks + 10 && this.animationTicks <= attackTriggerTicks + 15) {
                this.setTwinkleParticles(6,1,0,-22,-10);
                this.setTwinkleParticles(-6,1,0,-22,-10);
                this.setTwinkleParticles(0,1,6,-22,-10);
                this.setTwinkleParticles(0,1,-6,-22,-10);
                this.setTwinkleParticles(3,1,3,-22,-10);
                this.setTwinkleParticles(-3,1,3,-22,-10);
                this.setTwinkleParticles(3,1,-3,-22,-10);
                this.setTwinkleParticles(-0,1,-3,-22,-10);
            }
        }
    }

    public void setTwinkleParticles(float dx,float dy,float dz, int cirCustom, int twCustom) {
        ParticleOptions cir = new CircleTwinkleParticleOption(cirCustom);
        this.level().addParticle(cir,
                this.getX() + dx,
                this.getY() + dy,
                this.getZ() + dz,
                0, 0, 0);
        ParticleOptions tw = new TwinkleParticleOption(twCustom);
        this.level().addParticle(tw,
                this.getX() + dx,
                this.getY() + dy,
                this.getZ() + dz,
                0, 0, 0);
    }

    protected void doOnServer(boolean flag) {
        if (!this.level().isClientSide) {
//            System.out.println("瑕棋子服务端tick：");
//            System.out.println("瑕棋子服务端参数sentSpikeEvent：" + this.sentSpikeEvent);
//            System.out.println("瑕棋子服务端参数isWaiting：" + this.isWaiting());
//            System.out.println("瑕棋子服务端参数WaitingTime：" + this.warmupDelayTicks);
//            System.out.println("瑕棋子服务端参数animationTicks：" + this.animationTicks);
//            System.out.println("瑕棋子服务端tickCount：" + this.tickCount);
//            System.out.println("瑕棋子服务端Range：" + this.getRadius());
            //校验等待状态，如果客户端的等待状态与服务端不同，将其同步
            if (this.getEntityData().get(DATA_WAITING) != flag) {
                this.setWaiting(flag);
            }

            //等待状态执行的方法
            if (flag) {
                doOnWaiting(flag);
            }
            //启动状态执行的方法，注意上面的段落会改等待状态，所以不要用flag判断等待状态
            if (!this.isWaiting()) {
                doOnPersist();
            }
        }
    }

    protected void doOnWaiting(boolean flag) {

        // 更新等待状态（是否处于等待时间内）
        boolean flag1 = !this.needStart();

        //如果等待状态变化了，那么改变等待状态并执行开始时的效果
        if (flag != flag1) {
            this.level().broadcastEntityEvent(this, (byte)4);
            this.setWaiting(flag1);
        }
    }
    //判断攻击是否启动的逻辑
    protected boolean needStart() {

        float range = this.getRadius() * this.findRange;
        boolean need = false;
        // 每5刻处理一次索敌逻辑（减少性能消耗）
        if (this.tickCount % 5 == 0) {
            AABB aabb = this.getBoundingBox().inflate(this.getRadius() * this.findRange);
            AABB selection = new AABB(aabb.minX, this.getY() - 0.6D, aabb.minZ, aabb.maxX, this.getY() + 3.5D, aabb.maxZ);

            for(LivingEntity livingentity : this.level().getEntitiesOfClass(LivingEntity.class, selection)) {
                LivingEntity caster = this.getOwner(); // 获取效果所有者
                // 只对未记录或已超过重应用延迟的生物进行处理
                if (livingentity.isAlive() && livingentity != caster) {
                    if (caster == null
                            || (caster instanceof Player && livingentity instanceof Enemy)
                            || (!caster.isAlliedTo(livingentity) && !livingentity.isAlliedTo(caster))
                    ) {

                        // 计算生物与效果云中心的水平距离平方（优化：避免开方）
                        double d8 = livingentity.getX() - this.getX();
                        double d1 = livingentity.getZ() - this.getZ();
                        double d3 = d8 * d8 + d1 * d1;

                        // 在范围内
                        if (d3 <= (double)(range * range)) {
                            //设置等待时间，用于攻击计数
                            this.warmupDelayTicks = this.tickCount;
                            //超凡模式下具有聚怪功能
                             if (!DifficultyModeUtil.isPowerBattleMode()) return true;
                             else {
                                 need = true;
                                 if (livingentity instanceof Mob mob) {
                                     mob.setTarget(this);
                                     mob.getBrain().setMemoryWithExpiry(MemoryModuleType.ANGRY_AT, this.getUUID(), 600);
                                     mob.getBrain().setMemoryWithExpiry(MemoryModuleType.ATTACK_TARGET, this, 600);
                                     if (mob instanceof Warden warden) {
                                         warden.increaseAngerAt(this, AngerLevel.ANGRY.getMinimumAnger() + 20, false);
                                         warden.setAttackTarget(this);
                                     }
                                 }
                             }
                        }
                    }
                }
            }
        }
        return need;
    }

    protected final Map<Entity, Integer> victims = Maps.newHashMap(); // 记录受影响实体及下次可再次受影响的刻数
    //攻击启动后的逻辑
    protected void doOnPersist() {
        // 检查是否超过生命周期（等待时间 + 持续时间），超过则移除实体
        if (this.tickCount >= this.warmupDelayTicks + attackDuration) {
            this.discard();
            return;
        }
        //实际执行范围攻击
        if (this.tickCount == this.warmupDelayTicks + attackTriggerTicks
                ||this.tickCount == this.warmupDelayTicks + attackTriggerTicks + 5
                ||this.tickCount == this.warmupDelayTicks + attackTriggerTicks + 10
        ) {
            doRangeAttack();
        }
    }

    public void doRangeAttack() {

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
                        //对生物造成伤害或施加其他效果
                        this.dealDamageTo(livingentity, caster);
                    }
                }
            }
        }
        //System.out.println("看看伤害:" + getDamage());
        //System.out.println("现在在造成伤害，当前lifeTicks：" + animationTicks + ",这里是客户端吗？" + this.level().isClientSide);

    }
    //对单个目标实际造成伤害
    //棋子不同技能的伤害倍率在下方走事件实现（因为涉及到棋子类型和buff影响，懒得在子类重写了）
    protected void dealDamageTo(LivingEntity pTarget, @Nullable LivingEntity owner) {
        double damage = this.getAttribute(Attributes.ATTACK_DAMAGE) == null ? 0 : this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        if (damage <= 0) damage = 6;
        if (pTarget.isAlive() && !pTarget.isInvulnerable()) {
            DamageSource source;
            if (pTarget.getHealth() <= damage) {
                source = this.damageSources().indirectMagic(this,owner);
            } else source = this.damageSources().indirectMagic(owner == null ? this : owner,this);
            boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();

            //超凡模式默认叠满望二哥被动，此处为望二哥加伤
            if (isPowerful) damage *= 1.39f;
            boolean flag = pTarget.hurt(source, (float) damage);
            pTarget.invulnerableTime = 0;

            //望二哥法术穿透
            if (isPowerful && !flag) {
                LivingDamageUtil.hurtEntity(pTarget,source, (float) (damage * 0.36f));
            }
        }
    }

    /**
     * Handles an entity event received from a {@link net.minecraft.network.protocol.game.ClientboundEntityEventPacket}.
     */
    public void handleEntityEvent(byte pId) {
        super.handleEntityEvent(pId);
        if (pId == 4) {
            this.sentSpikeEvent = true;
            if (!this.isSilent()) {
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.IRON_GOLEM_ATTACK, this.getSoundSource(), 1.0F, this.random.nextFloat() * 0.2F + 0.85F, false);
            }
        }

    }
    /*======================下面是因为继承生物实体而需要重写的方法=========================*/
    //实体不会被其他实体推动，也不会推动其他实体
    public boolean isPushable() {
        return false;
    }

    public static AttributeSupplier.Builder createPiecesAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0D)
                .add(Attributes.ATTACK_DAMAGE, 1.0D)
                .add(Attributes.FOLLOW_RANGE, 16.0D)
                .add(Attributes.ARMOR, 10.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 10.0D)
                .add(Attributes.MAX_HEALTH, 600.0D);
    }

    public boolean isAlliedTo(@NotNull Entity pEntity) {
        LivingEntity livingentity = this.getOwner();
        if (pEntity == livingentity) {
            return true;
        }

        if (livingentity != null) {
            return livingentity.isAlliedTo(pEntity);
        }

        return super.isAlliedTo(pEntity);
    }
//    @Override
//    public boolean addEffect(MobEffectInstance pEffectInstance, @Nullable Entity pEntity) {
//        return false;
//    }
    @Override
    public boolean isDeadOrDying() {
        return false;
    }
    @Override
    public boolean isAlive() {return !this.isRemoved();}

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        System.out.println("Hurt传入的数值是" + pAmount);
        return super.hurt(pSource,pAmount);
    }
//
//    @Override
//    protected void actuallyHurt(DamageSource pDamageSource, float pDamageAmount) {
//        System.out.println("actHurt传入的数值是" + pDamageAmount);
//        System.out.println("当前血量" + getHealth());
//        super.actuallyHurt(pDamageSource,pDamageAmount);
//    }
//
//    @Override
//    public void setHealth(float health) {
//        System.out.println("原版改血传入的数值是" + health);
//    }
//
//    @Override
//    public float getHealth() {
//        return this.getMaxHealth();
//    }
//
//    @Override
//    public double getAttributeValue(@NotNull Attribute pAttribute) {
//        double d = super.getAttributeValue(pAttribute);
//        if (pAttribute == Attributes.MAX_HEALTH && d < 16) return 16;
//        return d;
//    }

    /*===========================下面是实现棋子之间的伤害倍率==============================*/

    @Mod.EventBusSubscriber
    public static class PrehistoricPowersPotionEffect {
        @SubscribeEvent
        public static void onCreatureHurt(LivingDamageEvent evt) {
            if (evt.isCanceled() || evt.getSource().is(DamageTypeTags.BYPASSES_EFFECTS)) {
                return;
            }
            LivingEntity hurtOne = evt.getEntity();
            LivingEntity attacker = null;
            if (evt.getSource().getEntity() instanceof PiecesHitboxEntity livingEntity){
                attacker = livingEntity;
            } else if (evt.getSource().getDirectEntity() instanceof PiecesHitboxEntity livingEntity){
                attacker = livingEntity;
            }

            if (!hurtOne.level().isClientSide && attacker != null) {
                //望二哥法术穿透
                boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
                if (isPowerful) {
                    double damage = attacker.getAttribute(Attributes.ATTACK_DAMAGE) == null ? 0 : attacker.getAttributeValue(Attributes.ATTACK_DAMAGE);
                    if (damage > 0 && evt.getAmount() < damage * 0.36) {
                        evt.setAmount((float) (damage * 0.36f));
                    }
                }

                MobEffectInstance moon = attacker.getEffect(ImmortalersDelightMobEffect.MOONBRIGHT.get());
                MobEffectInstance smoke = attacker.getEffect(ImmortalersDelightMobEffect.SMOKE_ABSTINENCE.get());
                //黑子伤害倍率，黑子减速效果
                if (attacker instanceof BlackPiecesHitboxEntity) {
                    //拿生效等级
                    int lv = 0;
                    if (smoke != null && smoke.getEffect() instanceof BaseMobEffect effect){
                        lv = effect.getTruthUsingAmplifier(smoke.getAmplifier()) + 1;
                    }
                    evt.setAmount(evt.getAmount() * (3.6f + 0.6f * lv));

                    //黑子减速，减速叠加
                    MobEffectInstance slow = hurtOne.getEffect(MobEffects.MOVEMENT_SLOWDOWN);
                    int amp = slow != null ? slow.getAmplifier() : 0;
                    if (amp > 3) amp = 3;
                    if (attacker.level().dimension() == Level.NETHER) {
                        InebriatedToxicFoodItem.addEffectWithoutCanBeAffected(hurtOne,
                                new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 120, amp + 2, true, false), attacker);
                    } else {
                        InebriatedToxicFoodItem.addEffectWithoutCanBeAffected(hurtOne,
                                new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 120, amp + 1, true, false), attacker);
                    }

                }
                //白子伤害倍率，注意白子停顿效果在实体中实现
                else if (attacker instanceof WhitePiecesHitboxEntity) {
                    //拿生效等级
                    int lv = 0;
                    if (moon != null && moon.getEffect() instanceof BaseMobEffect effect){
                        lv = effect.getTruthUsingAmplifier(moon.getAmplifier()) + 1;
                    }
                    evt.setAmount(evt.getAmount() * (0.8f + 0.2f * lv));
                }
                //瑕伤害倍率
                else {
                    //拿生效等级
                    int lv = 0;
                    if (smoke != null && smoke.getEffect() instanceof BaseMobEffect effect){
                        lv = effect.getTruthUsingAmplifier(smoke.getAmplifier()) + 1;
                    }
                    evt.setAmount(evt.getAmount() * (2.5f + 0.4f * lv));
                }
            }
        }
    }



    /*===========================下面是实现棋子之间的连线AI==============================*/

    @Nullable
    private PiecesHitboxEntity caravanHead;//“上家”，队列中的前一个
    @Nullable
    private PiecesHitboxEntity caravanTail;//下家，队列中的下一个
    //连接的实体ID，用于客户端渲染
    private static final EntityDataAccessor<Integer> DATA_ID_CARAVAN_HEAD = SynchedEntityData.defineId(PiecesHitboxEntity.class, EntityDataSerializers.INT);

    //离开商队：将自身的上家设为null。将上家的“下家”（通常为自身）设为null，以此断开联系
    public void leaveCaravan() {
        if (this.caravanHead != null) {
            this.caravanHead.caravanTail = null;
        }

        this.caravanHead = null;
    }

    //加入商队：为自身设置上家，并将上家的下家设为自身，建立双向联系
    public void joinCaravan(PiecesHitboxEntity pCaravanHead) {
        this.caravanHead = pCaravanHead;
        this.caravanHead.caravanTail = this;
    }

    //判断是否存在下家
    public boolean hasCaravanTail() {
        return this.caravanTail != null;
    }

    //判断是否存在上家，或用于判断是否在商队中
    public boolean inCaravan() {
        return this.caravanHead != null;
    }

    //获取上家
    @Nullable
    public PiecesHitboxEntity getCaravanHead() {
        return this.caravanHead;
    }

    //获取下家
    @Nullable
    public PiecesHitboxEntity getCaravanTail() {
        return this.caravanTail;
    }

    protected void doOnConnect() {
        if (!this.canContinueConnect()) this.stopConnect();
        if (this.canConnectTo())  this.startConnect();
    }

    /**
     * 判断是否应该开始执行此目标。
     * 条件：
     * 1. 羊驼未被拴绳且不在商队中。
     * 2. 在周围9格范围内搜索其他羊驼（或行商羊驼）。
     * 3. 优先寻找已经在商队中且没有尾巴的羊驼作为头羊；
     *    如果没有，则寻找被拴绳且没有尾巴的羊驼。
     * 4. 找到合适的头羊后，如果距离大于2格，且头羊（或头羊所在商队的首只）已被拴绳，
     *    则让当前羊驼加入该商队。
     * @return true 表示开始执行
     */
    public boolean canConnectTo() {
        // 若已经在商队中，则不能开始
        if (!this.inCaravan()) {
            double range = this.getAttributeValue(Attributes.FOLLOW_RANGE);
            // 获取周围视野范围内的所有实体，寻找同类
            List<Entity> list = this.level().getEntities(
                    this,
                    this.getBoundingBox().inflate(range, 4.0D, range),
                    (p_25505_) -> {
                        EntityType<?> entitytype = p_25505_.getType();
                        return entitytype == this.getType();
                    }
            );
            PiecesHitboxEntity piece = null;
            double d0 = Double.MAX_VALUE;

            // 第一轮：寻找已在商队中且没有下家的棋子（即可以作为连线终点的）
            for (Entity entity : list) {
                PiecesHitboxEntity piece1 = (PiecesHitboxEntity) entity;
                if (piece1.inCaravan() && !piece1.hasCaravanTail()) {
                    double d1 = this.distanceToSqr(piece1);
                    if (!(d1 > d0)) { // 取距离最近的一个
                        d0 = d1;
                        piece = piece1;
                    }
                }
            }

            // 如果没找到，第二轮：寻找没有下家的棋子
            if (piece == null) {
                for (Entity entity1 : list) {
                    PiecesHitboxEntity piece2 = (PiecesHitboxEntity) entity1;
                    if (!piece2.hasCaravanTail()) {
                        double d2 = this.distanceToSqr(piece2);
                        if (!(d2 > d0)) {
                            d0 = d2;
                            piece = piece2;
                        }
                    }
                }
            }

            // 仍未找到，返回false
            if (piece == null) {
                return false;
            }
            // 如果距离小于2格（太近），不需要加入
            if (d0 < 4.0D) {
                return false;
            }
            // 检查上家的上家是否是自身（避免二子成环）
            if (this.inCaravan() && piece.getCaravanHead().getUUID() == this.getUUID()) {
                return false;
            }
            // 满足所有条件，加入商队
            this.joinCaravan(piece);
            return true;
        } else {
            return false;
        }
    }

    public void startConnect() {

    }

    /**
     * 判断已启动的目标是否应继续执行。
     * 条件：
     * 1. 当前羊驼仍在商队中，且头羊存活。
     * 2. 递归检查商队首只羊驼是否被拴绳（参数0表示从自身开始检查）。
     * 3. 如果与头羊距离超过26格（676平方），则尝试加速追赶；
     *    但如果速度倍数已超过3.0且距离检查计数器为0，则放弃（脱离商队）。
     * @return true 表示继续执行
     */
    public boolean canContinueConnect() {
        if (this.inCaravan() && this.getCaravanHead().isAlive()) {
            double d0 = this.distanceToSqr(this.getCaravanHead());
            // 距离大于26格则断开连接
            return !(d0 > 676.0D);
        } else {
            return false;
        }
    }

    /**
     * 目标结束时调用：离开商队，重置速度为默认值（2.1）
     */
    public void stopConnect() {
        this.leaveCaravan();
    }
}



