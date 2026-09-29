package com.renyigesai.immortalers_delight.data.builder;


import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

import static com.renyigesai.immortalers_delight.data.Recipes.has;

public class SeriesBlockRecipeBuilder {
    public static void buildRecipes(Consumer<FinishedRecipe> consumer) {
        // ===== 基础方块合成 =====
        polishedFromWraithstone(consumer);
        wraithstoneBrickFromPolished(consumer);
        mossyWraithstone(consumer);
        chiseledWraithstoneFromSlab(consumer);

        // ===== 台阶/楼梯/墙 工作台配方 =====
        makeSlabStairsWall(
                consumer,
                ImmortalersDelightBlocks.WRAITHSTONE.get(),
                ImmortalersDelightBlocks.WRAITHSTONE_SLAB.get(),
                ImmortalersDelightBlocks.WRAITHSTONE_STAIRS.get(),
                ImmortalersDelightBlocks.WRAITHSTONE_WALL.get()
        );
        makeSlabStairsWall(
                consumer,
                ImmortalersDelightBlocks.POLISHED_WRAITHSTONE.get(),
                ImmortalersDelightBlocks.POLISHED_WRAITHSTONE_SLAB.get(),
                ImmortalersDelightBlocks.POLISHED_WRAITHSTONE_STAIRS.get(),
                ImmortalersDelightBlocks.POLISHED_WRAITHSTONE_WALL.get()
        );
        makeSlabStairsWall(
                consumer,
                ImmortalersDelightBlocks.WRAITHSTONE_BRICK.get(),
                ImmortalersDelightBlocks.WRAITHSTONE_BRICK_SLAB.get(),
                ImmortalersDelightBlocks.WRAITHSTONE_BRICK_STAIRS.get(),
                ImmortalersDelightBlocks.WRAITHSTONE_BRICK_WALL.get()
        );
        makeSlabStairsWall(
                consumer,
                ImmortalersDelightBlocks.MOSSY_WRAITHSTONE.get(),
                ImmortalersDelightBlocks.MOSSY_WRAITHSTONE_SLAB.get(),
                ImmortalersDelightBlocks.MOSSY_WRAITHSTONE_STAIRS.get(),
                ImmortalersDelightBlocks.MOSSY_WRAITHSTONE_WALL.get()
        );

        // ===== 切石机配方 =====
        stonecuttingVariants(
                consumer,
                ImmortalersDelightBlocks.WRAITHSTONE.get(),
                ImmortalersDelightBlocks.WRAITHSTONE_SLAB.get(),
                ImmortalersDelightBlocks.WRAITHSTONE_STAIRS.get(),
                ImmortalersDelightBlocks.WRAITHSTONE_WALL.get()
        );
        stonecuttingVariants(
                consumer,
                ImmortalersDelightBlocks.POLISHED_WRAITHSTONE.get(),
                ImmortalersDelightBlocks.POLISHED_WRAITHSTONE_SLAB.get(),
                ImmortalersDelightBlocks.POLISHED_WRAITHSTONE_STAIRS.get(),
                ImmortalersDelightBlocks.POLISHED_WRAITHSTONE_WALL.get()
        );
        stonecuttingVariants(
                consumer,
                ImmortalersDelightBlocks.WRAITHSTONE_BRICK.get(),
                ImmortalersDelightBlocks.WRAITHSTONE_BRICK_SLAB.get(),
                ImmortalersDelightBlocks.WRAITHSTONE_BRICK_STAIRS.get(),
                ImmortalersDelightBlocks.WRAITHSTONE_BRICK_WALL.get()
        );
        stonecuttingVariants(
                consumer,
                ImmortalersDelightBlocks.MOSSY_WRAITHSTONE.get(),
                ImmortalersDelightBlocks.MOSSY_WRAITHSTONE_SLAB.get(),
                ImmortalersDelightBlocks.MOSSY_WRAITHSTONE_STAIRS.get(),
                ImmortalersDelightBlocks.MOSSY_WRAITHSTONE_WALL.get()
        );

        // 切石机额外：怨灵石砖 -> 雕纹怨灵石
        stonecutting(consumer,
                ImmortalersDelightBlocks.WRAITHSTONE_BRICK.get(),
                ImmortalersDelightBlocks.CHISELED_WRAITHSTONE.get(),
                1
        );
    }

    // ---------- 基础方块 ----------

    private static void polishedFromWraithstone(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ImmortalersDelightBlocks.POLISHED_WRAITHSTONE.get(), 4)
                .pattern("XX")
                .pattern("XX")
                .define('X', ImmortalersDelightBlocks.WRAITHSTONE.get())
                .unlockedBy("has_wraithstone", has(ImmortalersDelightBlocks.WRAITHSTONE.get()))
                .save(consumer, new ResourceLocation(ImmortalersDelightMod.MODID, "polished_wraithstone"));
    }

    private static void wraithstoneBrickFromPolished(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ImmortalersDelightBlocks.WRAITHSTONE_BRICK.get(), 4)
                .pattern("XX")
                .pattern("XX")
                .define('X', ImmortalersDelightBlocks.POLISHED_WRAITHSTONE.get())
                .unlockedBy("has_polished_wraithstone", has(ImmortalersDelightBlocks.POLISHED_WRAITHSTONE.get()))
                .save(consumer, new ResourceLocation(ImmortalersDelightMod.MODID, "wraithstone_brick"));
    }

    private static void mossyWraithstone(Consumer<FinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS,ImmortalersDelightBlocks.MOSSY_WRAITHSTONE.get(), 1)
                .requires(ImmortalersDelightBlocks.WRAITHSTONE.get())
                .requires(Items.SOUL_SAND)
                .unlockedBy("has_wraithstone", has(ImmortalersDelightBlocks.WRAITHSTONE.get()))
                .save(consumer, new ResourceLocation(ImmortalersDelightMod.MODID, "mossy_wraithstone"));
    }

    private static void chiseledWraithstoneFromSlab(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ImmortalersDelightBlocks.CHISELED_WRAITHSTONE.get(), 1)
                .pattern("X")
                .pattern("X")
                .define('X', ImmortalersDelightBlocks.WRAITHSTONE_BRICK_SLAB.get())
                .unlockedBy("has_wraithstone_brick_slab", has(ImmortalersDelightBlocks.WRAITHSTONE_BRICK_SLAB.get()))
                .save(consumer, new ResourceLocation(ImmortalersDelightMod.MODID, "chiseled_wraithstone"));
    }

    // ---------- 工作台：台阶 / 楼梯 / 墙 ----------

    private static void makeSlabStairsWall(
            Consumer<FinishedRecipe> consumer,
            ItemLike material,
            ItemLike slab,
            ItemLike stairs,
            ItemLike wall
    ) {
        slab(consumer, material, slab);
        stairs(consumer, material, stairs);
        wall(consumer, material, wall);
    }

    private static void slab(Consumer<FinishedRecipe> consumer, ItemLike material, ItemLike result) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result, 6)
                .pattern("XXX")
                .define('X', material)
                .unlockedBy("has_" + name(material), has(material))
                .save(consumer, new ResourceLocation(ImmortalersDelightMod.MODID, name(result) + "_from_" + name(material)));
    }

    private static void stairs(Consumer<FinishedRecipe> consumer, ItemLike material, ItemLike result) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result, 4)
                .pattern("X  ")
                .pattern("XX ")
                .pattern("XXX")
                .define('X', material)
                .unlockedBy("has_" + name(material), has(material))
                .save(consumer, new ResourceLocation(ImmortalersDelightMod.MODID, name(result) + "_from_" + name(material)));
    }

    private static void wall(Consumer<FinishedRecipe> consumer, ItemLike material, ItemLike result) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result, 6)
                .pattern("XXX")
                .pattern("XXX")
                .define('X', material)
                .unlockedBy("has_" + name(material), has(material))
                .save(consumer, new ResourceLocation(ImmortalersDelightMod.MODID, name(result) + "_from_" + name(material)));
    }

    // ---------- 切石机 ----------

    private static void stonecuttingVariants(
            Consumer<FinishedRecipe> consumer,
            ItemLike material,
            ItemLike slab,
            ItemLike stairs,
            ItemLike wall
    ) {
        stonecutting(consumer, material, slab, 2);
        stonecutting(consumer, material, stairs, 1);
        stonecutting(consumer, material, wall, 1);
    }

    private static void stonecutting(Consumer<FinishedRecipe> consumer, ItemLike input, ItemLike result, int count) {
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(input), RecipeCategory.BUILDING_BLOCKS, result, count)
                .unlockedBy("has_" + name(input), has(input))
                .save(consumer, new ResourceLocation(
                        ImmortalersDelightMod.MODID,
                        name(result) + "_from_" + name(input) + "_stonecutting"
                ));
    }

    // ---------- 小工具 ----------

    private static String name(ItemLike itemLike) {
        return itemLike.asItem().builtInRegistryHolder().key().location().getPath();
    }
}
