package com.renyigesai.immortalers_delight.recipe;

import com.google.gson.*;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.registries.ForgeRegistries;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class AggregatedBlock implements Predicate<BlockState> {
    private final Value[] values;
    public static final AggregatedBlock EMPTY = new AggregatedBlock(Stream.empty());
    private AggregatedBlock(Stream<? extends Value> values) { this.values = values.toArray(Value[]::new); }
    public static AggregatedBlock of(Block... blocks) { return fromValues(Arrays.stream(blocks).map(b -> new BlockValue(b.defaultBlockState()))); }
    public static AggregatedBlock of(BlockState... states) { return fromValues(Arrays.stream(states).map(BlockValue::new)); }
    public static AggregatedBlock of(TagKey<Block> tag) { return fromValues(Stream.of(new TagValue(tag))); }
    public static AggregatedBlock fromValues(Stream<? extends Value> values) { AggregatedBlock result = new AggregatedBlock(values); return result.values.length == 0 ? EMPTY : result; }
    public boolean isEmpty() { return values.length == 0; }
    public BlockState[] getBlockStates() { return Arrays.stream(values).flatMap(v -> v.getBlocks().stream()).distinct().toArray(BlockState[]::new); }
    @Override public boolean test(@Nullable BlockState input) {
        if (input == null) return false;
        if (isEmpty()) return input.isAir();
        for (BlockState candidate : getBlockStates()) {
            if (!candidate.is(input.getBlock())) continue;
            boolean matches = true;
            for (Property<?> property : getDefaultOrNonProperties(candidate, false)) {
                if (!input.hasProperty(property) || !candidate.getValue(property).equals(input.getValue(property))) { matches = false; break; }
            }
            if (matches) return true;
        }
        return false;
    }
    public static Collection<Property<?>> getDefaultOrNonProperties(BlockState state, boolean defaults) {
        List<Property<?>> result = new ArrayList<>();
        BlockState defaultState = state.getBlock().defaultBlockState();
        for (Property<?> property : state.getProperties()) {
            boolean equal = state.getValue(property).equals(defaultState.getValue(property));
            if (defaults == equal) result.add(property);
        }
        return result;
    }
    public static BlockState setPropertyGeneric(BlockState state, Property<?> property, String value) {
        return setProperty(state, property, value);
    }
    private static <T extends Comparable<T>> BlockState setProperty(BlockState state, Property<T> property, String value) {
        return property.getValue(value).map(v -> state.setValue(property, v)).orElse(state);
    }
    public static AggregatedBlock fromJson(@Nullable JsonElement json) {
        if (json == null || json.isJsonNull()) throw new JsonSyntaxException("Block cannot be null");
        if (json.isJsonArray()) return fromValues(json.getAsJsonArray().asList().stream().map(e -> valueFromJson(GsonHelper.convertToJsonObject(e, "block"))));
        return fromValues(Stream.of(valueFromJson(GsonHelper.convertToJsonObject(json, "block"))));
    }
    public static Value valueFromJson(JsonObject json) {
        if (json.has("block") && json.has("tag")) throw new JsonSyntaxException("An entry cannot have both block and tag");
        if (json.has("block")) {
            BlockState state = BlockState.CODEC.parse(JsonOps.INSTANCE, json.get("block")).getOrThrow(false, e -> { throw new JsonSyntaxException(e); });
            if (state.isAir()) throw new JsonSyntaxException("Air is not allowed");
            return new BlockValue(state);
        }
        if (json.has("tag")) return new TagValue(TagKey.create(Registries.BLOCK, new ResourceLocation(GsonHelper.getAsString(json, "tag"))));
        throw new JsonSyntaxException("Entry must have block or tag");
    }
    public void writeToNetwork(FriendlyByteBuf buf) { buf.writeVarInt(values.length); for (Value value : values) { if (value instanceof BlockValue b) { buf.writeBoolean(false); writeBlockStateToNetwork(buf, b.state); } else { buf.writeBoolean(true); buf.writeResourceLocation(((TagValue)value).tag.location()); } } }
    public static AggregatedBlock readFromNetwork(FriendlyByteBuf buf) { int count = buf.readVarInt(); Stream.Builder<Value> b = Stream.builder(); for (int i=0;i<count;i++) { if (buf.readBoolean()) b.add(new TagValue(TagKey.create(Registries.BLOCK, buf.readResourceLocation()))); else b.add(new BlockValue(readStateFromNetwork(buf))); } return fromValues(b.build()); }
    public static void writeBlockStateToNetwork(FriendlyByteBuf buf, BlockState state) { buf.writeRegistryId(ForgeRegistries.BLOCKS, state.getBlock()); CompoundTag tag = new CompoundTag(); for (Property<?> p : state.getProperties()) tag.putString(p.getName(), propertyValueName(p, state.getValue(p))); buf.writeNbt(tag); }
    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>> String propertyValueName(Property<T> property, Comparable<?> value) { return property.getName((T) value); }
    public static BlockState readStateFromNetwork(FriendlyByteBuf buf) { Block block = buf.readRegistryIdSafe(Block.class); BlockState state = block.defaultBlockState(); CompoundTag tag = buf.readNbt(); if (tag != null) for (Property<?> p : state.getProperties()) if (tag.contains(p.getName())) state = setPropertyGeneric(state, p, tag.getString(p.getName())); return state; }
    public interface Value { Collection<BlockState> getBlocks(); JsonObject serialize(); }
    public static class BlockValue implements Value { private final BlockState state; BlockValue(BlockState state) { this.state=state; } public Collection<BlockState> getBlocks(){return Collections.singleton(state);} public JsonObject serialize(){JsonObject o=new JsonObject(); o.add("block", BlockState.CODEC.encodeStart(JsonOps.INSTANCE,state).getOrThrow(false,e->{throw new JsonSyntaxException(e);})); return o;} }
    public static class TagValue implements Value { private final TagKey<Block> tag; TagValue(TagKey<Block> tag){this.tag=tag;} public Collection<BlockState> getBlocks(){List<BlockState> r=new ArrayList<>(); for(Holder<Block> h:BuiltInRegistries.BLOCK.getTagOrEmpty(tag)) r.add(h.value().defaultBlockState()); if(r.isEmpty()) r.add(Blocks.BARRIER.defaultBlockState()); return r;} public JsonObject serialize(){JsonObject o=new JsonObject();o.addProperty("tag",tag.location().toString());return o;} }
}
