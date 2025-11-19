package com.renyigesai.immortalers_delight.init;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.potion.*;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ImmortalersDelightMobEffect {

    public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, ImmortalersDelightMod.MODID);
    public static final RegistryObject<MobEffect> INCANDESCENCE = REGISTRY.register("incandescence", IncandescenceMobEffect::new);


    public static final RegistryObject<MobEffect> LINGERING_FLAVOR = REGISTRY.register("lingering_flavor", IncandescenceMobEffect::new);
    public static final RegistryObject<MobEffect> WEAK_POISON = REGISTRY.register("weak_poison", DamageOnTimeMobEffect::new);
    public static final RegistryObject<MobEffect> WEAK_WITHER = REGISTRY.register("weak_wither", DamageOnTimeMobEffect::new);
    public static final RegistryObject<MobEffect> RELIEVE_POISON = REGISTRY.register("relieve_poison", RelievePotionEffectMobEffect::new);
    public static final RegistryObject<MobEffect> RESISTANCE_TO_UNDEAD = REGISTRY.register("resistance_to_undead", DamageResistMobEffect::new);
    public static final RegistryObject<MobEffect> RESISTANCE_TO_ARTHROPOD = REGISTRY.register("resistance_to_arthropod", DamageResistMobEffect::new);
    public static final RegistryObject<MobEffect> RESISTANCE_TO_ABYSSAL = REGISTRY.register("resistance_to_abyssal", DamageResistMobEffect::new);
    public static final RegistryObject<MobEffect> RESISTANCE_TO_ILLAGER = REGISTRY.register("resistance_to_illager", DamageResistMobEffect::new);
    public static final RegistryObject<MobEffect> RESISTANCE_TO_SURROUNDINGS = REGISTRY.register("resistance_to_surroundings", DamageResistMobEffect::new);
//    public static final RegistryObject<MobEffect> GREAT_MISERY = REGISTRY.register("great_misery", RelievePotionEffectMobEffect::new);
    public static final RegistryObject<MobEffect> KEEP_A_FAST = REGISTRY.register("keep_a_fast",KeepFastMobEffect::new);
    public static final RegistryObject<MobEffect> MAGICAL_REVERSE = REGISTRY.register("magical_reverse", MagicalReverseMobEffect::new);

    public static final RegistryObject<MobEffect> GAS_POISON = REGISTRY.register("gas_poison", GasPoisonMobEffect::new);
    public static final RegistryObject<MobEffect> INEBRIATED = REGISTRY.register("inebriated", InebriatedMobEffect::new);
    public static final RegistryObject<MobEffect> BURN_THE_BOATS = REGISTRY.register("burn_the_boats", BurnTheBoatsMobEffect::new);
    public static final RegistryObject<MobEffect> CULTURAL_LEGACY = REGISTRY.register("cultural_legacy", CulturalLegacyMobEffect::new);
    public static final RegistryObject<MobEffect> VITALITY = REGISTRY.register("vitality", VitalityMobEffect::new);
    public static final RegistryObject<MobEffect> SATIATED = REGISTRY.register("satiated", SatiatedMobEffect::new);
    public static final RegistryObject<MobEffect> WARM_CURRENT_SURGES = REGISTRY.register("warm_current_surges", WarmCurrentSurgesMobEffect::new);
    public static final RegistryObject<MobEffect> PREHISTORIC_POWERS = REGISTRY.register("prehistoric_powers", PrehistoricPowersMobEffect::new);
    public static final RegistryObject<MobEffect> COOL = REGISTRY.register("cool", CoolMobEffect::new);
    public static final RegistryObject<MobEffect> DEEPNESS = REGISTRY.register("deepness", DeepnessMobEffect::new);
    public static final RegistryObject<MobEffect> ESTEEMED_GUEST = REGISTRY.register("esteemed_guest", EsteemedGuestMobEffect::new);

    public static final RegistryObject<MobEffect> VULNERABLE = REGISTRY.register("vulnerable", VulnerableMobEffect::new);

    public static final RegistryObject<MobEffect> LINGERING_INFUSION = REGISTRY.register("lingering_infusion",()-> new LingeringInfusionMobEffect().addAttributeModifier(Attributes.ATTACK_DAMAGE,"7aadc50d-fcf7-43f6-a1c6-af5f56246aa7",-3.0, AttributeModifier.Operation.ADDITION));

}
