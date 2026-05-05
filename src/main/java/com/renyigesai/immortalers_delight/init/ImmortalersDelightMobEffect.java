package com.renyigesai.immortalers_delight.init;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.potion.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ImmortalersDelightMobEffect {

    public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(Registries.MOB_EFFECT, ImmortalersDelightMod.MODID);
    public static final DeferredHolder<MobEffect, MobEffect> INCANDESCENCE = REGISTRY.register("incandescence", IncandescenceMobEffect::new);


    public static final DeferredHolder<MobEffect, MobEffect> LINGERING_FLAVOR = REGISTRY.register("lingering_flavor", IncandescenceMobEffect::new);
    public static final DeferredHolder<MobEffect, MobEffect> WEAK_POISON = REGISTRY.register("weak_poison", DamageOnTimeMobEffect::new);
    public static final DeferredHolder<MobEffect, MobEffect> WEAK_WITHER = REGISTRY.register("weak_wither", DamageOnTimeMobEffect::new);
    public static final DeferredHolder<MobEffect, MobEffect> RELIEVE_POISON = REGISTRY.register("relieve_poison", RelievePotionEffectMobEffect::new);
    public static final DeferredHolder<MobEffect, MobEffect> RESISTANCE_TO_UNDEAD = REGISTRY.register("resistance_to_undead", DamageResistMobEffect::new);
    public static final DeferredHolder<MobEffect, MobEffect> RESISTANCE_TO_ARTHROPOD = REGISTRY.register("resistance_to_arthropod", DamageResistMobEffect::new);
    public static final DeferredHolder<MobEffect, MobEffect> RESISTANCE_TO_ABYSSAL = REGISTRY.register("resistance_to_abyssal", DamageResistMobEffect::new);
    public static final DeferredHolder<MobEffect, MobEffect> RESISTANCE_TO_ILLAGER = REGISTRY.register("resistance_to_illager", DamageResistMobEffect::new);
    public static final DeferredHolder<MobEffect, MobEffect> RESISTANCE_TO_SURROUNDINGS = REGISTRY.register("resistance_to_surroundings", DamageResistMobEffect::new);
//    public static final DeferredHolder<MobEffect, MobEffect> GREAT_MISERY = REGISTRY.register("great_misery", RelievePotionEffectMobEffect::new);
    public static final DeferredHolder<MobEffect, MobEffect> KEEP_A_FAST = REGISTRY.register("keep_a_fast",KeepFastMobEffect::new);
    public static final DeferredHolder<MobEffect, MobEffect> MAGICAL_REVERSE = REGISTRY.register("magical_reverse", MagicalReverseMobEffect::new);

    public static final DeferredHolder<MobEffect, MobEffect> GAS_POISON = REGISTRY.register("gas_poison", GasPoisonMobEffect::new);
    public static final DeferredHolder<MobEffect, MobEffect> INEBRIATED = REGISTRY.register("inebriated", InebriatedMobEffect::new);
    public static final DeferredHolder<MobEffect, MobEffect> BURN_THE_BOATS = REGISTRY.register("burn_the_boats", BurnTheBoatsMobEffect::new);
    public static final DeferredHolder<MobEffect, MobEffect> CULTURAL_LEGACY = REGISTRY.register("cultural_legacy", CulturalLegacyMobEffect::new);
    public static final DeferredHolder<MobEffect, MobEffect> VITALITY = REGISTRY.register("vitality", VitalityMobEffect::new);
    public static final DeferredHolder<MobEffect, MobEffect> SATIATED = REGISTRY.register("satiated", SatiatedMobEffect::new);
    public static final DeferredHolder<MobEffect, MobEffect> WARM_CURRENT_SURGES = REGISTRY.register("warm_current_surges", WarmCurrentSurgesMobEffect::new);
    public static final DeferredHolder<MobEffect, MobEffect> PREHISTORIC_POWERS = REGISTRY.register("prehistoric_powers",()->
            new PrehistoricPowersMobEffect()
                    .addAttributeModifier(Attributes.ATTACK_DAMAGE, ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "prehistoric_attack_damage"), 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    public static final DeferredHolder<MobEffect, MobEffect> COOL = REGISTRY.register("cool", CoolMobEffect::new);
    public static final DeferredHolder<MobEffect, MobEffect> DEEPNESS = REGISTRY.register("deepness", DeepnessMobEffect::new);
    public static final DeferredHolder<MobEffect, MobEffect> ESTEEMED_GUEST = REGISTRY.register("esteemed_guest", EsteemedGuestMobEffect::new);

    public static final DeferredHolder<MobEffect, MobEffect> VULNERABLE = REGISTRY.register("vulnerable", VulnerableMobEffect::new);

    public static final DeferredHolder<MobEffect, MobEffect> LINGERING_INFUSION = REGISTRY.register("lingering_infusion",()->
            new LingeringInfusionMobEffect()
            .addAttributeModifier(Attributes.ATTACK_DAMAGE, ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "lingering_infusion_attack_damage"), -3.0, AttributeModifier.Operation.ADD_VALUE));

    public static final DeferredHolder<MobEffect, MobEffect> GAIXIA = REGISTRY.register("gaixia", GaixiaMobEffect::new);
    public static final DeferredHolder<MobEffect, MobEffect> LET_IT_FREEZE = REGISTRY.register("let_it_freeze", LetItFreezeMobEffect::new);

    public static final DeferredHolder<MobEffect, MobEffect> UNYIELDING = REGISTRY.register("unyielding", UnyieldingMobEffect::new);
    public static final DeferredHolder<MobEffect, MobEffect> UP_SIDE_DOWN = REGISTRY.register("up_side_down", UpSideDownMobEffect::new);

    public static final DeferredHolder<MobEffect, MobEffect> SMOKE_ABSTINENCE = REGISTRY.register("smoke_abstinence", ()->
            new SmokeAbstinenceEffect()
                    .addAttributeModifier(Attributes.ATTACK_DAMAGE, ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "smoke_abstinence_attack_damage"), 3.0D, AttributeModifier.Operation.ADD_VALUE)
                    .addAttributeModifier(Attributes.ATTACK_SPEED, ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "smoke_abstinence_attack_speed"), (double)0.1F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    public static final DeferredHolder<MobEffect, MobEffect> MOONBRIGHT = REGISTRY.register("moon_bright", MoonBrightMobEffect::new);

}
