package com.renyigesai.immortalers_delight.data;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.api.annotation.ItemData;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class ItemModels extends ItemModelProvider {

    private static final Map<ItemData.ModelType,String> MODEL = Map.of(ItemData.ModelType.TRAPDOOR,"_bottom", ItemData.ModelType.FENCE,"_inventory",ItemData.ModelType.WALL,"_inventory");
    public ItemModels(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ImmortalersDelightMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        Class<ImmortalersDelightItems> _class = ImmortalersDelightItems.class;
        for (Field field : _class.getDeclaredFields()) {
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
                            ItemData.ModelType model = annotation.model();
                            if (model != ItemData.ModelType.CUSTOM) {
                                Item item = deferredItem.get();
                                if (model == ItemData.ModelType.GENERAL) {
                                    basicItem(item);
                                }
                                if (model == ItemData.ModelType.TOOL) {
                                    toolItem(item);
                                }
                                if (isBlockItem(item)){
                                    BlockItem blockItem = (BlockItem) item;
                                    if (isBlockModType(model)){
                                        blockItem(blockItem::getBlock,model);
                                    }
                                }
                            }
                        }
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private boolean isBlockModType(ItemData.ModelType model){
        return model == ItemData.ModelType.BLOCK ||
               model == ItemData.ModelType.TRAPDOOR ||
               model == ItemData.ModelType.WALL ||
               model == ItemData.ModelType.FENCE;
    }

    private boolean isBlockItem(Item item){
        return item instanceof BlockItem;
    }

    private ItemModelBuilder blockItem(Supplier<Block> block, String index) {
        return this.getBuilder(this.name(block.get()))
                .parent(new ModelFile.UncheckedModelFile(this.modLoc("block/" + this.name(block.get())+index)));
    }

    private void blockItem(Supplier<Block> block,ItemData.ModelType model) {
        if (MODEL.get(model) != null){
            this.withExistingParent(this.name(block.get()), this.modLoc("block/" + this.name(block.get()) + MODEL.get(model)));
        }else {
            this.withExistingParent(this.name(block.get()), this.modLoc("block/" + this.name(block.get())));
        }

    }

    public ItemModelBuilder toolItem(Item item) {
        return createToolItem(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item)),"");
    }

    private ItemModelBuilder createToolItem(ResourceLocation item, String name) {
        return getBuilder(item.toString()+name)
                .parent(new ModelFile.UncheckedModelFile("item/handheld"))
                .texture("layer0",new  ResourceLocation(item.getNamespace(), "item/" + item.getPath()));
    }

    private String name(Block block) {
        return Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(block)).getPath();
    }
}
