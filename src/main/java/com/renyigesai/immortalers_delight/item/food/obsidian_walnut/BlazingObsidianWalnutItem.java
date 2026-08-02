package com.renyigesai.immortalers_delight.item.food.obsidian_walnut;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.client.particle.ScreenLayerParticleOption;
import com.renyigesai.immortalers_delight.client.particle.ShockWaveParticleOption;
import com.renyigesai.immortalers_delight.entities.projectile.BlazingObsidianWalnutThrowingEntity;
import com.renyigesai.immortalers_delight.entities.projectile.ToxicGasGrenadeEntity;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightParticleTypes;
import com.renyigesai.immortalers_delight.potion.BaseMobEffect;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class BlazingObsidianWalnutItem extends Item {
    public BlazingObsidianWalnutItem(Properties pProperties) {
        super(pProperties);
    }

    /*=============================实现延时起爆功能==================================*/
    public static final String TAG_PERSISTENT = "Persistent";
    //两个NBT标签，用于客户端计数（服务端高频修改nbt会导致物品动画异常）
    public static final String TAG_USE_TIME = "UseTime";
    public static final String TAG_PREV_USE_TIME = "PrevUseTime";
    //服务端通过map记录倒计时
    public static final Map<UUID, Integer> itemLifeTick = new HashMap<>();
    public static final String TAG_STACK_ID = "StackID";
    public static final String TAG_BIG_BOOM = "BigBoom";

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int i, boolean held) {
        //实现创造限定的稳定性核桃
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.contains(TAG_PERSISTENT)) return;

        if (entity instanceof LivingEntity living) {
            int count = stack.getCount();
            //客户端记录：每tick增加tick计数，同时保存上一tick的计数
            if (level.isClientSide()) {
                int useTime = getUseTime(stack);
                if (tag.getInt(TAG_PREV_USE_TIME) != tag.getInt(TAG_USE_TIME)) {
                    tag.putInt(TAG_PREV_USE_TIME, getUseTime(stack));
                }

                int maxLoadTime = getMaxLoadTime();
                if (useTime < maxLoadTime) {
                    int set = useTime + 1;
                    setUseTime(stack, set);
                }

                //生成警告特效
                if (useTime > (maxLoadTime / 2)) {
                    float f = (float) useTime / maxLoadTime;
                    int apl = (int) (100 * f);
                    spawnLayerParticle(level,entity.getX(),entity.getY(),entity.getZ(), apl,0);

                    if (useTime > (maxLoadTime * 0.8)) spawnLayerParticle(level,entity.getX(),entity.getY(),entity.getZ(), apl,1);

                }

                //生成爆炸特效
                if (tag.contains(TAG_BIG_BOOM)) {
                    spawnParticle(level, 4 + count * 0.125f, level.random, entity.getEyePosition());
                }
            } else {
                //服务端记录：通过map与分配UUID为每个itemStack进行倒计时
                int maxLoadTime = getMaxLoadTime();
                if (tag.hasUUID(TAG_STACK_ID)) {
                    UUID uuid = tag.getUUID(TAG_STACK_ID);
                    int time = itemLifeTick.getOrDefault(uuid,0);
                    int newTime = time + 1;

                    //服务端发送警报消息
                    if (time == (maxLoadTime / 2)) {
                        if (entity instanceof Player player) player.displayClientMessage(
                                Component.translatable("message." + ImmortalersDelightMod.MODID+ ".blazing_obsidian_walnut_warning", new Object[0]),
                                true);
                    }

                    //时间到，爆炸
                    if (time >= maxLoadTime) {
                        //爆炸流程
                        if (!tag.contains(TAG_BIG_BOOM)) {
                            if (Boom(stack, level, living)) {
                                //通过数据同步到客户端生成特效
                                tag.putBoolean(TAG_BIG_BOOM,true);
                                newTime -= 5;
                            }
                        } else {
                            //消耗物品
                            stack.shrink(count);
                        }
                    }
                    //实际记录倒计时
                    itemLifeTick.put(uuid,newTime);
                } else {
                    tag.putUUID(TAG_STACK_ID,UUID.randomUUID());
                }

            }

        }

    }

    private static int getMaxLoadTime() {
        return 200;
    }

    public static int getUseTime(ItemStack stack) {
        CompoundTag compoundtag = stack.getTag();
        return compoundtag != null ? compoundtag.getInt(TAG_USE_TIME) : 0;
    }

    public static void setUseTime(ItemStack stack, int useTime) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt(TAG_PREV_USE_TIME, getUseTime(stack));
        tag.putInt(TAG_USE_TIME, useTime);
    }


    public static void spawnLayerParticle(Level level, double x, double y, double z, int custom, int type) {
        if (level == null) return;
        if (type == 0) {
//                ParticleOptions particleOption = ImmortalersDelightParticleTypes.INFERNAL_FORGING_SCREEN_LAYER.get();
            ScreenLayerParticleOption particleOption = new ScreenLayerParticleOption(custom);
            if (level instanceof ServerLevel serverLevel && !serverLevel.isClientSide()) {
                int j = serverLevel.sendParticles(
                        particleOption,  // 粒子参数
                        x, y, z,         // 生成位置
                        1,               // 生成数量
                        0.0D, 0.0D, 0.0D,// 位置无偏移
                        0.0D             // 速度（无作用）
                );
            } else if (level.isClientSide()) {
                level.addAlwaysVisibleParticle(particleOption, x, y, z, 0.0D, 0.0D, 0.0D);
            }
        }
        if (type == 1) {
            for (int i = 0; i < 25; i++) {
                ParticleOptions particleOption = i % 5 == 0 ? ParticleTypes.LARGE_SMOKE : ParticleTypes.FLAME;
                if (level instanceof ServerLevel serverLevel && !serverLevel.isClientSide()) {
                    serverLevel.sendParticles(
                            particleOption,
                            x - 1 + level.random.nextFloat() * 2,
                            y - 0.5 + level.random.nextFloat(),
                            z - 1 + level.random.nextFloat() * 2,
                            1,               // 生成数量
                            0.0D, 0.0D, 0.0D,// 位置无偏移
                            0.0D             // 速度（无作用）
                    );
                } else if (level.isClientSide()) {
                    level.addParticle(particleOption,
                            x - 1 + level.random.nextFloat() * 2,
                            y - 0.5 + level.random.nextFloat(),
                            z - 1 + level.random.nextFloat() * 2,
                            0.0D, 0.0D, 0.0D);
                }
            }
        }
    }

    /*==============================实现炸队友功能===================================*/

    public boolean Boom(ItemStack stack, Level level, LivingEntity entity) {
        if (level.isClientSide) return false;
        if (stack.isEmpty()) return false;
        int count = stack.getCount();
        float r = 4 + count * 0.125f;
        float damage = 17.75f + (entity.getRandom().nextInt(12) + count) * 0.5325f;
        if (DifficultyModeUtil.isPowerBattleMode()) damage *= damage;
        performBlastAttack(entity, entity.getEyePosition(), r,
                entity.damageSources().explosion(entity,entity), damage,
                true,true,true);
        return true;
    }

    public static void performBlastAttack(LivingEntity attacker, Vec3 pos, float range,
                                      DamageSource source, float damage,
                                      boolean isExplosion, boolean bypassCooldown, boolean isCircular) {
        if (!attacker.level().isClientSide) {
            ServerLevel serverLevel = (ServerLevel) attacker.level();

            if (isExplosion) {
                serverLevel.sendParticles(ParticleTypes.EXPLOSION,
                        pos.x, pos.y, pos.z,
                        (int)range,
                        0.5, 0.5, 0.5,
                        0.1
                );
            }

            doBlastDamage(getTargetsOfBlastDamage(attacker, pos, range, isCircular), pos, range, source, damage, true, bypassCooldown);
        }
    }

    public static List<LivingEntity> getTargetsOfBlastDamage(LivingEntity attacker, Vec3 pos, float range, boolean isCircular) {
        if (!attacker.level().isClientSide) {
            ServerLevel serverLevel = (ServerLevel) attacker.level();

            AABB boundingBox = new AABB(
                    pos.x - range, pos.y - range, pos.z - range,
                    pos.x + range, pos.y + range, pos.z + range
            );

            List<LivingEntity> entitiesInRange = serverLevel.getEntitiesOfClass(
                    LivingEntity.class,
                    boundingBox,
                    LivingEntity::isAlive
            );

            if (isCircular) {
                entitiesInRange.removeIf(entity -> entity.distanceToSqr(pos) > range * range);
            }
            return entitiesInRange;
        }
        return new ArrayList<>();
    }

    public static void doBlastDamage(List<LivingEntity> targets, Vec3 pos, float range, DamageSource source, float damage, boolean isExplosion, boolean bypassCooldown) {
        for (LivingEntity target : targets) {
            if (bypassCooldown) {target.invulnerableTime = 0;}
            if (isExplosion) {
                double distance = target.distanceToSqr(pos) < 1 ? 1 : target.distanceToSqr(pos);
                distance /= 0.5 * range;
                target.hurt(source, (float) (damage / distance));

            } else target.hurt(source, damage);

            double knockbackX = (target.getX() - pos.x) * 0.1;
            double knockbackY = (target.getY() - pos.y > 0 ? range - (target.getY() - pos.y) : (range * -1) - (target.getY() - pos.y)) * 0.1;
            double knockbackZ = (target.getZ() - pos.z) * 0.1;
            target.setDeltaMovement(knockbackX, knockbackY, knockbackZ);

        }
    }

    //生成粒子效果
    public void spawnParticle(Level level, double radius, RandomSource randomsource, Vec3 pPos){

        //客户端行为：生成粒子效果
        Vec3 center = new Vec3(pPos.x + 0.5, pPos.y + 0.5, pPos.z + 0.5);

        radius *= 0.5;
        radius += 3.3;
        for (int i = 0; i < 32; i++) {
            double angle = 2 * Math.PI * Math.random();
            double r = radius * Math.sqrt(Math.random());
            double x = center.x + r * Math.cos(angle);
            double z = center.z + r * Math.sin(angle);
            double y = center.y;
            if (r <= radius / 3) {
                level.addParticle(
                        ParticleTypes.SMOKE, false, x, y, z, 0, 0.025, 0
                );
            } else level.addParticle(
                    ParticleTypes.LAVA, false, x, y, z, 0, 0.025, 0
            );
        }
        ShockWaveParticleOption particleOption = new ShockWaveParticleOption(7);
        level.addAlwaysVisibleParticle(particleOption, false, pPos.x, pPos.y + 0.25, pPos.z, 0.0D, 0.0D, 0.0D);

        for(int i = 0; i < 8; ++i) {
            float dx = 0;
            float dy = 0;
            float dz = 0;
            if (i >= 1) {
                if (i <= 6) {
                    dx = (float) Math.sin(i);
                    dz = (float) Math.cos(i);
                } else dy = 0.6f;
            }
            level.addParticle(ImmortalersDelightParticleTypes.HUGE_SMOKE.get(), pPos.x + dx, pPos.y + dy, pPos.z + dz, randomsource.nextGaussian() * 0.15D, randomsource.nextDouble() * 0.2D, randomsource.nextGaussian() * 0.15D);
        }

        ParticleOptions particleoptions = ParticleTypes.LAVA;

        for(int k2 = 0; k2 < 100; ++k2) {
            double d13 = randomsource.nextDouble() * 4.0D;
            double d19 = randomsource.nextDouble() * Math.PI * 2.0D;
            double d25 = Math.cos(d19) * d13;
            double d30 = 0.01D + randomsource.nextDouble() * 0.5D;
            double d31 = Math.sin(d19) * d13;
            level.addParticle(particleoptions,
                    pPos.x + d25 * 0.1D, pPos.y + 0.3D, pPos.z + d31 * 0.1D,
                    d25, d30, d31);
        }

        level.playLocalSound(BlockPos.containing(pPos), SoundEvents.GENERIC_EXPLODE, SoundSource.NEUTRAL, 1.0F, randomsource.nextFloat() * 0.1F + 0.9F, false);
    }
    /*==========================实现投掷功能（关联实体）==============================*/

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        pPlayer.startUsingItem(pHand);
        return InteractionResultHolder.consume(itemstack);
    }
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }
    public void releaseUsing(ItemStack itemstack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft) {
        int i = this.getUseDuration(itemstack) - pTimeLeft;
        if (i < 0) return;
        float f = getPowerForTime(i);
        if (f > 0.1f) {
            pLevel.playSound((Player)null, pEntityLiving.getX(), pEntityLiving.getY(), pEntityLiving.getZ(), SoundEvents.LINGERING_POTION_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (pLevel.getRandom().nextFloat() * 0.4F + 0.8F));
            if (!pLevel.isClientSide) {
                BlazingObsidianWalnutThrowingEntity arrow = new BlazingObsidianWalnutThrowingEntity(pLevel, pEntityLiving);
                arrow.setItem(itemstack);
                arrow.shootFromRotation(pEntityLiving, pEntityLiving.getXRot(), pEntityLiving.getYRot(), 0.0F, 1.5F, 1.0F);
                //让胡桃变为蓝火
                MobEffectInstance instance = pEntityLiving.getEffect(ImmortalersDelightMobEffect.WARM_CURRENT_SURGES.get());
                if (instance != null) {
                    arrow.setDangerous(true);
                }
                pLevel.addFreshEntity(arrow);
            }
            if (pEntityLiving instanceof Player pPlayer && !pPlayer.getAbilities().instabuild) {
                itemstack.shrink(1);
            }
        }
    }
    public static float getPowerForTime(int pCharge) {
        float f = (float)pCharge / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    //实现一些tooltip提示
    @Override
    public void appendHoverText(ItemStack stack, @javax.annotation.Nullable Level level, List<Component> tooltip, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, tooltip, isAdvanced);
        MutableComponent textEmpty0 = Component.translatable("tooltip." + ImmortalersDelightMod.MODID+ "." + this);
        MutableComponent textEmpty1 = Component.translatable("tooltip." + ImmortalersDelightMod.MODID+ "." + this + "_1");
        MutableComponent textEmpty2 = Component.translatable("tooltip." + ImmortalersDelightMod.MODID+ "." + this + "_2");
        tooltip.add(textEmpty0.withStyle(ChatFormatting.GRAY));
        tooltip.add(textEmpty1.withStyle(ChatFormatting.YELLOW));
        tooltip.add(textEmpty2.withStyle(ChatFormatting.YELLOW));

        CompoundTag tag = stack.getOrCreateTag();
        if (tag.contains(TAG_PERSISTENT)) {
            MutableComponent textEmpty = Component.translatable("tooltip." + ImmortalersDelightMod.MODID+ "." + this + ".persistent");
            ChatFormatting color = ChatFormatting.GREEN;
            tooltip.add(textEmpty.withStyle(color));
        } else {
            ChatFormatting color = ChatFormatting.RED;
            int progress = 0;
            if (tag.contains(TAG_USE_TIME,Tag.TAG_INT)) {
                progress = getMaxLoadTime() - tag.getInt(TAG_USE_TIME);
                if (progress < tag.getInt(TAG_USE_TIME)) color = ChatFormatting.DARK_RED;
            }

            MutableComponent textEmpty = Component.translatable("tooltip." + ImmortalersDelightMod.MODID+ ".colorful." + this);
            tooltip.add(textEmpty.withStyle(color));

            if (progress > 0) {
                MutableComponent textValue = Component.translatable(
                        "tooltip." +ImmortalersDelightMod.MODID+ "." + this + ".progress", // 翻译键
                        (progress / 20) // 替换%d占位符
                );
                tooltip.add(textValue.withStyle(ChatFormatting.DARK_RED));
            }
        }
    }
}
