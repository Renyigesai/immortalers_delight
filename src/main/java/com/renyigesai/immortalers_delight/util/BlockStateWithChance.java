package com.renyigesai.immortalers_delight.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockState;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.Configuration;

/**
 * 工具类，用于记录并序列化/反序列化一个带概率的方块状态
 */
public class BlockStateWithChance {
    //空实例
//    public static final BlockStateWithChance EMPTY;
//    public static final String BLOCK_RECODE = "block";
//    public static final String BLOCK_STATE_PROPERTY_TYPE = "state_property";
//    public static final String BLOCK_STATE_VALUE_BOOLEAN = "boolean_value";
//    public static final String BLOCK_STATE_VALUE_INTEGER = "int_value";
//    public static final String BLOCK_STATE_VALUE_OTHER = "enum_value";
//    private final BlockState state;
//    public BlockState getState() {return this.state;}
//    private final float chance;
//    public float getChance() {return this.chance;}
//
//    public BlockStateWithChance(BlockState blockState, float chance) {
//        this.state = blockState;
//        this.chance = chance;
//    }
//
//    /**
//     * 序列化
//     * @param json
//     * @return
//     */
//    public JsonElement serialize() {
//        JsonObject json = new JsonObject();
//        ResourceLocation resourceLocation = BuiltInRegistries.BLOCK.getKey(this.state.getBlock());
//        json.addProperty(BLOCK_RECODE, resourceLocation.toString());
//
//        this.state.getProperties().forEach(property -> {
//            json.addProperty(property.getName(), this.state.getValue(property).toString());
//        });
//        this.state.getValues().forEach((property, value) -> {
//            JsonObject json1 = new JsonObject();
//            if (value instanceof Boolean) {
//                json1.addProperty(BLOCK_STATE_PROPERTY_TYPE, BLOCK_STATE_VALUE_BOOLEAN);
//                json1.addProperty(BLOCK_STATE_VALUE_BOOLEAN, (Boolean) value);
//            } else if (value instanceof Integer) {
//                json1.addProperty(BLOCK_STATE_PROPERTY_TYPE, BLOCK_STATE_VALUE_INTEGER);
//            }
//        });
//        int count = this.stack.getCount();
//        if (count != 1) {
//            json.addProperty("count", count);
//        }
//
//        if (this.stack.hasTag()) {
//            json.add("nbt", (new JsonParser()).parse(this.stack.getTag().toString()));
//        }
//
//        if (this.chance != 1.0F) {
//            json.addProperty("chance", this.chance);
//        }
//
//        return json;
//    }
}
