package com.renyigesai.immortalers_delight.potion;

import com.google.common.collect.Maps;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
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
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public class PrehistoricPowersMobEffect extends BaseMobEffect {
    private final Map<Attribute, AttributeModifier> attributeModifierMap = Maps.newHashMap();

    public PrehistoricPowersMobEffect() {
        super(MobEffectCategory.BENEFICIAL, -39424);
    }

    //每秒刷新属性修改
    @Override
    public void applyEffectTickInControl(@NotNull LivingEntity pLivingEntity, int pAmplifier) {
        this.removeAttributeModifiers(pLivingEntity, pLivingEntity.getAttributes(), pAmplifier);
        if (pLivingEntity.hasEffect(MobEffects.DAMAGE_BOOST)) this.addAttributeModifiers(pLivingEntity, pLivingEntity.getAttributes(), pAmplifier);
    }

    @Override
    public boolean isDurationEffectTickInControl(int duration, int amplifier) {
        return duration % 20 == 0;
    }

    @Override
    public void addAttributeModifiers(@NotNull LivingEntity pLivingEntity, @NotNull AttributeMap pAttributeMap, int pAmplifier) {
        int lv = 0;
        MobEffectInstance instance = pLivingEntity.getEffect(MobEffects.DAMAGE_BOOST);
        if (instance != null) lv = Math.min(instance.getAmplifier(), pAmplifier);
        if (DifficultyModeUtil.isPowerBattleMode()) {
            for(Map.Entry<Attribute, AttributeModifier> entry : this.attributeModifierMap.entrySet()) {
                AttributeInstance attributeinstance = pAttributeMap.getInstance(entry.getKey());
                if (attributeinstance != null) {
                    AttributeModifier attributemodifier = entry.getValue();
                    attributeinstance.removeModifier(attributemodifier);
                    attributeinstance.addPermanentModifier(new AttributeModifier(attributemodifier.getId(), this.getDescriptionId() + " " + lv, this.getAttributeModifierValue(lv, attributemodifier), attributemodifier.getOperation()));
                }
            }
        } else super.addAttributeModifiers(pLivingEntity, pAttributeMap, lv);
    }

    //实现旧版力量的伤害公式
    @Override
    public double getAttributeModifierValue(int pAmplifier, AttributeModifier pModifier) {
        if (pModifier.getOperation().toValue() == 2) {
            double buffer = 1;
            for (int i = 0; i <= pAmplifier; ++i) {buffer *= pModifier.getAmount() + 1;}
            return buffer - 1;
        }
        return pModifier.getAmount() * (double)(pAmplifier + 1);
    }
    //记录两套属性修饰符，一个用于普通模式，一个用于超凡模式
    @Override
    public @NotNull MobEffect addAttributeModifier(@NotNull Attribute pAttribute, @NotNull String pUuid, double pAmount, AttributeModifier.@NotNull Operation pOperation) {
        AttributeModifier attributemodifier = new AttributeModifier(UUID.fromString(pUuid), this::getDescriptionId, pAmount * 2, pOperation);
        this.attributeModifierMap.put(pAttribute, attributemodifier);
        return super.addAttributeModifier(pAttribute, pUuid, pAmount, pOperation);
    }

    @Override
    public @NotNull Map<Attribute, AttributeModifier> getAttributeModifiers() {
        if (DifficultyModeUtil.isPowerBattleMode()) return this.attributeModifierMap;
        return super.getAttributeModifiers();
    }



    @Mod.EventBusSubscriber
    public static class PrehistoricPowersPotionEffect {
        @SubscribeEvent
        public static void onCreatureHurt(LivingDamageEvent evt) {
            if (evt.isCanceled() || evt.getSource().is(DamageTypeTags.BYPASSES_EFFECTS)) {
                return;
            }
            LivingEntity hurtOne = evt.getEntity();
            LivingEntity attacker = null;
            boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
            if (evt.getSource().getEntity() instanceof LivingEntity livingEntity){
                attacker = livingEntity;
            }

            if (!hurtOne.level().isClientSide && attacker != null) {
                MobEffectInstance powers = attacker.getEffect(ImmortalersDelightMobEffect.PREHISTORIC_POWERS.get());
                MobEffectInstance strength = attacker.getEffect(MobEffects.DAMAGE_BOOST);
                if (powers != null && strength != null && powers.getEffect() instanceof BaseMobEffect effect){
                    int lv = Math.min(effect.getTruthUsingAmplifier(powers.getAmplifier()) + 1, strength.getAmplifier() + 1);
                    if (isPowerful) lv *= 2;
                    double damage = (Math.pow(1.3,lv) - 1)/0.3;
                    evt.setAmount(evt.getAmount() + (float)damage);
                }
            }
        }
    }
}
