package com.renyigesai.immortalers_delight.event;

import com.mojang.datafixers.util.Pair;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightFoodProperties;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightParticleTypes;
import com.renyigesai.immortalers_delight.util.task.TimekeepingTask;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;
import java.util.Optional;

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
                        if (DifficultyModeHelper.isPowerBattleMode()) {
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
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntityHurt(LivingHurtEvent evt) {
        if (evt.isCanceled() || evt.getSource().is(DamageTypeTags.BYPASSES_RESISTANCE)) {
            return;
        }
        boolean isPowerful = DifficultyModeHelper.isPowerBattleMode();
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
                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                    BlockPos pos = player.getOnPos().above();
                    vectorwing.farmersdelight.common.utility.ItemUtils.spawnItemEntity(level,new ItemStack(Items.GLASS_BOTTLE),
                            pos.getX() + 0.5,pos.getY() + 0.5,pos.getZ() + 0.5,0.0,0.0,0.0);
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
                    spawnParticle(piglin.level(), piglin.blockPosition());
                }
                if (TimekeepingTask.getImmortalTickTime() == 4000 + piglin.getPersistentData().getLong(DELETE_PIGLIN)) {
                    for (int i = 0; i < 2; i++) {
                        spawnParticle(piglin.level(), piglin.blockPosition());
                    }
                    piglin.level().playLocalSound(piglin.getX(),piglin.getY(),piglin.getZ(), SoundEvents.PIGLIN_CELEBRATE, SoundSource.HOSTILE,0.8F,0.8F,false);
                    piglin.discard();
                }
            }
        }
    }

    private static void spawnParticle(Level level, BlockPos pPos) {
        if (level instanceof ServerLevel serverLevel) {
            Vec3 center = new Vec3(pPos.getX() + 0.5, pPos.getY() + 0.5, pPos.getZ() + 0.5);
            double radius = 0.32;
            for (int i = 0; i < 3; i++) {
                double angle = 2 * Math.PI * Math.random();
                double r = radius * Math.sqrt(Math.random());
                double x = center.x + r * Math.cos(angle);
                double z = center.z + r * Math.sin(angle);
                double y = center.y;
                serverLevel.sendParticles(
                        ParticleTypes.HEART, x, y, z, 1, 0, 0, 0, 0.025
                );
            }
        }
    }
}
