package com.renyigesai.immortalers_delight.data;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.block.food.ImmortalersCakeBlock;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.common.block.CabinetBlock;

import java.util.List;

public class BlockStates extends BlockStateProvider {

    private static final ResourceLocation A_BUSH_PLANKS = new ResourceLocation(ImmortalersDelightMod.MODID, "block/a_bush_planks");
    public BlockStates(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, ImmortalersDelightMod.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        logBlock(ImmortalersDelightBlocks.STRIPPED_A_BUSH_LOG.get());
        axisBlock(ImmortalersDelightBlocks.A_BUSH_WOOD.get(),new ResourceLocation(ImmortalersDelightMod.MODID,"block/a_bush_log"),new ResourceLocation(ImmortalersDelightMod.MODID,"block/a_bush_log"));
        axisBlock(ImmortalersDelightBlocks.STRIPPED_A_BUSH_WOOD.get(),new ResourceLocation(ImmortalersDelightMod.MODID,"block/stripped_a_bush_log"),new ResourceLocation(ImmortalersDelightMod.MODID,"block/stripped_a_bush_log_top"));
        cubeAll(ImmortalersDelightBlocks.A_BUSH_PLANKS.get());
        stairsBlock(ImmortalersDelightBlocks.A_BUSH_STAIRS.get(), A_BUSH_PLANKS);
        slabBlock(ImmortalersDelightBlocks.A_BUSH_SLAB.get(), A_BUSH_PLANKS, A_BUSH_PLANKS);
        doorBlock(ImmortalersDelightBlocks.A_BUSH_DOOR.get(),new ResourceLocation(ImmortalersDelightMod.MODID,"block/a_bush_door_bottom"),new ResourceLocation(ImmortalersDelightMod.MODID,"block/a_bush_door_top"));
        trapdoorBlock(ImmortalersDelightBlocks.A_BUSH_TRAPDOOR.get(),new ResourceLocation(ImmortalersDelightMod.MODID,"block/a_bush_trapdoor"),true);
        fenceBlock(ImmortalersDelightBlocks.A_BUSH_FENCE.get(), A_BUSH_PLANKS);
        fenceGateBlock(ImmortalersDelightBlocks.A_BUSH_FENCE_GATE.get(), A_BUSH_PLANKS);
        pressurePlateBlock(ImmortalersDelightBlocks.A_BUSH_PRESSURE_PLATE.get(), A_BUSH_PLANKS);
        buttonBlock(ImmortalersDelightBlocks.A_BUSH_BUTTON.get(), A_BUSH_PLANKS);

        //自定义生成

        // 注册 Golden Cake
        registerImmortalersCake(ImmortalersDelightBlocks.GOLDEN_CAKE.get());
        // 注册 Moonlight Cake
        registerImmortalersCake(ImmortalersDelightBlocks.MOONLIGHT_CAKE.get());
    }

    private String name(Block block){
        return BuiltInRegistries.BLOCK.getKey(block).getPath();
    }


    //=====================================用于自动生成蛋糕+蜡烛蛋糕的blockState==========================================//
    // 严格按照 CANDLE_SUPPLIERS 索引顺序排列的模型后缀名
    private static final List<String> CANDLE_SUFFIXES = List.of(
            "candle",
            "white_candle",
            "orange_candle",
            "magenta_candle",
            "light_blue_candle",
            "yellow_candle",
            "lime_candle",
            "pink_candle",
            "gray_candle",
            "light_gray_candle",
            "cyan_candle",
            "purple_candle",
            "blue_candle",
            "brown_candle",
            "green_candle",
            "red_candle",
            "black_candle",
            "torchflower",
            "blaze_rod",
            "sextlotus_leaf",
            "a_bush"
    );


    /**
     * 自动为 ImmortalersCakeBlock 生成完整的 49 种状态映射
     */
    public void registerImmortalersCake(Block block) {
        ResourceLocation blockId = ForgeRegistries.BLOCKS.getKey(block);
        if (blockId == null) return;

        String cakeName = blockId.getPath(); // 例如 "golden_cake" 或 "moonlight_cake"
        VariantBlockStateBuilder builder = getVariantBuilder(block);

        // 1. 生成普通未插蜡烛状态 (candles=1, lit=false, bites=0~6)
        for (int bites = 0; bites <= 6; bites++) {
            String modelPath = bites == 0
                    ? "block/" + cakeName
                    : "block/" + cakeName + "_slice" + bites;

            builder.partialState()
                    .with(ImmortalersCakeBlock.CANDLES, 1)
                    .with(ImmortalersCakeBlock.BITES, bites)
                    .setModels(new ConfiguredModel(models().getExistingFile(modLoc(modelPath))));
        }

        // 2. 生成 21 种插了蜡烛的状态 (candles=2~4, bites 对应求模索引, lit=false/true)
        for (int index = 0; index < CANDLE_SUFFIXES.size(); index++) {
            int bitesValue = index % 7;
            int candlesValue = (index / 7) + 2;
            String candleSuffix = CANDLE_SUFFIXES.get(index);

            // 未点燃状态 (lit=false)
            String unlitModelPath = "block/" + cakeName + "_" + candleSuffix;
            builder.partialState()
                    .with(ImmortalersCakeBlock.CANDLES, candlesValue)
                    .with(ImmortalersCakeBlock.LIT, false)
                    .with(ImmortalersCakeBlock.BITES, bitesValue)
                    .setModels(new ConfiguredModel(models().getExistingFile(modLoc(unlitModelPath))));

            // 点燃状态 (lit=true)
            String litModelPath = "block/" + cakeName + "_" + candleSuffix + "_lit";
            builder.partialState()
                    .with(ImmortalersCakeBlock.CANDLES, candlesValue)
                    .with(ImmortalersCakeBlock.LIT, true)
                    .with(ImmortalersCakeBlock.BITES, bitesValue)
                    .setModels(new ConfiguredModel(models().getExistingFile(modLoc(litModelPath))));
        }
    }
}
