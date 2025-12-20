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
            .comment("Greatly enhance effects and monsters. Use for that games using mods with additional cultivation content- such as Curios, any Skill mods or Guns mods.")
            .comment("true: Always enabled this mode.")
            .comment("default: Automatically determine whether to enable it based on the player's combat performance.")
            .comment("false: Never enabled this mode.")
            .define("powerBattleMode", "default");

    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> TERRACOTTA_GOLEM_SIDE_DECORATES = BUILDER
            .comment("A list of items that can use on side of terracotta golem.")
            .comment("You can add other item to this list, it's texture must named be the same as item id.")
            .defineListAllowEmpty("items", List.of("minecraft:iron_ingot"), Config::validateItemName);

    public static final ForgeConfigSpec.ConfigValue<List<? extends List<?>>> REVERSE_NORMAL_EFFECT;
    public static final ForgeConfigSpec.ConfigValue<List<? extends List<?>>> REVERSE_INSTANT_EFFECT;
    private static final ForgeConfigSpec.BooleanValue RIGHT_CLICK_HARVEST = BUILDER.comment("After opening, you can right-click to harvest the crops of the module").define("rightClickHarvest", true);
    private static final ForgeConfigSpec.BooleanValue POWER_BATTLE_MODE_HINT = BUILDER.comment("After being turned off, when the Power Battle Mode is enabled, the prompt field will no longer be displayed in the game").define("powerBattleModeHint", true);
    private static final ForgeConfigSpec.BooleanValue POWER_BATTLE_MODE_STRENGTHEN_THE_ENEMIES = BUILDER
            .comment("We apologize for making players excessively capable. ")
            .comment("For the sake of balance, you can use this option to synchronously strengthen some of the original enemies.")
            .comment("Configure which enemies can be strengthened in the entity tags file.")
            .define("needStrengthenTheEnemies", false);
    private static final ForgeConfigSpec.DoubleValue MININ_PROBABILITY = BUILDER.comment("Set the probability of the sniffer beast mining Mod items").defineInRange("mininProbability", 0.5,0.0,1.0);
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
    public static double mininProbability;

    public static boolean weakPoisonHealthOverlay;

    public static String powerBattleMode;


    private static boolean validateItemName(final Object obj) {
        return obj instanceof final String itemName && ForgeRegistries.ITEMS.containsKey(new ResourceLocation(itemName));
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
        mininProbability = MININ_PROBABILITY.get();

    }
}
