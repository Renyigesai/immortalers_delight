package com.renyigesai.immortalers_delight.potion;

import com.google.common.collect.Maps;
import com.renyigesai.immortalers_delight.Config;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.message.ImmortalersEffectPayload;
import com.renyigesai.immortalers_delight.network.ImmortalersNetwork;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import com.renyigesai.immortalers_delight.util.task.TimekeepingTask;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InfernalForgingMobEffect extends BaseMobEffect {

    private static final Map<UUID, Long> entitiesLastAttack = new HashMap<>();
    private static final Map<UUID, Byte> entitiesWithCharges = new HashMap<>();
    private static final Map<UUID, Byte> entitiesWithStacks = Maps.newHashMap();
    private final Map<Holder<Attribute>, AttributeModifier> attributeModifierMap = Maps.newHashMap();

    public static HashMap<UUID, Byte> getEntitiesWithStacks() {
        return new HashMap<>(entitiesWithStacks);
    }

    public float tipBuffer = 1;

    public InfernalForgingMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 11674146);
    }

    @Override
    public MobEffect addAttributeModifier(Holder<Attribute> attribute, ResourceLocation id, double amount, AttributeModifier.Operation operation) {
        AttributeModifier attributemodifier = new AttributeModifier(id, amount, operation);
        this.attributeModifierMap.put(attribute, attributemodifier);
        return super.addAttributeModifier(attribute, id, amount, operation);
    }

    private static byte needReduceCharges(LivingEntity living, int amplifier) {
        HashMap<UUID, Long> bufferL = new HashMap<>(entitiesLastAttack);
        Long lastTime = bufferL.get(living.getUUID());
        if (lastTime == null) return (byte) 99;
        if (entitiesWithCharges.get(living.getUUID()) == null && entitiesWithStacks.get(living.getUUID()) == null) return (byte) 0;

        byte mode;
        boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
        if (amplifier <= 2) {
            mode = (byte) 1;
        } else if (amplifier <= 6) {
            mode = (byte) 2;
        } else if (amplifier <= 14) {
            mode = (byte) 3;
        } else {
            mode = (byte) 4;
        }
        if (isPowerful) mode--;

        Long time = TimekeepingTask.getImmortalTickTime();

        if (mode == 0) {
            return (byte) 0;
        } else if (mode == 1) {
            return (byte) ((time - lastTime >= 4000) ? 1 : 0);
        } else if (mode == 2) {
            return (byte) ((time - lastTime >= 3500) ? 1 : 0);
        } else if (mode == 3) {
            return (byte) ((time % 1000 > 500) ? 1 : 0);
        } else {
            return (byte) ((time - lastTime >= 4000) ? 99 : 0);
        }
    }

    @Override
    protected void applyEffectTickInControl(LivingEntity livingEntity, int amplifier) {
        byte b = needReduceCharges(livingEntity, amplifier);
        if (!livingEntity.level().isClientSide() && b > 0) {
            HashMap<UUID, Byte> bufferC = new HashMap<>(entitiesWithCharges);
            HashMap<UUID, Byte> bufferS = new HashMap<>(entitiesWithStacks);

            byte charge = bufferC.getOrDefault(livingEntity.getUUID(), (byte) 0);
            byte buffStack = bufferS.getOrDefault(livingEntity.getUUID(), (byte) 0);
            boolean needChange = false;
            if (charge > 0) {
                if (charge - b > 0) entitiesWithCharges.put(livingEntity.getUUID(), (byte) (charge - b));
                else entitiesWithCharges.remove(livingEntity.getUUID());
                needChange = true;
            } else if (buffStack > 0) {
                if (buffStack - b > 0) entitiesWithStacks.put(livingEntity.getUUID(), (byte) (buffStack - b));
                else entitiesWithStacks.remove(livingEntity.getUUID());
                needChange = true;
            }

            if (needChange && livingEntity instanceof ServerPlayer player) {
                boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
                float buffer = 1;
                if (!player.hasEffect(ImmortalersDelightMobEffect.INCANDESCENCE)) buffer = 0.6f;

                int maxCharge = getMaxCharges(amplifier, buffer);
                int progress = isPowerful ? charge - b : charge + buffStack - b;
                float apl = (float) progress / maxCharge;
                if (apl > 1) apl = 1;

                ImmortalersNetwork.sendNonLocal(new ImmortalersEffectPayload(2, apl), player);
            }

            this.refreshChargeModifiers(livingEntity, livingEntity.getAttributes(), amplifier);
        }
    }

    @Override
    public boolean isDurationEffectTickInControl(int duration, int amplifier) {
        return duration % 10 == 0;
    }

    @Override
    public void removeAttributeModifiers(@NotNull AttributeMap pAttributeMap) {
        entitiesWithStacks.clear();
        // keep maps keyed by entity; clear only happens per-entity via onEffectRemoved style below
        super.removeAttributeModifiers(pAttributeMap);
    }

    public void clearEntityCaches(LivingEntity livingEntity) {
        entitiesWithStacks.remove(livingEntity.getUUID());
        entitiesWithCharges.remove(livingEntity.getUUID());
        entitiesLastAttack.remove(livingEntity.getUUID());
        if (livingEntity instanceof ServerPlayer player && !player.level().isClientSide()) {
            ImmortalersNetwork.sendNonLocal(new ImmortalersEffectPayload(2, 0f), player);
        }
    }

    public void refreshChargeModifiers(@NotNull LivingEntity pLivingEntity, @NotNull AttributeMap pAttributeMap, int pAmplifier) {
        HashMap<UUID, Byte> bufferC = new HashMap<>(entitiesWithCharges);

        for (Map.Entry<Holder<Attribute>, AttributeModifier> entry : this.attributeModifierMap.entrySet()) {
            if (entry.getKey().is(Attributes.ATTACK_DAMAGE)) continue;

            Byte charges = bufferC.get(pLivingEntity.getUUID());
            if (charges == null) return;

            AttributeInstance attributeinstance = pAttributeMap.getInstance(entry.getKey());
            if (attributeinstance != null && charges > 0) {
                AttributeModifier attributemodifier = entry.getValue();
                attributeinstance.removeModifier(attributemodifier.id());
                double amount = this.getAttributeModifierValueSimple(pAmplifier, attributemodifier) * charges;
                attributeinstance.addPermanentModifier(new AttributeModifier(attributemodifier.id(), amount, attributemodifier.operation()));
            }
        }
    }

    public @NotNull Map<Holder<Attribute>, AttributeModifier> getAttributeModifiersSimple() {
        return this.attributeModifierMap;
    }

    public double getAttributeModifierValue(int pAmplifier, @NotNull AttributeModifier pModifier) {
        double d = pModifier.amount() * (double) (pAmplifier + 2);
        if (System.currentTimeMillis() % 4000 > 2000) d *= getMaxCharges(pAmplifier, tipBuffer);
        return d * Config.infernal_forging_attribute_multiplier;
    }

    public double getAttributeModifierValueSimple(int pAmplifier, @NotNull AttributeModifier pModifier) {
        return pModifier.amount() * (double) (pAmplifier + 2) * Config.infernal_forging_attribute_multiplier;
    }

    public static int getMaxCharges(int lv, float buffer) {
        int highestBit = Integer.highestOneBit(lv + 1);
        int value = (48 + highestBit - 1) / highestBit;
        float output = Math.max(1, value) * buffer;
        return Math.round(output);
    }

    @OnlyIn(Dist.CLIENT)
    @EventBusSubscriber(modid = ImmortalersDelightMod.MODID, value = Dist.CLIENT)
    public static class InfernalForgingClientEffect {
        @SubscribeEvent
        public static void onPlayerTick(EntityTickEvent.Post evt) {
            if (!(evt.getEntity() instanceof Player)) return;
            Player player1 = Minecraft.getInstance().player;
            float buffer = 1;
            if (player1 != null) {
                if (!player1.hasEffect(ImmortalersDelightMobEffect.INCANDESCENCE)) buffer = 0.6f;
            }

            if (ImmortalersDelightMobEffect.INFERNAL_FORGING.get() instanceof InfernalForgingMobEffect effect) {
                effect.tipBuffer = buffer;
            }
        }
    }

    @EventBusSubscriber(modid = ImmortalersDelightMod.MODID)
    public static class InfernalForgingMobPotionEffect {
        private static final Map<UUID, Float> entitiesFearFire = new HashMap<>();
        private static final Map<UUID, Boolean> playersNeedMSG = new HashMap<>();

        @SubscribeEvent
        public static void onPlayerTick(EntityTickEvent.Post evt) {
            if (!(evt.getEntity() instanceof Player player)) return;

            if (!player.level().isClientSide() && playersNeedMSG.get(player.getUUID()) != null) {
                HashMap<UUID, Byte> bufferC = new HashMap<>(entitiesWithCharges);
                HashMap<UUID, Byte> bufferS = new HashMap<>(entitiesWithStacks);

                byte charge = bufferC.getOrDefault(player.getUUID(), (byte) 0);
                byte buffStack = bufferS.getOrDefault(player.getUUID(), (byte) 0);

                MobEffectInstance effectInstance = player.getEffect(ImmortalersDelightMobEffect.INFERNAL_FORGING);
                if (effectInstance != null && effectInstance.getEffect().value() instanceof BaseMobEffect effect) {
                    int amplifier = effect.getTruthUsingAmplifier(effectInstance.getAmplifier());

                    float buffer = 1;
                    if (!player.hasEffect(ImmortalersDelightMobEffect.INCANDESCENCE)) buffer = 0.6f;

                    if (player instanceof ServerPlayer serverPlayer) {
                        boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
                        int maxCharge = getMaxCharges(amplifier, buffer);
                        int progress = isPowerful ? charge : charge + buffStack;
                        float apl = (float) progress / maxCharge;
                        if (apl > 1) apl = 1;
                        ImmortalersNetwork.sendNonLocal(new ImmortalersEffectPayload(2, apl), serverPlayer);
                    }
                }

                playersNeedMSG.remove(player.getUUID());
            }

            // Clear caches when effect ends
            if (!player.level().isClientSide() && !player.hasEffect(ImmortalersDelightMobEffect.INFERNAL_FORGING)) {
                if (entitiesWithCharges.containsKey(player.getUUID()) || entitiesWithStacks.containsKey(player.getUUID())) {
                    if (ImmortalersDelightMobEffect.INFERNAL_FORGING.get() instanceof InfernalForgingMobEffect effect) {
                        effect.clearEntityCaches(player);
                    }
                }
            }
        }

        @SubscribeEvent
        public static void onCreatureAttack(LivingDamageEvent.Pre evt) {
            if (evt.getSource().is(DamageTypeTags.BYPASSES_EFFECTS)) {
                return;
            }
            LivingEntity hurtOne = evt.getEntity();
            LivingEntity attacker = null;
            boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();

            if (evt.getSource().getEntity() instanceof LivingEntity livingEntity) {
                attacker = livingEntity;
            }

            HashMap<UUID, Byte> bufferC = new HashMap<>(entitiesWithCharges);
            HashMap<UUID, Byte> bufferS = new HashMap<>(entitiesWithStacks);

            if (!hurtOne.level().isClientSide && attacker != null) {
                if (!isPowerful) {
                    AttributeInstance attr = attacker.getAttribute(Attributes.ATTACK_DAMAGE);
                    double atk = attr == null ? 0 : attr.getValue();
                    if (evt.getNewDamage() < 1 || (evt.getNewDamage() < atk)) return;
                }

                MobEffectInstance effectInstance = attacker.getEffect(ImmortalersDelightMobEffect.INFERNAL_FORGING);
                if (effectInstance != null && effectInstance.getEffect().value() instanceof InfernalForgingMobEffect effect) {
                    int amplifier = effect.getTruthUsingAmplifier(effectInstance.getAmplifier());
                    float buffer = 1;
                    if (!attacker.hasEffect(ImmortalersDelightMobEffect.INCANDESCENCE)) buffer = 0.6f;

                    byte maxCharges = (byte) getMaxCharges(amplifier, buffer);

                    byte charge = bufferC.getOrDefault(attacker.getUUID(), (byte) 0);
                    byte buffStack = bufferS.getOrDefault(attacker.getUUID(), (byte) 0);
                    if (isPowerful) {
                        if (charge + 1 <= maxCharges) entitiesWithCharges.put(attacker.getUUID(), (byte) (charge + 1));
                        if (buffStack + 1 <= maxCharges) entitiesWithStacks.put(attacker.getUUID(), (byte) (buffStack + 1));
                    } else {
                        byte sum = (byte) (charge + buffStack);
                        if (sum + 1 > maxCharges) return;

                        AttributeInstance attributeinstance = attacker.getAttribute(Attributes.ATTACK_SPEED);
                        if (attributeinstance != null && attributeinstance.getValue() < 4.0f) {
                            entitiesWithCharges.put(attacker.getUUID(), (byte) (charge + 1));
                        } else {
                            entitiesWithStacks.put(attacker.getUUID(), (byte) (buffStack + 1));
                        }
                    }
                    entitiesLastAttack.put(attacker.getUUID(), TimekeepingTask.getImmortalTickTime());

                    AttributeModifier attackDamage = effect.getAttributeModifiersSimple().get(Attributes.ATTACK_DAMAGE);
                    if (attackDamage != null) {
                        float amount = (float) (effect.getAttributeModifierValueSimple(amplifier, attackDamage) * buffStack);
                        entitiesFearFire.put(hurtOne.getUUID(), amount);
                    }
                    effect.refreshChargeModifiers(attacker, attacker.getAttributes(), amplifier);

                    if (attacker instanceof Player player) {
                        playersNeedMSG.put(player.getUUID(), true);
                    }
                }
            }

            HashMap<UUID, Float> bufferF = new HashMap<>(entitiesFearFire);
            if (!hurtOne.level().isClientSide && evt.getSource().is(DamageTypeTags.IS_FIRE)) {
                if (bufferF.get(hurtOne.getUUID()) != null) {
                    float amount = bufferF.get(hurtOne.getUUID());
                    evt.setNewDamage(evt.getNewDamage() * (1 + amount));
                    entitiesFearFire.remove(hurtOne.getUUID());
                }
            }
        }
    }
}
