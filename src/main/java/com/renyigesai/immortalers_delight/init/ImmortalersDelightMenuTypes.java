package com.renyigesai.immortalers_delight.init;

import com.renyigesai.immortalers_delight.screen.EnchantalCoolerMenu;
import com.renyigesai.immortalers_delight.screen.TerracottaGolemMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ImmortalersDelightMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, "immortalers_delight");

    public static final DeferredHolder<MenuType<?>, MenuType<EnchantalCoolerMenu>> ENCHANTAL_COOLER_MENU = MENUS.register("enchantal_cooler_menu",
            () -> IMenuTypeExtension.create(EnchantalCoolerMenu::create));

    public static final DeferredHolder<MenuType<?>, MenuType<TerracottaGolemMenu>> TERRACOTTA_GOLEM_MENU = MENUS.register("terracotta_golem_menu",
            () -> IMenuTypeExtension.create(TerracottaGolemMenu::new));
}
