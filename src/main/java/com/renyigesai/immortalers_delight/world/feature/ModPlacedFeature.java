package com.renyigesai.immortalers_delight.world.feature;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.List;

import static net.minecraft.data.worldgen.placement.PlacementUtils.RANGE_8_8;

public class ModPlacedFeature {
    public static final ResourceKey<PlacedFeature> SUSPICIOUS_DIRT_PLACED = createKey("suspicious_dirt_placed");
    public static final ResourceKey<PlacedFeature> SUSPICIOUS_SOUL_SAND_PLACED = createKey("suspicious_soul_sand_placed");
    public static final ResourceKey<PlacedFeature> END_ORE_PLACED = createKey("end_ore_placed");
    /**
     * 注册使用的主方法，实际矿物生成器的注册在此处进行，这个方法应在Init或主类进行调用
     * @param context
     */
    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?,?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        /*
        在此处联系COF与PLACED，并传入放置方法以最终注册矿物放置器
        commonOrePlacement为矿物放置的方法，其参数按顺序为：
        每区块生成的尝试次数
        高度分布的描述，.uniform为平均分布
        最低生成高度，可以处在世界外
        最高生成高度，也可以处在世界外
         */
        register(context,
                SUSPICIOUS_DIRT_PLACED,
                configuredFeatures.getOrThrow(ModConfigureFeature.SUSPICIOUS_DIRT_COF),
                ModOrePlacement.commonOrePlacement(2,
                        HeightRangePlacement.uniform(
                        VerticalAnchor.absolute(56),
                        VerticalAnchor.absolute(88)
                        )
                )
        );
        /*
        commonOrePlacement为矿物放置的方法，其参数按顺序为：
        每区块生成的尝试次数
        高度分布的描述，此处使用PlacementUtils封装好的方法，该目录下还有其他方法可选
         */
        register(context,
                SUSPICIOUS_SOUL_SAND_PLACED,
                configuredFeatures.getOrThrow(ModConfigureFeature.SUSPICIOUS_SOUL_SAND_COF),
                ModOrePlacement.commonOrePlacement(3,
                        PlacementUtils.RANGE_8_8
                )
        );
        /*
        commonOrePlacement为矿物放置的方法，其参数按顺序为：
        每区块生成的尝试次数
        高度分布的描述，.triangle意为“三角分布”，即中间高度生成概率最大
        最低生成高度，可以处在世界外
        最高生成高度，也可以处在世界外
         */
        register(context,
                END_ORE_PLACED,
                configuredFeatures.getOrThrow(ModConfigureFeature.END_ORE_COF),
                ModOrePlacement.commonOrePlacement(9,
                        HeightRangePlacement.triangle(
                                VerticalAnchor.absolute(-11),
                                VerticalAnchor.absolute(96)
                        )
                )
        );
    }
    private static ResourceKey<PlacedFeature> createKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(ImmortalersDelightMod.MODID, name));
    }

    private static void register(BootstapContext<PlacedFeature> context,
                                 ResourceKey<PlacedFeature> key,
                                 Holder<ConfiguredFeature<?,?>> configuredFeature,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuredFeature, List.copyOf(modifiers)));
    }
}
