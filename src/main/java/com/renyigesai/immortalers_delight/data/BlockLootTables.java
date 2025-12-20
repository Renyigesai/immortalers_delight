package com.renyigesai.immortalers_delight.data;


import com.renyigesai.immortalers_delight.api.annotation.BlockData;
import com.renyigesai.immortalers_delight.block.DrinksBlock;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.RegistryObject;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BlockLootTables extends BlockLootSubProvider {

    private final Set<Block> generatedLootTables = new HashSet<>();

    public BlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        System.out.println("Yes Generate");
        dropAll();
        System.out.println("End Generate");
    }

    private void dropAll(){
        System.out.println("Yes dropAll()");
        Class<ImmortalersDelightBlocks> _class = ImmortalersDelightBlocks.class;
        for (Field field : _class.getDeclaredFields()) {
            if (field.isAnnotationPresent(BlockData.class)) {
                try {
                    Object object = field.get(null);
                    RegistryObject<Block> deferredBlock = null;
                    if (object instanceof RegistryObject<?> registryObject) {
                        if (Block.class.isAssignableFrom(registryObject.get().getClass())) {
                            deferredBlock = (RegistryObject<Block>) registryObject;
                        }
                        if (deferredBlock != null) {
                            System.out.println("Yes deferredBlock " + deferredBlock.get());
                            BlockData annotation = field.getAnnotation(BlockData.class);
                            BlockData.DropType dropType = annotation.dropType();
                            boolean generalDrop = dropType == BlockData.DropType.GENERAL;
                            System.out.println("Yes Drop " + generalDrop);
                            if (generalDrop) {
                                System.out.println("generalDrop Yes");
                                dropSelf(deferredBlock.get());
                            } else {
                                customDrop();
                            }
                        }
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void customDrop(){
        add(ImmortalersDelightBlocks.ANCIENT_WOOD_DOOR.get(),createDoorTable(ImmortalersDelightBlocks.ANCIENT_WOOD_DOOR.get()));
        add(ImmortalersDelightBlocks.HIMEKAIDO_DOOR.get(),createDoorTable(ImmortalersDelightBlocks.HIMEKAIDO_DOOR.get()));
        add(ImmortalersDelightBlocks.HIMEKAIDO_DOOR.get(),createDoorTable(ImmortalersDelightBlocks.HIMEKAIDO_DOOR.get()));
        add(ImmortalersDelightBlocks.LEISAMBOO_DOOR.get(),createDoorTable(ImmortalersDelightBlocks.LEISAMBOO_DOOR.get()));
        addAllDrink(ImmortalersDelightBlocks.LEISAMBOO_TEA.get(),
                ImmortalersDelightBlocks.ICED_BLACK_TEA.get(),
                ImmortalersDelightBlocks.PEARLIPEARL_MILK_TEA.get(),
                ImmortalersDelightBlocks.PEARLIPEARL_MILK_GREEN.get(),
                ImmortalersDelightBlocks.STOVE_BLACK_TEA.get(),
                ImmortalersDelightBlocks.LEAF_GREEN_TEA.get(),
                ImmortalersDelightBlocks.BRITISH_YELLOW_TEA.get(),
                ImmortalersDelightBlocks.LEAF_TEA.get(),
                ImmortalersDelightBlocks.YOGURT.get(),
                ImmortalersDelightBlocks.EVOLUTCORN_BEER.get(),
                ImmortalersDelightBlocks.STICKY_BEER.get(),
                ImmortalersDelightBlocks.VULCAN_COKTAIL.get(),
                ImmortalersDelightBlocks.DREUMK_WINE.get(),
                ImmortalersDelightBlocks.PIGLIN_ODORI_SAKE.get(),
                ImmortalersDelightBlocks.PEARLIP_BUBBLE_MILK.get(),
                ImmortalersDelightBlocks.PURGATORY_ALE.get(),
                ImmortalersDelightBlocks.FRUIT_TEA.get()
                );
    }

    private void addAllDrink(Block... blocks){
        List<Block> blockList = List.of(blocks);
        for (Block block : blockList) {
            this.add(block, blockIn -> this.createStateDrops(block, DrinksBlock.PILE));
        }
    }

    protected LootTable.Builder createStateDrops(Block pBlock, IntegerProperty property) {
        return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                .add(LootItem.lootTableItem(pBlock).apply(property.getPossibleValues(), integer ->
                        SetItemCountFunction.setCount(ConstantValue.exactly(integer))
                                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(pBlock)
                                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(property, integer)))
                ))
        );
    }



    @Override
    protected void add(Block block, LootTable.Builder builder) {
        this.generatedLootTables.add(block);
        this.map.put(block.getLootTable(), builder);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return generatedLootTables;
    }
}