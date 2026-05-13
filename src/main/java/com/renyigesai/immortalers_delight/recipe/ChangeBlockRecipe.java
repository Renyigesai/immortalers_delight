package com.renyigesai.immortalers_delight.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChangeBlockRecipe implements Recipe<SimpleContainer> {

    private final NonNullList<AggregatedBlock> inputBlocks;
    private final NonNullList<String> propertiesNeedDefault;
    private final NonNullList<String> propertiesKeepDefault;
    private final NonNullList<BlockPos> blockPoss;
    private final String recipe_type;
    private final ResourceLocation id;
    private final BlockState target;
    public ChangeBlockRecipe(String recipe_type,
                             NonNullList<AggregatedBlock> ingredient,
                             NonNullList<String> propertiesNeedDefault,
                             NonNullList<String> propertiesKeepDefault,
                             NonNullList<BlockPos> blockPoss,
                             BlockState target,
                             ResourceLocation id) {
        this.inputBlocks = ingredient;
        this.recipe_type = recipe_type;
        this.propertiesNeedDefault = propertiesNeedDefault;
        this.propertiesKeepDefault = propertiesKeepDefault;
        this.blockPoss = blockPoss;
        this.id = id;
        if (target.isAir()){
            this.target = Blocks.AIR.defaultBlockState();
        }else {
            this.target = target;
        }
    }
    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        return false;
    }
    //方块匹配器
    public boolean matchBlocks(@NotNull NonNullList<BlockState> states, @NotNull Level pLevel) {
        System.out.println("matching blocks");
        return states.size() == this.inputBlocks.size()
                && net.minecraftforge.common.util.RecipeMatcher.findMatches(states, this.inputBlocks) != null;
    }
    @Override
    public ItemStack assemble(SimpleContainer pContainer, RegistryAccess pRegistryAccess) {
        return new ItemStack(this.target.getBlock().asItem());
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    public @NotNull NonNullList<BlockPos> getBlockPoss() {
        return this.blockPoss;
    }
    public @NotNull NonNullList<String> getPropertiesNeedDefault() {
        return this.propertiesNeedDefault;
    }
    public @NotNull NonNullList<String> getPropertiesKeepDefault() {
        return this.propertiesKeepDefault;
    }
    @Override
    public @NotNull ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return new ItemStack(this.target.getBlock().asItem());
    }
    public  @NotNull BlockState getResultBlock(RegistryAccess pRegistryAccess) {
        return this.target;
    }
    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {return Serializer.INSTANCE;}

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public @NotNull NonNullList<AggregatedBlock> getInputBlocks() {
        return this.inputBlocks;
    }
    public static class Type implements RecipeType<ChangeBlockRecipe> {
        public static final ChangeBlockRecipe.Type INSTANCE = new ChangeBlockRecipe.Type();
        public static final String ID = "change_block";
    }
    public static class Serializer implements RecipeSerializer<ChangeBlockRecipe>{
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation("immortalers_delight", "change_block");

        @Override
        public ChangeBlockRecipe fromJson(@NotNull ResourceLocation pRecipeId, @NotNull JsonObject pSerializedRecipe) {
            //获取配方类型
            String typeID = GsonHelper.getAsString(pSerializedRecipe, "change_type", "");
            //获取输入时要匹配的默认状态
            NonNullList<String> propertiesNeedDefult = NonNullList.create();
            if (GsonHelper.isValidNode(pSerializedRecipe, "properties_need_defult")) {
                JsonArray properties_need_defult = GsonHelper.getAsJsonArray(pSerializedRecipe, "properties_need_defult");
                for (int i = 0; i < properties_need_defult.size(); i++) {
                    propertiesNeedDefult.add(properties_need_defult.get(i).getAsString());
                }
            }

            //获取输出时要保留的默认状态
            NonNullList<String> propertiesKeepDefult = NonNullList.create();
            if (GsonHelper.isValidNode(pSerializedRecipe, "properties_keep_defult")) {
                JsonArray properties_keep_defult = GsonHelper.getAsJsonArray(pSerializedRecipe, "properties_keep_defult");
                for (int i = 0; i < properties_keep_defult.size(); i++) {
                    propertiesKeepDefult.add(properties_keep_defult.get(i).getAsString());
                }
            }

            //ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));
            //解析输出的方块状态
            BlockState output = GsonHelper.isValidNode(pSerializedRecipe, "output") ? BlockState.CODEC
                    .parse(JsonOps.INSTANCE, GsonHelper.getAsJsonObject(pSerializedRecipe, "output"))  // 核心！官方解析
                    .getOrThrow(
                            false, // 不允许部分解析
                            errorMsg -> {
                                throw new JsonSyntaxException("fail to get block value from json: " + errorMsg);
                            }
                    ) : Blocks.AIR.defaultBlockState();

            // 动态获取原料数量
            JsonArray needblocks = GsonHelper.getAsJsonArray(pSerializedRecipe, "needblocks");
            NonNullList<AggregatedBlock> inputs = NonNullList.create();

            if (needblocks.size() > 9){
                throw new JsonParseException("Too many needblocks for block change recipe! The max is 9");
            }else {
                for (int i = 0; i < needblocks.size(); i++) {
                    inputs.add(AggregatedBlock.fromJson(needblocks.get(i)));
                }
//                if (inputs.size() > 1) {
                    NonNullList<BlockPos> configuration = NonNullList.create();
//                    JsonArray configuration_json = GsonHelper.getAsJsonArray(pSerializedRecipe, "configuration");
//                }

                return new ChangeBlockRecipe(typeID, inputs, propertiesNeedDefult, propertiesKeepDefult, configuration, output, pRecipeId);
            }
        }

        @Override
        public @Nullable ChangeBlockRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            NonNullList<String> propertiesNeedDefault = ChangeBlockRecipe.readNonNullStringListWithLimit(pBuffer);
            int ingredientCount = pBuffer.readInt();
            NonNullList<AggregatedBlock> inputs = NonNullList.withSize(ingredientCount, AggregatedBlock.EMPTY);

            for (int i = 0; i < ingredientCount; i++) {
                inputs.set(i, AggregatedBlock.readFromNetwork(pBuffer));
            }
            BlockState output = AggregatedBlock.readStateFromNetwork(pBuffer);
            NonNullList<String> propertiesKeepDefault = ChangeBlockRecipe.readNonNullStringListWithLimit(pBuffer);
            NonNullList<BlockPos> configuration = NonNullList.create();
            return new ChangeBlockRecipe(pRecipeId.toString(), inputs, propertiesNeedDefault, propertiesKeepDefault, configuration, output, pRecipeId);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, ChangeBlockRecipe pRecipe) {
            ChangeBlockRecipe.writeNonNullStringListWithLimit(pBuffer, pRecipe.getPropertiesNeedDefault());
            pBuffer.writeInt(pRecipe.inputBlocks.size());

            for (AggregatedBlock ingredient : pRecipe.getInputBlocks()) {
                ingredient.writeToNetwork(pBuffer);
            }
            AggregatedBlock.writeBlockStateToNetwork(pBuffer, pRecipe.target);
            ChangeBlockRecipe.writeNonNullStringListWithLimit(pBuffer, pRecipe.getPropertiesKeepDefault());
        }
    }

    // 写入 NonNullList<String> 到 ByteBuf
    public static void writeNonNullStringList(FriendlyByteBuf buf, NonNullList<String> list) {
        // 第一步：写入列表长度（VarInt 格式，符合 Minecraft 网络规范）
        buf.writeCollection(list, FriendlyByteBuf::writeUtf);
        // 注：writeCollection 内部已封装“写长度 + 逐元素写”，无需额外处理长度
    }

    // 从 ByteBuf 读取 NonNullList<String>
    public static NonNullList<String> readNonNullStringList(FriendlyByteBuf buf) {
        // 第一步：读取列表长度（由 writeCollection 写入的 VarInt）
        // 第二步：初始化 NonNullList（默认值用空字符串，因为列表元素不可为 null）
        NonNullList<String> list = NonNullList.withSize(0, "");
        // 第三步：读取列表元素并填充
        buf.readList(reader -> {
            String str = reader.readUtf();
            list.add(str);
            return str;
        });
        return list;
    }

    // 进阶：带“最大字符串长度限制”的读写（防止恶意数据）
    private static final int MAX_STRING_LENGTH = FriendlyByteBuf.MAX_STRING_LENGTH;

    public static void writeNonNullStringListWithLimit(FriendlyByteBuf buf, NonNullList<String> list) {
        buf.writeCollection(list, (buf1, str) -> buf1.writeUtf(str, MAX_STRING_LENGTH));
    }

    public static NonNullList<String> readNonNullStringListWithLimit(FriendlyByteBuf buf) {
        NonNullList<String> list = NonNullList.withSize(0, "");
        buf.readList(reader -> {
            String str = reader.readUtf(MAX_STRING_LENGTH);
            list.add(str);
            return str;
        });
        return list;
    }
}
