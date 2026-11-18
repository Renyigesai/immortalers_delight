package com.renyigesai.immortalers_delight.entities.living;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightEntities;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import com.renyigesai.immortalers_delight.util.task.TimekeepingTask;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


//这个实体用于提供伤害来源、当沙包与使用自身tick保护方块实体
public class SoulInfuserEntity extends Monster {
    // =====================  状态与字段管理 ======================= //
    @Nullable
    private Vec3 homePos = null;
    public Vec3 getHomePos() {return homePos;}
    public void setHomePos(Vec3 vec3) {homePos = vec3;}
    private int ignoreDamageTime = 0;              //判断当前是否处于无敌状态，这个值仅服务端
    public int getIgnoreDamageTime() {return ignoreDamageTime;};
    public void setIgnoreDamageTime(int time) {this.ignoreDamageTime = time;}
//    private int deathArmor = 20;                   //死亡护甲，相当于寄生虫的最小击杀数，这个值是双端的，通过flag同步
    // 集成布尔值访问器
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(SoulInfuserEntity.class, EntityDataSerializers.BYTE);
    private static final byte FLAG_MODE_3 = 0x01;      // 第0位：是否进入三阶段
    private static final byte FLAG_INVULNERABLE = 0x02;   // 第1位：是否处于无敌状态
    private static final byte FLAG_IS_POWERFUL = 0x04;    // 第2位：是否处在超凡模式
    private static final byte FLAG_MODE_2 = 0x08;    // 第3位：是否进入二阶段
    private static final byte FLAG_MODE_4 = 0x16;    // 第4位：是否进入四阶段

//    protected static final EntityDataAccessor<Integer> BROKEN_DEATH_ARMOR = SynchedEntityData.defineId(SoulInfuserEntity.class, EntityDataSerializers.INT);
//    public int getBrokenDeathArmor() {return this.getEntityData().get(BROKEN_DEATH_ARMOR);}
//    public void setBrokenDeathArmor(int brokenCount) {this.getEntityData().set(BROKEN_DEATH_ARMOR,brokenCount);}

    private static final Map<UUID,Long>  killed = new HashMap<>();          //记录成为kill指令目标的本实体的UUID
    public static final Map<BlockPos,Long> protectedPos = new HashMap<>();    //记录受保护的方块坐标，这个值仅服务端


    public SoulInfuserEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public SoulInfuserEntity(Level pLevel) {
        super(ImmortalersDelightEntities.HIJACKED_SOUL_INFUSER.get(), pLevel);
    }
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS_ID, (byte)0);
//        this.entityData.define(BROKEN_DEATH_ARMOR,1);
    }

    public boolean isPowerMode() {
        return (this.entityData.get(DATA_FLAGS_ID) & FLAG_IS_POWERFUL) != 0;
    }

    public void setPowerMode(boolean active) {
        byte flags = this.entityData.get(DATA_FLAGS_ID);
        if (active) {
            flags |= FLAG_IS_POWERFUL;
        } else {
            flags &= ~FLAG_IS_POWERFUL;
        }
        this.entityData.set(DATA_FLAGS_ID, flags);
    }

    /**
     * 设置是否处于无敌状态
     */
    public void setIgnoreDamage(boolean invulnerable) {
        byte flags = this.entityData.get(DATA_FLAGS_ID);
        if (invulnerable) {
            flags |= FLAG_INVULNERABLE;  // 设置第1位为1
        } else {
            flags &= ~FLAG_INVULNERABLE; // 设置第1位为0
        }
        this.entityData.set(DATA_FLAGS_ID, flags);
    }

    /**
     * 获取是否处于无敌状态
     */
    public boolean isIgnoreDamage() {
        return (this.entityData.get(DATA_FLAGS_ID) & FLAG_INVULNERABLE) != 0;
    }

    /**
     * 设置进入第几阶段
     */
    public void setModeType(int typeID) {
        byte flags = this.entityData.get(DATA_FLAGS_ID);
        if (typeID == 4) {
            flags |= FLAG_MODE_4;  // 设置第0位为1
        } else {
            flags &= ~FLAG_MODE_4; // 设置第0位为0
        }
        if (typeID == 3) {
            flags |= FLAG_MODE_3;  // 设置第0位为1
        } else {
            flags &= ~FLAG_MODE_3; // 设置第0位为0
        }
        if (typeID == 2) {
            flags |= FLAG_MODE_2;  // 设置第0位为1
        } else {
            flags &= ~FLAG_MODE_2; // 设置第0位为0
        }
        this.entityData.set(DATA_FLAGS_ID, flags);
    }

    /**
     * 获取当前阶段
     */
    public int getModeType() {
        int mode = 1;
        if ((this.entityData.get(DATA_FLAGS_ID) & FLAG_MODE_4) != 0) mode++;
        if ((this.entityData.get(DATA_FLAGS_ID) & FLAG_MODE_3) != 0) mode++;
        if ((this.entityData.get(DATA_FLAGS_ID) & FLAG_MODE_2) != 0) mode++;
        return mode;
    }

    private boolean isDangerous() {
        return this.getModeType() >= 3;
    }
    // ==================== 索敌机制 ==================== //
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new LookAtPlayerGoal(this, Player.class, (float) this.getAttributeValue(Attributes.FOLLOW_RANGE)));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, this.getClass())).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));

    }

    // ==================== 固着不动 / 抗击退 ==================== //
    @Override
    public void push(Entity pEntity) {
        // 不被推动
    }

    @Override
    public void setDeltaMovement(Vec3 pDeltaMovement) {
        // 忽略任何外力速度，实现抗击退
    }

    @Override
    public @NotNull Vec3 getDeltaMovement() {
        return Vec3.ZERO;
    }

    @Override
    public void move(MoverType pType, Vec3 pPos) {
        // 不允许任何位移（活塞、爆炸位移等一律忽略）
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    //船的固态碰撞箱

    public boolean canCollideWith(Entity pEntity) {
        return canVehicleCollide(this, pEntity);
    }

    public static boolean canVehicleCollide(Entity pVehicle, Entity pEntity) {
        return (pEntity.canBeCollidedWith() || pEntity.isPushable()) && !pVehicle.isPassengerOfSameVehicle(pEntity);
    }

    public boolean canBeCollidedWith() {
        return true;
    }

    // ==================== 属性 ==================== //
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 500.0D)
                .add(Attributes.ARMOR, 16.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 16.0D)
                .add(Attributes.ATTACK_DAMAGE, 10.0D)
                .add(Attributes.FOLLOW_RANGE, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    /**
     * 获取属性修饰符，这方法是双端的
     * 普通模式下的数值如上，超凡模式下改为血上限1200，护甲40韧性40，攻击力24
     * 二阶段后，护甲+50%，超凡模式下改为+100%
     * 因为我们没有依赖属性修复所以采用最终加算的方式绕过属性限制
     */
    @Override
    public double getAttributeValue(Attribute pAttribute) {
        double amount = super.getAttributeValue(pAttribute);
        boolean isPowerful = this.level().isClientSide() ? this.isDangerous() : DifficultyModeUtil.isPowerBattleMode();
        if (isPowerful) {
            if (pAttribute == Attributes.MAX_HEALTH) amount += 700;
            if (pAttribute == Attributes.ATTACK_DAMAGE) amount += 14;
            if (pAttribute == Attributes.ARMOR) amount += this.isDangerous() ? 60 : 20;
            if (pAttribute == Attributes.ARMOR_TOUGHNESS) amount += 20;
        }
        return amount;
    }



    // ==================== 伤害处理 ==================== //
    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        //独立无敌帧
        if (this.ignoreDamageTime > 0) {
            return false;
        }
        //免疫自身伤害
        if (pSource.getEntity() == this || pSource.getDirectEntity() == this) {
            return false;
        }

        //远程保护
        double distance = this.isDangerous() ? 99999 : 0;
        Entity attacker = pSource.getEntity();
        if (attacker instanceof LivingEntity) {
            distance = this.distanceToSqr(attacker);
        }
        if (distance > 400) {
            pAmount *= 0.05F;
        }

        return super.hurt(pSource, pAmount);
    }


    /**
     * 计算法术减伤，返回值为计算保护附魔与抗性提升减伤之后的值
     * 法术抗性50，普通模式下二阶段法术抗性+50%，超凡模式+80%
     */
    protected float getDamageAfterMagicAbsorb(DamageSource pDamageSource, float pDamageAmount) {
        //对非物理非真实伤害，计算法术抗性的减伤
        if (pDamageSource.is(DamageTypeTags.BYPASSES_ARMOR) && !pDamageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            float buffer = 0.5f;
            if (this.isDangerous()) buffer = DifficultyModeUtil.isPowerBattleMode() ? 0.1f : 0.25f;
            return super.getDamageAfterMagicAbsorb(pDamageSource,pDamageAmount * buffer);
        }
        return super.getDamageAfterMagicAbsorb(pDamageSource,pDamageAmount);
    }

    public void setOldHealth(float health) {
            super.setHealth(health);
    }
    @Override
    public void setHealth(float health) {
        boolean isLost = health < this.getHealth();
        if (!isLost || ignoreDamageTime <= 0) {
            super.setHealth(health);
        }
    }
    @Override
    public float getHealth() {
        float f = super.getHealth();
        if (this.getModeType() < 4) return Math.max(f,1);
        return f;
    }
    public void die(@NotNull DamageSource source) {
        if (this.getModeType() < 4 && !source.is(DamageTypes.GENERIC_KILL)) return;
        protectedPos.clear();
        super.die(source);
    }

    @Override
    public boolean isAlive() {
        if (this.getModeType() < 4) return !this.isRemoved();
        return super.isAlive();
    }
    @Override
    public boolean isDeadOrDying() {
        if (this.getModeType() < 4) return false;
        return super.isDeadOrDying();
    }
    // ==================== Tick 逻辑 ==================== //

    /** 处理独立无敌帧和超凡模式判定(客户端同步) */
    @Override
    public void tick() {
        super.tick();
        if (this.isAlive()) {
            //服务端行为
            if (!this.level().isClientSide) {
                //添加受保护的方块坐标
                if (this.tickCount % 40 == 0) protectedPos.put(this.blockPosition(),this.level().getGameTime());
                //处理独立无敌帧
                if (ignoreDamageTime > 0) {
                    if (ignoreDamageTime == 1) this.setIgnoreDamage(false);
                    ignoreDamageTime--;
                }
                //同步超凡状态
                if (this.tickCount % 40 == 1) {
                    boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
                    if (isPowerful != this.isPowerMode()) this.setPowerMode(isPowerful);
                }
                //锁于地面
                if (this.homePos != null) this.setPos(homePos);
            } else {
                //客户端行为

            }

        }

    }

    public float getATK() {
        AttributeInstance instance = this.getAttribute(Attributes.ATTACK_DAMAGE);
        if (instance != null) return (float) instance.getValue();
        return 10;
    }

    @Mod.EventBusSubscriber(
            modid = ImmortalersDelightMod.MODID,
            bus = Mod.EventBusSubscriber.Bus.FORGE
    )
    public static class SoulInfuserEntityEvents {
        public static final Map<BlockPos,BlockState> protectedState = new ConcurrentHashMap<>();    //记录受保护的方块坐标，这个值仅服务端

        //尝试还原保护的方块
        @SubscribeEvent
        public static void onTick(@Nonnull TickEvent.LevelTickEvent evt) {
            if (protectedState.isEmpty()) return;
            if (evt.level == null || evt.level.isClientSide()) return;
            if (evt.phase.equals(TickEvent.Phase.START)) {
                for (Map.Entry<BlockPos,BlockState> entry : protectedState.entrySet()) {
                    evt.level.setBlockAndUpdate(entry.getKey(),entry.getValue());
                }
                protectedState.clear();
            }
        }

        //保护脚下的方块不可被破坏
        @SubscribeEvent
        public static void onBlockBreak(BlockEvent.BreakEvent event) {
            BlockPos pos = event.getPos();
            if (SoulInfuserEntity.protectedPos.containsKey(pos)) {
                if (event.getPlayer().level().getGameTime() > SoulInfuserEntity.protectedPos.getOrDefault(pos,0L) + 20) {
                    protectedPos.remove(pos);
                } else {
                    event.setResult(Event.Result.DENY);
                    event.setCanceled(true);
                }
            }
        }

        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void onOpUseCommand(CommandEvent event) {
            if (event.isCanceled()) return;

            List<ParsedCommandNode<CommandSourceStack>> nodes =
                    event.getParseResults().getContext().getNodes();

            // 第一个节点是 "kill"
            String rootNode = nodes.get(0).getNode().getName();

            // 确认是 /kill 命令
            if ("kill".equals(rootNode)) {
                ParseResults<CommandSourceStack> parse = event.getParseResults();

                boolean hasTargets = nodes.size() >= 2 && "targets".equals(nodes.get(1).getNode().getName());
                if (!hasTargets) {return;}

                // 将目标添加到豁免列表
                CommandContext<CommandSourceStack> ctx = parse.getContext().build(parse.getReader().getString());
                try {
                    // 读取目标实体
                    Collection<? extends Entity> targets = EntityArgument.getEntities(ctx, "targets");

                    //将目标添加到豁免实体表
                    for (Entity entity : targets) {
                        if (entity instanceof LivingEntity living) {
                            killed.put(living.getUUID(), TimekeepingTask.getImmortalTickTime());
                        }
                    }
                } catch (CommandSyntaxException ignored) {

                }
            }
        }

        //通过豁免实体列表来判定是否可以结束战斗
        public static boolean needExemptEntity(LivingEntity entity) {
            //如果不在豁免列表中，直接返回否定
            if (killed.get(entity.getUUID()) != null) {
                boolean needBreak = false;
                //如果在且是新鲜的解除指令，改为可以解除
                if (Math.abs(killed.get(entity.getUUID()) - TimekeepingTask.getImmortalTickTime()) < 100) {
                    needBreak = true;
                }
                //如果是已超时的解除指令，清表
                if (!needBreak) killed.remove(entity.getUUID());
                return needBreak;
            }
            return false;
        }
    }
}