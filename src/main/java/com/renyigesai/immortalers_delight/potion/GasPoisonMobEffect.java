package com.renyigesai.immortalers_delight.potion;
import net.neoforged.fml.common.EventBusSubscriber;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.item.weapon.GoldenFabricArmor;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect.*;

public class GasPoisonMobEffect extends BaseMobEffect {
    public GasPoisonMobEffect() {
        super(MobEffectCategory.HARMFUL, 9574964);
    }
    @Override
    public boolean applyEffectTick(LivingEntity pEntity, int amplifier) {
        super.applyEffectTick(pEntity, amplifier);
        int i = pEntity.getRandom().nextInt(5);
        switch (i) {
            case 0 -> pEntity.addEffect(new MobEffectInstance(MobEffects.HUNGER, 100, amplifier));
            case 1 -> pEntity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, amplifier));
            case 2 -> pEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 100, amplifier));
            case 3 -> pEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, amplifier));
            case 4 -> pEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, amplifier));
        }
        return true;
    }
    @Override
    public void applyEffectTickInControl(LivingEntity pEntity, int amplifier) {
        if (this == GAS_POISON.get() && !pEntity.level().isClientSide()) {
            boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
            float health = pEntity.getHealth();
            float damage = (20 > pEntity.getMaxHealth() ? 20 : pEntity.getMaxHealth()) * 0.06F;
            if (!isPowerful && damage > 6 + 3 * amplifier) {
                damage = 6+3*amplifier;
            }
            boolean isOP = pEntity instanceof Player player && player.isCreative();
            if (!isOP || isPowerful) {
                pEntity.invulnerableTime = 0;
                pEntity.hurt(getDamageSource(pEntity, null), damage);
                pEntity.invulnerableTime = 0;
            }
        }
    }

    public static DamageSource getDamageSource(Entity hurtOne, @Nullable Entity attacker) {
        if (attacker != null) {
            return new DamageSource(hurtOne.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.parse("immortalers_delight:gas"))), attacker);
        }
        return new DamageSource(hurtOne.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.parse("immortalers_delight:gas"))));
    }

    @Override
    /**
     * 判断效果是否应该刻更新
     * @param pDuration 效果剩余持续时间
     * @param pAmplifier 效果的放大等级
     * @return 如果效果应该刻更新则返回true，否则返回false
     */
    public boolean isDurationEffectTickInControl(int pDuration, int pAmplifier) {
        // 计算更新间隔，基础值为32，随着放大等级增加而增大
        int j = 32 >> pAmplifier;
        // 如果计算出的间隔大于0
        if (j > 0) {
            // 当剩余时间对间隔取余为0时，表示应该更新效果
            return pDuration % j == 0;
        } else {
            // 如果间隔小于等于0，则始终更新效果
            return true;
        }
    }

    @EventBusSubscriber(
            modid = ImmortalersDelightMod.MODID)
    public static class GasPoisonPotionEffect {

//        private static final Map<UUID,Float> entityDamage = new HashMap<UUID,Float>();
//        @SubscribeEvent(priority = EventPriority.LOWEST)
//        public static void onLivingAttack(LivingAttackEvent event) {
//            //这里是条件判断，什么伤害需要绝对真伤
//            if (event.getSource().is(ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.parse("immortalers_delight:gas")))) {
//                if (event.isCanceled()) event.setCanceled(false);
//                LivingEntity pEntity = event.getEntity();
//                float health = pEntity.getHealth();
//                float damage = event.getNewDamage();
//                //记录应该减扣至的血量
//                entityDamage.put(pEntity.getUUID(), health - damage);
//            }
//        }
//        @SubscribeEvent(priority = EventPriority.LOWEST)
//        public static void onLivingHurt(LivingDamageEvent.Pre event) {
//            //这里是条件判断，什么伤害需要绝对真伤
//            if (event.getSource().is(ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.parse("immortalers_delight:gas")))) {
//                if (event.isCanceled()) event.setCanceled(false);
//            }
//        }
//        @SubscribeEvent(priority = EventPriority.LOWEST)
//        public static void onLivingDamage(LivingDamageEvent event) {
//            boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
//            LivingEntity pEntity = event.getEntity();
//            if (entityDamage.containsKey(pEntity.getUUID())) {
//                float health = pEntity.getHealth();
//                float needHealth = entityDamage.get(pEntity.getUUID());
//                //如果发现被减伤了(当前血量减伤害值大于记录的血量)
//                if (isPowerful && health - event.getNewDamage() > needHealth) {
//                    pEntity.setHealth(needHealth < 0.0F ? 0.0F : needHealth);
//                    event.setNewDamage(0.0F);
//                }
//                entityDamage.remove(pEntity.getUUID());
//            }
//        }
        @SubscribeEvent
        public static void onRemoveFromEntity(MobEffectEvent.Remove event) {
            if (event != null && event.getEntity() != null) {
                LivingEntity entity = event.getEntity();

                if (entity instanceof Player player && player.isCreative()) return;
                if (!entity.getCommandSenderWorld().isClientSide
                        && entity.hasEffect(ImmortalersDelightMobEffect.GAS_POISON)
                        && !entity.hasEffect(ImmortalersDelightMobEffect.MAGICAL_REVERSE)
                        && !(entity.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof GoldenFabricArmor)) {
                    if (event.getEffectInstance() != null
                            && (event.getEffectInstance().getEffect() == ImmortalersDelightMobEffect.GAS_POISON
                            || event.getEffectInstance().getEffect() == MobEffects.HUNGER
                            || event.getEffectInstance().getEffect() == MobEffects.BLINDNESS
                            || event.getEffectInstance().getEffect() == MobEffects.CONFUSION
                            || event.getEffectInstance().getEffect() == MobEffects.MOVEMENT_SLOWDOWN
                            || event.getEffectInstance().getEffect() == MobEffects.WEAKNESS)) {
                        event.setCanceled(true);
                    }
                }
            }
        }
    }
}
