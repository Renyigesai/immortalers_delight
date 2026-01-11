package com.renyigesai.immortalers_delight.data;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStates extends BlockStateProvider {

    private static final ResourceLocation A_BUSH_PLANKS = new ResourceLocation(ImmortalersDelightMod.MODID, "block/a_bush_planks");
    public BlockStates(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, ImmortalersDelightMod.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
//        logBlock(ImmortalersDelightBlocks.STRIPPED_A_BUSH_LOG.get());
//        axisBlock(ImmortalersDelightBlocks.A_BUSH_WOOD.get(),new ResourceLocation(ImmortalersDelightMod.MODID,"block/a_bush_log"),new ResourceLocation(ImmortalersDelightMod.MODID,"block/a_bush_log"));
//        axisBlock(ImmortalersDelightBlocks.STRIPPED_A_BUSH_WOOD.get(),new ResourceLocation(ImmortalersDelightMod.MODID,"block/stripped_a_bush_log"),new ResourceLocation(ImmortalersDelightMod.MODID,"block/stripped_a_bush_log_top"));
//        cubeAll(ImmortalersDelightBlocks.A_BUSH_PLANKS.get());
//        stairsBlock(ImmortalersDelightBlocks.A_BUSH_STAIRS.get(), A_BUSH_PLANKS);
//        slabBlock(ImmortalersDelightBlocks.A_BUSH_SLAB.get(), A_BUSH_PLANKS, A_BUSH_PLANKS);
//        doorBlock(ImmortalersDelightBlocks.A_BUSH_DOOR.get(),new ResourceLocation(ImmortalersDelightMod.MODID,"block/a_bush_door_bottom"),new ResourceLocation(ImmortalersDelightMod.MODID,"block/a_bush_door_top"));
//        trapdoorBlock(ImmortalersDelightBlocks.A_BUSH_TRAPDOOR.get(),new ResourceLocation(ImmortalersDelightMod.MODID,"block/a_bush_trapdoor"),true);
//        fenceBlock(ImmortalersDelightBlocks.A_BUSH_FENCE.get(), A_BUSH_PLANKS);
//        fenceGateBlock(ImmortalersDelightBlocks.A_BUSH_FENCE_GATE.get(), A_BUSH_PLANKS);
//        pressurePlateBlock(ImmortalersDelightBlocks.A_BUSH_PRESSURE_PLATE.get(), A_BUSH_PLANKS);
//        buttonBlock(ImmortalersDelightBlocks.A_BUSH_BUTTON.get(), A_BUSH_PLANKS);

    }

    private String name(Block block){
        return BuiltInRegistries.BLOCK.getKey(block).getPath();
    }

}
