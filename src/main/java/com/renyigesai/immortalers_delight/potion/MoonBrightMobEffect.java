package com.renyigesai.immortalers_delight.potion;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.potion.immortaleffects.KuuvahkiEffect;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

public class MoonBrightMobEffect extends MobEffect {
    //用于限制大范围遍历实体的频率
    private static boolean bypassLastUpdate = false;
    public MoonBrightMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 9055202);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        if (pDuration > 80) return pDuration % 80 == 0;
        else if (pDuration > 40) return pDuration % 40 == 0;
        else return pDuration % 20 == 0;
    }

    //周期性对范围内的实体添加发光效果
    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity.level() instanceof ServerLevel serverLevel && !serverLevel.isClientSide) {
            //在暗处时，令周围的实体发光
            if (isNight(serverLevel, pLivingEntity.getOnPos().above((int) pLivingEntity.getEyeHeight()))) {
                //降低检测频率，减少消耗
                if (!bypassLastUpdate) {
                    bypassLastUpdate = true;
                    return;
                } else bypassLastUpdate = false;
                //检测大范围内实体，添加发光效果
                List<LivingEntity> entities = serverLevel.getEntitiesOfClass(
                        LivingEntity.class,
                        pLivingEntity.getBoundingBox().inflate(pLivingEntity instanceof Player ? 16 : 8)
                );
                for (LivingEntity livingEntity : entities) {
                    if (livingEntity != pLivingEntity && livingEntity.isAlive()) {
                        livingEntity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 200, 0, false, false, true));
                    }
                }
            } else {
                bypassLastUpdate = true;
            }

        }
    }

    private static boolean isNight(Level pLevel, BlockPos pPos) {
        // 1. 获取天空亮度（扣除世界的天空暗化值）
        int i = pLevel.getBrightness(LightLayer.SKY, pPos) - pLevel.getSkyDarken();
        // 获取太阳角度
        float f = pLevel.getSunAngle(1.0F);

        if (i > 0) {
            // 3. 太阳角度修正（避免信号突变）
            float f1 = f < (float)Math.PI ? 0.0F : ((float)Math.PI * 2F);
            f += (f1 - f) * 0.2F;
            i = Math.round((float)i * Mth.cos(f)); // 结合太阳角度余弦值修正亮度
        }

        // 4. 限制信号范围在0-15
        i = Mth.clamp(i, 0, 15);
        // 5. 信号变化时更新方块状态
        i = 15 - i;
        return i >= 12;
    }
    @Mod.EventBusSubscriber(
            modid = ImmortalersDelightMod.MODID,
            bus = Mod.EventBusSubscriber.Bus.FORGE
    )
    public static class KuuvahkiPotionEffect{


        //当携带者对目标造成弹射物伤害时，对其施加负面状态
        //如果使用药水箭或光灵箭，debuff事件会大幅延长
        //当携带者受到伤害时，会对攻击者施加发光效果进行标记
        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void onLivingHurt(LivingHurtEvent event) {
            LivingEntity hurtOne = event.getEntity();
            if (hurtOne.level().isClientSide()) return;
            LivingEntity attacker = event.getSource().getEntity() instanceof LivingEntity ? (LivingEntity) event.getSource().getEntity() : null;

            //受到伤害时，发光标记攻击者
            MobEffectInstance thisEffect = hurtOne.getEffect(ImmortalersDelightMobEffect.MOONBRIGHT.get());
            if (thisEffect != null && attacker != null) {
                attacker.addEffect(new MobEffectInstance(MobEffects.GLOWING, 200, 0));
            }

            //箭矢造成伤害时，或超凡模式下任意弹射物伤害时，对目标施加负面状态
            if (attacker != null && hurtOne.hasEffect(MobEffects.GLOWING)) {
                thisEffect = attacker.getEffect(ImmortalersDelightMobEffect.MOONBRIGHT.get());
                if (thisEffect != null) {
                    int lv = thisEffect.getAmplifier() + 1;
                    boolean isPowered = DifficultyModeUtil.isPowerBattleMode();
                    boolean flag = isPowered && event.getSource().is(DamageTypeTags.IS_PROJECTILE);
                    //判断伤害条件，普通模式需要为箭矢实体，超凡模式则满足弹射物伤害即可
                    Entity hiter = event.getSource().getDirectEntity();
                    if (flag || hiter instanceof AbstractArrow) {
                        //基础效果时间为4秒，每级再提升2秒
                        int time = 80 + 40 * lv;

                        //在满月夜，效果时间翻4倍(但是过长的效果时间也可能导致范围轰击迟迟打不出来(笑))
                        boolean isFullMoon = canSeeMoon(hurtOne.level()) && hurtOne.level().getMoonPhase() == 4;
                        if (isFullMoon) time *= 4;


                        //效果等级乘以（月明等级+1）/（2的月明等级次方）倍，月明等级越高，效果时间越短(但每级的dot伤害也翻倍)
                        time = time * (lv + 1) / (1 << lv);

                        //保底时间,确保不会出现time=0
                        time = Math.max(time, 2 + lv);
                        if (isFullMoon) time *= 3;

                        if (hiter instanceof AbstractArrow abstractArrow) {
                            // 如果是可捡起的箭或药水箭（药水箭默认无法捡起），效果时间翻倍
                            if (abstractArrow.pickup == AbstractArrow.Pickup.ALLOWED
                                    || abstractArrow instanceof Arrow arrow && arrow.getColor() != -1) time *= 2;
                        }
                        KuuvahkiEffect.addImmortalEffectWithSource(hurtOne, time, lv, attacker);
                    }
                }
            }
        }

        /**
         * 判断维度是否能看到月亮
         * @param level 目标世界（如entity.level()）
         * @return true=能看到月亮
         */
        public static boolean canSeeMoon(Level level) {
            if (level == null) return false;

            DimensionType dimensionType = level.dimensionType();
            ResourceKey<Level> dimensionKey = level.dimension();

            // 原版维度快速判断
            if (dimensionKey == Level.OVERWORLD) return true;
            if (dimensionKey == Level.NETHER || dimensionKey == Level.END) return false;

            // 自定义维度通用判断
            return dimensionType.hasSkyLight() && !dimensionType.hasCeiling();
        }
        //================已废弃==================//

        // 在箭矢命中时，判断射击者，并判断是否是药水箭。
        // 取消箭矢的击中逻辑，造成法术伤害。注意这会让药水箭的药水效果不生效。
        // 这个事件双端执行
//        @SubscribeEvent(priority = EventPriority.LOWEST)
//        public static void onArrowHit(ProjectileImpactEvent event) {
//            Projectile projectile = event.getProjectile();
//
//            HitResult hitResult = event.getRayTraceResult();
//            HitResult.Type hitresult$type = hitResult.getType();
//
//            //如果是击中了实体
//            if (hitresult$type == HitResult.Type.ENTITY) {
//                //判断箭矢的射手是否有月明buff
//                if (projectile instanceof AbstractArrow abstractArrow && abstractArrow.getOwner() != null && abstractArrow.getOwner() instanceof LivingEntity shooter) {
//                    MobEffectInstance thisEffect = shooter.getEffect(ImmortalersDelightMobEffect.MOONBRIGHT.get());
//                    if (thisEffect != null) {
//                        //造成法术伤害
//                        double range = 2.5D;
//                        Vec3 srcVec = abstractArrow.getEyePosition();
//                        Vec3 lookVec = abstractArrow.getViewVector(1.0F);
//                        Vec3 destVec = srcVec.add(lookVec.x() * range, lookVec.y() * range, lookVec.z() * range);
//                        float var9 = 1.0F;
//                        List<Entity> possibleList = abstractArrow.level().getEntities(abstractArrow, abstractArrow.getBoundingBox().expandTowards(lookVec.x() * range, lookVec.y() * range, lookVec.z() * range).inflate(var9, var9, var9));
//
//                        boolean flag = false;
//                        if (!possibleList.isEmpty()) {
//                            for (Entity entity : possibleList) {
//                                if (entity instanceof LivingEntity hurtOne) {
//                                    float borderSize = 0.5F;
//                                    AABB collisionBB = entity.getBoundingBox().inflate(borderSize, borderSize, borderSize);
//                                    Optional<Vec3> interceptPos = collisionBB.clip(srcVec, destVec);
//                                    if (collisionBB.contains(srcVec)) {
//                                        flag = true;
//                                    } else if (interceptPos.isPresent()) {
//                                        flag = true;
//                                    }
//
//                                    if (flag) {
//                                        if (hurtOne.invulnerableTime <= 10) moonArrowHitEntity(abstractArrow, hurtOne);
//                                        else System.out.println("无敌时间中");
//                                    }
//                                }
//                            }
//                        } else System.out.println("no entities");
//                        //取消事件，打断箭矢击中的逻辑，这会让箭矢继续飞行
//                        event.setCanceled(true);
//                    } else System.out.println("no moonbright");
//                }
//            }
//
//        }
//
//        // 搬运了箭矢的部分HitEntity代码
//        public static boolean moonArrowHitEntity(AbstractArrow arrow, LivingEntity hurtOne) {
//            System.out.println("moonArrowHitEntity");
//            //获取伤害
//            float f = (float)arrow.getDeltaMovement().length();
//            int i = Mth.ceil(Mth.clamp((double)f * arrow.getBaseDamage(), 0.0D, (double)Integer.MAX_VALUE));
//            //计算暴击伤害
//            if (arrow.isCritArrow()) {
//                long j = (long)arrow.level().getRandom().nextInt(i / 2 + 2);
//                i = (int)Math.min(j + (long)i, 2147483647L);
//            }
//            //构造伤害源，替换为法术伤害
//            Entity shooter = arrow.getOwner();
//            DamageSource damagesource;
//            if (shooter == null) {
//                damagesource = arrow.damageSources().indirectMagic(arrow, arrow);
//            } else {
//                damagesource = arrow.damageSources().indirectMagic(arrow, shooter);
//                if (shooter instanceof LivingEntity) {
//                    ((LivingEntity)shooter).setLastHurtMob(hurtOne);
//                }
//            }
//            //记录实体的燃烧时间
//            boolean flag = hurtOne.getType() == EntityType.ENDERMAN;
//            int k = hurtOne.getRemainingFireTicks();
//            if (arrow.isOnFire() && !flag) {
//                hurtOne.setRemainingFireTicks(k + 100);
//            }
//
//            hurtOne.invulnerableTime = 0;
//            if (hurtOne.hurt(damagesource, (float)i)) {
//                if (flag) {
//                    return false;
//                }
//                //处理冲击箭的击退
//                if (arrow.getKnockback() > 0) {
//                    double d0 = Math.max(0.0D, 1.0D - hurtOne.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
//                    Vec3 vec3 = arrow.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D).normalize().scale((double)arrow.getKnockback() * 0.6D * d0);
//                    if (vec3.lengthSqr() > 0.0D) {
//                        hurtOne.push(vec3.x, 0.1D, vec3.z);
//                    }
//                }
//                //处理附魔，大概是荆棘之类
//                if (!arrow.level().isClientSide() && shooter instanceof LivingEntity) {
//                    EnchantmentHelper.doPostHurtEffects(hurtOne, shooter);
//                    EnchantmentHelper.doPostDamageEffects((LivingEntity)shooter, hurtOne);
//                }
//
//                hurtOne.addEffect(new MobEffectInstance(MobEffects.GLOWING, 200, 0));
//                if (hurtOne != shooter && hurtOne instanceof Player && shooter instanceof ServerPlayer && !arrow.isSilent()) {
//                    ((ServerPlayer)shooter).connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, 0.0F));
//                }
//
//                return true;
//            } else {
//                System.out.println("实体免疫此伤害");
//                //如果没有造成伤害，那么复原击中实体的燃烧时间
//                hurtOne.setRemainingFireTicks(k);
//                //如果实体免疫了箭矢，箭矢会被反弹
//                arrow.setDeltaMovement(arrow.getDeltaMovement().scale(-0.1D));
//                arrow.setYRot(arrow.getYRot() + 180.0F);
//                arrow.yRotO += 180.0F;
//
//                return false;
//            }
//        }

        //这个事件基本什么用都没有，甚至获取不到箭矢的实体
//        @SubscribeEvent(priority = EventPriority.LOWEST)
//        public static void onArrowShoot(ArrowLooseEvent event) {
//            if (!(event.getLevel() instanceof ServerLevel)) return;
//            Player shooter = event.getEntity();
//
//            MobEffectInstance thisEffect = shooter.getEffect(ImmortalersDelightMobEffect.MOONBRIGHT.get());
//            if (thisEffect != null) {
//                arrowList.add(event.get);
//            }
//        }

    }
}
