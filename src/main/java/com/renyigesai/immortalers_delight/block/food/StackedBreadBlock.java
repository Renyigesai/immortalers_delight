package com.renyigesai.immortalers_delight.block.food;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.renyigesai.immortalers_delight.block.StackedFoodBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

public class StackedBreadBlock extends StackedFoodBlock {

    public static final MapCodec<StackedBreadBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BlockBehaviour.propertiesCodec(),
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("serving_item").forGetter(b -> b.servingItem.get()),
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("pile_item").forGetter(b -> b.pileItem.get()),
            Codec.INT.fieldOf("pile_per_item").forGetter(b -> b.pilePerItem)
    ).apply(instance, (props, serving, pile, perItem) -> new StackedBreadBlock(props, () -> serving, () -> pile, perItem)));

    public StackedBreadBlock(Properties p_54120_, Supplier<Item> servingItem, Supplier<Item> pileItem, int pilePerItem) {
        super(p_54120_, servingItem, pileItem, pilePerItem);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }
}
