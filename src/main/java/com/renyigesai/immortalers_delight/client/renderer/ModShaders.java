package com.renyigesai.immortalers_delight.client.renderer;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;

@Mod.EventBusSubscriber(modid = "immortalers_delight", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModShaders {

    private static ShaderInstance unlitBlockShader;

    @SubscribeEvent
    public static void registerShaders(RegisterShadersEvent event) throws IOException {
        event.registerShader(
                new ShaderInstance(
                        event.getResourceProvider(),
                        new ResourceLocation("immortalers_delight", "rendertype_unlit_block"),
                        DefaultVertexFormat.BLOCK // 核心：使用 BLOCK 格式匹配物品/方块烘焙网格
                ),
                shader -> unlitBlockShader = shader
        );
    }

    public static ShaderInstance getUnlitBlockShader() {
        return unlitBlockShader;
    }
}
