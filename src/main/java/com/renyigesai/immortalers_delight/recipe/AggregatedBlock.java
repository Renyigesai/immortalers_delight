package com.renyigesai.immortalers_delight.recipe;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Block-state analogue of {@link net.minecraft.world.item.crafting.Ingredient}:
 * matches a single block state, a tag of blocks, or a union of either.
 */
public class AggregatedBlock implements Predicate<BlockState> {
    private static final AtomicInteger INVALIDATION_COUNTER = new AtomicInteger();

    public static void invalidateAll() {
        INVALIDATION_COUNTER.incrementAndGet();
    }

    public static final AggregatedBlock EMPTY = new AggregatedBlock(Stream.empty());

    private final AggregatedBlock.Value[] values;
    @Nullable
    private BlockState[] blockStates;
    private int invalidationCounter;

    public static final Codec<Value> VALUE_CODEC = Codec.either(
            BlockState.CODEC.fieldOf("block").codec(),
            ResourceLocation.CODEC.fieldOf("tag").codec()
    ).flatXmap(
            either -> either.map(
                    state -> {
                        if (state.isAir()) {
                            return DataResult.error(() -> "Air block is not allowed in inputs of change block recipe!");
                        }
                        return DataResult.success(new BlockValue(state));
                    },
                    tagId -> DataResult.success(new TagValue(TagKey.create(Registries.BLOCK, tagId)))
            ),
            value -> {
                if (value instanceof BlockValue blockValue) {
                    return DataResult.success(Either.left(blockValue.blockState));
                }
                if (value instanceof TagValue tagValue) {
                    return DataResult.success(Either.right(tagValue.tag.location()));
                }
                return DataResult.error(() -> "Unknown AggregatedBlock value type");
            }
    );

    public static final Codec<AggregatedBlock> CODEC = Codec.either(VALUE_CODEC, VALUE_CODEC.listOf()).xmap(
            either -> either.map(
                    value -> fromValues(Stream.of(value)),
                    list -> fromValues(list.stream())
            ),
            aggregated -> {
                if (aggregated.values.length == 1) {
                    return Either.left(aggregated.values[0]);
                }
                return Either.right(Arrays.asList(aggregated.values));
            }
    );

    public static final StreamCodec<ByteBuf, BlockState> BLOCK_STATE_STREAM_CODEC =
            ByteBufCodecs.idMapper(Block.BLOCK_STATE_REGISTRY);

    public static final StreamCodec<RegistryFriendlyByteBuf, AggregatedBlock> STREAM_CODEC =
            StreamCodec.of(AggregatedBlock::writeToNetwork, AggregatedBlock::readFromNetwork);

    protected AggregatedBlock(Stream<? extends AggregatedBlock.Value> pValues) {
        this.values = pValues.toArray(Value[]::new);
    }

    public BlockState[] getBlockStates() {
        if (this.blockStates == null || checkInvalidation()) {
            markValid();
            this.blockStates = Arrays.stream(this.values)
                    .flatMap(value -> value.getBlocks().stream())
                    .distinct()
                    .toArray(BlockState[]::new);
        }
        return this.blockStates;
    }

    @Override
    public boolean test(@Nullable BlockState blockState) {
        if (blockState == null) {
            return false;
        }
        if (this.isEmpty()) {
            return blockState.isAir();
        }
        for (BlockState state : this.getBlockStates()) {
            if (!state.is(blockState.getBlock())) {
                continue;
            }
            Collection<Property<?>> propertiesExclude = getDefaultOrNonProperties(state, false);
            if (propertiesExclude.isEmpty()) {
                return true;
            }
            boolean isRight = true;
            for (Property<?> property : propertiesExclude) {
                Comparable<?> expected = state.getValue(property);
                Comparable<?> actual = blockState.getValue(property);
                if (!expected.equals(actual)) {
                    isRight = false;
                    break;
                }
            }
            if (isRight) {
                return true;
            }
        }
        return false;
    }

    /**
     * Compare a BlockState to its block's default state and return properties that are
     * non-default ({@code isDefault == false}) or default ({@code isDefault == true}).
     */
    public static Collection<Property<?>> getDefaultOrNonProperties(BlockState currentState, boolean isDefault) {
        Collection<Property<?>> diffProperties = new ArrayList<>();
        BlockState defaultState = currentState.getBlock().defaultBlockState();
        Map<Property<?>, Comparable<?>> currentValues = currentState.getValues();

        for (Property<?> property : currentState.getProperties()) {
            Comparable<?> currentValue = currentValues.get(property);
            Comparable<?> defaultValue = defaultState.getValue(property);
            if (currentValue == null) {
                continue;
            }
            if (!isDefault && !currentValue.equals(defaultValue)) {
                diffProperties.add(property);
            }
            if (isDefault && currentValue.equals(defaultValue)) {
                diffProperties.add(property);
            }
        }
        return diffProperties;
    }

    public boolean isEmpty() {
        return this.values.length == 0;
    }

    public final boolean checkInvalidation() {
        int currentInvalidationCounter = INVALIDATION_COUNTER.get();
        if (this.invalidationCounter != currentInvalidationCounter) {
            invalidate();
            return true;
        }
        return false;
    }

    protected final void markValid() {
        this.invalidationCounter = INVALIDATION_COUNTER.get();
    }

    protected void invalidate() {
        this.blockStates = null;
    }

    public static AggregatedBlock fromValues(Stream<? extends AggregatedBlock.Value> pStream) {
        AggregatedBlock ingredient = new AggregatedBlock(pStream);
        return ingredient.isEmpty() ? EMPTY : ingredient;
    }

    public static AggregatedBlock of() {
        return EMPTY;
    }

    public static AggregatedBlock of(Block... blocks) {
        return of(Arrays.stream(blocks).map(Block::defaultBlockState));
    }

    public static AggregatedBlock of(BlockState... blockStates) {
        return of(Arrays.stream(blockStates));
    }

    public static AggregatedBlock of(Stream<BlockState> stateStream) {
        return fromValues(stateStream.filter(state -> !state.isAir()).map(BlockValue::new));
    }

    public static AggregatedBlock of(TagKey<Block> pTag) {
        return fromValues(Stream.of(new TagValue(pTag)));
    }

    public static void writeToNetwork(RegistryFriendlyByteBuf buf, AggregatedBlock aggregated) {
        buf.writeVarInt(aggregated.values.length);
        for (Value value : aggregated.values) {
            if (value instanceof BlockValue blockValue) {
                buf.writeBoolean(false);
                BLOCK_STATE_STREAM_CODEC.encode(buf, blockValue.blockState);
            } else if (value instanceof TagValue tagValue) {
                buf.writeBoolean(true);
                buf.writeResourceLocation(tagValue.tag.location());
            }
        }
    }

    public static AggregatedBlock readFromNetwork(RegistryFriendlyByteBuf buf) {
        int valueCount = buf.readVarInt();
        Stream.Builder<Value> valueStream = Stream.builder();
        for (int i = 0; i < valueCount; i++) {
            boolean isTag = buf.readBoolean();
            if (!isTag) {
                valueStream.add(new BlockValue(BLOCK_STATE_STREAM_CODEC.decode(buf)));
            } else {
                ResourceLocation tagId = buf.readResourceLocation();
                valueStream.add(new TagValue(TagKey.create(Registries.BLOCK, tagId)));
            }
        }
        return fromValues(valueStream.build());
    }

    @SuppressWarnings("unchecked")
    public static <T extends Comparable<T>> BlockState setPropertyGeneric(BlockState state, Property<T> prop, String valueStr) {
        return prop.getValue(valueStr).map(v -> state.setValue(prop, v)).orElse(state);
    }

    public static AggregatedBlock merge(Collection<AggregatedBlock> parts) {
        return fromValues(parts.stream().flatMap(i -> Arrays.stream(i.values)));
    }

    public static class BlockValue implements AggregatedBlock.Value {
        private final BlockState blockState;

        public BlockValue(BlockState pState) {
            this.blockState = pState;
        }

        @Override
        public Collection<BlockState> getBlocks() {
            return Collections.singleton(this.blockState);
        }
    }

    public static class TagValue implements AggregatedBlock.Value {
        private final TagKey<Block> tag;

        public TagValue(TagKey<Block> pTag) {
            this.tag = pTag;
        }

        @Override
        public Collection<BlockState> getBlocks() {
            List<BlockState> list = new ArrayList<>();
            for (Holder<Block> holder : BuiltInRegistries.BLOCK.getTagOrEmpty(this.tag)) {
                list.add(holder.value().defaultBlockState());
            }
            if (list.isEmpty()) {
                list.add(Blocks.BARRIER.defaultBlockState());
            }
            return list;
        }
    }

    public interface Value {
        Collection<BlockState> getBlocks();
    }
}
