package com.renyigesai.immortalers_delight.init;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.potion.IncandescenceMobEffect;
import com.renyigesai.immortalers_delight.potion.DamageOnTimeMobEffect;
import com.renyigesai.immortalers_delight.potion.DamageResistMobEffect;
import com.renyigesai.immortalers_delight.potion.RelievePotionEffectMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.common.registry.ModEffects;

public class ImmortalersDelightMobEffect {

    public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, ImmortalersDelightMod.MODID);
    public static final RegistryObject<MobEffect> INCANDESCENCE = REGISTRY.register("incandescence", IncandescenceMobEffect::new);


    public static final RegistryObject<MobEffect> WEAK_POISON = REGISTRY.register("weak_poison", DamageOnTimeMobEffect::new);
    public static final RegistryObject<MobEffect> WEAK_WITHER = REGISTRY.register("weak_wither", DamageOnTimeMobEffect::new);
    public static final RegistryObject<MobEffect> RELIEVE_POISON = REGISTRY.register("relieve_poison", RelievePotionEffectMobEffect::new);
    public static final RegistryObject<MobEffect> RESISTANCE_TO_UNDEAD = REGISTRY.register("resistance_to_undead", DamageResistMobEffect::new);

    public static final RegistryObject<MobEffect> RESISTANCE_TO_ARTHROPOD = REGISTRY.register("resistance_to_arthropod", DamageResistMobEffect::new);

    public static final RegistryObject<MobEffect> RESISTANCE_TO_ABYSSAL = REGISTRY.register("resistance_to_abyssal", DamageResistMobEffect::new);

    public static final RegistryObject<MobEffect> RESISTANCE_TO_ILLAGER = REGISTRY.register("resistance_to_illager", DamageResistMobEffect::new);
    public static final RegistryObject<MobEffect> GREAT_MISERY = REGISTRY.register("great_misery", RelievePotionEffectMobEffect::new);
    public static final RegistryObject<MobEffect> MAGICAL_REVERSE = REGISTRY.register("magical_reverse", IncandescenceMobEffect::new);

    public static final RegistryObject<MobEffect> GAS_POISON = REGISTRY.register("gas_poison", IncandescenceMobEffect::new);

    public static final RegistryObject<MobEffect> AFTERTASTE = REGISTRY.register("aftertaste", IncandescenceMobEffect::new);
}
