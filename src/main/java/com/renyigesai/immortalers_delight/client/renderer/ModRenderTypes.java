package com.renyigesai.immortalers_delight.client.renderer;


import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public class ModRenderTypes extends RenderStateShard {

    // 必须继承 RenderStateShard 才能访问受保护的着色器和状态常量
    private ModRenderTypes(String name, Runnable setup, Runnable clear) {
        super(name, setup, clear);
    }
//    private static final Function<ResourceLocation, RenderType> EYES = Util.memoize((p_286170_) -> {
//        RenderStateShard.TextureStateShard renderstateshard$texturestateshard = new RenderStateShard.TextureStateShard(p_286170_, false, false);
//        return create("eyes", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder()
//                .setShaderState(RENDERTYPE_EYES_SHADER)
//                .setTextureState(renderstateshard$texturestateshard)
//                .setTransparencyState(ADDITIVE_TRANSPARENCY)
//                .setWriteMaskState(COLOR_WRITE)
//                .createCompositeState(false));
//    });
    /**
     * 自定义暗色发光/能量体通道：
     * 1. 采用 TRANSLUCENT_TRANSPARENCY（常规 Alpha 混合），完整保留深色、暗部灰度和半透明过渡；
     * 2. 使用 RENDERTYPE_ENTITY_TRANSLUCENT_EMISSIVE_SHADER，剔除漫反射明暗计算；
     * 3. 启用 COLOR_WRITE，写入深度缓冲防止内层乱序穿透；
     * 4. 关闭 NO_CULL，允许双面渲染展示能量内壁。
     */
    public static final Function<ResourceLocation, RenderType> UNLIT_TRANSLUCENT = Util.memoize((texture) -> {
        RenderType.CompositeState state = RenderType.CompositeState.builder()
                .setShaderState(RENDERTYPE_EYES_SHADER) // 自发光无漫反射着色器
                .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
                .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)               // 常规 Alpha 混合，不丢失灰度
                .setCullState(CULL)                                        // 禁用背面剔除（能量体双面可见）
                .setWriteMaskState(COLOR_WRITE)                               // 仅写颜色或 COLOR_DEPTH_WRITE
                .setOverlayState(NO_OVERLAY)
                .createCompositeState(false);

        return RenderType.create(
                "unlit_translucent",
                DefaultVertexFormat.NEW_ENTITY,
                VertexFormat.Mode.QUADS,
                256,
                true,
                true,
                state
        );
    });
}