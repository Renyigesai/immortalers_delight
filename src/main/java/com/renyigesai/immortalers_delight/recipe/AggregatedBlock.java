package com.renyigesai.immortalers_delight.recipe;

import com.google.common.collect.Lists;
import com.google.gson.*;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparators;
import it.unimi.dsi.fastutil.ints.IntList;
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
import java.util.stream.StreamSupport;

public class AggregatedBlock implements Predicate<BlockState> {
    // 全局缓存失效计数器（Forge 用于强制刷新所有配方缓存）
    private static final java.util.concurrent.atomic.AtomicInteger INVALIDATION_COUNTER = new java.util.concurrent.atomic.AtomicInteger();

    // 强制所有 AggregatedBlock 缓存失效
    public static void invalidateAll() {
        INVALIDATION_COUNTER.incrementAndGet();
    }
    // 空材料（什么都不需要）
    public static final AggregatedBlock EMPTY = new AggregatedBlock(Stream.empty());
    // 内部存储的所有“值”（每个值可以是单个物品 或 一个标签）
    private final AggregatedBlock.Value[] values;
    // 缓存：所有满足条件的方块状态列表
    @Nullable
    private BlockState[] blockStates;
    // 缓存：用于配方快速匹配的状态ID
    @Nullable
    private IntList stateIds;
    // 缓存版本号（用于判断是否需要刷新）
    private int invalidationCounter;

    /**
     * 构造方法
     * @param pValues 流形式的 Value（方块状态/标签）
     */
    protected AggregatedBlock(Stream<? extends AggregatedBlock.Value> pValues) {
        this.values = pValues.toArray((i) -> {
            return new AggregatedBlock.Value[i];
        });
    }
    /**
     * 获取所有满足该原料条件的方块状态
     * 会自动缓存，避免重复计算
     * 若输入{橡木原木，原木标签}，在遍历、去重后得到对应可以匹配的方块为{橡木原木、桦木原木…(原木标签对应的所有方块)…}
     */
    public BlockState[] getBlockStates() {
        if (this.blockStates == null) {
            this.blockStates = Arrays.stream(this.values).flatMap((value) -> {
                return value.getBlocks().stream();
            }).distinct().toArray((i) -> {
                return new BlockState[i];
            });
        }
        return this.blockStates;
    }
    /**
     * 【核心方法】
     * 判断物品栈是否符合该原料要求
     * 传入null=>返回false
     * 传入空原料=>匹配空气
     * 否则先判断方块状态对应的方块是否匹配该原料
     * 然后获取双方的方块状态，然后判断方块状态是否匹配
     */
    @Override
    public boolean test(@Nullable BlockState blockState) {
        if (blockState == null) {
            return false;
        } else if (this.isEmpty()) {
            return blockState.isAir();
        } else {
            for(BlockState state : this.getBlockStates()) {
                //先匹配方块，如果是相同方块，则进行属性匹配，否则没有必要再匹配属性，直接下一个
                if (state.is(blockState.getBlock())) {
                    //System.out.println("匹配方块：" + state);
                    //与默认状态相同的状态要被排除
                    //获取要排除的方块状态
                    Collection<Property<?>> propertiesExclude = AggregatedBlock.getDefaultOrNonProperties(state,false);
                    //System.out.println("需要匹配的属性：" + propertiesExclude);
                    //如果没有需要匹配的属性，因为已经匹配了方块，所以直接返回true
                    if (propertiesExclude.isEmpty()) return true;
                    //是否成功的标记
                    boolean isRight = true;
                    //否则继续匹配属性
                    //System.out.println("开始匹配属性");
                    for (Property<?> property : propertiesExclude) {
                        //获取需要的属性值
                        Comparable<?> value = state.getValue(property);
                        //System.out.println("需要的属性：" + property + " " + value);
                        //获取传入的属性值
                        Comparable<?> value1 = blockState.getValue(property);
                        //System.out.println("传入的属性：" + property + " " + value1);
                        //判断属性值是否相同
                        if (!value.equals(value1)) {
                            //属性值不同，没必要继续匹配属性，标记不成功，打断for直接下一个
                            isRight = false;
                            //System.out.println("属性匹配失败");
                            break;
                        }
                    }
                    if (isRight) return true;
                }
            }
            return false;
        }
    }
    /**
     * 比较当前 BlockState 和它方块的默认状态
     * 返回所有【值与默认不同】的 Property<?>
     */
    public static Collection<Property<?>> getDefaultOrNonProperties(BlockState currentState, boolean isDefault) {
        Collection<Property<?>> diffProperties = new ArrayList<>();

        // 获取该方块的默认状态
        Block defaultBlock = currentState.getBlock();
        BlockState defaultState = defaultBlock.defaultBlockState();

        // 获取当前状态所有 属性=值
        Map<Property<?>, Comparable<?>> currentValues = currentState.getValues();

        // 遍历所有属性
        for (Property<?> property : currentState.getProperties()) {
            // 当前值
            Comparable<?> currentValue = currentValues.get(property);
            // 默认值
            Comparable<?> defaultValue = defaultState.getValue(property);

            // 只要不相等 → 就是非默认属性
            if (currentValue != null ) {
                if (!isDefault && !currentValue.equals(defaultValue)) {
                    diffProperties.add(property);
                }
                if (isDefault && currentValue.equals(defaultValue)) {
                    diffProperties.add(property);
                }
            }
        }

        return diffProperties;
    }

    /**
     * 获取用于快速匹配的堆叠ID列表（用于合成界面快速检索）
     */
    public IntList getStackingIds() {
        if (this.stateIds == null || checkInvalidation()) {// 缓存判空/失效检查
            this.markValid();// 标记缓存为最新版本
            BlockState[] blockStates1 = this.getBlockStates();// 获取所有符合条件的方块状态
            this.stateIds = new IntArrayList(blockStates1.length);// 初始化ID列表（容量适配）

            for(BlockState state : blockStates1) {// 遍历所有方块状态，生成ID
                this.stateIds.add(getStateIndex(state));
            }

            this.stateIds.sort(IntComparators.NATURAL_COMPARATOR);// 对ID排序，方便后续二分查找
        }
        return this.stateIds;// 返回缓存的ID列表
    }
    public static int getStateIndex(BlockState state) {
        return BuiltInRegistries.BLOCK.getId(state.getBlock());
    }

    // 是否为空原料
    public boolean isEmpty() {
        return this.values.length == 0;
    }

    // 检查缓存是否已失效（全局版本号不一致则失效）
    public final boolean checkInvalidation() {
        int currentInvalidationCounter = INVALIDATION_COUNTER.get();
        if (this.invalidationCounter != currentInvalidationCounter) {
            invalidate();
            return true;
        }
        return false;
    }

    // 标记当前缓存为最新版本
    protected final void markValid() {
        this.invalidationCounter = INVALIDATION_COUNTER.get();
    }

    // 清空所有缓存
    protected void invalidate() {
        this.blockStates = null;
        this.stateIds = null;
    }

    public boolean isSimple() {
        return true;
    }

    private final boolean isVanilla = this.getClass() == AggregatedBlock.class;
    public final boolean isVanilla() {
        return isVanilla;
    }

    //合并多个原料(内部使用)
    public static AggregatedBlock fromValues(Stream<? extends AggregatedBlock.Value> pStream) {
        AggregatedBlock ingredient = new AggregatedBlock(pStream);
        return ingredient.isEmpty() ? EMPTY : ingredient;
    }

    // 空原料
    public static AggregatedBlock of() {
        return EMPTY;
    }
    // 从方块创建（自动用默认状态）
    public static AggregatedBlock of(Block... blocks) {
        return of(Arrays.stream(blocks).map(Block::defaultBlockState));
    }
    // 从方块状态创建（手动指定状态）
    public static AggregatedBlock of(BlockState... blockStates) {
        return of(Arrays.stream(blockStates));
    }

    /**
     * 处理 BlockState 流的最终实现，也是上述 Block[]/BlockState[] 重载方法的底层依赖。
     * 过滤：filter((blockState) -> !blockState.isAir()) —— 剔除所有空气方块（避免空气被当作有效原料）；
     * 转换：map(AggregatedBlock.BlockValue::new) —— 将每个有效 BlockState 封装为 BlockValue（AggregatedBlock 的内部值类型，对应「单个方块状态」）；
     * 封装：调用 fromValues 将 BlockValue 流转为 AggregatedBlock 实例。
     * @param stateStream
     * @return
     */
    public static AggregatedBlock of(Stream<BlockState> stateStream) {
        return fromValues(stateStream.filter((blockState) -> {
            return !blockState.isAir();
        }).map(AggregatedBlock.BlockValue::new));
    }
    // 从标签创建 AggregatedBlock 实例。
    public static AggregatedBlock of(TagKey<Block> pTag) {
        return fromValues(Stream.of(new AggregatedBlock.TagValue(pTag)));
    }

    /**
     * 【网络序列化】
     * 将原料写入网络数据包（发给客户端/服务端）
     */
    public void writeToNetwork(FriendlyByteBuf buf) {
        // 写入Value数量
        buf.writeVarInt(this.values.length);
        for (Value value : this.values) {
            // 标记当前Value类型：0=BlockState，1=Tag
            if (value instanceof BlockValue) {
                BlockState state = ((BlockValue) value).blockState;
                writeBlockStateToNetwork(buf, state);
            } else if (value instanceof TagValue) {
                buf.writeBoolean(true); // Tag类型
                TagKey<Block> tag = ((TagValue) value).tag;
                // 序列化Tag的ResourceLocation
                buf.writeResourceLocation(tag.location());
            }
        }
    }
    public static void writeBlockStateToNetwork(FriendlyByteBuf buf, BlockState state) {
        buf.writeBoolean(false); // 非Tag类型
        // 序列化BlockState（Minecraft内置方法）
        buf.writeRegistryId(ForgeRegistries.BLOCKS, state.getBlock());
        // 序列化BlockState的属性
        CompoundTag tag = new CompoundTag();
        // 手动把 ImmutableMap 转 CompoundTag
        for (Map.Entry<Property<?>, Comparable<?>> entry : state.getValues().entrySet()) {
            Property<?> prop = entry.getKey();
            Comparable<?> val = entry.getValue();
            String valueStr = getPropertyValue(prop, val);
            tag.putString(prop.getName(), valueStr);
        }
        buf.writeNbt(tag);
    }
    // 工具方法：安全获取属性的字符串值，解决泛型报错
    private static <T extends Comparable<T>> String getPropertyValue(Property<T> prop, Comparable<?> value) {
        return prop.getName((T) value);
    }
    /**
     * 【JSON 序列化】
     * 将当前原料转为 JSON 格式（用于导出配方文件）
     */
    public JsonElement toJson() {
        if (this.values.length == 1) {// 1. 判断内部存储的原料值数量是否为1
            return this.values[0].serialize();// 2. 若只有1个原料值：直接返回该值的序列化结果（单个JsonObject）
        } else {
            JsonArray jsonarray = new JsonArray();// 3. 若有多个原料值：创建JSON数组存储所有值的序列化结果
            for(AggregatedBlock.Value aggregatedBlock$value : this.values) {
                jsonarray.add(aggregatedBlock$value.serialize());// 4. 遍历所有原料值，逐个序列化并添加到数组中
            }
            return jsonarray;// 5. 返回JSON数组
        }
    }

    /**
     * 【网络反序列化】
     * 从网络数据包读取 AggregatedBlock
     */
    public static AggregatedBlock readFromNetwork(FriendlyByteBuf buf) {
        int valueCount = buf.readVarInt();
        Stream.Builder<Value> valueStream = Stream.builder();

        for (int i = 0; i < valueCount; i++) {
            boolean isTag = buf.readBoolean();
            if (!isTag) {
                BlockState state = readStateFromNetwork(buf);
                valueStream.add(new BlockValue(state));
            } else {
                // 读取Tag
                ResourceLocation tagId = buf.readResourceLocation();
                TagKey<Block> tag = TagKey.create(Registries.BLOCK, tagId);
                valueStream.add(new TagValue(tag));
            }
        }

        return fromValues(valueStream.build());
    }
    /**
     * 安全设置属性（解决泛型不匹配报错）
     */
    @SuppressWarnings("unchecked")
    public static <T extends Comparable<T>> BlockState setPropertyGeneric(BlockState state, Property<T> prop, String valueStr) {
        return prop.getValue(valueStr).map(v -> state.setValue(prop, v)).orElse(state);
    }
    // 从网络数据包读取 BlockState
    public static BlockState readStateFromNetwork(FriendlyByteBuf buf) {
        // 读取BlockState
        Block block = buf.readRegistryIdSafe(Block.class);

        // 读取 NBT 并恢复状态
        CompoundTag tag = buf.readNbt();
        BlockState state = block.defaultBlockState();

        if (tag != null) for (Property<?> prop : state.getProperties()) {
            if (tag.contains(prop.getName())) {
                state = setPropertyGeneric(state, prop, tag.getString(prop.getName()));
            }
        }
        return state;
    }
    /**
     * 【JSON 反序列化】
     * 从 JSON 读取 自身（配方文件读取核心）
     */
    public static AggregatedBlock fromJson(@Nullable JsonElement pJson) {
        return fromJson(pJson, true);
    }

    /**
     * 【JSON 反序列化】
     * 从 JSON 读取 配方文件
     */
    public static AggregatedBlock fromJson(@Nullable JsonElement pJson, boolean pCanBeEmpty) {
        if (pJson != null && !pJson.isJsonNull()) {
            //判断是否是对象(即用{大括号}包裹的结构)
            if (pJson.isJsonObject()) {
                //处理单个对象，调用valueFromJson（）生成Value然后将Value通过fromValues（）打包为AggregatedBlock
                return fromValues(Stream.of(valueFromJson(pJson.getAsJsonObject())));
            }
            //判断是否是数组(即用[中括号]包裹的结构)
            else if (pJson.isJsonArray()) {
                JsonArray jsonarray = pJson.getAsJsonArray();
                //空数组报错
                if (jsonarray.size() == 0 && !pCanBeEmpty) {
                    throw new JsonSyntaxException("Block array cannot be empty, at least one block must be defined");
                } else {
                    //批量处理对象，将数组转流之后对每个对象应用一次处理单个对象的过程
                    return fromValues(StreamSupport.stream(jsonarray.spliterator(), false).map((p_289756_) -> {
                        return valueFromJson(GsonHelper.convertToJsonObject(p_289756_, "block"));
                    }));
                }
            } else {
                throw new JsonSyntaxException("Expected block to be object or array of objects");
            }
        } else {
            throw new JsonSyntaxException("Block cannot be null");
        }
    }

    /**
     * 从 JSON 对象解析单个材料（方块状态 或 标签）
     * 现在支持：
     * { "block": { "Name": "minecraft:birch_leaves", "Properties": { ... } } }
     * 或
     * { "tag": "minecraft:logs" }
     */
    public static AggregatedBlock.Value valueFromJson(JsonObject pJson) {
        if (pJson.has("block") && pJson.has("tag")) {
            throw new JsonSyntaxException("An ingredient entry cannot have both block and tag!");
        }

        // --- 使用 BlockState.CODEC 解析 ---
        if (pJson.has("block")) {
            // 从 "block" 子节点解析
            BlockState state = BlockState.CODEC
                    .parse(JsonOps.INSTANCE, pJson.get("block"))  // 核心！官方解析
                    .getOrThrow(
                            false, // 不允许部分解析
                            errorMsg -> {
                                throw new JsonSyntaxException("fail to get block value from json: " + errorMsg);
                            }
                    );       // 错误抛异常

            // 防御性逻辑：不允许空气
            if (state.isAir()) {
                throw new JsonSyntaxException("Air block is not allowed in inputs of change block recipe!");
            }

            return new AggregatedBlock.BlockValue(state);
        }

        // --- Tag 逻辑不变 ---
        if (pJson.has("tag")) {
            String tagStr = GsonHelper.getAsString(pJson, "tag");
            ResourceLocation tagId = new ResourceLocation(tagStr);
            TagKey<Block> tag = TagKey.create(Registries.BLOCK, tagId);
            return new AggregatedBlock.TagValue(tag);
        }

        throw new JsonSyntaxException("Entry must have either 'block' or 'tag'");
    }

//    /**
//     * 从 JSON 对象解析单个材料（方块 或 标签）
//     */
//    public static AggregatedBlock.Value valueFromJson(JsonObject pJson) {
//        if (pJson.has("block") && pJson.has("tag")) {
//            throw new JsonParseException("An ingredient entry is either a tag or an block, not both");
//        } else if (pJson.has("block")) {
//            Block block = blockFromJson(pJson);
//            return new AggregatedBlock.BlockValue(block.defaultBlockState());
//        } else if (pJson.has("tag")) {
//            ResourceLocation resourcelocation = new ResourceLocation(GsonHelper.getAsString(pJson, "tag"));
//            TagKey<Block> tagkey = TagKey.create(Registries.BLOCK, resourcelocation);
//            return new AggregatedBlock.TagValue(tagkey);
//        } else {
//            throw new JsonParseException("An ingredient entry needs either a tag or an item");
//        }
//    }

    public static Block blockFromJson(JsonObject pItemObject) {
        String s = GsonHelper.getAsString(pItemObject, "block");
        Block block = BuiltInRegistries.BLOCK.getOptional(ResourceLocation.tryParse(s)).orElseThrow(() -> {
            return new JsonSyntaxException("Unknown block '" + s + "'");
        });
        if (block == Blocks.AIR) {
            throw new JsonSyntaxException("Empty ingredient not allowed here");
        } else {
            return block;
        }
    }
    /**
     * 将 BlockState 序列化为 Mojang 标准 JsonElement
     * 结构自动生成：{"Name":"xxx","Properties":{...}}
     */
    public static JsonElement blockStateToJson(BlockState state) {
        DataResult<JsonElement> result = BlockState.CODEC.encodeStart(JsonOps.INSTANCE, state);
        return result.getOrThrow(false, errMsg -> {
            throw new JsonSyntaxException("序列化方块状态失败：" + errMsg);
        });
    }

    // 合并多个原料（内部用）
    public static AggregatedBlock merge(Collection<AggregatedBlock> parts) {
        return fromValues(parts.stream().flatMap(i -> Arrays.stream(i.values)));
    }

    // ================================
    // 内部类：单个方块状态类型的值
    // ================================
    public static class BlockValue implements AggregatedBlock.Value {
        private final BlockState blockState;

        public BlockValue(BlockState pState) {
            this.blockState = pState;
        }

        @Override
        public Collection<BlockState> getBlocks() {
            return Collections.singleton(this.blockState);
        }
        @Override
        public JsonObject serialize() {
            JsonObject jsonobject = new JsonObject();
            // 把 BlockState 序列化为标准 {Name,Properties} 对象
            JsonElement stateJson = blockStateToJson(this.blockState);
            jsonobject.add("block", stateJson);
            return jsonobject;
        }
//        @Override
//        public JsonObject serialize() {
//            JsonObject json = new JsonObject();
//            // 序列化也可以换成 CODEC 编码，更标准
//            BlockState.CODEC
//                    .encodeStart(JsonOps.INSTANCE, blockState)
//                    .result()
//                    .ifPresent(jsonElem -> json.add("block", jsonElem));
//            return json;
//        }
//        public ResourceLocation getBlockNameSafe() {
//            ResourceLocation id = ForgeRegistries.BLOCKS.getKey(blockState.getBlock());
//            return id == null ? ForgeRegistries.BLOCKS.getKey(Blocks.AIR) : id;
//        }
    }
//    public static class BlockValue implements AggregatedBlock.Value {
//        private final BlockState blockState;
//
//        public BlockValue(BlockState pState) {
//            this.blockState = pState;
//        }
//
//        @Override
//        public Collection<BlockState> getBlocks() {
//            return Collections.singleton(this.blockState);
//        }
//
//        // 序列化为 {"block": "xxx"}
//        @Override
//        public JsonObject serialize() {
//            JsonObject jsonobject = new JsonObject();
//            jsonobject.addProperty("block", getBlockNameSafe().toString());
//            return jsonobject;
//        }
//        //安全获取方块ID，如果为空则返回空气方块，用以弥补ItemStack自动补充空气物品的逻辑
//        public ResourceLocation getBlockNameSafe() {
//            ResourceLocation location = ForgeRegistries.BLOCKS.getKey(this.blockState.getBlock());
//            return location == null ? ForgeRegistries.BLOCKS.getKey(Blocks.AIR) : location;
//        }
//    }

    // ================================
    // 内部类：标签类型的值
    // ================================
    public static class TagValue implements AggregatedBlock.Value {
        private final TagKey<Block> tag;

        public TagValue(TagKey<Block> pTag) {
            this.tag = pTag;
        }

        /**
         * 获取该标签下所有方块
         * 关键：从注册表读取，而不是存在 TagKey 里
         */
        @Override
        public Collection<BlockState> getBlocks() {
            List<BlockState> list = Lists.newArrayList();
            for(Holder<Block> holder : BuiltInRegistries.BLOCK.getTagOrEmpty(tag)) {
                list.add(holder.value().defaultBlockState());
            }
            if (list.size() == 0) {
                list.add(Blocks.BARRIER.defaultBlockState());
            }
            return list;
        }

        // 序列化为 {"tag": "xxx"}
        @Override
        public JsonObject serialize() {
            JsonObject jsonobject = new JsonObject();
            jsonobject.addProperty("tag", this.tag.location().toString());
            return jsonobject;
        }
    }

    // ================================
    // 基础接口：原料值
    // ================================
    public interface Value {
        Collection<BlockState> getBlocks();
        JsonObject serialize();
    }
}
