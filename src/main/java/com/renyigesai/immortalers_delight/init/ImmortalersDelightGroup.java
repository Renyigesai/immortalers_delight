package com.renyigesai.immortalers_delight.init;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.api.annotation.ItemData;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.lang.reflect.Field;

public class ImmortalersDelightGroup {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ImmortalersDelightMod.MODID);

//    public static final RegistryObject<CreativeModeTab> TAB_FARMERS_DELIGHT = CREATIVE_TABS.register(ImmortalersDelightMod.MODID + "_main",
//            () -> CreativeModeTab.builder()
//                    .title(Component.translatable("creativetab_immortalers_delight_tab"))
//                    .icon(() -> new ItemStack(ImmortalersDelightItems.EVOLUTCORN.get()))
//                    .displayItems((parameters, output) -> ImmortalersDelightItems.CREATIVE_TAB_ITEMS.forEach((item) -> output.accept(item.get())))
//                    .build());

    public static final RegistryObject<CreativeModeTab> MAIN_TAB = CREATIVE_TABS.register(ImmortalersDelightMod.MODID + "_main",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("creativetab_immortalers_delight_main_tab"))
                    .icon(() -> new ItemStack(ImmortalersDelightItems.EVOLUTCORN.get()))
                    .displayItems((parameters, output) -> {
                        addCreativeModeTab(output,ImmortalersDelightMod.MODID + "_main",ImmortalersDelightItems.class,true);
                        ImmortalersDelightItems.CREATIVE_TAB_ITEMS.forEach(itemRegistryObject ->  output.accept(itemRegistryObject.get()));
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> DECORATIVE_BLOCKS_TAB = CREATIVE_TABS.register(ImmortalersDelightMod.MODID + "_decorative_blocks",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("creativetab_immortalers_delight_decorative_blocks_tab"))
                    .icon(() -> new ItemStack(ImmortalersDelightItems.HIMEKAIDO_LOG.get()))
                    .displayItems((parameters, output) -> addCreativeModeTab(output,ImmortalersDelightMod.MODID + "_decorative_blocks",ImmortalersDelightItems.class,true))
                    .build());

    private static void addCreativeModeTab(CreativeModeTab.Output output,String tab,Class<?> items,boolean conditions){
        if (!conditions){
            return;
        }
        for (Field field : items.getDeclaredFields()) {
            boolean isAnnotationPresent = field.isAnnotationPresent(ItemData.class);
            if (isAnnotationPresent){
                try {
                    Object object = field.get(null);
                    RegistryObject<Item> deferredItem = null;
                    if (object instanceof RegistryObject<?> registryObject){
                        if (Item.class.isAssignableFrom(registryObject.get().getClass())){
                            deferredItem = (RegistryObject<Item>) registryObject;
                        }
                        if (deferredItem != null){
                            ItemData annotation = field.getAnnotation(ItemData.class);
                            if (annotation != null && tab.equals(annotation.group())){
                                Item item = deferredItem.get();
                                output.accept(item);
                            }
                        }
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
