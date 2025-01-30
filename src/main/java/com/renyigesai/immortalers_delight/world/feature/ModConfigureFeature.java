package com.renyigesai.immortalers_delight.world.feature;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

import static com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks.HIMEKAIDO_LEAVES;
import static com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks.HIMEKAIDO_LOG;

public class ModConfigureFeature {
    /**
     * 声明Key，在注册中将使用
     */
    public static final ResourceKey<ConfiguredFeature<?,?>> SUSPICIOUS_DIRT_COF = registerKey("suspicious_dirt_cof");
    public static final ResourceKey<ConfiguredFeature<?,?>> SUSPICIOUS_SOUL_SAND_COF = registerKey("suspicious_soul_sand_cof");
    public static final ResourceKey<ConfiguredFeature<?,?>> END_ORE_COF = registerKey("end_ore_cof");


    public static final ResourceKey<ConfiguredFeature<?,?>> OAK_LIKE_TREE_KEY = registerKey("end_ore_cof");

    /**
     * 注册使用的主方法，实际矿物规则的注册在此处进行，这个方法应在Init或主类进行调用
     * @param context
     */
    public static void bootrap(BootstapContext<ConfiguredFeature<?,?>> context) {
        /*
        声明替换规则，即指出可被替换的方块
         */
        RuleTest sandReplaceables = new TagMatchTest(BlockTags.SMELTS_TO_GLASS);
        RuleTest dirtReplaceables = new TagMatchTest(BlockTags.DIRT);
        RuleTest soulSandReplaceables = new TagMatchTest(BlockTags.SOUL_FIRE_BASE_BLOCKS);
        RuleTest endReplaceables = new BlockMatchTest(Blocks.END_STONE);

        /*
        您可以用List将多个规则打包，以在不同方块中生成相同甚至不同种矿物，可以发现我们在此处已经指定需要生成的方块，因此这将对应双参数OreConfiguration
         */
        List<OreConfiguration.TargetBlockState> suspiciousDirt = List.of(
                OreConfiguration.target(dirtReplaceables, Blocks.STONE.defaultBlockState()),
                OreConfiguration.target(sandReplaceables, Blocks.SUSPICIOUS_SAND.defaultBlockState())
        );
        /*
        使用双参数OreConfiguration注册方法，参考前文，配合List打包的规则使用，第二行的参数分别为
        打包的OreList，矿脉规模
         */
        register(context, SUSPICIOUS_DIRT_COF, Feature.ORE, new OreConfiguration(
                suspiciousDirt, 1
        ));
        /*
        使用三参数OreConfiguration的注册方法，第二行的参数分别为
        替换规则RuleTest，将生成的方块的BlockState，矿脉规模
         */
        register(context, SUSPICIOUS_SOUL_SAND_COF, Feature.ORE, new OreConfiguration(
                soulSandReplaceables, Blocks.SOUL_SAND.defaultBlockState(), 2));

        register(context, END_ORE_COF, Feature.ORE, new OreConfiguration(
                endReplaceables, Blocks.END_STONE_BRICK_WALL.defaultBlockState(), 2));

        /*
        树的世界生成
         */
        register(context, OAK_LIKE_TREE_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                /*
                确定树干方块，随后是树干放置器。其参数为基础高度+随机额外高度1+随机额外高度2
                 */
                BlockStateProvider.simple(HIMEKAIDO_LOG.get()),
                new StraightTrunkPlacer(5,6,3),
                /*
                确定树叶方块，随后是树叶放置器（半圆形放置器），即金合欢或大丛林树的树冠
                 */
                BlockStateProvider.simple(HIMEKAIDO_LEAVES.get()),
                new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 4),
                /*
                对于树生长条件的检测器
                 */
                new TwoLayersFeatureSize(1,0,2)
        ).build());
    }

    /**
     * 我们在最顶部注册Key时使用的方法
     * @param name
     * @return
     */
    public static ResourceKey<ConfiguredFeature<?,?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(ImmortalersDelightMod.MODID, name));
    }

    /**
     * 四参注册机，
     * @param context
     * @param key
     * 即为在最顶部注册的ResourceKey
     * @param feature
     * 生成阶段，如生成矿物使用Feature.ORE
     * @param configuration
     * 可以理解为矿物的生成方式，包含了“将被替换的方块”，“将生成的方块”，“矿脉规模”
     * @param <FC>
     * @param <F>
     */
    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(
            BootstapContext<ConfiguredFeature<?,?>> context, ResourceKey<ConfiguredFeature<?,?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
