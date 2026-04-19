package com.renyigesai.immortalers_delight;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.stream.Collectors;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = ImmortalersDelightMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    static final ForgeConfigSpec SPEC;
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue LOG_DIRT_BLOCK = BUILDER.comment("Whether to log the dirt block on common setup").define("logDirtBlock", true);

    private static final ForgeConfigSpec.IntValue MAGIC_NUMBER = BUILDER.comment("A magic number").defineInRange("magicNumber", 42, 0, Integer.MAX_VALUE);

    public static final ForgeConfigSpec.ConfigValue<String> MAGIC_NUMBER_INTRODUCTION = BUILDER.comment("What you want the introduction message to be for the magic number").define("magicNumberIntroduction", "The magic number is... ");

    // a list of strings that are treated as resource locations for items
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS = BUILDER.comment("A list of items to log on common setup.").defineListAllowEmpty("items", List.of("minecraft:iron_ingot"), Config::validateItemName);

    private static final ForgeConfigSpec.BooleanValue WEAK_POISON_HEALTH_OVERLAY = BUILDER.comment("Whether to enable the health value display override for the weak potion effect").define("useWeakPoisonOverLay", true);

    private static final ForgeConfigSpec.IntValue ANCIENT_BOAT_NEEDED_1_NUMBER = BUILDER.comment("The number of #ancient_boat_need_1 that need to be held in hand to repair the ancient boat.").defineInRange("count of [ancient_boat_need_1]", 5, 0, Short.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue ANCIENT_BOAT_NEEDED_2_NUMBER = BUILDER.comment("The number of #ancient_boat_need_2 that need to be held in hand to repair the ancient boat.").defineInRange("count of [ancient_boat_need_2]", 2, 0, Short.MAX_VALUE);
    private static final ForgeConfigSpec.IntValue ANCIENT_CHEST_BOAT_NEEDED_1_NUMBER = BUILDER.comment("The number of #ancient_chest_boat_need_1 that need to be held in hand to repair the ancient chest boat.").defineInRange("count of [ancient_chest_boat_need_1]", 5, 0, Short.MAX_VALUE);
    private static final ForgeConfigSpec.IntValue ANCIENT_CHEST_BOAT_NEEDED_2_NUMBER = BUILDER.comment("The number of #ancient_chest_boat_need_2 that need to be held in hand to repair the ancient chest boat.").defineInRange("count of [ancient_chest_boat_need_2]", 1, 0, Short.MAX_VALUE);

    public static final ForgeConfigSpec.ConfigValue<String> POWER_BATTLE_MODE = BUILDER
            .comment(" ")
            .comment("Greatly enhance effects and monsters. Use for that games using mods with additional cultivation content- such as Curios, any Skill mods or Guns mods.")
            .comment("true: Always enabled this mode.")
            .comment("default: Automatically determine whether to enable it based on the player's combat performance.")
            .comment("false: Never enabled this mode.")
            .define("powerBattleMode", "default");

    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> TERRACOTTA_GOLEM_SIDE_DECORATES = BUILDER
            .comment(" ")
            .comment("A list of items that can use on side of terracotta golem.")
            .comment("You can add other item to this list, it's texture must named be the same as item id.")
            .defineListAllowEmpty("items", List.of("minecraft:iron_ingot"), Config::validateItemName);

    public static final ForgeConfigSpec.ConfigValue<List<? extends List<?>>> REVERSE_NORMAL_EFFECT;
    public static final ForgeConfigSpec.ConfigValue<List<? extends List<?>>> REVERSE_INSTANT_EFFECT;
    private static final ForgeConfigSpec.BooleanValue RIGHT_CLICK_HARVEST = BUILDER.comment("After opening, you can right-click to harvest the crops of the module").define("rightClickHarvest", true);
    private static final ForgeConfigSpec.BooleanValue POWER_BATTLE_MODE_HINT = BUILDER.comment("After being turned off, when the Power Battle Mode is enabled, the prompt field will no longer be displayed in the game").define("powerBattleModeHint", true);
    private static final ForgeConfigSpec.BooleanValue POWER_BATTLE_MODE_STRENGTHEN_THE_ENEMIES = BUILDER
            .comment(" ")
            .comment("We apologize for making players excessively capable. ")
            .comment("For the sake of balance, you can use this option to synchronously strengthen some of the original enemies.")
            .comment("Configure which enemies can be strengthened in the entity tags file, to all the functions.")
            .define("needStrengthenTheEnemies", false);
//    private static final ForgeConfigSpec.BooleanValue USE_DYNAMIC_DAMAGE = BUILDER
//            .comment(" ")
//            .comment("Configure the attack damage multiplier of powered mobs be increased based on the target's health value")
//            .define("useDynamicDamage", false);
//    private static final ForgeConfigSpec.ConfigValue<List<? extends Float>> MAXIMUM_ATTACK_DAMAGE_MULTIPLIER = BUILDER
//            .comment(" ")
//            .comment("Set the maximum attack damage multiplier of powered mobs")
//            .comment("If use, it will make damage become (1 + the value) * old damage")
//            .comment("Example: [0.5, 2.0, 5.0],it must have 3 number,to normal mobs,elite mobs,mod bosses.")
//            .defineList("maximumDamageMultiplier", List.of(0.5f, 2.0f, 5.0f), o -> o instanceof Float);
//    private static final ForgeConfigSpec.ConfigValue<List<? extends Float>> ATTACK_DAMAGE_MULTIPLIER_PER_HEALTH = BUILDER
//            .comment(" ")
//            .comment("Set the attack damage multiplier per target's health")
//            .comment("Example: [0.002, 0.006, 0.02],it must have 3 number,to normal mobs,elite mobs,mod bosses.")
//            .comment("Use gradient calculation. For example, for elite mobs, follow the default value:")
//            .comment(" the damage multiplier increases by 0.002 per health point until it reaches 1.5x (1+0.5), ")
//            .comment("then the damage multiplier increases by 0.006 per health point until it reaches 3.0x (1+2.0). ")
//            .defineList("damageMultiplierPerHealth", List.of(0.002f, 0.006f, 0.02f), o -> o instanceof Float);
//    private static final ForgeConfigSpec.BooleanValue USE_MIN_DAMAGE = BUILDER
//            .comment(" ")
//            .comment("Configure the min value when powered mobs attack")
//            .define("useMinDamage", false);
//    private static final ForgeConfigSpec.ConfigValue<List<? extends Float>> MIN_DAMAGE = BUILDER
//            .comment(" ")
//            .comment("Set the min value when powered mobs attack")
//            .comment("Example: [1.0, 2.0, 2.5],it must have 3 number,to normal mobs,elite mobs,mod bosses.")
//            .defineList("minDamageValue", List.of(1.0f, 2.0f, 2.5f), o -> o instanceof Float);
//    private static final ForgeConfigSpec.BooleanValue USE_HIGH_DAMAGE_COUNTERACTION = BUILDER
//            .comment(" ")
//            .comment("Configure the damage multiplier taken by mobs will decrease as the base damage value increases.")
//            .define("useHigh-DamageCounteraction", true);
//    private static final ForgeConfigSpec.ConfigValue<List<? extends Float>> MAXIMUM_DAMAGE_COUNTERACTION = BUILDER
//            .comment(" ")
//            .comment("Set the maximum damage divisor taken by mobs")
//            .comment("For example,if set 9.0 to a mob, damage taken by mobs will min become 0.1x.")
//            .comment("Example: [7.0, 11.0, 15.0],it must have 3 number,to normal mobs,elite mobs,mod bosses.")
//            .defineList("maximumDamageDivisor", List.of(7.0f, 11.0f, 15.0f), o -> o instanceof Float);
//    private static final ForgeConfigSpec.ConfigValue<List<? extends Float>> DAMAGE_COUNTERACTION_PER_DAMAGE = BUILDER
//            .comment(" ")
//            .comment("Set the attack damage divisor per damage taken by mobs")
//            .comment("Example: [0.04, 0.05, 0.08],it must have 3 number,to normal mobs,elite mobs,mod bosses.")
//            .defineList("damageDivisorPerDamage", List.of(0.04f, 0.05f, 0.08f), o -> o instanceof Float);
    private static final ForgeConfigSpec.BooleanValue USE_BETTER_STUN = BUILDER
            .comment(" ")
            .comment("Configures whether to ignore the entity's potion effect immunity when the stun effect derives other potion effects.")
            .comment("This allows stun to have a more consistent effect on different mobs.")
            .define("useBetterStun", true);
    private static final ForgeConfigSpec.DoubleValue MININ_PROBABILITY = BUILDER.comment("Set the probability of the sniffer beast mining Mod items").defineInRange("mininProbability", 0.5,0.0,1.0);


    public static final ForgeConfigSpec.ConfigValue<List<? extends List<?>>> EFFECTS_USING_LINEAR_GROWTH;
    public static final ForgeConfigSpec.ConfigValue<List<? extends List<?>>> EFFECTS_USING_CLASSICAL_HARMONIC_SERIES_GROWTH;
    public static final ForgeConfigSpec.ConfigValue<List<? extends List<?>>> EFFECTS_USING_GRADUAL_HARMONIC_SERIES_GROWTH;
    public static final ForgeConfigSpec.ConfigValue<List<? extends List<?>>> MOB_DYNAMIC_DAMAGE;
    public static final ForgeConfigSpec.ConfigValue<List<? extends List<?>>> MOB_MIN_DAMAGE;
    public static final ForgeConfigSpec.ConfigValue<List<? extends List<?>>> MOB_HIGH_DAMAGE_COUNTERACTION;

    static {
        BUILDER.push("ReverseNormalEffect")
                .comment("Potion effects that can be reversed. Effect in this Map will be remove every tick. ",
                        "It does not prevent the application of effects, so it may not completely block the activation of harmful potion effects sometimes if only use this.",
                        "\"input-effect-n\" means ID of the effect to be converted,\"output-effect-n\" means ID of the effect that conversion result.",
                        "Format: [[\"input-effect-1\", \"output-effect-1\"], [\"input-effect-2\", \"output-effect-2\"], ...etc]");
        REVERSE_NORMAL_EFFECT = BUILDER
                .defineList("reverseNormalEffect", Arrays.asList
                                (
                                        Arrays.asList("minecraft:bad_omen", "minecraft:hero_of_the_village"),
                                        Arrays.asList("minecraft:unluck", "minecraft:luck"),
                                        Arrays.asList("minecraft:glowing", "minecraft:invisibility"),
                                        Arrays.asList("minecraft:slowness", "minecraft:speed"),
                                        Arrays.asList("minecraft:levitation", "minecraft:slow_falling"),
                                        Arrays.asList("minecraft:darkness", "minecraft:conduit_power"),
                                        Arrays.asList("minecraft:mining_fatigue", "minecraft:haste"),
                                        Arrays.asList("minecraft:weakness", "minecraft:strength"),
                                        Arrays.asList("minecraft:poison", "immortalers_delight:lingering_infusion"),
                                        Arrays.asList("minecraft:hunger", "immortalers_delight:satiated"),
                                        Arrays.asList("minecraft:wither", "immortalers_delight:vitality"),
                                        Arrays.asList("minecraft:nausea", "farmersdelight:nourishment"),
                                        Arrays.asList("minecraft:blindness", "minecraft:night_vision"),
                                        Arrays.asList("immortalers_delight:weak_poison", "farmersdelight:comfort"),
                                        Arrays.asList("immortalers_delight:weak_wither", "minecraft:regeneration"),
                                        Arrays.asList("twilightforest:frosty", "immortalers_delight:cool"),
                                        Arrays.asList("aether:inebriation", "aether:remedy")

                                ),
                        it -> it instanceof List && ((List<?>) it).get(0) instanceof String && ((List<?>) it).get(1) instanceof String);

        BUILDER.pop();

        BUILDER.push("ReverseInstantEffect")
                .comment("It takes effect when the effect in this Map is being applied, blocking its application and reversing it.",
                        "It only takes effect when the effect is being added, so it alone cannot convert existing effects.",
                        "\"input-effect-n\" means ID of the effect to be converted,\"output-effect-n\" means ID of the effect that conversion result.",
                        "Format: [[\"input-effect-1\", \"output-effect-1\"], [\"input-effect-2\", \"output-effect-2\"], ...etc]");
        REVERSE_INSTANT_EFFECT = BUILDER
                .defineList("reverseInstantEffect", Arrays.asList
                                (
                                        Arrays.asList("minecraft:instant_damage", "minecraft:instant_health"),
                                        Arrays.asList("minecraft:blindness", "minecraft:night_vision")

                                        ),
                        it -> it instanceof List && ((List<?>) it).get(0) instanceof String && ((List<?>) it).get(1) instanceof String);

        BUILDER.pop();

        BUILDER.push("EffectsUsingLinearGrowth")
                .comment("Determine which potion effects use linear growth.",
                        "Each entry should include a string representing the effect ID, along with two numbers,",
                        "the numbers will serving as the additiveTermA and multiplicativeTermB of the growth function respectively.",
                        "The final effective level of the potion effect is \"A + level × B\", where level is the original level of the effect — that is, the level entered when obtaining it via commands.",
                        "If an effect does not appear in any of the three lists, it will take effect according to the rules in this list by default.",
                        "Format: [[\"effect-id-1\", additive-term-A, multiplicative-term-B], [\"effect-id-2\", additive-term-A, multiplicative-term-B], ...etc]");
        EFFECTS_USING_LINEAR_GROWTH = BUILDER
                .defineList("effectsUsingLinearGrowth", Arrays.asList
                                (
                                        Arrays.asList("immortalers_delight:vulnerable", 0, 0.5),
                                        Arrays.asList("immortalers_delight:relieve_poison", 1, 1)

                                ),
                        it -> it instanceof List && ((List<?>) it).get(0) instanceof String && ((List<?>) it).get(1) instanceof Number);

        BUILDER.pop();

        BUILDER.push("EffectsUsingClassicalHarmonicSeriesGrowth")
                .comment("Determine which potion effects use linear growth.",
                        "Each entry should include a string representing the effect ID, along with two numbers,",
                        "the numbers will serving as the additiveTermA and multiplicativeTermB of the growth function respectively.",
                        "The final effective level of the potion effect is \"A + level × B\", ",
                        "The level is calculated using the classic harmonic series. For effects with amplifier = 1 to 4, the values are 1, 1.5, 1.833, and 2.083 respectively.",
                        "Format: [[\"effect-id-1\", additive-term-A, multiplicative-term-B], [\"effect-id-2\", additive-term-A, multiplicative-term-B], ...etc]");
        EFFECTS_USING_CLASSICAL_HARMONIC_SERIES_GROWTH = BUILDER
                .defineList("effectsUsingClassicalHarmonicSeriesGrowth", Arrays.asList
                                (
                                        Arrays.asList("immortalers_delight:gas_poison", 0, 1),
                                        Arrays.asList("immortalers_delight:inebriated", 0, 1),
                                        Arrays.asList("immortalers_delight:vitality", 0, 1)

                                ),
                        it -> it instanceof List && ((List<?>) it).get(0) instanceof String && ((List<?>) it).get(1) instanceof Number);

        BUILDER.pop();

        BUILDER.push("EffectsUsingGradualHarmonicSeriesGrowth")
                .comment("Determine which potion effects use linear growth.",
                        "Each entry should include a string representing the effect ID, along with two numbers,",
                        "the numbers will serving as the additiveTermA and multiplicativeTermB of the growth function respectively.",
                        "The final effective level of the potion effect is \"A + level × B\",",
                        "The level is calculated using a gentle harmonic series. For effects with amplifier values from 1 to 10, the corresponding values are 1, 1.5, 2, 2.333, 2.666, 3, 3.25, 3.5, 3.75, and 4 respectively.",
                        "Format: [[\"effect-id-1\", additive-term-A, multiplicative-term-B], [\"effect-id-2\", additive-term-A, multiplicative-term-B], ...etc]");
        EFFECTS_USING_GRADUAL_HARMONIC_SERIES_GROWTH = BUILDER
                .defineList("effectsUsingGradualHarmonicSeriesGrowth", Arrays.asList
                                (
                                        Arrays.asList("immortalers_delight:prehistoric_powers", -0.5, 1.5),
                                        Arrays.asList("immortalers_delight:resistance_to_undead", 0, 1),
                                        Arrays.asList("immortalers_delight:resistance_to_arthropod", 0, 1),
                                        Arrays.asList("immortalers_delight:resistance_to_abyssal", 0, 1),
                                        Arrays.asList("immortalers_delight:resistance_to_illager", 0, 1),
                                        Arrays.asList("immortalers_delight:warm_current_surges", 0, 1),
                                        Arrays.asList("immortalers_delight:lingering_infusion", 0, 1),
                                        Arrays.asList("immortalers_delight:deepness", 0, 1),
                                        Arrays.asList("immortalers_delight:let_it_freeze", 0, 1),
                                        Arrays.asList("immortalers_delight:moon_bright", 0, 1)

                                ),
                        it -> it instanceof List && ((List<?>) it).get(0) instanceof String && ((List<?>) it).get(1) instanceof Number);

        BUILDER.pop();

        BUILDER.push("MobDynamicDamage")
                .comment("Configure the attack damage multiplier of powered mobs be increased based on the target's health value.",
                        "If use, it will make damage become (1 + the value) * old damage.",
                        "the numbers will be stored as a growth factor A and an upper limit B.",
                        "The damage multiplier will increase by A per point of the target's health until it reaches B.",
                        "This configuration option only takes effect when the needStrengthenTheEnemies option is enabled.",
                        "Format: [[\"mob_or_tag-id-1\", growth-factor-A, upper-limit-B], [\"mob_or_tag-id-2\", growth-factor-A, upper-limit-B], ...etc]");
        MOB_DYNAMIC_DAMAGE = BUILDER
                .defineList("mobDynamicDamage", Arrays.asList
                                (
                                        Arrays.asList("#immortalers_delight:normal_mobs", 0, 1),
                                        Arrays.asList("#immortalers_delight:elite_mobs", 0.002, 1.5),
                                        Arrays.asList("#immortalers_delight:mid_boss", 0.006, 3),
                                        Arrays.asList("twilightforest:minoshroom", 0.02, 6)

                                ),
                        it -> it instanceof List && ((List<?>) it).get(0) instanceof String && ((List<?>) it).get(1) instanceof Number);

        BUILDER.pop();


        BUILDER.push("MobMinDamage")
                .comment("Configure the min value when powered mobs attack.",
                        "If use, it will make the damage have a min value when powered mobs attack, even if you have the [Resistance V].",
                        "the numbers will be stored as a percentage value A and an fixed value B.",
                        "The damage will increase by \"A × target's maxHealth + B\".",
                        "This configuration option only takes effect when the needStrengthenTheEnemies option is enabled.",
                        "Format: [[\"mob_or_tag-id-1\", percentage-value-A, fixed-value-B], [\"mob_or_tag-id-2\", percentage-value-A, fixed-value-B], ...etc]");
        MOB_MIN_DAMAGE = BUILDER
                .defineList("mobMinDamage", Arrays.asList
                                (
                                        Arrays.asList("#immortalers_delight:normal_mobs", 0, 1),
                                        Arrays.asList("#immortalers_delight:elite_mobs", 0, 2),
                                        Arrays.asList("#immortalers_delight:mid_boss", 0, 2.5),
                                        Arrays.asList("irons_spellbooks:dead_king", 0.03, 6)

                                ),
                        it -> it instanceof List && ((List<?>) it).get(0) instanceof String && ((List<?>) it).get(1) instanceof Number);

        BUILDER.pop();


        BUILDER.push("MobHighDamageCounteraction")
                .comment("Configure the damage multiplier taken by mobs will decrease as the base damage value increases.",
                        "If use, it will divide the damage taken by the powered mobs by a coefficient.",
                        "the numbers will be stored as a growth factor A and an upper limit B.",
                        "The damage divisor will increase by A per point of the damage value until it reaches B.",
                        "For example,if set upper limit 9.0 to a mob, damage taken by mobs will min become 0.1x if the damage high enough.",
                        "This configuration option only takes effect when the needStrengthenTheEnemies option is enabled.",
                        "Format: [[\"mob_or_tag-id-1\", growth-factor-A, upper-limit-B], [\"mob_or_tag-id-2\", growth-factor-A, upper-limit-B], ...etc]");
        MOB_HIGH_DAMAGE_COUNTERACTION = BUILDER
                .defineList("mobHighDamageCounteraction", Arrays.asList
                                (
                                        Arrays.asList("#immortalers_delight:normal_mobs", 0.08, 15),
                                        Arrays.asList("#immortalers_delight:elite_mobs", 0.05, 11),
                                        Arrays.asList("#immortalers_delight:mid_boss", 0.04, 7),
                                        Arrays.asList("goety:brood_mother", 0.1, 100)

                                ),
                        it -> it instanceof List && ((List<?>) it).get(0) instanceof String && ((List<?>) it).get(1) instanceof Number);

        BUILDER.pop();


        SPEC = BUILDER.build();
    }

    public static boolean logDirtBlock;
    public static int magicNumber;
    public static int ancientBoatNeeded_1;
    public static int ancientChestBoatNeeded_1;
    public static int ancientBoatNeeded_2;
    public static int ancientChestBoatNeeded_2;
    public static String magicNumberIntroduction;
    public static Set<Item> items;
    public static boolean rightClickHarvest;
    public static boolean powerBattleModeHint;
    public static boolean powerBattleModeStrengthenTheEnemies;
//    public static boolean useDynamicDamage;
//    public static List<Float> maximumAttackDamageMultiplier;
//    public static List<Float> attackDamageMultiplierPerHealth;
//    public static boolean useMinDamage;
//    public static List<Float> minDamage;
//    public static boolean useHighDamageCounteraction;
//    public static List<Float> maximumDamageCounteraction;
//    public static List<Float> damageCounteractionPerDamage;
    public static boolean useBetterStun;
    public static double mininProbability;

    public static boolean weakPoisonHealthOverlay;

    public static String powerBattleMode;


    private static boolean validateItemName(final Object obj) {
        return obj instanceof final String itemName && ForgeRegistries.ITEMS.containsKey(new ResourceLocation(itemName));
    }

    public static List<Float> convertToListFloat(List<?> sourceList) {
        // 1. 创建空的List<Float>（推荐ArrayList，也可用LinkedList）
        List<Float> targetList = new ArrayList<>();

        // 2. 遍历原列表，拷贝所有元素，处理Double和Float类型
        if (sourceList != null) { // 判空，避免空指针
            for (Object obj : sourceList) {
                if (obj instanceof Float) {
                    targetList.add((Float) obj);
                } else if (obj instanceof Double) {
                    targetList.add(((Double) obj).floatValue());
                } else if (obj instanceof Number) {
                    targetList.add(((Number) obj).floatValue());
                }
            }
        }

        return targetList;
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        logDirtBlock = LOG_DIRT_BLOCK.get();
        magicNumber = MAGIC_NUMBER.get();
        ancientBoatNeeded_1 = ANCIENT_BOAT_NEEDED_1_NUMBER.get();
        ancientChestBoatNeeded_1 = ANCIENT_CHEST_BOAT_NEEDED_1_NUMBER.get();
        ancientBoatNeeded_2 = ANCIENT_BOAT_NEEDED_2_NUMBER.get();
        ancientChestBoatNeeded_2 = ANCIENT_CHEST_BOAT_NEEDED_2_NUMBER.get();
        magicNumberIntroduction = MAGIC_NUMBER_INTRODUCTION.get();

        // convert the list of strings into a set of items
        items = ITEM_STRINGS.get().stream().map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).collect(Collectors.toSet());

        weakPoisonHealthOverlay = WEAK_POISON_HEALTH_OVERLAY.get();
        powerBattleMode = POWER_BATTLE_MODE.get();
        rightClickHarvest = RIGHT_CLICK_HARVEST.get();
        powerBattleModeHint = POWER_BATTLE_MODE_HINT.get();

        powerBattleModeStrengthenTheEnemies = POWER_BATTLE_MODE_STRENGTHEN_THE_ENEMIES.get();
//        useDynamicDamage = USE_DYNAMIC_DAMAGE.get();
//        maximumAttackDamageMultiplier = convertToListFloat(MAXIMUM_ATTACK_DAMAGE_MULTIPLIER.get());
//        attackDamageMultiplierPerHealth = convertToListFloat(ATTACK_DAMAGE_MULTIPLIER_PER_HEALTH.get());
//        useMinDamage = USE_MIN_DAMAGE.get();
//        minDamage = convertToListFloat(MIN_DAMAGE.get());
//        useHighDamageCounteraction =  USE_HIGH_DAMAGE_COUNTERACTION.get();
//        maximumDamageCounteraction = convertToListFloat(MAXIMUM_DAMAGE_COUNTERACTION.get());
//        damageCounteractionPerDamage = convertToListFloat(DAMAGE_COUNTERACTION_PER_DAMAGE.get());

        useBetterStun = USE_BETTER_STUN.get();

        mininProbability = MININ_PROBABILITY.get();

    }
}
