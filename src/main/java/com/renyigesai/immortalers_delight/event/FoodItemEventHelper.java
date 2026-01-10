package com.renyigesai.immortalers_delight.event;

import com.mojang.datafixers.util.Pair;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.client.particle.ShockWaveParticleOption;
import com.renyigesai.immortalers_delight.entities.living.illager_archaeological_team.Scavenger;
import com.renyigesai.immortalers_delight.entities.projectile.KiBlastEntity;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightFoodProperties;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.potion.GasPoisonMobEffect;
import com.renyigesai.immortalers_delight.potion.GasPoisonPotionEffect;
import com.renyigesai.immortalers_delight.potion.immortaleffects.DeathlessEffect;
import com.renyigesai.immortalers_delight.potion.immortaleffects.FreezeEffect;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import com.renyigesai.immortalers_delight.util.task.TimekeepingTask;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vectorwing.farmersdelight.common.item.DogFoodItem;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModParticleTypes;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.common.utility.MathUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber
public class FoodItemEventHelper {
    @SubscribeEvent
    public static void onUseItemFinish(LivingEntityUseItemEvent.Finish event) {
        if (event != null && event.getEntity() != null) {
            ItemStack stack = event.getItem();
            Entity entity = event.getEntity();
            if (entity instanceof LivingEntity livingEntity && !livingEntity.level().isClientSide()) {
                if (stack.getItem().isEdible()) {
                    //大红包子的隐藏幸运效果
                    if (stack.getFoodProperties(livingEntity) == ImmortalersDelightFoodProperties.RED_STUFFED_BUN) {
                        if (DifficultyModeUtil.isPowerBattleMode()) {
                            if (livingEntity.getRandom().nextInt(3) == 0) {
                                livingEntity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 6000,3));
                                livingEntity.addEffect(new MobEffectInstance(MobEffects.LUCK, 2700));
                            }
                        }else if (livingEntity.getRandom().nextInt(3) == 0) {
                            livingEntity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 6000,1));
                            livingEntity.addEffect(new MobEffectInstance(MobEffects.LUCK, 600));
                        }
                    }
                    //棱珠牛奶的解除buff
                    if (stack.getFoodProperties(livingEntity) == ImmortalersDelightFoodProperties.PEARLIP_BUBBLE_MILK) {
                        livingEntity.curePotionEffects(new ItemStack(Items.MILK_BUCKET));
                        for (Pair<MobEffectInstance, Float> pair : Objects.requireNonNull(stack.getFoodProperties(livingEntity)).getEffects()) {
                            livingEntity.addEffect(new MobEffectInstance(pair.getFirst()));
                        }
                    }
                    //红美玲的气功波
                    if (stack.getItem() == ImmortalersDelightItems.HONE_MEI_LING.get()) {
                        shootKiBlast(livingEntity);
                    }
                    //瓦斯麦汤的buff
                    if (stack.getItem() == ImmortalersDelightItems.KWAT_SOUP.get()) {
                        boolean isPowerBattleMode = DifficultyModeUtil.isPowerBattleMode();
                        float damage = isPowerBattleMode ? Math.min(18.0f,livingEntity.getMaxHealth()*0.9f) : Math.max(18.0f,livingEntity.getMaxHealth()*0.9f);
                        DamageSource source = isPowerBattleMode ? (livingEntity instanceof Player player ? player.damageSources().playerAttack(player) : livingEntity.damageSources().mobAttack(livingEntity)) : GasPoisonMobEffect.getDamageSource(livingEntity,null);
                        livingEntity.hurt(source, damage);
                        livingEntity.addEffect(new MobEffectInstance(MobEffects.SATURATION, 400));
                    }
                    //冰瓦斯麦汤的buff
                    if (stack.getItem() == ImmortalersDelightItems.ICED_KWAT_SOUP.get()) {
                        MobEffectInstance incandence = livingEntity.getEffect(ImmortalersDelightMobEffect.INCANDESCENCE.get());
                        if (incandence != null) {
                            int lv = incandence.getAmplifier();
                            int duration = incandence.getDuration();
                            if (lv > 0) {
                                lv--;
                                duration *= 2.5f;
                            }
                            livingEntity.addEffect(new MobEffectInstance(ImmortalersDelightMobEffect.COOL.get(), duration, lv));
                            FreezeEffect.applyImmortalEffect(livingEntity, 200, 0);
                        }
                    }
                    //玉黍硬糖吃完后2s无敌帧
                    if (stack.getItem() == ImmortalersDelightItems.EVOLUTCORN_HARD_CANDY.get()) {
                        DeathlessEffect.applyImmortalEffect(livingEntity, 40, 0);
                    }
                }
            }
        }
    }

    public static void shootKiBlast(LivingEntity attacker) {
        LivingEntity livingEntity = attacker;
        if (livingEntity.level() instanceof ServerLevel serverLevel) {
            List<LivingEntity> list = livingEntity.level().getEntitiesOfClass(LivingEntity.class, new AABB(livingEntity.getOnPos()).inflate(3.0D, 3.0D, 3.0D));
            if (!list.isEmpty()) {
                for (LivingEntity hurtOne : list) {
                    if (hurtOne != livingEntity && !livingEntity.isAlliedTo(hurtOne) && !hurtOne.isAlliedTo(livingEntity)){
                        float damage = (float) hurtOne.getAttributeValue(Attributes.ATTACK_DAMAGE);
                        damage = Math.min(damage, 5.0f) * (DifficultyModeUtil.isPowerBattleMode() ? 3.85f : 2.3f);
                        hurtOne.hurt(hurtOne.level().damageSources().mobAttack(livingEntity), damage);
                        double knockBackResistance = hurtOne.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
                        if (knockBackResistance < 1.0D) {
                            Vec3 directionVector = hurtOne.getPosition(1.0f).subtract(livingEntity.getPosition(1.0f));
                            double distance = livingEntity.distanceToSqr(hurtOne);
                            hurtOne.setDeltaMovement(hurtOne.getDeltaMovement().add(
                                    directionVector.x / (1 + 0.2 * distance) * (1 - knockBackResistance), 0.5D, directionVector.z / (1 + 0.2 * distance) * (1 - knockBackResistance)));
                            hurtOne.setYRot(hurtOne.yHeadRot);
                            hurtOne.setOnGround(false);
                            hurtOne.hasImpulse = true;
                        }
                    }
                }
            }
            spawnShriekParticle(serverLevel, livingEntity.getX(), livingEntity.getY() + livingEntity.getEyeHeight() * 0.5f, livingEntity.getZ(),1);
        }
        // 1. 获取玩家的视线方向向量
        Vec3 lookDirection = livingEntity.getViewVector(1.0F);
        // 2. 后续逻辑：如沿该方向生成投射物（示例）
        double spawnX = livingEntity.getX() + lookDirection.x;
        double spawnY = livingEntity.getEyeY() + lookDirection.y;
        double spawnZ = livingEntity.getZ() + lookDirection.z;
        KiBlastEntity fireball = new KiBlastEntity(livingEntity.level(), livingEntity,
                lookDirection.x * 0.5D, lookDirection.y * 0.5D,lookDirection.z * 0.5D);
        if (DifficultyModeUtil.isPowerBattleMode()) fireball.setDangerous(true);
        fireball.setPos(spawnX, spawnY, spawnZ);
        livingEntity.level().addFreshEntity(fireball);
    }
    public static void spawnShriekParticle(ServerLevel serverLevel, double x, double y, double z, int delay) {
        // 1. 创建粒子参数（封装delay）
        ShockWaveParticleOption particleOption = new ShockWaveParticleOption(delay);

        // 2. 调用带ParticleOptions的sendParticles重载方法
        serverLevel.sendParticles(
                particleOption,  // 粒子参数（含SHRIEK类型+delay）
                x, y, z,         // 生成位置
                1,               // 生成数量
                0.0D, 0.0D, 0.0D,// 位置无偏移
                0.0D             // 速度（无作用）
        );
    }

    @SubscribeEvent
    public static void onEntityHurt(LivingHurtEvent evt) {
        if (evt.isCanceled() || evt.getSource().is(DamageTypeTags.BYPASSES_RESISTANCE)) {
            return;
        }
        boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
        LivingEntity hurtOne = evt.getEntity();
        LivingEntity attacker = null;
        if (evt.getSource().getEntity() instanceof LivingEntity livingEntity){
            attacker = livingEntity;
        }

        if (!hurtOne.level().isClientSide) {
            if (hurtOne.getUseItem().getItem() == ImmortalersDelightItems.EVOLUTCORN_HARD_CANDY.get()) {
                if (hurtOne.getTicksUsingItem() > (isPowerful ? 16 : 32)) {
                    if (isPowerful) {
                        float buffer = 0.4f - (0.6F * hurtOne.getTicksUsingItem() / hurtOne.getUseItem().getUseDuration());
                        if (buffer > 0) {
                            evt.setAmount(evt.getAmount() * buffer);
                        } else {
                            hurtOne.heal(evt.getAmount() * buffer * (-1));
                            evt.setAmount(0);
                            evt.setCanceled(true);
                        }
                    } else evt.setAmount(evt.getAmount() * 0.4f);
                }
            }
        }
    }
    public static final String DELETE_PIGLIN = ImmortalersDelightMod.MODID + "_delete_piglin";
    @SubscribeEvent
    public static void onPlayerFeed(PlayerInteractEvent.EntityInteractSpecific event) {
        if (event.getEntity() != null && event.getTarget() instanceof LivingEntity target){
            Player player = event.getEntity();
            Level level = player.level();
            ItemStack itemStack = event.getItemStack();
            if (!(level instanceof ServerLevel serverLevel) || itemStack == ItemStack.EMPTY) return;
            //喂食伏特加
            if (itemStack.getItem() == ImmortalersDelightItems.CLEAR_WATER_VODKA.get()) {
                addInebriatedEffect(itemStack,serverLevel,target);
                if (itemStack.getCraftingRemainingItem() != ItemStack.EMPTY && !player.isCreative()) {
                    player.addItem(itemStack.getCraftingRemainingItem());
                    itemStack.shrink(1);
                }
            }
            //金瓦斯麦面包
            if (itemStack.getItem() == ImmortalersDelightItems.GOLDEN_KWAT_TOAST.get()) {
                if (target instanceof PiglinBrute piglin) {
                    player.addEffect(new MobEffectInstance(ImmortalersDelightMobEffect.ESTEEMED_GUEST.get(), 48000,1));
                    piglin.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(ImmortalersDelightItems.GOLDEN_KWAT_TOAST.get()));
                    CompoundTag tag = piglin.getPersistentData();
                    if (tag.get(DELETE_PIGLIN) == null) tag.putLong(DELETE_PIGLIN, TimekeepingTask.getImmortalTickTime());
                    if (!player.getAbilities().instabuild) {
                        itemStack.shrink(1);
                    }
                }
            }
            //诡异香肠喂狗
//            if (itemStack.getItem() == ImmortalersDelightItems.BIZARRE_SAUSAGE.get()) {
//                if (target.getType().is(ModTags.DOG_FOOD_USERS)) {
//                    boolean isTameable = target instanceof TamableAnimal;
//                    if (target.isAlive() && (!isTameable || ((TamableAnimal)target).isTame()) && itemStack.getItem().equals(ModItems.DOG_FOOD.get())) {
//                        target.setHealth(target.getMaxHealth());
//
//                        for (Pair<MobEffectInstance, Float> pair : Objects.requireNonNull(itemStack.getFoodProperties(player)).getEffects()) {
//                            if (pair.getFirst().getEffect().isBeneficial()) target.addEffect(new MobEffectInstance(pair.getFirst()));
//                            else target.addEffect(new MobEffectInstance(ImmortalersDelightMobEffect.BURN_THE_BOATS.get(), 12000, 3));
//                        }
//
//                        target.level().playSound((Player)null, target.blockPosition(), SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 0.8F, 0.8F);
//
//                        for(int i = 0; i < 5; ++i) {
//                            double xSpeed = MathUtils.RAND.nextGaussian() * 0.02;
//                            double ySpeed = MathUtils.RAND.nextGaussian() * 0.02;
//                            double zSpeed = MathUtils.RAND.nextGaussian() * 0.02;
//                            target.level().addParticle(ModParticleTypes.STAR.get(),target.getRandomX(1.0), target.getRandomY() + 0.5, target.getRandomZ(1.0), xSpeed, ySpeed, zSpeed);
//                        }
//
//                        if (itemStack.getCraftingRemainingItem() != ItemStack.EMPTY && !player.isCreative()) {
//                            player.addItem(itemStack.getCraftingRemainingItem());
//                            itemStack.shrink(1);
//                        }
//
//                        event.setCancellationResult(InteractionResult.SUCCESS);
//                        event.setCanceled(true);
//                    }
//                }
//            }
        }
    }

    private static void addInebriatedEffect(ItemStack stack, Level level, LivingEntity livingEntity) {
        // 从物品栈中获取具体的物品
        Item item = stack.getItem();
        // 检查该物品是否为可食用物品
        if (item.isEdible()) {
            // 遍历物品的食物属性中定义的所有药水效果及其概率
            for (Pair<MobEffectInstance, Float> pair : stack.getFoodProperties(livingEntity).getEffects()) {
                // 条件判断：
                // 1. 当前不是客户端，因为药水效果的添加通常在服务器端处理，以保证数据一致性。
                // 2. 药水效果实例不为空，确保有有效的药水效果。
                // 3. 药水效果为我们指定的酒精效果
                if (!level.isClientSide && pair.getFirst() != null) {
                    if (pair.getFirst().getEffect() == ImmortalersDelightMobEffect.INEBRIATED.get()) {
                        // 创建一个新的药水效果实例，使用原有的药水效果实例作为模板。
                        // 然后将该药水效果添加到食用物品的实体上。
                        int oldLv = livingEntity.hasEffect(ImmortalersDelightMobEffect.INEBRIATED.get()) ? livingEntity.getEffect(ImmortalersDelightMobEffect.INEBRIATED.get()).getAmplifier() : 0;
                        int oldTime = livingEntity.hasEffect(ImmortalersDelightMobEffect.INEBRIATED.get()) ? livingEntity.getEffect(ImmortalersDelightMobEffect.INEBRIATED.get()).getDuration() : 0;
                        int time = pair.getFirst().getDuration() + oldTime;
                        int lv = pair.getFirst().getAmplifier() > oldLv ? pair.getFirst().getAmplifier() : oldLv;
                        livingEntity.addEffect(new MobEffectInstance(pair.getFirst().getEffect(),time,lv));
                        //InebriatedEffect.applyImmortalEffect(livingEntity,(double) time / 20 + 0.1,lv);
                    }
                    else  livingEntity.addEffect(pair.getFirst());
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingTickEvent event) {

        if (!event.getEntity().level().isClientSide()) {
            if (event.getEntity() instanceof AbstractPiglin piglin && piglin.getPersistentData().contains(DELETE_PIGLIN,Tag.TAG_LONG)) {
                if (TimekeepingTask.getImmortalTickTime() % 500 <= 50) {
                    spawnParticle(piglin.level(), piglin.blockPosition(),0);
                }
                if (TimekeepingTask.getImmortalTickTime() >= 4000 + piglin.getPersistentData().getLong(DELETE_PIGLIN)) {
                    for (int i = 0; i < 2; i++) {
                        spawnParticle(piglin.level(), piglin.blockPosition(),1);
                    }
                    piglin.level().playLocalSound(piglin.getX(),piglin.getY(),piglin.getZ(), SoundEvents.PIGLIN_CELEBRATE, SoundSource.HOSTILE,0.8F,0.8F,false);
                    piglin.discard();
                }
            }
        }
    }

    private static void spawnParticle(Level level, BlockPos pPos,int type) {
        if (level instanceof ServerLevel serverLevel) {
            Vec3 center = new Vec3(pPos.getX() + 0.5, pPos.getY() + 0.5, pPos.getZ() + 0.5);
            double radius = 0.32 + type;
            for (int i = 0; i < 3 + 12 * type; i++) {
                double angle = 2 * Math.PI * Math.random();
                double r = radius * Math.sqrt(Math.random());
                double x = center.x + r * Math.cos(angle);
                double z = center.z + r * Math.sin(angle);
                double y = center.y;
                if (type == 0) {
                    serverLevel.sendParticles(
                            ParticleTypes.HEART, x, y, z, 1, 0, 0, 0, 0.025
                    );
                }
                if (type == 1) {
                    serverLevel.sendParticles(
                            ParticleTypes.WITCH, x, y, z, 1, 0, 0, 0, 0.025
                    );
                }
            }
        }
    }
}
