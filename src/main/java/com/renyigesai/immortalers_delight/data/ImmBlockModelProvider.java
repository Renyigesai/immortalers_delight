package com.renyigesai.immortalers_delight.data;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.List;

public class ImmBlockModelProvider extends BlockModelProvider {

    // 17 种原版蜡烛的前缀映射
    private static final List<String> VANILLA_CANDLES = List.of(
            "",                 // 默认蜡烛 (candle_cake)
            "white_", "orange_", "magenta_", "light_blue_", "yellow_", "lime_",
            "pink_", "gray_", "light_gray_", "cyan_", "purple_", "blue_",
            "brown_", "green_", "red_", "black_"
    );

    // 4 种模组自制特殊插入物 ID
    private static final List<String> SPECIAL_CANDLES = List.of(
            "torchflower",
            "blaze_rod",
            "sextlotus_leaf",
            "a_bush"
    );

    public ImmBlockModelProvider(PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
        super(output, modid, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        // 为 Golden Cake 批量生成所有模型 JSON
        generateCakeModels("golden_cake");

        // 为 Moonlight Cake 批量生成所有模型 JSON
        generateCakeModels("moonlight_cake");
    }

    /**
     * 自动为指定蛋糕生成全部 49 个模型 JSON
     * 纹理默认放置在: assets/immortalers_delight/textures/block/{cakeName}_top.png 等
     */
    public void generateCakeModels(String cakeName) {
        // 绑定该蛋糕的 4 种基础贴图路径
        ResourceLocation topTex = modLoc("block/" + cakeName + "_top");
        ResourceLocation bottomTex = modLoc("block/" + cakeName + "_bottom");
        ResourceLocation sideTex = modLoc("block/" + cakeName + "_side");
        ResourceLocation insideTex = modLoc("block/" + cakeName + "_inner"); // 原版一般为 cake_inner

        // 1. 生成纯蛋糕完整模型 (golden_cake.json)
        withExistingParent(cakeName, mcLoc("block/cake"))
                .texture("top", topTex)
                .texture("bottom", bottomTex)
                .texture("side", sideTex)
                .texture("inside", insideTex)
                .texture("particle", sideTex);

        // 2. 生成 6 个切片模型 (golden_cake_slice1 ~ 6.json)
        for (int slice = 1; slice <= 6; slice++) {
            withExistingParent(cakeName + "_slice" + slice, mcLoc("block/cake_slice" + slice))
                    .texture("top", topTex)
                    .texture("bottom", bottomTex)
                    .texture("side", sideTex)
                    .texture("inside", insideTex)
                    .texture("particle", sideTex);
        }

        // 3. 生成 17 种原版蜡烛模型 (未点亮 + 点亮，共 34 个)
        for (String candle : VANILLA_CANDLES) {
            String parentName = candle + "candle_cake"; // 对应原版如 block/red_candle_cake
            String targetUnlit = cakeName + "_" + candle + "candle";
            String targetLit = targetUnlit + "_lit";

            // 未点亮
            withExistingParent(targetUnlit, mcLoc("block/" + parentName))
                    .texture("top", topTex)
                    .texture("bottom", bottomTex)
                    .texture("side", sideTex)
                    .texture("inside", insideTex)
                    .texture("particle", sideTex);

            // 点亮状态
            withExistingParent(targetLit, mcLoc("block/" + parentName + "_lit"))
                    .texture("top", topTex)
                    .texture("bottom", bottomTex)
                    .texture("side", sideTex)
                    .texture("inside", insideTex)
                    .texture("particle", sideTex);
        }

        // 4. 生成 4 种自制特殊插入物模型 (未点亮 + 点亮，共 8 个)
        // 注意：此处继承的是模组自制的模板（例如 block/template/template_torchflower_cake）
        for (String specialId : SPECIAL_CANDLES) {
            String targetUnlit = cakeName + "_" + specialId;
            String targetLit = targetUnlit + "_lit";

            // 特殊插入物未点亮
            withExistingParent(targetUnlit, modLoc("block/template/template_" + specialId + "_cake"))
                    .texture("top", topTex)
                    .texture("bottom", bottomTex)
                    .texture("side", sideTex)
                    .texture("inside", insideTex)
                    .texture("particle", sideTex);

            // 特殊插入物点亮
            withExistingParent(targetLit, modLoc("block/template/template_" + specialId + "_cake"))
                    .texture("top", topTex)
                    .texture("bottom", bottomTex)
                    .texture("side", sideTex)
                    .texture("inside", insideTex)
                    .texture("particle", sideTex);
        }
    }
}