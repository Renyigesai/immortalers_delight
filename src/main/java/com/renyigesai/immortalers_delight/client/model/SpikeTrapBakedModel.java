package com.renyigesai.immortalers_delight.client.model;

import com.renyigesai.immortalers_delight.block.SpikeTrapBlock;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Tilt;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpikeTrapBakedModel implements BakedModel {
    //烘焙模型，用于存储默认情况游戏(json)帮我们生成的模型，也就是我们平常直接使用的模型
    BakedModel defaultModel;
    //ModelProperty可以用于在ModelData对象中存储一些属性，并可选地对这些属性进行判断和校验
    public static ModelProperty<BlockState> COPIED_BLOCK = new ModelProperty<>();

    public SpikeTrapBakedModel(BakedModel existingModel){
        this.defaultModel = existingModel;
    }
    //用于获取模型的面片，如果渲染类型为空，则返回所有的面片
    /**
     * 获得该模型本身的模型，之后判断modeldata中我们添加的字段是不是null，
     * 如果是null，那么我们直接返回默认的数据，
     * 如果不是null我们获得对应的该字段的blockstate，通过blockRendererDispatcher获得对应blockstate的模型数据，
     * 设置rendermoodel，然后返回该model的数据
     */
    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData data, @Nullable RenderType renderType) {
        BakedModel renderModel = defaultModel;
        if (data.has(COPIED_BLOCK)) {
            BlockState copiedBlock = data.get(COPIED_BLOCK);
            if (copiedBlock != null) {
                Minecraft mc = Minecraft.getInstance();
                BlockRenderDispatcher blockRendererDispatcher = mc.getBlockRenderer();
                renderModel = blockRendererDispatcher.getBlockModel(copiedBlock);
            }
        }
        return renderModel.getQuads(state,side,rand,data,renderType);
    }

    //用于获取模型数据.getQuads方法会用使用到ModelData这个参数，而这个参数传入的数据就是由此方法提供的
    /**
     * 首先获得当前方块的下面方块的blockstate，
     * 之后构建一个modeldata，复制了原modeldata的数据，并添加了COPIED_BLOCK这个ModelProperty,其数值为null，
     * 然后判断该方块下面方块是不是空气方块或则是我们的陷阱方块，
     * 如果是的话，就返回这个modeldata，其中的COPIED_BLOCK的字段是null。
     * 如果不是，我们就返回COPIED_BLOCK字段是下面方块的blockstate，
     * 之后我们通过这个blockstate获得对应方块状态的model
     */
    @Override
    public @NotNull ModelData getModelData(@NotNull BlockAndTintGetter level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData modelData) {
        BlockState downBlockState = level.getBlockState(pos.below());
        ModelData modelDataMap = modelData.derive().with(COPIED_BLOCK,null).build();

        if(downBlockState.getBlock() == Blocks.AIR
                || downBlockState.getBlock() instanceof SpikeTrapBlock){
            return modelDataMap;
        }

        return modelDataMap.derive().with(COPIED_BLOCK,downBlockState).build();
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState pState, @Nullable Direction pDirection, RandomSource pRandom) {
        throw new AssertionError("IBakedModel::getQuads should never be called, only IForgeBakedModel::getQuads");
    }

    //用于判断是否使用环境遮蔽
    @Override
    public boolean useAmbientOcclusion() {
        return defaultModel.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return defaultModel.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return defaultModel.usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return defaultModel.isCustomRenderer();
    }

    //用于获取粒子图标
    @Override
    public TextureAtlasSprite getParticleIcon() {
        return defaultModel.getParticleIcon();
    }

    @Override
    public ItemOverrides getOverrides() {
        return defaultModel.getOverrides();
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
    public static class ModEventBus{
        @SubscribeEvent
        public static void onModelBaked(ModelEvent.ModifyBakingResult event){
            for(BlockState blockstate: ImmortalersDelightBlocks.SPIKE_TRAP.get().getStateDefinition().getPossibleStates()){
                if (blockstate.hasProperty(BlockStateProperties.TILT) && blockstate.getValue(BlockStateProperties.TILT) == Tilt.NONE) {
                    ModelResourceLocation modelResourceLocation = BlockModelShaper.stateToModelLocation(blockstate);
                    BakedModel existingModel = event.getModels().get(modelResourceLocation);
                    if(existingModel==null){
                        throw new RuntimeException("Did not find Obsidian Hidden in registry");
                    }else if (existingModel instanceof SpikeTrapBakedModel) {
                        throw new RuntimeException("Tried to replaceObsidian Hidden twice");
                    }else {
                        SpikeTrapBakedModel obsidianHiddenBlockModel = new SpikeTrapBakedModel(existingModel);
                        event.getModels().put(modelResourceLocation, obsidianHiddenBlockModel);
                    }
                }

            }
        }
    }
}
