package com.renyigesai.immortalers_delight.init;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ImmortalersDelightGroup {

    public static final DeferredRegister<CreativeModeTab> REGISTER =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ImmortalersDelightMod.MODID);
    public static final RegistryObject<CreativeModeTab> IMMORTALERS_DELIGHT_TAB = REGISTER.register("immortalers_delight_tab",() ->
            CreativeModeTab.builder().icon(()-> new ItemStack(ImmortalersDelightItems.EVOLUTCORN.get()))
                    .title(Component.translatable("creativetab_immortalers_delight_tab"))
                    .displayItems(((itemDisplayParameters, output) -> {
                        output.accept(ImmortalersDelightBlocks.HIMEKAIDO_LOG.get());
                        output.accept(ImmortalersDelightBlocks.STRIPPED_HIMEKAIDO_LOG.get());
                        output.accept(ImmortalersDelightBlocks.HIMEKAIDO_WOOD.get());
                        output.accept(ImmortalersDelightBlocks.STRIPPED_HIMEKAIDO_WOOD.get());
                        output.accept(ImmortalersDelightBlocks.HIMEKAIDO_PLANKS.get());
                        output.accept(ImmortalersDelightBlocks.HIMEKAIDO_STAIRS.get());
                        output.accept(ImmortalersDelightBlocks.HIMEKAIDO_SLAB.get());
                        output.accept(ImmortalersDelightBlocks.HIMEKAIDO_FENCE.get());
                        output.accept(ImmortalersDelightBlocks.HIMEKAIDO_FENCE_GATE.get());
                        output.accept(ImmortalersDelightBlocks.HIMEKAIDO_DOOR.get());
                        output.accept(ImmortalersDelightBlocks.HIMEKAIDO_TRAPDOOR.get());
                        output.accept(ImmortalersDelightBlocks.HIMEKAIDO_PRESSURE_PLATE.get());
                        output.accept(ImmortalersDelightBlocks.HIMEKAIDO_BUTTON.get());
                        output.accept(ImmortalersDelightBlocks.HIMEKAIDO_CABINET.get());
                        output.accept(ImmortalersDelightItems.EVOLUTCORN.get());
                        output.accept(ImmortalersDelightItems.EVOLUTCORN_GRAINS.get());
                        output.accept(ImmortalersDelightItems.ROAST_EVOLUTCORN.get());
                        output.accept(ImmortalersDelightItems.ROAST_EVOLUTCORN_CHOPS.get());
                        output.accept(ImmortalersDelightItems.ANCIENT_FIBER.get());
                        output.accept(ImmortalersDelightItems.POPOLUTCORN.get());
                        output.accept(ImmortalersDelightItems.CRETACEOUS_ZEA_BALL.get());
                        output.accept(ImmortalersDelightBlocks.ZEA_PANCAKE.get());
                        output.accept(ImmortalersDelightItems.ZEA_PANCAKE_SLICE.get());
                        output.accept(ImmortalersDelightItems.PEARLIP.get());
                        output.accept(ImmortalersDelightItems.PEARLIP_SHELL.get());
                        output.accept(ImmortalersDelightItems.PEARLIPEARL.get());
                        output.accept(ImmortalersDelightItems.PEARLIPEARL_EGGSTEAM.get());
                        output.accept(ImmortalersDelightItems.PEARLIP_JELLY.get());
                        output.accept(ImmortalersDelightItems.PEARLIP_PUMPKIN_PIE.get());
                        output.accept(ImmortalersDelightItems.PEARLIPEARL_TART.get());
                        output.accept(ImmortalersDelightItems.PEARLIP_MILK_SHAKE.get());
                        output.accept(ImmortalersDelightBlocks.MILLENIAN_BAMBOO.get());
                        output.accept(ImmortalersDelightItems.BOWL_OF_MILLENIAN_BAMBOO.get());
                        output.accept(ImmortalersDelightItems.PEATIC_MUSA_SALAD.get());
                        output.accept(ImmortalersDelightItems.COLORFUL_GRILLED_SKEWERS.get());

                    }))
                    .build());

}
