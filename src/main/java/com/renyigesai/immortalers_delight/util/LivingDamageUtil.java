package com.renyigesai.immortalers_delight.util;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.monster.hoglin.HoglinBase;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.CapabilityProvider;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class LivingDamageUtil {
    //疣猪兽击退
    public static void knockbackLikeHogLin(LivingEntity attacker, LivingEntity hurtOne) {
        double d0 = attacker.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
        double d1 = hurtOne.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
        double d2 = d0 - d1;
        if (!(d2 <= 0.0D)) {
            double d3 = hurtOne.getX() - attacker.getX();
            double d4 = hurtOne.getZ() - attacker.getZ();
            float f = (float)(attacker.level().random.nextInt(21) - 10);
            double d5 = d2 * (double)(attacker.level().random.nextFloat() * 0.5F + 0.2F);
            Vec3 vec3 = (new Vec3(d3, 0.0D, d4)).normalize().scale(d5).yRot(f);
            double d6 = d2 * (double)attacker.level().random.nextFloat() * 0.5D;
            hurtOne.push(vec3.x, d6, vec3.z);
            hurtOne.hurtMarked = true;
        }
    }
    //掠夺兽击退
    public static void knockbackLikeRavager(LivingEntity attacker, LivingEntity hurtOne) {
        if (attacker.getRandom().nextDouble() < 0.5D) {
            attacker.playSound(SoundEvents.RAVAGER_STUNNED, 1.0F, 1.0F);
            attacker.level().broadcastEntityEvent(attacker, (byte)39);
            hurtOne.push(attacker);
        } else {
            strongKnockback(attacker,hurtOne);
        }

        hurtOne.hurtMarked = true;
    }
    private static void strongKnockback(LivingEntity attacker, Entity hurtOne) {
        double d0 = hurtOne.getX() - attacker.getX();
        double d1 = hurtOne.getZ() - attacker.getZ();
        double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
        hurtOne.push(d0 / d2 * 4.0D, 0.2D, d1 / d2 * 4.0D);
    }
    //实现玩家盾牌扣耐久
    public static void hurtCurrentlyUsedShield(Player player, float pDamage) {
        if (player.getUseItem().canPerformAction(net.minecraftforge.common.ToolActions.SHIELD_BLOCK)) {
            if (!player.level().isClientSide) {
                player.awardStat(Stats.ITEM_USED.get(player.getUseItem().getItem()));
            }

            if (pDamage >= 3.0F) {
                int i = 1 + Mth.floor(pDamage);
                InteractionHand interactionhand = player.getUsedItemHand();
                player.getUseItem().hurtAndBreak(i, player, (p_219739_) -> {
                    p_219739_.broadcastBreakEvent(interactionhand);
                    net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(player, player.getUseItem(), interactionhand);
                    player.stopUsingItem(); // Forge: fix MC-168573
                });
                if (player.getUseItem().isEmpty()) {
                    if (interactionhand == InteractionHand.MAIN_HAND) {
                        player.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                    } else {
                        player.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
                    }

                    player.playSound(SoundEvents.SHIELD_BREAK, 0.8F, 0.8F + player.level().random.nextFloat() * 0.4F);
                }
            }

        }
    }

    public static boolean willBeDisableShield(LivingEntity attacker, LivingEntity hurtOne, ItemStack shield) {
        return attacker.canDisableShield() || attacker.getMainHandItem().canDisableShield(shield, hurtOne, attacker);
    }
    public static boolean hurtEntity(LivingEntity hurtOne, DamageSource source, float pDamage) {
        float damage = pDamage;
        if (!net.minecraftforge.common.ForgeHooks.onLivingAttack(hurtOne, source, damage)) return false;
        if (hurtOne.level().isClientSide) {
            return false;
        } else if (hurtOne.isDeadOrDying()) {
            return false;
        } else {
            //让那个生物起床
            if (hurtOne.isSleeping() && !hurtOne.level().isClientSide) {
                hurtOne.stopSleeping();
            }

            //用于判定伤害是否完全被盾牌格挡
            boolean flag = false;
            float f1 = 0.0F;
            if (damage > 0.0F && hurtOne.isDamageSourceBlocked(source)) {
                net.minecraftforge.event.entity.living.ShieldBlockEvent ev = net.minecraftforge.common.ForgeHooks.onShieldBlock(hurtOne, source, damage);
                if (!ev.isCanceled()) {
                    if (ev.shieldTakesDamage() && hurtOne instanceof Player player) hurtCurrentlyUsedShield(player,damage);
                    f1 = ev.getBlockedDamage();
                    damage -= ev.getBlockedDamage();
                    if (!source.is(DamageTypeTags.IS_PROJECTILE)) {
                        Entity entity = source.getDirectEntity();
                        if (entity instanceof LivingEntity attacker) {
                            hurtOne.knockback(0.5D, hurtOne.getX() - attacker.getX(), hurtOne.getZ() - attacker.getZ());
                            if (attacker instanceof Ravager) knockbackLikeRavager(attacker,hurtOne);
                            if (attacker instanceof HoglinBase) knockbackLikeHogLin(attacker,hurtOne);
                            if (hurtOne instanceof Player player && willBeDisableShield(attacker,hurtOne,attacker.getMainHandItem())) player.disableShield(true);
                        }
                    }

                    flag = damage <= 0;
                }
                if (source.is(DamageTypeTags.IS_FREEZING) && hurtOne.getType().is(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES)) {
                    damage *= 5.0F;
                }
            }

            //进行基础设置，对实体实际造成扣血并修改相关量
            hurtOne.setNoActionTime(0);
            hurtOne.walkAnimation.setSpeed(1.5F);
            hurtOne.invulnerableTime = 20;
            hurtOne.getCombatTracker().recordDamage(source, damage);
            hurtOne.gameEvent(GameEvent.ENTITY_DAMAGE);
            hurtOne.hurtDuration = 10;
            hurtOne.hurtTime = hurtOne.hurtDuration;
            hurtOne.setHealth(hurtOne.getHealth() - damage);


            Entity attacker = source.getEntity();
            //处理生物仇恨和群体仇恨
            if (attacker != null) {
                if (attacker instanceof LivingEntity livingAttacker) {
                    if (!source.is(DamageTypeTags.NO_ANGER)) {
                        hurtOne.setLastHurtByMob(livingAttacker);
                    }
                }

                if (attacker instanceof Player playerAttacker) {
                    hurtOne.setLastHurtByPlayer(playerAttacker);
                } else if (attacker instanceof net.minecraft.world.entity.TamableAnimal tamableEntity) {
                    if (tamableEntity.isTame()) {
                        LivingEntity owner = tamableEntity.getOwner();
                        if (owner instanceof Player player2) {
                            hurtOne.setLastHurtByPlayer(player2);
                        } else {
                            hurtOne.setLastHurtByPlayer(null);
                        }
                    }
                }
            }

            hurtOne.level().broadcastDamageEvent(hurtOne, source);

            //是否让实体显红
            if (!source.is(DamageTypeTags.NO_IMPACT)) {
                hurtOne.hurtMarked = true;
            }

            //造成爆炸击退
            if (attacker != null && !source.is(DamageTypeTags.IS_EXPLOSION)) {
                double d0 = attacker.getX() - hurtOne.getX();

                double d1;
                for (d1 = attacker.getZ() - hurtOne.getZ(); d0 * d0 + d1 * d1 < 1.0E-4D; d1 = (Math.random() - Math.random()) * 0.01D) {
                    d0 = (Math.random() - Math.random()) * 0.01D;
                }

                hurtOne.knockback(0.4F, d0, d1);
                if (!flag) {
                    hurtOne.indicateDamage(d0, d1);
                }
            }

            //设置死亡状态或者播放音效
            if (hurtOne.isDeadOrDying()) {
                hurtOne.die(source);
                hurtOne.deathTime = 19;
            } else {
                SoundEvent soundevent = SoundEvents.GENERIC_HURT;
                hurtOne.playSound(soundevent, 2F, hurtOne.getVoicePitch());
            }

            //这个判定表示是否确实伤害成功
            boolean flag2 = !flag || damage > 0.0F;

            //成就触发器，统计计数器
            if (hurtOne instanceof ServerPlayer serverPlayer) {
                CriteriaTriggers.ENTITY_HURT_PLAYER.trigger(serverPlayer, source, damage, damage, flag);
                if (f1 > 0.0F && f1 < 3.4028235E37F) {
                    serverPlayer.awardStat(Stats.CUSTOM.get(Stats.DAMAGE_BLOCKED_BY_SHIELD), Math.round(f1 * 10.0F));
                }
            }

            if (attacker instanceof ServerPlayer) {
                CriteriaTriggers.PLAYER_HURT_ENTITY.trigger((ServerPlayer) attacker, hurtOne, source, damage, damage, flag);
            }

            return flag2;
        }
    }

    public static void actuallyHurtLivingEntity(LivingEntity hurtOne, DamageSource pDamageSource, float pDamageAmount) {
        if (!hurtOne.isInvulnerableTo(pDamageSource)) {
            pDamageAmount = net.minecraftforge.common.ForgeHooks.onLivingHurt(hurtOne, pDamageSource, pDamageAmount);
            if (pDamageAmount <= 0) return;
            //pDamageAmount = hurtOne.getDamageAfterArmorAbsorb(pDamageSource, pDamageAmount);
            //pDamageAmount = hurtOne.getDamageAfterMagicAbsorb(pDamageSource, pDamageAmount);
            float f1 = Math.max(pDamageAmount - hurtOne.getAbsorptionAmount(), 0.0F);
            hurtOne.setAbsorptionAmount(hurtOne.getAbsorptionAmount() - (pDamageAmount - f1));
            float f = pDamageAmount - f1;
            if (f > 0.0F && f < 3.4028235E37F) {
                Entity entity = pDamageSource.getEntity();
                if (entity instanceof ServerPlayer) {
                    ServerPlayer serverplayer = (ServerPlayer)entity;
                    serverplayer.awardStat(Stats.DAMAGE_DEALT_ABSORBED, Math.round(f * 10.0F));
                }
            }

            f1 = net.minecraftforge.common.ForgeHooks.onLivingDamage(hurtOne, pDamageSource, f1);
            if (f1 != 0.0F) {
                hurtOne.getCombatTracker().recordDamage(pDamageSource, f1);
                hurtOne.setHealth(hurtOne.getHealth() - f1);
                hurtOne.setAbsorptionAmount(hurtOne.getAbsorptionAmount() - f1);
                hurtOne.gameEvent(GameEvent.ENTITY_DAMAGE);
            }
        }
    }

    // 缓存Method对象，避免每次反射获取，提升性能
    private static Method ACTUALLY_HURT_METHOD;

    // 静态代码块初始化Method（仅执行一次）
    static {
        try {
            // 获取actuallyHurt方法（参数：DamageSource + float）
            ACTUALLY_HURT_METHOD = LivingEntity.class.getDeclaredMethod(
                    "actuallyHurt",
                    DamageSource.class,
                    float.class
            );
            // 突破protected访问限制
            ACTUALLY_HURT_METHOD.setAccessible(true);
        } catch (NoSuchMethodException e) {
            // 打印异常并抛出运行时异常，方便调试
            e.printStackTrace();
            throw new RuntimeException("无法找到LivingEntity的actuallyHurt方法", e);
        }
    }

    /**
     * 安全调用actuallyHurt方法
     * @param entity 目标实体（如玩家、僵尸等LivingEntity子类）
     * @param damageSource 伤害源（如GENERIC、FALL、ATTACK等）
     * @param damageAmount 伤害值（浮点型，如5.0F表示5点伤害）
     */
    public static void callActuallyHurt(LivingEntity entity, DamageSource damageSource, float damageAmount) {
        if (entity == null || damageSource == null) {
            throw new IllegalArgumentException("实体或伤害源不能为null");
        }

        try {
            // 调用方法：第一个参数是实体实例，后续是方法参数
            ACTUALLY_HURT_METHOD.invoke(entity, damageSource, damageAmount);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("无法访问actuallyHurt方法", e);
        } catch (InvocationTargetException e) {
            // 捕获方法内部抛出的异常（如实体已死亡等）
            throw new RuntimeException("调用actuallyHurt时方法内部出错", e.getTargetException());
        }
    }

}
