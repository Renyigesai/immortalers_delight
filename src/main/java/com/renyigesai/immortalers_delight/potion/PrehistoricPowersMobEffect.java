package com.renyigesai.immortalers_delight.potion;

import com.google.common.collect.Maps;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public class PrehistoricPowersMobEffect extends MobEffect {
    private final Map<Attribute, AttributeModifier> attributeModifierMap = Maps.newHashMap();

    public PrehistoricPowersMobEffect() {
        super(MobEffectCategory.BENEFICIAL, -39424);
    }

    //每秒刷新属性修改
    @Override
    public void applyEffectTick(@NotNull LivingEntity pLivingEntity, int pAmplifier) {
        this.removeAttributeModifiers(pLivingEntity, pLivingEntity.getAttributes(), pAmplifier);
        if (pLivingEntity.hasEffect(MobEffects.DAMAGE_BOOST)) this.addAttributeModifiers(pLivingEntity, pLivingEntity.getAttributes(), pAmplifier);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }

    @Override
    public void addAttributeModifiers(@NotNull LivingEntity pLivingEntity, @NotNull AttributeMap pAttributeMap, int pAmplifier) {
        int lv = 0;
        MobEffectInstance instance = pLivingEntity.getEffect(MobEffects.DAMAGE_BOOST);
        if (instance != null) lv = Math.min(instance.getAmplifier(), pAmplifier);
        super.addAttributeModifiers(pLivingEntity, pAttributeMap, lv);
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
}
