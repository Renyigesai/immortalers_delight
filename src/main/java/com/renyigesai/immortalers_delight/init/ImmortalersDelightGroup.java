package com.renyigesai.immortalers_delight.init;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ImmortalersDelightGroup {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ImmortalersDelightMod.MODID);

    public static final RegistryObject<CreativeModeTab> TAB_FARMERS_DELIGHT = CREATIVE_TABS.register(ImmortalersDelightMod.MODID,
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("creativetab_immortalers_delight_tab"))
                    .icon(() -> new ItemStack(ImmortalersDelightItems.EVOLUTCORN.get()))
                    .displayItems((parameters, output) -> ImmortalersDelightItems.CREATIVE_TAB_ITEMS.forEach((item) -> output.accept(item.get())))
                    .build());


}
