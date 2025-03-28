package com.renyigesai.immortalers_delight.init;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.block.*;
import com.renyigesai.immortalers_delight.block.enchantal_cooler.EnchantalCoolerBlock;
import com.renyigesai.immortalers_delight.block.enchantal_cooler.EnchantalCoolerBlockEntity;
import com.renyigesai.immortalers_delight.block.hanging_sign.ImmortalersDelightCeilingHangingSignBlock;
import com.renyigesai.immortalers_delight.block.hanging_sign.ImmortalersDelightWallHangingSignBlockBlock;
import com.renyigesai.immortalers_delight.block.sign.ImmortalersDelightStandingSignBlock;
import com.renyigesai.immortalers_delight.block.sign.ImmortalersDelightWallSignBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.common.block.CabinetBlock;
import vectorwing.farmersdelight.common.block.PieBlock;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public class ImmortalersDelightBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, ImmortalersDelightMod.MODID);

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_REGISTRY =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ImmortalersDelightMod.MODID);

    public static final RegistryObject<Block> ENCHANTAL_COOLER;
    public static final RegistryObject<BlockEntityType<EnchantalCoolerBlockEntity>> ENCHANTAL_COOLER_ENTITY;
    public static final RegistryObject<BlockEntityType<RotatingRoastMeatBlockEntity>> ROTATING_ROAST_MEAT_ENTITY;

    public static final RegistryObject<Block> HIMEKAIDO_LOG = BLOCKS.register("himekaido_log",() ->
            log(MapColor.WOOD, MapColor.PODZOL));

    public static final RegistryObject<Block> LEISAMBOO_STALK = BLOCKS.register("leisamboo_stalk",() ->
            new LeisambooStalkBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).forceSolidOn().randomTicks().instabreak().strength(1.0F).sound(SoundType.BAMBOO).noOcclusion().dynamicShape()));
    public static final RegistryObject<Block> LEISAMBOO_CROP = BLOCKS.register("leisamboo_crop",() ->
            new LeisambooCropBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).forceSolidOn().randomTicks().instabreak().strength(1.0F).sound(SoundType.BAMBOO).noOcclusion().dynamicShape()));

    public static final RegistryObject<Block> HIMEKAIDO_WOOD = BLOCKS.register("himekaido_wood",() ->
            log(MapColor.WOOD, MapColor.PODZOL));

    public static final RegistryObject<Block> STRIPPED_HIMEKAIDO_WOOD = BLOCKS.register("stripped_himekaido_wood",() ->
            log(MapColor.WOOD, MapColor.PODZOL));

    public static final RegistryObject<Block> STRIPPED_HIMEKAIDO_LOG = BLOCKS.register("stripped_himekaido_log",() ->
            log(MapColor.WOOD, MapColor.PODZOL));

    public static final RegistryObject<Block> HIMEKAIDO_SHRUB = BLOCKS.register("himekaido_shrub",() ->
            new HimekaidoShrubBlock(BlockBehaviour.Properties.copy(Blocks.SWEET_BERRY_BUSH).noCollission().randomTicks().instabreak()));
    public static final RegistryObject<Block> HIMEKAIDO_FRUITED_LEAVES = BLOCKS.register("himekaido_fruited_leaves",() ->
        new HimekaidoLeavesFruited(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));
    public static final RegistryObject<Block> HIMEKAIDO_FLOWERING_LEAVES = BLOCKS.register("himekaido_flowering_leaves",() ->
        new HimekaidoLeavesGrowing((HimekaidoLeavesFruited) HIMEKAIDO_FRUITED_LEAVES.get(), BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));
    public static final RegistryObject<Block> HIMEKAIDO_LEAVES = BLOCKS.register("himekaido_leaves",() ->
            new HimekaidoLeavesGrowing((HimekaidoLeavesGrowing) HIMEKAIDO_FLOWERING_LEAVES.get(), BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));

    public static final RegistryObject<Block> HIMEKAIDO_PLANKS = BLOCKS.register("himekaido_planks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));

    public static final RegistryObject<Block> HIMEKAIDO_STAIRS = BLOCKS.register("himekaido_stairs",
            () -> new StairBlock(HIMEKAIDO_PLANKS.get().defaultBlockState(),BlockBehaviour.Properties.copy(HIMEKAIDO_PLANKS.get())));

    public static final RegistryObject<Block> HIMEKAIDO_SLAB = BLOCKS.register("himekaido_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SLAB)));

    public static final RegistryObject<Block> HIMEKAIDO_DOOR = BLOCKS.register("himekaido_door",
            () -> new DoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR),BlockSetType.OAK));

    public static final RegistryObject<Block> HIMEKAIDO_TRAPDOOR = BLOCKS.register("himekaido_trapdoor",
            () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR),BlockSetType.OAK));

    public static final RegistryObject<Block> HIMEKAIDO_FENCE = BLOCKS.register("himekaido_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE)));

    public static final RegistryObject<Block> HIMEKAIDO_FENCE_GATE = BLOCKS.register("himekaido_fence_gate",
            () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE), WoodType.OAK));

    public static final RegistryObject<Block> HIMEKAIDO_PRESSURE_PLATE = BLOCKS.register("himekaido_pressure_plate",
            () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING,BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE),BlockSetType.OAK));

    public static final RegistryObject<Block> HIMEKAIDO_BUTTON = BLOCKS.register("himekaido_button",
            () ->woodenButton(BlockSetType.OAK));

    public static final RegistryObject<Block> HIMEKAIDO_CABINET = BLOCKS.register("himekaido_cabinet",
            () -> new CabinetBlock(Block.Properties.copy(Blocks.BARREL)));

    public static final RegistryObject<Block> MILLENIAN_BAMBOO = BLOCKS.register("millenian_bamboo",
            () -> new MillenianBambooBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).strength(2.0F,3.0F).sound(SoundType.BAMBOO),ImmortalersDelightItems.BOWL_OF_MILLENIAN_BAMBOO));
    public static final RegistryObject<Block> HIMEKAIDO_SIGN = BLOCKS.register("himekaido_sign",
            () ->  new ImmortalersDelightStandingSignBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(1.0F).ignitedByLava(), ImmortalersDelightWoodType.HIMEKAIDO));
    public static final RegistryObject<Block>  HIMEKAIDO_WALL_SIGN = BLOCKS.register("himekaido_wall_sign",
            () -> new ImmortalersDelightWallSignBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(1.0F).dropsLike(HIMEKAIDO_SIGN.get()).ignitedByLava(), ImmortalersDelightWoodType.HIMEKAIDO));

    public static final RegistryObject<Block> HIMEKAIDO_HANGING_SIGN = BLOCKS.register("himekaido_hanging_sign",
            () ->  new ImmortalersDelightCeilingHangingSignBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(1.0F).ignitedByLava(), ImmortalersDelightWoodType.HIMEKAIDO));
    public static final RegistryObject<Block>  HIMEKAIDO_WALL_HANGING_SIGN = BLOCKS.register("himekaido_wall_hanging_sign",
            () -> new ImmortalersDelightWallHangingSignBlockBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(1.0F).dropsLike(HIMEKAIDO_SIGN.get()).ignitedByLava(), ImmortalersDelightWoodType.HIMEKAIDO));

    /**
     * 溪柱制品
     */
    public static final RegistryObject<Block> LEISAMBOO_PLANKS = BLOCKS.register("leisamboo_planks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));

    public static final RegistryObject<Block> LEISAMBOO_STAIRS = BLOCKS.register("leisamboo_stairs",
            () -> new StairBlock(LEISAMBOO_PLANKS.get().defaultBlockState(),BlockBehaviour.Properties.copy(LEISAMBOO_PLANKS.get())));

    public static final RegistryObject<Block> LEISAMBOO_SLAB = BLOCKS.register("leisamboo_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SLAB)));
    public static final RegistryObject<Block> LEISAMBOO_DOOR = BLOCKS.register("leisamboo_door",
            () -> new DoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR),BlockSetType.OAK));

    public static final RegistryObject<Block> LEISAMBOO_TRAPDOOR = BLOCKS.register("leisamboo_trapdoor",
            () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR),BlockSetType.OAK));

    public static final RegistryObject<Block> LEISAMBOO_FENCE = BLOCKS.register("leisamboo_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE)));

    public static final RegistryObject<Block> LEISAMBOO_FENCE_GATE = BLOCKS.register("leisamboo_fence_gate",
            () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE), WoodType.OAK));

    public static final RegistryObject<Block> LEISAMBOO_PRESSURE_PLATE = BLOCKS.register("leisamboo_pressure_plate",
            () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING,BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE),BlockSetType.OAK));

    public static final RegistryObject<Block> LEISAMBOO_BUTTON = BLOCKS.register("leisamboo_button",
            () ->woodenButton(BlockSetType.OAK));

    public static final RegistryObject<Block> LEISAMBOO_CABINET = BLOCKS.register("leisamboo_cabinet",
            () -> new CabinetBlock(Block.Properties.copy(Blocks.BARREL)));

    public static final RegistryObject<Block> LEISAMBOO_SIGN = BLOCKS.register("leisamboo_sign",
            () ->  new ImmortalersDelightStandingSignBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(1.0F).ignitedByLava(), ImmortalersDelightWoodType.HIMEKAIDO));
    public static final RegistryObject<Block>  LEISAMBOO_WALL_SIGN = BLOCKS.register("leisamboo_wall_sign",
            () -> new ImmortalersDelightWallSignBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(1.0F).dropsLike(HIMEKAIDO_SIGN.get()).ignitedByLava(), ImmortalersDelightWoodType.HIMEKAIDO));

    public static final RegistryObject<Block> LEISAMBOO_HANGING_SIGN = BLOCKS.register("leisamboo_hanging_sign",
            () ->  new ImmortalersDelightCeilingHangingSignBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(1.0F).ignitedByLava(), ImmortalersDelightWoodType.HIMEKAIDO));
    public static final RegistryObject<Block>  LEISAMBOO_WALL_HANGING_SIGN = BLOCKS.register("leisamboo_wall_hanging_sign",
            () -> new ImmortalersDelightWallHangingSignBlockBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(1.0F).dropsLike(HIMEKAIDO_SIGN.get()).ignitedByLava(), ImmortalersDelightWoodType.HIMEKAIDO));

    public static final RegistryObject<Block> EVOLUTCORN = BLOCKS.register("evolutcorn",
            () -> new EvolutcornBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.CROP).pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<Block> PEARLIPEARL_BUNDLE = BLOCKS.register("pearlipearl_bundle",
            ()-> new PearlipearlBeanBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN).instrument(NoteBlockInstrument.DIDGERIDOO).strength(0.5F).sound(SoundType.WOOD).pushReaction(PushReaction.DESTROY).randomTicks()));

    public static final RegistryObject<Block> PEARLIPEARL_STALK = BLOCKS.register("pearlipearl_stalk",
            ()-> new PearlipearlStalkBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN).instrument(NoteBlockInstrument.DIDGERIDOO).strength(1.0F).sound(SoundType.WOOD).pushReaction(PushReaction.DESTROY).randomTicks()));

    public static final RegistryObject<Block> ZEA_PANCAKE = BLOCKS.register("zea_pancake",()->
            new ZeaPancakeBLock(BlockBehaviour.Properties.copy(Blocks.CAKE)));

    public static final RegistryObject<Block> STEWED_ROTTEN_MEAT_POT = BLOCKS.register("stewed_rotten_meat_pot",()->
            new StewedRottenMeatPot(BlockBehaviour.Properties.copy(Blocks.DECORATED_POT),ImmortalersDelightItems.BOWL_OF_STEWED_ROTTEN_MEAT_IN_CLAY_POT));
    public static final RegistryObject<Block> BRAISED_SPIDER_EYES_BLOCK = BLOCKS.register("braised_spider_eyes_block",()->
            new BraisedSpiderEyesBlock(BlockBehaviour.Properties.copy(Blocks.CAKE)));

    public static final RegistryObject<Block> ROTATING_ROAST_MEAT = BLOCKS.register("rotating_roast_meat",()->
            new RotatingRoastMeatBlock(BlockBehaviour.Properties.copy(Blocks.CAKE)));

    public static final RegistryObject<Block> SNIFFER_ROTATING_ROAST_MEAT = BLOCKS.register("sniffer_rotating_roast_meat",()->
            new RotatingRoastMeatBlock(BlockBehaviour.Properties.copy(Blocks.CAKE)));

    public static final RegistryObject<Block> TARTARE_CHICKEN_BIG_MEAL = BLOCKS.register("tartare_chicken_big_meal",()->
            new TartareChickenBigMealBlock(BlockBehaviour.Properties.copy(Blocks.CAKE)));

    public static final RegistryObject<Block> EVOLUTCORN_GRAIN_BAG = BLOCKS.register("evolutcorn_grain_bag",()->
            new Block(BlockBehaviour.Properties.copy(Blocks.WHITE_WOOL)));

    public static final RegistryObject<Block> HIMEKAIDO_CRATE = BLOCKS.register("himekaido_crate",()->
            new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));

    public static final RegistryObject<Block> PEARLIP_CRATE = BLOCKS.register("pearlip_crate",()->
            new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> BEEF_CRATE = BLOCKS.register("beef_crate",()->
            new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> COOKED_BEEF_CRATE = BLOCKS.register("cooked_beef_crate",()->
            new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> CHICKEN_CRATE = BLOCKS.register("chicken_crate",()->
            new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> SPIDER_EYE_CRATE = BLOCKS.register("spider_eye_crate",()->
            new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));

    public static final RegistryObject<Block> EVOLUTCORN_BLOCK = BLOCKS.register("evolutcorn_block",()->
            new Block(BlockBehaviour.Properties.copy(Blocks.HAY_BLOCK)));

    public static final RegistryObject<Block> CULTURAL_LEGACY = BLOCKS.register("cultural_legacy",
            () -> new CulturalLegacyEffectToolBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().randomTicks().lightLevel(ageBlockEmission(2)).instabreak().sound(SoundType.GLASS).pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<Block> PEARLIP_PIE = BLOCKS.register("pearlip_pie",()->
            new PieBlock(BlockBehaviour.Properties.copy(Blocks.CAKE),ImmortalersDelightItems.PEARLIP_PIE_SLICE));

    public static final RegistryObject<Block> HIMEKAIDO_YOGURT_PIE = BLOCKS.register("himekaido_yogurt_pie",()->
            new PieBlock(BlockBehaviour.Properties.copy(Blocks.CAKE),ImmortalersDelightItems.HIMEKAIDO_YOGURT_PIE_SLICE));

    public static final RegistryObject<Block> KWAT_WHEAT = BLOCKS.register("kwat_wheat",
            () -> new KwatWheatCrop(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.CROP).pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<Block> NETHER_BREAD_CREAM_SOUP = BLOCKS.register("nether_bread_cream_soup",
            () -> new KwatWheatToastStewedVegetablesBlock(BlockBehaviour.Properties.copy(Blocks.CAKE)));

    static {
        //方块实体 Block Entity

        ENCHANTAL_COOLER = BLOCKS.register("enchantal_cooler",()->
                new EnchantalCoolerBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));

        ENCHANTAL_COOLER_ENTITY = BLOCK_ENTITY_REGISTRY.register("enchantal_cooler",
                ()-> BlockEntityType.Builder.of(EnchantalCoolerBlockEntity::new, ENCHANTAL_COOLER.get()).build(null));

        ROTATING_ROAST_MEAT_ENTITY = BLOCK_ENTITY_REGISTRY.register("rotating_roast_meat",
                ()-> BlockEntityType.Builder.of(RotatingRoastMeatBlockEntity::new, ROTATING_ROAST_MEAT.get()).build(null));
    }

    private static ToIntFunction<BlockState> ageBlockEmission(int exLightValue) {
        return (p_50763_) -> {
            return p_50763_.getValue(BlockStateProperties.AGE_7) + exLightValue;
        };
    }
    private static BasicsLogsBlock log(MapColor p_285370_, MapColor p_285126_) {
        return new BasicsLogsBlock(BlockBehaviour.Properties.of().mapColor((p_152624_) -> {
            return p_152624_.getValue(BasicsLogsBlock.AXIS) == Direction.Axis.Y ? p_285370_ : p_285126_;
        }).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).ignitedByLava());
    }

    private static ButtonBlock woodenButton(BlockSetType p_278239_, FeatureFlag... p_278229_) {
        BlockBehaviour.Properties blockbehaviour$properties = BlockBehaviour.Properties.of().noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY);
        if (p_278229_.length > 0) {
            blockbehaviour$properties = blockbehaviour$properties.requiredFeatures(p_278229_);
        }

        return new ButtonBlock(blockbehaviour$properties, p_278239_, 30, true);
    }


    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
