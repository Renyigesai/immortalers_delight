package com.renyigesai.immortalers_delight.client.model_layers;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.client.model.SkelverfishBomberModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SkelverfishBomberCoreLayer<T extends Entity, M extends SkelverfishBomberModel<T>> extends EyesLayer<T, M> {

    private static final RenderType BOMBER_CORE = RenderType.eyes(new ResourceLocation(ImmortalersDelightMod.MODID, "textures/entity/skelverfish_bomber_core.png"));

    public SkelverfishBomberCoreLayer(RenderLayerParent<T, M> pRenderer) {
        super(pRenderer);
    }

    @Override
    public RenderType renderType() {
        return BOMBER_CORE;
    }
}
