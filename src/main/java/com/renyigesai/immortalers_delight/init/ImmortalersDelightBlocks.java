package com.renyigesai.immortalers_delight.init;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.block.*;
import com.renyigesai.immortalers_delight.block.enchantal_cooler.EnchantalCoolerBlock;
import com.renyigesai.immortalers_delight.block.enchantal_cooler.EnchantalCoolerBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.common.block.CabinetBlock;

import java.util.function.Supplier;

public class ImmortalersDelightBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, ImmortalersDelightMod.MODID);

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_REGISTRY =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ImmortalersDelightMod.MODID);

//    public static final RegistryObject<Block> HIMEKAIDO_LOG = registerBlock("himekaido_log",() ->
//            log(MapColor.WOOD, MapColor.PODZOL));

        public static final RegistryObject<Block> ENCHANTAL_COOLER;
        public static final RegistryObject<BlockEntityType<EnchantalCoolerBlockEntity>> ENCHANTAL_COOLER_ENTITY;

    public static final RegistryObject<Block> HIMEKAIDO_LOG = registerBlock("himekaido_log",() ->
            log(MapColor.WOOD, MapColor.PODZOL));

    public static final RegistryObject<Block> HIMEKAIDO_WOOD = registerBlock("himekaido_wood",() ->
            log(MapColor.WOOD, MapColor.PODZOL));

    public static final RegistryObject<Block> STRIPPED_HIMEKAIDO_WOOD = registerBlock("stripped_himekaido_wood",() ->
            log(MapColor.WOOD, MapColor.PODZOL));

    public static final RegistryObject<Block> STRIPPED_HIMEKAIDO_LOG = registerBlock("stripped_himekaido_log",() ->
            log(MapColor.WOOD, MapColor.PODZOL));

    public static final RegistryObject<Block> HIMEKAIDO_SHRUB = registerBlock("himekaido_shrub",() ->
            new HimekaidoShrubBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PODZOL).noCollission().randomTicks().instabreak()));

//    public static final RegistryObject<Block> HIMEKAIDO_LEAVES = registerBlock("himekaido_leaves",
//            () -> new HimekaidoLeavesGrowingOld(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).pushReaction(PushReaction.DESTROY)) {
//                @Override
//                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction)
//                {
//                    return true;
//                }
//                @Override
//                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction)
//                {
//                    return 32;
//                }
//                @Override
//                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction)
//                {
//                    return 65;
//                }
//            }
//    );himekaido_leaves
    public static final RegistryObject<Block> HIMEKAIDO_FRUITED_LEAVES = registerBlock("himekaido_fruited_leaves",() ->
        new HimekaidoLeavesFruited(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));
    public static final RegistryObject<Block> HIMEKAIDO_FLOWERING_LEAVES = registerBlock("himekaido_flowering_leaves",() ->
        new HimekaidoLeavesGrowing((HimekaidoLeavesFruited) HIMEKAIDO_FRUITED_LEAVES.get(), BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));
    public static final RegistryObject<Block> HIMEKAIDO_LEAVES = registerBlock("himekaido_leaves",() ->
            new HimekaidoLeavesGrowing((HimekaidoLeavesGrowing) HIMEKAIDO_FLOWERING_LEAVES.get(), BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));

    public static final RegistryObject<Block> HIMEKAIDO_PLANKS = registerBlock("himekaido_planks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));

    public static final RegistryObject<Block> HIMEKAIDO_STAIRS = registerBlock("himekaido_stairs",
            () -> new StairBlock(HIMEKAIDO_PLANKS.get().defaultBlockState(),BlockBehaviour.Properties.copy(HIMEKAIDO_PLANKS.get())));

    public static final RegistryObject<Block> HIMEKAIDO_SLAB = registerBlock("himekaido_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SLAB)));

    public static final RegistryObject<Block> HIMEKAIDO_DOOR = registerBlock("himekaido_door",
            () -> new DoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR),BlockSetType.OAK));

    public static final RegistryObject<Block> HIMEKAIDO_TRAPDOOR = registerBlock("himekaido_trapdoor",
            () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR),BlockSetType.OAK));

    public static final RegistryObject<Block> HIMEKAIDO_FENCE = registerBlock("himekaido_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE)));

    public static final RegistryObject<Block> HIMEKAIDO_FENCE_GATE = registerBlock("himekaido_fence_gate",
            () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE), WoodType.OAK));

    public static final RegistryObject<Block> HIMEKAIDO_PRESSURE_PLATE = registerBlock("himekaido_pressure_plate",
            () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING,BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE),BlockSetType.OAK));

    public static final RegistryObject<Block> HIMEKAIDO_BUTTON = registerBlock("himekaido_button",
            () ->woodenButton(BlockSetType.OAK));

    public static final RegistryObject<Block> HIMEKAIDO_CABINET = registerBlock("himekaido_cabinet",
            () -> new CabinetBlock(Block.Properties.copy(Blocks.BARREL)));

    public static final RegistryObject<Block> MILLENIAN_BAMBOO = registerBlock("millenian_bamboo",
            () -> new MillenianBambooBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).strength(2.0F,3.0F).sound(SoundType.BAMBOO),ImmortalersDelightItems.BOWL_OF_MILLENIAN_BAMBOO));

    public static final RegistryObject<Block> EVOLUTCORN = BLOCKS.register("evolutcorn",
            () -> new EvolutcornBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.CROP).pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<Block> PEARLIPEARL_BUNDLE = BLOCKS.register("pearlipearl_bundle",
            ()-> new PearlipearlBeanBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN).instrument(NoteBlockInstrument.DIDGERIDOO).strength(0.5F).sound(SoundType.WOOD).pushReaction(PushReaction.DESTROY).randomTicks()));

    public static final RegistryObject<Block> PEARLIPEARL_STALK = BLOCKS.register("pearlipearl_stalk",
            ()-> new PearlipearlStalkBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN).instrument(NoteBlockInstrument.DIDGERIDOO).strength(1.0F).sound(SoundType.WOOD).pushReaction(PushReaction.DESTROY).randomTicks()));

    public static final RegistryObject<Block> ZEA_PANCAKE = registerBlock("zea_pancake",()->
            new ZeaPancakeBLock(BlockBehaviour.Properties.copy(Blocks.CAKE)));

    static {

        ENCHANTAL_COOLER = BLOCKS.register("enchantal_cooler",()->
                new EnchantalCoolerBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));

        ENCHANTAL_COOLER_ENTITY = BLOCK_ENTITY_REGISTRY.register("enchantal_cooler",
                ()-> BlockEntityType.Builder.of(EnchantalCoolerBlockEntity::new, ENCHANTAL_COOLER.get()).build(null));
    }

    public static final RegistryObject<Block> STEWED_ROTTEN_MEAT_POT = registerBlock("stewed_rotten_meat_pot",()->
            new StewedRottenMeatPot(BlockBehaviour.Properties.copy(Blocks.DECORATED_POT),ImmortalersDelightItems.BOWL_OF_STEWED_ROTTEN_MEAT_IN_CLAY_POT));
    public static final RegistryObject<Block> BRAISED_SPIDER_EYES_BLOCK = registerBlock("braised_spider_eyes_block",()->
            new BraisedSpiderEyesBlock(BlockBehaviour.Properties.copy(Blocks.CAKE)));


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

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ImmortalersDelightItems.REGISTER.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
