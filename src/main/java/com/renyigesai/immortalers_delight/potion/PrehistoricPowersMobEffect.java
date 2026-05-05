package com.renyigesai.immortalers_delight.potion;

import com.google.common.collect.Maps;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class PrehistoricPowersMobEffect extends BaseMobEffect {
    private final Map<Holder<Attribute>, AttributeModifier> attributeModifierMap = Maps.newHashMap();

    public PrehistoricPowersMobEffect() {
        super(MobEffectCategory.BENEFICIAL, -39424);
    }

    @Override
    protected void applyEffectTickInControl(@NotNull LivingEntity pLivingEntity, int pAmplifier) {
        AttributeMap map = pLivingEntity.getAttributes();
        this.removeAttributeModifiers(map);
        if (pLivingEntity.hasEffect(MobEffects.DAMAGE_BOOST)) {
            applyCustomModifiers(pLivingEntity, map, pAmplifier);
        }
    }

    private void applyCustomModifiers(LivingEntity entity, AttributeMap pAttributeMap, int pAmplifier) {
        int lv = 0;
        MobEffectInstance instance = entity.getEffect(MobEffects.DAMAGE_BOOST);
        if (instance != null) {
            lv = Math.min(instance.getAmplifier(), pAmplifier);
        }
        if (DifficultyModeUtil.isPowerBattleMode()) {
            for (Map.Entry<Holder<Attribute>, AttributeModifier> entry : this.attributeModifierMap.entrySet()) {
                AttributeInstance attributeinstance = pAttributeMap.getInstance(entry.getKey());
                if (attributeinstance != null) {
                    AttributeModifier templ = entry.getValue();
                    attributeinstance.removeModifier(templ.id());
                    double value = this.getAttributeModifierValue(lv, templ);
                    attributeinstance.addPermanentModifier(new AttributeModifier(templ.id(), value, templ.operation()));
                }
            }
        } else {
            this.addAttributeModifiers(pAttributeMap, lv);
        }
    }

    @Override
    public boolean isDurationEffectTickInControl(int duration, int amplifier) {
        return duration % 20 == 0;
    }

    public double getAttributeModifierValue(int pAmplifier, AttributeModifier pModifier) {
        if (pModifier.operation() == AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
            double buffer = 1;
            for (int i = 0; i <= pAmplifier; ++i) {
                buffer *= pModifier.amount() + 1;
            }
            return buffer - 1;
        }
        return pModifier.amount() * (double) (pAmplifier + 1);
    }

    @Override
    public MobEffect addAttributeModifier(Holder<Attribute> attribute, ResourceLocation id, double amount, AttributeModifier.Operation operation) {
        AttributeModifier attributemodifier = new AttributeModifier(id, amount * 2, operation);
        this.attributeModifierMap.put(attribute, attributemodifier);
        return super.addAttributeModifier(attribute, id, amount, operation);
    }

    @Override
    public void addAttributeModifiers(AttributeMap attributeMap, int amplifier) {
        if (DifficultyModeUtil.isPowerBattleMode()) {
            return;
        }
        super.addAttributeModifiers(attributeMap, amplifier);
    }

    @EventBusSubscriber(modid = ImmortalersDelightMod.MODID)
    public static class PrehistoricPowersPotionEffect {
        @SubscribeEvent
        public static void onCreatureHurt(net.neoforged.neoforge.event.entity.living.LivingDamageEvent.Pre evt) {
            if (evt.getSource().is(DamageTypeTags.BYPASSES_EFFECTS)) {
                return;
            }
            LivingEntity hurtOne = evt.getEntity();
            LivingEntity attacker = null;
            boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
            if (evt.getSource().getEntity() instanceof LivingEntity livingEntity) {
                attacker = livingEntity;
            }

            if (!hurtOne.level().isClientSide && attacker != null) {
                MobEffectInstance powers = attacker.getEffect(ImmortalersDelightMobEffect.PREHISTORIC_POWERS);
                MobEffectInstance strength = attacker.getEffect(MobEffects.DAMAGE_BOOST);
                if (powers != null && strength != null && powers.getEffect().value() instanceof BaseMobEffect effect) {
                    int lv = Math.min(effect.getTruthUsingAmplifier(powers.getAmplifier()) + 1, strength.getAmplifier() + 1);
                    if (isPowerful) {
                        lv *= 2;
                    }
                    double damage = (Math.pow(1.3, lv) - 1) / 0.3;
                    evt.setNewDamage(evt.getNewDamage() + (float) damage);
                }
            }
        }
    }
}
