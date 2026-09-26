package com.renyigesai.immortalers_delight.potion;

import com.google.common.collect.Maps;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public class PrehistoricPowersMobEffect extends BaseMobEffect {
    public static final String USE_OLD_BUFFER = ImmortalersDelightMod.MODID + "_old_prehistoric_powers";
    private final Map<Attribute, AttributeModifier> attributeModifierMap = Maps.newHashMap();

    public PrehistoricPowersMobEffect() {
        super(MobEffectCategory.BENEFICIAL, -39424);
    }

    //每秒刷新属性修改
    @Override
    public void applyEffectTickInControl(@NotNull LivingEntity pLivingEntity, int pAmplifier) {

        super.removeAttributeModifiers(pLivingEntity, pLivingEntity.getAttributes(), pAmplifier);
        //老版buff不基于属性修饰符，所以直接返回
        CompoundTag nbt = pLivingEntity.getPersistentData();
        //检测强化标记，服务端改属性会同步客户端
        if (nbt.contains(USE_OLD_BUFFER) || pLivingEntity.level().isClientSide()) return;
        if (pLivingEntity.hasEffect(MobEffects.DAMAGE_BOOST)) this.addAttributeModifiers(pLivingEntity, pLivingEntity.getAttributes(), pAmplifier);
    }

    @Override
    public boolean isDurationEffectTickInControl(int duration, int amplifier) {
        return duration % 20 == 0;
    }

    @Override
    public void addAttributeModifiers(@NotNull LivingEntity pLivingEntity, @NotNull AttributeMap pAttributeMap, int pAmplifier) {
        //新版buff实现
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

    @Override
    public void removeAttributeModifiers(@NotNull LivingEntity pLivingEntity, @NotNull AttributeMap pAttributeMap, int pAmplifier) {
        CompoundTag nbt = pLivingEntity.getPersistentData();
        //检测强化标记
        if (nbt.contains(USE_OLD_BUFFER)) {
            //如果不是永久强化，令其失效
            if (!nbt.getBoolean(USE_OLD_BUFFER)) nbt.remove(USE_OLD_BUFFER);
        }
        super.removeAttributeModifiers(pLivingEntity,pAttributeMap,pAmplifier);
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

                    CompoundTag nbt = attacker.getPersistentData();
                    //检测强化标记
                    if (nbt.contains(USE_OLD_BUFFER)) {
                        //旧版buff实现
                        float d0 = evt.getAmount();
                        float d1 = 1.3F;
                        int n = (isPowerful && lv > 0 ? 1 : 0) + lv;

                        for(int i = 0; i < n; ++i) {
                            d1 *= !(attacker instanceof Player) ? d1 : 1.3F;
                        }

                        if (isPowerful) {
                            ++d1;
                        }

                        float dn = d0 * d1 + (d1 - 1.0F) / 0.3F;
                        evt.setAmount(dn);

                    } else {
                        //新版buff实现
                        if (isPowerful) lv *= 2;
                        double damage = (Math.pow(1.3,lv) - 1)/0.3;
                        evt.setAmount(evt.getAmount() + (float)damage);
                    }
                }
            }
        }
    }
}
