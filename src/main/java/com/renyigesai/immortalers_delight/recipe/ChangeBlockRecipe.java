package com.renyigesai.immortalers_delight.recipe;

import com.google.gson.*;
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
import org.jetbrains.annotations.Nullable;

public class ChangeBlockRecipe implements Recipe<SimpleContainer> {
    private final NonNullList<AggregatedBlock> inputs; private final NonNullList<String> keepDefault; private final BlockState target; private final ResourceLocation id;
    public ChangeBlockRecipe(NonNullList<AggregatedBlock> inputs, NonNullList<String> keepDefault, BlockState target, ResourceLocation id){this.inputs=inputs;this.keepDefault=keepDefault;this.target=target;this.id=id;}
    public boolean matchBlocks(NonNullList<BlockState> states, Level level){ return states.size()==inputs.size() && net.minecraftforge.common.util.RecipeMatcher.findMatches(states, inputs)!=null; }
    public boolean matches(SimpleContainer c, Level l){return false;} public ItemStack assemble(SimpleContainer c,RegistryAccess a){return getResultItem(a);} public boolean canCraftInDimensions(int w,int h){return true;} public ItemStack getResultItem(RegistryAccess a){return new ItemStack(target.getBlock().asItem());} public ResourceLocation getId(){return id;} public RecipeSerializer<?> getSerializer(){return Serializer.INSTANCE;} public RecipeType<?> getType(){return Type.INSTANCE;}
    public NonNullList<AggregatedBlock> getInputBlocks(){return inputs;} public BlockState getResultBlock(RegistryAccess a){return target;} public NonNullList<String> getPropertiesKeepDefault(){return keepDefault;}
    public static class Type implements RecipeType<ChangeBlockRecipe>{public static final Type INSTANCE=new Type(); public static final String ID="change_block";}
    public static class Serializer implements RecipeSerializer<ChangeBlockRecipe>{public static final Serializer INSTANCE=new Serializer();
        public ChangeBlockRecipe fromJson(ResourceLocation id,JsonObject json){NonNullList<AggregatedBlock> in=NonNullList.create();JsonArray a=GsonHelper.getAsJsonArray(json,"needblocks");if(a.size()>9)throw new JsonParseException("Too many needblocks");for(JsonElement e:a)in.add(AggregatedBlock.fromJson(e));NonNullList<String> keep=readStrings(json,"properties_keep_defult");BlockState out=json.has("output")?BlockState.CODEC.parse(JsonOps.INSTANCE,json.get("output")).getOrThrow(false,e->{throw new JsonSyntaxException(e);}):Blocks.AIR.defaultBlockState();return new ChangeBlockRecipe(in,keep,out,id);}
        private NonNullList<String> readStrings(JsonObject j,String key){NonNullList<String> r=NonNullList.create();if(j.has(key))for(JsonElement e:j.getAsJsonArray(key))r.add(e.getAsString());return r;}
        @Nullable public ChangeBlockRecipe fromNetwork(ResourceLocation id,FriendlyByteBuf b){int n=b.readVarInt();NonNullList<AggregatedBlock> in=NonNullList.create();for(int i=0;i<n;i++)in.add(AggregatedBlock.readFromNetwork(b));BlockState out=AggregatedBlock.readStateFromNetwork(b);NonNullList<String> keep=NonNullList.create();b.readList(x->{keep.add(x.readUtf(32767));return keep.get(keep.size()-1);});return new ChangeBlockRecipe(in,keep,out,id);}
        public void toNetwork(FriendlyByteBuf b,ChangeBlockRecipe r){b.writeVarInt(r.inputs.size());for(AggregatedBlock i:r.inputs)i.writeToNetwork(b);AggregatedBlock.writeBlockStateToNetwork(b,r.target);b.writeCollection(r.keepDefault,FriendlyByteBuf::writeUtf);}
    }
}
