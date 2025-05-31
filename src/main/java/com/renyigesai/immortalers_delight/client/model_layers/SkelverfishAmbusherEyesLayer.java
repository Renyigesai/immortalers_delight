package com.renyigesai.immortalers_delight.client.model_layers;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.client.model.SilverfishModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SkelverfishAmbusherEyesLayer<T extends Entity, M extends SilverfishModel<T>> extends EyesLayer<T, M> {

    private static final RenderType AMBUSHER_EYES = RenderType.eyes(new ResourceLocation(ImmortalersDelightMod.MODID, "textures/entity/skelverfish_ambusher_eyes.png"));

    public SkelverfishAmbusherEyesLayer(RenderLayerParent<T, M> pRenderer) {
        super(pRenderer);
    }

    @Override
    public RenderType renderType() {
        return AMBUSHER_EYES;
    }
}
