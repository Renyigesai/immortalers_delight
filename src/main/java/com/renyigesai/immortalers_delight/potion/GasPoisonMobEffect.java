package com.renyigesai.immortalers_delight.potion;

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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import static com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect.*;

public class GasPoisonMobEffect extends MobEffect {
    public GasPoisonMobEffect() {
        super(MobEffectCategory.HARMFUL, 9574964);
    }
    @Override
    public void applyEffectTick(LivingEntity pEntity, int amplifier) {
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
                if (isPowerful && (health - damage) > 0 && pEntity.getHealth() > (health - damage)) {
                    pEntity.setHealth(health - damage);
                }
                int i = pEntity.getRandom().nextInt(5);
                switch (i) {
                    case 0 -> pEntity.addEffect(new MobEffectInstance(MobEffects.HUNGER, 100, amplifier));
                    case 1 -> pEntity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, amplifier));
                    case 2 -> pEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 100, amplifier));
                    case 3 -> pEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, amplifier));
                    case 4 -> pEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, amplifier));
                }
            }
        }
    }

    public static DamageSource getDamageSource(Entity hurtOne, @Nullable Entity attacker) {
        if (attacker != null) {
            return new DamageSource(hurtOne.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("immortalers_delight:gas"))), attacker);
        }
        return new DamageSource(hurtOne.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("immortalers_delight:gas"))));
    }

    @Override
    /**
     * 判断效果是否应该刻更新
     * @param pDuration 效果剩余持续时间
     * @param pAmplifier 效果的放大等级
     * @return 如果效果应该刻更新则返回true，否则返回false
     */
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
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

}
