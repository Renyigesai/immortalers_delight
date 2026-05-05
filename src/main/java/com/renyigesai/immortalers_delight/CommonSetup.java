package com.renyigesai.immortalers_delight;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.common.crafting.CompoundIngredient;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.Arrays;

public class CommonSetup
{
	public static void init(final FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			registerCompostables();
			registerItemSetAdditions();
		});
	}

	public static void registerCompostables() {
		/*30%*/
		ComposterBlock.COMPOSTABLES.put(ImmortalersDelightItems.HIMEKAIDO_SEED.get(), 0.3f);
		ComposterBlock.COMPOSTABLES.put(ImmortalersDelightItems.KWAT_WHEAT_SEEDS.get(), 0.3f);
		ComposterBlock.COMPOSTABLES.put(ImmortalersDelightItems.ALFALFA_SEEDS.get(), 0.3f);
		ComposterBlock.COMPOSTABLES.put(ImmortalersDelightItems.EVOLUTCORN_GRAINS.get(), 0.3f);
		ComposterBlock.COMPOSTABLES.put(ImmortalersDelightItems.PEARLIPEARL.get(), 0.3f);
		ComposterBlock.COMPOSTABLES.put(ImmortalersDelightItems.HIMEKAIDO_LEAVES.get(), 0.3f);
		ComposterBlock.COMPOSTABLES.put(ImmortalersDelightItems.HIMEKAIDO_FLOWERING_LEAVES.get(), 0.3f);
		ComposterBlock.COMPOSTABLES.put(ImmortalersDelightItems.ALFALFA.get(), 0.3f);
		ComposterBlock.COMPOSTABLES.put(ImmortalersDelightItems.PEARLIP_SHELL.get(), 0.3f);
		ComposterBlock.COMPOSTABLES.put(ImmortalersDelightItems.EMPTY_BAMBOO_CUP.get(), 0.3f);
		ComposterBlock.COMPOSTABLES.put(ImmortalersDelightItems.WARPED_LAUREL_SEEDS.get(), 0.3f);
		/*65%*/
		ComposterBlock.COMPOSTABLES.put(ImmortalersDelightItems.HIMEKAIDO.get(), 0.65f);
		ComposterBlock.COMPOSTABLES.put(ImmortalersDelightItems.EVOLUTCORN.get(), 0.65f);
		ComposterBlock.COMPOSTABLES.put(ImmortalersDelightItems.KWAT_WHEAT.get(), 0.65f);
		ComposterBlock.COMPOSTABLES.put(ImmortalersDelightItems.PEARLIP.get(), 0.65f);
		ComposterBlock.COMPOSTABLES.put(ImmortalersDelightItems.CONTAINS_TEA_LEISAMBOO.get(), 0.65f);
		ComposterBlock.COMPOSTABLES.put(ImmortalersDelightItems.HIMEKAIDO_FRUITED_LEAVES.get(), 0.65f);
		ComposterBlock.COMPOSTABLES.put(ImmortalersDelightItems.WARPED_LAUREL.get(), 0.65f);
		ComposterBlock.COMPOSTABLES.put(ImmortalersDelightItems.TRAVARICE.get(), 0.65f);
		/*85%*/
		ComposterBlock.COMPOSTABLES.put(ImmortalersDelightItems.EVOLUTCORN_BLOCK.get(), 0.85f);
		ComposterBlock.COMPOSTABLES.put(ImmortalersDelightItems.KWAT_WHEAT_BLOCK.get(), 0.85f);
		ComposterBlock.COMPOSTABLES.put(ImmortalersDelightItems.ALFALFA_BLOCK.get(), 0.85f);
	}

	public static void fuel(){

	}

	public static void registerItemSetAdditions() {
		Ingredient newFood = Ingredient.of(ImmortalersDelightItems.ALFALFA.get());
		Pig.FOOD_ITEMS = new CompoundIngredient(Arrays.asList(Pig.FOOD_ITEMS, newFood))
		{
		};
	}
}
