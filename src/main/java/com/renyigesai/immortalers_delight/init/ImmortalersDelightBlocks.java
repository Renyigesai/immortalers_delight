package com.renyigesai.immortalers_delight.init;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.block.*;
import com.renyigesai.immortalers_delight.block.ancient_stove.AncientStoveBlock;
import com.renyigesai.immortalers_delight.block.ancient_stove.AncientStoveBlockEntity;
import com.renyigesai.immortalers_delight.block.ancient_stove.WeatheringAncientStoveBlock;
import com.renyigesai.immortalers_delight.block.enchantal_cooler.EnchantalCoolerBlock;
import com.renyigesai.immortalers_delight.block.enchantal_cooler.EnchantalCoolerBlockEntity;
import com.renyigesai.immortalers_delight.block.hanging_sign.ImmortalersDelightCeilingHangingSignBlock;
import com.renyigesai.immortalers_delight.block.hanging_sign.ImmortalersDelightWallHangingSignBlockBlock;
import com.renyigesai.immortalers_delight.block.sign.ImmortalersDelightStandingSignBlock;
import com.renyigesai.immortalers_delight.block.sign.ImmortalersDelightWallSignBlock;
import com.renyigesai.immortalers_delight.block.tree.TravastrugglerTreeGrower;
import com.renyigesai.immortalers_delight.fluid.HotSpringFluidsBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
import vectorwing.farmersdelight.common.block.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public class ImmortalersDelightBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, ImmortalersDelightMod.MODID);

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_REGISTRY =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ImmortalersDelightMod.MODID);

    public static final RegistryObject<Block> ENCHANTAL_COOLER;
    public static final RegistryObject<BlockEntityType<EnchantalCoolerBlockEntity>> ENCHANTAL_COOLER_ENTITY;
    public static final RegistryObject<Block> ANCIENT_STOVE;
    public static final RegistryObject<BlockEntityType<AncientStoveBlockEntity>> ANCIENT_STOVE_ENTITY;
    public static final RegistryObject<BlockEntityType<RotatingRoastMeatBlockEntity>> ROTATING_ROAST_MEAT_ENTITY;
    public static final RegistryObject<LiquidBlock> HOT_SPRING_BLOCK;

    public static final RegistryObject<Block> HIMEKAIDO_LOG = BLOCKS.register("himekaido_log",() ->
            log(MapColor.WOOD, MapColor.PODZOL));

    public static final RegistryObject<Block> LEISAMBOO_STALK = BLOCKS.register("leisamboo_stalk",() ->
            new LeisambooStalkBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).forceSolidOn().randomTicks().instabreak().strength(1.0F).sound(SoundType.BAMBOO).noOcclusion().dynamicShape().pushReaction(PushReaction.DESTROY).offsetType(BlockBehaviour.OffsetType.XZ)));
    public static final RegistryObject<Block> LEISAMBOO_CROP = BLOCKS.register("leisamboo_crop",() ->
            new LeisambooCropBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).forceSolidOn().randomTicks().instabreak().strength(1.0F).sound(SoundType.BAMBOO).noOcclusion().dynamicShape().pushReaction(PushReaction.DESTROY).offsetType(BlockBehaviour.OffsetType.XZ)));

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
            () -> new DoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR),ImmortalersDelightWoodSetType.HIMEKAIDO));

    public static final RegistryObject<Block> HIMEKAIDO_TRAPDOOR = BLOCKS.register("himekaido_trapdoor",
            () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR),ImmortalersDelightWoodSetType.HIMEKAIDO));

    public static final RegistryObject<Block> HIMEKAIDO_FENCE = BLOCKS.register("himekaido_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE)));

    public static final RegistryObject<Block> HIMEKAIDO_FENCE_GATE = BLOCKS.register("himekaido_fence_gate",
            () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE), ImmortalersDelightWoodType.HIMEKAIDO));

    public static final RegistryObject<Block> HIMEKAIDO_PRESSURE_PLATE = BLOCKS.register("himekaido_pressure_plate",
            () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING,BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE),ImmortalersDelightWoodSetType.HIMEKAIDO));

    public static final RegistryObject<Block> HIMEKAIDO_BUTTON = BLOCKS.register("himekaido_button",
            () ->woodenButton(ImmortalersDelightWoodSetType.HIMEKAIDO));

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
    /*
    古木
    */

    public static final RegistryObject<Block> ANCIENT_WOOD_LOG = BLOCKS.register("ancient_wood_log",() ->
            log(MapColor.WOOD, MapColor.PODZOL));

    public static final RegistryObject<Block> ANCIENT_WOOD = BLOCKS.register("ancient_wood",() ->
            log(MapColor.WOOD, MapColor.PODZOL));

    public static final RegistryObject<Block> STRIPPED_ANCIENT_WOOD = BLOCKS.register("stripped_ancient_wood",() ->
            log(MapColor.WOOD, MapColor.PODZOL));

    public static final RegistryObject<Block> STRIPPED_ANCIENT_WOOD_LOG = BLOCKS.register("stripped_ancient_wood_log",() ->
            log(MapColor.WOOD, MapColor.PODZOL));

    public static final RegistryObject<Block> ANCIENT_WOOD_PLANKS = BLOCKS.register("ancient_wood_planks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));

    public static final RegistryObject<Block> ANCIENT_WOOD_CABINET = BLOCKS.register("ancient_wood_cabinet",
            () -> new CabinetBlock(Block.Properties.copy(Blocks.BARREL)));

    public static final RegistryObject<Block> ANCIENT_WOOD_STAIRS = BLOCKS.register("ancient_wood_stairs",
            () -> new StairBlock(HIMEKAIDO_PLANKS.get().defaultBlockState(),BlockBehaviour.Properties.copy(ANCIENT_WOOD_PLANKS.get())));

    public static final RegistryObject<Block> ANCIENT_WOOD_SLAB = BLOCKS.register("ancient_wood_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SLAB)));

    public static final RegistryObject<Block> ANCIENT_WOOD_DOOR = BLOCKS.register("ancient_wood_door",
            () -> new DoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR),ImmortalersDelightWoodSetType.ANCIENT_WOOD));

        public static final RegistryObject<Block> ANCIENT_WOOD_TRAPDOOR = BLOCKS.register("ancient_wood_trapdoor",
            () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR),ImmortalersDelightWoodSetType.ANCIENT_WOOD));

    public static final RegistryObject<Block> ANCIENT_WOOD_FENCE = BLOCKS.register("ancient_wood_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE)));

    public static final RegistryObject<Block> ANCIENT_WOOD_FENCE_GATE = BLOCKS.register("ancient_wood_fence_gate",
            () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE), ImmortalersDelightWoodType.ANCIENT_WOOD));

    public static final RegistryObject<Block> ANCIENT_WOOD_PRESSURE_PLATE = BLOCKS.register("ancient_wood_pressure_plate",
            () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING,BlockBehaviour.Properties.copy(Blocks.OAK_PRESSURE_PLATE),ImmortalersDelightWoodSetType.ANCIENT_WOOD));

    public static final RegistryObject<Block> ANCIENT_WOOD_BUTTON = BLOCKS.register("ancient_wood_button",
            () ->woodenButton(ImmortalersDelightWoodSetType.ANCIENT_WOOD));

    /*
    泥砖
     */
    public static final RegistryObject<Block> MUD_TILES = BLOCKS.register("mud_tiles",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.MUD_BRICKS)));

    public static final RegistryObject<Block> MUD_TILES_STAIRS = BLOCKS.register("mud_tiles_stairs",
            () -> new StairBlock(MUD_TILES.get().defaultBlockState(),BlockBehaviour.Properties.copy(MUD_TILES.get())));

    public static final RegistryObject<Block> MUD_TILES_SLAB = BLOCKS.register("mud_tiles_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.MUD_BRICKS)));

    public static final RegistryObject<Block> MUD_TILES_WALL = BLOCKS.register("mud_tiles_wall",
            () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.MUD_BRICKS).forceSolidOn()));

    public static final RegistryObject<Block> CRACKED_MUD_TILES = BLOCKS.register("cracked_mud_tiles",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.MUD_BRICKS)));

    public static final RegistryObject<Block> CRACKED_MUD_TILES_STAIRS = BLOCKS.register("cracked_mud_tiles_stairs",
            () -> new StairBlock(CRACKED_MUD_TILES.get().defaultBlockState(),BlockBehaviour.Properties.copy(CRACKED_MUD_TILES.get())));

    public static final RegistryObject<Block> CRACKED_MUD_TILES_SLAB = BLOCKS.register("cracked_mud_tiles_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.MUD_BRICKS)));

    public static final RegistryObject<Block> CRACKED_MUD_TILES_WALL = BLOCKS.register("cracked_mud_tiles_wall",
            () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.MUD_BRICKS).forceSolidOn()));

    public static final RegistryObject<Block> MOSSY_MUD_BRICK = BLOCKS.register("mossy_mud_brick",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.MUD_BRICKS)));

    public static final RegistryObject<Block> MOSSY_MUD_BRICK_STAIRS = BLOCKS.register("mossy_mud_brick_stairs",
            () -> new StairBlock(MOSSY_MUD_BRICK.get().defaultBlockState(),BlockBehaviour.Properties.copy(MOSSY_MUD_BRICK.get())));

    public static final RegistryObject<Block> MOSSY_MUD_BRICK_SLAB = BLOCKS.register("mossy_mud_brick_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.MUD_BRICKS)));

    public static final RegistryObject<Block> MOSSY_MUD_BRICK_WALL = BLOCKS.register("mossy_mud_brick_wall",
            () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.MUD_BRICKS).forceSolidOn()));

    public static final RegistryObject<Block> CRACKED_MUD_BRICK = BLOCKS.register("cracked_mud_brick",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.MUD_BRICKS)));

    public static final RegistryObject<Block> CRACKED_MUD_BRICK_STAIRS = BLOCKS.register("cracked_mud_brick_stairs",
            () -> new StairBlock(CRACKED_MUD_BRICK.get().defaultBlockState(),BlockBehaviour.Properties.copy(CRACKED_MUD_BRICK.get())));

    public static final RegistryObject<Block> CRACKED_MUD_BRICK_SLAB = BLOCKS.register("cracked_mud_brick_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.MUD_BRICKS)));

    public static final RegistryObject<Block> CRACKED_MUD_BRICK_WALL = BLOCKS.register("cracked_mud_brick_wall",
            () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.MUD_BRICKS).forceSolidOn()));

    public static final RegistryObject<Block> CHISELED_MUD_BRICK = BLOCKS.register("chiseled_mud_brick",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.MUD_BRICKS)));

    public static final RegistryObject<Block> PACKED_MUD_STAIRS = BLOCKS.register("packed_mud_stairs",
            () -> new StairBlock(Blocks.PACKED_MUD.defaultBlockState(),BlockBehaviour.Properties.copy(Blocks.PACKED_MUD)));

    public static final RegistryObject<Block> PACKED_MUD_SLAB = BLOCKS.register("packed_mud_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.MUD_BRICKS)));

    public static final RegistryObject<Block> PACKED_MUD_WALL = BLOCKS.register("packed_mud_wall",
            () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.MUD_BRICKS).forceSolidOn()));

    /**
     * 溪竹制品
     */
    public static final RegistryObject<Block> LEISAMBOO_PLANKS = BLOCKS.register("leisamboo_planks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));

    public static final RegistryObject<Block> LEISAMBOO_CABINET = BLOCKS.register("leisamboo_cabinet",
            () -> new CabinetBlock(Block.Properties.copy(Blocks.BARREL)));

    public static final RegistryObject<Block> LEISAMBOO_STAIRS = BLOCKS.register("leisamboo_stairs",
            () -> new StairBlock(LEISAMBOO_PLANKS.get().defaultBlockState(),BlockBehaviour.Properties.copy(LEISAMBOO_PLANKS.get())));

    public static final RegistryObject<Block> LEISAMBOO_SLAB = BLOCKS.register("leisamboo_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SLAB)));
    public static final RegistryObject<Block> LEISAMBOO_DOOR = BLOCKS.register("leisamboo_door",
            () -> new DoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR),ImmortalersDelightWoodSetType.LEISAMBOO));

    public static final RegistryObject<Block> LEISAMBOO_TRAPDOOR = BLOCKS.register("leisamboo_trapdoor",
            () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR),ImmortalersDelightWoodSetType.LEISAMBOO));

    public static final RegistryObject<Block> LEISAMBOO_FENCE = BLOCKS.register("leisamboo_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE)));

    public static final RegistryObject<Block> LEISAMBOO_FENCE_GATE = BLOCKS.register("leisamboo_fence_gate",
            () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE), ImmortalersDelightWoodType.LEISAMBOO));

    public static final RegistryObject<Block> LEISAMBOO_PRESSURE_PLATE = BLOCKS.register("leisamboo_pressure_plate",
            () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING,BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE),ImmortalersDelightWoodSetType.LEISAMBOO));

    public static final RegistryObject<Block> LEISAMBOO_BUTTON = BLOCKS.register("leisamboo_button",
            () ->woodenButton(ImmortalersDelightWoodSetType.LEISAMBOO));

    public static final RegistryObject<Block> LEISAMBOO_SIGN = BLOCKS.register("leisamboo_sign",
            () ->  new ImmortalersDelightStandingSignBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(1.0F).ignitedByLava(), ImmortalersDelightWoodType.LEISAMBOO));
    public static final RegistryObject<Block>  LEISAMBOO_WALL_SIGN = BLOCKS.register("leisamboo_wall_sign",
            () -> new ImmortalersDelightWallSignBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(1.0F).dropsLike(LEISAMBOO_SIGN.get()).ignitedByLava(), ImmortalersDelightWoodType.LEISAMBOO));

    public static final RegistryObject<Block> LEISAMBOO_HANGING_SIGN = BLOCKS.register("leisamboo_hanging_sign",
            () ->  new ImmortalersDelightCeilingHangingSignBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(1.0F).ignitedByLava(), ImmortalersDelightWoodType.LEISAMBOO));
    public static final RegistryObject<Block>  LEISAMBOO_WALL_HANGING_SIGN = BLOCKS.register("leisamboo_wall_hanging_sign",
            () -> new ImmortalersDelightWallHangingSignBlockBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(1.0F).dropsLike(LEISAMBOO_HANGING_SIGN.get()).ignitedByLava(), ImmortalersDelightWoodType.LEISAMBOO));

    /*
    棱蕉制品
     */
    public static final RegistryObject<Block> PEARLIP_SHELL_PLANKS = BLOCKS.register("pearlip_shell_planks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));

    public static final RegistryObject<Block> PEARLIP_SHELL_STAIRS = BLOCKS.register("pearlip_shell_stairs",
            () -> new StairBlock(PEARLIP_SHELL_PLANKS.get().defaultBlockState(),BlockBehaviour.Properties.copy(PEARLIP_SHELL_PLANKS.get())));

    public static final RegistryObject<Block> PEARLIP_SHELL_SLAB = BLOCKS.register("pearlip_shell_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SLAB)));
    public static final RegistryObject<Block> PEARLIP_SHELL_DOOR = BLOCKS.register("pearlip_shell_door",
            () -> new DoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR),ImmortalersDelightWoodSetType.PEARLIP_SHELL));

    public static final RegistryObject<Block> PEARLIP_SHELL_TRAPDOOR = BLOCKS.register("pearlip_shell_trapdoor",
            () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR),ImmortalersDelightWoodSetType.PEARLIP_SHELL));

    public static final RegistryObject<Block> PEARLIP_SHELL_FENCE = BLOCKS.register("pearlip_shell_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE)));

    public static final RegistryObject<Block> PEARLIP_SHELL_FENCE_GATE = BLOCKS.register("pearlip_shell_fence_gate",
            () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE), ImmortalersDelightWoodType.PEARLIP_SHELL));

    public static final RegistryObject<Block> PEARLIP_SHELL_PRESSURE_PLATE = BLOCKS.register("pearlip_shell_pressure_plate",
            () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING,BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE),ImmortalersDelightWoodSetType.PEARLIP_SHELL));

    public static final RegistryObject<Block> PEARLIP_SHELL_BUTTON = BLOCKS.register("pearlip_shell_button",
            () ->woodenButton(ImmortalersDelightWoodSetType.PEARLIP_SHELL));

    public static final RegistryObject<Block> PEARLIP_SHELL_CABINET = BLOCKS.register("pearlip_shell_cabinet",
            () -> new CabinetBlock(Block.Properties.copy(Blocks.BARREL)));

    public static final RegistryObject<Block> PEARLIP_SHELL_SIGN = BLOCKS.register("pearlip_shell_sign",
            () ->  new ImmortalersDelightStandingSignBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(1.0F).ignitedByLava(), ImmortalersDelightWoodType.PEARLIP_SHELL));
    public static final RegistryObject<Block>  PEARLIP_SHELL_WALL_SIGN = BLOCKS.register("pearlip_shell_wall_sign",
            () -> new ImmortalersDelightWallSignBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(1.0F).dropsLike(PEARLIP_SHELL_SIGN.get()).ignitedByLava(), ImmortalersDelightWoodType.PEARLIP_SHELL));

    public static final RegistryObject<Block> PEARLIP_SHELL_HANGING_SIGN = BLOCKS.register("pearlip_shell_hanging_sign",
            () ->  new ImmortalersDelightCeilingHangingSignBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(1.0F).ignitedByLava(), ImmortalersDelightWoodType.PEARLIP_SHELL));
    public static final RegistryObject<Block>  PEARLIP_SHELL_WALL_HANGING_SIGN = BLOCKS.register("pearlip_shell_wall_hanging_sign",
            () -> new ImmortalersDelightWallHangingSignBlockBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(1.0F).dropsLike(PEARLIP_SHELL_HANGING_SIGN.get()).ignitedByLava(), ImmortalersDelightWoodType.PEARLIP_SHELL));

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

    public static final RegistryObject<Block> SCARLET_DEVILS_CAKE = BLOCKS.register("scarlet_devils_cake",()->
            new ScarletDevilsCakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE)));

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
    public static final RegistryObject<Block> KWAT_WHEAT_BLOCK = BLOCKS.register("kwat_wheat_block",()->
            new Block(BlockBehaviour.Properties.copy(Blocks.HAY_BLOCK)));
    public static final RegistryObject<Block> ALFALFA_BLOCK = BLOCKS.register("alfalfa_block",()->
            new Block(BlockBehaviour.Properties.copy(Blocks.HAY_BLOCK)));
    public static final RegistryObject<Block> LEISAMBOO_BLOCK = BLOCKS.register("leisamboo_block",()->
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

    public static final RegistryObject<Block> POD_SHELL_BURGER_MEAT = BLOCKS.register("pod_shell_burger_meat",
            () -> new PodShellBurgerMeatBlock(BlockBehaviour.Properties.copy(Blocks.CAKE)));

    public static final RegistryObject<Block> EXPOSED_ANCIENT_STOVE = BLOCKS.register("exposed_ancient_stove",()->
            new WeatheringAncientStoveBlock(BlockBehaviour.Properties.copy(Blocks.EXPOSED_COPPER).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.COPPER),WeatheringCopper.WeatherState.EXPOSED));

    public static final RegistryObject<Block> WEATHERED_ANCIENT_STOVE = BLOCKS.register("weathered_ancient_stove",()->
            new WeatheringAncientStoveBlock(BlockBehaviour.Properties.copy(Blocks.WEATHERED_COPPER).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.COPPER),WeatheringCopper.WeatherState.WEATHERED));

    public static final RegistryObject<Block> OXIDIZED_ANCIENT_STOVE = BLOCKS.register("oxidized_ancient_stove", () ->
            new WeatheringAncientStoveBlock(BlockBehaviour.Properties.copy(Blocks.OXIDIZED_COPPER).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.COPPER),WeatheringCopper.WeatherState.OXIDIZED));

    /*
    通天竹
    */
    public static final RegistryObject<Block> TRAVASTRUGGLER_SAPLING = BLOCKS.register("travastruggler_sapling", () -> new SaplingBlock(new TravastrugglerTreeGrower(),BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));
    public static final RegistryObject<Block> TRAVASTRUGGLER_LOG = BLOCKS.register("travastruggler_log", ()-> travastrugglerLog());
    public static final RegistryObject<Block> STRIPPED_TRAVASTRUGGLER_LOG = BLOCKS.register("stripped_travastruggler_log", ()-> travastrugglerLog());
    public static final RegistryObject<Block> TRAVA_PLANKS = BLOCKS.register("trava_planks", () -> new Block(BlockBehaviour.Properties.of().strength(2).ignitedByLava().sound(SoundType.BAMBOO_WOOD).mapColor(MapColor.COLOR_GRAY)));
    public static final RegistryObject<Block> TRAVASTRUGGLER_LEAVES = BLOCKS.register("travastruggler_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));
    public static final RegistryObject<Block> TRAVASTRUGGLER_LEAVES_TRAVARICE = BLOCKS.register("travastruggler_leaves_travarice", () -> new TravastrugglerLeavesTravariceBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));


    /*
    饮料方块
    */

    public static final RegistryObject<Block> LEISAMBOO_TEA = BLOCKS.register("leisamboo_tea",()->new DrinksBlock(BlockBehaviour.Properties.copy(Blocks.BAMBOO_PLANKS).strength(0.3F)));

    public static final RegistryObject<Block> ICED_BLACK_TEA = drinksBlock("iced_black_tea");

    public static final RegistryObject<Block> PEARLIPEARL_MILK_TEA = drinksBlock("pearlipearl_milk_tea");

    public static final RegistryObject<Block> PEARLIPEARL_MILK_GREEN = drinksBlock("pearlipearl_milk_green");

    public static final RegistryObject<Block> STOVE_BLACK_TEA = drinksBlock("stove_black_tea");

    public static final RegistryObject<Block> LEAF_GREEN_TEA = drinksBlock("leaf_green_tea");

    public static final RegistryObject<Block> BRITISH_YELLOW_TEA = drinksBlock("british_yellow_tea");

    public static final RegistryObject<Block> LEAF_TEA = drinksBlock("leaf_tea");

    public static final RegistryObject<Block> YOGURT = drinksBlock("yogurt");

    public static final RegistryObject<Block> EVOLUTCORN_BEER = drinksBlock("evolutcorn_beer");

    public static final RegistryObject<Block> VULCAN_COKTAIL = drinksBlock("vulcan_coktail");

    public static final RegistryObject<Block> DREUMK_WINE = drinksBlock("dreumk_wine");

    public static final RegistryObject<Block> PIGLIN_ODORI_SAKE = BLOCKS.register("piglin_odori_sake",()->new DrinksBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(0.3F)));



    public static final RegistryObject<Block> POISONOUS_SPIKE_TRAP = BLOCKS.register("poisonous_spike_trap",
            () -> new SpikeTrapBlock(2.0F,
                    BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).forceSolidOff().sound(SoundType.DEEPSLATE).strength(18.0F, 50.0F).pushReaction(PushReaction.BLOCK)) {
            });
    public static final RegistryObject<Block> SPIKE_TRAP = BLOCKS.register("spike_trap",
            () -> new SpikeTrapBlock(4.0F,BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).forceSolidOff().sound(SoundType.DEEPSLATE).strength(18.0F, 50.0F).pushReaction(PushReaction.BLOCK)) {
            });

    public static final RegistryObject<Block> POISONOUS_LONG_SPIKE_TRAP = BLOCKS.register("poisonous_long_spike_trap",
            () -> new SpikeTrapBlock(true,2.0F,
                    BlockBehaviour.Properties.copy(Blocks.BIG_DRIPLEAF).mapColor(MapColor.DEEPSLATE).sound(SoundType.DEEPSLATE).strength(18.0F, 50.0F).pushReaction(PushReaction.BLOCK)));
    public static final RegistryObject<Block> LONG_SPIKE_TRAP = BLOCKS.register("long_spike_trap",
            () -> new SpikeTrapBlock(true,6.0F,
                    BlockBehaviour.Properties.copy(Blocks.BIG_DRIPLEAF).mapColor(MapColor.DEEPSLATE).sound(SoundType.DEEPSLATE).strength(18.0F, 50.0F).pushReaction(PushReaction.BLOCK)));

    public static final RegistryObject<Block> POISONOUS_METAL_CALTROP = BLOCKS.register("poisonous_metal_caltrop",
            () -> new MetalCaltropBlock(false,2.0F,
                    BlockBehaviour.Properties.copy(Blocks.PISTON).strength(55.0F, 1200.0F)) {
            });
    public static final RegistryObject<Block> METAL_CALTROP = BLOCKS.register("metal_caltrop",
            () -> new MetalCaltropBlock(false,3.0F,
                    BlockBehaviour.Properties.copy(Blocks.PISTON).strength(55.0F, 1200.0F).pushReaction(PushReaction.NORMAL)) {
            });
    public static final RegistryObject<Block> SPIKE_BAR_BASE = BLOCKS.register("spike_bar_base",
            () -> new IronBarsBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(15.0F, 50.0F).sound(SoundType.METAL).noOcclusion()));
    public static final RegistryObject<Block> SPIKE_BAR = BLOCKS.register("spike_bar",
            () -> new SpikeBarBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(15.0F, 50.0F).sound(SoundType.METAL).noOcclusion()));

    public static final RegistryObject<Block> INFESTED_SAND = BLOCKS.register("infested_sand",
            () -> new InfestedFallingBlock(Blocks.SUSPICIOUS_SAND,BlockBehaviour.Properties.copy(Blocks.SAND)));

    public static final RegistryObject<Block> INFESTED_GRAVEL = BLOCKS.register("infested_gravel",
            () -> new InfestedFallingBlock(Blocks.SUSPICIOUS_GRAVEL,BlockBehaviour.Properties.copy(Blocks.GRAVEL)));

    public static final RegistryObject<Block> INFESTED_COAL_BLOCK = BLOCKS.register("infested_coal_block",
            () -> new InfestedOreBlock(Blocks.COAL_BLOCK,BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK)));
    public static final RegistryObject<Block> INFESTED_COPPER_BLOCK = BLOCKS.register("infested_copper_block",
            () -> new InfestedOreBlock(Blocks.COPPER_BLOCK,BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE)));

    public static final RegistryObject<Block> INFESTED_IRON_BLOCK = BLOCKS.register("infested_iron_block",
            () -> new InfestedOreBlock(Blocks.IRON_BLOCK,BlockBehaviour.Properties.of().mapColor(MapColor.METAL)));

    public static final RegistryObject<Block> INFESTED_REDSTONE_BLOCK = BLOCKS.register("infested_redstone_block",
            () -> new InfestedOreBlock(Blocks.REDSTONE_BLOCK,BlockBehaviour.Properties.of().mapColor(MapColor.FIRE)));
    public static final RegistryObject<Block> INFESTED_LAPIS_BLOCK = BLOCKS.register("infested_lapis_block",
            () -> new InfestedOreBlock(Blocks.LAPIS_BLOCK,BlockBehaviour.Properties.of().mapColor(MapColor.LAPIS)));

    public static final RegistryObject<Block> INFESTED_GOLD_BLOCK = BLOCKS.register("infested_gold_block",
            () -> new InfestedOreBlock(Blocks.GOLD_BLOCK,BlockBehaviour.Properties.of().mapColor(MapColor.GOLD)));
    public static final RegistryObject<Block> INFESTED_EMERALD_BLOCK = BLOCKS.register("infested_emerald_block",
            () -> new InfestedOreBlock(Blocks.EMERALD_BLOCK,BlockBehaviour.Properties.of().mapColor(MapColor.EMERALD)));

    public static final RegistryObject<Block> INFESTED_DIAMOND_BLOCK = BLOCKS.register("infested_diamond_block",
            () -> new InfestedOreBlock(Blocks.DIAMOND_BLOCK,BlockBehaviour.Properties.of().mapColor(MapColor.DIAMOND)));

    public static final RegistryObject<Block> ALFALFA = BLOCKS.register("alfalfa",
            () -> new AlfalfaCropBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.CROP).pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<Block> PEARLIP_RICE_ROLL_BOAT = BLOCKS.register("pearlip_rice_roll_boat",
            () -> new PearlipRiceRollBoatBlock(BlockBehaviour.Properties.copy(Blocks.CAKE)));

    /*诡怨藤*/
    public static final RegistryObject<Block> WARPED_LAUREL_CROP = BLOCKS.register("warped_laurel_crop",()->
            new WarpedLaurelCrop(BlockBehaviour.Properties.copy(Blocks.NETHER_WART).speedFactor(0.4F)));

    /*瓶子草*/
    public static final RegistryObject<Block> CHEESE_MELON_JUICE = BLOCKS.register("cheese_melon_juice",()->
            new CheeseMelonJuiceBlock(BlockBehaviour.Properties.copy(Blocks.CAKE),ImmortalersDelightItems.BOTTLE_MELON_JUICE));
    public static final RegistryObject<Block> PITCHER_PLANT_CLAYPOT_RICE = BLOCKS.register("pitcher_plant_claypot_rice",()->
            new PitcherPlantClaypotRiceBlock(BlockBehaviour.Properties.copy(Blocks.CAKE),ImmortalersDelightItems.BOWL_PITCHER_PLANT_CLAYPOT_RICE,false));

    /*嗅探兽毛块*/
    public static final RegistryObject<Block> SNIFFER_FUR_BLOCK = BLOCKS.register("sniffer_fur_block",()-> new Block(BlockBehaviour.Properties.copy(Blocks.WHITE_WOOL).strength(0.3F)));
    public static final RegistryObject<Block> SNIFFER_FUR_TATAMI = BLOCKS.register("sniffer_fur_tatami",()-> new TatamiBlock(BlockBehaviour.Properties.copy(Blocks.WHITE_WOOL).strength(0.3F)));
    public static final RegistryObject<Block> SNIFFER_FUR_FULL_TATAMI_MAT = BLOCKS.register("sniffer_fur_full_tatami_mat", () -> new TatamiMatBlock(BlockBehaviour.Properties.copy(Blocks.WHITE_WOOL).strength(0.3F)));
    public static final RegistryObject<Block> SNIFFER_FUR_HALF_TATAMI_MAT = BLOCKS.register("sniffer_fur_half_tatami_mat", () -> new TatamiHalfMatBlock(BlockBehaviour.Properties.copy(Blocks.WHITE_WOOL).strength(0.3F).pushReaction(PushReaction.DESTROY)));

    /*石锅*/
    public static final RegistryObject<Block> STONE_POT = BLOCKS.register("stone_pot",()-> new StonePotBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));
    public static final RegistryObject<Block> UNIVERSAL_CHICKEN_SOUP = BLOCKS.register("universal_chicken_soup",()-> new UniversalChickenSoupBlock(BlockBehaviour.Properties.copy(Blocks.STONE),ImmortalersDelightItems.BOWL_OF_UNIVERSAL_CHICKEN_SOUP,true));

    public static final RegistryObject<Block> GAIXIA_SILME = BLOCKS.register("gaixia_silme",()-> new GaixiaSlimeBlock(BlockBehaviour.Properties.copy(Blocks.SLIME_BLOCK).forceSolidOn().noCollission()));

    //oxidized

    static {
        //方块实体 Block Entity

        ENCHANTAL_COOLER = BLOCKS.register("enchantal_cooler",()->
                new EnchantalCoolerBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));

        ENCHANTAL_COOLER_ENTITY = BLOCK_ENTITY_REGISTRY.register("enchantal_cooler",
                ()-> BlockEntityType.Builder.of(EnchantalCoolerBlockEntity::new, ENCHANTAL_COOLER.get()).build(null));

        ANCIENT_STOVE = BLOCKS.register("ancient_stove",()->
                new AncientStoveBlock(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.COPPER), WeatheringCopper.WeatherState.UNAFFECTED));

        ANCIENT_STOVE_ENTITY = BLOCK_ENTITY_REGISTRY.register("ancient_stove",
                ()-> BlockEntityType.Builder.of(AncientStoveBlockEntity::new, ANCIENT_STOVE.get()).build(null));

        ROTATING_ROAST_MEAT_ENTITY = BLOCK_ENTITY_REGISTRY.register("rotating_roast_meat",
                ()-> BlockEntityType.Builder.of(RotatingRoastMeatBlockEntity::new, ROTATING_ROAST_MEAT.get()).build(null));

        HOT_SPRING_BLOCK = BLOCKS.register("hot_spring_block", HotSpringFluidsBlock::new);
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

    private static BasicsLogsBlock travastrugglerLog() {
        return new TravastrugglerLogBlock(BlockBehaviour.Properties.of().mapColor((p_152624_) -> {
            return p_152624_.getValue(BasicsLogsBlock.AXIS) == Direction.Axis.Y ? MapColor.COLOR_YELLOW : MapColor.PLANT;
        }).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.BAMBOO).ignitedByLava());
    }

    private static ButtonBlock woodenButton(BlockSetType p_278239_, FeatureFlag... p_278229_) {
        BlockBehaviour.Properties blockbehaviour$properties = BlockBehaviour.Properties.of().noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY);
        if (p_278229_.length > 0) {
            blockbehaviour$properties = blockbehaviour$properties.requiredFeatures(p_278229_);
        }

        return new ButtonBlock(blockbehaviour$properties, p_278239_, 30, true);
    }

    private static RegistryObject<Block> drinksBlock(String name){
        return BLOCKS.register(name,()->new DrinksBlock(BlockBehaviour.Properties.copy(Blocks.GLASS)));
    }


    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
