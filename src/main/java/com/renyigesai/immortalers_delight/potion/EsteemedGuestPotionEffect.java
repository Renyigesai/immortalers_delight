package com.renyigesai.immortalers_delight.potion;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.event.DifficultyModeHelper;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightEntities;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.monster.piglin.*;
import net.minecraft.world.entity.monster.warden.AngerLevel;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Mod.EventBusSubscriber
public class EsteemedGuestPotionEffect {
    public static final String FRIEND_TO = ImmortalersDelightMod.MODID + "_esteemed_guest_friend_to";
    public static final UUID ESTEEMED_GUEST_FOLLOW_MOD = UUID.fromString("58e46d04-d6a8-4397-a1a9-2f09c919b388");
    public static final UUID ESTEEMED_GUEST_HEALTH_MOD = UUID.fromString("2f945b07-f673-4c32-abbb-eaa86f15787f");
    @SubscribeEvent
    public static void PiglinIgnore(LivingEvent.LivingTickEvent event) {

        if (!event.getEntity().level().isClientSide() && event.getEntity() instanceof AbstractPiglin piglin) {
            if (piglin.getTarget() != null) {
                boolean isPowerful = DifficultyModeHelper.isPowerBattleMode();
                boolean flag = isPowerful || piglin.getLastHurtByMob() != piglin.getTarget();
                boolean flag2 = piglin.getTarget().hasEffect(ImmortalersDelightMobEffect.ESTEEMED_GUEST.get());
                if (piglin.getPersistentData().contains(FRIEND_TO,Tag.TAG_INT_ARRAY)) {
                    if (piglin.getPersistentData().getUUID(FRIEND_TO).toString().equals(piglin.getTarget().getUUID().toString())) {
                        flag2 = true;
                    }
                }
                if (flag && flag2) {
                    piglin.setTarget(null);
                    piglin.setAggressive(false);
                    piglin.getBrain().setMemory(MemoryModuleType.ANGRY_AT, Optional.empty());
                    piglin.getBrain().setMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, Optional.empty());
                    piglin.getBrain().setMemory(MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD, Optional.empty());
                }
            }
        }
    }

    @SubscribeEvent
    public static void callPiglinFriend(LivingHurtEvent evt) {
        if (evt.isCanceled() || evt.getEntity().level().isClientSide) {
            return;
        }
        boolean isPowerful = DifficultyModeHelper.isPowerBattleMode();
        LivingEntity hurtOne = evt.getEntity();
        if (hurtOne.hasEffect(ImmortalersDelightMobEffect.ESTEEMED_GUEST.get()) && evt.getSource().getEntity() instanceof LivingEntity attacker) {
            int lv = hurtOne.hasEffect(ImmortalersDelightMobEffect.ESTEEMED_GUEST.get())? Objects.requireNonNull(hurtOne.getEffect(ImmortalersDelightMobEffect.ESTEEMED_GUEST.get())).getAmplifier() :0;
            //lv++;
            float workHealth = (hurtOne.getMaxHealth() * lv) / (2 * (lv + 1)) > 3 * (lv + 1) ? 3 * (lv + 1) : (hurtOne.getMaxHealth() * lv / (2 * (lv + 1)));
            if (isPowerful) {
                if (hurtOne.getHealth() - evt.getAmount() < workHealth) {
                    spawnPiglinFriends(hurtOne.level(),attacker,hurtOne,lv);
                    hurtOne.removeEffect(ImmortalersDelightMobEffect.ESTEEMED_GUEST.get());
                }
            } else if (hurtOne.getHealth() < workHealth) {
                spawnPiglinFriends(hurtOne.level(),attacker,hurtOne,lv);
                hurtOne.removeEffect(ImmortalersDelightMobEffect.ESTEEMED_GUEST.get());
            }
        }
    }

    private static void spawnPiglinFriends(Level level, LivingEntity target, LivingEntity friend, int lv) {
        if (!level.isClientSide()) {
            PiglinBrute piglin = EntityType.PIGLIN_BRUTE.create(level);
            if (piglin != null) {
                boolean isPowerful = DifficultyModeHelper.isPowerBattleMode();
                piglin.moveTo(target.getX(), target.getY(), target.getZ(), 0.0F, 0.0F);
                piglin.setPersistenceRequired();
                AttributeInstance att = piglin.getAttribute(Attributes.FOLLOW_RANGE);
                if (att != null && att.getModifier(ESTEEMED_GUEST_FOLLOW_MOD) == null)
                    att.addTransientModifier(
                            new AttributeModifier(
                                    ESTEEMED_GUEST_FOLLOW_MOD,
                                    "esteemed_guest_follow_mod",
                                    64,
                                    AttributeModifier.Operation.ADDITION
                            )
                    );
                att = piglin.getAttribute(Attributes.MAX_HEALTH);
                if (att != null && att.getModifier(ESTEEMED_GUEST_HEALTH_MOD) == null)
                    att.addPermanentModifier(
                            new AttributeModifier(
                                    ESTEEMED_GUEST_HEALTH_MOD,
                                    "esteemed_guest_health_mod",
                                    lv,
                                    AttributeModifier.Operation.MULTIPLY_BASE)
                    );
                piglin.addEffect(new MobEffectInstance(MobEffects.HEAL, lv,8));
                piglin.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, -1,lv > 255 ? 255 : lv, false, false));
                if (isPowerful) {
                    piglin.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.NETHERITE_AXE));
                    piglin.setItemSlot(EquipmentSlot.OFFHAND, createCustomShield());
                    piglin.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.NETHERITE_HELMET));
                    piglin.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.NETHERITE_CHESTPLATE));
                    piglin.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.NETHERITE_LEGGINGS));
                    piglin.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.NETHERITE_BOOTS));
                } else {
                    piglin.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_AXE));
                    piglin.setItemSlot(EquipmentSlot.OFFHAND, createCustomShield());
                }
                CompoundTag tag = piglin.getPersistentData();
                if (tag.get(FRIEND_TO) == null) tag.putUUID(FRIEND_TO, friend.getUUID());
                piglin.setImmuneToZombification(true);
                level.addFreshEntity(piglin);
                piglin.setTarget(target);
                piglin.getBrain().setMemoryWithExpiry(MemoryModuleType.ANGRY_AT, target.getUUID(), 600);
                piglin.getBrain().setMemoryWithExpiry(MemoryModuleType.ATTACK_TARGET, target, 600);

                if (target instanceof Mob mob) {
                    AttributeInstance att1 = mob.getAttribute(Attributes.FOLLOW_RANGE);
                    if (att1 != null && att1.getModifier(ESTEEMED_GUEST_FOLLOW_MOD) == null)
                        att1.addTransientModifier(
                                new AttributeModifier(
                                        ESTEEMED_GUEST_FOLLOW_MOD,
                                        "esteemed_guest_follow_mod",
                                        64,
                                        AttributeModifier.Operation.ADDITION
                                )
                        );
                    mob.setTarget(piglin);
                    mob.getBrain().setMemoryWithExpiry(MemoryModuleType.ANGRY_AT, piglin.getUUID(), 600);
                    mob.getBrain().setMemoryWithExpiry(MemoryModuleType.ATTACK_TARGET, piglin, 600);
                    if (mob instanceof Warden warden) {
                        warden.increaseAngerAt(target, AngerLevel.ANGRY.getMinimumAnger() + 20, false);
                        warden.setAttackTarget(target);
                    }
                }
            }
            for (int i = 0; i < 2; i++) {
                if (i % 2 == 0) spawnParticle(friend.level(),friend.blockPosition());
                if (i % 2 == 1) spawnParticle(target.level(),target.blockPosition());
            }
        }
    }

    /**
     * 创建带有指定旗帜图案的盾牌物品栈
     * @return 带有自定义旗帜图案的盾牌
     */
    public static ItemStack createCustomShield() {
        // 1. 创建盾牌物品栈
        ItemStack shield = new ItemStack(Items.SHIELD);

        // 2. 构建外层NBT标签
        CompoundTag shieldTag = new CompoundTag();

        // 3. 构建BlockEntityTag（旗帜数据）
        CompoundTag blockEntityTag = new CompoundTag();
        blockEntityTag.putInt("Base", 9); // 基础颜色（9对应蓝色）

        // 4. 构建图案列表（Patterns）
        ListTag patterns = new ListTag();

        // 添加每个图案（Color：颜色值；Pattern：图案ID）
        // 注意：图案ID需使用BannerPattern的注册表名称（如"gru"、"bs"等）
        addPattern(patterns, 11, "gru");   // 颜色11（浅灰），图案"gru"
        addPattern(patterns, 5, "bs");    // 颜色5（绿色），图案"bs"
        addPattern(patterns, 13, "bts");  // 颜色13（棕色），图案"bts"
        addPattern(patterns, 15, "flo");  // 颜色15（白色），图案"flo"
        addPattern(patterns, 15, "moj");  // 颜色15（白色），图案"moj"
        addPattern(patterns, 15, "glb");  // 颜色15（白色），图案"glb"
        addPattern(patterns, 3, "mc");    // 颜色3（青色），图案"mc"

        // 将图案列表添加到BlockEntityTag
        blockEntityTag.put("Patterns", patterns);
        // 指定BlockEntity类型为旗帜（必须设置，否则图案不生效）
        blockEntityTag.putString("id", "minecraft:banner");

        // 5. 将BlockEntityTag和耐久值添加到外层标签
        shieldTag.put("BlockEntityTag", blockEntityTag);
        shieldTag.putInt("Damage", 0); // 耐久值（0为满耐久）

        // 6. 将NBT标签设置到盾牌物品栈
        shield.setTag(shieldTag);

        return shield;
    }

    /**
     * 向图案列表中添加一个图案
     * @param patterns 图案列表（ListTag）
     * @param color 颜色值（0-15，对应羊毛颜色）
     * @param patternId 图案ID（如"gru"、"bs"，需与BannerPattern一致）
     */
    private static void addPattern(ListTag patterns, int color, String patternId) {
        CompoundTag patternTag = new CompoundTag();
        patternTag.putInt("Color", color);
        patternTag.putString("Pattern", patternId);
        patterns.add(patternTag);
    }

    private static void spawnParticle(Level level, BlockPos pPos) {
        if (level instanceof ServerLevel serverLevel) {
            Vec3 center = new Vec3(pPos.getX() + 0.5, pPos.getY() + 0.5, pPos.getZ() + 0.5);
            double radius = 1.5;
            for (int i = 0; i < 16; i++) {
                double angle = 2 * Math.PI * Math.random();
                double r = radius * Math.sqrt(Math.random());
                double x = center.x + r * Math.cos(angle);
                double z = center.z + r * Math.sin(angle);
                double y = center.y + angle * 0.5;
                serverLevel.sendParticles(
                        ParticleTypes.ANGRY_VILLAGER, x, y, z, 1, 0, 0, 0, 0.025
                );
            }
        }
    }
}
