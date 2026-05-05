package com.renyigesai.immortalers_delight.init;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.api.annotation.ItemData;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.lang.reflect.Field;

public class ImmortalersDelightGroup {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ImmortalersDelightMod.MODID);


    /**name加数字是为了排序*/
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN_TAB = CREATIVE_TABS.register("0_" + ImmortalersDelightMod.MODID + "_main",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("creativetab_immortalers_delight_main_tab"))
                    .icon(() -> new ItemStack(ImmortalersDelightItems.EVOLUTCORN.get()))
                    .displayItems((parameters, output) -> {
                        addCreativeModeTab(output,ImmortalersDelightMod.MODID + "_main",ImmortalersDelightItems.class,true);
                    })
                    .build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> DECORATIVE_BLOCKS_TAB = CREATIVE_TABS.register("1_" + ImmortalersDelightMod.MODID + "_decorative_blocks",
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
                    DeferredHolder<Item, Item> deferredItem = null;
                    if (object instanceof DeferredHolder<?, ?> registryObject){
                        if (Item.class.isAssignableFrom(registryObject.get().getClass())){
                            @SuppressWarnings("unchecked")
                            DeferredHolder<Item, Item> itemHolder = (DeferredHolder<Item, Item>) registryObject;
                            deferredItem = itemHolder;
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
