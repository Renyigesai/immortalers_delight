package com.renyigesai.immortalers_delight.potion;
import net.neoforged.fml.common.EventBusSubscriber;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import com.renyigesai.immortalers_delight.util.LivingDamageUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.monster.warden.AngerLevel;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;

import java.util.Optional;

import net.minecraft.resources.ResourceLocation;
public class EsteemedGuestMobEffect extends BaseMobEffect {
    public static final String FRIEND_TO = ImmortalersDelightMod.MODID + "_esteemed_guest_friend_to";
    public static final ResourceLocation ESTEEMED_GUEST_FOLLOW_MOD_ID = ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "esteemed_guest_follow");
    public static final ResourceLocation ESTEEMED_GUEST_HEALTH_MOD_ID = ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "esteemed_guest_health");
    public EsteemedGuestMobEffect() {
        super(MobEffectCategory.NEUTRAL, -39424);
    }

    @Override
    public boolean isDurationEffectTickInControl(int pDuration, int pAmplifier) {
            return true;
    }


    @EventBusSubscriber(
            modid = ImmortalersDelightMod.MODID)
    public static class EsteemedGuestPotionEffect {
        private static final int[] LEVEL_UP = new int[] {5, 10, 15, 18, 20, 22, 23, 24};
        @SubscribeEvent
        public static void PiglinIgnore(EntityTickEvent.Post event) {

            if (!event.getEntity().level().isClientSide() && event.getEntity() instanceof AbstractPiglin piglin) {
                if (piglin.getTarget() != null) {
                    boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
                    boolean flag = isPowerful || piglin.getLastHurtByMob() != piglin.getTarget();
                    boolean flag2 = piglin.getTarget().hasEffect(ImmortalersDelightMobEffect.ESTEEMED_GUEST);
                    if (piglin.getPersistentData().contains(FRIEND_TO, Tag.TAG_INT_ARRAY)) {
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
        public static void callPiglinFriend(LivingDamageEvent.Pre evt) {
            if (evt.getEntity().level().isClientSide()) {
                return;
            }
            boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
            LivingEntity hurtOne = evt.getEntity();
            MobEffectInstance esteemedGuest = hurtOne.getEffect(ImmortalersDelightMobEffect.ESTEEMED_GUEST);
            if (esteemedGuest != null && esteemedGuest.getEffect().value() instanceof BaseMobEffect baseMobEffect && evt.getSource().getEntity() instanceof LivingEntity attacker) {
                int lv = baseMobEffect.getTruthUsingAmplifier(esteemedGuest.getAmplifier());
                //lv++;
                float workHealth = (hurtOne.getMaxHealth() * lv) / (2 * (lv + 1)) > 3 * (lv + 1) ? 3 * (lv + 1) : (hurtOne.getMaxHealth() * lv / (2 * (lv + 1)));
                if (isPowerful) {
                    if (hurtOne.getHealth() - evt.getNewDamage() < workHealth) {
                        spawnPiglinFriends(hurtOne.level(),attacker,hurtOne,lv,esteemedGuest.getAmplifier());
                        hurtOne.removeEffect(ImmortalersDelightMobEffect.ESTEEMED_GUEST);
                    }
                } else if (hurtOne.getHealth() < workHealth) {
                    spawnPiglinFriends(hurtOne.level(),attacker,hurtOne,lv,esteemedGuest.getAmplifier());
                    hurtOne.removeEffect(ImmortalersDelightMobEffect.ESTEEMED_GUEST);
                }
            }
        }

        private static void spawnPiglinFriends(Level level, LivingEntity target, LivingEntity friend, int lv, int amplifier) {
            if (!level.isClientSide()) {
                PiglinBrute piglin = EntityType.PIGLIN_BRUTE.create(level);
                if (piglin != null) {
                    boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
                    piglin.moveTo(target.getX(), target.getY(), target.getZ(), 0.0F, 0.0F);
                    piglin.setPersistenceRequired();
                    AttributeInstance att = piglin.getAttribute(Attributes.FOLLOW_RANGE);
                    if (att != null && att.getModifier(ESTEEMED_GUEST_FOLLOW_MOD_ID) == null)
                        att.addTransientModifier(
                                new AttributeModifier(
                                        ESTEEMED_GUEST_FOLLOW_MOD_ID,
                                        64,
                                        AttributeModifier.Operation.ADD_VALUE
                                )
                        );
                    att = piglin.getAttribute(Attributes.MAX_HEALTH);
                    if (att != null && att.getModifier(ESTEEMED_GUEST_HEALTH_MOD_ID) == null)
                        att.addPermanentModifier(
                                new AttributeModifier(
                                        ESTEEMED_GUEST_HEALTH_MOD_ID,
                                        lv,
                                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                        );
                    piglin.addEffect(new MobEffectInstance(MobEffects.HEAL, 1, Math.min(amplifier, 127)));
                    piglin.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, -1, amplifier, false, false));
                    var enchReg = piglin.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
                    if (isPowerful) {
                        int enchLv = lv + LEVEL_UP[(LivingDamageUtil.getPowerOfTwo(lv + 1) >= LEVEL_UP.length ? LEVEL_UP.length - 1 : LivingDamageUtil.getPowerOfTwo(lv + 1))];
                        ItemStack mainHand = new ItemStack(Items.NETHERITE_AXE);
                        mainHand.enchant(enchReg.getOrThrow(Enchantments.SHARPNESS), lv + 1);
                        piglin.setItemSlot(EquipmentSlot.MAINHAND, mainHand);
                        ItemStack offHand = createCustomShield();
                        offHand.enchant(enchReg.getOrThrow(Enchantments.UNBREAKING), Math.min(enchLv, 3));
                        piglin.setItemSlot(EquipmentSlot.OFFHAND, offHand);
                        ItemStack head = new ItemStack(Items.NETHERITE_HELMET);
                        head.enchant(enchReg.getOrThrow(Enchantments.PROTECTION), Math.min(enchLv, 4));
                        piglin.setItemSlot(EquipmentSlot.HEAD, head);
                        ItemStack chest = new ItemStack(Items.NETHERITE_CHESTPLATE);
                        chest.enchant(enchReg.getOrThrow(Enchantments.PROTECTION), Math.min(enchLv, 4));
                        piglin.setItemSlot(EquipmentSlot.CHEST, chest);
                        ItemStack legs = new ItemStack(Items.NETHERITE_LEGGINGS);
                        legs.enchant(enchReg.getOrThrow(Enchantments.PROTECTION), Math.min(enchLv, 4));
                        piglin.setItemSlot(EquipmentSlot.LEGS, legs);
                        ItemStack boots = new ItemStack(Items.NETHERITE_BOOTS);
                        boots.enchant(enchReg.getOrThrow(Enchantments.PROTECTION), Math.min(enchLv, 4));
                        piglin.setItemSlot(EquipmentSlot.FEET, boots);
                    } else {
                        int enchLv = lv + LEVEL_UP[(LivingDamageUtil.getPowerOfTwo(lv + 1) >= LEVEL_UP.length ? LEVEL_UP.length - 1 : LivingDamageUtil.getPowerOfTwo(lv + 1))];
                        ItemStack mainHand = new ItemStack(Items.GOLDEN_AXE);
                        mainHand.enchant(enchReg.getOrThrow(Enchantments.SHARPNESS), lv + 1);
                        piglin.setItemSlot(EquipmentSlot.MAINHAND, mainHand);
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
                        if (att1 != null && att1.getModifier(ESTEEMED_GUEST_FOLLOW_MOD_ID) == null)
                            att1.addTransientModifier(
                                    new AttributeModifier(
                                            ESTEEMED_GUEST_FOLLOW_MOD_ID,
                                            64,
                                            AttributeModifier.Operation.ADD_VALUE
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
                    if (i % 2 == 0) spawnParticle(friend.level(),friend.blockPosition(),1);
                    if (i % 2 == 1) spawnParticle(target.level(),target.blockPosition(),0);
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
            shield.set(DataComponents.CUSTOM_DATA, CustomData.of(shieldTag));

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

        private static void spawnParticle(Level level, BlockPos pPos, int type) {
            if (level instanceof ServerLevel serverLevel) {
                Vec3 center = new Vec3(pPos.getX() + 0.5, pPos.getY() + 0.5, pPos.getZ() + 0.5);
                double radius = 1.5;
                for (int i = 0; i < 16; i++) {
                    double angle = 2 * Math.PI * Math.random();
                    double r = radius * Math.sqrt(Math.random());
                    double x = center.x + r * Math.cos(angle);
                    double z = center.z + r * Math.sin(angle);
                    double y = center.y + angle * 0.5;
                    if (type == 0) {
                        serverLevel.sendParticles(
                                ParticleTypes.ANGRY_VILLAGER, x, y, z, 1, 0, 0, 0, 0.025
                        );
                    }if (type == 1) {
                        serverLevel.sendParticles(
                                ParticleTypes.WITCH, x, y, z, 1, 0, 0, 0, 0.025
                        );
                    }
                }
            }
        }
    }
}
