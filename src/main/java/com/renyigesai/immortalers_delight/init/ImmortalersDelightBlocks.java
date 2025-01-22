package com.renyigesai.immortalers_delight.init;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.block.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StemGrownBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ImmortalersDelightBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, ImmortalersDelightMod.MODID);

    public static final RegistryObject<Block> MILLENIAN_BAMBOO = registerBlock("millenian_bamboo",
            () -> new MillenianBambooBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).strength(2.0F,3.0F).sound(SoundType.BAMBOO)));

    public static final RegistryObject<Block> EVOLUTCORN = BLOCKS.register("evolutcorn",
            () -> new EvolutcornBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.CROP).pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<StemGrownBlock> BANANA_BUNDLE = BLOCKS.register("banana_bundle",
            ()-> new PeaticMusaRubineaBundleBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).randomTicks().instabreak().sound(SoundType.CROP).pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<Block> BANANA_STALK = BLOCKS.register("banana_stalk",
            ()-> new BananaStalkBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).randomTicks().instabreak().sound(SoundType.CROP).pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<Block> ZEA_PANCAKE = registerBlock("zea_pancake",()->
            new ZeaPancakeBLock(BlockBehaviour.Properties.copy(Blocks.CAKE)));

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
