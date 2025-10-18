package com.renyigesai.immortalers_delight.entities.living;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.screen.TerracottaGolemMenu;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Predicate;

import static com.renyigesai.immortalers_delight.potion.EsteemedGuestPotionEffect.FRIEND_TO;

public class TerracottaGolem extends TamableAnimal implements ContainerListener, HasCustomInventoryScreen, RangedAttackMob, NeutralMob {
    private static final EntityDataAccessor<Byte> DATA_ID_DECORATE_LEFT = SynchedEntityData.defineId(TerracottaGolem.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> DATA_ID_DECORATE_BACK = SynchedEntityData.defineId(TerracottaGolem.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> DATA_ID_DECORATE_RIGHT = SynchedEntityData.defineId(TerracottaGolem.class, EntityDataSerializers.BYTE);

    // 同步数据访问器：用于客户端和服务器同步马匹状态标志（驯服、鞍具等）
    private static final EntityDataAccessor<Byte> DATA_ID_FLAGS = SynchedEntityData.defineId(TerracottaGolem.class, EntityDataSerializers.BYTE);
    // 标志位：已驯服
    private static final int FLAG_TAME = 2;
    // 标志位：装备鞍具
    private static final int FLAG_SADDLE = 4;
    // 标志位：已繁殖
    private static final int FLAG_BRED = 8;
    // 标志位：正在进食
    private static final int FLAG_EATING = 16;
    // 标志位：正在站立（后腿站立）
    private static final int FLAG_STANDING = 32;
    // 标志位：张开嘴
    private static final int FLAG_OPEN_MOUTH = 64;
    // 马匹的 inventory 容器（存储鞍具、护甲等）
    protected SimpleContainer inventory;

    private net.minecraftforge.common.util.LazyOptional<?> itemHandler = null;
    public static final int INV_SLOT_LEFT = 0;
    public static final int INV_SLOT_BACK = 1;
    public static final int INV_SLOT_RIGHT = 2;
    private static final EntityDataAccessor<Boolean> DATA_INTERESTED_ID = SynchedEntityData.defineId(TerracottaGolem.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_COLLAR_COLOR = SynchedEntityData.defineId(TerracottaGolem.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(TerracottaGolem.class, EntityDataSerializers.INT);
    public static final Predicate<LivingEntity> PREY_SELECTOR = (p_289448_) -> {
        EntityType<?> entitytype = p_289448_.getType();
        return entitytype == EntityType.SHEEP || entitytype == EntityType.RABBIT || entitytype == EntityType.FOX;
    };
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    @javax.annotation.Nullable
    private UUID persistentAngerTarget;

    public static final AnimationState idleAnimationState = new AnimationState();
    public int idleAnimationTimeOut = 0;
    public static final AnimationState attackAnimationState = new AnimationState();
    public int attackAnimationTimeOut = 0;


    /**===================数据管理部分，提供实体字段的getter和setter方法======================**/

    /**
     * 获取狼项圈的颜色
     * @return 项圈颜色
     */
    public DyeColor getCollarColor() {
        return DyeColor.byId(this.entityData.get(DATA_COLLAR_COLOR));
    }

    /**
     * 设置狼项圈的颜色
     * @param pCollarColor 项圈颜色
     */
    public void setCollarColor(DyeColor pCollarColor) {
        this.entityData.set(DATA_COLLAR_COLOR, pCollarColor.getId());
    }

    public boolean isInterested() {
        return this.entityData.get(DATA_INTERESTED_ID);
    }

    /**
     * 设置狼是否感兴趣
     * @param pIsInterested true表示感兴趣，false表示不感兴趣
     */
    public void setIsInterested(boolean pIsInterested) {
        this.entityData.set(DATA_INTERESTED_ID, pIsInterested);
    }

    protected boolean getFlag(int pFlagId) {
        return (this.entityData.get(DATA_ID_FLAGS) & pFlagId) != 0;
    }

    protected void setFlag(int pFlagId, boolean pValue) {
        byte b0 = this.entityData.get(DATA_ID_FLAGS);
        if (pValue) {
            this.entityData.set(DATA_ID_FLAGS, (byte)(b0 | pFlagId));
        } else {
            this.entityData.set(DATA_ID_FLAGS, (byte)(b0 & ~pFlagId));
        }

    }

    public boolean isEating() {
        return this.getFlag(16);
    }

    public boolean isStanding() {
        return this.getFlag(32);
    }

    public boolean isBred() {
        return this.getFlag(8);
    }

    public void setBred(boolean pBreeding) {
        this.setFlag(8, pBreeding);
    }

    public boolean isSaddleable() {
        return this.isAlive() && !this.isBaby() && this.isTame();
    }

    public void equipSaddle(@javax.annotation.Nullable SoundSource pSource) {
        this.inventory.setItem(0, new ItemStack(Items.SADDLE));
    }

    public void equipArmor(Player pPlayer, ItemStack pArmor) {
        if (this.isArmor(pArmor)) {
            this.inventory.setItem(1, pArmor.copyWithCount(1));
            if (!pPlayer.getAbilities().instabuild) {
                pArmor.shrink(1);
            }
        }

    }

    public boolean isArmor(ItemStack pStack) {
        return pStack.is(ItemTags.DECORATED_POT_INGREDIENTS);
    }

    public boolean isSaddled() {
        return this.getFlag(4);
    }

    /**==================处理中立生物的相关逻辑。of course，通常这个傀儡在创建时应该是已经驯服的==================**/

    /**
     * 获取狼持续愤怒的剩余时间
     * @return 剩余愤怒时间
     */
    public int getRemainingPersistentAngerTime() {
        return this.entityData.get(DATA_REMAINING_ANGER_TIME);
    }

    /**
     * 设置狼持续愤怒的剩余时间
     * @param pTime 剩余愤怒时间
     */
    public void setRemainingPersistentAngerTime(int pTime) {
        this.entityData.set(DATA_REMAINING_ANGER_TIME, pTime);
    }

    /**
     * 开始狼的持续愤怒计时
     * 随机设置狼的持续愤怒时间
     */
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    /**
     * 获取狼持续愤怒的目标UUID
     * @return 目标UUID
     */
    @Nullable
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    /**
     * 设置狼持续愤怒的目标UUID
     * @param pTarget 目标UUID
     */
    public void setPersistentAngerTarget(@Nullable UUID pTarget) {
        this.persistentAngerTarget = pTarget;
    }


    public int getAmbientStandInterval() {
        return this.getAmbientSoundInterval();
    }

    /**======================背包管理相关方法，读取特定位置的陶片纹样并处理其他背包交互==========================**/

    public byte getLeftDecorateID() {
        return this.entityData.get(DATA_ID_DECORATE_LEFT);
    }
    public byte getBackDecorateID() {
        return this.entityData.get(DATA_ID_DECORATE_BACK);
    }
    public byte getRightDecorateID() {
        return this.entityData.get(DATA_ID_DECORATE_RIGHT);
    }
//    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
//        if (!this.isVehicle() && !this.isBaby()) {
//            if (this.isTame() && pPlayer.isSecondaryUseActive()) {
//                this.openCustomInventoryScreen(pPlayer);
//                return InteractionResult.sidedSuccess(this.level().isClientSide);
//            } else {
//                ItemStack itemstack = pPlayer.getItemInHand(pHand);
//                if (!itemstack.isEmpty()) {
//                    InteractionResult interactionresult = itemstack.interactLivingEntity(pPlayer, this, pHand);
//                    if (interactionresult.consumesAction()) {
//                        return interactionresult;
//                    }
//
//                    if (this.canWearArmor() && this.isArmor(itemstack) && !this.isWearingArmor()) {
//                        this.equipArmor(pPlayer, itemstack);
//                        return InteractionResult.sidedSuccess(this.level().isClientSide);
//                    }
//                }
//
//                //this.doPlayerRide(pPlayer);
//                return InteractionResult.sidedSuccess(this.level().isClientSide);
//            }
//        } else {
//            return super.mobInteract(pPlayer, pHand);
//        }
//    }

    /**
     * 获取 inventory 槽位数量
     * @return 槽位数量
     */
    protected int getInventorySize() {
        return 23;
    }

    /**
     * 获取 inventory 的列数
     * @return 5列
     */
    public int getInventoryColumns() {
        return 5;
    }
    /**
     * 创建 inventory 容器
     * 若已有 inventory，则复制其中的物品到新容器
     */
    protected void createInventory() {
        SimpleContainer simplecontainer = this.inventory;
        this.inventory = new SimpleContainer(this.getInventorySize()); // 创建新容器
        if (simplecontainer != null) { // 若有旧容器
            simplecontainer.removeListener(this);
            int i = Math.min(simplecontainer.getContainerSize(), this.inventory.getContainerSize()); // 取最小槽位数量

            for(int j = 0; j < i; ++j) { // 复制物品
                ItemStack itemstack = simplecontainer.getItem(j);
                if (!itemstack.isEmpty()) {
                    this.inventory.setItem(j, itemstack.copy());
                }
            }
        }

        this.inventory.addListener(this); // 添加容器监听器（自身）
        this.updateContainerEquipment(); // 更新装备状态
        // 初始化Forge的物品处理器（用于mod扩展）
        this.itemHandler = net.minecraftforge.common.util.LazyOptional.of(() -> new net.minecraftforge.items.wrapper.InvWrapper(this.inventory));
    }

    /**
     * 更新容器中的装备状态（同步鞍具标志位）
     */
    protected void updateContainerEquipment() {
        if (!this.level().isClientSide) { // 仅在服务器端执行
            this.entityData.set(DATA_ID_DECORATE_LEFT, whatDecorateIs(this.inventory.getItem(INV_SLOT_LEFT)));
            this.entityData.set(DATA_ID_DECORATE_BACK, whatDecorateIs(this.inventory.getItem(INV_SLOT_BACK)));
            this.entityData.set(DATA_ID_DECORATE_RIGHT, whatDecorateIs(this.inventory.getItem(INV_SLOT_RIGHT)));
            this.setFlag(FLAG_SADDLE, !this.inventory.getItem(INV_SLOT_BACK).isEmpty()); // 若鞍具槽有物品，设置鞍具标志
        }
    }

    protected byte whatDecorateIs(ItemStack itemStack) {
        if (itemStack.is(Items.ANGLER_POTTERY_SHERD)) {
            return 1;
        } else if (itemStack.is(Items.ARCHER_POTTERY_SHERD)) {
            return 2;
        } else if (itemStack.is(Items.ARMS_UP_POTTERY_SHERD)) {
            return 3;
        } else if (itemStack.is(Items.BLADE_POTTERY_SHERD)) {
            return 4;
        } else if (itemStack.is(Items.BREWER_POTTERY_SHERD)) {
            return 5;
        } else if (itemStack.is(Items.BURN_POTTERY_SHERD)) {
            return 6;
        } else if (itemStack.is(Items.DANGER_POTTERY_SHERD)) {
            return 7;
        } else if (itemStack.is(Items.EXPLORER_POTTERY_SHERD)) {
            return 8;
        } else if (itemStack.is(Items.FRIEND_POTTERY_SHERD)) {
            return 9;
        } else if (itemStack.is(Items.HEART_POTTERY_SHERD)) {
            return 10;
        } else if (itemStack.is(Items.HEARTBREAK_POTTERY_SHERD)) {
            return 11;
        } else if (itemStack.is(Items.HOWL_POTTERY_SHERD)) {
            return 12;
        } else if (itemStack.is(Items.MINER_POTTERY_SHERD)) {
            return 13;
        } else if (itemStack.is(Items.MOURNER_POTTERY_SHERD)) {
            return 14;
        } else if (itemStack.is(Items.PLENTY_POTTERY_SHERD)) {
            return 15;
        } else if (itemStack.is(Items.PRIZE_POTTERY_SHERD)) {
            return 16;
        } else if (itemStack.is(Items.SHEAF_POTTERY_SHERD)) {
            return 17;
        } else if (itemStack.is(Items.SHELTER_POTTERY_SHERD)) {
            return 18;
        } else if (itemStack.is(Items.SKULL_POTTERY_SHERD)) {
            return 19;
        } else if (itemStack.is(Items.SNORT_POTTERY_SHERD)) {
            return 20;
        }
        //HERO_POTTERY_SHERD
        //PLENTIFUL_POTTERY_SHERD
        return 0;
    }

    /**
     * 当容器内容变化时调用（实现ContainerListener接口）
     * @param pContainer 发生变化的容器
     */
    @Override
    public void containerChanged(Container pContainer) {
        boolean flag = this.isSaddled(); // 记录之前的鞍具状态
        this.updateContainerEquipment(); // 更新装备状态
        // 若超过20tick且之前无鞍具现在有，则播放装备鞍具音效
        if (this.tickCount > 20 && !flag && this.isSaddled()) {
            //this.playSound(this.getSaddleSoundEvent(), 0.5F, 1.0F);
        }
    }

    /**
     * 打开自定义 inventory 界面（供玩家操作）
     * 仅在服务器端、非骑乘状态或玩家是乘客、已驯服时生效
     * @param pPlayer 打开界面的玩家
     */
    @Override
    public void openCustomInventoryScreen(Player pPlayer) {
        System.out.println("openCustomInventoryScreen开始");
        if(pPlayer instanceof ServerPlayer serverplayer) {
            if (isAlive()) {

                // 删除冗余代码并转用简洁的打开方式
                NetworkHooks.openScreen(serverplayer,
                        new SimpleMenuProvider(
                                (containerId, inv, ServerPlayer) ->
                                new TerracottaGolemMenu(containerId,inv,this),
                                Component.translatable(ImmortalersDelightMod.MODID + ".container.terracotta_golem")
                        ),
                        friendlyByteBuf -> friendlyByteBuf.writeVarInt(this.getId())); // 写入实体id，随后传入客户端
            }
        }
    }


    @Override
    public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable net.minecraft.core.Direction facing) {
        if (this.isAlive() && capability == net.minecraftforge.common.capabilities.ForgeCapabilities.ITEM_HANDLER && itemHandler != null)
            return itemHandler.cast();
        return super.getCapability(capability, facing);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        if (itemHandler != null) {
            net.minecraftforge.common.util.LazyOptional<?> oldHandler = itemHandler;
            itemHandler = null;
            oldHandler.invalidate();
        }
    }
//
    public boolean hasInventoryChanged(Container pInventory) {
        return this.inventory != pInventory;
    }

    public boolean hasChest() {
        return true;
    }


    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source.is(DamageTypes.IN_WALL) || source.is(DamageTypes.FALLING_BLOCK) || super.isInvulnerableTo(source);
    }

    protected void dropEquipment() {
        super.dropEquipment();
        if (inventory != null) {
            for (int i = 0; i < inventory.getContainerSize(); ++i) {
                ItemStack itemstack = inventory.getItem(i);
                if (!itemstack.isEmpty()) {
                    this.spawnAtLocation(itemstack, 0.0F);
                }
            }
        }
    }


    public boolean canBreatheUnderwater() {
        return true;
    }

//    @Override
//    public boolean isBaby() {
//        return true;
//    }

    /**==================实体基本方法，处理Goal注册、属性注册、音效等实体通用方法==================**/
    public TerracottaGolem(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setTame(false);
        this.setMaxUpStep(1.0F);
        this.createInventory();
    }
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(1, new TerracottaGolem.TerracottaGolemPanicGoal(1.5D));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(7, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 1.0D));

        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
        //this.targetSelector.addGoal(5, new NonTameRandomTargetGoal<>(this, Animal.class, false, PREY_SELECTOR));
        //this.targetSelector.addGoal(6, new NonTameRandomTargetGoal<>(this, Turtle.class, false, Turtle.BABY_ON_LAND_SELECTOR));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Mob.class, 5, false, false, (p_28879_) -> {
            return p_28879_ instanceof Enemy && !(p_28879_ instanceof Creeper);
        }));
        this.targetSelector.addGoal(8, new ResetUniversalAngerTargetGoal<>(this, true));
    }

    public static AttributeSupplier.Builder createTerracottaGolemAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 24.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.ARMOR, 5.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 5.0D)
                .add(Attributes.MAX_HEALTH, 10.0D);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_INTERESTED_ID, false);
        this.entityData.define(DATA_COLLAR_COLOR, DyeColor.RED.getId());
        this.entityData.define(DATA_REMAINING_ANGER_TIME, 0);
        this.entityData.define(DATA_ID_FLAGS, (byte)0);
        this.entityData.define(DATA_ID_DECORATE_BACK, (byte)0);
        this.entityData.define(DATA_ID_DECORATE_LEFT, (byte)0);
        this.entityData.define(DATA_ID_DECORATE_RIGHT, (byte)0);
    }


    /**
     * 将狼的额外数据保存到NBT标签中
     * 保存狼的项圈颜色和愤怒状态等数据
     * @param pCompound NBT标签
     */
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        // 将狼的项圈颜色保存到NBT标签中
        pCompound.putByte("CollarColor", (byte)this.getCollarColor().getId());
        // 保存狼的持续愤怒数据
        this.addPersistentAngerSaveData(pCompound);

        ListTag listtag = new ListTag();

        for(int i = 0; i < this.inventory.getContainerSize(); ++i) {
            ItemStack itemstack = this.inventory.getItem(i);
            if (!itemstack.isEmpty()) {
                CompoundTag compoundtag = new CompoundTag();
                compoundtag.putByte("Slot", (byte)i);
                itemstack.save(compoundtag);
                listtag.add(compoundtag);
            }
        }

        pCompound.put("Items", listtag);
    }

    /**
     * 从NBT标签中读取狼的额外数据
     * 读取狼的项圈颜色和愤怒状态等数据
     * @param pCompound NBT标签
     */
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        // 如果NBT标签中包含项圈颜色数据，则设置狼的项圈颜色
        if (pCompound.contains("CollarColor", 99)) {
            this.setCollarColor(DyeColor.byId(pCompound.getInt("CollarColor")));
        }
        // 读取狼的持续愤怒数据
        this.readPersistentAngerSaveData(this.level(), pCompound);

        System.out.println("readAdditionalSaveData读完了狼的nbt");
        this.createInventory();
        ListTag listtag = pCompound.getList("Items", 10); // 更改为正确的tag类型，防止读取时出错

        for(int i = 0; i < listtag.size(); ++i) {
            CompoundTag compoundtag = listtag.getCompound(i);
            int j = compoundtag.getByte("Slot") & 255;
            if (j < this.inventory.getContainerSize()) {
                this.inventory.setItem(j, ItemStack.of(compoundtag));
            }
        }

    }


    /**
     * 获取狼的环境音效
     * 根据狼的状态返回不同的环境音效，如愤怒、驯服、健康状态等
     * @return 环境音效
     */
    protected SoundEvent getAmbientSound() {
        if (this.isAngry()) {
            return SoundEvents.WOLF_GROWL; // 狼愤怒时发出咆哮声
        } else if (this.random.nextInt(3) == 0) {
            return this.isTame() && this.getHealth() < 10.0F ? SoundEvents.WOLF_WHINE : SoundEvents.WOLF_PANT;
            // 驯服且生命值低于10时发出呜咽声，否则发出喘气声
        } else {
            return SoundEvents.WOLF_AMBIENT; // 正常状态下发出环境音效
        }
    }

    /**
     * 获取狼受伤时的音效
     * 当狼受到伤害时播放该音效
     * @param pDamageSource 伤害来源
     * @return 受伤音效
     */
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.WOLF_HURT;
    }

    /**
     * 获取狼死亡时的音效
     * 当狼死亡时播放该音效
     * @return 死亡音效
     */
    protected SoundEvent getDeathSound() {
        return SoundEvents.WOLF_DEATH;
    }

    /**
     * 设置狼的驯服状态
     * 根据驯服状态调整狼的最大生命值和攻击伤害
     * @param pTamed true表示驯服，false表示未驯服
     */
    public void setTame(boolean pTamed) {
        super.setTame(pTamed);
        if (pTamed) {
            // 驯服时将最大生命值设置为20
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(20.0D);
            this.setHealth(20.0F);
        } else {
            // 未驯服时将最大生命值设置为8
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(8.0D);
        }
        // 设置狼的攻击伤害为4
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(4.0D);
    }

    public boolean canBeLeashed(Player pPlayer) {
        return !this.isAngry() && super.canBeLeashed(pPlayer);
    }

    public @NotNull Vec3 getLeashOffset() {
        return new Vec3(0.0D, (double)(0.6F * this.getEyeHeight()), (double)(this.getBbWidth() * 0.4F));
    }

    public InteractionResult openInventoryScreenByRightClick(Player player, InteractionHand hand) {
        System.out.println("openInventoryScreenByRightClick开始");
        ItemStack stack = player.getItemInHand(hand);
        boolean owner = this.isTame() && isOwnedBy(player);
        InteractionResult type = super.mobInteract(player, hand);
        //打开背包（仅主人）
        if (owner) {
            if (!player.isShiftKeyDown()) {
                this.openCustomInventoryScreen(player);
                this.setOrderedToSit(true);
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }

        } else {
            System.out.println("不是主人");
        }

        //驯服该生物
        if (!isTame() && stack.is(Items.DIAMOND_BLOCK)) {
            this.usePlayerItem(player, hand, stack);
            this.gameEvent(GameEvent.EAT);
            if (!net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, player)) {
                this.tame(player);
                this.level().broadcastEntityEvent(this, (byte) 7);
            } else {
                this.level().broadcastEntityEvent(this, (byte) 6);
            }
            return InteractionResult.SUCCESS;
        }

        //治疗
        if (isTame() && stack.is(Items.COAL) && this.getHealth() < this.getMaxHealth()) {
            this.heal(5);
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }

            this.gameEvent(GameEvent.EAT, this);
            return InteractionResult.SUCCESS;

        }

        //切换坐下跟随游荡
//        InteractionResult interactionresult = stack.interactLivingEntity(player, this, hand);
//        if (interactionresult != InteractionResult.SUCCESS && type != InteractionResult.SUCCESS && isTame() && isOwnedBy(player)) {
//            if (player.isShiftKeyDown()) {
//                this.setCommand(this.getCommand() + 1);
//                if (this.getCommand() == 3) {
//                    this.setCommand(0);
//                }
//                player.displayClientMessage(Component.translatable("entity.cataclysm.all.command_" + this.getCommand(), this.getName()), true);
//                boolean sit = this.getCommand() == 2;
//                if (sit) {
//                    this.setOrderedToSit(true);
//                    return InteractionResult.SUCCESS;
//                } else {
//                    this.setOrderedToSit(false);
//                    return InteractionResult.SUCCESS;
//                }
//            }
//        }
        return type;
    }
    /**===================================繁殖相关方法===============================**/
    @Nullable
    @Override
    public Wolf getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        Wolf wolf = EntityType.WOLF.create(pLevel);
        if (wolf != null) {
            UUID uuid = this.getOwnerUUID();
            if (uuid != null) {
                // 如果当前狼有主人，设置后代狼的主人并标记为驯服
                wolf.setOwnerUUID(uuid);
                wolf.setTame(true);
            }
        }
        return wolf;
    }

    /**
     * 判断狼是否可以与另一个动物交配
     * 狼必须被驯服且对方也是狼才能交配
     * @param pOtherAnimal 另一个动物实体
     * @return true表示可以交配，false表示不可以
     */
    public boolean canMate(Animal pOtherAnimal) {
        if (pOtherAnimal == this) {
            return false;
        } else if (!this.isTame()) {
            return false;
        } else if (!(pOtherAnimal instanceof Wolf)) {
            return false;
        } else {
            Wolf wolf = (Wolf)pOtherAnimal;
            if (!wolf.isTame()) {
                return false;
            } else if (wolf.isInSittingPose()) {
                return false;
            } else {
                return this.isInLove() && wolf.isInLove();
            }
        }
    }

    /**
     * 处理玩家与狼的交互逻辑
     * 包括喂食、染色、驯服等交互操作
     * @param pPlayer 交互的玩家
     * @param pHand 交互的手
     * @return 交互结果
     */
    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        Item item = itemstack.getItem();
        openInventoryScreenByRightClick(pPlayer, pHand);
        if (this.level().isClientSide) {
            // 在客户端判断是否可以进行交互
            boolean flag = this.isOwnedBy(pPlayer) || this.isTame() || itemstack.is(Items.BONE) && !this.isTame() && !this.isAngry();
            return flag ? InteractionResult.CONSUME : InteractionResult.PASS;
        } else if (this.isTame()) {
//            // 如果狼已经被驯服
            if (this.isFood(itemstack) && this.getHealth() < this.getMaxHealth()) {
//                // 如果玩家手中的物品是狼的食物且狼的生命值未满，喂食狼
//                this.heal((float)itemstack.getFoodProperties(this).getNutrition());
//                if (!pPlayer.getAbilities().instabuild) {
//                    // 非创造模式下减少物品数量
//                    itemstack.shrink(1);
//                }
//                // 触发进食游戏事件
//                this.gameEvent(GameEvent.EAT, this);
                return InteractionResult.SUCCESS;
            } else {
//                if (item instanceof DyeItem) {
//                    // 如果玩家手中的物品是染料
//                    DyeItem dyeitem = (DyeItem)item;
//                    if (this.isOwnedBy(pPlayer)) {
//                        // 如果狼是该玩家的宠物
//                        DyeColor dyecolor = dyeitem.getDyeColor();
//                        if (dyecolor != this.getCollarColor()) {
//                            // 如果染料颜色与狼的项圈颜色不同，染色狼的项圈
//                            this.setCollarColor(dyecolor);
//                            if (!pPlayer.getAbilities().instabuild) {
//                                // 非创造模式下减少物品数量
//                                itemstack.shrink(1);
//                            }
//                            return InteractionResult.SUCCESS;
//                        }
//                    }
//                }
                // 调用父类的交互方法
                InteractionResult interactionresult = super.mobInteract(pPlayer, pHand);
                if ((!interactionresult.consumesAction() || this.isBaby()) && this.isOwnedBy(pPlayer)) {
                    // 如果交互未消耗动作或狼是幼崽，且狼是该玩家的宠物，切换狼的坐下状态
                    this.setOrderedToSit(!this.isOrderedToSit());
                    this.jumping = false;
                    this.navigation.stop();
                    this.setTarget((LivingEntity)null);
                    return InteractionResult.SUCCESS;
                } else {
                    return interactionresult;
                }
            }
        } else if (itemstack.is(Items.BONE) && !this.isAngry()) {
            // 如果狼未被驯服，玩家手中拿着骨头且狼未愤怒，尝试驯服狼
            if (!pPlayer.getAbilities().instabuild) {
                // 非创造模式下减少物品数量
                itemstack.shrink(1);
            }
            if (this.random.nextInt(3) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, pPlayer)) {
                // 有三分之一的概率驯服成功
                this.tame(pPlayer);
                this.navigation.stop();
                this.setTarget((LivingEntity)null);
                this.setOrderedToSit(true);
                // 广播狼被驯服的事件
                this.level().broadcastEntityEvent(this, (byte)7);
            } else {
                // 驯服失败，广播失败事件
                this.level().broadcastEntityEvent(this, (byte)6);
            }
            return InteractionResult.SUCCESS;
        } else {
            return super.mobInteract(pPlayer, pHand);
        }
    }

    /**======================战斗相关方法，处理远程与近战攻击的逻辑=========================**/
    /**
     * 处理攻击目标的方法，用于随同主人攻击与反击攻击主人的目标；防止友伤
     * @param pTarget
     * @param pOwner
     * @return
     */
    @Override
    public boolean wantsToAttack(LivingEntity pTarget, LivingEntity pOwner) {
        if (!(pTarget instanceof Creeper) && !(pTarget instanceof AbstractGolem)) {
            if (pTarget instanceof TerracottaGolem thisGolem) {
                return !thisGolem.isTame();
            } else if (pTarget instanceof TamableAnimal tamableAnimal) {
                return !tamableAnimal.isTame() || tamableAnimal.getOwner() != pOwner;
            } else if (pTarget instanceof Player && pOwner instanceof Player && !((Player)pOwner).canHarmPlayer((Player)pTarget)) {
                return false;
            } else if (pTarget instanceof AbstractHorse horse && horse.isTamed()) {
                return false;
            } else {
                return !pTarget.getPersistentData().contains(FRIEND_TO, Tag.TAG_INT_ARRAY);
            }
        } else {
            return false;
        }
    }


    public boolean isAlliedTo(Entity entityIn) {
        if (this.isTame()) {
            LivingEntity livingentity = this.getOwner();
            if (entityIn == livingentity) {
                return true;
            }
            if (entityIn instanceof TamableAnimal) {
                return ((TamableAnimal) entityIn).isOwnedBy(livingentity);
            }
            if (livingentity != null) {
                return livingentity.isAlliedTo(entityIn);
            }
        }

        return super.isAlliedTo(entityIn);
    }

    @Override
    public void performRangedAttack(LivingEntity pTarget, float pVelocity) {

    }

    /**
     * 处理狼受到伤害的逻辑
     * 当狼受到伤害时，取消坐下命令，并根据伤害来源调整伤害值
     * @param pSource 伤害来源
     * @param pAmount 伤害量
     * @return true表示伤害处理成功，false表示免疫该伤害
     */
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.isInvulnerableTo(pSource)) {
            return false;
        } else {
            Entity entity = pSource.getEntity();
            if (!this.level().isClientSide) {
                this.setOrderedToSit(false);
            }

            if (entity != null && !(entity instanceof Player) && !(entity instanceof AbstractArrow)) {
                pAmount = (pAmount + 1.0F) / 2.0F;
            }

            return super.hurt(pSource, pAmount);
        }
    }

    /**
     * 狼攻击目标的逻辑
     * 狼对目标造成伤害并应用附魔效果
     * @param pEntity 攻击目标
     * @return true表示攻击成功，false表示攻击失败
     */
    public boolean doHurtTarget(Entity pEntity) {
        boolean flag = pEntity.hurt(this.damageSources().mobAttack(this), (float)((int)this.getAttributeValue(Attributes.ATTACK_DAMAGE)));
        if (flag) {
            // 攻击成功时应用附魔效果
            this.doEnchantDamageEffects(this, pEntity);
        }
        return flag;
    }

    public SimpleContainer getInventory() {
        return this.inventory;
    }

    /**=========================内部类自定义AI，自定义攻击方式等等行为=========================**/

    class TerracottaGolemPanicGoal extends PanicGoal {
        public TerracottaGolemPanicGoal(double pSpeedModifier) {
            super(TerracottaGolem.this, pSpeedModifier);
        }

        protected boolean shouldPanic() {
            return this.mob.isFreezing() || this.mob.isOnFire();
        }
    }
}
