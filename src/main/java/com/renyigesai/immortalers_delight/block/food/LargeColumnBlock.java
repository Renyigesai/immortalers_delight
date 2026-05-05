package com.renyigesai.immortalers_delight.block.food;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

public class LargeColumnBlock extends StackedBreadBlock{

    public static final MapCodec<LargeColumnBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BlockBehaviour.propertiesCodec(),
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("serving_item").forGetter(b -> b.servingItem.get()),
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("pile_item").forGetter(b -> b.pileItem.get()),
            Codec.INT.fieldOf("pile_per_item").forGetter(b -> b.pilePerItem)
    ).apply(instance, (props, serving, pile, perItem) -> new LargeColumnBlock(props, () -> serving, () -> pile, perItem)));

    private static final VoxelShape[] SHAPE_BY_AGE_0 = new VoxelShape[]{
            Block.box(0.1D, 0.0D, 0.1D, 15.9D, 16.0D, 15.9D),
            Block.box(0.1D, 0.0D, 0.1D, 15.9D, 11.0D, 15.9D),
            Block.box(1.0D, 0.0D, 1.0D, 15.0D, 6.0D, 15.0D),
            Block.box(1.0D, 0.0D, 1.0D, 15.0D, 1.0D, 15.0D)
    };
    public LargeColumnBlock(Properties p_54120_, Supplier<Item> servingItem, Supplier<Item> pileItem, int pilePerItem) {
        super(p_54120_, servingItem, pileItem, pilePerItem);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    public int getMaxBites(){return 6;}
    @Override
    public boolean isEdible(){return false;}
    @Override
    public boolean isCuttable(ItemStack stack){return stack.is(ItemTags.AXES);}
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        int i =state.getValue(BITES) / 2;
        if (i >= SHAPE_BY_AGE_0.length) i = SHAPE_BY_AGE_0.length - 1;
        return SHAPE_BY_AGE_0[i];
    }
}
