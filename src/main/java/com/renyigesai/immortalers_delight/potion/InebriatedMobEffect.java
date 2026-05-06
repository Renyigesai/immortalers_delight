package com.renyigesai.immortalers_delight.potion;
import net.neoforged.fml.common.EventBusSubscriber;

import com.renyigesai.immortalers_delight.Config;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.item.food.InebriatedToxicFoodItem;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;

import java.util.Objects;

import static com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect.*;

public class InebriatedMobEffect extends BaseMobEffect {
    public InebriatedMobEffect() {
        super(MobEffectCategory.HARMFUL, 4959736);
    }

    @Override
    public boolean applyEffectTick(LivingEntity pEntity, int amplifier) {
        if (this == INEBRIATED.get() && !pEntity.level().isClientSide()) {
            int time = pEntity.hasEffect(INEBRIATED) ? Objects.requireNonNull(pEntity.getEffect(INEBRIATED)).getDuration() : 0;
            if (time > 3600) {
                super.applyEffectTick(pEntity, amplifier);
                if (Config.useBetterStun) {
                    InebriatedToxicFoodItem.addEffectWithoutCanBeAffected(pEntity, new MobEffectInstance(WEAK_POISON, time, amplifier), null);
                }else pEntity.addEffect(new MobEffectInstance(WEAK_POISON, time, amplifier));
                pEntity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, time, amplifier));
                pEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, time, amplifier));
                pEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, time, amplifier));
                pEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, time, amplifier));

            }
        }
        return true;
    }
    @Override
    public void applyEffectTickInControl(LivingEntity pEntity, int amplifier) {
        boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
        float health = pEntity.getHealth();
        float damage = (20 > pEntity.getMaxHealth() ? 20 : pEntity.getMaxHealth()) * 0.08F;
        if (!isPowerful && damage > 8 + 4 * amplifier) {
            damage = 8+4*amplifier;
        }
        boolean isOP = pEntity instanceof Player player && player.isCreative();
        if (!isOP || isPowerful) {
            pEntity.invulnerableTime = 0;
            ResourceKey<DamageType> drunk = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "drunk"));
            pEntity.hurt(pEntity.damageSources().source(drunk, null), damage);
            pEntity.invulnerableTime = 0;
            if (isPowerful && (health - damage) > 0 && pEntity.getHealth() > (health - damage)) {
                pEntity.setHealth(health - damage);
            }
        }
    }

    @Override
    public boolean isDurationEffectTickInControl(int pDuration, int pAmplifier) {
        int j = 64 >> pAmplifier;
        if (j > 0) {
            return pDuration % j == 0;
        } else {
            return true;
        }
    }


    @EventBusSubscriber(
            modid = ImmortalersDelightMod.MODID)
    public static class InebriatedPotionEffect {

        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void onAddToEntity(MobEffectEvent.Applicable event) {
            if (event != null && event.getEntity() != null) {
                LivingEntity entity = event.getEntity();
                if (!entity.level().isClientSide()
                        && event.getEffectInstance().getEffect().is(ImmortalersDelightMobEffect.INEBRIATED)
                        && !entity.hasEffect(ImmortalersDelightMobEffect.MAGICAL_REVERSE)) {
                    event.setResult(MobEffectEvent.Applicable.Result.APPLY);
                }
            }
        }
        @SubscribeEvent
        public static void onRemoveFromEntity(MobEffectEvent.Remove event) {
            if (event != null && event.getEntity() != null) {
                LivingEntity entity = event.getEntity();

                if (entity instanceof Player player && player.isCreative()) return;
                if (!entity.level().isClientSide()) {
                    MobEffectInstance inebriated = entity.getEffect(ImmortalersDelightMobEffect.INEBRIATED);
                    MobEffectInstance magicalReverse = entity.getEffect(ImmortalersDelightMobEffect.MAGICAL_REVERSE);
                    int maxTime = magicalReverse == null ? 0 : (magicalReverse.getDuration() * 100) << magicalReverse.getAmplifier();
                    if (inebriated != null && inebriated.getDuration() > maxTime) {
                        if (event.getEffectInstance() != null
                                && (event.getEffectInstance().getEffect().is(ImmortalersDelightMobEffect.INEBRIATED)
                                || event.getEffectInstance().getEffect().is(ImmortalersDelightMobEffect.WEAK_POISON)
                                || event.getEffectInstance().getEffect().is(MobEffects.POISON)
                                || event.getEffectInstance().getEffect().is(MobEffects.BLINDNESS)
                                || event.getEffectInstance().getEffect().is(MobEffects.CONFUSION)
                                || event.getEffectInstance().getEffect().is(MobEffects.MOVEMENT_SLOWDOWN)
                                || event.getEffectInstance().getEffect().is(MobEffects.WEAKNESS))) {
                            event.setCanceled(true);
                        }
                    }
                }
            }
        }
    }
}
