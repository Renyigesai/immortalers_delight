package com.renyigesai.immortalers_delight.init;

import com.renyigesai.immortalers_delight.screen.EnchantalCoolerMenu;
import com.renyigesai.immortalers_delight.screen.TerracottaGolemMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ImmortalersDelightMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, "immortalers_delight");

    public static final RegistryObject<MenuType<EnchantalCoolerMenu>> ENCHANTAL_COOLER_MENU = MENUS.register("enchantal_cooler_menu",
            () -> IForgeMenuType.create(EnchantalCoolerMenu::create));
//
//    public static final RegistryObject<MenuType<TerracottaGolemMenu>> TERRACOTTA_GOLEM_MENU = MENUS.register("terracotta_golem_menu",
//            () -> new MenuType<>(()));
}
